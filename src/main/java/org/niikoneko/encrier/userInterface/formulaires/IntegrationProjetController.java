package org.niikoneko.encrier.userInterface.formulaires;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.niikoneko.encrier.data.DataConnector;
import org.niikoneko.encrier.jpa.ProjetMots;
import org.niikoneko.encrier.jpa.StageProjet;
import org.niikoneko.encrier.utils.NumberFormatter;
import org.tinylog.Logger;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class IntegrationProjetController {

    private static StageProjet currentProjetStage;

    @FXML
    private Label titre;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private TextField nombreMots;
    @FXML
    private TextField joursPasses;
    @FXML
    private TextField heuresPassees;
    @FXML
    private TextField minutesPassees;
    @FXML
    private TextField moyenneVitesse;
    @FXML
    private TextField nombreSessions;
    @FXML
    private CheckBox aleatoire;
    @FXML
    private TextField randcoef;
    @FXML
    private Button annuler;
    @FXML
    private Label errorLabel;


    public void initialize() {
        titre.setText("Intégration du projet " + currentProjetStage.getProjet().getNom());
        nombreMots.setTextFormatter(new NumberFormatter());
        joursPasses.setTextFormatter(new NumberFormatter());
        heuresPassees.setTextFormatter(new NumberFormatter());
        minutesPassees.setTextFormatter(new NumberFormatter());
        moyenneVitesse.setTextFormatter(new NumberFormatter());
        nombreSessions.setTextFormatter(new NumberFormatter());
        randcoef.setTextFormatter(new NumberFormatter());
        randcoef.setText("20");
        randcoef.setEditable(false);
        randcoef.setCache(true);
        startDate.setDayCellFactory(d -> new DateCell() {
            @Override public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(LocalDate.now()));
            }
        });
        endDate.setDayCellFactory(d -> new DateCell() {
            @Override public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(LocalDate.now()));
            }
        });

    }

    public void onIntegrerClick() {
        // Verification préalable
        // Si les champs obligatoires ne sont pas remplis
        if (startDate.getValue() == null || nombreMots.getText().isEmpty() ||
                (joursPasses.getText().isEmpty() && heuresPassees.getText().isEmpty()
                && minutesPassees.getText().isEmpty() && moyenneVitesse.getText().isEmpty())) {
            errorLabel.setText("Vous devez renseigner une date de début et un nombre de mots, " +
                    "puis un temps passé ou une vitesse moyenne d'écriture.");
            return;
        }
        if (endDate.getValue() != null && startDate.getValue().isAfter(endDate.getValue())) {
            errorLabel.setText("La date de début doit être antérieure à la date de fin.");
            return;
        }
        if (Integer.parseInt(randcoef.getText()) <= 0 || Integer.parseInt(randcoef.getText()) >= 100 ) {
            errorLabel.setText("Le coefficient d'aléatoire doit être compris entre 0 et 100.");
            return;
        }
        Duration tempsEcriture = calculateDuration();
        if (tempsEcriture == null) return; // Si le calcul de durée n'a pas abouti (champs concurrents)
        DataConnector bddHandler = new DataConnector();
        String errorMessage;
        LocalDate dateLastSession;
        if (endDate.getValue() == null) dateLastSession = LocalDate.now();
        else dateLastSession = endDate.getValue();
        // Si pas de nombre de sessions, on en crée deux
        if (nombreSessions.getText().isEmpty()) {
            // Création d'une session à 0 à la date de début
            ProjetMots startSession = new ProjetMots(currentProjetStage, startDate.getValue(),
                    0, Duration.of(0, ChronoUnit.MINUTES));
            errorMessage = bddHandler.createProjetMots(startSession);
            if (!errorMessage.isEmpty()) {
                errorLabel.setText(errorMessage);
                return;
            }
            // Création d'une session finale à la date de fin (ou date du jour)
            ProjetMots newSession = new ProjetMots(currentProjetStage, dateLastSession,
                    Integer.parseInt(nombreMots.getText()), tempsEcriture);
            errorMessage = bddHandler.createProjetMots(newSession);
            if (!errorMessage.isEmpty()) {
                errorLabel.setText(errorMessage);
                return;
            }
        } else {
            LocalDate dateFirstSession = startDate.getValue();
            long nbSessions = Integer.parseInt(nombreSessions.getText());
            long nbMots = Integer.parseInt(nombreMots.getText());
            long nbJours = dateLastSession.toEpochDay() - dateFirstSession.toEpochDay();
            long joursEntreSessions;
            long motsParSession;
            Duration tempsParSession;
            if (nbSessions >= nbJours) {
                joursEntreSessions = 1;
                motsParSession = nbMots / nbJours;
                tempsParSession = tempsEcriture.dividedBy(nbJours);
                nbSessions = nbJours;
            } else {
                joursEntreSessions = nbJours / (nbSessions - 1);
                motsParSession = nbMots / nbSessions;
                tempsParSession = tempsEcriture.dividedBy(nbSessions);
            }
            ProjetMots tempSession;
            Duration dureeSession;
            double randcoef = 0;
            if (aleatoire.isSelected()) randcoef = 0.2;
            double alea;
            for (int i = 0; i < nbSessions - 1; i++) {
                alea = 1 + Math.random() * 2 * randcoef - randcoef;
                dureeSession = Duration.ofSeconds(Math.round(tempsParSession.getSeconds() * alea));
                tempSession = new ProjetMots(currentProjetStage,
                        dateFirstSession.plusDays((long) i * joursEntreSessions),
                        Math.round(motsParSession * alea), dureeSession);
                errorMessage = bddHandler.createProjetMots(tempSession);
                if (!errorMessage.isEmpty()) {
                    errorLabel.setText(errorMessage);
                    return;
                }
            }
            tempSession = new ProjetMots(currentProjetStage,
                    dateLastSession,
                    nbMots - bddHandler.getNombreMotsFromProjet(currentProjetStage.getProjet()),
                    tempsEcriture.minus(bddHandler.getTempsFromProjet(currentProjetStage.getProjet())));
            errorMessage = bddHandler.createProjetMots(tempSession);
            if (!errorMessage.isEmpty()) {
                errorLabel.setText(errorMessage);
                return;
            }
        }
        Stage current = (Stage) annuler.getScene().getWindow();
        current.close();
    }

    private Duration calculateDuration() {
        if (joursPasses.getText().isEmpty() && heuresPassees.getText().isEmpty() &&
                minutesPassees.getText().isEmpty() && !moyenneVitesse.getText().isEmpty()) {
            return Duration.ofMinutes(Integer.parseInt(nombreMots.getText()) /
                    Integer.parseInt(moyenneVitesse.getText()));
        } else if ((!joursPasses.getText().isEmpty() || !heuresPassees.getText().isEmpty() ||
                !minutesPassees.getText().isEmpty()) && moyenneVitesse.getText().isEmpty()) {
            int jours;
            int heures;
            int minutes;
            if (joursPasses.getText().isEmpty()) jours = 0;
            else jours = Integer.parseInt(joursPasses.getText());
            if (heuresPassees.getText().isEmpty()) heures = 0;
            else heures = Integer.parseInt(heuresPassees.getText());
            if (minutesPassees.getText().isEmpty()) minutes = 0;
            else minutes = Integer.parseInt(minutesPassees.getText());
            Duration tempsTotal = Duration.ofDays(jours);
            tempsTotal = tempsTotal.plus(Duration.ofHours(heures));
            tempsTotal = tempsTotal.plus(Duration.ofMinutes(minutes));
            return tempsTotal;
        } else {
            errorLabel.setText("Vous devez entrer soit un temps soit une moyenne, pas les deux.");
            return null;
        }
    }

    public void onAleaClick() {
        if (aleatoire.isSelected()) {
            randcoef.setCache(false);
            randcoef.setEditable(true);
        } else {
            randcoef.setEditable(false);
            randcoef.setCache(true);
        }
    }

    public void onAnnulerClick() {
        Stage current = (Stage) annuler.getScene().getWindow();
        Logger.debug("Intégration du projet {} annulée", currentProjetStage.getProjet().getNom());
        current.close();
    }

    public static void loadProjetStage(StageProjet curStage) {
        currentProjetStage = curStage;
    }
}

package org.niikoneko.encrier.formulaires;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.niikoneko.encrier.data.DataConnector;
import org.niikoneko.encrier.jpa.Projet;
import org.niikoneko.encrier.jpa.ProjetMots;
import org.niikoneko.encrier.utils.NumberFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class IntegrationProjetController {

    private static final Logger logger = LoggerFactory.getLogger(NouveauProjetController.class);

    private static Projet currentProjet;

    @FXML
    private Label titre;
    @FXML
    private DatePicker startDate;
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
    private Button annuler;
    @FXML
    private Label errorLabel;


    public void initialize() {
        titre.setText("Intégration du projet " + currentProjet.getNom());
        nombreMots.setTextFormatter(new NumberFormatter());
        joursPasses.setTextFormatter(new NumberFormatter());
        heuresPassees.setTextFormatter(new NumberFormatter());
        minutesPassees.setTextFormatter(new NumberFormatter());
        moyenneVitesse.setTextFormatter(new NumberFormatter());
    }

    public void onIntegrerClick() {
        // Verification préalable
        if (startDate.getValue() == null || nombreMots.getText().isEmpty() ||
                (joursPasses.getText().isEmpty() && heuresPassees.getText().isEmpty()
                        && minutesPassees.getText().isEmpty() && moyenneVitesse.getText().isEmpty())) {
            errorLabel.setText("Vous devez renseigner une date de début et un nombre de mots, " +
                    "puis un temps passé ou une vitesse moyenne d'écriture.");
            return;
        }
        DataConnector bddHandler = new DataConnector();
        String errorMessage;
        // Création d'une session à 0 à la date de début
        ProjetMots startSession = new ProjetMots(currentProjet, startDate.getValue(),
                0, Duration.of(0, ChronoUnit.MINUTES));
        errorMessage = bddHandler.createProjetMots(startSession);
        if (!errorMessage.isEmpty()) {
            errorLabel.setText(errorMessage);
            return;
        }
        Duration tempsEcriture = calculateDuration();
        ProjetMots newSession = new ProjetMots(currentProjet, LocalDate.now(),
                Integer.parseInt(nombreMots.getText()), tempsEcriture);
        errorMessage = bddHandler.createProjetMots(newSession);
        if (!errorMessage.isEmpty()) {
            errorLabel.setText(errorMessage);
            return;
        }
        Stage current = (Stage) annuler.getScene().getWindow();
        current.close();
    }

    private Duration calculateDuration() {
        if (joursPasses.getText().isEmpty() && heuresPassees.getText().isEmpty() &&
                minutesPassees.getText().isEmpty()) {
            return Duration.ofMinutes(Integer.parseInt(nombreMots.getText()) /
                    Integer.parseInt(moyenneVitesse.getText()));
        } else {
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
        }
    }

    public void onAnnulerClick() {
        Stage current = (Stage) annuler.getScene().getWindow();
        logger.debug("Intégration du projet {} annulée", currentProjet.getNom());
        current.close();
    }

    public static void loadProjet(Projet aIntegrer) {
        currentProjet = aIntegrer;
    }
}

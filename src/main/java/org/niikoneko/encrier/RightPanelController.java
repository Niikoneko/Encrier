package org.niikoneko.encrier;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.niikoneko.encrier.data.DataConnector;
import org.niikoneko.encrier.jpa.Projet;
import org.niikoneko.encrier.jpa.ProjetMots;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.UnaryOperator;

public class RightPanelController {

    private static RightPanelController instance;

    private final static Logger logger = LoggerFactory.getLogger(RightPanelController.class);

    private final DataConnector bddHandler = new DataConnector();

    private Projet currentProjet;

    @FXML
    private Pane cachePane;
    @FXML
    private DatePicker dateSession;
    @FXML
    private TextField nombreMots;
    @FXML
    private TextField heuresSession;
    @FXML
    private TextField minutesSession;
    @FXML
    private Label errorLabel;

    public void initialize() {
        instance = this;
        dateSession.setDayCellFactory(d -> new DateCell() {
            @Override public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(LocalDate.now()));
            }
        });
        UnaryOperator<TextFormatter.Change> numberFilter = new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                change.setText(change.getText().replaceAll("[^\\d]", ""));
                return change;
            }
        };
        nombreMots.setTextFormatter(new TextFormatter<>(numberFilter));
        heuresSession.setTextFormatter(new TextFormatter<>(numberFilter));
        minutesSession.setTextFormatter(new TextFormatter<>(numberFilter));
        cachePane.setVisible(true);
    }

    public void onEnregistrerSessionClick() {
        // Verification préalable
        if (dateSession.getValue() == null || nombreMots.getText().isEmpty()
            || minutesSession.getText().isEmpty()) {
            errorLabel.setText("Vous devez renseigner une date, \nun nombre de mots et un temps de \nsession.");
            return;
        }
        if (heuresSession.getText().isEmpty()) heuresSession.setText("0");
        if (currentProjet == null) {
            errorLabel.setText("Erreur : Aucun projet sélectionné, \nle bouton ne devrait pas être \naccessible.");
            logger.error("Clic sur enregistrer sans projet sélectionné.");
            return;
        }
        LocalTime tempsSaisi = LocalTime.of(Integer.parseInt(heuresSession.getText()),
                Integer.parseInt(minutesSession.getText()));
        ProjetMots session = new ProjetMots(currentProjet, dateSession.getValue(),
                Integer.parseInt(nombreMots.getText()), tempsSaisi);
        //TODO Enregistrer ce bel objet en base
        errorLabel.setText("Pas encore implémenté. \nEt oui, c'est triste...");
    }

    /**
     * Prend en compte le projet sélectionné et lève le cache
     * @param projet le projet sélectionné
     */
    public void setCurrentProjet(Projet projet) {
        this.currentProjet = projet;
        this.cachePane.setVisible(false);
    }

    /**
     * Récupère l'instance initialisée
     * @return l'instance actuellement utilisée
     */
    public static RightPanelController getInstance() {
        return instance;
    }
}

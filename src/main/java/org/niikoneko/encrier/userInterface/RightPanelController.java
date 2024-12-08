package org.niikoneko.encrier.userInterface;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.niikoneko.encrier.data.DataConnector;
import org.niikoneko.encrier.jpa.ProjetMots;
import org.niikoneko.encrier.jpa.StageProjet;
import org.niikoneko.encrier.utils.NumberFormatter;
import org.tinylog.Logger;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Controller du bandeau droit : actions sur projet sélectionné
 */
public class RightPanelController {

    private static RightPanelController instance;

    private final DataConnector bddHandler = new DataConnector();

    private StageProjet currentProjetStage;

    private MainController mainController;

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
        nombreMots.setTextFormatter(new NumberFormatter());
        heuresSession.setTextFormatter(new NumberFormatter());
        minutesSession.setTextFormatter(new NumberFormatter());
        cachePane.setVisible(true);
    }

    public void onEnregistrerSessionClick() {
        Logger.debug("Enregistrement d'une nouvelle session d'écriture pour le projet {} à l'étape {}.",
                currentProjetStage.getProjet(), currentProjetStage);
        // Verification préalable
        if (dateSession.getValue() == null || nombreMots.getText().isEmpty()
            || minutesSession.getText().isEmpty() && heuresSession.getText().isEmpty()) {
            errorLabel.setText("Vous devez renseigner une date, \nun nombre de mots et un temps de \nsession.");
            return;
        }
        if (heuresSession.getText().isEmpty()) heuresSession.setText("0");
        if (minutesSession.getText().isEmpty()) minutesSession.setText("0");
        if (currentProjetStage == null) {
            errorLabel.setText("Erreur : Aucun projet sélectionné, \nle bouton ne devrait pas être \naccessible.");
            Logger.error("Clic sur enregistrer sans projet sélectionné.");
            return;
        }
        Duration tempsSaisi = Duration.of(Integer.parseInt(heuresSession.getText()), ChronoUnit.HOURS);
        tempsSaisi = tempsSaisi.plus(Duration.of(Integer.parseInt(minutesSession.getText()), ChronoUnit.MINUTES));
        ProjetMots session = new ProjetMots(currentProjetStage, dateSession.getValue(),
                Integer.parseInt(nombreMots.getText()), tempsSaisi);
        String erreur = bddHandler.createProjetMots(session);
        if (erreur.isEmpty())
            mainController.updateCentralView();
        else errorLabel.setText(erreur);
    }

    public void onSupprimerProjetClick() {
        Alert message = new Alert(Alert.AlertType.CONFIRMATION);
        message.setContentText("Êtes-vous sûr de vouloir supprimer le projet \"" +
                currentProjetStage.getProjet().getNom() + "\" ?");
        message.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                String ret = bddHandler.deleteProjet(currentProjetStage.getProjet());
                if (ret.isEmpty())
                    mainController.initialize();
                else
                    errorLabel.setText(ret);
            }
        });
    }

    /**
     * Prend en compte l'étape projet sélectionnée et lève le cache ou réinitialise si null
     * @param stage l'étape projet sélectionnée
     */
    public void setCurrentProjetStage(StageProjet stage) {
        if (stage == null) {
            this.currentProjetStage = null;
            this.cachePane.setVisible(true);
        } else {
            this.currentProjetStage = stage;
            this.cachePane.setVisible(false);
        }
    }

    /**
     * Récupère l'instance initialisée
     * @return l'instance actuellement utilisée
     */
    public static RightPanelController getInstance() {
        return instance;
    }

    public void declareMainController(MainController main) {
        this.mainController = main;
    }
}

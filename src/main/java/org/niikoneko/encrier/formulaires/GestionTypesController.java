package org.niikoneko.encrier.formulaires;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.niikoneko.encrier.MainController;
import org.niikoneko.encrier.data.DataConnector;
import org.niikoneko.encrier.jpa.Projet;
import org.niikoneko.encrier.jpa.TypeProjet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GestionTypesController {

    private static final Logger logger = LoggerFactory.getLogger(GestionTypesController.class);

    private final DataConnector bddHandler = new DataConnector();
    public static MainController controller;

    // Elements interactifs
    @FXML
    private Label errorLabel;
    @FXML
    private ListView<TypeProjet> typesViewList;
    @FXML
    private TextField nomType;
    @FXML
    private TextArea descriptionType;
    @FXML
    private Button ajouter;
    @FXML
    private Button enregistrer;
    @FXML
    private Button supprimer;
    @FXML
    private Button quitter;

    private TypeProjet selected;

    /**
     * Initialisation de la vue
     */
    public void initialize() {
        typesViewList.setItems(FXCollections.observableArrayList(bddHandler.getAllTypesProjets()));
        errorLabel.setText("");
        nomType.setText("");
        descriptionType.setText("");
        selected = new TypeProjet("", "");
    }

    @FXML
    public void onItemListClick() {
        errorLabel.setText("");
        selected = typesViewList.getSelectionModel().getSelectedItem();
        nomType.setText(selected.getNom());
        descriptionType.setText(selected.getDescription());
    }

    @FXML
    public void onAjouterClick() {
        errorLabel.setText("");
        selected = new TypeProjet("", "");
        nomType.setText("");
        descriptionType.setText("");
    }

    @FXML
    public void onEnregistrerClick() {
        // Verification préalable
        if (nomType.getText().isEmpty() || descriptionType.getText().isEmpty()) {
            errorLabel.setText("Vous devez renseigner un nom de type et une description.");
            return;
        }
        // Enregistrement du type
        selected.setNom(nomType.getText()).setDescription(descriptionType.getText());
        String error = bddHandler.createOrUpdateTypeProjet(selected);
        if (!error.isEmpty())
            errorLabel.setText(error);
        else
            initialize();
    }

    @FXML
    public void onSupprimerClick() {
        List<Projet> projetsType = bddHandler.getAllProjetsFromType(selected);
        if (!projetsType.isEmpty()) {
            Alert message = new Alert(Alert.AlertType.ERROR);
            message.setContentText("Vous ne pouvez pas supprimer ce type, il assigné à " + projetsType.size() + " projets.");
            message.showAndWait();
        } else {
            Alert message = new Alert(Alert.AlertType.CONFIRMATION);
            message.setContentText("Êtes-vous sûr de vouloir supprimer ce type de projet ?");
            message.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    logger.debug("Suppression du type de projet {}", selected);
                    bddHandler.deleteTypeProjet(selected);
                }
            });
        }
        initialize();
    }

    @FXML
    public void onQuitterClick() {
        Stage current = (Stage) quitter.getScene().getWindow();
        logger.debug("Fermeture de la fenêtre d'édition des types de projet");
        current.close();
    }
}

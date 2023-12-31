package org.niikoneko.encrier.formulaires;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.niikoneko.encrier.userInterface.MainController;
import org.niikoneko.encrier.data.DataConnector;
import org.niikoneko.encrier.jpa.Projet;
import org.niikoneko.encrier.jpa.TypeProjet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller pour le formulaire de nouveau projet
 * @author Niikoneko
 * @since 2023/01
 * @version 1.1
 */
public class NouveauProjetController {

    private static final Logger logger = LoggerFactory.getLogger(NouveauProjetController.class);

    private static final List<TypeProjet> typesList = new ArrayList<>();
    public static MainController controller;

    @FXML
    private ChoiceBox<TypeProjet> typeProjet;
    @FXML
    private TextField nomProjet;
    @FXML
    private TextArea descriptionProjet;
    @FXML
    private CheckBox triggerIntegration;
    @FXML
    private Button annuler;
    @FXML
    Label errorLabel;

    /**
     * Initialisation de la vue
     */
    public void initialize() {
        setChoices();
    }

    @FXML
    protected void onCreerClick() throws IOException {
        // Verification préalable
        if (nomProjet.getText().isEmpty() || typeProjet.getValue() == null) {
            errorLabel.setText("Vous devez renseigner un type et un nom de projet.");
            return;
        }
        // Création du projet
        Projet newProjet = new Projet(typeProjet.getValue(), nomProjet.getText(), descriptionProjet.getText());
        DataConnector bddHandler = new DataConnector();
        logger.debug("Création du projet " + newProjet.getNom());
        String error = bddHandler.createOrUpdateProjet(newProjet);
        if (error.isEmpty()) {
            Stage current = (Stage) annuler.getScene().getWindow();
            controller.initialize();
            if (triggerIntegration.isSelected()) {
                controller.launchIntegration(bddHandler. getProjetFromNom(newProjet.getNom()));
            }
            current.close();
        } else {
            errorLabel.setText("Erreur de création du projet : " + error);
        }
    }

    @FXML
    protected void onAnnulerClick() {
        Stage current = (Stage) annuler.getScene().getWindow();
        logger.debug("Création de nouveau projet annulée");
        current.close();
    }

    /**
     * Précharge les types de projet disponibles lors de l'appel par le MainController
     * @param typesListToAdd Les types de projet
     */
    public static void loadChoices(List<TypeProjet> typesListToAdd) {
        typesList.clear();
        typesList.addAll(typesListToAdd);
    }

    /**
     * Assigne les types lors de l'initialisation
     */
    private void setChoices() {
        typeProjet.setItems(FXCollections.observableArrayList(typesList));
    }
}

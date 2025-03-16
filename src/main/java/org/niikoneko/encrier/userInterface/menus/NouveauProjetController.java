package org.niikoneko.encrier.userInterface.menus;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.niikoneko.encrier.jpa.Stage;
import org.niikoneko.encrier.jpa.StageProjet;
import org.niikoneko.encrier.userInterface.MainController;
import org.niikoneko.encrier.data.DataConnector;
import org.niikoneko.encrier.jpa.Projet;
import org.niikoneko.encrier.jpa.TypeProjet;
import org.tinylog.Logger;

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

    private static final List<TypeProjet> typesList = new ArrayList<>();
    private static final List<Stage> stageList = new ArrayList<>();
    public static MainController controller;

    @FXML
    private ChoiceBox<TypeProjet> typeProjet;
    @FXML
    private TextField nomProjet;
    @FXML
    private TextArea descriptionProjet;
    @FXML
    private ChoiceBox<Stage> typeStage;
    @FXML
    private TextField nomStage;
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
        if (nomProjet.getText().isEmpty() || typeProjet.getValue() == null
            || typeStage.getValue() == null || nomStage.getText().isEmpty()) {
            errorLabel.setText("Vous devez renseigner au minimum un type et un nom de projet, un type et un nom d'étape.");
            return;
        }
        DataConnector bddHandler = new DataConnector();
        // Création du projet
        Projet newProjet = new Projet(typeProjet.getValue(), nomProjet.getText(), descriptionProjet.getText());
        Logger.debug("Création du projet " + newProjet.getNom());
        String error = bddHandler.createOrUpdateProjet(newProjet);
        if (error.isEmpty()) {
            newProjet = bddHandler.getProjetFromNom(newProjet.getNom());
        } else {
            Logger.error("Erreur de création BDD du projet {}. Message : {}", newProjet.getNom(), error);
            errorLabel.setText("Erreur de création du projet : " + error);
            return;
        }
        // Création de l'étape projet en cours
        StageProjet newEtape = new StageProjet(newProjet, typeStage.getValue(), 1, nomStage.getText());
        Logger.debug("Création de l'étape projet " + newEtape.getNom());
        error = bddHandler.createOrUpdateStageProjet(newEtape);
        if (error.isEmpty()) {
            newEtape = bddHandler.getStageProjetFromProjetEtNom(newProjet, newEtape.getNom());
            newProjet.setStageProjet(newEtape);
            error =  bddHandler.createOrUpdateProjet(newProjet);
        } else {
            Logger.error("Erreur de création BDD du projet {}. Message : {}", newProjet.getNom(), error);
            errorLabel.setText("Erreur de création du projet : " + error);
            return;
        }
        javafx.stage.Stage current = (javafx.stage.Stage) annuler.getScene().getWindow();
        controller.initialize();
        if (triggerIntegration.isSelected()) {
            controller.launchIntegration(newEtape);
        }
        current.close();
    }

    @FXML
    protected void onStageTypeSelect() {
        switch (typeStage.getValue().getType()) {
            case ECRITURE, ECRITURE_ET_SUIVI:
                triggerIntegration.setVisible(true);
                break;
            default:
                triggerIntegration.setVisible(false);
        }
    }

    @FXML
    protected void onAnnulerClick() {
        javafx.stage.Stage current = (javafx.stage.Stage) annuler.getScene().getWindow();
        Logger.debug("Création de nouveau projet annulée");
        current.close();
    }

    /**
     * Précharge les types de projet disponibles lors de l'appel par le MainController
     * @param typesListToAdd Les types de projet
     */
    public static void loadTPChoices(List<TypeProjet> typesListToAdd) {
        typesList.clear();
        typesList.addAll(typesListToAdd);
    }

    /**
     * Précharge les types d'étape projet disponibles lors de l'appel par le MainController
     * @param stageListToAdd Les types d'étape projet
     */
    public static void loadTEChoices(List<Stage> stageListToAdd) {
        stageList.clear();
        stageList.addAll(stageListToAdd);
    }

    /**
     * Assigne les types lors de l'initialisation
     */
    private void setChoices() {
        typeProjet.setItems(FXCollections.observableArrayList(typesList));
        typeStage.setItems(FXCollections.observableArrayList(stageList));
    }
}

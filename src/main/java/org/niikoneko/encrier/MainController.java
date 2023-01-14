package org.niikoneko.encrier;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.niikoneko.encrier.data.DataConnector;
import org.niikoneko.encrier.formulaires.NouveauProjetController;
import org.niikoneko.encrier.jpa.Projet;
import org.niikoneko.encrier.jpa.TypeProjet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class MainController {

    private final static Logger logger = LoggerFactory.getLogger(MainController.class);

    private final DataConnector bddHandler = new DataConnector();

    /* -- Menu Principal : Projets -- */
    @FXML
    private MenuItem nouveau;
    @FXML
    private MenuItem archiver;
    @FXML
    private MenuItem gererTypes;
    @FXML
    private MenuItem parametres;
    @FXML
    private MenuItem quitter;

    /* -- Menu Principal : Aide -- */
    @FXML
    private MenuItem aPropos;

    /* -- Elements de la vue centrale -- */
    @FXML
    private ListView<Projet> listProjets;

    public void initialize() {
        List<Projet> projetList = bddHandler.getAllProjets();
        listProjets.setItems(FXCollections.observableArrayList(projetList));
    }

    /* -- Méthodes sur menu -- */
    @FXML
    protected void onNouveauClick() throws IOException {
        logger.debug("Ouverture de la fenêtre de création d'un nouveau projet");
        List<TypeProjet> listTypes = bddHandler.getAllTypesProjets();
        NouveauProjetController.loadChoices(listTypes);
        NouveauProjetController.controller = this;
        FXMLLoader loader = new FXMLLoader(MainController.class.getResource("formulaires/nouveau_projet.fxml"));
        Scene projetFormScene = new Scene(loader.load(), 600, 400);
        Stage nouveauProjet = new Stage();
        nouveauProjet.setScene(projetFormScene);
        nouveauProjet.setTitle("Formulaire");
        nouveauProjet.show();
    }

    @FXML
    protected void onArchiverClick() {
        Alert message = new Alert(Alert.AlertType.INFORMATION);
        message.setContentText("Pas encore implémenté");
        message.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                logger.info("L'utilisateur a bien vu qu'il ne pouvait pas archiver de projet");
            }
        });
    }

    @FXML
    protected void onGestionTypesClick() throws IOException {
        logger.debug("Ouverture de la fenêtre de gestion des types de projet");
        FXMLLoader loader = new FXMLLoader(MainController.class.getResource("formulaires/gestion_types.fxml"));
        Scene gestionTypesScene = new Scene(loader.load(), 700, 400);
        Stage gestionTypes = new Stage();
        gestionTypes.setScene(gestionTypesScene);
        gestionTypes.setTitle("Gestion des types de projet");
        gestionTypes.show();
    }

    @FXML
    protected void onParametresClick() {
        Alert message = new Alert(Alert.AlertType.INFORMATION);
        message.setContentText("Pas encore implémenté");
        message.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                logger.info("L'utilisateur a bien vu qu'il ne pouvait pas accéder aux paramètres");
            }
        });
    }

    @FXML
    protected void onQuitterClick() {
        logger.info("Clic sur quitter, sortie de l'application");
        System.exit(0);
    }

    /* -- Méthodes sur liste de projet -- */

    @FXML
    protected void onProjetClick(){

    }
}
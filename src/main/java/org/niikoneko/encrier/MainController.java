package org.niikoneko.encrier;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.niikoneko.encrier.data.DataConnector;
import org.niikoneko.encrier.formulaires.IntegrationProjetController;
import org.niikoneko.encrier.formulaires.NouveauProjetController;
import org.niikoneko.encrier.jpa.Projet;
import org.niikoneko.encrier.jpa.TypeProjet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class MainController {

    private static MainController instance;

    private final static Logger logger = LoggerFactory.getLogger(MainController.class);

    private final DataConnector bddHandler = new DataConnector();

    private RightPanelController rightController;

    private CentralViewController centralController;

    private Projet selectedProjet;

    /* -- Elements de la vue centrale -- */
    @FXML
    private ListView<Projet> listProjets;

    /* -- Elements du bas -- */
    @FXML
    private Label versionText;
    @FXML
    private Label leftBottomText;

    public void initialize() {
        instance = this;
        // MAJ de la liste des projets
        List<Projet> projetList = bddHandler.getAllProjets();
        listProjets.setItems(FXCollections.observableArrayList(projetList));
        // Récupération du rightController
        rightController = RightPanelController.getInstance();
        rightController.declareMainController(this);
        rightController.initialize();
        // Récupération du centralController
        centralController = CentralViewController.getInstance();
        centralController.initialize();
        // Reprise du projet sélectionné
        if (selectedProjet != null && bddHandler.getProjetFromId(selectedProjet.getId()) != null) {
            selectedProjet = projetList.stream().filter(p -> p.getId().equals(selectedProjet.getId())).toList().get(0);
            listProjets.getSelectionModel().select(selectedProjet);
        }
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
        selectedProjet = listProjets.getSelectionModel().getSelectedItem();
        if (selectedProjet != null) {
            rightController.setCurrentProjet(selectedProjet);
            centralController.setCurrentProjet(selectedProjet);
        }
    }

    /* -- Méthodes déclenchées par le système -- */

    protected void updateCentralView() {
        centralController.updateDisplays();
    }

    public void launchIntegration(Projet projet) throws IOException {
        logger.debug("Ouverture de la fenêtre d'intégration d'un nouveau projet.");
        IntegrationProjetController.loadProjet(projet);
        FXMLLoader loader = new FXMLLoader(MainController.class.getResource("formulaires/integrer_projet.fxml"));
        Scene integFormScene = new Scene(loader.load(), 600, 400);
        Stage integrationProjet = new Stage();
        integrationProjet.setScene(integFormScene);
        integrationProjet.setTitle("Formulaire");
        integrationProjet.show();
    }

    /**
     * Récupère l'instance initialisée
     * @return l'instance actuellement utilisée
     */
    public static MainController getInstance() {
        return instance;
    }

    protected void setVersion(String version, String leftText) {
        versionText.setText(version);
        leftBottomText.setText(leftText);
    }
}
package org.niikoneko.encrier;

import javafx.scene.image.Image;
import org.niikoneko.encrier.data.DataConnector;
import org.niikoneko.encrier.userInterface.MainController;
import org.slf4j.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class Main extends javafx.application.Application {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    private final static Properties proprietes = new Properties();

    @Override
    public void start(Stage stage) throws IOException {
        // Informations de base
        proprietes.load(this.getClass().getClassLoader().getResourceAsStream(".properties"));
        String startupMessage = """
                  |   |     oooooooooooo                                 o8o
                  |   |     `888'     `8                                 `"'
                  |===|      888         ooo. .oo.    .ooooo.  oooo d8b oooo   .ooooo.  oooo d8b
                  |___|      888oooo8    `888P"Y88b  d88' `"Y8 `888""8P `888  d88' `88b `888""8P
                   ).(       888    "     888   888  888        888      888  888ooo888  888
                   \\|/       888       o  888   888  888   .o8  888      888  888    .o  888
                    '       o888ooooood8 o888o o888o `Y8bod8P' d888b    o888o `Y8bod8P' d888b
                """;
        logger.info("\n" + startupMessage);
        String version = "Version " + proprietes.getProperty("version");
        logger.info(version);

        // Démarrage de l'appli
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("userInterface/main_view.fxml"));
        Image icone = new Image("icon_Encrier.png");
        Scene scene = new Scene(fxmlLoader.load(), 1000, 650);
        stage.setTitle("Encrier");
        stage.setScene(scene);
        stage.setMinHeight(650);
        stage.setMinWidth(1200);
        stage.setMaximized(true);
        stage.getIcons().add(icone);
        stage.show();
        MainController mainController = MainController.getInstance();
        mainController.setParams(version, "Snapshot - Travail en cours", icone);
    }

    public static void main(String[] args) {
        // Connection BDD
        DataConnector bddHandler = new DataConnector();
        if (!bddHandler.Connectto()) {
            logger.info("Pas de BDD détectée, installation.");
            boolean installOk = bddHandler.bddInstall();
            if (!installOk) {
                logger.error("Erreur d'installation de la BDD.");
                System.exit(1);
            }
            logger.info("BDD installée");
        }
        launch();
    }

    /**
     * Récupère une propriété sur demande
     * @param name le nom / la clé de la propriété
     * @return la valeur associée
     */
    public static String getProperty(String name) {
        return proprietes.getProperty(name);
    }
}
package org.niikoneko.encrier;

import org.niikoneko.encrier.data.DataConnector;
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

    private final String startupMessage =
"""
  |   |     oooooooooooo                                 o8o
  |   |     `888'     `8                                 `"'
  |===|      888         ooo. .oo.    .ooooo.  oooo d8b oooo   .ooooo.  oooo d8b
  |___|      888oooo8    `888P"Y88b  d88' `"Y8 `888""8P `888  d88' `88b `888""8P
   ).(       888    "     888   888  888        888      888  888ooo888  888
   \\|/       888       o  888   888  888   .o8  888      888  888    .o  888
    '       o888ooooood8 o888o o888o `Y8bod8P' d888b    o888o `Y8bod8P' d888b
""";

    private final String startupMessageAlt =
"""
  ,
 "\\",        ooooooooo.   oooo
 "=\\=",      `888   `Y88. `888
  "=\\=",      888   .d88'  888  oooo  oooo  ooo. .oo.  .oo.    .ooooo.      88
   "=\\=",     888ooo88P'   888  `888  `888  `888P"Y88bP"Y88b  d88' `88b     88
    "-\\-"     888          888   888   888   888   888   888  888ooo888 8888888888
       \\      888          888   888   888   888   888   888  888    .o     88
        `    o888o        o888o  `V88V"V8P' o888o o888o o888o `Y8bod8P'     88
""";

    @Override
    public void start(Stage stage) throws IOException {
        // Informations de base
        proprietes.load(this.getClass().getClassLoader().getResourceAsStream(".properties"));
        logger.info("\n" + startupMessage);
        logger.info("Version " + proprietes.getProperty("version"));

        // Démarrage de l'appli
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setTitle("Encrier v" + proprietes.getProperty("version"));
        stage.setScene(scene);
        stage.show();
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
}
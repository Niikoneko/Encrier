package org.niikoneko.encrier.userInterface.formulaires;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Controller pour la fenêtre "Donner son avis"
 * @author Niikoneko
 * @since 2024/01
 * @version 1.0
 */
public class AvisController {

    private static final Logger logger = LoggerFactory.getLogger(AvisController.class);
    @FXML
    private Button closeButton;

    @FXML
    protected void onFermerClick() {
        Stage current = (Stage) closeButton.getScene().getWindow();
        current.close();
    }

    @FXML
    protected void onAvisClick() {
        try {
            Desktop.getDesktop().browse(new URL("https://forms.gle/nrU1kp33JVh24JiG7").toURI());
        } catch (IOException e) {
            logger.error("Erreur IO à l'ouverture du lien :", e);
        } catch (URISyntaxException e) {
            logger.error("Erreur de syntaxe URL :", e);
        }
    }
}

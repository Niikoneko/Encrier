package org.niikoneko.encrier.userInterface.menus;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Controller pour le formulaire de signalement de bugs
 * @author Niikoneko
 * @since 2024/01
 * @version 1.0
 */
public class SignalerBugController {

    @FXML
    private Button closeButton;

    @FXML
    protected void onFermerClick() {
        Stage current = (Stage) closeButton.getScene().getWindow();
        current.close();
    }

    @FXML
    protected void onSupportFormClick() {
        try {
            Desktop.getDesktop().browse(new URL("https://forms.gle/6roaUfudc5DDR1JV6").toURI());
        } catch (IOException e) {
            Logger.error("Erreur IO à l'ouverture du lien :", e);
        } catch (URISyntaxException e) {
            Logger.error("Erreur de syntaxe URL :", e);
        }
    }

    @FXML
    protected void onDiscordClick() {
        try {
            Desktop.getDesktop().browse(new URL("https://discord.gg/DyQN5NYEXh").toURI());
        } catch (IOException e) {
            Logger.error("Erreur IO à l'ouverture du lien :", e);
        } catch (URISyntaxException e) {
            Logger.error("Erreur de syntaxe URL :", e);
        }
    }
}

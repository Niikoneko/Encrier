package org.niikoneko.encrier.userInterface.formulaires;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Controller pour la fenêtre "À propos"
 * @author Niikoneko
 * @since 2024/01
 * @version 1.0
 */
public class AProposController {

    @FXML
    private Button closeButton;

    @FXML
    protected void onFermerClick() {
        Stage current = (Stage) closeButton.getScene().getWindow();
        current.close();
    }

    @FXML
    protected void onGitHubClick() {
        try {
            Desktop.getDesktop().browse(new URL("https://github.com/Niikoneko/Encrier").toURI());
        } catch (IOException e) {
            Logger.error("Erreur IO à l'ouverture du lien :", e);
        } catch (URISyntaxException e) {
            Logger.error("Erreur de syntaxe URL :", e);
        }
    }

    @FXML
    protected void onInstagramClick() {
        try {
            Desktop.getDesktop().browse(new URL("https://www.instagram.com/_niikoneko_?igsh=azF2eW9sYm5seGQw").toURI());
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

    @FXML
    protected void onLicenseClick() {
        try {
            Desktop.getDesktop().open(new File("org/niikoneko/encrier/Encrier_License.md"));
        } catch (IOException e) {
            Logger.error("Erreur IO à l'ouverture du fichier de licence :", e);
        }
    }
}

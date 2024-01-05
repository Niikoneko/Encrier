package org.niikoneko.encrier.userInterface.formulaires;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.niikoneko.encrier.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Controller pour le formulaire de signalement de bugs
 * @author Niikoneko
 * @since 2024/01
 * @version 1.0
 */
public class SignalerBugController {

    private static final Logger logger = LoggerFactory.getLogger(SignalerBugController.class);
    @FXML
    private Label errorMessage;
    @FXML
    private TextField titre;
    @FXML
    private TextArea description;
    @FXML
    private Button annuler;
    @FXML
    private Button envoi;
    @FXML
    private Label progressMessage;
    @FXML
    private ProgressBar sendProgress;

    private boolean flagSent = false;

    @FXML
    protected void onAnnulerClick() {
        Stage current = (Stage) annuler.getScene().getWindow();
        current.close();
    }

    @FXML
    protected void onEnvoyerClick() {
        if (flagSent) {
            onAnnulerClick();
            return;
        }
        sendProgress.setProgress(0.25);
        progressMessage.setText("Vérification");
        if (!checkData()) return;
        sendProgress.setProgress(0.5);
        progressMessage.setText("Préparation");
        logger.info("Préparation d'envoi d'un bug");
        String address = "https://api.github.com/repos/" + Main.getProperty("github_owner")
                + "/" + Main.getProperty("github_repo") + "/issues";
        String data = buildData();
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        HttpRequest issueReq = HttpRequest.newBuilder()
                .uri(URI.create(address))
                .timeout(Duration.ofMinutes(2))
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + Main.getProperty("github_token"))
                .header("X-GitHub-Api-Version", "2022-11-28")
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();
        sendProgress.setProgress(0.75);
        progressMessage.setText("Envoi");
        try {
            HttpResponse<String> response = client.send(issueReq, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 201) {
                logger.error("Réponse reçue en erreur : code HTTP {}, corps de réponse {}", response.statusCode(), response.body());
                logger.error("Corps de requête envoyé : {}", data);
            } else {
                sendProgress.setProgress(1.0);
                progressMessage.setText("Envoi reçu !");
                progressMessage.setTextFill(Color.GREEN);
                logger.info("Bug envoyé");
                flagSent = true;
                envoi.setText("Fermer");
            }
        } catch (IOException e) {
            logger.error("Erreur I/O à l'envoi de la requête", e);
        } catch (InterruptedException e) {
            logger.error("Interruption à l'envoi de la requête", e);
        }
    }

    /**
     * Permet de vérifier les paramètres du formulaire
     * @return true si tout est OK, paramètre les erreurs sinon
     */
    private boolean checkData() {
        if (titre.getText().isEmpty() || titre.getText().length() < 20) {
            errorMessage.setText("Vous devez indiquer un titre d'au moins 20 caractères");
            sendProgress.setProgress(0);
            progressMessage.setText("");
            return false;
        }
        if (description.getText().isEmpty() || description.getText().length() < 50) {
            errorMessage.setText("Vous devez indiquer une description d'au moins 50 caractères");
            sendProgress.setProgress(0);
            progressMessage.setText("");
            return false;
        }
        return true;
    }

    /**
     * Construit le bloc data de la requête
     * @return le bloc data construit
     */
    private String buildData() {
        String builder = "{\"title\":\"" +
                titre.getText() +
                "\",\"body\":\"" +
                description.getText() +
                "\",\"assignees\":[\"Niikoneko\"],\"labels\":[\"bug\"]}";
        return builder;
    }
}

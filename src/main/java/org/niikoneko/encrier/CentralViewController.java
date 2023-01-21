package org.niikoneko.encrier;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.niikoneko.encrier.data.DataConnector;
import org.niikoneko.encrier.jpa.Projet;
import org.niikoneko.encrier.jpa.ProjetMots;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class CentralViewController {

    private static CentralViewController instance;

    private final static Logger logger = LoggerFactory.getLogger(RightPanelController.class);

    private final DataConnector bddHandler = new DataConnector();

    private Projet currentProjet;

    @FXML
    private Pane cachePane;
    @FXML
    private Label titre;
    @FXML
    private Label description;
    @FXML
    private Label mots;
    @FXML
    private Label temps;
    @FXML
    private LineChart<String, Long> graphiqueNbMots;


    public void initialize() {
        instance = this;
    }

    /**
     * Met à jour les affichages sur le projet
     */
    protected void updateDisplays() {
        if (currentProjet != null) {
            // -- Infos immédiates
            titre.setText(currentProjet.getNom());
            description.setText(currentProjet.getDescription());
            mots.setText(bddHandler.getNombreMotsFromProjet(currentProjet) + " Mots");
            // Calcul du temps passé
            Duration tempsCumul = bddHandler.getTempsFromProjet(currentProjet);
            long jours = tempsCumul.toDays();
            tempsCumul = tempsCumul.minus(Duration.of(jours, ChronoUnit.DAYS));
            long heures = tempsCumul.toHours();
            tempsCumul = tempsCumul.minus(Duration.of(heures, ChronoUnit.HOURS));
            long minutes = tempsCumul.toMinutes();
            temps.setText(jours + "j  " + heures + ":" + minutes + " passés");
            // -- Mise à jour des graphiques
            // Nombre de mots / temps
            graphiqueNbMots.getData().clear();
            graphiqueNbMots.getData().add(createNbMotsSeries());
        } else {
            titre.setText("");
            mots.setText("------ Mots");
            temps.setText("--j  --:-- passés");
        }
    }

    private XYChart.Series<String, Long> createNbMotsSeries() {
        XYChart.Series<String, Long> evolutionNbMots = new XYChart.Series<>();
        evolutionNbMots.setName("Nombre de mots du projet " + currentProjet.getNom());
        List<ProjetMots> rawEntries = bddHandler.getAllProjetMotsFromProjet(currentProjet);
        long cumulNbMots = 0;
        LocalDate lastDate = null;
        for (ProjetMots session : rawEntries) {
            if (lastDate == null) {
                lastDate = session.getEntryDate();
            } else if (session.getEntryDate().isAfter(lastDate)) {
                evolutionNbMots.getData().add(new XYChart.Data<>(lastDate.toString(), cumulNbMots));
                lastDate = session.getEntryDate();
            }
            cumulNbMots += session.getNombreMots();
        }
        evolutionNbMots.getData().add(new XYChart.Data<>(
                Objects.requireNonNullElseGet(lastDate, LocalDate::now).toString(), cumulNbMots));
        return evolutionNbMots;
    }

    /**
     * Prend en compte le projet sélectionné et lève le cache ou réinitialise si null
     * @param projet le projet sélectionné
     */
    protected void setCurrentProjet(Projet projet) {
        if (projet == null) {
            this.currentProjet = null;
            this.cachePane.setVisible(true);
        } else {
            this.currentProjet = projet;
            this.cachePane.setVisible(false);
        }
        updateDisplays();
    }

    /**
     * Récupère l'instance initialisée
     * @return l'instance actuellement utilisée
     */
    public static CentralViewController getInstance() {
        return instance;
    }
}

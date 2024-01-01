package org.niikoneko.encrier.userInterface;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import org.niikoneko.encrier.data.DataConnector;
import org.niikoneko.encrier.jpa.Projet;
import org.niikoneko.encrier.jpa.ProjetMots;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
    private LineChart<Number, Long> graphiqueNbMots;


    public void initialize() {
        instance = this;
        titre.setText("");
        description.setText("");
        mots.setText("------ Mots");
        temps.setText("--j  --:-- passés");
        cachePane.setVisible(true);
        ValueAxis<Number> axis = (ValueAxis<Number>) graphiqueNbMots.getXAxis();
        axis.setTickLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Number aLong) {
                return LocalDate.ofEpochDay(aLong.longValue()).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
            }

            @Override
            public Long fromString(String s) {
                return null;
            }
        });
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
            graphiqueNbMots.setAnimated(false);
            graphiqueNbMots.getData().add(createNbMotsSeries((ValueAxis<Number>) graphiqueNbMots.getXAxis()));
        } else {
            titre.setText("");
            description.setText("");
            mots.setText("------ Mots");
            temps.setText("--j  --:-- passés");
        }
    }

    private XYChart.Series<Number, Long> createNbMotsSeries(ValueAxis<Number> axeX) {
        XYChart.Series<Number, Long> evolutionNbMots = new XYChart.Series<>();
        evolutionNbMots.setName("Nombre de mots du projet " + currentProjet.getNom());
        List<ProjetMots> rawEntries = bddHandler.getAllProjetMotsFromProjet(currentProjet);
        long cumulNbMots = 0;
        LocalDate lastDate = null;
        for (ProjetMots session : rawEntries) {
            if (lastDate == null) {
                lastDate = session.getEntryDate();
                axeX.setLowerBound(lastDate.toEpochDay());
            } else if (session.getEntryDate().isAfter(lastDate)) {
                evolutionNbMots.getData().add(new XYChart.Data<>(
                        lastDate.toEpochDay(), cumulNbMots));
                lastDate = session.getEntryDate();
            }
            cumulNbMots += session.getNombreMots();
        }
        if (lastDate == null) {
            lastDate = LocalDate.now();
            evolutionNbMots.getData().add(new XYChart.Data<>(lastDate.toEpochDay(), cumulNbMots));
            axeX.setLowerBound(lastDate.minusDays(10).toEpochDay());
            axeX.setUpperBound(lastDate.plusDays(10).toEpochDay());
        } else {
            evolutionNbMots.getData().add(new XYChart.Data<>(lastDate.toEpochDay(), cumulNbMots));
            axeX.setUpperBound(lastDate.toEpochDay());
        }
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

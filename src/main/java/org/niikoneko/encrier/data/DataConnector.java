package org.niikoneko.encrier.data;

import org.niikoneko.encrier.jpa.Projet;
import org.niikoneko.encrier.jpa.ProjetMots;
import org.niikoneko.encrier.jpa.TypeProjet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe d'interface avec la BDD
 * @author Niikoneko
 * @since 2023/01
 * @version 0.1
 */
public class DataConnector {

    private final static Logger logger = LoggerFactory.getLogger(DataConnector.class);

    /**
     * Fonction de test d'existence de la BDD
     * @return true si la BDD existe
     */
    public boolean Connectto() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection conn = DriverManager.getConnection(
                    BddInfos.bddUrl + ";ifexists=true", BddInfos.bddUser, BddInfos.bddPass);
            conn.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Récupère tous les types de projet existants en BDD
     * @return La liste des types de projet
     */
    public List<TypeProjet> getAllTypesProjets() {
        String query = "SELECT * FROM \"type_projet\";";
        try {
            List<TypeProjet> resultat = new ArrayList<TypeProjet>();
            ResultSet result = executeQuery(query);
            while (result.next()) {
                resultat.add(new TypeProjet(result.getLong("id"),
                        result.getString("nom"),
                        result.getString("description")));
            }
            return resultat;
        } catch (SQLException e) {
            logger.error("Erreur de récupération d'objets. Requête : \n {}", query, e);
        }
        return null;
    }

    /**
     * Récupère un type de projet à partir de son id
     * @param id L'id du type de projet
     * @return Le TypeProjet associé
     */
    public TypeProjet getTypeProjetFromId(long id) {
        String query = "SELECT * FROM \"type_projet\" WHERE \"id\" = " + id + ";";
        try {
            TypeProjet resultat;
            ResultSet result = executeQuery(query);
            result.next();
            resultat = new TypeProjet(result.getLong("id"),
                    result.getString("nom"),
                    result.getString("description"));
            return resultat;
        } catch (SQLException e) {
            logger.error("Erreur de récupération d'un type de projet. Requête : \n {}", query, e);
        }
        return null;
    }

    /**
     * Récupère tous les projets existants en BDD
     * @return La liste des projets
     */
    public List<Projet> getAllProjets() {
        String query = "SELECT * FROM \"projet\";";
        try {
            List<Projet> resultat = new ArrayList<>();
            ResultSet result = executeQuery(query);
            while (result.next()) {
                TypeProjet type = getTypeProjetFromId(result.getLong("type_id"));
                resultat.add(new Projet(result.getLong("id"),
                        type,
                        result.getString("nom"),
                        result.getString("description"),
                        result.getBoolean("archive")));
            }
            return resultat;
        } catch (SQLException e) {
            logger.error("Erreur de récupération d'objets. Requête : \n {}", query, e);
        }
        return null;
    }

    /**
     * Récupère tous les projets associés à un type donné
     * @param type Le type de projet
     * @return La liste des projets associés au type
     */
    public List<Projet> getAllProjetsFromType(TypeProjet type) {
        String query = "SELECT * FROM \"projet\"" + "WHERE \"type_id\" = " + type.getId() + ";";
        try {
            List<Projet> resultat = new ArrayList<>();
            ResultSet result = executeQuery(query);
            while (result.next()) {
                resultat.add(new Projet(result.getLong("id"),
                        type,
                        result.getString("nom"),
                        result.getString("description"),
                        result.getBoolean("archive")));
            }
            return resultat;
        } catch (SQLException e) {
            logger.error("Erreur de récupération d'objets. Requête : \n {}", query, e);
        }
        return null;
    }

    /**
     * Récupère un projet par son id
     * @param id L'id du projet
     * @return Le projet possédant cet id
     */
    public Projet getProjetFromId(long id) {
        String query = "SELECT * FROM \"projet\" WHERE \"id\" = " + id + ";";
        try {
            Projet resultat;
            ResultSet result = executeQuery(query);
            result.next();
            TypeProjet type = getTypeProjetFromId(result.getLong("type_id"));
            resultat = new Projet(result.getLong("id"),
                    type,
                    result.getString("nom"),
                    result.getString("description"),
                    result.getBoolean("archive"));
            return resultat;
        } catch (SQLException e) {
            logger.error("Erreur de récupération d'un type de projet. Requête : \n {}", query, e);
        }
        return null;
    }

    public List<ProjetMots> getAllProjetMotsFromProjet(Projet projet) {
        String query = "SELECT * FROM \"projet_mots\"" +
                "WHERE \"projet_id\" = " + projet.getId() + ";";
        List<ProjetMots> resultats = new ArrayList<>();
        try {
            ResultSet result = executeQuery(query);
            while (result.next()) {
                resultats.add(new ProjetMots(result.getLong("id"),
                        projet,
                        result.getDate("entry_date").toLocalDate(),
                        result.getLong("nombre_mots"),
                        getDurationFromProjetMots(result.getString("temps_session"))
                ));
            }
        } catch (SQLException e) {
            logger.error("Erreur de récupération d'objets. Requête : \n {}", query, e);
        }
        return resultats;
    }

    public int getNombreMotsFromProjet(Projet projet) {
        String query = "SELECT SUM(\"nombre_mots\") as mots FROM \"projet_mots\"" +
                "WHERE \"projet_id\" = " + projet.getId() + ";";
        try {
            ResultSet result = executeQuery(query);
            result.next();
            return result.getInt("mots");
        } catch (SQLException e) {
            logger.error("Erreur de récupération d'objets. Requête : \n {}", query, e);
        }
        return 0;
    }

    public Duration getTempsFromProjet(Projet projet) {
        String query = "SELECT SUM(\"temps_session\") as temps FROM \"projet_mots\"" +
                "WHERE \"projet_id\" = " + projet.getId() + ";";
        try {
            ResultSet result = executeQuery(query);
            result.next();
            if (result.getString("temps") != null)
                return getDurationFromProjetMots(result.getString("temps"));
        } catch (SQLException e) {
            logger.error("Erreur de récupération d'objets. Requête : \n {}", query, e);
        }
        return Duration.of(0, ChronoUnit.MINUTES);
    }

    /**
     * Création de la base de données (premier lancement)
     * @return true si la création est OK, false sinon
     */
    public boolean bddInstall() {
        // Création de la BDD par les scripts dédiés
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection conn = DriverManager.getConnection(
                    BddInfos.bddUrl + ";create=true", BddInfos.bddUser, BddInfos.bddPass);
            Statement state = conn.createStatement();
            state.execute(Files.readString(Path.of("classes/org/niikoneko/encrier/data/encrier.sql")));
            state.close();
            state = conn.createStatement();
            state.execute(Files.readString(Path.of("classes/org/niikoneko/encrier/data/encrier_constraints.sql")));
            state.close();
            state = conn.createStatement();
            state.execute(Files.readString(Path.of("classes/org/niikoneko/encrier/data/encrier_default_data.sql")));
            state.close();
            conn.close();
        } catch (Exception e) {
            logger.error("Erreur de création de la BDD ", e);
            return false;
        }
        return true;
    }

    /**
     * Créé un type projet si l'id est vide, le met à jour sinon
     * @param type Le type de projet à créer ou modifier
     * @return Un texte vide si ok, l'erreur si erreur
     */
    public String createOrUpdateTypeProjet(TypeProjet type) {
        String query = "";
        if (type.getId() == null) {
            // Création
            query = "INSERT INTO \"type_projet\" (\"nom\", \"description\")" +
                    "VALUES ('" + type.getNom() + "', '" + type.getDescription() + "');";
        } else {
            // Mise à jour
            query = "UPDATE \"type_projet\" " +
                    "SET \"nom\" = '" + type.getNom() + "', " +
                    "\"description\" = '" + type.getDescription() + "' " +
                    "WHERE \"id\" = " + type.getId() + ";";
        }
        try {
            executeQuery(query);
            return "";
        } catch (SQLIntegrityConstraintViolationException e) {
            logger.error("Violation de contrainte SQL en création de type de projet : nom unique. Requête : \n {}", query);
            return "Un type de projet du même nom existe déjà.";
        } catch (SQLException e) {
            logger.error("Erreur de création ou MAJ d'un type de projet. Requête : \n {}", query, e);
            return "Erreur inconnue.";
        }
    }

    /**
     * Créé un projet si l'id est vide, le met à jour sinon
     * @param projet Le projet à créer ou modifier
     * @return Un texte vide si ok, l'erreur si erreur
     */
    public String createOrUpdateProjet(Projet projet) {
        String query = "";
        if (projet.getId() == null) {
            // Création
             query = "INSERT INTO \"projet\" (\"type_id\", \"nom\", \"description\", \"archive\")" +
                    "VALUES ('" + projet.getTypeProjet().getId() + "', '" + projet.getNom() + "', '" +
                    projet.getDescription() + "', '" + projet.getAchive() + "');";
        } else {
            // Mise à jour
            query = "UPDATE \"projet\"" +
                    "SET \"type_id\" = '" + projet.getTypeProjet().getId() + "', " +
                    "\"nom\" = '" + projet.getNom() + "', " +
                    "\"description\" = '" + projet.getDescription() + "', " +
                    "\"archive\" = '" + projet.getAchive() + "' " +
                    "WHERE \"id\" = " + projet.getId() + ";";
        }
        try {
            executeQuery(query);
            return "";
        } catch (SQLIntegrityConstraintViolationException e) {
            logger.error("Violation de contrainte SQL en création de projet : nom unique. Requête : \n {}", query);
            return "Un projet du même nom existe déjà.";
        } catch (SQLException e) {
            logger.error("Erreur de création ou MAJ d'un projet. Requête : \n {}", query, e);
            return "Erreur inconnue.";
        }
    }

    /**
     * Création d'une session d'écriture
     * @param session la session à créer en base
     * @return Un texte vide si ok, l'erreur si erreur
     */
    public String createProjetMots(ProjetMots session) {
        String query = "INSERT INTO \"projet_mots\" (\"projet_id\", \"entry_date\", \"nombre_mots\", \"temps_session\")" +
                "VALUES ('" + session.getProjet().getId() + "', '" + session.getEntryDate() + "', '" +
                session.getNombreMots() + "', " +session.getTempsSession().toMinutes() + ");";
        try {
            executeQuery(query);
            return "";
        } catch (SQLException e) {
            logger.error("Erreur de création d'une session d'écriture. Requête : \n {}", query, e);
            return "Erreur inconnue.";
        }
    }

    /**
     * Suppression d'un type de projet
     * @param type Le type à supprimer
     * @return Un texte vide si ok, l'erreur si erreur
     */
    public String deleteTypeProjet(TypeProjet type) {
        String query = "DELETE FROM \"type_projet\"" +
                "WHERE \"id\" = " + type.getId() + ";";
        try {
            executeQuery(query);
            return "";
        } catch (SQLException e) {
            logger.error("Erreur de suppression d'un type de projet. Requête : \n {}", query, e);
            return "Erreur inconnue.";
        }
    }

    /**
     * Execution d'une requête en base
     * @param query La requête à utiliser
     * @return Le ResultSet de réponse
     * @throws SQLException Si erreur lors de l'exécution
     */
    private ResultSet executeQuery(String query) throws SQLException {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection conn = DriverManager.getConnection(
                    BddInfos.bddUrl + ";ifexists=true", BddInfos.bddUser, BddInfos.bddPass);
            Statement state = conn.createStatement();
            ResultSet result = state.executeQuery(query);
            state.close();
            conn.close();
            return result;
        } catch (ClassNotFoundException e) {
            logger.error("Erreur : JDBC non trouvé. Revoir les paramètres d'installation.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Transforme la donnée récupérée en Duration Java
     * @param dataBaseData La donnée récupérée de la BDD
     * @return Une duration à partir de l'Interval
     */
    private Duration getDurationFromProjetMots(String dataBaseData) {
        String[] interval = dataBaseData.split(" ");
        int jours = Integer.parseInt(interval[0]);
        String[] timePart = interval[1].split(":");
        int heures = Integer.parseInt(timePart[0]);
        int minutes = Integer.parseInt(timePart[1]);
        return Duration.ofDays(jours).plus(Duration.ofHours(heures)).plus(Duration.ofMinutes(minutes));
    }
}
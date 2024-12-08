package org.niikoneko.encrier.data;

import org.niikoneko.encrier.jpa.*;
import org.tinylog.Logger;

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

    /**
     * Fonction de test d'existence de la BDD
     * @return true si la BDD existe
     */
    public boolean ConnectTo() {
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
            List<TypeProjet> resultat = new ArrayList<>();
            ResultSet result = executeQuery(query);
            while (result.next()) {
                resultat.add(new TypeProjet(result.getLong("id"),
                        result.getString("nom"),
                        result.getString("description")));
            }
            return resultat;
        } catch (SQLException e) {
            Logger.error("Erreur de récupération d'objets. Requête : \n {}", query, e);
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
            if (result.next()) {
                resultat = new TypeProjet(result.getLong("id"),
                        result.getString("nom"),
                        result.getString("description"));
                return resultat;
            }
        } catch (SQLException e) {
            Logger.error("Erreur de récupération d'un type de projet. Requête : \n {}", query, e);
        }
        return null;
    }

    /**
     * Récupère tous les types d'étapes projet existantes en BDD
     * @return La liste des types d'étapes projet
     */
    public List<Stage> getAllStages() {
        String query = "SELECT * FROM \"stage\";";
        try {
            List<Stage> resultat = new ArrayList<>();
            ResultSet result = executeQuery(query);
            while (result.next()) {
                resultat.add(new Stage(result.getLong("id"),
                        result.getString("nom"),
                        TypesEtapes.fromString(result.getString("type")),
                        result.getString("description")));
            }
            return resultat;
        } catch (SQLException e) {
            Logger.error("Erreur de récupération d'objets. Requête : \n {}", query, e);
        }
        return null;
    }

    /**
     * Récupère un type d'étape projet à partir de son id
     * @param id L'id du type d'étape projet
     * @return L'objet Stage associé
     */
    public Stage getStagesFromId(long id) {
        String query = "SELECT * FROM \"stage\" WHERE \"id\" = " + id + ";";
        try {
            Stage resultat;
            ResultSet result = executeQuery(query);
            if (result.next()) {
                resultat = new Stage(result.getLong("id"),
                        result.getString("nom"),
                        TypesEtapes.fromString(result.getString("type")),
                        result.getString("description"));
                return resultat;
            }
        } catch (SQLException e) {
            Logger.error("Erreur de récupération de type d'étapes de projet. Requête : \n {}", query, e);
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
            Projet tempProjet;
            while (result.next()) {
                TypeProjet type = getTypeProjetFromId(result.getLong("type_id"));
                tempProjet = new Projet(result.getLong("id"),
                        type,
                        result.getString("nom"),
                        result.getString("description"));
                StageProjet curStage = getStageProjetFromProjetAndId(tempProjet, result.getLong("stage_id"));
                tempProjet.setStageProjet(curStage);
                resultat.add(tempProjet);
            }
            return resultat;
        } catch (SQLException e) {
            Logger.error("Erreur de récupération d'objets. Requête : \n {}", query, e);
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
            Projet tempProjet;
            while (result.next()) {
                tempProjet = new Projet(result.getLong("id"),
                        type,
                        result.getString("nom"),
                        result.getString("description"));
                StageProjet curStage = getStageProjetFromProjetAndId(tempProjet, result.getLong("stage_id"));
                tempProjet.setStageProjet(curStage);
                resultat.add(tempProjet);
            }
            return resultat;
        } catch (SQLException e) {
            Logger.error("Erreur de récupération d'objets. Requête : \n {}", query, e);
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
            if (result.next()) {
                TypeProjet type = getTypeProjetFromId(result.getLong("type_id"));
                resultat = new Projet(result.getLong("id"),
                        type,
                        result.getString("nom"),
                        result.getString("description"));
                StageProjet curStage = getStageProjetFromProjetAndId(resultat, result.getLong("stage_id"));
                resultat.setStageProjet(curStage);
                return resultat;
            }
        } catch (SQLException e) {
            Logger.error("Erreur de récupération d'un projet. Requête : \n {}", query, e);
        }
        return null;
    }

    /**
     * Récupère un projet par son nom
     * @param nom Le nom du projet
     * @return Le projet possédant ce nom
     */
    public Projet getProjetFromNom(String nom) {
        String query = "SELECT * FROM \"projet\" WHERE \"nom\" = '" + replaceApostrophes(nom) + "';";
        try {
            Projet resultat;
            ResultSet result = executeQuery(query);
            if (result.next()) {
                TypeProjet type = getTypeProjetFromId(result.getLong("type_id"));
                resultat = new Projet(result.getLong("id"),
                        type,
                        result.getString("nom"),
                        result.getString("description"));
                StageProjet curStage = getStageProjetFromProjetAndId(resultat, result.getLong("stage_id"));
                resultat.setStageProjet(curStage);
                return resultat;
            }
        } catch (SQLException e) {
            Logger.error("Erreur de récupération d'un projet. Requête : \n {}", query, e);
        }
        return null;
    }

    /**
     * Récupère une étape projet par son id
     * @param id L'id de l'étape projet
     * @return L'étape projet possédant cet id
     */
    public StageProjet getStageProjetFromProjetAndId(Projet projet, long id) {
        String query = "SELECT * FROM \"stage_projet\" WHERE \"id\" = " + id + ";";
        try {
            StageProjet resultat;
            ResultSet result = executeQuery(query);
            if (result.next()) {
                Stage stage = getStagesFromId(result.getLong("id_stage"));
                resultat = new StageProjet(result.getLong("id"),
                        projet,
                        stage,
                        result.getInt("ordre"),
                        result.getString("nom"));
                return resultat;
            }
        } catch (SQLException e) {
            Logger.error("Erreur de récupération d'une étape de projet. Requête : \n {}", query, e);
        }
        return null;
    }

    /**
     * Récupère une étape projet par son projet et son nom
     * @param projet Le projet associé
     * @param nom Le nom de l'étape projet
     * @return L'étape projet possédant ce projet et ce nom
     */
    public StageProjet getStageProjetFromProjetEtNom(Projet projet, String nom) {
        String query = "SELECT * FROM \"stage_projet\" " +
                "WHERE \"projet_id\" = '" + projet.getId() + "' " +
                "AND \"nom\" = '" + replaceApostrophes(nom) + "';";
        try {
            StageProjet resultat;
            ResultSet result = executeQuery(query);
            if (result.next()) {
                Stage stage = getStagesFromId(result.getLong("id_stage"));
                resultat = new StageProjet(result.getLong("id"),
                        projet,
                        stage,
                        result.getInt("ordre"),
                        result.getString("nom"));
                return resultat;
            }
        } catch (SQLException e) {
            Logger.error("Erreur de récupération d'une étape de projet. Requête : \n {}", query, e);
        }
        return null;
    }

    public List<ProjetMots> getAllProjetMotsFromStageProjet(StageProjet stageProjet) {
        String query = "SELECT * FROM \"projet_mots\"" +
                "WHERE \"projet_id\" = " + stageProjet.getId() + " " +
                "ORDER BY \"entry_date\" ASC;";
        List<ProjetMots> resultats = new ArrayList<>();
        try {
            ResultSet result = executeQuery(query);
            while (result.next()) {
                resultats.add(new ProjetMots(result.getLong("id"),
                        stageProjet,
                        result.getDate("entry_date").toLocalDate(),
                        result.getLong("nombre_mots"),
                        getDurationFromProjetMots(result.getString("temps_session"))
                ));
            }
        } catch (SQLException e) {
            Logger.error("Erreur de récupération d'objets. Requête : \n {}", query, e);
        }
        return resultats;
    }

    public int getNombreMotsFromProjet(Projet projet) {
        String query = "SELECT SUM(\"nombre_mots\") as mots FROM \"projet_mots\"" +
                "WHERE \"projet_id\" = " + projet.getId() + ";";
        try {
            ResultSet result = executeQuery(query);
            if (result.next())
                return result.getInt("mots");
        } catch (SQLException e) {
            Logger.error("Erreur de récupération d'objets. Requête : \n {}", query, e);
        }
        return 0;
    }

    public Duration getTempsFromProjet(Projet projet) {
        String query = "SELECT SUM(\"temps_session\") as temps FROM \"projet_mots\"" +
                "WHERE \"projet_id\" = " + projet.getId() + ";";
        try {
            ResultSet result = executeQuery(query);
            if (result.next() && !(result.getString("temps") == null))
                return getDurationFromProjetMots(result.getString("temps"));
        } catch (SQLException e) {
            Logger.error("Erreur de récupération d'objets. Requête : \n {}", query, e);
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
            state.execute(Files.readString(Path.of("data/encrier.sql")));
            state.close();
            state = conn.createStatement();
            state.execute(Files.readString(Path.of("data/encrier_constraints.sql")));
            state.close();
            state = conn.createStatement();
            state.execute(Files.readString(Path.of("data/encrier_default_data.sql")));
            state.close();
            conn.close();
        } catch (Exception e) {
            Logger.error("Erreur de création de la BDD ", e);
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
        String query;
        if (type.getId() == null) {
            // Création
            query = "INSERT INTO \"type_projet\" (\"nom\", \"description\")" +
                    "VALUES ('" + replaceApostrophes(type.getNom()) + "', '" +
                    replaceApostrophes(type.getDescription()) + "');";
        } else {
            // Mise à jour
            query = "UPDATE \"type_projet\" " +
                    "SET \"nom\" = '" + replaceApostrophes(type.getNom()) + "', " +
                    "\"description\" = '" + replaceApostrophes(type.getDescription()) + "' " +
                    "WHERE \"id\" = " + type.getId() + ";";
        }
        try {
            executeQuery(query);
            return "";
        } catch (SQLIntegrityConstraintViolationException e) {
            Logger.error("Violation de contrainte SQL en création de type de projet : nom unique. Requête : \n {}", query);
            return "Un type de projet du même nom existe déjà.";
        } catch (SQLException e) {
            Logger.error("Erreur de création ou MAJ d'un type de projet. Requête : \n {}", query, e);
            return "Erreur inconnue.";
        }
    }

    /**
     * Créé un projet si l'id est vide, le met à jour sinon
     * @param projet Le projet à créer ou modifier
     * @return Un texte vide si ok, l'erreur si erreur
     */
    public String createOrUpdateProjet(Projet projet) {
        String query;
        if (projet.getId() == null) {
            // Création
             query = "INSERT INTO \"projet\" (\"type_id\", \"nom\", \"description\") " +
                    "VALUES ('" + projet.getTypeProjet().getId() + "', '" +
                    replaceApostrophes(projet.getNom()) + "', '" +
                    replaceApostrophes(projet.getDescription()) + "');";
        } else {
            // Mise à jour
            query = "UPDATE \"projet\"" +
                    "SET \"type_id\" = '" + projet.getTypeProjet().getId() + "', " +
                    "\"nom\" = '" + replaceApostrophes(projet.getNom()) + "', " +
                    "\"description\" = '" + replaceApostrophes(projet.getDescription()) + "', " +
                    "\"stage_id\" = '" + projet.getStageProjet().getId() + "' " +
                    "WHERE \"id\" = " + projet.getId() + ";";
        }
        try {
            executeQuery(query);
            return "";
        } catch (SQLIntegrityConstraintViolationException e) {
            Logger.error("Violation de contrainte SQL en création de projet : nom unique. Requête : \n {}", query);
            return "Un projet du même nom existe déjà.";
        } catch (SQLException e) {
            Logger.error("Erreur de création ou MAJ d'un projet. Requête : \n {}", query, e);
            return "Erreur inconnue.";
        }
    }

    /**
     * Créé une étape projet si l'id est vide, la met à jour sinon
     * @param etape L'étape projet à créer ou modifier
     * @return Un texte vide si ok, l'erreur si erreur
     */
    public String createOrUpdateStageProjet(StageProjet etape) {
        String query;
        if (etape.getId() == null) {
            // Création
            query = "INSERT INTO \"stage_projet\" (\"projet_id\", \"id_stage\", \"ordre\", \"nom\") " +
                    "VALUES ('" + etape.getProjet().getId() + "', '" +
                    etape.getStage().getId() + "', '" +
                    etape.getOrdre() + "', '" +
                    replaceApostrophes(etape.getNom()) + "');";
        } else {
            // Mise à jour
            query = "UPDATE \"stage_projet\"" +
                    "SET \"projet_id\" = '" + etape.getProjet().getId() + "', " +
                    "\"id_stage\" = '" + etape.getStage().getId() + "', " +
                    "\"ordre\" = '" + etape.getOrdre() + "', " +
                    "\"nom\" = '" + replaceApostrophes(etape.getNom()) + "' " +
                    "WHERE \"id\" = " + etape.getId() + ";";
        }
        try {
            executeQuery(query);
            return "";
        } catch (SQLException e) {
            Logger.error("Erreur de création ou MAJ d'une étape projet. Requête : \n {}", query, e);
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
                "VALUES ('" + session.getStageProjet().getId() + "', '" + session.getEntryDate() + "', '" +
                session.getNombreMots() + "', " +session.getTempsSession().toMinutes() + ");";
        try {
            executeQuery(query);
            return "";
        } catch (SQLException e) {
            Logger.error("Erreur de création d'une session d'écriture. Requête : \n {}", query, e);
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
            Logger.error("Erreur de suppression d'un type de projet. Requête : \n {}", query, e);
            return "Erreur inconnue.";
        }
    }

    /**
     * Suppression d'un projet
     * @param projet Le projet à supprimer
     * @return Un texte vide si ok, l'erreur si erreur
     */
    public String deleteProjet(Projet projet) {
        String query = "DELETE FROM \"projet\"" +
                "WHERE \"id\" = " + projet.getId() + ";";
        try {
            executeQuery(query);
            return "";
        } catch (SQLException e) {
            Logger.error("Erreur de suppression d'un projet. Requête : \n {}", query, e);
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
            Logger.error("Erreur : JDBC non trouvé. Revoir les paramètres d'installation.");
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

    private String replaceApostrophes(String toReplace) {
        return toReplace.replace("'", "''");
    }
}
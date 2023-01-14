package org.niikoneko.encrier.data;

import org.niikoneko.encrier.jpa.Projet;
import org.niikoneko.encrier.jpa.TypeProjet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
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
        try {
            List<TypeProjet> resultat = new ArrayList<TypeProjet>();
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection conn = DriverManager.getConnection(
                    BddInfos.bddUrl + ";ifexists=true", BddInfos.bddUser, BddInfos.bddPass);
            Statement state = conn.createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM \"type_projet\";");
            while (result.next()) {
                resultat.add(new TypeProjet(result.getLong("id"),
                        result.getString("nom"),
                        result.getString("description")));
            }
            state.close();
            conn.close();
            return resultat;
        } catch (Exception e) {
            logger.error("Erreur de récupération d'objets ", e);
        }
        return null;
    }

    /**
     * Récupère un type de projet à partir de son id
     * @param id L'id du type de projet
     * @return Le TypeProjet associé
     */
    public TypeProjet getTypeProjetFromId(long id) {
        try {
            TypeProjet resultat;
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection conn = DriverManager.getConnection(
                    BddInfos.bddUrl + ";ifexists=true", BddInfos.bddUser, BddInfos.bddPass);
            Statement state = conn.createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM \"type_projet\" WHERE \"id\" = " + id + ";");
            result.next();
            resultat = new TypeProjet(result.getLong("id"),
                    result.getString("nom"),
                    result.getString("description"));
            state.close();
            conn.close();
            return resultat;
        } catch (Exception e) {
            logger.error("Erreur de récupération d'un type de projet ", e);
        }
        return null;
    }

    /**
     * Récupère tous les projets existants en BDD
     * @return La liste des projets
     */
    public List<Projet> getAllProjets() {
        try {
            List<Projet> resultat = new ArrayList<Projet>();
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection conn = DriverManager.getConnection(
                    BddInfos.bddUrl + ";ifexists=true", BddInfos.bddUser, BddInfos.bddPass);
            Statement state = conn.createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM \"projet\";");
            while (result.next()) {
                TypeProjet type = getTypeProjetFromId(result.getLong("type_id"));
                resultat.add(new Projet(result.getLong("id"),
                        type,
                        result.getString("nom"),
                        result.getString("description"),
                        result.getBoolean("archive")));
            }
            state.close();
            conn.close();
            return resultat;
        } catch (Exception e) {
            logger.error("Erreur de récupération d'objets ", e);
        }
        return null;
    }

    /**
     * Récupère tous les projets associés à un type donné
     * @param type Le type de projet
     * @return La liste des projets associés au type
     */
    public List<Projet> getAllProjetsFromType(TypeProjet type) {
        try {
            List<Projet> resultat = new ArrayList<Projet>();
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection conn = DriverManager.getConnection(
                    BddInfos.bddUrl + ";ifexists=true", BddInfos.bddUser, BddInfos.bddPass);
            Statement state = conn.createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM \"projet\"" +
                    "WHERE \"type_id\" = " + type.getId() + ";");
            while (result.next()) {
                resultat.add(new Projet(result.getLong("id"),
                        type,
                        result.getString("nom"),
                        result.getString("description"),
                        result.getBoolean("archive")));
            }
            state.close();
            conn.close();
            return resultat;
        } catch (Exception e) {
            logger.error("Erreur de récupération d'objets ", e);
        }
        return null;
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
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection conn = DriverManager.getConnection(
                    BddInfos.bddUrl + ";ifexists=true", BddInfos.bddUser, BddInfos.bddPass);
            Statement state = conn.createStatement();
            if (type.getId() == null) {
                // Création
                state.executeQuery("INSERT INTO \"type_projet\" (\"nom\", \"description\")" +
                    "VALUES ('" + type.getNom() + "', '" + type.getDescription() + "');");
            } else {
                // Mise à jour
                state.executeQuery("UPDATE \"type_projet\" " +
                    "SET \"nom\" = '" + type.getNom() + "', " +
                    "\"description\" = '" + type.getDescription() + "' " +
                    "WHERE \"id\" = " + type.getId() + ";");
            }
            state.close();
            conn.close();
            return "";
        } catch (SQLIntegrityConstraintViolationException e) {
            logger.error("Violation de contrainte SQL en création de type de projet : nom unique");
            return "Un type de projet du même nom existe déjà.";
        } catch (Exception e) {
            logger.error("Erreur de création ou MAJ d'un type de projet", e);
            return "Erreur inconnue.";
        }
    }

    /**
     * Créé un projet si l'id est vide, le met à jour sinon
     * @param projet Le projet à créer ou modifier
     * @return Un texte vide si ok, l'erreur si erreur
     */
    public String createOrUpdateProjet(Projet projet) {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection conn = DriverManager.getConnection(
                    BddInfos.bddUrl + ";ifexists=true", BddInfos.bddUser, BddInfos.bddPass);
            Statement state = conn.createStatement();
            if (projet.getId() == null) {
                // Création
                state.executeQuery("INSERT INTO \"projet\" (\"type_id\", \"nom\", \"description\", \"archive\")" +
                        "VALUES ('" + projet.getTypeProjet().getId() + "', '" + projet.getNom() + "', '" +
                        projet.getDescription() + "', '" + projet.getAchive() + "');");
            } else {
                // Mise à jour
                state.executeQuery("UPDATE \"projet\"" +
                        "SET \"type_id\" = '" + projet.getTypeProjet().getId() + "', " +
                        "\"nom\" = '" + projet.getNom() + "', " +
                        "\"description\" = '" + projet.getDescription() + "', " +
                        "\"archive\" = '" + projet.getAchive() + "' " +
                        "WHERE \"id\" = " + projet.getId() + ";");
            }
            state.close();
            conn.close();
            return "";
        } catch (SQLIntegrityConstraintViolationException e) {
            logger.error("Violation de contrainte SQL en création de projet : nom unique");
            return "Un projet du même nom existe déjà.";
        } catch (Exception e) {
            logger.error("Erreur de création ou MAJ d'un projet", e);
            return "Erreur inconnue.";
        }
    }

    /**
     * Suppression d'un type de projet
     * @param type Le type à supprimer
     * @return Un texte vide si ok, l'erreur si erreur
     */
    public String deleteTypeProjet(TypeProjet type) {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection conn = DriverManager.getConnection(
                    BddInfos.bddUrl + ";ifexists=true", BddInfos.bddUser, BddInfos.bddPass);
            Statement state = conn.createStatement();
            state.executeQuery("DELETE FROM \"type_projet\"" +
                    "WHERE \"id\" = " + type.getId() + ";");
            state.close();
            conn.close();
            return "";
        } catch (Exception e) {
            logger.error("Erreur de création ou MAJ d'un type de projet", e);
            return "Erreur inconnue.";
        }
    }
}
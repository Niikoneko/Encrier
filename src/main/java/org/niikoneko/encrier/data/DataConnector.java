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
     * Création de la base de données (premier lancement)
     * @return true si la création est OK, false sinon
     */
    public boolean bddInstall() {
        // Création de la BDD
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
            conn.close();
        } catch (Exception e) {
            logger.error("Erreur de création de la BDD ", e);
            return false;
        }
        // Création des types par défaut TODO Passer par un script SQL
        boolean ok = createTypeProjet("Roman", "Un manuscrit de roman, organisé en chapitres.").isEmpty();
        if (ok)
            ok = createTypeProjet("Nouvelle", "Un manuscrit court, sans forcement de chapitres.").isEmpty();
        return ok;
    }

    /**
     * Création d'un projet
     * @param type Le type du projet
     * @param nom Le nom de projet
     * @param description Une brève description, optionnelle
     * @return true si la création a réussi, false sinon
     */
    public String createProjet(TypeProjet type, String nom, String description) {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection conn = DriverManager.getConnection(
                    BddInfos.bddUrl + ";create=true", BddInfos.bddUser, BddInfos.bddPass);
            Statement state = conn.createStatement();
            state.executeQuery("INSERT INTO \"projet\" (\"type_id\", \"nom\", \"description\")" +
                    "VALUES ('" + type.getId() + "', '" + nom + "', '" + description + "');");
            state.close();
            conn.close();
            return "";
        } catch (SQLIntegrityConstraintViolationException e) {
            logger.error("Violation de contrainte SQL en création de projet : nom unique");
            return "Un projet du même nom existe déjà.";
        } catch (Exception e) {
            logger.error("Erreur de création d'un projet", e);
            return "Erreur inconnue.";
        }
    }

    /**
     * Création d'un type de projet
     * @param nom Le nom du type
     * @param description Une brève description
     * @return true si la création a réussi, false sinon
     */
    public String createTypeProjet(String nom, String description) {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection conn = DriverManager.getConnection(
                    BddInfos.bddUrl + ";create=true", BddInfos.bddUser, BddInfos.bddPass);
            Statement state = conn.createStatement();
            state.executeQuery("INSERT INTO \"type_projet\" (\"nom\", \"description\")" +
                    "VALUES ('" + nom + "', '" + description + "');");
            state.close();
            conn.close();
            return "";
        } catch (SQLIntegrityConstraintViolationException e) {
            logger.error("Violation de contrainte SQL en création de projet : nom unique");
            return "Un type de projet du même nom existe déjà.";
        } catch (Exception e) {
            logger.error("Erreur de création d'un type de projet", e);
            return "Erreur inconnue.";
        }
    }
}
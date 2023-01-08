package org.niikoneko.encrier.jpa;

import java.sql.Time;
import java.util.Date;


public class ProjetMots {

    public static final String table = "projet_mots";

    private Long id;

    private Projet projet;

    private Date entryDate;

    private Long nombreMots;

    private Time tempsSession;

    public Time getTempsSession() {
        return tempsSession;
    }

    public void setTempsSession(Time tempsSession) {
        this.tempsSession = tempsSession;
    }

    public Long getNombreMots() {
        return nombreMots;
    }

    public void setNombreMots(Long nombreMots) {
        this.nombreMots = nombreMots;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
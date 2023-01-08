package org.niikoneko.encrier.jpa;

import java.util.Date;


public class ProjetChapitres {

    public static final String table = "projet_chapitres";

    private Long id;

    private Projet projet;

    private Chapitre chapitre;

    private Date finishDate;

    private Long nombreMots;

    public Long getNombreMots() {
        return nombreMots;
    }

    public void setNombreMots(Long nombreMots) {
        this.nombreMots = nombreMots;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finish_date) {
        this.finishDate = finish_date;
    }

    public Chapitre getChapitre() {
        return chapitre;
    }

    public void setChapitre(Chapitre chapitre) {
        this.chapitre = chapitre;
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
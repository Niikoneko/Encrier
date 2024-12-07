package org.niikoneko.encrier.jpa;

import java.util.Date;


public class ProjetChapitres {

    private Long id;

    private StageProjet etapeProjet;

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

    public StageProjet getStageProjet() {
        return etapeProjet;
    }

    public void setStageProjet(StageProjet etapeProjet) {
        this.etapeProjet = etapeProjet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
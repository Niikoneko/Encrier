package org.niikoneko.encrier.jpa;

public class BetaLecteur {

    private Long id;

    private StageProjet stageProjet;

    private String nom;

    private BLStatus status;

    public Long getId() {
        return id;
    }

    public StageProjet getStageProjet() {
        return stageProjet;
    }

    public String getNom() {
        return nom;
    }

    public BetaLecteur setNom(String nom) {
        this.nom = nom;
        return this;
    }

    public BLStatus getStatus() {
        return status;
    }

    public BetaLecteur setStatus(BLStatus status) {
        this.status = status;
        return this;
    }
}
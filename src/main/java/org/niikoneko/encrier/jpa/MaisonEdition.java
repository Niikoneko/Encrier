package org.niikoneko.encrier.jpa;

import java.time.LocalDate;

public class MaisonEdition {

    private Long id;

    private StageProjet stageProjet;

    private String nom;

    private boolean soumissionOuverte;

    private String coordonnees;

    private LocalDate deadline;

    private MEStatus status;

    public Long getId() {
        return id;
    }

    public StageProjet getStageProjet() {
        return stageProjet;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public boolean isSoumissionOuverte() {
        return soumissionOuverte;
    }

    public void setSoumissionOuverte(boolean soumissionOuverte) {
        this.soumissionOuverte = soumissionOuverte;
    }

    public String getCoordonnees() {
        return coordonnees;
    }

    public void setCoordonnees(String coordonnees) {
        this.coordonnees = coordonnees;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public MEStatus getStatus() {
        return status;
    }

    public void setStatus(MEStatus status) {
        this.status = status;
    }
}
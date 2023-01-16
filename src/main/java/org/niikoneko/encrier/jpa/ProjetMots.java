package org.niikoneko.encrier.jpa;

import java.time.LocalDate;
import java.time.LocalTime;


public class ProjetMots {

    public ProjetMots(long id, Projet projet, LocalDate entryDate, long nombreMots, LocalTime tempsSession) {
        this.id = id;
        this.projet = projet;
        this.entryDate = entryDate;
        this.nombreMots = nombreMots;
        this.tempsSession = tempsSession;
    }

    /**
     * Constructeur pré-création
     * @param projet Le projet associé
     * @param entryDate La date de la session d'écriture
     * @param nombreMots Le nombre de mots ajoutés
     * @param tempsSession Le temps de la session
     */
    public ProjetMots(Projet projet, LocalDate entryDate, long nombreMots, LocalTime tempsSession) {
        this.projet = projet;
        this.entryDate = entryDate;
        this.nombreMots = nombreMots;
        this.tempsSession = tempsSession;
    }

    private Long id;

    private Projet projet;

    private LocalDate entryDate;

    private Long nombreMots;

    private LocalTime tempsSession;

    public LocalTime getTempsSession() {
        return tempsSession;
    }

    public void setTempsSession(LocalTime tempsSession) {
        this.tempsSession = tempsSession;
    }

    public Long getNombreMots() {
        return nombreMots;
    }

    public void setNombreMots(Long nombreMots) {
        this.nombreMots = nombreMots;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
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
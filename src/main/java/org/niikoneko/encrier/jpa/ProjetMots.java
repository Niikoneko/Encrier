package org.niikoneko.encrier.jpa;

import java.time.Duration;
import java.time.LocalDate;


public class ProjetMots {

    public ProjetMots(long id, StageProjet etape, LocalDate entryDate, long nombreMots, Duration tempsSession) {
        this.id = id;
        this.etape = etape;
        this.entryDate = entryDate;
        this.nombreMots = nombreMots;
        this.tempsSession = tempsSession;
    }

    /**
     * Constructeur pré-création
     * @param etape L'étape projet associée
     * @param entryDate La date de la session d'écriture
     * @param nombreMots Le nombre de mots ajoutés
     * @param tempsSession Le temps de la session
     */
    public ProjetMots(StageProjet etape, LocalDate entryDate, long nombreMots, Duration tempsSession) {
        this.etape = etape;
        this.entryDate = entryDate;
        this.nombreMots = nombreMots;
        this.tempsSession = tempsSession;
    }

    private Long id;

    private StageProjet etape;

    private LocalDate entryDate;

    private Long nombreMots;

    private Duration tempsSession;

    public Duration getTempsSession() {
        return tempsSession;
    }

    public void setTempsSession(Duration tempsSession) {
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

    public StageProjet getStageProjet() {
        return etape;
    }

    public void setStageProjet(StageProjet etape) {
        this.etape = etape;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
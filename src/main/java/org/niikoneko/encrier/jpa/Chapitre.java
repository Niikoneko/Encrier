package org.niikoneko.encrier.jpa;

public class Chapitre {

    public Chapitre(Long id, Projet projet, String type, String titre, Integer ordre, String notes) {
        this.id = id;
        this.projet = projet;
        this.chapitreType = type;
        this.titre = titre;
        this.ordre = ordre;
        this.notes = notes;
    }

    private Long id;

    private Projet projet;

    private String chapitreType;

    private String titre;

    private Integer ordre;

    private String notes;

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getChapitreType() {
        return chapitreType;
    }

    public void setChapitreType(String chapitreType) {
        this.chapitreType = chapitreType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
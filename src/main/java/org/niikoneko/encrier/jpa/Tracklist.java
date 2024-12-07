package org.niikoneko.encrier.jpa;

public class Tracklist {

    private Long id;

    private StageProjet stageProjet;

    private Chapitre chapitre;

    private boolean cochable;

    private boolean coche;

    private String categorie;

    private String description;

    public Long getId() {
        return id;
    }

    public StageProjet getStageProjet() {
        return stageProjet;
    }

    public Chapitre getChapitre() {
        return chapitre;
    }

    public boolean isCochable() {
        return cochable;
    }

    public Tracklist setCochable(boolean cochable) {
        this.cochable = cochable;
        return this;
    }

    public boolean isCoche() {
        return coche;
    }

    public Tracklist setCoche(boolean coche) {
        this.coche = coche;
        return this;
    }

    public String getCategorie() {
        return categorie;
    }

    public Tracklist setCategorie(String categorie) {
        this.categorie = categorie;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Tracklist setDescription(String description) {
        this.description = description;
        return this;
    }
}
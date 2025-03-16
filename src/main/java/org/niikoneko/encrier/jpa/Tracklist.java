package org.niikoneko.encrier.jpa;

public class Tracklist {

    public Tracklist(Long id, StageProjet stage, Chapitre chapitre, boolean cochable, boolean coche, String categorie,
                     String description) {
        this.id = id;
        this.stageProjet = stage;
        this.chapitre = chapitre;
        this.cochable = cochable;
        this.coche = coche;
        this.categorie = categorie;
        this.description = description;
    }

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
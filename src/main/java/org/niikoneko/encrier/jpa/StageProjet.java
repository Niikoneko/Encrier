package org.niikoneko.encrier.jpa;

public class StageProjet {

    public StageProjet(Long id, Projet projet, Stage stage, Integer ordre, String nom) {
        this.id = id;
        this.projet = projet;
        this.stage = stage;
        this.ordre = ordre;
        this.nom = nom;
    }

    /**
     * Constructeur pré-création
     * @param projet Le projet portant l'étape
     * @param stage Le type d'étape associé
     * @param ordre L'ordre de l'étape pour le projet
     * @param nom Le nom de l'étape
     */
    public StageProjet(Projet projet, Stage stage, Integer ordre, String nom) {
        this.projet = projet;
        this.stage = stage;
        this.ordre = ordre;
        this.nom = nom;
    }

    private Long id;

    private Projet projet;

    private Stage stage;

    private Integer ordre;

    private String nom;

    public Projet getProjet() {
        return projet;
    }

    public Stage getStage() {
        return stage;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public String getNom() {
        return nom;
    }

    public StageProjet setNom(String nom) {
        this.nom = nom;
        return this;
    }

    public Long getId() {
        return id;
    }

    public StageProjet setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return nom;
    }
}
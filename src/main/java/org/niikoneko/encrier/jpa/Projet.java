package org.niikoneko.encrier.jpa;

public class Projet {

    public Projet(Long id, TypeProjet type, String nom, String description, StageProjet etape) {
        this.id = id;
        this.typeProjet = type;
        this.nom = nom;
        this.description = description;
        this.etape = etape;
    }

    /**
     * Constructeur pré-création
     * @param type le type de projet
     * @param nom Le nom du projet
     * @param description Une description du projet
     */
    public Projet(TypeProjet type, String nom, String description, StageProjet etape) {
        this.typeProjet = type;
        this.nom = nom;
        this.description = description;
        this.etape = etape;
    }

    private Long id;


    private TypeProjet typeProjet;

    private String nom;

    private String description;

    private StageProjet etape;

    public StageProjet getStageProjet() {
        return etape;
    }

    public Projet setStageProjet(StageProjet etape) {
        this.etape = etape;
        return this;
    }

    public TypeProjet getTypeProjet() {
        return typeProjet;
    }

    public Projet setTypeProjet(TypeProjet typeProjet) {
        this.typeProjet = typeProjet;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Projet setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getNom() {
        return nom;
    }

    public Projet setNom(String nom) {
        this.nom = nom;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Projet setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return nom;
    }
}
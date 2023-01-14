package org.niikoneko.encrier.jpa;

public class Projet {

    public Projet(Long id, TypeProjet type, String nom, String description, boolean archive) {
        this.id = id;
        this.typeProjet = type;
        this.nom = nom;
        this.description = description;
        this.archive = archive;
    }

    /**
     * Constructeur pré-création
     * @param type le type de projet
     * @param nom Le nom du projet
     * @param description Une description du projet
     */
    public Projet(TypeProjet type, String nom, String description) {
        this.typeProjet = type;
        this.nom = nom;
        this.description = description;
        this.archive = false;
    }

    public static final String table = "projet";

    private Long id;


    private TypeProjet typeProjet;

    private String nom;

    private String description;

    private Boolean archive;

    public Boolean getAchive() {
        return archive;
    }

    public Projet setAchive(Boolean achive) {
        this.archive = achive;
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
package org.niikoneko.encrier.jpa;

public class TypeProjet {

    public TypeProjet(Long id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
    }

    /**
     * Constructeur pré-création. Utilité à confirmer.
     * @param nom Le nom du type
     * @param description La description du type
     */
    public TypeProjet(String nom, String description) {
        this.id = (long) -1;
        this.nom = nom;
        this.description = description;
    }

    public static final String table = "type_projet";

    private Long id;

    private String nom;

    private String description;

    public String getDescription() {
        return description;
    }

    public TypeProjet setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getNom() {
        return nom;
    }

    public TypeProjet setNom(String nom) {
        this.nom = nom;
        return this;
    }

    public Long getId() {
        return id;
    }

    public TypeProjet setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return nom;
    }
}
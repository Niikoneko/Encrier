package org.niikoneko.encrier.jpa;

import org.niikoneko.encrier.data.TypesEtapes;

public class Stages {

    public Stages(Long id, String nom, TypesEtapes type, String description) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.description = description;
    }

    /**
     * Constructeur pré-création
     * @param nom Le nom de l'étape
     * @param type Le type d'étape
     * @param description La description de l'étape
     */
    public Stages(String nom, TypesEtapes type, String description) {
        this.nom = nom;
        this.type = type;
        this.description = description;
    }

    private Long id;

    private String nom;

    private TypesEtapes type;

    private String description;

    public String getDescription() {
        return description;
    }

    public Stages setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getNom() {
        return nom;
    }

    public Stages setNom(String nom) {
        this.nom = nom;
        return this;
    }

    public String getType() {
        return type.getDbValue();
    }

    public Stages setType(TypesEtapes type) {
        this.type = type;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Stages setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return nom;
    }
}
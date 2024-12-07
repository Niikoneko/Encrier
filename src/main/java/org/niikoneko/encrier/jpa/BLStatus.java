package org.niikoneko.encrier.jpa;

public class BLStatus {

    private Long id;

    private String nom;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public BLStatus setNom(String nom) {
        this.nom = nom;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public BLStatus setDescription(String description) {
        this.description = description;
        return this;
    }
}
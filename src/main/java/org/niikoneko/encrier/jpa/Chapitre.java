package org.niikoneko.encrier.jpa;

public class Chapitre {

    public static final String table = "chapitre";

    private Long id;

    private Projet projet;

    private String chapitreType;

    private String titre;

    private Integer ordre;

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
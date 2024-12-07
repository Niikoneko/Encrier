package org.niikoneko.encrier.jpa;

public class MEDossier {

    /**
     *
     "id" BIGINT PRIMARY KEY,
     "id_me" BIGINT NOT NULL,
     "type" VARCHAR(64) NOT NULL,
     "element" VARCHAR(255) NOT NULL,
     "coche" BOOLEAN NOT NULL
     */

    private Long id;

    private MaisonEdition maisonEdition;

    private String type;

    private String element;

    private boolean coche;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MaisonEdition getMaisonEdition() {
        return maisonEdition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public boolean isCoche() {
        return coche;
    }

    public void setCoche(boolean coche) {
        this.coche = coche;
    }
}
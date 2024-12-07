package org.niikoneko.encrier.data;

public enum TypesEtapes {
    SUIVI ("Suivi"),
    ECRITURE("Ecriture"),
    ECRITURE_ET_SUIVI("Ecriture et suivi"),
    BETA_LECTURE("Beta-lecture"),
    ENVOI_ME("Envoi ME");

    private final String dbValue;

    private TypesEtapes(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return this.dbValue;
    }

    public static TypesEtapes fromString(String value) {
        return switch (value) {
            case "Suivi" -> SUIVI;
            case "Ecriture" -> ECRITURE;
            case "Ecriture et suivi" -> ECRITURE_ET_SUIVI;
            case "Beta-lecture" -> BETA_LECTURE;
            case "Envoi ME" -> ENVOI_ME;
            default -> null;
        };
    }
}

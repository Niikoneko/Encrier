package org.niikoneko.encrier.jpa;

public class ReponseBL {

    private Long id;

    private BetaLecteur betaLecteur;

    private BLQuestion question;

    private String reponse;

    public Long getId() {
        return id;
    }

    public BetaLecteur getBetaLecteur() {
        return betaLecteur;
    }

    public BLQuestion getQuestion() {
        return question;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }
}
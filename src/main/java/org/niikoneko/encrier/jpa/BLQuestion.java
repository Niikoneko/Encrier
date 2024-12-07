package org.niikoneko.encrier.jpa;

public class BLQuestion {

    private Long id;

    private StageProjet stageProjet;

    private String question;

    public Long getId() {
        return id;
    }

    public StageProjet getStageProjet() {
        return stageProjet;
    }

    public String getQuestion() {
        return question;
    }

    public BLQuestion setQuestion(String question) {
        this.question = question;
        return this;
    }
}
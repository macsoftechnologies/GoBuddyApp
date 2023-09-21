package com.colourmoon.gobuddy.model;

public class TutorialModel {
    private String tutorialYoutubeId;
    private String tutorialTitle;
    private String tutorialDesc;

    public TutorialModel(String tutorialYoutubeId, String tutorialTitle, String tutorialDesc) {
        this.tutorialYoutubeId = tutorialYoutubeId;
        this.tutorialTitle = tutorialTitle;
        this.tutorialDesc = tutorialDesc;
    }

    public String getTutorialYoutubeId() {
        return tutorialYoutubeId;
    }

    public String getTutorialTitle() {
        return tutorialTitle;
    }

    public String getTutorialDesc() {
        return tutorialDesc;
    }
}

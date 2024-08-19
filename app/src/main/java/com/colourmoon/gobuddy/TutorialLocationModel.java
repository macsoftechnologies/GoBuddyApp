package com.colourmoon.gobuddy;

public class TutorialLocationModel {
    private String video_link;
    private String title;
    private String description;


    public TutorialLocationModel(String video_link, String title, String description) {
        this.video_link = video_link;
        this.title = title;
        this.description = description;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




}

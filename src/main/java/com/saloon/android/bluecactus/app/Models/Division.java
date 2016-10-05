package com.saloon.android.bluecactus.app.Models;

/**
 * Created by usman on 10/4/16.
 */
public class Division {

    String title;
    String description;
    String image_url;

    public Division() {
    }

    public Division(String title, String image_url, String description) {
        this.title = title;
        this.image_url = image_url;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

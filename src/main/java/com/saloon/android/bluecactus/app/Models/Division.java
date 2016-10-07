package com.saloon.android.bluecactus.app.Models;

/**
 * Created by usman on 10/4/16.
 */
public class Division {

    String title;
    String description;
    String image_url;
    String image_url_thumb;
    String image_url_medium;

    public Division() {
    }

    public Division(String title, String description, String image_url, String image_url_thumb, String image_url_medium) {
        this.title = title;
        this.description = description;
        this.image_url = image_url;
        this.image_url_thumb = image_url_thumb;
        this.image_url_medium = image_url_medium;
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

    public String getImage_url_thumb() {
        return image_url_thumb;
    }

    public void setImage_url_thumb(String image_url_thumb) {
        this.image_url_thumb = image_url_thumb;
    }

    public String getImage_url_medium() {
        return image_url_medium;
    }

    public void setImage_url_medium(String image_url_medium) {
        this.image_url_medium = image_url_medium;
    }
}

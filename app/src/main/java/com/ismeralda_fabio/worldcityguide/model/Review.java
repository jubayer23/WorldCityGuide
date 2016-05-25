package com.ismeralda_fabio.worldcityguide.model;

/**
 * Created by comsol on 10/28/2015.
 */
public class Review {
    String author_name;
    String text;
    String rating;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

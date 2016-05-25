package com.ismeralda_fabio.worldcityguide.model;

/**
 * Created by comsol on 03-Dec-15.
 */
public class FavouritePlace {
    public String title;
    public String full_address;
    public String photo_ref;
    public String lat;
    public String lng;
    public String reference;
    public String place_id;

    public FavouritePlace(String title, String full_address, String photo_ref, String lat, String lng, String reference, String place_id) {
        this.title = title;
        this.full_address = full_address;
        this.photo_ref = photo_ref;
        this.lat = lat;
        this.lng = lng;
        this.reference = reference;
        this.place_id = place_id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getPhoto_ref() {
        return photo_ref;
    }

    public void setPhoto_ref(String photo_ref) {
        this.photo_ref = photo_ref;
    }


    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }


    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return "FavouritePlace{" +
                "title='" + title + '\'' +
                ", full_address='" + full_address + '\'' +
                ", photo_ref='" + photo_ref + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", reference='" + reference + '\'' +
                ", place_id='" + place_id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        FavouritePlace other = (FavouritePlace) o;
        if(this.title.equals(other.title)
                && this.full_address.equals(other.full_address)
                && this.lat.equals(other.lat)
                && this.lng.equals(other.lng)
                ){
            return true;
        }else{
            return false;
        } //To change body of generated methods, choose Tools | Templates.
    }
}

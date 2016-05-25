package com.ismeralda_fabio.worldcityguide.jsonParser;

import com.ismeralda_fabio.worldcityguide.model.Place;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by comsol on 11/2/2015.
 */
public class PlaceJSONParser {

    private ArrayList<Place> Places;
    Gson gson;

    public PlaceJSONParser()
    {
        gson = new Gson();
    }

    public List<Place> parseJsonFeed(JSONObject response) {
        try {
            JSONArray jsonArPak = response.getJSONArray("results");

            for (int i = 0; i < jsonArPak.length(); i++) {
                JSONObject tempObject = jsonArPak.getJSONObject(i);

                Place place = gson.fromJson(tempObject.toString(), Place.class);


                try {
                    JSONArray photosJSONArray = tempObject.getJSONArray("photos");
                    JSONObject photoObj = photosJSONArray.getJSONObject(0);

                    place.setPhoto_ref(photoObj.getString("photo_reference"));

                } catch (Exception e) {

                    place.setPhoto_ref("CmRdAAAAraBKakPfbTmnRF7ol45oAFr5CbJO-ImcOwh2TcUJ0JtsdvE1FjGn0wQLIl_aJroqY1mJmD1E6QY4OjQz8Zu-hkKW82QEKJ6l1aspv7V40co7kE-_PT8pg7EsB__c9cpYEhD43c9maYnXlwL1D1LAiAyIGhSZyBdwqAXtkaaAMoCJXO9EqUjp9w&sensor=true&key=AIzaSyCHsF72opxJ7MfM5dqq4z_-2ujjujukI3E");
                    e.printStackTrace();
                }

                Places.add(place);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Places;
    }
}

package com.ismeralda_fabio.worldcityguide.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.ismeralda_fabio.worldcityguide.model.FavouritePlace;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SaveManager {

    // Shared Preferences
    private static SharedPreferences mSharedPreferences;

    private static final int PRIVATE_MODE = Context.MODE_PRIVATE;
    private static final String PREF_NAME = "com.ismeralda_fabio.worldcityguide";
    private static final String KEY_SEARCH_COUNTER = "search_counter";
    private static final String KEY_ICON_COUNTER = "icon_counter";

    private static final String KEY_USER_LAT = "user_lat";
    private static final String KEY_USER_LNG = "user_lng";
    private static final String KEY_API_COUNTER = "api_counter";



    private SharedPreferences.Editor editor;
    private Context context;

    public SaveManager(Context context) {
        this.context = context;
        mSharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = mSharedPreferences.edit();
        editor.apply();
    }

    private SharedPreferences getSharedPreferences(final String prefName,
                                                   final int mode) {
        return this.context.getSharedPreferences(prefName, mode);
    }

    public void setLat(String value) {
        editor.putString(KEY_USER_LAT, value);
        editor.apply();
    }

    public Double getLat() {
        return Double.parseDouble(mSharedPreferences.getString(KEY_USER_LAT, ""));
    }

    public void setLng(String value) {
        editor.putString(KEY_USER_LNG, value);
        editor.apply();
    }

    public Double getLng() {
        return Double.parseDouble(mSharedPreferences.getString(KEY_USER_LNG, ""));
    }


    public void setApiCounter(int value) {
        editor.putInt(KEY_API_COUNTER, value);
        editor.apply();
    }

    public int getApiCounter() {
        return mSharedPreferences.getInt(KEY_API_COUNTER, 1);
    }

    public void setSearchCounter(int value) {
        editor.putInt(KEY_SEARCH_COUNTER, value);
        editor.apply();
    }

    public Integer getSearchCounter() {
        return mSharedPreferences.getInt(KEY_SEARCH_COUNTER, 0);
    }

    public void setIconCounter(int value) {
        editor.putInt(KEY_ICON_COUNTER, value);
        editor.apply();
    }

    public Integer getIconCounter() {
        return mSharedPreferences.getInt(KEY_ICON_COUNTER, 0);
    }

    public void setFavPlaces(ArrayList<FavouritePlace> list){
        //editor = pref.edit();
        editor.putInt("Count",  list.size());
        int count = 0;
        for (FavouritePlace i : list){

            Gson gson = new Gson();
            String json = gson.toJson(i); // myObject - instance of MyObject

            editor.putString("favouriteplace_" + count++, json);
        }

        editor.commit();
    }
    public ArrayList<FavouritePlace> getFavPlaces(){
        ArrayList<FavouritePlace> temp = new ArrayList<FavouritePlace>();

        int count = mSharedPreferences.getInt("Count", 0);
        temp.clear();
        for (int i = 0; i < count; i++){

            Gson gson = new Gson();
            String json = mSharedPreferences.getString("favouriteplace_" + i, "");
            FavouritePlace obj = gson.fromJson(json, FavouritePlace.class);
            temp.add(obj);


            //temp.add(mSharedPreferences.getString("favouriteplace_" + i, ""));
        }
        return temp;
    }
}
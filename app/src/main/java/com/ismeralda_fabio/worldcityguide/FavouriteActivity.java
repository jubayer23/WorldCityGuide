package com.ismeralda_fabio.worldcityguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ismeralda_fabio.worldcityguide.adapter.FavListAdapter;
import com.ismeralda_fabio.worldcityguide.appdata.AppConstant;
import com.ismeralda_fabio.worldcityguide.dialog.AlertDialogManager;
import com.ismeralda_fabio.worldcityguide.model.FavouritePlace;
import com.ismeralda_fabio.worldcityguide.sharedprefs.SaveManager;
import com.ismeralda_fabio.worldcityguide.utils.ConnectionDetector;
import com.ismeralda_fabio.worldcityguide.utils.GPSTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/**
 * Created by comsol on 03-Dec-15.
 */
public class FavouriteActivity extends AppCompatActivity {

    ListView listView;

    FavListAdapter favListAdapter;

    public static  ArrayList<FavouritePlace> favouritePlaces;

    SaveManager saveData;

    ConnectionDetector cd;

    GPSTracker gps;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        loadAdView();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (!cd.isConnectingToInternet()) {
                    //Internet Connection is not present
                    alert.showAlertDialog(FavouriteActivity.this, "Internet Connection Error",
                            "Please connect to working Internet connection", false);
                    //stop executing code by return
                    return;
                }
                gps = new GPSTracker(FavouriteActivity.this);
                if (!gps.canGetLocation()) {
                    alert.showAlertDialog(FavouriteActivity.this, "GPS Status",
                            "Couldn't get location information. Please enable GPS",
                            false);
                    //stop executing code by return
                    return;
                }

                if (cd.isConnectingToInternet() && gps.canGetLocation()) {
                    FavouritePlace favPlace = favouritePlaces.get(position);
                    String reference = favPlace.getReference();
                    String photo_reference = favPlace.getPhoto_ref();
                    AppConstant.des_lat = favPlace.getLat();
                    AppConstant.des_lng = favPlace.getLng();
                    String des_location = favPlace.getLat() + "," + favPlace.getLng();


                    //Toast.makeText(PlaceList.this, reference, Toast.LENGTH_SHORT).show();

                    // Log.d("DEBUG", reference);

                    //Log.d("DEBUG loc 2", String.valueOf(place.getGeometry().getLocation().getLat()) + "," + String.valueOf(place.getGeometry().getLocation().getLng()));

                    // Starting new intent
                    Intent in = new Intent(getApplicationContext(),
                            PlaceDetails.class);

                    // Sending place refrence id to single place activity
                    // place refrence id used to get "Place full details"
                    in.putExtra("ref", reference);
                    in.putExtra("place_id", favPlace.getPlace_id());
                    in.putExtra("photo_ref", photo_reference);
                    in.putExtra("des_location", des_location);
                    in.putExtra("api_counter", saveData.getApiCounter());


                    startActivity(in);
                }
                // getting values from selected ListItem


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        favouritePlaces = saveData.getFavPlaces();

        favListAdapter = new FavListAdapter(
                FavouriteActivity.this, favouritePlaces);
        listView.setAdapter(favListAdapter);

    }

    private void init() {

        listView = (ListView) findViewById(R.id.place_list_fav);

        saveData = new SaveManager(this);

        cd = new ConnectionDetector(this);

        gps = new GPSTracker(this);
    }

    public void loadAdView() {
        AdView mAdView = (AdView) findViewById(R.id.adViewFav);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // load INTERTITIAL ADD VIEW


    }
}

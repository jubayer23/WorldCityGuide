package com.ismeralda_fabio.worldcityguide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ismeralda_fabio.worldcityguide.appdata.AppConstant;
import com.ismeralda_fabio.worldcityguide.appdata.AppController;
import com.ismeralda_fabio.worldcityguide.model.FavouritePlace;
import com.ismeralda_fabio.worldcityguide.model.PlaceDetail;
import com.ismeralda_fabio.worldcityguide.sharedprefs.SaveManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by comsol on 10/27/2015.
 */
public class PlaceDetails extends Activity implements View.OnClickListener {

    private RelativeLayout networkImageView;
    private TextView address, phone, walk, cycle, sbus, distanceTv, title, web_address;

    private String photo_ref, image_url, placeDetails_url, des_location, driving_url, cycle_url, walk_url, place_reference;


    private ProgressBar pBar;

    private Bitmap bmImg;

    private Gson gson;

    private PlaceDetail placeDetail;


    private ImageView showInMap, favIcon;

    private LinearLayout review;

    private static int API_COUNTER = 0;

    private static final int DRIVING = 1, WALKING = 2, BYCYCLING = 3;

    ArrayList<FavouritePlace> favouritePlaces;

    FavouritePlace favouritePlace;

    SaveManager saveData;

    private boolean FALG_ALREADY_FAV = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placedetails);

        Intent i = getIntent();

        // Place referece id
        place_reference = i.getStringExtra("ref");
        photo_ref = i.getStringExtra("photo_ref");

        des_location = i.getStringExtra("des_location");

        API_COUNTER = i.getIntExtra("api_counter", 0);


        loadAdView();

        init();


        favouritePlaces = saveData.getFavPlaces();

        setupApiUrl(place_reference);

        sendRequestToServer_des(driving_url, DRIVING);

        sendRequestToServer_des(walk_url, WALKING);

        sendRequestToServer_des(cycle_url, BYCYCLING);

        sendRequestToServer(placeDetails_url);

        // new loadSingleView().execute();
    }

    public void loadAdView() {
        AdView mAdView = (AdView) findViewById(R.id.adViewDetails);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void init() {

        saveData = new SaveManager(this);

        gson = new Gson();

        FALG_ALREADY_FAV = false;

        networkImageView = (RelativeLayout) findViewById(R.id.photo);


        image_url = photo_ref;

        //networkImageView.setImageUrl(AppConstant.url_photo_search_placelist + photo_ref + "&key="+ AppConstant.BROWSER_API_KEY, imageLoader);
        title = (TextView) findViewById(R.id.title);
        address = (TextView) findViewById(R.id.address);
        phone = (TextView) findViewById(R.id.phone);
        walk = (TextView) findViewById(R.id.walk);
        cycle = (TextView) findViewById(R.id.cycle);
        sbus = (TextView) findViewById(R.id.sbus);
        web_address = (TextView) findViewById(R.id.website);
        web_address.setOnClickListener(this);


        distanceTv = (TextView) findViewById(R.id.distance);

        showInMap = (ImageView) findViewById(R.id.showinmap);
        showInMap.setOnClickListener(this);

        favIcon = (ImageView) findViewById(R.id.makefav);
        favIcon.setOnClickListener(this);

        review = (LinearLayout) findViewById(R.id.review);
        review.setOnClickListener(this);

        pBar = (ProgressBar) findViewById(R.id.loadingProgressBar);


    }


    private void setupApiUrl(String reference) {
        // TODO Auto-generated method stub

        placeDetails_url = AppConstant.url_place_details + "reference="
                + reference + "&key=" + AppConstant.API_KEY[API_COUNTER];

        driving_url = AppConstant.url_distance + "origins=" + AppConstant.user_static_location.getUserLoc() + "&destinations=" + des_location + "&mode=driving&key=" + AppConstant.API_KEY[API_COUNTER];
        // https://maps.googleapis.com/maps/api/directions/json?
        // origin=Brooklyn&destination=Queens&departure_time=1343641500&mode=transit&key=YOUR_API_KEY
        cycle_url = AppConstant.url_distance + "origins=" + AppConstant.user_static_location.getUserLoc() + "&destinations=" + des_location + "&mode=bicycling&key=" + AppConstant.API_KEY[API_COUNTER];
        walk_url = AppConstant.url_distance + "origins=" + AppConstant.user_static_location.getUserLoc() + "&destinations=" + des_location + "&mode=walking&key=" + AppConstant.API_KEY[API_COUNTER];

        Log.d("DEBUG_placeDetails_url", placeDetails_url);

    }

    public void sendRequestToServer_des(String sentUrl, final int mode) {


        StringRequest req = new StringRequest(Request.Method.GET, sentUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray rows = obj.getJSONArray("rows");
                    JSONObject tempObject1 = rows.getJSONObject(0);

                    JSONArray elements = tempObject1.getJSONArray("elements");

                    JSONObject tempObject2 = elements.getJSONObject(0);

                    String status = tempObject2.getString("status");

                    if (!status.equalsIgnoreCase("ZERO_RESULTS")) {
                        if (mode == DRIVING) {
                            JSONObject distance = tempObject2.getJSONObject("distance");

                            String distanceString = distance.getString("text");
                            distanceTv.setText(distanceString);

                            JSONObject duration = tempObject2.getJSONObject("duration");

                            String durationSring = duration.getString("text");

                            sbus.setText(durationSring);
                        }
                        if (mode == WALKING) {


                            JSONObject duration = tempObject2.getJSONObject("duration");

                            String durationSring = duration.getString("text");

                            walk.setText(durationSring);
                        }
                        if (mode == BYCYCLING) {


                            JSONObject duration = tempObject2.getJSONObject("duration");

                            String durationSring = duration.getString("text");

                            cycle.setText(durationSring);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pBar.setVisibility(View.INVISIBLE);

            }
        });

        // req.setRetryPolicy(new DefaultRetryPolicy(Constants.MY_SOCKET_TIMEOUT_MS,
        //        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(req);

    }

    public void sendRequestToServer(String sentUrl) {


        StringRequest req = new StringRequest(Request.Method.GET, sentUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                new BackgroungTaskRunner(response).execute();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pBar.setVisibility(View.INVISIBLE);

            }
        });

        // req.setRetryPolicy(new DefaultRetryPolicy(Constants.MY_SOCKET_TIMEOUT_MS,
        //        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(req);

    }


    public class BackgroungTaskRunner extends AsyncTask<String, String, String> {
        String response;


        public BackgroungTaskRunner(String response) {
            this.response = response;

        }


        @Override
        public String doInBackground(String... params) {
            try {


                URL url = new URL(image_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bmImg = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {

                parseJsonFeed(new JSONObject(response));

            } catch (JSONException e) {
            }
            return null;
        }

        @Override
        public void onPostExecute(String result) {

            title.setText(placeDetail.getName());

            address.setText(placeDetail.getFormatted_address());

            if (placeDetail.getWebsite() != null) web_address.setText(placeDetail.getWebsite());
            else
                web_address.setText("Not Found");

            favouritePlace = new FavouritePlace(placeDetail.getName(), placeDetail.getFormatted_address(), image_url, AppConstant.des_lat, AppConstant.des_lng, place_reference, "place_id");

            if (favouritePlaces.contains(favouritePlace)) {
                FALG_ALREADY_FAV = true;
                favIcon.setImageResource(R.drawable.fav_yes);
            } else {
                favIcon.setImageResource(R.drawable.fav_no);
            }

            if (placeDetail.getInternational_phone_number() != null) {
                phone.setText(placeDetail.getInternational_phone_number());
                phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!placeDetail.getInternational_phone_number().equals("")) {
                            Uri number = Uri.parse("tel:" + placeDetail.getInternational_phone_number());
                            Intent dial = new Intent(Intent.ACTION_DIAL, number);
                            startActivity(dial);
                        }
                    }
                });

            } else phone.setText("Not Found");


            Drawable dr = new BitmapDrawable(bmImg);
            (networkImageView).setBackgroundDrawable(dr);


            try {
                if (placeDetail.getReviews().get(0).getAuthor_name() != null) {

                    AppConstant.reviews = placeDetail.getReviews();


                } else {
                    AppConstant.reviews.clear();
                }
            } catch (Exception e) {
                AppConstant.reviews.clear();
                e.printStackTrace();
            }


            pBar.setVisibility(View.GONE);


        }
    }


    private void parseJsonFeed(JSONObject response) {
        try {
            JSONObject tempObject = response.getJSONObject("result");
            //JSONObject tempObject = jsonArPak.getJSONObject(i);
            placeDetail = gson.fromJson(tempObject.toString(), PlaceDetail.class);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {


        int id = v.getId();

        if (id == R.id.showinmap) {
            String title = placeDetail.getName();

            AlertDialog.Builder alert = new AlertDialog.Builder(PlaceDetails.this);
            alert.setTitle(title);

            alert.setMessage("Do you Want see Direction");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    Uri gmmIntentUri = Uri.parse("google.navigation:q="
                            + des_location);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);

                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            alert.show();
        }

        if (id == R.id.makefav) {
            if (FALG_ALREADY_FAV) {
                favIcon.setImageResource(R.drawable.fav_no);
                favouritePlaces.remove(favouritePlace);
                saveData.setFavPlaces(favouritePlaces);
            } else {
                favIcon.setImageResource(R.drawable.fav_yes);
                favouritePlaces.add(favouritePlace);
                saveData.setFavPlaces(favouritePlaces);
            }
        }

        if (id == R.id.website) {
            if (!web_address.getText().toString().equalsIgnoreCase("Not Found")) {
                Intent intent = new Intent(PlaceDetails.this, WebViewActivity.class);
                intent.putExtra("url", web_address.getText().toString());
                startActivity(intent);

            }


        }

        if (id == R.id.review) {
            Intent intent = new Intent(PlaceDetails.this, ReviewActivity.class);
            startActivity(intent);
        }
    }

}

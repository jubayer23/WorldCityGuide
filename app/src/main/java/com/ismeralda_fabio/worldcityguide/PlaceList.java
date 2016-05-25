package com.ismeralda_fabio.worldcityguide;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ismeralda_fabio.worldcityguide.adapter.PlaceListAdapter;
import com.ismeralda_fabio.worldcityguide.appdata.AppConstant;
import com.ismeralda_fabio.worldcityguide.appdata.AppController;
import com.ismeralda_fabio.worldcityguide.model.Place;
import com.ismeralda_fabio.worldcityguide.model.UserLocation;
import com.ismeralda_fabio.worldcityguide.sharedprefs.SaveManager;
import com.ismeralda_fabio.worldcityguide.utils.GPSTracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by comsol on 10/16/2015.
 */
public class PlaceList extends Activity implements View.OnClickListener, AbsListView.OnScrollListener {

    Gson gson;

    // GPS Location
    GPSTracker gps;

    // user location
    UserLocation user_loc;

    private ListView listView;

    private EditText editText_search;

    private PlaceListAdapter placeListAdapter;

    private String url, urlAutoComplete;

    private static String search_name = "";

    private ImageView btn_show_map;

    private ProgressBar pBar, pBar2;

    String photo_ref = "no data";

    private ArrayList<Place> Places_my;
    private ArrayList<Place> places_new;


    private static int radius_counter = 1;

    private boolean PLACE_SEARCH = false;
    private boolean PLACE_SEARCH_AGAIN = false;

    private static final String TAG = "Chartboost";

    public static int API_COUNTER = 0;


    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));


    private String pageToken = "";
    private boolean isLoading = false;


    private String query_type, searchText;


    private SaveManager saveData;

    // ADMOB VARIABLE
    private InterstitialAd mInterstitialAd;

    private static int AGAIN_SEARCH_COUNTER = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        radius_counter = 1;
        pageToken = "";
        AGAIN_SEARCH_COUNTER = 0;

        saveData = new SaveManager(this);

        Intent intent = getIntent();


        searchText = intent.getStringExtra(MainActivity.KEY_SEARCHTEXT);
        PLACE_SEARCH = intent.getBooleanExtra(MainActivity.KEY_IS_PLACESEARCH, false);
        query_type = intent.getStringExtra(MainActivity.KEY_QUERYTYPE);


        AppConstant.query_type = query_type;


        AppConstant.PLACE_SEARCH = PLACE_SEARCH;


        //getActionBar().show();

        PLACE_SEARCH_AGAIN = false;

        getUserLocationFromGps();

        grabUiId();

        loadAdView();

        setupApiUrl(query_type, API_COUNTER);

        //hitUrl();
        pageToken = "";
        isLoading = true;

        placeListAdapter = new PlaceListAdapter(
                PlaceList.this, Places_my);
        listView.setAdapter(placeListAdapter);

        if (PLACE_SEARCH) {


            sendRequestToServer(setupPlaceSearch(searchText, API_COUNTER));

        } else {

            editText_search.setVisibility(View.GONE);
            sendRequestToServerForIcon(url);

        }


        /**
         * ListItem click event
         * On selecting a listitem SinglePlaceActivity is launched
         * */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem

                Place place = AppConstant.staticPlaces.get(position);
                String reference = place.getReference();
                String photo_reference = place.getPhoto_ref();
                double des_lat = place.getGeometry().getLocation().getLat();
                double des_lng = place.getGeometry().getLocation().getLng();
                AppConstant.des_lat = String.valueOf(des_lat);
                AppConstant.des_lng = String.valueOf(des_lng);
                String des_location = String.valueOf(des_lat) + "," + String.valueOf(des_lng);


                //Toast.makeText(PlaceList.this, reference, Toast.LENGTH_SHORT).show();

                // Log.d("DEBUG", reference);

                Log.d("DEBUG loc 2", String.valueOf(place.getGeometry().getLocation().getLat()) + "," + String.valueOf(place.getGeometry().getLocation().getLng()));

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        PlaceDetails.class);

                // Sending place refrence id to single place activity
                // place refrence id used to get "Place full details"
                in.putExtra("ref", reference);
                in.putExtra("place_id", place.getPlace_id());
                in.putExtra("photo_ref", photo_reference);
                in.putExtra("des_location", des_location);
                in.putExtra("api_counter", API_COUNTER);


                startActivity(in);


            }
        });
        listView.setOnScrollListener(this);


        editText_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    AGAIN_SEARCH_COUNTER++;

                    pBar.setVisibility(View.VISIBLE);

                    PLACE_SEARCH_AGAIN = true;
                    pageToken = "";
                    isLoading = true;

                    Places_my = new ArrayList<Place>();


                    searchText = editText_search.getText().toString();

                    sendRequestToServer(setupPlaceSearch(editText_search.getText().toString(), API_COUNTER));
                    return true;
                }
                return false;
            }
        });


    }


    private void getUserLocationFromGps() {
        // TODO Auto-generated method stub
        gps = new GPSTracker(this);

        user_loc = new UserLocation(gps.getLatitude(), gps.getLongitude());

        AppConstant.user_static_location = user_loc;

        // Log.d("DEBUG", String.valueOf(gps.getLatitude()) + "," + String.valueOf(gps.getLongitude()));
    }

    private void grabUiId() {
        // TODO Auto-generated method stub


        gson = new Gson();

        API_COUNTER = 0;

        pBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
        pBar2 = (ProgressBar) findViewById(R.id.loadingProgressBar_listview);

        editText_search = (EditText) findViewById(R.id.edit_search);
        editText_search.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.place_list);


        btn_show_map = (ImageView) findViewById(R.id.btn_show_map);
        btn_show_map.setOnClickListener(this);


        Places_my = new ArrayList<Place>();
        places_new = new ArrayList<Place>();

        // Check if no view has focus:

    }


    public void loadAdView() {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // load INTERTITIAL ADD VIEW

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.intertitial_ad_unit_id));
        requestNewInterstitial();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(
                "554FD1C059BF37BF1981C59FF9E1DAE0").build();

        mInterstitialAd.loadAd(adRequest);
    }


    private void setupApiUrl(String type, int API_COUNTER) {
        // TODO Auto-generated method stub

        String name = search_name.length() >= 1 ? "&name=" + search_name : "";


        url = AppConstant.url_search + "types=" + type + name + "&rankby=distance" + "&location=" + user_loc.getUserLoc() +
                "&sensor=false" + "&key=" + AppConstant.API_KEY[API_COUNTER];
        // Log.d("DEBUG_url", url);
    }


    private String setupPlaceSearch(String user_input, int API_COUNTER) {

        String query = "";

        try {
            query = "query=" + URLEncoder.encode(user_input, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        // place type to be searched


        String radius = "radius=50000";

        String location = "location=" + user_loc.getUserLoc();

        String key = "key=" + AppConstant.API_KEY[API_COUNTER];

        // Building the parameters to the web service
        String parameters = query + "&" + location + "&" + radius + "&" + key;


        return AppConstant.url_place_search + parameters;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.edit_search) {
            editText_search.requestFocusFromTouch();
        }

        if (id == R.id.btn_show_map) {
            Intent intent = new Intent(PlaceList.this, MapActivity.class);
            startActivity(intent);
        }
    }


    public void sendRequestToServer(String sentUrl) {

        Log.d("DEBUG_url", sentUrl);
        StringRequest req = new StringRequest(Request.Method.GET, sentUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("OVER_QUERY_LIMIT")) {
                        if ((API_COUNTER + 1) < AppConstant.API_KEY.length) {
                            API_COUNTER++;
                            AppConstant.API_COUNTER = API_COUNTER;
                            saveData.setApiCounter(API_COUNTER);
                            Log.d("DEBUG_Counter", String.valueOf(API_COUNTER));
                            sendRequestToServer(setupPlaceSearch(searchText, API_COUNTER));
                        } else {
                            Toast.makeText(PlaceList.this, "OVER_QUERY_LIMIT", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        new BackgroungTaskRunner(response).execute();
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

    public void sendRequestToServerForIcon(String sentUrl) {

        Log.d("DEBUG_url", sentUrl);
        StringRequest req = new StringRequest(Request.Method.GET, sentUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("OVER_QUERY_LIMIT")) {
                        if ((API_COUNTER + 1) < AppConstant.API_KEY.length) {
                            API_COUNTER++;
                            AppConstant.API_COUNTER = API_COUNTER;
                            saveData.setApiCounter(API_COUNTER);
                            Log.d("DEBUG_Counter", String.valueOf(API_COUNTER));
                            setupApiUrl(query_type, API_COUNTER);
                            sendRequestToServerForIcon(url);
                        } else {
                            Toast.makeText(PlaceList.this, "OVER_QUERY_LIMIT", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        new BackgroungTaskRunnerForIcon(response).execute();
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


    public class BackgroungTaskRunner extends AsyncTask<String, String, String> {
        String response;


        public BackgroungTaskRunner(String response) {
            this.response = response;

        }


        @Override
        public String doInBackground(String... params) {


            return null;
        }

        @Override
        public void onPostExecute(String result) {

            try {

                parseJsonFeed(new JSONObject(response));


            } catch (JSONException e) {
            }


            AppConstant.staticPlaces = Places_my;


            if (PLACE_SEARCH_AGAIN) {
                PLACE_SEARCH_AGAIN = false;
                placeListAdapter = new PlaceListAdapter(
                        PlaceList.this, Places_my);
                listView.setAdapter(placeListAdapter);
            } else {
                if (placeListAdapter == null) {
                    placeListAdapter = new PlaceListAdapter(
                            PlaceList.this, Places_my);
                    listView.setAdapter(placeListAdapter);

                } else {
                    placeListAdapter.addMore(places_new);
                }
            }


            PLACE_SEARCH_AGAIN = false;
            isLoading = false;
            pBar.setVisibility(View.GONE);
            pBar2.setVisibility(View.GONE);

            if(AGAIN_SEARCH_COUNTER == 2)
            {
                AGAIN_SEARCH_COUNTER = 0;
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }

            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public class BackgroungTaskRunnerForIcon extends AsyncTask<String, String, String> {
        String response;


        public BackgroungTaskRunnerForIcon(String response) {
            this.response = response;

        }


        @Override
        public String doInBackground(String... params) {


            return null;
        }

        @Override
        public void onPostExecute(String result) {

            try {

                parseJsonFeed(new JSONObject(response));


            } catch (JSONException e) {
            }

            sendRequestToServer(setupPlaceSearch(query_type, API_COUNTER));
        }
    }


    public void parseJsonFeed(JSONObject response) {

        places_new.clear();


        try {
            pageToken = response.getString("next_page_token");
        } catch (JSONException e) {
            pageToken = "";
        }


        try {


            JSONArray jsonArPak = response.getJSONArray("results");

            for (int i = 0; i < jsonArPak.length(); i++) {
                JSONObject tempObject = jsonArPak.getJSONObject(i);

                Place place = gson.fromJson(tempObject.toString(), Place.class);


                try {
                    JSONArray photosJSONArray = tempObject.getJSONArray("photos");
                    JSONObject photoObj = photosJSONArray.getJSONObject(0);

                    place.setPhoto_ref(AppConstant.url_photo_search + photoObj.getString("photo_reference") + "&key=" + AppConstant.API_KEY[API_COUNTER]);

                } catch (Exception e) {

                    place.setPhoto_ref(getCustomPhotoRef(query_type + searchText));
                    e.printStackTrace();
                }

                Places_my.add(place);
                places_new.add(place);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String getCustomPhotoRef(String type) {

        return "http://i.imgur.com/JmurHyK.png";
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (listView.getLastVisiblePosition() == listView.getAdapter().getCount() - 1 &&
                listView.getChildAt(listView.getChildCount() - 1).getBottom() <= listView.getHeight() && pageToken.length() > 3 && !isLoading) {
            pBar2.setVisibility(View.VISIBLE);
            isLoading = true;
            String url2 = AppConstant.url_place_search + "pagetoken=" + pageToken + "&key=" + AppConstant.API_KEY[API_COUNTER];
            sendRequestToServer(url2);
            //It is scrolled all the way down here
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
            Log.d("DEBUG_scroll", "yes");
        }
    }


    @Override
    public void onBackPressed() {
        // If an interstitial is on screen, close    {
        AppConstant.PLACE_SEARCH = false;
        super.onBackPressed();

    }


}

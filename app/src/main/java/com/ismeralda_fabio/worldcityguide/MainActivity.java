package com.ismeralda_fabio.worldcityguide;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.ismeralda_fabio.worldcityguide.appdata.AppConstant;
import com.ismeralda_fabio.worldcityguide.dialog.AlertDialogManager;
import com.ismeralda_fabio.worldcityguide.model.UserLocation;
import com.ismeralda_fabio.worldcityguide.sharedprefs.SaveManager;
import com.ismeralda_fabio.worldcityguide.utils.ConnectionDetector;
import com.ismeralda_fabio.worldcityguide.utils.GPSTracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;


/**
 * Created by comsol on 10/16/2015.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private LinearLayout linearLayout_MAIN;

    // Connection detector class
    ConnectionDetector cd;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // GPS Location
    GPSTracker gps;

    private String query_type[] = new String[500];

    private int item_counter = 0;

    private String[] levelParams;

    private ArrayList<LinearLayout> LVerticalLayoutArraylist = new ArrayList<LinearLayout>();


    private SaveManager saveData;

    private SearchView searchView;

    // ADMOB VARIABLE
    private InterstitialAd mInterstitialAd;

    private static boolean FLAG_SEARCH = false;

    private String searchText;

    private final static String APP_PNAME = "com.ismeralda_fabio.worldcityguide";// Package
    // Name


    private View v1;

    public static final String KEY_QUERYTYPE = "query_type";
    public static final String KEY_SEARCHTEXT = "query_search";
    public static final String KEY_IS_PLACESEARCH = "is_place_search";
    public static final String KEY_TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getActionBar().setDisplayShowTitleEnabled(true);
        // getActionBar().setDisplayHomeAsUpEnabled(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setIcon(R.mipmap.icon);




        item_counter = 0;

        FLAG_SEARCH = false;

        grabUiId();

        setupUi();

        loadAdView();

        cd = new ConnectionDetector(getApplicationContext());

        // creating GPS Class object
        gps = new GPSTracker(this);


        saveData = new SaveManager(this);

        if (!gps.canGetLocation()) {
            showGPSDisabledAlertToUser();
        }


    }


    private void setupUi() {
        LVerticalLayoutArraylist.clear();

        LinearLayout.LayoutParams tvMainparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tvMainparams.gravity = Gravity.CENTER;
        LinearLayout.LayoutParams LinearHorizontalparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams LinearVerticalParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        LinearLayout.LayoutParams commonComponentparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        commonComponentparams.gravity = Gravity.CENTER;
        /*******************************1ST LOOP****************************/
        for (int i = 0; i < AppConstant.Cat_SubCat_Titles.length; i++) {
            //Log.d("DEBUG", AppConstant.Cat_SubCat_Titles[i][0] + "\n");


            TextView tvMain = new TextView(this);
            tvMain.setLayoutParams(tvMainparams);
            tvMain.setBackgroundResource(R.drawable.layout_bg_rounded);
            tvMain.setGravity(Gravity.CENTER);
            tvMain.setPadding(20, 0, 20, 0);
            tvMain.setText(AppConstant.Cat_SubCat_Titles[i][0]);
            tvMain.setTextColor(getResources().getColor(R.color.white));
            tvMain.setTypeface(Typeface.DEFAULT_BOLD);

            linearLayout_MAIN.addView(tvMain);

            LinearLayout LhorizontalLayout = new LinearLayout(this);
            LhorizontalLayout.setLayoutParams(LinearHorizontalparams);
            LhorizontalLayout.setGravity(Gravity.CENTER);
            LhorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            LhorizontalLayout.setWeightSum(3f);

            final float scale = this.getResources().getDisplayMetrics().density;
            int pixels = (int) (100 * scale + 0.5f);


            /*******************************2ND LOOP*****************************/
            int j;
            for (j = 1; j < AppConstant.Cat_SubCat_Titles[i].length; j++) {
                // Log.d("DEBUG", AppConstant.Cat_SubCat_Titles[i][j] + "\n");


                levelParams = AppConstant.Cat_SubCat_Titles[i][j].split("/");
                query_type[item_counter++] = levelParams[2];

                LinearLayout LVerticalLayout = new LinearLayout(this);
                LVerticalLayout.setLayoutParams(LinearVerticalParams);
                LVerticalLayout.setOrientation(LinearLayout.VERTICAL);


                ImageView icon = new ImageView(this);

                LinearLayout.LayoutParams commonComponentparams_2 = new LinearLayout.LayoutParams(pixels, pixels);
                icon.setLayoutParams(commonComponentparams_2);
                icon.setImageResource(MainActivity.this.getResources().getIdentifier(
                        levelParams[0], "drawable", getPackageName()));
                TextView tv = new TextView(this);
                tv.setLayoutParams(commonComponentparams);
                tv.setMaxLines(1);
                if (levelParams[1].length() >= 15) {
                    tv.setTextSize(11);
                }
                tv.setText(levelParams[1]);

                LVerticalLayout.addView(icon);
                LVerticalLayout.addView(tv);
                LVerticalLayout.setOnClickListener(this);
                LVerticalLayoutArraylist.add(LVerticalLayout);

                LhorizontalLayout.addView(LVerticalLayout);


                if ((j % 3) == 0) {
                    linearLayout_MAIN.addView(LhorizontalLayout);

                    LhorizontalLayout = new LinearLayout(this);
                    LhorizontalLayout.setLayoutParams(LinearHorizontalparams);
                    LhorizontalLayout.setGravity(Gravity.CENTER);
                    LhorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LhorizontalLayout.setWeightSum(3f);

                }
            }

            if ((((j - 1) % 3) != 0)) {
                linearLayout_MAIN.addView(LhorizontalLayout);
            }
        }
    }

    private void grabUiId() {
        linearLayout_MAIN = (LinearLayout) findViewById(R.id.ll_layout);
    }

    public void loadAdView() {
        AdView mAdView = (AdView) findViewById(R.id.adViewMain);
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


                Intent intent = new Intent(MainActivity.this, PlaceList.class);

                if (FLAG_SEARCH) {
                    intent.putExtra(KEY_IS_PLACESEARCH, true);
                    intent.putExtra(KEY_SEARCHTEXT, searchText);
                    intent.putExtra(KEY_QUERYTYPE, "");
                    intent.putExtra(KEY_TITLE, "ALL LIST");
                } else {
                    intent.putExtra(KEY_IS_PLACESEARCH, false);
                    intent.putExtra(KEY_SEARCHTEXT, "");
                    intent.putExtra(KEY_QUERYTYPE, query_type[LVerticalLayoutArraylist.indexOf(v1)]);
                    intent.putExtra(KEY_TITLE, "ALL LIST");
                }


                startActivity(intent);
                // beginPlayingGame();
            }
        });


    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(
                "554FD1C059BF37BF1981C59FF9E1DAE0").build();

        mInterstitialAd.loadAd(adRequest);
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setMessage("Turn ON your GPS to get better RESULTS.").setCancelable(false).setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                Intent localIntent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
                MainActivity.this.startActivity(localIntent);
            }
        });
        localBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.cancel();
            }
        });
        localBuilder.create().show();
    }

    @Override
    public void onClick(View v) {

        if (!cd.isConnectingToInternet()) {
            //Internet Connection is not present
            alert.showAlertDialog(MainActivity.this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            //stop executing code by return
            return;
        }
        gps = new GPSTracker(this);
        if (!gps.canGetLocation()) {
            alert.showAlertDialog(MainActivity.this, "GPS Status",
                    "Couldn't get location information. Please enable GPS",
                    false);
            //stop executing code by return
            return;
        }
        if (!cd.isGoogelPlayInstalled()) {
            //Internet Connection is not present
            alert.showAlertDialog(MainActivity.this, "Google Play Services",
                    "No google play services!!!Please Install google play services", false);
            //stop executing code by return
            return;
        }


        int id = v.getId();
        if (LVerticalLayoutArraylist.contains(v)) {
            // Toast.makeText(this,
            //         String.valueOf(keyboardCharArray[keyboard.indexOf(view)]),
            //         Toast.LENGTH_SHORT).show();
            //updateSolveKeyboard(keyboardCharArray[keyboard.indexOf(view)],
            //        keyboard.indexOf(view), view);


            // checkarray(userword);
            int counter = saveData.getIconCounter();
            saveData.setIconCounter(++counter);

            v1 = v;
            FLAG_SEARCH = false;

            if (counter >= 2) {

                saveData.setIconCounter(0);

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            } else {

                Intent intent = new Intent(MainActivity.this, PlaceList.class);

                intent.putExtra(KEY_IS_PLACESEARCH, false);
                intent.putExtra(KEY_SEARCHTEXT, "");
                intent.putExtra(KEY_QUERYTYPE, query_type[LVerticalLayoutArraylist.indexOf(v1)]);
                intent.putExtra(KEY_TITLE, "ALL LIST");

                startActivity(intent);
            }


        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        AppConstant.PLACE_SEARCH = false;

        cd = new ConnectionDetector(getApplicationContext());

        // creating GPS Class object
        gps = new GPSTracker(this);

        if (gps.canGetLocation() && cd.isConnectingToInternet()) {

            AppConstant.user_static_location = new UserLocation(gps.getLatitude(), gps.getLongitude());
            saveData.setLat(String.valueOf(gps.getLatitude()));
            saveData.setLng(String.valueOf(gps.getLongitude()));
        }


    }


    @Override
    public void onBackPressed() {


        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                int counter = saveData.getSearchCounter();
                saveData.setSearchCounter(++counter);

                searchText = query;
                FLAG_SEARCH = true;

                if (counter >= 2) {
                    saveData.setSearchCounter(0);
                    // Do your task here

                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }

                } else {
                    Intent intent = new Intent(MainActivity.this, PlaceList.class);


                    intent.putExtra(KEY_IS_PLACESEARCH, true);
                    intent.putExtra(KEY_SEARCHTEXT, searchText);
                    intent.putExtra(KEY_QUERYTYPE, "");
                    intent.putExtra(KEY_TITLE, "ALL LIST");


                    startActivity(intent);
                }


                return true;
            }

        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * On selecting action bar icons
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_search:
                // search action

                return true;
            case R.id.action_fav:
                // location found
                Intent intent = new Intent(MainActivity.this, FavouriteActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_moreapps:

                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Ismeralda+Fabio"));
                startActivity(i);

                return true;

            case R.id.action_decaimer:


                alert.showAlertDialog(this, "Disclaimer", "Disclaimer: The provided locations details and content is provided by google and the application developer doesn't hold liable for any inaccurate or incorrect information.", true);
                // refresh
                return true;
            case R.id.action_check_updates:

                startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("https://play.google.com/store/apps/details?id="
                                + APP_PNAME)));
                // help action
                return true;

            case R.id.action_share:

                Intent intent2 = new Intent(Intent.ACTION_SEND);
                intent2.setType("text/plain");
                intent2.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id="
                                + APP_PNAME);
                intent2.putExtra(Intent.EXTRA_SUBJECT, "Hey..Find out this wonderful apps that i think you should try it!!");

                startActivity(Intent.createChooser(intent2, "Share"));

                // help action
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

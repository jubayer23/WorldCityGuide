package com.ismeralda_fabio.worldcityguide;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ismeralda_fabio.worldcityguide.adapter.PlaceCommentAdapter;
import com.ismeralda_fabio.worldcityguide.appdata.AppConstant;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ReviewActivity extends Activity {

    private ListView listView;

    private TextView textView;

    private PlaceCommentAdapter placeCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        loadAdView();

        listView = (ListView) findViewById(R.id.place_list_comment);

        textView = (TextView) findViewById(R.id.temp);

        if (AppConstant.reviews.size() <= 0) {
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        } else {
            placeCommentAdapter = new PlaceCommentAdapter(
                    ReviewActivity.this, AppConstant.reviews);
            listView.setAdapter(placeCommentAdapter);
        }


    }

    public void loadAdView() {
        AdView mAdView = (AdView) findViewById(R.id.adViewReview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


}

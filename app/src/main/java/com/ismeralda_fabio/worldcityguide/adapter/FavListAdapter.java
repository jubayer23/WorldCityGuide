package com.ismeralda_fabio.worldcityguide.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ismeralda_fabio.worldcityguide.R;
import com.ismeralda_fabio.worldcityguide.appdata.AppController;
import com.ismeralda_fabio.worldcityguide.model.FavouritePlace;
import com.ismeralda_fabio.worldcityguide.sharedprefs.SaveManager;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("DefaultLocale")
public class FavListAdapter extends BaseAdapter {

    private List<FavouritePlace> Displayedplaces;
    private List<FavouritePlace> Originalplaces;
    private LayoutInflater inflater;
    @SuppressWarnings("unused")
    private Activity activity;

    private SaveManager saveData;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public FavListAdapter(Activity activity, List<FavouritePlace> places) {
        this.activity = activity;
        this.Displayedplaces = places;
        this.Originalplaces = places;

        saveData = new SaveManager(activity);

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Displayedplaces.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.place_fav, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.networkImageView = (NetworkImageView) convertView
                    .findViewById(R.id.product_thumb);


            viewHolder.productName = (TextView) convertView
                    .findViewById(R.id.product_name);

            viewHolder.productDetails = (TextView) convertView
                    .findViewById(R.id.product_details);
            viewHolder.destination = (TextView) convertView
                    .findViewById(R.id.distance);

            viewHolder.delete_icon = (ImageView)convertView.findViewById(R.id.fav_delete);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FavouritePlace place = Displayedplaces.get(position);

        float[] results = new float[1];
        Location.distanceBetween(saveData.getLat(),
                saveData.getLng(), Double.parseDouble(place.getLat()),
                Double.parseDouble(place.getLng()), results);


        //String rep = place.getPhotos().get(0).getPhoto_reference();


        viewHolder.networkImageView.setImageUrl(place.getPhoto_ref(), imageLoader);


        viewHolder.productName.setText(place.getTitle());
        viewHolder.productDetails.setText(place.getFull_address());

        if ((int) results[0] <= 1000) {
            viewHolder.destination.setText(String.valueOf(results[0]).substring(0, 3) + "m");
        } else {
            viewHolder.destination.setText(String.valueOf(results[0] / 1000).substring(0, 3) + "km");
        }


        viewHolder.delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Displayedplaces.remove(position);
                saveData.setFavPlaces((ArrayList<FavouritePlace>) Displayedplaces);
                addMore();
            }
        });


        return convertView;
    }

    public void addMore() {
        //this.Displayedplaces.addAll(places);
        notifyDataSetChanged();
    }


    private static class ViewHolder {
        private NetworkImageView networkImageView;
        private TextView productName;
        private TextView productDetails;
        private TextView destination;
        private ImageView delete_icon;
    }


}
package com.ismeralda_fabio.worldcityguide.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ismeralda_fabio.worldcityguide.R;
import com.ismeralda_fabio.worldcityguide.appdata.AppConstant;
import com.ismeralda_fabio.worldcityguide.appdata.AppController;
import com.ismeralda_fabio.worldcityguide.model.Place;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("DefaultLocale")
public class PlaceListAdapter extends BaseAdapter implements Filterable {

    private List<Place> Displayedplaces;
    private List<Place> Originalplaces;
    private LayoutInflater inflater;


    @SuppressWarnings("unused")
    private Activity activity;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public PlaceListAdapter(Activity activity, List<Place> places) {
        this.activity = activity;
        this.Displayedplaces = places;
        this.Originalplaces = places;



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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            if (AppConstant.PLACE_SEARCH)
                convertView = inflater.inflate(R.layout.place2, parent, false);
            else convertView = inflater.inflate(R.layout.place, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.networkImageView = (NetworkImageView) convertView
                    .findViewById(R.id.product_thumb);


            viewHolder.productName = (TextView) convertView
                    .findViewById(R.id.product_name);

            viewHolder.productDetails = (TextView) convertView
                    .findViewById(R.id.product_details);
            viewHolder.destination = (TextView) convertView
                    .findViewById(R.id.distance);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Place place = Displayedplaces.get(position);

        float[] results = new float[1];
        Location.distanceBetween(AppConstant.user_static_location.getLat(),
                AppConstant.user_static_location.getLng(), place.getGeometry().getLocation().getLat(),
                place.getGeometry().getLocation().getLng(), results);


        //String rep = place.getPhotos().get(0).getPhoto_reference();


        viewHolder.networkImageView.setImageUrl(place.getPhoto_ref(), imageLoader);


        viewHolder.productName.setText(place.getName());
        if (AppConstant.PLACE_SEARCH) viewHolder.productDetails.setVisibility(View.GONE);
        else viewHolder.productDetails.setText(place.getVicinity());

        if ((int) results[0] <= 1000) {
            viewHolder.destination.setText(String.valueOf(results[0]).substring(0, 3) + "m");
        } else {
            viewHolder.destination.setText(String.valueOf(results[0] / 1000).substring(0, 3) + "km");
        }


        return convertView;
    }

    public void addMore(List<Place> places) {
        //this.Displayedplaces.addAll(places);
        notifyDataSetChanged();
    }


    private static class ViewHolder {
        private NetworkImageView networkImageView;
        private TextView productName;
        private TextView productDetails;
        private TextView destination;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                Displayedplaces = (List<Place>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<Place> FilteredArrList = new ArrayList<Place>();

                if (Originalplaces == null) {
                    Originalplaces = new ArrayList<Place>(Displayedplaces); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = Originalplaces.size();
                    results.values = Originalplaces;
                    AppConstant.staticPlaces = Originalplaces;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < Originalplaces.size(); i++) {
                        String data = Originalplaces.get(i).getName();
                        if (data.toLowerCase().contains(constraint.toString())) {
                            Place place = Originalplaces.get(i);

                            Log.d("DEBUG", "icon=" + place.getIcon());
                            Log.d("DEBUG", "id=" + place.getId());
                            Log.d("DEBUG", "Name=" + place.getName());
                            Log.d("DEBUG", "REf=" + place.getReference());
                            Log.d("DEBUG", "vicinity=" + place.getVicinity());
                            Log.d("DEBUG", "lan=" + String.valueOf(place.getGeometry().getLocation().getLat()));

                            FilteredArrList.add(new Place(place.getIcon(), place.getName(), place.getVicinity(), place.getReference(), place.getGeometry()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                    AppConstant.staticPlaces = FilteredArrList;

                    Place place = Originalplaces.get(0);
                    Log.d("DEBUG_2", "icon=" + place.getIcon());
                    Log.d("DEBUG_2", "id=" + place.getId());
                    Log.d("DEBUG_2", "Name=" + place.getName());
                    Log.d("DEBUG_2", "REf=" + place.getReference());
                    Log.d("DEBUG_2", "vicinity=" + place.getVicinity());
                    Log.d("DEBUG_2", "lan=" + String.valueOf(place.getGeometry().getLocation().getLat()));
                }
                return results;
            }
        };
        return filter;
    }
}
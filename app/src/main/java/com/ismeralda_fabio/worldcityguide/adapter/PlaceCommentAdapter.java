package com.ismeralda_fabio.worldcityguide.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ismeralda_fabio.worldcityguide.R;
import com.ismeralda_fabio.worldcityguide.model.Review;

import java.util.List;


@SuppressLint("DefaultLocale")
public class PlaceCommentAdapter extends BaseAdapter {

    private List<Review> reviews;

    private LayoutInflater inflater;
    @SuppressWarnings("unused")
    private Activity activity;


    public PlaceCommentAdapter(Activity activity, List<Review> reviews) {
        this.activity = activity;
        this.reviews = reviews;


        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return reviews.size();
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


            convertView = inflater.inflate(R.layout.user_comment, parent, false);

            viewHolder = new ViewHolder();


            viewHolder.comment = (TextView) convertView
                    .findViewById(R.id.user_comment);

            viewHolder.ll_user_rating = (LinearLayout) convertView.findViewById(R.id.user_rating);

            viewHolder.user_name = (TextView) convertView
                    .findViewById(R.id.user_name);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Review review = reviews.get(position);




        //String rep = place.getPhotos().get(0).getPhoto_reference();


        viewHolder.comment.setText(review.getText());

        LinearLayout.LayoutParams imgParams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);



        if(viewHolder.ll_user_rating.getChildCount() > 0)viewHolder.ll_user_rating.removeAllViews();

        for (int i = 0; i < Integer.parseInt(review.getRating()); i++) {

            if(i>5)break;
            ImageView img = new ImageView(activity);




            img.setLayoutParams(imgParams);

            img.setImageResource(R.drawable.fav_comment);


            viewHolder.ll_user_rating.addView(img);


        }


        viewHolder.user_name.setText(review.getAuthor_name());


        return convertView;
    }

    public void addMore(List<Review> reviews) {
        this.reviews.addAll(reviews);
        notifyDataSetChanged();
    }

    private static class ViewHolder {

        private TextView comment;
        private TextView user_name;


        private LinearLayout ll_user_rating;

    }


}
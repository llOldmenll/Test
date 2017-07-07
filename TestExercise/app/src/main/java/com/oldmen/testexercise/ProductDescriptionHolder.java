package com.oldmen.testexercise;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;


public class ProductDescriptionHolder extends RecyclerView.ViewHolder {

    private TextView userName;
    private TextView date;
    private TextView userReview;
    private RatingBar ratingBar;

    public ProductDescriptionHolder(View itemView) {
        super(itemView);

        userName = (TextView) itemView.findViewById(R.id.review_user_name);
        date = (TextView) itemView.findViewById(R.id.review_date);
        userReview = (TextView) itemView.findViewById(R.id.review_text);
        ratingBar = (RatingBar) itemView.findViewById(R.id.review_rating);
    }

    public void bindReview(Review review){

        String dataString = review.getCreatedAt();
        String normalData = (dataString.substring(0,dataString.indexOf("T"))).replace("-",".");

        userName.setText(review.getCreatedBy().getUsername());
        date.setText(normalData);
        userReview.setText(review.getText());
        ratingBar.setRating((float) review.getRate());
    }
}

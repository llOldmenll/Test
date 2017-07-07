package com.oldmen.testexercise.holder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.oldmen.testexercise.R;
import com.oldmen.testexercise.container.Product;
import com.squareup.picasso.Picasso;


public class ProductListHolder extends RecyclerView.ViewHolder {

    private final String IMAGE_BASE_URL = "http://smktesting.herokuapp.com/static/";
    private TextView productTitle;
    private RatingBar ratingBar;
    private ImageView previewImage;
    public CardView cardView;


    public ProductListHolder(View itemView) {
        super(itemView);

        productTitle = (TextView) itemView.findViewById(R.id.product_preview_title);
        ratingBar = (RatingBar) itemView.findViewById(R.id.product_preview_rating);
        previewImage = (ImageView) itemView.findViewById(R.id.product_preview_image);
        cardView = (CardView) itemView.findViewById(R.id.product_list_card);
    }

    public void bindProduct(final Product product, final Context context) {

        productTitle.setText(product.getTitle());
        ratingBar.setRating((float) product.getRating());
        Picasso.with(context)
                .load(IMAGE_BASE_URL + product.getImg())
                .into(previewImage);
    }
}

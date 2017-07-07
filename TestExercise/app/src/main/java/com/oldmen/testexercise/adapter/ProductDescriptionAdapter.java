package com.oldmen.testexercise.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oldmen.testexercise.holder.ProductDescriptionHolder;
import com.oldmen.testexercise.R;
import com.oldmen.testexercise.container.Review;

import java.util.ArrayList;


public class ProductDescriptionAdapter extends RecyclerView.Adapter<ProductDescriptionHolder> {

    private ArrayList<Review> reviewArrayList = new ArrayList<>();

    public ProductDescriptionAdapter(ArrayList<Review> reviewArrayList) {
        this.reviewArrayList = reviewArrayList;
    }

    @Override
    public ProductDescriptionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ProductDescriptionHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductDescriptionHolder holder, int position) {
        holder.bindReview(reviewArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }
}

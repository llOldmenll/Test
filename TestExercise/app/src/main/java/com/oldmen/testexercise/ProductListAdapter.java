package com.oldmen.testexercise;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class ProductListAdapter extends RecyclerView.Adapter<ProductListHolder> {

    private ArrayList<Product> productArrayList;
    private Context context;
    private OnCardClickListener listener;


    public ProductListAdapter(ArrayList<Product> productArrayList, Context context, OnCardClickListener listener) {
        this.productArrayList = productArrayList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ProductListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new ProductListHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductListHolder holder, int position) {

        final Product product = productArrayList.get(position);

        holder.bindProduct(product, context);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.cardClicked(product.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }
}

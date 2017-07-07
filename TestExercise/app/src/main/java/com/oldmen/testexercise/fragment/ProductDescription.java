package com.oldmen.testexercise.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oldmen.testexercise.adapter.ProductDescriptionAdapter;
import com.oldmen.testexercise.R;
import com.oldmen.testexercise.container.Review;
import com.oldmen.testexercise.activity.MainActivity;
import com.oldmen.testexercise.container.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ProductDescription extends Fragment {

    private final String IMAGE_BASE_URL = "http://smktesting.herokuapp.com/static/";
    private TextView title;
    private TextView description;
    private ImageView productImage;
    private RecyclerView recyclerReviews;
    private ProductDescriptionAdapter adapter;
    private FloatingActionButton fab;
    private ArrayList<Review> productReviews = new ArrayList<>();
    private Product product;
    public static final String PRODUCT_DESCR_FRAGMENT_TAG = "productDescription";
    private OnFabListener listener;

    public ProductDescription() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle data = getArguments();

        productReviews = (ArrayList<Review>) data.getSerializable(MainActivity.PRODUCT_REVIEWS_KEY);
        productReviews = reverseArrayList(productReviews);
        product = data.getParcelable(MainActivity.PRODUCT_SAMPLE_KEY);
        return inflater.inflate(R.layout.fragment_product_description, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        title = (TextView) view.findViewById(R.id.product_description_title);
        description = (TextView) view.findViewById(R.id.product_description);
        productImage = (ImageView) view.findViewById(R.id.product_description_image);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        recyclerReviews = (RecyclerView) view.findViewById(R.id.recycler_reviews_list);
        recyclerReviews.setNestedScrollingEnabled(false);

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        //float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        productImage.getLayoutParams().height = (int) (dpHeight * 1.2);

        assert product != null;
        title.setText(product.getTitle());
        String decrip = "Description: " + product.getText();
        description.setText(decrip);
        Picasso.with(getActivity())
                .load(IMAGE_BASE_URL + product.getImg())
                .into(productImage);

        recyclerReviews.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ProductDescriptionAdapter(productReviews);
        recyclerReviews.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFabBtnClicked(product.getId());
            }
        });
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        MainActivity.currentFragmentTag = PRODUCT_DESCR_FRAGMENT_TAG;
        listener = (OnFabListener) context;
    }


    private ArrayList<Review> reverseArrayList(ArrayList<Review> list) {
        ArrayList<Review> reverse = new ArrayList<>();

        for (int i = (list.size() - 1); i >= 0; i--) {
            reverse.add(list.get(i));
        }
        return reverse;
    }

    public interface OnFabListener {

        void onFabBtnClicked(int productId);
    }

}

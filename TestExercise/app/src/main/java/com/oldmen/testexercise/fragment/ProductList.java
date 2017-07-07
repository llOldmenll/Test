package com.oldmen.testexercise.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oldmen.testexercise.adapter.ProductListAdapter;
import com.oldmen.testexercise.R;
import com.oldmen.testexercise.activity.MainActivity;
import com.oldmen.testexercise.callback.OnCardClickListener;
import com.oldmen.testexercise.container.Product;

import java.util.ArrayList;

public class ProductList extends Fragment {

    private ArrayList<Product> productArrayList = new ArrayList<>();
    private RecyclerView recyclerProductList;
    OnCardClickListener listener;
    public static final String PRODUCT_LIST_FRAGMENT_TAG = "productList";


    public ProductList() {
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
        productArrayList = data.getParcelableArrayList(MainActivity.PRODUCT_LIST_KEY);
        return inflater.inflate(R.layout.fragment_product_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        recyclerProductList = (RecyclerView) view.findViewById(R.id.recycler_product_list);

        recyclerProductList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerProductList.setAdapter(new ProductListAdapter(productArrayList,getActivity(),listener));

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        MainActivity.currentFragmentTag = PRODUCT_LIST_FRAGMENT_TAG;
        listener = (OnCardClickListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}

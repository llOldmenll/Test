package com.oldmen.testexercise.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.oldmen.testexercise.utils.InternetConnection;
import com.oldmen.testexercise.callback.OnCardClickListener;
import com.oldmen.testexercise.container.PostRetrofitReview;
import com.oldmen.testexercise.container.PostRetrofitReviewResponse;
import com.oldmen.testexercise.fragment.ProductDescription;
import com.oldmen.testexercise.fragment.ProductList;
import com.oldmen.testexercise.R;
import com.oldmen.testexercise.container.Review;
import com.oldmen.testexercise.fragment.ReviewDialogFragment;
import com.oldmen.testexercise.container.ReviewsRating;
import com.oldmen.testexercise.container.UserContainer;
import com.oldmen.testexercise.utils.UserSessionUtils;
import com.oldmen.testexercise.api.ApiService;
import com.oldmen.testexercise.api.RetrofitClient;
import com.oldmen.testexercise.container.Product;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.oldmen.testexercise.fragment.ProductDescription.PRODUCT_DESCR_FRAGMENT_TAG;


public class MainActivity extends AppCompatActivity implements OnCardClickListener, ReviewDialogFragment.onDialogListener, ProductDescription.OnFabListener {

    public final static String PRODUCT_LIST_KEY = "Product List Array";
    public final static String PRODUCT_SAMPLE_KEY = "Product sample";
    public final static String PRODUCT_REVIEWS_KEY = "Product Description";
    public final static String PRODUCT_ID_KEY = "Product id";

    private TextView helloText;
    private Button navigBtnSignIn;
    private Button navigBtnLogOut;

    public static String currentFragmentTag;
    private ArrayList<Product> productArrayList = new ArrayList<>();
    private ArrayList<ReviewsRating> productRatingOnlyList = new ArrayList<>();
    private ArrayList<Review> productReviewsList = new ArrayList<>();
    private View parentView;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId){
            case R.id.item_update_info:
                if (currentFragmentTag == null || currentFragmentTag.equals(ProductList.PRODUCT_LIST_FRAGMENT_TAG)) {
                    ApiService api = RetrofitClient.getApiService(this);
                    Call<ArrayList<Product>> call = api.getProductList();
                    call.enqueue(new Callback<ArrayList<Product>>() {
                        @Override
                        public void onResponse(@NonNull Call<ArrayList<Product>> call, @NonNull Response<ArrayList<Product>> response) {
                            productArrayList = response.body();

                            if (productArrayList != null) {
                                for (final Product prd : productArrayList) {
                                    averageProductRating(prd);
                                }
                            }

                        }

                        @Override
                        public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                            Log.i("Fail", "Something wrone");
                        }
                    });
                } else {
                    getFragmentManager().popBackStack(currentFragmentTag, 1);
                }
                break;
            case R.id.item_registration:
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("Test", "onRestore");

        if (savedInstanceState != null) {
            if (savedInstanceState.getParcelableArrayList(PRODUCT_LIST_KEY) != null)
                productArrayList = savedInstanceState.getParcelableArrayList(PRODUCT_LIST_KEY);
            if (savedInstanceState.getParcelableArrayList(PRODUCT_REVIEWS_KEY) != null)
                productReviewsList = savedInstanceState.getParcelableArrayList(PRODUCT_REVIEWS_KEY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }



        Log.i("Test", "onCreate");
        parentView = findViewById(R.id.parent_layout);
        helloText = (TextView) findViewById(R.id.hello_text);
        navigBtnSignIn = (Button) findViewById(R.id.sign_in_navigation_btn);
        navigBtnLogOut = (Button) findViewById(R.id.log_out_navigation_btn);
        UserContainer curUser = UserSessionUtils.readSession(this);

        navigBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        navigBtnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSessionUtils.deleteSession(MainActivity.this);
                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        if (curUser.getUserToken() != null && !curUser.getUserToken().equals("") && !curUser.getUserToken().equals("nothing")) {
            String helloTxt = "Hello, " + curUser.getUserLogin();
            helloText.setText(helloTxt);
            navigBtnSignIn.setVisibility(View.INVISIBLE);
            navigBtnLogOut.setVisibility(View.VISIBLE);
        } else {
            helloText.setText(getString(R.string.hello_dear_user));
            navigBtnSignIn.setVisibility(View.VISIBLE);
            navigBtnLogOut.setVisibility(View.INVISIBLE);
        }

        if (InternetConnection.checkConnection(getApplicationContext())) {

            if (!isRegistraitionSkip()) {

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
                return;
            }
            if (currentFragmentTag == null || currentFragmentTag.equals(ProductList.PRODUCT_LIST_FRAGMENT_TAG)) {

                ApiService api = RetrofitClient.getApiService(this);
                Call<ArrayList<Product>> call = api.getProductList();
                call.enqueue(new Callback<ArrayList<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<ArrayList<Product>> call, @NonNull Response<ArrayList<Product>> response) {
                        productArrayList = response.body();

                        if (productArrayList != null) {
                            for (final Product prd : productArrayList) {
                                averageProductRating(prd);
                            }
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<ArrayList<Product>> call, @NonNull Throwable t) {
                        Log.i("Fail", "Something wrone");
                    }
                });
            } else {

                getFragmentManager().popBackStack(currentFragmentTag, 1);
            }
        } else {

            Dialog.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            };
            createAlertDialog("Internet connection not available!", listener);
            Snackbar.make(parentView, "Internet connection not available!", Snackbar.LENGTH_LONG).show();
        }

    }

    private void averageProductRating(final Product prd) {
        ApiService api = RetrofitClient.getApiService(this);
        Call<ArrayList<ReviewsRating>> call2 = api.getProductRating(prd.getId());
        call2.enqueue(new Callback<ArrayList<ReviewsRating>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ReviewsRating>> call, @NonNull Response<ArrayList<ReviewsRating>> response) {
                productRatingOnlyList = response.body();

                assert productRatingOnlyList != null;
                int count = 0;
                int sum = 0;
                double average;

                for (ReviewsRating rate : productRatingOnlyList) {
                    count++;
                    sum += rate.getRate();
                }

                average = (double) sum / count;
                prd.setRating(average);

                startProductListFragment();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<ReviewsRating>> call, @NonNull Throwable t) {

                Log.i("Fail", "Something wrong with average rating!");

            }
        });
    }

    private void startProductListFragment() {
        Bundle data = new Bundle();
        data.putParcelableArrayList(PRODUCT_LIST_KEY, productArrayList);
        ProductList productListFragment = new ProductList();
        productListFragment.setArguments(data);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, productListFragment, ProductList.PRODUCT_LIST_FRAGMENT_TAG)
                //.addToBackStack("1")
                .commit();
    }

    private void getAllReviews(final int productId) {
        ApiService api = RetrofitClient.getApiService(this);
        Call<ArrayList<Review>> call = api.getProductReviews(productId);
        call.enqueue(new Callback<ArrayList<Review>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Review>> call, @NonNull Response<ArrayList<Review>> response) {
                productReviewsList = response.body();
                startProductDescrFragment(productId - 1);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Review>> call, @NonNull Throwable t) {
                Log.i("Fail", "Something wrong with reviews!");
            }
        });
    }

    private void startProductDescrFragment(int position) {

        Bundle data = new Bundle();
        Product product = productArrayList.get(position);

        data.putParcelable(PRODUCT_SAMPLE_KEY, product);
        data.putSerializable(PRODUCT_REVIEWS_KEY, productReviewsList);

        ProductDescription productDescription = new ProductDescription();
        productDescription.setArguments(data);

        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); ++i) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, productDescription, PRODUCT_DESCR_FRAGMENT_TAG)
                .addToBackStack("2")
                .commit();
    }

    private boolean isRegistraitionSkip() {
        if (isUserSignedIn() || isSkipModeOn()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isSkipModeOn() {
        UserContainer userContainer = UserSessionUtils.readSession(this);
        String skipRegistrationMode = userContainer.getUserSkipRegistrMode();

        if (skipRegistrationMode.equals(UserSessionUtils.YES_KEY)) {
            return true;
        } else {
            return false;
        }

    }

    private boolean isUserSignedIn() {
        UserContainer userContainer = UserSessionUtils.readSession(this);
        String userToken = userContainer.getUserToken();

        if (userToken != null && !userToken.equals("") && !userToken.equals("nothing")) {
            return true;
        } else {
            return false;
        }

    }

    private void updateReviews(final int productId) {

        ApiService api = RetrofitClient.getApiService(this);
        Call<ArrayList<Review>> call = api.getProductReviews(productId);
        call.enqueue(new Callback<ArrayList<Review>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Review>> call, @NonNull Response<ArrayList<Review>> response) {
                productReviewsList = response.body();
                startProductDescrFragment(productId - 1);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Review>> call, @NonNull Throwable t) {
                Log.i("Fail", "Something wrong with reviews!");
            }
        });
    }

    @Override
    public void cardClicked(int productId) {
        getAllReviews(productId);
    }

    @Override
    public void onSubmitReviewClicked(String review, int rating, final int id) {

        Log.i("info", "Review: " + review);
        Log.i("info", "Review: " + String.valueOf(rating));

        PostRetrofitReview newReview = new PostRetrofitReview();
        newReview.setText(review);
        newReview.setRate(rating);

        ApiService api = RetrofitClient.getApiService(this);
        Call<PostRetrofitReviewResponse> call = api.postUserReview(id, newReview);
        call.enqueue(new Callback<PostRetrofitReviewResponse>() {
            @Override
            public void onResponse(@NonNull Call<PostRetrofitReviewResponse> call, @NonNull Response<PostRetrofitReviewResponse> response) {
                PostRetrofitReviewResponse resp = response.body();
                updateReviews(id);
            }

            @Override
            public void onFailure(@NonNull Call<PostRetrofitReviewResponse> call, @NonNull Throwable t) {

            }
        });

    }

    @Override
    public void onFabBtnClicked(int productId) {

        UserContainer user = UserSessionUtils.readSession(this);
        String token = user.getUserToken();

        if (token != null && !token.equals("") && !token.equals("nothing")) {
            Bundle data = new Bundle();
            data.putInt(PRODUCT_ID_KEY, productId);

            ReviewDialogFragment dialog = new ReviewDialogFragment();
            dialog.setArguments(data);
            dialog.show(this.getSupportFragmentManager(), "dialog");
        } else
            createNotUserDialog();
    }

    private void createNotUserDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("Sorry, You can't write review!" + "\nPlease Sign in first!");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.login), new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void createAlertDialog(String message, DialogInterface.OnClickListener listener) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", listener);
        alertDialog.show();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Log.i("Test", "onSaveInstanceState");

        if (productArrayList != null) {
            outState.putParcelableArrayList(PRODUCT_LIST_KEY, productArrayList);
        }

        if (productReviewsList != null) {
            outState.putParcelableArrayList(PRODUCT_REVIEWS_KEY, productReviewsList);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Log.i("Test", "onDestroy");
        currentFragmentTag = null;
    }
}

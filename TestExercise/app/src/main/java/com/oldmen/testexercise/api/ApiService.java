package com.oldmen.testexercise.api;


import com.oldmen.testexercise.container.PostRetrofitBody;
import com.oldmen.testexercise.container.PostRetrofitResponse;
import com.oldmen.testexercise.container.PostRetrofitReview;
import com.oldmen.testexercise.container.PostRetrofitReviewResponse;
import com.oldmen.testexercise.container.Review;
import com.oldmen.testexercise.container.ReviewsRating;
import com.oldmen.testexercise.container.Product;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/api/products/")
    Call<ArrayList<Product>> getProductList();


    @GET("api/reviews/{id}")
    Call<ArrayList<ReviewsRating>> getProductRating(@Path("id") int id);

    @GET("api/reviews/{id}")
    Call<ArrayList<Review>> getProductReviews(@Path("id") int id);

    @POST("/api/register/")
    Call<PostRetrofitResponse> postUserRegistration(@Body PostRetrofitBody body);

    @POST("/api/login/")
    Call<PostRetrofitResponse> postUserLogin(@Body PostRetrofitBody body);

    @POST("/api/reviews/{id}")
    Call<PostRetrofitReviewResponse> postUserReview (@Path("id") int id, @Body PostRetrofitReview review);
}

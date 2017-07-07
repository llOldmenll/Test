package com.oldmen.testexercise;


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

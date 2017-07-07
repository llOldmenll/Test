package com.oldmen.testexercise;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {

    private static final String ROOT_URL = "http://smktesting.herokuapp.com";
    private static String token;

    private static Interceptor mTokenInterceptor = new Interceptor() {
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            if (token != null) {
                Request.Builder requestBuilder = request.newBuilder()
                        .addHeader("Authorization", "Token " + token);
                Request newRequest = requestBuilder.build();

                return chain.proceed(newRequest);
            }
            return chain.proceed(request);

        }
    };

    private static OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(mTokenInterceptor)
            .build();


    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static ApiService getApiService(Context context) {

        UserContainer user = UserSessionUtils.readSession(context);
        String userToken = user.getUserToken();

        if(userToken != null && !userToken.equals("") && userToken != "nothing"){
            token = userToken;
        }

        return getRetrofitInstance().create(ApiService.class);
    }

}

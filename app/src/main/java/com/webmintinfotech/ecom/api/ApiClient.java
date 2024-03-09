package com.webmintinfotech.ecom.api;

import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class ApiClient {

    private static final String BASE_URL = "https://ecom.cosmicjewel.site/";
    private static final String APP_URL = BASE_URL + "api/";
    private static final long TIMEOUT = 60;

    public static ApiInterface getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);
        GsonBuilder gsonBuilder = new GsonBuilder().setLenient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APP_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .build();
        return retrofit.create(ApiInterface.class);
    }
}

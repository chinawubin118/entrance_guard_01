package com.ruitu.entrance_guard.model.api;

import com.ruitu.entrance_guard.Constant;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wubin on 2017/4/20.
 */

public class APIRetrofit {

    private static ApiService SERVICE;

    public static ApiService getDefault() {
        if (SERVICE == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
//                    .addInterceptor(new HeaderInterceptor())
                    .addInterceptor(logging)
                    .build();

            SERVICE = new Retrofit.Builder()
//                    .baseUrl(Constant.RETROFIT_URL)
                    .baseUrl(Constant.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build().create(ApiService.class);
        }
        return SERVICE;
    }

    private static ApiService SERVICE_01;

    public static ApiService getService_01() {
        if (null == SERVICE_01) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            SERVICE_01 = new Retrofit.Builder()
                    .baseUrl(Constant.URL_01)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build().create(ApiService.class);
        }

        return SERVICE_01;
    }
}

package com.wind.advertisementreceiveapplication.network;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wind.advertisementreceiveapplication.DisplayVideoActivity;
import com.wind.advertisementreceiveapplication.MainActivity;
import com.wind.advertisementreceiveapplication.network.api.DownloadVideoApi;
import com.wind.advertisementreceiveapplication.network.mapper.NetworkVideoResponseBodyMapper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhengzhe on 2017/12/14.
 */

public class Network {
    private static DownloadVideoApi mDownloadVideoApi;
    private static CallAdapter.Factory mRxjavaCallAdapterFactory = RxJava2CallAdapterFactory.create();
    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    public static void downloadVideo(String url, String name) {
        GsonBuilder gb = new GsonBuilder();
        Gson gson = gb.setLenient().create();
        if (mDownloadVideoApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.31.109:8080")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(mRxjavaCallAdapterFactory)
                    .client(mOkHttpClient)
                    .build();
            mDownloadVideoApi = retrofit.create(DownloadVideoApi.class);
        }
        mDownloadVideoApi.downVideo(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new NetworkVideoResponseBodyMapper(name))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        android.util.Log.d("zz", "Network + downloadVideo() + s = " + s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        android.util.Log.d("zz", "Network + downloadVideo() + error = " + throwable.toString());
                    }
                });
    }
}

package com.wind.advertisementreceiveapplication.network;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wind.advertisementreceiveapplication.DisplayVideoActivity;
import com.wind.advertisementreceiveapplication.MainActivity;
import com.wind.advertisementreceiveapplication.constants.Constants;
import com.wind.advertisementreceiveapplication.network.api.DownloadVideoApi;
import com.wind.advertisementreceiveapplication.network.mapper.NetworkVideoResponseBodyMapper;
import com.wind.advertisementreceiveapplication.service.PlayVideoService;

import java.io.File;

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

    public static void downloadVideo(final Context context, String url, final String name, final String playTime) {
        GsonBuilder gb = new GsonBuilder();
        Gson gson = gb.setLenient().create();
        if (mDownloadVideoApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
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
                        if ("success".equals(s)) {
                            startPlayVideoService(context, name, playTime);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        android.util.Log.d("zz", "Network + downloadVideo() + error = " + throwable.toString());
                    }
                });
    }

    private static void startPlayVideoService(Context context, String name, String playTime) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                + File.separator + "downloadVideo" + File.separator + name;
        Intent intent = new Intent(context, PlayVideoService.class);
        intent.putExtra(Constants.VIDEOPATH, path);
        intent.putExtra("current_video_play_time", playTime);
        context.startService(intent);
    }
}

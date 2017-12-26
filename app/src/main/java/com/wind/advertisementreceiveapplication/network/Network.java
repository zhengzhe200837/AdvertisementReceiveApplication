package com.wind.advertisementreceiveapplication.network;

import android.content.Context;
import android.content.Intent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wind.advertisementreceiveapplication.constants.Constants;
import com.wind.advertisementreceiveapplication.network.api.DownloadVideoApi;
import com.wind.advertisementreceiveapplication.network.api.UploadPlayStatusApi;
import com.wind.advertisementreceiveapplication.network.mapper.NetworkVideoResponseBodyMapper;
import com.wind.advertisementreceiveapplication.network.model.PostModelUploadPlayStatus;
import com.wind.advertisementreceiveapplication.network.model.ReceiveInfoFromServer;
import com.wind.advertisementreceiveapplication.service.PlayVideoService;
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
    private static UploadPlayStatusApi mUploadPlayStatusApi;

    public static void uploadPlayStatus(int orderStatus, String orderId) {
        GsonBuilder gb = new GsonBuilder();
        Gson gson = gb.setLenient().create();
        if (mUploadPlayStatusApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(mOkHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(mRxjavaCallAdapterFactory)
                    .build();
            mUploadPlayStatusApi = retrofit.create(UploadPlayStatusApi.class);
        }
        mUploadPlayStatusApi.uploadPlayStatus(new PostModelUploadPlayStatus(orderStatus, orderId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        android.util.Log.d("zz", "Network + uploadPlayStatus() + s = " + s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        android.util.Log.d("zz", "Network + uploadPlayStatus() + error = " + throwable.toString());
                    }
                });
    }

    public static void downloadVideo(final Context context, final ReceiveInfoFromServer rifs) {
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
        mDownloadVideoApi.downVideo(rifs.getUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new NetworkVideoResponseBodyMapper(rifs.getVideoName()))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        android.util.Log.d("zz", "Network + downloadVideo() + s = " + s);
                        if ("success".equals(s)) {
                            startPlayVideoService(context, rifs);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        android.util.Log.d("zz", "Network + downloadVideo() + error = " + throwable.toString());
                    }
                });
    }

    private static void startPlayVideoService(Context context, ReceiveInfoFromServer rifs) {
        Intent intent = new Intent(context, PlayVideoService.class);
        intent.putExtra(Constants.VIDEO_DATA, rifs);
        context.startService(intent);
    }
}

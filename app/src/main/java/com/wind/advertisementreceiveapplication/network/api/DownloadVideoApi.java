package com.wind.advertisementreceiveapplication.network.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by zhengzhe on 2017/12/14.
 */

public interface DownloadVideoApi {
    @GET
    Observable<ResponseBody> downVideo(@Url String url);
}

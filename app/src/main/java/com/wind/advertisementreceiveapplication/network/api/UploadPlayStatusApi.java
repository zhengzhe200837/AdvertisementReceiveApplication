package com.wind.advertisementreceiveapplication.network.api;

import com.wind.advertisementreceiveapplication.constants.Constants;
import com.wind.advertisementreceiveapplication.network.model.PostModelUploadPlayStatus;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by zhengzhe on 2017/12/26.
 */

public interface UploadPlayStatusApi {
    @POST(Constants.UPLOAD_PLAY_STATUS_INTERFACE)
    Observable<String> uploadPlayStatus(@Body PostModelUploadPlayStatus body);
}

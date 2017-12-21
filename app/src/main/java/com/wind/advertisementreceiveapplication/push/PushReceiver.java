package com.wind.advertisementreceiveapplication.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.wind.advertisementreceiveapplication.constants.Constants;
import com.wind.advertisementreceiveapplication.network.Network;
import com.wind.advertisementreceiveapplication.network.model.ReceiveInfoFromServer;
import com.wind.advertisementreceiveapplication.service.PlayVideoService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/7/2 0002.
 */
public class PushReceiver extends BroadcastReceiver {
    private Context mContext;

    //当收到消息，会回调onReceive()方法,onReceive中解析
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;

        Bundle bundle = intent.getExtras();
        android.util.Log.d("zz", "onReceive - " + intent.getAction());
        //注册
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            android.util.Log.d("zz", "push + 1");
            String title = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            android.util.Log.d("zz", "title = " + title);
            //收到自定义消息
        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            android.util.Log.d("zz", "push + 2");
            //自定义消息可以发送json数据
            android.util.Log.d("zz", "收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject jsonObject = new JSONObject(extras);
                String data = jsonObject.get("from").toString();
                Gson gson = new Gson();
                ReceiveInfoFromServer rifs = gson.fromJson(data, ReceiveInfoFromServer.class);
                String url = rifs.getUrl();
                String name = rifs.getVideoName();
                mCurrentVideoPlayTime = rifs.getPlayTime();
                Network.downloadVideo(url, name);
                android.util.Log.d("zz", "from = " + jsonObject.get("from"));
                android.util.Log.d("zz", "url = " + url + " name = " + name);
                android.util.Log.d("zz", "mCurrentVideoPlayTime = "  + mCurrentVideoPlayTime);
                startPlayVideoService(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            android.util.Log.d("zz", "push + 3");
            // 在这里可以做些统计，或者做些其他工作(通知栏被点击)
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            android.util.Log.d("zz", "push + 4");
            // 在这里可以自己写代码去定义用户点击后的行为
            //保存服务器推送下来的附加字段。这是个 JSON 字符串。
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject obj = new JSONObject(extras);
                String url = obj.getString("url");
                System.out.println("url："+url);
                //跳浏览器
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            android.util.Log.d("zz", "push + 5");
            android.util.Log.d("zz", "Unhandled intent - " + intent.getAction());
        }
    }

    private String mCurrentVideoPlayTime;
    private void startPlayVideoService(String name) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                + File.separator + "downloadVideo" + File.separator + name;
        Intent intent = new Intent(mContext, PlayVideoService.class);
        intent.putExtra(Constants.VIDEOPATH, path);
        intent.putExtra("current_video_play_time", mCurrentVideoPlayTime);
        mContext.startService(intent);
    }
}


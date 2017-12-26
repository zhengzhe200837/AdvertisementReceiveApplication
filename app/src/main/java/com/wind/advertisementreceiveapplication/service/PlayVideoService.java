package com.wind.advertisementreceiveapplication.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import com.wind.advertisementreceiveapplication.DisplayVideoActivity;
import com.wind.advertisementreceiveapplication.constants.Constants;
import com.wind.advertisementreceiveapplication.network.model.ReceiveInfoFromServer;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengzhe on 2017/12/14.
 */

public class PlayVideoService extends Service {
    private AlarmManager mAlarmManager;
    private String mCurrentVideoPlayTime;
    private long mCurrentVideoConvertPlayTime;
    private Map<String, ReceiveInfoFromServer> mMap = new HashMap<>();
    private List<Map.Entry<String,ReceiveInfoFromServer>> mList;
    private ReceiveInfoFromServer mVideoData;

    private void mapSort(Map<String, ReceiveInfoFromServer> map) {
        mList = new ArrayList<Map.Entry<String, ReceiveInfoFromServer>>(map.entrySet());
        Collections.sort(mList,new Comparator<Map.Entry<String, ReceiveInfoFromServer>>() {
            //升序排序
            public int compare(Map.Entry<String, ReceiveInfoFromServer> o1,
                               Map.Entry<String, ReceiveInfoFromServer> o2) {
                if (convertTime(o1.getKey()) > convertTime(o2.getKey())) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    @Override
    public void onCreate() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        android.util.Log.d("zz", "PlayVideoService + onStartCommand() + has + minimum_time = " + intent.hasExtra(Constants.MINIMUM_TIME));
        android.util.Log.d("zz", "PlayVideoService + onStartCommand() + has + current_video_play_time = " + intent.hasExtra("current_video_play_time"));
        android.util.Log.d("zz", "PlayVideoService + onStartCommand() + returnkey = " + intent.getStringExtra(Constants.MINIMUM_TIME));
        if (intent != null && intent.hasExtra(Constants.MINIMUM_TIME)) {
            mMap.remove(intent.getStringExtra(Constants.MINIMUM_TIME));
        }

        if (intent != null) {
            if (intent.hasExtra(Constants.VIDEO_DATA)) {
                mVideoData = (ReceiveInfoFromServer)intent.getParcelableExtra(Constants.VIDEO_DATA);
                mCurrentVideoPlayTime = mVideoData.getPlayTime();
                mMap.put(mCurrentVideoPlayTime, mVideoData);
            }
        } else {
            android.util.Log.d("zz", "PlayVideoService + onStartCommand() + intent == null");
        }

        mapSort(mMap);
        for(Map.Entry<String, ReceiveInfoFromServer> entry : mList) {
            String key = entry.getKey();
            ReceiveInfoFromServer value = entry.getValue();
            android.util.Log.d("zz", "PlayVideoService + onStartCommand() + key = " + key);
            android.util.Log.d("zz", "PlayVideoService + onStartCommand() + value = " + value.toString());
        }

        if (mList.size() > 0) {
            String minimumkey = mList.get(0).getKey();
            ReceiveInfoFromServer minimumValue = mList.get(0).getValue();
            android.util.Log.d("zz", "PlayVideoService + onStartCommand() + minimumkey = " + minimumkey);
            android.util.Log.d("zz", "PlayVideoService + onStartCommand() + minimumValue = " + minimumValue.toString());

            mCurrentVideoConvertPlayTime = convertTime(minimumkey);
            if (mAlarmManager == null) {
                mAlarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
            }

            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    + File.separator + "downloadVideo" + File.separator + minimumValue.getVideoName();
            int playTimes = minimumValue.getPlayTimes();

            Intent it = new Intent(this, DisplayVideoActivity.class);
            it.putExtra(Constants.MINIMUM_TIME, minimumkey);
            it.putExtra(Constants.VIDEOPATHKEY, path);
            it.putExtra(Constants.VIDEO_PLAY_TIMES, playTimes);
            PendingIntent pi = PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);  //PendingIntent.FLAG_UPDATE_CURRENT 次flag会更新Intent
//            PendingIntent pi = PendingIntent.getActivity(this, 0, it, 0);  //flag 为0时不会更新itent,导致从activity返回的数据不变
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, mCurrentVideoConvertPlayTime, pi);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private long convertTime(String time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        Date timeDate = null;
        try {
            timeDate = dateFormat.parse(time);
        } catch (Exception e) {
            android.util.Log.d("zz", "PlayVideoService + convertTime() + error = " + e.toString());
        }
        return timeDate.getTime();
    }
}

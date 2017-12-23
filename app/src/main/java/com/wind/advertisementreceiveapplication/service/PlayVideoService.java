package com.wind.advertisementreceiveapplication.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.wind.advertisementreceiveapplication.DisplayVideoActivity;
import com.wind.advertisementreceiveapplication.MainActivity;
import com.wind.advertisementreceiveapplication.constants.Constants;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by zhengzhe on 2017/12/14.
 */

public class PlayVideoService extends Service {
    private AlarmManager mAlarmManager;
    private String mPath = null;
    private String mCurrentVideoPlayTime;
    private long mCurrentVideoConvertPlayTime;
    private Map<String, String> mMap = new HashMap<>();
    private List<Map.Entry<String,String>> mList;

    private void mapSort(Map<String, String> map) {
        mList = new ArrayList<Map.Entry<String,String>>(map.entrySet());
        Collections.sort(mList,new Comparator<Map.Entry<String,String>>() {
            //升序排序
            public int compare(Map.Entry<String, String> o1,
                               Map.Entry<String, String> o2) {
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
        android.util.Log.d("zz", "PlayVideoService + onStartCommand() + has + minimum_time = " + intent.hasExtra("minimum_time"));
        android.util.Log.d("zz", "PlayVideoService + onStartCommand() + has + current_video_play_time = " + intent.hasExtra("current_video_play_time"));
        android.util.Log.d("zz", "PlayVideoService + onStartCommand() + returnkey = " + intent.getStringExtra("minimum_time"));
        if (intent != null && intent.hasExtra("minimum_time")) {
            mMap.remove(intent.getStringExtra("minimum_time"));
        }

        if (intent != null) {
            if (intent.hasExtra(Constants.VIDEOPATH) && intent.hasExtra("current_video_play_time")) {
                mPath = intent.getStringExtra(Constants.VIDEOPATH);
                android.util.Log.d("zz", "PlayVideoService + onStartCommand() + mPath = " + mPath);
                mCurrentVideoPlayTime = intent.getStringExtra("current_video_play_time");
                mMap.put(mCurrentVideoPlayTime, mPath);
            }
        } else {
            android.util.Log.d("zz", "PlayVideoService + onStartCommand() + intent == null");
        }

        mapSort(mMap);
        for(Map.Entry<String, String> entry : mList) {
            String key = entry.getKey();
            String value = entry.getValue();
            android.util.Log.d("zz", "PlayVideoService + onStartCommand() + key = " + key + " value = " + value);
        }

        if (mList.size() > 0) {
            String minimumkey = mList.get(0).getKey();
            String minimumValue = mList.get(0).getValue();
            android.util.Log.d("zz", "PlayVideoService + onStartCommand() + minimumkey = " + minimumkey + " minimumValue = " + minimumValue);

            mCurrentVideoConvertPlayTime = convertTime(minimumkey);
            if (mAlarmManager == null) {
                mAlarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
            }
            Intent it = new Intent(this, DisplayVideoActivity.class);
            it.putExtra("minimum_time", minimumkey);
            it.putExtra(DisplayVideoActivity.VIDEOPATHKEY, minimumValue);
            PendingIntent pi = PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);  //PendingIntent.FLAG_UPDATE_CURRENT 次flag会更新Intent
//            PendingIntent pi = PendingIntent.getActivity(this, 0, it, 0);  //flag 为0时不会更新itent,导致从activity返回的数据不变
            android.util.Log.d("zz", "PlayVideoService + onStartCommand() + mCurrentVideoConvertPlayTime = " + mCurrentVideoConvertPlayTime);
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

    //上传一个视频
//     if (intent != null) {
//        mPath = intent.getStringExtra(Constants.VIDEOPATH);
//        mCurrentVideoPlayTime = intent.getStringExtra("current_video_play_time");
//        mMap.put(mCurrentVideoPlayTime, mPath);
//    } else {
//        android.util.Log.d("zz", "PlayVideoService + onStartCommand() + intent == null");
//    }
//    mCurrentVideoConvertPlayTime = convertTime(mCurrentVideoPlayTime);
//        if (mAlarmManager == null) {
//        mAlarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
//    }
//    Intent it = new Intent(this, DisplayVideoActivity.class);
//    it.putExtra(DisplayVideoActivity.VIDEOPATHKEY, mPath);
//    PendingIntent pi = PendingIntent.getActivity(this, 0, it, 0);
//    mAlarmManager.set(AlarmManager.RTC_WAKEUP, mCurrentVideoConvertPlayTime, pi);
}

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
import java.util.Date;

/**
 * Created by zhengzhe on 2017/12/14.
 */

public class PlayVideoService extends Service {
    private AlarmManager mAlarmManager;
    private String mPath = null;
    private String mCurrentVideoPlayTime;
    private long mCurrentVideoConvertPlayTime;

    @Override
    public void onCreate() {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPath = intent.getStringExtra(Constants.VIDEOPATH);
        mCurrentVideoPlayTime = intent.getStringExtra("current_video_play_time");
        android.util.Log.d("zz", "PlayVideoService + onStartCommand() + mPath = " + mPath);
        android.util.Log.d("zz", "PlayVideoService + onStartCommand() + mCurrentVideoPlayTime = " + mCurrentVideoPlayTime);
        convertTime(mCurrentVideoPlayTime);
        if (mAlarmManager == null) {
            mAlarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        }
        Intent it = new Intent(this, DisplayVideoActivity.class);
        it.putExtra(DisplayVideoActivity.VIDEOPATHKEY, mPath);
        PendingIntent pi = PendingIntent.getActivity(this, 0, it, 0);
        android.util.Log.d("zz", "PlayVideoService + onStartCommand() + currentTime = " + System.currentTimeMillis());
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, mCurrentVideoConvertPlayTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void convertTime(String time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        Date timeDate = null;
        try {
            timeDate = dateFormat.parse(time);
        } catch (Exception e) {
            android.util.Log.d("zz", "PlayVideoService + convertTime() + error = " + e.toString());
        }
        mCurrentVideoConvertPlayTime = timeDate.getTime();
        android.util.Log.d("zz", "PlayVideoService + convertTime() + mConvertPlayTime = " + mCurrentVideoConvertPlayTime);
    }
}

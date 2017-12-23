package com.wind.advertisementreceiveapplication;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.wind.advertisementreceiveapplication.constants.Constants;
import com.wind.advertisementreceiveapplication.service.PlayVideoService;

/**
 * Created by zhengzhe on 2017/12/14.
 */

public class DisplayVideoActivity extends Activity implements SurfaceHolder.Callback {
    private SurfaceView mSurfaceView;
    private String mVideoPath;
    private String mMinimunTime;
    public static final String VIDEOPATHKEY = "VIDEOPATHKEY";
    private SurfaceHolder mSurfaceHolder;
    private MediaPlayer mMediaPlayer;
    private Display mDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        android.util.Log.d("zz", "DisplayVideoActivity + onCreate()");
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.display_video_activty_layout);
        mSurfaceView = (SurfaceView)findViewById(R.id.surface_view);
        mVideoPath = getIntent().getStringExtra(VIDEOPATHKEY);
        //播放多个视频
        mMinimunTime = getIntent().getStringExtra("minimum_time");
        android.util.Log.d("zz", "DisplayVideoActivity + mMinimunTime = " + mMinimunTime);
        android.util.Log.d("zz", "DisplayVideoActivity + mVideoPath = " + mVideoPath);
        initMediaPlayer();
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mDisplay = this.getWindowManager().getDefaultDisplay();

    }

    private void startService() {
        Intent intent = new Intent(this, PlayVideoService.class);
        intent.putExtra("minimum_time", mMinimunTime);
        startService(intent);
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                FrameLayout.LayoutParams lllp = (FrameLayout.LayoutParams)mSurfaceView.getLayoutParams();
                mMediaPlayer.start();
            }
        });
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                android.util.Log.d("zz", "DisplayVideoActivity + onSeekComplete");
            }
        });
        mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

            }
        });
        try {
            mMediaPlayer.setDataSource(mVideoPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mMediaPlayer.setDisplay(holder);
        mMediaPlayer.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    protected void onDestroy() {
        android.util.Log.d("zz", "DisplayVideoActivity + onDestroy()");
        if (mMinimunTime != null) {
            startService();
        }
        super.onDestroy();
    }
}
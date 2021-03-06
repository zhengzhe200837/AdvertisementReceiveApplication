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
import com.wind.advertisementreceiveapplication.network.Network;
import com.wind.advertisementreceiveapplication.service.PlayVideoService;

/**
 * Created by zhengzhe on 2017/12/14.
 */

public class DisplayVideoActivity extends Activity implements SurfaceHolder.Callback {
    private SurfaceView mSurfaceView;
    private String mVideoPath;
    private String mMinimunTime;
    private SurfaceHolder mSurfaceHolder;
    private MediaPlayer mMediaPlayer;
    private Display mDisplay;
    private int mTotalPlayTimes = 1;
    private int mCurrentPlayTimes = 0;
    private String mOrderId;

    private boolean isPublicVideo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        android.util.Log.d("zz", "DisplayVideoActivity + onCreate()");
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.display_video_activty_layout);
        mSurfaceView = (SurfaceView)findViewById(R.id.surface_view);
        mVideoPath = getIntent().getStringExtra(Constants.VIDEOPATHKEY);
        if (getIntent().hasExtra(Constants.VIDEO_PLAY_TIMES)
                && getIntent().hasExtra(Constants.VIDEO_ORDER_ID)
                && getIntent().hasExtra(Constants.MINIMUM_TIME)) {
            mTotalPlayTimes = getIntent().getIntExtra(Constants.VIDEO_PLAY_TIMES, 1);
            mOrderId = getIntent().getStringExtra(Constants.VIDEO_ORDER_ID);
            //播放多个视频
            mMinimunTime = getIntent().getStringExtra(Constants.MINIMUM_TIME);
            android.util.Log.d("zz", "DisplayVideoActivity + mMinimunTime = " + mMinimunTime);
            android.util.Log.d("zz", "DisplayVideoActivity + mTotalPlayTimes = " + mTotalPlayTimes);
            android.util.Log.d("zz", "DisplayVideoActivity + mOrderId = " + mOrderId);
        }

        android.util.Log.d("zz", "DisplayVideoActivity + mVideoPath = " + mVideoPath);

        if (getIntent().hasExtra(Constants.PUBLIC_VIDEO)) {
            isPublicVideo = getIntent().getBooleanExtra(Constants.PUBLIC_VIDEO, false);
        }
        android.util.Log.d("zz", "DisplayVideoActivity + isPublicVideo = " + isPublicVideo);

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mDisplay = this.getWindowManager().getDefaultDisplay();
    }

    private void startService() {
        Intent intent = new Intent(this, PlayVideoService.class);
        intent.putExtra(Constants.MINIMUM_TIME, mMinimunTime);
        startService(intent);
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        if (isPublicVideo) {
            mMediaPlayer.setLooping(true);
        }
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!isPublicVideo) {
                    mCurrentPlayTimes++;
                    android.util.Log.d("zz", "DisplayVideoActivity + onCompletion() + mCurrentPlayTimes = " + mCurrentPlayTimes);
                    android.util.Log.d("zz", "DisplayVideoActivity + onCompletion() + mTotalPlayTimes = " + mTotalPlayTimes);
                    if (mCurrentPlayTimes < mTotalPlayTimes) {
                        mMediaPlayer.start();
                    }
                    if (mCurrentPlayTimes == mTotalPlayTimes) {
                        mCurrentPlayTimes = 0;
                        mMediaPlayer.release();

                        Network.uploadPlayStatus(1, mOrderId);  //已播放
                        mOrderId = null;

                        finish();
                    }
                }
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                android.util.Log.d("zz", "DisplayVideoActivity + onError() + what = " + what + " extra = " + extra);
                return false;
            }
        });
        mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                android.util.Log.d("zz", "DisplayVideoActivity + onInfo() + what = " + what + " extra = " + extra);
                return false;
            }
        });
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                android.util.Log.d("zz", "DisplayVideoActivity + onPrepared() + isPublicVideo = " + isPublicVideo + " stopLocation = " + stopLocation);
                if (isPublicVideo) {
                    mMediaPlayer.seekTo(stopLocation);
                }
                mMediaPlayer.start();
            }
        });
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
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
        android.util.Log.d("zz", "DisplayVideoActivity + surfaceCreated() + isPublicVideo = " + isPublicVideo);
        mMediaPlayer.setDisplay(holder);
        mMediaPlayer.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    private int stopLocation = -1;
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        android.util.Log.d("zz", "DisplayVideoActivity + surfaceDestroyed + isPublicVideo = " + isPublicVideo);
        if (isPublicVideo) {
            stopLocation = mMediaPlayer.getCurrentPosition();
            android.util.Log.d("zz", "DisplayVideoActivity + surfaceDestroyed() + isPublicVideo = " + isPublicVideo + " stopLocation = " + stopLocation);
            mMediaPlayer.release();
        }
    }

    @Override
    protected void onResume() {
        initMediaPlayer();
        android.util.Log.d("zz", "DisplayVideoActivity + onResume() + isPublicVideo = " + isPublicVideo);
        super.onResume();
    }

    @Override
    protected void onPause() {
        android.util.Log.d("zz", "DisplayVideoActivity + onPause() + isPublicVideo = " + isPublicVideo);
        super.onPause();
    }

    @Override
    protected void onStop() {
        android.util.Log.d("zz", "DisplayVideoActivity + onStop() + isPublicVideo = " + isPublicVideo);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        android.util.Log.d("zz", "DisplayVideoActivity + onDestroy() + isPublicVideo = " + isPublicVideo);
        if (!isPublicVideo && mMinimunTime != null) {
            startService();
        }
        super.onDestroy();
    }
}
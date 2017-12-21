package com.wind.advertisementreceiveapplication;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Created by zhengzhe on 2017/12/14.
 */

public class DisplayVideoActivity extends Activity implements SurfaceHolder.Callback {
    private SurfaceView mSurfaceView;
    private String mVideoPath;
    public static final String VIDEOPATHKEY = "VIDEOPATHKEY";
    private SurfaceHolder mSurfaceHolder;
    private MediaPlayer mMediaPlayer;
    private Display mDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.display_video_activty_layout);
        mSurfaceView = (SurfaceView)findViewById(R.id.surface_view);
        mVideoPath = getIntent().getStringExtra(VIDEOPATHKEY);
        android.util.Log.d("zz", "DisplayVideoActivity + mVideoPath = " + mVideoPath);
        initMediaPlayer();
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mDisplay = this.getWindowManager().getDefaultDisplay();
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
                int originalVideoWidth = mMediaPlayer.getVideoWidth();
                int originalVideoHeight = mMediaPlayer.getVideoHeight();
                FrameLayout.LayoutParams lllp = (FrameLayout.LayoutParams)mSurfaceView.getLayoutParams();
//                if(originalVideoWidth > mDisplay.getWidth() || originalVideoHeight > mDisplay.getHeight()) {
//                    float wRatio = (float)originalVideoWidth/(float)mDisplay.getWidth();
//                    float hRatio = (float)originalVideoHeight/(float)mDisplay.getHeight();
//                    float ratio = Math.max(wRatio, hRatio);
//                    int videoWidth = (int)Math.ceil((float)originalVideoWidth/ratio);
//                    int videoHeight = (int)Math.ceil((float)originalVideoHeight/ratio);
//                    lllp.width = videoWidth;
//                    lllp.height = videoHeight;
//                    mSurfaceView.setLayoutParams(lllp);
//                    mMediaPlayer.start();
//                } else {
//                    lllp.width = originalVideoWidth;
//                    lllp.height = originalVideoHeight;
//                    mSurfaceView.setLayoutParams(lllp);
//                    mMediaPlayer.start();
//                }
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
}
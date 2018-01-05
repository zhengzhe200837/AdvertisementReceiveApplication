package com.wind.advertisementreceiveapplication;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wind.advertisementreceiveapplication.constants.Constants;

import java.io.File;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                }, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    int[] grantResults) {
        android.util.Log.d("zz", "MainActivity + onRequestPermissionsResult + requestCode = " + requestCode);
        for(int i = 0; i < grantResults.length; i++) {
            android.util.Log.d("zz", "MainActivity + onRequestPermissionsResult + i = " + i + " result = " + grantResults[i]);
        }
        if (requestCode == 1) {
            if (grantResults[0] == 0 && grantResults[1] == 0) {
                Intent it = new Intent(this, DisplayVideoActivity.class);
                android.util.Log.d("zz", "MainActivity + externalStorage = " + Environment.getExternalStorageDirectory());
                String publicVideoPath = Environment.getExternalStorageDirectory()
                        + File.separator + "bluetooth" + File.separator + "test.mp4";
                it.putExtra(Constants.VIDEOPATHKEY, publicVideoPath);
                it.putExtra(Constants.PUBLIC_VIDEO, true);
                startActivity(it);
                finish();
            }
        }
    }
}

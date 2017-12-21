package com.wind.advertisementreceiveapplication;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestPermission();
//        setContentView(R.layout.activity_main);
    }

    private void requestPermission() {
        android.util.Log.d("zz", "MainActivity + requestPermission()");
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, 1);
    }
}

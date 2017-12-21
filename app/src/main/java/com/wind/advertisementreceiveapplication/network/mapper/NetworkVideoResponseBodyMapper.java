package com.wind.advertisementreceiveapplication.network.mapper;

import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Created by zhengzhe on 2017/12/13.
 */

public class NetworkVideoResponseBodyMapper implements Function<ResponseBody, String> {
    private String result = null;
    private String name = null;
    public NetworkVideoResponseBodyMapper(String name) {
        this.name = name;
    }

    @Override
    public String apply(ResponseBody responseBody) {
        writeResponseBodyToDisk(responseBody);
        return result;
    }
    private void writeResponseBodyToDisk(ResponseBody responseBody) {
        File videoPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                .getAbsolutePath() + File.separator + "downloadVideo");
        android.util.Log.d("zz", "NetworkVideoResponseBodyMapper + path + exists = " + videoPath.exists());
        if (!videoPath.exists()) {
            videoPath.mkdir();
        }
        android.util.Log.d("zz", "NetworkVideoResponseBodyMapper + exists = " + videoPath.exists());
        File videoFile = new File(videoPath, name);
        long fileSize = responseBody.contentLength();  //视频大小
        InputStream is = null;
        OutputStream os = null;
        byte[] fileReader = new byte[4096];
        long videoFileTotalSize = 0;
        try {
            videoFile.createNewFile();
            is = responseBody.byteStream();
            os = new FileOutputStream(videoFile);
            int readSize = 0;
            while(readSize != -1) {
                readSize = is.read(fileReader);
                if (readSize != -1) {
                    os.write(fileReader, 0, readSize);
                    videoFileTotalSize += readSize;
                }
            }
            result = "success";
            os.flush();
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        } catch (Exception e) {
            result = "error";
            android.util.Log.d("zz", "NetworkVideoResponseBodyMapper + error = " + e.toString());
        } finally {
        }
    }
}

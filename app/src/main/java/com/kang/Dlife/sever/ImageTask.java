package com.kang.Dlife.sever;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by regan on 2018/3/5.
 */

public class ImageTask extends AsyncTask<Object, Integer, Bitmap> {

    private final static String TAG = "ImageTask";
    private String url;
    private String json;
    private int imageSize;
    //private WeakReference<Bitmap> bitmapWeakReference;    // WeakReference物件不會阻止參照到的實體被回收

//    ImageTask(String url, String json, int imageSize) {
//
//        this(url, json, imageSize, null);
//    }

    public ImageTask(String url, String json, int imageSize) {
        this.url = url;
        this.json = json;
        this.imageSize = imageSize;
        //this.bitmapWeakReference = new WeakReference<>(bitmap);
    }


    // 背景執行
    @Override
    protected Bitmap doInBackground(Object... objects) {
        return getRemoteImage();
    }

    private Bitmap getRemoteImage() {
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true); // allow inputs
            connection.setDoOutput(true); // allow outputs
            connection.setUseCaches(false); // do not use a cached copy
            connection.setRequestMethod("POST");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            bw.write(json);
            Log.d(TAG, "output: " + json);
            bw.close();

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                bitmap = BitmapFactory.decodeStream( new BufferedInputStream(connection.getInputStream()));
            } else {
                Log.d(TAG, "response code: " + responseCode);
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return bitmap;
    }

//    @Override
//    protected void onPostExecute(Bitmap bitmap) {
//        Bitmap thisbitmap = bitmapWeakReference.get();
//        if (isCancelled() || thisbitmap == null) {
//            return;
//        }
//        if (bitmap != null) {
//            thisbitmap = bitmap;
//        } else {
////            imageView.setImageResource(R.drawable.ex_photo);
//        }
//    }
    

}

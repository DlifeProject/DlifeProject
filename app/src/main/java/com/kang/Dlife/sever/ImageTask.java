package com.kang.Dlife.sever;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by regan on 2018/3/5.
 */

public class ImageTask extends AsyncTask<Object, Integer, Bitmap> {

    private final static String TAG = "ImageTask";
    private String url;
    private int id, imageSize;
    private WeakReference<ImageView> imageViewWeakReference;    // WeakReference物件不會阻止參照到的實體被回收


    // 背景執行
    @Override
    protected Bitmap doInBackground(Object... objects) {
        return null;
    }

    //
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
    }
}

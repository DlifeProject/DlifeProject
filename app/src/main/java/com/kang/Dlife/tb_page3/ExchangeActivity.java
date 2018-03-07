package com.kang.Dlife.tb_page3;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kang.Dlife.Common;
import com.kang.Dlife.R;
import com.kang.Dlife.sever.MyTask;

import java.util.Hashtable;


public class ExchangeActivity extends AppCompatActivity {

    private ImageButton ibBack;
    private TextView tvHeader, tvFriendShareCategory, tvMyShareCategory, tvSend;
    private ImageView ivFriendPhoto, ivMyPhoto, iv_previous, iv_next;
    private Hashtable<String,Integer> PhotoSKHashTable = new CategoryPhotoHashTable().getinit();
    private Hashtable<String, Bitmap> categoryPhotoHashTable = new Hashtable<String, Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3_friend_exchange);

        // get my shareable category list [category , photo sk]
        getMyShareableList();

        // asyn get my shareable category photo [photo sk , Bitmap]
        asynCategoryPhoto();

        findView(this);


    }

    private void getMyShareableList() {

//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("action", "MyShareableCateList");
//        jsonObject.addProperty("account", Common.getAccount(Context.this));
//        jsonObject.addProperty("password", Common.getPWD(Context.this));
//        String url = Common.URL + Common.FRIEND;
//
//        MyTask getListJson = new MyTask(url,jsonObject.toString());


    }

    private void asynCategoryPhoto() {
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("action", "MyShareableCatePhoto");
//        jsonObject.addProperty("account", Common.getAccount(Context.this));
//        jsonObject.addProperty("password", Common.getPWD(Context.this));
//        String url = Common.URL + Common.FRIEND;
        
    }



    private void findView(ExchangeActivity exchangeActivity) {

        ibBack = (ImageButton) findViewById(R.id.ibBack);
        // back func...

        tvHeader = (TextView) findViewById(R.id.tvHeader);
        tvFriendShareCategory = (TextView) findViewById(R.id.tvFriendShareCategory);
        // func

        tvMyShareCategory = (TextView) findViewById(R.id.tvMyShareCategory);
        tvSend = (TextView) findViewById(R.id.tvSend);
        ivFriendPhoto = (ImageView) findViewById(R.id.ivFriendPhoto);
        ivMyPhoto = (ImageView) findViewById(R.id.ivMyPhoto);
        iv_previous = (ImageView) findViewById(R.id.iv_previous);
        iv_next = (ImageView) findViewById(R.id.iv_next);
    }


}


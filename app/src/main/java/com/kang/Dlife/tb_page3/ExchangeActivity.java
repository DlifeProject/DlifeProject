package com.kang.Dlife.tb_page3;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kang.Dlife.Common;
import com.kang.Dlife.R;
import com.kang.Dlife.sever.ImageTask;
import com.kang.Dlife.sever.MyTask;
import com.kang.Dlife.tb_page2.CategorySum;
import com.kang.Dlife.tb_page2.diary_view.PhotoSpot;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class ExchangeActivity extends AppCompatActivity {

    private ImageButton ibBack;
    private TextView tvHeader, tvFriendShareCategory, tvMyShareCategory, tvSend;
    private ImageView ivFriendPhoto, ivMyPhoto, iv_previous, iv_next;
    private RelativeLayout rlMyshare;
    private List<CategorySum> categorySums;
    private ArrayList<MyCategoryPhotoSK> photoSKList = new  ArrayList<MyCategoryPhotoSK>();
    private Hashtable<String, Bitmap> categoryPhotoHashTable = new Hashtable<String, Bitmap>();
    private int photoSKHashTableIndex = -1;

    private int imageSize;
    private ImageTask imageTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3_friend_exchange);

        rlMyshare = (RelativeLayout) findViewById(R.id.rlMyshare);
        ivMyPhoto = (ImageView) findViewById(R.id.ivMyPhoto);
        findView(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        imageSize = 9999999;
        // get my shareable category list [category , photo sk]
        getMyShareAbleList(this);

    }

    private void getMyShareAbleList(Context context) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "MyShareAbleCateList");
        jsonObject.addProperty("account", Common.getAccount(context));
        jsonObject.addProperty("password", Common.getPWD(context));
        String url = Common.URL + Common.FRIEND;

        MyTask getListJson = new MyTask(url,jsonObject.toString());

        String inStr = null;

        try {
            inStr = getListJson.execute().get().trim();
            if(inStr.isEmpty() == false && !inStr.equals("")){

                Gson gson = new Gson();
                JsonObject friendListJsonObject = gson.fromJson(inStr, JsonObject.class);
                String friendListString =  friendListJsonObject.get("MyShareAbleCateList").getAsString();
                JsonArray myShareAbleArray = gson.fromJson(friendListString, JsonArray.class);

                Type tempFriendList = new TypeToken<List<CategorySum>>() {}.getType();
                categorySums = new Gson().fromJson(myShareAbleArray, tempFriendList);

                for(int i=0; i < categorySums.size(); i++){

                    MyCategoryPhotoSK myCategoryPhotoSK = new MyCategoryPhotoSK();
                    myCategoryPhotoSK.category = categorySums.get(i).getCategoryType();
                    myCategoryPhotoSK.photoSK = categorySums.get(i).getDiaryPhotoSK();
                    photoSKList.add(myCategoryPhotoSK);
                    //asynCategoryPhoto(context,i);
                }

                asynCategoryPhoto(context);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void asynCategoryPhoto(Context context) throws ExecutionException, InterruptedException {

        String url = Common.URL + Common.WEBPHOTO;

        for(int i = 0;i < photoSKList.size(); i++){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getImage");
            jsonObject.addProperty("account", Common.getAccount(context));
            jsonObject.addProperty("password", Common.getPWD(context));
            jsonObject.addProperty("imageSize", imageSize);
            jsonObject.addProperty("id", photoSKList.get(i).photoSK);
            String json = jsonObject.toString();

            Bitmap bitmap = null;

            imageTask = new ImageTask( url, json, imageSize);
            bitmap = imageTask.execute().get();
            categoryPhotoHashTable.put(photoSKList.get(i).category,bitmap);
        }
        
    }

    private void findView(ExchangeActivity exchangeActivity) {

        ibBack = (ImageButton) findViewById(R.id.ibBack);
        tvHeader = (TextView) findViewById(R.id.tvHeader);
        tvFriendShareCategory = (TextView) findViewById(R.id.tvFriendShareCategory);
        // func

        tvMyShareCategory = (TextView) findViewById(R.id.tvMyShareCategory);
        tvSend = (TextView) findViewById(R.id.tvSend);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!tvMyShareCategory.getText().equals("choose a category")){

                    toShareDiary(ExchangeActivity.this);

                }else{
                    Common.showToast(ExchangeActivity.this ,"choose a category");
                }
                //sendShareCategory()
            }
        });
        ivFriendPhoto = (ImageView) findViewById(R.id.ivFriendPhoto);
        iv_previous = (ImageView) findViewById(R.id.iv_previous);
        iv_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMyShare("previous");
            }
        });
        iv_next = (ImageView) findViewById(R.id.iv_next);
        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMyShare("next");
            }
        });
    }

    private void toShareDiary(Context context) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "toRequestShare");
        jsonObject.addProperty("account", Common.getAccount(context));
        jsonObject.addProperty("password", Common.getPWD(context));
        jsonObject.addProperty("shareCategory", tvMyShareCategory.getText().toString());

        String url = Common.URL + Common.FRIEND;
        MyTask getListJson = new MyTask(url,jsonObject.toString());
        String inStr = "";
        try {
            inStr = getListJson.execute().get().trim();
            Gson gson = new Gson();
            JsonObject outJsonObject = gson.fromJson(inStr, JsonObject.class);
            String ltString = outJsonObject.get("toRequestShare").getAsString();

            Type tySum = new TypeToken<CategorySum>() {
            }.getType();
            CategorySum categorySum = new Gson().fromJson(ltString, tySum);
            if(categorySum.getDiaryPhotoSK() > 0 ){
                String urlPhoto = Common.URL + Common.WEBPHOTO;
                SpotGetImageTask spotGetImageTask = new SpotGetImageTask(urlPhoto, categorySum.getDiaryPhotoSK(), imageSize, ivFriendPhoto);
                spotGetImageTask.execute();
                tvFriendShareCategory.setText((CharSequence) categorySum.getCategoryType());

            }else{
                //沒有朋友
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeMyShare(String action) {
        // init share msg
        if(photoSKHashTableIndex == -1){
            if(photoSKList.size() > 0){
                photoSKHashTableIndex = 0;
            }else{
                tvMyShareCategory.setText(" ? ");
                ivMyPhoto.setImageDrawable(getResources().getDrawable( R.drawable.no_diary));
            }
        }else{
            if(action.equals("previous")){
                photoSKHashTableIndex = photoSKHashTableIndex - 1;
                if(photoSKHashTableIndex < 0){
                    photoSKHashTableIndex = photoSKList.size() - 1;
                }
            }else{
                photoSKHashTableIndex = photoSKHashTableIndex + 1;
                if(photoSKHashTableIndex >= photoSKList.size()){
                    photoSKHashTableIndex = 0;
                }
            }
        }

        if(photoSKHashTableIndex >= 0){
            tvMyShareCategory.setText(photoSKList.get(photoSKHashTableIndex).category);

            ivMyPhoto.setImageBitmap(categoryPhotoHashTable.get(
                    photoSKList.get(photoSKHashTableIndex).category
            ));
            //ivMyPhoto.setScaleType(ImageView.ScaleType.FIT_END);

        }

    }

    public void onBackClick(View view) {
        finish();
    }

    class MyCategoryPhotoSK implements Serializable {
        public String category = "";
        public int photoSK = 0;
    }

    public class SpotGetImageTask extends AsyncTask<Object, Integer, Bitmap> {
        private final static String TAG = "SpotGetImageTask";
        private String url;
        private int id, imageSize;

        // WeakReference物件不會阻止參照到的實體被回收
        private WeakReference<ImageView> imageViewWeakReference;

        SpotGetImageTask(String url, int id, int imageSize) {
            this(url, id, imageSize, null);
        }

        public SpotGetImageTask(String url, int id, int imageSize, ImageView imageView) {
            this.url = url;
            this.id = id;
            this.imageSize = imageSize;
            this.imageViewWeakReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(Object... params) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getImage");
            jsonObject.addProperty("account", Common.getAccount(ExchangeActivity.this));
            jsonObject.addProperty("password", Common.getPWD(ExchangeActivity.this));
            jsonObject.addProperty("id", id);
            jsonObject.addProperty("imageSize", imageSize);
            return getRemoteImage(url, jsonObject.toString());
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView imageView = imageViewWeakReference.get();
            if (isCancelled() || imageView == null) {
                return;
            }
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                // imageView.setImageResource(R.drawable.ex_photo);
            }
        }

        private Bitmap getRemoteImage(String url, String jsonOut) {
            HttpURLConnection connection = null;
            Bitmap bitmap = null;
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setDoInput(true); // allow inputs
                connection.setDoOutput(true); // allow outputs
                connection.setUseCaches(false); // do not use a cached copy
                connection.setRequestMethod("POST");
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                bw.write(jsonOut);
                Log.d(TAG, "output: " + jsonOut);
                bw.close();

                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {
                    bitmap = BitmapFactory.decodeStream(
                            new BufferedInputStream(connection.getInputStream()));
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
    }
}


package com.kang.Dlife.tb_page1.diary_edit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kang.Dlife.Common;
import com.kang.Dlife.R;
import com.kang.Dlife.sever.GoogleNearbyItem;
import com.kang.Dlife.sever.LocationDao;
import com.kang.Dlife.sever.LocationToDiary;
import com.kang.Dlife.sever.MyTask;
import com.kang.GalleryPick.config.GalleryConfig;
import com.kang.GalleryPick.config.GalleryPick;
import com.kang.GalleryPick.inter.IHandlerCallBack;
import com.kang.GalleryPick.inter.ImageLoader;
import com.kang.GalleryPick.widget.GalleryImageView;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class DiaryEdit extends Activity {
    private static final String TAG = "DiaryEdit";
    private Context mContext;
    private Activity mActivity;
    private RecyclerView rvResultPhoto;
    private ImageButton btn;
    private ImageButton ibOk;
    private EditText etDiary;
    private PhotoAdapter photoAdapter;
    private List<String> path = new ArrayList<>();
    private String categorySelect;
    private String locationSelect;
    private GalleryConfig galleryConfig;
    private IHandlerCallBack iHandlerCallBack;
    private ImageView ivExPhoto;
    private MyTask dataUploadTask;
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private TextView tvDate, tvTimeStart, tvTimeEnd, tvLocation;
    private ImageButton ibMap;
    private ImageButton ibMap2;
    public LocationToDiary bundleP;
    public ArrayList<Object> bundleQ;
    private int selectPhotoSize;
    public static ArrayList<Integer> bundlePhoto;
    public Hashtable<Integer, LocationToDiary> bundleHash = new Hashtable<Integer, LocationToDiary>();
    private static SpotGetImageTask spotGetImageTask;
    private List<GoogleNearbyItem> nearbyItem;
    private int selectNearBy;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        @SuppressLint("ResourceType") Transition explode = TransitionInflater.from(this).inflateTransition(R.anim.explode);
        //退出時使用
        getWindow().setExitTransition(explode);
        //進入時使用
        getWindow().setEnterTransition(explode);
        //再次進入時使用
        getWindow().setReenterTransition(explode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        bundleP = (LocationToDiary) bundle.getSerializable("Page1Adapter");
        bundlePhoto = (ArrayList<Integer>) bundle.getIntegerArrayList("Page1Photo");
        setContentView(R.layout.page1_diary_edit);
        // Spinner選單
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final String[] category = {"Shopping", "Hobby", "Learning", "Travel", "Work"};
        final ArrayAdapter<String> category_list = new ArrayAdapter<>(DiaryEdit.this,
                android.R.layout.simple_spinner_dropdown_item,
                category);

        category_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(category_list);
        spinner.setSelection(bundleP.getTop_category_sk() - 1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySelect = category[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // 取得latitude, longitude , 取得鄰近地標
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "nearby");
        jsonObject.addProperty("account", Common.getAccount(DiaryEdit.this));
        jsonObject.addProperty("password", Common.getPWD(DiaryEdit.this));
        jsonObject.addProperty("latitude", bundleP.getLatitude());
        jsonObject.addProperty("longitude", bundleP.getLongitude());
        String jsonOut = jsonObject.toString();

        int insterCount = 0;
        if (networkConnected()) {
            String url = Common.URL + Common.MAPAPI;
            MyTask myTask = new MyTask(url, jsonObject.toString());
            MyTask getNearByTask = new MyTask(url, jsonOut);
            try {
                String getNearByjsonIn = getNearByTask.execute().get();
                Gson gson = new Gson();
                JsonObject jsonObject2 = gson.fromJson(getNearByjsonIn, JsonObject.class);
                String nearbyItems = jsonObject2.get("nearbyItems").getAsString();

                JsonArray nearByItemJsonArray = gson.fromJson(nearbyItems, JsonArray.class);
                Type tySum = new TypeToken<List<GoogleNearbyItem>>() {
                }.getType();
                nearbyItem = new Gson().fromJson(nearByItemJsonArray, tySum);
                Log.e(TAG, Integer.toString(nearbyItem.size()));

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
        // 地點Spinner
        Spinner locationSpinner = (Spinner) findViewById(R.id.locationSpinner);

        ArrayList<String> location = new ArrayList<>();
        for (int i = 1; i <= nearbyItem.size(); i++) {
            location.add(nearbyItem.get(i - 1).getName());
        }


        final ArrayAdapter<String> location_list = new ArrayAdapter<>(DiaryEdit.this,
                android.R.layout.simple_spinner_dropdown_item,
                location);


        location_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(location_list);
        locationSpinner.setSelection(0);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectNearBy = position;
                bundleQ = new ArrayList<Object>();
                bundleQ.add(nearbyItem.get(position).getLatitude());
                bundleQ.add(nearbyItem.get(position).getLongitude());
                bundleQ.add(nearbyItem.get(position).getName());
                bundleQ.add(nearbyItem.get(position).getPlaceID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        mContext = this;
        mActivity = this;

        initView(bundleP);
        initGallery();
        init();


        // 座標轉換
        Geocoder geocoder = new Geocoder(DiaryEdit.this);
        try {
            List<Address> addressList = null;

            addressList = geocoder.getFromLocation(bundleP.getLatitude(), bundleP.getLongitude(), 1);


            String addrStr = "";
            if (addressList.size() > 0) {
                Address address = addressList.get(0);
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addrStr = address.getLocality();

                }
            } else {
                addrStr = "unknown";
            }
            tvLocation.setText(addrStr);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        ibMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(DiaryEdit.this, GoogleMap.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("place", bundleP);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        ibMap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(DiaryEdit.this, GoogleMap.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("nearByLocation", bundleQ);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        // 上傳日記內容和類別
        ibOk.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                String diary = etDiary.getText().toString().trim();
                String category_select = categorySelect;
                double longitude = bundleP.getLongitude();
                double latitude = bundleP.getLatitude();
                if (diary.isEmpty()) {
                    Toast.makeText(DiaryEdit.this,
                            R.string.text_InvalidName,
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                if (bundleP.getStartLocationSK() != 0) {

                    // 上傳SQLite的日記
                    DiaryEditSpot spot = new DiaryEditSpot(diary, category_select, longitude, latitude);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "insertDiary");
                    jsonObject.addProperty("account", Common.getAccount(DiaryEdit.this));
                    jsonObject.addProperty("password", Common.getPWD(DiaryEdit.this));
                    jsonObject.addProperty("categoryType", category_select);
                    bundleP.setNote(diary);
                    jsonObject.addProperty("diaryDetail", new Gson().toJson(bundleP.toDiaryDetail()));

                    int insterCount = 0;
                    if (networkConnected()) {
                        String url = Common.URL + Common.WEBDIARY;
                        MyTask myTask = new MyTask(url, jsonObject.toString());
                        try {
                            String inStr = myTask.execute().get().trim();
                            insterCount = Integer.valueOf(inStr);
                            if (insterCount == 0) {

                            } else {

                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }

                    // 上傳地標訊息
                    JsonObject nearByJsonObject = new JsonObject();
                    nearByJsonObject.addProperty("action", "nearBySelect");
                    nearByJsonObject.addProperty("account", Common.getAccount(DiaryEdit.this));
                    nearByJsonObject.addProperty("password", Common.getPWD(DiaryEdit.this));
                    nearByJsonObject.addProperty("nearBy", new Gson().toJson(nearbyItem.get(selectNearBy)));

                    if (networkConnected()) {
                        String url = Common.URL + Common.MAPAPI;
                        MyTask myTask = new MyTask(url, nearByJsonObject.toString());
                        try {
                            String inStr = myTask.execute().get().trim();
                            insterCount = Integer.valueOf(inStr);
                            if (insterCount == 0) {

                            } else {

                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }



                    // 上傳多張圖片
                    if (path.size() > 0 && insterCount > 0) {
                        for (int i = 0; i < path.size(); i++) {
                            Bitmap picture = BitmapFactory.decodeFile(path.get(i));
                            Bitmap downsizedImage = Common.downSize(picture, 350);

                            byte[] image = Common.bitmapToPNG(downsizedImage);
                            JsonObject jsonObject1 = new JsonObject();
                            jsonObject1.addProperty("action", "insertDiaryPhoto");
                            jsonObject1.addProperty("account", Common.getAccount(DiaryEdit.this));
                            jsonObject1.addProperty("password", Common.getPWD(DiaryEdit.this));
                            jsonObject1.addProperty("diaryDetailSK", insterCount);
                            jsonObject1.addProperty("imageBase64", Base64.encodeToString(image, Base64.DEFAULT));

                            if (networkConnected()) {
                                String url = Common.URL + Common.WEBDIARY;
                                MyTask myTask = new MyTask(url, jsonObject1.toString());
                                try {
                                    String inStr = myTask.execute().get().trim();
                                    int count = Integer.valueOf(inStr);
                                    if (count == 0) {

                                    } else {

                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, e.toString());
                                }
                            }
                        }
                    }
                    LocationDao locationDao = new LocationDao(DiaryEdit.this);
                    locationDao.deleteById(bundleP.getStartLocationSK(), bundleP.getEndLocationSK());
                    finish();
                } else {

                    //更新日記
                    DiaryEditSpot spot = new DiaryEditSpot(diary, category_select, longitude, latitude);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "uploadDiary");
                    jsonObject.addProperty("account", Common.getAccount(DiaryEdit.this));
                    jsonObject.addProperty("password", Common.getPWD(DiaryEdit.this));
                    jsonObject.addProperty("uploadCategoryType", category_select);
                    bundleP.setNote(diary);
                    jsonObject.addProperty("uploadDiaryDetail", new Gson().toJson(bundleP.toDiaryDetail()));

                    int insterCount = 0;
                    if (networkConnected()) {
                        String url = Common.URL + Common.WEBDIARY;
                        MyTask myTask = new MyTask(url, jsonObject.toString());
                        try {
                            String inStr = myTask.execute().get().trim();
                            if (inStr.equals("uploadDiarySuccess")) {

                            } else {

                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }

                    // 上傳新選的地標訊息
                    JsonObject nearByJsonObject = new JsonObject();
                    nearByJsonObject.addProperty("action", "uploadNearBySelect");
                    nearByJsonObject.addProperty("account", Common.getAccount(DiaryEdit.this));
                    nearByJsonObject.addProperty("password", Common.getPWD(DiaryEdit.this));
                    nearByJsonObject.addProperty("nearBy", new Gson().toJson(nearbyItem.get(selectNearBy)));

                    if (networkConnected()) {
                        String url = Common.URL + Common.MAPAPI;
                        MyTask myTask = new MyTask(url, nearByJsonObject.toString());
                        try {
                            String inStr = myTask.execute().get().trim();
                            insterCount = Integer.valueOf(inStr);
                            if (insterCount == 0) {

                            } else {

                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }

                    // 上傳新選的圖片
                    if (path.size() > 0) {
                        for (int i = 0; i < path.size(); i++) {
                            Bitmap picture = BitmapFactory.decodeFile(path.get(i));
                            Bitmap downsizedImage = Common.downSize(picture, 350);

                            byte[] image = Common.bitmapToPNG(downsizedImage);
                            JsonObject jsonObject1 = new JsonObject();
                            jsonObject1.addProperty("action", "insertDiaryPhoto");
                            jsonObject1.addProperty("account", Common.getAccount(DiaryEdit.this));
                            jsonObject1.addProperty("password", Common.getPWD(DiaryEdit.this));
                            jsonObject1.addProperty("diaryDetailSK", bundleP.getSk());
                            jsonObject1.addProperty("imageBase64", Base64.encodeToString(image, Base64.DEFAULT));

                            if (networkConnected()) {
                                String url = Common.URL + Common.WEBDIARY;
                                MyTask myTask = new MyTask(url, jsonObject1.toString());
                                try {
                                    String inStr = myTask.execute().get().trim();
                                    int count = Integer.valueOf(inStr);
                                    if (count == 0) {

                                    } else {

                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, e.toString());
                                }
                            }
                        }
                    }
                    finish();
                }

            }
        });


        if (bundlePhoto != null && (bundlePhoto.size() != 0 || selectPhotoSize != 0)) {
            ivExPhoto.setVisibility(View.GONE);
        } else {
            ivExPhoto.setVisibility(View.VISIBLE);
        }
    }

    // 檢察網路連線
    private boolean networkConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo.isConnected();
        } else {
            return false;
        }
    }

    // 找按鈕
    private void initView(LocationToDiary page1Spot) {
        btn = (ImageButton) super.findViewById(R.id.btn);
        rvResultPhoto = (RecyclerView) super.findViewById(R.id.rvResultPhoto);
        etDiary = (EditText) super.findViewById(R.id.etDiary);
        ibOk = (ImageButton) super.findViewById(R.id.ibOk);
        tvDate = (TextView) super.findViewById(R.id.tvDate);
        tvLocation = (TextView) super.findViewById(R.id.tvLocation);
        tvTimeStart = (TextView) super.findViewById(R.id.tvTimeStart);
        tvTimeEnd = (TextView) super.findViewById(R.id.tvTimeEnd);
        ivExPhoto = (ImageView) super.findViewById(R.id.iv_exphoto);
        ibMap = (ImageButton) super.findViewById(R.id.ibMap);
        ibMap2 = (ImageButton) super.findViewById(R.id.ibMap2);

        if (page1Spot.getStartDate() != null) {
            tvDate.setText(Common.dateStringToDay(page1Spot.getStartDate()));
            tvTimeStart.setText(Common.dateStringToHM(page1Spot.getStartDate()));
            tvTimeEnd.setText(Common.dateStringToHM(page1Spot.getEndDate()));
        } else {
            tvDate.setText(Common.dateStringToDay(page1Spot.getEnd_date()));
            tvTimeStart.setText(Common.dateStringToHM(page1Spot.getStart_date()));
            tvTimeEnd.setText(Common.dateStringToHM(page1Spot.getEnd_date()));
            etDiary.setText(page1Spot.getNote());
        }
    }

    // 照片選取器
    private void init() {

        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // 加載框架
                .iHandlerCallBack(iHandlerCallBack)     // 監聽端口(必填)
                .provider("com.kang.Dlife.fileprovider")   // provider(必填)
                .pathList(path)                         // 記錄已選圖片
                .multiSelect(true)                      // 是否多選
                .multiSelect(true, 1000)   // 是否多選, 多選數量
                .maxSize(9)                             // 多選數量
                .crop(false)                             // 裁切功能
                .crop(false, 1, 1, 500, 500)   //裁切配置參數
                .isShowCamera(true)                     // 開啟相機按鈕
                .filePath("/Gallery/Pictures")          // 圖片儲存位置
                .build();
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //更改按下去時的圖片
                    btn.setImageResource(R.drawable.photo_select_ontouch);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //抬起來時的圖片
                    btn.setImageResource(R.drawable.photo_select);
                }
                return false;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryConfig.getBuilder().isOpenCamera(false).build();
                initPermissions();
            }
        });


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvResultPhoto.setOnFlingListener(null);
        // 放這就不會滑到底
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(rvResultPhoto);
        rvResultPhoto.setLayoutManager(gridLayoutManager);
        photoAdapter = new PhotoAdapter(this, path);
        rvResultPhoto.setAdapter(photoAdapter);

    }

    //RecyclerView 物件
    public class PhotoAdapter extends
            RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

        private Context context;
        private LayoutInflater mLayoutInflater;
        private List<String> result;
        private int imageSize;
        private final static String TAG = "Page1Adapter";
        private Bitmap bitmap;


        public PhotoAdapter(Context context, List<String> result) {
            mLayoutInflater = LayoutInflater.from(context);
            this.context = context;
            this.result = result;
            imageSize = getResources().getDisplayMetrics().widthPixels;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.page1_diary_edit_item_photo, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            switch (getItemViewType(position)) {

                case 0:
                    // 抓sever的圖片
                    if (bundlePhoto.size() != 0) {
                        String url = Common.URL + Common.WEBPHOTO;
                        int id = bundlePhoto.get(position);
                        spotGetImageTask = new SpotGetImageTask(url, id, imageSize, holder.ivPhoto);
                        spotGetImageTask.execute();
                    }
                    break;

                default:
                    // 抓使用者選的圖片
                    selectPhotoSize = result.size();
                    if (bundlePhoto != null) {
                        Glide.with(context)
                                .load(result.get(position - bundlePhoto.size()))
                                .centerCrop()
                                .into(holder.ivPhoto);
                    } else {
                        Glide.with(context)
                                .load(result.get(position))
                                .centerCrop()
                                .into(holder.ivPhoto);
                    }
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (bundlePhoto != null && position <= bundlePhoto.size() - 1) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public int getItemCount() {
            if (bundlePhoto != null) {
                return bundlePhoto.size() + result.size();
            } else {
                return result.size();
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView ivPhoto;

            public ViewHolder(View itemView) {
                super(itemView);
                ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            }

        }


    }


    // 授權管理
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "需要授權 ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i(TAG, "拒絕過了");
                Toast.makeText(mContext, "請在手機設定裡打開應用程式授權", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "進行授權");
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            Log.i(TAG, "不需要授權 ");
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(DiaryEdit.this);
        }
    }

    // 授權結果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "同意授權");
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(DiaryEdit.this);
            } else {
                Log.i(TAG, "拒絕授權");
            }
        }
    }

    // 開啟選取照片頁面
    private void initGallery() {
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
                Log.i(TAG, "onStart: 開啟");
            }

            @Override
            public void onSuccess(List<String> photoList) {
                Log.i(TAG, "onSuccess: 返回");
                path.clear();
                for (String s : photoList) {
                    Log.i(TAG, s);
                    path.add(s);
                }
                photoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel: 取消");
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "onFinish: 結束");
            }

            @Override
            public void onError() {
                Log.i(TAG, "onError: 錯誤");
            }
        };

    }

    // 返回上一頁面
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onBackClick(View view) {
        finish();
    }

    //載入選取照片的框架
    public static class GlideImageLoader implements ImageLoader {

        private final static String TAG = "GlideImageLoader";

        @Override
        public void displayImage(Activity activity, Context context, String path, GalleryImageView galleryImageView, int width, int height) {
            Glide.with(context)
                    .load(path)
                    .placeholder(R.mipmap.gallery_pick_photo)
                    .centerCrop()
                    .into(galleryImageView);
        }

        @Override
        public void clearMemoryCache() {

        }
    }

    public static class BaseApplication extends Application {

        private final static String TAG = "BaseApplication";

        @Override
        public void onCreate() {
            super.onCreate();
        }
    }

    //新開 SpotGetImageTask 寫入/java 下
    public class SpotGetImageTask extends AsyncTask<Object, Integer, Bitmap> {
        private final static String TAG = "SpotGetImageTask";
        private String url;
        private int id, imageSize;
        private HttpURLConnection connection;
        private Bitmap bitmap;

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
            jsonObject.addProperty("account", Common.getAccount(DiaryEdit.this));
            jsonObject.addProperty("password", Common.getPWD(DiaryEdit.this));
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
//            imageView.setImageResource(R.drawable.default_image);
            }
        }

        private Bitmap getRemoteImage(String url, String jsonOut) {

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
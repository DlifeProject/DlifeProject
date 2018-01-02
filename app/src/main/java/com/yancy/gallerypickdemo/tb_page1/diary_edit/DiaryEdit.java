package com.yancy.gallerypickdemo.tb_page1.diary_edit;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;
import com.yancy.gallerypick.inter.ImageLoader;
import com.yancy.gallerypick.widget.GalleryImageView;
import com.yancy.gallerypickdemo.Common;
import com.yancy.gallerypickdemo.sever.MyTask;
import com.yancy.gallerypickdemo.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
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
    private GalleryConfig galleryConfig;
    private IHandlerCallBack iHandlerCallBack;
    private ImageView iv;
    private Bitmap picture;
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page1_diary_edit);
        // Spinner選單
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final String[] category = {"Shopping", "Hobby", "Learning", "Travel", "Work"};
        final ArrayAdapter<String> category_list = new ArrayAdapter<>(DiaryEdit.this,
                android.R.layout.simple_spinner_dropdown_item,
                category);

        category_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(category_list);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySelect = category[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mContext = this;
        mActivity = this;

        initView();
        initGallery();
        init();
        //傳送資料到資料庫
        ibOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String diary = etDiary.getText().toString().trim();
                String category_select = categorySelect;
                if (diary.isEmpty()) {
                    Toast.makeText(DiaryEdit.this,
                            R.string.text_InvalidName,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                DiaryEditSpot spot = new DiaryEditSpot(diary, category_select);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "insert");
                jsonObject.addProperty("DiaryEditSpot", new Gson().toJson(spot));

                if (path.size() > 0) {
                    for (int i = 0; i < path.size(); i++) {
                        File file = new File(path.get(i));
                    }
                }

                if (networkConnected()) {
                    String url = Common.URL + Common.WEBDIARY;
                    MyTask myTask = new MyTask(url, jsonObject.toString());
                    try {
                        String inStr = myTask.execute().get();
                        String count = inStr;
                        if (count.equals("0") ) {

                        } else {

                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }
                finish();
            }
        });

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
    private void initView() {
        btn = (ImageButton) super.findViewById(R.id.btn);
        rvResultPhoto = (RecyclerView) super.findViewById(R.id.rvResultPhoto);
        etDiary = (EditText) super.findViewById(R.id.etDiary);
        ibOk = (ImageButton) super.findViewById(R.id.ibOk);

    }

    // 照片選取器
    private void init() {


        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // 加載框架
                .iHandlerCallBack(iHandlerCallBack)     // 監聽端口(必填)
                .provider("com.yancy.gallerypickdemo.fileprovider")   // provider(必填)
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
    public static class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

        private Context context;
        private LayoutInflater mLayoutInflater;
        private List<String> result;
        private final static String TAG = "PhotoAdapter";

        public PhotoAdapter(Context context, List<String> result) {
            mLayoutInflater = LayoutInflater.from(context);
            this.context = context;
            this.result = result;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.page1_diary_edit_item_photo, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Glide.with(context)
                    .load(result.get(position))
                    .centerCrop()
                    .into(holder.ivPhoto);
        }

        @Override
        public int getItemCount() {
            return result.size();
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

}

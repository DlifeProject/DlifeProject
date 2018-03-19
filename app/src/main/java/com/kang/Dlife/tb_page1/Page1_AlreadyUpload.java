package com.kang.Dlife.tb_page1;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kang.Dlife.Common;
import com.kang.Dlife.R;
import com.kang.Dlife.sever.LocationDao;
import com.kang.Dlife.sever.LocationToDiary;
import com.kang.Dlife.sever.MyTask;
import com.kang.Dlife.tb_page1.diary_edit.DiaryEdit;
import com.kang.Dlife.tb_page2.diary_view.PhotoSpot;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.CONNECTIVITY_SERVICE;

public class Page1_AlreadyUpload extends Fragment {
    public Hashtable<Integer, LocationToDiary> bundleHash = new Hashtable<Integer, LocationToDiary>();
    private LocationDao locationDao;
    private RecyclerView rvDiary;
    private MyTask getAllDiaryTask;
    private List<LocationToDiary> allDiaryList;
    private SpotGetImageTask spotGetImageTask;
    private MyTask newsGetAllTask;
    private ImageView ivNoUploadDiary;
    private ArrayList<Integer> photoSK;
    private int recyclerClick;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        //因為有宣告view, 所以之後可以在這頁裡面找下面之後要用到的id
        View view = inflater.inflate(R.layout.page1_already_upload, container, false);
        rvDiary = (RecyclerView) view.findViewById(R.id.diarylist);
        ivNoUploadDiary = view.findViewById(R.id.ivNoUploadDiary);

        return view;
    }

    public void onResume() {
        super.onResume();
        showAllDiarys();
        photoSK = new ArrayList<Integer>();
        rvDiary.setLayoutManager(
                new StaggeredGridLayoutManager(
                        // spanCount(列數 or 行數), HORIZONTAL -> 水平, VERTICAL -> 垂直
                        1, StaggeredGridLayoutManager.VERTICAL));
        if (allDiaryList != null) {
            Collections.reverse(allDiaryList);
            ivNoUploadDiary.setVisibility(View.GONE);
            rvDiary.setAdapter(new DiaryAdapter(getActivity(), allDiaryList));
        } else {
            ivNoUploadDiary.setVisibility(View.VISIBLE);
        }
        if (recyclerClick == 0){
            rvDiary.scrollToPosition(recyclerClick);
        } else {
            rvDiary.scrollToPosition(recyclerClick - 1);
        }
    }

    private class DiaryAdapter extends
            RecyclerView.Adapter<DiaryAdapter.MyViewHolder> {
        private Context context;
        private List<LocationToDiary> allDiary;


        DiaryAdapter(Context context, List<LocationToDiary> allDiary) {
            this.context = context;
            this.allDiary = allDiary;
        }


        class MyViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            private ImageView icWeather;
            private ImageView icNew;
            private TextView tvDate;
            private TextView tvTimeStart;
            private TextView tvTimeEnd;
            private TextView tvPlace;
            private TextView tvDiary;
            private RecyclerView rvPhoto;


            MyViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                icWeather = itemView.findViewById(R.id.weather);
                icNew = itemView.findViewById(R.id.icnew);
                tvDate = itemView.findViewById(R.id.tvDate);
                tvTimeStart = itemView.findViewById(R.id.tvTimeStart);
                tvTimeEnd = itemView.findViewById(R.id.tvTimeEnd);
                tvPlace = itemView.findViewById(R.id.tvPlace);
                tvDiary = itemView.findViewById(R.id.tvDiary);
                rvPhoto = itemView.findViewById(R.id.rvImagePhoto);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.page1_recycleview, viewGroup, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {

            final LocationToDiary diaryDetailWeb = allDiary.get(position);
            List<PhotoSpot> photoSpotList = null;
            // 抓sever的資料
            viewHolder.tvDiary.setText(diaryDetailWeb.getNote());
            viewHolder.icWeather.setImageResource(R.drawable.ic_sun);
            viewHolder.icNew.setImageResource(0);
            viewHolder.tvDate.setText(Common.dateStringToDay(diaryDetailWeb.getEnd_date()));
            viewHolder.tvTimeStart.setText(Common.dateStringToHM(diaryDetailWeb.getStart_date()));
            viewHolder.tvTimeEnd.setText(Common.dateStringToHM(diaryDetailWeb.getEnd_date()));
            Geocoder geocoder = new Geocoder(getActivity());
            //測試時初始化防呆
            if (diaryDetailWeb.getLatitude() != 0.0) {
                try {
                    List<Address> addressList =
                            geocoder.getFromLocation(diaryDetailWeb.getLatitude(), diaryDetailWeb.getLongitude(), 1);
                    if (addressList.size() > 0) {
                        Address address = addressList.get(0);
                        String addrStr = "";
                        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                            addrStr = address.getLocality();
                        }
                        viewHolder.tvPlace.setText(addrStr);
                    } else {
                        viewHolder.tvPlace.setText("Place");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 照片的recyclerView
                viewHolder.rvPhoto.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
                // 禁止recyclerView滑動
                CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(getActivity());
                linearLayoutManager.setScrollEnabled(false);
                viewHolder.rvPhoto.setLayoutManager(linearLayoutManager);
                //先放這就不會滑到底
                viewHolder.rvPhoto.setOnFlingListener(null);
                PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
                pagerSnapHelper.attachToRecyclerView(viewHolder.rvPhoto);


                if (Common.checkNetConnected(getActivity())) {
                    String url = Common.URL + Common.WEBPHOTO;

                    try {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "getDiaryPhotoSKList");
                        jsonObject.addProperty("account", Common.getAccount(getActivity()));
                        jsonObject.addProperty("password", Common.getPWD(getActivity()));
                        jsonObject.addProperty("diarySK", allDiary.get(position).getSk());
                        String jsonOut = jsonObject.toString();

                        MyTask getDiaryPhotoSKTask = new MyTask(url, jsonOut);
                        String getDiaryPhotoSKjsonIn = getDiaryPhotoSKTask.execute().get().trim();
                        Gson gson = new Gson();
                        JsonObject jsonObject2 = gson.fromJson(getDiaryPhotoSKjsonIn, JsonObject.class);
                        String getDiaryPhotoSKString = jsonObject2.get("getDiaryPhotoSKList").getAsString();
                        Type ltWeb = new TypeToken<List<PhotoSpot>>() {
                        }.getType();
                        photoSpotList = new Gson().fromJson(getDiaryPhotoSKString, ltWeb);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }

                }
                viewHolder.rvPhoto.setAdapter(new photoAdapter(context, photoSpotList));

            }
            // recyclerView 點擊監聽

            final List<PhotoSpot> finalPhotoSpotList = photoSpotList;
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View view) {
                    for(int i = 0; i <= finalPhotoSpotList.size() - 1; i++){
                        final PhotoSpot photoSpot = finalPhotoSpotList.get(i);
                        int id = photoSpot.getSk();
                        photoSK.add(id);
                    }
                    recyclerClick = position;
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), DiaryEdit.class);
                    LocationToDiary bundleP = new LocationToDiary(bundleHash.get((int) view.getTag()));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Page1Adapter", bundleP);
                    bundle.putIntegerArrayList("Page1Photo", photoSK);
                    intent.putExtras(bundle);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), viewHolder.itemView, "shareNames").toBundle());
                }
            });
            // 長按監聽
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(getActivity(), v, Gravity.END);
                    // 彈出視窗
                    popupMenu.inflate(R.menu.popup_menu);
                    // 彈出視窗的點擊監聽器
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            Toast.makeText(getActivity(), Common.dateStringToDay(diaryDetailWeb.getEnd_date())
                                            + " " + Common.dateStringToHM(diaryDetailWeb.getStart_date())
                                            + "-" + Common.dateStringToHM(diaryDetailWeb.getEnd_date())
                                            + "被删除了",
                                    Toast.LENGTH_SHORT).show();

                            // 刪除sever日記
                            JsonObject nearByJsonObject = new JsonObject();
                            nearByJsonObject.addProperty("action", "toDeleteDiary");
                            nearByJsonObject.addProperty("account", Common.getAccount(getActivity()));
                            nearByJsonObject.addProperty("password", Common.getPWD(getActivity()));
                            nearByJsonObject.addProperty("diaryDetailSK", diaryDetailWeb.getSk());

                            if (Common.checkNetConnected(getActivity())) {
                                String url = Common.URL + Common.WEBDIARY;
                                MyTask myTask = new MyTask(url, nearByJsonObject.toString());

                                int insterCount = 0;
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

                            // 重新執行recycleView
                            notifyDataSetChanged();
                            return true;

                        }
                    });
                    popupMenu.show();
                    return true;
                }
            });
            viewHolder.itemView.setTag(position);
            bundleHash.put(position, diaryDetailWeb);
        }

        @Override
        public int getItemCount() {
            return allDiary.size();
        }
    }


    // 取得上傳的日記
    private void showAllDiarys() {
        if (Common.checkNetConnected(getActivity())) {
            String url = "";
            List<LocationToDiary> diaryList = null;
            try {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getRecyclerViewDiary");
                jsonObject.addProperty("account", Common.getAccount(getActivity()));
                jsonObject.addProperty("password", Common.getPWD(getActivity()));
                String jsonOut = jsonObject.toString();

                url = Common.URL + Common.WEBDIARY;
                getAllDiaryTask = new MyTask(url, jsonOut);
                String getDiaryJsonIn = getAllDiaryTask.execute().get();

                Gson gson = new Gson();
                JsonObject diaryInJsonObject = gson.fromJson(getDiaryJsonIn, JsonObject.class);
                String ltDiaryDetailString = diaryInJsonObject.get("getRecyclerViewDiary").getAsString();

                Type tySum = new TypeToken<List<LocationToDiary>>() {
                }.getType();
                allDiaryList = new Gson().fromJson(ltDiaryDetailString, tySum);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (allDiaryList == null || allDiaryList.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoNewsFound);
            } else {

            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
    }

    private class photoAdapter extends
            RecyclerView.Adapter<photoAdapter.MyViewHolder> {
        private Context context;
        private List<PhotoSpot> photoSpotList;
        private int imageSize;

        photoAdapter(Context context, List<PhotoSpot> photoSpotList) {
            this.context = context;
            this.photoSpotList = photoSpotList;
            imageSize = getResources().getDisplayMetrics().widthPixels;
        }

        //相當於一個資料夾hold住這三個的捷徑
        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivRecyclerImage;


            MyViewHolder(View itemView) {
                super(itemView);
                ivRecyclerImage = (ImageView) itemView.findViewById(R.id.ivPhotoImage);

            }
        }

        //建立個替身 因為常用  建立完捷徑再給他整理起來
        @Override
        public int getItemCount() {
            return photoSpotList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.page1_recycleview_photo, viewGroup, false);
            return new MyViewHolder(itemView);
        }


        //position 就是當初listview的 index
        @Override
        public void onBindViewHolder(final MyViewHolder viewHolder, int position) {

            final PhotoSpot photoSpot = photoSpotList.get(position);

            String url = Common.URL + Common.WEBPHOTO;
            int id = photoSpot.getSk();
            spotGetImageTask = new SpotGetImageTask(url, id, imageSize, viewHolder.ivRecyclerImage);
            spotGetImageTask.execute();


            //只要沒寫get 就是一直讓他抓 不等圖 不然會卡著等圖
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
            jsonObject.addProperty("account", Common.getAccount(getActivity()));
            jsonObject.addProperty("password", Common.getPWD(getActivity()));
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

    @Override
    public void onStop() {
        super.onStop();
        if (newsGetAllTask != null) {
            newsGetAllTask.cancel(true);
        }

    }

    // 鎖定recyclerView
    public class CustomLinearLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomLinearLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }

}

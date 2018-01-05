package com.kang.Dlife.tb_page2.diary_view;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kang.Dlife.Common;
import com.kang.Dlife.R;
import com.kang.Dlife.sever.MyTask;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;


public class DiaryView extends AppCompatActivity {
    private ImageButton btBack;


    private final static String TAG = "IgTestRecycler2Activity";
    private RecyclerView recyclerView;
    private MyTask newsGetAllTask;
    private ImageButton ibMap;
    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page2_diary_view);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

//        List<IgDiary> igList = getIglist();

//        recyclerView.setAdapter(new IgAdapter(this, igList));
        initView();
        ibMap = (ImageButton) super.findViewById(R.id.ibMap);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        showAllNews();

    }

    private void initView() {
        btBack = (ImageButton) super.findViewById(R.id.btBack);
    }

    @Override
    public void onStart() {
        super.onStart();
        showAllNews();
    }

    private void showAllNews() {

        if (networkConnected()) {
            String url = Common.URL + "IgDiaryServlet";
            List<DiaryViewSpot> igList = null;
            try {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getAll");
// 到時候這邊設帳密 讓server端設if else取得帳密               jsonObject.addProperty("accout", Common.getAccount());

                String jsonOut = jsonObject.toString();
                newsGetAllTask = new MyTask(url, jsonOut);
//------------------------------------------

                String jsonIn = newsGetAllTask.execute().get();


                Log.d(TAG, jsonIn);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<DiaryViewSpot>>() {
                }.getType();

                if (jsonIn != null) {
                    igList = gson.fromJson(jsonIn, listType);
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (igList == null || igList.isEmpty()) {
                Common.showToast(this, R.string.msg_NoNewsFound);
            } else {
                recyclerView.setAdapter(new IgAdapter(this, igList));
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }
    }

    //檢察網路
    private boolean networkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            //這邊會提醒你有沒有加permission
            return networkInfo.isConnected();
        } else {
            return false;
        }
    }


    //  供內部使用 所以可以包來包去 因為已經是private 了所以裡面的不用寫
    private class IgAdapter extends
            RecyclerView.Adapter<IgAdapter.MyViewHolder> {
        private Context context;
        private List<DiaryViewSpot> igList;

        //顯示什麼東西
        IgAdapter(Context context, List<DiaryViewSpot> igList) {
            this.context = context;
            this.igList = igList;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tvDate, tvLocation, tvTime, tvNote, tvContinue;
            RecyclerView mRecyclerView;

            MyViewHolder(View itemView) {
                super(itemView);
                mRecyclerView = (RecyclerView) itemView.findViewById(R.id.rvImage);

                tvDate = (TextView) itemView
                        .findViewById(R.id.tvDate);
                tvLocation = (TextView) itemView
                        .findViewById(R.id.tvLocation);
                tvTime = (TextView) itemView
                        .findViewById(R.id.tvTime);
                tvNote = (TextView) itemView.findViewById(R.id.tvNote);
                tvContinue = itemView.findViewById(R.id.tvContinue);

            }
        }

        @Override
        public IgAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.page2_diary_view_recyclerview, viewGroup, false);

            return new MyViewHolder(itemView);
        }

        @Override

        public void onBindViewHolder(final MyViewHolder viewHolder, int position) {


            viewHolder.mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

            viewHolder.mRecyclerView.setOnFlingListener(null);//先放這就不會滑到底
            PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
            pagerSnapHelper.attachToRecyclerView(viewHolder.mRecyclerView);

            List<PhotoSpot> photoSpotList = null;

            if (networkConnected()) {
                String url = Common.URL + "IgPictureServlet";

                try {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "getAll");
// 到時候這邊設帳密 讓server端設if else取得帳密               jsonObject.addProperty("accout", Common.getAccount());

                    String jsonOut = jsonObject.toString();
                    newsGetAllTask = new MyTask(url, jsonOut);
//------------------------------------------

                    String jsonIn = newsGetAllTask.execute().get();


                    Log.d(TAG, jsonIn);
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<PhotoSpot>>() {
                    }.getType();

                    if (jsonIn != null) {
                        photoSpotList = gson.fromJson(jsonIn, listType);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }

            }


            viewHolder.mRecyclerView.setAdapter(new IgPictureAdapter(context, photoSpotList));


            final DiaryViewSpot diaryViewSpot = igList.get(position);
            String time = diaryViewSpot.getStartTime() + "-" + diaryViewSpot.getEndTime();
            viewHolder.tvDate.setText(diaryViewSpot.getDate());
            longitude = diaryViewSpot.getLongitude();
            latitude = diaryViewSpot.getLatitude();
            Geocoder geocoder = new Geocoder(DiaryView.this);
            try {
                List<Address> addressList =
                        geocoder.getFromLocation(latitude, longitude, 1);
                Address address = addressList.get(0);
                String addrStr = "";
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addrStr = address.getAddressLine(i);

                }
                viewHolder.tvLocation.setText(addrStr);
            } catch (IOException e) {
                e.printStackTrace();
            }


            viewHolder.tvTime.setText(time);
            viewHolder.tvNote.setText(diaryViewSpot.getNote());
            viewHolder.tvContinue.setVisibility(View.GONE);

            if (viewHolder.tvNote.length() > 11) {
                viewHolder.tvNote.setText(diaryViewSpot.getNote().substring(0, 11));

                viewHolder.tvContinue.setVisibility(View.VISIBLE);


                viewHolder.tvContinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        viewHolder.tvContinue.setVisibility(View.GONE);
                        viewHolder.tvNote.setText(diaryViewSpot.getNote().substring(0));
                    }
                });
            }


        }

        @Override
        public int getItemCount() {
            return igList.size();
        }
    }


    private class IgPictureAdapter extends
            RecyclerView.Adapter<IgPictureAdapter.MyViewHolder> {
        private Context context;
        private List<PhotoSpot> photoSpotList;
        private int imageSize;

        IgPictureAdapter(Context context, List<PhotoSpot> photoSpotList) {
            this.context = context;
            this.photoSpotList = photoSpotList;
            imageSize = getResources().getDisplayMetrics().widthPixels;
        }

        //相當於一個資料夾ＨＯＬＤ住這三個的捷徑
        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivRecyclerImage;


            MyViewHolder(View itemView) {
                super(itemView);
                ivRecyclerImage = (ImageView) itemView.findViewById(R.id.ivRecyclerImage);


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
            View itemView = layoutInflater.inflate(R.layout.page2_diary_view_photo_item, viewGroup, false);
            return new MyViewHolder(itemView);
        }

        //position 就是當初listview的 index
        @Override
        public void onBindViewHolder(final MyViewHolder viewHolder, int position) {

            final PhotoSpot photoSpot = photoSpotList.get(position);
//
//            String url = Common.URL + "/IgPictureServlet";
//            int id = photoSpot.getSk();
//            spotGetImageTask = new SpotGetImageTask(url, id, imageSize, viewHolder.ivRecyclerImage);
//            spotGetImageTask.execute();   //只要沒寫get 就是一直讓他抓 不等圖 不然會卡著等圖


            Glide.with(context)
                    .load(photoSpot.getPhoto_img())
                    .centerCrop()
                    .into(viewHolder.ivRecyclerImage);
//            viewHolder.ivRecyclerImage.setImageResource(photoSpot.getPhoto_img().set);
//

//            viewHolder.ivRecyclerImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    ImageView imageView = new ImageView(context);
//                    imageView.setImageResource(photoSpot.getImage());
//
//                    //點擊跑出來的
//                    Toast toast = new Toast(context);
//                    toast.setView(imageView);
//                    toast.setDuration(Toast.LENGTH_SHORT);
//                    toast.show();
//
//
//                }
//            });
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (newsGetAllTask != null) {
            newsGetAllTask.cancel(true);
        }

    }
}






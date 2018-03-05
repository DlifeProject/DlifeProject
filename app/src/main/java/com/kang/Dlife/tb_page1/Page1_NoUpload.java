package com.kang.Dlife.tb_page1;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
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

public class Page1_NoUpload extends Fragment {
    public Hashtable<Integer, LocationToDiary> bundleHash = new Hashtable<Integer, LocationToDiary>();
    private LocationDao locationDao;
    private RecyclerView rvDiary;
    private MyTask getAllDiaryTask;
    private List<LocationToDiary> allDiaryList;
    private ImageView ivNoDiary;
    private int recyclerClick;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        //因為有宣告view, 所以之後可以在這頁裡面找下面之後要用到的id
        View view = inflater.inflate(R.layout.page1_no_upload, container, false);
        rvDiary = (RecyclerView) view.findViewById(R.id.diarylist);
        ivNoDiary = view.findViewById(R.id.ivNoDiary);
        return view;
    }
    public void onResume() {
        super.onResume();
        rvDiary.setLayoutManager(
                new StaggeredGridLayoutManager(
                        // spanCount(列數 or 行數), HORIZONTAL -> 水平, VERTICAL -> 垂直
                        1, StaggeredGridLayoutManager.VERTICAL));
        final List<LocationToDiary> SQLiteDiary = getSpots();
        if (SQLiteDiary.size() > 0){
            ivNoDiary.setVisibility(View.GONE);
        } else {
            ivNoDiary.setVisibility(View.VISIBLE);
        }
        rvDiary.setAdapter(new DiaryAdapter(getActivity(), SQLiteDiary));
        if (recyclerClick == 0){
            rvDiary.scrollToPosition(recyclerClick);
        } else {
            rvDiary.scrollToPosition(recyclerClick - 1);
        }
    }
    private class DiaryAdapter extends
            RecyclerView.Adapter<DiaryAdapter.MyViewHolder> {
        private Context context;
        private List<LocationToDiary> SQLiteDiary;


        DiaryAdapter(Context context, List<LocationToDiary> SQLiteDiary) {
            this.context = context;
            this.SQLiteDiary = SQLiteDiary;
        }


        class MyViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            private ImageView icWeather;
            private ImageView icNew;
            private TextView tvDate;
            private TextView tvTimeStart;
            private TextView tvTimeEnd;
            private TextView tvPlace;

            MyViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                icWeather = itemView.findViewById(R.id.weather);
                icNew = itemView.findViewById(R.id.icnew);
                tvDate = itemView.findViewById(R.id.tvDate);
                tvTimeStart = itemView.findViewById(R.id.tvTimeStart);
                tvTimeEnd = itemView.findViewById(R.id.tvTimeEnd);
                tvPlace = itemView.findViewById(R.id.tvPlace);
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

            final LocationToDiary locationToDiary = SQLiteDiary.get(position);
            final int deleteStartSK = locationToDiary.getStartLocationSK();
            final int deleteEndSK = locationToDiary.getEndLocationSK();


                viewHolder.imageView.setImageResource(locationToDiary.getImageId());
                viewHolder.icWeather.setImageResource(R.drawable.ic_sun);
                viewHolder.icNew.setImageResource(R.drawable.ic_new);
                viewHolder.tvDate.setText(Common.dateStringToDay(locationToDiary.getEndDate()));
                viewHolder.tvTimeStart.setText(Common.dateStringToHM(locationToDiary.getStartDate()));
                viewHolder.tvTimeEnd.setText(Common.dateStringToHM(locationToDiary.getEndDate()));
                Geocoder geocoder = new Geocoder(getActivity());
                //測試時初始化防呆
                if (locationToDiary.getLatitude() != 0.0) {
                    try {
                        List<Address> addressList =
                                geocoder.getFromLocation(locationToDiary.getLatitude(), locationToDiary.getLongitude(), 1);
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
                }

                // recyclerview 點擊監聽
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View view) {
                        recyclerClick = position;
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), DiaryEdit.class);
                        LocationToDiary bundleP = new LocationToDiary(bundleHash.get((int) view.getTag()));
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Page1Adapter", bundleP);
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

                                LocationToDiary spot = SQLiteDiary.get(position);
                                Toast.makeText(getActivity(), Common.dateStringToDay(spot.getEndDate())
                                                + " " + Common.dateStringToHM(spot.getStartDate())
                                                + "-" + Common.dateStringToHM(spot.getEndDate())
                                                + "被删除了",
                                        Toast.LENGTH_SHORT).show();

                                SQLiteDiary.remove(position);
                                // 刪除有動畫效果
                                notifyItemRemoved(position);
                                // 刪除SQL_lite的欄位
                                locationDao.deleteById(deleteStartSK, deleteEndSK);
                                // 重置DiaryDat
                                SQLiteDiary = getSpots();
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
                bundleHash.put(position, locationToDiary);
            }

        @Override
        public int getItemCount() {
            return SQLiteDiary.size();
        }
    }

    private List<LocationToDiary> getSpots() {
        locationDao = new LocationDao(getContext());
        List<LocationToDiary> ltDiary = locationDao.autoDiary(getContext());
        List<LocationToDiary> SQliteDiary = new ArrayList<>();
        for (LocationToDiary d : ltDiary) {
            LocationToDiary addDiary = new LocationToDiary(d);
            addDiary.setImageId(R.drawable.picture1);
            SQliteDiary.add(addDiary);
        }
        // listView倒序
        Collections.reverse(SQliteDiary);
        return SQliteDiary;
    }
}

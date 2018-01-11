package com.kang.Dlife.tb_page1;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.kang.Dlife.Common;
import com.kang.Dlife.R;
import com.kang.Dlife.data_base.DiaryDetail;
import com.kang.Dlife.data_base.LocationTrace;
import com.kang.Dlife.sever.LocationDao;
import com.kang.Dlife.sever.LocationToDiary;
import com.kang.Dlife.tb_page1.diary_edit.DiaryEdit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

//不是Activity就一定要宣告View
public class Page1 extends Fragment {
    //    public Hashtable<Integer,LocationToDiary> bundleHash = new Hashtable<Integer,LocationToDiary>();
    public Hashtable<Integer, LocationToDiary> bundleHash = new Hashtable<Integer, LocationToDiary>();
    private LocationDao locationDao;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        //因為有宣告view, 所以之後可以在這頁裡面找下面之後要用到的id
        View view = inflater.inflate(R.layout.page1, container, false);
        //因為是Fragment所以要把this改成getActivity
        RecyclerView rvDiary = (RecyclerView) view.findViewById(R.id.diarylist);
        rvDiary.setLayoutManager(
                new StaggeredGridLayoutManager(
                        // spanCount(列數 or 行數), HORIZONTAL -> 水平, VERTICAL -> 垂直
                        1, StaggeredGridLayoutManager.VERTICAL));
        final List<LocationToDiary> Page1Spots = getSpots();
        rvDiary.setAdapter(new DiaryAdapter(getActivity(), Page1Spots));
        return view;
    }

    private class DiaryAdapter extends
            RecyclerView.Adapter<DiaryAdapter.MyViewHolder> implements View.OnClickListener {
        private Context context;
        private List<LocationToDiary> DiaryData;

        DiaryAdapter(Context context, List<LocationToDiary> DiaryData) {
            this.context = context;
            this.DiaryData = DiaryData;
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
            }
        }

        @Override
        public int getItemCount() {
            return DiaryData.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.page1_recycleview, viewGroup, false);
            itemView.setOnClickListener(this);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder viewHolder, final int position) {
            final LocationToDiary locationToDiary = DiaryData.get(position);
            viewHolder.imageView.setImageResource(locationToDiary.getImageId());
            viewHolder.icWeather.setImageResource(locationToDiary.getIcWeatherId());
            viewHolder.icNew.setImageResource(locationToDiary.getIcNewId());
            viewHolder.tvDate.setText(Common.dateStringToDay(locationToDiary.getStartDate()));
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

                            LocationToDiary spot = DiaryData.get(position);
                            Toast.makeText(getActivity(), Common.dateStringToDay(spot.getStartDate())
                                            + " " + Common.dateStringToHM(spot.getStartDate())
                                            + "-" + Common.dateStringToHM(spot.getEndDate())
                                            + "被删除了",
                                    Toast.LENGTH_SHORT).show();

                            DiaryData.remove(position);
                            // 刪除有動畫效果
                            notifyItemRemoved(position);
                            // 刪除SQL_lite的欄位
                            locationDao.deleteById(locationToDiary.getEndLocationSK());
                            // 重置DiaryData
                            DiaryData = getSpots();
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

        // recyclerview 點擊監聽
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), DiaryEdit.class);
            LocationToDiary bundleP = new LocationToDiary(bundleHash.get((int) view.getTag()));
            Bundle bundle = new Bundle();
            bundle.putSerializable("Page1Adapter", bundleP);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }


    private List<LocationToDiary> getSpots() {
        locationDao = new LocationDao(getContext());
        List<LocationToDiary> ltDiary = locationDao.autoDiary(getContext());
        List<LocationToDiary> page1Spots = new ArrayList<>();
        for (LocationToDiary d : ltDiary) {
            LocationToDiary addDiary = new LocationToDiary(d);
            addDiary.setImageId(R.drawable.ex_photo);
            addDiary.setIcWeatherId(R.drawable.ic_sun);
            addDiary.setIcNewId(R.drawable.ic_new);
            page1Spots.add(addDiary);
        }
        // listView倒序
//        Collections.reverse(page1Spots);

        return page1Spots;
    }
}
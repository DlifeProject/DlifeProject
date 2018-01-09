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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.kang.Dlife.Common;
import com.kang.Dlife.R;
import com.kang.Dlife.data_base.DiaryDetail;
import com.kang.Dlife.sever.LocationDao;
import com.kang.Dlife.sever.LocationToDiary;
import com.kang.Dlife.tb_page1.diary_edit.DiaryEdit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

//不是Activity就一定要宣告View
public class Page1 extends Fragment {
    private ListView lvSpots;
    public Hashtable<Integer,LocationToDiary> bundleHash = new Hashtable<Integer,LocationToDiary>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        //因為有宣告view, 所以之後可以在這頁裡面找下面之後要用到的id
        View view = inflater.inflate(R.layout.page1, container, false);
        //因為已經宣告過Fragment, 所以可直接在下面寫
        findViews(view);
        final List<LocationToDiary> Page1Spots = getSpots();
        //因為是Fragment所以要把this改成getActivity
        lvSpots.setAdapter(new SpotAdapter(Page1Spots, getActivity()));
        lvSpots.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {

                Intent intent = new Intent();
                intent.setClass(getActivity(), DiaryEdit.class);
                LocationToDiary bundleP = new LocationToDiary(bundleHash.get(index));
                Bundle bundle = new Bundle();
                bundle.putSerializable("Page1Spot",bundleP);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        lvSpots.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            //長按事件監聽器
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int index, long l) {

                PopupMenu popupMenu = new PopupMenu(getActivity(), view, Gravity.END);
                // 彈出視窗
                popupMenu.inflate(R.menu.popup_menu);
                // 彈出視窗的點擊監聽器
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        LocationToDiary spot = Page1Spots.get(index);

                        Toast.makeText(getActivity(), Common.dateStringToDay(spot.getStartDate())
                                        + " " + Common.dateStringToHM(spot.getStartDate())
                                        + "-" + Common.dateStringToHM(spot.getEndDate())
                                        + "被删除了",
                                Toast.LENGTH_SHORT).show();

                        //刪除所選該欄
                        Page1Spots.remove(spot);
                        //刷新頁面重新呼叫
                        lvSpots.setAdapter(new SpotAdapter(Page1Spots, getActivity()));
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
        return view;
    }

    private class SpotAdapter extends BaseAdapter {
        List<LocationToDiary> page1Spots;
        Context context;

        public SpotAdapter(List<LocationToDiary> Page1Spots, Context context) {
            this.page1Spots = Page1Spots;
            this.context = context;
        }

        @Override
        public int getCount() {
            return page1Spots.size();
        }

        @Override
        public View getView(int index, View itemView, ViewGroup viewGroup) {

            LocationToDiary page1Spot = page1Spots.get(index);
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            itemView = layoutInflater.inflate(R.layout.page1_listview, viewGroup, false);
            ImageView imageView = itemView.findViewById(R.id.imageView);
            ImageView icWeather = itemView.findViewById(R.id.weather);
            ImageView icNew = itemView.findViewById(R.id.icnew);
            TextView tvDate = itemView.findViewById(R.id.tvDate);
            TextView tvTimeStart = itemView.findViewById(R.id.tvTimeStart);
            TextView tvTimeEnd = itemView.findViewById(R.id.tvTimeEnd);
            TextView tvPlace = itemView.findViewById(R.id.tvPlace);
            TextView tvDiary = itemView.findViewById(R.id.tvDiary);

            imageView.setImageResource(page1Spot.getImageId());
            icWeather.setImageResource(page1Spot.getIcWeatherId());
            icNew.setImageResource(page1Spot.getIcNewId());
            tvDate.setText(Common.dateStringToDay(page1Spot.getStartDate()));
            tvTimeStart.setText(Common.dateStringToHM(page1Spot.getStartDate()));
            tvTimeEnd.setText(Common.dateStringToHM(page1Spot.getEndDate()));
            Geocoder geocoder = new Geocoder(getActivity());

            //測試時初始化防呆
            if(page1Spot.getLatitude() != 0.0){
                try{
                    List<Address> addressList =
                            geocoder.getFromLocation(page1Spot.getLatitude(), page1Spot.getLongitude(), 1);
                    if(addressList.size() > 0){
                        Address address = addressList.get(0);
                        String addrStr = "";
                        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++){
                            addrStr = address.getLocality();
                        }
                        tvPlace.setText(addrStr);
                    }else{
                        tvPlace.setText("Place");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            tvDiary.setText(page1Spot.getNote());

            bundleHash.put(index,page1Spot);

            return itemView;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

    }


    //因為前面是用到view所以findViews裡面不能為空值
    private void findViews(View view) {
        lvSpots = view.findViewById(R.id.listView);
    }

    private List<LocationToDiary> getSpots() {
        LocationDao locationDao = new LocationDao(getContext());
        List<LocationToDiary> ltDiary = locationDao.autoDiary(getContext());
        List<LocationToDiary> page1Spots = new ArrayList<>();
        for(LocationToDiary d:ltDiary) {
            LocationToDiary addDiary = new LocationToDiary(d);
            addDiary.setImageId(R.drawable.ex_photo);
            addDiary.setIcWeatherId(R.drawable.ic_sun);
            addDiary.setIcNewId(R.drawable.ic_new);
            page1Spots.add(addDiary);
        }
        // listView倒序
        Collections.reverse(page1Spots);

        return page1Spots;
    }
}
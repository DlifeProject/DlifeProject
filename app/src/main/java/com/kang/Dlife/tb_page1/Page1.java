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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.kang.Dlife.Common;
import com.kang.Dlife.R;
import com.kang.Dlife.data_base.LocationDao;
import com.kang.Dlife.tb_page1.diary_edit.DiaryEdit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

//不是Activity就一定要宣告View
public class Page1 extends Fragment {
    private ListView lvSpots;
    public Hashtable<Integer,DiaryDetail> bundleHash = new Hashtable<Integer,DiaryDetail>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        //因為有宣告view, 所以之後可以在這頁裡面找下面之後要用到的id
        View view = inflater.inflate(R.layout.page1, container, false);
        //因為已經宣告過Fragment, 所以可直接在下面寫
        findViews(view);
        final List<DiaryDetail> Page1Spots = getSpots();
        //因為是Fragment所以要把this改成getActivity
        lvSpots.setAdapter(new SpotAdapter(Page1Spots, getActivity()));
        lvSpots.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {


                Intent intent = new Intent();
                intent.setClass(getActivity(), DiaryEdit.class);
                DiaryDetail bundleP = new DiaryDetail(bundleHash.get(index));
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
                        DiaryDetail spot = Page1Spots.get(index);

                        Toast.makeText(getActivity(), Common.dateStringToDay(spot.getStart_date())
                                        + " " + Common.dateStringToHM(spot.getStart_date())
                                        + "-" + Common.dateStringToHM(spot.getEnd_date())
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
        List<DiaryDetail> page1Spots;
        Context context;

        public SpotAdapter(List<DiaryDetail> Page1Spots, Context context) {
            this.page1Spots = Page1Spots;
            this.context = context;
        }

        @Override
        public int getCount() {
            return page1Spots.size();
        }

        @Override
        public View getView(int index, View itemView, ViewGroup viewGroup) {
            DiaryDetail page1Spot = page1Spots.get(index);
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

            imageView.setImageResource(page1Spot.imageId);
            icWeather.setImageResource(page1Spot.icWeatherId);
            icNew.setImageResource(page1Spot.icNewId);
            tvDate.setText(Common.dateStringToDay(page1Spot.getStart_date()));
            tvTimeStart.setText(Common.dateStringToHM(page1Spot.getStart_date()));
            tvTimeEnd.setText(Common.dateStringToHM(page1Spot.getEnd_date()));
            Geocoder geocoder = new Geocoder(getActivity());
            try{
                List<Address> addressList =
                        geocoder.getFromLocation(page1Spot.latitude, page1Spot.longitude, 1);
                Address address = addressList.get(0);
                String addrStr = "";
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++){
                    addrStr = address.getLocality();

                }
                tvPlace.setText(addrStr);
            } catch (IOException e) {
                e.printStackTrace();
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

    private List<DiaryDetail> getSpots() {
        LocationDao locationDao = new LocationDao(getContext());
        List<DiaryDetail> ltDiary = locationDao.autoDiary(getContext());
        List<DiaryDetail> page1Spots = new ArrayList<>();
        for(DiaryDetail d:ltDiary) {
            DiaryDetail addDiary = new DiaryDetail(d);
            addDiary.imageId = R.drawable.ex_photo;
            addDiary.icWeatherId = R.drawable.ic_sun;
            addDiary.icNewId = R.drawable.ic_new;
            page1Spots.add(addDiary);


        }

//        Page1Spots.add(new Page1Spot(
//                R.drawable.picture1,
//                R.drawable.ic_sun,
//                R.drawable.ic_new,
//                "2017/11/10",
//                "09:00",
//                "12:00",
//                "國立中央大學",
//                "最近每天都在..."));
//        Page1Spots.add(new Page1Spot(R.drawable.picture2, R.drawable.ic_cloudy,
//                0, "2017/11/9", "08:00", "10:00",
//                "拉亞漢堡", "吃早餐"));
//        Page1Spots.add(new Page1Spot(R.drawable.picture3, R.drawable.ic_raining,
//                0, "2017/11/8", "14:00", "16:00",
//                "文化國小", "沒事不知道做啥"));
        return page1Spots;
    }
}


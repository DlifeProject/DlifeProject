package com.kang.Dlife.tb_page2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kang.Dlife.Common;
import com.kang.Dlife.R;
import com.kang.Dlife.sever.MyTask;
import com.kang.Dlife.tb_page1.diary_edit.DiaryEdit;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class Page2 extends Fragment implements View.OnClickListener {
    private final String TAG = "page2";
    private List<Object> itemList;
    private ViewPager vpItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page2, container, false);
        List<Object> itemList = getItemList();
        ItemAdapter itemAdapter = new ItemAdapter(getFragmentManager(), itemList);
        vpItem = view.findViewById(R.id.vpItem);
        vpItem.setAdapter(itemAdapter);RelativeLayout ry_Previous = view.findViewById(R.id.ry_Previous);
        ry_Previous.setOnClickListener(this);
        RelativeLayout ry_Next = view.findViewById(R.id.ry_Next);
        ry_Next.setOnClickListener(this);



        return view;

    }


    public List<Object> getItemList() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "categorySum");
        jsonObject.addProperty("account", Common.getAccount(getContext()));
        jsonObject.addProperty("password", Common.getPWD(getContext()));

        String msg = "";
        if (Common.checkNetConnected(getContext())) {
            String url = Common.URL + Common.WEBSUMMARY;
            MyTask myTask = new MyTask(url, jsonObject.toString());
            try {
                msg = myTask.execute().get().trim();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        Gson gson = new Gson();
        JsonObject outJsonObject = gson.fromJson(msg,JsonObject.class);
        String ltString = outJsonObject.get("CategorySum").getAsString();

        Type tySum = new TypeToken<List<CategorySum>>(){}.getType();
        List<CategorySum> ltJson = new Gson().fromJson(ltString, tySum);


        itemList = new ArrayList<>();
        itemList.add(new PieChartItem("Daily", 2017,
                10, 17, 2017, 10, 31));
        for(CategorySum s:ltJson){
            itemList.add(new CategorySum(s));
        }

        return itemList;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ry_Next:
                if (vpItem.getCurrentItem() == itemList.size() - 1) {
                    vpItem.setCurrentItem(itemList.size() - 1);
                } else {
                    vpItem.setCurrentItem(vpItem.getCurrentItem() + 1);
                }
                break;
            case R.id.ry_Previous:
                if (vpItem.getCurrentItem() == 0) {
                    vpItem.setCurrentItem(0);
                } else {
                    vpItem.setCurrentItem(vpItem.getCurrentItem() - 1);
                }
                break;
            default:
                break;
        }

    }


    private class ItemAdapter extends FragmentPagerAdapter {
        List<Object> itemList;

        public ItemAdapter(FragmentManager fm, List<Object> itemList) {
            super(fm);
            this.itemList = itemList;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;

            if (position == 0) {
                PieChartItem item = (PieChartItem) itemList.get(position);
                PieChartFragment pieChart_Fragment = new PieChartFragment();
                fragment = pieChart_Fragment;
                Bundle args = new Bundle();
                args.putSerializable("specialitem", item);
                fragment.setArguments(args);

            } else {
                CategorySum item = (CategorySum) itemList.get(position);
                CoverButton coverButton = new CoverButton();
                fragment = coverButton;
                Bundle args = new Bundle();
                args.putSerializable("item", item);
                fragment.setArguments(args);

            }
            return fragment;
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

    }

}
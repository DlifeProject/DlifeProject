package com.kang.Dlife.tb_page3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kang.Dlife.Common;
import com.kang.Dlife.R;
import com.kang.Dlife.sever.MyTask;

import java.lang.reflect.Type;
import java.util.List;

public class Page3 extends Fragment {

    private TextView tvNewFriendName;
    private TextView tvFriendShareCategory;
    private TextView tvMyShareCategory;

    private final static String TAG = "Page3";
    public List<MatchFriendItem> matchFriendItemList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page3, container, false);

        //init memberShareRelationList
        setMemberShareRelationList(getActivity());

        //我加了切換activity的button
        ImageButton addExchange=view.findViewById(R.id.addExchange);
        addExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity()  , ExchangeActivity.class);
                startActivity(intent);
            }
        });


        //init the latest friend
        tvNewFriendName = (TextView) view.findViewById(R.id.tvNewFriendName);
        tvMyShareCategory = (TextView) view.findViewById(R.id.tvMyShareCategory);
        tvFriendShareCategory = (TextView) view.findViewById(R.id.tvFriendShareCategory);


        if(matchFriendItemList.size() == 0 || matchFriendItemList.isEmpty()){
            tvNewFriendName.setText("Get a friend");
            tvMyShareCategory.setText("?");
            tvFriendShareCategory.setText("?");
        }else{

            tvNewFriendName.setText(matchFriendItemList.get(0).getMyFriendName());
            tvNewFriendName.setOnClickListener(new FriendDiaryListener(getActivity(),matchFriendItemList.get(0)));
            tvMyShareCategory.setText(matchFriendItemList.get(0).getMyCategory().substring(0,1));
            tvFriendShareCategory.setText(matchFriendItemList.get(0).getMyFriendCategory().substring(0,1));
        }


        //init friend list
        RecyclerView rvFriendlist = (RecyclerView) view.findViewById(R.id.rvFriendlist);
        rvFriendlist.setLayoutManager(
                new StaggeredGridLayoutManager(
                        // spanCount(列數 or 行數), HORIZONTAL -> 水平, VERTICAL -> 垂直
                        1, StaggeredGridLayoutManager.VERTICAL));
        final List<MatchFriendItem> friendSpots = getMatchSpots();
        rvFriendlist.setAdapter(new MatchAdapter(getActivity(), friendSpots));

        return view;

    }

    private void setMemberShareRelationList(FragmentActivity activity) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getFriendList");
        jsonObject.addProperty("account", Common.getAccount(getActivity()));
        jsonObject.addProperty("password", Common.getPWD(getActivity()));
        String url = Common.URL + Common.FRIEND;

        MyTask login = new MyTask(url,jsonObject.toString());
        String inStr = null;
        try {
            inStr = login.execute().get().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(inStr.isEmpty() == false && !inStr.equals("")){

            Gson gson = new Gson();
            JsonObject friendListJsonObject = gson.fromJson(inStr, JsonObject.class);
            String friendListString =  friendListJsonObject.get("getFriendList").getAsString();
            JsonArray friendListArray = gson.fromJson(friendListString, JsonArray.class);

            Type tempFriendList = new TypeToken<List<MatchFriendItem>>() {}.getType();
            matchFriendItemList = new Gson().fromJson(friendListArray, tempFriendList);

        }

    }
    private List<MatchFriendItem> getMatchSpots() {
        return matchFriendItemList;
    }

    private class MatchAdapter extends
            RecyclerView.Adapter<MatchAdapter.MyViewHolder> {

        private Context context;
        private List<MatchFriendItem> matchFriendItems;

        MatchAdapter(Context context, List<MatchFriendItem> matchFriendItems){
            this.matchFriendItems = matchFriendItems;
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.page3_recycleview, viewGroup, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder viewHolder, int position) {

            position = position + 1;

            final MatchFriendItem matchFriendItem = matchFriendItems.get(position);

            viewHolder.tvMyCategory.setText(matchFriendItem.getMyCategory().substring(0,1));
            viewHolder.tvMyFriendName.setText(matchFriendItem.getMyFriendName());
            viewHolder.tvMyFriendName.setOnClickListener(new FriendDiaryListener(getActivity(),matchFriendItem));
            viewHolder.tvMyFriendCategory.setText(matchFriendItem.getMyFriendCategory().substring(0,1));

        }

        @Override
        public int getItemCount() {

            int itemCount = matchFriendItems.size();
            if(itemCount >= 1){
                itemCount = itemCount - 1;
            }

            return itemCount;
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            private LinearLayout llFriendItem;
            private TextView tvMyCategory;
            private TextView tvMyFriendName;
            private TextView tvMyFriendCategory;

            public MyViewHolder(View itemView) {
                super(itemView);
                llFriendItem = itemView.findViewById(R.id.llFriendItem);
                tvMyCategory = itemView.findViewById(R.id.tvMyCategory);
                tvMyFriendName = itemView.findViewById(R.id.tvMyFriendName);
                tvMyFriendCategory = itemView.findViewById(R.id.tvMyFriendCategory);
            }
        }
    }

    class FriendDiaryListener implements View.OnClickListener {

        private Activity activity;
        private MatchFriendItem matchFriendItem;

        public FriendDiaryListener(Activity activity, MatchFriendItem matchFriendItem) {
            super();
            this.activity = activity;
            this.matchFriendItem = matchFriendItem;
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            intent.setClass(activity, FriendDiaryViewActivity.class);
            Bundle bundle = new Bundle();

            bundle.putSerializable("MatchFriendItem", matchFriendItem);
            bundle.putString("fromPage","page3");
            intent.putExtras(bundle);
            startActivity(intent);

        }
    }

}
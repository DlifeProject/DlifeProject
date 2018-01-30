package com.kang.Dlife.tb_page3;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kang.Dlife.Common;
import com.kang.Dlife.R;

import java.util.ArrayList;
import java.util.List;

public class Page3 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page3, container, false);

        RecyclerView rvFriendlist = (RecyclerView) view.findViewById(R.id.rvFriendlist);

        rvFriendlist.setLayoutManager(
                new StaggeredGridLayoutManager(
                        // spanCount(列數 or 行數), HORIZONTAL -> 水平, VERTICAL -> 垂直
                        1, StaggeredGridLayoutManager.VERTICAL));
        final List<MatchFriendItem> friendSpots = getMatchSpots();
        rvFriendlist.setAdapter(new MatchAdapter(getActivity(), friendSpots));

        return view;

    }

    private List<MatchFriendItem> getMatchSpots() {
        List<MatchFriendItem> matchFriendItems = new ArrayList<>();

        matchFriendItems.add(new MatchFriendItem("H","S","cow"));
        matchFriendItems.add(new MatchFriendItem("R","S","bay"));
        matchFriendItems.add(new MatchFriendItem("S","S","la"));

        return matchFriendItems;
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
            final MatchFriendItem matchFriendItem = matchFriendItems.get(position);

            viewHolder.tvMyCategory.setText(matchFriendItem.getMyCategory());
            viewHolder.tvMyFriendName.setText(matchFriendItem.getMyFriendName());
            viewHolder.tvMyFriendCategory.setText(matchFriendItem.getMyFriendCategory());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.showToast(context,"hi");
                }
            });

            int i;
            i = 0;


        }

        @Override
        public int getItemCount() { return matchFriendItems.size(); }


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

}
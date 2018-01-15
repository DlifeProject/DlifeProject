package com.kang.Dlife.tb_page2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kang.Dlife.R;
import com.kang.Dlife.tb_page2.diary_view.DiaryView;


public class CoverButton extends Fragment {
    private CategorySum item;
    RelativeLayout ry_click;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            item = (CategorySum) getArguments().getSerializable("item");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page2_viewpager, container, false);
        if (getArguments() != null) {
            item = (CategorySum) getArguments().getSerializable("item");
        }
        ImageView iv_pic = view.findViewById(R.id.iv_pic);
        iv_pic.setImageResource(item.getDiaryPhotoSK());

        final TextView tv_click = view.findViewById(R.id.tv_click);
        tv_click.setText(item.getCategoryType());

        ry_click = view.findViewById(R.id.ry_click);
        ry_click.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //更改按下去時的圖片
                    tv_click.setTextColor(Color.parseColor("#4f4f4f"));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //抬起來時的圖片
                    tv_click.setTextColor(Color.WHITE);
                }
                return false;
            }
        });
        ry_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(getActivity(),DiaryView.class);
                Bundle bundle = new Bundle();

                bundle.putSerializable("CategorySum",item);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        TextView tv_theme = view.findViewById(R.id.tv_theme);
        tv_theme.setText(item.getNote());

        TextView tv_year = view.findViewById(R.id.tv_year);
        tv_year.setText(String.valueOf(item.getYear()));

        TextView tv_month = view.findViewById(R.id.tv_month);
        tv_month.setText(String.valueOf(item.getMonth()));

        TextView tv_day = view.findViewById(R.id.tv_day);
        tv_day.setText(String.valueOf(item.getDay()));

        TextView tv_threedays = view.findViewById(R.id.tv_threedays);
        tv_threedays.setText(String.valueOf(item.getThree_day()));

        TextView tv_sevendays = view.findViewById(R.id.tv_sevendays);
        tv_sevendays.setText(String.valueOf(item.getSeven_day()));

        return view;
    }
}
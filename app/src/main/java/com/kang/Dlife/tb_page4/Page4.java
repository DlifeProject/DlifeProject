package com.kang.Dlife.tb_page4;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kang.Dlife.R;


public class Page4 extends Fragment {
    private LinearLayout person_data;
    private LinearLayout person_located;
    private LinearLayout facebook;
    private LinearLayout google;
    private LinearLayout gps_list;
    private LinearLayout gps_upload;
    private ImageView iv1;      //個人資料
    private ImageView iv2;      //個人地點
    private ImageView iv3;      //FB
    private ImageView iv4;      //GOOGLE
    private ImageView iv5;      //GPS 列表
    private ImageView iv6;      //GPS 上傳
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        //因為有宣告view, 所以之後可以在這頁裡面找下面之後要用到的id
        View view = inflater.inflate(R.layout.page4, container, false);
        findViews(view);
        person_data.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //更改按下去時的圖片
                    iv1.setImageResource(R.drawable.person_data_onclick);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //抬起來時的圖片
                    iv1.setImageResource(R.drawable.person_data);
                }
                return false;
            }
        });
        person_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), PersonData.class);
                startActivity(intent);
            }
        });
        person_located.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //更改按下去時的圖片
                    iv2.setImageResource(R.drawable.person_located_onclick);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //抬起來時的圖片
                    iv2.setImageResource(R.drawable.person_located);
                }
                return false;
            }
        });
        person_located.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), PersonLocated.class);
                startActivity(intent);
            }
        });
        facebook.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //更改按下去時的圖片
                    iv3.setImageResource(R.drawable.facebook_onclick);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //抬起來時的圖片
                    iv3.setImageResource(R.drawable.facebook);
                }
                return false;
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), FacebookActivity.class);
                startActivity(intent);
            }
        });
        google.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //更改按下去時的圖片
                    iv4.setImageResource(R.drawable.google_onclick);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //抬起來時的圖片
                    iv4.setImageResource(R.drawable.google);
                }
                return false;
            }
        });
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), Google.class);
                startActivity(intent);
            }
        });
        gps_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //更改按下去時的圖片
                    iv5.setImageResource(R.drawable.gps_list_onclick);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //抬起來時的圖片
                    iv5.setImageResource(R.drawable.gps_list);
                }
                return false;
            }
        });
        gps_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), GpsList.class);
                startActivity(intent);
            }
        });
        gps_upload.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //更改按下去時的圖片
                    iv6.setImageResource(R.drawable.gps_upload_onclick);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //抬起來時的圖片
                    iv6.setImageResource(R.drawable.gps_upload);
                }
                return false;
            }
        });
        gps_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), GpsUpload.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        //Common.showToast(getContext(),"test");

    }

    private void findViews(View view) {
        person_data = view.findViewById(R.id.person_data);
        person_located = view.findViewById(R.id.person_located);
        facebook = view.findViewById(R.id.facebook);
        google = view.findViewById(R.id.google);
        gps_list = view.findViewById(R.id.gps_list);
        gps_upload = view.findViewById(R.id.gps_upload);
        iv1 = view.findViewById(R.id.iv1);
        iv2 = view.findViewById(R.id.iv2);
        iv3 = view.findViewById(R.id.iv3);
        iv4 = view.findViewById(R.id.iv4);
        iv5 = view.findViewById(R.id.iv5);
        iv6 = view.findViewById(R.id.iv6);
    }
}
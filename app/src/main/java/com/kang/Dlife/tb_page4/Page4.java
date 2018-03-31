package com.kang.Dlife.tb_page4;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kang.Dlife.Common;
import com.kang.Dlife.R;
import com.kang.Dlife.data_base.Member;
import com.kang.Dlife.sever.MyTask;
import com.kang.Dlife.tb_page4.personData.ChangeBirthday;
import com.kang.Dlife.tb_page4.personData.ChangeGender;
import com.kang.Dlife.tb_page4.personData.ChangeNickname;
import com.kang.Dlife.tb_page4.personData.ChangePassword;

import org.w3c.dom.Text;


public class Page4 extends Fragment {


    private static final String TAG = "page4";

    private TextView tvAccount, tvNickname, tvBirthday, tvGender, tvFacebookList, tvLogout;
    private ImageButton ibChangePassword, ibNickname, ibBirthday, ibGender, ibFacebook, ibLogout;

    private String account = "";
    public String nickname = "";
    private String birthday = "";
    private int gender = 2;
    private String logout = "";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        //因為有宣告view, 所以之後可以在這頁裡面找下面之後要用到的id
        View view = inflater.inflate(R.layout.page4, container, false);
        findView(view);
        setViewData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setViewData();
    }

    private void setViewData() {
        String url = Common.URL + Common.WEBLOGIN;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "memberProfile");
        jsonObject.addProperty("account", Common.getAccount(getActivity()));
        jsonObject.addProperty("password", Common.getPWD(getActivity()));
        MyTask login = new MyTask(url,jsonObject.toString());
        try {
            String inStr;
            inStr = login.execute().get().trim();
            Gson gson = new Gson();
            JsonObject diaryInJsonObject = gson.fromJson(inStr,JsonObject.class);
            String memberProfileString = diaryInJsonObject.get("memberProfile").getAsString().trim();

            Gson gsonIn = new Gson();
            Member memberProfile = gsonIn.fromJson(memberProfileString,Member.class);

            account = memberProfile.getApp_account();
            nickname = memberProfile.getNick_name();
            if(nickname.isEmpty()){
                nickname = "";
            }
            birthday = memberProfile.getBirthday();
            gender = memberProfile.getSex();
            logout = memberProfile.getLogin_date();

            tvAccount.setText(account);
            tvNickname.setText(nickname);
            tvBirthday.setText(birthday);
            if(gender == 0){
                tvGender.setText(R.string.lady);
            }else if(gender == 1){
                tvGender.setText(R.string.man);
            }else{
                tvGender.setText("--");
            }
            tvLogout.setText(logout);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String urlFB = Common.URL + Common.FRIEND;
        JsonObject jsonObjectFBCount = new JsonObject();
        jsonObjectFBCount.addProperty("action", "getFacebookFriendCount");
        jsonObjectFBCount.addProperty("account", Common.getAccount(getActivity()));
        jsonObjectFBCount.addProperty("password", Common.getPWD(getActivity()));
        MyTask taskFBCount = new MyTask(urlFB,jsonObjectFBCount.toString());
        try {
            String inStr;
            inStr = taskFBCount.execute().get().trim();
            if(inStr.equals("")){
                tvFacebookList.setText("Unknow");
            }else{
                tvFacebookList.setText(inStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findView(View view) {

        tvAccount = (TextView)view.findViewById(R.id.tvAccount);
        tvNickname = (TextView)view.findViewById(R.id.tvNickname);
        tvBirthday = (TextView)view.findViewById(R.id.tvBirthday);
        tvGender = (TextView)view.findViewById(R.id.tvGender);
        tvFacebookList = (TextView)view.findViewById(R.id.tvFacebookList);
        tvLogout = (TextView)view.findViewById(R.id.tvLogout);

        ibChangePassword = (ImageButton)view.findViewById(R.id.ibChangePassword);
        ibNickname = (ImageButton)view.findViewById(R.id.ibNickname);
        ibBirthday = (ImageButton)view.findViewById(R.id.ibBirthday);
        ibGender = (ImageButton)view.findViewById(R.id.ibGender);
        ibFacebook = (ImageButton)view.findViewById(R.id.ibFacebook);
        ibLogout = (ImageButton)view.findViewById(R.id.ibLogout);

        tvAccount.setText(account);
        tvNickname.setText(nickname);
        tvBirthday.setText(birthday);
        if(gender == 0){
            tvGender.setText(R.string.lady);
        }else if(gender == 1){
            tvGender.setText(R.string.man);
        }else{
            tvGender.setText("--");
        }
        tvLogout.setText(logout);

        ibChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass( getActivity(), ChangePassword.class);
                startActivity(intent);
            }
        });

        ibNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass( getActivity(), ChangeNickname.class);
                Bundle bundle = new Bundle();
                bundle.putString("nickname",nickname);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        ibBirthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass( getActivity(), ChangeBirthday.class);
                Bundle bundle = new Bundle();
                bundle.putString("birthday",birthday);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        ibGender.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass( getActivity(), ChangeGender.class);
                Bundle bundle = new Bundle();
                bundle.putInt("gender",gender);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        ibFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass( getActivity(), FacebookActivity.class);
                startActivity(intent);

            }
        });

    }
}
package com.kang.Dlife.tb_page4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kang.Dlife.Common;
import com.kang.Dlife.R;
import com.kang.Dlife.TabActivity;
import com.kang.Dlife.data_base.Member;
import com.kang.Dlife.sever.MyTask;


public class PersonData extends AppCompatActivity {

    private static final String TAG = "PersonData";

    private TextView tvAccount, tvNickname, tvBirthday, tvGender, tvLogout;
    private ImageButton ibChangePassword, ibNickname, ibBirthday, ibGender, ibLogout;

    private String account = "";
    private String nickname = "";
    private String birthday = "";
    private int gender = 2;
    private String logout = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.page4_person_data);
        findView();


    }

    private void findView() {

        tvAccount = (TextView)findViewById(R.id.tvAccount);
        tvNickname = (TextView)findViewById(R.id.tvNickname);
        tvBirthday = (TextView)findViewById(R.id.tvBirthday);
        tvGender = (TextView)findViewById(R.id.tvGender);
        tvLogout = (TextView)findViewById(R.id.tvLogout);

        ibChangePassword = (ImageButton)findViewById(R.id.ibChangePassword);
        ibNickname = (ImageButton)findViewById(R.id.ibNickname);
        ibBirthday = (ImageButton)findViewById(R.id.ibBirthday);
        ibGender = (ImageButton)findViewById(R.id.ibGender);
        ibLogout = (ImageButton)findViewById(R.id.ibLogout);

        String url = Common.URL + Common.WEBLOGIN;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "memberProfile");
        jsonObject.addProperty("account", Common.getAccount(PersonData.this));
        jsonObject.addProperty("password", Common.getPWD(PersonData.this));


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
            birthday = memberProfile.getBirthday();
            gender = memberProfile.getSex();
            logout = memberProfile.getLogin_date();

        } catch (Exception e) {
            e.printStackTrace();
        }


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
                intent.setClass( PersonData.this, ChangePassword.class);
                startActivity(intent);
            }
        });

    }

    public void onBackClick(View view) {
        finish();
    }
}

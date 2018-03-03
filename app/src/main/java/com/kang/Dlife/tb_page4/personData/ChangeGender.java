package com.kang.Dlife.tb_page4.personData;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.kang.Dlife.Common;
import com.kang.Dlife.R;
import com.kang.Dlife.sever.MyTask;

public class ChangeGender extends AppCompatActivity {

    private TextView tvUpdate;
    private RadioGroup rgGender;
    private Button btChangeGenger;
    private int genderNumber = 0;
    private boolean isUpdate = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page4_change_gender);
        findView();
    }

    public void onBackClick(View view) {
        finish();
    }

    private void findView() {

        Bundle bundle = getIntent().getExtras();
        final int bundleGender = bundle.getInt("gender");

        tvUpdate = (TextView)findViewById(R.id.tvUpdate);
        rgGender = (RadioGroup)findViewById(R.id.rgGender);
        if(bundleGender == 0){
            rgGender.check(R.id.rbLady);
        }else if(bundleGender == 1){
            rgGender.check(R.id.rbMan);
        }
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.rbMan){
                    btChangeGenger.setVisibility(View.GONE);
                    genderNumber = 1;
                    if (genderNumber != bundleGender && !isUpdate){
                        btChangeGenger.setVisibility(View.VISIBLE);
                    }
                }else if(checkedId == R.id.rbLady && !isUpdate){
                    btChangeGenger.setVisibility(View.GONE);
                    genderNumber = 0;
                    if (genderNumber != bundleGender){
                        btChangeGenger.setVisibility(View.VISIBLE);
                    }
                }else{
                    btChangeGenger.setVisibility(View.GONE);
                    genderNumber = 2;
                    if (genderNumber != bundleGender && !isUpdate){
                        btChangeGenger.setVisibility(View.VISIBLE);
                    }
                }

            }
        });


        btChangeGenger = (Button)findViewById(R.id.btChangeGenger);
        btChangeGenger.setVisibility(View.GONE);
        btChangeGenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "changeGender");
                jsonObject.addProperty("account", Common.getAccount(ChangeGender.this));
                jsonObject.addProperty("password", Common.getPWD(ChangeGender.this));
                jsonObject.addProperty("newGender", Integer.toString(genderNumber));

                String url = Common.URL + Common.WEBLOGIN;

                MyTask login = new MyTask(url,jsonObject.toString());
                String inStr = null;
                try {
                    inStr = login.execute().get().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(inStr.equals("genderUpdateSuccess")){
                    tvUpdate.setText("Update Success");
                    btChangeGenger.setVisibility(View.GONE);
                    isUpdate = true;
                }

            }
        });



    }
}

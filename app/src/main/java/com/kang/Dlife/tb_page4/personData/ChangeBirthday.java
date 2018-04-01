package com.kang.Dlife.tb_page4.personData;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.kang.Dlife.Common;
import com.kang.Dlife.R;
import com.kang.Dlife.sever.MyTask;

import java.util.Calendar;


public class ChangeBirthday extends AppCompatActivity {

    private TextView tvUpdate;
    private TextView tvChangeBirthday;
    private Button btChangeNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page4_change_birthday);
        findView();
    }

    public void onBackClick(View view) {
        finish();
    }

    private void findView() {

        Bundle bundle = getIntent().getExtras();
        final String bundleBirthday = bundle.getString("birthday");

        tvUpdate = (TextView)findViewById(R.id.tvUpdate);
        tvChangeBirthday = (TextView)findViewById(R.id.tvChangeBirthday);
        btChangeNickName = (Button)findViewById(R.id.btChangeNickName);

        tvUpdate.setText(R.string.initBirthday);
        tvChangeBirthday.setText(bundleBirthday);
        tvChangeBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year;
                int month;
                int day;

                if(bundleBirthday == null || bundleBirthday.equals("")){
                    year = 1990;
                    month = 0;
                    day = 1;
                }else{
                    String[] birthdayArray = bundleBirthday.split("-");
                    year = Integer.parseInt(birthdayArray[0]);
                    month = Integer.parseInt(birthdayArray[1]);
                    day = Integer.parseInt(birthdayArray[2]);
                }


                new DatePickerDialog(ChangeBirthday.this, android.R.style.Theme_Holo_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvChangeBirthday.setText(Common.setDateFormat(year,month + 1,dayOfMonth));
                        btChangeNickName.setVisibility(View.VISIBLE);
                    }

                }, year,month, day).show();

            }
        });

        btChangeNickName.setVisibility(View.GONE);
        btChangeNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "changeBirthday");
                jsonObject.addProperty("account", Common.getAccount(ChangeBirthday.this));
                jsonObject.addProperty("password", Common.getPWD(ChangeBirthday.this));
                jsonObject.addProperty("newBirthday", (String) tvChangeBirthday.getText());

                String url = Common.URL + Common.WEBLOGIN;

                MyTask login = new MyTask(url,jsonObject.toString());
                String inStr = null;
                try {
                    inStr = login.execute().get().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(inStr.equals("birthdayUpdateSuccess")){
                    tvUpdate.setText("Update Success");
                    Common.setPrefferencesString(ChangeBirthday.this,Common.PREFFERENCES_BIRTHDAY,(String) tvChangeBirthday.getText());
                    btChangeNickName.setVisibility(View.GONE);
                }

            }
        });




    }

}

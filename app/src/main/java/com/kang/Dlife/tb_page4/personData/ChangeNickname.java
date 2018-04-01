package com.kang.Dlife.tb_page4.personData;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.kang.Dlife.Common;
import com.kang.Dlife.R;
import com.kang.Dlife.sever.MyTask;

public class ChangeNickname extends AppCompatActivity {

    private TextView tvUpdate;
    private EditText etChangeNickname;
    private Button btChangeNickName;
    String bundleNickname = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page4_change_nickname);

        Bundle bundle = getIntent().getExtras();
        bundleNickname = bundle.getString("nickname");

        findView();
    }

    public void onBackClick(View view) {
        finish();
    }

    private void findView() {

        tvUpdate = (TextView)findViewById(R.id.tvUpdate);
        etChangeNickname = (EditText)findViewById(R.id.etChangeNickname);
        btChangeNickName = (Button)findViewById(R.id.btChangeNickName);

        if(bundleNickname == null){
            tvUpdate.setText("---");
        }else{
            tvUpdate.setText(bundleNickname);
        }

        btChangeNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btChangeNickName.setVisibility(View.INVISIBLE);
                String newNickname = etChangeNickname.getText().toString().trim();

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "changeNickname");
                jsonObject.addProperty("account", Common.getAccount(ChangeNickname.this));
                jsonObject.addProperty("password", Common.getPWD(ChangeNickname.this));
                jsonObject.addProperty("newNickname", newNickname);

                String url = Common.URL + Common.WEBLOGIN;

                MyTask login = new MyTask(url,jsonObject.toString());
                String inStr = null;
                try {
                    inStr = login.execute().get().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(inStr.equals("nicknameUpdateSuccess")){
                    tvUpdate.setText("Update Success");
                    Common.setPrefferencesString(ChangeNickname.this,Common.PREFFERENCES_NICKNAME,newNickname);
                }

            }
        });


    }

}

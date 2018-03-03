package com.kang.Dlife.tb_page4.personData;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kang.Dlife.Common;
import com.kang.Dlife.R;
import com.kang.Dlife.sever.MyTask;

public class ChangePassword extends AppCompatActivity {

    private EditText etChangePassword;
    private TextView tvUpdate;
    private Button btChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page4_change_password);
        findView();
    }

    private void findView() {

        etChangePassword = (EditText)findViewById(R.id.etChangePassword);
        tvUpdate = (TextView)findViewById(R.id.tvUpdate);
        btChangePassword = (Button)findViewById(R.id.btChangePassword);
        btChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btChangePassword.setVisibility(View.INVISIBLE);
                String newPassword = etChangePassword.getText().toString().trim();
                if(Common.checkPWDSecurity(newPassword).equals("ok") ){

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "changePassword");
                    jsonObject.addProperty("account", Common.getAccount(ChangePassword.this));
                    jsonObject.addProperty("password", Common.getPWD(ChangePassword.this));
                    jsonObject.addProperty("newPassword", newPassword);

                    String url = Common.URL + Common.WEBLOGIN;

                    MyTask login = new MyTask(url,jsonObject.toString());
                    String inStr = null;
                    try {
                        inStr = login.execute().get().trim();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if(inStr.equals("passwordUpdateSuccess")){
                        tvUpdate.setText("Update Success");
                        Common.setPrefferencesString(ChangePassword.this,Common.PREFFERENCES_USER_PASSWORD,newPassword);
                    }


                }else{
                    Common.showToast(ChangePassword.this, newPassword);
                    btChangePassword.setVisibility(View.VISIBLE);
                }

            }
        });


    }

    public void onBackClick(View view) {
        finish();
    }
}

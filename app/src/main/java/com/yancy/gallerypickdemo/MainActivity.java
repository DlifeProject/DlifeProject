package com.yancy.gallerypickdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yancy.gallerypickdemo.data_base.Member;
import com.yancy.gallerypickdemo.sever.MyTask;


public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private String userAccount, userPassword, userUUID;

    private TextView tvTemp;
    private EditText etLoginAccount;
    private EditText etLoginPassword;
    private Button btLogin,btReset,btLoginRegan,btLoginJessica,btLoginC,btLoginAllen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_login);


        userUUID = Common.getUUID(this);
        findView(this);

        if(Common.checkNetConnected(this)){
            if(checkInitLogin()){
                //startService
                Common.startTabActivity(this);
            }
        }else{
            Common.showToast(this, "Web is not connected");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    private boolean checkInitLogin() {

        userAccount = Common.getPrefferencesData(this, Common.PREFFERENCES_USER_ACCOUNT);
        userPassword = Common.getPrefferencesData(this, Common.PREFFERENCES_USER_PASSWORD);

        if(userAccount.length() > 0 && userPassword.length() > 0 && userUUID.length() > 0){

            String loginStatus = webLogin(this,userAccount, userPassword, userUUID);
            if(loginStatus == "login"){
                Common.updateLoginPreferences(this, userAccount, userPassword, userUUID);
                return true;
            }else{
                return false;
            }

        }else{
            return false;
        }

    }

    private void findView(final Context c){

        tvTemp = (TextView) findViewById(R.id.tvTemp);
        etLoginAccount = (EditText) findViewById(R.id.etLoginAccount);
        etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);

        btReset = (Button) findViewById(R.id.btReset);
        btReset.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                etLoginAccount.setText("");
                etLoginPassword.setText("");
                Common.setPrefferencesString(c,Common.PREFFERENCES_UUID,"");
                Common.setPrefferencesString(c,Common.PREFFERENCES_USER_ACCOUNT,"");
                Common.setPrefferencesString(c,Common.PREFFERENCES_USER_PASSWORD,"");
                Common.setPrefferencesString(c,Common.PREFFERENCES_USER_LAST_LOGIN_DATE,"");
            }
        });

        btLogin = (Button) findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                tvTemp.setText("save account");
                userAccount = etLoginAccount.getText().toString().trim();
                userPassword = etLoginPassword.getText().toString().trim();
                String loginMessage = "";
                if(checkLoginRule()){
                    loginMessage = webLogin(c,userAccount,userPassword,userUUID);

                    if( loginMessage.equals("login")){
                        Common.updateLoginPreferences(c, userAccount,  userPassword, userUUID);
                        Common.startTabActivity(c);
                    }else{
                        Common.showToast(c,loginMessage);
                    }
                }
            }
        });

        btLoginRegan = (Button) findViewById(R.id.btLoginRegan);
        btLoginRegan.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                etLoginAccount.setText("irv278@gmail.com");
                etLoginPassword.setText("Regan");
            }
        });

        btLoginJessica = (Button) findViewById(R.id.btLoginJessica);
        btLoginJessica.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                etLoginAccount.setText("a55665203030@gmail.com");
                etLoginPassword.setText("Jessica");
            }
        });

        btLoginC = (Button) findViewById(R.id.btLoginC);
        btLoginC.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                etLoginAccount.setText("twodan7566@gmail.com");
                etLoginPassword.setText("twodan7566@gmail.com");
            }
        });

        btLoginAllen = (Button) findViewById(R.id.btLoginAllen);
        btLoginAllen.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                etLoginAccount.setText("af19git5@gmail.com");
                etLoginPassword.setText("btLoginAllen");
            }
        });

    }

    private boolean checkLoginRule(){

        String loginAccount = String.valueOf(etLoginAccount.getText()).trim();
        String loginPassword = String.valueOf(etLoginAccount.getText()).trim();

        if(loginAccount.isEmpty()){
            Common.showToast(this, "Account is empty");
            return false;
        }else if(!checkLoginAccountEmail(loginAccount)){
            Common.showToast(this, "Is not an email address!");
            return false;
        }else if(loginPassword.length() < 6){
            Common.showToast(this, "Password should longer then 6 char");
            return false;
        }else{
            return true;
        }

    }

    private boolean checkLoginAccountEmail(String account){

        if(account.contains("@")){
            return true;
        }else{
            return false;
        }

    }

    public static String webLogin(Context c,String userAccount, String userPassword, String userUUID){

        Member member = new Member(userUUID,userAccount,userPassword);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "login");
        jsonObject.addProperty("member", new Gson().toJson(member));

        String url = Common.URL + Common.WEBLOGIN;

        MyTask login = new MyTask(url,jsonObject.toString());
        String inStr = null;
        try {
            inStr = login.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inStr;
    }

}

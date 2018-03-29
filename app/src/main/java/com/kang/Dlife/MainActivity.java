package com.kang.Dlife;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kang.Dlife.data_base.Member;
import com.kang.Dlife.sever.MyTask;
import com.kang.Dlife.tb_page4.FacebookMember;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private String userAccount, userPassword, userUUID;

    private TextView tvTemp;
    private EditText etLoginAccount;
    private EditText etLoginPassword;
    private Button btLogin, btReset, btFacebookLogin, btLoginRegan, btLoginJessica, btLoginC, btLoginAllen;

    //facebook
    private String fbUserId;
    private String fbUserName;
    private String fbEmail;
    private List<FacebookMember> friendIdList;
    private LoginManager loginManager;
    public CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userUUID = Common.getUUID(this);
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.main_login);

        findView(this);

        if (Common.checkNetConnected(this)) {
            if (checkInitLogin()) {
                //Common.startTabActivity(MainActivity.this);
                //askPermissions();
                //finish();
            }
        } else {
            Common.showToast(this, "Web is not connected");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //finish();
    }


    private boolean checkInitLogin() {

        userAccount = Common.getPrefferencesData(this, Common.PREFFERENCES_USER_ACCOUNT);
        userPassword = Common.getPrefferencesData(this, Common.PREFFERENCES_USER_PASSWORD);

        if (userAccount.length() > 0 && userPassword.length() > 0 && userUUID.length() > 0) {
            String loginStatus = webLogin(this, userAccount, userPassword, userUUID);
            if (loginStatus.equals("login")) {
                Common.updateLoginPreferences(this, userAccount, userPassword, userUUID);
                etLoginAccount.setText(userAccount);
                etLoginPassword.setText(userPassword);
                //Common.startTabActivity(MainActivity.this);
                //askPermissions();
                //finish();
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }

    }

    private void findView(final Context c) {

        tvTemp = (TextView) findViewById(R.id.tvTemp);
        etLoginAccount = (EditText) findViewById(R.id.etLoginAccount);
        etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);

        btReset = (Button) findViewById(R.id.btReset);
        btReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                etLoginAccount.setText("");
                etLoginPassword.setText("");
                Common.setPrefferencesString(c, Common.PREFFERENCES_UUID, "");
                Common.setPrefferencesString(c, Common.PREFFERENCES_USER_ACCOUNT, "");
                Common.setPrefferencesString(c, Common.PREFFERENCES_USER_PASSWORD, "");
                Common.setPrefferencesString(c, Common.PREFFERENCES_USER_LAST_LOGIN_DATE, "");
            }
        });

        btLogin = (Button) findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tvTemp.setText("save account");
                userAccount = etLoginAccount.getText().toString().trim();
                userPassword = etLoginPassword.getText().toString().trim();
                String loginMessage = "";
                if (checkLoginRule()) {
                    loginMessage = webLogin(c, userAccount, userPassword, userUUID);

                    if (loginMessage.equals("login") || loginMessage.equals("addSuccess")) {
                        Common.updateLoginPreferences(c, userAccount, userPassword, userUUID);
                        Common.startTabActivity(c);
                        askPermissions();
                        finish();
                    } else {
                        Common.showToast(c, loginMessage);
                    }
                }
            }
        });
        btFacebookLogin = (Button) findViewById(R.id.btFacebookLogin);
        btFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.updateLoginPreferences(c, userAccount, userPassword, userUUID);
                loginFB();
            }
        });

        btLoginRegan = (Button) findViewById(R.id.btLoginRegan);
        btLoginRegan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                etLoginAccount.setText("irv278@gmail.com");
                etLoginPassword.setText("Regan");
            }
        });
        btLoginRegan.setVisibility(View.INVISIBLE);

        btLoginJessica = (Button) findViewById(R.id.btLoginJessica);
        btLoginJessica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                etLoginAccount.setText("a55665203030@gmail.com");
                etLoginPassword.setText("Jessica");
            }
        });
        btLoginJessica.setVisibility(View.INVISIBLE);

        btLoginC = (Button) findViewById(R.id.btLoginC);
        btLoginC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                etLoginAccount.setText("twodan7566@gmail.com");
                etLoginPassword.setText("twodan7566@gmail.com");
            }
        });
        btLoginC.setVisibility(View.INVISIBLE);

        btLoginAllen = (Button) findViewById(R.id.btLoginAllen);
        btLoginAllen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                etLoginAccount.setText("af19git5@gmail.com");
                etLoginPassword.setText("Allen");
            }
        });
        btLoginAllen.setVisibility(View.INVISIBLE);

    }

    private boolean checkLoginRule() {

        String loginAccount = String.valueOf(etLoginAccount.getText()).trim();
        String loginPassword = String.valueOf(etLoginAccount.getText()).trim();

        if (loginAccount.isEmpty()) {
            Common.showToast(this, "Account is empty");
            return false;
        } else if (!Common.checkLoginAccountEmail(loginAccount)) {
            Common.showToast(this, "Is not an email address!");
            return false;
        } else if (loginPassword.length() < 6) {
            Common.showToast(this, "Password should longer then 6 char");
            return false;
        } else {
            return true;
        }

    }

    public static String webLogin(Context c, String userAccount, String userPassword, String userUUID) {

        Member member = new Member(userUUID, userAccount, userPassword);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "login");
        jsonObject.addProperty("member", new Gson().toJson(member));
        String url = Common.URL + Common.WEBLOGIN;

        MyTask login = new MyTask(url, jsonObject.toString());
        String inStr = null;
        try {
            inStr = login.execute().get().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inStr;
    }


    private static final int REQ_PERMISSIONS = 0;

    // New Permission see Appendix A
    private void askPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(MainActivity.this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
        }

        if (!permissionsRequest.isEmpty()) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    permissionsRequest.toArray(new String[permissionsRequest.size()]),
                    REQ_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_PERMISSIONS:
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        String text = "onRequestPermissionsResult";
                        Common.showToast(MainActivity.this, text);
                        return;
                    }
                }
                break;
        }
    }

    private boolean doFBLogin(Context c){
        boolean isfinish = false;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "FBLogin");
        jsonObject.addProperty("android_user_id", userUUID);
        jsonObject.addProperty("ios_user_id", "");
        jsonObject.addProperty("app_account", fbEmail);
        jsonObject.addProperty("fb_account", fbUserId);
        jsonObject.addProperty("nick_name", fbUserName);
        String url = Common.URL + Common.WEBLOGIN;

        MyTask login = new MyTask(url, jsonObject.toString());
        String inStr = "";
        try {
            inStr = login.execute().get().trim();
            etLoginAccount.setText(fbEmail);
            etLoginPassword.setText(inStr);
            //Common.showToast(c, "userAccount->" + userAccount + " pwd->" + inStr + " userUUID->" + userUUID);

            Common.updateLoginPreferences(c, fbEmail, inStr, userUUID);
            if(!inStr.equals("") || inStr == null){
                isfinish = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isfinish;
    }

    private void loginFB() {
        // 設定FB login的顯示方式 ; 預設是：NATIVE_WITH_FALLBACK
        /**
         * 1. NATIVE_WITH_FALLBACK
         * 2. NATIVE_ONLY
         * 3. KATANA_ONLY
         * 4. WEB_ONLY
         * 5. WEB_VIEW_ONLY
         * 6. DEVICE_AUTH
         */
        // 設定要跟用戶取得的權限，以下3個是基本可以取得，不需要經過FB的審核
        loginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);

        List<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_friends");

        // 設定要讀取的權限
        loginManager.logInWithReadPermissions(this, permissions);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    if (response.getConnection().getResponseCode() == 200) {

                                        fbUserId = object.getString("id");
                                        fbUserName = object.getString("name");
                                        fbEmail = object.getString("email");

                                        if(doFBLogin(MainActivity.this)){
                                            String friendsJString = object.getString("friends");
                                            Gson gson = new Gson();
                                            JsonObject friendsJsonObject = gson.fromJson(object.getString("friends"), JsonObject.class);
                                            JsonArray friendsArray = friendsJsonObject.get("data").getAsJsonArray();
                                            friendIdList = new ArrayList<>();
                                            for (int i = 0; i < friendsArray.size(); i++) {
                                                JsonObject obj = friendsArray.get(i).getAsJsonObject();
                                                String user_friends_name = obj.get("name").getAsString();
                                                String user_friends_id = obj.get("id").getAsString();
                                                friendIdList.add(new FacebookMember(user_friends_name, user_friends_id));
                                            }

                                            JsonObject jsonObject = new JsonObject();
                                            jsonObject.addProperty("action", "updateFBList");
                                            jsonObject.addProperty("account", fbEmail);
                                            jsonObject.addProperty("password", etLoginPassword.getText().toString().trim());
                                            jsonObject.addProperty("FBid", fbUserId);
                                            jsonObject.addProperty("FBList", new Gson().toJson(friendIdList));
                                            String url = Common.URL + Common.FRIEND;

                                            MyTask login = new MyTask(url, jsonObject.toString());
                                            String inStr = null;
                                            try {
                                                inStr = login.execute().get().trim();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            Common.startTabActivity(MainActivity.this);
                                            askPermissions();
                                            finish();
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    Thread.sleep(1000);//括号里面的5000代表5000毫秒，也就是5秒，可以该成你需要的时间
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
                // 如果要取得email，需透過添加參數的方式來獲取(如下)
                // 不添加只能取得id & name
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,friends");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {
                // 用戶取消
                Log.d(TAG, "Facebook onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                // 登入失敗
                Log.d(TAG, "Facebook onError:" + error.toString());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }

}

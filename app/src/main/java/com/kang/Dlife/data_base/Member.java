package com.kang.Dlife.data_base;


public class Member {
    private String android_user_id;
    private String ios_user_id;
    private String app_account;
    private String app_pwd;
    private String fb_account;
    private String google_account;
    private String nick_name;
    private int sex;
    private String birthday;
    private String birth_year;
    private String login_date;
    private String post_date;


    public Member(String android_user_id, String app_account, String app_pwd) {
        super();
        this.android_user_id = android_user_id;
        this.app_account = app_account;
        this.app_pwd = app_pwd;
    }
    public String getAndroid_user_id() {
        return android_user_id;
    }
    public void setAndroid_user_id(String android_user_id) {
        this.android_user_id = android_user_id;
    }
    public String getIos_user_id() {
        return ios_user_id;
    }
    public void setIos_user_id(String ios_user_id) {
        this.ios_user_id = ios_user_id;
    }
    public String getApp_account() {
        return app_account;
    }
    public void setApp_account(String app_account) {
        this.app_account = app_account;
    }
    public String getApp_pwd() {
        return app_pwd;
    }
    public void setApp_pwd(String app_pwd) {
        this.app_pwd = app_pwd;
    }
    public String getFb_account() {
        return fb_account;
    }
    public void setFb_account(String fb_account) {
        this.fb_account = fb_account;
    }
    public String getGoogle_account() {
        return google_account;
    }
    public void setGoogle_account(String google_account) {
        this.google_account = google_account;
    }
    public String getNick_name() {
        return nick_name;
    }
    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }
    public int getSex() {
        return sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getBirth_year() {
        return birth_year;
    }
    public void setBirth_year(String birth_year) {
        this.birth_year = birth_year;
    }
    public String getLogin_date() {
        return login_date;
    }
    public void setLogin_date(String login_date) {
        this.login_date = login_date;
    }
    public String getPost_date() {
        return post_date;
    }
    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

}

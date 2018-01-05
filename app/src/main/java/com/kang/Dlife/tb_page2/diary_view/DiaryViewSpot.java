package com.kang.Dlife.tb_page2.diary_view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by weisunquan on 2017/12/4.
 */

public class DiaryViewSpot {
    private int id;

    private int member_sk;
    private int top_category_sk;
    private int member_location_sk;

    private String date;
    private String startTime;
    private String endTime;
    private String note;
    private double longitude;
    private double latitude;

    public DiaryViewSpot(int id, int member_sk, int top_category_sk, int member_location_sk, String date, String startTime, String endTime, String note, double longitude, double latitude) {
        this.id = id;
        this.member_sk = member_sk;
        this.top_category_sk = top_category_sk;
        this.member_location_sk = member_location_sk;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.note = note;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMember_sk() {
        return member_sk;
    }

    public void setMember_sk(int member_sk) {
        this.member_sk = member_sk;
    }

    public int getTop_category_sk() {
        return top_category_sk;
    }

    public void setTop_category_sk(int top_category_sk) {
        this.top_category_sk = top_category_sk;
    }

    public int getMember_location_sk() {
        return member_location_sk;
    }

    public void setMember_location_sk(int member_location_sk) {
        this.member_location_sk = member_location_sk;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getLongitude() {
        return longitude;
    }


    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }


    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getFormatedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return dateFormat.format(new Date(date));
    }
    public String getFormatedStartTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss", Locale.US);
        return dateFormat.format(new Date(startTime));
    }

    public String getFormatedEndTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss", Locale.US);
        return dateFormat.format(new Date(endTime));
    }
}
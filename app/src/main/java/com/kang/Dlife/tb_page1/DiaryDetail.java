package com.kang.Dlife.tb_page1;


import com.kang.Dlife.Common;

import java.io.Serializable;

public class DiaryDetail implements Serializable {

    private int sk;
    private int member_sk;
    private int top_category_sk;
    private int member_location_sk;
    private String note;
    private String start_stamp;
    private String end_stamp;
    private String start_date;
    private String end_date;
    private String post_day;
    private String post_date;

    public int imageId;
    public int icWeatherId;
    public int icNewId;
    public String place = "中央大學";
    public double longitude;
    public double latitude;
    public double altitude;
    public int startLocationSK;
    public int endLocationSK;

    public DiaryDetail() {
        super();
    }

    public DiaryDetail(DiaryDetail d){
        super();
        this.sk = d.getSk();
        this.member_sk = d.getMember_sk();
        this.top_category_sk = d.getTop_category_sk();
        this.member_location_sk = d.getMember_location_sk();
        this.note = d.getNote();
        this.start_stamp = d.getStart_stamp();
        this.end_stamp = d.getEnd_stamp();
        this.start_date = d.getStart_date();
        this.end_date = d.getEnd_date();
        this.post_day = d.getPost_day();
        this.post_date = d.getPost_date();
        longitude = d.longitude;
        latitude = d.latitude;
        altitude = d.altitude;
        startLocationSK = d.startLocationSK;
        endLocationSK = d.endLocationSK;
    }

    public DiaryDetail(int member_sk, String note,
                       String start_stamp, String end_stamp) {
        super();
        this.member_sk = member_sk;
        this.note = note;
        this.start_stamp = start_stamp;
        this.end_stamp = end_stamp;
    }

    public DiaryDetail(int member_sk, int top_category_sk, int member_location_sk, String note,
                       String start_stamp, String end_stamp, String post_day, String post_date) {
        super();
        this.member_sk = member_sk;
        this.top_category_sk = top_category_sk;
        this.member_location_sk = member_location_sk;
        this.note = note;
        this.start_stamp = start_stamp;
        this.end_stamp = end_stamp;
        this.post_day = post_day;
        this.post_date = post_date;
    }

    public DiaryDetail(int sk, int member_sk, int top_category_sk, int member_location_sk, String note,
                       String start_stamp, String end_stamp, String start_date, String end_date, String post_day,
                       String post_date) {
        super();
        this.sk = sk;
        this.member_sk = member_sk;
        this.top_category_sk = top_category_sk;
        this.member_location_sk = member_location_sk;
        this.note = note;
        this.start_stamp = start_stamp;
        this.end_stamp = end_stamp;
        this.start_date = start_date;
        this.end_date = end_date;
        this.post_day = post_day;
        this.post_date = post_date;
    }


    public int getSk() {
        return sk;
    }
    public void setSk(int sk) {
        this.sk = sk;
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
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public String getStart_stamp() {
        return start_stamp;
    }
    public void setStart_stamp(String start_stamp) {
        this.start_stamp = start_stamp;
    }
    public String getEnd_stamp() {
        return end_stamp;
    }
    public void setEnd_stamp(String end_stamp) {
        this.end_stamp = end_stamp;
    }
    public String getPost_day() {
        return post_day;
    }
    public void setPost_day(String post_day) {
        this.post_day = post_day;
    }
    public String getPost_date() {
        return post_date;
    }
    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String describe() {
        String msg = "";
        msg += "--------------------" + "\n";
        msg += "sk : " + getSk() + "\n";
        msg += "longitude : " + longitude + "\n";
        msg += "latitude : " + latitude + "\n";
        msg += "altitude : " + altitude + "\n";
        msg += "start_stamp : " + getStart_stamp() + "\n";
        msg += "end_stamp : " + getEnd_stamp() + "\n";
        msg += "start_date : " + getStart_date() + "\n";
        msg += "end_date : " + getEnd_date() + "\n";
        msg += "post_day : " + getPost_day() + "\n";
        msg += "post_date : " + getPost_date() + "\n";
        msg += "---to spot time--" + "\n";
        msg += Common.dateStringToDay(getStart_date()) + "\n";
        msg += Common.dateStringToHM(getStart_date()) + "\n";
        msg += "--------------------" + "\n";
        return msg;
    }
}

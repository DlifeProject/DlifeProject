package com.kang.Dlife.data_base;


import java.io.Serializable;

public class DiaryDetailWeb implements Serializable {

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
    private double longitude;
    private double latitude;
    private double altitude;

    public void describe() {
        System.out.println("--------------------");
        System.out.println("member_sk : " + member_sk);
        System.out.println("longitude : " + longitude);
        System.out.println("latitude : " + latitude);
        System.out.println("altitude : " + altitude);
        System.out.println("start_stamp : " + start_stamp);
        System.out.println("end_stamp : " + end_stamp);
        System.out.println("start_date : " + start_date);
        System.out.println("end_date : " + end_date);
        System.out.println("post_day : " + post_day);
        System.out.println("post_date : " + post_date);
        System.out.println("--------------------");
    }
    public DiaryDetailWeb() {
        super();
    }

    public DiaryDetailWeb(int member_sk, String note,
                       String start_stamp, String end_stamp) {
        super();
        this.member_sk = member_sk;
        this.note = note;
        this.start_stamp = start_stamp;
        this.end_stamp = end_stamp;
    }

    public DiaryDetailWeb(int member_sk, int top_category_sk, int member_location_sk, String note,
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

    public DiaryDetailWeb(int sk, int member_sk, int top_category_sk, int member_location_sk, String note,
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

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }
}

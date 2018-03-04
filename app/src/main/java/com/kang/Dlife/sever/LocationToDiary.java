package com.kang.Dlife.sever;



import com.kang.Dlife.data_base.DiaryDetail;

import java.io.Serializable;

public class LocationToDiary implements Serializable {

    private int sk;
    private int member_sk;
    private int top_category_sk;
    private int member_location_sk;
    private String start_stamp;
    private String end_stamp;
    private String start_date;
    private String end_date;
    private String post_day;
    private String post_date;

    private double longitude;
    private double latitude;
    private double altitude;
    private String startStamp;
    private String startDate;
    private String endStamp;
    private String endDate;
    private String postStamp;
    private String postDay;
    private String postDate;
    private int startLocationSK;
    private int endLocationSK;

    private int imageId;
    private int icWeatherId;
    private int icNewId;
    private String place = "中央大學";
    private String note;

    public LocationToDiary(){
        super();
    }

    public LocationToDiary(LocationToDiary d) {
        super();
        this.sk = d.getSk();
        this.member_sk = d.getMember_sk();
        this.top_category_sk = d.getTop_category_sk();
        this.member_location_sk = d.getMember_location_sk();
        this.start_stamp = d.getStart_stamp();
        this.end_stamp = d.getEnd_stamp();
        this.start_date = d.getStart_date();
        this.end_date = d.getEnd_date();
        this.post_day = d.getPost_day();
        this.post_date = d.getPost_date();

        this.longitude = d.getLongitude();
        this.latitude = d.getLatitude();
        this.altitude = d.getAltitude();
        this.startStamp = d.getStartStamp();
        this.startDate = d.getStartDate();
        this.endStamp = d.getEndStamp();
        this.endDate = d.getEndDate();
        this.postStamp = d.getPostStamp();
        this.postDay = d.getPostDay();
        this.postDate = d.getPostDate();
        this.startLocationSK = d.getStartLocationSK();
        this.endLocationSK = d.getEndLocationSK();
        this.imageId = d.getImageId();
        this.icWeatherId = d.getIcWeatherId();
        this.icNewId = d.getIcNewId();
        this.place = d.getPlace();
        this.note = d.getNote();
    }

    public int getMember_sk() {
        return member_sk;
    }

    public void setMember_sk(int member_sk) {
        this.member_sk = member_sk;
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

    public String getStartStamp() {
        return startStamp;
    }

    public void setStartStamp(String startStamp) {
        this.startStamp = startStamp;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndStamp() {
        return endStamp;
    }

    public void setEndStamp(String endStamp) {
        this.endStamp = endStamp;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPostStamp() {
        return postStamp;
    }

    public void setPostStamp(String postStamp) {
        this.postStamp = postStamp;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public int getStartLocationSK() {
        return startLocationSK;
    }

    public void setStartLocationSK(int startLocationSK) {
        this.startLocationSK = startLocationSK;
    }

    public int getEndLocationSK() {
        return endLocationSK;
    }

    public void setEndLocationSK(int endLocationSK) {
        this.endLocationSK = endLocationSK;
    }

    public String getPostDay() {
        return postDay;
    }

    public void setPostDay(String postDay) {
        this.postDay = postDay;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getIcWeatherId() {
        return icWeatherId;
    }

    public void setIcWeatherId(int icWeatherId) {
        this.icWeatherId = icWeatherId;
    }

    public int getIcNewId() {
        return icNewId;
    }

    public void setIcNewId(int icNewId) {
        this.icNewId = icNewId;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public DiaryDetail toDiaryDetail(){
        DiaryDetail diaryDetail = new DiaryDetail();

        diaryDetail.setSk(sk);
        diaryDetail.setMember_sk(member_sk);
        diaryDetail.setTop_category_sk(0);
        diaryDetail.setMember_location_sk(0);
        diaryDetail.setNote(note);
        diaryDetail.setStart_stamp(startStamp);
        diaryDetail.setEnd_stamp(endStamp);
        diaryDetail.setStart_date(startDate);
        diaryDetail.setEnd_date(endDate);
        diaryDetail.setLatitude(latitude);
        diaryDetail.setLongitude(longitude);
        diaryDetail.setAltitude(altitude);

        return diaryDetail;

    }

    public int getSk() {
        return sk;
    }

    public void setSk(int sk) {
        this.sk = sk;
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
}

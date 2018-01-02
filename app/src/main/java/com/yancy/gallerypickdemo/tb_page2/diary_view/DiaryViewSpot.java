package com.yancy.gallerypickdemo.tb_page2.diary_view;

/**
 * Created by weisunquan on 2017/12/4.
 */

public class DiaryViewSpot {

//    private  int image;
    private  String date;
    private String location;
    private  String time;
    private String note;

    public DiaryViewSpot(String date, String location, String time, String note) {
//        this.image = image;
        this.date = date;
        this.location = location;
        this.time = time;
        this.note = note;
    }
//
//    public int getImage() {
//        return image;
//    }
//
//    public void setImage(int image) {
//        this.image = image;
//    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

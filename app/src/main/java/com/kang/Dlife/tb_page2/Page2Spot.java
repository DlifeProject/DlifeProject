package com.kang.Dlife.tb_page2;

import java.io.Serializable;

/**
 * Created by allen on 2017/12/4.
 */

public class Page2Spot implements Serializable{


    public Page2Spot(int image, int year, int month, int day, int three_day, int seven_day, String conent, String bt_name) {
        this.image = image;
        this.year = year;
        this.month = month;
        this.day = day;
        this.three_day = three_day;
        this.seven_day = seven_day;
        this.conent = conent;
        this.bt_name = bt_name;
    }

    private int image, year, month, day, three_day , seven_day;
    private String conent;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getThree_day() {
        return three_day;
    }

    public void setThree_day(int three_day) {
        this.three_day = three_day;
    }

    public int getSeven_day() {
        return seven_day;
    }

    public void setSeven_day(int seven_day) {
        this.seven_day = seven_day;
    }

    public String getConent() {
        return conent;
    }

    public void setConent(String conent) {
        this.conent = conent;
    }

    public String getBt_name() {
        return bt_name;
    }

    public void setBt_name(String bt_name) {
        this.bt_name = bt_name;
    }

    private String bt_name;
}

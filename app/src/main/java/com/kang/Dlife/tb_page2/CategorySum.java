package com.kang.Dlife.tb_page2;


import java.io.Serializable;

public class CategorySum implements Serializable {

    private int diaryPhotoSK;
    private String categoryType;
    private String year;
    private String month;
    private String day;

    private int three_day;
    private int seven_day;
    private String note;

    public CategorySum() {
        super();
    }

    public CategorySum(int diaryPhotoSK, String categoryType, String year, String month, String day, int three_day,
                       int seven_day, String conent) {
        super();
        this.diaryPhotoSK = diaryPhotoSK;
        this.categoryType = categoryType;
        this.year = year;
        this.month = month;
        this.day = day;
        this.three_day = three_day;
        this.seven_day = seven_day;
        this.note = conent;
    }

    public CategorySum(CategorySum s) {
        super();
        this.diaryPhotoSK = s.getDiaryPhotoSK();
        this.categoryType = s.getCategoryType();
        this.year = s.getYear();
        this.month = s.getMonth();
        this.day = s.getDay();
        this.three_day = s.getThree_day();
        this.seven_day = s.getSeven_day();
        this.note = s.getNote();
    }

    public int getDiaryPhotoSK() {
        return diaryPhotoSK;
    }

    public void setDiaryPhotoSK(int diaryPhotoSK) {
        this.diaryPhotoSK = diaryPhotoSK;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
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

    public String getNote() {
        return note;
    }

    public void setNote(String conent) {
        this.note = conent;
    }

}
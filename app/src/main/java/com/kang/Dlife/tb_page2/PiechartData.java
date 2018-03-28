package com.kang.Dlife.tb_page2;

import java.io.Serializable;

public class PiechartData implements Serializable {

    private String category;
    private double categoryTime;
    private int diaryCount;

    public PiechartData() {
        super();
    }

    public PiechartData(String category, double categoryTime, int diaryCount) {
        super();
        this.category = category;
        this.categoryTime = categoryTime;
        this.diaryCount = diaryCount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getCategoryTime() {
        return categoryTime;
    }

    public void setCategoryTime(double categoryTime) {
        this.categoryTime = categoryTime;
    }

    public int getDiaryCount() {
        return diaryCount;
    }
    public void setDiaryCount(int diaryCount) {
        this.diaryCount = diaryCount;
    }
}
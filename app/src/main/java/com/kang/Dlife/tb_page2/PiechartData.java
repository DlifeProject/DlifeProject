package com.kang.Dlife.tb_page2;

/**
 * Created by allen on 2018/1/10.
 */

class PiechartData {
    String category;
    long categoryTime;

    public PiechartData(String category, long categoryTime) {
        this.category = category;
        this.categoryTime = categoryTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getCategoryTime() {
        return categoryTime;
    }

    public void setCategoryTime(long categoryTime) {
        this.categoryTime = categoryTime;
    }
}

package com.kang.Dlife.tb_page1.diary_edit;

public class DiaryEditSpot {

    private String diary;
    private String category;
    private double longitude;
    private double latitude;

    public DiaryEditSpot(String diary, String category, double longitude, double latitude) {
        this.diary = diary;
        this.category = category;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getDiary() {
        return diary;
    }

    public String getCategory() {
        return category;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setDiary(String diary) {
        this.diary = diary;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

}

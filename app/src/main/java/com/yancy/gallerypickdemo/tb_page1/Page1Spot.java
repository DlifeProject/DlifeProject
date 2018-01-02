package com.yancy.gallerypickdemo.tb_page1;

public class Page1Spot {
    private int id;
    private int imageId;
    private int icWeatherId;
    private int icNewId;
    private String date;
    private String timeStart;
    private String timeEnd;
    private String place;
    private String diary;

    public Page1Spot(int imageId, int icWeatherId, int icNewId, String date,
                     String timeStart, String timeEnd, String place,
                     String diary) {
        this.imageId = imageId;
        this.icWeatherId = icWeatherId;
        this.icNewId = icNewId;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.place = place;
        this.diary = diary;
    }

    public int getId() {
        return id;
    }

    public int getImageId() {
        return imageId;
    }

    public int getIcWeatherId() {
        return icWeatherId;
    }

    public int getIcNewId() {
        return icNewId;
    }

    public String getDate() {
        return date;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public String getPlace() {
        return place;
    }

    public String getDiary() {
        return diary;
    }

}

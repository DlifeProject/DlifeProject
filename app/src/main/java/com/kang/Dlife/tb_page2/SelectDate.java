package com.kang.Dlife.tb_page2;

import java.sql.Timestamp;

/**
 * Created by allen on 2018/1/10.
 */

class SelectDate {
    Timestamp startDate, endDate;

    public SelectDate(Timestamp startDate, Timestamp endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }
}

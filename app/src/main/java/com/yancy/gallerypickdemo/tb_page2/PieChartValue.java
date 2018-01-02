package com.yancy.gallerypickdemo.tb_page2;

import android.icu.text.DecimalFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by allen on 2017/12/20.
 */

public class PieChartValue implements IValueFormatter {

    private DecimalFormat mFormat;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public PieChartValue() {
        mFormat = new DecimalFormat("###,###,##0"); // use one decimal
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // write your logic here
        if(value == 0) {
            return "";
        }
        return mFormat.format(value) + " %"; // e.g. append a dollar-sign

    }
}

package com.kang.Dlife.tb_page2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kang.Dlife.Common;
import com.kang.Dlife.R;
import com.kang.Dlife.sever.MyTask;
import com.kang.Dlife.tb_page2.diary_view.DiaryView;
import com.kang.Dlife.tb_page3.Page3;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.CONNECTIVITY_SERVICE;


public class PieChartFragment extends Fragment {
        private PieChartItem item;
        private DatePickerDialog.OnDateSetListener sinceDateSetlistener, endDateSetlistener;
        private RelativeLayout ry_Since, ry_End, ry_click;
        public int syear, smonth, sday, eyear, emonth, eday, totalHour;
        TextView tv_click, tv_startyear, tv_startmonth, tv_startday, tv_endyear, tv_endmonth, tv_endday;
        public long shopping = 10, work = 10, hobby = 10, travel = 10, learning = 10;
        Timestamp startDate, endDate;
        private final static String TAG = "CategoryPieFragment";
        private PieChart pieChart;



        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                item = (PieChartItem) getArguments().getSerializable("specialitem");
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.page2_pie_chart, container, false);

            pieChart = view.findViewById(R.id.pieChart);
            ry_Since = view.findViewById(R.id.ry_Since);
            ry_End = view.findViewById(R.id.ry_End);
            tv_click = view.findViewById(R.id.tv_click);
            tv_click.setText(String.valueOf(item.getName()));

            ry_click = view.findViewById(R.id.ry_circleclick);
            ry_click.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        //更改按下去時的圖片
                        tv_click.setTextColor(Color.parseColor("#55d7bb"));
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        //抬起來時的圖片
                        tv_click.setTextColor(Color.parseColor("#f5f5f6"));
                    }
                    return false;
                }
            });
            ry_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), DiaryView.class));
                }
            });
            Calendar calendar = Calendar.getInstance();
            int default_syear = calendar.get(Calendar.YEAR);
            int default_eyear = default_syear;
            int default_smonth = calendar.get(Calendar.MONTH) + 1;
            int default_emonth = default_smonth;
            int default_sday = calendar.get(Calendar.DAY_OF_MONTH) - 2;
            int default_eday = default_sday + 2;


            tv_startyear = view.findViewById(R.id.tv_year);
            tv_startyear.setText(String.valueOf(default_syear));
            tv_startmonth = view.findViewById(R.id.tv_month);
            tv_startmonth.setText(String.valueOf(default_smonth));
            tv_startday = view.findViewById(R.id.tv_day);
            tv_startday.setText(String.valueOf(default_sday));
            tv_endyear = view.findViewById(R.id.tv_year_end);
            tv_endyear.setText(String.valueOf(default_eyear));
            tv_endmonth = view.findViewById(R.id.tv_month_end);
            tv_endmonth.setText(String.valueOf(default_emonth));
            tv_endday = view.findViewById(R.id.tv_day_end);
            tv_endday.setText(String.valueOf(default_eday));
            Button bt_check = view.findViewById(R.id.bt_check);

            ry_Since.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH) - 2;

                    DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                            android.R.style.Theme_Holo_Dialog_MinWidth, sinceDateSetlistener, year, month, day);
                    DatePicker datePicker = dialog.getDatePicker();
                    if (eday != 0) {
                        Calendar endcal = Calendar.getInstance();
                        endcal.set(eyear, emonth, eday);
                        datePicker.setMaxDate(endcal.getTimeInMillis());
                        endcal.add(Calendar.DAY_OF_MONTH, -6);
                        datePicker.setMinDate(endcal.getTimeInMillis());
                    } else {
                        datePicker.setMaxDate(new Date().getTime());
                    }
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });

            sinceDateSetlistener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker sinceDatePicker, int startyear, int startmonth, int startday) {

                    tv_startyear.setText(String.valueOf(startyear));
                    tv_startmonth.setText(String.valueOf(startmonth + 1));
                    tv_startday.setText(String.valueOf(startday));
                    syear = startyear;
                    smonth = startmonth;
                    sday = startday;

                }
            };
            ry_End.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {
                    int year, month, day;

                    Calendar cal = Calendar.getInstance();
                    Calendar startcal = Calendar.getInstance();
                    startcal.set(syear, smonth + 1, sday);
                    if (startcal.after(cal)) {
                        year = syear;
                        month = smonth + 1;
                        day = sday;

                    } else {
                        year = cal.get(Calendar.YEAR);
                        month = cal.get(Calendar.MONTH);
                        day = cal.get(Calendar.DAY_OF_MONTH);
                    }

                    DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                            android.R.style.Theme_Holo_Dialog_MinWidth, endDateSetlistener, year, month, day);
                    if (sday == 0) {
                        DatePicker datePicker = dialog.getDatePicker();
                        datePicker.setMaxDate(new Date().getTime());
                    } else {
                        DatePicker datePicker = dialog.getDatePicker();
                        startcal = Calendar.getInstance();
                        startcal.set(syear, smonth, sday);
                        datePicker.setMinDate(startcal.getTimeInMillis());
                        long days = Calendar.getInstance().getTimeInMillis() - startcal.getTimeInMillis();
                        int i = 24 * 60 * 60 * 1000;
                        if (days <= i * 6) {
                            startcal.add(Calendar.DAY_OF_MONTH, (int) (days / i));
                            datePicker.setMaxDate(startcal.getTimeInMillis());
                        } else {
                            startcal.add(Calendar.DAY_OF_MONTH, 6);
                            datePicker.setMaxDate(startcal.getTimeInMillis());
                        }
                    }
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });

            endDateSetlistener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    tv_endyear.setText(String.valueOf(year));
                    tv_endmonth.setText(String.valueOf(month + 1));
                    tv_endday.setText(String.valueOf(day));
                    eyear = year;
                    emonth = month;
                    eday = day;
                }
            };
            bt_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(syear - 1, smonth + 12, sday);
                    startDate = new Timestamp(calendar1.getTimeInMillis());
                    calendar1.set(eyear - 1, emonth + 12, eday);
                    endDate = new Timestamp(calendar1.getTimeInMillis());
                    SelectDate selectDate = new SelectDate(startDate, endDate);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "select");
                    jsonObject.addProperty("account", Common.getAccount(getContext()));
                    jsonObject.addProperty("password", Common.getPWD(getContext()));
                    jsonObject.addProperty("SelectDate", new Gson().toJson(selectDate));
                    if (networkConnected()) {
                        String url = Common.URL + "PiechartServlet";
                        MyTask myTask = new MyTask(url, jsonObject.toString());
                        //jsonObject需要轉成字串
                        try {
                            String inStr = myTask.execute().get();
                            System.out.println("inStr:" + inStr.toString());
                            Log.d(TAG, "inStr:" + inStr.toString());
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<PiechartDeta>>() {
                            }.getType();
                            List<PiechartDeta> piechartDetaList = gson.fromJson(inStr, listType);
                            for (PiechartDeta piechartDeta : piechartDetaList) {
                                String cat = piechartDeta.getCategory();
                                switch (cat) {
                                    case "shopping":
                                        shopping = 0;
                                        shopping += piechartDeta.categoryTime;
                                        break;
                                    case "work":
                                        work = 0;
                                        work += piechartDeta.categoryTime;
                                        break;
                                    case "hobby":
                                        hobby = 0;
                                        hobby += piechartDeta.categoryTime;
                                        break;
                                    case "travel":
                                        travel = 0;
                                        travel += piechartDeta.categoryTime;
                                        break;
                                    case "learning":
                                        learning = 0;
                                        learning += piechartDeta.categoryTime;
                                        break;
                                    default:
                                        break;
                                }
                            }
                            drawPiechart();
                            long  totaltime = shopping + work + hobby + travel +learning;
                            totalHour = (int)totaltime / 1000 / 60 / 60;
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }

                    }
                }
            });

        /* 設定可否旋轉 */
            pieChart.setRotationEnabled(true);

            int piechart_month = calendar.get(Calendar.MONTH) + 1;

        /* 設定圓心文字 */
            pieChart.setCenterText(String.valueOf(

                    setMonth(piechart_month)));
        /* 設定圓心文字大小 */
            pieChart.setCenterTextSize(35);
            pieChart.animateXY(700, 700);
            Description description = new Description();
            description.setText("Total : " + totalHour + "hrs");
            description.setTextSize(25);
            pieChart.setDescription(description);

            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry entry, Highlight highlight) {
                    Log.d(TAG, "entry: " + entry.toString() + "; highlight: " + highlight.toString());
                    PieEntry pieEntry = (PieEntry) entry;
                    String text = pieEntry.getLabel() + "\n" + pieEntry.getValue();
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected() {

                }
            });

            drawPiechart();




            return view;

        }

        private String setMonth(int i) {
            switch (i) {
                case 1:
                    return "JAN";
                case 2:
                    return "FEB";
                case 3:
                    return "MAR";
                case 4:
                    return "APR";
                case 5:
                    return "MAY";
                case 6:
                    return "JAN";
                case 7:
                    return "JUL";
                case 8:
                    return "AUG";
                case 9:
                    return "SEP";
                case 10:
                    return "OCT";
                case 11:
                    return "NOV";
                case 12:
                    return "DEC";
                default:
                    break;
            }
            return "";
        }

        private List<PieEntry> getSalesEntries() {
            List<PieEntry> salesEntries = new ArrayList<>();

            salesEntries.add(new PieEntry(calculate(work), "Work"));
            salesEntries.add(new PieEntry(calculate(shopping), "Shopping"));
            salesEntries.add(new PieEntry(calculate(hobby), "Hobby"));
            salesEntries.add(new PieEntry(calculate(travel), "Learning"));
            salesEntries.add(new PieEntry(calculate(learning), "Travel"));
            return salesEntries;


        }

        private long calculate(long l) {
            return l * 100 / (shopping + work + hobby + travel + learning);

        }

        private boolean networkConnected() {
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo.isConnected();
            } else {
                return false;
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private void drawPiechart(){
            List<PieEntry> pieEntries = getSalesEntries();

            PieDataSet pieDataSet = new PieDataSet(pieEntries, "Car Sales");
            pieDataSet.setValueFormatter(new PieChartValue());
            pieDataSet.setValueTextColor(Color.BLUE);
            pieDataSet.setValueTextSize(20);
            pieDataSet.setSliceSpace(2);

        /* 使用官訂顏色範本，顏色不能超過5種，否則官定範本要加顏色 */
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            PieData pieData = new PieData(pieDataSet);
            Legend legend = pieChart.getLegend();
            legend.setEnabled(false);
            pieChart.setData(pieData);
            pieChart.invalidate();

        }
    }




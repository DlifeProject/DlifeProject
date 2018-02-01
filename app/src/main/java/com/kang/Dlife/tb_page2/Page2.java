package com.kang.Dlife.tb_page2;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.content.Context.CONNECTIVITY_SERVICE;


public class Page2 extends Fragment implements View.OnClickListener {
    private final String TAG = "page2";
    private List<Object> itemList;
    private long shopping = 10, work = 10, hobby = 10, travel = 10, learning = 10;
    DatePickerDialog.OnDateSetListener sinceDateSetlistener, endDateSetlistener;
    private int syear, smonth, sday, eyear, emonth, eday, totalHour;
    Timestamp startDate, endDate;
    CategorySum item;
    RecyclerView rvItem;
    int rvposition = 0;
    ImageButton previousImageButton, nextImageButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page2, container, false);
        rvItem = view.findViewById(R.id.rvItem);
        rvItem.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        rvItem.setOnFlingListener(null);
        // 放這就不會滑到底
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(rvItem);
        List<Object> itemList = getItemList();
        rvItem.setAdapter(new Page2Adapter(getContext(), itemList));
        RelativeLayout ry_Previous = view.findViewById(R.id.ry_Previous);
        ImageButton previousImageButton = view.findViewById(R.id.iv_previous);
        ry_Previous.setOnClickListener(this);
        RelativeLayout ry_Next = view.findViewById(R.id.ry_Next);
        ImageButton nextImageButton = view.findViewById(R.id.iv_next);
        ry_Next.setOnClickListener(this);

//        if (rvposition == 0){
//            previousImageButton.setVisibility(View.INVISIBLE);
//
//        } else {
//            previousImageButton.setVisibility(View.VISIBLE);
//        }
//        if (rvposition == itemList.size()-1) {
//
//            nextImageButton.setVisibility(View.INVISIBLE);
//
//        } else {
//            nextImageButton.setVisibility(View.VISIBLE);
//        }
        return view;

    }


    private class Page2Adapter extends RecyclerView.Adapter {

        private Context context;
        private List<Object> itemList;

        public Page2Adapter(Context context, List<Object> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            if (viewType == 0) {
                View itemView0 = layoutInflater.inflate(R.layout.page2_pie_chart, viewGroup, false);
                return new PieChartViewHolder(itemView0);
            } else {
                View itemView1 = layoutInflater.inflate(R.layout.page2_viewpager, viewGroup, false);
                return new CategoryViewHolder(itemView1);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Object obj = itemList.get(position);
            ArrayList<PieEntry> piechartDataList = null;
            CategorySum categorySum = null;
            if (obj instanceof ArrayList) {
                piechartDataList = (ArrayList<PieEntry>) obj;
            } else if (obj instanceof CategorySum) {
                categorySum = (CategorySum) obj;
            }

            switch (getItemViewType(position)) {
                case 0:
                    final PieChartViewHolder pieChartViewHolder = (PieChartViewHolder) holder;
                    pieChartViewHolder.tv_click.setText("Daily");
                    pieChartViewHolder.ry_click.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                //更改按下去時的圖片
                                pieChartViewHolder.tv_click.setTextColor(Color.parseColor("#55d7bb"));
                            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                //抬起來時的圖片
                                pieChartViewHolder.tv_click.setTextColor(Color.parseColor("#f5f5f6"));
                            }
                            return false;
                        }
                    });

                    pieChartViewHolder.ry_click.setOnClickListener(new View.OnClickListener() {
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

                    pieChartViewHolder.tv_startyear.setText(String.valueOf(default_syear));
                    pieChartViewHolder.tv_startmonth.setText(String.valueOf(default_smonth));
                    pieChartViewHolder.tv_startday.setText(String.valueOf(default_sday));
                    pieChartViewHolder.tv_endyear.setText(String.valueOf(default_eyear));
                    pieChartViewHolder.tv_endmonth.setText(String.valueOf(default_emonth));
                    pieChartViewHolder.tv_endday.setText(String.valueOf(default_eday));
                    pieChartViewHolder.ry_Since.setOnClickListener(new View.OnClickListener() {
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

                            pieChartViewHolder.tv_startyear.setText(String.valueOf(startyear));
                            pieChartViewHolder.tv_startmonth.setText(String.valueOf(startmonth + 1));
                            pieChartViewHolder.tv_startday.setText(String.valueOf(startday));
                            syear = startyear;
                            smonth = startmonth;
                            sday = startday;

                        }
                    };
                    pieChartViewHolder.ry_End.setOnClickListener(new View.OnClickListener() {
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
                            pieChartViewHolder.tv_endyear.setText(String.valueOf(year));
                            pieChartViewHolder.tv_endmonth.setText(String.valueOf(month + 1));
                            pieChartViewHolder.tv_endday.setText(String.valueOf(day));
                            eyear = year;
                            emonth = month;
                            eday = day;
                        }
                    };
                    pieChartViewHolder.bt_check.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
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
                                    Type listType = new TypeToken<List<PiechartData>>() {
                                    }.getType();
                                    List<PiechartData> piechartDataList = gson.fromJson(inStr, listType);
                                    for (PiechartData piechartData : piechartDataList) {
                                        String cat = piechartData.getCategory();
                                        switch (cat) {
                                            case "shopping":
                                                shopping = 0;
                                                shopping += piechartData.categoryTime;
                                                break;
                                            case "work":
                                                work = 0;
                                                work += piechartData.categoryTime;
                                                break;
                                            case "hobby":
                                                hobby = 0;
                                                hobby += piechartData.categoryTime;
                                                break;
                                            case "travel":
                                                travel = 0;
                                                travel += piechartData.categoryTime;
                                                break;
                                            case "learning":
                                                learning = 0;
                                                learning += piechartData.categoryTime;
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                    long totaltime = shopping + work + hobby + travel + learning;
                                    totalHour = (int) totaltime / 1000 / 60 / 60;
                                    notifyDataSetChanged();
//
                                } catch (Exception e) {
                                    Log.e(TAG, e.toString());
                                }

                            }
                        }
                    });

        /* 設定可否旋轉 */
                    pieChartViewHolder.pieChart.setRotationEnabled(true);

                    int piechart_month = calendar.get(Calendar.MONTH) + 1;

        /* 設定圓心文字 */
                    pieChartViewHolder.pieChart.setCenterText(String.valueOf(setMonth(piechart_month)));
        /* 設定圓心文字大小 */
                    pieChartViewHolder.pieChart.setCenterTextSize(35);
                    pieChartViewHolder.pieChart.animateXY(700, 700);
                    Description description = new Description();
                    description.setText("Total : " + totalHour + "hrs");
                    description.setTextSize(25);
                    pieChartViewHolder.pieChart.setDescription(description);

                    pieChartViewHolder.pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
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

                    List<PieEntry> pieEntries = piechartDataList;

                    PieDataSet pieDataSet = new PieDataSet(pieEntries, "Car Sales");
                    pieDataSet.setValueFormatter(new PieChartValue());
                    pieDataSet.setValueTextColor(Color.BLUE);
                    pieDataSet.setValueTextSize(20);
                    pieDataSet.setSliceSpace(2);

        /* 使用官訂顏色範本，顏色不能超過5種，否則官定範本要加顏色 */
                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    PieData pieData = new PieData(pieDataSet);
                    Legend legend = pieChartViewHolder.pieChart.getLegend();
                    legend.setEnabled(false);
                    pieChartViewHolder.pieChart.setData(pieData);
                    pieChartViewHolder.pieChart.invalidate();


                    break;
                default:
                    final CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
                    item = categorySum;
                    categoryViewHolder.iv_pic.setImageResource(item.getDiaryPhotoSK());
                    categoryViewHolder.tv_click.setText(item.getCategoryType());

                    categoryViewHolder.ry_click.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                //更改按下去時的圖片
                                categoryViewHolder.tv_click.setTextColor(Color.parseColor("#4f4f4f"));
                            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                //抬起來時的圖片
                                categoryViewHolder.tv_click.setTextColor(Color.WHITE);
                            }
                            return false;
                        }
                    });
                    categoryViewHolder.ry_click.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent();
                            intent.setClass(getActivity(), DiaryView.class);
                            Bundle bundle = new Bundle();

                            bundle.putSerializable("CategorySum", item);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });

                    categoryViewHolder.tv_theme.setText(item.getNote());

                    categoryViewHolder.tv_year.setText(String.valueOf(item.getYear()));

                    categoryViewHolder.tv_month.setText(String.valueOf(item.getMonth()));

                    categoryViewHolder.tv_day.setText(String.valueOf(item.getDay()));

                    categoryViewHolder.tv_threedays.setText(String.valueOf(item.getThree_day()));

                    categoryViewHolder.tv_sevendays.setText(String.valueOf(item.getSeven_day()));

                    break;
            }


        }


        @Override
        public int getItemViewType(int position) {
            rvposition = position;
            if (position == 0) {
                return 0;
            } else {
                return 1;
            }

        }


        @Override
        public int getItemCount() {
            return itemList.size();
        }

        class PieChartViewHolder extends RecyclerView.ViewHolder {


            RelativeLayout ry_Since, ry_End, ry_click;

            TextView tv_click, tv_startyear, tv_startmonth, tv_startday, tv_endyear, tv_endmonth, tv_endday;
            PieChart pieChart;

            Button bt_check;

            public PieChartViewHolder(View itemView) {
                super(itemView);

                pieChart = itemView.findViewById(R.id.pieChart);
                ry_Since = itemView.findViewById(R.id.ry_Since);
                ry_End = itemView.findViewById(R.id.ry_End);
                tv_click = itemView.findViewById(R.id.tv_click);
                ry_click = itemView.findViewById(R.id.ry_circleclick);
                tv_startyear = itemView.findViewById(R.id.tv_year);
                tv_startmonth = itemView.findViewById(R.id.tv_month);
                tv_startday = itemView.findViewById(R.id.tv_day);
                tv_endyear = itemView.findViewById(R.id.tv_year_end);
                tv_endmonth = itemView.findViewById(R.id.tv_month_end);
                tv_endday = itemView.findViewById(R.id.tv_day_end);
                bt_check = itemView.findViewById(R.id.bt_check);

            }
        }

        class CategoryViewHolder extends RecyclerView.ViewHolder {


            RelativeLayout ry_click;
            ImageView iv_pic;
            TextView tv_click, tv_theme, tv_year, tv_month, tv_day, tv_threedays, tv_sevendays;

            public CategoryViewHolder(View itemView) {
                super(itemView);
                ry_click = itemView.findViewById(R.id.ry_click);
                tv_click = itemView.findViewById(R.id.tv_click);
                iv_pic = itemView.findViewById(R.id.iv_pic);
                tv_theme = itemView.findViewById(R.id.tv_theme);
                tv_year = itemView.findViewById(R.id.tv_year);
                tv_month = itemView.findViewById(R.id.tv_month);
                tv_day = itemView.findViewById(R.id.tv_day);
                tv_threedays = itemView.findViewById(R.id.tv_threedays);
                tv_sevendays = itemView.findViewById(R.id.tv_sevendays);
            }
        }


    }


    public List<Object> getItemList() {

        List<CategorySum> ltJson = getCategoryList();
        // List<PiechartData> piechartData = getPiechartdata();

        itemList = new ArrayList<>();
        itemList.add(getSalesEntries());
//        itemList.add(new PieChartItem("Daily", 2017,
//                10, 17, 2017, 10, 31));
        for (CategorySum s : ltJson) {
            itemList.add(new CategorySum(s));
        }

        return itemList;

    }


    public List<CategorySum> getCategoryList() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "categorySum");
        jsonObject.addProperty("account", Common.getAccount(getContext()));
        jsonObject.addProperty("password", Common.getPWD(getContext()));

        String msg = "";
        if (Common.checkNetConnected(getContext())) {

            String url = Common.URL + Common.WEBSUMMARY;
            MyTask myTask = new MyTask(url, jsonObject.toString());
            try {
                msg = myTask.execute().get().trim();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        Gson gson = new Gson();
        JsonObject outJsonObject = gson.fromJson(msg, JsonObject.class);
        String ltString = outJsonObject.get("CategorySum").getAsString();

        Type tySum = new TypeToken<List<CategorySum>>() {
        }.getType();
        List<CategorySum> ltJson = new Gson().fromJson(ltString, tySum);

        return ltJson;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ry_Next:
                if (rvposition == itemList.size() - 1) {
                    rvItem.scrollToPosition(itemList.size() - 1);
                } else {
                    rvItem.scrollToPosition(rvposition + 1);
                }
                break;
            case R.id.ry_Previous:
                if (rvposition == 0) {
                    rvItem.scrollToPosition(0);
                } else {
                    rvItem.scrollToPosition(rvposition - 1);
                }
                break;
            default:
                break;
        }

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
}

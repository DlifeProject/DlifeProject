package com.kang.Dlife.tb_page2;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class Page2 extends Fragment implements View.OnClickListener {
    private final String TAG = "page2";

    private String[] allItemIndex = new String[6];
    private int itemIndex = 0;
    private List<PiechartData> piechartDataList = new ArrayList<>();
    private List<CategorySum> categorySum = new ArrayList<>();

    // 統計時間 修改為 hr
    private double totalHour = 0;
    private int totalDiaryCount = 0;
    public DatePickerDialog.OnDateSetListener sinceDateSetListener, endDateSetListener;

    private final int initSubDay = -90;
    //修改為 0000-00-00 在使用 array 存放 年 月 日
    private String startDay = Common.toDayString(initSubDay);
    private String[] startDayArray = startDay.split("-");
    private String endDay = Common.getNowDayString();
    private String[] endDayArray = endDay.split("-");

    public RelativeLayout ry_Previous, ry_Next;
    public RecyclerView rvItem;
    public Page2Adapter.PieChartViewHolder pieChartViewHolder;

    private void setStartDayArray() {
        startDayArray = startDay.split("-");
    }
    private void setEndDayArray() {
        endDayArray = endDay.split("-");
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ry_Next:
                itemIndex = itemIndex + 1;
                if (itemIndex >= allItemIndex.length) {
                    itemIndex = 0;
                    rvItem.scrollToPosition(itemIndex);
                } else {
                    rvItem.scrollToPosition(itemIndex);
                }
                break;
            case R.id.ry_Previous:
                itemIndex = itemIndex - 1;
                if (itemIndex < 0) {
                    itemIndex = allItemIndex.length - 1;
                    rvItem.scrollToPosition(itemIndex);
                } else {
                    rvItem.scrollToPosition(itemIndex);
                }
                break;
            default:
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page2, container, false);
        for(int i=0; i<allItemIndex.length;i++){
            if(i==0){
                allItemIndex[0] = "Diary";
            }else{
                allItemIndex[i] = Common.DEFAULTCATE[i-1];
            }
        }
        setStartDayArray();
        setEndDayArray();
        setCategoryCategorySum();

        // 一頁一頁卡住
        //分類資料 不含pie chart
        rvItem = view.findViewById(R.id.rvItem);
        rvItem.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        rvItem.setOnFlingListener(null);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(rvItem);
        rvItem.setAdapter(new Page2Adapter(getContext()));

        ry_Previous = view.findViewById(R.id.ry_Previous);
        ry_Next = view.findViewById(R.id.ry_Next);
        ry_Next.setOnClickListener(this);
        ry_Previous.setOnClickListener(this);

        return view;
    }

    private class Page2Adapter extends RecyclerView.Adapter {
        public int index;
        private Context context;

        public Page2Adapter(Context context) {
            this.context = context;
        }

        private void setEndDayString() {
            setEndDayArray();
            pieChartViewHolder.tv_endyear.setText(endDayArray[0]);
            pieChartViewHolder.tv_endmonth.setText(endDayArray[1]);
            pieChartViewHolder.tv_endday.setText(endDayArray[2]);
        }

        private void setFromDayString() {
            setStartDayArray();
            pieChartViewHolder.tv_startyear.setText(startDayArray[0]);
            pieChartViewHolder.tv_startmonth.setText(startDayArray[1]);
            pieChartViewHolder.tv_startday.setText(startDayArray[2]);
        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0){
                return 0;
            }else{
                return 1;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            if (itemIndex == 0 && viewType == 0) {       //pie chart
                View itemView0 = layoutInflater.inflate(R.layout.page2_pie_chart, viewGroup, false);
                return new PieChartViewHolder(itemView0);
            } else {                    //category
                View itemView1 = layoutInflater.inflate(R.layout.page2_viewpager, viewGroup, false);
                return new CategoryViewHolder(itemView1);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(position == 0){
                setPieChartView(context,holder);
            }else{
                setCategoryView(holder,position);
            }

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private void setPieChartView(Context context, RecyclerView.ViewHolder holder) {
            //setCategoryCategorySum();
            pieChartViewHolder = (PieChartViewHolder) holder;
            pieChartViewHolder.tv_click.setText("Daily");
            pieChartViewHolder.ry_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("startDay", startDay);
                    bundle.putString("endDay", endDay);
                    bundle.putInt("categoryListIndex", -1);
                    startActivity(new Intent(getActivity(), DiaryView.class).putExtras(bundle));
                }
            });

            setFromDayString();
            setEndDayString();

            pieChartViewHolder.ry_Since.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatePickerDialog dialog = new DatePickerDialog(
                            getActivity()
                            , android.R.style.Theme_Holo_Dialog_MinWidth
                            , sinceDateSetListener
                            , Integer.valueOf(startDayArray[0])
                            , Integer.valueOf(startDayArray[1])
                            , Integer.valueOf(startDayArray[2])
                    );

                    DatePicker datePicker = dialog.getDatePicker();
                    Calendar scalendar = Calendar.getInstance();
                    datePicker.setMaxDate(scalendar.getTimeInMillis());
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });

            sinceDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker sinceDatePicker, int startyear, int startmonth, int startday) {
                    startDay = Common.setDateFormat(startyear,startmonth + 1,startday);
                    setFromDayString();
                    setCategoryPieChartData();
                }
            };

            pieChartViewHolder.ry_End.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {

                    Calendar endCalendar = Calendar.getInstance();
                    endCalendar.set(
                             Integer.valueOf(startDayArray[0])
                            ,Integer.valueOf(startDayArray[1]) - 1
                            ,Integer.valueOf(startDayArray[2])
                            );

                    DatePickerDialog dialog = new DatePickerDialog(
                            getActivity()
                            ,android.R.style.Theme_Holo_Dialog_MinWidth
                            , endDateSetListener
                            , Integer.valueOf(endDayArray[0])
                            , Integer.valueOf(endDayArray[1])
                            , Integer.valueOf(endDayArray[2])
                    );

                    DatePicker datePicker = dialog.getDatePicker();
                    datePicker.setMaxDate(endCalendar.getTimeInMillis());
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });

            endDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int endyear, int endmonth, int endday) {
                    endDay = Common.setDateFormat(endyear,endmonth + 1, endday);
                    setEndDayString();
                    setCategoryPieChartData();
                }
            };
            setCategoryPieChartData();
            createPieChart();


        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void createPieChart() {

            pieChartViewHolder.pieChart.setRotationEnabled(true);   //設定可否旋轉
            pieChartViewHolder.pieChart.setCenterText(String.valueOf(totalDiaryCount));  // 設定圓心文字
            pieChartViewHolder.pieChart.setCenterTextSize(35);      // 設定圓心文字大小
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
                public void onNothingSelected() { }
            });

            Legend legend = pieChartViewHolder.pieChart.getLegend();
            legend.setEnabled(false);
            pieChartViewHolder.pieChart.setData(getPieData());
            pieChartViewHolder.pieChart.invalidate();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private void setCategoryPieChartData() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "select");
            jsonObject.addProperty("account", Common.getAccount(getContext()));
            jsonObject.addProperty("password", Common.getPWD(getContext()));
            jsonObject.addProperty("startDay", startDay);
            jsonObject.addProperty("endDay", endDay);

            String url = Common.URL + Common.PIECHARTSERVLET;
            MyTask myTask = new MyTask(url, jsonObject.toString());
            try {
                String inStr = myTask.execute().get().trim();
                Gson gson = new Gson();
                JsonObject jsonObject2 = gson.fromJson(inStr, JsonObject.class);
                String selectItems = jsonObject2.get("select").getAsString();

                Type listType = new TypeToken<List<PiechartData>>() {}.getType();
                piechartDataList = gson.fromJson(selectItems, listType);
                totalDiaryCount = 0;
                totalHour = 0;
                for (PiechartData piechartData : piechartDataList){
                    totalHour = totalHour + piechartData.getCategoryTime();
                    totalDiaryCount = totalDiaryCount + piechartData.getDiaryCount();
                }
                pieChartViewHolder.pieChart.setData(getPieData());
                pieChartViewHolder.pieChart.invalidate();

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        private PieData getPieData(){

            List<PieEntry> pieEntries = getPieEntries();
            PieDataSet pieDataSet = new PieDataSet(pieEntries, "Diaries");
            pieDataSet.setValueFormatter(new PieChartValue());
            pieDataSet.setValueTextColor(Color.BLUE);
            pieDataSet.setValueTextSize(20);
            pieDataSet.setSliceSpace(2);
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS); //使用官訂顏色範本，顏色不能超過5種，否則官定範本要加顏色
            PieData pieData = new PieData(pieDataSet);
            return pieData;
        }

        private List<PieEntry> getPieEntries() {
            List<PieEntry> pieEntries = new ArrayList<>();
            if(totalHour == 0){
                pieEntries.add(new PieEntry(100 ,"not enough diary"));
            }else {
                for (PiechartData piechartData : piechartDataList){
                    if (piechartData.getCategoryTime() > 0){
                        float tempFloat =  (float)piechartData.getCategoryTime() / (float)totalHour * 100;
                        pieEntries.add(new PieEntry(tempFloat, piechartData.getCategory()));
                    }
                }
            }
            return pieEntries;
        }

        public void setCategoryView(RecyclerView.ViewHolder holder, int position){
            index = position - 1;
            final CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
            CategorySum item = categorySum.get(index);
            String url = Common.URL + Common.WEBPHOTO;
            //
            int imageSize = 350;
            if(item.getDiaryPhotoSK() > 0 ) {
                SpotGetImageTask spotGetImageTask = new SpotGetImageTask(url, item.getDiaryPhotoSK(), imageSize, categoryViewHolder.iv_pic);
                spotGetImageTask.execute();
            }
            categoryViewHolder.tv_click.setText(item.getCategoryType());
            categoryViewHolder.ry_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), DiaryView.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("startDay", startDay);
                    bundle.putString("endDay", endDay);
                    bundle.putInt("categoryListIndex", index);
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
        }

        @Override
        public int getItemCount() {
            int indexLength = allItemIndex.length;
            return indexLength;
        }

        class PieChartViewHolder extends RecyclerView.ViewHolder {

            RelativeLayout ry_click;
            LinearLayout ry_End, ry_Since;
            TextView tv_click, tv_startyear, tv_startmonth, tv_startday, tv_endyear, tv_endmonth, tv_endday;
            PieChart pieChart;

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
            }
        }

        class CategoryViewHolder extends RecyclerView.ViewHolder {

            RelativeLayout ry_click;
            ImageView iv_pic;
            TextView tv_click, tv_theme, tv_year, tv_month, tv_day, tv_threedays, tv_sevendays;

            @SuppressLint("WrongViewCast")
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

    public void setCategoryCategorySum() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "categorySum");
        jsonObject.addProperty("account", Common.getAccount(getContext()));
        jsonObject.addProperty("password", Common.getPWD(getContext()));
        String url = Common.URL + Common.WEBSUMMARY;
        String msg = "";
        MyTask myTask = new MyTask(url, jsonObject.toString());
        try {
            msg = myTask.execute().get().trim();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        Gson gson = new Gson();
        JsonObject outJsonObject = gson.fromJson(msg, JsonObject.class);
        String ltString = outJsonObject.get("categorySum").getAsString();

        Type tySum = new TypeToken<List<CategorySum>>() {
        }.getType();
        categorySum = new Gson().fromJson(ltString, tySum);
    }


    public class SpotGetImageTask extends AsyncTask<Object, Integer, Bitmap> {
        private final static String TAG = "SpotGetImageTask";
        private String url;
        private int id, imageSize;

        // WeakReference物件不會阻止參照到的實體被回收
        private WeakReference<ImageView> imageViewWeakReference;

        SpotGetImageTask(String url, int id, int imageSize) {
            this(url, id, imageSize, null);
        }

        public SpotGetImageTask(String url, int id, int imageSize, ImageView imageView) {
            this.url = url;
            this.id = id;
            this.imageSize = imageSize;
            this.imageViewWeakReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(Object... params) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getImage");
            jsonObject.addProperty("account", Common.getAccount(getContext()));
            jsonObject.addProperty("password", Common.getPWD(getContext()));
            jsonObject.addProperty("id", id);
            jsonObject.addProperty("imageSize", imageSize);
            return getRemoteImage(url, jsonObject.toString());
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView imageView = imageViewWeakReference.get();
            if (isCancelled() || imageView == null) {
                return;
            }
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                // imageView.setImageResource(R.drawable.ex_photo);
            }
        }

        private Bitmap getRemoteImage(String url, String jsonOut) {
            HttpURLConnection connection = null;
            Bitmap bitmap = null;
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setDoInput(true); // allow inputs
                connection.setDoOutput(true); // allow outputs
                connection.setUseCaches(false); // do not use a cached copy
                connection.setRequestMethod("POST");
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                bw.write(jsonOut);
                Log.d(TAG, "output: " + jsonOut);
                bw.close();

                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {
                    bitmap = BitmapFactory.decodeStream(
                            new BufferedInputStream(connection.getInputStream()));
                } else {
                    Log.d(TAG, "response code: " + responseCode);
                }
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return bitmap;
        }
    }
}

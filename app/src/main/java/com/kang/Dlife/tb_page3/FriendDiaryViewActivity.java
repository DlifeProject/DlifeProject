package com.kang.Dlife.tb_page3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.kang.Dlife.Common;
import com.kang.Dlife.R;
import com.kang.Dlife.sever.MyTask;
import com.kang.Dlife.tb_page2.CategorySum;
import com.kang.Dlife.tb_page2.diary_view.PhotoSpot;
import com.kang.Dlife.sever.LocationToDiary;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FriendDiaryViewActivity extends AppCompatActivity {


    //Regan add
    private final static String TAG = "FriendDiaryViewActivity";
    public Bundle bundle;
    private MatchFriendItem matchFriendItem;
    private String fromPage;
    private TextView headerTitle;


    private ImageButton ibBack;
    private RecyclerView recyclerView;
    private MyTask newsGetAllTask;
    private SpotGetImageTask spotGetImageTask;
    List<LocationToDiary> igList;

    private double longitude;
    private double latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page2_diary_view);

        bundle = getIntent().getExtras();
        headerTitle = (TextView) findViewById(R.id.textView);
        matchFriendItem = (MatchFriendItem) bundle.getSerializable("MatchFriendItem");
        fromPage = bundle.getString("fromPage");
        if(fromPage.equals("page3")){
            // open header button
            headerTitle.setText(matchFriendItem.getMyFriendName());


        }else{
            // hidden header button
            headerTitle.setText("------");
        }
        ibBack = (ImageButton) super.findViewById(R.id.ibBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if (matchFriendItem==null) {
            Common.showToast(this, R.string.msg_NoThisFriend);
        }else {

            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            showAllNews();
        }

    }


    private void showAllNews() {

        if (Common.checkNetConnected(this)) {
            String url = "";
            List<LocationToDiary> ltJson = null;
            try {

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getFriendDiary");
                jsonObject.addProperty("account", Common.getAccount(FriendDiaryViewActivity.this));
                jsonObject.addProperty("password", Common.getPWD(FriendDiaryViewActivity.this));
                jsonObject.addProperty("MyFriendSK", matchFriendItem.getMyFriendSK());
                jsonObject.addProperty("MyFriendCategorySK", matchFriendItem.getMyFriendCategorySK());
                String jsonOut = jsonObject.toString();

                url = Common.URL + Common.FRIEND;
                MyTask getDiaryTask = new MyTask(url, jsonOut);
                String getDiaryJsonIn = getDiaryTask.execute().get().trim();

                if(getDiaryJsonIn.equals("getfriendDiaryError")){
                    Common.showToast(this, R.string.msg_FriendHasNoDiary);
                }else{
                    Gson gson = new Gson();
                    JsonObject diaryInJsonObject = gson.fromJson(getDiaryJsonIn, JsonObject.class);
                    String ltDiaryDetailString = diaryInJsonObject.get("getFriendDiary").getAsString();

                    Type friendDiary = new TypeToken<List<LocationToDiary>>() { }.getType();
                    igList = new Gson().fromJson(ltDiaryDetailString, friendDiary);


                }



            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }


            if (igList == null || igList.isEmpty()) {
                Common.showToast(this, R.string.msg_FriendHasNoDiary);
            } else {
                recyclerView.setAdapter(new IgAdapter(this, igList));
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }
    }


    //  供內部使用 所以可以包來包去 因為已經是private 了所以裡面的不用寫
    private class IgAdapter extends RecyclerView.Adapter<IgAdapter.MyViewHolder> {

        private Context context;
        private List<LocationToDiary> igList;

        //顯示什麼東西
        IgAdapter(Context context, List<LocationToDiary> igList) {
            this.context = context;
            this.igList = igList;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tvDate, tvLocation, tvTime, tvNote, tvContinue;
            RecyclerView mRecyclerView;

            MyViewHolder(View itemView) {
                super(itemView);
                mRecyclerView = (RecyclerView) itemView.findViewById(R.id.rvImage);

                tvDate = (TextView) itemView
                        .findViewById(R.id.tvDate);
                tvLocation = (TextView) itemView
                        .findViewById(R.id.tvLocation);
                tvTime = (TextView) itemView
                        .findViewById(R.id.tvTime);
                tvNote = (TextView) itemView.findViewById(R.id.tvNote);
                tvContinue = itemView.findViewById(R.id.tvContinue);

            }
        }

        @Override
        public IgAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.page2_diary_view_recyclerview, viewGroup, false);

            return new IgAdapter.MyViewHolder(itemView);
        }

        @Override

        public void onBindViewHolder(final IgAdapter.MyViewHolder viewHolder, int position) {

            viewHolder.mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

            //先放這就不會滑到底
            viewHolder.mRecyclerView.setOnFlingListener(null);
            PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
            pagerSnapHelper.attachToRecyclerView(viewHolder.mRecyclerView);

            List<PhotoSpot> photoSpotList = null;

            if (Common.checkNetConnected(context)) {
                String url = Common.URL + Common.WEBPHOTO;

                try {

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "getDiaryPhotoSKList");
                    jsonObject.addProperty("account", Common.getAccount(FriendDiaryViewActivity.this));
                    jsonObject.addProperty("password", Common.getPWD(FriendDiaryViewActivity.this));
                    jsonObject.addProperty("diarySK", igList.get(position).getSk());
                    String jsonOut = jsonObject.toString();

                    MyTask getDiaryPhotoSKTask = new MyTask(url, jsonOut);

                    String getDiaryPhotoSKjsonIn = getDiaryPhotoSKTask.execute().get();
                    Type ltWeb = new TypeToken<List<PhotoSpot>>() {
                    }.getType();
                    photoSpotList = new Gson().fromJson(getDiaryPhotoSKjsonIn, ltWeb);


                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }

            }


            viewHolder.mRecyclerView.setAdapter(new IgPictureAdapter(context, photoSpotList));


            //寫成function 加入 common
            final LocationToDiary diaryDetail = igList.get(position);
            //时间格式,HH是24小时制，hh是AM PM12小时制
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//比如timestamp=1449210225945；
            long longStart = Long.valueOf(diaryDetail.getStart_stamp());
            long longEnd = Long.valueOf(diaryDetail.getEnd_stamp());
            String startTime = sdf.format(new Date(longStart * 1000L));
            String endTime = sdf.format(new Date(longEnd * 1000L));
//至于取10位或取13位，date_temp*1000L就是这种截取作用。如果是和服务器传值的，就和后台商量好就可以了
            String time = startTime + "-" + endTime;


//          String time = Common.dateStringToHM(diaryDetail.getStart_stamp())  + "-" +Common.dateStringToHM(diaryDetail.getEnd_stamp()) ;
            viewHolder.tvDate.setText(diaryDetail.getPost_day());
            longitude = diaryDetail.getLongitude();
            latitude = diaryDetail.getLatitude();
            Geocoder geocoder = new Geocoder(FriendDiaryViewActivity.this);
            try {
                List<Address> addressList =
                        geocoder.getFromLocation(latitude, longitude, 1);
                String addrStr = "";
                if (addressList.size() > 0) {
                    Address address = addressList.get(0);
                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                        addrStr = address.getAddressLine(i);

                    }
                    viewHolder.tvLocation.setText(addrStr);

                } else {
                    viewHolder.tvLocation.setText("unknown");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            viewHolder.tvTime.setText(time);
            viewHolder.tvNote.setText(diaryDetail.getNote());
            viewHolder.tvContinue.setVisibility(View.GONE);

            if (viewHolder.tvNote.length() > 11) {
                viewHolder.tvNote.setText(diaryDetail.getNote().substring(0, 11));

                viewHolder.tvContinue.setVisibility(View.VISIBLE);


                viewHolder.tvContinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        viewHolder.tvContinue.setVisibility(View.GONE);
                        viewHolder.tvNote.setText(diaryDetail.getNote().substring(0));
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return igList.size();
        }
    }


    private class IgPictureAdapter extends
            RecyclerView.Adapter<IgPictureAdapter.MyViewHolder> {
        private Context context;
        private List<PhotoSpot> photoSpotList;
        private int imageSize;

        IgPictureAdapter(Context context, List<PhotoSpot> photoSpotList) {
            this.context = context;
            this.photoSpotList = photoSpotList;
            imageSize = getResources().getDisplayMetrics().widthPixels;
        }

        //相當於一個資料夾hold住這三個的捷徑
        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivRecyclerImage;


            MyViewHolder(View itemView) {
                super(itemView);
                ivRecyclerImage = (ImageView) itemView.findViewById(R.id.ivRecyclerImage);


            }
        }

        //建立個替身 因為常用  建立完捷徑再給他整理起來
        @Override
        public int getItemCount() {
            return photoSpotList.size();
        }

        @Override
        public IgPictureAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.page2_diary_view_photo_item, viewGroup, false);
            return new IgPictureAdapter.MyViewHolder(itemView);
        }

        //position 就是當初listview的 index
        @Override
        public void onBindViewHolder(final IgPictureAdapter.MyViewHolder viewHolder, int position) {

            final PhotoSpot photoSpot = photoSpotList.get(position);

            String url = Common.URL + Common.WEBPHOTO;

            int id = photoSpot.getSk();
            spotGetImageTask = new SpotGetImageTask(url, id, imageSize, viewHolder.ivRecyclerImage);
            spotGetImageTask.execute();   //只要沒寫get 就是一直讓他抓 不等圖 不然會卡著等圖
        }
    }

    //新開 SpotGetImageTask 寫入/java 下
    class SpotGetImageTask extends AsyncTask<Object, Integer, Bitmap> {
        private final static String TAG = "SpotGetImageTask";
        private String url;
        private int id, imageSize;

        // WeakReference物件不會阻止參照到的實體被回收
        private WeakReference<ImageView> imageViewWeakReference;

        SpotGetImageTask(String url, int id, int imageSize) {
            this(url, id, imageSize, null);
        }

        SpotGetImageTask(String url, int id, int imageSize, ImageView imageView) {
            this.url = url;
            this.id = id;
            this.imageSize = imageSize;
            this.imageViewWeakReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(Object... params) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getImage");
            jsonObject.addProperty("account", Common.getAccount(FriendDiaryViewActivity.this));
            jsonObject.addProperty("password", Common.getPWD(FriendDiaryViewActivity.this));
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
//            imageView.setImageResource(R.drawable.default_image);
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

    @Override
    public void onStop() {
        super.onStop();
        if (newsGetAllTask != null) {
            newsGetAllTask.cancel(true);
        }

    }
}

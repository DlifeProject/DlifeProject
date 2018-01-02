package com.yancy.gallerypickdemo.tb_page2.diary_view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yancy.gallerypickdemo.R;

import java.util.ArrayList;
import java.util.List;


public class DiaryView extends AppCompatActivity {
    private ImageButton btBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page2_diary_view);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        List<DiaryViewSpot> igList = getIglist();

        recyclerView.setAdapter(new IgAdapter(this, igList));
        initView();

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                }});


    }
    private void initView() {
        btBack = (ImageButton) super.findViewById(R.id.btBack);
    }

    //  供內部使用 所以可以包來包去 因為已經是private 了所以裡面的不用寫
    private class IgAdapter extends
            RecyclerView.Adapter<IgAdapter.MyViewHolder> {
        private Context context;
        private List<DiaryViewSpot> igList;

        //顯示什麼東西
        IgAdapter(Context context, List<DiaryViewSpot> igList) {
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

            return new MyViewHolder(itemView);
        }

        @Override

        public void onBindViewHolder(final MyViewHolder viewHolder, int position) {

            viewHolder.mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
//
//
//            SnapHelper snapHelperStart = new StartSnapHelper();
//            snapHelperStart.attachToRecyclerView(viewHolder.mRecyclerView);
            viewHolder.mRecyclerView.setOnFlingListener(null);//先放這就不會滑到底
            PagerSnapHelper pagerSnapHelper=new PagerSnapHelper();
            pagerSnapHelper.attachToRecyclerView(viewHolder.mRecyclerView);

            List<PhotoSpot> photoSpotList = getPicturelist();

            viewHolder.mRecyclerView.setAdapter(new IgPictureAdapter(context, photoSpotList));




























            final DiaryViewSpot diaryViewSpot = igList.get(position);
            viewHolder.tvDate.setText(diaryViewSpot.getDate());
            viewHolder.tvLocation.setText(diaryViewSpot.getLocation());
            viewHolder.tvTime.setText(diaryViewSpot.getTime());
            viewHolder.tvNote.setText(diaryViewSpot.getNote());
            viewHolder.tvContinue.setVisibility(View.GONE);

            if (viewHolder.tvNote.length() > 11) {
                viewHolder.tvNote.setText(diaryViewSpot.getNote().substring(0, 11));

                viewHolder.tvContinue.setVisibility(View.VISIBLE);


                viewHolder.tvContinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        viewHolder.tvContinue.setVisibility(View.GONE);
                        viewHolder.tvNote.setText(diaryViewSpot.getNote().substring(0));
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

        IgPictureAdapter(Context context, List<PhotoSpot> photoSpotList) {
            this.context = context;
            this.photoSpotList = photoSpotList;
        }
        //相當於一個資料夾ＨＯＬＤ住這三個的捷徑
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
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.page2_diary_view_photo_item, viewGroup, false);
            return new MyViewHolder(itemView);
        }
        //position 就是當初listview的 index
        @Override
        public void onBindViewHolder(final MyViewHolder viewHolder, int position) {
            final PhotoSpot photoSpot = photoSpotList.get(position);
            viewHolder.ivRecyclerImage.setImageResource(photoSpot.getImage());



//            viewHolder.ivRecyclerImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    ImageView imageView = new ImageView(context);
//                    imageView.setImageResource(photoSpot.getImage());
//
//                    //點擊跑出來的
//                    Toast toast = new Toast(context);
//                    toast.setView(imageView);
//                    toast.setDuration(Toast.LENGTH_SHORT);
//                    toast.show();
//
//
//                }
//            });
        }
    }


    public List<DiaryViewSpot> getIglist() {
        List<DiaryViewSpot> igList = new ArrayList<>();
        igList.add(new DiaryViewSpot("2017/12/01", "中央", "8:00-10:00", "今天來學校上課，好開心"));
        igList.add(new DiaryViewSpot(
                "2017/12/02", "蘭苑臭臭屋", "5:30-10:00", "蘭苑手工薯條超級難吃，吃起來臭臭的，又貴，好想退貨喔! 結果不能退老闆也太機車了吧"));
        igList.add(new DiaryViewSpot("2017/12/06", "松鼠", "6:00-10:00", "天天吃松鼠，身體變麻糬，趕快再吃一根玉蜀黍，就能壓壓驚，挖勒沒押韻！"));
        igList.add(new DiaryViewSpot("2017/12/01", "中央", "8:00-10:00", "今天來學校上課，好開心"));
        igList.add(new DiaryViewSpot(
                "2017/12/02", "蘭苑臭臭屋", "5:30-10:00", "蘭苑手工薯條超級難吃，吃起來臭臭的，又貴，好想退貨喔! 結果不能退老闆也太機車了吧"));
        igList.add(new DiaryViewSpot("2017/12/06", "松鼠", "6:00-10:00", "天天吃松鼠，身體變麻糬，趕快再吃一根玉蜀黍，就能壓壓驚，挖勒沒押韻！"));
        return igList;

    }


    public List<PhotoSpot> getPicturelist() {
        List<PhotoSpot> photoSpotList = new ArrayList<>();

        photoSpotList.add(new PhotoSpot(R.drawable.cat_1));
        photoSpotList.add(new PhotoSpot(R.drawable.lion));
        photoSpotList.add(new PhotoSpot(R.drawable.tired));
        photoSpotList.add(new PhotoSpot(R.drawable.cat_1));
        photoSpotList.add(new PhotoSpot(R.drawable.lion));
        photoSpotList.add(new PhotoSpot(R.drawable.tired));
        return photoSpotList;
    }

}



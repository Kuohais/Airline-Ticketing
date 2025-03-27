package com.example.airlineticketing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airlineticketing.R;
import com.example.airlineticketing.bean.TrainPassStation;

import java.util.ArrayList;
import java.util.List;

public class TrainPassStationAdapter extends RecyclerView.Adapter<TrainPassStationAdapter.ViewHolder> {
    private List<TrainPassStation> list = new ArrayList<>();
    private Context mActivity;
    private ItemListener mItemListener;

    public void setItemListener(ItemListener itemListener) {
        this.mItemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_rv_trainpassstation_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TrainPassStation trainPassStation = list.get(i);
        if (trainPassStation != null) {
            viewHolder.cityname.setText(String.format("城市名称：%s", trainPassStation.getCityname()));
            viewHolder.stationname.setText(String.format("站点名称：%s", trainPassStation.getStationname()));
            viewHolder.stationid.setText(String.format("站点顺序：%s", trainPassStation.getStationid()));
            viewHolder.arrivetime.setText(String.format("达到时间：%s", trainPassStation.getArrivetime()));
            viewHolder.staytime.setText(String.format("停留时间：%s", trainPassStation.getStaytime()));
            viewHolder.leavetime.setText(String.format("离开时间：%s", trainPassStation.getLeavetime()));
            viewHolder.btn_first.setVisibility(i==0?View.GONE:View.VISIBLE);
            viewHolder.btn_second.setVisibility(i==0?View.GONE:View.VISIBLE);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener != null) {
                        mItemListener.ItemClick(trainPassStation);
                    }

                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mItemListener != null) {
                        mItemListener.ItemLongClick(trainPassStation);
                    }

                    return false;
                }
            });
            viewHolder.btn_first.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener != null) {
                        if (i>0){
                            mItemListener.FirstClick(list.get(i-1),trainPassStation);
                        }

                    }
                }
            });
            viewHolder.btn_second.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener != null) {
                        if (i>0){
                            mItemListener.SecondClick(list.get(i-1),trainPassStation);
                        }
                    }
                }
            });
        }
    }

    public void addItem(List<TrainPassStation> listAdd) {
        //如果是加载第一页，需要先清空数据列表
        this.list.clear();
        if (listAdd != null) {
            //添加数据
            this.list.addAll(listAdd);
        }
        //通知RecyclerView进行改变--整体
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView cityname;
        private TextView stationname;
        private TextView stationid;
        private TextView arrivetime;
        private TextView staytime;
        private TextView leavetime;
        private Button btn_first;
        private Button btn_second;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityname = itemView.findViewById(R.id.cityname);
            stationname = itemView.findViewById(R.id.stationname);
            stationid = itemView.findViewById(R.id.stationid);
            arrivetime = itemView.findViewById(R.id.arrivetime);
            staytime = itemView.findViewById(R.id.staytime);
            leavetime = itemView.findViewById(R.id.leavetime);
            btn_first = itemView.findViewById(R.id.btn_first);
            btn_second = itemView.findViewById(R.id.btn_second);
        }
    }


    public interface ItemListener {
        void ItemClick(TrainPassStation trainPassStation);
        void ItemLongClick(TrainPassStation trainPassStation);
        void FirstClick(TrainPassStation firstTrainPassStation,TrainPassStation secondTrainPassStation);
        void SecondClick(TrainPassStation firstTrainPassStation,TrainPassStation secondTrainPassStation);
    }
}

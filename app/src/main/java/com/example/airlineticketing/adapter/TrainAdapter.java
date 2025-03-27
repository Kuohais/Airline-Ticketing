package com.example.airlineticketing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airlineticketing.R;
import com.example.airlineticketing.bean.TrainMessage;

import java.util.ArrayList;
import java.util.List;

public class TrainAdapter extends RecyclerView.Adapter<TrainAdapter.ViewHolder> {
    private List<TrainMessage> list = new ArrayList<>();
    private Context mActivity;
    private ItemListener mItemListener;

    public void setItemListener(ItemListener itemListener) {
        this.mItemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_rv_train_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TrainMessage trainMessage = list.get(i);
        if (trainMessage != null) {
            viewHolder.trainid.setText(String.format("机次：%s", trainMessage.getTrainId()));
            viewHolder.trainname.setText(String.format("名称：%s", trainMessage.getTrainname()));
            viewHolder.time_view.setText(String.format("日期：%s", trainMessage.getTime()));
            viewHolder.type.setText(String.format("类型：%s", trainMessage.getTraintype()));
            viewHolder.startstation.setText(String.format("起始站：%s", trainMessage.getStartstation()));
            viewHolder.arrivestation.setText(String.format("终点站：%s", trainMessage.getArrivestation()));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener != null) {
                        mItemListener.ItemClick(trainMessage);
                    }

                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mItemListener != null) {
                        mItemListener.ItemLongClick(trainMessage);
                    }

                    return false;
                }
            });
        }
    }

    public void addItem(List<TrainMessage> listAdd) {
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
        private TextView trainid;
        private TextView trainname;
        private TextView time_view;
        private TextView type;
        private TextView startstation;
        private TextView arrivestation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trainid = itemView.findViewById(R.id.trainid);
            trainname = itemView.findViewById(R.id.trainname);
            time_view = itemView.findViewById(R.id.time_view);
            type = itemView.findViewById(R.id.type);
            startstation = itemView.findViewById(R.id.startstation);
            arrivestation = itemView.findViewById(R.id.arrivestation);
        }
    }


    public interface ItemListener {
        void ItemClick(TrainMessage trainMessage);
        void ItemLongClick(TrainMessage trainMessage);
    }
}

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
import com.example.airlineticketing.bean.TrainVo;

import java.util.ArrayList;
import java.util.List;

public class TrainBuyAdapter extends RecyclerView.Adapter<TrainBuyAdapter.ViewHolder> {
    private List<TrainVo> list = new ArrayList<>();
    private Context mActivity;
    private ItemListener mItemListener;

    public void setItemListener(ItemListener itemListener) {
        this.mItemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_rv_train_buy_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TrainVo trainVo = list.get(i);
        if (trainVo != null) {
            viewHolder.trainId_view.setText(trainVo.getTrainId());
            viewHolder.start_station_view.setText(trainVo.getStart_station_name());
            viewHolder.leave_time_view.setText(trainVo.getLeaveTime());
            viewHolder.arrive_station_view.setText(trainVo.getArrive_station_name());
            viewHolder.arrive_time_view.setText(trainVo.getArriveTime());
            viewHolder.total_time_view.setText(trainVo.getArriveTime());
            viewHolder.total_time_view.setText(calculateTotalTime(trainVo.getLeaveTime(), trainVo.getArriveTime()));
            viewHolder.first_seat_price_view.setText(String.format("¥%.2f", trainVo.getFirstSeatPrice()));
            viewHolder.second_seat_price_view.setText(String.format("¥%.2f", trainVo.getSecondSeatPrice()));


            if(trainVo.getSecondSeatNumber()>=10){
                viewHolder.first_seat_number_view.setText("有");
            }else{
                viewHolder.first_seat_number_view.setText(trainVo.getFirstSeatNumber()+"张");
            }

            if(trainVo.getSecondSeatNumber()>=10){
                viewHolder.second_seat_number_view.setText("有");
            }else{
                viewHolder.second_seat_number_view.setText(trainVo.getSecondSeatNumber()+"张");
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener != null) {
                        mItemListener.ItemClick(trainVo);
                    }

                }
            });
            viewHolder.first_seat_buy_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener != null) {
                        mItemListener.FirstBuy(trainVo);
                    }
                }
            });
            viewHolder.second_seat_buy_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener != null) {
                        mItemListener.SecondBuy(trainVo);
                    }
                }
            });
        }
    }

    public void addItem(List<TrainVo> listAdd) {
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
        private TextView trainId_view;
        private TextView start_station_view;
        private TextView leave_time_view;
        private TextView arrive_station_view;
        private TextView arrive_time_view;
        private TextView total_time_view;
        private TextView first_seat_number_view;
        private TextView first_seat_price_view;
        private TextView second_seat_number_view;
        private TextView second_seat_price_view;
        private Button first_seat_buy_button;
        private Button second_seat_buy_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trainId_view = itemView.findViewById(R.id.trainid_view);
            start_station_view = itemView.findViewById(R.id.start_station_view);
            leave_time_view = itemView.findViewById(R.id.leave_time_view);
            arrive_station_view = itemView.findViewById(R.id.arrive_station_view);
            arrive_time_view = itemView.findViewById(R.id.arrive_time_view);
            total_time_view = itemView.findViewById(R.id.total_time_view);
            first_seat_number_view = itemView.findViewById(R.id.first_seat_number_view);
            first_seat_price_view = itemView.findViewById(R.id.first_seat_price_view);
            second_seat_number_view = itemView.findViewById(R.id.second_seat_number_view);
            second_seat_price_view = itemView.findViewById(R.id.second_seat_price_view);

            first_seat_buy_button = itemView.findViewById(R.id.first_seat_buy_button);
            second_seat_buy_button = itemView.findViewById(R.id.second_seat_buy_button);
        }
    }


    //根据起飞时间和到达时间计算总时间
    public String calculateTotalTime(String leavetime,String arrivetime){

        //找出分隔符":"的位置
        int leavetime_divide=leavetime.indexOf(":");
        int arrivetime_divide=arrivetime.indexOf(":");

        //获取起飞时间和到达时间的时和分
        int leave_hour=Integer.parseInt(leavetime.substring(0,leavetime_divide));
        int leave_minute=Integer.parseInt(leavetime.substring(leavetime_divide+1,leavetime.length()));
        int arrive_hour=Integer.parseInt(arrivetime.substring(0,arrivetime_divide));
        int arrive_minute=Integer.parseInt(arrivetime.substring(arrivetime_divide+1,arrivetime.length()));

        int total_minute=arrive_hour*60+arrive_minute-(leave_hour*60+leave_minute);
        int hour=total_minute/60;
        int minute=total_minute%60;

        return hour+"小时"+minute+"分钟";

    }

    public interface ItemListener {
        void ItemClick(TrainVo trainVo);
        void FirstBuy(TrainVo trainVo);
        void SecondBuy(TrainVo trainVo);
    }
}

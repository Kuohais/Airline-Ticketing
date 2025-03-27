package com.example.airlineticketing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airlineticketing.R;
import com.example.airlineticketing.bean.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> list = new ArrayList<>();
    private Context mActivity;
    private ItemListener mItemListener;

    public void setItemListener(ItemListener itemListener) {
        this.mItemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_rv_order_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Order order = list.get(i);
        if (order != null) {
            viewHolder.trinid_view.setText(order.getTrainId());
            viewHolder.start_arrive_view.setText(order.getStart_station_name()+" 一 "+order.getArrive_station_name());
            viewHolder.leavetime_view.setText("起飞时间："+order.getTime()+" "+order.getLeaveTime()+"开");
            viewHolder.seat_view.setText("座        次："+order.getCarriageId()+"机厢 "+order.getRowId()+order.getPosition()+" "+order.getSeatType());
            viewHolder.passenger_view.setText("乘  客  名："+order.getUserId());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener != null) {
                        mItemListener.ItemClick(order);
                    }

                }
            });
        }
    }

    public void addItem(List<Order> listAdd) {
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
        TextView trinid_view;
        TextView start_arrive_view;
        TextView leavetime_view;
        TextView seat_view;
        TextView passenger_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trinid_view=itemView.findViewById(R.id.trainid_view2);
            start_arrive_view=itemView.findViewById(R.id.start_arrive_view2);
            leavetime_view=itemView.findViewById(R.id.leavetime_view2);
            seat_view=itemView.findViewById(R.id.seat_view2);
            passenger_view=itemView.findViewById(R.id.passenger_view2);
        }
    }


    public interface ItemListener {
        void ItemClick(Order order);
    }
}

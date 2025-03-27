package com.example.airlineticketing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.example.airlineticketing.R;
import com.example.airlineticketing.bean.Message;
import com.example.airlineticketing.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context mActivity;
    private List<Message> list = new ArrayList<>();
    private RequestOptions headerRO = new RequestOptions().circleCrop();//圆角变换
    private ItemListener mItemListener;
    private Integer userId;

    public void setItemListener(ItemListener itemListener) {
        this.mItemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        userId = (Integer) SPUtils.get(mActivity, SPUtils.USER_ID, 0);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_rv_message_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Message message = list.get(i);
        if (message != null) {
            viewHolder.nickName.setText(message.getName());
            viewHolder.date.setText(message.getDate());
            viewHolder.content.setText(message.getContent());
            viewHolder.delete.setVisibility(userId == message.getUserId() ? View.VISIBLE : View.GONE);
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener != null && userId == message.getUserId()) {
                        mItemListener.ItemLongClick(message);
                    }
                }
            });
        }
    }

    public void addItem(List<Message> listAdd) {
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
        private TextView nickName;
        private TextView date;
        private TextView content;
        private ImageView img;
        private ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nickName = itemView.findViewById(R.id.nickName);
            date = itemView.findViewById(R.id.date);
            content = itemView.findViewById(R.id.content);
            img = itemView.findViewById(R.id.img);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    public interface ItemListener {
        void ItemClick(Message message);

        void ItemLongClick(Message message);
    }
}

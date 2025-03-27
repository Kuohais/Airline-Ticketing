package com.example.airlineticketing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airlineticketing.R;
import com.example.airlineticketing.bean.User;
import com.example.airlineticketing.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> list = new ArrayList<>();
    private Context mActivity;
    private ItemListener mItemListener;
    public void setItemListener(ItemListener itemListener){
        this.mItemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_rv_user_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        User user = list.get(i);
        if (user != null) {
            Integer userId = (Integer) SPUtils.get(mActivity, SPUtils.USER_ID, 0);
            viewHolder.itemView.setVisibility(userId.intValue() == user.getId().intValue() ? View.GONE : View.VISIBLE);
            viewHolder.nickName.setText(user.getNickName());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemListener!=null){
                        mItemListener.ItemClick(user);
                    }

                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mItemListener!=null){
                        mItemListener.Delete(user.getId());
                    }

                    return false;
                }
            });
        }
    }

    public void addItem(List<User> listAdd) {
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nickName = itemView.findViewById(R.id.nickName);
        }
    }

    public interface ItemListener{
        void ItemClick(User user);
        void Delete(Integer id);
    }
}

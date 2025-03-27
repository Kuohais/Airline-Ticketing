package com.example.airlineticketing.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airlineticketing.R;
import com.example.airlineticketing.adapter.OrderAdapter;
import com.example.airlineticketing.bean.Order;
import com.example.airlineticketing.enums.UserTypeEnum;
import com.example.airlineticketing.ui.activity.OrderDetailActivity;
import com.example.airlineticketing.util.MySqliteOpenHelper;
import com.example.airlineticketing.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 订单
 */
public class OrderFragment extends Fragment {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private LinearLayout llEmpty;
    private RecyclerView rvList;
    private OrderAdapter orderAdapter;
    private List<Order> list = null;
    private Integer userId;
    private Integer userType;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_order,container,false);
        userId =(Integer) SPUtils.get(myActivity,SPUtils.USER_ID,0);
        userType =(Integer) SPUtils.get(myActivity,SPUtils.USER_TYPE,0);
        helper = new MySqliteOpenHelper(myActivity);
        llEmpty = view.findViewById(R.id.ll_empty);
        rvList = view.findViewById(R.id.rv_list);
        init();
        queryOrder();
        return view;
    }

    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(myActivity);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        orderAdapter = new OrderAdapter();
        //=2.3、设置recyclerView的适配器
        rvList.setAdapter(orderAdapter);
        orderAdapter.setItemListener(new OrderAdapter.ItemListener() {
            @Override
            public void ItemClick(Order order) {
                Intent intent = new Intent(myActivity, OrderDetailActivity.class);
                intent.putExtra("order",order);
                startActivityForResult(intent,100);
            }
        });
    }


    //查询订单
    public void queryOrder(){
        SQLiteDatabase db = helper.getWritableDatabase();
        list = new ArrayList<>();
        list.clear();
        //查询当前用户的所有订单
        String order_sql="select * from orderstable";
        if(userType.intValue()== UserTypeEnum.User.getCode()){
            order_sql+=" where userId='"+userId+"'";
        }

        Cursor result = db.rawQuery(order_sql, null);
        //有订单
        while(result.moveToNext()){

            //读取当前订单项的内容
            Integer id = result.getInt(0);
            String time = result.getString(1);
            Integer userId = result.getInt(2);
            String trainId = result.getString(3);
            String start_station_name = result.getString(4);
            String leaveTime = result.getString(5);
            String arrive_station_name = result.getString(6);
            String arriveTime = result.getString(7);
            Integer carriageId = result.getInt(8);
            Integer rowId = result.getInt(9);
            String position = result.getString(10);
            String seatType = result.getString(11);
            Integer price = result.getInt(12);
            String orderTime = result.getString(13);
            Order order = new Order(id,time,userId,trainId,start_station_name,leaveTime,arrive_station_name,arriveTime,carriageId,rowId,position,seatType,price,orderTime);
            list.add(order);

        }

        if (list != null && list.size() > 0) {
            rvList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            orderAdapter.addItem(list);
        } else {
            rvList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }

    }



    @Override
    public void onResume() {
        super.onResume();
        queryOrder();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100 && resultCode==RESULT_OK){
            queryOrder();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        queryOrder();
    }
}

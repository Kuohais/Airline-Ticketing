package com.example.airlineticketing.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airlineticketing.R;
import com.example.airlineticketing.adapter.TrainAdapter;
import com.example.airlineticketing.bean.TrainMessage;
import com.example.airlineticketing.ui.activity.AddTrainMessageActivity;
import com.example.airlineticketing.ui.activity.TrainPassStationActivity;
import com.example.airlineticketing.util.MySqliteOpenHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理员首页
 */

public class AdminHomeFragment extends Fragment {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private LinearLayout llEmpty;
    private RecyclerView rvList;
    private TrainAdapter trainAdapter;
    private FloatingActionButton btn_add;
    private List<TrainMessage> list = null;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);
        helper = new MySqliteOpenHelper(myActivity);
        llEmpty = view.findViewById(R.id.ll_empty);
        rvList = view.findViewById(R.id.rv_list);
        btn_add = view.findViewById(R.id.btn_add);
        init();
        loadData();
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
        trainAdapter = new TrainAdapter();
        //=2.3、设置recyclerView的适配器
        rvList.setAdapter(trainAdapter);
        trainAdapter.setItemListener(new TrainAdapter.ItemListener() {
            @Override
            public void ItemClick(TrainMessage trainMessage) {
                final String[] items = new String[]{"查看经停站", "删除", "修改"};//创建item
                AlertDialog alertDialog = new AlertDialog.Builder(myActivity)
                        .setIcon(R.mipmap.ic_launcher)
                        .setItems(items, new DialogInterface.OnClickListener() {//添加列表
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0://查看经停站
                                        Intent intent = new Intent(myActivity, TrainPassStationActivity.class);
                                        intent.putExtra("trainMessage",trainMessage);
                                        startActivity(intent);
                                        break;
                                    case 1://删除
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(myActivity);
                                        dialog.setMessage("确认要删除该机次吗");
                                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //删除该航班的所有信息(包括机次表，经停站表，座位表，价格表)
                                                SQLiteDatabase db = helper.getWritableDatabase();
                                                String delete_sql_1="delete from trainmessege where trainid='"+trainMessage.getTrainId()+"'";
                                                String delete_sql_2="delete from trainpassstation where trainid='"+trainMessage.getTrainId()+"'";
                                                String delete_sql_3="delete from trainseat where trainid='"+trainMessage.getTrainId()+"'";
                                                String delete_sql_4="delete from trainticketprice where trainid='"+trainMessage.getTrainId()+"'";
                                                db.execSQL(delete_sql_1);
                                                db.execSQL(delete_sql_2);
                                                db.execSQL(delete_sql_3);
                                                db.execSQL(delete_sql_4);
                                                Toast.makeText(myActivity, "删除成功", Toast.LENGTH_SHORT).show();
                                                loadData();
                                            }
                                        });
                                        dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.show();
                                        break;
                                    case 2://修改
                                        Intent intent1 = new Intent(myActivity, AddTrainMessageActivity.class);
                                        intent1.putExtra("trainMessage",trainMessage);
                                        startActivityForResult(intent1,100);
                                        break;
                                }

                            }
                        })
                        .create();
                alertDialog.show();
            }

            @Override
            public void ItemLongClick(TrainMessage trainMessage) {


            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myActivity, AddTrainMessageActivity.class);
                startActivity(intent);

            }
        });
    }


    //查询
    public void loadData(){
        SQLiteDatabase db = helper.getWritableDatabase();
        list = new ArrayList<>();
        list.clear();
        //查询当前用户的所有订单
        String sql="select * from trainmessege";

        Cursor result = db.rawQuery(sql, null);
        //有订单
        while(result.moveToNext()){
            String time = result.getString(0);
            String trainId = result.getString(1);
            String trainname = result.getString(2);
            String traintype = result.getString(3);
            String startstation = result.getString(4);
            String arrivestation = result.getString(5);
            Integer carriagenumber = result.getInt(6);
            Integer seatnumber = result.getInt(7);
            TrainMessage order = new TrainMessage(time,trainId,trainname,traintype,startstation,arrivestation,carriagenumber,seatnumber);
            list.add(order);

        }

        if (list != null && list.size() > 0) {
            rvList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            trainAdapter.addItem(list);
        } else {
            rvList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }

    }



    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}

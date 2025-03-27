package com.example.airlineticketing.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airlineticketing.R;
import com.example.airlineticketing.adapter.TrainPassStationAdapter;
import com.example.airlineticketing.bean.TrainMessage;
import com.example.airlineticketing.bean.TrainPassStation;
import com.example.airlineticketing.util.MySqliteOpenHelper;
import com.example.airlineticketing.widget.ActionBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * 经停站
 */

public class TrainPassStationActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private ActionBar mTitleBar;//标题栏
    private Activity myActivity;
    private LinearLayout llEmpty;
    private RecyclerView rvList;
    private TrainPassStationAdapter trainPassStationAdapter;
    private FloatingActionButton btn_add;
    private List<TrainPassStation> list = null;
    private TrainMessage trainMessage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        setContentView(R.layout.activity_trainpassstation);
        helper = new MySqliteOpenHelper(myActivity);
        llEmpty = findViewById(R.id.ll_empty);
        rvList = findViewById(R.id.rv_list);
        btn_add = findViewById(R.id.btn_add);
        mTitleBar = (ActionBar) findViewById(R.id.myActionBar);
        trainMessage = (TrainMessage) getIntent().getSerializableExtra("trainMessage");
        mTitleBar.setData(myActivity, String.format("查看机次%s经停站", trainMessage.getTrainId()), R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }

            @Override
            public void onTitleClick(TextView tvTitle) {

            }
        });
        init();
        loadData();

    }

    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(myActivity);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        trainPassStationAdapter = new TrainPassStationAdapter();
        //=2.3、设置recyclerView的适配器
        rvList.setAdapter(trainPassStationAdapter);
        trainPassStationAdapter.setItemListener(new TrainPassStationAdapter.ItemListener() {
            @Override
            public void ItemClick(TrainPassStation trainPassStation) {
                Intent intent = new Intent(myActivity, AddTrainPassStationActivity.class);
                intent.putExtra("trainid", trainMessage.getTrainId());
                intent.putExtra("trainPassStation", trainPassStation);
                startActivity(intent);
            }

            @Override
            public void ItemLongClick(TrainPassStation trainPassStation) {


            }

            @Override
            public void FirstClick(TrainPassStation firstTrainPassStation, TrainPassStation secondTrainPassStation) {
                SQLiteDatabase db = helper.getWritableDatabase();
                String sql = "select price from trainticketprice where trainid = ? and fromstationid = ? and tostationid =? and seattype =?";
                Cursor cursor = db.rawQuery(sql, new String[]{trainMessage.getTrainId(),
                        firstTrainPassStation.getStationid(), secondTrainPassStation.getStationid(), "头等舱"});
                int price = 0;
                boolean b = cursor.moveToFirst();
                if (b) price = cursor.getInt(0);
                AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
                View view = getLayoutInflater().inflate(R.layout.dialog_price_setting, null);
                final EditText editText = (EditText) view.findViewById(R.id.et_price);//拿到弹窗输入框
                editText.setText(String.valueOf(price));
                builder.setTitle(String.format("%s -- %s 头等舱票价",firstTrainPassStation.getStationname(),secondTrainPassStation.getStationname()))
                        .setView(view)//设置自定义布局
                        //确定按钮的点击事件
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String priceStr = editText.getText().toString().trim();
                                if (TextUtils.isEmpty(priceStr)) {
                                    Toast.makeText(myActivity, "价格不能为空", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (b){//有票价
                                    String updateSql ="update trainticketprice set price =? where trainid = ? and fromstationid = ? and tostationid =? and seattype =? ";
                                    db.execSQL(updateSql,new Object[]{priceStr,trainMessage.getTrainId(),
                                            firstTrainPassStation.getStationid(), secondTrainPassStation.getStationid(), "头等舱"});
                                }{//无票价
                                    String insertSql ="insert into trainticketprice(trainid,fromstationname,tostationname,fromstationid,tostationid,seattype,price)values(?,?,?,?,?,?,?)";
                                    db.execSQL(insertSql,new Object[]{trainMessage.getTrainId(),firstTrainPassStation.getStationname(),secondTrainPassStation.getStationname(),
                                            firstTrainPassStation.getStationid(), secondTrainPassStation.getStationid(), "头等舱",priceStr});
                                }
                                Toast.makeText(myActivity, "设置成功", Toast.LENGTH_SHORT).show();
                            }
                        })
                        //设置取消按钮的点击事件
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        //弹窗消失的监听事件
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        })
                        .create();
                builder.show();
            }

            @Override
            public void SecondClick(TrainPassStation firstTrainPassStation, TrainPassStation secondTrainPassStation) {
                SQLiteDatabase db = helper.getWritableDatabase();
                String sql = "select price from trainticketprice where trainid = ? and fromstationid = ? and tostationid =? and seattype =?";
                Cursor cursor = db.rawQuery(sql, new String[]{trainMessage.getTrainId(),
                        firstTrainPassStation.getStationid(), secondTrainPassStation.getStationid(), "经济舱"});
                int price = 0;
                boolean b = cursor.moveToFirst();
                if (b) price = cursor.getInt(0);
                AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
                View view = getLayoutInflater().inflate(R.layout.dialog_price_setting, null);
                final EditText editText = (EditText) view.findViewById(R.id.et_price);//拿到弹窗输入框
                editText.setText(String.valueOf(price));
                builder.setTitle(String.format("%s -- %s 经济舱票价",firstTrainPassStation.getStationname(),secondTrainPassStation.getStationname()))
                        .setView(view)//设置自定义布局
                        //确定按钮的点击事件
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String priceStr = editText.getText().toString().trim();
                                if (TextUtils.isEmpty(priceStr)) {
                                    Toast.makeText(myActivity, "价格不能为空", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (b){//有票价
                                    String updateSql ="update trainticketprice set price =? where trainid = ? and fromstationid = ? and tostationid =? and seattype =? ";
                                    db.execSQL(updateSql,new Object[]{priceStr,trainMessage.getTrainId(),
                                            firstTrainPassStation.getStationid(), secondTrainPassStation.getStationid(), "经济舱"});
                                }{//无票价
                                    String insertSql ="insert into trainticketprice(trainid,fromstationname,tostationname,fromstationid,tostationid,seattype,price)values(?,?,?,?,?,?,?)";
                                    db.execSQL(insertSql,new Object[]{trainMessage.getTrainId(),firstTrainPassStation.getStationname(),secondTrainPassStation.getStationname(),
                                            firstTrainPassStation.getStationid(), secondTrainPassStation.getStationid(), "经济舱",priceStr});
                                }
                                Toast.makeText(myActivity, "设置成功", Toast.LENGTH_SHORT).show();
                            }
                        })
                        //设置取消按钮的点击事件
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        //弹窗消失的监听事件
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        })
                        .create();
                builder.show();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myActivity, AddTrainPassStationActivity.class);
                intent.putExtra("trainid", trainMessage.getTrainId());
                startActivity(intent);

            }
        });
    }


    //查询
    public void loadData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        list = new ArrayList<>();
        list.clear();
        //查询当前用户的所有订单
        String sql = "select * from trainpassstation where trainId =?";

        Cursor result = db.rawQuery(sql, new String[]{trainMessage.getTrainId()});
        while (result.moveToNext()) {
            String time = result.getString(0);
            String trainId = result.getString(1);
            String cityname = result.getString(2);
            String stationname = result.getString(3);
            String stationid = result.getString(4);
            String arrivetime = result.getString(5);
            String staytime = result.getString(6);
            String leavetime = result.getString(7);
            TrainPassStation trainPassStation = new TrainPassStation(time, trainId, cityname, stationname, stationid, arrivetime, staytime, leavetime);
            list.add(trainPassStation);

        }

        if (list != null && list.size() > 0) {
            rvList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            trainPassStationAdapter.addItem(list);
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

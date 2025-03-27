package com.example.airlineticketing.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airlineticketing.R;
import com.example.airlineticketing.adapter.TrainBuyAdapter;
import com.example.airlineticketing.bean.TrainVo;
import com.example.airlineticketing.util.MySqliteOpenHelper;
import com.example.airlineticketing.util.SPUtils;
import com.example.airlineticketing.widget.ActionBar;
import com.lljjcoder.style.citylist.Toast.ToastUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.widget.GridLayout.HORIZONTAL;

/**
 * 机票查询
 */
public class QueryActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity activity;
    private ActionBar mTitleBar;//标题栏
    private LinearLayout llEmpty;
    private RecyclerView rvList;
    private TrainBuyAdapter trainBuyAdapter;
    private String start_city_name;
    private String arrive_city_name;
    private String time;
    private String mDate;
    private Boolean only;
    private TextView time_view;
    private Calendar currentDate;
    private List<TrainVo> list = null;
    private String transitionName;
    private Integer userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_query);
        time_view = findViewById(R.id.time_view);
        mTitleBar = findViewById(R.id.myActionBar);
        rvList = findViewById(R.id.rv_list);
        llEmpty = findViewById(R.id.ll_empty);
        helper = new MySqliteOpenHelper(activity);
        start_city_name = getIntent().getStringExtra("start_city_name");
        arrive_city_name = getIntent().getStringExtra("arrive_city_name");
        time = getIntent().getStringExtra("time");
        mDate = getIntent().getStringExtra("date");
        only = getIntent().getBooleanExtra("only", false);
        userId = (Integer) SPUtils.get(this, SPUtils.USER_ID, 0);
        time_view.setText(time);
        mTitleBar.setData(activity, start_city_name + " <> " + arrive_city_name, R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }

            @Override
            public void onTitleClick(TextView tvTitle) {
                transitionName = start_city_name;
                start_city_name = arrive_city_name;
                arrive_city_name = transitionName;
                tvTitle.setText(start_city_name + " <> " + arrive_city_name);
                loadData();
            }
        });
        init();
        loadData();
        //点击后弹出日期选择器
        time_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //获取当前时间
                currentDate = Calendar.getInstance(Locale.CHINA);

                //弹出日期选择器
                new DatePickerDialog(QueryActivity.this, 0, new DatePickerDialog.OnDateSetListener() {
                    // 绑定监听器(How the parent is notified that the date is set.)
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // 此处得到选择的时间，可以进行你想要的操作


                        //根据日期计算周几
                        int week = DateToWeek(year, monthOfYear + 1, dayOfMonth);
                        String nums[] = {"日", "一", "二", "三", "四", "五", "六"};
                        String date = (monthOfYear + 1) + "月" + dayOfMonth + "日" + "   " + "周" + nums[week - 1];
                        mDate= String.format("%s/%s/%s",year,(monthOfYear + 1),dayOfMonth);
                        time_view.setText(date);

                        loadData();

                    }

                }
                        // 设置初始日期
                        , currentDate.get(Calendar.YEAR)
                        , currentDate.get(Calendar.MONTH)
                        , currentDate.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

    }

    private void init() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(QueryActivity.this);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        trainBuyAdapter = new TrainBuyAdapter();
        //=2.3、设置recyclerView的适配器
        rvList.setAdapter(trainBuyAdapter);
        trainBuyAdapter.setItemListener(new TrainBuyAdapter.ItemListener() {
            @Override
            public void ItemClick(TrainVo trainVo) {

            }

            @SuppressLint("WrongConstant")
            @Override
            public void FirstBuy(TrainVo trainVo) {
                //首先判断是否有余票
                if (trainVo.getFirstSeatNumber() <= 0) {
                    //没有余票
                    ToastUtils.showShortToast(QueryActivity.this, "没有余票了");
                } else {
                    //有余票
                    //弹出选择框让用户选择座位

                    //建立选择器
                    final RadioGroup seat_position = new RadioGroup(QueryActivity.this);
                    seat_position.setOrientation(HORIZONTAL);
                    RadioButton AButton = new RadioButton(QueryActivity.this);
                    AButton.setText("A");
                    AButton.setId(R.id.AButton);
                    AButton.setChecked(true);
                    RadioButton BButton = new RadioButton(QueryActivity.this);
                    BButton.setId(R.id.BButton);
                    BButton.setText("B");
                    RadioButton CButton = new RadioButton(QueryActivity.this);
                    CButton.setId(R.id.CButton);
                    CButton.setText("C");
                    RadioButton DButton = new RadioButton(QueryActivity.this);
                    DButton.setId(R.id.DButton);
                    DButton.setText("D");
                    RadioButton FButton = new RadioButton(QueryActivity.this);
                    FButton.setId(R.id.FButton);
                    FButton.setText("F");

                    seat_position.addView(AButton);
                    seat_position.addView(BButton);
                    seat_position.addView(CButton);
                    seat_position.addView(DButton);
                    seat_position.addView(FButton);


                    AlertDialog.Builder adb = new AlertDialog.Builder(QueryActivity.this);
                    adb.setTitle("请选择座位的位置");
                    adb.setView(seat_position);
                    adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //点击确定后开始购票

                            SQLiteDatabase db = helper.getWritableDatabase();
                            //获取用户选择的位置
                            String position_detail = getResources().getResourceName(seat_position.getCheckedRadioButtonId());
                            String position = position_detail.substring(position_detail.length() - 7, position_detail.length() - 6);

                            //查询用户选择的位置是否还有余票
                            String FirstSeatSql0 = "select * from trainseat where trainid='" + trainVo.getTrainId() + "' and seattype='头等舱' and position='" + position + "' and (tostation<=" + trainVo.getStart_station_id() + " or fromstation>=" + trainVo.getArrive_station_id() + ")";
                            Cursor result0 = db.rawQuery(FirstSeatSql0, null);

                            //获取用户当前余额
                            String money_sql = "select * from user where id='" + userId + "'";
                            Cursor money_result = db.rawQuery(money_sql, null);
                            money_result.moveToFirst();
                            int old_money = money_result.getInt(7);
                            if (old_money >= trainVo.getSecondSeatPrice()) {
                                if (result0.getCount() > 0) {

                                    //所选类型的座位还有余票,在该类型的座位中随机选一个

                                    //首先在可以购票的座次之间随机选择一个座次,产生一个1-FirstSeatNumber的随机数,代表选择第几个座位
                                    Random random = new Random();
                                    int rn = random.nextInt(result0.getCount()) + 1;


                                    String FirstSeatSql = "select * from trainseat where trainid='" + trainVo.getTrainId() + "' and seattype='头等舱' and position='" + position + "' and (tostation<=" + trainVo.getStart_station_id() + " or fromstation>=" + trainVo.getArrive_station_id() + ")";
                                    Cursor fc = db.rawQuery(FirstSeatSql, null);
                                    fc.moveToFirst();

                                    int current = 1;
                                    while (current < rn) {
                                        fc.moveToNext();
                                        current++;
                                    }

                                    //获取选中座位的信息
                                    int rowid = fc.getInt(3);
                                    int from = fc.getInt(6);
                                    int to = fc.getInt(7);


                                    //修改该座位的信息
                                    int new_from = Math.min(from, trainVo.getStart_station_id());
                                    int new_to = Math.max(to, trainVo.getArrive_station_id());
                                    String updateSql1 = "update trainseat set fromstation=" + new_from + " where trainid='" + trainVo.getTrainId() + "' and seattype='头等舱' and rowid=" + rowid + " and position='" + position + "'";
                                    String updateSql2 = "update trainseat set tostation=" + new_to + " where trainid='" + trainVo.getTrainId() + "' and seattype='头等舱' and rowid=" + rowid + " and position='" + position + "'";
                                    db.execSQL(updateSql1);
                                    db.execSQL(updateSql2);


                                    //添加订单
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                    String ordertime = df.format(new Date());// 获取当前系统时间(下单时间)

                                    String orderSql = "insert into orderstable(time,userId,trainid,startstationname,leavetime,arrivestationname,arrivetime,carriageid,rowid,position,seattype,price,ordertime ) values('" + time + "','" + userId + "','" + trainVo.getTrainId() + "','" + trainVo.getStart_station_name() + "','" + trainVo.getLeaveTime() + "','" + trainVo.getArrive_station_name() + "','" + trainVo.getArriveTime() + "',1," + rowid + ",'" + position + "','头等舱'," + trainVo.getFirstSeatPrice() + ",'" + ordertime + "')";
                                    db.execSQL(orderSql);


                                    //扣除用户余额

                                    //更改余额
                                    double new_money = old_money - trainVo.getFirstSeatPrice();
                                    String new_money_sql = "update user set balance=" + new_money + " where id='" + userId + "'";
                                    db.execSQL(new_money_sql);


                                    //提示用户购买成功
                                    AlertDialog.Builder adb2 = new AlertDialog.Builder(QueryActivity.this);
                                    adb2.setTitle("购买成功提醒");
                                    adb2.setMessage("恭喜你成功购买了" + time + "从" + trainVo.getStart_station_name() + "开往" + trainVo.getArrive_station_name() + "的" + trainVo.getTrainId() + "次航班头等舱,您的座位为1机厢" + rowid + position);
                                    adb2.show();

                                } else {

                                    //所选座位无余票,在所有座位中随机选一个

                                    //首先在可以购票的座次之间随机选择一个座次,产生一个1-FirstSeatNumber的随机数,代表选择第几个座位
                                    Random random = new Random();
                                    int rn = random.nextInt(trainVo.getFirstSeatNumber()) + 1;


                                    String FirstSeatSql = "select * from trainseat where trainid='" + trainVo.getTrainId() + "' and seattype='头等舱' and (tostation<=" + trainVo.getStart_station_id() + " or fromstation>=" + trainVo.getArrive_station_id() + ")";
                                    Cursor fc = db.rawQuery(FirstSeatSql, null);
                                    fc.moveToFirst();


                                    int current = 1;
                                    while (current < rn) {
                                        fc.moveToNext();
                                        current++;
                                    }

                                    //获取选中座位的信息
                                    int rowid = fc.getInt(3);
                                    String position2 = fc.getString(4);
                                    int from = fc.getInt(6);
                                    int to = fc.getInt(7);


                                    //修改该座位的信息
                                    int new_from = Math.min(from, trainVo.getStart_station_id());
                                    int new_to = Math.max(to, trainVo.getArrive_station_id());
                                    String updateSql1 = "update trainseat set fromstation=" + new_from + " where trainid='" + trainVo.getTrainId() + "' and seattype='头等舱' and rowid=" + rowid + " and position='" + position2 + "'";
                                    String updateSql2 = "update trainseat set tostation=" + new_to + " where trainid='" + trainVo.getTrainId() + "' and seattype='头等舱' and rowid=" + rowid + " and position='" + position2 + "'";
                                    db.execSQL(updateSql1);
                                    db.execSQL(updateSql2);


                                    //添加订单
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                    String ordertime = df.format(new Date());// 获取当前系统时间(下单时间)

                                    String orderSql = "insert into orderstable(time,userId,trainid,startstationname,leavetime,arrivestationname,arrivetime,carriageid,rowid,position,seattype,price,ordertime ) values('" + time + "','" + userId + "','" + trainVo.getTrainId() + "','" + trainVo.getStart_station_name() + "','" + trainVo.getLeaveTime() + "','" + trainVo.getArrive_station_name() + "','" + trainVo.getArriveTime() + "',1," + rowid + ",'" + position2 + "','头等舱'," + trainVo.getFirstSeatPrice() + ",'" + ordertime + "')";
                                    db.execSQL(orderSql);


                                    //扣除用户余额

                                    //更改余额
                                    double new_money = old_money - trainVo.getFirstSeatPrice();
                                    String new_money_sql = "update user set balance=" + new_money + " where id='" + userId + "'";
                                    db.execSQL(new_money_sql);


                                    //提示用户购买成功
                                    AlertDialog.Builder adb2 = new AlertDialog.Builder(QueryActivity.this);
                                    adb2.setTitle("购买成功提醒");
                                    adb2.setMessage("恭喜你成功购买了" + time + "从" + trainVo.getStart_station_name() + "开往" + trainVo.getArrive_station_name() + "的" + trainVo.getTrainId() + "次航班头等舱,您的座位为1机厢" + rowid + position2);
                                    adb2.show();

                                }

                            } else {
                                ToastUtils.showShortToast(QueryActivity.this, "余额不足");
                            }

                        }
                    });
                    adb.setNegativeButton("取消", null);
                    adb.show();

                }

            }

            @SuppressLint("WrongConstant")
            @Override
            public void SecondBuy(TrainVo trainVo) {

                //首先判断是否有余票
                if (trainVo.getSecondSeatNumber() <= 0) {
                    //没有余票
                    ToastUtils.showShortToast(QueryActivity.this, "没有余票了");
                } else {
                    //有余票


                    //弹出选择框让用户选择座位

                    //建立选择器
                    final RadioGroup seat_position = new RadioGroup(QueryActivity.this);
                    seat_position.setOrientation(HORIZONTAL);

                    RadioButton AButton = new RadioButton(QueryActivity.this);
                    AButton.setText("A");
                    AButton.setId(R.id.AButton);
                    RadioButton BButton = new RadioButton(QueryActivity.this);
                    BButton.setId(R.id.BButton);
                    BButton.setText("B");
                    RadioButton CButton = new RadioButton(QueryActivity.this);
                    CButton.setId(R.id.CButton);
                    CButton.setText("C");
                    RadioButton DButton = new RadioButton(QueryActivity.this);
                    DButton.setId(R.id.DButton);
                    DButton.setText("D");
                    RadioButton FButton = new RadioButton(QueryActivity.this);
                    FButton.setId(R.id.FButton);
                    FButton.setText("F");


                    seat_position.addView(AButton);
                    seat_position.addView(BButton);
                    seat_position.addView(CButton);
                    seat_position.addView(DButton);
                    seat_position.addView(FButton);


                    AlertDialog.Builder adb = new AlertDialog.Builder(QueryActivity.this);
                    adb.setTitle("请选择座位的位置");
                    adb.setView(seat_position);
                    adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //点击确定后开始购票

                            SQLiteDatabase db = helper.getWritableDatabase();

                            //获取用户选择的位置
                            String position_detail = getResources().getResourceName(seat_position.getCheckedRadioButtonId());
                            String position = position_detail.substring(position_detail.length() - 7, position_detail.length() - 6);

                            //查询用户选择的位置是否还有余票
                            String FirstSeatSql0 = "select * from trainseat where trainid='" + trainVo.getTrainId() + "' and seattype='经济舱' and position='" + position + "' and (tostation<=" + trainVo.getStart_station_id() + " or fromstation>=" + trainVo.getArrive_station_id() + ")";
                            Cursor result0 = db.rawQuery(FirstSeatSql0, null);


                            //获取用户当前余额
                            String money_sql = "select * from user where id='" + userId + "'";
                            Cursor money_result = db.rawQuery(money_sql, null);
                            money_result.moveToFirst();
                            int old_money = money_result.getInt(7);
                            if (old_money >= trainVo.getSecondSeatPrice()) {
                                if (result0.getCount() > 0) {

                                    //所选类型的座位还有余票,在该类型的座位中随机选一个


                                    //首先在可以购票的座次之间随机选择一个座次,产生一个1-FirstSeatNumber的随机数,代表选择第几个座位
                                    Random random = new Random();
                                    int rn = random.nextInt(result0.getCount()) + 1;


                                    String FirstSeatSql = "select * from trainseat where trainid='" + trainVo.getTrainId() + "' and seattype='经济舱' and position='" + position + "' and (tostation<=" + trainVo.getStart_station_id() + " or fromstation>=" + trainVo.getArrive_station_id() + ")";
                                    Cursor fc = db.rawQuery(FirstSeatSql, null);
                                    fc.moveToFirst();

                                    int current = 1;
                                    while (current < rn) {
                                        fc.moveToNext();
                                        current++;
                                    }

                                    //获取选中座位的信息
                                    int rowid = fc.getInt(3);
                                    int from = fc.getInt(6);
                                    int to = fc.getInt(7);


                                    //修改该座位的信息
                                    int new_from = Math.min(from, trainVo.getStart_station_id());
                                    int new_to = Math.max(to, trainVo.getArrive_station_id());
                                    String updateSql1 = "update trainseat set fromstation=" + new_from + " where trainid='" + trainVo.getTrainId() + "' and seattype='经济舱' and rowid=" + rowid + " and position='" + position + "'";
                                    String updateSql2 = "update trainseat set tostation=" + new_to + " where trainid='" + trainVo.getTrainId() + "' and seattype='经济舱' and rowid=" + rowid + " and position='" + position + "'";
                                    db.execSQL(updateSql1);
                                    db.execSQL(updateSql2);


                                    //添加订单
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                    String ordertime = df.format(new Date());// 获取当前系统时间(下单时间)

                                    String orderSql = "insert into orderstable(time,userId,trainid,startstationname,leavetime,arrivestationname,arrivetime,carriageid,rowid,position,seattype,price,ordertime ) values('" + time + "','" + userId + "','" + trainVo.getTrainId() + "','" + trainVo.getStart_station_name() + "','" + trainVo.getLeaveTime() + "','" + trainVo.getArrive_station_name() + "','" + trainVo.getArriveTime() + "',2," + rowid + ",'" + position + "','经济舱'," + trainVo.getSecondSeatPrice() + ",'" + ordertime + "')";
                                    db.execSQL(orderSql);


                                    //扣除用户余额


                                    //更改余额
                                    double new_money = old_money - trainVo.getSecondSeatPrice();
                                    String new_money_sql = "update user set balance=" + new_money + " where id='" + userId + "'";
                                    db.execSQL(new_money_sql);


                                    //提示用户购买成功
                                    AlertDialog.Builder adb2 = new AlertDialog.Builder(QueryActivity.this);
                                    adb2.setTitle("购买成功提醒");
                                    adb2.setMessage("恭喜你成功购买了" + time + "从" + trainVo.getStart_station_name() + "开往" + trainVo.getArrive_station_name() + "的" + trainVo.getTrainId() + "次航班经济舱,您的座位为2机厢" + rowid + position);
                                    adb2.show();


                                } else {

                                    //所选座位无余票,在所有座位中随机选一个

                                    //首先在可以购票的座次之间随机选择一个座次,产生一个1-FirstSeatNumber的随机数,代表选择第几个座位
                                    Random random = new Random();
                                    int rn = random.nextInt(trainVo.getSecondSeatNumber()) + 1;


                                    String FirstSeatSql = "select * from trainseat where trainid='" + trainVo.getTrainId() + "' and seattype='经济舱' and (tostation<=" + trainVo.getStart_station_id() + " or fromstation>=" + trainVo.getArrive_station_id() + ")";
                                    Cursor fc = db.rawQuery(FirstSeatSql, null);
                                    fc.moveToFirst();


                                    int current = 1;
                                    while (current < rn) {
                                        fc.moveToNext();
                                        current++;
                                    }

                                    //获取选中座位的信息
                                    int rowid = fc.getInt(3);
                                    String position2 = fc.getString(4);
                                    int from = fc.getInt(6);
                                    int to = fc.getInt(7);


                                    //修改该座位的信息
                                    int new_from = Math.min(from, trainVo.getStart_station_id());
                                    int new_to = Math.max(to, trainVo.getArrive_station_id());
                                    String updateSql1 = "update trainseat set fromstation=" + new_from + " where trainid='" + trainVo.getTrainId() + "' and seattype='经济舱' and rowid=" + rowid + " and position='" + position2 + "'";
                                    String updateSql2 = "update trainseat set tostation=" + new_to + " where trainid='" + trainVo.getTrainId() + "' and seattype='经济舱' and rowid=" + rowid + " and position='" + position2 + "'";
                                    db.execSQL(updateSql1);
                                    db.execSQL(updateSql2);


                                    //添加订单
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                    String ordertime = df.format(new Date());// 获取当前系统时间(下单时间)

                                    String orderSql = "insert into orderstable(time,userId,trainid,startstationname,leavetime,arrivestationname,arrivetime,carriageid,rowid,position,seattype,price,ordertime ) values('" + time + "','" + userId + "','" + trainVo.getTrainId() + "','" + trainVo.getStart_station_name() + "','" + trainVo.getLeaveTime() + "','" + trainVo.getArrive_station_name() + "','" + trainVo.getArriveTime() + "',1," + rowid + ",'" + position2 + "','经济舱'," + trainVo.getSecondSeatPrice() + ",'" + ordertime + "')";
                                    db.execSQL(orderSql);


                                    //扣除用户余额

                                    //更改余额
                                    double new_money = old_money - trainVo.getSecondSeatPrice();
                                    String new_money_sql = "update user set balance=" + new_money + " where id='" + userId + "'";
                                    db.execSQL(new_money_sql);


                                    //提示用户购买成功
                                    AlertDialog.Builder adb2 = new AlertDialog.Builder(QueryActivity.this);
                                    adb2.setTitle("购买成功提醒");
                                    adb2.setMessage("恭喜你成功购买了" + time + "从" + trainVo.getStart_station_name() + "开往" + trainVo.getArrive_station_name() + "的" + trainVo.getTrainId() + "次航班经济舱,您的座位为2机厢" + rowid + position2);
                                    adb2.show();


                                }
                            } else {
                                ToastUtils.showShortToast(QueryActivity.this, "余额不足");
                            }


                        }
                    });
                    adb.setNegativeButton("取消", null);
                    adb.show();

                }
            }
        });
    }


    //将日期转化为周几
    public int DateToWeek(int year, int month, int day) {
        String datetime = year + "-" + month + "-" + day;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK);

        return w;
    }


    //查询满足条件的飞机
    private void loadData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        list = new ArrayList<>();
        //查询符合要求的机次
        String sql = "select distinct trainid from trainpassstation as A where time ='" + mDate + "' and cityname='" + start_city_name + "' and exists(select trainid from trainpassstation as B  where B.trainid=A.trainid and B.cityname='" + arrive_city_name + "' and B.stationid>A.stationid)";
        Cursor c = db.rawQuery(sql, null);

        //判断查询结果是否为空
        if (c.getCount() == 0) {
            //没有满足条件的机次
            ToastUtils.showLongToast(QueryActivity.this, "未查询到满足条件的机次");

        } else {
            //有满足条件的机次
            c.moveToFirst();

            while (c.isAfterLast() == false) {
                String trainId = c.getString(0);

                //查询起飞信息与到达信息

                //查询起飞信息
                String StartSQL = "select * from trainpassstation where trainid='" + trainId + "' and cityname='" + start_city_name + "'";
                Cursor c2 = db.rawQuery(StartSQL, null);
                c2.moveToFirst();
                String start_station_name = c2.getString(3);
                int start_station_id = c2.getInt(4);
                String leaveTime = c2.getString(7);

                //查询到达信息
                String ArriveSQL = "select * from trainpassstation where trainid='" + trainId + "' and cityname='" + arrive_city_name + "'";
                Cursor c3 = db.rawQuery(ArriveSQL, null);
                c3.moveToFirst();
                String arrive_station_name = c3.getString(3);
                int arrive_station_id = c3.getInt(4);
                String arriveTime = c3.getString(5);


                //查询余票数

                //查询头等舱票余票数
                String FirstSeatSql = "select * from trainseat where trainid='" + trainId + "' and seattype='头等舱' and (tostation<=" + start_station_id + " or fromstation>=" + arrive_station_id + ")";
                Cursor c4 = db.rawQuery(FirstSeatSql, null);
                int firstSeatNumber = c4.getCount();

                //查询经济舱票余票数
                String SecondSeatSql = "select * from trainseat where trainid='" + trainId + "' and seattype='经济舱' and (tostation<=" + start_station_id + " or fromstation>=" + arrive_station_id + ")";
                Cursor c5 = db.rawQuery(SecondSeatSql, null);
                int secondSeatNumber = c5.getCount();


                //查询票价

                //查询头等舱的票价
                String FirstPriceSql = "select * from trainticketprice where trainid='" + trainId + "' and seattype='头等舱' and fromstationid>=" + start_station_id + " and tostationid<=" + arrive_station_id;
                Cursor c6 = db.rawQuery(FirstPriceSql, null);
                c6.moveToFirst();

                double firstSeatPrice = 0;

                while (c6.isAfterLast() == false) {
                    firstSeatPrice += c6.getDouble(6);
                    c6.moveToNext();
                }
                c6.close();

                //查询经济舱的票价
                String secondPriceSql = "select * from trainticketprice where trainid='" + trainId + "' and seattype='经济舱' and fromstationid>=" + start_station_id + " and tostationid<=" + arrive_station_id;
                Cursor c7 = db.rawQuery(secondPriceSql, null);
                c7.moveToFirst();

                double secondSeatPrice = 0;

                while (c7.isAfterLast() == false) {
                    secondSeatPrice += c7.getDouble(6);
                    c7.moveToNext();
                }
                c7.close();


                // String messege = "满足条件的机次为:" + trainId + "起飞时间为:" + leaveTime + "到达时间为:" + arriveTime + "头等舱余票为:" + firstSeatNumber + "头等舱票价为:" + firstSeatPrice;
                //Toast.makeText(QueryActivity.this, messege, Toast.LENGTH_SHORT).show();
                TrainVo trainVo = new TrainVo(time, trainId, start_station_name, start_station_id, arrive_station_name, arrive_station_id, leaveTime, arriveTime, firstSeatNumber, secondSeatNumber, firstSeatPrice, secondSeatPrice);
                list.add(trainVo);
                c.moveToNext();

            }
            c.close();

        }
        if (list != null && list.size() > 0) {
            rvList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            trainBuyAdapter.addItem(list);
        } else {
            rvList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }

    }
}

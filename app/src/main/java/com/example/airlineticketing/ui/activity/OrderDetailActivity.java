package com.example.airlineticketing.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.airlineticketing.R;
import com.example.airlineticketing.bean.Order;
import com.example.airlineticketing.util.AES;
import com.example.airlineticketing.util.MySqliteOpenHelper;
import com.example.airlineticketing.widget.ActionBar;
import com.yzq.zxinglibrary.encode.CodeCreator;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderDetailActivity extends AppCompatActivity {
    private SQLiteOpenHelper helper;
    private ActionBar mActionBar;//标题栏
    TextView order_time_view;
    TextView start_station_view;
    TextView leave_time_view;
    TextView trainid_view;
    TextView total_time_view;
    TextView arrive_station_view;
    TextView arrive_time_view;
    TextView leave_date_view;
    TextView passenger_name_view;
    TextView price_view;
    TextView seat_messege_view;
    TextView change_ticket_view;
    TextView refund_ticket_view;
    Button show_button;

    String seat_messege;


    SQLiteDatabase db;
    private Order order;


    //初始化组件
    public void initViews() {
        order_time_view = findViewById(R.id.order_time_view);
        start_station_view = findViewById(R.id.start_station_view);
        leave_time_view = findViewById(R.id.leave_time_view);
        trainid_view = findViewById(R.id.trainid_view);
        total_time_view = findViewById(R.id.total_time_view);
        arrive_station_view = findViewById(R.id.arrive_station_view);
        arrive_time_view = findViewById(R.id.arrive_time_view);
        leave_date_view = findViewById(R.id.leave_date_view);
        passenger_name_view = findViewById(R.id.passenger_name_view);
        price_view = findViewById(R.id.price_view);
        seat_messege_view = findViewById(R.id.seat_messege_view);
        change_ticket_view = findViewById(R.id.change_ticket_view);
        refund_ticket_view = findViewById(R.id.refund_ticket_view);
        show_button = findViewById(R.id.show_button);
        mActionBar = findViewById(R.id.myActionBar);
        //侧滑菜单
        mActionBar.setData(this, "订单详情", R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
              /*//点击返回按钮之后跳转到订单界面并传递用户名
                Intent intent = new Intent(OrderDetailActivity.this, OrderActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("username", passenger_name);
                intent.putExtras(bundle);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);*/
                finish();
            }

            @Override
            public void onRightClick() {
            }

            @Override
            public void onTitleClick(TextView tvTitle) {

            }
        });
    }


    //给组件设置值
    public void setValues() {

        order = (Order) getIntent().getSerializableExtra("order");

        seat_messege = order.getSeatType() + " " + order.getCarriageId() + "车 " + order.getRowId() + order.getPosition() + "号";
        order_time_view.setText("下单时间" + order.getOrderTime());
        start_station_view.setText(order.getStart_station_name());
        leave_time_view.setText(order.getLeaveTime());
        trainid_view.setText(order.getTrainId());
        total_time_view.setText(calculateTotalTime(order.getLeaveTime(), order.getArriveTime()));//total_time
        arrive_station_view.setText(order.getArrive_station_name());
        arrive_time_view.setText(order.getArriveTime());
        leave_date_view.setText("起飞时间" + order.getTime());
        passenger_name_view.setText(String.valueOf(order.getUserId()));
        price_view.setText("¥ " + order.getPrice());
        seat_messege_view.setText(seat_messege);


    }


    //根据起飞时间和到达时间计算总时间
    public String calculateTotalTime(String leavetime, String arrivetime) {

        //找出分隔符":"的位置
        int leavetime_divide = leavetime.indexOf(":");
        int arrivetime_divide = arrivetime.indexOf(":");

        //获取起飞时间和到达时间的时和分
        int leave_hour = Integer.parseInt(leavetime.substring(0, leavetime_divide));
        int leave_minute = Integer.parseInt(leavetime.substring(leavetime_divide + 1, leavetime.length()));
        int arrive_hour = Integer.parseInt(arrivetime.substring(0, arrivetime_divide));
        int arrive_minute = Integer.parseInt(arrivetime.substring(arrivetime_divide + 1, arrivetime.length()));

        int total_minute = arrive_hour * 60 + arrive_minute - (leave_hour * 60 + leave_minute);
        int hour = total_minute / 60;
        int minute = total_minute % 60;

        return hour + "小时" + minute + "分钟";

    }


    //设置改签
    public void setChangeTicket() {

        change_ticket_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出提示框
                AlertDialog.Builder adb = new AlertDialog.Builder(OrderDetailActivity.this);
                adb.setTitle("提示");
                adb.setMessage("您确定要改签吗?");
                adb.setNegativeButton("取消", null);
                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db = helper.getReadableDatabase();
                        //首先将该订单从订单表中删除
                        String deleteSql = "delete from orderstable where userId='" + order.getUserId() + "' and ordertime='" + order.getOrderTime() + "'";
                        db.execSQL(deleteSql);


                        //恢复用户余额

                        //获取用户当前余额
                        String money_sql = "select balance from user where id='" + order.getUserId() + "'";
                        Cursor money_result = db.rawQuery(money_sql, null);
                        money_result.moveToFirst();
                        double balance = money_result.getDouble(0);

                        //更改余额
                        double new_money = balance + order.getPrice();
                        String new_money_sql = "update user set balance=" + new_money + " where id='" + order.getUserId() + "'";
                        db.execSQL(new_money_sql);


                        //然后跳转到查询界面并传递相应的数据
                        Intent intent = new Intent(OrderDetailActivity.this, QueryActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("start_city_name", order.getStart_station_name().substring(0, 2));
                        bundle.putString("arrive_city_name", order.getArrive_station_name().substring(0, 2));
                        bundle.putString("time", order.getTime());
                        bundle.putBoolean("only", true);
                        intent.putExtras(bundle);

                        startActivity(intent);


                    }
                });
                adb.show();
            }
        });

    }


    //设置退票
    public void setRefundTicket() {

        refund_ticket_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出提示框
                AlertDialog.Builder adb = new AlertDialog.Builder(OrderDetailActivity.this);
                adb.setTitle("提示");
                adb.setMessage("您确定要退票吗?");
                adb.setNegativeButton("取消", null);
                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        //将该订单从订单表中删除
                        db = helper.getReadableDatabase();
                        String deleteSql = "delete from orderstable where userId='" + order.getUserId() + "' and ordertime='" + order.getOrderTime() + "'";
                        db.execSQL(deleteSql);


                        //恢复用户余额

                        //获取用户当前余额
                        String money_sql = "select balance from user where id='" + order.getUserId() + "'";
                        Cursor money_result = db.rawQuery(money_sql, null);
                        money_result.moveToFirst();
                        int old_money = money_result.getInt(0);
                        //更改余额

                        //获取当前时间
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                        String current_time = df.format(new Date());// new Date()为获取当前系统时间
                        String s[] = current_time.split("-");
                        int current_month = Integer.parseInt(s[1]);
                        int current_day = Integer.parseInt(s[2]);

                        //获取起飞时间
                        String l_date = order.getTime().substring(0, order.getTime().length() - 6);
                        String s2[] = l_date.split("月");
                        int leave_month = Integer.parseInt(s2[0]);
                        int leave_day = Integer.parseInt(s2[1]);

                        int new_money;
                        if ((leave_month * 30 + leave_day) - (current_month * 30 + current_day) >= 2) {
                            new_money = old_money + order.getPrice();
                        } else {
                            new_money = old_money + order.getPrice() * 9 / 10;
                        }


                        String new_money_sql = "update user set balance=" + new_money + " where id='" + order.getUserId() + "'";
                        db.execSQL(new_money_sql);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
                adb.show();

            }
        });

    }


    //设置显示二维码
    public void setShowQRCode() {

        show_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //点击按钮之后显示二维码


                //生成字符串并AES加密
                String s = order.getId() + order.getTime() + order.getTrainId() + order.getSeatType() +
                        order.getRowId() + order.getPosition() + order.getOrderTime();
                String ss = AES.encode("4166b0828a80cc1b37e5e3731b176f50", s);


                //生成二维码
                Bitmap bitmap = CodeCreator.createQRCode(ss, 600, 600, null);
                ImageView qrimage = new ImageView(OrderDetailActivity.this);
                qrimage.setImageBitmap(bitmap);

                AlertDialog.Builder adb = new AlertDialog.Builder(OrderDetailActivity.this);
                adb.setView(qrimage);
                adb.show();


            }
        });

    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        helper = new MySqliteOpenHelper(this);

        //初始化组件
        initViews();


        //给组件设置值
        setValues();

        //设置改签
        setChangeTicket();


        //设置退票
        setRefundTicket();


        //设置显示二维码
        setShowQRCode();


    }

    public void back(View view){
        finish();
    }


}

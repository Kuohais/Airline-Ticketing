package com.example.airlineticketing.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airlineticketing.R;
import com.example.airlineticketing.util.MySqliteOpenHelper;
import com.example.airlineticketing.util.SPUtils;
import com.example.airlineticketing.util.StatusBarUtil;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * 开屏页面
 */
public class OpeningActivity extends AppCompatActivity {
    private Activity myActivity;
    Workbook TrainMessege_book;
    Workbook TrainPassStation_book;
    Workbook TrainSeat_book;
    Workbook TrainTicketPrice_book;

    Sheet TrainMessege_sheet;
    Sheet TrainPassStation_sheet;
    Sheet TrainSeat_sheet;
    Sheet TrainTicketPrice_sheet;
    MySqliteOpenHelper helper = null;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        helper = new MySqliteOpenHelper(this);
        //设置页面布局
        setContentView(R.layout.activity_opening);
        try {
            initView();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    private void initView() throws IOException, JSONException {
        StatusBarUtil.setStatusBar(myActivity, true);//设置当前界面是否是全屏模式（状态栏）
        StatusBarUtil.setStatusBarLightMode(myActivity, true);//状态栏文字颜色
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
                    finish();
                    return;
                }
                Boolean isFirst = (Boolean) SPUtils.get(myActivity, SPUtils.IF_FIRST, true);
                Integer userId = (Integer) SPUtils.get(myActivity, SPUtils.USER_ID, 0);
                if (isFirst) {//第一次进来  初始化本地数据
                    SQLiteDatabase db = helper.getWritableDatabase();
                    SPUtils.put(myActivity, SPUtils.IF_FIRST, false);//第一次
                    //打开Excel文件
                    try {
                        TrainMessege_book = Workbook.getWorkbook(getAssets().open("TrainMessege.xls"));
                        TrainPassStation_book = Workbook.getWorkbook(getAssets().open("TrainPassStation.xls"));
                        TrainSeat_book = Workbook.getWorkbook(getAssets().open("TrainSeat.xls"));
                        TrainTicketPrice_book = Workbook.getWorkbook(getAssets().open("TrainTicketPrice.xls"));

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BiffException e) {
                        e.printStackTrace();
                    }
                    TrainMessege_sheet = TrainMessege_book.getSheet(0);
                    TrainPassStation_sheet = TrainPassStation_book.getSheet(0);
                    TrainSeat_sheet = TrainSeat_book.getSheet(0);
                    TrainTicketPrice_sheet = TrainTicketPrice_book.getSheet(0);
                    //插入数据
                    for (int i = 1; i < TrainMessege_sheet.getRows(); i++) {
                        String time = TrainMessege_sheet.getCell(0, i).getContents();
                        String trainid = TrainMessege_sheet.getCell(1, i).getContents();
                        String trainname = TrainMessege_sheet.getCell(2, i).getContents();
                        String traintype = TrainMessege_sheet.getCell(3, i).getContents();
                        String start_station_name = TrainMessege_sheet.getCell(4, i).getContents();
                        String arrive_station_name = TrainMessege_sheet.getCell(5, i).getContents();
                        int carriagenumber = Integer.parseInt(TrainMessege_sheet.getCell(6, i).getContents());
                        int seatnumber = Integer.parseInt(TrainMessege_sheet.getCell(7, i).getContents());
                        String sql = "insert into trainmessege values('" + time + "','" + trainid + "','" + trainname + "','" + traintype + "','" + start_station_name + "','" + arrive_station_name + "'," + carriagenumber + "," + seatnumber + ")";
                        db.execSQL(sql);
                    }
                    //插入数据
                    for (int i = 1; i < TrainPassStation_sheet.getRows(); i++) {

                        String time = TrainPassStation_sheet.getCell(0, i).getContents();
                        String trainid = TrainPassStation_sheet.getCell(1, i).getContents();
                        String cityname = TrainPassStation_sheet.getCell(2, i).getContents();
                        String stationname = TrainPassStation_sheet.getCell(3, i).getContents();
                        int stationid = Integer.parseInt(TrainPassStation_sheet.getCell(4, i).getContents());
                        String arrivetime = TrainPassStation_sheet.getCell(5, i).getContents();
                        int staytime = Integer.parseInt(TrainPassStation_sheet.getCell(6, i).getContents());
                        String leavetime = TrainPassStation_sheet.getCell(7, i).getContents();

                        String sql = "insert into trainpassstation values('" + time + "','" + trainid + "','" + cityname + "','" + stationname + "'," + stationid + ",'" + arrivetime + "'," + staytime + ",'" + leavetime + "')";
                        db.execSQL(sql);

                    }
                    //插入数据
                    for (int i = 1; i < TrainSeat_sheet.getRows(); i++) {

                        String time = TrainSeat_sheet.getCell(0, i).getContents();
                        String trainid = TrainSeat_sheet.getCell(1, i).getContents();
                        int carriageid = Integer.parseInt(TrainSeat_sheet.getCell(2, i).getContents());
                        int rowid = Integer.parseInt(TrainSeat_sheet.getCell(3, i).getContents());
                        String position = TrainSeat_sheet.getCell(4, i).getContents();
                        String seattype = TrainSeat_sheet.getCell(5, i).getContents();
                        int fromstation = Integer.parseInt(TrainSeat_sheet.getCell(6, i).getContents());
                        int tostation = Integer.parseInt(TrainSeat_sheet.getCell(7, i).getContents());

                        String sql = "insert into trainseat values('" + time + "','" + trainid + "'," + carriageid + "," + rowid + ",'" + position + "','" + seattype + "'," + fromstation + "," + tostation + ")";
                        db.execSQL(sql);

                    }
                    //插入数据
                    for (int i = 1; i < TrainTicketPrice_sheet.getRows(); i++) {

                        String trainid = TrainTicketPrice_sheet.getCell(0, i).getContents();
                        String fromstationname = TrainTicketPrice_sheet.getCell(1, i).getContents();
                        String tostationname = TrainTicketPrice_sheet.getCell(2, i).getContents();
                        int fromstationid = Integer.parseInt(TrainTicketPrice_sheet.getCell(3, i).getContents());
                        int tostationid = Integer.parseInt(TrainTicketPrice_sheet.getCell(4, i).getContents());
                        String seattype = TrainTicketPrice_sheet.getCell(5, i).getContents();
                        int price = Integer.parseInt(TrainTicketPrice_sheet.getCell(6, i).getContents());

                        String sql = "insert into trainticketprice values('" + trainid + "','" + fromstationname + "','" + tostationname + "'," + fromstationid + "," + tostationid + ",'" + seattype + "'," + price + ")";
                        db.execSQL(sql);

                    }

                }
                //两秒后跳转到主页面
                Intent intent = new Intent();
                if (userId > 0) {//已登录
                    intent.setClass(OpeningActivity.this, MainActivity.class);
                } else {
                    intent.setClass(OpeningActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 2000);
    }


    @Override
    public void onBackPressed() {

    }
}

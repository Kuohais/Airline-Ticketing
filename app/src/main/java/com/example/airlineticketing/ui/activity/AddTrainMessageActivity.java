package com.example.airlineticketing.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airlineticketing.R;
import com.example.airlineticketing.bean.TrainMessage;
import com.example.airlineticketing.enums.TrainTypeEnum;
import com.example.airlineticketing.util.MySqliteOpenHelper;
import com.example.airlineticketing.util.SPUtils;
import com.example.airlineticketing.widget.ActionBar;
import com.lljjcoder.style.citypickerview.CityPickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 添加机次
 */
public class AddTrainMessageActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity activity;
    private ActionBar mTitleBar;//标题栏
    private EditText etTrainId;
    private EditText etTrainname;
    private Spinner etTraintype;
    private EditText etStartstation;
    private EditText etArrivestation;
    private TextView time_view;
    private Integer userId;
    private TrainMessage trainMessage;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //申明对象
    CityPickerView mPicker = new CityPickerView();
    Calendar currentDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_trainmessage_add);
        helper = new MySqliteOpenHelper(this);
        userId = (Integer) SPUtils.get(activity, SPUtils.USER_ID, 0);
        etTrainId = findViewById(R.id.trainId);
        etTrainname = findViewById(R.id.trainname);
        etTraintype = findViewById(R.id.traintype);
        etStartstation = findViewById(R.id.startstation);
        etArrivestation = findViewById(R.id.arrivestation);
        time_view = findViewById(R.id.time_view);
        trainMessage = (TrainMessage) getIntent().getSerializableExtra("trainMessage");
        if (trainMessage != null) {
            etTrainId.setText(trainMessage.getTrainId());
            etTrainname.setText(trainMessage.getTrainname());
            etTraintype.setSelection(TrainTypeEnum.getCode(trainMessage.getTraintype()));
            etStartstation.setText(trainMessage.getStartstation());
            etArrivestation.setText(trainMessage.getArrivestation());
            time_view.setText(trainMessage.getTime());
            etTrainId.setEnabled(false);
        }
        mTitleBar = (ActionBar) findViewById(R.id.myActionBar);
        mTitleBar.setData(activity, "编辑机次", R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
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
        time_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //获取当前时间
                currentDate = Calendar.getInstance(Locale.CHINA);

                //弹出日期选择器
                new DatePickerDialog(AddTrainMessageActivity.this, 0, new DatePickerDialog.OnDateSetListener() {
                    // 绑定监听器(How the parent is notified that the date is set.)
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // 此处得到选择的时间，可以进行你想要的操作
                        time_view.setText(String.format("%s/%s/%s", year, (monthOfYear + 1), dayOfMonth));

                    }

                }
                        // 设置初始日期
                        , currentDate.get(Calendar.YEAR)
                        , currentDate.get(Calendar.MONTH)
                        , currentDate.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }

    //保存信息
    public void save(View v) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String trainId = etTrainId.getText().toString();
        String trainname = etTrainname.getText().toString();
        String time = time_view.getText().toString();
        String traintype = etTraintype.getSelectedItem().toString();
        String startstation = etStartstation.getText().toString();
        String arrivestation = etArrivestation.getText().toString();

        if ("".equals(trainId)) {
            Toast.makeText(activity, "机次号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(trainname)) {
            Toast.makeText(activity, "机次名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("请选择".equals(time)) {
            Toast.makeText(activity, "请选择机次日期", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(startstation)) {
            Toast.makeText(activity, "起始站不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(arrivestation)) {
            Toast.makeText(activity, "终点站不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String sql = "select * from trainmessege where trainid =?";

        Cursor result = db.rawQuery(sql, new String[]{trainId});
        TrainMessage dbTrainMessage = null;
        //有订单
        while (result.moveToNext()) {
            String dbTime = result.getString(0);
            String dbTrainId = result.getString(1);
            String dbTrainname = result.getString(2);
            String dbTraintype = result.getString(3);
            String dbStartstation = result.getString(4);
            String dbArrivestation = result.getString(5);
            Integer dbCarriagenumber = result.getInt(6);
            Integer dbSeatnumber = result.getInt(7);
            dbTrainMessage = new TrainMessage(dbTime, dbTrainId, dbTrainname, dbTraintype, dbStartstation, dbArrivestation, dbCarriagenumber, dbSeatnumber);
        }

        if (trainMessage == null) {
            if (dbTrainMessage == null) {
                String insertSql = "insert into trainmessege(time,trainid,trainname,traintype,startstation,arrivestation,carriagenumber,seatnumber)values(?,?,?,?,?,?,?,?)";
                db.execSQL(insertSql, new Object[]{time, trainId, trainname, traintype, startstation, arrivestation, 2, 60});
                //新增座次
                String insertTrainSeat = "insert into trainseat(time,trainid,carriageid,rowid,position,seattype,fromstation,tostation)values(?,?,?,?,?,?,?,?)";
                String[] positions = new String[]{"A", "B", "C", "D", "E"};
                for (int i = 1; i <= 5; i++) {
                    for (String position : positions) {
                        db.execSQL(insertTrainSeat, new Object[]{time, trainId, 1, i, position, "头等舱", 0, 0});
                    }
                }
                for (int i = 1; i <= 5; i++) {
                    for (String position : positions) {
                        db.execSQL(insertTrainSeat, new Object[]{time, trainId, 2, i, position, "经济舱", 0, 0});
                    }
                }
                //新增经停站
                String insertTrainpassstationSql = "insert into trainpassstation(time,trainid,cityname,stationname,stationid,arrivetime,staytime,leavetime)values(?,?,?,?,?,?,?,?)";
                db.execSQL(insertTrainpassstationSql, new Object[]{time, trainId, trainname, trainname, 1, "", "", ""});
                finish();
            } else {
                Toast.makeText(activity, "该机次号已存在", Toast.LENGTH_SHORT).show();
            }
        } else {
            String updateSql = "update trainmessege set time=?,trainname=?,traintype=?,startstation=?,arrivestation=? where trainId =?";
            db.execSQL(updateSql, new Object[]{time,trainname, traintype, startstation, arrivestation, trainId});
            finish();
        }
    }
}

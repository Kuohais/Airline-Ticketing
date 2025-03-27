package com.example.airlineticketing.ui.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airlineticketing.R;
import com.example.airlineticketing.bean.TrainPassStation;
import com.example.airlineticketing.util.MySqliteOpenHelper;
import com.example.airlineticketing.util.SPUtils;
import com.example.airlineticketing.widget.ActionBar;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 添加经停站
 */
public class AddTrainPassStationActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity activity;
    private ActionBar mTitleBar;//标题栏
    private EditText etCityname;
    private EditText etStationname;
    private EditText etArrivetime;
    private EditText etStaytime;
    private EditText etLeavetime;
    private Integer userId;
    private TrainPassStation trainPassStation;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String trainid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_trainpassstation_add);
        helper = new MySqliteOpenHelper(this);
        userId = (Integer) SPUtils.get(activity, SPUtils.USER_ID, 0);
        etCityname = findViewById(R.id.cityname);
        etStationname = findViewById(R.id.stationname);
        etArrivetime = findViewById(R.id.arrivetime);
        etStaytime = findViewById(R.id.staytime);
        etLeavetime = findViewById(R.id.leavetime);
        trainid = getIntent().getStringExtra("trainid");
        trainPassStation = (TrainPassStation) getIntent().getSerializableExtra("trainPassStation");
        if (trainPassStation != null) {
            etCityname.setText(trainPassStation.getCityname());
            etStationname.setText(trainPassStation.getStationname());
            etArrivetime.setText(trainPassStation.getArrivetime());
            etStaytime.setText(trainPassStation.getStaytime());
            etLeavetime.setText(trainPassStation.getLeavetime());
        }
        mTitleBar = (ActionBar) findViewById(R.id.myActionBar);
        mTitleBar.setData(activity, String.format("编辑机次%s经停站",trainid), R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
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
    }

    //保存信息
    public void save(View v) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String cityname = etCityname.getText().toString();
        String stationname = etStationname.getText().toString();
        String arrivetime = etArrivetime.getText().toString();
        String staytime = etStaytime.getText().toString();
        String leavetime = etLeavetime.getText().toString();

        if ("".equals(cityname)) {
            Toast.makeText(activity, "城市名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(stationname)) {
            Toast.makeText(activity, "站点名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(arrivetime)) {
            Toast.makeText(activity, "到达时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(staytime)) {
            Toast.makeText(activity, "停留时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(leavetime)) {
            Toast.makeText(activity, "离开时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String sql = "select count(*) from trainpassstation where trainid =?";
        int stationid = 0;
        Cursor result = db.rawQuery(sql, new String[]{trainid});
        result.moveToFirst();
        stationid = result.getInt(0);

        if (trainPassStation == null) {
            String insertSql = "insert into trainpassstation(time,trainid,cityname,stationname,stationid,arrivetime,staytime,leavetime)values(?,?,?,?,?,?,?,?)";
            db.execSQL(insertSql, new Object[]{sf.format(new Date()), trainid, cityname, stationname, stationid + 1, arrivetime, staytime, leavetime});



        } else {
            String updateSql = "update trainpassstation set cityname=?,stationname=?,arrivetime=?,staytime=?,leavetime=? where time =? and trainid=?";
            db.execSQL(updateSql, new Object[]{cityname, stationname, arrivetime, staytime, leavetime, trainPassStation.getTime(), trainPassStation.getTrainid()});

        }
        Toast.makeText(activity, "保存成功", Toast.LENGTH_SHORT).show();
        finish();
    }
}

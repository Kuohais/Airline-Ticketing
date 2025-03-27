package com.example.airlineticketing.ui.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airlineticketing.R;
import com.example.airlineticketing.bean.User;
import com.example.airlineticketing.util.MySqliteOpenHelper;
import com.example.airlineticketing.util.SPUtils;
import com.example.airlineticketing.widget.ActionBar;

/**
 * 个人信息
 */
public class PersonActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity mActivity;
    private ActionBar mTitleBar;//标题栏
    private TextView tvAccount;
    private TextView etNickName;
    private TextView etNumber;
    private TextView etEmail;
    private Button btnSave;//保存
    private Integer userId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        mActivity = this;
        helper = new MySqliteOpenHelper(this);
        userId = (Integer) SPUtils.get(mActivity,SPUtils.USER_ID,0);
        tvAccount = findViewById(R.id.tv_account);
        etNickName = findViewById(R.id.tv_nickName);
        etNumber = findViewById(R.id.tv_age);
        etEmail = findViewById(R.id.tv_email);
        btnSave = findViewById(R.id.btn_save);
        mTitleBar = (ActionBar) findViewById(R.id.myActionBar);
        mTitleBar.setData(mActivity,"个人信息", R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
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
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        User user = null;
        String sql = "select * from user where id = ?";
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                String dbAccount = cursor.getString(1);
                String dbPassword = cursor.getString(2);
                String dbNickName = cursor.getString(3);
                String dbNumber = cursor.getString(4);
                String dbEmail = cursor.getString(5);
                Integer dbUserType = cursor.getInt(6);
                Double dbBalance = cursor.getDouble(7);
                user = new User(dbId, dbAccount,dbPassword,dbNickName,dbNumber,dbEmail,dbUserType,dbBalance);
            }
        }
        db.close();
        if (user != null) {
            tvAccount.setText(user.getAccount());
            etNickName.setText(user.getNickName());
            etNumber.setText(String.valueOf(user.getNumber()));
            etEmail.setText(user.getEmail());
        }
        //保存
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getWritableDatabase();
                String nickName = etNickName.getText().toString();
                String number = etNumber.getText().toString();
                String email = etEmail.getText().toString();
                if ("".equals(nickName)) {
                    Toast.makeText(mActivity,"昵称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("".equals(number)) {
                    Toast.makeText(mActivity,"手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("".equals(email)) {
                    Toast.makeText(mActivity,"地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String updateSql = "update user set nickName=?,number=?,email=? where id =?";
                db.execSQL(updateSql,new Object[]{nickName,number,email,userId});
                db.close();
                Toast.makeText(mActivity,"保存成功", Toast.LENGTH_SHORT).show();
                finish();//关闭页面
            }
        });
    }

}

package com.example.airlineticketing.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airlineticketing.R;
import com.example.airlineticketing.bean.User;
import com.example.airlineticketing.util.MySqliteOpenHelper;
import com.example.airlineticketing.util.SPUtils;
import com.example.airlineticketing.widget.ActionBar;

/**
 * 我的钱包
 */
public class BalanceActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity activity;
    private ActionBar mTitleBar;//标题栏
    private TextView tv_wallet;
    private Button btn_pay;
    private Button btn_withdraw;
    private Integer userId;
    private User mUser = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity =this;
        setContentView(R.layout.activity_balance);
        helper = new MySqliteOpenHelper(activity);
        tv_wallet = findViewById(R.id.tv_wallet);
        btn_pay = findViewById(R.id.btn_pay);
        btn_withdraw = findViewById(R.id.btn_withdraw);
        mTitleBar = (ActionBar) findViewById(R.id.myActionBar);
        mTitleBar.setData(activity,"我的钱包", R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
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
        userId = (Integer) SPUtils.get(activity,SPUtils.USER_ID,0);
        loadData();
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = helper.getWritableDatabase();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                View view1 = getLayoutInflater().inflate(R.layout.dialog_wallet, null);
                final EditText editText = (EditText) view1.findViewById(R.id.et_value);//拿到弹窗输入框
                builder.setTitle("充值金额")
                        .setView(view1)//设置自定义布局
                        //确定按钮的点击事件
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String value = editText.getText().toString().trim();
                                if (TextUtils.isEmpty(value)) {
                                    Toast.makeText(activity, "金额不能为空", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (Double.valueOf(value) < 0){
                                    Toast.makeText(activity, "充值金额不能小于0", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                db.execSQL("update user set balance = ? where id = ?", new Object[]{mUser.getBalance() + Double.valueOf(value),userId});
                                Toast.makeText(activity,"充值成功",Toast.LENGTH_SHORT).show();
                                loadData();
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
        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = helper.getWritableDatabase();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                View view1 = getLayoutInflater().inflate(R.layout.dialog_wallet, null);
                final EditText editText = (EditText) view1.findViewById(R.id.et_value);//拿到弹窗输入框
                builder.setTitle("提现金额")
                        .setView(view1)//设置自定义布局
                        //确定按钮的点击事件
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String value = editText.getText().toString().trim();
                                if (TextUtils.isEmpty(value)) {
                                    Toast.makeText(activity, "金额不能为空", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (mUser.getBalance() - Double.valueOf(value) < 0){
                                    Toast.makeText(activity, "已经超出可提现金额", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                db.execSQL("update user set balance = ? where id = ?", new Object[]{mUser.getBalance() - Double.valueOf(value),userId});
                                Toast.makeText(activity,"提现成功",Toast.LENGTH_SHORT).show();
                                loadData();
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
    }

    private void loadData(){
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from user where id = ?";
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
                mUser = new User(dbId, dbAccount,dbPassword,dbNickName,dbNumber,dbEmail,dbUserType,dbBalance);
                tv_wallet.setText(String.format("%.2f元",mUser.getBalance()));
            }
        }
    }
}

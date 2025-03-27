package com.example.airlineticketing.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airlineticketing.R;
import com.example.airlineticketing.bean.User;
import com.example.airlineticketing.util.MySqliteOpenHelper;
import com.example.airlineticketing.widget.ActionBar;

/**
 * 用户明细
 */
public class UserDetailActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private ActionBar mActionBar;//标题栏
    private Activity mActivity;
    private TextView account;
    private TextView nickName;
    private TextView number;
    private TextView email;
    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        mActivity = this;
        helper = new MySqliteOpenHelper(this);
        account = findViewById(R.id.account);
        nickName = findViewById(R.id.nickName);
        number = findViewById(R.id.age);
        email = findViewById(R.id.email);
        mActionBar = findViewById(R.id.myActionBar);
        //侧滑菜单
        mActionBar.setData(mActivity,"用户信息", R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
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
        mUser = (User) getIntent().getSerializableExtra("user");
        if (mUser != null) {
            account.setText(mUser.getAccount());
            nickName.setText(mUser.getNickName());
            number.setText(String.valueOf(mUser.getNumber()));
            email.setText(mUser.getEmail());
        }
    }

}

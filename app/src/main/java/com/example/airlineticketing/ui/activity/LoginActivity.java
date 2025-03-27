package com.example.airlineticketing.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airlineticketing.R;
import com.example.airlineticketing.bean.User;
import com.example.airlineticketing.enums.UserTypeEnum;
import com.example.airlineticketing.util.MySqliteOpenHelper;
import com.example.airlineticketing.util.SPUtils;
import com.example.airlineticketing.widget.ActionBar;

/**
 * 登录页面
 */
public class LoginActivity extends Activity {
    MySqliteOpenHelper helper = null;
    private static final String TAG = "LoginActivity";
    private Activity activity;
    private ActionBar mTitleBar;//标题栏
    private EditText etAccount;//手机号
    private EditText etPassword;//密码
    private TextView tvRegister;//注册
    private Button btnLogin;//登录按钮
    private RadioGroup rgType;//用户类型
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        helper = new MySqliteOpenHelper(this);
        setContentView(R.layout.activity_login);//加载页面
        etAccount =(EditText) findViewById(R.id.et_account);//获取手机号
        etPassword=(EditText)findViewById(R.id.et_password);//获取密码
        tvRegister=(TextView)findViewById(R.id.tv_register);//获取注册
        btnLogin=(Button)findViewById(R.id.btn_login);//获取登录
        rgType = findViewById(R.id.rg_type);
        mTitleBar = (ActionBar)findViewById(R.id.myActionBar);
        mTitleBar.setData(activity,"登录",0, 0, 0,getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
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

        //手机号注册
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到注册页面
                Intent intent=new Intent(activity, RegisterActivity.class);
                startActivity(intent);
            }
        });
        //设置点击按钮
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭虚拟键盘
                InputMethodManager inputMethodManager= (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
                //获取请求参数
                String account= etAccount.getText().toString();
                String password=etPassword.getText().toString();
                if ("".equals(account)){//账号不能为空
                    Toast.makeText(activity,"账号不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if ("".equals(password)){//密码为空
                    Toast.makeText(activity,"密码为空", Toast.LENGTH_LONG).show();
                    return;
                }
                int userType = rgType.getCheckedRadioButtonId() == R.id.rb_user? UserTypeEnum.User.getCode():UserTypeEnum.Admin.getCode();//用户类型
                User mUser = null;
                String sql = "select * from user where account = ?";
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor cursor = db.rawQuery(sql, new String[]{account});
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
                    }
                }
                db.close();
                if (mUser != null) {
                    if (userType == mUser.getUserType().intValue()){
                        if (!password.equals(mUser.getPassword())) {
                            Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                        }else{
                            SPUtils.put(LoginActivity.this,SPUtils.USER_ID,mUser.getId());
                            SPUtils.put(LoginActivity.this,SPUtils.USER_TYPE,mUser.getUserType());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "用户类型不一致", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

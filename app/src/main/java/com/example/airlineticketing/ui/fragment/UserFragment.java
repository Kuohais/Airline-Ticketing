package com.example.airlineticketing.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.airlineticketing.MyApplication;
import com.example.airlineticketing.R;
import com.example.airlineticketing.enums.UserTypeEnum;
import com.example.airlineticketing.ui.activity.AboutActivity;
import com.example.airlineticketing.ui.activity.AdminMessageActivity;
import com.example.airlineticketing.ui.activity.BalanceActivity;
import com.example.airlineticketing.ui.activity.LoginActivity;
import com.example.airlineticketing.ui.activity.PasswordActivity;
import com.example.airlineticketing.ui.activity.PersonActivity;
import com.example.airlineticketing.ui.activity.UserMessageActivity;
import com.example.airlineticketing.util.SPUtils;


/**
 * 个人中心
 */
public class UserFragment extends Fragment {
    private Activity mActivity;
    private LinearLayout llPerson;
    private LinearLayout llSecurity;
    private LinearLayout llWallet;
    private LinearLayout llMessage;
    private LinearLayout llAbout;
    private Button btnLogout;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user,container,false);
        llPerson = view.findViewById(R.id.person);
        llSecurity = view.findViewById(R.id.security);
        llWallet = view.findViewById(R.id.wallet);
        llMessage = view.findViewById(R.id.message);
        llAbout = view.findViewById(R.id.about);
        btnLogout = view.findViewById(R.id.logout);
        initView();
        return view;
    }

    private void initView() {
        //用户类型
        Integer userType = (Integer) SPUtils.get(mActivity,SPUtils.USER_TYPE,0);
        llWallet.setVisibility(userType.intValue() == UserTypeEnum.User.getCode() ? View.VISIBLE: View.GONE);
        //个人信息
        llPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面
                Intent intent = new Intent(mActivity, PersonActivity.class);
                startActivity(intent);
            }
        });
        //账号安全
        llSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面
                Intent intent = new Intent(mActivity, PasswordActivity.class);
                startActivity(intent);
            }
        });
        //我的钱包
        llWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面
                Intent intent = new Intent(mActivity, BalanceActivity.class);
                startActivity(intent);
            }
        });
        //留言反馈
        llMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面
                Intent intent;
                if (userType.intValue() == UserTypeEnum.User.getCode()){
                    intent = new Intent(mActivity, UserMessageActivity.class);
                }else {
                    intent = new Intent(mActivity, AdminMessageActivity.class);
                }
                startActivity(intent);
            }
        });
        //关于软件
        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面
                Intent intent = new Intent(mActivity, AboutActivity.class);
                startActivity(intent);
            }
        });
        //退出登录
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.Instance.getMainActivity().finish();
                SPUtils.remove(mActivity,SPUtils.USER_ID);
                SPUtils.remove(mActivity,SPUtils.USER_TYPE);
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        });
    }
}

package com.example.airlineticketing.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airlineticketing.R;
import com.example.airlineticketing.adapter.UserAdapter;
import com.example.airlineticketing.bean.User;
import com.example.airlineticketing.enums.UserTypeEnum;
import com.example.airlineticketing.ui.activity.UserDetailActivity;
import com.example.airlineticketing.util.KeyBoardUtil;
import com.example.airlineticketing.util.MySqliteOpenHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 用户管理
 */
public class UserManageFragment extends Fragment {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private LinearLayout llEmpty;
    private RecyclerView rvUserList;
    private UserAdapter mUserAdapter;
    private FloatingActionButton btnAdd;
    private EditText etQuery;//搜索内容
    private ImageView ivSearch;//搜索图标
    private List<User> userList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_manage, container, false);
        helper = new MySqliteOpenHelper(myActivity);
        rvUserList = view.findViewById(R.id.rv_user_list);
        llEmpty = view.findViewById(R.id.ll_empty);
        etQuery = view.findViewById(R.id.et_query);
        ivSearch = view.findViewById(R.id.iv_search);
        //获取控件
        initView();
        //软键盘搜索
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();//加载数据
            }
        });
        //点击软键盘中的搜索
        etQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtil.hideKeyboard(v);//隐藏软键盘
                    loadData();//加载数据
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(myActivity);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvUserList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        mUserAdapter = new UserAdapter();
        //=2.3、设置recyclerView的适配器
        rvUserList.setAdapter(mUserAdapter);
        mUserAdapter.setItemListener(new UserAdapter.ItemListener() {
            @Override
            public void ItemClick(User user) {
                Intent intent = new Intent(myActivity, UserDetailActivity.class);
                intent.putExtra("user", user);
                startActivityForResult(intent, 100);
            }

            @Override
            public void Delete(Integer id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(myActivity);
                dialog.setMessage("确认要删除该用户吗");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = helper.getWritableDatabase();
                        String sql1 = "delete from orderstable where userId=?";
                        String sql2 = "delete from user where id=?";
                        db.execSQL(sql1, new String[]{String.valueOf(id)});
                        db.execSQL(sql2, new String[]{String.valueOf(id)});
                        db.close();
                        Toast.makeText(myActivity, "删除成功", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                });
                dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        String content = etQuery.getText().toString();//获取搜索内容
        userList = new ArrayList<>();
        User mUser = null;
        String sql = "select * from user where userType =" + UserTypeEnum.User.getCode();//普通用户
        if (!"".equals(content)) {
            sql += " and nickName like ?";
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, !"".equals(content) ? new String[]{"%" + content + "%"} : null);
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
                userList.add(mUser);
            }
        }
        Collections.reverse(userList);
        if (userList != null && userList.size() > 0) {
            rvUserList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            mUserAdapter.addItem(userList);
        } else {
            rvUserList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}

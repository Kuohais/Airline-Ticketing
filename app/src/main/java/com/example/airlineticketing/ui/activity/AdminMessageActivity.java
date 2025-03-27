package com.example.airlineticketing.ui.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airlineticketing.R;
import com.example.airlineticketing.adapter.MessageAdapter;
import com.example.airlineticketing.bean.Message;
import com.example.airlineticketing.util.MySqliteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 *留言建议
 */
public class AdminMessageActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private LinearLayout llEmpty;
    private RecyclerView rvList;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_message);
        myActivity = this;
        helper = new MySqliteOpenHelper(myActivity);
        rvList = findViewById(R.id.rv_list);
        llEmpty = findViewById(R.id.ll_empty);
        //获取控件
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(myActivity);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        messageAdapter = new MessageAdapter();
        //=2.3、设置recyclerView的适配器
        rvList.setAdapter(messageAdapter);
        messageAdapter.setItemListener(new MessageAdapter.ItemListener() {
            @Override
            public void ItemClick(Message message) {

            }

            @Override
            public void ItemLongClick(Message message) {

            }
        });
        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        messageList = new ArrayList<>();
        Message message = null;
        String sql = "select m.*,u.nickName from message m,user u where m.userId = u.id order by date desc";
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
                Integer dbId = cursor.getInt(0);
                Integer dbUserId = cursor.getInt(1);
                String dbContent = cursor.getString(2);
                String dbDate = cursor.getString(3);
                String dbName = cursor.getString(4);
                message = new Message(dbId,dbUserId, dbContent, dbDate,dbName);
                messageList.add(message);
            }
        }
        if (messageList != null && messageList.size() > 0) {
            rvList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            messageAdapter.addItem(messageList);
        } else {
            rvList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }
    }
    public void back(View view){
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

}

package com.example.airlineticketing.ui.activity;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airlineticketing.R;
import com.example.airlineticketing.bean.Message;
import com.example.airlineticketing.util.MySqliteOpenHelper;
import com.example.airlineticketing.util.SPUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 编辑留言
 */
public class UserAddMessageActivity extends AppCompatActivity {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;
    private EditText etContent;
    private Message message;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
    private Integer userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message_add);
        myActivity = this;
        helper = new MySqliteOpenHelper(myActivity);
        etContent = findViewById(R.id.content);
        message = (Message) getIntent().getSerializableExtra("message");
        userId = (Integer) SPUtils.get(myActivity, SPUtils.USER_ID, 0);
        if (message != null) {
            etContent.setText(message.getContent());
        }
    }


    public void publish(View view) {
        String content = etContent.getText().toString();
        if ("".equals(content)) {
            Toast.makeText(myActivity, "请输入信息内容", Toast.LENGTH_SHORT).show();
            return;
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        if (message == null) {
            String insertSql = "insert into message(userId,content,date) values(?,?,?)";
            db.execSQL(insertSql, new Object[]{userId, content, sf.format(new Date())});
        } else {
            String updateSql = "update message set content=? where id = ?";
            db.execSQL(updateSql, new Object[]{content, message.getId()});
        }
        Toast.makeText(myActivity, "保存成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void back(View view) {
        finish();
    }

}

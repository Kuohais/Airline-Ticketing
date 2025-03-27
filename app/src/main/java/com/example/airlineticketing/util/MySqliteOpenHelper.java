package com.example.airlineticketing.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.airlineticketing.enums.UserTypeEnum;

/**
 * description :数据库管理类,负责管理数据库的创建、升级工作
 */
public class MySqliteOpenHelper extends SQLiteOpenHelper {
    //数据库名字
    public static final String DB_NAME = "airlineticketing.db";

    //数据库版本
    public static final int DB_VERSION = 1;
    private Context context;

    public MySqliteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context=context;
    }

    /**
     * 在数据库首次创建的时候调用，创建表以及可以进行一些表数据的初始化
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        //_id为主键并且自增长一般命名为_id
        //建立用户表
        db.execSQL("create table user(id integer primary key autoincrement,account,password,nickName,number,email,userType,balance)");
        //建立反馈表
        db.execSQL("create table message(id integer primary key autoincrement,userId,content,date)");
        //建立飞机机次表
        db.execSQL("CREATE TABLE IF NOT EXISTS trainmessege(time varchar(20),trainid varchar(20),trainname varchar(20),traintype varchar(20),startstation varchar(100),arrivestation varchar(100),carriagenumber int,seatnumber int)");
        //建立飞机经停站表
        db.execSQL("CREATE TABLE IF NOT EXISTS trainpassstation(time varchar(20),trainid varchar(20),cityname varchar(20),stationname varchar(20),stationid int,arrivetime varchar(20),staytime int,leavetime varchar(20))");
        //建立飞机票座次表
        db.execSQL("CREATE TABLE IF NOT EXISTS trainseat(time varchar(20),trainid varchar(20),carriageid int,rowid int,position varchar(20),seattype varchar(20),fromstation int,tostation int)");
        //建立飞机票价格表
        db.execSQL("CREATE TABLE IF NOT EXISTS trainticketprice(trainid varchar(20),fromstationname varchar(20),tostationname varchar(20),fromstationid int,tostationid int,seattype varchar(20),price int)");
        //建立订单表
        db.execSQL("CREATE TABLE IF NOT EXISTS orderstable(id integer primary key autoincrement,time varchar(20),userId,trainid varchar(20),startstationname varchar(20),leavetime varchar(20),arrivestationname varchar(20),arrivetime varchar(20),carriageid int,rowid int,position varchar(10),seattype varchar(20),price int,ordertime varchar(20))");


        //初始化管理员数据
        String insertSql = "insert into user(account,password,nickName,number,email,userType) values(?,?,?,?,?,?)";
        db.execSQL(insertSql,new Object[]{"admin","123","管理员",21,"973319261@qq.com",UserTypeEnum.Admin.getCode()});
    }
    /**
     * 数据库升级的时候回调该方法，在数据库版本号DB_VERSION升级的时候才会调用
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //给表添加一个字段
        //db.execSQL("alter table person add age integer");
    }

    /**
     * 数据库打开的时候回调该方法
     *
     * @param db
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}


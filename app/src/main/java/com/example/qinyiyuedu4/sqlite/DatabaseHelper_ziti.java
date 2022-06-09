package com.example.qinyiyuedu4.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.qinyiyuedu4.pojo.Book;


public class DatabaseHelper_ziti extends SQLiteOpenHelper {
    public DatabaseHelper_ziti(@Nullable Context context) {
        //上下文、数据库名称、游标工厂、版本号
        super(context, Book.DATABASE_NAME_ZI_TI, null, 1);
    }

    //数据库不存在时才会调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "已创建字体数据库");
        //创建表
        db.execSQL("create table "+Book.TABLE_NAME_ZI_TI +"(daxiao integer,yanse varchar,beijing varchar)");
        //加入一条初始化数据
        ContentValues values=new ContentValues();
        values.put("daxiao",20);
        values.put("yanse","#000000");
        values.put("beijing","#ffffff");
        db.insert(Book.TABLE_NAME_ZI_TI,null,values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseHelper", "升级数据库");
    }
}

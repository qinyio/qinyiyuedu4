package com.example.qinyiyuedu4.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.qinyiyuedu4.pojo.Book;


public class DatabaseHelper_shu_ji_ji_lu extends SQLiteOpenHelper {
    public DatabaseHelper_shu_ji_ji_lu(@Nullable Context context) {
        //上下文、数据库名称、游标工厂、版本号
        super(context, Book.DATABASE_NAME_SHU_JI_JI_LU, null, 1);
    }

    //数据库不存在时才会调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "已创建书籍数据库");
        //表中创建字段：书名，作者,图片地址，书籍地址,最新，简介，书签,读至
        db.execSQL("create table " + Book.TABLE_NAME_SHU_JI_JI_LU + "(name varchar,zuozhe varchar,image varchar,href varchar,zuixin varchar,jianjie varchar,shuqian integer,duzhi varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseHelper", "升级数据库");
    }
}

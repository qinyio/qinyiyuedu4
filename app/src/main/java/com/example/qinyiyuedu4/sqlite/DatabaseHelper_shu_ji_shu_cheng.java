package com.example.qinyiyuedu4.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.qinyiyuedu4.pojo.Book;


public class DatabaseHelper_shu_ji_shu_cheng extends SQLiteOpenHelper {
    public DatabaseHelper_shu_ji_shu_cheng(@Nullable Context context) {
        //上下文、数据库名称、游标工厂、版本号
        super(context, Book.DATABASE_NAME_SHU_JI_SHU_CHENG, null, 1);
    }

    //数据库不存在时才会调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "已创建书籍数据库");
        //表中创建字段：书名，作者,图片地址，书籍地址,最新，简介，书签,读至
        db.execSQL("create table " + Book.TABLE_NAME_SHU_JI_SHU_CHENG + "(name varchar,zuozhe varchar,image varchar,href varchar,zuixin varchar,jianjie varchar)");
        db.execSQL("create table " + Book.TABLE_NAME_SHU_JI_SHU_CHENG_1 + "(name varchar,zuozhe varchar,image varchar,href varchar,zuixin varchar,jianjie varchar)");
        db.execSQL("create table " + Book.TABLE_NAME_SHU_JI_SHU_CHENG_2 + "(name varchar,zuozhe varchar,image varchar,href varchar,zuixin varchar,jianjie varchar)");
        db.execSQL("create table " + Book.TABLE_NAME_SHU_JI_SHU_CHENG_3 + "(name varchar,zuozhe varchar,image varchar,href varchar,zuixin varchar,jianjie varchar)");
        db.execSQL("create table " + Book.TABLE_NAME_SHU_JI_SHU_CHENG_4 + "(name varchar,zuozhe varchar,image varchar,href varchar,zuixin varchar,jianjie varchar)");
        db.execSQL("create table " + Book.TABLE_NAME_SHU_JI_SHU_CHENG_5 + "(name varchar,zuozhe varchar,image varchar,href varchar,zuixin varchar,jianjie varchar)");
        db.execSQL("create table " + Book.TABLE_NAME_SHU_JI_SHU_CHENG_6 + "(name varchar,zuozhe varchar,image varchar,href varchar,zuixin varchar,jianjie varchar)");
        db.execSQL("create table " + Book.TABLE_NAME_SHU_JI_SHU_CHENG_7 + "(name varchar,zuozhe varchar,image varchar,href varchar,zuixin varchar,jianjie varchar)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseHelper", "升级数据库");
    }
}

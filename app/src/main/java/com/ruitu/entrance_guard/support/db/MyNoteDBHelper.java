package com.ruitu.entrance_guard.support.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wubin on 2017/3/6.
 */

public class MyNoteDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "my_notepad";//表名
    public static final String ID = "_id";//id
    //    public static final String TIME = "time";//时间
//    public static final String CONTENT = "content";//文字内容
//    public static final String IMAGE_PATH = "image_path";//图片路径
//    public static final String VIDEO_PATH = "video_path";//视频路径
    public static final String CARD_NUMBER = "card_number";

    public MyNoteDBHelper(Context context) {
        //context name cursorFactory version
        super(context, "my_notepad.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CARD_NUMBER + " TEXT NOT NULL" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

package com.ruitu.entrance_guard.mvp.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AndroidUtil;
import com.ruitu.entrance_guard.model.api.APIFactory;
import com.ruitu.entrance_guard.model.api.APIRetrofit;
import com.ruitu.entrance_guard.model.bean.Card;
import com.ruitu.entrance_guard.model.bean.DeviceBean;
import com.ruitu.entrance_guard.model.bean.NoticeBean;
import com.ruitu.entrance_guard.model.bean.UpdateVersionBean;
import com.ruitu.entrance_guard.model.bean.WeatherBean;
import com.ruitu.entrance_guard.mvp.contract.MainContract;
import com.ruitu.entrance_guard.support.db.MyNoteDBHelper;
import com.ruitu.entrance_guard.support.utils.SharedPrefsUtil;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wubin on 2017/05/11
 */

public class MainModelImpl implements MainContract.Model {

    protected MyNoteDBHelper myNoteDBHelper;
    protected SQLiteDatabase writableDB;

    public MainModelImpl() {
        myNoteDBHelper = new MyNoteDBHelper(Arad.app);
        writableDB = myNoteDBHelper.getWritableDatabase();
    }

    @Override
    public Observable<WeatherBean> getWeatherInfo() {
        return APIRetrofit.getService_01()
                .getWeatherInfo(111)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<NoticeBean> getNotice() {
        return APIFactory.getApiInstance()
                .getNotice()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UpdateVersionBean> checkNewVersion() {
        return APIFactory.getApiInstance()
                .checkMenjinNewVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<DeviceBean> getDeviceIdByMac() {//根据mac获取设备id
        return APIFactory.getApiInstance()
                .getDeviceIdByMac(AndroidUtil.getLocalMacAddressFromWifiInfo(Arad.app))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Card>> getCardList(String equipmentId) {
        return APIFactory.getApiInstance()
                .getCardList(equipmentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //插入数据
    @Override
    public void insertData(String content) {
        ContentValues cv = new ContentValues();
        cv.put(MyNoteDBHelper.CARD_NUMBER, content);
        writableDB.insert(MyNoteDBHelper.TABLE_NAME, null, cv);
    }

    //判断卡号是否可用
    @Override
    public boolean isCardCanUse(String cardNum) {
        if (TextUtils.isEmpty(cardNum)) return false;
        String savedNum = SharedPrefsUtil.getValue(Arad.app, "card_number", "");
        Log.i("wubin", "savedNum = " + savedNum);
        Log.i("wubin", "cardNum = " + cardNum);
        return savedNum.contains(cardNum);//已经存储的卡号是否包含用户刷的卡号
    }

//    //查询出来已经添加的数据
//    private void queryNotes() {
//        Cursor cursor = writableDB.query(MyNoteDBHelper.TABLE_NAME, null, null, null, null, null, null);
//        while (cursor.moveToNext()) {
//
//            int id = cursor.getInt(cursor.getColumnIndex(MyNoteDBHelper.ID));
//            String content = cursor.getString(cursor.getColumnIndex(MyNoteDBHelper.CONTENT));
//            String time = cursor.getString(cursor.getColumnIndex(MyNoteDBHelper.TIME));
//            String imagePath = cursor.getString(cursor.getColumnIndex(MyNoteDBHelper.IMAGE_PATH));
//            String videoPath = cursor.getString(cursor.getColumnIndex(MyNoteDBHelper.VIDEO_PATH));
//
//            Note note = new Note();
//            note.setId(id);
//            note.setTitle(content);
//            note.setTime(time);
//            note.setImagePath(imagePath);
//            note.setVideoPath(videoPath);
//            noteList.add(note);
//        }
//        cursor.close();
//    }
}
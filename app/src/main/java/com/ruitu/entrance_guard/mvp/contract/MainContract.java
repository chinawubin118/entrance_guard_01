package com.ruitu.entrance_guard.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;
import com.ruitu.entrance_guard.model.bean.NoticeBean;
import com.ruitu.entrance_guard.model.bean.UpdateVersionBean;
import com.ruitu.entrance_guard.model.bean.WeatherBean;

import rx.Observable;

/**
 * Created by wubin on 2017/5/11.
 */

public interface MainContract {
    public interface View extends BaseView {
        void setWeatherInfo2View(WeatherBean weatherBean);//将天气数据设置到view上

        void setNoticeToView(NoticeBean noticeBean);//将公告设置到view上

        void showNotice();//显示公告

        void hideNotice();//隐藏公告

        void showPressedKeys(String newPressedKey);//显示按下的按键

        void backSpace();//消除最后一个按下的数字
    }

    public abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getWeatherInfo();

        public abstract void getNotice();

        public abstract void checkNewVersion();//获取版本更新信息

        public abstract void unlock();//开锁:直接调用底层开锁

        public abstract void lock();//上锁:直接调用底层上锁
    }

    public interface Model extends BaseModel {
        Observable<WeatherBean> getWeatherInfo();

        Observable<NoticeBean> getNotice();

        Observable<UpdateVersionBean> checkNewVersion();//获取版本更新信息
    }
}
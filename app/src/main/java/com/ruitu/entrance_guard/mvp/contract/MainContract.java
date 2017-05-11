package com.ruitu.entrance_guard.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;
import com.ruitu.entrance_guard.model.bean.NoticeBean;
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
    }

    public abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getWeatherInfo();

        public abstract void getNotice();
    }

    public interface Model extends BaseModel {
        Observable<WeatherBean> getWeatherInfo();

        Observable<NoticeBean> getNotice();
    }
}
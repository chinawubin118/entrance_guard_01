package com.ruitu.entrance_guard.mvp.model;

import com.ruitu.entrance_guard.model.api.APIFactory;
import com.ruitu.entrance_guard.model.api.APIRetrofit;
import com.ruitu.entrance_guard.model.bean.NoticeBean;
import com.ruitu.entrance_guard.model.bean.WeatherBean;
import com.ruitu.entrance_guard.mvp.contract.MainContract;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wubin on 2017/05/11
 */

public class MainModelImpl implements MainContract.Model {

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
}
package com.ruitu.entrance_guard.mvp.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ruitu.entrance_guard.model.bean.NoticeBean;
import com.ruitu.entrance_guard.model.bean.WeatherBean;
import com.ruitu.entrance_guard.mvp.contract.MainContract;

import java.util.Date;

import rx.Subscriber;

/**
 * Created by wubin on 2017/05/11
 */

public class MainPresenterImpl extends MainContract.Presenter {

    @Override
    public void getWeatherInfo() {
        mRxManage.add(mModel.getWeatherInfo().subscribe(new Subscriber<WeatherBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(WeatherBean weatherBean) {
                if (null != weatherBean) {
                    WeatherBean.ResultsBean.NowBean weatherNow = weatherBean.getResults().get(0).getNow();
                    Log.i("http", "当前天气:" + weatherNow.getText());//天气文本，例如“晴”、“多云”
                    Log.i("http", "当前温度:" + weatherNow.getTemperature());//获取当前温度
                    Log.i("http", "当前天气:" + weatherNow.getCode());//获取当前天气代码
                }
                mView.setWeatherInfo2View(weatherBean);
            }
        }));
    }

    @Override
    public void getNotice() {
        mRxManage.add(mModel.getNotice()
                .subscribe(new Subscriber<NoticeBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(NoticeBean noticeBean) {
                        Log.i("http", "请求继续:onError:");//
                        if (null != noticeBean) {
                            Log.i("http", "noticeBean.getNotice() = " + noticeBean.getNotice());

                            try {
                                long outTime = noticeBean.getOutTime();
                            } catch (Exception e) {//用于异常检测(一旦获取过期时间异常,说明后台返回的时间有问题,自己设置一个)
                                noticeBean.setOutTime(new Date().getTime());
                            }

                            if (isNoticeOut(noticeBean)) {//公告已过时
                                mView.hideNotice();
                            } else {//未过时
                                mView.showNotice();
                                mView.setNoticeToView(noticeBean);

                                sendNewMessage(noticeBean);//隔一秒发送一个消息,检测公告是否过时
                            }
                        }
                    }
                }));
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (1002 == msg.what) {//检测公告是否过时
                NoticeBean noticeBean = (NoticeBean) msg.obj;

                if (isNoticeOut(noticeBean)) {//公告已过时:隐藏公告
                    mView.hideNotice();
                } else {
                    sendNewMessage(noticeBean);//隔一秒发送一个消息,检测公告是否过时
                }
            }
        }
    };

    //判断通知是否过期
    private boolean isNoticeOut(NoticeBean noticeBean) {
        //判断是否应该显示通知
        long currentTime = new Date().getTime();//当前时间
        long outTime = noticeBean.getOutTime();//公告过期的时间
        return currentTime > outTime;
    }

    //隔一秒发送一个消息,检测公告是否过时
    private void sendNewMessage(NoticeBean noticeBean) {
        //未过时的时候开启一个定时器,过时的时候隐藏公告
        Message message = Message.obtain();
        message.obj = noticeBean;
        message.what = 1002;
        handler.sendMessageDelayed(message, 1000);
    }
}
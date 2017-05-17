package com.ruitu.entrance_guard.mvp.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AndroidUtil;
import com.ruitu.entrance_guard.model.bean.Card;
import com.ruitu.entrance_guard.model.bean.DeviceBean;
import com.ruitu.entrance_guard.model.bean.NoticeBean;
import com.ruitu.entrance_guard.model.bean.UpdateVersionBean;
import com.ruitu.entrance_guard.model.bean.WeatherBean;
import com.ruitu.entrance_guard.mvp.contract.MainContract;
import com.ruitu.entrance_guard.support.utils.SharedPrefsUtil;
import com.ruitu.entrance_guard.support.utils.TimeUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.semtec.www.epcontrol.EPControl;
import rx.Subscriber;

/**
 * Created by wubin on 2017/05/11
 */
public class MainPresenterImpl extends MainContract.Presenter {

    @Override
    public void getWeatherInfo() {
        mModel.getWeatherInfo().subscribe(new Subscriber<WeatherBean>() {
            @Override
            public void onCompleted() {
                handler.sendEmptyMessageDelayed(1005, 10800000);//请求天气失败以后,每三小时请求一次天气
            }

            @Override
            public void onError(Throwable e) {
                handler.sendEmptyMessageDelayed(1005, 10800000);//请求天气成功以后,每三小时请求一次天气
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
        });
    }

    @Override
    public void getNotice() {
        mModel.getNotice()
                .subscribe(new Subscriber<NoticeBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(NoticeBean noticeBean) {
                        if (null != noticeBean) {
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
//                                //到过期时直接发送一个消息,隐藏公告
//                                handler.sendEmptyMessageDelayed(1003, noticeBean.getOutTime() - new Date().getTime());
                            }
                        }
                    }
                });
    }

    @Override
    public void checkNewVersion() {
        mModel.checkNewVersion()
                .subscribe(new Subscriber<UpdateVersionBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(UpdateVersionBean updateVersionBean) {
                        if (null != updateVersionBean) {
                            int currVersionCode = AndroidUtil.getVerCode(Arad.app);
                            int newVersionCode = updateVersionBean.getCode();
                            if (newVersionCode > currVersionCode) {//服务器上的版本>当前安装的版本,需要执行更新操作

                            } else {//当前已是最新版本:隔一个小时检测是否有新版本
                                handler.sendEmptyMessageDelayed(1004, TimeUtils.HOUR_AS_MILLIS);
                            }
                        }
                    }
                });
    }

    @Override
    public void unlock() {
        EPControl.EpControlUnlock();//开锁
    }

    @Override
    public void lock() {
        EPControl.EpControlLock();//上锁
    }

    @Override
    public boolean isMenjinConnectSuccess() {
        return EPControl.EpControlConnect() >= 0;
    }

    @Override
    public void getDeviceIdByMac() {//根据mac获取设备id
        mModel.getDeviceIdByMac().subscribe(new Subscriber<DeviceBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(DeviceBean deviceBean) {
                if (null != deviceBean && null != deviceBean.getData()) {
                    DeviceBean.DataBean dataBean = deviceBean.getData();
                    SharedPrefsUtil.putValue(Arad.app, "door_id",dataBean.getId());
                    getCardList(dataBean.getId());
                }
            }
        });
    }

    @Override
    public void getCardList(String deviceId) {
        mModel.getCardList(deviceId)
                .subscribe(new Subscriber<List<Card>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<Card> cards) {
                        String cardNumbers = getStringByList(cards);
                        Log.i("wubin", "存储的cardNumbers = " + cardNumbers);
                        SharedPrefsUtil.putValue(Arad.app,"card_number",cardNumbers);
                        mModel.insertData(cardNumbers);
                    }
                });
    }

    @Override
    public boolean isCardCanUse(String cardNum) {
        return mModel.isCardCanUse(cardNum);
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
//            if (msg.what == 1003) {//收到了隐藏公告的消息
//                mView.hideNotice();
//            }
            if (msg.what == 1004) {//收到检查新版本的消息
                checkNewVersion();//继续检查是否有新版本
            }
            if (msg.what == 1005) {//请求天气
                mModel.getWeatherInfo();
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

    public String getStringByList(List<Card> cardList) {
        if (null == cardList || cardList.size() == 0) return "";
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < cardList.size(); i++) {
            sb.append(cardList.get(i).getNumber() + ",");
        }
        return sb.toString();
    }
}
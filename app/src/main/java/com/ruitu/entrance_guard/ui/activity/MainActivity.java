package com.ruitu.entrance_guard.ui.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ruitu.entrance_guard.Constant;
import com.ruitu.entrance_guard.R;
import com.ruitu.entrance_guard.impl.PageChangeListenerImpl;
import com.ruitu.entrance_guard.model.bean.NoticeBean;
import com.ruitu.entrance_guard.model.bean.WeatherBean;
import com.ruitu.entrance_guard.mvp.contract.MainContract;
import com.ruitu.entrance_guard.mvp.model.MainModelImpl;
import com.ruitu.entrance_guard.mvp.presenter.MainPresenterImpl;
import com.ruitu.entrance_guard.net.UrlManager;
import com.ruitu.entrance_guard.net.socket.SocketManager;
import com.ruitu.entrance_guard.receiver.TimeChangeReceiver;
import com.ruitu.entrance_guard.support.utils.AnimationUtils;
import com.ruitu.entrance_guard.support.utils.KeyUtils;
import com.ruitu.entrance_guard.support.utils.MyToast;
import com.ruitu.entrance_guard.support.utils.SharedPrefsUtil;
import com.ruitu.entrance_guard.support.utils.TimeUtils;
import com.ruitu.entrance_guard.support.utils.UiUtils;
import com.ruitu.entrance_guard.support.utils.WeatherUtils;
import com.ruitu.entrance_guard.ui.adapter.AdAdapter;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.math.BigInteger;
import java.net.URI;

import cn.semtec.www.epcontrol.EPControl;
import cn.semtec.www.semteccardreaderlib.SerialPortActivity;

import static cn.semtec.www.epcontrol.Util.bytesToHex;

public class MainActivity extends SerialPortActivity<MainPresenterImpl, MainModelImpl>
        implements MainContract.View, View.OnClickListener, TimeChangeReceiver.OnTimeChangeListener {

    private String TAG = "MainActivity";

    private ViewPager vp_ad;//轮播广告
    private RelativeLayout rl_bg;//弹出来的公告(包含文字和背景)
    private TextView tv_state, tv_time, tv_date, tv_weekday;//在线状态,当前时间,日期,星期几
    private TextView tv_city_name, tv_temp;//城市名,当前温度
    private ImageView iv_weather_icon;//天气图标
    private TextView tv_notice, tv_card;//公告内容,刷卡的提示
    private View ad_content, dial_content;//广告和公告的布局,拨号时的布局
    private TextView et_dial_num;//显示摁下的号码
    private View red_state, green_state, gray_state;//三个状态圆圈
    private TextView tv_state2;//在线状态

    //广告图片
    private int[] imgResIds = {R.mipmap.img_01, R.mipmap.img_02, R.mipmap.img_03};
    //当前选中的图片的position
    private int currPosition;

    //接收时间改变的receiver
    private TimeChangeReceiver timeReceiver;

    private Integer SOCKET_CHECK_TIME = 30000; // 自动检测服务器是否在线的时间间隔

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化view控件
        vp_ad = (ViewPager) findViewById(R.id.vp_ad);
        rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_weekday = (TextView) findViewById(R.id.tv_weekday);
        tv_city_name = (TextView) findViewById(R.id.tv_city_name);
        tv_temp = (TextView) findViewById(R.id.tv_temp);
        tv_notice = (TextView) findViewById(R.id.tv_notice);
        tv_card = (TextView) findViewById(R.id.tv_card);
        iv_weather_icon = (ImageView) findViewById(R.id.iv_weather_icon);
        et_dial_num = (TextView) findViewById(R.id.et_dial_num);
        tv_state2 = (TextView) findViewById(R.id.tv_state2);
        ad_content = findViewById(R.id.ad_content);
        dial_content = findViewById(R.id.dial_content);
        red_state = findViewById(R.id.red_state);
        green_state = findViewById(R.id.green_state);
        gray_state = findViewById(R.id.gray_state);

        initAndRegisterReceiver();//初始化和注册时间改变的监听
        vp_ad.setAdapter(new AdAdapter(this, imgResIds));//设置轮播
        hideNotice();//默认不显示通知

        addListener();//派发事件监听
        executeLunbo();
        mPresenter.getWeatherInfo();//获取心知天气信息
        mPresenter.getNotice();//获取公告
        mPresenter.checkNewVersion();//检查新版本
        mPresenter.getDeviceIdByMac(); //根据mac获取设备id

        handler.sendEmptyMessageDelayed(1005, 2000);//延迟2s检测是否连接成功

        //initWebSocket();
        handlerCheckSocket.postDelayed(runnableCheckSocket,SOCKET_CHECK_TIME);
    }

    //初始化并注册timeReceiver
    private void initAndRegisterReceiver() {
        timeReceiver = new TimeChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(timeReceiver, filter);
    }

    //页面改变监听
    private void addListener() {
        vp_ad.addOnPageChangeListener(new PageChangeListenerImpl() {
            @Override
            public void onPageSelected(int position) {
                currPosition = position;
            }
        });
        rl_bg.setOnClickListener(this);
        tv_state.setOnClickListener(this);
        timeReceiver.setOnTimeChangeListener(this);
    }

    //执行广告的轮播
    private void executeLunbo() {
        handler.sendEmptyMessageDelayed(1001, Constant.TIME_UNIT);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001://广告轮播
                    currPosition++;
                    if (currPosition >= imgResIds.length) {
                        currPosition = 0;
                    }
                    vp_ad.setCurrentItem(currPosition);
                    sendEmptyMessageDelayed(1001, Constant.TIME_UNIT);
                    break;
                case 1005:
                    if (mPresenter.isMenjinConnectSuccess()) {//门禁连接成功
                        MyToast.showShortToast(mContext, "连接成功");
                        setOnlineState(1);
                    }
                    break;
                case 1006:
                    EPControl.EpControlLock();//开锁后自动锁门
                    break;

                //下面两个应该是收到刷卡的数据的时候执行
                case 0:
                    try {
                        String newNum = new BigInteger(strToDisp, 16).toString();
                        if (mPresenter.isCardCanUse(newNum)) {
                            mPresenter.unlock();//执行解锁
                            MyToast.showShortToast(mContext, "刷卡开门成功!");
                            KeyUtils.playVoiceByKeycode(KeyUtils.DOOR_IS_OPENED, mContext);
                            handler.sendEmptyMessageDelayed(1006, KeyUtils.AUTO_LOCK_TIME);
                        } else {
                            MyToast.showShortToast(mContext, "开门失败!");
                        }
                    } catch (Exception e) {
                        MyToast.showShortToast(mContext, "开门失败!");
                    }
                    break;
                case 1:
//                    tv_card.setText("请读卡");
                    break;
            }
        }
    };

    Handler handlerCheckSocket = new Handler();
    Runnable runnableCheckSocket = new Runnable() {

        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                initWebSocket();
                boolean isOpen = SocketManager.webSocket.isOpen();
                SocketManager.register(SharedPrefsUtil.getValue(Arad.app, "door_id", ""));
                if(!isOpen){
                    initWebSocket();
                }
                Log.i(TAG, "run: 服务器在线状态:"  + isOpen);
            } catch (Exception e) {
                System.out.println("正在尝试重新链接...");
            }finally {
                handlerCheckSocket.postDelayed(this, SOCKET_CHECK_TIME);
            }

        }
    };

    @Override
    public void onClick(View v) {
        if (v == rl_bg) {//点击弹出来的公告
            hideNotice();
        }
        if (v == tv_state) {
            showNotice();
        }
    }

    @Override
    public void OnTimeChange(String currentTime, String currentDate, String currentWeekday) {
        setTimeToView(currentTime, currentDate, currentWeekday);

        String hourNum = TimeUtils.getHourNum();
        int hourNumInt = Integer.parseInt(hourNum);
        switch (hourNumInt) {
            //时间改变的时候
        }
    }

    //为视图设置时间
    private void setTimeToView(String time, String date, String weekday) {
        tv_time.setText(time);
        tv_date.setText(date);
        tv_weekday.setText(weekday);
    }

    //显示公告
    @Override
    public void showNotice() {
        if (rl_bg.getVisibility() != View.VISIBLE) {
            rl_bg.setVisibility(View.VISIBLE);
            AnimationUtils.AlphaAnimation(rl_bg, 0f, 1f, 200);
        }
    }

    //隐藏公告
    @Override
    public void hideNotice() {
        if (rl_bg.getVisibility() != View.GONE) {
            rl_bg.setVisibility(View.GONE);
            AnimationUtils.AlphaAnimation(rl_bg, 1f, 0f, 200);
        }
    }

    //mvp/v中的方法:显示按下的按键
    @Override
    public void showPressedKeys(String newPressedKey) {
        String oldStr = et_dial_num.getText().toString();
        ad_content.setVisibility(View.GONE);
        dial_content.setVisibility(View.VISIBLE);//显示拨号的布局

        if (oldStr.length() < KeyUtils.MAX_LENGTH) {
            StringBuilder sb = new StringBuilder(oldStr);
            et_dial_num.setText(sb.append(newPressedKey).toString());
        }
    }

    //mvp/v中的方法:消除最后一个按下的数字
    @Override
    public void backSpace() {
        String oldStr = et_dial_num.getText().toString();
        if (!TextUtils.isEmpty(oldStr)) {
            String newStr = oldStr.substring(0, oldStr.length() - 1);
            et_dial_num.setText(newStr);
        }

        if (oldStr.length() <= 1) {
            ad_content.setVisibility(View.VISIBLE);
            dial_content.setVisibility(View.GONE);//显示广告的布局
        }
    }

    /**
     * @param stateCode 1在线 2离线 3异常
     */
    @Override
    public void setOnlineState(int stateCode) {
        switch (stateCode) {
            case 1://在线
                red_state.setVisibility(View.GONE);
                gray_state.setVisibility(View.GONE);
                green_state.setVisibility(View.VISIBLE);
                tv_state2.setText("(在线)");
                break;
            case 2://不在线
                red_state.setVisibility(View.GONE);
                gray_state.setVisibility(View.VISIBLE);
                green_state.setVisibility(View.GONE);
                tv_state2.setText("(离线)");
                break;
            case 3://其他:显示红灯
                red_state.setVisibility(View.VISIBLE);
                gray_state.setVisibility(View.GONE);
                green_state.setVisibility(View.GONE);
                tv_state2.setText("(异常)");
                break;

        }
    }

    //mvp/v中的方法
    @Override
    public void setWeatherInfo2View(WeatherBean weatherBean) {
        try {
            WeatherBean.ResultsBean.NowBean nowBean = weatherBean.getResults().get(0).getNow();
            iv_weather_icon.setImageResource(WeatherUtils.getWeatherIcon(Integer.parseInt(nowBean.getCode())));
            tv_temp.setText("当前温度:" + nowBean.getTemperature() + "℃");
        } catch (Exception e) {//设置天气出现异常
            iv_weather_icon.setImageResource(R.drawable.sunny);
            tv_temp.setText("暂无温度数据");
        }
    }

    @Override
    public void setNoticeToView(NoticeBean noticeBean) {
        try {
            tv_notice.setText(noticeBean.getNotice());//设置公告到弹窗上
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String dialContent = et_dial_num.getText().toString();
        if (dialContent.length() < KeyUtils.MAX_LENGTH ||
                keyCode == KeyUtils.XING_HAO_JIAN || keyCode == KeyUtils.JING_HAO_JIAN) {
            KeyUtils.playVoiceByKeycode(keyCode, mContext);//没有达到四位数时,播放声音
        }

        if (keyCode == KeyUtils.XING_HAO_JIAN) {//按下的是"*"
            backSpace();//执行回退操作
            return super.onKeyDown(keyCode, event);
        }
        if (keyCode != KeyUtils.JING_HAO_JIAN) {//按下的不是井号键
            String keyStr = KeyUtils.getKeyStrByKeycode(keyCode);
            showPressedKeys(keyStr);
        } else {//按下的是井号键
            if (dialContent.length() < KeyUtils.MAX_LENGTH -1) {//拨号长度不够
                MyToast.showShortToast(mContext, "请拨至少三位的号码!例如：304");
            } else {//可以进行拨号了
                if(dialContent.length() == 3){
                    dialContent = "0" + dialContent;
                }
                MyToast.showShortToast(mContext, "开始呼叫" + dialContent + ".....");
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private String strToDisp;

    @Override
    protected void onDataReceived(byte[] buffer, int size) {
        strToDisp = bytesToHex(buffer, size);

        handler.sendEmptyMessage(0);
        handler.sendEmptyMessageDelayed(1, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UiUtils.hideBottomUIMenu(this);//不显示底部虚拟按键
        setTimeToView(TimeUtils.getHourAndMinute(), TimeUtils.getDate(), TimeUtils.getWeekOfDate());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(timeReceiver);
    }

    /**
     * 初始化wensocket连接
     */
    private void initWebSocket() {
        URI uri = null;
        try {
            uri = new URI(UrlManager.HOST_WS + "/webSocketPhone");
            SocketManager.webSocket = new WebSocketClient(uri, new Draft_17()) {

                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    Log.i(TAG, "onOpen: 连接成功");
                    SocketManager.register(SharedPrefsUtil.getValue(Arad.app, "door_id", ""));
                }

                @Override
                public void onMessage(String s) {
                    Log.i(TAG, "onMessage: " + s);
                    String[] managerArr = s.split(",");
                    if(managerArr.length > 0){
                        if(managerArr.length == 2 && managerArr[0].equals("tip") ){
                            String[] registerArr = managerArr[1].split(":");
                            if(registerArr.length == 2 && registerArr[0].equals("register")){ //用户注册（反馈）
                                String status = registerArr[1];
                                Log.i(TAG, "onMessage: " + status);
                            }
                        }
                        if(managerArr.length == 2 && managerArr[0].equals("manager") ){
                            String[] registerArr = managerArr[1].split(":");
                            if(registerArr.length > 0 && registerArr[0].equals("openDoor")){ //开门
                                try {
                                    mPresenter.unlock();//执行解锁
                                    MyToast.showShortToast(mContext, "APP开门成功!");
                                    KeyUtils.playVoiceByKeycode(KeyUtils.DOOR_IS_OPENED, mContext);
                                    handler.sendEmptyMessageDelayed(1006, KeyUtils.AUTO_LOCK_TIME);
                                } catch (Exception e) {
                                    MyToast.showShortToast(mContext, "开门失败!");
                                }
                            }
                        }
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    Log.i(TAG, "onClose: 已关闭");
                    Log.i(TAG, "onClose: " + i);
                    Log.i(TAG, "onClose: " + s);
                    Log.i(TAG, "onClose: " + b);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            };
            //webSocket.setSocket(SSLSocketFactory.getDefault().createSocket(uri.getHost(), uri.getPort()));
            SocketManager.webSocket.connect();

        } catch (Exception e) {
            Log.i(TAG, "initWebSocket: 重新链接服务器！");
        }

    }
}

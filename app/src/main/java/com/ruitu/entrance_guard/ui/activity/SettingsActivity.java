package com.ruitu.entrance_guard.ui.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.beanu.arad.utils.AndroidUtil;
import com.ruitu.entrance_guard.Constant;
import com.ruitu.entrance_guard.R;
import com.ruitu.entrance_guard.model.api.APIRetrofit;
import com.ruitu.entrance_guard.model.bean.UpdateVersionBean;
import com.ruitu.entrance_guard.mvp.contract.SettingsContract;
import com.ruitu.entrance_guard.mvp.model.SettingsModelImpl;
import com.ruitu.entrance_guard.mvp.presenter.SettingsPresenterImpl;
import com.ruitu.entrance_guard.support.utils.ApkController;
import com.ruitu.entrance_guard.support.utils.CommonUtils;
import com.ruitu.entrance_guard.support.utils.KeyUtils;
import com.ruitu.entrance_guard.support.utils.MyToast;
import com.ruitu.entrance_guard.support.utils.UiUtils;

import java.io.File;

import cn.semtec.www.semteccardreaderlib.SerialPortActivity;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class SettingsActivity extends SerialPortActivity<SettingsPresenterImpl, SettingsModelImpl> implements SettingsContract.View {
    private View menu_1_coverage, menu_2_coverage, menu_3_coverage, menu_4_coverage, menu_5_coverage, menu_6_coverage;//菜单的前景(还是背景啊?)

    private CommonUtils commonUtils;

    public static final int STATUS_MENU_1 = 1;
    public static final int STATUS_MENU_2 = 2;
    public static final int STATUS_MENU_3 = 3;
    public static final int STATUS_MENU_4 = 4;
    public static final int STATUS_MENU_5 = 5;
    public static final int STATUS_MENU_6 = 6;

    private int currMenuStatus = 1;//菜单状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        menu_1_coverage = findViewById(R.id.menu_1_coverage);
        menu_2_coverage = findViewById(R.id.menu_2_coverage);
        menu_3_coverage = findViewById(R.id.menu_3_coverage);
        menu_4_coverage = findViewById(R.id.menu_4_coverage);
        menu_5_coverage = findViewById(R.id.menu_5_coverage);
        menu_6_coverage = findViewById(R.id.menu_6_coverage);

        commonUtils = new CommonUtils();
    }

    @Override
    protected void onDataReceived(byte[] buffer, int size) {

    }

    /**
     * 取消下载进度条的handler
     */
    private Handler cancelProgressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    showProgressWithText(false, "");
                    MyToast.showShortToast(getApplication(), "下载完成");
                    final File musicFile = new File(Environment.getExternalStorageDirectory() + "/menjin/" + "update_version.apk");

                    if (ApkController.hasRootPerssion()) {
                        Log.i("wubin", "该机有有有root权限...");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final boolean result = (ApkController.clientInstall(musicFile.getAbsolutePath()));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (result) {
                                            MyToast.showShortToast(getApplication(), "安装成功");
                                            Log.i("wubin", "安装成功...");
                                        } else {
                                            MyToast.showShortToast(getApplication(), "安装失败");
                                            Log.i("wubin", "安装失败...");
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        Log.i("wubin", "该机没有root权限...");
                    }

                    break;
                case 1111:
                    showProgressWithText(false, "");
                    MyToast.showShortToast(getApplication(), "下载失败,请重试");
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String keyStr = KeyUtils.getKeyStrByKeycode(keyCode);

        if (TextUtils.equals("5", keyStr)) {//相当于确定
            switch (currMenuStatus) {
                case STATUS_MENU_1://音量
                    startActivity(AudioSettingActivity.class);
                    break;
                case STATUS_MENU_2://当前版本
                    MyToast.showShortToast(getApplication(), "当前版本:V" + AndroidUtil.getVerName(getApplication()));
                    break;
                case STATUS_MENU_3://版本更新
                    showProgressWithText(true, "正在更新版本...");
                    APIRetrofit.getDefault().checkMenjinNewVersion()
                            .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                            .subscribe(new Subscriber<UpdateVersionBean>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    showProgressWithText(false, "正在更新版本...");
                                }

                                @Override
                                public void onNext(UpdateVersionBean updateVersionBean) {
//                                    UpdateChecker.checkForNotification(SettingsActivity.this, "版本更新",
//                                            Constant.BASE_URL + updateVersionBean.getUrl(), updateVersionBean.getCode());
                                    try {
                                        commonUtils.setOnDownloadProgressChange(new CommonUtils.OnDownloadProgressChange() {
                                            @Override
                                            public void onProgressChanged(int currentSize) {//下载进度改变的时候执行
                                                Log.i("wubin", "currentSize = " + currentSize);
                                            }

                                            @Override
                                            public void onGetFileSize(int fileSize) {//获取到下载文件大小的时候执行
                                                Log.i("wubin", "fileSize = " + fileSize);
                                            }
                                        });

                                        File musicFile = new File(Environment.getExternalStorageDirectory() + "/menjin/" + "update_version.apk");
                                        if (!musicFile.getParentFile().exists()) {
                                            musicFile.getParentFile().mkdirs();
                                        }
                                        commonUtils.download(Constant.BASE_URL + updateVersionBean.getUrl(), musicFile.getAbsolutePath());
                                    } catch (Exception e) {
                                        MyToast.showShortToast(getApplication(), "下载出错,请重试");
                                        cancelProgressHandler.sendEmptyMessage(1111);
                                        e.printStackTrace();
                                    }
                                    cancelProgressHandler.sendEmptyMessage(1001);//下载完成
                                }
                            });
                    break;
                case STATUS_MENU_4://延迟锁
                    startActivity(DelayLockSettingActivity.class);
                    break;
                case STATUS_MENU_5:
                    MyToast.showShortToast(getApplication(), "敬请期待...");
                    break;
                case STATUS_MENU_6://退出
                    onBackPressed();
                    break;
            }
        } else if (TextUtils.equals("2", keyStr)) {//相当于上
            switch (currMenuStatus) {
                case STATUS_MENU_1:
                case STATUS_MENU_4:
                    currMenuStatus = STATUS_MENU_1;
                    break;
                case STATUS_MENU_2:
                case STATUS_MENU_5:
                    currMenuStatus = STATUS_MENU_2;
                    break;
                case STATUS_MENU_3:
                case STATUS_MENU_6:
                    currMenuStatus = STATUS_MENU_3;
                    break;
            }
        } else if (TextUtils.equals("8", keyStr)) {//相当于下
            switch (currMenuStatus) {
                case STATUS_MENU_1:
                case STATUS_MENU_4:
                    currMenuStatus = STATUS_MENU_4;
                    break;
                case STATUS_MENU_2:
                case STATUS_MENU_5:
                    currMenuStatus = STATUS_MENU_5;
                    break;
                case STATUS_MENU_3:
                case STATUS_MENU_6:
                    currMenuStatus = STATUS_MENU_6;
                    break;
            }
        } else if (TextUtils.equals("4", keyStr)) {//相当于左
            switch (currMenuStatus) {
                case STATUS_MENU_1:
                    currMenuStatus = STATUS_MENU_6;
                    break;
                case STATUS_MENU_2:
                    currMenuStatus = STATUS_MENU_1;
                    break;
                case STATUS_MENU_3:
                    currMenuStatus = STATUS_MENU_2;
                    break;
                case STATUS_MENU_4:
                    currMenuStatus = STATUS_MENU_3;
                    break;
                case STATUS_MENU_5:
                    currMenuStatus = STATUS_MENU_4;
                    break;
                case STATUS_MENU_6:
                    currMenuStatus = STATUS_MENU_5;
                    break;
            }
        } else if (TextUtils.equals("6", keyStr)) {//相当于右
            switch (currMenuStatus) {
                case STATUS_MENU_1:
                    currMenuStatus = STATUS_MENU_2;
                    break;
                case STATUS_MENU_2:
                    currMenuStatus = STATUS_MENU_3;
                    break;
                case STATUS_MENU_3:
                    currMenuStatus = STATUS_MENU_4;
                    break;
                case STATUS_MENU_4:
                    currMenuStatus = STATUS_MENU_5;
                    break;
                case STATUS_MENU_5:
                    currMenuStatus = STATUS_MENU_6;
                    break;
                case STATUS_MENU_6:
                    currMenuStatus = STATUS_MENU_1;
                    break;
            }
        } else {

        }

        setBgByKeyDown(currMenuStatus);

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void setBgByKeyDown(int status) {
        //View.VISIBLE = 0.....View.INVISIBLE = 4.....View.GONE = 8.....
        switch (status) {
            case STATUS_MENU_1:
                setMenuBg(0, 8, 8, 8, 8, 8);
                break;
            case STATUS_MENU_2:
                setMenuBg(8, 0, 8, 8, 8, 8);
                break;
            case STATUS_MENU_3:
                setMenuBg(8, 8, 0, 8, 8, 8);
                break;
            case STATUS_MENU_4:
                setMenuBg(8, 8, 8, 0, 8, 8);
                break;
            case STATUS_MENU_5:
                setMenuBg(8, 8, 8, 8, 0, 8);
                break;
            case STATUS_MENU_6:
                setMenuBg(8, 8, 8, 8, 8, 0);
                break;
        }
    }

    private void setMenuBg(int v1, int v2, int v3, int v4, int v5, int v6) {
        menu_1_coverage.setVisibility(v1);
        menu_2_coverage.setVisibility(v2);
        menu_3_coverage.setVisibility(v3);
        menu_4_coverage.setVisibility(v4);
        menu_5_coverage.setVisibility(v5);
        menu_6_coverage.setVisibility(v6);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UiUtils.hideBottomUIMenu(this);//不显示底部虚拟按键
    }
}

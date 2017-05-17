package com.ruitu.entrance_guard.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.beanu.arad.utils.AndroidUtil;
import com.ruitu.entrance_guard.R;
import com.ruitu.entrance_guard.mvp.contract.SettingsContract;
import com.ruitu.entrance_guard.mvp.model.SettingsModelImpl;
import com.ruitu.entrance_guard.mvp.presenter.SettingsPresenterImpl;
import com.ruitu.entrance_guard.support.utils.KeyUtils;
import com.ruitu.entrance_guard.support.utils.MyToast;
import com.ruitu.entrance_guard.support.utils.UiUtils;

import cn.semtec.www.semteccardreaderlib.SerialPortActivity;

public class SettingsActivity extends SerialPortActivity<SettingsPresenterImpl, SettingsModelImpl> implements SettingsContract.View {
    private View menu_1_coverage, menu_2_coverage, menu_3_coverage, menu_4_coverage, menu_5_coverage, menu_6_coverage;//菜单的前景(还是背景啊?)

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
    }

    @Override
    protected void onDataReceived(byte[] buffer, int size) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String keyStr = KeyUtils.getKeyStrByKeycode(keyCode);

        if (TextUtils.equals("5", keyStr)) {//相当于确定
            switch (currMenuStatus) {
                case STATUS_MENU_1:
                    startActivity(AudioSettingActivity.class);
                    break;
                case STATUS_MENU_2:
                    MyToast.showShortToast(getApplication(), "当前版本:V" + AndroidUtil.getVerName(getApplication()));
                    break;
                case STATUS_MENU_3:
                    break;
                case STATUS_MENU_4:
                    startActivity(DelayLockSettingActivity.class);
                    break;
                case STATUS_MENU_5:
                    MyToast.showShortToast(getApplication(), "敬请期待...");
                    break;
                case STATUS_MENU_6:
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

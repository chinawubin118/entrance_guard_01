package com.ruitu.entrance_guard.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;

/**
 * Created by wubin on 2017/5/17.
 */

public interface SettingsContract {

    public interface View extends BaseView {
        void setBgByKeyDown(int status);//根据状态设置按键的背景
    }

    public abstract class Presenter extends BasePresenter<View, Model> {
        public abstract boolean silentInstall(String cardNum);//测试静默安装
    }

    public interface Model extends BaseModel {
    }
}
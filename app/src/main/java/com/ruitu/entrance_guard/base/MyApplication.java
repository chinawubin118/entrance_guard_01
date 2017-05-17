package com.ruitu.entrance_guard.base;

import com.beanu.arad.AradApplication;
import com.beanu.arad.AradApplicationConfig;
import com.thinkpage.lib.api.TPWeatherManager;

import cn.semtec.www.epcontrol.EPControl;

/**
 * Created by wubin on 2017/5/11.
 */

public class MyApplication extends AradApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        TPWeatherManager weatherManager = TPWeatherManager.sharedWeatherManager();
        weatherManager.initWithKeyAndUserId("tyc5m2krtykod7bw","U0422597AB");

        disableCrashHandler();
        EPControl.EpControlConnect();
    }

    @Override
    protected AradApplicationConfig appConfig() {
        return new AradApplicationConfig();
    }


}

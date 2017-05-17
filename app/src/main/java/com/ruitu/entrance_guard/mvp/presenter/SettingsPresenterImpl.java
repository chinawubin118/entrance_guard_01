package com.ruitu.entrance_guard.mvp.presenter;
import com.ruitu.entrance_guard.mvp.contract.SettingsContract;

/**
* Created by wubin on 2017/05/17
*/

public class SettingsPresenterImpl extends SettingsContract.Presenter{

    @Override
    public boolean silentInstall(String cardNum) {
        
        return false;
    }
}
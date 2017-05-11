package com.ruitu.entrance_guard.model.bean;

/**
 * Created by wubin on 2017/5/11.
 */

public class NoticeBean {
    /**
     * id : 8000036221458206407
     * notice : 第五条公告
     * setTime : 1494520038000
     * outTime : 1495384033000
     */
    private String id;
    private String notice;
    private long setTime;
    private long outTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public long getSetTime() {
        return setTime;
    }

    public void setSetTime(long setTime) {
        this.setTime = setTime;
    }

    public long getOutTime() {
        return outTime;
    }

    public void setOutTime(long outTime) {
        this.outTime = outTime;
    }
}

package com.ruitu.entrance_guard.model.bean;

/**
 * Created by wubin on 2017/5/12.
 */

public class UpdateVersionBean {

    private String id;
    private int code;//版本号
    private String name;//版本名
    private String url;//下载地址:使用BASE_URL+ url
    private long setTime;//版本上传的时间
    private String content;//版本更新内容
    private String type;//类型(猜测 1 手机app 2 门禁系统)

    @Override
    public String toString() {
        return "UpdateVersionBean{" +
                "id='" + id + '\'' +
                ", code=" + code +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", setTime=" + setTime +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSetTime() {
        return setTime;
    }

    public void setSetTime(long setTime) {
        this.setTime = setTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

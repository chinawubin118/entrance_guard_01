package com.ruitu.entrance_guard.model.bean;

/**
 * Created by wubin on 2017/5/14.
 */
public class Card {
    private String id;
    private String type;//卡类型
    private String number;//设备编号
    private String residenceId;//小区id
    private String buildingId;//楼栋id
    private String unitId;//单元id
    private String createTime;//注册时间
    private String cardAging;//失效时间
    private String status;//失效状态（0无效  1有效）

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(String residenceId) {
        this.residenceId = residenceId;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCardAging() {
        return cardAging;
    }

    public void setCardAging(String cardAging) {
        this.cardAging = cardAging;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

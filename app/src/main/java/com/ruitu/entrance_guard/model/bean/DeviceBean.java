package com.ruitu.entrance_guard.model.bean;

/**
 * Created by wubin on 2017/5/14.
 */

public class DeviceBean {

    /**
     * success : true
     * code : null
     * data : {"id":"551448787379702142","number":"测试机001","type":"1","residenceId":"1619124251162133776","buildingId":"3842350077176558491","unitId":"5771592910846279466","createTime":1494782104000,"endTime":1494710083000,"roomId":null,"mac":"e0:76:d0:e6:da:0e","sip":null,"password":null,"lockInfo":"这个是测试机001...","serverIp":null,"residence":null,"building":null,"unit":null,"room":null}
     */
    private boolean success;
    private String code;
    private DataBean data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 551448787379702142
         * number : 测试机001
         * type : 1
         * residenceId : 1619124251162133776
         * buildingId : 3842350077176558491
         * unitId : 5771592910846279466
         * createTime : 1494782104000
         * endTime : 1494710083000
         * roomId : null
         * mac : e0:76:d0:e6:da:0e
         * sip : null
         * password : null
         * lockInfo : 这个是测试机001...
         * serverIp : null
         */
        private String id;
        private String number;
        private String type;
        private String residenceId;
        private String buildingId;
        private String unitId;
        private long createTime;
        private long endTime;
        private String roomId;
        private String mac;
        private String sip;
        private String password;
        private String lockInfo;
        private String serverIp;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getSip() {
            return sip;
        }

        public void setSip(String sip) {
            this.sip = sip;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getLockInfo() {
            return lockInfo;
        }

        public void setLockInfo(String lockInfo) {
            this.lockInfo = lockInfo;
        }

        public String getServerIp() {
            return serverIp;
        }

        public void setServerIp(String serverIp) {
            this.serverIp = serverIp;
        }
    }
}

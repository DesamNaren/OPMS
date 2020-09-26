package com.cgg.pps.model.request.faq.faqsubmit;

public class PaddyTestMasterRequest {
    private String UserID;
    private String Password;
    private String DeviceID;
    private String VersionID;
    private String PPCID;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getVersionID() {
        return VersionID;
    }

    public void setVersionID(String versionID) {
        VersionID = versionID;
    }

    public String getPPCID() {
        return PPCID;
    }

    public void setPPCID(String PPCID) {
        this.PPCID = PPCID;
    }
}

package com.cgg.pps.model.request.validateuser.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidateUserRequest {

    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("DeviceID")
    @Expose
    private String deviceID;
    @SerializedName("VersionID")
    @Expose
    private String versionID;
    @SerializedName("LatLong")
    @Expose
    private String latLong;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getVersionID() {
        return versionID;
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

}

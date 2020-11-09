package com.cgg.pps.model.request.devicemapping;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceMappingRequest {
    @SerializedName("ppcCode")
    @Expose
    private String ppcCode;
    @SerializedName("DeviceID")
    @Expose
    private String deviceId;

    public String getPpcCode() {
        return ppcCode;
    }

    public void setPpcCode(String ppcCode) {
        this.ppcCode = ppcCode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}

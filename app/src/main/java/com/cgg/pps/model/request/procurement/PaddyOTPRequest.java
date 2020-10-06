package com.cgg.pps.model.request.procurement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaddyOTPRequest {
    @SerializedName("PPCID")
    @Expose
    private String pPCID;
    @SerializedName("TokenNo")
    @Expose
    private String tokenNo;
    @SerializedName("MobileNo")
    @Expose
    private String MobileNo;


    public String getpPCID() {
        return pPCID;
    }

    public void setpPCID(String pPCID) {
        this.pPCID = pPCID;
    }


    public String getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }
}

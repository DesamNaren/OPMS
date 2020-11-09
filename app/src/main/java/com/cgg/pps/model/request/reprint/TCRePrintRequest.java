package com.cgg.pps.model.request.reprint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TCRePrintRequest {

    @SerializedName("AuthenticationID")
    @Expose
    private String AuthenticationID;
    @SerializedName("OnlineTruckChitNo")
    @Expose
    private String onlineTruckChitNo;
    @SerializedName("ManualTruckChitNo")
    @Expose
    private String manualTruckChitNo;

    public String getAuthenticationID() {
        return AuthenticationID;
    }

    public void setAuthenticationID(String authenticationID) {
        AuthenticationID = authenticationID;
    }

    public String getOnlineTruckChitNo() {
        return onlineTruckChitNo;
    }

    public void setOnlineTruckChitNo(String onlineTruckChitNo) {
        this.onlineTruckChitNo = onlineTruckChitNo;
    }

    public String getManualTruckChitNo() {
        return manualTruckChitNo;
    }

    public void setManualTruckChitNo(String manualTruckChitNo) {
        this.manualTruckChitNo = manualTruckChitNo;
    }
}

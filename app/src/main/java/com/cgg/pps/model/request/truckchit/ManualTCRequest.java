package com.cgg.pps.model.request.truckchit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ManualTCRequest {

    @SerializedName("AuthenticationID")
    @Expose
    private String AuthenticationID;
    @SerializedName("PPCID")
    @Expose
    private String pPCID;
    @SerializedName("ManualTruckChit")
    @Expose
    private String manualTruckChit;

    public String getAuthenticationID() {
        return AuthenticationID;
    }

    public void setAuthenticationID(String authenticationID) {
        AuthenticationID = authenticationID;
    }

    public String getPPCID() {
        return pPCID;
    }

    public void setPPCID(String pPCID) {
        this.pPCID = pPCID;
    }

    public String getManualTruckChit() {
        return manualTruckChit;
    }

    public void setManualTruckChit(String manualTruckChit) {
        this.manualTruckChit = manualTruckChit;
    }

}

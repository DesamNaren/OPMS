package com.cgg.pps.model.request.gunnydetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GunnyDetailsRequest {
    @SerializedName("AuthenticationID")
    @Expose
    private String AuthenticationID;
    @SerializedName("PPCID")
    @Expose
    private String pPCID;


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
}

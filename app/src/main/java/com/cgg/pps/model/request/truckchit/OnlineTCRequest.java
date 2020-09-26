package com.cgg.pps.model.request.truckchit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnlineTCRequest {

    @SerializedName("AuthenticationID")
    @Expose
    private String AuthenticationID;

    @SerializedName("PPCID")
    @Expose
    private String ppcID;

    public String getPpcID() {
        return ppcID;
    }

    public void setPpcID(String PPCID) {
        this.ppcID = PPCID;
    }


    public String getAuthenticationID() {
        return AuthenticationID;
    }

    public void setAuthenticationID(String authenticationID) {
        AuthenticationID = authenticationID;
    }
}

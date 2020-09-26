package com.cgg.pps.model.request.devicemgmt.bb.masterbank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MasterBankRequest {

    @SerializedName("AuthenticationID")
    @Expose
    private String AuthenticationID;

    public String getAuthenticationID() {
        return AuthenticationID;
    }

    public void setAuthenticationID(String authenticationID) {
        AuthenticationID = authenticationID;
    }
}

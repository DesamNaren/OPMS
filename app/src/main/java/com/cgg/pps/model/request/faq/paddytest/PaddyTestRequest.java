package com.cgg.pps.model.request.faq.paddytest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaddyTestRequest {

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

    public String getpPCID() {
        return pPCID;
    }

    public void setpPCID(String pPCID) {
        this.pPCID = pPCID;
    }
}

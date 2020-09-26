package com.cgg.pps.model.request.farmer.socialstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialStatusRequest {

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

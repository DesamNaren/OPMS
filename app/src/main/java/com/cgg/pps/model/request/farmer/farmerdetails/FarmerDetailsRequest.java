package com.cgg.pps.model.request.farmer.farmerdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmerDetailsRequest {

    @SerializedName("AuthenticationID")
    @Expose
    private String AuthenticationID;
    @SerializedName("PPCID")
    @Expose
    private String pPCID;
    @SerializedName("RegistrationNo")
    @Expose
    private String registrationNo;


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

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

}

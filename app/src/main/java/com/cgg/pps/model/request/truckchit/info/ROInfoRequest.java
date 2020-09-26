package com.cgg.pps.model.request.truckchit.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ROInfoRequest {

    @SerializedName("AuthenticationID")
    @Expose
    private String AuthenticationID;
    @SerializedName("PPCID")
    @Expose
    private String pPCID;
    @SerializedName("millerID")
    @Expose
    private String millerID;
    @SerializedName("Ro_Number")
    @Expose
    private String roNumber;


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

    public String getMillerID() {
        return millerID;
    }

    public void setMillerID(String millerID) {
        this.millerID = millerID;
    }

    public String getRoNumber() {
        return roNumber;
    }

    public void setRoNumber(String roNumber) {
        this.roNumber = roNumber;
    }

}

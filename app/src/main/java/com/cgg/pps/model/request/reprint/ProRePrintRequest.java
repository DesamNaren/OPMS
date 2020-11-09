package com.cgg.pps.model.request.reprint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProRePrintRequest {

    @SerializedName("AuthenticationID")
    @Expose
    private String AuthenticationID;
    @SerializedName("PPCID")
    @Expose
    private String pPCID;
    @SerializedName("FarmerTranscationID")
    @Expose
    private String farmerTranscationID;


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

    public String getFarmerTranscationID() {
        return farmerTranscationID;
    }

    public void setFarmerTranscationID(String farmerTranscationID) {
        this.farmerTranscationID = farmerTranscationID;
    }
}

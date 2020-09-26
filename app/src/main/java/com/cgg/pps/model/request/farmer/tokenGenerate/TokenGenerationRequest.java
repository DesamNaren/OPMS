package com.cgg.pps.model.request.farmer.tokenGenerate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenGenerationRequest {

    @SerializedName("AuthenticationID")
    @Expose
    private String AuthenticationID;
    @SerializedName("PPCID")
    @Expose
    private String pPCID;
    @SerializedName("xmlFarmerTokenData")
    @Expose
    private String xmlFarmerTokenData;


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

    public String getXmlFarmerTokenData() {
        return xmlFarmerTokenData;
    }

    public void setXmlFarmerTokenData(String xmlFarmerTokenData) {
        this.xmlFarmerTokenData = xmlFarmerTokenData;
    }

}

package com.cgg.pps.model.request.farmer.farmerUpdate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FarmerUpdateRequest {

    @SerializedName("AuthenticationID")
    @Expose
    private String AuthenticationID;
    @SerializedName("PPCID")
    @Expose
    private String pPCID;
    @SerializedName("xmlFarmerRegistrationDetails")
    @Expose
    private String xmlFarmerRegistrationDetails;

    private FarmerUpdateXMLData farmerXMLData;
    private ArrayList<FarmerUpdateLandXMLData> landXMLDataArrayList;

    public String getpPCID() {
        return pPCID;
    }

    public void setpPCID(String pPCID) {
        this.pPCID = pPCID;
    }

    public FarmerUpdateXMLData getFarmerXMLData() {
        return farmerXMLData;
    }

    public void setFarmerXMLData(FarmerUpdateXMLData farmerXMLData) {
        this.farmerXMLData = farmerXMLData;
    }

    public ArrayList<FarmerUpdateLandXMLData> getLandXMLDataArrayList() {
        return landXMLDataArrayList;
    }

    public void setLandXMLDataArrayList(ArrayList<FarmerUpdateLandXMLData> landXMLDataArrayList) {
        this.landXMLDataArrayList = landXMLDataArrayList;
    }


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

    public String getXmlFarmerRegistrationDetails() {
        return xmlFarmerRegistrationDetails;
    }

    public void setXmlFarmerRegistrationDetails(String xmlFarmerRegistrationDetails) {
        this.xmlFarmerRegistrationDetails = xmlFarmerRegistrationDetails;
    }

}

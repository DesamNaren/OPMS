package com.cgg.pps.model.request.farmer.farmersubmit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FarmerSubmitRequest {

    @SerializedName("xmlTokenRegistrationData")
    @Expose
    private String xmlFarmerRegistrationData;
    @SerializedName("AuthenticationID")
    @Expose
    private String AuthenticationID;
    @SerializedName("PPCID")
    @Expose
    private String pPCID;

    private FarmerXMLData farmerXMLData;
    private ArrayList<FarmerLandXMLData> landXMLDataArrayList;

    public String getpPCID() {
        return pPCID;
    }

    public void setpPCID(String pPCID) {
        this.pPCID = pPCID;
    }

    public FarmerXMLData getFarmerXMLData() {
        return farmerXMLData;
    }

    public void setFarmerXMLData(FarmerXMLData farmerXMLData) {
        this.farmerXMLData = farmerXMLData;
    }

    public ArrayList<FarmerLandXMLData> getLandXMLDataArrayList() {
        return landXMLDataArrayList;
    }

    public void setLandXMLDataArrayList(ArrayList<FarmerLandXMLData> landXMLDataArrayList) {
        this.landXMLDataArrayList = landXMLDataArrayList;
    }

    public String getXmlFarmerRegistrationData() {
        return xmlFarmerRegistrationData;
    }

    public void setXmlFarmerRegistrationData(String xmlFarmerRegistrationData) {
        this.xmlFarmerRegistrationData = xmlFarmerRegistrationData;
    }


    public String getAuthenticationID() {
        return AuthenticationID;
    }

    public void setAuthenticationID(String authenticationID) {
        AuthenticationID = authenticationID;
    }
}

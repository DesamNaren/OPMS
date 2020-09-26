package com.cgg.pps.model.request.gunnydetails;

import java.util.ArrayList;

public class GunnyOrderRequest {
    private String AuthenticationID;
    private String PPCID;
    private String xmlIssuingGunnyData;
    private GunnyXMLData gunnyXMLData;
    private ArrayList<GunnyXMLData> gunnyXMLDataArrayList;

    public GunnyXMLData getGunnyXMLData() {
        return gunnyXMLData;
    }

    public void setGunnyXMLData(GunnyXMLData gunnyXMLData) {
        this.gunnyXMLData = gunnyXMLData;
    }

    public ArrayList<GunnyXMLData> getGunnyXMLDataArrayList() {
        return gunnyXMLDataArrayList;
    }

    public void setGunnyXMLDataArrayList(ArrayList<GunnyXMLData> gunnyXMLDataArrayList) {
        this.gunnyXMLDataArrayList = gunnyXMLDataArrayList;
    }

    public String getXmlIssuingGunnyData() {
        return xmlIssuingGunnyData;
    }

    public void setXmlIssuingGunnyData(String xmlIssuingGunnyData) {
        this.xmlIssuingGunnyData = xmlIssuingGunnyData;
    }

    public String getAuthenticationID() {
        return AuthenticationID;
    }

    public void setAuthenticationID(String authenticationID) {
        AuthenticationID = authenticationID;
    }

    public String getPPCID() {
        return PPCID;
    }

    public void setPPCID(String PPCID) {
        this.PPCID = PPCID;
    }
}

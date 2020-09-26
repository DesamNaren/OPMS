package com.cgg.pps.model.request.gunnydetails;

import java.util.ArrayList;

public class GunnysSubmitRequest {
    private String PPCID;
    private String AuthenticationID;
    private GunnyXMLData gunnyXMLData;
    private ArrayList<GunnyXMLData> gunnyXMLDataArrayList;
    private String xmlIssuingGunnyData;


    private String TokenNo;
    private String TokenID;
    private String RegistrationNo;
    private String RegistrationID;
    private String TransactionID;
    private String TransactionRowID;

    public String getTokenNo() {
        return TokenNo;
    }

    public void setTokenNo(String tokenNo) {
        TokenNo = tokenNo;
    }

    public String getTokenID() {
        return TokenID;
    }

    public void setTokenID(String tokenID) {
        TokenID = tokenID;
    }

    public String getRegistrationNo() {
        return RegistrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        RegistrationNo = registrationNo;
    }

    public String getRegistrationID() {
        return RegistrationID;
    }

    public void setRegistrationID(String registrationID) {
        RegistrationID = registrationID;
    }

    public String getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID(String transactionID) {
        TransactionID = transactionID;
    }

    public String getTransactionRowID() {
        return TransactionRowID;
    }

    public void setTransactionRowID(String transactionRowID) {
        TransactionRowID = transactionRowID;
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
}

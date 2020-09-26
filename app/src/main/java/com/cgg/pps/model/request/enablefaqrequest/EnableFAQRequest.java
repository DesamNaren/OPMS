package com.cgg.pps.model.request.enablefaqrequest;

public class EnableFAQRequest {
    private String AuthenticationID;
    private String PPCID;
    private String xmlEnableToFAQ;


    public String getXmlEnableToFAQ() {
        return xmlEnableToFAQ;
    }

    public void setXmlEnableToFAQ(String xmlEnableToFAQ) {
        this.xmlEnableToFAQ = xmlEnableToFAQ;
    }

    public String getPPCID() {
        return PPCID;
    }

    public void setPPCID(String PPCID) {
        this.PPCID = PPCID;
    }

    public String getAuthenticationID() {
        return AuthenticationID;
    }

    public void setAuthenticationID(String authenticationID) {
        AuthenticationID = authenticationID;
    }
}

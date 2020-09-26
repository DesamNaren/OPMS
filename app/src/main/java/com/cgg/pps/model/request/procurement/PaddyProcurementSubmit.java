package com.cgg.pps.model.request.procurement;

public class PaddyProcurementSubmit {
    private String AuthenticationID;
    private String PPCID;
    private PaddyXMLData paddyXMLData;
    private String xmlFarmerProcurementData;

    public PaddyXMLData getPaddyXMLData() {
        return paddyXMLData;
    }

    public void setPaddyXMLData(PaddyXMLData paddyXMLData) {
        this.paddyXMLData = paddyXMLData;
    }


    public String getXmlFarmerProcurementData() {
        return xmlFarmerProcurementData;
    }

    public void setXmlFarmerProcurementData(String xmlFarmerProcurementData) {
        this.xmlFarmerProcurementData = xmlFarmerProcurementData;
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

package com.cgg.pps.model.request.faq.faqsubmit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FAQRequest {

    @SerializedName("xmlFAQData")
    @Expose
    private String xmlFAQData;
    @SerializedName("AuthenticationID")
    @Expose
    private String AuthenticationID;
    @SerializedName("PPCID")
    @Expose
    private String pPCID;

    private FaqXMLData faqXMLData;

    public FaqXMLData getFaqXMLData() {
        return faqXMLData;
    }

    public void setFaqXMLData(FaqXMLData faqXMLData) {
        this.faqXMLData = faqXMLData;
    }

    public String getXmlFAQData() {
        return xmlFAQData;
    }

    public void setXmlFAQData(String xmlFAQData) {
        this.xmlFAQData = xmlFAQData;
    }

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
}

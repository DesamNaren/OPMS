package com.cgg.pps.model.request.truckchit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TCFinalSubmitRequest {

    @SerializedName("AuthenticationID")
    @Expose
    private String AuthenticationID;
    @SerializedName("PPCID")
    @Expose
    private Integer pPCID;
    @SerializedName("Truckchit")
    @Expose
    private String truckchit;
    @SerializedName("xmlTruckchitData")
    @Expose
    private String xmlTruckchitData;
    @SerializedName("uploadTRImage")
    @Expose
    private String uploadTRImage;


    public String getAuthenticationID() {
        return AuthenticationID;
    }

    public void setAuthenticationID(String authenticationID) {
        AuthenticationID = authenticationID;
    }

    public Integer getpPCID() {
        return pPCID;
    }

    public void setpPCID(Integer pPCID) {
        this.pPCID = pPCID;
    }

    public String getTruckchit() {
        return truckchit;
    }

    public void setTruckchit(String truckchit) {
        this.truckchit = truckchit;
    }

    public String getXmlTruckchitData() {
        return xmlTruckchitData;
    }

    public void setXmlTruckchitData(String xmlTruckchitData) {
        this.xmlTruckchitData = xmlTruckchitData;
    }

    public String getUploadTRImage() {
        return uploadTRImage;
    }

    public void setUploadTRImage(String uploadTRImage) {
        this.uploadTRImage = uploadTRImage;
    }
}
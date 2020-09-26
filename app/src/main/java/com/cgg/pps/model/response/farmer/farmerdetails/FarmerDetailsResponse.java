package com.cgg.pps.model.response.farmer.farmerdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class FarmerDetailsResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("FarmerLandDetail")
    @Expose
    private List<FarmerLandDetail> farmerLandDetail = null;
    @SerializedName("FarmerBasicDetail")
    @Expose
    private FarmerBasicDetail farmerBasicDetail;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public List<FarmerLandDetail> getFarmerLandDetail() {
        return farmerLandDetail;
    }

    public void setFarmerLandDetail(List<FarmerLandDetail> farmerLandDetail) {
        this.farmerLandDetail = farmerLandDetail;
    }

    public FarmerBasicDetail getFarmerBasicDetail() {
        return farmerBasicDetail;
    }

    public void setFarmerBasicDetail(FarmerBasicDetail farmerBasicDetail) {
        this.farmerBasicDetail = farmerBasicDetail;
    }

}



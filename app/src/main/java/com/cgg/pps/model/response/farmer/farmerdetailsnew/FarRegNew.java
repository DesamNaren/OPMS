package com.cgg.pps.model.response.farmer.farmerdetailsnew;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarRegNew {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("FarmerLandDetail")
    @Expose
    private List<FarmerLandDetailNew> farmerLandDetail = null;
    @SerializedName("FarmerBasicDetail")
    @Expose
    private FarmerBasicDetailNew farmerBasicDetail;

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

    public List<FarmerLandDetailNew> getFarmerLandDetail() {
        return farmerLandDetail;
    }

    public void setFarmerLandDetail(List<FarmerLandDetailNew> farmerLandDetail) {
        this.farmerLandDetail = farmerLandDetail;
    }

    public FarmerBasicDetailNew getFarmerBasicDetail() {
        return farmerBasicDetail;
    }

    public void setFarmerBasicDetail(FarmerBasicDetailNew farmerBasicDetail) {
        this.farmerBasicDetail = farmerBasicDetail;
    }

}



package com.cgg.pps.model.response.devicemgmt.masterdistrict;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MasterDistrictMainResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("masterDistrictResponse")
    @Expose
    private List<DistrictEntity> districtEntity = null;

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

    public List<DistrictEntity> getDistrictEntity() {
        return districtEntity;
    }

    public void setDistrictEntity(List<DistrictEntity> districtEntity) {
        this.districtEntity = districtEntity;
    }

}


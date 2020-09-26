package com.cgg.pps.model.response.devicemgmt.mastermandal;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MasterMandalMainResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("masterMandalsResponse")
    @Expose
    private List<MandalEntity> mandalEntity = null;

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

    public List<MandalEntity> getMandalEntity() {
        return mandalEntity;
    }

    public void setMandalEntity(List<MandalEntity> mandalEntity) {
        this.mandalEntity = mandalEntity;
    }

}


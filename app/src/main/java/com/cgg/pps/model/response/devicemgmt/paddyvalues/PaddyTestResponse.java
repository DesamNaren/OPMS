package com.cgg.pps.model.response.devicemgmt.paddyvalues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PaddyTestResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("PaddyDetailsMasterResponse")
    @Expose
    private List<PaddyEntity> paddyEntity = null;

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

    public List<PaddyEntity> getPaddyEntity() {
        return paddyEntity;
    }

    public void setPaddyEntity(List<PaddyEntity> paddyEntity) {
        this.paddyEntity = paddyEntity;
    }



}

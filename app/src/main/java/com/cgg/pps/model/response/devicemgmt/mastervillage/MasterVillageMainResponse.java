package com.cgg.pps.model.response.devicemgmt.mastervillage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MasterVillageMainResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("districtMandalViallgeResponse")
    @Expose
    private List<VillageEntity> villageEntity = null;

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

    public List<VillageEntity> getVillageEntity() {
        return villageEntity;
    }

    public void setVillageEntity(List<VillageEntity> villageEntity) {
        this.villageEntity = villageEntity;
    }

}

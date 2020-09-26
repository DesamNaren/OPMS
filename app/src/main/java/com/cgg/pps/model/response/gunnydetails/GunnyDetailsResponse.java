package com.cgg.pps.model.response.gunnydetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GunnyDetailsResponse {
    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("gunnyResponse")
    @Expose
    private List<GunnyResponse> gunnyResponse = null;

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

    public List<GunnyResponse> getGunnyResponse() {
        return gunnyResponse;
    }

    public void setGunnyResponse(List<GunnyResponse> gunnyResponse) {
        this.gunnyResponse = gunnyResponse;
    }

}


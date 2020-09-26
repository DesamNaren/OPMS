package com.cgg.pps.model.response.procurement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IssuedGunnyDataResponse {
    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("issuedGunnyResponse")
    @Expose
    private List<IssuedGunnyResponse> issuedGunnyResponse = null;

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

    public List<IssuedGunnyResponse> getIssuedGunnyResponse() {
        return issuedGunnyResponse;
    }

    public void setIssuedGunnyResponse(List<IssuedGunnyResponse> issuedGunnyResponse) {
        this.issuedGunnyResponse = issuedGunnyResponse;
    }
}

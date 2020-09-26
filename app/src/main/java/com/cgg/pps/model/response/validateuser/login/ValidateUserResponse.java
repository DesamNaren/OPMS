package com.cgg.pps.model.response.validateuser.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ValidateUserResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("pPCUserDetails")
    @Expose
    private PPCUserDetails pPCUserDetails;


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

    public PPCUserDetails getPPCUserDetails() {
        return pPCUserDetails;
    }

    public void setPPCUserDetails(PPCUserDetails pPCUserDetails) {
        this.pPCUserDetails = pPCUserDetails;
    }

}

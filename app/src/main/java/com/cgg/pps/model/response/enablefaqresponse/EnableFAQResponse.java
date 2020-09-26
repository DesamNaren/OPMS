package com.cgg.pps.model.response.enablefaqresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EnableFAQResponse {
    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("tokenRejectionOutput")
    @Expose
    private List<EnableFAQResponseOutput> tokenRejectionOutput = null;

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

    public List<EnableFAQResponseOutput> getTokenRejectionOutput() {
        return tokenRejectionOutput;
    }

    public void setTokenRejectionOutput(List<EnableFAQResponseOutput> tokenRejectionOutput) {
        this.tokenRejectionOutput = tokenRejectionOutput;
    }
}

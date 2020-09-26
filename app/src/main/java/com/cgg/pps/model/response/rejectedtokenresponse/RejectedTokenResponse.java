package com.cgg.pps.model.response.rejectedtokenresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RejectedTokenResponse {
    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("tokenRejectionOutput")
    @Expose
    private List<TokenRejectionOutput> tokenRejectionOutput = null;

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

    public List<TokenRejectionOutput> getTokenRejectionOutput() {
        return tokenRejectionOutput;
    }

    public void setTokenRejectionOutput(List<TokenRejectionOutput> tokenRejectionOutput) {
        this.tokenRejectionOutput = tokenRejectionOutput;
    }

}


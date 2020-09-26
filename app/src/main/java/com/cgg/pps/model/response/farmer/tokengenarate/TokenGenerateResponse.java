package com.cgg.pps.model.response.farmer.tokengenarate;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TokenGenerateResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("tokenDetails")
    @Expose
    private List<TokenDetail> tokenDetails = null;

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

    public List<TokenDetail> getTokenDetails() {
        return tokenDetails;
    }

    public void setTokenDetails(List<TokenDetail> tokenDetails) {
        this.tokenDetails = tokenDetails;
    }

}
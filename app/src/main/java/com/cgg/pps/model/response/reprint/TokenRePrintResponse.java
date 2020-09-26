package com.cgg.pps.model.response.reprint;

import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokenOutput;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenRePrintResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("getTokenOutput")
    @Expose
    private List<GetTokenOutput> getTokenOutput = null;

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

    public List<GetTokenOutput> getGetTokenOutput() {
        return getTokenOutput;
    }

    public void setGetTokenOutput(List<GetTokenOutput> getTokenOutput) {
        this.getTokenOutput = getTokenOutput;
    }
}
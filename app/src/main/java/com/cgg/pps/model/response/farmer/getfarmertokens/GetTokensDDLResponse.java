package com.cgg.pps.model.response.farmer.getfarmertokens;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTokensDDLResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("GetTokensforFarmer")
    @Expose
    private List<GetDDLTokens> getDDLTokens = null;

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

    public List<GetDDLTokens> getGetDDLTokens() {
        return getDDLTokens;
    }

    public void setGetDDLTokens(List<GetDDLTokens> getDDLTokens) {
        this.getDDLTokens = getDDLTokens;
    }
}


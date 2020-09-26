package com.cgg.pps.model.response.farmer.season10;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFarmerTokensResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("getTokenBindData")
    @Expose
    private List<GetFarmerTokenData> getFarmerTokenData = null;

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

    public List<GetFarmerTokenData> getGetFarmerTokenData() {
        return getFarmerTokenData;
    }

    public void setGetFarmerTokenData(List<GetFarmerTokenData> getFarmerTokenData) {
        this.getFarmerTokenData = getFarmerTokenData;
    }
}


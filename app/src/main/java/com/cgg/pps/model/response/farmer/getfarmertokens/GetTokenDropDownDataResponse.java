package com.cgg.pps.model.response.farmer.getfarmertokens;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetTokenDropDownDataResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("getTokenBindData")
    @Expose
    private List<GetTokenDropDownData> getTokenDropDownData = null;

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

    public List<GetTokenDropDownData> getGetTokenDropDownData() {
        return getTokenDropDownData;
    }

    public void setGetTokenDropDownData(List<GetTokenDropDownData> getTokenDropDownData) {
        this.getTokenDropDownData = getTokenDropDownData;
    }
}

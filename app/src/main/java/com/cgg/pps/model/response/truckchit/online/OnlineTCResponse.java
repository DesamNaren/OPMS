package com.cgg.pps.model.response.truckchit.online;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnlineTCResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("getOnlineTruckchitNumber")
    @Expose
    private GetOnlineTruckchitNumber getOnlineTruckchitNumber;

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

    public GetOnlineTruckchitNumber getGetOnlineTruckchitNumber() {
        return getOnlineTruckchitNumber;
    }

    public void setGetOnlineTruckchitNumber(GetOnlineTruckchitNumber getOnlineTruckchitNumber) {
        this.getOnlineTruckchitNumber = getOnlineTruckchitNumber;
    }

}

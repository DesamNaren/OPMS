package com.cgg.pps.model.response.reprint.truckchit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TCRePrintResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("gettruck")
    @Expose
    private List<Gettruck> gettruck = null;

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

    public List<Gettruck> getGettruck() {
        return gettruck;
    }

    public void setGettruck(List<Gettruck> gettruck) {
        this.gettruck = gettruck;
    }

}

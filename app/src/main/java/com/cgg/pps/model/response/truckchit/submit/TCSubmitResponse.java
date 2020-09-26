package com.cgg.pps.model.response.truckchit.submit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TCSubmitResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("saveTruckChitOutput")
    @Expose
    private SaveTruckChitOutput saveTruckChitOutput;

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

    public SaveTruckChitOutput getSaveTruckChitOutput() {
        return saveTruckChitOutput;
    }

    public void setSaveTruckChitOutput(SaveTruckChitOutput saveTruckChitOutput) {
        this.saveTruckChitOutput = saveTruckChitOutput;
    }

}

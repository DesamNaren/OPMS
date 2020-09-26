package com.cgg.pps.model.response.truckchit.manual;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ManualTCResponse {
    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("uniqueManualTruckchitCount")
    @Expose
    private UniqueManualTruckchitCount uniqueManualTruckchitCount;

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

    public UniqueManualTruckchitCount getUniqueManualTruckchitCount() {
        return uniqueManualTruckchitCount;
    }

    public void setUniqueManualTruckchitCount(UniqueManualTruckchitCount uniqueManualTruckchitCount) {
        this.uniqueManualTruckchitCount = uniqueManualTruckchitCount;
    }


}


package com.cgg.pps.model.response.truckchit.mastervehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransportResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("masterTransporterDetails")
    @Expose
    private List<TransporterDetail> transporterDetails = null;

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

    public List<TransporterDetail> getTransporterDetails() {
        return transporterDetails;
    }

    public void setTransporterDetails(List<TransporterDetail> transporterDetails) {
        this.transporterDetails = transporterDetails;
    }

}

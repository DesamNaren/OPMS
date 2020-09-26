package com.cgg.pps.model.response.truckchit.vehicle_type;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleTypesResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("transporterVehicleType")
    @Expose
    private List<TransporterVehicleType> transporterVehicleType = null;

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

    public List<TransporterVehicleType> getTransporterVehicleType() {
        return transporterVehicleType;
    }

    public void setTransporterVehicleType(List<TransporterVehicleType> transporterVehicleType) {
        this.transporterVehicleType = transporterVehicleType;
    }

}
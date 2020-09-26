package com.cgg.pps.model.response.truckchit.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ROInfoResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("rooutput")
    @Expose
    private RODetails rODetails;
    @SerializedName("DetailedRoInfoOutput")
    @Expose
    private List<DetailedRoInfoOutput> detailedRoInfoOutput = null;

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

    public RODetails getRODetails() {
        return rODetails;
    }

    public void setRODetails(RODetails rODetails) {
        this.rODetails = rODetails;
    }

    public List<DetailedRoInfoOutput> getDetailedRoInfoOutput() {
        return detailedRoInfoOutput;
    }

    public void setDetailedRoInfoOutput(List<DetailedRoInfoOutput> detailedRoInfoOutput) {
        this.detailedRoInfoOutput = detailedRoInfoOutput;
    }

}

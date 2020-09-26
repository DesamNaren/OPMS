package com.cgg.pps.model.response.farmer.farmersubmit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmerSubmitResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("saveTokenRegistrationOutput")
    @Expose
    private Object saveTokenRegistrationOutput;

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

    public Object getSaveTokenRegistrationOutput() {
        return saveTokenRegistrationOutput;
    }

    public void setSaveTokenRegistrationOutput(Object saveTokenRegistrationOutput) {
        this.saveTokenRegistrationOutput = saveTokenRegistrationOutput;
    }

}

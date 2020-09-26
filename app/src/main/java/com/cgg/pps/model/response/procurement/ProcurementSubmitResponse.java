package com.cgg.pps.model.response.procurement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProcurementSubmitResponse {
    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("saveProcurementOutput")
    @Expose
    private SaveProcurementOutput saveProcurementOutput;

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

    public SaveProcurementOutput getSaveProcurementOutput() {
        return saveProcurementOutput;
    }

    public void setSaveProcurementOutput(SaveProcurementOutput saveProcurementOutput) {
        this.saveProcurementOutput = saveProcurementOutput;
    }

}


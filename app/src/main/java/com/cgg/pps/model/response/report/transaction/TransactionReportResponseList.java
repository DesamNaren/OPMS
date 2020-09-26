package com.cgg.pps.model.response.report.transaction;

import com.cgg.pps.model.response.report.Report;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TransactionReportResponseList extends Report {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("getTranscations")
    @Expose
    private List<TransactionReportResponse> getTranscations = null;

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

    public List<TransactionReportResponse> getGetTranscations() {
        return getTranscations;
    }

    public void setGetTranscations(List<TransactionReportResponse> getTranscations) {
        this.getTranscations = getTranscations;
    }


}

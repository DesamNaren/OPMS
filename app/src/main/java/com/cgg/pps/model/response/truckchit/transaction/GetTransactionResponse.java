package com.cgg.pps.model.response.truckchit.transaction;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTransactionResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("getTranscations")
    @Expose
    private List<TransactionsData> transactionsData = null;

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

    public List<TransactionsData> getTransactionsData() {
        return transactionsData;
    }

    public void setTransactionsData(List<TransactionsData> transactionsData) {
        this.transactionsData = transactionsData;
    }

}


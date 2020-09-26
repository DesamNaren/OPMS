package com.cgg.pps.model.response.devicemgmt.masterbank;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MasterBankMainResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("masterBankResponse")
    @Expose
    private List<BankEntity> bankEntity = null;

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

    public List<BankEntity> getBankEntity() {
        return bankEntity;
    }

    public void setBankEntity(List<BankEntity> bankEntity) {
        this.bankEntity = bankEntity;
    }

}


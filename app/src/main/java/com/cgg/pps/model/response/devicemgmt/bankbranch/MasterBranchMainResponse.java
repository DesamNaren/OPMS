package com.cgg.pps.model.response.devicemgmt.bankbranch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MasterBranchMainResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("bankResponse")
    @Expose
    private List<BranchEntity> bankResponse = null;

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

    public List<BranchEntity> getBankResponse() {
        return bankResponse;
    }

    public void setBankResponse(List<BranchEntity> bankResponse) {
        this.bankResponse = bankResponse;
    }

}

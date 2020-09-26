package com.cgg.pps.model.response.report.payment_report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PaddyPaymentInfoResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("paddyPaymentInput")
    @Expose
    private String paddyPaymentInput;
    @SerializedName("PaddyPaymentOutput")
    @Expose
    private List<PaddyPaymentOutput> paddyPaymentOutput = null;

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

    public String getPaddyPaymentInput() {
        return paddyPaymentInput;
    }

    public void setPaddyPaymentInput(String paddyPaymentInput) {
        this.paddyPaymentInput = paddyPaymentInput;
    }

    public List<PaddyPaymentOutput> getPaddyPaymentOutput() {
        return paddyPaymentOutput;
    }

    public void setPaddyPaymentOutput(List<PaddyPaymentOutput> paddyPaymentOutput) {
        this.paddyPaymentOutput = paddyPaymentOutput;
    }

}

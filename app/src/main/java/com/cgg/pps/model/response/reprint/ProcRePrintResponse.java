package com.cgg.pps.model.response.reprint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProcRePrintResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("Printprocurementdata")
    @Expose
    private Printprocurementdata printprocurementdata;
    @SerializedName("printtruckchitdata")
    @Expose
    private String printtruckchitdata;


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

    public Printprocurementdata getPrintprocurementdata() {
        return printprocurementdata;
    }

    public void setPrintprocurementdata(Printprocurementdata printprocurementdata) {
        this.printprocurementdata = printprocurementdata;
    }

    public String getPrinttruckchitdata() {
        return printtruckchitdata;
    }

    public void setPrinttruckchitdata(String printtruckchitdata) {
        this.printtruckchitdata = printtruckchitdata;
    }
}

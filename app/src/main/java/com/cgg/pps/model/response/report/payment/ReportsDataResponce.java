package com.cgg.pps.model.response.report.payment;

import com.cgg.pps.model.response.report.gunny.GunnyReportResponse;
import com.cgg.pps.model.response.report.mill.MillReportResponse;
import com.cgg.pps.model.response.report.truckchit.TCDetailReportResponse;
import com.cgg.pps.model.response.report.truckchit.TCReportResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReportsDataResponce {
    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("getFarmerPaymentStatusData")
    @Expose
    private List<PaymentReportResponse> getFarmerPaymentStatusData = null;
    @SerializedName("getReportTokenwiseGunnyData")
    @Expose
    private List<GunnyReportResponse> getReportTokenwiseGunnyData = null;
    @SerializedName("getReportAllocatedMillwiseQuantityData")
    @Expose
    private List<MillReportResponse> getReportAllocatedMillwiseQuantityData;


    @SerializedName("getTruckchitDataReport")
    @Expose
    private List<TCReportResponse> getTruckchitDataReport;

//    @SerializedName("getAllotedROwiseQuantityDataReport")
//    @Expose
//    private Object getAllotedROwiseQuantityDataReport;

    @SerializedName("getTruckchitTransactionDataReport")
    @Expose
    private List<TCDetailReportResponse> getTruckchitTransactionDataReport;

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


    public List<PaymentReportResponse> getGetFarmerPaymentStatusData() {
        return getFarmerPaymentStatusData;
    }

    public void setGetFarmerPaymentStatusData(List<PaymentReportResponse> getFarmerPaymentStatusData) {
        this.getFarmerPaymentStatusData = getFarmerPaymentStatusData;
    }

    public List<GunnyReportResponse> getGetReportTokenwiseGunnyData() {
        return getReportTokenwiseGunnyData;
    }

    public void setGetReportTokenwiseGunnyData(List<GunnyReportResponse> getReportTokenwiseGunnyData) {
        this.getReportTokenwiseGunnyData = getReportTokenwiseGunnyData;
    }

    public List<MillReportResponse> getGetReportAllocatedMillwiseQuantityData() {
        return getReportAllocatedMillwiseQuantityData;
    }

    public void setGetReportAllocatedMillwiseQuantityData(List<MillReportResponse> getReportAllocatedMillwiseQuantityData) {
        this.getReportAllocatedMillwiseQuantityData = getReportAllocatedMillwiseQuantityData;
    }

    public List<TCReportResponse> getGetTruckchitDataReport() {
        return getTruckchitDataReport;
    }

    public void setGetTruckchitDataReport(List<TCReportResponse> getTruckchitDataReport) {
        this.getTruckchitDataReport = getTruckchitDataReport;
    }

    public List<TCDetailReportResponse> getGetTruckchitTransactionDataReport() {
        return getTruckchitTransactionDataReport;
    }

    public void setGetTruckchitTransactionDataReport(List<TCDetailReportResponse> getTruckchitTransactionDataReport) {
        this.getTruckchitTransactionDataReport = getTruckchitTransactionDataReport;
    }
}


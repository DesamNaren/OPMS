package com.cgg.pps.model.response.report.truckchit;

import java.io.Serializable;

public class TCDetailReportResponse implements Serializable {
    private String TRID;
    private String TRNo;
    private String TRManualNo;
    private String FarmerTransactionID;
    private String Status;
    private String StatusDescription;
    private String bagSentNew;
    private String bagSentOld;
    private String bagSentTotal;
    private String quantitySentGradeA;
    private String quantitySentGradeCommon;
    private String Output;

    public String getTRID() {
        return TRID;
    }

    public void setTRID(String TRID) {
        this.TRID = TRID;
    }

    public String getTRNo() {
        return TRNo;
    }

    public void setTRNo(String TRNo) {
        this.TRNo = TRNo;
    }

    public String getTRManualNo() {
        return TRManualNo;
    }

    public void setTRManualNo(String TRManualNo) {
        this.TRManualNo = TRManualNo;
    }

    public String getFarmerTransactionID() {
        return FarmerTransactionID;
    }

    public void setFarmerTransactionID(String farmerTransactionID) {
        FarmerTransactionID = farmerTransactionID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStatusDescription() {
        return StatusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        StatusDescription = statusDescription;
    }

    public String getBagSentNew() {
        return bagSentNew;
    }

    public void setBagSentNew(String bagSentNew) {
        this.bagSentNew = bagSentNew;
    }

    public String getBagSentOld() {
        return bagSentOld;
    }

    public void setBagSentOld(String bagSentOld) {
        this.bagSentOld = bagSentOld;
    }

    public String getBagSentTotal() {
        return bagSentTotal;
    }

    public void setBagSentTotal(String bagSentTotal) {
        this.bagSentTotal = bagSentTotal;
    }

    public String getQuantitySentGradeA() {
        return quantitySentGradeA;
    }

    public void setQuantitySentGradeA(String quantitySentGradeA) {
        this.quantitySentGradeA = quantitySentGradeA;
    }

    public String getQuantitySentGradeCommon() {
        return quantitySentGradeCommon;
    }

    public void setQuantitySentGradeCommon(String quantitySentGradeCommon) {
        this.quantitySentGradeCommon = quantitySentGradeCommon;
    }

    public String getOutput() {
        return Output;
    }

    public void setOutput(String output) {
        Output = output;
    }
}
package com.cgg.pps.model.response.truckchit.ro;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MillerData {

    @SerializedName("MillerID")
    @Expose
    private Long millerID;
    @SerializedName("millerCode")
    @Expose
    private String millerCode;
    @SerializedName("MillName")
    @Expose
    private String millName;
    @SerializedName("MillAddress")
    @Expose
    private String millAddress;
    @SerializedName("ProprietorName")
    @Expose
    private String proprietorName;
    @SerializedName("ProprietorMobile")
    @Expose
    private String proprietorMobile;
    @SerializedName("QuantityAllotedToPPC")
    @Expose
    private Double quantityAllotedToPPC;
    @SerializedName("ApprovalFromDM")
    @Expose
    private String approvalFromDM;
    @SerializedName("DistanceFromPPC")
    @Expose
    private Double distanceFromPPC;
    @SerializedName("Ro_Number")
    @Expose
    private String roNumber;
    @SerializedName("RO_ID")
    @Expose
    private Long rOID;

    public Long getMillerID() {
        return millerID;
    }

    public void setMillerID(Long millerID) {
        this.millerID = millerID;
    }

    public String getMillerCode() {
        return millerCode;
    }

    public void setMillerCode(String millerCode) {
        this.millerCode = millerCode;
    }

    public String getMillName() {
        return millName;
    }

    public void setMillName(String millName) {
        this.millName = millName;
    }

    public String getMillAddress() {
        return millAddress;
    }

    public void setMillAddress(String millAddress) {
        this.millAddress = millAddress;
    }

    public String getProprietorName() {
        return proprietorName;
    }

    public void setProprietorName(String proprietorName) {
        this.proprietorName = proprietorName;
    }

    public String getProprietorMobile() {
        return proprietorMobile;
    }

    public void setProprietorMobile(String proprietorMobile) {
        this.proprietorMobile = proprietorMobile;
    }

    public Double getQuantityAllotedToPPC() {
        return quantityAllotedToPPC;
    }

    public void setQuantityAllotedToPPC(Double quantityAllotedToPPC) {
        this.quantityAllotedToPPC = quantityAllotedToPPC;
    }

    public String getApprovalFromDM() {
        return approvalFromDM;
    }

    public void setApprovalFromDM(String approvalFromDM) {
        this.approvalFromDM = approvalFromDM;
    }

    public Double getDistanceFromPPC() {
        return distanceFromPPC;
    }

    public void setDistanceFromPPC(Double distanceFromPPC) {
        this.distanceFromPPC = distanceFromPPC;
    }

    public String getRoNumber() {
        return roNumber;
    }

    public void setRoNumber(String roNumber) {
        this.roNumber = roNumber;
    }

    public Long getROID() {
        return rOID;
    }

    public void setROID(Long rOID) {
        this.rOID = rOID;
    }

}

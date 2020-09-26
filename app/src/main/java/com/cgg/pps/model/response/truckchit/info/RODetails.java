package com.cgg.pps.model.response.truckchit.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RODetails {
    @SerializedName("MillerCode")
    @Expose
    private String millerCode;

    @SerializedName("MillName")
    @Expose
    private String millName;
    @SerializedName("Ro_Number")
    @Expose
    private String roNumber;
    @SerializedName("RO_ID")
    @Expose
    private String roID;
    @SerializedName("ppcname")
    @Expose
    private String ppcName;
    @SerializedName("AllotedDate")
    @Expose
    private String allotedDate;
    @SerializedName("AllotedQuantity")
    @Expose
    private Double allotedQuantity;
    @SerializedName("ModifiedQuantity")
    @Expose
    private Double modifiedQuantity;

    public String getMillerCode() {
        return millerCode;
    }

    public void setMillerCode(String millerCode) {
        this.millerCode = millerCode;
    }

    public String getRoID() {
        return roID;
    }

    public void setRoID(String roID) {
        this.roID = roID;
    }

    public String getPpcName() {
        return ppcName;
    }

    public void setPpcName(String ppcName) {
        this.ppcName = ppcName;
    }

    public String getMillName() {
        return millName;
    }

    public void setMillName(String millName) {
        this.millName = millName;
    }

    public String getRoNumber() {
        return roNumber;
    }

    public void setRoNumber(String roNumber) {
        this.roNumber = roNumber;
    }

    public String getAllotedDate() {
        return allotedDate;
    }

    public void setAllotedDate(String allotedDate) {
        this.allotedDate = allotedDate;
    }

    public Double getAllotedQuantity() {
        return allotedQuantity;
    }

    public void setAllotedQuantity(Double allotedQuantity) {
        this.allotedQuantity = allotedQuantity;
    }

    public Double getModifiedQuantity() {
        return modifiedQuantity;
    }

    public void setModifiedQuantity(Double modifiedQuantity) {
        this.modifiedQuantity = modifiedQuantity;
    }

}


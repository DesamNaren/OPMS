package com.cgg.pps.model.response.rejectedtokenresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenRejectionOutput {

    @SerializedName("PPCID")
    @Expose
    private Integer pPCID;
    @SerializedName("TokenID")
    @Expose
    private Integer tokenID;
    @SerializedName("TokenNo")
    @Expose
    private String tokenNo;
    @SerializedName("FarmerRegID")
    @Expose
    private Long farmerRegID;
    @SerializedName("RegistrationNo")
    @Expose
    private String registrationNo;
    @SerializedName("FAQDate")
    @Expose
    private String fAQDate;
    @SerializedName("FAQType")
    @Expose
    private String fAQType;
    @SerializedName("PaddyType")
    @Expose
    private Integer paddyType;
    @SerializedName("Quantity")
    @Expose
    private Double quantity;
    @SerializedName("Wastage")
    @Expose
    private Double wastage;
    @SerializedName("OtherEatingGrains")
    @Expose
    private Double otherEatingGrains;
    @SerializedName("UnmaturedGrains")
    @Expose
    private Double unmaturedGrains;
    @SerializedName("DamagedGrains")
    @Expose
    private Double damagedGrains;
    @SerializedName("LowGradeGrain")
    @Expose
    private Double lowGradeGrain;
    @SerializedName("Moisture")
    @Expose
    private Double moisture;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("SystemIP")
    @Expose
    private String systemIP;
    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("UpdatedBy")
    @Expose
    private String updatedBy;
    @SerializedName("UpdatedDate")
    @Expose
    private String updatedDate;

    public Integer getPPCID() {
        return pPCID;
    }

    public void setPPCID(Integer pPCID) {
        this.pPCID = pPCID;
    }

    public Integer getTokenID() {
        return tokenID;
    }

    public void setTokenID(Integer tokenID) {
        this.tokenID = tokenID;
    }

    public String getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    public Long getFarmerRegID() {
        return farmerRegID;
    }

    public void setFarmerRegID(Long farmerRegID) {
        this.farmerRegID = farmerRegID;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getFAQDate() {
        return fAQDate;
    }

    public void setFAQDate(String fAQDate) {
        this.fAQDate = fAQDate;
    }

    public String getFAQType() {
        return fAQType;
    }

    public void setFAQType(String fAQType) {
        this.fAQType = fAQType;
    }

    public Integer getPaddyType() {
        return paddyType;
    }

    public void setPaddyType(Integer paddyType) {
        this.paddyType = paddyType;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getWastage() {
        return wastage;
    }

    public void setWastage(Double wastage) {
        this.wastage = wastage;
    }

    public Double getOtherEatingGrains() {
        return otherEatingGrains;
    }

    public void setOtherEatingGrains(Double otherEatingGrains) {
        this.otherEatingGrains = otherEatingGrains;
    }

    public Double getUnmaturedGrains() {
        return unmaturedGrains;
    }

    public void setUnmaturedGrains(Double unmaturedGrains) {
        this.unmaturedGrains = unmaturedGrains;
    }

    public Double getDamagedGrains() {
        return damagedGrains;
    }

    public void setDamagedGrains(Double damagedGrains) {
        this.damagedGrains = damagedGrains;
    }

    public Double getLowGradeGrain() {
        return lowGradeGrain;
    }

    public void setLowGradeGrain(Double lowGradeGrain) {
        this.lowGradeGrain = lowGradeGrain;
    }

    public Double getMoisture() {
        return moisture;
    }

    public void setMoisture(Double moisture) {
        this.moisture = moisture;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSystemIP() {
        return systemIP;
    }

    public void setSystemIP(String systemIP) {
        this.systemIP = systemIP;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

}

package com.cgg.pps.model.response.gunnydetails.gunnysubmit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IssuedGunnyResponse {

    @SerializedName("PPCID")
    @Expose
    private Integer pPCID;
    @SerializedName("TokenID")
    @Expose
    private Integer tokenID;
    @SerializedName("TokenNo")
    @Expose
    private String tokenNo;
    @SerializedName("RegID")
    @Expose
    private Long regID;
    @SerializedName("RegistrationNo")
    @Expose
    private String registrationNo;
    @SerializedName("FarmerTransactionRowID")
    @Expose
    private Long farmerTransactionRowID;
    @SerializedName("FarmerTransactionID")
    @Expose
    private String farmerTransactionID;
    @SerializedName("GunnyOrderID")
    @Expose
    private Long gunnyOrderID;
    @SerializedName("TokenNewBags")
    @Expose
    private Integer tokenNewBags;
    @SerializedName("TokenOldBags")
    @Expose
    private Integer tokenOldBags;
    @SerializedName("TokenTotalBags")
    @Expose
    private Integer tokenTotalBags;
    @SerializedName("ProcuredNewBags")
    @Expose
    private Integer procuredNewBags;
    @SerializedName("ProcuredOldBags")
    @Expose
    private Integer procuredOldBags;
    @SerializedName("SeasonID")
    @Expose
    private Integer seasonID;
    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;

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

    public Long getRegID() {
        return regID;
    }

    public void setRegID(Long regID) {
        this.regID = regID;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public Long getFarmerTransactionRowID() {
        return farmerTransactionRowID;
    }

    public void setFarmerTransactionRowID(Long farmerTransactionRowID) {
        this.farmerTransactionRowID = farmerTransactionRowID;
    }

    public String getFarmerTransactionID() {
        return farmerTransactionID;
    }

    public void setFarmerTransactionID(String farmerTransactionID) {
        this.farmerTransactionID = farmerTransactionID;
    }

    public Long getGunnyOrderID() {
        return gunnyOrderID;
    }

    public void setGunnyOrderID(Long gunnyOrderID) {
        this.gunnyOrderID = gunnyOrderID;
    }

    public Integer getTokenNewBags() {
        return tokenNewBags;
    }

    public void setTokenNewBags(Integer tokenNewBags) {
        this.tokenNewBags = tokenNewBags;
    }

    public Integer getTokenOldBags() {
        return tokenOldBags;
    }

    public void setTokenOldBags(Integer tokenOldBags) {
        this.tokenOldBags = tokenOldBags;
    }

    public Integer getTokenTotalBags() {
        return tokenTotalBags;
    }

    public void setTokenTotalBags(Integer tokenTotalBags) {
        this.tokenTotalBags = tokenTotalBags;
    }

    public Integer getProcuredNewBags() {
        return procuredNewBags;
    }

    public void setProcuredNewBags(Integer procuredNewBags) {
        this.procuredNewBags = procuredNewBags;
    }

    public Integer getProcuredOldBags() {
        return procuredOldBags;
    }

    public void setProcuredOldBags(Integer procuredOldBags) {
        this.procuredOldBags = procuredOldBags;
    }

    public Integer getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(Integer seasonID) {
        this.seasonID = seasonID;
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

}

package com.cgg.pps.model.response.farmer.tokengenarate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenDetail {

    @SerializedName("TokenNo")
    @Expose
    private String tokenNo;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("FarmerName")
    @Expose
    private String farmerName;
    @SerializedName("AppointmentDate")
    @Expose
    private String appointmentDate;
    @SerializedName("SystemIP")
    @Expose
    private String systemIP;
    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("SeasonID")
    @Expose
    private Integer seasonID;
    @SerializedName("Year")
    @Expose
    private Integer year;
    @SerializedName("TokenStatus")
    @Expose
    private Integer tokenStatus;

    public String getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
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

    public Integer getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(Integer seasonID) {
        this.seasonID = seasonID;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getTokenStatus() {
        return tokenStatus;
    }

    public void setTokenStatus(Integer tokenStatus) {
        this.tokenStatus = tokenStatus;
    }
}

package com.cgg.pps.model.response.farmer.season10;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFarmerTokenData {

    @SerializedName("TokenID")
    @Expose
    private Integer tokenID;
    @SerializedName("TokenNo")
    @Expose
    private String tokenNo;
    @SerializedName("FarmerName")
    @Expose
    private String farmerName;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("AppointmentDate")
    @Expose
    private String appointmentDate;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;

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

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}

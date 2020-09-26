package com.cgg.pps.model.response.truckchit.mastervehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransporterDetail {

    @SerializedName("TransporterID")
    @Expose
    private Long transporterID;
    @SerializedName("TransporterCode")
    @Expose
    private String transporterCode;
    @SerializedName("TransportName")
    @Expose
    private String transportName;
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("HouseNo")
    @Expose
    private String houseNo;
    @SerializedName("StreetName")
    @Expose
    private String streetName;
    @SerializedName("DistrictID")
    @Expose
    private String districtID;
    @SerializedName("MandalID")
    @Expose
    private String mandalID;
    @SerializedName("VillageID")
    @Expose
    private String villageID;
    @SerializedName("Pincode")
    @Expose
    private String pincode;
    @SerializedName("IFSCCode")
    @Expose
    private String iFSCCode;
    @SerializedName("NameasperBank")
    @Expose
    private String nameasperBank;
    @SerializedName("BankAccountNo")
    @Expose
    private String bankAccountNo;
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
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;

    public Long getTransporterID() {
        return transporterID;
    }

    public void setTransporterID(Long transporterID) {
        this.transporterID = transporterID;
    }

    public String getTransporterCode() {
        return transporterCode;
    }

    public void setTransporterCode(String transporterCode) {
        this.transporterCode = transporterCode;
    }

    public String getTransportName() {
        return transportName;
    }

    public void setTransportName(String transportName) {
        this.transportName = transportName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getMandalID() {
        return mandalID;
    }

    public void setMandalID(String mandalID) {
        this.mandalID = mandalID;
    }

    public String getVillageID() {
        return villageID;
    }

    public void setVillageID(String villageID) {
        this.villageID = villageID;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getIFSCCode() {
        return iFSCCode;
    }

    public void setIFSCCode(String iFSCCode) {
        this.iFSCCode = iFSCCode;
    }

    public String getNameasperBank() {
        return nameasperBank;
    }

    public void setNameasperBank(String nameasperBank) {
        this.nameasperBank = nameasperBank;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}

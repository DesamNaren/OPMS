package com.cgg.pps.model.response.farmer.farmerdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmerBasicDetail {

    @SerializedName("RegID")
    @Expose
    private Long regID;
    @SerializedName("RegistrationNo")
    @Expose
    private String registrationNo;
    @SerializedName("FarmerName")
    @Expose
    private String farmerName;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("SocialStatusID")
    @Expose
    private Integer socialStatusID;
    @SerializedName("Age")
    @Expose
    private Integer age;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("FarmerFatherName")
    @Expose
    private String farmerFatherName;
    @SerializedName("DistrictID")
    @Expose
    private String districtID;
    @SerializedName("MandalID")
    @Expose
    private String mandalID;
    @SerializedName("VillageID")
    @Expose
    private String villageID;
    @SerializedName("StreetName")
    @Expose
    private String streetName;
    @SerializedName("HouseNo")
    @Expose
    private String houseNo;
    @SerializedName("Pincode")
    @Expose
    private String pincode;
    @SerializedName("BankId")
    @Expose
    private String bankId;
    @SerializedName("BranchId")
    @Expose
    private String branchId;
    @SerializedName("IFSCCode")
    @Expose
    private String iFSCCode;
    @SerializedName("BankAccountNo")
    @Expose
    private String bankAccountNo;
    @SerializedName("NameasperBank")
    @Expose
    private String nameasperBank;
    @SerializedName("LandCount")
    @Expose
    private Integer landCount;
    @SerializedName("CultivableAcres")
    @Expose
    private Double cultivableAcres;
    @SerializedName("TotalAcres")
    @Expose
    private Double totalAcres;
    @SerializedName("PaddyElizibility")
    @Expose
    private Double paddyElizibility;
    @SerializedName("BalancePaddyQty")
    @Expose
    private Double balancePaddyQty;
    @SerializedName("PaddyQty")
    @Expose
    private Double paddyQty;

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

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getSocialStatusID() {
        return socialStatusID;
    }

    public void setSocialStatusID(Integer socialStatusID) {
        this.socialStatusID = socialStatusID;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFarmerFatherName() {
        return farmerFatherName;
    }

    public void setFarmerFatherName(String farmerFatherName) {
        this.farmerFatherName = farmerFatherName;
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

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getIFSCCode() {
        return iFSCCode;
    }

    public void setIFSCCode(String iFSCCode) {
        this.iFSCCode = iFSCCode;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getNameasperBank() {
        return nameasperBank;
    }

    public void setNameasperBank(String nameasperBank) {
        this.nameasperBank = nameasperBank;
    }

    public Integer getLandCount() {
        return landCount;
    }

    public void setLandCount(Integer landCount) {
        this.landCount = landCount;
    }

    public Double getCultivableAcres() {
        return cultivableAcres;
    }

    public void setCultivableAcres(Double cultivableAcres) {
        this.cultivableAcres = cultivableAcres;
    }

    public Double getTotalAcres() {
        return totalAcres;
    }

    public void setTotalAcres(Double totalAcres) {
        this.totalAcres = totalAcres;
    }

    public Double getPaddyElizibility() {
        return paddyElizibility;
    }

    public void setPaddyElizibility(Double paddyElizibility) {
        this.paddyElizibility = paddyElizibility;
    }

    public Double getBalancePaddyQty() {
        return balancePaddyQty;
    }

    public void setBalancePaddyQty(Double balancePaddyQty) {
        this.balancePaddyQty = balancePaddyQty;
    }

    public Double getPaddyQty() {
        return paddyQty;
    }

    public void setPaddyQty(Double paddyQty) {
        this.paddyQty = paddyQty;
    }

}

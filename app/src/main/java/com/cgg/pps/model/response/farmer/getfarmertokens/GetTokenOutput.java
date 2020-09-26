package com.cgg.pps.model.response.farmer.getfarmertokens;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetTokenOutput implements Serializable {

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
    @SerializedName("FarmerTransactionID")
    @Expose
    private String farmerTransactionID;
    @SerializedName("FarmerTransactionRowID")
    @Expose
    private Long farmerTransactionRowID;
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
    @SerializedName("Gender")
    @Expose
    private Integer gender;
    @SerializedName("Age")
    @Expose
    private Integer age;
    @SerializedName("FarmerFatherName")
    @Expose
    private String farmerFatherName;
    @SerializedName("FDistrictID")
    @Expose
    private String fDistrictID;
    @SerializedName("FMandalID")
    @Expose
    private String fMandalID;
    @SerializedName("FVillageID")
    @Expose
    private String fVillageID;
    @SerializedName("FStreetName")
    @Expose
    private String fStreetName;
    @SerializedName("FHouseNo")
    @Expose
    private String fHouseNo;
    @SerializedName("FPincode")
    @Expose
    private String fPincode;
    @SerializedName("BankId")
    @Expose
    private Integer bankId;
    @SerializedName("BranchId")
    @Expose
    private Integer branchId;
    @SerializedName("IFSCCode")
    @Expose
    private String iFSCCode;
    @SerializedName("BankAccountNo")
    @Expose
    private String bankAccountNo;
    @SerializedName("NameasperBank")
    @Expose
    private String nameasperBank;
    @SerializedName("ApproxmateQuantity")
    @Expose
    private Double approxmateQuantity;
    @SerializedName("ApproximatePaddyType")
    @Expose
    private Integer approximatePaddyType;
    @SerializedName("TotalCultivableArea")
    @Expose
    private Double totalCultivableArea;
    @SerializedName("PaddyTypeID")
    @Expose
    private Integer paddyTypeID;
    @SerializedName("PaddyType")
    @Expose
    private String paddyType;
    @SerializedName("ProcuredQuantity")
    @Expose
    private Double procuredQuantity;
    @SerializedName("GOIDCount")
    @Expose
    private Long gOIDCount;
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
    @SerializedName("TRNewBags")
    @Expose
    private Integer tRNewBags;
    @SerializedName("TROldBags")
    @Expose
    private Integer tROldBags;
    @SerializedName("TRTotalBags")
    @Expose
    private Integer tRTotalBags;
    @SerializedName("FAQType")
    @Expose
    private String fAQType;
    @SerializedName("FAQDate")
    @Expose
    private String fAQDate;
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
    @SerializedName("TotalAmount")
    @Expose
    private Double totalAmount;
    @SerializedName("StatusID")
    @Expose
    private String statusID;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("TokenStatus")
    @Expose
    private Integer tokenStatus;
    @SerializedName("TokenStatusMessage")
    @Expose
    private String tokenStatusMessage;
    @SerializedName("NewBags")
    @Expose
    private Integer newBags;
    @SerializedName("OldBags")
    @Expose
    private Integer oldBags;
    @SerializedName("TotalBags")
    @Expose
    private Integer totalBags;

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

    public String getFarmerTransactionID() {
        return farmerTransactionID;
    }

    public void setFarmerTransactionID(String farmerTransactionID) {
        this.farmerTransactionID = farmerTransactionID;
    }

    public Long getFarmerTransactionRowID() {
        return farmerTransactionRowID;
    }

    public void setFarmerTransactionRowID(Long farmerTransactionRowID) {
        this.farmerTransactionRowID = farmerTransactionRowID;
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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getFarmerFatherName() {
        return farmerFatherName;
    }

    public void setFarmerFatherName(String farmerFatherName) {
        this.farmerFatherName = farmerFatherName;
    }

    public String getFDistrictID() {
        return fDistrictID;
    }

    public void setFDistrictID(String fDistrictID) {
        this.fDistrictID = fDistrictID;
    }

    public String getFMandalID() {
        return fMandalID;
    }

    public void setFMandalID(String fMandalID) {
        this.fMandalID = fMandalID;
    }

    public String getFVillageID() {
        return fVillageID;
    }

    public void setFVillageID(String fVillageID) {
        this.fVillageID = fVillageID;
    }

    public String getFStreetName() {
        return fStreetName;
    }

    public void setFStreetName(String fStreetName) {
        this.fStreetName = fStreetName;
    }

    public String getFHouseNo() {
        return fHouseNo;
    }

    public void setFHouseNo(String fHouseNo) {
        this.fHouseNo = fHouseNo;
    }

    public String getFPincode() {
        return fPincode;
    }

    public void setFPincode(String fPincode) {
        this.fPincode = fPincode;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
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

    public Double getApproxmateQuantity() {
        return approxmateQuantity;
    }

    public void setApproxmateQuantity(Double approxmateQuantity) {
        this.approxmateQuantity = approxmateQuantity;
    }

    public Integer getApproximatePaddyType() {
        return approximatePaddyType;
    }

    public void setApproximatePaddyType(Integer approximatePaddyType) {
        this.approximatePaddyType = approximatePaddyType;
    }

    public Double getTotalCultivableArea() {
        return totalCultivableArea;
    }

    public void setTotalCultivableArea(Double totalCultivableArea) {
        this.totalCultivableArea = totalCultivableArea;
    }

    public Integer getPaddyTypeID() {
        return paddyTypeID;
    }

    public void setPaddyTypeID(Integer paddyTypeID) {
        this.paddyTypeID = paddyTypeID;
    }

    public String getPaddyType() {
        return paddyType;
    }

    public void setPaddyType(String paddyType) {
        this.paddyType = paddyType;
    }

    public Double getProcuredQuantity() {
        return procuredQuantity;
    }

    public void setProcuredQuantity(Double procuredQuantity) {
        this.procuredQuantity = procuredQuantity;
    }

    public Long getGOIDCount() {
        return gOIDCount;
    }

    public void setGOIDCount(Long gOIDCount) {
        this.gOIDCount = gOIDCount;
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

    public Integer getTRNewBags() {
        return tRNewBags;
    }

    public void setTRNewBags(Integer tRNewBags) {
        this.tRNewBags = tRNewBags;
    }

    public Integer getTROldBags() {
        return tROldBags;
    }

    public void setTROldBags(Integer tROldBags) {
        this.tROldBags = tROldBags;
    }

    public Integer getTRTotalBags() {
        return tRTotalBags;
    }

    public void setTRTotalBags(Integer tRTotalBags) {
        this.tRTotalBags = tRTotalBags;
    }

    public String getFAQType() {
        return fAQType;
    }

    public void setFAQType(String fAQType) {
        this.fAQType = fAQType;
    }

    public String getFAQDate() {
        return fAQDate;
    }

    public void setFAQDate(String fAQDate) {
        this.fAQDate = fAQDate;
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

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatusID() {
        return statusID;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTokenStatus() {
        return tokenStatus;
    }

    public void setTokenStatus(Integer tokenStatus) {
        this.tokenStatus = tokenStatus;
    }

    public String getTokenStatusMessage() {
        return tokenStatusMessage;
    }

    public void setTokenStatusMessage(String tokenStatusMessage) {
        this.tokenStatusMessage = tokenStatusMessage;
    }

    public Integer getOldBags() {
        return oldBags;
    }

    public void setOldBags(Integer oldBags) {
        this.oldBags = oldBags;
    }

    public Integer getNewBags() {
        return newBags;
    }

    public void setNewBags(Integer newBags) {
        this.newBags = newBags;
    }

    public Integer getTotalBags() {
        return totalBags;
    }

    public void setTotalBags(Integer totalBags) {
        this.totalBags = totalBags;
    }

}

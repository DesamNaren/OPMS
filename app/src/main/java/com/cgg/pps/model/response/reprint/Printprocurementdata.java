package com.cgg.pps.model.response.reprint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Printprocurementdata {

    @SerializedName("FarmerName")
    @Expose
    private String farmerName;
    @SerializedName("TokenID")
    @Expose
    private Integer tokenID;
    @SerializedName("TokenNo")
    @Expose
    private String tokenNo;
    @SerializedName("RegistrationID")
    @Expose
    private Long registrationID;
    @SerializedName("RegistrationNo")
    @Expose
    private String registrationNo;
    @SerializedName("FarmerFatherName")
    @Expose
    private String farmerFatherName;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("BankID")
    @Expose
    private Integer bankID;
    @SerializedName("BankName")
    @Expose
    private String bankName;
    @SerializedName("BranchID")
    @Expose
    private Integer branchID;
    @SerializedName("BranchName")
    @Expose
    private String branchName;
    @SerializedName("IFSCCode")
    @Expose
    private String iFSCCode;
    @SerializedName("BankAccountNo")
    @Expose
    private String bankAccountNo;
    @SerializedName("NameasperBank")
    @Expose
    private String nameasperBank;
    @SerializedName("FarmerTransactionID")
    @Expose
    private String farmerTransactionID;
    @SerializedName("Quantity")
    @Expose
    private Double quantity;
    @SerializedName("NewBags")
    @Expose
    private Integer newBags;
    @SerializedName("OldBags")
    @Expose
    private Integer oldBags;
    @SerializedName("TotalBags")
    @Expose
    private Integer totalBags;
    @SerializedName("MSPAmount")
    @Expose
    private Double mSPAmount;
    @SerializedName("TotalAmount")
    @Expose
    private Double totalAmount;
    @SerializedName("TokenDate")
    @Expose
    private String tokenDate;
    @SerializedName("AppointmentDate")
    @Expose
    private String appointmentDate;
    @SerializedName("FAQDate")
    @Expose
    private String fAQDate;
    @SerializedName("TransactionDate")
    @Expose
    private String transactionDate;
    @SerializedName("PaddyTypeID")
    @Expose
    private Integer paddyTypeID;
    @SerializedName("PaddyType")
    @Expose
    private String paddyType;
    @SerializedName("URNNumber")
    @Expose
    private String URNNumber;

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
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

    public Long getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(Long registrationID) {
        this.registrationID = registrationID;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getFarmerFatherName() {
        return farmerFatherName;
    }

    public void setFarmerFatherName(String farmerFatherName) {
        this.farmerFatherName = farmerFatherName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getBankID() {
        return bankID;
    }

    public void setBankID(Integer bankID) {
        this.bankID = bankID;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getBranchID() {
        return branchID;
    }

    public void setBranchID(Integer branchID) {
        this.branchID = branchID;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
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

    public String getFarmerTransactionID() {
        return farmerTransactionID;
    }

    public void setFarmerTransactionID(String farmerTransactionID) {
        this.farmerTransactionID = farmerTransactionID;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Integer getNewBags() {
        return newBags;
    }

    public void setNewBags(Integer newBags) {
        this.newBags = newBags;
    }

    public Integer getOldBags() {
        return oldBags;
    }

    public void setOldBags(Integer oldBags) {
        this.oldBags = oldBags;
    }

    public Integer getTotalBags() {
        return totalBags;
    }

    public void setTotalBags(Integer totalBags) {
        this.totalBags = totalBags;
    }

    public Double getMSPAmount() {
        return mSPAmount;
    }

    public void setMSPAmount(Double mSPAmount) {
        this.mSPAmount = mSPAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(String tokenDate) {
        this.tokenDate = tokenDate;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getFAQDate() {
        return fAQDate;
    }

    public void setFAQDate(String fAQDate) {
        this.fAQDate = fAQDate;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
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

    public String getURNNumber() {
        return URNNumber;
    }

    public void setURNNumber(String URNNumber) {
        this.URNNumber = URNNumber;
    }
}

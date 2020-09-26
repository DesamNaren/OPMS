package com.cgg.pps.model.response.truckchit.transaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionsData {

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
    @SerializedName("FarmerName")
    @Expose
    private String farmerName;
    @SerializedName("FarmerFatherName")
    @Expose
    private String farmerFatherName;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("BankId")
    @Expose
    private Integer bankId;
    @SerializedName("BankName")
    @Expose
    private String bankName;
    @SerializedName("BranchId")
    @Expose
    private Integer branchId;
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
    @SerializedName("PaddyTypeID")
    @Expose
    private Integer paddyTypeID;
    @SerializedName("PaddyType")
    @Expose
    private String paddyType;
    @SerializedName("TotalQuantity")
    @Expose
    private Double totalQuantity;
    @SerializedName("MSPAmount")
    @Expose
    private Double mSPAmount;
    @SerializedName("TotalAmount")
    @Expose
    private Double totalAmount;
    @SerializedName("NewBags")
    @Expose
    private Integer newBags;
    @SerializedName("OldBags")
    @Expose
    private Integer oldBags;
    @SerializedName("TotalBags")
    @Expose
    private Integer totalBags;
    @SerializedName("TRNewBags")
    @Expose
    private Integer tRNewBags;
    @SerializedName("TROldBags")
    @Expose
    private Integer tROldBags;
    @SerializedName("TRTotalBags")
    @Expose
    private Integer tRTotalBags;
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
    @SerializedName("TruckChitDate")
    @Expose
    private String truckChitDate;
    @SerializedName("BankProcessedDate")
    @Expose
    private String bankProcessedDate;
    @SerializedName("BankRefNo")
    @Expose
    private String bankRefNo;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("StatusID")
    @Expose
    private String statusID;
    @SerializedName("Status")
    @Expose
    private String status;

    private Integer RemNewBags;
    private Integer RemOldBags;
    private Integer RemTOtBags;


    public String getiFSCCode() {
        return iFSCCode;
    }

    public void setiFSCCode(String iFSCCode) {
        this.iFSCCode = iFSCCode;
    }

    public Double getmSPAmount() {
        return mSPAmount;
    }

    public void setmSPAmount(Double mSPAmount) {
        this.mSPAmount = mSPAmount;
    }

    public Integer gettRNewBags() {
        return tRNewBags;
    }

    public void settRNewBags(Integer tRNewBags) {
        this.tRNewBags = tRNewBags;
    }

    public Integer gettROldBags() {
        return tROldBags;
    }

    public void settROldBags(Integer tROldBags) {
        this.tROldBags = tROldBags;
    }

    public Integer gettRTotalBags() {
        return tRTotalBags;
    }

    public void settRTotalBags(Integer tRTotalBags) {
        this.tRTotalBags = tRTotalBags;
    }

    public String getfAQDate() {
        return fAQDate;
    }

    public void setfAQDate(String fAQDate) {
        this.fAQDate = fAQDate;
    }

    public Integer getRemNewBags() {
        return RemNewBags;
    }

    public void setRemNewBags(Integer remNewBags) {
        RemNewBags = remNewBags;
    }

    public Integer getRemOldBags() {
        return RemOldBags;
    }

    public void setRemOldBags(Integer remOldBags) {
        RemOldBags = remOldBags;
    }

    public Integer getRemTOtBags() {
        return RemTOtBags;
    }

    public void setRemTOtBags(Integer remTOtBags) {
        RemTOtBags = remTOtBags;
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

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
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

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
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

    public Double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Double totalQuantity) {
        this.totalQuantity = totalQuantity;
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

    public String getTruckChitDate() {
        return truckChitDate;
    }

    public void setTruckChitDate(String truckChitDate) {
        this.truckChitDate = truckChitDate;
    }

    public String getBankProcessedDate() {
        return bankProcessedDate;
    }

    public void setBankProcessedDate(String bankProcessedDate) {
        this.bankProcessedDate = bankProcessedDate;
    }

    public String getBankRefNo() {
        return bankRefNo;
    }

    public void setBankRefNo(String bankRefNo) {
        this.bankRefNo = bankRefNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

}

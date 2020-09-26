package com.cgg.pps.model.response.report.payment;

import java.io.Serializable;

public class PaymentReportResponse implements Serializable {
    private String RegistrationNo;
    private String FarmerTransactionID;
    private String FarmerName;
    private String BankAccountNo;
    private String NameasperBank;
    private String BankName;
    private String BranchName;
    private String IFSCCODE;
    private String PPCCode;
    private String PPCName;
    private String AmountTransferStatus;
    private String TransactionDate;
    private String NewBags;
    private String OldBags;
    private String TotalBags;
    private String AQuantity;
    private String CQuantity;
    private String TotalQuantity;
    private String TotalAmount;
    private String AgencyName;
    private String DistrictName;
    private String BankRefNo;
    private String SBIRefNo;
    private String BankProcessedDate;
    private String GunnyOrderDetails;
    private String ReleaseOrderDetails;
    private String Output;

    public String getRegistrationNo() {
        return RegistrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        RegistrationNo = registrationNo;
    }

    public String getFarmerTransactionID() {
        return FarmerTransactionID;
    }

    public void setFarmerTransactionID(String farmerTransactionID) {
        FarmerTransactionID = farmerTransactionID;
    }

    public String getFarmerName() {
        return FarmerName;
    }

    public void setFarmerName(String farmerName) {
        FarmerName = farmerName;
    }

    public String getBankAccountNo() {
        return BankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        BankAccountNo = bankAccountNo;
    }

    public String getNameasperBank() {
        return NameasperBank;
    }

    public void setNameasperBank(String nameasperBank) {
        NameasperBank = nameasperBank;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }

    public String getIFSCCODE() {
        return IFSCCODE;
    }

    public void setIFSCCODE(String IFSCCODE) {
        this.IFSCCODE = IFSCCODE;
    }

    public String getPPCCode() {
        return PPCCode;
    }

    public void setPPCCode(String PPCCode) {
        this.PPCCode = PPCCode;
    }

    public String getPPCName() {
        return PPCName;
    }

    public void setPPCName(String PPCName) {
        this.PPCName = PPCName;
    }

    public String getAmountTransferStatus() {
        return AmountTransferStatus;
    }

    public void setAmountTransferStatus(String amountTransferStatus) {
        AmountTransferStatus = amountTransferStatus;
    }

    public String getTransactionDate() {
        return TransactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        TransactionDate = transactionDate;
    }

    public String getNewBags() {
        return NewBags;
    }

    public void setNewBags(String newBags) {
        NewBags = newBags;
    }

    public String getOldBags() {
        return OldBags;
    }

    public void setOldBags(String oldBags) {
        OldBags = oldBags;
    }

    public String getTotalBags() {
        return TotalBags;
    }

    public void setTotalBags(String totalBags) {
        TotalBags = totalBags;
    }

    public String getAQuantity() {
        return AQuantity;
    }

    public void setAQuantity(String AQuantity) {
        this.AQuantity = AQuantity;
    }

    public String getCQuantity() {
        return CQuantity;
    }

    public void setCQuantity(String CQuantity) {
        this.CQuantity = CQuantity;
    }

    public String getTotalQuantity() {
        return TotalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        TotalQuantity = totalQuantity;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getAgencyName() {
        return AgencyName;
    }

    public void setAgencyName(String agencyName) {
        AgencyName = agencyName;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    public String getBankRefNo() {
        return BankRefNo;
    }

    public void setBankRefNo(String bankRefNo) {
        BankRefNo = bankRefNo;
    }

    public String getSBIRefNo() {
        return SBIRefNo;
    }

    public void setSBIRefNo(String SBIRefNo) {
        this.SBIRefNo = SBIRefNo;
    }

    public String getBankProcessedDate() {
        return BankProcessedDate;
    }

    public void setBankProcessedDate(String bankProcessedDate) {
        BankProcessedDate = bankProcessedDate;
    }

    public String getGunnyOrderDetails() {
        return GunnyOrderDetails;
    }

    public void setGunnyOrderDetails(String gunnyOrderDetails) {
        GunnyOrderDetails = gunnyOrderDetails;
    }

    public String getReleaseOrderDetails() {
        return ReleaseOrderDetails;
    }

    public void setReleaseOrderDetails(String releaseOrderDetails) {
        ReleaseOrderDetails = releaseOrderDetails;
    }

    public String getOutput() {
        return Output;
    }

    public void setOutput(String output) {
        Output = output;
    }
}


package com.cgg.pps.model.response.report.payment_report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaddyPaymentOutput implements Serializable {

    @SerializedName("FarmerName")
    @Expose
    private String farmerName;
    @SerializedName("BranchName")
    @Expose
    private String branchName;
    @SerializedName("BankName")
    @Expose
    private String bankName;
    @SerializedName("IFSCCode")
    @Expose
    private String iFSCCode;
    @SerializedName("BankAccountNo")
    @Expose
    private String bankAccountNo;
    @SerializedName("NameasperBank")
    @Expose
    private String nameasperBank;
    @SerializedName("PPCName")
    @Expose
    private String pPCName;
    @SerializedName("PPCCode")
    @Expose
    private String pPCCode;
    @SerializedName("PPCID")
    @Expose
    private Integer pPCID;
    @SerializedName("NewBags")
    @Expose
    private Integer newBags;
    @SerializedName("OldBags")
    @Expose
    private Integer oldBags;
    @SerializedName("TotalBags")
    @Expose
    private Integer totalBags;
    @SerializedName("AQUANTITY")
    @Expose
    private Double aQUANTITY;
    @SerializedName("CQUANTITY")
    @Expose
    private Double cQUANTITY;
    @SerializedName("TotalQuantity")
    @Expose
    private Double totalQuantity;
    @SerializedName("RegistrationNo")
    @Expose
    private String registrationNo;
    @SerializedName("RegistrationNo1")
    @Expose
    private String registrationNo1;
    @SerializedName("TotalAmount")
    @Expose
    private Double totalAmount;
    @SerializedName("PaymentDate")
    @Expose
    private String paymentDate;
    @SerializedName("TransactionDate")
    @Expose
    private String transactionDate;
    @SerializedName("TruckChitDate")
    @Expose
    private String truckChitDate;
    @SerializedName("AgencyName")
    @Expose
    private String agencyName;
    @SerializedName("DistrictName")
    @Expose
    private String districtName;
    @SerializedName("AmountTransferStatus")
    @Expose
    private String amountTransferStatus;

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getiFSCCode() {
        return iFSCCode;
    }

    public void setiFSCCode(String iFSCCode) {
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

    public String getpPCName() {
        return pPCName;
    }

    public void setpPCName(String pPCName) {
        this.pPCName = pPCName;
    }

    public String getpPCCode() {
        return pPCCode;
    }

    public void setpPCCode(String pPCCode) {
        this.pPCCode = pPCCode;
    }

    public Integer getpPCID() {
        return pPCID;
    }

    public void setpPCID(Integer pPCID) {
        this.pPCID = pPCID;
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

    public Double getaQUANTITY() {
        return aQUANTITY;
    }

    public void setaQUANTITY(Double aQUANTITY) {
        this.aQUANTITY = aQUANTITY;
    }

    public Double getcQUANTITY() {
        return cQUANTITY;
    }

    public void setcQUANTITY(Double cQUANTITY) {
        this.cQUANTITY = cQUANTITY;
    }

    public Double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getRegistrationNo1() {
        return registrationNo1;
    }

    public void setRegistrationNo1(String registrationNo1) {
        this.registrationNo1 = registrationNo1;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
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

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAmountTransferStatus() {
        return amountTransferStatus;
    }

    public void setAmountTransferStatus(String amountTransferStatus) {
        this.amountTransferStatus = amountTransferStatus;
    }
}

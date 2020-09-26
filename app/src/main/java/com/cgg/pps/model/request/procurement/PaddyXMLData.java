package com.cgg.pps.model.request.procurement;

import com.cgg.pps.model.request.procurement.PaddyProcOrderXMLData;

import java.util.ArrayList;

public class PaddyXMLData {
    private String ppccode;
    private String ppcid;
    private String SystemIP;
    private String seasonID;
    private String TokenNo;
    private String TokenID;
    private String RegistrationNo;
    private String RegistrationID;
    private String TransactionID;
    private String TransactionRowID;
    private Double TotalQuantity;
    private Integer NewBags;
    private Integer OldBags;
    private Integer TotalBags;
    private Double TotalAmount;
    private Double MSPAmount;
    private String PaddyProcured_Date;
    private String CreatedDate;

    private PaddyProcOrderXMLData paddyOrderXMLData;
    private ArrayList<PaddyProcOrderXMLData> paddyOrderXMLDataArrayList;

    public String getPpccode() {
        return ppccode;
    }

    public void setPpccode(String ppccode) {
        this.ppccode = ppccode;
    }

    public String getPpcid() {
        return ppcid;
    }

    public void setPpcid(String ppcid) {
        this.ppcid = ppcid;
    }

    public String getSystemIP() {
        return SystemIP;
    }

    public void setSystemIP(String systemIP) {
        SystemIP = systemIP;
    }

    public String getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(String seasonID) {
        this.seasonID = seasonID;
    }

    public String getTokenNo() {
        return TokenNo;
    }

    public void setTokenNo(String tokenNo) {
        TokenNo = tokenNo;
    }

    public String getTokenID() {
        return TokenID;
    }

    public void setTokenID(String tokenID) {
        TokenID = tokenID;
    }

    public String getRegistrationNo() {
        return RegistrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        RegistrationNo = registrationNo;
    }

    public String getRegistrationID() {
        return RegistrationID;
    }

    public void setRegistrationID(String registrationID) {
        RegistrationID = registrationID;
    }

    public String getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID(String transactionID) {
        TransactionID = transactionID;
    }

    public String getTransactionRowID() {
        return TransactionRowID;
    }

    public void setTransactionRowID(String transactionRowID) {
        TransactionRowID = transactionRowID;
    }

    public Double getTotalQuantity() {
        return TotalQuantity;
    }

    public void setTotalQuantity(Double totalQuantity) {
        TotalQuantity = totalQuantity;
    }

    public Integer getNewBags() {
        return NewBags;
    }

    public void setNewBags(Integer newBags) {
        NewBags = newBags;
    }

    public Integer getOldBags() {
        return OldBags;
    }

    public void setOldBags(Integer oldBags) {
        OldBags = oldBags;
    }

    public Integer getTotalBags() {
        return TotalBags;
    }

    public void setTotalBags(Integer totalBags) {
        TotalBags = totalBags;
    }

    public Double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        TotalAmount = totalAmount;
    }

    public Double getMSPAmount() {
        return MSPAmount;
    }

    public void setMSPAmount(Double MSPAmount) {
        this.MSPAmount = MSPAmount;
    }

    public String getPaddyProcured_Date() {
        return PaddyProcured_Date;
    }

    public void setPaddyProcured_Date(String paddyProcured_Date) {
        PaddyProcured_Date = paddyProcured_Date;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public PaddyProcOrderXMLData getPaddyOrderXMLData() {
        return paddyOrderXMLData;
    }

    public void setPaddyOrderXMLData(PaddyProcOrderXMLData paddyOrderXMLData) {
        this.paddyOrderXMLData = paddyOrderXMLData;
    }

    public ArrayList<PaddyProcOrderXMLData> getPaddyOrderXMLDataArrayList() {
        return paddyOrderXMLDataArrayList;
    }

    public void setPaddyOrderXMLDataArrayList(ArrayList<PaddyProcOrderXMLData> paddyOrderXMLDataArrayList) {
        this.paddyOrderXMLDataArrayList = paddyOrderXMLDataArrayList;
    }
}

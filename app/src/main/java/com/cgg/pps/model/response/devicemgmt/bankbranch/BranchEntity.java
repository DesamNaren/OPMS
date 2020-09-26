package com.cgg.pps.model.response.devicemgmt.bankbranch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BranchEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("BankID")
    @Expose
    private Integer bankID;
    @SerializedName("BranchID")
    @Expose
    private Integer branchID;
    @SerializedName("BranchName")
    @Expose
    private String branchName;
    @SerializedName("IFSC")
    @Expose
    private String iFSC;
    @SerializedName("IFSC2")
    @Expose
    private String iFSC2;

    public Integer getBankID() {
        return bankID;
    }

    public void setBankID(Integer bankID) {
        this.bankID = bankID;
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

    public String getIFSC() {
        return iFSC;
    }

    public void setIFSC(String iFSC) {
        this.iFSC = iFSC;
    }

    public String getIFSC2() {
        return iFSC2;
    }

    public void setIFSC2(String iFSC2) {
        this.iFSC2 = iFSC2;
    }

}

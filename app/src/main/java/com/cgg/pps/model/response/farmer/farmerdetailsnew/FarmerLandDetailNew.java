package com.cgg.pps.model.response.farmer.farmerdetailsnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmerLandDetailNew {

    @SerializedName("DataSource")
    @Expose
    private String dataSource;
    @SerializedName("MainLandRowId")
    @Expose
    private Integer mainLandRowId;
    @SerializedName("SubLandRowId")
    @Expose
    private Integer subLandRowId;
    @SerializedName("ProcRowId")
    @Expose
    private Integer procRowId;
    @SerializedName("FarmerRegistrationId")
    @Expose
    private Integer farmerRegistrationId;
    @SerializedName("OwnershipTypeId")
    @Expose
    private Integer ownershipTypeId;
    @SerializedName("LandOwnerName")
    @Expose
    private String landOwnerName;
    @SerializedName("LandOwnerAadhaar")
    @Expose
    private String landOwnerAadhaar;
    @SerializedName("LegalHeirRelationId")
    @Expose
    private Integer legalHeirRelationId;
    @SerializedName("DistrictID")
    @Expose
    private String districtID;
    @SerializedName("MandalID")
    @Expose
    private String mandalID;
    @SerializedName("VillageID")
    @Expose
    private String villageID;
    @SerializedName("PassBookNo")
    @Expose
    private String passBookNo;
    @SerializedName("SurveyNo")
    @Expose
    private String surveyNo;
    @SerializedName("LandTypeId")
    @Expose
    private Integer landTypeId;
    @SerializedName("TotalArea")
    @Expose
    private Double totalArea;
    @SerializedName("TotalLeaseArea")
    @Expose
    private Double totalLeaseArea;
    @SerializedName("TotalCultivableArea")
    @Expose
    private Double totalCultivableArea;
    @SerializedName("YieldperAcre")
    @Expose
    private Integer yieldperAcre;
    @SerializedName("PendingApproval")
    @Expose
    private Integer pendingApproval;
    @SerializedName("PendingApprovalMessage")
    @Expose
    private String pendingApprovalMessage;

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Integer getMainLandRowId() {
        return mainLandRowId;
    }

    public void setMainLandRowId(Integer mainLandRowId) {
        this.mainLandRowId = mainLandRowId;
    }

    public Integer getSubLandRowId() {
        return subLandRowId;
    }

    public void setSubLandRowId(Integer subLandRowId) {
        this.subLandRowId = subLandRowId;
    }

    public Integer getProcRowId() {
        return procRowId;
    }

    public void setProcRowId(Integer procRowId) {
        this.procRowId = procRowId;
    }

    public Integer getFarmerRegistrationId() {
        return farmerRegistrationId;
    }

    public void setFarmerRegistrationId(Integer farmerRegistrationId) {
        this.farmerRegistrationId = farmerRegistrationId;
    }

    public Integer getOwnershipTypeId() {
        return ownershipTypeId;
    }

    public void setOwnershipTypeId(Integer ownershipTypeId) {
        this.ownershipTypeId = ownershipTypeId;
    }

    public String getLandOwnerName() {
        return landOwnerName;
    }

    public void setLandOwnerName(String landOwnerName) {
        this.landOwnerName = landOwnerName;
    }

    public String getLandOwnerAadhaar() {
        return landOwnerAadhaar;
    }

    public void setLandOwnerAadhaar(String landOwnerAadhaar) {
        this.landOwnerAadhaar = landOwnerAadhaar;
    }

    public Integer getLegalHeirRelationId() {
        return legalHeirRelationId;
    }

    public void setLegalHeirRelationId(Integer legalHeirRelationId) {
        this.legalHeirRelationId = legalHeirRelationId;
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

    public String getPassBookNo() {
        return passBookNo;
    }

    public void setPassBookNo(String passBookNo) {
        this.passBookNo = passBookNo;
    }

    public String getSurveyNo() {
        return surveyNo;
    }

    public void setSurveyNo(String surveyNo) {
        this.surveyNo = surveyNo;
    }

    public Integer getLandTypeId() {
        return landTypeId;
    }

    public void setLandTypeId(Integer landTypeId) {
        this.landTypeId = landTypeId;
    }

    public Double getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(Double totalArea) {
        this.totalArea = totalArea;
    }

    public Double getTotalLeaseArea() {
        return totalLeaseArea;
    }

    public void setTotalLeaseArea(Double totalLeaseArea) {
        this.totalLeaseArea = totalLeaseArea;
    }

    public Double getTotalCultivableArea() {
        return totalCultivableArea;
    }

    public void setTotalCultivableArea(Double totalCultivableArea) {
        this.totalCultivableArea = totalCultivableArea;
    }

    public Integer getYieldperAcre() {
        return yieldperAcre;
    }

    public void setYieldperAcre(Integer yieldperAcre) {
        this.yieldperAcre = yieldperAcre;
    }

    public Integer getPendingApproval() {
        return pendingApproval;
    }

    public void setPendingApproval(Integer pendingApproval) {
        this.pendingApproval = pendingApproval;
    }

    public String getPendingApprovalMessage() {
        return pendingApprovalMessage;
    }

    public void setPendingApprovalMessage(String pendingApprovalMessage) {
        this.pendingApprovalMessage = pendingApprovalMessage;
    }

}

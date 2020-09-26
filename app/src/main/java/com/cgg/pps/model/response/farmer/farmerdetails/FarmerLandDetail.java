package com.cgg.pps.model.response.farmer.farmerdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmerLandDetail {

    @SerializedName("DataSource")
    @Expose
    private String dataSource;
    @SerializedName("MainLandRowId")
    @Expose
    private Long mainLandRowId;
    @SerializedName("SubLandRowId")
    @Expose
    private Long subLandRowId;
    @SerializedName("ProcRowId")
    @Expose
    private Long procRowId;
    @SerializedName("FarmerRegistrationId")
    @Expose
    private Long farmerRegistrationId;
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
    @SerializedName("mappedToPPC")
    @Expose
    private String mappedToPPC;

    public String getMappedToPPC() {
        return mappedToPPC;
    }

    public void setMappedToPPC(String mappedToPPC) {
        this.mappedToPPC = mappedToPPC;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Long getMainLandRowId() {
        return mainLandRowId;
    }

    public void setMainLandRowId(Long mainLandRowId) {
        this.mainLandRowId = mainLandRowId;
    }

    public Long getSubLandRowId() {
        return subLandRowId;
    }

    public void setSubLandRowId(Long subLandRowId) {
        this.subLandRowId = subLandRowId;
    }

    public Long getProcRowId() {
        return procRowId;
    }

    public void setProcRowId(Long procRowId) {
        this.procRowId = procRowId;
    }

    public Long getFarmerRegistrationId() {
        return farmerRegistrationId;
    }

    public void setFarmerRegistrationId(Long farmerRegistrationId) {
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

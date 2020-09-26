package com.cgg.pps.model.response.validateuser.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PPCUserDetails {

    @PrimaryKey
    private int id;

    public int getId() {
        return id;
    }

    @SerializedName("PPCID")
    @Expose
    private Integer pPCID;
    @SerializedName("PPCCode")
    @Expose
    private String pPCCode;
    @SerializedName("PPCName")
    @Expose
    private String pPCName;
    @SerializedName("MaximumProcurementCapacity")
    @Expose
    private String maximumProcurementCapacity;
    @SerializedName("AgencyID")
    @Expose
    private Integer agencyID;
    @SerializedName("CoordinatorName")
    @Expose
    private String coordinatorName;
    @SerializedName("CoordinatorMobile")
    @Expose
    private String coordinatorMobile;
    @SerializedName("CoordinatorEmail")
    @Expose
    private String coordinatorEmail;
    @SerializedName("HouseNo")
    @Expose
    private String houseNo;
    @SerializedName("StreetName")
    @Expose
    private String streetName;
    @SerializedName("DistrictID")
    @Expose
    private String districtID;
    @SerializedName("MandalID")
    @Expose
    private String mandalID;
    @SerializedName("VillageID")
    @Expose
    private String villageID;
    @SerializedName("Pincode")
    @Expose
    private String pincode;
    @SerializedName("seasonID")
    @Expose
    private Integer seasonID;
    @SerializedName("AuthenticationID")
    @Expose
    private String AuthenticationID;
    @SerializedName("SeasonStartDate")
    @Expose
    private String seasonStartDate;
    @SerializedName("ServerDate")
    @Expose
    private String serverDate;
    @SerializedName("YieldPerAcre")
    @Expose
    private Integer yieldPerAcre;


    public Integer getPPCID() {
        return pPCID;
    }

    public void setPPCID(Integer pPCID) {
        this.pPCID = pPCID;
    }

    public String getPPCCode() {
        return pPCCode;
    }

    public void setPPCCode(String pPCCode) {
        this.pPCCode = pPCCode;
    }

    public String getPPCName() {
        return pPCName;
    }

    public void setPPCName(String pPCName) {
        this.pPCName = pPCName;
    }

    public String getMaximumProcurementCapacity() {
        return maximumProcurementCapacity;
    }

    public void setMaximumProcurementCapacity(String maximumProcurementCapacity) {
        this.maximumProcurementCapacity = maximumProcurementCapacity;
    }

    public Integer getAgencyID() {
        return agencyID;
    }

    public void setAgencyID(Integer agencyID) {
        this.agencyID = agencyID;
    }

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public void setCoordinatorName(String coordinatorName) {
        this.coordinatorName = coordinatorName;
    }

    public String getCoordinatorMobile() {
        return coordinatorMobile;
    }

    public void setCoordinatorMobile(String coordinatorMobile) {
        this.coordinatorMobile = coordinatorMobile;
    }

    public String getCoordinatorEmail() {
        return coordinatorEmail;
    }

    public void setCoordinatorEmail(String coordinatorEmail) {
        this.coordinatorEmail = coordinatorEmail;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Integer getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(Integer seasonID) {
        this.seasonID = seasonID;
    }

    public String getAuthenticationID() {
        return AuthenticationID;
    }

    public void setAuthenticationID(String authenticationID) {
        AuthenticationID = authenticationID;
    }

    public String getSeasonStartDate() {
        return seasonStartDate;
    }

    public void setSeasonStartDate(String seasonStartDate) {
        this.seasonStartDate = seasonStartDate;
    }

    public String getServerDate() {
        return serverDate;
    }

    public void setServerDate(String serverDate) {
        this.serverDate = serverDate;
    }

    public Integer getYieldPerAcre() {
        return yieldPerAcre;
    }

    public void setYieldPerAcre(Integer yieldPerAcre) {
        this.yieldPerAcre = yieldPerAcre;
    }

}

package com.cgg.pps.model.request.ppc_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MappedPPCDetailsRequest {

    @SerializedName("AuthenticationID")
    @Expose
    private String authenticationID;
    @SerializedName("DistrictID")
    @Expose
    private String districtID;
    @SerializedName("MandalID")
    @Expose
    private String mandalID;
    @SerializedName("VillageID")
    @Expose
    private String villageID;

    public String getAuthenticationID() {
        return authenticationID;
    }

    public void setAuthenticationID(String authenticationID) {
        this.authenticationID = authenticationID;
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

}
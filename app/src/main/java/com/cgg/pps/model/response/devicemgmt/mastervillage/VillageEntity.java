package com.cgg.pps.model.response.devicemgmt.mastervillage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class VillageEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("DistrictID")
    @Expose
    private String districtID;
    @SerializedName("MandalID")
    @Expose
    private String mandalID;
    @SerializedName("VillageID")
    @Expose
    private String villageID;
    @SerializedName("VillageName")
    @Expose
    private String villageName;
    @SerializedName("VillageNameTelugu")
    @Expose
    private String villageNameTelugu;

    @SerializedName("IsMapped")
    @Expose
    private String IsMapped;

    public String getIsMapped() {
        return IsMapped;
    }

    public void setIsMapped(String isMapped) {
        IsMapped = isMapped;
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

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getVillageNameTelugu() {
        return villageNameTelugu;
    }

    public void setVillageNameTelugu(String villageNameTelugu) {
        this.villageNameTelugu = villageNameTelugu;
    }

}

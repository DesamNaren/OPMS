package com.cgg.pps.model.response.devicemgmt.mastermandal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MandalEntity {

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
    @SerializedName("MandalName")
    @Expose
    private String mandalName;
    @SerializedName("MandalNameTelugu")
    @Expose
    private String mandalNameTelugu;

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

    public String getMandalName() {
        return mandalName;
    }

    public void setMandalName(String mandalName) {
        this.mandalName = mandalName;
    }

    public String getMandalNameTelugu() {
        return mandalNameTelugu;
    }

    public void setMandalNameTelugu(String mandalNameTelugu) {
        this.mandalNameTelugu = mandalNameTelugu;
    }

}

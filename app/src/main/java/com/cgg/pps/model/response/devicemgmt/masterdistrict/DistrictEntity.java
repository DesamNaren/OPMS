package com.cgg.pps.model.response.devicemgmt.masterdistrict;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class DistrictEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("dist_id")
    @Expose
    private String distId;
    @SerializedName("dist_name")
    @Expose
    private String distName;

    @SerializedName("IsMapped")
    @Expose
    private String IsMapped;

    public String getIsMapped() {
        return IsMapped;
    }

    public void setIsMapped(String isMapped) {
        IsMapped = isMapped;
    }

    public String getDistId() {
        return distId;
    }

    public void setDistId(String distId) {
        this.distId = distId;
    }

    public String getDistName() {
        return distName;
    }

    public void setDistName(String distName) {
        this.distName = distName;
    }


}

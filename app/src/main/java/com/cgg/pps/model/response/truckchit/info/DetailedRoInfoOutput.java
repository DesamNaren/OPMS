package com.cgg.pps.model.response.truckchit.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailedRoInfoOutput {

    @SerializedName("ModifiedQuantity")
    @Expose
    private Double modifiedQuantity;
    @SerializedName("Activity")
    @Expose
    private String activity;
    @SerializedName("ModifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("Remarks")
    @Expose
    private String remarks;

    public Double getModifiedQuantity() {
        return modifiedQuantity;
    }

    public void setModifiedQuantity(Double modifiedQuantity) {
        this.modifiedQuantity = modifiedQuantity;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
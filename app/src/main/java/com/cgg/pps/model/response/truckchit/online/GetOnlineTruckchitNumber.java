package com.cgg.pps.model.response.truckchit.online;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetOnlineTruckchitNumber {

    @SerializedName("OnlineTruckChitNumber")
    @Expose
    private String onlineTruckChitNumber;

    public String getOnlineTruckChitNumber() {
        return onlineTruckChitNumber;
    }

    public void setOnlineTruckChitNumber(String onlineTruckChitNumber) {
        this.onlineTruckChitNumber = onlineTruckChitNumber;
    }

}

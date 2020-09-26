package com.cgg.pps.model.response.truckchit.manual;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UniqueManualTruckchitCount {

    @SerializedName("Count")
    @Expose
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
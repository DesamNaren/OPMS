package com.cgg.pps.model.response.truckchit.vehicle_type;

import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransporterVehicleType {

    @SerializedName("VehicleTypeName")
    @Expose
    private String vehicleTypeName;
    @SerializedName("VehicleTypeID")
    @Expose
    private Integer vehicleTypeID;

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public Integer getVehicleTypeID() {
        return vehicleTypeID;
    }

    public void setVehicleTypeID(Integer vehicleTypeID) {
        this.vehicleTypeID = vehicleTypeID;
    }

}

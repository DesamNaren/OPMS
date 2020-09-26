package com.cgg.pps.model.response.truckchit.mastervehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class VehicleDetails {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("VehicalNo")
    @Expose
    private String vehicalNo;
    @SerializedName("VehicleID")
    @Expose
    private Integer vehicleID;
    @SerializedName("VehicalCompany")
    @Expose
    private String vehicalCompany;
    @SerializedName("VehicleTypeName")
    @Expose
    private String vehicleTypeName;
    @SerializedName("TransporterID")
    @Expose
    private Long transporterID;

    @SerializedName("TransportName")
    @Expose
    private String transporterName;

    public String getTransporterName() {
        return transporterName;
    }

    public void setTransporterName(String transporterName) {
        this.transporterName = transporterName;
    }

    @SerializedName("ChasisNo")
    @Expose
    private String chasisNo;
    @SerializedName("EmptyWeight")
    @Expose
    private Double emptyWeight;
    @SerializedName("MaximumCapacity")
    @Expose
    private Double maximumCapacity;

    public String getVehicalNo() {
        return vehicalNo;
    }

    public void setVehicalNo(String vehicalNo) {
        this.vehicalNo = vehicalNo;
    }

    public Integer getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(Integer vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getVehicalCompany() {
        return vehicalCompany;
    }

    public void setVehicalCompany(String vehicalCompany) {
        this.vehicalCompany = vehicalCompany;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public Long getTransporterID() {
        return transporterID;
    }

    public void setTransporterID(Long transporterID) {
        this.transporterID = transporterID;
    }

    public String getChasisNo() {
        return chasisNo;
    }

    public void setChasisNo(String chasisNo) {
        this.chasisNo = chasisNo;
    }

    public Double getEmptyWeight() {
        return emptyWeight;
    }

    public void setEmptyWeight(Double emptyWeight) {
        this.emptyWeight = emptyWeight;
    }

    public Double getMaximumCapacity() {
        return maximumCapacity;
    }

    public void setMaximumCapacity(Double maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }
}

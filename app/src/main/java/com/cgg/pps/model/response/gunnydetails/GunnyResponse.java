package com.cgg.pps.model.response.gunnydetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GunnyResponse {

    @SerializedName("ORDERID")
    @Expose
    private Long oRDERID;
    @SerializedName("Sourcetype")
    @Expose
    private String sourcetype;
    @SerializedName("ReceivedFrom")
    @Expose
    private Object receivedFrom;
    @SerializedName("ManualGunniesTruckchitID")
    @Expose
    private Long manualGunniesTruckchitID;
    @SerializedName("NewBags")
    @Expose
    private Integer newBags;
    @SerializedName("OldBags")
    @Expose
    private Integer oldBags;
    @SerializedName("TotalBags")
    @Expose
    private Integer totalBags;
    @SerializedName("VehicleNo")
    @Expose
    private String vehicleNo;
    @SerializedName("DriverName")
    @Expose
    private String driverName;
    @SerializedName("DriverMobileNo")
    @Expose
    private String driverMobileNo;

    public Long getORDERID() {
        return oRDERID;
    }

    public void setORDERID(Long oRDERID) {
        this.oRDERID = oRDERID;
    }

    public String getSourcetype() {
        return sourcetype;
    }

    public void setSourcetype(String sourcetype) {
        this.sourcetype = sourcetype;
    }

    public Object getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(Object receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public Long getManualGunniesTruckchitID() {
        return manualGunniesTruckchitID;
    }

    public void setManualGunniesTruckchitID(Long manualGunniesTruckchitID) {
        this.manualGunniesTruckchitID = manualGunniesTruckchitID;
    }

    public Integer getNewBags() {
        return newBags;
    }

    public void setNewBags(Integer newBags) {
        this.newBags = newBags;
    }

    public Integer getOldBags() {
        return oldBags;
    }

    public void setOldBags(Integer oldBags) {
        this.oldBags = oldBags;
    }

    public Integer getTotalBags() {
        return totalBags;
    }

    public void setTotalBags(Integer totalBags) {
        this.totalBags = totalBags;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverMobileNo() {
        return driverMobileNo;
    }

    public void setDriverMobileNo(String driverMobileNo) {
        this.driverMobileNo = driverMobileNo;
    }

}

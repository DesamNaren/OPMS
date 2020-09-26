package com.cgg.pps.model.response.reprint.truckchit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gettruck {

    @SerializedName("PPCName")
    @Expose
    private String pPCName;
    @SerializedName("RO_Number")
    @Expose
    private String rONumber;
    @SerializedName("MillName")
    @Expose
    private String millName;
    @SerializedName("GradeApaddydetail")
    @Expose
    private GradeApaddydetail gradeApaddydetail;
    @SerializedName("Commonpaddydetail")
    @Expose
    private Commonpaddydetail commonpaddydetail;
    @SerializedName("TruckSlipNo")
    @Expose
    private String truckSlipNo;
    @SerializedName("DriverName")
    @Expose
    private String driverName;
    @SerializedName("DriverMobileNo")
    @Expose
    private String driverMobileNo;
    @SerializedName("ManualPPCTruckchitID")
    @Expose
    private String manualPPCTruckchitID;
    @SerializedName("FromPPCID")
    @Expose
    private Integer fromPPCID;
    @SerializedName("VehicalNo")
    @Expose
    private String vehicalNo;
    @SerializedName("TransportDate")
    @Expose
    private String transportDate;
    @SerializedName("TransportName")
    @Expose
    private String transportName;
    @SerializedName("MillAddress")
    @Expose
    private String millAddress;
    @SerializedName("PPCAddress")
    @Expose
    private String pPCAddress;
    @SerializedName("VehicalType")
    @Expose
    private String vehicalType;
    @SerializedName("TotalNewBags")
    @Expose
    private Integer totalNewBags;
    @SerializedName("TotalOldbags")
    @Expose
    private Integer totalOldbags;
    @SerializedName("TotalBags")
    @Expose
    private Integer totalBags;
    @SerializedName("TotalQuantity")
    @Expose
    private Double totalQuantity;

    public String getPPCName() {
        return pPCName;
    }

    public void setPPCName(String pPCName) {
        this.pPCName = pPCName;
    }

    public String getRONumber() {
        return rONumber;
    }

    public void setRONumber(String rONumber) {
        this.rONumber = rONumber;
    }

    public String getMillName() {
        return millName;
    }

    public void setMillName(String millName) {
        this.millName = millName;
    }

    public GradeApaddydetail getGradeApaddydetail() {
        return gradeApaddydetail;
    }

    public void setGradeApaddydetail(GradeApaddydetail gradeApaddydetail) {
        this.gradeApaddydetail = gradeApaddydetail;
    }

    public Commonpaddydetail getCommonpaddydetail() {
        return commonpaddydetail;
    }

    public void setCommonpaddydetail(Commonpaddydetail commonpaddydetail) {
        this.commonpaddydetail = commonpaddydetail;
    }

    public String getTruckSlipNo() {
        return truckSlipNo;
    }

    public void setTruckSlipNo(String truckSlipNo) {
        this.truckSlipNo = truckSlipNo;
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

    public String getManualPPCTruckchitID() {
        return manualPPCTruckchitID;
    }

    public void setManualPPCTruckchitID(String manualPPCTruckchitID) {
        this.manualPPCTruckchitID = manualPPCTruckchitID;
    }

    public Integer getFromPPCID() {
        return fromPPCID;
    }

    public void setFromPPCID(Integer fromPPCID) {
        this.fromPPCID = fromPPCID;
    }

    public String getVehicalNo() {
        return vehicalNo;
    }

    public void setVehicalNo(String vehicalNo) {
        this.vehicalNo = vehicalNo;
    }

    public String getTransportDate() {
        return transportDate;
    }

    public void setTransportDate(String transportDate) {
        this.transportDate = transportDate;
    }

    public String getTransportName() {
        return transportName;
    }

    public void setTransportName(String transportName) {
        this.transportName = transportName;
    }

    public String getMillAddress() {
        return millAddress;
    }

    public void setMillAddress(String millAddress) {
        this.millAddress = millAddress;
    }

    public String getPPCAddress() {
        return pPCAddress;
    }

    public void setPPCAddress(String pPCAddress) {
        this.pPCAddress = pPCAddress;
    }

    public String getVehicalType() {
        return vehicalType;
    }

    public void setVehicalType(String vehicalType) {
        this.vehicalType = vehicalType;
    }

    public Integer getTotalNewBags() {
        return totalNewBags;
    }

    public void setTotalNewBags(Integer totalNewBags) {
        this.totalNewBags = totalNewBags;
    }

    public Integer getTotalOldbags() {
        return totalOldbags;
    }

    public void setTotalOldbags(Integer totalOldbags) {
        this.totalOldbags = totalOldbags;
    }

    public Integer getTotalBags() {
        return totalBags;
    }

    public void setTotalBags(Integer totalBags) {
        this.totalBags = totalBags;
    }

    public Double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

}

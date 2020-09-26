package com.cgg.pps.model.request.truckchit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TCSubmitRequest {

    @SerializedName("AuthenticationID")
    @Expose
    private String AuthenticationID;
    @SerializedName("PPCID")
    @Expose
    private Integer pPCID;
    @SerializedName("Truckchit")
    @Expose
    private String truckchit;
    @SerializedName("xmlTruckchitData")
    @Expose
    private String xmlTruckchitData;
    @SerializedName("uploadTRImage")
    @Expose
    private String uploadTRImage;


    public String getAuthenticationID() {
        return AuthenticationID;
    }

    public void setAuthenticationID(String authenticationID) {
        AuthenticationID = authenticationID;
    }

    public String getTruckchit() {
        return truckchit;
    }

    public void setTruckchit(String truckchit) {
        this.truckchit = truckchit;
    }

    public String getXmlTruckchitData() {
        return xmlTruckchitData;
    }

    public void setXmlTruckchitData(String xmlTruckchitData) {
        this.xmlTruckchitData = xmlTruckchitData;
    }

    public String getUploadTRImage() {
        return uploadTRImage;
    }

    public void setUploadTRImage(String uploadTRImage) {
        this.uploadTRImage = uploadTRImage;
    }


    private String ppccode;
    private Long millid;
    private String SystemIP;
    private Integer seasonID;
    private Long TransportID;
    private String TransportDate;
    private Integer VehicleTypeID;
    private String VehicleNumber;
    private String DriverName;
    private String DriverLicenceNumber;
    private String DriverMobileNumber;
    private String ManualPPCTruckchitNumber;
    private Integer NewBags;
    private Integer OldBags;
    private Integer TotalBags;
    private Double TotalQty;
    private String CreatedDate;


    private long t_NewBags;
    private long t_OldBags;
    private long t_TotalBags;
    private Double t_TotalQty;


    public long getT_NewBags() {
        return t_NewBags;
    }

    public void setT_NewBags(long t_NewBags) {
        this.t_NewBags = t_NewBags;
    }

    public long getT_OldBags() {
        return t_OldBags;
    }

    public void setT_OldBags(long t_OldBags) {
        this.t_OldBags = t_OldBags;
    }

    public long getT_TotalBags() {
        return t_TotalBags;
    }

    public void setT_TotalBags(long t_TotalBags) {
        this.t_TotalBags = t_TotalBags;
    }

    public void setT_TotalBags(Integer t_TotalBags) {
        this.t_TotalBags = t_TotalBags;
    }

    public Double getT_TotalQty() {
        return t_TotalQty;
    }

    public void setT_TotalQty(Double t_TotalQty) {
        this.t_TotalQty = t_TotalQty;
    }

    private TCTransData tcTransData;
    private ArrayList<TCTransData> transDataArrayList;

    private String xmlTransData;


    public String getPpccode() {
        return ppccode;
    }

    public void setPpccode(String ppccode) {
        this.ppccode = ppccode;
    }

    public Integer getPpcid() {
        return pPCID;
    }

    public void setPpcid(Integer ppcid) {
        this.pPCID = ppcid;
    }

    public Long getMillid() {
        return millid;
    }

    public void setMillid(Long millid) {
        this.millid = millid;
    }

    public String getSystemIP() {
        return SystemIP;
    }

    public void setSystemIP(String systemIP) {
        SystemIP = systemIP;
    }

    public Integer getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(Integer seasonID) {
        this.seasonID = seasonID;
    }

    public Long getTransportID() {
        return TransportID;
    }

    public void setTransportID(Long transportID) {
        TransportID = transportID;
    }

    public String getTransportDate() {
        return TransportDate;
    }

    public void setTransportDate(String transportDate) {
        TransportDate = transportDate;
    }

    public Integer getVehicleTypeID() {
        return VehicleTypeID;
    }

    public void setVehicleTypeID(Integer vehicleTypeID) {
        VehicleTypeID = vehicleTypeID;
    }

    public String getVehicleNumber() {
        return VehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        VehicleNumber = vehicleNumber;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public String getDriverLicenceNumber() {
        return DriverLicenceNumber;
    }

    public void setDriverLicenceNumber(String driverLicenceNumber) {
        DriverLicenceNumber = driverLicenceNumber;
    }

    public String getDriverMobileNumber() {
        return DriverMobileNumber;
    }

    public void setDriverMobileNumber(String driverMobileNumber) {
        DriverMobileNumber = driverMobileNumber;
    }

    public String getManualPPCTruckchitNumber() {
        return ManualPPCTruckchitNumber;
    }

    public void setManualPPCTruckchitNumber(String manualPPCTruckchitNumber) {
        ManualPPCTruckchitNumber = manualPPCTruckchitNumber;
    }

    public Integer getNewBags() {
        return NewBags;
    }

    public void setNewBags(Integer newBags) {
        NewBags = newBags;
    }

    public Integer getOldBags() {
        return OldBags;
    }

    public void setOldBags(Integer oldBags) {
        OldBags = oldBags;
    }

    public Integer getTotalBags() {
        return TotalBags;
    }

    public void setTotalBags(Integer totalBags) {
        TotalBags = totalBags;
    }

    public Double getTotalQty() {
        return TotalQty;
    }

    public void setTotalQty(Double totalQty) {
        TotalQty = totalQty;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public TCTransData getTcTransData() {
        return tcTransData;
    }

    public void setTcTransData(TCTransData tcTransData) {
        this.tcTransData = tcTransData;
    }

    public ArrayList<TCTransData> getTransDataArrayList() {
        return transDataArrayList;
    }

    public void setTransDataArrayList(ArrayList<TCTransData> transDataArrayList) {
        this.transDataArrayList = transDataArrayList;
    }

    public String getXmlTransData() {
        return xmlTransData;
    }

    public void setXmlTransData(String xmlTransData) {
        this.xmlTransData = xmlTransData;
    }
}
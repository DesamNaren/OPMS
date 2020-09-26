package com.cgg.pps.model.response.report.gunny;

import java.io.Serializable;

public class GunnyReportResponse implements Serializable {
    private String ORDERID;
    private String Sourcetype;
    private String Received;
    private String ManualGunniesTruckchitID;
    private String OrderNewBags;
    private String OrderOldBags;
    private String OrderTotalBags;
    private String TokenNewBags;
    private String TokenOldBags;
    private String TokenTotalBags;
    private String ProcuredNewBags;
    private String ProcuredOldBags;
    private String ProcuredTotalBags;
    private String CancelledNewBags;
    private String CancelledOldBags;
    private String CancelledTotalBags;
    private String AvailableNewBags;
    private String AvailableOldBags;
    private String AvailableTotalBags;
    private String VehicleNo;
    private String VehicleType;
    private String DriverName;
    private String DriverMobileNo;

    public String getDriverMobileNo() {
        return DriverMobileNo;
    }

    public void setDriverMobileNo(String driverMobileNo) {
        DriverMobileNo = driverMobileNo;
    }

    public String getORDERID() {
        return ORDERID;
    }

    public void setORDERID(String ORDERID) {
        this.ORDERID = ORDERID;
    }

    public String getSourcetype() {
        return Sourcetype;
    }

    public void setSourcetype(String sourcetype) {
        Sourcetype = sourcetype;
    }

    public String getReceived() {
        return Received;
    }

    public void setReceived(String received) {
        Received = received;
    }

    public String getManualGunniesTruckchitID() {
        return ManualGunniesTruckchitID;
    }

    public void setManualGunniesTruckchitID(String manualGunniesTruckchitID) {
        ManualGunniesTruckchitID = manualGunniesTruckchitID;
    }

    public String getOrderNewBags() {
        return OrderNewBags;
    }

    public void setOrderNewBags(String orderNewBags) {
        OrderNewBags = orderNewBags;
    }

    public String getOrderOldBags() {
        return OrderOldBags;
    }

    public void setOrderOldBags(String orderOldBags) {
        OrderOldBags = orderOldBags;
    }

    public String getOrderTotalBags() {
        return OrderTotalBags;
    }

    public void setOrderTotalBags(String orderTotalBags) {
        OrderTotalBags = orderTotalBags;
    }

    public String getTokenNewBags() {
        return TokenNewBags;
    }

    public void setTokenNewBags(String tokenNewBags) {
        TokenNewBags = tokenNewBags;
    }

    public String getTokenOldBags() {
        return TokenOldBags;
    }

    public void setTokenOldBags(String tokenOldBags) {
        TokenOldBags = tokenOldBags;
    }

    public String getTokenTotalBags() {
        return TokenTotalBags;
    }

    public void setTokenTotalBags(String tokenTotalBags) {
        TokenTotalBags = tokenTotalBags;
    }

    public String getProcuredNewBags() {
        return ProcuredNewBags;
    }

    public void setProcuredNewBags(String procuredNewBags) {
        ProcuredNewBags = procuredNewBags;
    }

    public String getProcuredOldBags() {
        return ProcuredOldBags;
    }

    public void setProcuredOldBags(String procuredOldBags) {
        ProcuredOldBags = procuredOldBags;
    }

    public String getProcuredTotalBags() {
        return ProcuredTotalBags;
    }

    public void setProcuredTotalBags(String procuredTotalBags) {
        ProcuredTotalBags = procuredTotalBags;
    }

    public String getCancelledNewBags() {
        return CancelledNewBags;
    }

    public void setCancelledNewBags(String cancelledNewBags) {
        CancelledNewBags = cancelledNewBags;
    }

    public String getCancelledOldBags() {
        return CancelledOldBags;
    }

    public void setCancelledOldBags(String cancelledOldBags) {
        CancelledOldBags = cancelledOldBags;
    }

    public String getCancelledTotalBags() {
        return CancelledTotalBags;
    }

    public void setCancelledTotalBags(String cancelledTotalBags) {
        CancelledTotalBags = cancelledTotalBags;
    }

    public String getAvailableNewBags() {
        return AvailableNewBags;
    }

    public void setAvailableNewBags(String availableNewBags) {
        AvailableNewBags = availableNewBags;
    }

    public String getAvailableOldBags() {
        return AvailableOldBags;
    }

    public void setAvailableOldBags(String availableOldBags) {
        AvailableOldBags = availableOldBags;
    }

    public String getAvailableTotalBags() {
        return AvailableTotalBags;
    }

    public void setAvailableTotalBags(String availableTotalBags) {
        AvailableTotalBags = availableTotalBags;
    }

    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        VehicleNo = vehicleNo;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }
}
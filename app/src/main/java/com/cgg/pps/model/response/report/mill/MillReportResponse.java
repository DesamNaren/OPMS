package com.cgg.pps.model.response.report.mill;

import java.io.Serializable;

public class MillReportResponse implements Serializable {
    private String DistrictName;
    private String MandalName;
    private String Nature;
    private String MillID;
    private String MillCode;
    private String MillName;
    private String ROCount;
    private String AllotedQuantity;
    private String UtilizedQuantity;
    private String BalanceQuantity;
    private boolean statusValue;

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    public String getMandalName() {
        return MandalName;
    }

    public void setMandalName(String mandalName) {
        MandalName = mandalName;
    }

    public String getNature() {
        return Nature;
    }

    public void setNature(String nature) {
        Nature = nature;
    }

    public String getMillID() {
        return MillID;
    }

    public void setMillID(String millID) {
        MillID = millID;
    }

    public String getMillCode() {
        return MillCode;
    }

    public void setMillCode(String millCode) {
        MillCode = millCode;
    }

    public String getMillName() {
        return MillName;
    }

    public void setMillName(String millName) {
        MillName = millName;
    }

    public String getROCount() {
        return ROCount;
    }

    public void setROCount(String ROCount) {
        this.ROCount = ROCount;
    }

    public String getAllotedQuantity() {
        return AllotedQuantity;
    }

    public void setAllotedQuantity(String allotedQuantity) {
        AllotedQuantity = allotedQuantity;
    }

    public String getUtilizedQuantity() {
        return UtilizedQuantity;
    }

    public void setUtilizedQuantity(String utilizedQuantity) {
        UtilizedQuantity = utilizedQuantity;
    }

    public String getBalanceQuantity() {
        return BalanceQuantity;
    }

    public void setBalanceQuantity(String balanceQuantity) {
        BalanceQuantity = balanceQuantity;
    }

    public boolean isStatusValue() {
        return statusValue;
    }

    public void setStatusValue(boolean statusValue) {
        this.statusValue = statusValue;
    }
}
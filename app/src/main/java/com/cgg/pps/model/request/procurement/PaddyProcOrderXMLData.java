package com.cgg.pps.model.request.procurement;

public class PaddyProcOrderXMLData {
    private String GunnyOrderId;
    private Integer TokenNewBags;
    private Integer TokenOldBags;
    private Integer TokenTotalBags;
    private Integer ProcuredNewBags;
    private Integer ProcuredOldBags;
    private Integer ProcuredTotalBags;
    private String GunnysGiven_Date;

    public String getGunnyOrderId() {
        return GunnyOrderId;
    }

    public void setGunnyOrderId(String gunnyOrderId) {
        GunnyOrderId = gunnyOrderId;
    }

    public Integer getTokenNewBags() {
        return TokenNewBags;
    }

    public void setTokenNewBags(Integer tokenNewBags) {
        TokenNewBags = tokenNewBags;
    }

    public Integer getTokenOldBags() {
        return TokenOldBags;
    }

    public void setTokenOldBags(Integer tokenOldBags) {
        TokenOldBags = tokenOldBags;
    }

    public Integer getTokenTotalBags() {
        return TokenTotalBags;
    }

    public void setTokenTotalBags(Integer tokenTotalBags) {
        TokenTotalBags = tokenTotalBags;
    }

    public Integer getProcuredNewBags() {
        return ProcuredNewBags;
    }

    public void setProcuredNewBags(Integer procuredNewBags) {
        ProcuredNewBags = procuredNewBags;
    }

    public Integer getProcuredOldBags() {
        return ProcuredOldBags;
    }

    public void setProcuredOldBags(Integer procuredOldBags) {
        ProcuredOldBags = procuredOldBags;
    }

    public Integer getProcuredTotalBags() {
        return ProcuredTotalBags;
    }

    public void setProcuredTotalBags(Integer procuredTotalBags) {
        ProcuredTotalBags = procuredTotalBags;
    }

    public String getGunnysGiven_Date() {
        return GunnysGiven_Date;
    }

    public void setGunnysGiven_Date(String gunnysGiven_Date) {
        GunnysGiven_Date = gunnysGiven_Date;
    }
}

package com.cgg.pps.model.request.gunnydetails;

public class GunnyXMLData {

    private String GunnyOrderId;
    private String TokenNewBags;
    private String TokenOldBags;
    private String TokenTotalBags;
    private String GunnysGiven_Date;

    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getGunnyOrderId() {
        return GunnyOrderId;
    }

    public void setGunnyOrderId(String gunnyOrderId) {
        GunnyOrderId = gunnyOrderId;
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

    public String getGunnysGiven_Date() {
        return GunnysGiven_Date;
    }

    public void setGunnysGiven_Date(String gunnysGiven_Date) {
        GunnysGiven_Date = gunnysGiven_Date;
    }
}

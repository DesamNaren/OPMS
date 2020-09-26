package com.cgg.pps.model.response.farmer.getfarmertokens;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDDLTokens {

    @SerializedName("TokenID")
    @Expose
    private long tokenID;
    @SerializedName("TokenNo")
    @Expose
    private String tokenNo;

    public long getTokenID() {
        return tokenID;
    }

    public void setTokenID(long tokenID) {
        this.tokenID = tokenID;
    }

    public String getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

}

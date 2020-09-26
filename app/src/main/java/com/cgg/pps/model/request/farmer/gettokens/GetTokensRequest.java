package com.cgg.pps.model.request.farmer.gettokens;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTokensRequest {

    @SerializedName("AuthenticationID")
    @Expose
    private String AuthenticationID;
    @SerializedName("PPCID")
    @Expose
    private String pPCID;
    @SerializedName("TokenID")
    @Expose
    private long tokenID;
    @SerializedName("TokenStatus")
    @Expose
    private String tokenStatus;


    public String getAuthenticationID() {
        return AuthenticationID;
    }

    public void setAuthenticationID(String authenticationID) {
        AuthenticationID = authenticationID;
    }

    public String getPPCID() {
        return pPCID;
    }

    public void setPPCID(String pPCID) {
        this.pPCID = pPCID;
    }

    public long getTokenID() {
        return tokenID;
    }

    public void setTokenID(long tokenID) {
        this.tokenID = tokenID;
    }

    public String getTokenStatus() {
        return tokenStatus;
    }

    public void setTokenStatus(String tokenStatus) {
        this.tokenStatus = tokenStatus;
    }

}

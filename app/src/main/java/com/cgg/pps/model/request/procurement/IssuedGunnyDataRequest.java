package com.cgg.pps.model.request.procurement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IssuedGunnyDataRequest {
    @SerializedName("AuthenticationID")
    @Expose
    private String AuthenticationID;
    @SerializedName("PPCID")
    @Expose
    private String pPCID;
    @SerializedName("TokenID")
    @Expose
    private String tokenID;
    @SerializedName("TransactionID")
    @Expose
    private String transactionID;
    @SerializedName("TransactionRowID")
    @Expose
    private String transactionRowID;
    @SerializedName("TransactionStatus")
    @Expose
    private String transactionStatus;


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

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionRowID() {
        return transactionRowID;
    }

    public void setTransactionRowID(String transactionRowID) {
        this.transactionRowID = transactionRowID;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

}

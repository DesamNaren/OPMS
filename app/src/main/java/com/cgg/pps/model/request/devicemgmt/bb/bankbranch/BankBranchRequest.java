package com.cgg.pps.model.request.devicemgmt.bb.bankbranch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankBranchRequest {

    @SerializedName("AuthenticationID")
    @Expose
    private String AuthenticationID;
    @SerializedName("PPCID")
    @Expose
    private Integer pPCID;

    public String getAuthenticationID() {
        return AuthenticationID;
    }

    public void setAuthenticationID(String authenticationID) {
        AuthenticationID = authenticationID;
    }

    public Integer getPPCID() {
        return pPCID;
    }

    public void setPPCID(Integer pPCID) {
        this.pPCID = pPCID;
    }

}

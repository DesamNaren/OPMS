package com.cgg.pps.model.request.truckchit;

public class MillerMasterRequest {
    private String AuthenticationID;
    private String PPCID;

    public String getAuthenticationID() {
        return AuthenticationID;
    }

    public void setAuthenticationID(String authenticationID) {
        AuthenticationID = authenticationID;
    }

    public String getPPCID() {
        return PPCID;
    }

    public void setPPCID(String PPCID) {
        this.PPCID = PPCID;
    }
}

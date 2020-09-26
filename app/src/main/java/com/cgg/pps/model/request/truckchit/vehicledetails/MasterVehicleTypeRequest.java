package com.cgg.pps.model.request.truckchit.vehicledetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MasterVehicleTypeRequest {

    @SerializedName("AuthenticationID")
    @Expose
    private String AuthenticationID;


    public String getAuthenticationID() {
        return AuthenticationID;
    }

    public void setAuthenticationID(String authenticationID) {
        AuthenticationID = authenticationID;
    }
}

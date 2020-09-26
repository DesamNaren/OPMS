package com.cgg.pps.model.response.procurement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveProcurementOutput {

    @SerializedName("FarmerTransactionID")
    @Expose
    private String farmerTransactionID;

    public String getFarmerTransactionID() {
        return farmerTransactionID;
    }

    public void setFarmerTransactionID(String farmerTransactionID) {
        this.farmerTransactionID = farmerTransactionID;
    }

}

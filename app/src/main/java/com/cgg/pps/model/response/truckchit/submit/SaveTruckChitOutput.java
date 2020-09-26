package com.cgg.pps.model.response.truckchit.submit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveTruckChitOutput {

    @SerializedName("TruckSlipNo")
    @Expose
    private String truckSlipNo;
    @SerializedName("ManualPPCTruckchitID")
    @Expose
    private String manualPPCTruckchitID;

    public String getTruckSlipNo() {
        return truckSlipNo;
    }

    public void setTruckSlipNo(String truckSlipNo) {
        this.truckSlipNo = truckSlipNo;
    }

    public String getManualPPCTruckchitID() {
        return manualPPCTruckchitID;
    }

    public void setManualPPCTruckchitID(String manualPPCTruckchitID) {
        this.manualPPCTruckchitID = manualPPCTruckchitID;
    }

}

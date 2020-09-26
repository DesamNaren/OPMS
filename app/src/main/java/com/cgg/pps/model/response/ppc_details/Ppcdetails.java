package com.cgg.pps.model.response.ppc_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ppcdetails {

    @SerializedName("PPCCode")
    @Expose
    private String pPCCode;
    @SerializedName("PPCName")
    @Expose
    private String pPCName;
    @SerializedName("CoordinatorName")
    @Expose
    private String coordinatorName;
    @SerializedName("CoordinatorMobile")
    @Expose
    private String coordinatorMobile;

    public String getPPCCode() {
        return pPCCode;
    }

    public void setPPCCode(String pPCCode) {
        this.pPCCode = pPCCode;
    }

    public String getPPCName() {
        return pPCName;
    }

    public void setPPCName(String pPCName) {
        this.pPCName = pPCName;
    }

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public void setCoordinatorName(String coordinatorName) {
        this.coordinatorName = coordinatorName;
    }

    public String getCoordinatorMobile() {
        return coordinatorMobile;
    }

    public void setCoordinatorMobile(String coordinatorMobile) {
        this.coordinatorMobile = coordinatorMobile;
    }

}

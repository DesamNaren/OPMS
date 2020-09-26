package com.cgg.pps.model.response.reprint.truckchit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Commonpaddydetail {

    @SerializedName("CommonNewbag")
    @Expose
    private Integer commonNewbag;
    @SerializedName("CommonOldbag")
    @Expose
    private Integer commonOldbag;
    @SerializedName("CommonTotalbag")
    @Expose
    private Integer commonTotalbag;
    @SerializedName("CommonQuantity")
    @Expose
    private Double commonQuantity;

    public Integer getCommonNewbag() {
        return commonNewbag;
    }

    public void setCommonNewbag(Integer commonNewbag) {
        this.commonNewbag = commonNewbag;
    }

    public Integer getCommonOldbag() {
        return commonOldbag;
    }

    public void setCommonOldbag(Integer commonOldbag) {
        this.commonOldbag = commonOldbag;
    }

    public Integer getCommonTotalbag() {
        return commonTotalbag;
    }

    public void setCommonTotalbag(Integer commonTotalbag) {
        this.commonTotalbag = commonTotalbag;
    }

    public Double getCommonQuantity() {
        return commonQuantity;
    }

    public void setCommonQuantity(Double commonQuantity) {
        this.commonQuantity = commonQuantity;
    }

}

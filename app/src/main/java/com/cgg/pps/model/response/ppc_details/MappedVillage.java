package com.cgg.pps.model.response.ppc_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MappedVillage {

    @SerializedName("mappedVilageId")
    @Expose
    private String mappedVilageId;
    @SerializedName("mappedVillage")
    @Expose
    private String mappedVillage;

    public String getMappedVilageId() {
        return mappedVilageId;
    }

    public void setMappedVilageId(String mappedVilageId) {
        this.mappedVilageId = mappedVilageId;
    }

    public String getMappedVillage() {
        return mappedVillage;
    }

    public void setMappedVillage(String mappedVillage) {
        this.mappedVillage = mappedVillage;
    }

}

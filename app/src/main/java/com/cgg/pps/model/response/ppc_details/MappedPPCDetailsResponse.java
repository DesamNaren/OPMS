package com.cgg.pps.model.response.ppc_details;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MappedPPCDetailsResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("ppcdetails")
    @Expose
    private List<Ppcdetails> ppcdetails;
    @SerializedName("mappedVillages")
    @Expose
    private List<MappedVillage> mappedVillages = null;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public List<Ppcdetails> getPpcdetails() {
        return ppcdetails;
    }

    public void setPpcdetails(List<Ppcdetails> ppcdetails) {
        this.ppcdetails = ppcdetails;
    }

    public List<MappedVillage> getMappedVillages() {
        return mappedVillages;
    }

    public void setMappedVillages(List<MappedVillage> mappedVillages) {
        this.mappedVillages = mappedVillages;
    }

}


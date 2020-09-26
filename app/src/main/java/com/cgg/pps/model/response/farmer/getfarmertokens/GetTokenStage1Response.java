package com.cgg.pps.model.response.farmer.getfarmertokens;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTokenStage1Response {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("getTokensddl")
    @Expose
    private List<Stage1GetTokensddl> stage1GetTokensddls = null;

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

    public List<Stage1GetTokensddl> getStage1GetTokensddls() {
        return stage1GetTokensddls;
    }

    public void setStage1GetTokensddls(List<Stage1GetTokensddl> stage1GetTokensddls) {
        this.stage1GetTokensddls = stage1GetTokensddls;
    }
}


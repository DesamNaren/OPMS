package com.cgg.pps.model.response.validateuser.logout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogoutResponse {


    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("validateUserVersion")
    @Expose
    private String validateUserVersion;
    @SerializedName("result")
    @Expose
    private String result;


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

    public String getValidateUserVersion() {
        return validateUserVersion;
    }

    public void setValidateUserVersion(String validateUserVersion) {
        this.validateUserVersion = validateUserVersion;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


}

package com.cgg.pps.model.response.validateuser.changepwd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePasswordResponse {

    @SerializedName("$id")
    @Expose
    private String $id;
    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("ResponseName")
    @Expose
    private String responseName;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("validateUserVersion")
    @Expose
    private String validateUserVersion;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("Error")
    @Expose
    private String error;

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseName() {
        return responseName;
    }

    public void setResponseName(String responseName) {
        this.responseName = responseName;
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}

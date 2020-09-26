package com.cgg.pps.model.request.reports;

public class CommonReportRequest {
    private String AuthenticationID;
    private String PPCID;
    private String TokenID;
    private String TokenNo;
    private String RegistrationNo;
    private String TransactionID;
    private String TransactionRowID;
    private String TransactionStatus;
    private String serviceName;

    public String getPPCID() {
        return PPCID;
    }

    public void setPPCID(String PPCID) {
        this.PPCID = PPCID;
    }

    public String getTokenID() {
        return TokenID;
    }

    public void setTokenID(String tokenID) {
        TokenID = tokenID;
    }

    public String getTokenNo() {
        return TokenNo;
    }

    public void setTokenNo(String tokenNo) {
        TokenNo = tokenNo;
    }

    public String getRegistrationNo() {
        return RegistrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        RegistrationNo = registrationNo;
    }

    public String getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID(String transactionID) {
        TransactionID = transactionID;
    }

    public String getTransactionRowID() {
        return TransactionRowID;
    }

    public void setTransactionRowID(String transactionRowID) {
        TransactionRowID = transactionRowID;
    }

    public String getTransactionStatus() {
        return TransactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        TransactionStatus = transactionStatus;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAuthenticationID() {
        return AuthenticationID;
    }

    public void setAuthenticationID(String authenticationID) {
        AuthenticationID = authenticationID;
    }
}

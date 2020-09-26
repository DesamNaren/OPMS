package com.cgg.pps.model.request.truckchit.transaction;

public class TCTransaction {

    private String TokenNumber;
    private String TokenID;
    private String TransactionID;
    private Long TransactionRowID;
    private Integer PaddyType;
    private Integer NewBags;
    private Integer OldBags;
    private Integer TotalBags;
    private Double Quantity;
    private String ROID;


    public String getTokenNumber() {
        return TokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        TokenNumber = tokenNumber;
    }

    public String getTokenID() {
        return TokenID;
    }

    public void setTokenID(String tokenID) {
        TokenID = tokenID;
    }

    public String getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID(String transactionID) {
        TransactionID = transactionID;
    }

    public Long getTransactionRowID() {
        return TransactionRowID;
    }

    public void setTransactionRowID(Long transactionRowID) {
        TransactionRowID = transactionRowID;
    }

    public Integer getPaddyType() {
        return PaddyType;
    }

    public void setPaddyType(Integer paddyType) {
        PaddyType = paddyType;
    }

    public Integer getNewBags() {
        return NewBags;
    }

    public void setNewBags(Integer newBags) {
        NewBags = newBags;
    }

    public Integer getOldBags() {
        return OldBags;
    }

    public void setOldBags(Integer oldBags) {
        OldBags = oldBags;
    }

    public Integer getTotalBags() {
        return TotalBags;
    }

    public void setTotalBags(Integer totalBags) {
        TotalBags = totalBags;
    }

    public Double getQuantity() {
        return Quantity;
    }

    public void setQuantity(Double quantity) {
        Quantity = quantity;
    }

    public String getROID() {
        return ROID;
    }

    public void setROID(String ROID) {
        this.ROID = ROID;
    }
}

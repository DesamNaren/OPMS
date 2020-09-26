package com.cgg.pps.model.response.devicemgmt.paddyvalues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PaddyEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("GradeID")
    @Expose
    private Integer gradeID;
    @SerializedName("GradeName")
    @Expose
    private String gradeName;
    @SerializedName("MSPAmount")
    @Expose
    private Integer mSPAmount;
    @SerializedName("TestID")
    @Expose
    private Integer testID;
    @SerializedName("TestType")
    @Expose
    private String testType;
    @SerializedName("Wastage")
    @Expose
    private Integer wastage;
    @SerializedName("OtherEatingGrains")
    @Expose
    private Integer otherEatingGrains;
    @SerializedName("UnmaturedGrains")
    @Expose
    private Integer unmaturedGrains;
    @SerializedName("DamagedGrains")
    @Expose
    private Integer damagedGrains;
    @SerializedName("LowGradeGrain")
    @Expose
    private Integer lowGradeGrain;
    @SerializedName("Moisture")
    @Expose
    private Integer moisture;
    @SerializedName("FromDate")
    @Expose
    private String fromDate;
    @SerializedName("SeasonID")
    @Expose
    private Integer seasonID;

    public Integer getGradeID() {
        return gradeID;
    }

    public void setGradeID(Integer gradeID) {
        this.gradeID = gradeID;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public Integer getMSPAmount() {
        return mSPAmount;
    }

    public void setMSPAmount(Integer mSPAmount) {
        this.mSPAmount = mSPAmount;
    }

    public Integer getTestID() {
        return testID;
    }

    public void setTestID(Integer testID) {
        this.testID = testID;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public Integer getWastage() {
        return wastage;
    }

    public void setWastage(Integer wastage) {
        this.wastage = wastage;
    }

    public Integer getOtherEatingGrains() {
        return otherEatingGrains;
    }

    public void setOtherEatingGrains(Integer otherEatingGrains) {
        this.otherEatingGrains = otherEatingGrains;
    }

    public Integer getUnmaturedGrains() {
        return unmaturedGrains;
    }

    public void setUnmaturedGrains(Integer unmaturedGrains) {
        this.unmaturedGrains = unmaturedGrains;
    }

    public Integer getDamagedGrains() {
        return damagedGrains;
    }

    public void setDamagedGrains(Integer damagedGrains) {
        this.damagedGrains = damagedGrains;
    }

    public Integer getLowGradeGrain() {
        return lowGradeGrain;
    }

    public void setLowGradeGrain(Integer lowGradeGrain) {
        this.lowGradeGrain = lowGradeGrain;
    }

    public Integer getMoisture() {
        return moisture;
    }

    public void setMoisture(Integer moisture) {
        this.moisture = moisture;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public Integer getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(Integer seasonID) {
        this.seasonID = seasonID;
    }

}

package com.cgg.pps.model.response.reprint.truckchit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GradeApaddydetail {

    @SerializedName("GradeANewbag")
    @Expose
    private Integer gradeANewbag;
    @SerializedName("GradeAOldbag")
    @Expose
    private Integer gradeAOldbag;
    @SerializedName("GradeATotalbag")
    @Expose
    private Integer gradeATotalbag;
    @SerializedName("GradeAQuantity")
    @Expose
    private Double gradeAQuantity;

    public Integer getGradeANewbag() {
        return gradeANewbag;
    }

    public void setGradeANewbag(Integer gradeANewbag) {
        this.gradeANewbag = gradeANewbag;
    }

    public Integer getGradeAOldbag() {
        return gradeAOldbag;
    }

    public void setGradeAOldbag(Integer gradeAOldbag) {
        this.gradeAOldbag = gradeAOldbag;
    }

    public Integer getGradeATotalbag() {
        return gradeATotalbag;
    }

    public void setGradeATotalbag(Integer gradeATotalbag) {
        this.gradeATotalbag = gradeATotalbag;
    }

    public Double getGradeAQuantity() {
        return gradeAQuantity;
    }

    public void setGradeAQuantity(Double gradeAQuantity) {
        this.gradeAQuantity = gradeAQuantity;
    }

}

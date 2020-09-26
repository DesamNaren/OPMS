package com.cgg.pps.model.response.socialstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class SocialEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("SocialStatus")
    @Expose

    private String socialStatus;
    @SerializedName("SocialStatusID")
    @Expose
    private Integer socialStatusID;


    public String getSocialStatus() {
        return socialStatus;
    }

    public void setSocialStatus(String socialStatus) {
        this.socialStatus = socialStatus;
    }

    public Integer getSocialStatusID() {
        return socialStatusID;
    }

    public void setSocialStatusID(Integer socialStatusID) {
        this.socialStatusID = socialStatusID;
    }

}

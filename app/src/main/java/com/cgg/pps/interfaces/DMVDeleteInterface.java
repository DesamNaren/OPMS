package com.cgg.pps.interfaces;

import android.widget.Spinner;

import com.cgg.pps.model.response.devicemgmt.masterdistrict.DistrictEntity;
import com.cgg.pps.model.response.devicemgmt.mastermandal.MandalEntity;
import com.cgg.pps.model.response.devicemgmt.mastervillage.VillageEntity;
import com.cgg.pps.model.response.socialstatus.SocialEntity;

import java.util.List;

public interface DMVDeleteInterface {
    void onDataNotAvailable();
    void distCount(int cnt);
    void vehCount(int cnt);
}

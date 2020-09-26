package com.cgg.pps.interfaces;

import android.widget.Spinner;

import com.cgg.pps.model.response.devicemgmt.masterdistrict.DistrictEntity;
import com.cgg.pps.model.response.devicemgmt.mastermandal.MandalEntity;
import com.cgg.pps.model.response.devicemgmt.mastervillage.VillageEntity;
import com.cgg.pps.model.response.socialstatus.SocialEntity;

import java.util.List;

public interface DMVInterface {
    void getAllDistrictEntities(List<DistrictEntity> districtNames);

    void getAllMandalEntities(List<MandalEntity> mandalEntities);

    void getAllVillageEntities(List<VillageEntity> villageEntities);

    void getAllSocialEntities(List<SocialEntity> socialEntities);

    void onDataNotAvailable();

    void distCount(int cnt);

    void mandalCount(int cnt);

    void villageCount(int cnt);

    void getAllMandalNamesByDist(List<String> mandalEntities, Spinner spinner, Spinner vspinner, String distName);

    void getAllLandMandalNamesByDist(List<String> mandalEntities, Spinner spinner, Spinner vspinner, String distName);

    void getAllVillagesByMan(List<String> villageEntities, Spinner spinner, String distName, String manName);

    void getAllowDistrictEntities(List<DistrictEntity> districtNames);

    void getAllowMandalEntities(List<MandalEntity> mandalNames);

    void getAllowVillageEntities(List<VillageEntity> villageNames);

}

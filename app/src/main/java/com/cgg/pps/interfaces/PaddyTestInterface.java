package com.cgg.pps.interfaces;

import com.cgg.pps.model.response.devicemgmt.masterbank.BankEntity;
import com.cgg.pps.model.response.devicemgmt.paddyvalues.PaddyEntity;

import java.util.ArrayList;
import java.util.List;

public interface PaddyTestInterface {
    void getAllPaddyValues(List<PaddyEntity> paddyEntities);
    void paddyTestCount(int cnt);

    void getDistName(String distName);
    void getManName(String manName);
    void getVilName(String vilName);
}

package com.cgg.pps.interfaces;

import android.widget.TextView;

import java.util.List;

public interface DMVLTestFAQInterface {
    void getAllMandalNamesByDist(List<String> mandalEntities, TextView spinner, String distName);

    void getAllLandMandalNamesByDist(List<String> mandalEntities, TextView spinner, String distName);

    void getAllVillagesByMan(List<String> villageEntities, TextView spinner, String manName);
    void getPaddyTestData();
}

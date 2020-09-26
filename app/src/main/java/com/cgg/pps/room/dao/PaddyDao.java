package com.cgg.pps.room.dao;

import com.cgg.pps.model.response.devicemgmt.masterbank.BankEntity;
import com.cgg.pps.model.response.devicemgmt.paddyvalues.PaddyEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface PaddyDao {

    @Insert
    void insertAllPaddy(List<PaddyEntity> paddyTestEntities);

    @Query("DELETE FROM PaddyEntity")
    void deleteAllPaddy();


    @Query("SELECT COUNT(*) FROM PaddyEntity")
    int paddyTestCount();


    @Query("SELECT * FROM PaddyEntity")
    List<PaddyEntity> getAllPaddyValues();

    @Query("SELECT distinct distName from DistrictEntity where distId LIKE :distId ")
    String getDistrictName(String distId);


    @Query("Select mandalName from MandalEntity where districtID LIKE :distID and mandalID LIKE :manID")
    String getMandalName(String distID, String manID);


    @Query("Select VillageName from VillageEntity where districtID LIKE :distID and mandalID LIKE :manID and villageID LIKE :vilId")
    String getVillageName(String distID, String manID, String vilId);

    


}

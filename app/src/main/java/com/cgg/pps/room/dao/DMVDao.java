package com.cgg.pps.room.dao;

import com.cgg.pps.model.response.devicemgmt.masterdistrict.DistrictEntity;
import com.cgg.pps.model.response.devicemgmt.mastermandal.MandalEntity;
import com.cgg.pps.model.response.devicemgmt.mastervillage.VillageEntity;
import com.cgg.pps.model.response.socialstatus.SocialEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface DMVDao {


    @Insert
    void insertAllDistricts(List<DistrictEntity> districtEntities);

    @Query("DELETE FROM DistrictEntity")
    void deleteAllDistricts();

    @Query("SELECT COUNT(*) FROM DistrictEntity")
    int districtCount();

    @Insert
    void insertAllMandals(List<MandalEntity> mandalEntities);


    @Query("DELETE FROM MandalEntity")
    void deleteAllMandals();


    @Query("SELECT COUNT(*) FROM MANDALENTITY")
    int mandalCount();

    @Insert
    void insertAllVillages(List<VillageEntity> villageEntities);

    @Query("DELETE FROM VillageEntity")
    void deleteAllVillages();

    @Query("SELECT COUNT(*) FROM VillageEntity")
    int villageCount();


    @Query("SELECT distinct distName from DistrictEntity where distId LIKE :distId ")
    String getDistrictName(String distId);

    @Query("SELECT distinct socialStatus from SocialEntity where socialStatusID LIKE :sId ")
    String getSocialStatusName(String sId);

    @Query("SELECT distinct distId from DistrictEntity where distName LIKE :distName ")
    String getDistrictID(String distName);

    @Query("Select mandalID from MandalEntity where districtID LIKE :distId and mandalName LIKE :manName")
    String getMandalID(String distId, String manName);

    @Query("Select mandalName from MandalEntity where districtID LIKE :distID and mandalID LIKE :manID")
    String getMandalName(String distID, String manID);


    @Query("Select VillageName from VillageEntity where districtID LIKE :distID and mandalID LIKE :manID and villageID LIKE :vilId")
    String getVillageName(String distID, String manID, String vilId);


    @Query("Select villageID from VillageEntity where districtID LIKE :distID and mandalID LIKE :manID and villageName LIKE :vilName")
    String getVillageId(String distID, String manID, String vilName);

    @Query("SELECT * FROM DistrictEntity")
    List<DistrictEntity> getAllDistricts();


    @Query("SELECT * FROM MandalEntity")
    List<MandalEntity> getAllMandals();


    @Query("SELECT * FROM VillageEntity")
    List<VillageEntity> getAllVillages();


    @Query("select distinct M.mandalName from  MandalEntity M,DistrictEntity D "
            + "where M.districtID=D.distId and D.distName LIKE :districtName")
    List<String> getMandalsByDistName(String districtName);

    @Query("select distinct V.villageName from  VillageEntity V,MandalEntity M, DistrictEntity D"
            + " where V.districtID=D.distId and V.mandalID=M.mandalID and M.districtID=D.distId and" +
            " d.distName LIKE :districtName and M.mandalName LIKE :mandalName")
    List<String> getVillagesByManName(String districtName, String mandalName);


    @Query("SELECT * FROM SocialEntity")
    List<SocialEntity> getAllSocialEntities();


    // allow districts in far updattion
    @Query("SELECT * FROM DistrictEntity where IsMapped='Y'")
    List<DistrictEntity> getAllowDistricts();

    // allow mandals in far updattion
    @Query("SELECT * FROM MandalEntity where IsMapped='Y'")
    List<MandalEntity> getAllowMandals();

    // allow villages in far updattion
    @Query("SELECT * FROM VillageEntity where IsMapped='Y'")
    List<VillageEntity> getAllowVillages();
}

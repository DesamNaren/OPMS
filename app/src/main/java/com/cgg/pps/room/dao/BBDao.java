package com.cgg.pps.room.dao;

import com.cgg.pps.model.response.devicemgmt.bankbranch.BranchEntity;
import com.cgg.pps.model.response.devicemgmt.masterbank.BankEntity;
import com.cgg.pps.model.response.devicemgmt.paddyvalues.PaddyEntity;
import com.cgg.pps.model.response.socialstatus.SocialEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface BBDao {

    @Insert
    void insertAllBanks(List<BankEntity> bankEntities);

    @Query("DELETE FROM BankEntity")
    void deleteAllBanks();

    @Query("SELECT distinct bankName from BankEntity where bankID LIKE :bankId ")
    String getBankName(String bankId);


    @Query("SELECT distinct bankID from BankEntity where bankName LIKE :bankName ")
    Integer getBankId(String bankName);


    @Query("SELECT distinct branchName from BranchEntity where bankID LIKE :bankId and branchID LIKE :branchId ")
    String getBranchName(String bankId, String branchId);


    @Query("SELECT distinct branchID from BranchEntity where bankID LIKE :bankId and branchName LIKE :branchName ")
    Integer getBranchId(Integer bankId, String branchName);

    @Query("SELECT COUNT(*) FROM BankEntity")
    int bankCount();


    @Query("SELECT COUNT(*) FROM SocialEntity")
    int socialCount();

    @Insert
    void insertAllBranches(List<BranchEntity> branchEntities);

    @Query("DELETE FROM BranchEntity")
    void deleteAllBranches();

    @Query("SELECT COUNT(*) FROM BranchEntity")
    int branchCount();

    @Query("SELECT * FROM BankEntity")
    List<BankEntity> getAllBanks();


    @Query("SELECT * FROM BranchEntity")
    List<BranchEntity> getAllBranches();

    @Insert
    void insertPaddyValues(List<PaddyEntity> paddyEntities);

    @Query("DELETE FROM PaddyEntity")
    void deletePaddyValues();

    @Insert
    void insertSocialValues(List<SocialEntity> socialEntities);

    @Query("DELETE FROM SocialEntity")
    void deleteSocialValues();

}

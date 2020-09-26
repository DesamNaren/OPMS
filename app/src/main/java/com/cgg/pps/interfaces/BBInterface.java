package com.cgg.pps.interfaces;

import com.cgg.pps.model.response.devicemgmt.bankbranch.BranchEntity;
import com.cgg.pps.model.response.devicemgmt.masterbank.BankEntity;

import java.util.List;

public interface BBInterface {
    void getAllBanks(List<BankEntity> bankEntities);

    void getAllBranches(List<BranchEntity> branchEntities);

    void onDataNotAvailable();

    void bankCount(int cnt);

    void branchCount(int cnt);

    void getBankName(String bankName);

    void getBranchName(String bankName);

    void socialCount(int cnt);

}

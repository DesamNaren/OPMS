package com.cgg.pps.interfaces;

import com.cgg.pps.base.BaseView;
import com.cgg.pps.model.response.devicemgmt.bankbranch.MasterBranchMainResponse;
import com.cgg.pps.model.response.devicemgmt.masterbank.MasterBankMainResponse;
import com.cgg.pps.model.response.devicemgmt.masterdistrict.MasterDistrictMainResponse;
import com.cgg.pps.model.response.devicemgmt.mastermandal.MasterMandalMainResponse;
import com.cgg.pps.model.response.devicemgmt.mastervillage.MasterVillageMainResponse;
import com.cgg.pps.model.response.devicemgmt.paddyvalues.PaddyTestResponse;
import com.cgg.pps.model.response.socialstatus.SocialStatusResponse;
import com.cgg.pps.model.response.truckchit.mastervehicle.VehicleResponse;

public interface DevMgmtInterface extends BaseView {
    void getBankResponse(MasterBankMainResponse masterBankResponse);
    void getPaddyTestResponse(PaddyTestResponse paddyTestResponse);
    void getSocialStatusResponse(SocialStatusResponse socialStatusResponse);
    void getBranchResponse(MasterBranchMainResponse masterBranchResponse);
    void getDistrictResponse(MasterDistrictMainResponse masterDistrictMainResponse);
    void getMandalResponse(MasterMandalMainResponse masterMandalMainResponse);
    void getVillageResponse(MasterVillageMainResponse masterVillageMainResponse);
    void getVehicleDataResponse(VehicleResponse vehicleResponse);
}

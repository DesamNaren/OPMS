package com.cgg.pps.interfaces;

import com.cgg.pps.base.BaseView;
import com.cgg.pps.model.request.gunnydetails.GunnyDetailsRequest;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensDDLResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
import com.cgg.pps.model.response.gunnydetails.GunnyDetailsResponse;
import com.cgg.pps.model.response.gunnydetails.gunnysubmit.GunnySubmitResponse;


public interface GunnysGivenInterface extends BaseView {
    void getTokensGunnysResponseData(GetTokensDDLResponse getTokensFarmerResponse);
    void GetSelectedGunnysTokenData(GetTokensResponse getTokensFarmerResponse);
    void GetGetGunnyOrderDetails(GunnyDetailsResponse gunnyDetailsResponse);
    void GetGunnySubmitDetails(GunnySubmitResponse gunnySubmitResponse);
}
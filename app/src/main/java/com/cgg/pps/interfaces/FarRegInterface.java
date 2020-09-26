package com.cgg.pps.interfaces;

import com.cgg.pps.base.BaseView;
import com.cgg.pps.model.response.farmer.farmerdetails.FarmerDetailsResponse;
import com.cgg.pps.model.response.farmer.farmersubmit.FarmerSubmitResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokenDropDownDataResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokenStage1Response;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensDDLResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
import com.cgg.pps.model.response.farmer.tokengenarate.TokenGenerateResponse;

public interface FarRegInterface extends BaseView {
    void getFarmerRegResponse(FarmerDetailsResponse farmerDetailsResponse);
    void tokenGenerateResponse(TokenGenerateResponse tokenGenerateResponse);
    void getTokensFarmerResponse(GetTokenStage1Response getTokenStage1Response);
    void getTokenDropDownDataResponse(GetTokenDropDownDataResponse getTokenDropDownDataResponse);
    void farmerSubmitResponse(FarmerSubmitResponse farmerSubmitResponse);
}

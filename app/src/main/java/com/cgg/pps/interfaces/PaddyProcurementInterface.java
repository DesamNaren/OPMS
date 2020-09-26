package com.cgg.pps.interfaces;

import com.cgg.pps.base.BaseView;
import com.cgg.pps.model.response.faq.FAQSubmitResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensDDLResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
import com.cgg.pps.model.response.gunnydetails.GunnyDetailsResponse;
import com.cgg.pps.model.response.gunnydetails.gunnysubmit.GunnySubmitResponse;
import com.cgg.pps.model.response.procurement.IssuedGunnyDataResponse;
import com.cgg.pps.model.response.procurement.ProcurementSubmitResponse;

public interface PaddyProcurementInterface extends BaseView {
    void getTokensProcurementResponseData(GetTokensDDLResponse getTokensDDLResponse);
    void GetSelectedProcurementTokenData(GetTokensResponse getTokensFarmerResponse);
    void GetIssuedGunnyDataResponseData(IssuedGunnyDataResponse issuedGunnyDataResponse);
    void PaddyProcurementSubmit(ProcurementSubmitResponse procurementSubmitResponse);
}

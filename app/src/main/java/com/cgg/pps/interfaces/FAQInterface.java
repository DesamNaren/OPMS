package com.cgg.pps.interfaces;

import com.cgg.pps.base.BaseView;
import com.cgg.pps.model.response.faq.FAQSubmitResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensDDLResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;

public interface FAQInterface extends BaseView {
    void getTokensFAQResponseData(GetTokensDDLResponse getTokensDDLResponse);
    void GetSelectedTokenData(GetTokensResponse getTokensFarmerResponse);
    void FAQSubmit(FAQSubmitResponse faqSubmitResponse);
}

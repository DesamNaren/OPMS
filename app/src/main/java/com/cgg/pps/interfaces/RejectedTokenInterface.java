package com.cgg.pps.interfaces;

import com.cgg.pps.base.BaseView;
import com.cgg.pps.model.response.enablefaqresponse.EnableFAQResponse;
import com.cgg.pps.model.response.rejectedtokenresponse.RejectedTokenResponse;
import com.cgg.pps.model.response.rejectedtokenresponse.TokenRejectionOutput;

public interface RejectedTokenInterface extends BaseView {
    void GetRejectedTokenData(RejectedTokenResponse rejectedTokenResponse);
    void GetFAQEnableResponse(EnableFAQResponse enableFAQResponse);
}
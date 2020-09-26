package com.cgg.pps.interfaces;

import com.cgg.pps.base.BaseView;
import com.cgg.pps.model.response.enablefaqresponse.EnableFAQResponse;

public interface EnableFAQTokenInterface extends BaseView {
    void GetApprovedTokenData(EnableFAQResponse enableFAQResponse);
}

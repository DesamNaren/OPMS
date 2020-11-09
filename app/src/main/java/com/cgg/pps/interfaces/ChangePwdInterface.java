package com.cgg.pps.interfaces;

import com.cgg.pps.base.BaseView;
import com.cgg.pps.model.response.changepwd.ChangePwdResponse;

public interface ChangePwdInterface extends BaseView {
    void updatePwdResponse(ChangePwdResponse changePwdResponse);
}

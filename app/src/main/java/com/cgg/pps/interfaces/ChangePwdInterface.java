package com.cgg.pps.interfaces;

import com.cgg.pps.base.BaseView;
import com.cgg.pps.model.response.ChangePwdResponse;
import com.cgg.pps.model.response.truckchit.mastervehicle.VehicleResponse;
import com.cgg.pps.model.response.validateuser.login.ValidateUserResponse;

public interface ChangePwdInterface extends BaseView {
    void updatePwdResponse(ChangePwdResponse changePwdResponse);
}

package com.cgg.pps.interfaces;

import com.cgg.pps.base.BaseView;
import com.cgg.pps.model.response.validateuser.logout.LogoutResponse;

public interface LogoutInterface extends BaseView {
    void getLogoutResponse(LogoutResponse logoutResponse);
}

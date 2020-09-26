package com.cgg.pps.interfaces;

import com.cgg.pps.base.BaseView;
import com.cgg.pps.model.response.farmer.farmerUpdate.FarmerUpdateResponse;

public interface FarUpdateInterface extends BaseView {
    void getFarmerUpdateResponse(FarmerUpdateResponse farmerUpdateResponse);
}

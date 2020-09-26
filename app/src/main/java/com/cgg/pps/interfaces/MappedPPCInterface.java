package com.cgg.pps.interfaces;

import com.cgg.pps.base.BaseView;
import com.cgg.pps.model.response.ppc_details.MappedPPCDetailsResponse;

public interface MappedPPCInterface extends BaseView {
    void getMappedPPCDetailsResponse(MappedPPCDetailsResponse mappedPpcDetailsResponse);
}

package com.cgg.pps.interfaces;

import com.cgg.pps.base.BaseView;
import com.cgg.pps.model.response.reprint.ProcRePrintResponse;
import com.cgg.pps.model.response.reprint.TokenRePrintResponse;
import com.cgg.pps.model.response.reprint.truckchit.TCRePrintResponse;

public interface RePrintInterface extends BaseView {
    void GetRePrintTokenData(TokenRePrintResponse tokenRePrintResponse);
    void GetProcRePrintData(ProcRePrintResponse procRePrintResponse);
    void GetTCRePrintData(TCRePrintResponse tcRePrintResponse);
}

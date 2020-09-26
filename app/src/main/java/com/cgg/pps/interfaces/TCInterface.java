package com.cgg.pps.interfaces;

import com.cgg.pps.base.BaseView;
import com.cgg.pps.model.request.truckchit.TCSubmitRequest;
import com.cgg.pps.model.response.truckchit.info.ROInfoResponse;
import com.cgg.pps.model.response.truckchit.manual.ManualTCResponse;
import com.cgg.pps.model.response.truckchit.mastervehicle.VehicleResponse;
import com.cgg.pps.model.response.truckchit.online.OnlineTCResponse;
import com.cgg.pps.model.response.truckchit.ro.MillerMainResponse;
import com.cgg.pps.model.response.truckchit.submit.TCSubmitResponse;
import com.cgg.pps.model.response.truckchit.transaction.GetTransactionResponse;
import com.cgg.pps.model.response.truckchit.vehicle_type.VehicleTypesResponse;

public interface TCInterface extends BaseView {
    void getMillerMasterResponseData(MillerMainResponse millerMainResponse);
    void getOnlineTCResponseData(OnlineTCResponse onlineTCResponse);
    void getROInfoResponseData(ROInfoResponse roInfoResponse);
    void getManualTCResponseData(ManualTCResponse manualTCResponse, boolean finalSubmitFlag, TCSubmitRequest tcSubmitRequest);
    void getTransactionResponseData(GetTransactionResponse getTransactionResponse);
    void getVehicleDataResponse(VehicleResponse vehicleResponse, boolean flag);
    void getMasterVehicleTypeResponse(VehicleTypesResponse vehicleTypesResponse, boolean flag);
    void getTruckChitSubmitResponse(TCSubmitResponse tcSubmitResponse);
}

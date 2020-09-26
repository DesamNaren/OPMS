package com.cgg.pps.interfaces;

import com.cgg.pps.model.response.devicemgmt.bankbranch.BranchEntity;
import com.cgg.pps.model.response.devicemgmt.masterbank.BankEntity;
import com.cgg.pps.model.response.socialstatus.SocialEntity;
import com.cgg.pps.model.response.truckchit.mastervehicle.VehicleDetails;
import com.cgg.pps.model.response.truckchit.vehicle_type.TransporterVehicleType;

import java.util.List;

public interface VehicleInterface {
    void getAllVehicles(List<VehicleDetails> vehicleDetails);
    void getAllTransports(List<String> transports);
    void getVehiclesTypes(List<TransporterVehicleType> transporterVehicleTypes);
    void getTransportId(Long trnId);
    void getVehicleDetails(VehicleDetails vehicleDetails);
    void getVehCnt(int cnt);
    void getVehTypeCnt(int cnt);
    void getAllMasterTransports(List<VehicleDetails> vehicleDetails);
}

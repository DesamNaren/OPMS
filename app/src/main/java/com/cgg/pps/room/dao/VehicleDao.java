package com.cgg.pps.room.dao;

import com.cgg.pps.model.response.devicemgmt.bankbranch.BranchEntity;
import com.cgg.pps.model.response.devicemgmt.masterbank.BankEntity;
import com.cgg.pps.model.response.devicemgmt.paddyvalues.PaddyEntity;
import com.cgg.pps.model.response.socialstatus.SocialEntity;
import com.cgg.pps.model.response.truckchit.mastervehicle.VehicleDetails;
import com.cgg.pps.model.response.truckchit.vehicle_type.TransporterVehicleType;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface VehicleDao {

    @Insert
    void insertAllVehicles(List<VehicleDetails> vehicleDetails);

    @Query("DELETE FROM VehicleDetails")
    void deleteAllVehicles();

//    @Insert
//    void insertAllVehicleTypes(List<TransporterVehicleType> transporterVehicleTypes);

//    @Query("DELETE FROM TransporterVehicleType")
//    void deleteAllVehicleTypes();

    //    @Query("SELECT COUNT(*) FROM TransporterVehicleType")
//    int vehTypeCount();

//    @Query("SELECT * FROM TransporterVehicleType")
//    List<TransporterVehicleType> getVehicleTypes();

    @Query("SELECT DISTINCT transporterName FROM VehicleDetails")
    List<String> getAllTransports();

    @Query("SELECT * FROM VehicleDetails")
    List<VehicleDetails> getAllMasterTransports();

    @Query("SELECT transporterID  FROM vehicledetails where transporterName LIKE :trnName")
    Long getTransporterID(String trnName);

    @Query("SELECT *  FROM VehicleDetails where transporterID LIKE :trnID")
    List<VehicleDetails> getAllVehicles(Long trnID);


    @Query("SELECT *  FROM vehicledetails where transporterID LIKE :trnId AND vehicalNo LIKE :vehNum")
    VehicleDetails getVehicleDetails(Long trnId, String vehNum);

    @Query("SELECT COUNT(*) FROM VehicleDetails")
    int vehCount();
}

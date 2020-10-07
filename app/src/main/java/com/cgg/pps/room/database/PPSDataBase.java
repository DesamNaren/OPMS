package com.cgg.pps.room.database;

import android.content.Context;

import com.cgg.pps.model.response.devicemgmt.bankbranch.BranchEntity;
import com.cgg.pps.model.response.devicemgmt.masterbank.BankEntity;
import com.cgg.pps.model.response.devicemgmt.masterdistrict.DistrictEntity;
import com.cgg.pps.model.response.devicemgmt.mastermandal.MandalEntity;
import com.cgg.pps.model.response.devicemgmt.mastervillage.VillageEntity;
import com.cgg.pps.model.response.devicemgmt.paddyvalues.PaddyEntity;
import com.cgg.pps.model.response.socialstatus.SocialEntity;
import com.cgg.pps.model.response.truckchit.mastervehicle.VehicleDetails;
import com.cgg.pps.room.dao.BBDao;
import com.cgg.pps.room.dao.DMVDao;
import com.cgg.pps.room.dao.PaddyDao;
import com.cgg.pps.room.dao.VehicleDao;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {BankEntity.class, BranchEntity.class,
        DistrictEntity.class, MandalEntity.class, VillageEntity.class,
        PaddyEntity.class, SocialEntity.class, VehicleDetails.class},
        version = 2, exportSchema = false)

public abstract class PPSDataBase extends RoomDatabase {

    private static final String PPS_DB = "PPS_DATABASE.db";
    private static PPSDataBase instance;

    public static synchronized PPSDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, PPSDataBase.class, PPS_DB)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract BBDao getBbDao();

    public abstract VehicleDao getVehicleDao();

    public abstract DMVDao getDmvDao();

    public abstract PaddyDao getPaddyDao();

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };
}

package com.cgg.pps.room.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.cgg.pps.interfaces.DMVDeleteInterface;
import com.cgg.pps.interfaces.DMVInterface;
import com.cgg.pps.interfaces.VehicleInterface;
import com.cgg.pps.model.response.socialstatus.SocialEntity;
import com.cgg.pps.model.response.truckchit.mastervehicle.VehicleDetails;
import com.cgg.pps.model.response.truckchit.vehicle_type.TransporterVehicleType;
import com.cgg.pps.room.database.PPSDataBase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class VehicleRepository {

    private PPSDataBase db;
    private CompositeDisposable disposable = new CompositeDisposable();
    private Context context;
    private List<VehicleDetails> vehicleDetails;
    private List<VehicleDetails> masterTransports;
    private List<String> transNames;
    private List<TransporterVehicleType> transporterVehicleTypes;
    private Long trnId = (long) -1;
    private VehicleDetails vehicleDetailsSingle;

    public VehicleRepository(Context context) {
        this.context = context;
        db = PPSDataBase.getInstance(context);
    }

    public void insertAllVehicles(final VehicleInterface vehicleInterface, final List<VehicleDetails> vehicleDetails) {
        new InsertVehilceAsyncTask(vehicleInterface, vehicleDetails).execute();
    }

    public void deleteAllVehicles(final DMVDeleteInterface dmvInterface) {
        new DeleteVehilceAsyncTask(dmvInterface).execute();
    }


//    public void insertVehicleTypes(final VehicleInterface vehicleInterface, final List<TransporterVehicleType> transporterVehicleTypes) {
//        new InsertVehicleTypeAsyncTask(vehicleInterface, transporterVehicleTypes).execute();
//    }


    @SuppressLint("StaticFieldLeak")
    private class InsertVehilceAsyncTask extends AsyncTask<Void, Void, Integer> {
        private VehicleInterface vehicleInterface;
        private List<VehicleDetails> vehicleDetails;

        InsertVehilceAsyncTask(VehicleInterface vehicleInterface, List<VehicleDetails> vehicleDetails) {
            this.vehicleInterface = vehicleInterface;
            this.vehicleDetails = vehicleDetails;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            db.getVehicleDao().deleteAllVehicles();
            db.getVehicleDao().insertAllVehicles(vehicleDetails);
            return db.getVehicleDao().vehCount();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            vehicleInterface.getVehCnt(integer);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DeleteVehilceAsyncTask extends AsyncTask<Void, Void, Integer> {
        DMVDeleteInterface dmvInterface;
        DeleteVehilceAsyncTask(DMVDeleteInterface dmvInterface){
            this.dmvInterface = dmvInterface;
        }
        @Override
        protected Integer doInBackground(Void... voids) {
            db.getVehicleDao().deleteAllVehicles();
            return db.getVehicleDao().vehCount();
        }

        @Override
        protected void onPostExecute(Integer v) {
            super.onPostExecute(v);
            dmvInterface.vehCount(v);
        }
    }

//    @SuppressLint("StaticFieldLeak")
//    private class InsertVehicleTypeAsyncTask extends AsyncTask<Void, Void, Integer> {
//        private VehicleInterface vehicleInterface;
//        private List<TransporterVehicleType> vehicleTypes;
//
//        InsertVehicleTypeAsyncTask(VehicleInterface vehicleInterface, List<TransporterVehicleType> vehicleTypes) {
//            this.vehicleInterface = vehicleInterface;
//            this.vehicleTypes = vehicleTypes;
//        }
//
//
//        @Override
//        protected Integer doInBackground(Void... voids) {
//            db.getVehicleDao().deleteAllVehicleTypes();
//            db.getVehicleDao().insertAllVehicleTypes(vehicleTypes);
//            return db.getVehicleDao().vehTypeCount();
//        }
//
//        @Override
//        protected void onPostExecute(Integer integer) {
//            super.onPostExecute(integer);
//            vehicleInterface.getVehTypeCnt(integer);
//        }
//    }

    @SuppressLint("CheckResult")
    public void getTransporterID(final VehicleInterface vehicleInterface, String trnName) {

        Flowable.create(new FlowableOnSubscribe<Long>() {
            @Override
            public void subscribe(FlowableEmitter<Long> emitter) throws Exception {
                trnId = db.getVehicleDao().getTransporterID(trnName);
                if (!emitter.isCancelled())
                    if (trnId != null)
                        emitter.onNext(trnId);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long trnId) throws Exception {
                        vehicleInterface.getTransportId(trnId);
                    }
                });
    }


    @SuppressLint("CheckResult")
    public void getAllVehicles(final VehicleInterface vehicleInterface, Long trnID) {
        vehicleDetails = new ArrayList<>();
        Flowable.create(new FlowableOnSubscribe<List<VehicleDetails>>() {
            @Override
            public void subscribe(FlowableEmitter<List<VehicleDetails>> emitter) throws Exception {
                vehicleDetails = db.getVehicleDao().getAllVehicles(trnID);
                if (!emitter.isCancelled())
                    emitter.onNext(vehicleDetails);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VehicleDetails>>() {
                    @Override
                    public void accept(List<VehicleDetails> vehicleDetails) throws Exception {
                        vehicleInterface.getAllVehicles(vehicleDetails);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getAllTransports(final VehicleInterface vehicleInterface) {
        transNames = new ArrayList<>();
        Flowable.create(new FlowableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(FlowableEmitter<List<String>> emitter) throws Exception {
                transNames = db.getVehicleDao().getAllTransports();
                if (!emitter.isCancelled())
                    emitter.onNext(transNames);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> transNames) throws Exception {
                        vehicleInterface.getAllTransports(transNames);
                    }
                });
    }

//    @SuppressLint("CheckResult")
//    public void getVehicleMaster(final VehicleInterface vehicleInterface) {
//        transporterVehicleTypes = new ArrayList<>();
//        Flowable.create(new FlowableOnSubscribe<List<TransporterVehicleType>>() {
//            @Override
//            public void subscribe(FlowableEmitter<List<TransporterVehicleType>> emitter) throws Exception {
//                transporterVehicleTypes = db.getVehicleDao().getVehicleTypes();
//                if (!emitter.isCancelled())
//                    emitter.onNext(transporterVehicleTypes);
//            }
//        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<TransporterVehicleType>>() {
//                    @Override
//                    public void accept(List<TransporterVehicleType> transporterVehicleTypes) throws Exception {
//                        vehicleInterface.getVehiclesTypes(transporterVehicleTypes);
//                    }
//                });
//    }


    @SuppressLint("CheckResult")
    public void getVehicleData(final VehicleInterface vehicleInterface, Long trnID, String vehNo) {
        vehicleDetailsSingle = new VehicleDetails();
        Flowable.create(new FlowableOnSubscribe<VehicleDetails>() {
            @Override
            public void subscribe(FlowableEmitter<VehicleDetails> emitter) throws Exception {
                vehicleDetailsSingle = db.getVehicleDao().getVehicleDetails(trnID, vehNo);
                if (!emitter.isCancelled())
                    emitter.onNext(vehicleDetailsSingle);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<VehicleDetails>() {
                    @Override
                    public void accept(VehicleDetails vehicleDetails) throws Exception {
                        vehicleInterface.getVehicleDetails(vehicleDetailsSingle);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getAllMasterTransports(final VehicleInterface vehicleInterface) {

        Flowable.create(new FlowableOnSubscribe<List<VehicleDetails>>() {
            @Override
            public void subscribe(FlowableEmitter<List<VehicleDetails>> emitter) throws Exception {
                masterTransports = db.getVehicleDao().getAllMasterTransports();

                if (!emitter.isCancelled())
                    emitter.onNext(masterTransports);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VehicleDetails>>() {
                    @Override
                    public void accept(List<VehicleDetails> vehicleDetailsList) throws Exception {
                        vehicleInterface.getAllMasterTransports(vehicleDetailsList);
                    }
                });
    }
}

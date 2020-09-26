package com.cgg.pps.room.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.cgg.pps.interfaces.DMVDeleteInterface;
import com.cgg.pps.interfaces.DMVInterface;
import com.cgg.pps.model.response.devicemgmt.masterdistrict.DistrictEntity;
import com.cgg.pps.model.response.devicemgmt.mastermandal.MandalEntity;
import com.cgg.pps.model.response.devicemgmt.mastervillage.VillageEntity;
import com.cgg.pps.model.response.socialstatus.SocialEntity;
import com.cgg.pps.room.database.PPSDataBase;
import com.cgg.pps.util.Utils;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class DMVRepository {
    private PPSDataBase db;
    private List<DistrictEntity> districtEntityList;
    private List<MandalEntity> mandalEntityList;
    private List<VillageEntity> villageEntityList;
    private List<SocialEntity> socialEntities;
    private CompositeDisposable disposable = new CompositeDisposable();
    private Context context;

    public DMVRepository(Context context) {
        this.context = context;
        db = PPSDataBase.getInstance(context);
    }

    public void getDistrictsCount(final DMVInterface dmvInterface) {
        new GetDistrictsAsyncTask(dmvInterface).execute();
    }

    public void getMandalsCount(final DMVInterface dmvInterface) {
        new GetMandalsAsyncTask(dmvInterface).execute();
    }

    public void getViallagesCount(final DMVInterface dmvInterface) {
        new GetVillagesAsyncTask(dmvInterface).execute();
    }

    public void deleteDMVAsyncTask(final DMVDeleteInterface dmvInterface) {
        new DeleteDMVAsyncTask(dmvInterface).execute();
    }



    public void insertAllDistricts(final DMVInterface dmvInterface, final List<DistrictEntity> districtEntities) {
        new InsertDistrictAsyncTask(dmvInterface, districtEntities).execute();
    }

    public void insertAllMandals(final DMVInterface dmvInterface, final List<MandalEntity> mandalEntities) {
        new InsertMandalAsyncTask(dmvInterface, mandalEntities).execute();
    }

    public void insertAllVillages(final DMVInterface dmvInterface, final List<VillageEntity> villageEntities) {
        new InsertVillageAsyncTask(dmvInterface, villageEntities).execute();
    }

    public void getMandalsByDistName(final DMVInterface dmvInterface, final String distName, Spinner spinner, Spinner vspinner) {
        new GetMandalsByDistNameAsyncTask(dmvInterface, distName, spinner, vspinner).execute();
    }

    public void getVillagesByManName(final DMVInterface dmvInterface, final String distName, String manName, Spinner spinner) {
        new GetVillagesByManNameAsyncTask(dmvInterface, distName, manName, spinner).execute();
    }



    @SuppressLint("StaticFieldLeak")
    private class InsertDistrictAsyncTask extends AsyncTask<Void, Void, Integer> {
        List<DistrictEntity> districtEntities;
        DMVInterface dmvInterface;

        InsertDistrictAsyncTask(DMVInterface dmvInterface,
                                List<DistrictEntity> districtEntities) {
            this.districtEntities = districtEntities;
            this.dmvInterface = dmvInterface;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            db.getDmvDao().deleteAllDistricts();
            db.getDmvDao().insertAllDistricts(districtEntities);
            return db.getDmvDao().districtCount();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dmvInterface.distCount(integer);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DeleteDMVAsyncTask extends AsyncTask<Void, Void, Integer> {
        DMVDeleteInterface dmvInterface;

        DeleteDMVAsyncTask(DMVDeleteInterface dmvInterface) {
            this.dmvInterface = dmvInterface;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            db.getDmvDao().deleteAllDistricts();
            db.getDmvDao().deleteAllMandals();
            db.getDmvDao().deleteAllVillages();
            return db.getDmvDao().districtCount();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dmvInterface.distCount(integer);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertMandalAsyncTask extends AsyncTask<Void, Void, Integer> {
        List<MandalEntity> mandalEntities;
        DMVInterface dmvInterface;

        InsertMandalAsyncTask(DMVInterface dmvInterface,
                              List<MandalEntity> mandalEntities) {
            this.mandalEntities = mandalEntities;
            this.dmvInterface = dmvInterface;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            db.getDmvDao().deleteAllMandals();
            db.getDmvDao().insertAllMandals(mandalEntities);
            return db.getDmvDao().mandalCount();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dmvInterface.mandalCount(integer);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertVillageAsyncTask extends AsyncTask<Void, Void, Integer> {
        List<VillageEntity> villageEntities;
        DMVInterface dmvInterface;

        InsertVillageAsyncTask(DMVInterface dmvInterface,
                               List<VillageEntity> villageEntities) {
            this.villageEntities = villageEntities;
            this.dmvInterface = dmvInterface;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            db.getDmvDao().deleteAllVillages();
            db.getDmvDao().insertAllVillages(villageEntities);
            return db.getDmvDao().villageCount();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dmvInterface.villageCount(integer);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetDistrictsAsyncTask extends AsyncTask<Void, Void, Integer> {
        DMVInterface dmvInterface;

        GetDistrictsAsyncTask(DMVInterface dmvInterface) {
            this.dmvInterface = dmvInterface;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            return db.getDmvDao().districtCount();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dmvInterface.distCount(integer);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetMandalsAsyncTask extends AsyncTask<Void, Void, Integer> {
        DMVInterface dmvInterface;

        GetMandalsAsyncTask(DMVInterface dmvInterface) {
            this.dmvInterface = dmvInterface;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            return db.getDmvDao().mandalCount();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dmvInterface.mandalCount(integer);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetVillagesAsyncTask extends AsyncTask<Void, Void, Integer> {
        DMVInterface dmvInterface;

        GetVillagesAsyncTask(DMVInterface dmvInterface) {
            this.dmvInterface = dmvInterface;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            return db.getDmvDao().villageCount();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dmvInterface.villageCount(integer);
        }
    }

    String dID;

    @SuppressLint("CheckResult")
    public String getDistrictID(String distName) {

        PublishSubject.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                dID = db.getDmvDao().getDistrictID(distName);
            }
        }).subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String distID) throws Exception {
                        dID = distID;
                    }
                });
        return dID;
    }

    String mID;

    @SuppressLint("CheckResult")
    public String getMandalID(String distId, String manName) {

        PublishSubject.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                mID = db.getDmvDao().getMandalID(distId, manName);
            }
        }).subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String manID) throws Exception {
                        mID = manID;
                    }
                });
        return mID;
    }

    String vID;

    @SuppressLint("CheckResult")
    public String getVillageID(String distId, String manId, String vilName) {

        PublishSubject.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                vID = db.getDmvDao().getVillageId(distId, manId, vilName);
            }
        }).subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String vilId) throws Exception {
                        vID = vilId;
                    }
                });
        return vID;
    }


    @SuppressLint("CheckResult")
    public void getBasicDistrictName(final DMVInterface dmvInterface, String distId, Spinner spinner, List<String> disStringList) {

        PublishSubject.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String distName = db.getDmvDao().getDistrictName(distId);
                if (!emitter.isDisposed())
                    if (TextUtils.isEmpty(distName)) {
                        distName = "";
                    }
                emitter.onNext(distName);

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String dName) throws Exception {
                        Utils.loadSpinnerSetSelectedItem(context,
                                disStringList,
                                spinner, dName);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getLandDistrictName(final DMVInterface dmvInterface, String distId, Spinner spinner, List<String> disStringList) {

        PublishSubject.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String distName = db.getDmvDao().getDistrictName(distId);
                if (!emitter.isDisposed())
                    if (TextUtils.isEmpty(distName)) {
                        distName = "";
                    }
                emitter.onNext(distName);

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String dName) throws Exception {
                        Utils.loadSpinnerSetSelectedItem(context,
                                disStringList,
                                spinner, dName);
                    }
                });
    }


    @SuppressLint("CheckResult")
    public void getSocialStatusName(final DMVInterface dmvInterface, String socilaId, Spinner spinner, List<String> socialStrings) {

        PublishSubject.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                String sName = db.getDmvDao().getSocialStatusName(socilaId);
                if (!emitter.isDisposed())
                    if (TextUtils.isEmpty(sName)) {
                        sName = socialStrings.get(0);
                    }
                emitter.onNext(sName);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String sName) throws Exception {
                        Utils.loadSpinnerSetSelectedItem(context,
                                socialStrings,
                                spinner, sName);
                    }
                });
    }


    @SuppressLint("CheckResult")
    public void getMandalName(final DMVInterface dmvInterface, String distId, String manId, Spinner spinner, List<String> manStringList) {

        PublishSubject.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String manName = db.getDmvDao().getMandalName(distId, manId);
                emitter.onNext(manName);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String manName) throws Exception {
                        Utils.loadSpinnerSetSelectedItem(context,
                                manStringList,
                                spinner, manName);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getBasicMandalName(final DMVInterface dmvInterface, String distId, String manId, Spinner spinner, List<String> manStringList) {

        PublishSubject.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String manName = db.getDmvDao().getMandalName(distId, manId);
                if (!emitter.isDisposed())
                    if (TextUtils.isEmpty(manName)) {
                        manName = "";
                    }
                emitter.onNext(manName);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String manName) throws Exception {

                        Utils.loadSpinnerSetSelectedItem(context,
                                manStringList,
                                spinner, manName);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getLandMandalName(final DMVInterface dmvInterface, String distId, String manId, Spinner spinner, List<String> manStringList) {

        PublishSubject.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
               String manName = db.getDmvDao().getMandalName(distId, manId);
                if (!emitter.isDisposed())
                    if (TextUtils.isEmpty(manName)) {
                        manName = "";
                    }
                    emitter.onNext(manName);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String manName) throws Exception {

                        Utils.loadSpinnerSetSelectedItem(context,
                                manStringList,
                                spinner, manName);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getVillageName(final DMVInterface dmvInterface, String distId, String manId, String vilId, Spinner spinner, List<String> vilStringList) {

        PublishSubject.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String vilName = db.getDmvDao().getVillageName(distId, manId, vilId);

                if (!emitter.isDisposed())
                    if (TextUtils.isEmpty(vilName)) {
                        vilName = "";
                    }
                    emitter.onNext(vilName);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String vilName) throws Exception {
                        Utils.loadSpinnerSetSelectedItem(context,
                                vilStringList,
                                spinner, vilName);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getBasicVillageName(final DMVInterface dmvInterface, String distId, String manId, String vilId, Spinner spinner,
                                   List<String> vilStringList) {

        PublishSubject.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String vilName = db.getDmvDao().getVillageName(distId, manId, vilId);
                if (!emitter.isDisposed())
                    if (TextUtils.isEmpty(vilName)) {
                        vilName = "";
                    }
                emitter.onNext(vilName);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String vilName) throws Exception {
                        Utils.loadSpinnerSetSelectedItem(context,
                                vilStringList,
                                spinner, vilName);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getLandVillageName(final DMVInterface dmvInterface, String distId, String manId, String vilId, Spinner spinner,
                                   List<String> vilStringList) {

        PublishSubject.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String vilName = db.getDmvDao().getVillageName(distId, manId, vilId);
                if (!emitter.isDisposed())
                    if (TextUtils.isEmpty(vilName)) {
                        vilName = "";
                    }
                emitter.onNext(vilName);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String vilName) throws Exception {
                        Utils.loadSpinnerSetSelectedItem(context,
                                vilStringList,
                                spinner, vilName);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getAllDistricts(final DMVInterface dmvInterface) {
        Flowable.create(new FlowableOnSubscribe<List<DistrictEntity>>() {
            @Override
            public void subscribe(FlowableEmitter<List<DistrictEntity>> emitter) throws Exception {
                districtEntityList = db.getDmvDao().getAllDistricts();
                if (!emitter.isCancelled())
                    emitter.onNext(districtEntityList);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DistrictEntity>>() {
                    @Override
                    public void accept(List<DistrictEntity> districtEntityList) throws Exception {
                        dmvInterface.getAllDistrictEntities(districtEntityList);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getAllMandals(final DMVInterface dmvInterface) {
        Flowable.create(new FlowableOnSubscribe<List<MandalEntity>>() {
            @Override
            public void subscribe(FlowableEmitter<List<MandalEntity>> emitter) throws Exception {
                mandalEntityList = db.getDmvDao().getAllMandals();
                if (!emitter.isCancelled())
                    emitter.onNext(mandalEntityList);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MandalEntity>>() {
                    @Override
                    public void accept(List<MandalEntity> mandalEntityList) throws Exception {
                        dmvInterface.getAllMandalEntities(mandalEntityList);
                    }
                });
    }


    @SuppressLint("CheckResult")
    public void getAllVillages(final DMVInterface dmvInterface) {

        Flowable.create(new FlowableOnSubscribe<List<VillageEntity>>() {
            @Override
            public void subscribe(FlowableEmitter<List<VillageEntity>> emitter) throws Exception {
                villageEntityList = db.getDmvDao().getAllVillages();

                if (!emitter.isCancelled())
                    emitter.onNext(villageEntityList);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VillageEntity>>() {
                    @Override
                    public void accept(List<VillageEntity> villageEntityList) throws Exception {
                        dmvInterface.getAllVillageEntities(villageEntityList);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getAllowDistricts(final DMVInterface dmvInterface) {
        Flowable.create(new FlowableOnSubscribe<List<DistrictEntity>>() {
            @Override
            public void subscribe(FlowableEmitter<List<DistrictEntity>> emitter) throws Exception {
                districtEntityList = db.getDmvDao().getAllowDistricts();
                if (!emitter.isCancelled())
                    emitter.onNext(districtEntityList);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DistrictEntity>>() {
                    @Override
                    public void accept(List<DistrictEntity> districtEntityList) throws Exception {
                        dmvInterface.getAllowDistrictEntities(districtEntityList);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getAllowMandals(final DMVInterface dmvInterface) {
        Flowable.create(new FlowableOnSubscribe<List<MandalEntity>>() {
            @Override
            public void subscribe(FlowableEmitter<List<MandalEntity>> emitter) throws Exception {
                mandalEntityList = db.getDmvDao().getAllowMandals();
                if (!emitter.isCancelled())
                    emitter.onNext(mandalEntityList);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MandalEntity>>() {
                    @Override
                    public void accept(List<MandalEntity> mandalEntityList) throws Exception {
                        dmvInterface.getAllowMandalEntities(mandalEntityList);
                    }
                });
    }


    @SuppressLint("CheckResult")
    public void getAllowVillages(final DMVInterface dmvInterface) {

        Flowable.create(new FlowableOnSubscribe<List<VillageEntity>>() {
            @Override
            public void subscribe(FlowableEmitter<List<VillageEntity>> emitter) throws Exception {
                villageEntityList = db.getDmvDao().getAllowVillages();

                if (!emitter.isCancelled())
                    emitter.onNext(villageEntityList);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<VillageEntity>>() {
                    @Override
                    public void accept(List<VillageEntity> villageEntityList) throws Exception {
                        dmvInterface.getAllowVillageEntities(villageEntityList);
                    }
                });
    }

    @SuppressLint("StaticFieldLeak")
    private class GetMandalsByDistNameAsyncTask extends AsyncTask<Void, Void, List<String>> {
        String distName;
        String manName;
        DMVInterface dmvInterface;
        Spinner spinner;
        Spinner vspinner;

        GetMandalsByDistNameAsyncTask(DMVInterface dmvInterface,
                                      String distName, Spinner spinner, Spinner vspinner) {
            this.distName = distName;
            this.dmvInterface = dmvInterface;
            this.spinner = spinner;
            this.vspinner = vspinner;
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            return db.getDmvDao().getMandalsByDistName(distName);
        }

        @Override
        protected void onPostExecute(List<String> list) {
            super.onPostExecute(list);
            dmvInterface.getAllMandalNamesByDist(list, spinner, vspinner, distName);
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class GetLandMandalsByDistNameAsyncTask extends AsyncTask<Void, Void, List<String>> {
        String distName;
        String manName;
        DMVInterface dmvInterface;
        Spinner spinner;
        Spinner vspinner;
        String manID;

        GetLandMandalsByDistNameAsyncTask(DMVInterface dmvInterface,
                                          String distName, Spinner spinner, Spinner vspinner) {
            this.distName = distName;
            this.dmvInterface = dmvInterface;
            this.spinner = spinner;
            this.vspinner = vspinner;
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            return db.getDmvDao().getMandalsByDistName(distName);
        }

        @Override
        protected void onPostExecute(List<String> list) {
            super.onPostExecute(list);
            dmvInterface.getAllLandMandalNamesByDist(list, spinner, vspinner, distName);
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class GetVillagesByManNameAsyncTask extends AsyncTask<Void, Void, List<String>> {
        String distName;
        String manName;
        String vilName;
        DMVInterface dmvInterface;
        Spinner spinner;

        GetVillagesByManNameAsyncTask(DMVInterface dmvInterface,
                                      String distName, String manName, Spinner spinner) {
            this.distName = distName;
            this.manName = manName;
            this.dmvInterface = dmvInterface;
            this.spinner = spinner;
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            return db.getDmvDao().getVillagesByManName(distName, manName);
        }

        @Override
        protected void onPostExecute(List<String> list) {
            super.onPostExecute(list);
            dmvInterface.getAllVillagesByMan(list, spinner, distName, manName);
        }
    }

    @SuppressLint("CheckResult")
    public void getAllSocialStatus(final DMVInterface dmvInterface) {

        Flowable.create(new FlowableOnSubscribe<List<SocialEntity>>() {
            @Override
            public void subscribe(FlowableEmitter<List<SocialEntity>> emitter) throws Exception {
                socialEntities = db.getDmvDao().getAllSocialEntities();

                if (!emitter.isCancelled())
                    emitter.onNext(socialEntities);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<SocialEntity>>() {
                    @Override
                    public void accept(List<SocialEntity> socialEntityList) throws Exception {
                        dmvInterface.getAllSocialEntities(socialEntityList);
                    }
                });
    }
}

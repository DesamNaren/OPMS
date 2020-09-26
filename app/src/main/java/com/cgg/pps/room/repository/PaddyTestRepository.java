package com.cgg.pps.room.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.cgg.pps.interfaces.BBInterface;
import com.cgg.pps.interfaces.PaddyTestInterface;
import com.cgg.pps.model.response.devicemgmt.masterbank.BankEntity;
import com.cgg.pps.model.response.devicemgmt.paddyvalues.PaddyEntity;
import com.cgg.pps.room.database.PPSDataBase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class PaddyTestRepository {
    private PPSDataBase db;
    private List<PaddyEntity> paddyEntityList;
    private CompositeDisposable disposable = new CompositeDisposable();

    public PaddyTestRepository(Context context) {
        db = PPSDataBase.getInstance(context);
    }


    public void insertPaddyTestValues(final PaddyTestInterface paddyTestInterface, final List<PaddyEntity> paddyEntities) {
        new InsertPaddyAsyncTask(paddyTestInterface, paddyEntities).execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertPaddyAsyncTask extends AsyncTask<Void, Void, Integer> {
        List<PaddyEntity> paddyEntities;
        PaddyTestInterface paddyTestInterface;

        InsertPaddyAsyncTask(PaddyTestInterface paddyTestInterface, List<PaddyEntity> paddyEntities) {
            this.paddyEntities = paddyEntities;
            this.paddyTestInterface = paddyTestInterface;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            db.getPaddyDao().deleteAllPaddy();
            db.getPaddyDao().insertAllPaddy(paddyEntities);
            return db.getPaddyDao().paddyTestCount();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            paddyTestInterface.paddyTestCount(integer);
        }
    }

    @SuppressLint("CheckResult")
    public void getAllPaddyValues(final PaddyTestInterface paddyTestInterface) {
        paddyEntityList = new ArrayList<>();
        Flowable.create(new FlowableOnSubscribe<List<PaddyEntity>>() {
            @Override
            public void subscribe(FlowableEmitter<List<PaddyEntity>> emitter) throws Exception {
                paddyEntityList = db.getPaddyDao().getAllPaddyValues();
                if (!emitter.isCancelled())
                    emitter.onNext(paddyEntityList);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<PaddyEntity>>() {
                    @Override
                    public void accept(List<PaddyEntity> paddyEntities) throws Exception {
                        paddyTestInterface.getAllPaddyValues(paddyEntities);
                    }
                });
    }


    @SuppressLint("CheckResult")
    public void getDistrictName(final PaddyTestInterface paddyTestInterface, String distID) {

        PublishSubject.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String distName = db.getPaddyDao().getDistrictName(distID);
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
                        paddyTestInterface.getDistName(dName);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getMandalName(final PaddyTestInterface paddyTestInterface, String distID, String manID) {

        PublishSubject.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String manName = db.getPaddyDao().getMandalName(distID, manID);
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
                    public void accept(String mName) throws Exception {
                        paddyTestInterface.getManName(mName);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getVillageName(final PaddyTestInterface paddyTestInterface, String distID, String manID, String vilID) {

        PublishSubject.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String vilName = db.getPaddyDao().getVillageName(distID, manID, vilID);
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
                    public void accept(String vName) throws Exception {
                        paddyTestInterface.getVilName(vName);
                    }
                });
    }
}

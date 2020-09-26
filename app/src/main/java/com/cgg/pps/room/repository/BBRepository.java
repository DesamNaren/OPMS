package com.cgg.pps.room.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.cgg.pps.interfaces.BBInterface;
import com.cgg.pps.model.response.devicemgmt.bankbranch.BranchEntity;
import com.cgg.pps.model.response.devicemgmt.masterbank.BankEntity;
import com.cgg.pps.model.response.socialstatus.SocialEntity;
import com.cgg.pps.room.database.PPSDataBase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class BBRepository {
    private PPSDataBase db;
    private CompositeDisposable disposable = new CompositeDisposable();
    private Context context;
    private List<BankEntity> bankEntityList;
    private List<BranchEntity> branchEntityList;

    public BBRepository(Context context) {
        this.context = context;
        db = PPSDataBase.getInstance(context);
    }

    public void insertAllBanks(final BBInterface bbInterface, final List<BankEntity> bankEntities) {
        new InsertBankAsyncTask(bbInterface, bankEntities).execute();
    }

    public void insertAllBranches(final BBInterface bbInterface, final List<BranchEntity> bankEntities) {
        new InsertBranchAsyncTask(bbInterface, bankEntities).execute();
    }

    public void getBanksCount(final BBInterface bbInterface) {
        new GetBanksAsyncTask(bbInterface).execute();
    }

    public void getBranchesCount(final BBInterface bbInterface) {
        new GetBranchesAsyncTask(bbInterface).execute();
    }


    public void insertSocialStatus(final BBInterface bbInterface, final List<SocialEntity> socialEntities) {
        new InsertSocialAsyncTask(bbInterface, socialEntities).execute();
    }

    public void deleteAllBanks(final BBInterface bbInterface) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.getBbDao().deleteAllBanks();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {
//                         bbInterface.onOtItemDeleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        bbInterface.onDataNotAvailable();
                    }
                });
    }


    public void deleteAllBranches(final BBInterface bbInterface) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.getBbDao().deleteAllBranches();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {
//                bbInterface.onOtItemDeleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        bbInterface.onDataNotAvailable();
                    }
                });
    }


    @SuppressLint("CheckResult")
    public void getBankName(final BBInterface bbInterface, String bankID) {

        PublishSubject.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String bankNmae = db.getBbDao().getBankName(bankID);
                if (!emitter.isDisposed())
                    if (TextUtils.isEmpty(bankNmae)) {
                        bankNmae = "";
                    }
                emitter.onNext(bankNmae);

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String bName) throws Exception {
                        bbInterface.getBankName(bName);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getBranchName(final BBInterface bbInterface, String bankID, String branchID) {

        PublishSubject.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String branchNmae = db.getBbDao().getBranchName(bankID, branchID);
                if (!emitter.isDisposed())
                    if (TextUtils.isEmpty(branchNmae)) {
                        branchNmae = "";
                    }
                emitter.onNext(branchNmae);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String branchNmae) throws Exception {
                        bbInterface.getBranchName(branchNmae);
                    }
                });
    }


    @SuppressLint("StaticFieldLeak")
    private class InsertSocialAsyncTask extends AsyncTask<Void, Void, Integer> {
        List<SocialEntity> socialEntities;
        BBInterface bbInterface;

        InsertSocialAsyncTask(BBInterface bbInterface, List<SocialEntity> socialEntities) {
            this.socialEntities = socialEntities;
            this.bbInterface = bbInterface;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            db.getBbDao().deleteSocialValues();
            db.getBbDao().insertSocialValues(socialEntities);
            return db.getBbDao().socialCount();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            bbInterface.socialCount(integer);
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class InsertBankAsyncTask extends AsyncTask<Void, Void, Integer> {
        List<BankEntity> bankEntities;
        BBInterface bbInterface;

        InsertBankAsyncTask(BBInterface bbInterface, List<BankEntity> bankEntities) {
            this.bankEntities = bankEntities;
            this.bbInterface = bbInterface;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            db.getBbDao().deleteAllBanks();
            db.getBbDao().insertAllBanks(bankEntities);
            return db.getBbDao().bankCount();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            bbInterface.bankCount(integer);
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class InsertBranchAsyncTask extends AsyncTask<Void, Void, Integer> {
        List<BranchEntity> branchEntities;
        BBInterface bbInterface;

        InsertBranchAsyncTask(BBInterface bbInterface, List<BranchEntity> branchEntities) {
            this.branchEntities = branchEntities;
            this.bbInterface = bbInterface;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            db.getBbDao().deleteAllBranches();
            db.getBbDao().insertAllBranches(branchEntities);
            return db.getBbDao().branchCount();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            bbInterface.branchCount(integer);
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class GetBanksAsyncTask extends AsyncTask<Void, Void, Integer> {
        BBInterface bbInterface;

        GetBanksAsyncTask(BBInterface bbInterface) {
            this.bbInterface = bbInterface;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            return db.getBbDao().bankCount();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            bbInterface.bankCount(integer);
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class GetBranchesAsyncTask extends AsyncTask<Void, Void, Integer> {
        BBInterface bbInterface;

        GetBranchesAsyncTask(BBInterface bbInterface) {
            this.bbInterface = bbInterface;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            return db.getBbDao().branchCount();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            bbInterface.branchCount(integer);
        }
    }

    @SuppressLint("CheckResult")
    public void getAllBanks(final BBInterface bbInterface) {
        bankEntityList = new ArrayList<>();
        Flowable.create(new FlowableOnSubscribe<List<BankEntity>>() {
            @Override
            public void subscribe(FlowableEmitter<List<BankEntity>> emitter) throws Exception {
                bankEntityList = db.getBbDao().getAllBanks();
                if (!emitter.isCancelled())
                    emitter.onNext(bankEntityList);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<BankEntity>>() {
                    @Override
                    public void accept(List<BankEntity> bankEntityList) throws Exception {
                        bbInterface.getAllBanks(bankEntityList);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getAllBranches(final BBInterface bbInterface) {
        Flowable.create(new FlowableOnSubscribe<List<BranchEntity>>() {
            @Override
            public void subscribe(FlowableEmitter<List<BranchEntity>> emitter) throws Exception {
                branchEntityList = db.getBbDao().getAllBranches();
                if (!emitter.isCancelled())
                    emitter.onNext(branchEntityList);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<BranchEntity>>() {
                    @Override
                    public void accept(List<BranchEntity> branchEntityList) throws Exception {
                        bbInterface.getAllBranches(branchEntityList);
                    }
                });
    }

    Integer bID;

    @SuppressLint("CheckResult")
    public Integer getBankID(String bankName) {

        Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(SingleEmitter<Integer> emitter) throws Exception {
                bID = db.getBbDao().getBankId(bankName);
            }
        })
                .delay(10, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        bID = integer;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

        return bID;
    }


    Integer brID;

    @SuppressLint("CheckResult")
    public Integer getBranchID(Integer bankId, String brName) {

        PublishSubject.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                brID = db.getBbDao().getBranchId(bankId, brName);
            }
        }).subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer branchId) throws Exception {
                        brID = branchId;
                    }
                });
        return brID;
    }

}

package com.cgg.pps.presenter;

import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.base.BasePresenter;
import com.cgg.pps.interfaces.FarRegInterfaceNew;
import com.cgg.pps.model.request.farmer.farmerdetails.FarmerDetailsRequest;
import com.cgg.pps.model.response.farmer.farmerdetailsnew.FarRegNew;
import com.cgg.pps.network.OPMSService;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FarRegPresenterNew implements BasePresenter<FarRegInterfaceNew> {

    private FarRegInterfaceNew farRegInterface;
    private FarRegNew farmerDetailsResponse;
    private CompositeDisposable disposable = new CompositeDisposable();


    @Override
    public void attachView(FarRegInterfaceNew view) {
        this.farRegInterface = view;
    }

    @Override
    public void detachView() {
        if (disposable != null) disposable.dispose();
    }

    @Override
    public String handleError(Throwable e) {
        return null;
    }

    @Override
    public void handleException(Exception e) {

    }

    public void GetFarmerData(FarmerDetailsRequest farmerDetailsRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(farRegInterface.getContext());
            OPMSService gitHubService = application.getDBService();


            gitHubService.getFarRegResponseNew(farmerDetailsRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<FarRegNew>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(FarRegNew farmerDetailsResponse) {
                            FarRegPresenterNew.this.farmerDetailsResponse = farmerDetailsResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            farRegInterface.getFarmerRegResponse(null);
                        }

                        @Override
                        public void onComplete() {
                            farRegInterface.getFarmerRegResponse(farmerDetailsResponse);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            farRegInterface.getFarmerRegResponse(null);
        }
    }
}

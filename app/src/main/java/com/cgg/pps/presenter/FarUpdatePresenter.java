package com.cgg.pps.presenter;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.base.BasePresenter;
import com.cgg.pps.interfaces.FarUpdateInterface;
import com.cgg.pps.model.request.farmer.farmerUpdate.FarmerUpdateRequest;
import com.cgg.pps.model.response.farmer.farmerUpdate.FarmerUpdateResponse;
import com.cgg.pps.network.OPMSService;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.Utils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class FarUpdatePresenter implements BasePresenter<FarUpdateInterface> {

    private FarUpdateInterface farUpdateInterface;
    private FarmerUpdateResponse farmerUpdateResponse;
    private CompositeDisposable disposable = new CompositeDisposable();


    @Override
    public void attachView(FarUpdateInterface view) {
        this.farUpdateInterface = view;
    }

    @Override
    public void detachView() {
        if (disposable != null) disposable.dispose();
    }

    public void UpdateFarmerData(FarmerUpdateRequest farmerUpdateRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(farUpdateInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getFarUpdateResponse(farmerUpdateRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<FarmerUpdateResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(FarmerUpdateResponse farmerUpdateResponse) {
                            FarUpdatePresenter.this.farmerUpdateResponse = farmerUpdateResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            farUpdateInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            farUpdateInterface.getFarmerUpdateResponse(farmerUpdateResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }


    @Override
    public void handleException(Exception e) {
        farUpdateInterface.onError(AppConstants.EXCEPTION_CODE, ": " + e.getMessage());
    }

    @Override
    public String handleError(Throwable e) {
        String msg = "";
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            msg = Utils.getErrorMessage(responseBody);
        } else if (e instanceof SocketTimeoutException) {
            msg = farUpdateInterface.getContext().getString(R.string.con_time_out);
        } else if (e instanceof IOException) {
            msg = farUpdateInterface.getContext().getString(R.string.something) +
                    farUpdateInterface.getContext().getString(R.string.io_exe);
        } else {
            msg = farUpdateInterface.getContext().getString(R.string.server_not)
                    + ": " + e.getMessage();
        }
        return msg;
    }
}

package com.cgg.pps.presenter;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.base.BasePresenter;
import com.cgg.pps.interfaces.ChangePwdInterface;
import com.cgg.pps.interfaces.LoginInterface;
import com.cgg.pps.model.request.ChangePwdRequest;
import com.cgg.pps.model.request.truckchit.vehicledetails.VehicleDetailsRequest;
import com.cgg.pps.model.request.validateuser.login.ValidateUserRequest;
import com.cgg.pps.model.response.ChangePwdResponse;
import com.cgg.pps.model.response.truckchit.mastervehicle.VehicleResponse;
import com.cgg.pps.model.response.validateuser.login.ValidateUserResponse;
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

public class ChangePwdPresenter implements BasePresenter<ChangePwdInterface> {

    private ChangePwdInterface changePwdInterface;
    private ChangePwdResponse changePwdResponse;
    private CompositeDisposable disposable = new CompositeDisposable();


    @Override
    public void attachView(ChangePwdInterface view) {
        this.changePwdInterface = view;
    }

    @Override
    public void detachView() {
        if (disposable != null) disposable.dispose();
    }

    public void updatePassword(ChangePwdRequest changePwdRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(changePwdInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.UpdatePwdResponse(changePwdRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<ChangePwdResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(ChangePwdResponse changePwdResponse) {
                            ChangePwdPresenter.this.changePwdResponse = changePwdResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            changePwdInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            changePwdInterface.updatePwdResponse(changePwdResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }


    @Override
    public void handleException(Exception e) {
        changePwdInterface.onError(AppConstants.EXCEPTION_CODE, ": " + e.getMessage());
    }

    @Override
    public String handleError(Throwable e) {
        String msg = "";
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            msg = Utils.getErrorMessage(responseBody);
        } else if (e instanceof SocketTimeoutException) {
            msg = changePwdInterface.getContext().getString(R.string.con_time_out);
        } else if (e instanceof IOException) {
            msg = changePwdInterface.getContext().getString(R.string.something) +
                    changePwdInterface.getContext().getString(R.string.io_exe);
        } else {
            msg = changePwdInterface.getContext().getString(R.string.server_not)
                    + ": " + e.getMessage();
        }
        return msg;
    }


}

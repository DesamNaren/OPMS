package com.cgg.pps.presenter;

import android.content.res.Resources;
import android.widget.Toast;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.base.BasePresenter;
import com.cgg.pps.interfaces.LoginInterface;
import com.cgg.pps.model.request.devicemapping.DeviceMappingRequest;
import com.cgg.pps.model.request.truckchit.vehicledetails.VehicleDetailsRequest;
import com.cgg.pps.model.request.validateuser.login.ValidateUserRequest;
import com.cgg.pps.model.response.devicemapping.DeviceMappingResponse;
import com.cgg.pps.model.response.truckchit.mastervehicle.VehicleResponse;
import com.cgg.pps.model.response.validateuser.login.ValidateUserResponse;
import com.cgg.pps.network.OPMSService;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class LoginPresenter implements BasePresenter<LoginInterface> {

    private LoginInterface loginInterface;
    private ValidateUserResponse validateUserResponse;
    private DeviceMappingResponse deviceMappingResponse;

    private CompositeDisposable disposable = new CompositeDisposable();


    @Override
    public void attachView(LoginInterface view) {
        this.loginInterface = view;
    }

    @Override
    public void detachView() {
        if (disposable != null) disposable.dispose();
    }

    public void validateUser(ValidateUserRequest validateUserRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(loginInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getValidateUserResponse(validateUserRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<ValidateUserResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(ValidateUserResponse validateUserResponse) {
                            LoginPresenter.this.validateUserResponse = validateUserResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            loginInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            loginInterface.getLoginResponse(validateUserResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void deviceMapping(DeviceMappingRequest deviceMappingRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(loginInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getDeviceMappingResponse(deviceMappingRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<DeviceMappingResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(DeviceMappingResponse deviceMappingResponse) {
                            LoginPresenter.this.deviceMappingResponse = deviceMappingResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            loginInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            loginInterface.getDeviceMappingResponse(deviceMappingResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }


    @Override
    public void handleException(Exception e) {
        loginInterface.onError(AppConstants.EXCEPTION_CODE, ": " + e.getMessage());
    }

    @Override
    public String handleError(Throwable e) {
        String msg = "";
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            msg = Utils.getErrorMessage(responseBody);
        } else if (e instanceof SocketTimeoutException) {
            msg = loginInterface.getContext().getString(R.string.con_time_out);
        } else if (e instanceof IOException) {
            msg = loginInterface.getContext().getString(R.string.something) +
                    loginInterface.getContext().getString(R.string.io_exe);
        } else {
            msg = loginInterface.getContext().getString(R.string.server_not)
                    + ": " + e.getMessage();
        }
        return msg;
    }

}

package com.cgg.pps.presenter;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.base.BasePresenter;
import com.cgg.pps.interfaces.PaddyProcurementInterface;
import com.cgg.pps.model.request.farmer.gettokens.GetTokensRequest;
import com.cgg.pps.model.request.procurement.IssuedGunnyDataRequest;
import com.cgg.pps.model.request.procurement.PaddyOTPRequest;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensDDLResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
import com.cgg.pps.model.response.procurement.IssuedGunnyDataResponse;
import com.cgg.pps.model.request.procurement.PaddyProcurementSubmit;
import com.cgg.pps.model.response.procurement.OTPResponse;
import com.cgg.pps.model.response.procurement.ProcurementSubmitResponse;
import com.cgg.pps.network.OPMSService;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.Utils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.OnErrorNotImplementedException;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class PaddyProcurementPresenter implements BasePresenter<PaddyProcurementInterface> {
    PaddyProcurementInterface paddyProcurementInterface;
    private GetTokensResponse getTokensProcurementResponse;
    private GetTokensDDLResponse getTokensDDLResponse;
    private IssuedGunnyDataResponse issuedGunnyDataResponse;
    ProcurementSubmitResponse procurementSubmitResponse;
    OTPResponse otpResponse;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void attachView(PaddyProcurementInterface view) {
        this.paddyProcurementInterface = view;
    }

    @Override
    public void detachView() {
        if (disposable != null) disposable.dispose();
    }

    public void GetProcurementGeneratedTokens(GetTokensRequest getTokensProcurementRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(paddyProcurementInterface.getContext());
            OPMSService gitHubService = application.getDBService();
            gitHubService.getTokensDdlResponse(getTokensProcurementRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<GetTokensDDLResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(GetTokensDDLResponse getTokensDDLResponse) {
                            PaddyProcurementPresenter.this.getTokensDDLResponse = getTokensDDLResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            paddyProcurementInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            paddyProcurementInterface.getTokensProcurementResponseData(getTokensDDLResponse);
                        }
                    });
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void GetSelectedProcurementTokenData(GetTokensRequest getTokensProcurementRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(paddyProcurementInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getTokensProcurementResponse(getTokensProcurementRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<GetTokensResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(GetTokensResponse getTokensProcurementResponseData) {
                            PaddyProcurementPresenter.this.getTokensProcurementResponse = getTokensProcurementResponseData;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            paddyProcurementInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            paddyProcurementInterface.GetSelectedProcurementTokenData(getTokensProcurementResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void GetIssuedGunnyDataResponseData(IssuedGunnyDataRequest issuedGunnyDataRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(paddyProcurementInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.GetIssuedGunnyDataResponse(issuedGunnyDataRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<IssuedGunnyDataResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(IssuedGunnyDataResponse issuedGunnyDataResponse) {
                            PaddyProcurementPresenter.this.issuedGunnyDataResponse = issuedGunnyDataResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            paddyProcurementInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            paddyProcurementInterface.GetIssuedGunnyDataResponseData(issuedGunnyDataResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void PaddyProcurementOTP(PaddyProcurementSubmit paddyProcurementSubmit, PaddyOTPRequest paddyOTPRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(paddyProcurementInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.GetPaddyOTPtResponse(paddyOTPRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<OTPResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(OTPResponse otpResponse) {
                            PaddyProcurementPresenter.this.otpResponse = otpResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            paddyProcurementInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            paddyProcurementInterface.PaddyProcurementOTP(paddyProcurementSubmit, otpResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void PaddyProcurementSubmit(PaddyProcurementSubmit paddyProcurementSubmit) {
        try {
            OPMSApplication application = OPMSApplication.get(paddyProcurementInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.SaveProcurementData(paddyProcurementSubmit)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<ProcurementSubmitResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(ProcurementSubmitResponse procurementSubmitResponse) {
                            PaddyProcurementPresenter.this.procurementSubmitResponse = procurementSubmitResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            paddyProcurementInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            paddyProcurementInterface.PaddyProcurementSubmit(procurementSubmitResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void handleException(Exception e) {
        paddyProcurementInterface.onError(AppConstants.EXCEPTION_CODE, ": " + e.getMessage());
    }

    @Override
    public String handleError(Throwable e) {
        String msg = "";
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            msg = Utils.getErrorMessage(responseBody);
        } else if (e instanceof SocketTimeoutException) {
            msg = paddyProcurementInterface.getContext().getString(R.string.con_time_out);
        } else if (e instanceof IOException) {
            msg = paddyProcurementInterface.getContext().getString(R.string.something) +
                    paddyProcurementInterface.getContext().getString(R.string.io_exe);
        } else {
            msg = paddyProcurementInterface.getContext().getString(R.string.server_not)
                    + ": " + e.getMessage();
        }
        return msg;
    }
}


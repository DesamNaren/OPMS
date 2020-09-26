package com.cgg.pps.presenter;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.base.BasePresenter;
import com.cgg.pps.interfaces.GunnysGivenInterface;
import com.cgg.pps.model.request.farmer.gettokens.GetTokensRequest;
import com.cgg.pps.model.request.gunnydetails.GunnyDetailsRequest;
import com.cgg.pps.model.request.gunnydetails.GunnysSubmitRequest;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensDDLResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
import com.cgg.pps.model.response.gunnydetails.GunnyDetailsResponse;
import com.cgg.pps.model.response.gunnydetails.gunnysubmit.GunnySubmitResponse;
import com.cgg.pps.network.OPMSService;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.Utils;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class GunnysGivenPresenter implements BasePresenter<GunnysGivenInterface> {
    private GunnysGivenInterface gunnysGivenInterface;
    private GetTokensResponse getTokensgunnysResponse;
    private GetTokensDDLResponse getTokensDDLResponse;
    private GunnyDetailsResponse gunnyDetailsResponse;
    private GunnySubmitResponse gunnySubmitResponse;
    private CompositeDisposable disposable = new CompositeDisposable();
    @Override
    public void attachView(GunnysGivenInterface view) {
        this.gunnysGivenInterface = view;
    }

    @Override
    public void detachView() {
        if (disposable != null) disposable.dispose();
    }

    public void GetGunnysGeneratedTokens(GetTokensRequest getTokensGunnysRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(gunnysGivenInterface.getContext());
            OPMSService gitHubService = application.getDBService();
            gitHubService.getTokensDdlResponse(getTokensGunnysRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<GetTokensDDLResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(GetTokensDDLResponse getTokensDDLResponse) {
                            GunnysGivenPresenter.this.getTokensDDLResponse = getTokensDDLResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            gunnysGivenInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            gunnysGivenInterface.getTokensGunnysResponseData(getTokensDDLResponse);
                        }
                    });
        } catch (Exception e) {
            handleException(e);
        }
    }
    public void GetSelectedGunnysTokenData(GetTokensRequest getTokensGunnysRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(gunnysGivenInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getTokensGunnyResponse(getTokensGunnysRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<GetTokensResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(GetTokensResponse getTokensGunnysResponseData) {
                            GunnysGivenPresenter.this.getTokensgunnysResponse = getTokensGunnysResponseData;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            gunnysGivenInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            gunnysGivenInterface.GetSelectedGunnysTokenData(getTokensgunnysResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }


    public void GetGunnyOrderDetails(GunnyDetailsRequest gunnyDetailsRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(gunnysGivenInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getGunnyDetails(gunnyDetailsRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<GunnyDetailsResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(GunnyDetailsResponse gunnyDetailsResponse) {
                            GunnysGivenPresenter.this.gunnyDetailsResponse = gunnyDetailsResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            gunnysGivenInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            gunnysGivenInterface.GetGetGunnyOrderDetails(gunnyDetailsResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }


    public void GunnySubmitCall(GunnysSubmitRequest gunnysSubmitRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(gunnysGivenInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            Gson gson = new Gson();
            String str  = gson.toJson(gunnysSubmitRequest);


            gitHubService.getGunnySubmitResponse(gunnysSubmitRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<GunnySubmitResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(GunnySubmitResponse gunnySubmitResponse) {
                            GunnysGivenPresenter.this.gunnySubmitResponse = gunnySubmitResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            gunnysGivenInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            gunnysGivenInterface.GetGunnySubmitDetails(gunnySubmitResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void handleException(Exception e) {
        gunnysGivenInterface.onError(AppConstants.EXCEPTION_CODE, ": " + e.getMessage());
    }

    @Override
    public String handleError(Throwable e) {
        String msg = "";
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            msg = Utils.getErrorMessage(responseBody);
        } else if (e instanceof SocketTimeoutException) {
            msg = gunnysGivenInterface.getContext().getString(R.string.con_time_out);
        } else if (e instanceof IOException) {
            msg = gunnysGivenInterface.getContext().getString(R.string.something) +
                    gunnysGivenInterface.getContext().getString(R.string.io_exe);
        } else {
            msg = gunnysGivenInterface.getContext().getString(R.string.server_not)
                    + ": " + e.getMessage();
        }
        return msg;
    }
}

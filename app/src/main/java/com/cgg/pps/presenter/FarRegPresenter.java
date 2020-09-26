package com.cgg.pps.presenter;

import android.app.Dialog;
import android.util.Log;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.base.BasePresenter;
import com.cgg.pps.interfaces.FarRegInterface;
import com.cgg.pps.model.request.farmer.GetTokensStage1Request;
import com.cgg.pps.model.request.farmer.farmerdetails.FarmerDetailsRequest;
import com.cgg.pps.model.request.farmer.farmersubmit.FarmerSubmitRequest;
import com.cgg.pps.model.request.farmer.gettokens.GetTokensRequest;
import com.cgg.pps.model.request.farmer.tokenGenerate.TokenGenerationRequest;
import com.cgg.pps.model.response.farmer.farmerdetails.FarmerDetailsResponse;
import com.cgg.pps.model.response.farmer.farmersubmit.FarmerSubmitResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokenDropDownDataResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokenStage1Response;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensDDLResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
import com.cgg.pps.model.response.farmer.tokengenarate.TokenGenerateResponse;
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

public class FarRegPresenter implements BasePresenter<FarRegInterface> {

    private FarRegInterface farRegInterface;
    private FarmerDetailsResponse farmerDetailsResponse;
    private TokenGenerateResponse tokenGenerateResponse;
    private GetTokenStage1Response getTokenStage1Response;
    private GetTokenDropDownDataResponse getTokenDropDownDataResponse;
    private FarmerSubmitResponse farmerSubmitResponse;
    private CompositeDisposable disposable = new CompositeDisposable();


    @Override
    public void attachView(FarRegInterface view) {
        this.farRegInterface = view;
    }

    @Override
    public void detachView() {
        if (disposable != null) disposable.dispose();
    }

    public void GetFarmerData(FarmerDetailsRequest farmerDetailsRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(farRegInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getFarRegResponse(farmerDetailsRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<FarmerDetailsResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(FarmerDetailsResponse farmerDetailsResponse) {
                            FarRegPresenter.this.farmerDetailsResponse = farmerDetailsResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            farRegInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            farRegInterface.getFarmerRegResponse(farmerDetailsResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void GenerateToken(TokenGenerationRequest tokenGenerationRequest, Dialog dialog) {
        try {
            OPMSApplication application = OPMSApplication.get(farRegInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.tokenGenerateResponse(tokenGenerationRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<TokenGenerateResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(TokenGenerateResponse tokenGenerateResponse) {
                            FarRegPresenter.this.tokenGenerateResponse = tokenGenerateResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            farRegInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            farRegInterface.tokenGenerateResponse(tokenGenerateResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void GetFarmerGeneratedTokens(GetTokensStage1Request getTokensFarmerRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(farRegInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getTokensStage1Response(getTokensFarmerRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<GetTokenStage1Response>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(GetTokenStage1Response getTokenStage1Response) {
                            FarRegPresenter.this.getTokenStage1Response = getTokenStage1Response;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            farRegInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            farRegInterface.getTokensFarmerResponse(getTokenStage1Response);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void GetTokenDropDownData(GetTokensRequest getTokensFarmerRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(farRegInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getTokensDropDownDataResponse(getTokensFarmerRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<GetTokenDropDownDataResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(GetTokenDropDownDataResponse getTokenDropDownDataResponse) {
                            FarRegPresenter.this.getTokenDropDownDataResponse = getTokenDropDownDataResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            farRegInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            farRegInterface.getTokenDropDownDataResponse(getTokenDropDownDataResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void FarmerSubmit(FarmerSubmitRequest farmerSubmitRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(farRegInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            Gson gson = new Gson();
            String s = gson.toJson(farmerSubmitRequest);
            Log.i("SUBMIT_DATA", "FarmerSubmit: "+s);


            gitHubService.farmerSubmitResponse(farmerSubmitRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<FarmerSubmitResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(FarmerSubmitResponse farmerSubmitResponse) {
                            FarRegPresenter.this.farmerSubmitResponse = farmerSubmitResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            farRegInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            farRegInterface.farmerSubmitResponse(farmerSubmitResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void handleException(Exception e) {
        farRegInterface.onError(AppConstants.EXCEPTION_CODE, ": " + e.getMessage());
    }

    @Override
    public String handleError(Throwable e) {
        String msg = "";
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            msg = Utils.getErrorMessage(responseBody);
        } else if (e instanceof SocketTimeoutException) {
            msg = farRegInterface.getContext().getString(R.string.con_time_out);
        } else if (e instanceof IOException) {
            msg = farRegInterface.getContext().getString(R.string.something) +
                    farRegInterface.getContext().getString(R.string.io_exe);
        } else {
            msg = farRegInterface.getContext().getString(R.string.server_not)
                    + ": " + e.getMessage();
        }
        return msg;
    }


}

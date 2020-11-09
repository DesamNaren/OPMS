package com.cgg.pps.presenter;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.base.BasePresenter;
import com.cgg.pps.interfaces.RePrintInterface;
import com.cgg.pps.model.request.reprint.ProRePrintRequest;
import com.cgg.pps.model.request.reprint.TCRePrintRequest;
import com.cgg.pps.model.request.reprint.TokenRePrintRequest;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
import com.cgg.pps.model.response.reprint.ProcRePrintResponse;
import com.cgg.pps.model.response.reprint.TokenRePrintResponse;
import com.cgg.pps.model.response.reprint.truckchit.TCRePrintResponse;
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

public class RePrintPresenter implements BasePresenter<RePrintInterface> {
    private RePrintInterface rePrintInterface;
    private GetTokensResponse getTokensResponse;
    private TokenRePrintResponse tokenRePrintResponse;
    private ProcRePrintResponse procRePrintResponse;
    private TCRePrintResponse tcRePrintResponse;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void attachView(RePrintInterface view) {
        this.rePrintInterface = view;
    }

    @Override
    public void detachView() {
        if (disposable != null) disposable.dispose();
    }


    public void GetTokenRePrintData(TokenRePrintRequest tokenRePrintRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(rePrintInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getTokenRePrintResponse(tokenRePrintRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<TokenRePrintResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(TokenRePrintResponse tokenRePrintResponse) {
                            RePrintPresenter.this.tokenRePrintResponse = tokenRePrintResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            rePrintInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            rePrintInterface.GetRePrintTokenData(tokenRePrintResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }


    public void GetProTxnData(ProRePrintRequest proRePrintRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(rePrintInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getProcRePrintResponse(proRePrintRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<ProcRePrintResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(ProcRePrintResponse procRePrintResponse) {
                            RePrintPresenter.this.procRePrintResponse = procRePrintResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            rePrintInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            rePrintInterface.GetProcRePrintData(procRePrintResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void GetTCTxnData(TCRePrintRequest tcRePrintRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(rePrintInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getTCRePrintResponse(tcRePrintRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<TCRePrintResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(TCRePrintResponse tcRePrintResponse) {
                            RePrintPresenter.this.tcRePrintResponse = tcRePrintResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            rePrintInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            rePrintInterface.GetTCRePrintData(tcRePrintResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }


    @Override
    public void handleException(Exception e) {
        rePrintInterface.onError(AppConstants.EXCEPTION_CODE, ": " + e.getMessage());
    }

    @Override
    public String handleError(Throwable e) {
        String msg = "";
        if (e instanceof retrofit2.HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            msg = Utils.getErrorMessage(responseBody);
        } else if (e instanceof SocketTimeoutException) {
            msg = rePrintInterface.getContext().getString(R.string.con_time_out);
        } else if (e instanceof IOException) {
            msg = rePrintInterface.getContext().getString(R.string.something) +
                    rePrintInterface.getContext().getString(R.string.io_exe);
        } else {
            msg = rePrintInterface.getContext().getString(R.string.server_not)
                    + ": " + e.getMessage();
        }
        return msg;
    }

}

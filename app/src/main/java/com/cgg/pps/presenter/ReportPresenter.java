package com.cgg.pps.presenter;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.base.BasePresenter;
import com.cgg.pps.interfaces.TokenReportInterface;
import com.cgg.pps.model.request.farmer.gettokens.GetTokensRequest;
import com.cgg.pps.model.request.reports.CommonReportRequest;
import com.cgg.pps.model.request.reports.PaymentInfoRequest;
import com.cgg.pps.model.request.reports.TransactionReportRequest;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
import com.cgg.pps.model.response.report.payment.PaymentReportResponse;
import com.cgg.pps.model.response.report.payment.ReportsDataResponce;
import com.cgg.pps.model.response.report.transaction.TransactionReportResponseList;
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

public class ReportPresenter implements BasePresenter<TokenReportInterface> {
    private TokenReportInterface tokenReportInterface;
    private GetTokensResponse getTokensResponse;
    private ReportsDataResponce paymentReportResponseList;
    private TransactionReportResponseList transactionReportResponseList;
    private PaymentReportResponse paymentReportResponse;
    private CompositeDisposable disposable = new CompositeDisposable();
    @Override
    public void attachView(TokenReportInterface view) {
        this.tokenReportInterface = view;
    }

    @Override
    public void detachView() {
        if (disposable != null) disposable.dispose();
    }

    public void GetTokenWiseReports(GetTokensRequest getTokensReportsRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(tokenReportInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getTokensReportsResponse(getTokensReportsRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<GetTokensResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(GetTokensResponse getTokensResponse) {
                            ReportPresenter.this.getTokensResponse = getTokensResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            tokenReportInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            tokenReportInterface.getTokensResponseData(getTokensResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }
    public void GetPaymentStatusReports(CommonReportRequest commonReportRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(tokenReportInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getCommanReportsResponse(commonReportRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<ReportsDataResponce>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(ReportsDataResponce paymentReportResponseList) {
                            ReportPresenter.this.paymentReportResponseList = paymentReportResponseList;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            tokenReportInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            tokenReportInterface.getPaymentStatusReportData(paymentReportResponseList);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void getTransactionsReports(TransactionReportRequest transactionReportRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(tokenReportInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.GetTransactionsResponse(transactionReportRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<TransactionReportResponseList>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(TransactionReportResponseList transactionReportResponseList) {
                            ReportPresenter.this.transactionReportResponseList = transactionReportResponseList;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            tokenReportInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            tokenReportInterface.getTransactionsReportData(transactionReportResponseList);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }



    @Override
    public void handleException(Exception e) {
        tokenReportInterface.onError(AppConstants.EXCEPTION_CODE, ": " + e.getMessage());
    }

    @Override
    public String handleError(Throwable e) {
        String msg = "";
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            msg = Utils.getErrorMessage(responseBody);
        } else if (e instanceof SocketTimeoutException) {
            msg = tokenReportInterface.getContext().getString(R.string.con_time_out);
        } else if (e instanceof IOException) {
            msg = tokenReportInterface.getContext().getString(R.string.something) +
                    tokenReportInterface.getContext().getString(R.string.io_exe);
        } else {
            msg = tokenReportInterface.getContext().getString(R.string.server_not)
                    + ": " + e.getMessage();
        }
        return msg;
    }
}


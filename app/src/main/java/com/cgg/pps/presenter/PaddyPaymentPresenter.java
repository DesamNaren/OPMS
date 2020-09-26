package com.cgg.pps.presenter;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.base.BasePresenter;
import com.cgg.pps.interfaces.PaddyPaymentReportInterface;
import com.cgg.pps.interfaces.RePrintInterface;
import com.cgg.pps.model.request.ProRePrintRequest;
import com.cgg.pps.model.request.TCRePrintRequest;
import com.cgg.pps.model.request.TokenRePrintRequest;
import com.cgg.pps.model.request.reports.PaymentInfoRequest;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
import com.cgg.pps.model.response.report.payment.PaymentReportResponse;
import com.cgg.pps.model.response.report.payment_report.PaddyPaymentInfoResponse;
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

public class PaddyPaymentPresenter implements BasePresenter<PaddyPaymentReportInterface> {
    private PaddyPaymentReportInterface reportInterface;
    private PaddyPaymentInfoResponse paddyPaymentInfoResponse;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void attachView(PaddyPaymentReportInterface view) {
        this.reportInterface = view;
    }

    @Override
    public void detachView() {
        if (disposable != null) disposable.dispose();
    }


    public void getPaddyPaymentReports(PaymentInfoRequest paymentInfoRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(reportInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.GetPaddyPaymentResponse(paymentInfoRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<PaddyPaymentInfoResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(PaddyPaymentInfoResponse paddyPaymentInfoResponse) {
                            PaddyPaymentPresenter.this.paddyPaymentInfoResponse = paddyPaymentInfoResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            reportInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            reportInterface.getPaddyPaymentReportData(paddyPaymentInfoResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }


    @Override
    public void handleException(Exception e) {
        reportInterface.onError(AppConstants.EXCEPTION_CODE, ": " + e.getMessage());
    }

    @Override
    public String handleError(Throwable e) {
        String msg = "";
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            msg = Utils.getErrorMessage(responseBody);
        } else if (e instanceof SocketTimeoutException) {
            msg = reportInterface.getContext().getString(R.string.con_time_out);
        } else if (e instanceof IOException) {
            msg = reportInterface.getContext().getString(R.string.something) +
                    reportInterface.getContext().getString(R.string.io_exe);
        } else {
            msg = reportInterface.getContext().getString(R.string.server_not)
                    + ": " + e.getMessage();
        }
        return msg;
    }

}

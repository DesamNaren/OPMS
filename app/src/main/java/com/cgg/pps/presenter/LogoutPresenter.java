package com.cgg.pps.presenter;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.base.BasePresenter;
import com.cgg.pps.interfaces.LogoutInterface;
import com.cgg.pps.model.request.validateuser.logout.LogoutRequest;
import com.cgg.pps.model.response.validateuser.logout.LogoutResponse;
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

public class LogoutPresenter implements BasePresenter<LogoutInterface> {

    private LogoutInterface logoutInterface;
    private LogoutResponse logoutResponse;
    private CompositeDisposable disposable = new CompositeDisposable();


    @Override
    public void attachView(LogoutInterface view) {
        this.logoutInterface = view;
    }

    @Override
    public void detachView() {
        if (disposable != null) disposable.dispose();
    }

    public void callLogout(LogoutRequest logoutRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(logoutInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getLogoutResponse(logoutRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<LogoutResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(LogoutResponse logoutResponse) {
                            LogoutPresenter.this.logoutResponse = logoutResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            logoutInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            logoutInterface.getLogoutResponse(logoutResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void handleException(Exception e) {
        logoutInterface.onError(AppConstants.EXCEPTION_CODE, ": " + e.getMessage());
    }

    @Override
    public String handleError(Throwable e) {
        String msg = "";
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            msg = Utils.getErrorMessage(responseBody);
        } else if (e instanceof SocketTimeoutException) {
            msg = logoutInterface.getContext().getString(R.string.con_time_out);
        } else if (e instanceof IOException) {
            msg = logoutInterface.getContext().getString(R.string.something) +
                    logoutInterface.getContext().getString(R.string.io_exe);
        } else {
            msg = logoutInterface.getContext().getString(R.string.server_not)
                    + ": " + e.getMessage();
        }
        return msg;
    }
}

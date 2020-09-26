package com.cgg.pps.presenter;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.base.BasePresenter;
import com.cgg.pps.interfaces.MappedPPCInterface;
import com.cgg.pps.model.request.ppc_details.MappedPPCDetailsRequest;
import com.cgg.pps.model.response.ppc_details.MappedPPCDetailsResponse;
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

public class MappedPCPresenter implements BasePresenter<MappedPPCInterface> {

    private MappedPPCInterface mappedPpcInterface;
    private MappedPPCDetailsResponse mappedPpcDetailsResponse;
    private CompositeDisposable disposable = new CompositeDisposable();


    @Override
    public void attachView(MappedPPCInterface view) {
        this.mappedPpcInterface = view;
    }

    @Override
    public void detachView() {
        if (disposable != null) disposable.dispose();
    }

    public void GetMappedPPCVillagesData(MappedPPCDetailsRequest mappedPpcDetailsRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(mappedPpcInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getMappedPPCDetailsResponse(mappedPpcDetailsRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<MappedPPCDetailsResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(MappedPPCDetailsResponse mappedPpcDetailsResponse) {
                            MappedPCPresenter.this.mappedPpcDetailsResponse = mappedPpcDetailsResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            mappedPpcInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            mappedPpcInterface.getMappedPPCDetailsResponse(mappedPpcDetailsResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void handleException(Exception e) {
        mappedPpcInterface.onError(AppConstants.EXCEPTION_CODE, ": " + e.getMessage());
    }

    @Override
    public String handleError(Throwable e) {
        String msg = "";
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            msg = Utils.getErrorMessage(responseBody);
        } else if (e instanceof SocketTimeoutException) {
            msg = mappedPpcInterface.getContext().getString(R.string.con_time_out);
        } else if (e instanceof IOException) {
            msg = mappedPpcInterface.getContext().getString(R.string.something) +
                    mappedPpcInterface.getContext().getString(R.string.io_exe);
        } else {
            msg = mappedPpcInterface.getContext().getString(R.string.server_not)
                    + ": " + e.getMessage();
        }
        return msg;
    }


}

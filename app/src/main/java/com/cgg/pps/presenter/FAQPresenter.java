package com.cgg.pps.presenter;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.base.BasePresenter;
import com.cgg.pps.interfaces.FAQInterface;
import com.cgg.pps.model.request.faq.faqsubmit.FAQRequest;
import com.cgg.pps.model.request.farmer.gettokens.GetTokensRequest;
import com.cgg.pps.model.response.faq.FAQSubmitResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokenDropDownDataResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensDDLResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
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

public class FAQPresenter implements BasePresenter<FAQInterface> {
    private FAQInterface fAQInterface;
    private GetTokensDDLResponse getTokensDDLResponse;
    private GetTokensResponse getTokensFAQResponse;
    private FAQSubmitResponse faqSubmitResponse;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void attachView(FAQInterface view) {
        this.fAQInterface = view;
    }

    @Override
    public void detachView() {
        if (disposable != null) disposable.dispose();
    }

    @Override
    public void handleException(Exception e) {
        fAQInterface.onError(AppConstants.EXCEPTION_CODE, ": " + e.getMessage());
    }

    @Override
    public String handleError(Throwable e) {
        String msg = "";
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            msg = Utils.getErrorMessage(responseBody);
        } else if (e instanceof SocketTimeoutException) {
            msg = fAQInterface.getContext().getString(R.string.con_time_out);
        } else if (e instanceof IOException) {
            msg = fAQInterface.getContext().getString(R.string.something) +
                    fAQInterface.getContext().getString(R.string.io_exe);
        } else {
            msg = fAQInterface.getContext().getString(R.string.server_not)
                    + ": " + e.getMessage();
        }
        return msg;
    }

    public void GetFAQGeneratedTokens(GetTokensRequest getTokensFAQRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(fAQInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getTokensDdlResponse(getTokensFAQRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<GetTokensDDLResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(GetTokensDDLResponse getTokensDDLResponse) {
                            FAQPresenter.this.getTokensDDLResponse = getTokensDDLResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            fAQInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            fAQInterface.getTokensFAQResponseData(getTokensDDLResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void GetSelectedTokenData(GetTokensRequest getTokensFAQRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(fAQInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getSelectedFAQResponse(getTokensFAQRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<GetTokensResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(GetTokensResponse getTokensFAQResponse) {
                            FAQPresenter.this.getTokensFAQResponse = getTokensFAQResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            fAQInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            fAQInterface.GetSelectedTokenData(getTokensFAQResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void FAQSubmit(FAQRequest faqRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(fAQInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getFAQSubmitResponse(faqRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<FAQSubmitResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(FAQSubmitResponse faqSubmitResponse) {
                            FAQPresenter.this.faqSubmitResponse = faqSubmitResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            fAQInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            fAQInterface.FAQSubmit(faqSubmitResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }


}

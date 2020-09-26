package com.cgg.pps.presenter;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.base.BasePresenter;
import com.cgg.pps.interfaces.TCInterface;
import com.cgg.pps.model.request.truckchit.ManualTCRequest;
import com.cgg.pps.model.request.truckchit.MillerMasterRequest;
import com.cgg.pps.model.request.truckchit.OnlineTCRequest;
import com.cgg.pps.model.request.truckchit.TCFinalSubmitRequest;
import com.cgg.pps.model.request.truckchit.TCSubmitRequest;
import com.cgg.pps.model.request.truckchit.info.ROInfoRequest;
import com.cgg.pps.model.request.truckchit.transaction.TransactionRequest;
import com.cgg.pps.model.request.truckchit.vehicledetails.MasterVehicleTypeRequest;
import com.cgg.pps.model.request.truckchit.vehicledetails.VehicleDetailsRequest;
import com.cgg.pps.model.response.truckchit.info.ROInfoResponse;
import com.cgg.pps.model.response.truckchit.manual.ManualTCResponse;
import com.cgg.pps.model.response.truckchit.mastervehicle.TransportResponse;
import com.cgg.pps.model.response.truckchit.mastervehicle.VehicleResponse;
import com.cgg.pps.model.response.truckchit.online.OnlineTCResponse;
import com.cgg.pps.model.response.truckchit.ro.MillerMainResponse;
import com.cgg.pps.model.response.truckchit.submit.TCSubmitResponse;
import com.cgg.pps.model.response.truckchit.transaction.GetTransactionResponse;
import com.cgg.pps.model.response.truckchit.vehicle_type.VehicleTypesResponse;
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

public class TCPresenter implements BasePresenter<TCInterface> {
    private TCInterface tcInterface;
    private MillerMainResponse millerMainResponse;
    private OnlineTCResponse onlineTCResponse;
    private ROInfoResponse roInfoResponse;
    private ManualTCResponse manualTCResponse;
    private TCSubmitResponse tcSubmitResponse;
    private VehicleResponse vehicleResponse;
    private VehicleTypesResponse vehicleTypesResponse;
    private GetTransactionResponse getTransactionResponse;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void attachView(TCInterface view) {
        this.tcInterface = view;
    }

    @Override
    public void detachView() {
        if (disposable != null) disposable.dispose();
    }

    public void GetMillerMasterResponse(MillerMasterRequest millerMasterRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(tcInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getMillerMasterResponse(millerMasterRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<MillerMainResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(MillerMainResponse millerMainResponse) {
                            TCPresenter.this.millerMainResponse = millerMainResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            tcInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            tcInterface.getMillerMasterResponseData(millerMainResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }


    public void GetVehicleResponse(VehicleDetailsRequest vehicleDetailsRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(tcInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getVehicleResponse(vehicleDetailsRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<VehicleResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(VehicleResponse vehicleResponse) {
                            TCPresenter.this.vehicleResponse = vehicleResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            tcInterface.getVehicleDataResponse(null, true);
                        }

                        @Override
                        public void onComplete() {
                            tcInterface.getVehicleDataResponse(vehicleResponse, true);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            tcInterface.getVehicleDataResponse(null, true);
        }
    }

    public void GetMasterVehicleTypeResponse(MasterVehicleTypeRequest masterVehicleTypeRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(tcInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getMasterVehicleTypesResponse(masterVehicleTypeRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<VehicleTypesResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(VehicleTypesResponse vehicleTypesResponse) {
                            TCPresenter.this.vehicleTypesResponse = vehicleTypesResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            tcInterface.getMasterVehicleTypeResponse(null, true);
                        }

                        @Override
                        public void onComplete() {
                            tcInterface.getMasterVehicleTypeResponse(vehicleTypesResponse, true);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            tcInterface.getMasterVehicleTypeResponse(null, true);
        }
    }

    public void GetTransactionResponse(TransactionRequest transactionRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(tcInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getTransactionResponse(transactionRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<GetTransactionResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(GetTransactionResponse getTransactionResponse) {
                            TCPresenter.this.getTransactionResponse = getTransactionResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            tcInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            tcInterface.getTransactionResponseData(getTransactionResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void GetOnlineTCResponse(OnlineTCRequest onlineTCRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(tcInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getOnlineTCResponse(onlineTCRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<OnlineTCResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(OnlineTCResponse onlineTCResponse) {
                            TCPresenter.this.onlineTCResponse = onlineTCResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            tcInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            tcInterface.getOnlineTCResponseData(onlineTCResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void GetROInfoResponse(ROInfoRequest roInfoRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(tcInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getROInfoResponse(roInfoRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<ROInfoResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(ROInfoResponse roInfoResponse) {
                            TCPresenter.this.roInfoResponse = roInfoResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            tcInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            tcInterface.getROInfoResponseData(roInfoResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void GetManualTCResponse(ManualTCRequest manualTCRequest, boolean finalSubmitFlag,
                                    TCSubmitRequest tcSubmitRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(tcInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getManualTCResponse(manualTCRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<ManualTCResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(ManualTCResponse manualTCResponse) {
                            TCPresenter.this.manualTCResponse = manualTCResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            tcInterface.getManualTCResponseData(null, finalSubmitFlag, tcSubmitRequest);
                        }

                        @Override
                        public void onComplete() {
                            tcInterface.getManualTCResponseData(manualTCResponse, finalSubmitFlag, tcSubmitRequest);
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            tcInterface.getManualTCResponseData(null, finalSubmitFlag, tcSubmitRequest);
        }
    }


    public void SubmitCall(TCFinalSubmitRequest tcFinalSubmitRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(tcInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getTCSubmitResponse(tcFinalSubmitRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<TCSubmitResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(TCSubmitResponse tcSubmitResponse) {
                            TCPresenter.this.tcSubmitResponse = tcSubmitResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            tcInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            tcInterface.getTruckChitSubmitResponse(tcSubmitResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    @Override
    public void handleException(Exception e) {
        tcInterface.onError(AppConstants.EXCEPTION_CODE, ": " + e.getMessage());
    }

    @Override
    public String handleError(Throwable e) {
        String msg = "";
        if (e instanceof retrofit2.HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            msg = Utils.getErrorMessage(responseBody);
        } else if (e instanceof SocketTimeoutException) {
            msg = tcInterface.getContext().getString(R.string.con_time_out);
        } else if (e instanceof IOException) {
            msg = tcInterface.getContext().getString(R.string.something) +
                    tcInterface.getContext().getString(R.string.io_exe);
        } else {
            msg = tcInterface.getContext().getString(R.string.server_not)
                    + ": " + e.getMessage();
        }
        return msg;
    }
}

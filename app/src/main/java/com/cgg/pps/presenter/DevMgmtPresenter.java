package com.cgg.pps.presenter;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.base.BasePresenter;
import com.cgg.pps.interfaces.DevMgmtInterface;
import com.cgg.pps.model.request.devicemgmt.bb.bankbranch.BankBranchRequest;
import com.cgg.pps.model.request.devicemgmt.dmv.DMVRequest;
import com.cgg.pps.model.request.devicemgmt.bb.masterbank.MasterBankRequest;
import com.cgg.pps.model.request.faq.paddytest.PaddyTestRequest;
import com.cgg.pps.model.request.farmer.socialstatus.SocialStatusRequest;
import com.cgg.pps.model.request.truckchit.vehicledetails.VehicleDetailsRequest;
import com.cgg.pps.model.response.devicemgmt.bankbranch.MasterBranchMainResponse;
import com.cgg.pps.model.response.devicemgmt.masterbank.MasterBankMainResponse;
import com.cgg.pps.model.response.devicemgmt.masterdistrict.MasterDistrictMainResponse;
import com.cgg.pps.model.response.devicemgmt.mastermandal.MasterMandalMainResponse;
import com.cgg.pps.model.response.devicemgmt.mastervillage.MasterVillageMainResponse;
import com.cgg.pps.model.response.devicemgmt.paddyvalues.PaddyTestResponse;
import com.cgg.pps.model.response.socialstatus.SocialStatusResponse;
import com.cgg.pps.model.response.truckchit.mastervehicle.VehicleResponse;
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

public class DevMgmtPresenter implements BasePresenter<DevMgmtInterface> {

    private DevMgmtInterface devMgmtInterface;
    private MasterBankMainResponse masterBankResponse;
    private PaddyTestResponse paddyTestResponse;
    private SocialStatusResponse socialStatusResponse;
    private MasterBranchMainResponse masterBranchMainResponse;
    private MasterDistrictMainResponse masterDistrictMainResponse;
    private MasterMandalMainResponse masterMandalMainResponse;
    private MasterVillageMainResponse masterVillageMainResponse;
    private VehicleResponse vehicleResponse;
    private CompositeDisposable disposable = new CompositeDisposable();


    @Override
    public void attachView(DevMgmtInterface view) {
        this.devMgmtInterface = view;
    }

    @Override
    public void detachView() {
        if (disposable != null) disposable.dispose();
    }

    public void CallMasterBankData(MasterBankRequest masterBankRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(devMgmtInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getMasterBankResponse(masterBankRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<MasterBankMainResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(MasterBankMainResponse masterBankResponse) {
                            DevMgmtPresenter.this.masterBankResponse = masterBankResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg =  handleError(e);
                            devMgmtInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            devMgmtInterface.getBankResponse(masterBankResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }


    public void CallMasterBranchData(BankBranchRequest bankBranchRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(devMgmtInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getMasterBranchResponse(bankBranchRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<MasterBranchMainResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(MasterBranchMainResponse masterBranchMainResponse) {
                            DevMgmtPresenter.this.masterBranchMainResponse = masterBranchMainResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg =  handleError(e);
                            devMgmtInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            devMgmtInterface.getBranchResponse(masterBranchMainResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void CallMasterDistrictData(DMVRequest dmvRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(devMgmtInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getMasterDistrictResponse(dmvRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<MasterDistrictMainResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(MasterDistrictMainResponse masterDistrictMainResponse) {
                            DevMgmtPresenter.this.masterDistrictMainResponse = masterDistrictMainResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg =  handleError(e);
                            devMgmtInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            devMgmtInterface.getDistrictResponse(masterDistrictMainResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void CallMasterMandalData(DMVRequest dmvRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(devMgmtInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getMasterMandalResponse(dmvRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<MasterMandalMainResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(MasterMandalMainResponse masterMandalMainResponse) {
                            DevMgmtPresenter.this.masterMandalMainResponse = masterMandalMainResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg =  handleError(e);
                            devMgmtInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            devMgmtInterface.getMandalResponse(masterMandalMainResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void CallMasterVillageData(DMVRequest dmvRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(devMgmtInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getMasterVillageResponse(dmvRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<MasterVillageMainResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(MasterVillageMainResponse masterVillageMainResponse) {
                            DevMgmtPresenter.this.masterVillageMainResponse = masterVillageMainResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg =  handleError(e);
                            devMgmtInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            devMgmtInterface.getVillageResponse(masterVillageMainResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void CallPaddyTestData(PaddyTestRequest paddyTestRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(devMgmtInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getPaddyTestResponse(paddyTestRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<PaddyTestResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(PaddyTestResponse paddyTestResponse) {
                            DevMgmtPresenter.this.paddyTestResponse = paddyTestResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg =  handleError(e);
                            devMgmtInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            devMgmtInterface.getPaddyTestResponse(paddyTestResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }

    public void CallSocialStatusData(SocialStatusRequest socialStatusRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(devMgmtInterface.getContext());
            OPMSService gitHubService = application.getDBService();

            gitHubService.getSocialStatusResponse(socialStatusRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<SocialStatusResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable.add(d);
                        }

                        @Override
                        public void onNext(SocialStatusResponse socialStatusResponse) {
                            DevMgmtPresenter.this.socialStatusResponse = socialStatusResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg =  handleError(e);
                            devMgmtInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            devMgmtInterface.getSocialStatusResponse(socialStatusResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }


    public void CallVehicleResponse(VehicleDetailsRequest vehicleDetailsRequest) {
        try {
            OPMSApplication application = OPMSApplication.get(devMgmtInterface.getContext());
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
                            DevMgmtPresenter.this.vehicleResponse = vehicleResponse;
                        }

                        @Override
                        public void onError(Throwable e) {
                            String msg = handleError(e);
                            devMgmtInterface.onError(AppConstants.ERROR_CODE, msg);
                        }

                        @Override
                        public void onComplete() {
                            devMgmtInterface.getVehicleDataResponse(vehicleResponse);
                        }
                    });

        } catch (Exception e) {
            handleException(e);
        }
    }


    @Override
    public String handleError(Throwable e){
        String msg="";
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            msg = Utils.getErrorMessage(responseBody);
        } else if (e instanceof SocketTimeoutException) {
            msg = devMgmtInterface.getContext().getString(R.string.con_time_out);
        } else if (e instanceof IOException) {
            msg = devMgmtInterface.getContext().getString(R.string.something) +
                    devMgmtInterface.getContext().getString(R.string.io_exe);
        } else {
            msg = devMgmtInterface.getContext().getString(R.string.server_not)
                    + ": "+ e.getMessage();
        }
        return msg;
    }


    @Override
    public void handleException(Exception e) {
        devMgmtInterface.onError(AppConstants.EXCEPTION_CODE,": "+ e.getMessage());
    }
}

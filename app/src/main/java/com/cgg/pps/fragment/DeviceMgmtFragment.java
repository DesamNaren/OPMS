package com.cgg.pps.fragment;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.DevMgmtFragmentBinding;
import com.cgg.pps.interfaces.BBInterface;
import com.cgg.pps.interfaces.DMVInterface;
import com.cgg.pps.interfaces.DevMgmtInterface;
import com.cgg.pps.interfaces.PaddyTestInterface;
import com.cgg.pps.interfaces.VehicleInterface;
import com.cgg.pps.model.request.devicemgmt.bb.bankbranch.BankBranchRequest;
import com.cgg.pps.model.request.devicemgmt.bb.masterbank.MasterBankRequest;
import com.cgg.pps.model.request.devicemgmt.dmv.DMVRequest;
import com.cgg.pps.model.request.faq.paddytest.PaddyTestRequest;
import com.cgg.pps.model.request.farmer.socialstatus.SocialStatusRequest;
import com.cgg.pps.model.request.truckchit.vehicledetails.VehicleDetailsRequest;
import com.cgg.pps.model.response.devicemgmt.bankbranch.BranchEntity;
import com.cgg.pps.model.response.devicemgmt.bankbranch.MasterBranchMainResponse;
import com.cgg.pps.model.response.devicemgmt.masterbank.BankEntity;
import com.cgg.pps.model.response.devicemgmt.masterbank.MasterBankMainResponse;
import com.cgg.pps.model.response.devicemgmt.masterdistrict.DistrictEntity;
import com.cgg.pps.model.response.devicemgmt.masterdistrict.MasterDistrictMainResponse;
import com.cgg.pps.model.response.devicemgmt.mastermandal.MandalEntity;
import com.cgg.pps.model.response.devicemgmt.mastermandal.MasterMandalMainResponse;
import com.cgg.pps.model.response.devicemgmt.mastervillage.MasterVillageMainResponse;
import com.cgg.pps.model.response.devicemgmt.mastervillage.VillageEntity;
import com.cgg.pps.model.response.devicemgmt.paddyvalues.PaddyEntity;
import com.cgg.pps.model.response.devicemgmt.paddyvalues.PaddyTestResponse;
import com.cgg.pps.model.response.socialstatus.SocialEntity;
import com.cgg.pps.model.response.socialstatus.SocialStatusResponse;
import com.cgg.pps.model.response.truckchit.mastervehicle.VehicleDetails;
import com.cgg.pps.model.response.truckchit.mastervehicle.VehicleResponse;
import com.cgg.pps.model.response.truckchit.vehicle_type.TransporterVehicleType;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.presenter.DevMgmtPresenter;
import com.cgg.pps.room.repository.BBRepository;
import com.cgg.pps.room.repository.DMVRepository;
import com.cgg.pps.room.repository.PaddyTestRepository;
import com.cgg.pps.room.repository.VehicleRepository;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.ConnectionDetector;
import com.cgg.pps.util.CustomProgressDialog;
import com.cgg.pps.util.Utils;
import com.cgg.pps.view.DashboardActivity;
import com.cgg.pps.view.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.List;

public class DeviceMgmtFragment extends Fragment implements DevMgmtInterface, BBInterface, DMVInterface, VehicleInterface,
        PaddyTestInterface {
    private DevMgmtPresenter devMgmtPresenter;
    private DevMgmtFragmentBinding binding;
    private CustomProgressDialog customProgressDialog;
    private PPCUserDetails ppcUserDetails;
    private BBRepository bbRepository;
    private DMVRepository dmvRepository;
    private PaddyTestRepository paddyTestRepository;
    private VehicleRepository vehicleRepository;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.dev_mgmt_fragment, container, false);
        devMgmtPresenter = new DevMgmtPresenter();
        devMgmtPresenter.attachView(this);
        bbRepository = new BBRepository(getActivity());
        dmvRepository = new DMVRepository(getActivity());
        vehicleRepository = new VehicleRepository(getActivity());
        paddyTestRepository = new PaddyTestRepository(getActivity());
        customProgressDialog = new CustomProgressDialog(getActivity());
        binding.banks.setOnClickListener(onItemClick);
        binding.dmv.setOnClickListener(onItemClick);
        binding.paddy.setOnClickListener(onItemClick);
        binding.social.setOnClickListener(onItemClick);
        binding.banksLayout.setOnClickListener(onItemClick);
        binding.dmvLayout.setOnClickListener(onItemClick);
        binding.paddyLayout.setOnClickListener(onItemClick);
        binding.socialLayout.setOnClickListener(onItemClick);
        binding.vehLayout.setOnClickListener(onItemClick);
        binding.vehicles.setOnClickListener(onItemClick);

        getPreferenceData();

        bbRepository.getAllBanks(this);
        dmvRepository.getAllDistricts(this);
        paddyTestRepository.getAllPaddyValues(this);
        dmvRepository.getAllSocialStatus(this);
        vehicleRepository.getAllMasterTransports(this);

        return binding.getRoot();
    }

    public void onResume() {
        super.onResume();

        try {
            if (getActivity() != null)
                ((DashboardActivity) getActivity())
                        .setActionBarTitle(getResources().getString(R.string.DataSync));

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    private void getPreferenceData() {
        try {
            if (getActivity() != null) {
                Gson gson = OPMSApplication.get(getActivity()).getGson();
                SharedPreferences sharedPreferences = OPMSApplication.get(getActivity()).getPreferences();
                String string = sharedPreferences.getString(AppConstants.PPC_DATA, "");
                ppcUserDetails = gson.fromJson(string, PPCUserDetails.class);
                if (ppcUserDetails == null) {
                    Utils.ShowDeviceSessionAlert(getActivity(),
                            getResources().getString(R.string.DataSync),
                            getString(R.string.som_re),
                            getFragmentManager());
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener onItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.banks:
                    banksClickMethod();
                    break;
                case R.id.banksLayout:
                    banksClickMethod();
                    break;
                case R.id.dmv:
                    dmvClickMethod();
                    break;
                case R.id.dmvLayout:
                    dmvClickMethod();
                    break;
                case R.id.paddy:
                    paddyClickMethod();
                    break;
                case R.id.paddyLayout:
                    paddyClickMethod();
                    break;
                case R.id.social:
                    socialClickMethod();
                    break;
                case R.id.socialLayout:
                    socialClickMethod();
                    break;
                    case R.id.vehicles:
                        vehicleClickMethod();
                    break;
                case R.id.vehLayout:
                    vehicleClickMethod();
                    break;
            }
        }
    };

    private void banksClickMethod() {
        try {
            if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                if (!TextUtils.isEmpty(ppcUserDetails.getAuthenticationID())) {
                    if (customProgressDialog != null)
                        customProgressDialog.show();
                    MasterBankRequest masterBankRequest = new MasterBankRequest();
                    masterBankRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                    devMgmtPresenter.CallMasterBankData(masterBankRequest);
                }
            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        getResources().getString(R.string.no_internet),
                        getResources().getString(R.string.WARNING), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void dmvClickMethod() {
        try {
            if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                if (!TextUtils.isEmpty(ppcUserDetails.getAuthenticationID())) {
                    if (customProgressDialog != null)
                        customProgressDialog.show();
                    DMVRequest dmvRequest = new DMVRequest();
                    dmvRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                    devMgmtPresenter.CallMasterDistrictData(dmvRequest);
                }
            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        getResources().getString(R.string.no_internet),
                        getResources().getString(R.string.WARNING), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void paddyClickMethod() {
        try {
            if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                if (!TextUtils.isEmpty(ppcUserDetails.getAuthenticationID())) {
                    if (customProgressDialog != null)
                        customProgressDialog.show();
                    PaddyTestRequest paddyTestRequest = new PaddyTestRequest();
                    paddyTestRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                    paddyTestRequest.setpPCID(String.valueOf(ppcUserDetails.getPPCID()));
                    devMgmtPresenter.CallPaddyTestData(paddyTestRequest);
                }
            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        getResources().getString(R.string.no_internet),
                        getResources().getString(R.string.WARNING), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void socialClickMethod() {
        try {
            if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                if (!TextUtils.isEmpty(ppcUserDetails.getAuthenticationID())) {
                    if (customProgressDialog != null)
                        customProgressDialog.show();
                    SocialStatusRequest socialStatusRequest = new SocialStatusRequest();
                    socialStatusRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                    devMgmtPresenter.CallSocialStatusData(socialStatusRequest);
                }
            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        getResources().getString(R.string.no_internet),
                        getResources().getString(R.string.WARNING), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void vehicleClickMethod() {
        try {
            if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                if (!TextUtils.isEmpty(ppcUserDetails.getAuthenticationID())) {
                    if (customProgressDialog != null)
                        customProgressDialog.show();
                    VehicleDetailsRequest vehicleDetailsRequest = new VehicleDetailsRequest();
                    vehicleDetailsRequest.setPPCID(ppcUserDetails.getPPCID());
                    vehicleDetailsRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                    devMgmtPresenter.CallVehicleResponse(vehicleDetailsRequest);
                }
            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        getResources().getString(R.string.no_internet),
                        getResources().getString(R.string.WARNING), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getBankResponse(MasterBankMainResponse masterBankResponse) {
        try {
            if (masterBankResponse != null &&
                    masterBankResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {

                if (masterBankResponse.getBankEntity() != null &&
                        masterBankResponse.getBankEntity().size() > 0) {

                    bbRepository.insertAllBanks(this, masterBankResponse.getBankEntity());


                } else {
                    if (customProgressDialog != null && customProgressDialog.isShowing())
                        customProgressDialog.dismiss();
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.DataSync),
                            masterBankResponse.getResponseMessage(),
                            getString(R.string.ERROR), false);
                }
            } else if (masterBankResponse != null &&
                    masterBankResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        masterBankResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (masterBankResponse != null && masterBankResponse.getStatusCode() != null
                    && masterBankResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        masterBankResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (masterBankResponse != null && masterBankResponse.getStatusCode() != null &&
                    masterBankResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.DataSync),
                        masterBankResponse.getResponseMessage(), getFragmentManager());
            } else if (masterBankResponse != null && masterBankResponse.getStatusCode() != null
                    && masterBankResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();

                Utils.callPlayAlert(masterBankResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getPaddyTestResponse(PaddyTestResponse paddyTestResponse) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();

        try {
            if (paddyTestResponse != null &&
                    paddyTestResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {

                if (paddyTestResponse.getPaddyEntity() != null &&
                        paddyTestResponse.getPaddyEntity() != null
                        && paddyTestResponse.getPaddyEntity().size() > 0) {

                    paddyTestRepository.insertPaddyTestValues(this, paddyTestResponse.getPaddyEntity());


                } else {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.DataSync),
                            paddyTestResponse.getResponseMessage(),
                            getString(R.string.ERROR), false);
                }
            } else if (paddyTestResponse != null && paddyTestResponse.getStatusCode() != null
                    && paddyTestResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        paddyTestResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (paddyTestResponse != null && paddyTestResponse.getStatusCode() != null
                    && paddyTestResponse.getStatusCode() == AppConstants.VERSION_CODE) {

                Utils.callPlayAlert(paddyTestResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else if (paddyTestResponse != null && paddyTestResponse.getStatusCode() != null &&
                    paddyTestResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.DataSync),
                        paddyTestResponse.getResponseMessage(), getFragmentManager());
            } else if (paddyTestResponse != null && paddyTestResponse.getStatusCode() != null &&
                    paddyTestResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        paddyTestResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);

            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getSocialStatusResponse(SocialStatusResponse socialStatusResponse) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();


        try {
            if (socialStatusResponse != null &&
                    socialStatusResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {

                if (socialStatusResponse.getSocialEntities() != null &&
                        socialStatusResponse.getSocialEntities().size() > 0) {

                    bbRepository.insertSocialStatus(this, socialStatusResponse.getSocialEntities());


                } else {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.DataSync),
                            socialStatusResponse.getResponseMessage(),
                            getString(R.string.ERROR), false);
                }
            } else if (socialStatusResponse != null && socialStatusResponse.getStatusCode() != null
                    && socialStatusResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        socialStatusResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (socialStatusResponse != null && socialStatusResponse.getStatusCode() != null &&
                    socialStatusResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.DataSync),
                        socialStatusResponse.getResponseMessage(), getFragmentManager());
            } else if (socialStatusResponse != null && socialStatusResponse.getStatusCode() != null &&
                    socialStatusResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        socialStatusResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (socialStatusResponse != null && socialStatusResponse.getStatusCode() != null
                    && socialStatusResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(socialStatusResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getBranchResponse(MasterBranchMainResponse masterBranchResponse) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
        try {
            if (masterBranchResponse != null &&
                    masterBranchResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {

                if (masterBranchResponse.getBankResponse() != null &&
                        masterBranchResponse.getBankResponse().size() > 0) {
                    bbRepository.insertAllBranches(this, masterBranchResponse.getBankResponse());
                } else {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.DataSync),
                            masterBranchResponse.getResponseMessage(),
                            getString(R.string.ERROR), false);
                }
            } else if (masterBranchResponse != null && masterBranchResponse.getStatusCode() != null
                    && masterBranchResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        masterBranchResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (masterBranchResponse != null && masterBranchResponse.getStatusCode() != null
                    &&
                    masterBranchResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        masterBranchResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (masterBranchResponse != null && masterBranchResponse.getStatusCode() != null
                    && masterBranchResponse.getStatusCode() == AppConstants.VERSION_CODE) {

                Utils.callPlayAlert(masterBranchResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else if (masterBranchResponse != null && masterBranchResponse.getStatusCode() != null
                    &&
                    masterBranchResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.DataSync),
                        masterBranchResponse.getResponseMessage(), getFragmentManager());
            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getDistrictResponse(MasterDistrictMainResponse masterDistrictMainResponse) {
        try {
            if (masterDistrictMainResponse != null &&
                    masterDistrictMainResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {

                if (masterDistrictMainResponse.getDistrictEntity() != null
                        && masterDistrictMainResponse.getDistrictEntity().size() > 0) {
                    dmvRepository.insertAllDistricts(this, masterDistrictMainResponse.getDistrictEntity());


                } else {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.DataSync),
                            masterDistrictMainResponse.getResponseMessage(),
                            getString(R.string.ERROR), false);
                }
            } else if (masterDistrictMainResponse != null && masterDistrictMainResponse.getStatusCode() != null
                    && masterDistrictMainResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        masterDistrictMainResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (masterDistrictMainResponse != null && masterDistrictMainResponse.getStatusCode() != null
                    &&
                    masterDistrictMainResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        masterDistrictMainResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (masterDistrictMainResponse != null && masterDistrictMainResponse.getStatusCode() != null
                    && masterDistrictMainResponse.getStatusCode() == AppConstants.VERSION_CODE) {

                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();

                Utils.callPlayAlert(masterDistrictMainResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else if (masterDistrictMainResponse != null && masterDistrictMainResponse.getStatusCode() != null
                    &&
                    masterDistrictMainResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.DataSync),
                        masterDistrictMainResponse.getResponseMessage(), getFragmentManager());
            } else {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMandalResponse(MasterMandalMainResponse masterMandalMainResponse) {
        try {
            if (masterMandalMainResponse != null &&
                    masterMandalMainResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {

                if (masterMandalMainResponse.getMandalEntity() != null
                        && masterMandalMainResponse.getMandalEntity().size() > 0) {
                    dmvRepository.insertAllMandals(this, masterMandalMainResponse.getMandalEntity());


                } else {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.DataSync),
                            masterMandalMainResponse.getResponseMessage(),
                            getString(R.string.ERROR), false);
                }
            } else if (masterMandalMainResponse != null && masterMandalMainResponse.getStatusCode() != null
                    && masterMandalMainResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        masterMandalMainResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
            } else if (masterMandalMainResponse != null && masterMandalMainResponse.getStatusCode() != null
                    &&
                    masterMandalMainResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        masterMandalMainResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (masterMandalMainResponse != null && masterMandalMainResponse.getStatusCode() != null
                    && masterMandalMainResponse.getStatusCode() == AppConstants.VERSION_CODE) {

                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();

                Utils.callPlayAlert(masterMandalMainResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else if (masterMandalMainResponse != null && masterMandalMainResponse.getStatusCode() != null
                    &&
                    masterMandalMainResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.DataSync),
                        masterMandalMainResponse.getResponseMessage(), getFragmentManager());
            } else {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getVillageResponse(MasterVillageMainResponse masterVillageMainResponse) {

        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();

        try {
            if (masterVillageMainResponse != null &&
                    masterVillageMainResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {

                if (masterVillageMainResponse.getVillageEntity() != null
                        && masterVillageMainResponse.getVillageEntity().size() > 0) {
                    dmvRepository.insertAllVillages(this, masterVillageMainResponse.getVillageEntity());
                } else {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.DataSync),
                            masterVillageMainResponse.getResponseMessage(),
                            getString(R.string.ERROR), false);
                }
            } else if (masterVillageMainResponse != null && masterVillageMainResponse.getStatusCode() != null
                    && masterVillageMainResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        masterVillageMainResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (masterVillageMainResponse != null && masterVillageMainResponse.getStatusCode() != null
                    &&
                    masterVillageMainResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        masterVillageMainResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (masterVillageMainResponse != null && masterVillageMainResponse.getStatusCode() != null
                    && masterVillageMainResponse.getStatusCode() == AppConstants.VERSION_CODE) {

                Utils.callPlayAlert(masterVillageMainResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else if (masterVillageMainResponse != null && masterVillageMainResponse.getStatusCode() != null
                    &&
                    masterVillageMainResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.DataSync),
                        masterVillageMainResponse.getResponseMessage(), getFragmentManager());
            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getVehicleDataResponse(VehicleResponse vechicleDataResponse) {

        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();



        try {
            if (vechicleDataResponse != null &&
                    vechicleDataResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {

                if (vechicleDataResponse.getVehicleDetails() != null
                        && vechicleDataResponse.getVehicleDetails().size() > 0) {
                    vehicleRepository.insertAllVehicles(this, vechicleDataResponse.getVehicleDetails());


                } else {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.DataSync),
                            vechicleDataResponse.getResponseMessage(),
                            getString(R.string.ERROR), false);
                }
            } else if (vechicleDataResponse != null && vechicleDataResponse.getStatusCode() != null
                    && vechicleDataResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        vechicleDataResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (vechicleDataResponse != null && vechicleDataResponse.getStatusCode() != null
                    &&
                    vechicleDataResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        vechicleDataResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (vechicleDataResponse != null && vechicleDataResponse.getStatusCode() != null
                    && vechicleDataResponse.getStatusCode() == AppConstants.VERSION_CODE) {

                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();

                Utils.callPlayAlert(vechicleDataResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else if (vechicleDataResponse != null && vechicleDataResponse.getStatusCode() != null
                    &&
                    vechicleDataResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.DataSync),
                        vechicleDataResponse.getResponseMessage(), getFragmentManager());
            } else {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void getAllDistrictEntities(List<DistrictEntity> districtNames) {
        try {
            if (districtNames != null && districtNames.size() > 0) {
                dmvRepository.getAllMandals(this);
            } else {
                binding.dmv.setText(getString(R.string.download));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllMandalEntities(List<MandalEntity> mandalEntities) {
        try {
            if (mandalEntities != null && mandalEntities.size() > 0) {
                dmvRepository.getAllVillages(this);
            } else {
                binding.dmv.setText(getString(R.string.download));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllVillageEntities(List<VillageEntity> villageEntities) {
        try {
            if (villageEntities != null && villageEntities.size() > 0) {
                binding.dmv.setText(getString(R.string.re_download));
            } else {
                binding.dmv.setText(getString(R.string.download));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllSocialEntities(List<SocialEntity> socialEntities) {
        try {
            if (socialEntities != null && socialEntities.size() > 0) {
                binding.social.setText(getString(R.string.re_download));
            } else {
                binding.social.setText(getString(R.string.download));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getAllBanks(List<BankEntity> bankEntities) {
        try {
            if (bankEntities != null && bankEntities.size() > 0) {
                bbRepository.getAllBranches(this);
            } else {
                binding.banks.setText(getString(R.string.download));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllBranches(List<BranchEntity> branchEntities) {
        try {
            if (branchEntities != null && branchEntities.size() > 0) {
                binding.banks.setText(getString(R.string.re_download));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataNotAvailable() {
        // Failed to download alert
    }

    @Override
    public void distCount(int cnt) {
        try {
            if (cnt > 0) {
                DMVRequest dmvRequest = new DMVRequest();
                dmvRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                devMgmtPresenter.CallMasterMandalData(dmvRequest);
            } else {
                onDataNotAvailable();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mandalCount(int cnt) {
        try {
            if (cnt > 0) {
                DMVRequest dmvRequest = new DMVRequest();
                dmvRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                devMgmtPresenter.CallMasterVillageData(dmvRequest);
            } else {
                onDataNotAvailable();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void villageCount(int cnt) {
        try {
            if (cnt > 0) {
                binding.dmv.setText(getString(R.string.re_download));
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        getString(R.string.dmv_success),
                        getString(R.string.SUCCESS), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getAllMandalNamesByDist(List<String> mandalEntities, Spinner spinner, Spinner vspinner, String distName) {

    }

    @Override
    public void getAllLandMandalNamesByDist(List<String> mandalEntities, Spinner spinner, Spinner vspinner, String distName) {

    }


    @Override
    public void getAllVillagesByMan(List<String> villageEntities, Spinner spinner, String distName, String manName) {

    }

    @Override
    public void getAllowDistrictEntities(List<DistrictEntity> districtNames) {

    }

    @Override
    public void getAllowMandalEntities(List<MandalEntity> mandalNames) {

    }

    @Override
    public void getAllowVillageEntities(List<VillageEntity> villageNames) {

    }


    @Override
    public void bankCount(int cnt) {
        try {
            if (cnt > 0) {
                BankBranchRequest bankBranchRequest = new BankBranchRequest();
                bankBranchRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                devMgmtPresenter.CallMasterBranchData(bankBranchRequest);
            } else {
                onDataNotAvailable();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void branchCount(int cnt) {
        try {
            if (cnt > 0) {
                binding.banks.setText(getString(R.string.re_download));
                if (getActivity() != null) {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.DataSync),
                            getString(R.string.bb_success),
                            getString(R.string.SUCCESS), false);
                } else {
                    Utils.customAlert(getContext(),
                            getResources().getString(R.string.DataSync),
                            getString(R.string.bb_success),
                            getString(R.string.SUCCESS), false);
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getBankName(String bankName) {

    }

    @Override
    public void getBranchName(String bankName) {

    }


    @Override
    public void socialCount(int cnt) {
        try {
            if (cnt > 0) {
                binding.social.setText(getString(R.string.re_download));
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        getString(R.string.social_success),
                        getString(R.string.SUCCESS), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllPaddyValues(List<PaddyEntity> paddyEntities) {
        try {
            if (paddyEntities != null && paddyEntities.size() > 0) {
                binding.paddy.setText(getString(R.string.re_download));
                dmvRepository.getAllSocialStatus(this);
            } else {
                binding.paddy.setText(getString(R.string.download));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paddyTestCount(int cnt) {
        try {
            if (cnt > 0) {
                binding.paddy.setText(getString(R.string.re_download));
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.DataSync),
                        getString(R.string.paddy_success),
                        getString(R.string.SUCCESS), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getDistName(String distName) {

    }

    @Override
    public void getManName(String manName) {

    }

    @Override
    public void getVilName(String vilName) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (customProgressDialog != null) {
                customProgressDialog = null;
            }
            if (devMgmtPresenter != null) {
                devMgmtPresenter.detachView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(int code, String msg) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();

        if (code == AppConstants.ERROR_CODE) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.DataSync),
                    msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else if (code == AppConstants.EXCEPTION_CODE) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.DataSync),
                    getString(R.string.something) +
                            msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.DataSync),
                    getString(R.string.something),
                    getResources().getString(R.string.ERROR),
                    false);
        }
    }

    @Override
    public void getAllVehicles(List<VehicleDetails> vehicleDetails) {

    }

    @Override
    public void getAllTransports(List<String> transports) {

    }

    @Override
    public void getVehiclesTypes(List<TransporterVehicleType> transporterVehicleTypes) {

    }

    @Override
    public void getTransportId(Long trnId) {

    }

    @Override
    public void getVehicleDetails(VehicleDetails vehicleDetails) {

    }

    @Override
    public void getVehCnt(int cnt) {
        try {

            if (cnt > 0) {
                binding.vehicles.setText(getString(R.string.re_download));
                if (getActivity() != null) {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.DataSync),
                            getString(R.string.transport_success),
                            getString(R.string.SUCCESS), false);
                } else {
                    Utils.customAlert(getContext(),
                            getResources().getString(R.string.DataSync),
                            getString(R.string.transport_success),
                            getString(R.string.SUCCESS), false);
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getVehTypeCnt(int cnt) {

    }

    @Override
    public void getAllMasterTransports(List<VehicleDetails> vehicleDetails) {
        try {
            if (vehicleDetails!=null && vehicleDetails.size() > 0) {
                binding.vehicles.setText(getString(R.string.re_download));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


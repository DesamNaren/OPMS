package com.cgg.pps.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.analogics.thermalAPI.Bluetooth_Printer_2inch_ThermalAPI;
import com.cgg.pps.R;
import com.cgg.pps.adapter.PaddyConfirmAdapter;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.FarRegFragmentBinding;
import com.cgg.pps.interfaces.BBInterface;
import com.cgg.pps.interfaces.DMVInterface;
import com.cgg.pps.interfaces.FarRegInterface;
import com.cgg.pps.model.request.farmer.GetTokensStage1Request;
import com.cgg.pps.model.request.farmer.farmerdetails.FarmerDetailsRequest;
import com.cgg.pps.model.request.farmer.farmersubmit.FarmerLandXMLData;
import com.cgg.pps.model.request.farmer.farmersubmit.FarmerSubmitRequest;
import com.cgg.pps.model.request.farmer.farmersubmit.FarmerXMLData;
import com.cgg.pps.model.request.farmer.gettokens.GetTokensRequest;
import com.cgg.pps.model.request.farmer.tokenGenerate.TokenGenerationRequest;
import com.cgg.pps.model.response.devicemgmt.bankbranch.BranchEntity;
import com.cgg.pps.model.response.devicemgmt.masterbank.BankEntity;
import com.cgg.pps.model.response.devicemgmt.masterdistrict.DistrictEntity;
import com.cgg.pps.model.response.devicemgmt.mastermandal.MandalEntity;
import com.cgg.pps.model.response.devicemgmt.mastervillage.VillageEntity;
import com.cgg.pps.model.response.farmer.farmerdetails.FarmerDetailsResponse;
import com.cgg.pps.model.response.farmer.farmerdetails.FarmerLandDetail;
import com.cgg.pps.model.response.farmer.farmersubmit.FarmerSubmitResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokenDropDownDataResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokenStage1Response;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensDDLResponse;
import com.cgg.pps.model.response.farmer.tokengenarate.TokenGenerateResponse;
import com.cgg.pps.model.response.socialstatus.SocialEntity;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.presenter.FarRegPresenter;
import com.cgg.pps.room.repository.BBRepository;
import com.cgg.pps.room.repository.DMVRepository;
import com.cgg.pps.util.APrinter;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.ConnectionDetector;
import com.cgg.pps.util.CustomProgressDialog;
import com.cgg.pps.util.Utils;
import com.cgg.pps.view.DashboardActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FarRegFragment extends Fragment implements FarRegInterface,
        BBInterface, DMVInterface, AdapterView.OnItemSelectedListener {
    private FarRegPresenter farRegPresenter;
    private FarRegFragmentBinding binding;

    private ArrayList<View> landDetailsTableRowsArrayList;
    private ArrayList<View> FinalLandDetailsTableRowsArrayList;
    private GetTokenStage1Response getTokensFarmerResponseMain;
    private GetTokenDropDownDataResponse getTokenDropDownDataResponseMain;
    private PPCUserDetails ppcUserDetails;
    private BBRepository bbRepository;
    private DMVRepository dmvRepository;
    private List<DistrictEntity> districtEntities;
    private List<BankEntity> bankEntities;
    private List<BranchEntity> branchEntities;
    private List<MandalEntity> mandalEntities;
    private List<VillageEntity> villageEntities;
    private List<SocialEntity> socialEntities;
    private List<String> disStringListMain, manStringListMain, vilStringListMain, socialStringListMain;
    private List<String> bankStringList, branchStringList;
    private String f_district = "--Select--";
    private String f_mandal = "--Select--";
    private String f_village = "--Select--";
    private String f_social = "--Select--";
    private SharedPreferences sharedPreferences;

    private FarmerDetailsResponse fRes;
    private List<String> emptyList;
    private String enteredAadhar;
    private String farmerRegID;
    CustomProgressDialog customProgressDialog;
    private APrinter aPrinter;
    private Utils.DecimalDigitsInputFilter decimalDigitsInputFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.far_reg_fragment, container, false);
        farRegPresenter = new FarRegPresenter();
        farRegPresenter.attachView(this);
        customProgressDialog = new CustomProgressDialog(getActivity());
        bbRepository = new BBRepository(getActivity());
        dmvRepository = new DMVRepository(getActivity());
        districtEntities = new ArrayList<>();
        mandalEntities = new ArrayList<>();
        villageEntities = new ArrayList<>();
        socialEntities = new ArrayList<>();
        bankEntities = new ArrayList<>();
        disStringListMain = new ArrayList<>();
        manStringListMain = new ArrayList<>();
        vilStringListMain = new ArrayList<>();
        bankStringList = new ArrayList<>();
        branchStringList = new ArrayList<>();
        socialStringListMain = new ArrayList<>();
        farmerRegID = "0";

        emptyList = new ArrayList<>();
        emptyList.add("--Select--");

        landDetailsTableRowsArrayList = new ArrayList<View>();
        FinalLandDetailsTableRowsArrayList = new ArrayList<View>();
        binding.FRLandDetailsTl.removeAllViews();
        addNewRowToLandDetailsTable(null);

        binding.FRSearchTv.setOnClickListener(onBtnClick);
        binding.FRUpdateFarmerTv.setOnClickListener(onBtnClick);
        binding.FrTokenGenerate.setOnClickListener(onBtnClick);
        binding.FRPaddyDetailsCb.setOnClickListener(onBtnClick);
        binding.FRSubmitBtn.setOnClickListener(onBtnClick);
        binding.tokenRefresh.setOnClickListener(onBtnClick);
        binding.FRSearchdataEt.addTextChangedListener(new CustomTextWatcher(binding.FRSearchdataEt));


        setEditTextNE(binding.FRMainLl);

        getPreferenceData();
        aPrinter = APrinter.getaPrinter();

        dmvRepository.getAllDistricts(this);


        binding.FRAddDistrictSp.setOnItemSelectedListener(this);
        binding.FRAddMandalSp.setOnItemSelectedListener(this);
        binding.FRTokenNoSp.setOnItemSelectedListener(this);
        binding.FRBankNameSp.setOnItemClickListener(new AutoCompleteTVListener(binding.FRBankNameSp));
        binding.FRBankBranchNameSp.setOnItemClickListener(new AutoCompleteTVListener(binding.FRBankBranchNameSp));

        decimalDigitsInputFilter = new Utils.DecimalDigitsInputFilter(7, 2);

        if (sharedPreferences.getBoolean(AppConstants.PRINTER_CON_FLAG, false)) {
            if (AppConstants.BT_ADDRESS == null || AppConstants.BT_ADDRESS.equals("")) {
                Utils.ShowPrintAlert(getActivity(), getFragmentManager(), customProgressDialog);
            }
        }

        binding.rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utils.hideKeyboard(view);
                return false;
            }
        });

        return binding.getRoot();
    }

    private boolean connectBT() {
        boolean flag = false;
        try {
            if (!TextUtils.isEmpty(AppConstants.BT_ADDRESS)) {
                flag = aPrinter.openBT(AppConstants.BT_ADDRESS);
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.print_alert_not), Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }


    public void onResume() {
        super.onResume();

        try {
            if (getActivity() != null)
                ((DashboardActivity) getActivity())
                        .setActionBarTitle(getResources().getString(R.string.FarmerRegistration));

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    private void getPreferenceData() {
        try {
            if (getActivity() != null) {
                Gson gson = OPMSApplication.get(getActivity()).getGson();
                sharedPreferences = OPMSApplication.get(getActivity()).getPreferences();
                String string = sharedPreferences.getString(AppConstants.PPC_DATA, "");
                ppcUserDetails = gson.fromJson(string, PPCUserDetails.class);
                if (ppcUserDetails == null) {
                    Utils.ShowDeviceSessionAlert(getActivity(),
                            getResources().getString(R.string.FarmerRegistration),
                            getString(R.string.som_re),
                            getFragmentManager());
                }
            }

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FarmerDetailsRequest farmerRegRequest() {
        FarmerDetailsRequest farmerDetailsRequest = new FarmerDetailsRequest();
        farmerDetailsRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
        farmerDetailsRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));
        farmerDetailsRequest.setRegistrationNo(binding.FRSearchdataEt.getText().toString().trim());
        return farmerDetailsRequest;
    }

    @Override
    public void getFarmerRegResponse(FarmerDetailsResponse farmerDetailsResponse) {
        try {
            landDetailsTableRowsArrayList = new ArrayList<View>();
            FinalLandDetailsTableRowsArrayList = new ArrayList<View>();
            binding.FRLandDetailsTl.removeAllViews();

            fRes = farmerDetailsResponse;
            if (farmerDetailsResponse != null &&
                    farmerDetailsResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                binding.FRMainLl.setVisibility(View.VISIBLE);
                binding.mainFarDet.setVisibility(View.GONE);

                if (farmerDetailsResponse.getFarmerBasicDetail() != null) {
                    setFarmerBasicDetail(farmerDetailsResponse);
                    setFarmerBankDetail(farmerDetailsResponse);
                }
                if (farmerDetailsResponse.getFarmerLandDetail() != null &&
                        farmerDetailsResponse.getFarmerLandDetail() != null &&
                        farmerDetailsResponse.getFarmerLandDetail().size() > 0) {


                    setFarmerLandDetail(farmerDetailsResponse);
                }
            } else if (farmerDetailsResponse != null &&
                    farmerDetailsResponse.getStatusCode() == AppConstants.FAILURE_CODE) {

                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FarmerRegistration),
                        farmerDetailsResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (farmerDetailsResponse != null &&
                    farmerDetailsResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                showAlertNavigateToFarmerUpdate(farmerDetailsResponse.getResponseMessage());

                binding.FRAadhaarNoEt.setText(enteredAadhar);

                binding.FRMainLl.setVisibility(View.VISIBLE);
                Utils.loadSpinnerSetSelectedItem(getActivity(), disStringListMain, binding.FRAddDistrictSp, f_district);
                Utils.loadSpinnerSetSelectedItem(getActivity(), manStringListMain, binding.FRAddMandalSp, f_mandal);
                Utils.loadSpinnerSetSelectedItem(getActivity(), vilStringListMain, binding.FRAddVillageSp, f_village);
                Utils.loadSpinnerSetSelectedItem(getActivity(), socialStringListMain, binding.FRSocialStatus, f_social);
                Utils.loadAutoCompleteTextData(getActivity(), bankStringList, binding.FRBankNameSp);
            } else if (farmerDetailsResponse != null &&
                    farmerDetailsResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.FarmerRegistration),
                        farmerDetailsResponse.getResponseMessage(), getFragmentManager());
            } else if (farmerDetailsResponse != null && farmerDetailsResponse.getStatusCode() != null
                    && farmerDetailsResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.callPlayAlert(farmerDetailsResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FarmerRegistration),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }

            if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                getGeneratedTokens();
            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FarmerRegistration),
                        getResources().getString(R.string.no_internet),
                        getResources().getString(R.string.WARNING), false);
            }

        } catch (Resources.NotFoundException e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.FarmerRegistration),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
    }

    @Override
    public void tokenGenerateResponse(TokenGenerateResponse tokenGenerateResponse) {
        try {
            if (customProgressDialog != null && customProgressDialog.isShowing())
                customProgressDialog.dismiss();

            if (tokenGenerateResponse != null &&
                    tokenGenerateResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                getGeneratedTokens();

                String tokenData = " పిపిసి కోడ్  	     : " + tokenGenerateResponse.getTokenDetails().get(0).getCreatedBy() + "\n" +
                        " టోకెన్ ఇచ్చిన  తేది     : " + tokenGenerateResponse.getTokenDetails().get(0).getCreatedDate().substring(0, 10) + "\n" +
                        " రైతు రావాల్సిన తేది    : " + tokenGenerateResponse.getTokenDetails().get(0).getAppointmentDate() + "\n" +
                        " రైతు పేరు   	     : " + tokenGenerateResponse.getTokenDetails().get(0).getFarmerName() + "\n" +
                        " ఫోన్ నంబరు 	 : " + tokenGenerateResponse.getTokenDetails().get(0).getMobile() + "\n" +
                        " టోకెన్ నంబరు	 : " + tokenGenerateResponse.getTokenDetails().get(0).getTokenNo();

                ShowSuccessCustomAlert(tokenGenerateResponse, getActivity(), "Token Receipt!!", tokenData);


            } else if (tokenGenerateResponse != null &&
                    tokenGenerateResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FarmerRegistration),
                        tokenGenerateResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (tokenGenerateResponse != null &&
                    tokenGenerateResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FarmerRegistration),
                        tokenGenerateResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (tokenGenerateResponse != null && tokenGenerateResponse.getStatusCode() != null
                    && tokenGenerateResponse.getStatusCode() == AppConstants.VERSION_CODE) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                Utils.callPlayAlert(tokenGenerateResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else if (tokenGenerateResponse != null && tokenGenerateResponse.getStatusCode() != null &&
                    tokenGenerateResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.FarmerRegistration),
                        tokenGenerateResponse.getResponseMessage(), getFragmentManager());
            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FarmerRegistration),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.FarmerRegistration),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }

    }


    @Override
    public void getTokensFarmerResponse(GetTokenStage1Response getTokenStage1Response) {
        try {
            if (customProgressDialog != null && customProgressDialog.isShowing())
                customProgressDialog.dismiss();

            getTokensFarmerResponseMain = getTokenStage1Response;
            if (getTokenStage1Response != null &&
                    getTokenStage1Response.getStatusCode() == AppConstants.SUCCESS_CODE) {

                if (getTokenStage1Response.getStage1GetTokensddls() != null &&
                        getTokenStage1Response.getStage1GetTokensddls().size() > 0) {

                    ArrayList<String> getTokens = new ArrayList<>();
                    getTokens.add(0, "--Select--");
                    for (int i = 0; i < getTokenStage1Response.getStage1GetTokensddls().size(); i++) {
                        getTokens.add(getTokenStage1Response.getStage1GetTokensddls().get(i).getTokenNo());
                    }

                    if (getTokens.size() > 0) {
                        Utils.loadSpinnerData(getActivity(), getTokens, binding.FRTokenNoSp);
                    }

                }


            } else if (getTokenStage1Response != null &&
                    getTokenStage1Response.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FarmerRegistration),
                        getTokenStage1Response.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (getTokenStage1Response != null &&
                    getTokenStage1Response.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FarmerRegistration),
                        getTokenStage1Response.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (getTokenStage1Response != null &&
                    getTokenStage1Response.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.FarmerRegistration),
                        getTokenStage1Response.getResponseMessage(), getFragmentManager());
            } else if (getTokenStage1Response != null && getTokenStage1Response.getStatusCode() != null
                    && getTokenStage1Response.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(getTokenStage1Response.getResponseMessage(), getActivity(),
                        getFragmentManager(), true);

            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FarmerRegistration),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.FarmerRegistration),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }


    }

    @Override
    public void getTokenDropDownDataResponse(GetTokenDropDownDataResponse getTokenDropDownDataResponse) {
        try {

            if (customProgressDialog != null && customProgressDialog.isShowing())
                customProgressDialog.dismiss();

            getTokenDropDownDataResponseMain = getTokenDropDownDataResponse;
            if (getTokenDropDownDataResponse != null &&
                    getTokenDropDownDataResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {

                if (getTokenDropDownDataResponse.getGetTokenDropDownData() != null && getTokenDropDownDataResponse.getGetTokenDropDownData().size() > 0) {

                    for (int z = 0; z < getTokenDropDownDataResponse.getGetTokenDropDownData().size(); z++) {
                        binding.mainFarDet.setVisibility(View.VISIBLE);
                        binding.farTokenNoTv.setText(String.valueOf(getTokenDropDownDataResponse.getGetTokenDropDownData().get(z).getTokenNo()));
                        binding.farFarmerNameTv.setText(getTokenDropDownDataResponse.getGetTokenDropDownData().get(z).getFarmerName());
                        binding.farMobileTv.setText(getTokenDropDownDataResponse.getGetTokenDropDownData().get(z).getMobile());
                        binding.farAppTv.setText(getTokenDropDownDataResponse.getGetTokenDropDownData().get(z).getAppointmentDate());
                    }

                }


            } else if (getTokenDropDownDataResponse != null &&
                    getTokenDropDownDataResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FarmerRegistration),
                        getTokenDropDownDataResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (getTokenDropDownDataResponse != null &&
                    getTokenDropDownDataResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FarmerRegistration),
                        getTokenDropDownDataResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (getTokenDropDownDataResponse != null &&
                    getTokenDropDownDataResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.FarmerRegistration),
                        getTokenDropDownDataResponse.getResponseMessage(), getFragmentManager());
            } else if (getTokenDropDownDataResponse != null && getTokenDropDownDataResponse.getStatusCode() != null
                    && getTokenDropDownDataResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(getTokenDropDownDataResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FarmerRegistration),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.FarmerRegistration),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }
    }

    @Override
    public void farmerSubmitResponse(FarmerSubmitResponse farmerSubmitResponse) {
        try {
            if (customProgressDialog != null && customProgressDialog.isShowing())
                customProgressDialog.dismiss();

            if (farmerSubmitResponse != null &&
                    farmerSubmitResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {

                if (conDialog != null && conDialog.isShowing()) {
                    conDialog.dismiss();
                }


                Fragment fragment = new DashboardFragment();
                Utils.showAlertNavigateToFarmer1(getActivity(), fragment, getFragmentManager(),
                        getResources().getString(R.string.FarmerRegistration),
                        farmerSubmitResponse.getResponseMessage(), true);

            } else if (farmerSubmitResponse != null &&
                    farmerSubmitResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FarmerRegistration),
                        farmerSubmitResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (farmerSubmitResponse != null &&
                    farmerSubmitResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FarmerRegistration),
                        farmerSubmitResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (farmerSubmitResponse != null &&
                    farmerSubmitResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.FarmerRegistration),
                        farmerSubmitResponse.getResponseMessage(), getFragmentManager());
            } else if (farmerSubmitResponse != null && farmerSubmitResponse.getStatusCode() != null
                    && farmerSubmitResponse.getStatusCode() == AppConstants.VERSION_CODE) {

                if (conDialog != null && conDialog.isShowing()) {
                    conDialog.dismiss();
                }

                Utils.callPlayAlert(farmerSubmitResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FarmerRegistration),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.FarmerRegistration),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }

    }

    private void ShowSuccessCustomAlert(TokenGenerateResponse tokenGenerateResponse,
                                        Activity activity, String title, String msg) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_alert_success_print);
                dialog.setCancelable(false);

                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(getResources().getString(R.string.app_name) + ": " + title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(msg);

                Button print = dialog.findViewById(R.id.btDialogPrint);
                Button cancel = dialog.findViewById(R.id.btDialogCancel);

                if (!(sharedPreferences.getBoolean(AppConstants.PRINTER_CON_FLAG, false))) {
                    msg = msg.concat("\n").concat(getString(R.string.plz_take_reprint));
                    print.setText(getString(R.string.Ok));
                    cancel.setVisibility(View.GONE);
                }

                dialogMessage.setText(msg);


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                    }
                });

                print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (dialog.isShowing())
                            dialog.dismiss();

                        if (!(sharedPreferences.getBoolean(AppConstants.PRINTER_CON_FLAG, false))) {
                            Toast.makeText(activity, getString(R.string.plz_take_reprint), Toast.LENGTH_SHORT).show();
                        } else if (AppConstants.BT_ADDRESS == null || AppConstants.BT_ADDRESS.equals("")) {
                            Toast.makeText(activity, getString(R.string.plz_take_reprint), Toast.LENGTH_SHORT).show();
                        } else {
                            new printAsyncTask(tokenGenerateResponse).execute();
                        }

                    }
                });

                if (!dialog.isShowing())
                    dialog.show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    private boolean validateData() {
        String aadharNum = binding.FRSearchdataEt.getText().toString().trim();
        if (TextUtils.isEmpty(aadharNum)) {
            binding.FRSearchdataEt.setError(getString(R.string.aad_req));
            binding.FRSearchdataEt.requestFocus();
            return false;
        }

        boolean isValidAadhar = Utils.ValidateAadharNumber(aadharNum);
        if (!isValidAadhar) {
            binding.FRSearchdataEt.setError(getString(R.string.val_aad_num));
            binding.FRSearchdataEt.requestFocus();
            return false;
        }
        return true;
    }

    private void setFarmerLandDetail(FarmerDetailsResponse farmerDetailsResponse) {
        for (int i = 0; i < farmerDetailsResponse.getFarmerLandDetail().size(); i++) {
            addNewRowToLandDetailsTable(farmerDetailsResponse.getFarmerLandDetail().get(i));
        }
    }

    private void setFarmerBankDetail(FarmerDetailsResponse farmerDetailsResponse) {

        try {
            if (farmerDetailsResponse.getFarmerBasicDetail().getIFSCCode() != null) {
                binding.FRBankIFSCcodeEt.setText(farmerDetailsResponse.getFarmerBasicDetail().getIFSCCode().trim());
            }

            if (farmerDetailsResponse.getFarmerBasicDetail().getNameasperBank() != null) {
                binding.FRFarmerNameAsPerBankEt.setText(farmerDetailsResponse.getFarmerBasicDetail().getNameasperBank().trim());
            }


            if (farmerDetailsResponse.getFarmerBasicDetail().getBankAccountNo() != null) {
                binding.FRBankACNoEt.setText(farmerDetailsResponse.getFarmerBasicDetail().getBankAccountNo().trim());
            }

            if (farmerDetailsResponse.getFarmerBasicDetail().getBankAccountNo() != null) {
                binding.FRBankACNoCFMEt.setText(farmerDetailsResponse.getFarmerBasicDetail().getBankAccountNo().trim());
            }


            if (bankStringList.size() > 0) {
                if (farmerDetailsResponse.getFarmerBasicDetail().getBankId() != null) {
                    bbRepository.getBankName(this,
                            farmerDetailsResponse.getFarmerBasicDetail().getBankId().trim());
                }
            }

            if (branchStringList.size() > 0) {
                if (farmerDetailsResponse.getFarmerBasicDetail().getBranchId() != null) {
                    bbRepository.getBranchName(this,
                            farmerDetailsResponse.getFarmerBasicDetail().getBankId().trim(),
                            farmerDetailsResponse.getFarmerBasicDetail().getBranchId().trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFarmerBasicDetail(FarmerDetailsResponse farmerDetailsResponse) {
        if (farmerDetailsResponse.getFarmerBasicDetail().getFarmerName() != null) {
            binding.FRFarmerNameEt.setText(farmerDetailsResponse.getFarmerBasicDetail().getFarmerName().trim());
        }

        if (farmerDetailsResponse.getFarmerBasicDetail().getRegistrationNo() != null) {
            binding.FRAadhaarNoEt.setText(farmerDetailsResponse.getFarmerBasicDetail().getRegistrationNo().trim());
        }

        if (farmerDetailsResponse.getFarmerBasicDetail().getFarmerFatherName() != null) {
            binding.FRFatherNameEt.setText(farmerDetailsResponse.getFarmerBasicDetail().getFarmerFatherName().trim());
        }

        if (farmerDetailsResponse.getFarmerBasicDetail().getAge() != null && farmerDetailsResponse.getFarmerBasicDetail().getAge() > 0) {
            binding.FRAgeEt.setText(String.valueOf(farmerDetailsResponse.getFarmerBasicDetail().getAge()).trim());
        }

        if (farmerDetailsResponse.getFarmerBasicDetail().getMobile() != null) {
            binding.FRAddMobileEt.setText(farmerDetailsResponse.getFarmerBasicDetail().getMobile().trim());
        }


        if (farmerDetailsResponse.getFarmerBasicDetail().getStreetName() != null) {
            binding.FRLandMarkEt.setText(farmerDetailsResponse.getFarmerBasicDetail().getStreetName().trim());
        }

        if (farmerDetailsResponse.getFarmerBasicDetail().getHouseNo() != null) {
            binding.FRDoorNoEt.setText(farmerDetailsResponse.getFarmerBasicDetail().getHouseNo().trim());
        }

        if (farmerDetailsResponse.getFarmerBasicDetail().getPincode() != null) {
            binding.FRAddPincodeEt.setText(farmerDetailsResponse.getFarmerBasicDetail().getPincode().trim());
        }


        // Caused by java.lang.NullPointerException
        //Attempt to invoke virtual method 'int java.lang.String.hashCode()' on a null object reference (Oct 31, 2019, 3:11:00 PM)
        //com.cgg.pps.fragment.FarRegFragment.setFarmerBasicDetail

        //Need to handle null case on farmerDetailsResponse.getFarmerBasicDetail().getGender()
        if (farmerDetailsResponse.getFarmerBasicDetail().getGender() != null) {
            switch (farmerDetailsResponse.getFarmerBasicDetail().getGender()) {
                case "1":
                    binding.FRGenderMaleRd.setChecked(true);
                    break;
                case "2":
                    binding.FRGenderFemaleRd.setChecked(true);
                    break;
                default:
                    binding.FRRgGender.clearCheck();
            }
        }


        try {
            if (farmerDetailsResponse.getFarmerBasicDetail() != null && farmerDetailsResponse.getFarmerBasicDetail().getDistrictID() != null
                    && disStringListMain.size() > 0) {
                dmvRepository.getLandDistrictName(this, farmerDetailsResponse.getFarmerBasicDetail().getDistrictID().trim(),
                        binding.FRAddDistrictSp, disStringListMain);
            }


            if (farmerDetailsResponse.getFarmerBasicDetail() != null && farmerDetailsResponse.getFarmerBasicDetail().getDistrictID() != null
                    && farmerDetailsResponse.getFarmerBasicDetail().getMandalID() != null) {

                List<String> mandalsStringList = getMandalsListByDistID(farmerDetailsResponse);

                if (mandalsStringList.size() > 0) {
                    dmvRepository.getLandMandalName(this, farmerDetailsResponse.getFarmerBasicDetail().getDistrictID().trim()
                            , farmerDetailsResponse.getFarmerBasicDetail().getMandalID().trim()
                            , binding.FRAddMandalSp, mandalsStringList);
                }
            }

            if (farmerDetailsResponse.getFarmerBasicDetail() != null && farmerDetailsResponse.getFarmerBasicDetail().getDistrictID() != null
                    && farmerDetailsResponse.getFarmerBasicDetail().getMandalID() != null
                    && farmerDetailsResponse.getFarmerBasicDetail().getVillageID() != null) {

                List<String> villageStringList = getVillagesListByDistID(farmerDetailsResponse);

                if (villageStringList.size() > 0) {
                    dmvRepository.getLandVillageName(this, farmerDetailsResponse.getFarmerBasicDetail().getDistrictID().trim()
                            , farmerDetailsResponse.getFarmerBasicDetail().getMandalID().trim()
                            , farmerDetailsResponse.getFarmerBasicDetail().getVillageID().trim(),
                            binding.FRAddVillageSp, villageStringList);
                }
            }

            if (farmerDetailsResponse.getFarmerBasicDetail() != null && farmerDetailsResponse.getFarmerBasicDetail().getSocialStatusID() != null &&
                    socialStringListMain.size() > 0) {
                dmvRepository.getSocialStatusName(this, String.valueOf(farmerDetailsResponse.getFarmerBasicDetail().getSocialStatusID()).trim(),
                        binding.FRSocialStatus, socialStringListMain);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private List<String> getVillagesListByDistID(FarmerDetailsResponse farmerDetailsResponse) {
        List<String> villageStringList = new ArrayList<>();
        for (VillageEntity villageEntity : villageEntities) {
            if (villageEntity.getDistrictID().equalsIgnoreCase(farmerDetailsResponse.getFarmerBasicDetail().getDistrictID().trim())
                    &&
                    villageEntity.getMandalID().equalsIgnoreCase(farmerDetailsResponse.getFarmerBasicDetail().getMandalID().trim())) {
                villageStringList.add(villageEntity.getVillageName());
            }

        }
        return villageStringList;
    }

    private List<String> getMandalsListByDistID(FarmerDetailsResponse farmerDetailsResponse) {
        List<String> mandalsStringList = new ArrayList<>();
        for (MandalEntity mandalEntity : mandalEntities) {
            if (mandalEntity.getDistrictID().equalsIgnoreCase(farmerDetailsResponse.getFarmerBasicDetail().getDistrictID().trim())) {
                mandalsStringList.add(mandalEntity.getMandalName());
            }

        }
        return mandalsStringList;
    }

    private void addNewRowToLandDetailsTable(FarmerLandDetail landDetails) {
        try {

            if (landDetailsTableRowsArrayList.size() == 0) {
                binding.FRLandDetailsTl.removeAllViews();
            }

            LayoutInflater mInflater = LayoutInflater.from(getActivity());
            final TableRow paramView = (TableRow) mInflater.inflate(R.layout.fr_landdetails_tablerow, null);
            final LinearLayout landDetLL = paramView.findViewById(R.id.FRLd_landDetails_LL);
            landDetLL.setVisibility(View.GONE);

            final EditText ownerName = paramView.findViewById(R.id.FRLd_ownerName_Et);
            final EditText ownerAadhar = paramView.findViewById(R.id.FRLd_ownerAadhaar_Et);
            Spinner farmerNatureSp = paramView.findViewById(R.id.FRLd_landEnjoyerNature_Sp);
            Spinner farmerRelationSp = paramView.findViewById(R.id.FRLd_relationship_Sp);
            final EditText surveyNum = paramView.findViewById(R.id.FRLd_surveyNo_Et);
            final EditText passbookNum = paramView.findViewById(R.id.FRLd_khattaNo_Et);
            final EditText landAcr = paramView.findViewById(R.id.FRLd_landAcers_Et);
            final EditText landCul = paramView.findViewById(R.id.FRLd_cultivatedLand_Et);
            final EditText landExt = paramView.findViewById(R.id.FRLd_LeasedExtent_Et);

            final EditText landAlrLeaExt = paramView.findViewById(R.id.Already_FRLd_LeasedExtent_Et);
            final EditText landAlrCulExt = paramView.findViewById(R.id.Already_FRLd_cultivatedLand_Et);
            final EditText yealdperAccer = paramView.findViewById(R.id.yeald_Per_Accer);

            final Spinner landTypeSp = paramView.findViewById(R.id.FRLd_landType_Sp);
            final TextView hiddenID = paramView.findViewById(R.id.hiddenID);

            final Spinner districtSpinner = paramView.findViewById(R.id.FRLd_district_Sp);
            final Spinner mandalSpinner = paramView.findViewById(R.id.FRLd_mandal_Sp);
            final Spinner villageSpinner = paramView.findViewById(R.id.FRLd_village_Sp);


            landCul.setFilters(new InputFilter[]{decimalDigitsInputFilter});
            landExt.setFilters(new InputFilter[]{decimalDigitsInputFilter});

            setLayoutDisable(ownerName);
            setLayoutDisable(ownerAadhar);
            setLayoutDisable(farmerNatureSp);
            setLayoutDisable(farmerRelationSp);
            setLayoutDisable(surveyNum);
            setLayoutDisable(passbookNum);
            setLayoutDisable(landAcr);
            setLayoutDisable(landTypeSp);
            setLayoutDisable(districtSpinner);
            setLayoutDisable(mandalSpinner);
            setLayoutDisable(villageSpinner);
            setLayoutDisable(landAlrLeaExt);
            setLayoutDisable(landAlrCulExt);
            setLayoutDisable(yealdperAccer);

            final CheckBox FRLd_check_Cb = paramView.findViewById(R.id.FRLd_check_Cb);
            final ImageView FRLd_delete = paramView.findViewById(R.id.FRLd_delete);
            FRLd_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteRowFromLandDetailsTable(paramView);
                }
            });

            if (landDetails != null) {

                if (landDetails.getMappedToPPC() != null &&
                        landDetails.getMappedToPPC().equalsIgnoreCase("Y")
                        && landDetails.getPendingApproval() != 26) {
                    FRLd_check_Cb.setVisibility(View.VISIBLE);
                } else {
                    FRLd_check_Cb.setVisibility(View.INVISIBLE);
                }

                ownerName.setText(landDetails.getLandOwnerName().trim());
                ownerAadhar.setText(landDetails.getLandOwnerAadhaar().trim());
                farmerNatureSp.setSelection(landDetails.getOwnershipTypeId());

                setFarmerNatureData(farmerNatureSp, landDetLL, ownerName, ownerAadhar, farmerRelationSp, landDetails);

                farmerRelationSp.setSelection(landDetails.getLegalHeirRelationId());
                surveyNum.setText(landDetails.getSurveyNo().trim());

                passbookNum.setText(landDetails.getPassBookNo().trim());
                landAcr.setText(String.valueOf(landDetails.getTotalArea()));
                landAlrLeaExt.setText(String.valueOf(landDetails.getTotalLeaseArea()));
                landAlrCulExt.setText(String.valueOf(landDetails.getTotalCultivableArea()));
                yealdperAccer.setText("" + landDetails.getYieldperAcre());
                landTypeSp.setSelection(landDetails.getLandTypeId());
                hiddenID.setText(landDetails.getDataSource() + ":" +
                        landDetails.getFarmerRegistrationId() + ":" +
                        landDetails.getMainLandRowId() + ":" +
                        landDetails.getSubLandRowId() + ":" +
                        landDetails.getProcRowId() + ":"
                        + landDetails.getYieldperAcre());

                setFarmerLandDMVData(districtSpinner, mandalSpinner, villageSpinner, landDetails);

            } else {
                setFarmerNatureData(farmerNatureSp, landDetLL, ownerName, ownerAadhar, farmerRelationSp, null);
                setFarmerLandDMVData(districtSpinner, mandalSpinner, villageSpinner, null);
            }

            if (landDetails != null &&
                    landDetails.getTotalArea() != null &&
                    landDetails.getTotalCultivableArea() != null &&
                    landDetails.getTotalLeaseArea() != null) {
                if (landDetails.getTotalArea() <= landDetails.getTotalCultivableArea() + landDetails.getTotalLeaseArea()) {
                    paramView.setBackgroundColor(getResources().getColor(R.color.red_selection));
                    FRLd_check_Cb.setEnabled(false);
                    setLayoutDisable(landExt);
                    setLayoutDisable(landCul);
                }
            }


            FRLd_check_Cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FRLd_check_Cb.isChecked())
                        paramView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    else
                        paramView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
            });
            binding.FRLandDetailsTl.addView(paramView);
            landDetailsTableRowsArrayList.add(paramView);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<String> getLandMandalsListByDistID(FarmerLandDetail landDetails) {
        List<String> mandalsStringList = new ArrayList<>();
        for (MandalEntity mandalEntity : mandalEntities) {
            if (mandalEntity.getDistrictID().equalsIgnoreCase(landDetails.getDistrictID().trim())) {
                mandalsStringList.add(mandalEntity.getMandalName());
            }

        }
        return mandalsStringList;
    }

    private List<String> getLandVillagesListByDistID(FarmerLandDetail landDetails) {
        List<String> villageStringList = new ArrayList<>();
        for (VillageEntity villageEntity : villageEntities) {
            if (villageEntity.getDistrictID().equalsIgnoreCase(landDetails.getDistrictID().trim())
                    &&
                    villageEntity.getMandalID().equalsIgnoreCase(landDetails.getMandalID().trim())) {
                villageStringList.add(villageEntity.getVillageName());
            }

        }
        return villageStringList;
    }

    private void setFarmerLandDMVData(Spinner districtSpinner, Spinner mandalSpinner,
                                      Spinner villageSpinner, FarmerLandDetail landDetails) {

        try {
            if (disStringListMain.size() > 0) {
                if (landDetails.getDistrictID() != null) {
                    dmvRepository.getLandDistrictName(this, landDetails.getDistrictID().trim(),
                            districtSpinner, disStringListMain);
                }
            }
            List<String> mandalsStringList = getLandMandalsListByDistID(landDetails);

            if (mandalsStringList.size() > 0) {
                if (landDetails.getDistrictID() != null && landDetails.getMandalID() != null) {
                    dmvRepository.getLandMandalName(this, landDetails.getDistrictID().trim()
                            , landDetails.getMandalID().trim()
                            , mandalSpinner, mandalsStringList);
                }
            }

            List<String> villageStringList = getLandVillagesListByDistID(landDetails);

            if (villageStringList.size() > 0) {
                if (landDetails.getDistrictID() != null && landDetails.getMandalID() != null && landDetails.getVillageID() != null) {
                    dmvRepository.getLandVillageName(this, landDetails.getDistrictID().trim()
                            , landDetails.getMandalID().trim()
                            , landDetails.getVillageID().trim(),
                            villageSpinner, villageStringList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String districtName = parent.getSelectedItem().toString().trim();
                    if (!districtName.equalsIgnoreCase("--Select--")) {

                        List<String> mandalsStringList = new ArrayList<>();
                        mandalsStringList.add(0, "--Select--");

                        for (DistrictEntity districtEntity : districtEntities) {
                            if (districtEntity.getDistName().equalsIgnoreCase(districtName)) {
                                String distID = districtEntity.getDistId();


                                for (MandalEntity mandalEntity : mandalEntities) {
                                    if (mandalEntity.getDistrictID().equalsIgnoreCase(distID)) {
                                        mandalsStringList.add(mandalEntity.getMandalName());
                                    }


                                }
                            }
                        }
                        setLandData(landDetails, mandalSpinner, mandalsStringList);
                    } else {
                        Utils.loadSpinnerSetSelectedItem(getActivity(), emptyList, mandalSpinner, f_mandal);
                        Utils.loadSpinnerSetSelectedItem(getActivity(), emptyList, villageSpinner, f_village);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        mandalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String districtName = districtSpinner.getSelectedItem().toString().trim();
                    String mandalName = parent.getSelectedItem().toString().trim();
                    if (!mandalName.equalsIgnoreCase("--Select--")) {

                        String distId = null, manId = null;
                        List<String> villagesStringList = new ArrayList<>();
                        villagesStringList.add(0, "--Select--");

                        for (DistrictEntity districtEntity : districtEntities) {
                            if (districtEntity.getDistName().equalsIgnoreCase(districtName)) {
                                distId = districtEntity.getDistId();
                            }
                        }
                        for (MandalEntity mandalEntity : mandalEntities) {
                            if (mandalEntity.getDistrictID().equalsIgnoreCase(distId) &&
                                    mandalEntity.getMandalName().equalsIgnoreCase(mandalName)) {
                                manId = mandalEntity.getMandalID();
                            }
                        }

                        if (distId != null && manId != null) {
                            for (VillageEntity villageEntity : villageEntities) {
                                if (villageEntity.getDistrictID().equalsIgnoreCase(distId)
                                        &&
                                        villageEntity.getMandalID().equalsIgnoreCase(manId)) {
                                    villagesStringList.add(villageEntity.getVillageName());
                                }

                            }
                        }


                        setLandVilData(landDetails, villageSpinner, villagesStringList);
                    } else {
                        Utils.loadSpinnerSetSelectedItem(getActivity(), emptyList, villageSpinner, f_village);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void setBasicDistData(FarmerDetailsResponse farmerDetailsResponse, Spinner spi, List<String> mandalEntities) {
        try {
            dmvRepository.getLandMandalName(this, farmerDetailsResponse.getFarmerBasicDetail().getDistrictID().trim()
                    , farmerDetailsResponse.getFarmerBasicDetail().getMandalID().trim()
                    , spi, mandalEntities);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setLandData(FarmerLandDetail l, Spinner spi, List<String> mandalEntities) {
        try {
            dmvRepository.getLandMandalName(this, l.getDistrictID().trim()
                    , l.getMandalID().trim()
                    , spi, mandalEntities);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setLandVilData(FarmerLandDetail l, Spinner spi, List<String> villageEntities) {
        try {
            dmvRepository.getLandVillageName(this, l.getDistrictID().trim()
                    , l.getMandalID().trim()
                    , l.getVillageID().trim()
                    , spi, villageEntities);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBasicVilData(FarmerDetailsResponse farmerDetailsResponse, Spinner spi, List<String> villageEntities) {
        try {
            dmvRepository.getLandVillageName(this, farmerDetailsResponse.getFarmerBasicDetail().getDistrictID().trim()
                    , farmerDetailsResponse.getFarmerBasicDetail().getMandalID().trim()
                    , farmerDetailsResponse.getFarmerBasicDetail().getVillageID().trim()
                    , spi, villageEntities);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setLayoutDisable(View view) {
        view.setEnabled(false);
    }

    private void setLayoutEnable(View view) {
        view.setEnabled(true);
    }

    private void deleteRowFromLandDetailsTable(TableRow tableRow) {
        try {
            binding.FRLandDetailsTl.removeView(tableRow);
            landDetailsTableRowsArrayList.remove(tableRow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFarmerNatureData(Spinner farmerNatureSp, LinearLayout landDetLL, EditText ownerName,
                                     EditText ownerAadhar, Spinner farmerRelationSp, FarmerLandDetail landDetails) {
        farmerNatureSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    if (landDetails != null) {
                        int nature = parent.getSelectedItemPosition();
                        if (nature != 0) {
                            landDetLL.setVisibility(View.VISIBLE);
                            if (nature == 1) {
                                ownerName.setText(binding.FRFarmerNameEt.getText().toString());
                                ownerAadhar.setText(binding.FRAadhaarNoEt.getText().toString());
                                farmerRelationSp.setVisibility(View.INVISIBLE);
                            } else if (nature == 2) {
                                ownerName.setText(landDetails.getLandOwnerName());
                                ownerAadhar.setText(landDetails.getLandOwnerAadhaar());
                                farmerRelationSp.setVisibility(View.INVISIBLE);
                            } else if (nature == 3) {
                                ownerName.setText(landDetails.getLandOwnerName());
                                ownerAadhar.setText(landDetails.getLandOwnerAadhaar());
                                farmerRelationSp.setVisibility(View.VISIBLE);
                            }
                        } else {
                            landDetLL.setVisibility(View.GONE);
                        }
                    } else {
                        int nature = parent.getSelectedItemPosition();
                        if (nature != 0) {
                            landDetLL.setVisibility(View.VISIBLE);
                            if (nature == 1) {
                                ownerName.setText(binding.FRFarmerNameEt.getText().toString());
                                ownerAadhar.setText(binding.FRAadhaarNoEt.getText().toString());
                                farmerRelationSp.setVisibility(View.INVISIBLE);
                            } else if (nature == 2) {
                                ownerName.setText("");
                                ownerAadhar.setText("");
                                farmerRelationSp.setVisibility(View.INVISIBLE);
                            } else if (nature == 3) {
                                ownerName.setText("");
                                ownerAadhar.setText("");
                                farmerRelationSp.setVisibility(View.VISIBLE);
                            }
                        } else {
                            landDetLL.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private View.OnClickListener onBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                switch (view.getId()) {
                    case R.id.FR_search_Tv:
                        //Utils.customAlert(getActivity(),"","",getResources().getString(R.string.ERROR),true);
                        fRes = null;
                        binding.FRSearchdataEt.clearFocus();
                        Utils.hideKeyboard(view);
                        clearForm(binding.FRMainLl);


                        binding.FRAddDistrictSp.setSelection(0);
                        binding.FRAddMandalSp.setSelection(0);
                        binding.FRAddVillageSp.setSelection(0);
                        binding.FRSocialStatus.setSelection(0);
                        landDetailsTableRowsArrayList = new ArrayList<View>();
                        FinalLandDetailsTableRowsArrayList = new ArrayList<View>();
                        binding.FRLandDetailsTl.removeAllViews();
                        binding.FRPaddyDetailsCb.setChecked(false);

                        binding.FRPaddyDetailsLL.setVisibility(View.GONE);
                        binding.FRBankIFSCcodeEt.setEnabled(false);
                        binding.FRAadhaarNoEt.setEnabled(false);
                        enteredAadhar = binding.FRSearchdataEt.getText().toString().trim();

                        if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                            if (validateData()) {
                                FarmerDetailsRequest farmerDetailsRequest = farmerRegRequest();
                                if (customProgressDialog != null)
                                    customProgressDialog.show();
                                farRegPresenter.GetFarmerData(farmerDetailsRequest);
                            }
                        } else {
                            Utils.customAlert(getActivity(),
                                    getResources().getString(R.string.FarmerRegistration),
                                    getResources().getString(R.string.no_internet),
                                    getResources().getString(R.string.WARNING), false);
                        }

                        break;
                    case R.id.FR_update_farmer_Tv:
                        Fragment fragment = new FarUpdateFragment();
                        String fragmentTag = FarUpdateFragment.class.getSimpleName();
                        AppConstants.FARG_TAG = fragmentTag;
                        callFarUpdateFragment(fragment, fragmentTag);
                        break;

                    case R.id.Fr_Token_generate:
                        if (validateData()) {
                            TokenGenerateAlert();
                        }
                        break;

                    case R.id.FR_PaddyDetails_Cb:
                        if (((CheckBox) view).isChecked()) {
                            if (getTokensFarmerResponseMain != null && getTokensFarmerResponseMain.getStage1GetTokensddls() != null
                                    && getTokensFarmerResponseMain.getStage1GetTokensddls().size() > 0) {
                                binding.FRPaddyDetailsLL.setVisibility(View.VISIBLE);
                                binding.main411.setVisibility(View.VISIBLE);
                            } else {
                                if (getTokensFarmerResponseMain != null && getTokensFarmerResponseMain.getResponseMessage() != null)
                                    Utils.customAlert(getActivity(),
                                            getResources().getString(R.string.FarmerRegistration),
                                            getTokensFarmerResponseMain.getResponseMessage(),
                                            getString(R.string.ERROR), false);
                                else
                                    Utils.customAlert(getActivity(),
                                            getResources().getString(R.string.FarmerRegistration),
                                            getString(R.string.no_tokens),
                                            getString(R.string.INFORMATION), false);
                                ((CheckBox) view).setChecked(false);
                            }
                        } else {
                            binding.FRPaddyDetailsLL.setVisibility(View.GONE);
                            binding.main411.setVisibility(View.GONE);
                            binding.FRBankPassBookCB.setChecked(false);
                            binding.FRPattadharPassBookCB.setChecked(false);
                            binding.FRVROCertifiedCB.setChecked(false);
                        }
                        break;


                    case R.id.FR_Submit_Btn:
                        FarmerSubmitRequest farmerSubmitRequest = new FarmerSubmitRequest();
                        if (validateFarmerData(farmerSubmitRequest)) {
                            ShowSubmitRegisterAlert(farmerSubmitRequest, finalProcVal);
                        }
                        break;

                    case R.id.token_refresh:
                        getGeneratedTokens();
                        break;
                }
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
    };
    private Dialog conDialog;

    private void ShowSubmitRegisterAlert(FarmerSubmitRequest farmerSubmitRequest, Double finalCutLand) {
        try {
            if (getActivity() != null) {
                conDialog = new Dialog(getActivity());
                conDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                if (conDialog.getWindow() != null && conDialog.getWindow().getAttributes() != null) {
                    conDialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                    conDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    conDialog.setContentView(R.layout.paady_confirm_alert);
                    conDialog.setCancelable(false);
                    TextView titleTv = conDialog.findViewById(R.id.alert_titleTv);
                    titleTv.setText(getResources().getString(R.string.paddyConfirmation));
                    final RecyclerView recyclerView = conDialog.findViewById(R.id.rvPaddy);
                    PaddyConfirmAdapter paddyConfirmAdapter = new PaddyConfirmAdapter(getActivity(),
                            farmerSubmitRequest, conDialog, farRegPresenter, ppcUserDetails, getActivity()
                            , customProgressDialog);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(paddyConfirmAdapter);

                    if (!conDialog.isShowing())
                        conDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private Double finalProcVal;

    private boolean validateFarmerData(FarmerSubmitRequest farmerSubmitRequest) {

        String distID = null, manID = null, vilID = null;
        Integer bankID = 0, branchID = 0, socID = 0;

        FarmerXMLData farmerXMLData = new FarmerXMLData();
        ArrayList<FarmerLandXMLData> farmerLandXMLDataArrayList = new ArrayList<>();
        FinalLandDetailsTableRowsArrayList = new ArrayList<>();

        farmerXMLData.setRegistrationID(farmerRegID);

        if (binding.FRSearchdataEt.getText() != null) {
            binding.FRAadhaarNoEt.setText(binding.FRSearchdataEt.getText().toString().trim());
        }

        String farmerName = null;
        if (binding.FRFarmerNameEt.getText() != null) {
            farmerName = binding.FRFarmerNameEt.getText().toString().trim();
        }
        if (TextUtils.isEmpty(farmerName)) {
            setEditTextData(binding.FRFarmerNameEt, getString(R.string.far_name_required));
            return false;
        }

        if (!farmerName.matches("[a-zA-Z. ]+")) {
            setEditTextData(binding.FRFarmerNameEt, getString(R.string.val_far_name));
            return false;
        } else {
            farmerXMLData.setFarmerName(farmerName);
        }

        String aadhaarNo = null;
        if (binding.FRAadhaarNoEt.getText() != null) {
            aadhaarNo = binding.FRAadhaarNoEt.getText().toString().trim();
        }
        if (TextUtils.isEmpty(aadhaarNo)) {
            setEditTextData(binding.FRAadhaarNoEt, getString(R.string.aad_req));
            return false;
        }

        if (!Utils.ValidateAadharNumber(aadhaarNo)) {
            binding.FRAadhaarNoEt.setEnabled(true);

            setEditTextData(binding.FRAadhaarNoEt, getString(R.string.val_aad_req));
            return false;
        } else {
            farmerXMLData.setRegistrationNo(aadhaarNo);
        }

        String fatherName = null;
        if (binding.FRFatherNameEt.getText() != null) {
            fatherName = binding.FRFatherNameEt.getText().toString().trim();
        }

        if (TextUtils.isEmpty(fatherName)) {
            setEditTextData(binding.FRFatherNameEt, getString(R.string.far_fat_name));
            return false;
        }

        if (!fatherName.matches("[a-zA-Z. ]+")) {
            setEditTextData(binding.FRFatherNameEt, getString(R.string.val_fat_name));
            return false;
        } else {
            farmerXMLData.setFarmerFatherName(fatherName);
        }

        String gender;

        if (binding.FRRgGender.getCheckedRadioButtonId() == -1) {
            showAlertError(getString(R.string.sel_gen));
            return false;
        } else if (binding.FRGenderMaleRd.isChecked()) {
            gender = "1";
            farmerXMLData.setGender(gender);
        } else {
            gender = "2";
            farmerXMLData.setGender(gender);
        }

        if (binding.FRAgeEt.getText() != null) {
            String age = binding.FRAgeEt.getText().toString().trim();
            farmerXMLData.setAge(age);
        }
        String mobNum = null;
        if (binding.FRAddMobileEt.getText() != null) {
            mobNum = binding.FRAddMobileEt.getText().toString().trim();
        }
        if (TextUtils.isEmpty(mobNum)) {
            setEditTextData(binding.FRAddMobileEt, getString(R.string.enter_mob));
            return false;
        }
        if (!mobNum.matches("[6-9]{1}+[0-9]+") || mobNum.length() != 10) {
            setEditTextData(binding.FRAddMobileEt, getString(R.string.ent_val_mob));
            return false;
        } else {
            farmerXMLData.setFMobile(mobNum);
        }
        String district = "";
        if (binding.FRAddDistrictSp.getSelectedItem() != null) {
            district = binding.FRAddDistrictSp.getSelectedItem().toString();
        }
        if (TextUtils.isEmpty(district) || district.equalsIgnoreCase("--Select--")) {
            showAlertError(getString(R.string.sel_dis));
            return false;
        } else {

            for (DistrictEntity districtEntity : districtEntities) {
                if (districtEntity.getDistName().equalsIgnoreCase(district)) {
                    distID = districtEntity.getDistId();
                    break;
                }
            }
            if (distID != null) {
                farmerXMLData.setFDistrictID(distID);
            }

        }

        String mandal = "";
        if (binding.FRAddMandalSp.getSelectedItem() != null) {
            mandal = binding.FRAddMandalSp.getSelectedItem().toString();

        }
        if (TextUtils.isEmpty(mandal) || mandal.equalsIgnoreCase("--Select--")) {
            showAlertError(getString(R.string.sel_man));
            return false;
        } else {

            for (MandalEntity mandalEntity : mandalEntities) {
                if (mandalEntity.getDistrictID().equalsIgnoreCase(distID)
                        && mandalEntity.getMandalName().equalsIgnoreCase(mandal)) {
                    manID = mandalEntity.getMandalID();
                    break;
                }
            }

            if (manID != null) {
                farmerXMLData.setFMandalID(manID);
            }
        }
        String village = "";
        if (binding.FRAddVillageSp.getSelectedItem() != null) {
            village = binding.FRAddVillageSp.getSelectedItem().toString();
        }


        if (TextUtils.isEmpty(village) || village.equalsIgnoreCase("--Select--")) {
            showAlertError(getString(R.string.sel_vil));
            return false;
        } else {
            for (VillageEntity villageEntity : villageEntities) {
                if (villageEntity.getDistrictID().equalsIgnoreCase(distID)
                        && villageEntity.getMandalID().equalsIgnoreCase(manID)
                        && villageEntity.getVillageName().equalsIgnoreCase(village)) {
                    vilID = villageEntity.getVillageID();
                    break;
                }
            }

            if (vilID != null) {
                farmerXMLData.setFVillageID(vilID);
            }
        }

        if (binding.FRLandMarkEt.getText() != null) {
            String streetName = binding.FRLandMarkEt.getText().toString();
            farmerXMLData.setFLandMark(streetName);
        }


        if (binding.FRDoorNoEt.getText() != null) {
            String houseNum = binding.FRDoorNoEt.getText().toString();
            farmerXMLData.setFDoorNo(houseNum);
        }
        String pinCode = null;
        if (binding.FRAddPincodeEt.getText() != null) {
            pinCode = binding.FRAddPincodeEt.getText().toString();
        }
        if (!TextUtils.isEmpty(pinCode) && !pinCode.matches("5+[0-9]{5}")) {
            setEditTextData(binding.FRAddPincodeEt, getString(R.string.valid_pin));
            return false;
        } else {
            farmerXMLData.setFPincode(pinCode);
        }

        if (binding.FRSocialStatus.getSelectedItem() != null) {
            String socStatus = binding.FRSocialStatus.getSelectedItem().toString();
            if (socStatus.equalsIgnoreCase("--Select--")) {
                showAlertError(getString(R.string.sel_sta));
                return false;
            } else {

                for (SocialEntity socialEntity : socialEntities) {
                    if (socialEntity.getSocialStatus().equalsIgnoreCase(socStatus)) {
                        socID = socialEntity.getSocialStatusID();
                        break;
                    }
                }
                if (socID != null) {
                    farmerXMLData.setSocialStatusID(String.valueOf(socID));
                }
            }
        } else {
            showAlertError(getString(R.string.sel_sta));
            return false;
        }
        String bankName=null;
        if(binding.FRBankNameSp.getText()!=null) {
           bankName = binding.FRBankNameSp.getText().toString().trim();
        }

        if (TextUtils.isEmpty(bankName)) {
            setEditTextData(binding.FRBankNameSp, getString(R.string.ent_bank));
            return false;
        }

        /*if (!bankName.matches("[a-zA-Z.-]+")) {
            setEditTextData(binding.FRBankNameSp, getString(R.string.ent_val_bank));
            return false;
        }*/

        for (BankEntity bankEntity : bankEntities) {
            if (bankEntity.getBankName().equalsIgnoreCase(bankName)) {
                bankID = bankEntity.getBankID();
                break;
            }
        }

        if (bankID == 0) {
            setEditTextData(binding.FRBankNameSp, getString(R.string.sel_bank_list));
            return false;
        } else {
            farmerXMLData.setBankID(String.valueOf(bankID));
        }

        String branchName = null;
        if(binding.FRBankBranchNameSp.getText()!=null) {
            branchName = binding.FRBankBranchNameSp.getText().toString().trim();
        }

        if (TextUtils.isEmpty(branchName)) {
            setEditTextData(binding.FRBankBranchNameSp, getString(R.string.ent_bra));
            return false;
        }

       /* if (!branchName.matches("[a-zA-Z.- ]+")) {
            setEditTextData(binding.FRBankBranchNameSp, getString(R.string.ent_val_bra));
            return false;
        }*/

        for (BranchEntity branchEntity : branchEntities) {
            if (branchEntity.getBankID().equals(bankID) && branchEntity.getBranchName().equalsIgnoreCase(branchName)) {
                branchID = branchEntity.getBranchID();
                break;
            }
        }

        if (branchID == 0) {
            setEditTextData(binding.FRBankBranchNameSp, getString(R.string.sel_bra_list));
            return false;
        } else {
            farmerXMLData.setBranchID(String.valueOf(branchID));
        }

        String ifscCode = null;
        if(binding.FRBankIFSCcodeEt.getText()!=null) {
           ifscCode = binding.FRBankIFSCcodeEt.getText().toString().trim();
        }

        if (TextUtils.isEmpty(ifscCode)) {
            setEditTextData(binding.FRBankBranchNameSp, getString(R.string.ent_ifsc));
            return false;
        } else {
            farmerXMLData.setIFSCCode(ifscCode);
        }

        String farmerNameAsPerBank =null;
        if(binding.FRFarmerNameAsPerBankEt.getText()!=null) {
           farmerNameAsPerBank=   binding.FRFarmerNameAsPerBankEt.getText().toString().trim();
        }

        if (TextUtils.isEmpty(farmerNameAsPerBank)) {
            setEditTextData(binding.FRFarmerNameAsPerBankEt, getString(R.string.name_as_per_bank));
            return false;
        }

        if (!farmerNameAsPerBank.matches("[\\p{L}- ]+")) {
            setEditTextData(binding.FRFarmerNameAsPerBankEt, getString(R.string.only_alpha));
            return false;
        }

        if (farmerNameAsPerBank.length() > 50) {
            setEditTextData(binding.FRFarmerNameAsPerBankEt, getString(R.string.name_50));
            return false;
        } else {
            farmerXMLData.setNameasperBank(farmerNameAsPerBank);
        }

        String bankAccNo = null;
        if( binding.FRBankACNoEt.getText()!=null) {
            bankAccNo = binding.FRBankACNoEt.getText().toString().trim();
        }

        if (TextUtils.isEmpty(bankAccNo)) {
            setEditTextData(binding.FRBankACNoEt, getString(R.string.enter_bank_num));
            return false;
        }

        if (Long.parseLong(bankAccNo) == 0) {
            setEditTextData(binding.FRBankACNoEt, getString(R.string.bank_not_zero));
            return false;
        }

        if (bankAccNo.length() > 35) {
            setEditTextData(binding.FRBankACNoEt, getString(R.string.bank_35));
            return false;
        }
        String confirmBankAccNo = null;
        if(binding.FRBankACNoCFMEt.getText()!=null) {
          confirmBankAccNo =  binding.FRBankACNoCFMEt.getText().toString().trim();
        }

        if (TextUtils.isEmpty(confirmBankAccNo)) {
            setEditTextData(binding.FRBankACNoCFMEt, getString(R.string.cnf_bank));
            return false;
        }

        if (!bankAccNo.equalsIgnoreCase(confirmBankAccNo)) {
            setEditTextData(binding.FRBankACNoEt, getString(R.string.bank_not_matc));
            return false;
        } else {
            farmerXMLData.setBankAccountNo(bankAccNo);
        }


        if (!farmerName.equalsIgnoreCase(farmerNameAsPerBank)) {
            showAlertError(getString(R.string.far_name_bank));
            return false;
        }

        String createdDate = Utils.getCurrentDateTime();
        farmerXMLData.setCreatedDate(createdDate);


        if (landDetailsTableRowsArrayList.size() > 0) {

            ArrayList<View> tempViewArrayList = new ArrayList<>(landDetailsTableRowsArrayList);

            if (tempViewArrayList.size() > 0) {
                boolean isChecked = false;
                for (int i = 0; i < tempViewArrayList.size(); i++) {
                    TableRow tb = (TableRow) tempViewArrayList.get(i);
                    CheckBox check = tb.findViewById(R.id.FRLd_check_Cb);
                    if (check.isChecked()) {
                        isChecked = true;
                        FinalLandDetailsTableRowsArrayList.add(tempViewArrayList.get(i));
                    }
                }
                if (!isChecked) {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.FarmerRegistration), getString(R.string.sel_land_rec),
                            getString(R.string.INFORMATION), false);
                    return false;
                }
            }

            finalProcVal = 0.0;
            for (int i = 0; i < FinalLandDetailsTableRowsArrayList.size(); i++) {

                FarmerLandXMLData farmerLandXMLData = new FarmerLandXMLData();

                TableRow paramView = (TableRow) FinalLandDetailsTableRowsArrayList.get(i);
                Spinner FarmerNatureSp = paramView.findViewById(R.id.FRLd_landEnjoyerNature_Sp);
                Spinner farmerRelationSp = paramView.findViewById(R.id.FRLd_relationship_Sp);
                CheckBox check = paramView.findViewById(R.id.FRLd_check_Cb);
                TextView hiddenID = paramView.findViewById(R.id.hiddenID);
                int farmerNature = FarmerNatureSp.getSelectedItemPosition();
                TextView alrLeasedExt = paramView.findViewById(R.id.Already_FRLd_LeasedExtent_Et);
                TextView alrCulExt = paramView.findViewById(R.id.Already_FRLd_cultivatedLand_Et);

                if (check.isChecked()) {
                    String landDistID = null, landManID = null, landVilID = null;


                    String originalStr = hiddenID.getText().toString();
                    String[] strings = originalStr.split(":");
                    String DataSource = strings[0];
                    Integer FarmerRegistrationId = Integer.valueOf(strings[1]);
                    Integer MainLandRowId = Integer.valueOf(strings[2]);
                    Integer SubLandRowId = Integer.valueOf(strings[3]);
                    Integer ProcRowId = Integer.valueOf(strings[4]);
                    Integer YieldPerAcre = Integer.valueOf(strings[5]);

                    farmerLandXMLData.setDataSource(DataSource);
                    farmerLandXMLData.setFarmerRegistrationId(String.valueOf(FarmerRegistrationId));
                    farmerLandXMLData.setMainLandRowId(String.valueOf(MainLandRowId));
                    farmerLandXMLData.setSubLandRowId(String.valueOf(SubLandRowId));
                    farmerLandXMLData.setProcRowId(String.valueOf(ProcRowId));
                    farmerLandXMLData.setYieldperAcre(String.valueOf(YieldPerAcre));


                    if (farmerNature == 0) {
                        showAlertError(getString(R.string.sel_far));
                        FarmerNatureSp.requestFocus();
                        return false;
                    } else {
                        farmerLandXMLData.setOwnershipTypeId(String.valueOf(farmerNature));
                    }


                    EditText ownerNameEt = (paramView.findViewById(R.id.FRLd_ownerName_Et));
                    String ownerName = ownerNameEt.getText().toString().trim();

                    if (TextUtils.isEmpty(ownerName)) {
                        setEditTextData(ownerNameEt, getString(R.string.ent_owner));
                        return false;
                    } else {
                        farmerLandXMLData.setLandOwnerName(ownerName);
                    }

                    EditText ownerAadhaarEt = (paramView.findViewById(R.id.FRLd_ownerAadhaar_Et));
                    String ownerAadhaarNo = ownerAadhaarEt.getText().toString().trim();

                    if (!Utils.ValidateAadharNumber(ownerAadhaarNo)) {
                        setEditTextData(ownerAadhaarEt, getString(R.string.val_aad_num));
                        return false;
                    } else {
                        farmerLandXMLData.setLandOwnerAadhaar(ownerAadhaarNo);
                    }

                    int farmerRelID = farmerRelationSp.getSelectedItemPosition();
                    if (farmerNature == 3 && farmerRelID == 0) {
                        showAlertError(getString(R.string.far_rel));
                        farmerRelationSp.requestFocus();
                        return false;
                    } else {
                        farmerLandXMLData.setLegalHeirRelationId(String.valueOf(farmerRelID));
                    }


                    Spinner districtSp = (Spinner) paramView.findViewById(R.id.FRLd_district_Sp);
                    String landDist = districtSp.getSelectedItem().toString().trim();

                    if (landDist.equalsIgnoreCase("--Select--")) {
                        showAlertError(getString(R.string.dist_land));
                        return false;
                    } else {

                        for (DistrictEntity districtEntity : districtEntities) {
                            if (districtEntity.getDistName().equalsIgnoreCase(landDist)) {
                                landDistID = districtEntity.getDistId();
                                break;
                            }
                        }
                        if (landDistID != null) {
                            farmerLandXMLData.setDistrictID(landDistID);
                        }
                    }


                    Spinner mandalSpinner = (Spinner) paramView.findViewById(R.id.FRLd_mandal_Sp);
                    String landMan = mandalSpinner.getSelectedItem().toString().trim();

                    if (landMan.equalsIgnoreCase("--Select--")) {
                        showAlertError(getString(R.string.sel_land_man));
                        return false;
                    } else {
                        for (MandalEntity mandalEntity : mandalEntities) {
                            if (mandalEntity.getDistrictID().equalsIgnoreCase(landDistID) &&
                                    mandalEntity.getMandalName().equalsIgnoreCase(landMan)) {
                                landManID = mandalEntity.getMandalID();
                                break;
                            }
                        }

                        if (landManID != null) {
                            farmerLandXMLData.setMandalID(landManID);
                        }
                    }

                    Spinner villageSpinner = (Spinner) paramView.findViewById(R.id.FRLd_village_Sp);
                    String landVil = villageSpinner.getSelectedItem().toString().trim();

                    if (landVil.equalsIgnoreCase("--Select--")) {
                        showAlertError(getString(R.string.sel_land_vil));
                        return false;
                    } else {

                        for (VillageEntity villageEntity : villageEntities) {
                            if (villageEntity.getDistrictID().equalsIgnoreCase(landDistID)
                                    && villageEntity.getMandalID().equalsIgnoreCase(landManID)
                                    && villageEntity.getVillageName().equalsIgnoreCase(landVil)) {
                                landVilID = villageEntity.getVillageID();
                                break;
                            }
                        }

                        if (landVilID != null) {
                            farmerLandXMLData.setVillageID(landVilID);
                        }
                    }

                    EditText serveyNoEt = (paramView.findViewById(R.id.FRLd_surveyNo_Et));
                    String surveyNo = serveyNoEt.getText().toString().trim();

                    if (TextUtils.isEmpty(surveyNo)) {
                        setEditTextData(serveyNoEt, getString(R.string.sur_num));
                        return false;
                    }

                    if (surveyNo.contains("&")) {
                        setEditTextData(serveyNoEt, getString(R.string.sur_spl));
                        return false;
                    } else {
                        farmerLandXMLData.setSurveyNo(surveyNo);
                    }

                    EditText khattaNoEt = (paramView.findViewById(R.id.FRLd_khattaNo_Et));
                    String passbookNo = khattaNoEt.getText().toString().trim();

                    if (TextUtils.isEmpty(passbookNo)) {
                        setEditTextData(khattaNoEt, getString(R.string.pass_num));
                        return false;
                    }

                    if (passbookNo.length() <= 0) {
                        setEditTextData(khattaNoEt, getString(R.string.valid_pass_num));
                        return false;
                    } else {
                        farmerLandXMLData.setPassBookNo(passbookNo);
                    }

                    EditText landAcersEt = (paramView.findViewById(R.id.FRLd_landAcers_Et));

                    if (TextUtils.isEmpty(landAcersEt.getText().toString().trim())) {
                        setEditTextData(landAcersEt, getString(R.string.enter_land_rec));
                        return false;
                    }

                    if (landAcersEt.getText().toString().trim().equalsIgnoreCase(".")) {
                        setEditTextData(landAcersEt, getString(R.string.total_land_invalid));
                        return false;
                    }

                    float landAcres = Float.parseFloat(landAcersEt.getText().toString().trim());
                    if (landAcres == 0) {
                        setEditTextData(landAcersEt, getString(R.string.land_sh_not_zero));
                        return false;
                    } else {
                        farmerLandXMLData.setTotalArea(String.valueOf(landAcres));
                    }

                    Spinner landTypeSp = paramView.findViewById(R.id.FRLd_landType_Sp);
                    if (landTypeSp.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                        showAlertError(getString(R.string.sel_land_type));
                        return false;
                    } else {
                        farmerLandXMLData.setLandTypeId(String.valueOf(landTypeSp.getSelectedItemPosition()));
                    }

                    EditText totalLeaseExtentEt = (paramView.findViewById(R.id.FRLd_LeasedExtent_Et));
                    String totalLeaseExtent = totalLeaseExtentEt.getText().toString().trim();

                    if (TextUtils.isEmpty(totalLeaseExtent)) {
                        setEditTextData(totalLeaseExtentEt, getString(R.string.ent_padd_ext));
                        return false;
                    } else if (totalLeaseExtent.equalsIgnoreCase(".")) {
                        setEditTextData(totalLeaseExtentEt, getString(R.string.leas_invalid));
                        return false;
                    }

                    EditText totalCulExtentEt = (paramView.findViewById(R.id.FRLd_cultivatedLand_Et));
                    String totalCulExtent = totalCulExtentEt.getText().toString().trim();

                    if (TextUtils.isEmpty(totalCulExtent)) {
                        setEditTextData(totalCulExtentEt, getString(R.string.ent_cul_land));
                        return false;
                    } else if (totalCulExtent.equalsIgnoreCase(".")) {
                        setEditTextData(totalCulExtentEt, getString(R.string.cul_invalid));
                        return false;
                    }

                    float cultivatedextent = Float.parseFloat(totalCulExtentEt.getText().toString().trim());
                    if (cultivatedextent == 0) {
                        setEditTextData(totalCulExtentEt, getString(R.string.cul_0));
                        return false;
                    }


                    Float totArea = landAcres;
                    Float alreadyLeasedLand = Float.valueOf(alrLeasedExt.getText().toString());
                    Float alreadyCulLand = Float.valueOf(alrCulExt.getText().toString());
                    Float curLeaseLand = Float.valueOf(totalLeaseExtent);
                    Float curCulLand = cultivatedextent;


                    Float valTest = Float.valueOf(String.format("%.2f", alreadyLeasedLand + alreadyCulLand));

                    Float finalVal = totArea - (alreadyLeasedLand + alreadyCulLand + curLeaseLand + curCulLand);
                    finalVal = Float.valueOf(String.format("%.2f", finalVal));

                    if (valTest >= totArea) {
                        setEditTextData(totalCulExtentEt, getString(R.string.land_ext_not));
                        return false;
                    } else if ((finalVal < 0) || finalVal > totArea) {
                        setEditTextData(totalCulExtentEt, getString(R.string.land_ext_not));
                        return false;
                    } else {
                        farmerLandXMLData.setTotalLeaseArea(String.valueOf(curLeaseLand));
                        farmerLandXMLData.setTotalCultivableArea(String.valueOf(curCulLand));
                    }


                    farmerLandXMLData.setLandCreatedDate(createdDate);
                    farmerLandXMLDataArrayList.add(farmerLandXMLData);

                    finalProcVal += curCulLand;
                    finalProcVal = Double.valueOf(String.format("%.2f", finalProcVal));
                }
            }
        } else {
            showAlertError(getString(R.string.no_land));
            return false;
        }


        if (!binding.FRPaddyDetailsCb.isChecked()) {
            showAlertError(getString(R.string.sel_paddy_det));
            return false;
        }

        String PaddyType = binding.FRPaddyTypeSp.getSelectedItem().toString();
        if (PaddyType.equalsIgnoreCase("--Select--")) {
            showAlertError(getString(R.string.sel_pad_type));
            return false;
        } else {
            int paddyTypeID = binding.FRPaddyTypeSp.getSelectedItemPosition();
            farmerXMLData.setApproximatePaddyType(String.valueOf(paddyTypeID));
        }


        String tokenNo = binding.FRTokenNoSp.getSelectedItem().toString().trim();
        if (tokenNo.equalsIgnoreCase("--Select--")) {
            showAlertError(getString(R.string.sel_token_num));
            return false;
        }

        String tokenFarName = getTokenFarmerName(tokenNo, getTokenDropDownDataResponseMain);
        if (!tokenFarName.equalsIgnoreCase(farmerName)) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.FarmerRegistration), getString(R.string.far_as_per),
                    getString(R.string.INFORMATION), false);

            setEditTextData(binding.FRFarmerNameEt, getString(R.string.far_as_per));
            return false;
        }

        String tokenFarMob = getTokenFarmerMob(tokenNo, getTokenDropDownDataResponseMain);
        if (!tokenFarMob.equalsIgnoreCase(mobNum)) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.FarmerRegistration), getString(R.string.mob_as_per),
                    getString(R.string.INFORMATION), false);

            setEditTextData(binding.FRAddMobileEt, getString(R.string.mob_as_per));
            return false;
        }


        String bankpassbook_IsSubmited, PattadharPassBook_IsSubmited, VROCertified_IsSubmited;

        if (!binding.FRBankPassBookCB.isChecked()) {
            showAlertError(getString(R.string.sub_pass));
            return false;
        } else {
            bankpassbook_IsSubmited = "Y";
            farmerXMLData.setBankPassBook(bankpassbook_IsSubmited);
        }


        if (!binding.FRPattadharPassBookCB.isChecked()) {
            showAlertError(getString(R.string.sub_patt));
            return false;
        } else {
            PattadharPassBook_IsSubmited = "Y";
            farmerXMLData.setPattadharPassbook(PattadharPassBook_IsSubmited);
        }

        if (!binding.FRVROCertifiedCB.isChecked()) {
            showAlertError(getString(R.string.sub_vro));
            return false;
        } else {
            VROCertified_IsSubmited = "Y";
            farmerXMLData.setVROCertificate(VROCertified_IsSubmited);
        }


        farmerXMLData.setTokenNo(tokenNo);
        long tokenID = getTokenID(tokenNo, getTokenDropDownDataResponseMain);
        farmerXMLData.setTokenID(String.valueOf(tokenID));

        if (farmerLandXMLDataArrayList.size() > 0) {
            for (int x = 0; x < farmerLandXMLDataArrayList.size(); x++) {
                farmerLandXMLDataArrayList.get(x).setTokenId(String.valueOf(tokenID));
            }
        }


        farmerSubmitRequest.setFarmerXMLData(farmerXMLData);
        farmerSubmitRequest.setLandXMLDataArrayList(farmerLandXMLDataArrayList);


        return true;
    }

    private void showAlertError(String errorMsg) {
        Utils.customAlert(getActivity(),
                getResources().getString(R.string.FarmerRegistration),
                errorMsg,
                getString(R.string.INFORMATION), false);
    }


    private String getTokenFarmerMob(String tokenNo, GetTokenDropDownDataResponse getTokenDropDownDataResponse) {
        String tokenFarMob = "";
        for (int i = 0; i < getTokenDropDownDataResponse.getGetTokenDropDownData().size(); i++) {
            if (getTokenDropDownDataResponse.getGetTokenDropDownData().get(i).getTokenNo().equals(tokenNo)) {
                tokenFarMob = getTokenDropDownDataResponse.getGetTokenDropDownData().get(i).getMobile();
                break;
            }
        }
        return tokenFarMob;
    }

    private long getTokenID(String tokenNo, GetTokenDropDownDataResponse getTokenDropDownDataResponse) {
        long tokenID = -1;
        if (getTokenDropDownDataResponse != null &&
                getTokenDropDownDataResponse.getGetTokenDropDownData() != null &&
                getTokenDropDownDataResponse.getGetTokenDropDownData().size() > 0) {
            for (int i = 0; i < getTokenDropDownDataResponse.getGetTokenDropDownData().size(); i++) {
                if (getTokenDropDownDataResponse.getGetTokenDropDownData().get(i).getTokenNo().equals(tokenNo)) {
                    tokenID = getTokenDropDownDataResponse.getGetTokenDropDownData().get(i).getTokenID();
                    break;
                }
            }
        }
        return tokenID;
    }


    private String getTokenFarmerName(String tokenNo, GetTokenDropDownDataResponse getTokenDropDownDataResponse) {
        String tokenFarName = "";
        if (getTokenDropDownDataResponse != null && getTokenDropDownDataResponse.getGetTokenDropDownData() != null
                && getTokenDropDownDataResponse.getGetTokenDropDownData().size() > 0) {
                for (int i = 0; i < getTokenDropDownDataResponse.getGetTokenDropDownData().size(); i++) {
                if (getTokenDropDownDataResponse.getGetTokenDropDownData().get(i).getTokenNo().equals(tokenNo)) {
                    tokenFarName = getTokenDropDownDataResponse.getGetTokenDropDownData().get(i).getFarmerName();
                    break;
                }
            }
        }
        return tokenFarName;
    }


    private void setEditTextData(View view, String errorMsg) {
        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            editText.requestFocus();
            editText.setError(errorMsg);
            binding.scMain.scrollTo(0, view.getBottom());
        }

        if (view instanceof AutoCompleteTextView) {
            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view;
            autoCompleteTextView.requestFocus();
            autoCompleteTextView.setError(errorMsg);
            binding.scMain.scrollTo(0, view.getBottom());
        }


    }

    private void callFarUpdateFragment(Fragment fragment, String name) {

        try {
            SharedPreferences.Editor editor = OPMSApplication.get(getActivity()).getPreferencesEditor();
            Gson gson = OPMSApplication.get(getActivity()).getGson();
            editor.putString(AppConstants.FARMER_DATA, gson.toJson(fRes));
            editor.putString(AppConstants.FARMER_AADHAR, binding.FRSearchdataEt.getText().toString().trim());
            editor.commit();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.dashboard_container, fragment, name);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllBanks(List<BankEntity> bankEntities) {
        try {
            if (bankEntities!=null && bankEntities.size() > 0) {
                this.bankEntities = bankEntities;
                bankStringList = new ArrayList<>();

                for (BankEntity bankEntity : bankEntities) {
                    bankStringList.add(bankEntity.getBankName());
                }

                Utils.loadAutoCompleteTextData(getActivity(),
                        bankStringList,
                        binding.FRBankNameSp);

                if (bankStringList.size() > 0) {
                    bbRepository.getAllBranches(this);
                } else {

                    if (customProgressDialog != null && customProgressDialog.isShowing())
                        customProgressDialog.dismiss();
                    Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                            getResources().getString(R.string.FarmerRegistration),
                            getString(R.string.no_banks), true);


                }
            } else {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                        getResources().getString(R.string.FarmerRegistration),
                        getString(R.string.no_banks), true);

            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllDistrictEntities(List<DistrictEntity> districtEntities) {
        try {
            if (districtEntities!=null&& districtEntities.size() > 0) {
                this.districtEntities = districtEntities;
                disStringListMain = new ArrayList<>();
                disStringListMain.add(0, "--Select--");

                for (DistrictEntity districtEntity : districtEntities) {
                    disStringListMain.add(districtEntity.getDistName());
                }


                if (disStringListMain.size() > 0) {
                    dmvRepository.getAllMandals(this);
                } else {
                    if (customProgressDialog != null && customProgressDialog.isShowing())
                        customProgressDialog.dismiss();
                    Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                            getResources().getString(R.string.FarmerRegistration),
                            getString(R.string.no_districts), true);
                }
            } else {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                        getResources().getString(R.string.FarmerRegistration),
                        getString(R.string.no_districts), true);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void getAllMandalEntities(List<MandalEntity> mandalEntities) {
        try {
            if (mandalEntities!=null && mandalEntities.size() > 0) {
                this.mandalEntities = mandalEntities;
                manStringListMain = new ArrayList<>();
                manStringListMain.add(0, "--Select--");

                for (MandalEntity mandalEntity : mandalEntities) {
                    manStringListMain.add(mandalEntity.getMandalName());
                }

                if (manStringListMain.size() > 0) {
                    dmvRepository.getAllVillages(this);
                } else {
                    if (customProgressDialog != null && customProgressDialog.isShowing())
                        customProgressDialog.dismiss();
                    Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                            getResources().getString(R.string.FarmerRegistration),
                            getString(R.string.no_mandals), true);
                }
            } else {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                        getResources().getString(R.string.FarmerRegistration),
                        getString(R.string.no_mandals), true);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void getAllVillageEntities(List<VillageEntity> villageEntities) {
        try {
            if (villageEntities!=null && villageEntities.size() > 0) {
                this.villageEntities = villageEntities;
                vilStringListMain = new ArrayList<>();
                vilStringListMain.add(0, "--Select--");

                for (VillageEntity villageEntity : villageEntities) {
                    vilStringListMain.add(villageEntity.getVillageName());
                }


                if (vilStringListMain.size() > 0) {
                    bbRepository.getAllBanks(this);
                } else {
                    if (customProgressDialog != null && customProgressDialog.isShowing())
                        customProgressDialog.dismiss();
                    Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                            getResources().getString(R.string.FarmerRegistration),
                            getString(R.string.no_villages), true);
                }


            } else {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                        getResources().getString(R.string.FarmerRegistration),
                        getString(R.string.no_villages), true);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void getAllSocialEntities(List<SocialEntity> socialEntities) {
        try {
            if (socialEntities!=null && socialEntities.size() > 0) {
                this.socialEntities = socialEntities;
                socialStringListMain = new ArrayList<>();
                socialStringListMain.add(0, "--Select--");

                for (SocialEntity socialEntity : socialEntities) {
                    socialStringListMain.add(socialEntity.getSocialStatus());
                }

                if (socialStringListMain.size() <= 0) {
                    if (customProgressDialog != null && customProgressDialog.isShowing())
                        customProgressDialog.dismiss();
                    Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                            getResources().getString(R.string.FarmerRegistration),
                            getString(R.string.no_social), true);
                } else {

                    if (AppConstants.IS_CANCELLED) {
                        try {
                            if (customProgressDialog != null)
                                customProgressDialog.show();
                            AppConstants.IS_CANCELLED = false;
                            Gson gson = OPMSApplication.get(getActivity()).getGson();
                            String farmerDetails = sharedPreferences.getString(AppConstants.FARMER_DATA, "");
                            String farmerAadhar = sharedPreferences.getString(AppConstants.FARMER_AADHAR, "");
                            binding.FRSearchdataEt.setText(farmerAadhar);
                            enteredAadhar = binding.FRSearchdataEt.getText().toString().trim();
                            fRes = gson.fromJson(farmerDetails, FarmerDetailsResponse.class);
                            if (fRes != null) {
                                getFarmerRegResponse(fRes);
                            } else {
                                //something wrong
                            }

                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    if (AppConstants.IS_UPDATED) {
                        AppConstants.IS_UPDATED = false;
                        String farmerAadhar = sharedPreferences.getString(AppConstants.FARMER_AADHAR, "");
                        if (!TextUtils.isEmpty(farmerAadhar)) {
                            binding.FRSearchdataEt.setText(farmerAadhar);
                            binding.FRSearchTv.callOnClick();
                        }
                    }
                }
            } else {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                        getResources().getString(R.string.FarmerRegistration),
                        getString(R.string.no_social), true);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getGeneratedTokens() {
        GetTokensStage1Request getTokensStage1Request = new GetTokensStage1Request();
        getTokensStage1Request.setAuthenticationID(ppcUserDetails.getAuthenticationID());
        getTokensStage1Request.setpPCID(String.valueOf(ppcUserDetails.getPPCID()));
        getTokensStage1Request.setRegistrationNo(enteredAadhar);
        if (customProgressDialog != null)
            customProgressDialog.show();
        farRegPresenter.GetFarmerGeneratedTokens(getTokensStage1Request);
    }


    @Override
    public void onDataNotAvailable() {

    }

    @Override
    public void distCount(int cnt) {

    }

    @Override
    public void mandalCount(int cnt) {

    }

    @Override
    public void villageCount(int cnt) {

    }

    @Override
    public void bankCount(int cnt) {

    }

    @Override
    public void branchCount(int cnt) {

    }


    @Override
    public void getAllMandalNamesByDist(List<String> mandalEntities, Spinner spinner, Spinner vSpinner, String distName) {
        try {
            if (!distName.equalsIgnoreCase("--Select--")) {

                Utils.loadSpinnerSetSelectedItem(getActivity(), mandalEntities,
                        spinner, f_mandal);
            } else {
                Utils.loadSpinnerSetSelectedItem(getActivity(), mandalEntities, spinner, f_mandal);
                Utils.loadSpinnerSetSelectedItem(getActivity(), mandalEntities, vSpinner, f_village);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getAllLandMandalNamesByDist(List<String> mandalEntities, Spinner spinner, Spinner vSpinner, String distName) {
        if (!distName.equalsIgnoreCase("--Select--")) {

            Utils.loadSpinnerSetSelectedItem(getActivity(), mandalEntities,
                    spinner, f_mandal);
        } else {
            Utils.loadSpinnerSetSelectedItem(getActivity(), mandalEntities, spinner, f_mandal);
            Utils.loadSpinnerSetSelectedItem(getActivity(), mandalEntities, vSpinner, f_village);
        }
    }

    @Override
    public void getAllVillagesByMan(List<String> villageEntities, Spinner spinner, String distName, String manName) {

        try {
            if (!distName.equalsIgnoreCase("--Select--") && !manName.equalsIgnoreCase("--Select--")) {

                Utils.loadSpinnerSetSelectedItem(getActivity(), villageEntities,
                        spinner, f_village);
            } else {
                Utils.loadSpinnerSetSelectedItem(getActivity(), villageEntities, spinner, f_village);
            }

            if (!manName.equalsIgnoreCase("--Select--")) {
                Utils.loadSpinnerSetSelectedItem(getActivity(), villageEntities, spinner, f_village);
            } else {
                Utils.loadSpinnerSetSelectedItem(getActivity(), villageEntities, spinner, f_village);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void getAllBranches(List<BranchEntity> branchEntities) {

        try {
            if (branchEntities!=null && branchEntities.size() > 0) {
                this.branchEntities = branchEntities;
                branchStringList = new ArrayList<>();

                for (BranchEntity branchEntity : branchEntities) {
                    branchStringList.add(branchEntity.getBranchName());
                }

                Utils.loadAutoCompleteTextData(getActivity(),
                        branchStringList,
                        binding.FRBankBranchNameSp);

                if (branchStringList.size() > 0) {
                    dmvRepository.getAllSocialStatus(this);
                } else {
                    Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                            getResources().getString(R.string.FarmerRegistration),
                            getString(R.string.no_branches), true);
                }
            } else {
                Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                        getResources().getString(R.string.FarmerRegistration),
                        getString(R.string.no_branches), true);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getBankName(String bankName) {
        try {
            binding.FRBankNameSp.setText(bankName);
            bbRepository.getAllBanks(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getBranchName(String branchName) {
        try {
            binding.FRBankBranchNameSp.setText(branchName);
            bbRepository.getAllBranches(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void socialCount(int cnt) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        try {
            switch (parent.getId()) {
                case R.id.FR_AddDistrict_Sp:
                    try {
                        String districtName = parent.getSelectedItem().toString().trim();
                        if (!districtName.equalsIgnoreCase("--Select--")) {

                            List<String> mandalsStringList = new ArrayList<>();
                            mandalsStringList.add(0, "--Select--");

                            for (DistrictEntity districtEntity : districtEntities) {
                                if (districtEntity.getDistName().equalsIgnoreCase(districtName)) {
                                    String distID = districtEntity.getDistId();


                                    for (MandalEntity mandalEntity : mandalEntities) {
                                        if (mandalEntity.getDistrictID().equalsIgnoreCase(distID)) {
                                            mandalsStringList.add(mandalEntity.getMandalName());
                                        }


                                    }
                                }
                            }
                            if (fRes.getFarmerBasicDetail() != null) {
                                setBasicDistData(fRes, binding.FRAddMandalSp, mandalsStringList);
                            } else {

                                Utils.loadSpinnerSetSelectedItem(getActivity(), mandalsStringList, binding.FRAddMandalSp, f_mandal);
                            }
                        } else {
                            Utils.loadSpinnerSetSelectedItem(getActivity(), emptyList, binding.FRAddMandalSp, f_mandal);
                            Utils.loadSpinnerSetSelectedItem(getActivity(), emptyList, binding.FRAddVillageSp, f_village);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case R.id.FR_AddMandal_Sp:

                    try {
                        String districtName = binding.FRAddDistrictSp.getSelectedItem().toString().trim();
                        String mandalName = parent.getSelectedItem().toString().trim();

                        if (!mandalName.equalsIgnoreCase("--Select--")) {

                            String distId = null, manId = null;
                            List<String> villagesStringList = new ArrayList<>();
                            villagesStringList.add(0, "--Select--");

                            for (DistrictEntity districtEntity : districtEntities) {
                                if (districtEntity.getDistName().equalsIgnoreCase(districtName)) {
                                    distId = districtEntity.getDistId();
                                }
                            }
                            for (MandalEntity mandalEntity : mandalEntities) {
                                if (mandalEntity.getDistrictID().equalsIgnoreCase(distId) &&
                                        mandalEntity.getMandalName().equalsIgnoreCase(mandalName)) {
                                    manId = mandalEntity.getMandalID();
                                }
                            }

                            if (distId != null && manId != null) {
                                for (VillageEntity villageEntity : villageEntities) {
                                    if (villageEntity.getDistrictID().equalsIgnoreCase(distId)
                                            &&
                                            villageEntity.getMandalID().equalsIgnoreCase(manId)) {
                                        villagesStringList.add(villageEntity.getVillageName());
                                    }

                                }
                            }
                            if (fRes.getFarmerBasicDetail() != null) {
                                setBasicVilData(fRes, binding.FRAddVillageSp, villagesStringList);
                            } else {
                                Utils.loadSpinnerSetSelectedItem(getActivity(), villagesStringList, binding.FRAddVillageSp, f_village);
                            }


                        } else {
                            Utils.loadSpinnerSetSelectedItem(getActivity(), emptyList, binding.FRAddVillageSp, f_village);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case R.id.FR_tokenNo_sp:
                    try {

                        binding.farTokenNoTv.setText("");
                        binding.farFarmerNameTv.setText("");
                        binding.farMobileTv.setText("");
                        binding.farAppTv.setText("");

                        if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                            String tokenNum = parent.getSelectedItem().toString().trim();
                            if (!tokenNum.equalsIgnoreCase("--Select--")) {

                                if (getTokensFarmerResponseMain != null &&
                                        getTokensFarmerResponseMain.getStage1GetTokensddls() != null &&
                                        getTokensFarmerResponseMain.getStage1GetTokensddls().size() > 0) {
                                    long tokenId = -1;
                                    for (int z = 0; z < getTokensFarmerResponseMain.getStage1GetTokensddls().size(); z++) {
                                        if (getTokensFarmerResponseMain.getStage1GetTokensddls().get(z).getTokenNo().trim()
                                                .equals(tokenNum.trim())) {
                                            tokenId = getTokensFarmerResponseMain.getStage1GetTokensddls().get(z).getTokenID();
                                            break;
                                        }
                                    }

                                    if (tokenId != -1) {
                                        GetTokensRequest getTokensRequest = new GetTokensRequest();
                                        getTokensRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                                        getTokensRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));
                                        getTokensRequest.setTokenID(tokenId);

                                        if (customProgressDialog != null)
                                            customProgressDialog.show();
                                        farRegPresenter.GetTokenDropDownData(getTokensRequest);
                                    } else {
                                        Utils.customAlert(getActivity(),
                                                getResources().getString(R.string.FarmerRegistration),
                                                getResources().getString(R.string.something),
                                                getResources().getString(R.string.WARNING), false);
                                    }
                                }

                            } else {
                                binding.mainFarDet.setVisibility(View.GONE);
                            }

                        } else {
                            binding.mainFarDet.setVisibility(View.GONE);
                            Utils.customAlert(getActivity(),
                                    getResources().getString(R.string.FarmerRegistration),
                                    getResources().getString(R.string.no_internet),
                                    getResources().getString(R.string.WARNING), false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onError(int code, String msg) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();

        if (code == AppConstants.ERROR_CODE) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.FarmerRegistration),
                    msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else if (code == AppConstants.EXCEPTION_CODE) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.FarmerRegistration),
                    getString(R.string.something) +
                            msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.FarmerRegistration),
                    getString(R.string.something),
                    getResources().getString(R.string.ERROR),
                    false);
        }
    }

    private class CustomTextWatcher implements TextWatcher {
        private EditText mEditText;

        CustomTextWatcher(EditText e) {
            mEditText = e;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                switch (mEditText.getId()) {
                    case R.id.FR_BankACNo_Et:
                        binding.FRBankACNoCFMEt.setText("");
                        break;
                    case R.id.FR_searchdata_Et:
                        if (s.toString().length() != 12) {
                            binding.FRMainLl.setVisibility(View.GONE);
                            binding.mainFarDet.setVisibility(View.GONE);

                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void afterTextChanged(Editable s) {
        }
    }

    public class AutoCompleteTVListener implements AdapterView.OnItemClickListener {

        AutoCompleteTextView ac;

        AutoCompleteTVListener(AutoCompleteTextView myAc) {
            ac = myAc;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            try {
                switch (ac.getId()) {
                    case R.id.FR_BankName_Sp:
                        binding.FRBankBranchNameSp.setText("");
                        binding.FRBankIFSCcodeEt.setText("");
                        String bankName = binding.FRBankNameSp.getText().toString();
                        if (!TextUtils.isEmpty(bankName)) {

                            try {

                                List<String> branchStringList = new ArrayList<>();

                                for (BankEntity bankEntity : bankEntities) {
                                    if (bankEntity.getBankName().equalsIgnoreCase(bankName)) {
                                        Integer bankID = bankEntity.getBankID();


                                        for (BranchEntity branchEntity : branchEntities) {
                                            if (branchEntity.getBankID().equals(bankID)) {
                                                branchStringList.add(branchEntity.getBranchName());
                                            }


                                        }
                                    }
                                }
                                Utils.loadAutoCompleteTextData(getActivity(),
                                        branchStringList,
                                        binding.FRBankBranchNameSp);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        break;

                    case R.id.FR_BankBranchName_Sp:
                        try {
                            binding.FRBankIFSCcodeEt.setText("");
                            String bankNameSel = binding.FRBankNameSp.getText().toString();
                            String branchName = binding.FRBankBranchNameSp.getText().toString();

                            Integer bankID = null, branchId = null;

                            for (BankEntity bankEntity : bankEntities) {
                                if (bankEntity.getBankName().equalsIgnoreCase(bankNameSel)) {
                                    bankID = bankEntity.getBankID();
                                }
                            }
                            for (BranchEntity branchEntity : branchEntities) {
                                if (branchEntity.getBankID().equals(bankID) &&
                                        branchEntity.getBranchName().equalsIgnoreCase(branchName)) {
                                    branchId = branchEntity.getBranchID();
                                }
                            }

                            if (bankID != null && branchId != null) {
                                for (BranchEntity branchEntity : branchEntities) {
                                    if (branchEntity.getBankID().equals(bankID)
                                            &&
                                            branchEntity.getBranchID().equals(branchId)) {
                                        binding.FRBankIFSCcodeEt.setText(branchEntity.getIFSC());
                                    }

                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private int year;
    private int month;
    private int day;


    private void setEditTextNE(ViewGroup group) {
        try {
            for (int i = 0, count = group.getChildCount(); i < count; ++i) {
                View view = group.getChildAt(i);
                if (view instanceof EditText) {
                    if (view.getId() != R.id.FR_aadhaarNo_Et) {
                        ((EditText) view).setEnabled(false);
                    } else {
                        binding.FRAadhaarNoEt.setText(enteredAadhar);
                    }

                    if (view.getId() == R.id.FR_AproxQty_Et || view.getId() == R.id.FR_BankACNo_CFM_Et) {
                        ((EditText) view).setEnabled(true);
                    } else {
                        ((EditText) view).setEnabled(false);
                    }

                }

                if (view instanceof Spinner) {
                    if (view.getId() == R.id.FR_tokenNo_sp || view.getId() == R.id.FR_paddyType_Sp) {
                        ((Spinner) view).setEnabled(true);
                    } else {
                        ((Spinner) view).setEnabled(false);
                    }
                }

                if (view instanceof RadioButton) {
                    ((RadioButton) view).setEnabled(false);
                }

                if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                    setEditTextNE((ViewGroup) view);
            }


            binding.FRPaddyTypeSp.setSelection(0);
            binding.FRTokenNoSp.setSelection(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void clearForm(ViewGroup group) {
        try {
            for (int i = 0, count = group.getChildCount(); i < count; ++i) {
                View view = group.getChildAt(i);
                if (view instanceof EditText) {
                    if (view.getId() != R.id.FR_aadhaarNo_Et) {
                        ((EditText) view).setText("");
                        ((EditText) view).setError(null);
                    } else {
                        binding.FRAadhaarNoEt.setText(enteredAadhar);
                    }
                }

                if (view instanceof TextView) {
                    if (view.getId() == R.id.far_tokenNo_Tv
                            ||
                            view.getId() == R.id.far_farmerName_Tv
                            ||
                            view.getId() == R.id.far_mobile_Tv
                            ||
                            view.getId() == R.id.far_app_Tv) {
                        ((TextView) view).setText("");
                    }

                }
                if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                    clearForm((ViewGroup) view);
            }
            binding.FRPaddyTypeSp.setSelection(0);
            binding.FRTokenNoSp.setSelection(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Dialog dialog;

    private void TokenGenerateAlert() {
        try {
            if (getActivity() != null) {
                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                    dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    dialog.setContentView(R.layout.token_generate);
                    dialog.setCancelable(false);
                    final EditText name = dialog.findViewById(R.id.tokenIssused_farmerName_Et);
                    final EditText mob = dialog.findViewById(R.id.tokenIssused_mobileNo);
                    final TextView date = dialog.findViewById(R.id.tokenIssused_queuedate_tv);
                    final Button submit = dialog.findViewById(R.id.tokkenIssused_Submit_Btn);
                    final Button cancel = dialog.findViewById(R.id.cancel);

                    name.setText(binding.FRFarmerNameEt.getText().toString().trim());
                    mob.setText(binding.FRAddMobileEt.getText().toString().trim());

                    final Calendar c = Calendar.getInstance();
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);
                    date.setText(year + "-" + (month + 1) + "-" + day);

                    date.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDatePickerDialog(view);
                        }
                    });

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean isAutomatic = Utils.isTimeAutomatic(getActivity());
                            if (isAutomatic) {

                                if (validateTokenData(name, mob)) {

                                    String farmerName = name.getText().toString().trim();
                                    String farmerMob = mob.getText().toString().trim();
                                    String farmerAttDate = date.getText().toString().trim();
                                    String createdDate = Utils.getCurrentDateTime();
                                    String appointmentDate = Utils.getFarmerAppointmentDateToPass(farmerAttDate);

                                    String tokenStr = getTokenData(farmerName, farmerMob, createdDate, appointmentDate);

                                    TokenGenerationRequest tokenGenerationRequest = new TokenGenerationRequest();
                                    tokenGenerationRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));
                                    tokenGenerationRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                                    tokenGenerationRequest.setXmlFarmerTokenData(tokenStr);
                                    if (customProgressDialog != null)
                                        customProgressDialog.show();
                                    farRegPresenter.GenerateToken(tokenGenerationRequest, dialog);

                                }
                            }
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dialog.isShowing())
                                dialog.dismiss();
                        }
                    });
                    if (!dialog.isShowing())
                        dialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showDatePickerDialog(final View v) {
        try {
            if (getActivity() != null) {
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        ((TextView) v).setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, year, month, day);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean validateTokenData(EditText name, EditText mob) {
        if (name.getText().toString().trim().length() == 0) {
            name.setError(getString(R.string.far_name_required));
            name.requestFocus();
            return false;
        }

        if (mob.getText().toString().trim().length() == 0) {
            mob.setError(getString(R.string.far_mob_num_req));
            mob.requestFocus();
            return false;
        }

        if (!mob.getText().toString().trim().matches("[6-9]{1}+[0-9]+") || mob.getText().toString().trim().length() < 10) {
            mob.setError(getString(R.string.valid_far_mob_num_req));
            mob.requestFocus();
            return false;
        }
        return true;
    }

    private String getTokenData(String farName, String farMob, String creDate, String appDate) {
        return "<FarmerTokenDetails>" +
                "<ppc>" +
                "<ppccode>" + ppcUserDetails.getPPCCode() + "</ppccode>" + "<ppcid>" + ppcUserDetails.getPPCID() + "</ppcid>" + "<SystemIP>" + ppcUserDetails.getPPCCode() + "</SystemIP>" + "<seasonID>" + ppcUserDetails.getSeasonID() + "</seasonID>" +
                "</ppc>" +
                "<Token>" +
                "<FarmerName>" + farName + "</FarmerName>" + "<Mobile>" + farMob + "</Mobile>" + "<AppointmentDate>" + appDate + "</AppointmentDate>" + "<CreatedDate>" + creDate + "</CreatedDate>"
                + "<RegistrationNo>" + enteredAadhar + "</RegistrationNo>"
                + "</Token>" +
                "</FarmerTokenDetails>";
    }

    @SuppressLint("StaticFieldLeak")
    private class printAsyncTask extends AsyncTask<Void, Void, Boolean> {
        TokenGenerateResponse tokenGenerateResponse;
        Bitmap bmp = null;

        printAsyncTask(TokenGenerateResponse tokenGenerateResponse) {
            this.tokenGenerateResponse = tokenGenerateResponse;
            try {
                if (getActivity() != null) {
                    InputStream is = getActivity().getAssets().open("telanganalogo.bmp");
                    bmp = BitmapFactory.decodeStream(is);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (customProgressDialog != null &&
                    !customProgressDialog.isShowing())
                customProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... args) {
            boolean flag = false;
            try {
                flag = connectBT();
                if (flag) {
                    aPrinter.logoPrinting(AppConstants.BT_ADDRESS, bmp);
                    char nLine = '\n';
                    String str = "Token Receipt" + nLine +
                            "----------------------" + nLine +
                            "పిపిసి కోడ్: " + ppcUserDetails.getPPCCode() + nLine +
                            "టోకెన్ ఇచ్చిన  తేది : " + tokenGenerateResponse.getTokenDetails().get(0).getCreatedDate() + nLine +
                            "రైతు రావాల్సిన తేది: " + tokenGenerateResponse.getTokenDetails().get(0).getAppointmentDate() + nLine +
                            "రైతు పేరు: " + tokenGenerateResponse.getTokenDetails().get(0).getFarmerName() + nLine +
                            "ఫోన్ నంబరు: " + tokenGenerateResponse.getTokenDetails().get(0).getMobile() + nLine +
                            "టోకెన్ నంబరు: " + tokenGenerateResponse.getTokenDetails().get(0).getTokenNo() + nLine;

                    aPrinter.multiLingualPrint(AppConstants.BT_ADDRESS, str, 22, Typeface.SANS_SERIF);
                    Bluetooth_Printer_2inch_ThermalAPI printer = new Bluetooth_Printer_2inch_ThermalAPI();
                    aPrinter.printData(printer.Reset());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return flag;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (customProgressDialog != null && customProgressDialog.isShowing()) {
                customProgressDialog.dismiss();
            }
            if (!aBoolean) {
                Toast.makeText(getActivity(), "Failed to give token print", Toast.LENGTH_SHORT).show();
            }

            try {
                aPrinter.closeBT();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        try {
            if (customProgressDialog != null) {
                customProgressDialog = null;
            }
            if (farRegPresenter != null) {
                farRegPresenter.detachView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlertNavigateToFarmerUpdate(String alertMsg) {
        try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_alert_information);
                dialog.setCancelable(false);

                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(getResources().getString(R.string.app_name) + ": " + getResources().getString(R.string.FarmerRegistration));
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(alertMsg);

                Button yes = dialog.findViewById(R.id.btDialogYes);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Fragment fragment = new FarUpdateFragment();
                        String fragmentTag = FarUpdateFragment.class.getSimpleName();
                        AppConstants.FARG_TAG = fragmentTag;
                        callFarUpdateFragment(fragment, fragmentTag);
                    }
                });

                Button cancel = dialog.findViewById(R.id.btDialogNo);
                cancel.setVisibility(View.VISIBLE);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if (!dialog.isShowing())
                    dialog.show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }
}


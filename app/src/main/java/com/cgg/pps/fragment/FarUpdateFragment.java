package com.cgg.pps.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.FarmerUpdateFragBinding;
import com.cgg.pps.interfaces.BBInterface;
import com.cgg.pps.interfaces.DMVInterface;
import com.cgg.pps.interfaces.FarRegInterface;
import com.cgg.pps.interfaces.FarUpdateInterface;
import com.cgg.pps.model.request.farmer.farmerUpdate.FarmerUpdateLandXMLData;
import com.cgg.pps.model.request.farmer.farmerUpdate.FarmerUpdateRequest;
import com.cgg.pps.model.request.farmer.farmerUpdate.FarmerUpdateXMLData;
import com.cgg.pps.model.request.farmer.farmerdetails.FarmerDetailsRequest;
import com.cgg.pps.model.response.devicemgmt.bankbranch.BranchEntity;
import com.cgg.pps.model.response.devicemgmt.masterbank.BankEntity;
import com.cgg.pps.model.response.devicemgmt.masterdistrict.DistrictEntity;
import com.cgg.pps.model.response.devicemgmt.mastermandal.MandalEntity;
import com.cgg.pps.model.response.devicemgmt.mastervillage.VillageEntity;
import com.cgg.pps.model.response.farmer.farmerUpdate.FarmerUpdateResponse;
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
import com.cgg.pps.presenter.FarUpdatePresenter;
import com.cgg.pps.room.repository.BBRepository;
import com.cgg.pps.room.repository.DMVRepository;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.ConnectionDetector;
import com.cgg.pps.util.CustomProgressDialog;
import com.cgg.pps.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class FarUpdateFragment extends Fragment implements FarRegInterface, FarUpdateInterface,
        BBInterface, DMVInterface, AdapterView.OnItemSelectedListener {
    private FarRegPresenter farRegPresenter;
    private FarUpdatePresenter farUpdatePresenter;
    private FarmerUpdateFragBinding binding;
    private CustomProgressDialog customProgressDialog;
    private ArrayList<View> landDetailsTableRowsArrayList;
    private ArrayList<View> FinalLandDetailsTableRowsArrayList;
    private PPCUserDetails ppcUserDetails;
    private FarmerDetailsResponse farmerDetailsResponse;
    private BBRepository bbRepository;
    private DMVRepository dmvRepository;
    private List<DistrictEntity> districtEntities;
    private List<DistrictEntity> districtEntitiesLand;
    private List<SocialEntity> socialEntities;
    private List<BankEntity> bankEntities;
    private List<BranchEntity> branchEntities;
    private List<MandalEntity> mandalEntities;
    private List<MandalEntity> mandalEntitiesLand;
    private List<VillageEntity> villageEntities;
    private List<VillageEntity> villageEntitiesLand;
    private List<String> disStringListMain, manStringListMain, vilStringListMain, socialStringListMain;
    private List<String> disStringListMainLand, manStringListMainLand, vilStringListMainLand;
    private List<String> bankStringList, branchStringList;
    private String f_district = "--Select--";
    private String f_mandal = "--Select--";
    private String f_village = "--Select--";
    private String f_social = "--Select--";
    private FarmerDetailsResponse fRes;
    private String distName, manName, vilName;
    private String enteredAadhar;
    private List<String> emptyList;
    private String farmerRegID;
    private Utils.DecimalDigitsInputFilter decimalDigitsInputFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.farmer_update_frag, container, false);
        setHasOptionsMenu(true);
        farRegPresenter = new FarRegPresenter();
        farUpdatePresenter = new FarUpdatePresenter();
        farRegPresenter.attachView(this);
        farUpdatePresenter.attachView(this);
        bbRepository = new BBRepository(getActivity());
        dmvRepository = new DMVRepository(getActivity());
        districtEntities = new ArrayList<>();
        districtEntitiesLand = new ArrayList<>();
        socialEntities = new ArrayList<>();
        mandalEntities = new ArrayList<>();
        mandalEntitiesLand = new ArrayList<>();
        villageEntities = new ArrayList<>();
        villageEntitiesLand = new ArrayList<>();
        bankEntities = new ArrayList<>();
        disStringListMain = new ArrayList<>();
        disStringListMainLand = new ArrayList<>();
        manStringListMain = new ArrayList<>();
        manStringListMainLand = new ArrayList<>();
        vilStringListMain = new ArrayList<>();
        vilStringListMainLand = new ArrayList<>();
        bankStringList = new ArrayList<>();
        branchStringList = new ArrayList<>();
        socialStringListMain = new ArrayList<>();
        binding.FRAadhaarNoEt.setEnabled(false);
        binding.FRBankIFSCcodeEt.setEnabled(false);
        farmerRegID = "00";

        emptyList = new ArrayList<>();
        emptyList.add("--Select--");
        customProgressDialog = new CustomProgressDialog(getActivity());

        landDetailsTableRowsArrayList = new ArrayList<View>();
        FinalLandDetailsTableRowsArrayList = new ArrayList<View>();
        binding.FRLandDetailsTl.removeAllViews();

        binding.FRSearchTv.setOnClickListener(onBtnClick);
        binding.FRAddNewRowBtn.setOnClickListener(onBtnClick);
        binding.FRCancelBtn.setOnClickListener(onBtnClick);
        binding.FRSubmitBtn.setOnClickListener(onBtnClick);
        binding.FRSearchdataEt.addTextChangedListener(new CustomTextWatcher(binding.FRSearchdataEt));

        decimalDigitsInputFilter = new Utils.DecimalDigitsInputFilter(7, 2);

        if (customProgressDialog != null && !customProgressDialog.isShowing())
            customProgressDialog.show();

        bbRepository.getAllBanks(this);

        binding.FRAddDistrictSp.setOnItemSelectedListener(this);
        binding.FRAddMandalSp.setOnItemSelectedListener(this);
        binding.FRBankNameSp.setOnItemClickListener(new AutoCompleteTVListener(binding.FRBankNameSp));
        binding.FRBankBranchNameSp.setOnItemClickListener(new AutoCompleteTVListener(binding.FRBankBranchNameSp));


        binding.rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utils.hideKeyboard(view);
                return false;
            }
        });

        return binding.getRoot();
    }

    private void getPreferenceData() {
        try {
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

            String farmerDetails = sharedPreferences.getString(AppConstants.FARMER_DATA, "");
            String farmerAadhar = sharedPreferences.getString(AppConstants.FARMER_AADHAR, "");
            binding.FRSearchdataEt.setText(farmerAadhar);
            enteredAadhar = binding.FRSearchdataEt.getText().toString().trim();
            farmerDetailsResponse = gson.fromJson(farmerDetails, FarmerDetailsResponse.class);
            if (farmerDetailsResponse != null) {
                getFarmerRegResponse(farmerDetailsResponse);
            } else {
                //something wrong
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

    public void getFarmerRegResponse(FarmerDetailsResponse farmerDetailsResponse) {
        try {


            fRes = farmerDetailsResponse;
            if (farmerDetailsResponse != null &&
                    farmerDetailsResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                binding.FRMainLl.setVisibility(View.VISIBLE);


                if (farmerDetailsResponse.getFarmerBasicDetail() != null) {
                    setFarmerBasicDetail(farmerDetailsResponse);
                    setFarmerBankDetail(farmerDetailsResponse);
                } else {
                    binding.FRAadhaarNoEt.setText(enteredAadhar);
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
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FarmerRegistration),
                        farmerDetailsResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
                binding.FRAadhaarNoEt.setText(enteredAadhar);

                binding.FRMainLl.setVisibility(View.VISIBLE);
                Utils.loadSpinnerSetSelectedItem(getActivity(), disStringListMain, binding.FRAddDistrictSp, f_district);
                Utils.loadSpinnerSetSelectedItem(getActivity(), manStringListMain, binding.FRAddMandalSp, f_mandal);
                Utils.loadSpinnerSetSelectedItem(getActivity(), vilStringListMain, binding.FRAddVillageSp, f_village);
                Utils.loadSpinnerSetSelectedItem(getActivity(), socialStringListMain, binding.FRSocialStatus, f_social);
                Utils.loadAutoCompleteTextData(getActivity(), bankStringList, binding.FRBankNameSp);
            } else if (farmerDetailsResponse != null &&
                    farmerDetailsResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                /*Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FarmerRegistration),
                        farmerDetailsResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);*/
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.FarmerRegistration),
                        farmerDetailsResponse.getResponseMessage(), getFragmentManager());
            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FarmerRegistration),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }

        } catch (Resources.NotFoundException e) {

            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.FarmerRegistration),
                    getResources().getString(R.string.server_not),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
    }

    @Override
    public void tokenGenerateResponse(TokenGenerateResponse tokenGenerateResponse) {

    }

    @Override
    public void getTokensFarmerResponse(GetTokenStage1Response getTokensDDLResponse) {

    }

    @Override
    public void getTokenDropDownDataResponse(GetTokenDropDownDataResponse getTokenDropDownDataResponse) {

    }


    @Override
    public void farmerSubmitResponse(FarmerSubmitResponse farmerSubmitResponse) {

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
            binding.FRSearchdataEt.setError(getString(R.string.val_aad_req));
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFarmerBasicDetail(FarmerDetailsResponse farmerDetailsResponse) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (farmerDetailsResponse.getFarmerBasicDetail() != null && farmerDetailsResponse.getFarmerBasicDetail().getDistrictID() != null
                    && disStringListMain.size() > 0) {
                dmvRepository.getBasicDistrictName(this, farmerDetailsResponse.getFarmerBasicDetail().getDistrictID().trim(),
                        binding.FRAddDistrictSp, disStringListMain);
            }


            if (farmerDetailsResponse.getFarmerBasicDetail() != null && farmerDetailsResponse.getFarmerBasicDetail().getDistrictID() != null
                    && farmerDetailsResponse.getFarmerBasicDetail().getMandalID() != null) {

                List<String> mandalsStringList = getMandalsListByDistID(farmerDetailsResponse);

                if (mandalsStringList.size() > 0) {
                    dmvRepository.getBasicMandalName(this, farmerDetailsResponse.getFarmerBasicDetail().getDistrictID().trim()
                            , farmerDetailsResponse.getFarmerBasicDetail().getMandalID().trim()
                            , binding.FRAddMandalSp, mandalsStringList);
                }

            }

            if (farmerDetailsResponse.getFarmerBasicDetail() != null &&
                    farmerDetailsResponse.getFarmerBasicDetail().getDistrictID() != null
                    && farmerDetailsResponse.getFarmerBasicDetail().getMandalID() != null
                    && farmerDetailsResponse.getFarmerBasicDetail().getVillageID() != null) {

                List<String> villageStringList = getVillagesListByDistID(farmerDetailsResponse);

                if (villageStringList.size() > 0) {
                    dmvRepository.getBasicVillageName(this,
                            farmerDetailsResponse.getFarmerBasicDetail().getDistrictID().trim()
                            , farmerDetailsResponse.getFarmerBasicDetail().getMandalID().trim()
                            , farmerDetailsResponse.getFarmerBasicDetail().getVillageID().trim(),
                            binding.FRAddVillageSp, villageStringList);
                }
            }

            if (farmerDetailsResponse.getFarmerBasicDetail() != null &&
                    farmerDetailsResponse.getFarmerBasicDetail().getSocialStatusID() != null &&
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
        try {
            for (VillageEntity villageEntity : villageEntities) {
                if (villageEntity.getDistrictID().equalsIgnoreCase(farmerDetailsResponse.getFarmerBasicDetail().getDistrictID().trim())
                        &&
                        villageEntity.getMandalID().equalsIgnoreCase(farmerDetailsResponse.getFarmerBasicDetail().getMandalID().trim())) {
                    villageStringList.add(villageEntity.getVillageName());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return villageStringList;
    }

    private List<String> getMandalsListByDistID(FarmerDetailsResponse farmerDetailsResponse) {
        List<String> mandalsStringList = new ArrayList<>();
        try {
            for (MandalEntity mandalEntity : mandalEntities) {
                if (mandalEntity.getDistrictID().equalsIgnoreCase(farmerDetailsResponse.getFarmerBasicDetail().getDistrictID().trim())) {
                    mandalsStringList.add(mandalEntity.getMandalName());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mandalsStringList;
    }

    private void addNewRowToLandDetailsTable(FarmerLandDetail landDetails) {
        try {

            if (landDetailsTableRowsArrayList.size() == 0) {
                binding.FRLandDetailsTl.removeAllViews();
            }

            LayoutInflater mInflater = LayoutInflater.from(getActivity());
            final TableRow paramView = (TableRow) mInflater.inflate(R.layout.fr_update_landdetails_tablerow, null);
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


            final Spinner landTypeSp = paramView.findViewById(R.id.FRLd_landType_Sp);
            final TextView hiddenID = paramView.findViewById(R.id.hiddenID);

            final Spinner districtSpinner = paramView.findViewById(R.id.FRLd_district_Sp);
            final Spinner mandalSpinner = paramView.findViewById(R.id.FRLd_mandal_Sp);
            final Spinner villageSpinner = paramView.findViewById(R.id.FRLd_village_Sp);


            landAcr.setFilters(new InputFilter[]{decimalDigitsInputFilter});

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


            final CheckBox FRLd_check_Cb = paramView.findViewById(R.id.FRLd_check_Cb);
            final ImageView FRLd_delete = paramView.findViewById(R.id.FRLd_delete);
            FRLd_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteRowFromLandDetailsTable(paramView);
                }
            });

            ownerAadhar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(farmerNatureSp.getSelectedItem()!=null &&
                            farmerNatureSp.getSelectedItemPosition()>1 && s!=null&& s.length()==12){
                        if(s.toString().equals(enteredAadhar)){
                            FRLd_check_Cb.setChecked(false);
                            ownerAadhar.setText("");
                            ShowAlert();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            if (landDetails != null) {

                FRLd_delete.setVisibility(View.GONE);
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

                if (landDetails.getDataSource() != null && landDetails.getDataSource().contains("CCLA")) {
                    setLayoutEnable(landTypeSp);
                    FRLd_check_Cb.setVisibility(View.VISIBLE);
                }

                if (landDetails.getDataSource() != null && landDetails.getDataSource().contains("OPMS")
                        && landDetails.getPendingApproval() != 26) {
                    FRLd_check_Cb.setVisibility(View.INVISIBLE);
                }

                if (landDetails.getDataSource() != null && landDetails.getDataSource().contains("OPMS")
                        && landDetails.getPendingApproval() == 26) {
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
                landCul.setText(String.valueOf(landDetails.getTotalCultivableArea()));
                landExt.setText(String.valueOf(landDetails.getTotalLeaseArea()));
                landTypeSp.setSelection(landDetails.getLandTypeId());

                hiddenID.setText(landDetails.getDataSource() + ":" +
                        landDetails.getFarmerRegistrationId() + ":" +
                        landDetails.getMainLandRowId() + ":" +
                        landDetails.getSubLandRowId() + ":" +
                        landDetails.getProcRowId() + ":"
                        + landDetails.getYieldperAcre());


                setFarmerLandDMVData(districtSpinner, mandalSpinner, villageSpinner, landDetails);


            } else {
                FRLd_delete.setVisibility(View.VISIBLE);

                hiddenID.setText(0 + ":" +
                        0 + ":" +
                        0 + ":" +
                        0 + ":" +
                        0 + ":" +
                        0);

                setLayoutEnable(ownerName);
                setLayoutEnable(ownerAadhar);
                setLayoutEnable(farmerNatureSp);
                setLayoutEnable(farmerRelationSp);
                setLayoutEnable(surveyNum);
                setLayoutEnable(passbookNum);
                setLayoutEnable(landAcr);
                setLayoutEnable(landTypeSp);
                setLayoutEnable(districtSpinner);
                setLayoutEnable(mandalSpinner);
                setLayoutEnable(villageSpinner);

                setFarmerNatureData(farmerNatureSp, landDetLL, ownerName, ownerAadhar, farmerRelationSp, null);
                setFarmerLandDMVData(districtSpinner, mandalSpinner, villageSpinner, null);
            }

            binding.FRLandDetailsTl.addView(paramView);
            landDetailsTableRowsArrayList.add(paramView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.FarmerRegistration));
        builder.setMessage(getString(R.string.owner_aadhaar));
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void setLayoutEnable(View view) {
        view.setEnabled(true);
    }

    private List<String> getLandMandalsListByDistID(FarmerLandDetail landDetails) {
        List<String> mandalsStringList = new ArrayList<>();
        try {
            for (MandalEntity mandalEntity : mandalEntities) {
                if (mandalEntity.getDistrictID().equalsIgnoreCase(landDetails.getDistrictID().trim())) {
                    mandalsStringList.add(mandalEntity.getMandalName());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mandalsStringList;
    }

    private List<String> getLandVillagesListByDistID(FarmerLandDetail landDetails) {
        List<String> villageStringList = new ArrayList<>();
        try {
            for (VillageEntity villageEntity : villageEntities) {
                if (villageEntity.getDistrictID().equalsIgnoreCase(landDetails.getDistrictID().trim())
                        &&
                        villageEntity.getMandalID().equalsIgnoreCase(landDetails.getMandalID().trim())) {
                    villageStringList.add(villageEntity.getVillageName());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return villageStringList;
    }


    private void setFarmerLandDMVData(Spinner districtSpinner, Spinner mandalSpinner,
                                      Spinner villageSpinner, FarmerLandDetail landDetails) {

        try {
            if (landDetails != null) {
                if (disStringListMainLand.size() > 0) {
                    if (landDetails.getDistrictID() != null) {
                        dmvRepository.getLandDistrictName(this, landDetails.getDistrictID().trim(),
                                districtSpinner, disStringListMainLand);
                    } else {
                        Utils.loadSpinnerSetSelectedItem(getActivity(),
                                disStringListMainLand,
                                districtSpinner, f_district);
                    }
                }

                List<String> mandalsStringList = getLandMandalsListByDistID(landDetails);
                if (mandalsStringList.size() > 0) {
                    if (landDetails.getDistrictID() != null && landDetails.getMandalID() != null) {
                        dmvRepository.getLandMandalName(this, landDetails.getDistrictID().trim()
                                , landDetails.getMandalID().trim()
                                , mandalSpinner, mandalsStringList);
                    } else {
                        Utils.loadSpinnerSetSelectedItem(getActivity(),
                                manStringListMainLand,
                                mandalSpinner, f_mandal);
                    }
                }
                List<String> villageStringList = getLandVillagesListByDistID(landDetails);
                if (villageStringList.size() > 0) {
                    if (landDetails.getDistrictID() != null && landDetails.getMandalID() != null && landDetails.getVillageID() != null) {
                        dmvRepository.getLandVillageName(this, landDetails.getDistrictID().trim()
                                , landDetails.getMandalID().trim()
                                , landDetails.getVillageID().trim(),
                                villageSpinner, villageStringList);
                    } else {
                        Utils.loadSpinnerSetSelectedItem(getActivity(),
                                vilStringListMainLand,
                                villageSpinner, f_village);
                    }
                }

            } else {
                Utils.loadSpinnerSetSelectedItem(getActivity(),
                        disStringListMainLand,
                        districtSpinner, f_district);
                Utils.loadSpinnerSetSelectedItem(getActivity(),
                        manStringListMainLand,
                        mandalSpinner, f_mandal);
                Utils.loadSpinnerSetSelectedItem(getActivity(),
                        vilStringListMainLand,
                        villageSpinner, f_village);
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

                        for (DistrictEntity districtEntity : districtEntitiesLand) {
                            if (districtEntity.getDistName().equalsIgnoreCase(districtName)) {
                                String distID = districtEntity.getDistId();


                                for (MandalEntity mandalEntity : mandalEntitiesLand) {
                                    if (mandalEntity.getDistrictID().equalsIgnoreCase(distID)) {
                                        mandalsStringList.add(mandalEntity.getMandalName());
                                    }
                                }
                            }
                        }
                        if (landDetails != null)
                            setLandData(landDetails, mandalSpinner, mandalsStringList);
                        else {
                            Utils.loadSpinnerSetSelectedItem(getActivity(), mandalsStringList, mandalSpinner, f_mandal);
                            Utils.loadSpinnerSetSelectedItem(getActivity(), emptyList, villageSpinner, f_village);
                        }
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
                        for (DistrictEntity districtEntity : districtEntitiesLand) {
                            if (districtEntity.getDistName().equalsIgnoreCase(districtName)) {
                                distId = districtEntity.getDistId();
                            }
                        }
                        for (MandalEntity mandalEntity : mandalEntitiesLand) {
                            if (mandalEntity.getDistrictID().equalsIgnoreCase(distId) &&
                                    mandalEntity.getMandalName().equalsIgnoreCase(mandalName)) {
                                manId = mandalEntity.getMandalID();
                            }
                        }

                        if (distId != null && manId != null) {
                            for (VillageEntity villageEntity : villageEntitiesLand) {
                                if (villageEntity.getDistrictID().equalsIgnoreCase(distId)
                                        &&
                                        villageEntity.getMandalID().equalsIgnoreCase(manId)) {
                                    villagesStringList.add(villageEntity.getVillageName());
                                }

                            }
                        }

                        if (landDetails != null)
                            setLandVilData(landDetails, villageSpinner, villagesStringList);
                        else
                            Utils.loadSpinnerSetSelectedItem(getActivity(), villagesStringList, villageSpinner, f_village);

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


    private void setLayoutDisable(View view) {
        view.setEnabled(false);
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
                    ownerName.setEnabled(true);
                    ownerAadhar.setEnabled(true);
                    if (landDetails != null) {
                        int nature = parent.getSelectedItemPosition();
                        if (nature != 0) {
                            landDetLL.setVisibility(View.VISIBLE);
                            if (nature == 1) {
                                ownerName.setEnabled(false);
                                ownerAadhar.setEnabled(false);
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
                                ownerName.setEnabled(false);
                                ownerAadhar.setEnabled(false);
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
                    case R.id.FR_addNewRow_Btn:
                        addNewRowToLandDetailsTable(null);
                        break;
                    case R.id.FR_search_Tv:
                        fRes = null;
                        binding.FRSearchdataEt.clearFocus();
                        Utils.hideKeyboard(view);
                        clearForm(binding.FRMainLl);
                        landDetailsTableRowsArrayList = new ArrayList<View>();
                        FinalLandDetailsTableRowsArrayList = new ArrayList<View>();
                        binding.FRLandDetailsTl.removeAllViews();
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
                    case R.id.FR_Submit_Btn:
                        FarmerUpdateRequest farmerUpdateRequest = new FarmerUpdateRequest();


                        if (validateFarmerData(farmerUpdateRequest)) {
                            StringBuilder finalString = farmerSubmitReqData(farmerUpdateRequest);
                            if (finalString != null) {
                                farmerUpdateRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                                farmerUpdateRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));
                                farmerUpdateRequest.setXmlFarmerRegistrationDetails(String.valueOf(finalString));

                                ShowSubmitRegisterAlert(farmerUpdateRequest);
                            }
                        }
                        break;
                    case R.id.FR_Cancel_Btn:
                        callFragment();
                        break;
                }
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
    };


    private void callFragment() {
        try {
            AppConstants.FARG_TAG = FarRegFragment.class.getSimpleName();
            AppConstants.IS_CANCELLED = true;
            Fragment fragment = new FarRegFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.dashboard_container, fragment, AppConstants.FARG_TAG);
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

                bbRepository.getAllBranches(this);


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

    private void ShowSubmitRegisterAlert(FarmerUpdateRequest farmerUpdateRequest) {

        try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.farmer_updation_confirmation);
                dialog.setCancelable(false);
                if (!dialog.isShowing())
                    dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                TextView titleTv = dialog.findViewById(R.id.alert_titleTv);
                titleTv.setText(getResources().getString(R.string.updationConfirmation));
                Button yes = dialog.findViewById(R.id.paddyProcuremtProceedBtn);
                Button cancel = dialog.findViewById(R.id.cancelBtn);
                TextView confirmProceed = dialog.findViewById(R.id.confirmProceed);
                if (farmerUpdateRequest.getLandXMLDataArrayList() != null && farmerUpdateRequest.getLandXMLDataArrayList().size() == 0) {
                    confirmProceed.setText(getString(R.string.with_out_land));
                } else {
                    confirmProceed.setText(getString(R.string.are_you_sure_to_pr));
                }
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                            if (customProgressDialog != null && !customProgressDialog.isShowing())
                                customProgressDialog.show();
                            farUpdatePresenter.UpdateFarmerData(farmerUpdateRequest);
                        } else {
                            Utils.customAlert(getActivity(),
                                    getResources().getString(R.string.FarmerRegistration),
                                    getResources().getString(R.string.no_internet),
                                    getResources().getString(R.string.WARNING), false);
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllDistrictEntities(List<DistrictEntity> districtEntities) {
        try {
            if (districtEntities!=null && districtEntities.size() > 0) {
                this.districtEntities = districtEntities;
                disStringListMain = new ArrayList<>();
                disStringListMain.add(0, "--Select--");
                for (DistrictEntity districtEntity : districtEntities) {
                    disStringListMain.add(districtEntity.getDistName());
                }
                if (disStringListMain.size() > 0) {
                    Collections.sort(disStringListMain, new Comparator<String>() {
                        @Override
                        public int compare(String text1, String text2) {
                            return text1.compareToIgnoreCase(text2);
                        }
                    });
                    Utils.loadSpinnerSetSelectedItem(getActivity(), disStringListMain, binding.FRAddDistrictSp, f_district);

                    dmvRepository.getAllMandals(this);
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
                    Collections.sort(manStringListMain, new Comparator<String>() {
                        @Override
                        public int compare(String text1, String text2) {
                            return text1.compareToIgnoreCase(text2);
                        }
                    });
                    Utils.loadSpinnerSetSelectedItem(getActivity(), manStringListMain, binding.FRAddMandalSp, f_mandal);

                    dmvRepository.getAllVillages(this);
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
                    Collections.sort(vilStringListMain, new Comparator<String>() {
                        @Override
                        public int compare(String text1, String text2) {
                            return text1.compareToIgnoreCase(text2);
                        }
                    });
                    Utils.loadSpinnerSetSelectedItem(getActivity(), vilStringListMain, binding.FRAddVillageSp, f_village);

                    dmvRepository.getAllSocialStatus(this);
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

                Utils.loadSpinnerSetSelectedItem(getActivity(), socialStringListMain, binding.FRSocialStatus, f_social);


                dmvRepository.getAllowDistricts(this);


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
    public void getAllVillagesByMan(List<String> villageEntities, Spinner spinner, String distName, String manName) {

        try {
            if (!distName.equalsIgnoreCase("--Select--") && !manName.equalsIgnoreCase("--Select--")) {

                Utils.loadSpinnerSetSelectedItem(getActivity(), villageEntities,
                        spinner, f_village);
            } else {
                Utils.loadSpinnerSetSelectedItem(getActivity(), villageEntities, spinner, f_village);
            }

            if (!manName.equalsIgnoreCase("--Select--")) {
                Utils.loadSpinnerSetSelectedItem(getActivity(), villageEntities, spinner, vilName);
            } else {
                Utils.loadSpinnerSetSelectedItem(getActivity(), villageEntities, spinner, f_village);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllowDistrictEntities(List<DistrictEntity> districtEntitiesLand) {
        try {
            if (districtEntitiesLand!=null && districtEntitiesLand.size() > 0) {
                this.districtEntitiesLand = districtEntitiesLand;
                disStringListMainLand = new ArrayList<>();
                disStringListMainLand.add(0, "--Select--");
                for (DistrictEntity districtEntity : districtEntitiesLand) {
                    disStringListMainLand.add(districtEntity.getDistName());
                }
                if (disStringListMainLand.size() > 0) {
                    Collections.sort(disStringListMainLand, new Comparator<String>() {
                        @Override
                        public int compare(String text1, String text2) {
                            return text1.compareToIgnoreCase(text2);
                        }
                    });
                    dmvRepository.getAllowMandals(this);
                }

            } else {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();

                Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                        getResources().getString(R.string.FarmerRegistration),
                        getString(R.string.no_districts), true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void getAllowMandalEntities(List<MandalEntity> mandalEntitiesLand) {
        try {
            if (mandalEntitiesLand!=null && mandalEntitiesLand.size() > 0) {
                this.mandalEntitiesLand = mandalEntitiesLand;
                manStringListMainLand = new ArrayList<>();
                manStringListMainLand.add(0, "--Select--");
                for (MandalEntity mandalEntity : mandalEntities) {
                    manStringListMainLand.add(mandalEntity.getMandalName());
                }
                if (manStringListMainLand.size() > 0) {
                    Collections.sort(manStringListMainLand, new Comparator<String>() {
                        @Override
                        public int compare(String text1, String text2) {
                            return text1.compareToIgnoreCase(text2);
                        }
                    });

                    dmvRepository.getAllowVillages(this);
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
    public void getAllowVillageEntities(List<VillageEntity> villageEntitiesLand) {
        try {
            if (villageEntitiesLand!=null && villageEntitiesLand.size() > 0) {
                this.villageEntitiesLand = villageEntitiesLand;
                vilStringListMainLand = new ArrayList<>();
                vilStringListMainLand.add(0, "--Select--");
                for (VillageEntity villageEntity : villageEntities) {
                    vilStringListMainLand.add(villageEntity.getVillageName());
                }
                if (vilStringListMainLand.size() > 0) {
                    Collections.sort(vilStringListMainLand, new Comparator<String>() {
                        @Override
                        public int compare(String text1, String text2) {
                            return text1.compareToIgnoreCase(text2);
                        }
                    });

                    getPreferenceData();
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
    public void bankCount(int bankCnt) {
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

                dmvRepository.getAllDistricts(this);

            } else {

                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();

                Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                        getResources().getString(R.string.FarmerRegistration),
                        getString(R.string.no_branches), true);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void branchCount(int branchCnt) {

    }

    @Override
    public void getBankName(String bankName) {
        try {
            binding.FRBankNameSp.setText(bankName);

            if (branchStringList.size() > 0 && !TextUtils.isEmpty(bankName)) {

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

                if (farmerDetailsResponse.getFarmerBasicDetail().getBankId() != null
                        && farmerDetailsResponse.getFarmerBasicDetail().getBranchId() != null) {
                    bbRepository.getBranchName(this,
                            farmerDetailsResponse.getFarmerBasicDetail().getBankId().trim(),
                            farmerDetailsResponse.getFarmerBasicDetail().getBranchId().trim());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getBranchName(String branchName) {
        binding.FRBankBranchNameSp.setText(branchName);
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
                                    String distID = districtEntity.getDistId().trim();


                                    for (MandalEntity mandalEntity : mandalEntities) {
                                        if (mandalEntity.getDistrictID().trim().equalsIgnoreCase(distID)) {
                                            mandalsStringList.add(mandalEntity.getMandalName());
                                        }


                                    }
                                }
                            }


                            if (fRes != null && fRes.getFarmerBasicDetail() != null && fRes.getFarmerBasicDetail().getDistrictID() != null) {
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
                                    distId = districtEntity.getDistId().trim();
                                }
                            }
                            for (MandalEntity mandalEntity : mandalEntities) {
                                if (mandalEntity.getDistrictID().equalsIgnoreCase(distId) &&
                                        mandalEntity.getMandalName().equalsIgnoreCase(mandalName)) {
                                    manId = mandalEntity.getMandalID().trim();
                                }
                            }

                            if (distId != null && manId != null) {
                                for (VillageEntity villageEntity : villageEntities) {
                                    if (villageEntity.getDistrictID().trim().equalsIgnoreCase(distId)
                                            &&
                                            villageEntity.getMandalID().trim().equalsIgnoreCase(manId)) {
                                        villagesStringList.add(villageEntity.getVillageName());
                                    }

                                }
                            }
                            if (fRes != null && fRes.getFarmerBasicDetail() != null && fRes.getFarmerBasicDetail().getDistrictID() != null
                                    && fRes.getFarmerBasicDetail().getMandalID() != null) {
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
            }
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

    private void setBasicDistData(FarmerDetailsResponse farmerDetailsResponse, Spinner spi, List<String> mandalEntities) {
        try {
            dmvRepository.getLandMandalName(this, farmerDetailsResponse.getFarmerBasicDetail().getDistrictID().trim()
                    , farmerDetailsResponse.getFarmerBasicDetail().getMandalID().trim()
                    , spi, mandalEntities);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadVillageSpinnerLocalData(String districtName1, String manName, Spinner frAddVillageSp) {
        dmvRepository.getVillagesByManName(this, districtName1, manName, frAddVillageSp);

    }

    private void loadMandalSpinnerLocalData(String districtName, Spinner frAddMandalSp, Spinner frAddVillageSp) {
        dmvRepository.getMandalsByDistName(this, districtName, frAddMandalSp, frAddVillageSp);
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void getFarmerUpdateResponse(FarmerUpdateResponse farmerUpdateResponse) {
        try {

            if (customProgressDialog != null && customProgressDialog.isShowing())
                customProgressDialog.dismiss();
            if (farmerUpdateResponse != null) {
                if (farmerUpdateResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                    AppConstants.IS_UPDATED = true;
                    Fragment fragment = new FarRegFragment();
                    Utils.showAlertNavigateToFarmer(getActivity(), fragment, getFragmentManager(),
                            getResources().getString(R.string.FarmerRegistration),
                            farmerUpdateResponse.getResponseMessage(), true);

                } else if (farmerUpdateResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.FarmerRegistration),
                            farmerUpdateResponse.getResponseMessage(),
                            getString(R.string.ERROR), false);
                } else if (farmerDetailsResponse != null &&
                        farmerDetailsResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.FarmerRegistration),
                            farmerUpdateResponse.getResponseMessage(),
                            getString(R.string.ERROR), false);
                } else if (farmerUpdateResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                    /*Utils.customAlert(getActivity(),
                            getResources().getString(R.string.FarmerRegistration),
                            farmerUpdateResponse.getResponseMessage(),
                            getString(R.string.ERROR), false);*/
                    Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.FarmerRegistration),
                            farmerUpdateResponse.getResponseMessage(), getFragmentManager());
                } else if (farmerUpdateResponse.getStatusCode() != null
                        && farmerUpdateResponse.getStatusCode() == AppConstants.VERSION_CODE) {


                    Utils.callPlayAlert(farmerUpdateResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

                } else {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.login),
                            getResources().getString(R.string.server_not),
                            getString(R.string.ERROR), false);
                }
            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.login),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);

            }
        } catch (Exception e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.login),
                    getResources().getString(R.string.server_not),
                    getString(R.string.ERROR), false);
        }
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
            switch (mEditText.getId()) {
                case R.id.FR_BankACNo_Et:
                    binding.FRBankACNoCFMEt.setText("");
                    break;
                case R.id.FR_searchdata_Et:
                    if (s.toString().length() != 12) {
                        binding.FRMainLl.setVisibility(View.GONE);
                    }
                    break;
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
                                    break;
                                }
                            }
                            for (BranchEntity branchEntity : branchEntities) {
                                if (branchEntity.getBankID().equals(bankID) &&
                                        branchEntity.getBranchName().equalsIgnoreCase(branchName)) {
                                    branchId = branchEntity.getBranchID();
                                    break;
                                }
                            }

                            if (bankID != null && branchId != null) {
                                for (BranchEntity branchEntity : branchEntities) {
                                    if (branchEntity.getBankID().equals(bankID)
                                            &&
                                            branchEntity.getBranchID().equals(branchId)) {
                                        binding.FRBankIFSCcodeEt.setText(branchEntity.getIFSC());
                                        break;
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
                if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                    clearForm((ViewGroup) view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEditTextData(View view, String errorMsg) {
        try {
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                editText.setError(errorMsg);
                editText.requestFocus();
                binding.scMain.scrollTo(0, view.getBottom());
            }

            if (view instanceof AutoCompleteTextView) {
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view;
                autoCompleteTextView.setError(errorMsg);
                autoCompleteTextView.requestFocus();
                binding.scMain.scrollTo(0, view.getBottom());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void showAlertError(String errorMsg) {
        Utils.customAlert(getActivity(),
                getResources().getString(R.string.FarmerRegistration),
                errorMsg,
                getString(R.string.INFORMATION), false);
    }


    private boolean validateFarmerData(FarmerUpdateRequest farmerSubmitRequest) {
        String distID = null, manID = null, vilID = null;
        Integer bankID = 0, branchID = 0, socID = 0;

        FarmerUpdateXMLData farmerXMLData = new FarmerUpdateXMLData();
        ArrayList<FarmerUpdateLandXMLData> farmerLandXMLDataArrayList = new ArrayList<>();
        FinalLandDetailsTableRowsArrayList = new ArrayList<>();
        farmerXMLData.setRegistrationID(farmerRegID);

        binding.FRAadhaarNoEt.setText(binding.FRSearchdataEt.getText().toString().trim());

        String farmerName;
        farmerName = binding.FRFarmerNameEt.getText().toString().trim();

        if (TextUtils.isEmpty(farmerName)) {
            setEditTextData(binding.FRFarmerNameEt, getString(R.string.enter_farmer_name));
            return false;
        } else {
            farmerXMLData.setFarmerName(farmerName);
        }

        if (!farmerName.matches("[a-zA-Z. ]+")) {
            setEditTextData(binding.FRFarmerNameEt, getString(R.string.valid_far_name));
            return false;
        } else {
            farmerXMLData.setFarmerName(farmerName);
        }


        String aadhaarNo = binding.FRAadhaarNoEt.getText().toString().trim();

        if (TextUtils.isEmpty(aadhaarNo)) {
            setEditTextData(binding.FRAadhaarNoEt, getString(R.string.field_empty));
            return false;
        }

        if (!Utils.ValidateAadharNumber(aadhaarNo)) {
            binding.FRAadhaarNoEt.setEnabled(true);

            setEditTextData(binding.FRAadhaarNoEt, getString(R.string.valid_aadhar));
            return false;
        } else {
            farmerXMLData.setRegistrationNo(aadhaarNo);
        }

        String fatherName = binding.FRFatherNameEt.getText().toString().trim();

        if (TextUtils.isEmpty(fatherName)) {
            setEditTextData(binding.FRFatherNameEt, getString(R.string.enter_far_name));
            return false;
        } else {
            farmerXMLData.setFarmerFatherName(fatherName);
        }

        if (!fatherName.matches("[a-zA-Z. ]+")) {
            setEditTextData(binding.FRFatherNameEt, getString(R.string.valid_name));
            return false;
        } else {
            farmerXMLData.setFarmerFatherName(fatherName);
        }


        String gender;

        if (binding.FRRgGender.getCheckedRadioButtonId() == -1) {
            showAlertError("Please select gender");
            return false;
        } else if (binding.FRGenderMaleRd.isChecked()) {
            gender = "1";
            farmerXMLData.setGender(gender);
        } else {
            gender = "2";
            farmerXMLData.setGender(gender);
        }

        String age = binding.FRAgeEt.getText().toString().trim();
        farmerXMLData.setAge(age);

        String mobNum = binding.FRAddMobileEt.getText().toString().trim();

        if (TextUtils.isEmpty(mobNum)) {
            setEditTextData(binding.FRAddMobileEt, getString(R.string.enter_mob));
            return false;
        }
        if (!mobNum.matches("[6-9]{1}+[0-9]+") || mobNum.length() != 10) {
            setEditTextData(binding.FRAddMobileEt, getString(R.string.enter_valid_mob));
            return false;
        } else {
            farmerXMLData.setFMobile(mobNum);
        }


        String district = binding.FRAddDistrictSp.getSelectedItem().toString();
        if (district.equalsIgnoreCase("--Select--")) {
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

        String mandal = binding.FRAddMandalSp.getSelectedItem().toString();
        if (mandal.equalsIgnoreCase("--Select--")) {
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


        String village = binding.FRAddVillageSp.getSelectedItem().toString();
        if (village.equalsIgnoreCase("--Select--")) {
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

        String streetName = binding.FRLandMarkEt.getText().toString();
        farmerXMLData.setFLandMark(streetName);

        String houseNum = binding.FRDoorNoEt.getText().toString();
        farmerXMLData.setFDoorNo(houseNum);

        String pinCode = binding.FRAddPincodeEt.getText().toString();
        if (!TextUtils.isEmpty(pinCode) && !pinCode.matches("5+[0-9]{5}")) {
            setEditTextData(binding.FRAddPincodeEt, getString(R.string.valid_pin));
            return false;
        } else {
            farmerXMLData.setFPincode(pinCode);
        }

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


        String bankName = binding.FRBankNameSp.getText().toString().trim();

        if (TextUtils.isEmpty(bankName)) {
            setEditTextData(binding.FRBankNameSp, getString(R.string.ent_bank));
            return false;
        }

       /* if (!bankName.matches("[a-zA-Z.- ]+")) {
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


        String branchName = binding.FRBankBranchNameSp.getText().toString().trim();

        if (TextUtils.isEmpty(branchName)) {
            setEditTextData(binding.FRBankBranchNameSp, getString(R.string.ent_bra));
            return false;
        }

        /*if (!branchName.matches("[a-zA-Z.- ]+")) {
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

        String ifscCode = binding.FRBankIFSCcodeEt.getText().toString().trim();

        if (TextUtils.isEmpty(ifscCode)) {
            setEditTextData(binding.FRBankBranchNameSp, getString(R.string.ent_ifsc));
            return false;
        } else {
            farmerXMLData.setIFSCCode(ifscCode);
        }


        String farmerNameAsPerBank = binding.FRFarmerNameAsPerBankEt.getText().toString().trim();

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

        String bankAccNo = binding.FRBankACNoEt.getText().toString().trim();

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

        String confirmBankAccNo = binding.FRBankACNoCFMEt.getText().toString().trim();

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
            }


            for (int i = 0; i < FinalLandDetailsTableRowsArrayList.size(); i++) {

                FarmerUpdateLandXMLData farmerLandXMLData = new FarmerUpdateLandXMLData();

                TableRow paramView = (TableRow) FinalLandDetailsTableRowsArrayList.get(i);
                Spinner FarmerNatureSp = paramView.findViewById(R.id.FRLd_landEnjoyerNature_Sp);
                Spinner farmerRelationSp = paramView.findViewById(R.id.FRLd_relationship_Sp);
                TextView hiddenID = paramView.findViewById(R.id.hiddenID);
                CheckBox check = paramView.findViewById(R.id.FRLd_check_Cb);

                int farmerNature = FarmerNatureSp.getSelectedItemPosition();

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
                    farmerLandXMLData.setFarmerRegistrationId(FarmerRegistrationId);
                    farmerLandXMLData.setMainLandRowId(MainLandRowId);
                    farmerLandXMLData.setSubLandRowId(SubLandRowId);
                    farmerLandXMLData.setProcRowId(ProcRowId);
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
                    if (districtSp.getSelectedItem() != null) {
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
                    }


                    Spinner mandalSpinner = (Spinner) paramView.findViewById(R.id.FRLd_mandal_Sp);
                    if (mandalSpinner.getSelectedItem() != null) {

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
                    }


                    Spinner villageSpinner = (Spinner) paramView.findViewById(R.id.FRLd_village_Sp);
                    if (villageSpinner.getSelectedItem() != null) {

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

                    Float landAcres = Float.parseFloat(landAcersEt.getText().toString().trim());
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

                    farmerLandXMLDataArrayList.add(farmerLandXMLData);

                }
            }
        }


        farmerSubmitRequest.setFarmerXMLData(farmerXMLData);
        farmerSubmitRequest.setLandXMLDataArrayList(farmerLandXMLDataArrayList);


        return true;
    }

    private StringBuilder farmerSubmitReqData(FarmerUpdateRequest farmerSubmitRequest) {

        String farmerName = farmerSubmitRequest.getFarmerXMLData().getFarmerName();
        String regNo = farmerSubmitRequest.getFarmerXMLData().getRegistrationNo();
        String regId = farmerSubmitRequest.getFarmerXMLData().getRegistrationID();
        String gender = farmerSubmitRequest.getFarmerXMLData().getGender();
        String age = farmerSubmitRequest.getFarmerXMLData().getAge();
        String farmerFatherName = farmerSubmitRequest.getFarmerXMLData().getFarmerFatherName();
        String doorNo = farmerSubmitRequest.getFarmerXMLData().getFDoorNo();
        String landmark = farmerSubmitRequest.getFarmerXMLData().getFLandMark();
        String distID = farmerSubmitRequest.getFarmerXMLData().getFDistrictID();
        String socID = farmerSubmitRequest.getFarmerXMLData().getSocialStatusID();
        String manID = farmerSubmitRequest.getFarmerXMLData().getFMandalID();
        String vilID = farmerSubmitRequest.getFarmerXMLData().getFVillageID();
        String pincode = farmerSubmitRequest.getFarmerXMLData().getFPincode();
        String mobile = farmerSubmitRequest.getFarmerXMLData().getFMobile();
        String bankID = farmerSubmitRequest.getFarmerXMLData().getBankID();
        String branchId = farmerSubmitRequest.getFarmerXMLData().getBranchID();
        String ifscCode = farmerSubmitRequest.getFarmerXMLData().getIFSCCode();
        String nameAsPerBank = farmerSubmitRequest.getFarmerXMLData().getNameasperBank();
        String bankAccNo = farmerSubmitRequest.getFarmerXMLData().getBankAccountNo();

        final StringBuilder xmlDoc = new StringBuilder();
        xmlDoc.append("<FarmerRegistrationDetails>");
        xmlDoc.append("<ppc>");
        xmlDoc.append("<ppccode>" + ppcUserDetails.getPPCCode() + "</ppccode>"
                + "<ppcid>" + ppcUserDetails.getPPCID() + "</ppcid>"
                + "<SystemIP>" + ppcUserDetails.getPPCID() + "</SystemIP>"
                + "<seasonID>" + ppcUserDetails.getSeasonID() + "</seasonID>"
        );
        xmlDoc.append("</ppc>");
        xmlDoc.append("<Registration>");
        xmlDoc.append(
                "<RegistrationID>" + regId + "</RegistrationID>"
                        + "<RegistrationNo>" + regNo + "</RegistrationNo>"
                        + "<FarmerName>" + farmerName + "</FarmerName>"
                        + "<FarmerFatherName>" + farmerFatherName + "</FarmerFatherName>"
                        + "<SocialStatusID>" + socID + "</SocialStatusID>"
                        + "<Gender>" + gender + "</Gender>"
                        + "<Age>" + age + "</Age>"
                        + "<FMobile>" + mobile + "</FMobile>"
                        + "<FDistrictID>" + distID + "</FDistrictID>"
                        + "<FMandalID>" + manID + "</FMandalID>"
                        + "<FVillageID>" + vilID + "</FVillageID>"
                        + "<FLandMark>" + landmark + "</FLandMark>"
                        + "<FDoorNo>" + doorNo + "</FDoorNo>"
                        + "<FPincode>" + pincode + "</FPincode>"
                        + "<BankID>" + bankID + "</BankID>"
                        + "<BranchID>" + branchId + "</BranchID>"
                        + "<IFSCCode>" + ifscCode + "</IFSCCode>"
                        + "<NameasperBank>" + nameAsPerBank + "</NameasperBank>"
                        + "<BankAccountNo>" + bankAccNo + "</BankAccountNo>"
        );
        xmlDoc.append("</Registration>");

        xmlDoc.append("<LandSet>");

        for (int x = 0; x < farmerSubmitRequest.getLandXMLDataArrayList().size(); x++) {
            xmlDoc.append("<FarmerLndDetails>");
            xmlDoc.append("<MainLandRowId>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getMainLandRowId() + "</MainLandRowId >"
                    + "<SubLandRowId>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getSubLandRowId() + "</SubLandRowId>"
                    + "<ProcRowId>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getProcRowId() + "</ProcRowId>"
                    + "<TokenId>" + 0 + "</TokenId>"
                    + "<DataSource>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getDataSource() + "</DataSource>"
                    + "<FarmerRegistrationId>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getFarmerRegistrationId() + "</FarmerRegistrationId>"
                    + "<OwnershipTypeId>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getOwnershipTypeId() + "</OwnershipTypeId>"
                    + "<LegalHeirRelationId>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getLegalHeirRelationId() + "</LegalHeirRelationId>"
                    + "<DistrictID>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getDistrictID() + "</DistrictID>"
                    + "<MandalID>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getMandalID() + "</MandalID>"
                    + "<VillageID>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getVillageID() + "</VillageID>"
                    + "<LandTypeId>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getLandTypeId() + "</LandTypeId>"
                    + "<LandOwnerName>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getLandOwnerName() + "</LandOwnerName>"
                    + "<LandOwnerAadhaar>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getLandOwnerAadhaar() + "</LandOwnerAadhaar>"
                    + "<SurveyNo>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getSurveyNo() + "</SurveyNo>"
                    + "<PassBookNo>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getPassBookNo() + "</PassBookNo>"
                    + "<TotalArea>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getTotalArea() + "</TotalArea>"
                    + "<YieldperAcre>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getYieldperAcre() + "</YieldperAcre>"
                    + "<IsActive>" + false + "</IsActive>"
            );
            xmlDoc.append("</FarmerLndDetails>");
        }

        xmlDoc.append("</LandSet>");

        xmlDoc.append("</FarmerRegistrationDetails>");
        return xmlDoc;


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (customProgressDialog != null) {
                customProgressDialog = null;
            }
            if (farUpdatePresenter != null) {
                farUpdatePresenter.detachView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



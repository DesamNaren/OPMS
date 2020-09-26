package com.cgg.pps.fragment;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.FaqFragmentBinding;
import com.cgg.pps.interfaces.DMVInterface;
import com.cgg.pps.interfaces.FAQInterface;
import com.cgg.pps.interfaces.PaddyTestInterface;
import com.cgg.pps.model.request.faq.faqsubmit.FAQRequest;
import com.cgg.pps.model.request.faq.faqsubmit.FaqXMLData;
import com.cgg.pps.model.request.farmer.gettokens.GetTokensRequest;
import com.cgg.pps.model.response.devicemgmt.masterdistrict.DistrictEntity;
import com.cgg.pps.model.response.devicemgmt.mastermandal.MandalEntity;
import com.cgg.pps.model.response.devicemgmt.mastervillage.VillageEntity;
import com.cgg.pps.model.response.devicemgmt.paddyvalues.PaddyEntity;
import com.cgg.pps.model.response.faq.FAQSubmitResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokenOutput;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensDDLResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
import com.cgg.pps.model.response.socialstatus.SocialEntity;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.presenter.FAQPresenter;
import com.cgg.pps.room.repository.DMVRepository;
import com.cgg.pps.room.repository.PaddyTestRepository;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.ConnectionDetector;
import com.cgg.pps.util.CustomProgressDialog;
import com.cgg.pps.util.Utils;
import com.cgg.pps.view.DashboardActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FAQFragment extends Fragment implements FAQInterface, PaddyTestInterface, DMVInterface {
    private FaqFragmentBinding binding;
    private PPCUserDetails ppcUserDetails;
    private FAQPresenter fAQPresenter;
    private PaddyTestRepository paddyTestRepository;
    private List<PaddyEntity> paddyEntities;
    private GetTokenOutput getTokenOutputMain;
    private GetTokensDDLResponse getTokensResponseList;
    private String gradeAStatus = "Y";
    private String commonStatus = "Y";
    private String remarks = "test";
    private CustomProgressDialog customProgressDialog;
    private DMVRepository dmvRepository;
    private Utils.DecimalDigitsInputFilter decimalDigitsInputFilter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.faq_fragment, container, false);
        fAQPresenter = new FAQPresenter();
        fAQPresenter.attachView(this);
        paddyTestRepository = new PaddyTestRepository(getActivity());
        getPreferenceData();
        binding.faqTokenNoSp.setOnItemSelectedListener(onItemClick);
        binding.faqPaddyTypeSp.setOnItemSelectedListener(onItemClick);
        binding.faqPaddyTypeSp.setEnabled(false);
        binding.faqTestRD.setOnClickListener(onClick);
        binding.ursTestRD.setOnClickListener(onClick);
        binding.faqSubmitBtn.setOnClickListener(onClick);
        customProgressDialog = new CustomProgressDialog(getActivity());
        dmvRepository = new DMVRepository(getActivity());

        dmvRepository.getAllDistricts(this);

        binding.rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utils.hideKeyboard(view);
                return false;
            }
        });


        decimalDigitsInputFilter = new Utils.DecimalDigitsInputFilter(2,3);

        binding.faqInOrganicMatterCommonEt.setFilters(new InputFilter[]{decimalDigitsInputFilter});
        binding.faqInOrganicMatterGardeAEt.setFilters(new InputFilter[]{decimalDigitsInputFilter});


        binding.faqOrganicMatterCommonEt.setFilters(new InputFilter[]{decimalDigitsInputFilter});
        binding.faqOrganicMatterGardeAEt.setFilters(new InputFilter[]{decimalDigitsInputFilter});


        binding.faqImmatturedGrainsCommonEt.setFilters(new InputFilter[]{decimalDigitsInputFilter});
        binding.faqImmatturedGrainsGardeAEt.setFilters(new InputFilter[]{decimalDigitsInputFilter});


        binding.faqDamegedGrainsCommonEt.setFilters(new InputFilter[]{decimalDigitsInputFilter});
        binding.faqDamegedGrainsGardeAEt.setFilters(new InputFilter[]{decimalDigitsInputFilter});

        binding.faqLowerClassGrainsCommonEt.setFilters(new InputFilter[]{decimalDigitsInputFilter});
        binding.faqLowerClassGrainsGardeAEt.setFilters(new InputFilter[]{decimalDigitsInputFilter});

        binding.faqMoistureCommonEt.setFilters(new InputFilter[]{decimalDigitsInputFilter});
        binding.faqMoistureGardeAEt.setFilters(new InputFilter[]{decimalDigitsInputFilter});


        return binding.getRoot();
    }


    public void onResume() {
        super.onResume();

        try {
            if (getActivity() != null)
                ((DashboardActivity) getActivity())
                        .setActionBarTitle(getResources().getString(R.string.FAQ));

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
                            getResources().getString(R.string.FAQ),
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

    private void getGeneratedTokens(long tokenId) {
        try {
            GetTokensRequest getTokensFarmerRequest = new GetTokensRequest();
            getTokensFarmerRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
            getTokensFarmerRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));
            getTokensFarmerRequest.setTokenStatus("2");
            if (tokenId == -1) {
                if (customProgressDialog != null)
                    customProgressDialog.show();
                fAQPresenter.GetFAQGeneratedTokens(getTokensFarmerRequest);
            } else {
                if (customProgressDialog != null)
                    customProgressDialog.show();
                getTokensFarmerRequest.setTokenID(tokenId);
                fAQPresenter.GetSelectedTokenData(getTokensFarmerRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSelectedTokenData(GetTokensResponse getTokensFAQResponse) {
        try {
            int paddyType = 0;
            getTokenOutputMain = getTokensFAQResponse.getGetTokenOutput().get(0);

            if (getTokensFAQResponse.getGetTokenOutput().get(0).getTokenNo() != null) {
                binding.faqTokenNoTv.setText(getTokensFAQResponse.getGetTokenOutput().get(0).getTokenNo().trim());
            }
            if (getTokensFAQResponse.getGetTokenOutput().get(0).getRegistrationNo() != null) {
                String mask = getTokensFAQResponse.getGetTokenOutput().get(0).getRegistrationNo().trim().replaceAll("\\w(?=\\w{4})", "*");
                binding.faqRegIdTv.setText(mask);
            }
            if (getTokensFAQResponse.getGetTokenOutput().get(0).getFarmerName() != null) {
                binding.faqFarmerNameTv.setText(getTokensFAQResponse.getGetTokenOutput().get(0).getFarmerName().trim());
            }
            if (getTokensFAQResponse.getGetTokenOutput().get(0).getRegistrationNo() != null) {
                String mask = getTokensFAQResponse.getGetTokenOutput().get(0).getRegistrationNo().trim().replaceAll("\\w(?=\\w{4})", "*");
                binding.faqAadhaarNoTv.setText(mask);
            }
            if (getTokensFAQResponse.getGetTokenOutput().get(0).getFarmerFatherName() != null) {
                binding.faqFatherNameTv.setText(getTokensFAQResponse.getGetTokenOutput().get(0).getFarmerFatherName().trim());
            }
            if (getTokensFAQResponse.getGetTokenOutput().get(0).getMobile() != null) {
                binding.faqMobileTv.setText(getTokensFAQResponse.getGetTokenOutput().get(0).getMobile().trim());
            }
            if (getTokensFAQResponse.getGetTokenOutput().get(0).getFDistrictID() != null) {

                paddyTestRepository.getDistrictName(this,
                        getTokensFAQResponse.getGetTokenOutput().get(0).getFDistrictID().trim());
            }
            if (getTokensFAQResponse.getGetTokenOutput().get(0).getFDistrictID() != null
                    && getTokensFAQResponse.getGetTokenOutput().get(0).getFMandalID() != null) {
                paddyTestRepository.getMandalName(this,
                        getTokensFAQResponse.getGetTokenOutput().get(0).getFDistrictID().trim(),
                        getTokensFAQResponse.getGetTokenOutput().get(0).getFMandalID().trim());
            }
            if (getTokensFAQResponse.getGetTokenOutput().get(0).getFDistrictID() != null
                    && getTokensFAQResponse.getGetTokenOutput().get(0).getFMandalID() != null
                    && getTokensFAQResponse.getGetTokenOutput().get(0).getFVillageID() != null) {
                paddyTestRepository.getVillageName(this,
                        getTokensFAQResponse.getGetTokenOutput().get(0).getFDistrictID().trim(),
                        getTokensFAQResponse.getGetTokenOutput().get(0).getFMandalID().trim(),
                        getTokensFAQResponse.getGetTokenOutput().get(0).getFVillageID().trim());
            }

            if (getTokensFAQResponse.getGetTokenOutput().get(0).getApproximatePaddyType() != null) {
                paddyType = getTokensFAQResponse.getGetTokenOutput().get(0).getApproximatePaddyType();
                binding.faqPaddyTypeSp.setSelection(paddyType);
            }

//            for (int x = 0; x < paddyEntities.size(); x++) {
//                if (paddyType == paddyEntities.get(x).getGradeID()) {
//                    binding.faqPaddyTypeSp.setSelection(paddyType);
//                    break;
//                }
//            }
            if (getTokensFAQResponse.getGetTokenOutput().get(0).getApproxmateQuantity() != null) {
                binding.faqAproxQtyEt.setText(String.valueOf(getTokensFAQResponse.getGetTokenOutput().get(0).getApproxmateQuantity()));
            }

            clearQualityForm(binding.qualityLl);

            showFaqGradeAColoumn(false);
            showFaqCommonTypeColoumn(false);

            if (paddyType != 0) {
                if (paddyType == 1) {
                    binding.faqLowerClassGrainsGardeALlayout.setVisibility(View.VISIBLE);
                    showFaqGradeAColoumn(true);
                } else if (paddyType == 2) {
                    binding.faqLowerClassGrainsGardeALlayout.setVisibility(View.GONE);
                    showFaqCommonTypeColoumn(true);
                } else {
                    binding.faqLowerClassGrainsGardeALlayout.setVisibility(View.VISIBLE);
                    showFaqGradeAColoumn(true);
                    showFaqCommonTypeColoumn(true);
                }
            }

            getPaddyDetails(paddyType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPaddyDetails(int i) {
        try {
            for (int x = 0; x < paddyEntities.size(); x++) {
                if (i == paddyEntities.get(x).getGradeID()) {
                    binding.InorganicForeignMatterMaxTV.setText(String.valueOf(paddyEntities.get(x).getWastage()));
                    binding.OrganicForeignMatterMaxTV.setText(String.valueOf(paddyEntities.get(x).getOtherEatingGrains()));
                    binding.ImmatureShrunkenandShrivelledGrainsMaxTV.setText(String.valueOf(paddyEntities.get(x).getUnmaturedGrains()));
                    binding.DamagedDiscolouredSproutedandWeevilledGrainsMaxTV.setText(String.valueOf(paddyEntities.get(x).getDamagedGrains()));
                    binding.faqLowerClassGrains7TV.setText(String.valueOf(paddyEntities.get(x).getLowGradeGrain()));
                    binding.MoistureContentMaxTV.setText(String.valueOf(paddyEntities.get(x).getMoisture()));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearQualityForm(ViewGroup group) {
        try {
            for (int i = 0, count = group.getChildCount(); i < count; ++i) {
                View view = group.getChildAt(i);
                if (view instanceof EditText) {
                    ((EditText) view).setText("");
                    ((EditText) view).setError(null);
                }

                if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                    clearQualityForm((ViewGroup) view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFaqCommonTypeColoumn(boolean flag) {
        try {
            if (!flag) {
                binding.faqCommonValuesHeaderTv.setVisibility(View.GONE);
                binding.faqInOrganicMatterCommonLl.setVisibility(View.GONE);
                binding.faqOrganicMatterCommonLl.setVisibility(View.GONE);
                binding.faqImmatturedGrainsCommonLl.setVisibility(View.GONE);
                binding.faqDamegedGrainsCommonLl.setVisibility(View.GONE);
                binding.faqLowerClassGrainsCommonLl.setVisibility(View.GONE);
                binding.faqMoistureCommonLl.setVisibility(View.GONE);
            } else {
                binding.faqCommonValuesHeaderTv.setVisibility(View.VISIBLE);
                binding.faqInOrganicMatterCommonLl.setVisibility(View.VISIBLE);
                binding.faqOrganicMatterCommonLl.setVisibility(View.VISIBLE);
                binding.faqImmatturedGrainsCommonLl.setVisibility(View.VISIBLE);
                binding.faqDamegedGrainsCommonLl.setVisibility(View.VISIBLE);
                binding.faqLowerClassGrainsCommonLl.setVisibility(View.INVISIBLE);
                binding.faqMoistureCommonLl.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFaqGradeAColoumn(boolean flag) {
        try {
            if (!flag) {
                binding.faqGardeAValuesHeaderTv.setVisibility(View.GONE);
                binding.faqInOrganicMatterGardeALl.setVisibility(View.GONE);
                binding.faqOrganicMatterGardeALl.setVisibility(View.GONE);
                binding.faqImmatturedGrainsGardeALl.setVisibility(View.GONE);
                binding.faqDamegedGrainsGardeALl.setVisibility(View.GONE);
                binding.faqLowerClassGrainsGardeALlayout.setVisibility(View.GONE);
                binding.faqMoistureGardeALl.setVisibility(View.GONE);
            } else {
                binding.faqGardeAValuesHeaderTv.setVisibility(View.VISIBLE);
                binding.faqInOrganicMatterGardeALl.setVisibility(View.VISIBLE);
                binding.faqOrganicMatterGardeALl.setVisibility(View.VISIBLE);
                binding.faqImmatturedGrainsGardeALl.setVisibility(View.VISIBLE);
                binding.faqDamegedGrainsGardeALl.setVisibility(View.VISIBLE);
                binding.faqLowerClassGrainsGardeALlayout.setVisibility(View.VISIBLE);
                binding.faqMoistureGardeALl.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTokensFAQResponseData(GetTokensDDLResponse getTokensDDLResponse) {
        try {

            if (customProgressDialog != null && customProgressDialog.isShowing())
                customProgressDialog.dismiss();

            getTokensResponseList = getTokensDDLResponse;
            if (getTokensDDLResponse != null && getTokensDDLResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (getTokensDDLResponse.getGetDDLTokens() != null && getTokensDDLResponse.getGetDDLTokens().size() > 0) {
                    ArrayList<String> getTokens = new ArrayList<>();
                    getTokens.add(0, "--Select--");
                    for (int i = 0; i < getTokensDDLResponse.getGetDDLTokens().size(); i++) {
                        getTokens.add(getTokensDDLResponse.getGetDDLTokens().get(i).getTokenNo());
                    }
                    if (getTokens.size() > 0) {
                        Utils.loadSpinnerData(getActivity(), getTokens, binding.faqTokenNoSp);
                    }
                } else {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.FAQ),
                            getTokensDDLResponse.getResponseMessage(),
                            getString(R.string.ERROR), false);
                }
            } else if (getTokensDDLResponse != null &&
                    getTokensDDLResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FAQ),
                        getTokensDDLResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else if (getTokensDDLResponse != null &&
                    getTokensDDLResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FAQ),
                        getTokensDDLResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (getTokensDDLResponse != null && getTokensDDLResponse.getStatusCode() != null
                    && getTokensDDLResponse.getStatusCode() == AppConstants.VERSION_CODE) {

                Utils.callPlayAlert(getTokensDDLResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else if (getTokensDDLResponse != null && getTokensDDLResponse.getStatusCode() != null &&
                    getTokensDDLResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.FAQ),
                        getTokensDDLResponse.getResponseMessage(), getFragmentManager());
            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FAQ),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.FAQ),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }

    }


    @Override
    public void GetSelectedTokenData(GetTokensResponse getTokensFAQResponseData) {
        try {

            if (getTokensFAQResponseData != null && getTokensFAQResponseData.getStatusCode() == AppConstants.SUCCESS_CODE) {

                if (getTokensFAQResponseData.getGetTokenOutput() != null && getTokensFAQResponseData.getGetTokenOutput().size() > 0) {

                    getTokenOutputMain = getTokensFAQResponseData.getGetTokenOutput().get(0);

                    binding.faqMainLL.setVisibility(View.VISIBLE);
                    setSelectedTokenData(getTokensFAQResponseData);

                } else {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.FAQ),
                            getTokensFAQResponseData.getResponseMessage(),
                            getString(R.string.ERROR), false);
                }
            } else if (getTokensFAQResponseData != null &&
                    getTokensFAQResponseData.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FAQ),
                        getTokensFAQResponseData.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else if (getTokensFAQResponseData != null &&
                    getTokensFAQResponseData.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FAQ),
                        getTokensFAQResponseData.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (getTokensFAQResponseData != null &&
                    getTokensFAQResponseData.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.FAQ),
                        getTokensFAQResponseData.getResponseMessage(), getFragmentManager());
            } else if (getTokensFAQResponseData != null && getTokensFAQResponseData.getStatusCode() != null
                    && getTokensFAQResponseData.getStatusCode() == AppConstants.VERSION_CODE) {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();

                Utils.callPlayAlert(getTokensFAQResponseData.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FAQ),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.FAQ),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
    }

    @Override
    public void FAQSubmit(FAQSubmitResponse faqSubmitResponse) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
        try {
            if (faqSubmitResponse != null && faqSubmitResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {

                Fragment fragment = new DashboardFragment();
                Utils.showAlertNavigateToFarmer1(getActivity(), fragment, getFragmentManager(),
                        getResources().getString(R.string.FarmerRegistration),
                        faqSubmitResponse.getResponseMessage(), true);

            } else if (faqSubmitResponse != null &&
                    faqSubmitResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FAQ),
                        faqSubmitResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (faqSubmitResponse != null &&
                    faqSubmitResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FAQ),
                        faqSubmitResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else if (faqSubmitResponse != null && faqSubmitResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.FAQ),
                        faqSubmitResponse.getResponseMessage(), getFragmentManager());
            } else if (faqSubmitResponse != null && faqSubmitResponse.getStatusCode() != null
                    && faqSubmitResponse.getStatusCode() == AppConstants.VERSION_CODE) {

                Utils.callPlayAlert(faqSubmitResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.FAQ),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.FAQ),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }
    }

    private void clearForm(ViewGroup group) {
        try {
            for (int i = 0, count = group.getChildCount(); i < count; ++i) {
                View view = group.getChildAt(i);
                if (view instanceof EditText) {
                    ((EditText) view).setText("");
                    ((EditText) view).setError(null);
                }
                if (view instanceof TextView) {
                    int id = view.getId();
                    if (id == R.id.faq_tokenNo_Tv || id == R.id.faq_regId_Tv || id == R.id.faq_farmerName_Tv || id == R.id.faq_aadhaarNo_Tv || id == R.id.faq_fatherName_Tv
                            || id == R.id.faq_mobile_Tv || id == R.id.faq_district_Tv || id == R.id.faq_mandal_Tv || id == R.id.faq_village_Tv
                            || id == R.id.InorganicForeignMatter_MaxTV || id == R.id.OrganicForeignMatter_MaxTV || id == R.id.ImmatureShrunkenandShrivelledGrains_MaxTV
                            || id == R.id.DamagedDiscolouredSproutedandWeevilledGrains_MaxTV || id == R.id.faq_LowerClassGrains7_TV ||
                            id == R.id.MoistureContent_MaxTV || id == R.id.faq_paddyType_Sp || id == R.id.faq_AproxQty_Et || id == R.id.faq_AproxQty_Et)
                        ((TextView) view).setText("");

                }
                if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                    clearForm((ViewGroup) view);
            }
            binding.faqTestRD.setChecked(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getTokenID(String tokenNo, GetTokensDDLResponse getTokensResponseList) {
        long tokenID = 0;
        try {
            for (int i = 0; i < getTokensResponseList.getGetDDLTokens().size(); i++) {
                if (getTokensResponseList.getGetDDLTokens().get(i).getTokenNo().equals(tokenNo)) {
                    tokenID = getTokensResponseList.getGetDDLTokens().get(i).getTokenID();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tokenID;
    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                switch (v.getId()) {
                    case R.id.faqTest_RD:
                        getPaddyDetails(1);
                        break;
                    case R.id.ursTestRD:
                        getPaddyDetails(2);
                        break;

                    case R.id.faq_Submit_Btn:
                        FAQRequest faqRequest = new FAQRequest();
                        StringBuilder stringBuilder = new StringBuilder();
                        if (getTokenOutputMain != null) {
                            if (validatePaddyType(getTokenOutputMain, faqRequest, stringBuilder)) {
                                ShowSubmitFaqAlert(getTokenOutputMain, faqRequest, stringBuilder);
                            }
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private boolean validatePaddyType(GetTokenOutput getTokenOutput, FAQRequest faqRequest, StringBuilder msg) {

        FaqXMLData faqXMLData = new FaqXMLData();
        String createdDate = Utils.getCurrentDateTime();


        faqXMLData.setTokenNo(getTokenOutput.getTokenNo());
        faqXMLData.setTokenID(String.valueOf(getTokenOutput.getTokenID()));
        faqXMLData.setRegistrationNo(getTokenOutput.getRegistrationNo());
        faqXMLData.setRegistrationID(String.valueOf(getTokenOutput.getRegID()));

        String actInOrganicMatter = binding.InorganicForeignMatterMaxTV.getText().toString();
        String actOrganicMatter = binding.OrganicForeignMatterMaxTV.getText().toString();
        String actImmatturedGrains = binding.ImmatureShrunkenandShrivelledGrainsMaxTV.getText().toString();
        String actDammegedGrains = binding.DamagedDiscolouredSproutedandWeevilledGrainsMaxTV.getText().toString();
        String actLowerClassGrains = binding.faqLowerClassGrains7TV.getText().toString();
        String actMoisture = binding.MoistureContentMaxTV.getText().toString();


        boolean gradeA = false, common = false;

        int paddyType = binding.faqPaddyTypeSp.getSelectedItemPosition();

        if (paddyType == 0) {
            showAlertError();
            return false;
        }

        if (paddyType == 1) {
            gradeA = true;
            faqXMLData.setPaddyType(String.valueOf(paddyType));
        } else if (paddyType == 2) {
            common = true;
            faqXMLData.setPaddyType(String.valueOf(paddyType));
        } else {
            gradeA = true;
            common = true;
            faqXMLData.setPaddyType(String.valueOf(paddyType));
        }


        //FAQ Submission - As per Govt. Farmer can sell 40 quintals paddy/1 Acre. This farmer procured

        String paddyQty = binding.faqAproxQtyEt.getText().toString();
        if (TextUtils.isEmpty(paddyQty) || paddyQty.equalsIgnoreCase("0")) {
            setEditTextData(binding.faqAproxQtyEt, getString(R.string.app_qty_not_rec));
            return false;
        } else {
            faqXMLData.setTotalQuantity(paddyQty);
        }


        if (binding.faqTestRD.isChecked()) {
            faqXMLData.setFAQ_Type(String.valueOf(1));
        } else if (binding.ursTestRD.isChecked()) {
            faqXMLData.setFAQ_Type(String.valueOf(2));
        }

        msg.append(R.string.paddy_acc);
        msg.append("\n------------------------------------------------");


        boolean InOrganicMatterStatus = true, OrganicMatterStatus = true, ImmatturedGrainsStatus = true;
        boolean DammegedGrainsStatus = true, LowerClassGrainsStatus = true, MoistureStatus = true;


        if (gradeA) {
            String InOrganicMatter = binding.faqInOrganicMatterGardeAEt.getText().toString();
            if (TextUtils.isEmpty(InOrganicMatter)) {
                setEditTextData(binding.faqInOrganicMatterGardeAEt, getResources().getString(R.string.valid_in_org));
                return false;
            }else if (InOrganicMatter.trim().equalsIgnoreCase(".")) {
                setEditTextData(binding.faqInOrganicMatterGardeAEt, getString(R.string.valid_in_org));
                return false;
            } else if (Float.parseFloat(InOrganicMatter) > Float.parseFloat(actInOrganicMatter)) {
                InOrganicMatterStatus = false;
            }
            faqXMLData.setInOrganicForeignMatter(InOrganicMatter);
        }

        if (common) {
            String InOrganicMatter = binding.faqInOrganicMatterCommonEt.getText().toString();
            if (TextUtils.isEmpty(InOrganicMatter)) {
                setEditTextData(binding.faqInOrganicMatterCommonEt, getResources().getString(R.string.valid_in_org));
                return false;
            }else if (InOrganicMatter.trim().equalsIgnoreCase(".")) {
                setEditTextData(binding.faqInOrganicMatterCommonEt, getString(R.string.valid_in_org));
                return false;
            }  else if (Float.parseFloat(InOrganicMatter) > Float.parseFloat(actInOrganicMatter)) {
                InOrganicMatterStatus = false;
            }
            faqXMLData.setInOrganicForeignMatter(InOrganicMatter);

        }


        if (gradeA) {
            String OrganicMatter = binding.faqOrganicMatterGardeAEt.getText().toString();
            if (TextUtils.isEmpty(OrganicMatter)) {
                setEditTextData(binding.faqOrganicMatterGardeAEt, getString(R.string.valid_org));
                return false;
            }else if (OrganicMatter.trim().equalsIgnoreCase(".")) {
                setEditTextData(binding.faqOrganicMatterGardeAEt, getString(R.string.valid_org));
                return false;
            } else if (Float.parseFloat(OrganicMatter) > Float.parseFloat(actOrganicMatter)) {
                OrganicMatterStatus = false;
            }
            faqXMLData.setOrganicForeignMatter(OrganicMatter);

        }

        if (common) {
            String OrganicMatter = binding.faqOrganicMatterCommonEt.getText().toString();
            if (TextUtils.isEmpty(OrganicMatter)) {
                setEditTextData(binding.faqOrganicMatterCommonEt, getString(R.string.valid_org));
                return false;
            }else if (OrganicMatter.trim().equalsIgnoreCase(".")) {
                setEditTextData(binding.faqOrganicMatterCommonEt, getString(R.string.valid_org));
                return false;
            } else if (Float.parseFloat(OrganicMatter) > Float.parseFloat(actOrganicMatter)) {
                OrganicMatterStatus = false;
            }
            faqXMLData.setOrganicForeignMatter(OrganicMatter);
        }

        if (gradeA) {
            String ImmatturedGrains = binding.faqImmatturedGrainsGardeAEt.getText().toString();
            if (TextUtils.isEmpty(ImmatturedGrains)) {
                setEditTextData(binding.faqImmatturedGrainsGardeAEt, getString(R.string.valid_imm));
                return false;
            }else if (ImmatturedGrains.trim().equalsIgnoreCase(".")) {
                setEditTextData(binding.faqImmatturedGrainsGardeAEt, getString(R.string.valid_imm));
                return false;
            } else if (Float.parseFloat(ImmatturedGrains) > Float.parseFloat(actImmatturedGrains)) {
                ImmatturedGrainsStatus = false;
            }
            faqXMLData.setImmaturedGrains(ImmatturedGrains);
        }

        if (common) {
            String ImmatturedGrains = binding.faqImmatturedGrainsCommonEt.getText().toString();
            if (TextUtils.isEmpty(ImmatturedGrains)) {
                setEditTextData(binding.faqImmatturedGrainsCommonEt, getString(R.string.valid_imm));
                return false;
            }else if (ImmatturedGrains.trim().equalsIgnoreCase(".")) {
                setEditTextData(binding.faqImmatturedGrainsCommonEt, getString(R.string.valid_imm));
                return false;
            } else if (Float.parseFloat(ImmatturedGrains) > Float.parseFloat(actImmatturedGrains)) {
                ImmatturedGrainsStatus = false;
            }
            faqXMLData.setImmaturedGrains(ImmatturedGrains);
        }

        if (gradeA) {
            String DammegedGrains = binding.faqDamegedGrainsGardeAEt.getText().toString();
            if (TextUtils.isEmpty(DammegedGrains)) {
                setEditTextData(binding.faqDamegedGrainsGardeAEt, getString(R.string.valid_damaged));
                return false;
            }else if (DammegedGrains.trim().equalsIgnoreCase(".")) {
                setEditTextData(binding.faqDamegedGrainsGardeAEt, getString(R.string.valid_damaged));
                return false;
            }  else if (Float.parseFloat(DammegedGrains) > Float.parseFloat(actDammegedGrains)) {
                DammegedGrainsStatus = false;
            }
            faqXMLData.setDamagedGrains(DammegedGrains);
        }

        if (common) {
            String DammegedGrains = binding.faqDamegedGrainsCommonEt.getText().toString();
            if (TextUtils.isEmpty(DammegedGrains)) {
                setEditTextData(binding.faqDamegedGrainsCommonEt, getString(R.string.valid_damaged));
                return false;
            } else if (DammegedGrains.trim().equalsIgnoreCase(".")) {
                setEditTextData(binding.faqDamegedGrainsCommonEt, getString(R.string.valid_damaged));
                return false;
            }else if (Float.parseFloat(DammegedGrains) > Float.parseFloat(actDammegedGrains)) {
                DammegedGrainsStatus = false;
            }
            faqXMLData.setDamagedGrains(DammegedGrains);
        }


        String LowerClassGrains = binding.faqLowerClassGrainsGardeAEt.getText().toString();
        if (gradeA) {
            if (TextUtils.isEmpty(LowerClassGrains)) {
                setEditTextData(binding.faqLowerClassGrainsGardeAEt, getString(R.string.valid_lower));
                return false;
            } else if (LowerClassGrains.trim().equalsIgnoreCase(".")) {
                setEditTextData(binding.faqLowerClassGrainsGardeAEt, getString(R.string.valid_lower));
                return false;
            } else if (Float.parseFloat(LowerClassGrains) > Float.parseFloat(actLowerClassGrains)) {
                LowerClassGrainsStatus = false;
            }
            faqXMLData.setLowGradeGrain(LowerClassGrains);

        }
        if (common) {
            faqXMLData.setLowGradeGrain("0");
        }

        if (gradeA) {
            String Moisture = binding.faqMoistureGardeAEt.getText().toString();
            if (TextUtils.isEmpty(Moisture)) {
                setEditTextData(binding.faqMoistureGardeAEt, getString(R.string.valid_mos));
                return false;
            }else if (Moisture.trim().equalsIgnoreCase(".")) {
                setEditTextData(binding.faqMoistureGardeAEt, getString(R.string.valid_mos));
                return false;
            }  else if (Float.parseFloat(Moisture) > Float.parseFloat(actMoisture)) {
                MoistureStatus = false;
            }
            faqXMLData.setMoisture(Moisture);

        }

        if (common) {
            String Moisture = binding.faqMoistureCommonEt.getText().toString();
            if (TextUtils.isEmpty(Moisture)) {
                setEditTextData(binding.faqMoistureCommonEt, getString(R.string.valid_mos));
                return false;
            }else if (Moisture.trim().equalsIgnoreCase(".")) {
                setEditTextData(binding.faqMoistureCommonEt, getString(R.string.valid_mos));
                return false;
            } else if (Float.parseFloat(Moisture) > Float.parseFloat(actMoisture)) {
                MoistureStatus = false;
            }
            faqXMLData.setMoisture(Moisture);

        }

        if (gradeA) {
            if (InOrganicMatterStatus && OrganicMatterStatus && DammegedGrainsStatus && ImmatturedGrainsStatus && LowerClassGrainsStatus && MoistureStatus) {
                gradeAStatus = "Y";
                msg.append("\n GradeA : Accepted");
                faqXMLData.setFAQ_Status(gradeAStatus);
            } else {
                gradeAStatus = "N";
                msg.append("\n Grade A : Rejected");
                faqXMLData.setFAQ_Status(gradeAStatus);
            }
        }

        if (common) {
            if (InOrganicMatterStatus && OrganicMatterStatus && DammegedGrainsStatus && ImmatturedGrainsStatus && LowerClassGrainsStatus && MoistureStatus) {
                commonStatus = "Y";
                msg.append("\n Common : Accepted");
                faqXMLData.setFAQ_Status(commonStatus);
            } else {
                commonStatus = "N";
                msg.append("\n Common : Rejected");
                faqXMLData.setFAQ_Status(commonStatus);
            }
        }

        faqXMLData.setCreatedDate(createdDate);
        faqXMLData.setFAQ_Date(createdDate);
        faqRequest.setFaqXMLData(faqXMLData);

        return true;
    }


    AdapterView.OnItemSelectedListener onItemClick = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            try {
                switch (adapterView.getId()) {
                    case R.id.faq_token_no_Sp:
                        if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                            clearForm(binding.faqMainLL);
                            binding.faqMainLL.setVisibility(View.GONE);
                            String tokenNum = binding.faqTokenNoSp.getSelectedItem().toString().trim();
                            if (getTokensResponseList != null && getTokensResponseList.getGetDDLTokens().size() > 0 &&
                                    !TextUtils.isEmpty(tokenNum) && !tokenNum.equalsIgnoreCase("--Select--")) {
                                long tokenID = getTokenID(tokenNum, getTokensResponseList);
                                getGeneratedTokens(tokenID);
                            }
                        } else {
                            if (getActivity() != null) {
                                Utils.customAlert(getActivity(),
                                        getResources().getString(R.string.FAQ),
                                        getResources().getString(R.string.no_internet),
                                        getResources().getString(R.string.WARNING), false);
                            } else {
                                Utils.customAlert(getContext(),
                                        getResources().getString(R.string.FAQ),
                                        getResources().getString(R.string.no_internet),
                                        getResources().getString(R.string.WARNING), false);
                            }
                        }

                        break;

                    case R.id.faq_paddyType_Sp:
                        clearQualityForm(binding.qualityLl);
                        int paddyType = binding.faqPaddyTypeSp.getSelectedItemPosition();
                        showFaqGradeAColoumn(false);
                        showFaqCommonTypeColoumn(false);
                        if (paddyType != 0) {
                            binding.qualityLl.setVisibility(View.VISIBLE);
                            if (paddyType == 1) {
                                binding.faqLowerClassGrainsGardeALlayout.setVisibility(View.VISIBLE);
                                showFaqGradeAColoumn(true);
                            } else if (paddyType == 2) {
                                binding.faqLowerClassGrainsGardeALlayout.setVisibility(View.GONE);
                                showFaqCommonTypeColoumn(true);
                            } else {
                                binding.faqLowerClassGrainsGardeALlayout.setVisibility(View.VISIBLE);
                                showFaqGradeAColoumn(true);
                                showFaqCommonTypeColoumn(true);
                            }
                        } else {
                            binding.qualityLl.setVisibility(View.GONE);
                        }
                        break;

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void showAlertError() {
        Utils.customAlert(getActivity(),
                getResources().getString(R.string.FAQ),
                getString(R.string.paady_not_sel),
                getString(R.string.INFORMATION), false);
    }

    private void setEditTextData(View view, String errorMsg) {
        try {
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                editText.setError(errorMsg);
                editText.requestFocus();
            }

            if (view instanceof AutoCompleteTextView) {
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view;
                autoCompleteTextView.setError(errorMsg);
                autoCompleteTextView.requestFocus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void getAllPaddyValues(List<PaddyEntity> paddyEntities) {
        try {
            if (paddyEntities!=null && paddyEntities.size() > 0) {
                this.paddyEntities = paddyEntities;


                if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                    getGeneratedTokens(-1);
                } else {
                    if (getActivity() != null) {
                        Utils.customAlert(getActivity(),
                                getResources().getString(R.string.FAQ),
                                getResources().getString(R.string.no_internet),
                                getResources().getString(R.string.WARNING), false);
                    } else {
                        Utils.customAlert(getContext(),
                                getResources().getString(R.string.FAQ),
                                getResources().getString(R.string.no_internet),
                                getResources().getString(R.string.WARNING), false);
                    }
                }
            } else {
                Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                        getResources().getString(R.string.FAQ),
                        getString(R.string.no_paddy), true);

            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paddyTestCount(int cnt) {

    }

    @Override
    public void getDistName(String distName) {
        binding.faqDistrictTv.setText(distName);
    }

    @Override
    public void getManName(String manName) {
        binding.faqMandalTv.setText(manName);
    }

    @Override
    public void getVilName(String vilName) {
        binding.faqVillageTv.setText(vilName);
    }

    private void ShowSubmitFaqAlert(GetTokenOutput getTokenOutput, FAQRequest faqRequest, StringBuilder stringBuilder) {
        try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.dataentry_dialog);
                dialog.setCancelable(false);
                TextView msgTv = dialog.findViewById(R.id.proceedText);
                TextView tokenNumber = dialog.findViewById(R.id.tokenNumber);
                TextView PaddyType = dialog.findViewById(R.id.PaddyType);
                final EditText remarksEt = dialog.findViewById(R.id.message_EntryData_Et);
                final ScrollView scrollView = dialog.findViewById(R.id.scr);
                scrollView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        Utils.hideKeyboard(view);
                        return false;
                    }
                });
                if (gradeAStatus.equals("N") || commonStatus.equals("N")) {
                    remarksEt.setVisibility(View.VISIBLE);
                } else {
                    remarksEt.setVisibility(View.GONE);
                }
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                tokenNumber.setText(getResources().getString(R.string.tokendetails) + ": " + getTokenOutput.getTokenNo());
                PaddyType.setText(getResources().getString(R.string.paddy_type) + ": " + binding.faqPaddyTypeSp.getSelectedItem().toString());
                if (gradeAStatus.equals("N") || commonStatus.equals("N")) {
                    msgTv.setText(getString(R.string.rej));
                } else {
                    msgTv.setText(getString(R.string.acc));
                }

                Button yes = dialog.findViewById(R.id.ok_button);
                Button cancel = dialog.findViewById(R.id.cancel_button);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (gradeAStatus.equals("N") || commonStatus.equals("N")) {
                            remarks = remarksEt.getText().toString().trim();
                            if (remarks.equalsIgnoreCase("")) {
                                remarksEt.requestFocus();
                                remarksEt.setError(getString(R.string.remarks_entry));
                                return;
                            } else {
                                faqRequest.getFaqXMLData().setRemarks(remarks);
                            }


                        }
                        dialog.dismiss();
                        faqRequest.setpPCID(String.valueOf(ppcUserDetails.getPPCID()));
                        faqRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                        StringBuilder str = getFaqSubmitReq(faqRequest);
                        faqRequest.setXmlFAQData(str.toString());


                        if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                            if (customProgressDialog != null)
                                customProgressDialog.show();
                            fAQPresenter.FAQSubmit(faqRequest);
                        } else {
                            Utils.customAlert(getActivity(),
                                    getResources().getString(R.string.FAQ),
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

                if (!dialog.isShowing())
                    dialog.show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private StringBuilder getFaqSubmitReq(FAQRequest faqRequest) {
        final StringBuilder xmlDoc = new StringBuilder();
        xmlDoc.append("<FarmerFAQDetails>");
        xmlDoc.append("<ppc>");
        xmlDoc.append("<ppccode>").append(ppcUserDetails.getPPCCode()).append("</ppccode>").append("<ppcid>").append(ppcUserDetails.getPPCID()).append("</ppcid>").append("<SystemIP>").append(ppcUserDetails.getSeasonID()).append("</SystemIP>").append("<seasonID>").append(ppcUserDetails.getSeasonID()).append("</seasonID>");
        xmlDoc.append("</ppc>");
        xmlDoc.append("<Registration>");
        xmlDoc.append("<TokenNo>").append(faqRequest.getFaqXMLData().getTokenNo()).append("</TokenNo>").append("<TokenID>").append(faqRequest.getFaqXMLData().getTokenID()).append("</TokenID>").append("<RegistrationNo>").append(faqRequest.getFaqXMLData().getRegistrationNo()).append("</RegistrationNo>").append("<RegistrationID>").append(faqRequest.getFaqXMLData().getRegistrationID()).append("</RegistrationID>");
        xmlDoc.append("</Registration>");
        xmlDoc.append("<FAQ>");
        xmlDoc.append("<FAQ_Type>").append(faqRequest.getFaqXMLData().getFAQ_Type()).append("</FAQ_Type>").append("<PaddyType>").append(faqRequest.getFaqXMLData().getPaddyType()).append("</PaddyType>").append("<FAQ_Status>").append(faqRequest.getFaqXMLData().getFAQ_Status()).append("</FAQ_Status>")
                .append("<InOrganicForeignMatter>").append(faqRequest.getFaqXMLData().getInOrganicForeignMatter()).append("</InOrganicForeignMatter>")
                .append("<OrganicForeignMatter>").append(faqRequest.getFaqXMLData().getOrganicForeignMatter()).append("</OrganicForeignMatter>")
                .append("<ImmaturedGrains>").append(faqRequest.getFaqXMLData().getImmaturedGrains()).append("</ImmaturedGrains>")
                .append("<DamagedGrains>").append(faqRequest.getFaqXMLData().getDamagedGrains()).append("</DamagedGrains>")
                .append("<LowGradeGrain>").append(faqRequest.getFaqXMLData().getLowGradeGrain()).append("</LowGradeGrain>")
                .append("<Moisture>").append(faqRequest.getFaqXMLData().getMoisture()).append("</Moisture>")
                .append("<Remarks>").append(faqRequest.getFaqXMLData().getRemarks()).append("</Remarks>")
                .append("<TotalQuantity>").append(faqRequest.getFaqXMLData().getTotalQuantity()).append("</TotalQuantity>")
                .append("<FAQ_Date>").append(faqRequest.getFaqXMLData().getFAQ_Date()).append("</FAQ_Date>")
                .append("<CreatedDate>").append(faqRequest.getFaqXMLData().getCreatedDate()).append("</CreatedDate>");
        xmlDoc.append("</FAQ>");
        xmlDoc.append("</FarmerFAQDetails>");
        return xmlDoc;
    }

    @Override
    public void getAllDistrictEntities(List<DistrictEntity> districtEntities) {
        try {
            if (districtEntities.size() > 0) {
                dmvRepository.getAllMandals(this);
            } else {
                Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                        getResources().getString(R.string.FAQ),
                        getString(R.string.no_districts), true);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getAllMandalEntities(List<MandalEntity> mandalEntities) {
        try {
            if (mandalEntities.size() > 0) {
                dmvRepository.getAllVillages(this);
            } else {
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
            if (villageEntities.size() > 0) {
                paddyTestRepository.getAllPaddyValues(this);
            } else {
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
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (customProgressDialog != null) {
                customProgressDialog = null;
            }
            if (fAQPresenter != null) {
                fAQPresenter.detachView();
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
                    getResources().getString(R.string.FAQ),
                    msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else if (code == AppConstants.EXCEPTION_CODE) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.FAQ),
                    getString(R.string.something) +
                            msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.FAQ),
                    getString(R.string.something),
                    getResources().getString(R.string.ERROR),
                    false);
        }
    }
}

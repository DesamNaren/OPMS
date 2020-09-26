package com.cgg.pps.fragment;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.GunnysGivenFragmentBinding;
import com.cgg.pps.interfaces.DMVInterface;
import com.cgg.pps.interfaces.GunnysGivenInterface;
import com.cgg.pps.interfaces.PaddyTestInterface;
import com.cgg.pps.model.request.farmer.gettokens.GetTokensRequest;
import com.cgg.pps.model.request.gunnydetails.GunnyDetailsRequest;
import com.cgg.pps.model.request.gunnydetails.GunnyXMLData;
import com.cgg.pps.model.request.gunnydetails.GunnysSubmitRequest;
import com.cgg.pps.model.response.devicemgmt.masterdistrict.DistrictEntity;
import com.cgg.pps.model.response.devicemgmt.mastermandal.MandalEntity;
import com.cgg.pps.model.response.devicemgmt.mastervillage.VillageEntity;
import com.cgg.pps.model.response.devicemgmt.paddyvalues.PaddyEntity;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokenOutput;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensDDLResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
import com.cgg.pps.model.response.gunnydetails.GunnyDetailsResponse;
import com.cgg.pps.model.response.gunnydetails.GunnyResponse;
import com.cgg.pps.model.response.gunnydetails.gunnysubmit.GunnySubmitResponse;
import com.cgg.pps.model.response.socialstatus.SocialEntity;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.presenter.GunnysGivenPresenter;
import com.cgg.pps.room.repository.DMVRepository;
import com.cgg.pps.room.repository.PaddyTestRepository;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.ConnectionDetector;
import com.cgg.pps.util.CustomProgressDialog;
import com.cgg.pps.util.Utils;
import com.cgg.pps.view.DashboardActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GunnysGivenFragment extends Fragment implements GunnysGivenInterface, PaddyTestInterface, DMVInterface {
    private GunnysGivenFragmentBinding binding;
    private GunnysGivenPresenter gunnysGivenPresenter;
    private PPCUserDetails ppcUserDetails;
    private PaddyTestRepository paddyTestRepository;
    private GetTokensDDLResponse getTokensResponseList;
    private ArrayList<View> gunnyOrderTableRowsArrayList;
    private GunnyDetailsResponse gunnyOrderResponseListGlobal;
    private GetTokenOutput getTokenOutputGlobal;
    CustomProgressDialog customProgressDialog;
    private DMVRepository dmvRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.gunnys_given_fragment, container, false);
        gunnysGivenPresenter = new GunnysGivenPresenter();
        gunnysGivenPresenter.attachView(this);
        dmvRepository = new DMVRepository(getActivity());
        customProgressDialog = new CustomProgressDialog(getActivity());
        paddyTestRepository = new PaddyTestRepository(getActivity());
        getPreferenceData();
        binding.gunnyTokenNoSp.setOnItemSelectedListener(onItemClick);
        binding.GunnyAddNewRowBtn.setOnClickListener(onClick);
        binding.GunnyDelRowBtn.setOnClickListener(onClick);
        binding.GtoFSubmitBtn.setOnClickListener(onClick);
        gunnyOrderTableRowsArrayList = new ArrayList<>();
        binding.GunnyOrderTl.removeAllViews();


        dmvRepository.getAllDistricts(this);


        binding.rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utils.hideKeyboard(view);
                return false;
            }
        });

        return binding.getRoot();
    }

    public void onResume() {
        super.onResume();

        try {
            if (getActivity() != null) {
                ((DashboardActivity) getActivity())
                        .setActionBarTitle(getResources().getString(R.string.GunnysGivenToFarmer));
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }


    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                switch (v.getId()) {
                    case R.id.Gunny_addNewRow_Btn:
                        if (gunnyOrderResponseListGlobal != null &&
                                gunnyOrderResponseListGlobal.getGunnyResponse() != null
                                && gunnyOrderResponseListGlobal.getGunnyResponse().size() > 0) {
                            addRowToGunnyOrderTable(gunnyOrderResponseListGlobal);
                        }
                        break;
                    case R.id.Gunny_delRow_Btn:
                        deleteRowToGunnyOrderTable();
                        break;

                    case R.id.GtoF_Submit_Btn:
                        GunnysSubmitRequest gunnysSubmitRequest = new GunnysSubmitRequest();
                        if (validateData(gunnysSubmitRequest)) {
                            gunnysSubmitRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                            gunnysSubmitRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));

                            gunnysSubmitRequest.setTokenNo(getTokenOutputGlobal.getTokenNo());
                            gunnysSubmitRequest.setTokenID(String.valueOf(getTokenOutputGlobal.getTokenID()));
                            gunnysSubmitRequest.setRegistrationNo(getTokenOutputGlobal.getRegistrationNo());
                            gunnysSubmitRequest.setRegistrationID(String.valueOf(getTokenOutputGlobal.getRegID()));
                            gunnysSubmitRequest.setTransactionID(getTokenOutputGlobal.getFarmerTransactionID());
                            gunnysSubmitRequest.setTransactionRowID(String.valueOf(getTokenOutputGlobal.getFarmerTransactionRowID()));

                            StringBuilder finalString = gunnySubmitData(gunnysSubmitRequest);
                            if (finalString != null) {
                                gunnysSubmitRequest.setXmlIssuingGunnyData(String.valueOf(finalString));
                                ShowSubmitGunnyAlert(gunnysSubmitRequest);
                            }
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void ShowSubmitGunnyAlert(GunnysSubmitRequest gunnysSubmitRequest) {

        try {
            if (getActivity() != null) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                if (dialog.getWindow() != null) {
                    dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.procurement_submit_confirmation_dialog);
                    dialog.setCancelable(false);
                    if (!dialog.isShowing())
                        dialog.show();
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    TextView titleTv = dialog.findViewById(R.id.alert_titleTv);
                    titleTv.setText(getString(R.string.gunny_cnf));
                    Button yes = dialog.findViewById(R.id.paddyProcuremtProceedBtn);
                    Button cancel = dialog.findViewById(R.id.cancelBtn);
                    TextView farmerRegID = dialog.findViewById(R.id.farmerRegID);
                    TextView mobileNo = dialog.findViewById(R.id.mobileNo);
                    TextView Newbags = dialog.findViewById(R.id.Newbags);
                    TextView oldBags = dialog.findViewById(R.id.oldBags);
                    TextView Totalbags = dialog.findViewById(R.id.Totalbags);
                    TextView totalQuantity = dialog.findViewById(R.id.totalQuantity);
                    TextView MSPAmount = dialog.findViewById(R.id.MSPAmount);
                    TextView totalAmount = dialog.findViewById(R.id.totalAmount);
                    farmerRegID.setVisibility(View.GONE);
                    mobileNo.setVisibility(View.GONE);
                    totalQuantity.setVisibility(View.GONE);
                    MSPAmount.setVisibility(View.GONE);
                    totalAmount.setVisibility(View.GONE);
                    int totalNewbags = 0, toalOldBags = 0, TotalNewOldbags = 0;
                    for (int i = 0; i < gunnysSubmitRequest.getGunnyXMLDataArrayList().size(); i++) {
                        totalNewbags = totalNewbags + Integer.parseInt(gunnysSubmitRequest.getGunnyXMLDataArrayList().get(i).getTokenNewBags());
                        toalOldBags = toalOldBags + Integer.parseInt(gunnysSubmitRequest.getGunnyXMLDataArrayList().get(i).getTokenOldBags());
                        TotalNewOldbags = TotalNewOldbags + Integer.parseInt(gunnysSubmitRequest.getGunnyXMLDataArrayList().get(i).getTokenTotalBags());
                    }
                    Newbags.setText(getResources().getString(R.string.newbags_given_to_farmer) + ": " + totalNewbags);
                    oldBags.setText(getResources().getString(R.string.oldbags_given_to_farmer) + ": " + toalOldBags);
                    Totalbags.setText(getResources().getString(R.string.TotalBags) + ": " + TotalNewOldbags);
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                                if (customProgressDialog != null && !customProgressDialog.isShowing())
                                    customProgressDialog.show();
                                gunnysGivenPresenter.GunnySubmitCall(gunnysSubmitRequest);
                            } else {
                                Utils.customAlert(getActivity(),
                                        getResources().getString(R.string.GunnysGivenToFarmer),
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
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private StringBuilder gunnySubmitData(GunnysSubmitRequest gunnysSubmitRequest) {

        String tokenNum = gunnysSubmitRequest.getTokenNo();
        String tokenID = gunnysSubmitRequest.getTokenID();
        String ppcCode = ppcUserDetails.getPPCCode();
        String ppcID = String.valueOf(ppcUserDetails.getPPCID());
        String systemIP = ppcUserDetails.getPPCCode();
        String seasonID = String.valueOf(ppcUserDetails.getSeasonID());
        String regNo = gunnysSubmitRequest.getRegistrationNo();
        String regId = gunnysSubmitRequest.getRegistrationID();
        String trnId = gunnysSubmitRequest.getTransactionID();
        String trnRowId = gunnysSubmitRequest.getTransactionRowID();


        final StringBuilder xmlDoc = new StringBuilder();
        xmlDoc.append("<FarmerIssuingGunnyDetails>");
        xmlDoc.append("<ppc>");
        xmlDoc.append("<ppccode>" + ppcCode + "</ppccode>"
                + "<ppcid>" + ppcID + "</ppcid>"
                + "<SystemIP>" + systemIP + "</SystemIP>"
                + "<seasonID>" + seasonID + "</seasonID>"
        );
        xmlDoc.append("</ppc>");
        xmlDoc.append("<Registration>");
        xmlDoc.append("<TokenNo>" + tokenNum + "</TokenNo>"
                + "<TokenID>" + tokenID + "</TokenID>"
                + "<RegistrationNo>" + regNo + "</RegistrationNo>"
                + "<RegistrationID>" + regId + "</RegistrationID>"
                + "<TransactionID>" + trnId + "</TransactionID>"
                + "<TransactionRowID>" + trnRowId + "</TransactionRowID>"
        );
        xmlDoc.append("</Registration>");

        xmlDoc.append("<GunnyOrderSet>");

        for (int x = 0; x < gunnysSubmitRequest.getGunnyXMLDataArrayList().size(); x++) {
            xmlDoc.append("<GunnyOrder>");
            xmlDoc.append("<GunnyOrderId>" + gunnysSubmitRequest.getGunnyXMLDataArrayList().get(x).getGunnyOrderId() + "</GunnyOrderId>"
                    + "<TokenNewBags>" + gunnysSubmitRequest.getGunnyXMLDataArrayList().get(x).getTokenNewBags() + "</TokenNewBags>"
                    + "<TokenOldBags>" + gunnysSubmitRequest.getGunnyXMLDataArrayList().get(x).getTokenOldBags() + "</TokenOldBags>"
                    + "<TokenTotalBags>" + gunnysSubmitRequest.getGunnyXMLDataArrayList().get(x).getTokenTotalBags() + "</TokenTotalBags>"
                    + "<GunnysGiven_Date>" + gunnysSubmitRequest.getGunnyXMLDataArrayList().get(x).getGunnysGiven_Date() + "</GunnysGiven_Date>"
            );
            xmlDoc.append("</GunnyOrder>");
        }

        xmlDoc.append("</GunnyOrderSet>");

        xmlDoc.append("</FarmerIssuingGunnyDetails>");
        return xmlDoc;
    }


    private void deleteRowToGunnyOrderTable() {
        try {
            ArrayList<View> tempViewArrayList = new ArrayList<>(gunnyOrderTableRowsArrayList);

            if (tempViewArrayList.size() > 0) {
                boolean isChecked = false;
                for (int i = 0; i < tempViewArrayList.size(); i++) {
                    TableRow tb = (TableRow) tempViewArrayList.get(i);
                    CheckBox check = tb.findViewById(R.id.Gunny_Order_Cb);
                    if (check.isChecked()) {
                        isChecked = true;
                        binding.GunnyOrderTl.removeView(tb);
                        gunnyOrderTableRowsArrayList.remove(tempViewArrayList.get(i));
                    }
                }
                if (!isChecked) {
                    //Utils.ALERT_DIALOG_OK(getActivity(), getResources().getString(R.string.GunnysGivenToFarmer), "Please select the rows that you want to delete?");
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.GunnysGivenToFarmer),
                            getString(R.string.plz_delete),
                            getString(R.string.INFORMATION), false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getGeneratedTokens(long tokenId) {
        GetTokensRequest getTokensFarmerRequest = new GetTokensRequest();
        getTokensFarmerRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
        getTokensFarmerRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));
        getTokensFarmerRequest.setTokenStatus("3");
        if (tokenId == -1) {
            if (customProgressDialog != null)
                customProgressDialog.show();
            gunnysGivenPresenter.GetGunnysGeneratedTokens(getTokensFarmerRequest);
        } else {
            if (customProgressDialog != null)
                customProgressDialog.show();
            getTokensFarmerRequest.setTokenID(tokenId);
            gunnysGivenPresenter.GetSelectedGunnysTokenData(getTokensFarmerRequest);
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
                            getResources().getString(R.string.GunnysGivenToFarmer),
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

    private AdapterView.OnItemSelectedListener onItemClick = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            try {
                if (adapterView.getId() == R.id.gunny_token_no_Sp) {
                    clearForm(binding.GtoFMainLL);//GtoF_main_LL
                    binding.GtoFMainLL.setVisibility(View.GONE);
                    String tokenNum = binding.gunnyTokenNoSp.getSelectedItem().toString().trim();
                    if (getTokensResponseList != null && getTokensResponseList.getGetDDLTokens().size() > 0 &&
                            !TextUtils.isEmpty(tokenNum) && !tokenNum.equalsIgnoreCase("--Select--")) {
                        long tokenID = getTokenID(tokenNum, getTokensResponseList);
                        getGeneratedTokens(tokenID);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private long getTokenID(String tokenNo, GetTokensDDLResponse getTokensResponseList) {
        long tokenID = -1;
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
                    if (id == R.id.GtoF_tokenNo_Tv || id == R.id.GtoF_aadhaarNo_Tv || id == R.id.GtoF_farmerName_Tv || id == R.id.GtoF_fatherName_Tv
                            || id == R.id.GtoF_mobile_Tv || id == R.id.GtoF_district_Tv || id == R.id.GtoF_mandal_Tv || id == R.id.GtoF_village_Tv
                            || id == R.id.GtoF_Qty_Tv || id == R.id.GtoF_bagsReq_tv || id == R.id.GtoF_land_tv)
                        ((TextView) view).setText("");

                }
                if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                    clearForm((ViewGroup) view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTokensGunnysResponseData(GetTokensDDLResponse getTokensDDLResponse) {
        try {
            if (customProgressDialog != null && customProgressDialog.isShowing())
                customProgressDialog.dismiss();

            getTokensResponseList = getTokensDDLResponse;
            if (getTokensDDLResponse != null && getTokensDDLResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (getTokensDDLResponse.getGetDDLTokens() != null &&
                        getTokensDDLResponse.getGetDDLTokens().size() > 0) {
                    ArrayList<String> getTokens = new ArrayList<>();
                    getTokens.add(0, "--Select--");
                    for (int i = 0; i < getTokensDDLResponse.getGetDDLTokens().size(); i++) {
                        getTokens.add(getTokensDDLResponse.getGetDDLTokens().get(i).getTokenNo());
                    }
                    if (getTokens.size() > 0) {
                        Utils.loadSpinnerData(getActivity(), getTokens, binding.gunnyTokenNoSp);
                    }
                } else {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.GunnysGivenToFarmer),
                            getTokensDDLResponse.getResponseMessage(),
                            getString(R.string.ERROR), false);

                }
            } else if (getTokensDDLResponse != null &&
                    getTokensDDLResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.GunnysGivenToFarmer),
                        getTokensDDLResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (getTokensDDLResponse != null &&
                    getTokensDDLResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.GunnysGivenToFarmer),
                        getTokensDDLResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (getTokensDDLResponse != null &&
                    getTokensDDLResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.GunnysGivenToFarmer),
                        getTokensDDLResponse.getResponseMessage(), getFragmentManager());
            } else if (getTokensDDLResponse != null &&
                    getTokensDDLResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(getTokensDDLResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.GunnysGivenToFarmer),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.GunnysGivenToFarmer),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }

    }

    @Override
    public void GetSelectedGunnysTokenData(GetTokensResponse getTokensGunnysResponseData) {
        try {

            if (getTokensGunnysResponseData != null && getTokensGunnysResponseData.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (getTokensGunnysResponseData.getGetTokenOutput() != null && getTokensGunnysResponseData.getGetTokenOutput().size() > 0) {
                    getTokenOutputGlobal = getTokensGunnysResponseData.getGetTokenOutput().get(0);
                    binding.GtoFMainLL.setVisibility(View.VISIBLE);
                    setSelectedTokenData(getTokensGunnysResponseData);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.no_data), Toast.LENGTH_LONG).show();
                }
            } else if (getTokensGunnysResponseData != null &&
                    getTokensGunnysResponseData.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.GunnysGivenToFarmer),
                        getTokensGunnysResponseData.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (getTokensGunnysResponseData != null &&
                    getTokensGunnysResponseData.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.GunnysGivenToFarmer),
                        getTokensGunnysResponseData.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (getTokensGunnysResponseData != null &&
                    getTokensGunnysResponseData.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.GunnysGivenToFarmer),
                        getTokensGunnysResponseData.getResponseMessage(), getFragmentManager());
            } else if (getTokensGunnysResponseData != null &&
                    getTokensGunnysResponseData.getStatusCode() == AppConstants.VERSION_CODE) {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.callPlayAlert(getTokensGunnysResponseData.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.GunnysGivenToFarmer),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.GunnysGivenToFarmer),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
    }

    @Override
    public void GetGetGunnyOrderDetails(GunnyDetailsResponse gunnyDetailsResponse) {
        try {
            if (gunnyDetailsResponse != null && gunnyDetailsResponse.getStatusCode() == 600
                    && gunnyDetailsResponse.getGunnyResponse() != null &&
                    gunnyDetailsResponse.getGunnyResponse().size() > 0) {

                getGeneratedTokens(-1);
                gunnyOrderResponseListGlobal = gunnyDetailsResponse;
                if (gunnyDetailsResponse.getGunnyResponse().size() > 1) {
                    binding.addDeleteRow.setVisibility(View.VISIBLE);
                } else {
                    binding.addDeleteRow.setVisibility(View.GONE);
                }
                addRowToGunnyOrderTable(gunnyDetailsResponse);
            } else if (gunnyDetailsResponse != null &&
                    gunnyDetailsResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.GunnysGivenToFarmer),
                        gunnyDetailsResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (gunnyDetailsResponse != null &&
                    gunnyDetailsResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.GunnysGivenToFarmer),
                        gunnyDetailsResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (gunnyDetailsResponse != null &&
                    gunnyDetailsResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.GunnysGivenToFarmer),
                        gunnyDetailsResponse.getResponseMessage(), getFragmentManager());
            } else if (gunnyDetailsResponse != null &&
                    gunnyDetailsResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.callPlayAlert(gunnyDetailsResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.GunnysGivenToFarmer),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Exception e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.GunnysGivenToFarmer),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
    }

    @Override
    public void GetGunnySubmitDetails(GunnySubmitResponse gunnySubmitResponse) {
        try {
            if (customProgressDialog != null && customProgressDialog.isShowing())
                customProgressDialog.dismiss();

            if (gunnySubmitResponse != null && gunnySubmitResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {

                Fragment fragment = new DashboardFragment();
                Utils.showAlertNavigateToFarmer1(getActivity(), fragment, getFragmentManager(),
                        getResources().getString(R.string.GunnysGivenToFarmer),
                        gunnySubmitResponse.getResponseMessage(), true);

            } else if (gunnySubmitResponse != null &&
                    gunnySubmitResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.GunnysGivenToFarmer),
                        gunnySubmitResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (gunnySubmitResponse != null &&
                    gunnySubmitResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.GunnysGivenToFarmer),
                        gunnySubmitResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (gunnySubmitResponse != null &&
                    gunnySubmitResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.GunnysGivenToFarmer),
                        gunnySubmitResponse.getResponseMessage(), getFragmentManager());
            } else if (gunnySubmitResponse != null &&
                    gunnySubmitResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(gunnySubmitResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.GunnysGivenToFarmer),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.GunnysGivenToFarmer),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }

    }

    private void addRowToGunnyOrderTable(GunnyDetailsResponse gunnyDetailsResponse) {
        try {
            if (gunnyOrderTableRowsArrayList.size() == 0) {
                binding.GunnyOrderTl.removeAllViews();
            }
            LayoutInflater mInflater = LayoutInflater.from(getActivity());
            final TableRow paramView = (TableRow) mInflater.inflate(R.layout.gunny_order_table_row, null);
            CheckBox checkBox = paramView.findViewById(R.id.Gunny_Order_Cb);
            Spinner gunnyOrderSp = paramView.findViewById(R.id.Gunny_Order_Sp);
            final LinearLayout Gunny_OrderDetails_LL = paramView.findViewById(R.id.Gunny_OrderDetails_LL);
            Gunny_OrderDetails_LL.setVisibility(View.GONE);
            final TextView manualGunnyOrderNum = paramView.findViewById(R.id.Gunny_Manual_OrderId);
            final TextView availNewBags = paramView.findViewById(R.id.avail_new_bags);
            final TextView availOldBags = paramView.findViewById(R.id.avail_old_bags);
            final EditText enteredNewBags = paramView.findViewById(R.id.Gunny_Newbags_Et);
            final EditText enteredoldBags = paramView.findViewById(R.id.Gunny_Oldbags_Et);

            enteredNewBags.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    checkBox.setChecked(false);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            enteredoldBags.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    checkBox.setChecked(false);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            ArrayList<String> gunnyOrderIds = new ArrayList<>();
            gunnyOrderIds.add("--Select--");
            for (int x = 0; x < gunnyDetailsResponse.getGunnyResponse().size(); x++) {
                gunnyOrderIds.add(String.valueOf(gunnyDetailsResponse.getGunnyResponse().get(x).getORDERID()));
            }

            Utils.loadSpinnerData(getActivity(), gunnyOrderIds, gunnyOrderSp);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (buttonView.isChecked()) {
                            String orderId = gunnyOrderSp.getSelectedItem().toString();
                            if (orderId.equalsIgnoreCase("--Select--")) {
                                buttonView.setChecked(false);
                                showAlertError(getString(R.string.select_order));
                                return;
                            }

                            String newBags = enteredNewBags.getText().toString();
                            if (newBags.equalsIgnoreCase("")) {
                                buttonView.setChecked(false);
                                enteredNewBags.setError("Enter new bags");
                                enteredNewBags.requestFocus();
                                return;
                            }
                            String oldBags = enteredoldBags.getText().toString();
                            if (oldBags.equalsIgnoreCase("")) {
                                buttonView.setChecked(false);
                                enteredoldBags.setError("Enter old bags");
                                enteredoldBags.requestFocus();
                                return;
                            }

                            int totalBags = Integer.parseInt(newBags) + Integer.parseInt(oldBags);
                            if (totalBags <= 0) {
                                buttonView.setChecked(false);
                                showAlertError(getString(R.string.tot_bags_not_zero));
                                return;
                            }


                            String receivedNewbags = "", receivedOldbags = "";

                            if (!orderId.equalsIgnoreCase("--Select--")) {
                                receivedNewbags = availNewBags.getText().toString();
                                receivedOldbags = availOldBags.getText().toString();
                            }

                            if (Integer.parseInt(newBags) > Integer.parseInt(receivedNewbags)) {
                                buttonView.setChecked(false);
                                enteredNewBags.requestFocus();
                                setEditTextData(enteredNewBags, getString(R.string.stock_inf) + " " + receivedNewbags + " " + getString(R.string.ne_ba));
                                return;
                            }
                            if (Integer.parseInt(oldBags) > Integer.parseInt(receivedOldbags)) {
                                buttonView.setChecked(false);
                                enteredoldBags.requestFocus();
                                setEditTextData(enteredoldBags, getString(R.string.stock_inf) + " " + receivedOldbags + " " + getString(R.string.ol_ba));
                                return;
                            }
                            if (!(Integer.parseInt(newBags) <= Integer.parseInt(receivedNewbags) && Integer.parseInt(oldBags) <= Integer.parseInt(receivedOldbags))) {
                                buttonView.setChecked(false);
                                showAlertError(getString(R.string.stock_valid));
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            gunnyOrderSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    enteredNewBags.setText("");
                    enteredoldBags.setText("");
                    int selectedItemPosition = parent.getSelectedItemPosition();
                    if (selectedItemPosition != 0) {
                        String orderId = parent.getSelectedItem().toString();
                        GunnyResponse gunnyOrderResponse = null;
                        for (int x = 0; x < gunnyDetailsResponse.getGunnyResponse().size(); x++) {
                            if (orderId.equalsIgnoreCase(String.valueOf(gunnyDetailsResponse.getGunnyResponse().get(x).getORDERID()))) {
                                gunnyOrderResponse = gunnyDetailsResponse.getGunnyResponse().get(x);
                                break;
                            }
                        }


                        if (gunnyOrderResponse != null) {
                            manualGunnyOrderNum.setVisibility(View.VISIBLE);
                            availNewBags.setVisibility(View.VISIBLE);
                            availOldBags.setVisibility(View.VISIBLE);
                            manualGunnyOrderNum.setText("" + gunnyOrderResponse.getManualGunniesTruckchitID());
                            availNewBags.setText("" + gunnyOrderResponse.getNewBags());
                            availOldBags.setText("" + gunnyOrderResponse.getOldBags());
                            Gunny_OrderDetails_LL.setVisibility(View.VISIBLE);
                        }

                    } else {
                        manualGunnyOrderNum.setVisibility(View.INVISIBLE);
                        availNewBags.setVisibility(View.INVISIBLE);
                        availOldBags.setVisibility(View.INVISIBLE);
                        Gunny_OrderDetails_LL.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            binding.GunnyOrderTl.addView(paramView);
            gunnyOrderTableRowsArrayList.add(paramView);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setSelectedTokenData(GetTokensResponse getTokenGunnyResponse) {
        if (getTokenGunnyResponse.getGetTokenOutput().get(0).getTokenNo() != null) {
            binding.GtoFTokenNoTv.setText(getTokenGunnyResponse.getGetTokenOutput().get(0).getTokenNo().trim());
        }
        if (getTokenGunnyResponse.getGetTokenOutput().get(0).getRegistrationNo() != null) {
            String mask = getTokenGunnyResponse.getGetTokenOutput().get(0).getRegistrationNo().trim().replaceAll("\\w(?=\\w{4})", "*");
            binding.GtoFAadhaarNoTv.setText(mask);
        }
        if (getTokenGunnyResponse.getGetTokenOutput().get(0).getFarmerName() != null) {
            binding.GtoFFarmerNameTv.setText(getTokenGunnyResponse.getGetTokenOutput().get(0).getFarmerName().trim());
        }
        if (getTokenGunnyResponse.getGetTokenOutput().get(0).getFarmerFatherName() != null) {
            binding.GtoFFatherNameTv.setText(getTokenGunnyResponse.getGetTokenOutput().get(0).getFarmerFatherName().trim());
        }
        if (getTokenGunnyResponse.getGetTokenOutput().get(0).getMobile() != null) {
            binding.GtoFMobileTv.setText(getTokenGunnyResponse.getGetTokenOutput().get(0).getMobile().trim());
        }
        if (getTokenGunnyResponse.getGetTokenOutput().get(0).getFDistrictID() != null) {
            paddyTestRepository.getDistrictName(this,
                    getTokenGunnyResponse.getGetTokenOutput().get(0).getFDistrictID().trim());
        }

        if (getTokenGunnyResponse.getGetTokenOutput().get(0).getFDistrictID() != null
                && getTokenGunnyResponse.getGetTokenOutput().get(0).getFMandalID() != null) {
            paddyTestRepository.getMandalName(this,
                    getTokenGunnyResponse.getGetTokenOutput().get(0).getFDistrictID().trim(),
                    getTokenGunnyResponse.getGetTokenOutput().get(0).getFMandalID().trim());
        }

        if (getTokenGunnyResponse.getGetTokenOutput().get(0).getFDistrictID() != null
                && getTokenGunnyResponse.getGetTokenOutput().get(0).getFMandalID() != null
                && getTokenGunnyResponse.getGetTokenOutput().get(0).getFVillageID() != null) {
            paddyTestRepository.getVillageName(this,
                    getTokenGunnyResponse.getGetTokenOutput().get(0).getFDistrictID().trim(),
                    getTokenGunnyResponse.getGetTokenOutput().get(0).getFMandalID().trim(),
                    getTokenGunnyResponse.getGetTokenOutput().get(0).getFVillageID().trim());
        }

        if (getTokenGunnyResponse.getGetTokenOutput().get(0).getApproxmateQuantity() != null) {
            binding.GtoFQtyTv.setText(String.valueOf(getTokenGunnyResponse.getGetTokenOutput().get(0).getApproxmateQuantity()));
        }

        /* calculation based on landAcres*/
        //Double landAcres = getTokenGunnyResponse.getGetTokenOutput().get(0).getTotalCultivableArea();
        //Double procQty = landAcres * 40;
        //procQty = Double.valueOf(String.format("%.2f", procQty));

        // code changes made by DC on 10/09/19, calcs done based on the approx qty, as the landAcres data in the response is wrong

        /* calculation based on procQty*/
        if (getTokenGunnyResponse.getGetTokenOutput().get(0).getApproxmateQuantity() != null
                && getTokenGunnyResponse.getGetTokenOutput().get(0).getApproxmateQuantity() > 0) {
            Double procQty = getTokenGunnyResponse.getGetTokenOutput().get(0).getApproxmateQuantity();
            Double gunnyBags = (double) (procQty * 100 / 40);
            int maxGunnies = (int) Math.ceil(gunnyBags);
            binding.GtoFBagsReqTv.setText(String.valueOf(maxGunnies));
        } else {
            Utils.customAlert(getActivity(), getString(R.string.GunnysGivenToFarmer),
                    "Unable to calculate gunny bags due to no value for quantity",
                    getString(R.string.ERROR), false);
        }

    }

    private void GetGunnyOrderDetails() {
        if (ppcUserDetails != null && ppcUserDetails.getAuthenticationID() != null && ppcUserDetails.getPPCID() != null) {
            GunnyDetailsRequest gunnyDetailsRequest = new GunnyDetailsRequest();
            gunnyDetailsRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
            gunnyDetailsRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));
            if (customProgressDialog != null)
                customProgressDialog.show();
            gunnysGivenPresenter.GetGunnyOrderDetails(gunnyDetailsRequest);
        } else {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.GunnysGivenToFarmer),
                    getString(R.string.something) + " ..",
                    getResources().getString(R.string.ERROR),
                    false);
        }
    }

    @Override
    public void getAllPaddyValues(List<PaddyEntity> paddyEntities) {

    }

    @Override
    public void paddyTestCount(int cnt) {

    }

    @Override
    public void getDistName(String distName) {
        binding.GtoFDistrictTv.setText(distName);
    }

    @Override
    public void getManName(String manName) {
        binding.GtoFMandalTv.setText(manName);
    }

    @Override
    public void getVilName(String vilName) {
        binding.GtoFVillageTv.setText(vilName);
    }

    private boolean validateData(GunnysSubmitRequest gunnysSubmitRequest) {
        try {

            ArrayList<GunnyXMLData> gunnyXMLDataArrayList = new ArrayList<>();

            String orderId, newBags, oldBags, maxBags,
                    receivedNewbags = "0", receivedOldbags = "0", remainingNewbags, remainingOldbags, remainingTotalbags;

            int totalBagsInt = 0;

            if (!checkRecordsSelected()) {
                showAlertError(getString(R.string.sel_ord_to_submit));
                return false;
            }

            if (getOrderIdsFromGunnyOrderTable()) {
                showAlertError(getString(R.string.dup_order));
                return false;
            }


            for (int i = 0; i < gunnyOrderTableRowsArrayList.size(); i++) {
                GunnyXMLData gunnyXMLData = new GunnyXMLData();

                String createdDate = Utils.getCurrentDateTime();

                TableRow paramView = (TableRow) gunnyOrderTableRowsArrayList.get(i);
                CheckBox checkBox = paramView.findViewById(R.id.Gunny_Order_Cb);
                Spinner gunnyOrderSp = paramView.findViewById(R.id.Gunny_Order_Sp);
                EditText gunnyNewBagsEt = paramView.findViewById(R.id.Gunny_Newbags_Et);
                EditText gunnyOldBagsEt = paramView.findViewById(R.id.Gunny_Oldbags_Et);
                TextView availNewBags = paramView.findViewById(R.id.avail_new_bags);
                TextView availOldBags = paramView.findViewById(R.id.avail_old_bags);

                if (checkBox.isChecked()) {


                    orderId = gunnyOrderSp.getSelectedItem().toString();
                    if (orderId.equalsIgnoreCase("--Select--")) {
                        showAlertError(getString(R.string.select_order));
                        return false;
                    }

                    newBags = gunnyNewBagsEt.getText().toString();
                    if (newBags.equalsIgnoreCase("")) {
                        newBags = "0";
                    }
                    oldBags = gunnyOldBagsEt.getText().toString();
                    if (oldBags.equalsIgnoreCase("")) {
                        oldBags = "0";
                    }

                    if (!orderId.equalsIgnoreCase("--Select--")) {
                        receivedNewbags = availNewBags.getText().toString();
                        receivedOldbags = availOldBags.getText().toString();
                    }

                    totalBagsInt = totalBagsInt + Integer.parseInt(oldBags) + Integer.parseInt(newBags);
                    String totalBags = String.valueOf(totalBagsInt);

                    maxBags = binding.GtoFBagsReqTv.getText().toString();
                    if (maxBags.equalsIgnoreCase(""))
                        maxBags = "0";

                    if (Double.parseDouble(maxBags) < Integer.parseInt(totalBags)) {
                        showAlertError(getString(R.string.tot_bags_val));
                        return false;

                    } else {
                        if (totalBags.equalsIgnoreCase("") || totalBags.equalsIgnoreCase("0")) {
                            setEditTextData(gunnyNewBagsEt, getString(R.string.enter_new_bags));
                            setEditTextData(gunnyOldBagsEt, getString(R.string.enter_old_bags));
                            gunnyNewBagsEt.requestFocus();
                            return false;
                        }
                        if (Integer.parseInt(newBags) > Integer.parseInt(receivedNewbags)) {
                            gunnyNewBagsEt.requestFocus();
                            setEditTextData(gunnyNewBagsEt, getString(R.string.stock_inf) + " " + receivedNewbags + " " + getString(R.string.ne_ba));
                            return false;
                        }
                        if (Integer.parseInt(oldBags) > Integer.parseInt(receivedOldbags)) {
                            gunnyOldBagsEt.requestFocus();
                            setEditTextData(gunnyOldBagsEt, getString(R.string.stock_inf) + " " + receivedOldbags + " " + getString(R.string.ol_ba));
                            return false;
                        }
                        if (Integer.parseInt(newBags) <= Integer.parseInt(receivedNewbags) && Integer.parseInt(oldBags) <= Integer.parseInt(receivedOldbags)) {
                            gunnyXMLData.setGunnyOrderId(orderId);
                            gunnyXMLData.setTokenNewBags(newBags);
                            gunnyXMLData.setTokenOldBags(oldBags);
                            gunnyXMLData.setTokenTotalBags(String.valueOf(Integer.parseInt(newBags) + Integer.parseInt(oldBags)));
                            gunnyXMLData.setGunnysGiven_Date(createdDate);

                            gunnyXMLDataArrayList.add(gunnyXMLData);
                            gunnysSubmitRequest.setGunnyXMLDataArrayList(gunnyXMLDataArrayList);
                        } else {
                            showAlertError(getString(R.string.stock_valid));
                            return false;
                        }
                    }

                    if (gunnysSubmitRequest.getGunnyXMLDataArrayList() == null) {
                        showAlertError(getString(R.string.no_gunny));
                        return false;
                    }
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void showAlertError(String errorMsg) {
        try {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.GunnysGivenToFarmer),
                    errorMsg,
                    getString(R.string.INFORMATION), false);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setEditTextData(View view, String errorMsg) {
        try {
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                editText.setError(errorMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Boolean getOrderIdsFromGunnyOrderTable() {
        List<String> selectedOrderIds = new ArrayList<>();
        try {
            if (gunnyOrderTableRowsArrayList.size() > 0) {
                for (int i = 0; i < gunnyOrderTableRowsArrayList.size(); i++) {
                    TableRow tb = (TableRow) gunnyOrderTableRowsArrayList.get(i);
                    Spinner orderSp = tb.findViewById(R.id.Gunny_Order_Sp);
                    selectedOrderIds.add(orderSp.getSelectedItem().toString());
                }

                Set<String> set = new HashSet<String>();
                // Set.add() returns false if the set does not change, which
                // indicates that a duplicate element has been added.
                for (String each : selectedOrderIds) if (!set.add(each)) return true;
                return false;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Boolean checkRecordsSelected() {
        boolean flag = false;
        try {
            if (gunnyOrderTableRowsArrayList.size() > 0) {
                for (int i = 0; i < gunnyOrderTableRowsArrayList.size(); i++) {
                    TableRow tb = (TableRow) gunnyOrderTableRowsArrayList.get(i);
                    CheckBox checkBox = tb.findViewById(R.id.Gunny_Order_Cb);
                    if (checkBox.isChecked()) {
                        flag = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public void getAllDistrictEntities(List<DistrictEntity> districtEntities) {
        try {
            if (districtEntities.size() > 0) {
                dmvRepository.getAllMandals(this);
            } else {
                Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                        getResources().getString(R.string.GunnysGivenToFarmer),
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
                        getResources().getString(R.string.GunnysGivenToFarmer),
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

                if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                    GetGunnyOrderDetails();
                } else {
                    if (getActivity() != null) {
                        Utils.customAlert(getActivity(),
                                getResources().getString(R.string.GunnysGivenToFarmer),
                                getResources().getString(R.string.no_internet),
                                getResources().getString(R.string.WARNING), false);
                    } else {
                        Utils.customAlert(getContext(),
                                getResources().getString(R.string.GunnysGivenToFarmer),
                                getResources().getString(R.string.no_internet),
                                getResources().getString(R.string.WARNING), false);
                    }
                }

            } else {
                Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                        getResources().getString(R.string.GunnysGivenToFarmer),
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
            if (gunnysGivenPresenter != null) {
                gunnysGivenPresenter.detachView();
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
                    getResources().getString(R.string.GunnysGivenToFarmer),
                    msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else if (code == AppConstants.EXCEPTION_CODE) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.GunnysGivenToFarmer),
                    getString(R.string.something) +
                            msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.GunnysGivenToFarmer),
                    getString(R.string.something),
                    getResources().getString(R.string.ERROR),
                    false);
        }
    }
}

package com.cgg.pps.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.analogics.thermalAPI.Bluetooth_Printer_2inch_ThermalAPI;
import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.custom.CustomFontEditText;
import com.cgg.pps.custom.CustomFontTextView;
import com.cgg.pps.databinding.PaddyProcurementFragmentBinding;
import com.cgg.pps.interfaces.BBInterface;
import com.cgg.pps.interfaces.DMVInterface;
import com.cgg.pps.interfaces.PaddyProcurementInterface;
import com.cgg.pps.interfaces.PaddyTestInterface;
import com.cgg.pps.interfaces.RePrintInterface;
import com.cgg.pps.model.request.ProRePrintRequest;
import com.cgg.pps.model.request.farmer.gettokens.GetTokensRequest;
import com.cgg.pps.model.request.procurement.IssuedGunnyDataRequest;
import com.cgg.pps.model.request.procurement.PaddyOTPRequest;
import com.cgg.pps.model.request.procurement.PaddyProcOrderXMLData;
import com.cgg.pps.model.request.procurement.PaddyProcurementSubmit;
import com.cgg.pps.model.request.procurement.PaddyXMLData;
import com.cgg.pps.model.response.devicemgmt.bankbranch.BranchEntity;
import com.cgg.pps.model.response.devicemgmt.masterbank.BankEntity;
import com.cgg.pps.model.response.devicemgmt.masterdistrict.DistrictEntity;
import com.cgg.pps.model.response.devicemgmt.mastermandal.MandalEntity;
import com.cgg.pps.model.response.devicemgmt.mastervillage.VillageEntity;
import com.cgg.pps.model.response.devicemgmt.paddyvalues.PaddyEntity;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokenOutput;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensDDLResponse;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
import com.cgg.pps.model.response.procurement.IssuedGunnyDataResponse;
import com.cgg.pps.model.response.procurement.IssuedGunnyResponse;
import com.cgg.pps.model.response.procurement.OTPResponse;
import com.cgg.pps.model.response.procurement.PaddyOrderResponse;
import com.cgg.pps.model.response.procurement.ProcurementSubmitResponse;
import com.cgg.pps.model.response.reprint.ProcRePrintResponse;
import com.cgg.pps.model.response.reprint.TokenRePrintResponse;
import com.cgg.pps.model.response.reprint.truckchit.TCRePrintResponse;
import com.cgg.pps.model.response.socialstatus.SocialEntity;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.presenter.PaddyProcurementPresenter;
import com.cgg.pps.presenter.RePrintPresenter;
import com.cgg.pps.room.repository.BBRepository;
import com.cgg.pps.room.repository.DMVRepository;
import com.cgg.pps.room.repository.PaddyTestRepository;
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
import java.util.List;

public class PaddyProcurementFragment extends Fragment implements PaddyProcurementInterface,
        PaddyTestInterface, BBInterface, DMVInterface, RePrintInterface {
    PaddyProcurementFragmentBinding binding;
    private PPCUserDetails ppcUserDetails;
    PaddyProcurementPresenter paddyProcurementPresenter;
    private PaddyTestRepository paddyTestRepository;
    private List<PaddyEntity> paddyEntities;
    private GetTokenOutput getTokenOutputMain;
    private GetTokensResponse getTokenPaddyResponseGlobal;
    private BBRepository bbRepository;
    private GetTokensDDLResponse getProcurementResponseList;
    private ArrayList<TableRow> landDetailsTableRowsArrayList;
    private ArrayList<PaddyOrderResponse> paddyOrderResponseArrayList;
    CustomProgressDialog customProgressDialog;
    private IssuedGunnyDataResponse gunyOrderResponseListGlobal;

    private DMVRepository dmvRepository;
    private RePrintPresenter rePrintPresenter;
    private SharedPreferences sharedPreferences;

    private APrinter aPrinter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.paddy_procurement_fragment, container, false);
        paddyProcurementPresenter = new PaddyProcurementPresenter();
        rePrintPresenter = new RePrintPresenter();
        paddyProcurementPresenter.attachView(this);
        rePrintPresenter.attachView(this);
        dmvRepository = new DMVRepository(getActivity());
        customProgressDialog = new CustomProgressDialog(getActivity());
        paddyTestRepository = new PaddyTestRepository(getActivity());
        bbRepository = new BBRepository(getActivity());
        binding.paddyTokenNoSp.setOnItemSelectedListener(onItemClick);
        binding.pproGenerateamountBt.setOnClickListener(onClick);
        landDetailsTableRowsArrayList = new ArrayList<>();
        getPreferenceData();
        aPrinter = APrinter.getaPrinter();

        dmvRepository.getAllDistricts(this);

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
                        .setActionBarTitle(getResources().getString(R.string.PaddyProcurement));

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getAllBanks(List<BankEntity> bankEntities) {
        try {
            if (bankEntities.size() > 0) {
                bbRepository.getAllBranches(this);
            } else {
                Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                        getResources().getString(R.string.PaddyProcurementdetails),
                        getString(R.string.no_banks), true);

            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    AdapterView.OnItemSelectedListener onItemClick = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            try {
                if (adapterView.getId() == R.id.paddy_token_no_Sp) {
                    paddyOrderResponseArrayList = new ArrayList<>();
                    clearForm(binding.pproMainLl);
                    binding.pproMainLl.setVisibility(View.GONE);
                    String tokenNum = binding.paddyTokenNoSp.getSelectedItem().toString().trim();
                    if (getProcurementResponseList != null && getProcurementResponseList.getGetDDLTokens().size() > 0 &&
                            !TextUtils.isEmpty(tokenNum) && !tokenNum.equalsIgnoreCase("--Select--")) {
                        long tokenID = getTokenID(tokenNum, getProcurementResponseList);
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

    private boolean ValidateData(GetTokensResponse getTokenPaddyResponse, PaddyProcurementSubmit paddySubmitRequest, IssuedGunnyDataResponse gunyOrderResponseListGlobal) {
        try {

            if (getTokenPaddyResponse == null) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurementdetails),
                        getResources().getString(R.string.something),
                        getString(R.string.ERROR), false);
                return false;
            }

            if (gunyOrderResponseListGlobal == null) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurementdetails),
                        getResources().getString(R.string.something),
                        getString(R.string.ERROR), false);
                return false;
            }

            if (paddyOrderResponseArrayList.size() != gunyOrderResponseListGlobal.getIssuedGunnyResponse().size()) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurementdetails),
                        getString(R.string.plz_submit_all),
                        getString(R.string.INFORMATION), false);
                return false;
            }


            ArrayList<PaddyProcOrderXMLData> paddyOrderXMLDataArrayList = new ArrayList<>();
            PaddyXMLData paddyXMLData = new PaddyXMLData();

            String createdDate = Utils.getCurrentDateTime();

            double mspAmount = getPaddyMSPAmpount(getTokenPaddyResponse.getGetTokenOutput().get(0).getPaddyTypeID());
            if (mspAmount <= 0) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurementdetails),
                        getString(R.string.msp_empty),
                        getString(R.string.INFORMATION), false);
                return false;
            }


            double TotalAmount;
            String paddyFinalNewBags, paddyFinalOldBags;

            paddyFinalNewBags = binding.pproGunnynewbagsEt.getText().toString();
            paddyFinalOldBags = binding.pproGunnyoldbagsEt.getText().toString();

            if (TextUtils.isEmpty(paddyFinalNewBags)) {
                paddyFinalNewBags = "0";
            }

            if (TextUtils.isEmpty(paddyFinalOldBags)) {
                paddyFinalOldBags = "0";
            }

            String paddyFinalTotBags = Integer.toString(Integer.parseInt(paddyFinalNewBags)
                    + Integer.parseInt(paddyFinalOldBags));

            if (Integer.valueOf(paddyFinalTotBags) <= 0) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurementdetails),
                        getString(R.string.cannot_return_all),
                        getString(R.string.INFORMATION), false);
                return false;
            }

            int finalValBags = 0;
            for (int z = 0; z < paddyOrderResponseArrayList.size(); z++) {
                finalValBags = finalValBags + (paddyOrderResponseArrayList.get(z).getFinalTotalBags()
                        + paddyOrderResponseArrayList.get(z).getFinalEmptyBags());
            }

            Integer act = getTokenPaddyResponse.getGetTokenOutput().get(0).getTotalBags();
            if (finalValBags != act) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurementdetails),
                        getString(R.string.issued_mismatch),
                        getString(R.string.INFORMATION), false);
                return false;
            }

            double paddyBagsQty = Integer.valueOf(paddyFinalTotBags) * 0.40;


            TotalAmount = paddyBagsQty * mspAmount;


            paddyXMLData.setPpcid(String.valueOf(ppcUserDetails.getPPCID()));
            paddyXMLData.setPpccode(ppcUserDetails.getPPCCode());
            paddyXMLData.setSystemIP(Utils.getLocalIpAddress());
            paddyXMLData.setSeasonID(String.valueOf(ppcUserDetails.getSeasonID()));
            paddyXMLData.setTokenNo(getTokenPaddyResponse.getGetTokenOutput().get(0).getTokenNo());
            paddyXMLData.setTokenID("" + getTokenPaddyResponse.getGetTokenOutput().get(0).getTokenID());
            paddyXMLData.setRegistrationNo(getTokenPaddyResponse.getGetTokenOutput().get(0).getRegistrationNo());
            paddyXMLData.setRegistrationID("" + getTokenPaddyResponse.getGetTokenOutput().get(0).getRegID());
            paddyXMLData.setTransactionID(getTokenPaddyResponse.getGetTokenOutput().get(0).getFarmerTransactionID());
            paddyXMLData.setTransactionRowID("" + getTokenPaddyResponse.getGetTokenOutput().get(0).getFarmerTransactionRowID());


            if (paddyOrderResponseArrayList.size() > 0) {

                for (int x = 0; x < paddyOrderResponseArrayList.size(); x++) {
                    PaddyOrderResponse paddyOrderResponse = paddyOrderResponseArrayList.get(x);

                    PaddyProcOrderXMLData paddyOrderXMLData1 = new PaddyProcOrderXMLData();

                    paddyOrderXMLData1.setGunnyOrderId(paddyOrderResponse.getGunnyOrderId());
                    paddyOrderXMLData1.setTokenNewBags(paddyOrderResponse.getTokenNewBags());

                    paddyOrderXMLData1.setTokenOldBags(paddyOrderResponse.getTokenOldBags());
                    paddyOrderXMLData1.setTokenTotalBags(paddyOrderResponse.getTokenTotalBags());
                    paddyOrderXMLData1.setProcuredNewBags(paddyOrderResponse.getProcuredNewBags());
                    paddyOrderXMLData1.setProcuredOldBags(paddyOrderResponse.getProcuredOldBags());
                    paddyOrderXMLData1.setProcuredTotalBags(paddyOrderResponse.getProcuredTotalBags());
                    paddyOrderXMLData1.setGunnysGiven_Date(createdDate);

                    paddyOrderXMLDataArrayList.add(paddyOrderXMLData1);
                }
            }
            if (paddyOrderXMLDataArrayList.size() > 0) {
                paddyXMLData.setPaddyOrderXMLDataArrayList(paddyOrderXMLDataArrayList);
            }

            String totQty = String.format("%.2f", paddyBagsQty);
            paddyXMLData.setTotalQuantity(Double.valueOf(totQty));

            paddyXMLData.setNewBags(Integer.valueOf(paddyFinalNewBags));
            paddyXMLData.setOldBags(Integer.valueOf(paddyFinalOldBags));
            paddyXMLData.setTotalBags(Integer.valueOf(paddyFinalTotBags));

            String totAmt = String.format("%.2f", TotalAmount);
            paddyXMLData.setTotalAmount(Double.valueOf(totAmt));

            paddyXMLData.setMSPAmount(mspAmount);
            paddyXMLData.setPaddyProcured_Date(createdDate);
            paddyXMLData.setCreatedDate(createdDate);

            paddySubmitRequest.setPaddyXMLData(paddyXMLData);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (v.getId() == R.id.ppro_generateamount_Bt) {
                    PaddyProcurementSubmit paddySubmitRequest = new PaddyProcurementSubmit();
                    if (ValidateData(getTokenPaddyResponseGlobal, paddySubmitRequest, gunyOrderResponseListGlobal)) {
                        paddySubmitRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                        paddySubmitRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));
                        StringBuilder finalString = paddySubmitData(paddySubmitRequest);
                        if (finalString != null) {
                            paddySubmitRequest.setXmlFarmerProcurementData(String.valueOf(finalString));
                            ShowSubmitPaddyAlert(paddySubmitRequest);
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void ShowSubmitPaddyAlert(PaddyProcurementSubmit paddySubmitRequest) {
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
                    titleTv.setText(getResources().getString(R.string.procurementConfirmation));
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

                    farmerRegID.setText(getResources().getString(R.string.aadharNo) + ": " + paddySubmitRequest.getPaddyXMLData().getRegistrationNo());
                    mobileNo.setText(getResources().getString(R.string.MobileNo) + ": " + binding.pproMobileTv.getText().toString());
                    Newbags.setText(getResources().getString(R.string.NewBags) + ": " + paddySubmitRequest.getPaddyXMLData().getNewBags());
                    oldBags.setText(getResources().getString(R.string.OldBags) + ": " + paddySubmitRequest.getPaddyXMLData().getOldBags());
                    Totalbags.setText(getResources().getString(R.string.TotalBags) + ": " + paddySubmitRequest.getPaddyXMLData().getTotalBags());
                    double qtyPro = Double.parseDouble(String.format("%.2f", paddySubmitRequest.getPaddyXMLData().getTotalQuantity()));
                    String qtyProStr = String.format("%.3f", qtyPro);
                    totalQuantity.setText(getResources().getString(R.string.Quantity) + ": " + qtyProStr);
                    MSPAmount.setText(getResources().getString(R.string.MSPAmount) + ": " + paddySubmitRequest.getPaddyXMLData().getMSPAmount());
                    totalAmount.setText(getResources().getString(R.string.TotalAmount) + ": " + paddySubmitRequest.getPaddyXMLData().getTotalAmount());
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dialog.isShowing())
                                dialog.dismiss();

                            if ("login_otp".contains("true")) {
                                PaddyOTPRequest request = new PaddyOTPRequest();
                                request.setpPCID(String.valueOf(ppcUserDetails.getPPCID()));
                                request.setTokenNo(getTokenOutputMain.getTokenNo());
                                request.setMobileNo(getTokenOutputMain.getMobile());
                                GetOTPRequest(paddySubmitRequest, request);
                            } else {
                                if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                                    if (customProgressDialog != null && !customProgressDialog.isShowing())
                                        customProgressDialog.show();
                                    paddyProcurementPresenter.PaddyProcurementSubmit(paddySubmitRequest);
                                } else {
                                    Utils.customAlert(getActivity(),
                                            getResources().getString(R.string.PaddyProcurementdetails),
                                            getResources().getString(R.string.no_internet),
                                            getResources().getString(R.string.WARNING), false);
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
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showOTPAlert(PaddyProcurementSubmit paddySubmitRequest, String otpValue) {
        try {
            if (getActivity() != null) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                if (dialog.getWindow() != null) {
                    dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.paddy_otp_alert);
                    dialog.setCancelable(false);
                    if (!dialog.isShowing())
                        dialog.show();
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    Button verifyBtn = dialog.findViewById(R.id.verifyBtn);
                    Button btnCancel = dialog.findViewById(R.id.btnCancel);
                    CustomFontEditText otpEt = dialog.findViewById(R.id.otpEt);
                    CustomFontTextView resendTv = dialog.findViewById(R.id.resendTv);

                    resendTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

                    verifyBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String otp = "";
                            if (otpEt.getText() != null) {
                                otp = otpEt.getText().toString();
                            }
                            if (!TextUtils.isEmpty(otp) && otp.length() != 6) {
                                otpEt.setError(getString(R.string.six_digit_otp));
                                otpEt.requestFocus();
                            } else if (!otp.equalsIgnoreCase(otpValue)) {
                                otpEt.setError(getString(R.string.invalid_otp));
                                otpEt.requestFocus();
                            } else {
                                if (dialog.isShowing())
                                    dialog.dismiss();
                                if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                                    if (customProgressDialog != null && !customProgressDialog.isShowing())
                                        customProgressDialog.show();
                                    paddyProcurementPresenter.PaddyProcurementSubmit(paddySubmitRequest);
                                } else {
                                    Utils.customAlert(getActivity(),
                                            getResources().getString(R.string.PaddyProcurementdetails),
                                            getResources().getString(R.string.no_internet),
                                            getResources().getString(R.string.WARNING), false);
                                }
                            }
                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dialog.isShowing())
                                dialog.dismiss();
                        }
                    });
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private StringBuilder paddySubmitData(PaddyProcurementSubmit paddySubmitRequest) {

        String tokenNum = paddySubmitRequest.getPaddyXMLData().getTokenNo();
        String tokenID = paddySubmitRequest.getPaddyXMLData().getTokenID();
        String ppcCode = paddySubmitRequest.getPaddyXMLData().getPpccode();
        String ppcID = paddySubmitRequest.getPPCID();
        String systemIP = paddySubmitRequest.getPaddyXMLData().getSystemIP();
        String seasonID = paddySubmitRequest.getPaddyXMLData().getSeasonID();
        String regNo = paddySubmitRequest.getPaddyXMLData().getRegistrationNo();
        String regId = paddySubmitRequest.getPaddyXMLData().getRegistrationID();
        String trnId = paddySubmitRequest.getPaddyXMLData().getTransactionID();
        String trnRowId = paddySubmitRequest.getPaddyXMLData().getTransactionRowID();


        final StringBuilder xmlDoc = new StringBuilder();
        xmlDoc.append("<FarmerProcurementDetails>");
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

        if (paddySubmitRequest.getPaddyXMLData().getPaddyOrderXMLDataArrayList() != null &&
                paddySubmitRequest.getPaddyXMLData().getPaddyOrderXMLDataArrayList().size() > 0) {
            for (int x = 0; x < paddySubmitRequest.getPaddyXMLData().getPaddyOrderXMLDataArrayList().size(); x++) {
                xmlDoc.append("<GunnyOrder>");
                xmlDoc.append("<GunnyOrderId>" + paddySubmitRequest.getPaddyXMLData().getPaddyOrderXMLDataArrayList().get(x).getGunnyOrderId() + "</GunnyOrderId>"
                        + "<TokenNewBags>" + paddySubmitRequest.getPaddyXMLData().getPaddyOrderXMLDataArrayList().get(x).getTokenNewBags() + "</TokenNewBags>"
                        + "<TokenOldBags>" + paddySubmitRequest.getPaddyXMLData().getPaddyOrderXMLDataArrayList().get(x).getTokenOldBags() + "</TokenOldBags>"
                        + "<TokenTotalBags>" + paddySubmitRequest.getPaddyXMLData().getPaddyOrderXMLDataArrayList().get(x).getTokenTotalBags() + "</TokenTotalBags>"
                        + "<ProcuredNewBags>" + paddySubmitRequest.getPaddyXMLData().getPaddyOrderXMLDataArrayList().get(x).getProcuredNewBags() + "</ProcuredNewBags>"
                        + "<ProcuredOldBags>" + paddySubmitRequest.getPaddyXMLData().getPaddyOrderXMLDataArrayList().get(x).getProcuredOldBags() + "</ProcuredOldBags>"
                        + "<ProcuredTotalBags>" + paddySubmitRequest.getPaddyXMLData().getPaddyOrderXMLDataArrayList().get(x).getProcuredTotalBags() + "</ProcuredTotalBags>"
                        + "<GunnysGiven_Date>" + paddySubmitRequest.getPaddyXMLData().getPaddyOrderXMLDataArrayList().get(x).getGunnysGiven_Date() + "</GunnysGiven_Date>"
                );
                xmlDoc.append("</GunnyOrder>");
            }
        } else {
            if (gunyOrderResponseListGlobal != null && gunyOrderResponseListGlobal.getIssuedGunnyResponse() != null &&
                    gunyOrderResponseListGlobal.getIssuedGunnyResponse().size() > 0) {
                for (int x = 0; x < gunyOrderResponseListGlobal.getIssuedGunnyResponse().size(); x++) {
                    xmlDoc.append("<GunnyOrder>");
                    xmlDoc.append("<GunnyOrderId>" + gunyOrderResponseListGlobal.getIssuedGunnyResponse().get(x).getGunnyOrderID() + "</GunnyOrderId>"
                            + "<TokenNewBags>" + gunyOrderResponseListGlobal.getIssuedGunnyResponse().get(x).getTokenNewBags() + "</TokenNewBags>"
                            + "<TokenOldBags>" + gunyOrderResponseListGlobal.getIssuedGunnyResponse().get(x).getTokenOldBags() + "</TokenOldBags>"
                            + "<TokenTotalBags>" + gunyOrderResponseListGlobal.getIssuedGunnyResponse().get(x).getTokenTotalBags() + "</TokenTotalBags>"
                            + "<ProcuredNewBags>" + gunyOrderResponseListGlobal.getIssuedGunnyResponse().get(x).getTokenNewBags() + "</ProcuredNewBags>"
                            + "<ProcuredOldBags>" + gunyOrderResponseListGlobal.getIssuedGunnyResponse().get(x).getTokenOldBags() + "</ProcuredOldBags>"
                            + "<ProcuredTotalBags>" + gunyOrderResponseListGlobal.getIssuedGunnyResponse().get(x).getTokenTotalBags() + "</ProcuredTotalBags>"
                            + "<GunnysGiven_Date>" + gunyOrderResponseListGlobal.getIssuedGunnyResponse().get(x).getCreatedDate() + "</GunnysGiven_Date>"
                    );
                    xmlDoc.append("</GunnyOrder>");
                }
            }
        }

        xmlDoc.append("</GunnyOrderSet>");

        xmlDoc.append("<Procurement>");
        xmlDoc.append("<TotalQuantity>" + paddySubmitRequest.getPaddyXMLData().getTotalQuantity() + "</TotalQuantity>"
                + "<NewBags>" + paddySubmitRequest.getPaddyXMLData().getNewBags() + "</NewBags>"
                + "<OldBags>" + paddySubmitRequest.getPaddyXMLData().getOldBags() + "</OldBags>"
                + "<TotalBags>" + paddySubmitRequest.getPaddyXMLData().getTotalBags() + "</TotalBags>"
                + "<TotalAmount>" + paddySubmitRequest.getPaddyXMLData().getTotalAmount() + "</TotalAmount>"
                + "<MSPAmount>" + paddySubmitRequest.getPaddyXMLData().getMSPAmount() + "</MSPAmount>"
                + "<PaddyProcured_Date>" + paddySubmitRequest.getPaddyXMLData().getPaddyProcured_Date() + "</PaddyProcured_Date>"
                + "<CreatedDate>" + paddySubmitRequest.getPaddyXMLData().getCreatedDate() + "</CreatedDate>"
        );
        xmlDoc.append("</Procurement>");

        xmlDoc.append("</FarmerProcurementDetails>");
        return xmlDoc;
    }

    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }
            if (view instanceof TextView) {
                int id = view.getId();
                if (id == R.id.ppro_tokenNo_Tv || id == R.id.ppro_regId_Tv || id == R.id.ppro_farmerName_Tv || id == R.id.ppro_aadhaarNo_Tv
                        || id == R.id.ppro_fatherName_Tv || id == R.id.ppro_age_Tv || id == R.id.ppro_gender_Tv || id == R.id.ppro_mobile_Tv
                        || id == R.id.ppro_district_Tv || id == R.id.ppro_mandal_Tv || id == R.id.ppro_village_Tv
                        || id == R.id.ppro_bankName_Tv || id == R.id.ppro_Accno_Tv || id == R.id.ppro_BrnachName_Tv || id == R.id.ppro_IFSCcode_Tv
                        || id == R.id.InorganicForeignMatter_MaxTV || id == R.id.OrganicForeignMatter_MaxTV || id == R.id.ImmatureShrunkenandShrivelledGrains_MaxTV || id == R.id.DamagedDiscolouredSproutedandWeevilledGrains_MaxTV
                        || id == R.id.paddy_LowerClassGrains7_TV || id == R.id.MoistureContent_MaxTV
                        || id == R.id.ppro_InOrganicMatter_GardeA_Et || id == R.id.ppro_InOrganicMatter_Common_Et || id == R.id.ppro_OrganicMatter_GardeA_Et || id == R.id.ppro_OrganicMatter_Common_Et
                        || id == R.id.ppro_ImmatturedGrains_GardeA_Et || id == R.id.ppro_ImmatturedGrains_Common_Et || id == R.id.ppro_DamegedGrains_GardeA_Et || id == R.id.ppro_DamegedGrains_Common_Et
                        || id == R.id.ppro_LowerClassGrains_GardeA_Et || id == R.id.ppro_LowerClassGrains_Common_Et || id == R.id.ppro_Moisture_GardeA_Et || id == R.id.ppro_Moisture_Common_Et
                        || id == R.id.ppro_totalBagsGiventoFarmer_tv || id == R.id.ppro_totalNewBagsGiven_Tv || id == R.id.ppro_totalOldBagsGiven_Tv
                        || id == R.id.ppro_paddyGrade_tv || id == R.id.ppro_mspamount_tv)
                    ((TextView) view).setText("");

            }
            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearForm((ViewGroup) view);
        }
    }

    private long getTokenID(String tokenNo, GetTokensDDLResponse getTokensResponseList) {
        long tokenID = -1;
        for (int i = 0; i < getTokensResponseList.getGetDDLTokens().size(); i++) {
            if (getTokensResponseList.getGetDDLTokens().get(i).getTokenNo().equals(tokenNo)) {
                tokenID = getTokensResponseList.getGetDDLTokens().get(i).getTokenID();
                break;
            }
        }
        return tokenID;
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
                            getResources().getString(R.string.PaddyProcurementdetails),
                            getString(R.string.ses_expire_re),
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
        GetTokensRequest getTokensProcurementRequest = new GetTokensRequest();
        getTokensProcurementRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
        getTokensProcurementRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));
        getTokensProcurementRequest.setTokenStatus("4");
        if (tokenId == -1) {
            if (customProgressDialog != null)
                customProgressDialog.show();
            paddyProcurementPresenter.GetProcurementGeneratedTokens(getTokensProcurementRequest);
        } else {
            if (customProgressDialog != null)
                customProgressDialog.show();
            landDetailsTableRowsArrayList.clear();
            getTokensProcurementRequest.setTokenID(tokenId);
            paddyProcurementPresenter.GetSelectedProcurementTokenData(getTokensProcurementRequest);
        }
    }

    @Override
    public void getTokensProcurementResponseData(GetTokensDDLResponse getTokensProcurementResponse) {
        try {
            if (customProgressDialog != null && customProgressDialog.isShowing())
                customProgressDialog.dismiss();

            getProcurementResponseList = getTokensProcurementResponse;
            if (getTokensProcurementResponse != null && getTokensProcurementResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (getTokensProcurementResponse.getGetDDLTokens() != null && getTokensProcurementResponse.getGetDDLTokens().size() > 0) {
                    ArrayList<String> getTokens = new ArrayList<>();
                    getTokens.add(0, "--Select--");
                    for (int i = 0; i < getTokensProcurementResponse.getGetDDLTokens().size(); i++) {
                        getTokens.add(getTokensProcurementResponse.getGetDDLTokens().get(i).getTokenNo());
                    }
                    if (getTokens.size() > 0) {
                        Utils.loadSpinnerData(getActivity(), getTokens, binding.paddyTokenNoSp);
                    }
                } else {
                    Toast.makeText(getActivity(), "No Data Available", Toast.LENGTH_LONG).show();
                }
            } else if (getTokensProcurementResponse != null &&
                    getTokensProcurementResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurementdetails),
                        getTokensProcurementResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (getTokensProcurementResponse != null &&
                    getTokensProcurementResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurementdetails),
                        getTokensProcurementResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (getTokensProcurementResponse != null &&
                    getTokensProcurementResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(getTokensProcurementResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else if (getTokensProcurementResponse != null &&
                    getTokensProcurementResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.PaddyProcurementdetails),
                        getTokensProcurementResponse.getResponseMessage(), getFragmentManager());
            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurementdetails),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.PaddyProcurementdetails),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }

    }

    @Override
    public void GetSelectedProcurementTokenData(GetTokensResponse getTokensProcurementResponseData) {
        try {
            getTokenPaddyResponseGlobal = getTokensProcurementResponseData;
            if (getTokensProcurementResponseData != null && getTokensProcurementResponseData.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (getTokensProcurementResponseData.getGetTokenOutput() != null && getTokensProcurementResponseData.getGetTokenOutput().size() > 0) {
                    getTokenOutputMain = getTokensProcurementResponseData.getGetTokenOutput().get(0);
                    binding.pproMainLl.setVisibility(View.VISIBLE);
                    setSelectedTokenData(getTokensProcurementResponseData);
                    GetIssuedGunnyRequest(getTokensProcurementResponseData);
                } else {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.PaddyProcurementdetails),
                            getTokensProcurementResponseData.getResponseMessage(),
                            getString(R.string.ERROR), false);
                }
            } else if (getTokensProcurementResponseData != null &&
                    getTokensProcurementResponseData.getStatusCode() == AppConstants.FAILURE_CODE) {

                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurementdetails),
                        getTokensProcurementResponseData.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else if (getTokensProcurementResponseData != null &&
                    getTokensProcurementResponseData.getStatusCode() == AppConstants.NO_DAT_CODE) {

                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurementdetails),
                        getTokensProcurementResponseData.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else if (getTokensProcurementResponseData != null &&
                    getTokensProcurementResponseData.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(getTokensProcurementResponseData.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else if (getTokensProcurementResponseData != null &&
                    getTokensProcurementResponseData.getStatusCode() == AppConstants.SESSION_CODE) {

                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.PaddyProcurementdetails),
                        getTokensProcurementResponseData.getResponseMessage(), getFragmentManager());
            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurementdetails),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.PaddyProcurementdetails),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
    }

    private void GetIssuedGunnyRequest(GetTokensResponse getTokensProcurementResponseData) {
        IssuedGunnyDataRequest issuedGunnyDataRequest = new IssuedGunnyDataRequest();
        issuedGunnyDataRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
        issuedGunnyDataRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));
        issuedGunnyDataRequest.setTokenID("" + getTokensProcurementResponseData.getGetTokenOutput().get(0).getTokenID());
        issuedGunnyDataRequest.setTransactionID(getTokensProcurementResponseData.getGetTokenOutput().get(0).getFarmerTransactionID());
        issuedGunnyDataRequest.setTransactionRowID("" + getTokensProcurementResponseData.getGetTokenOutput().get(0).getFarmerTransactionRowID());
        issuedGunnyDataRequest.setTransactionStatus("00"); // static
        if (customProgressDialog != null)
            customProgressDialog.show();
        paddyProcurementPresenter.GetIssuedGunnyDataResponseData(issuedGunnyDataRequest);
    }

    private void GetOTPRequest(PaddyProcurementSubmit paddyProcurementSubmit, PaddyOTPRequest paddyOTPRequest) {
        if (ConnectionDetector.isConnectedToInternet(getActivity())) {
            if (customProgressDialog != null)
                customProgressDialog.show();
            paddyProcurementPresenter.PaddyProcurementOTP(paddyProcurementSubmit, paddyOTPRequest);
        } else {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.PaddyProcurementdetails),
                    getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.WARNING), false);
        }
    }


    @Override
    public void GetIssuedGunnyDataResponseData(IssuedGunnyDataResponse issuedGunnyDataResponse) {
        try {

            gunyOrderResponseListGlobal = issuedGunnyDataResponse;

            if (issuedGunnyDataResponse != null &&
                    issuedGunnyDataResponse.getStatusCode() == AppConstants.SUCCESS_CODE
                    && issuedGunnyDataResponse.getIssuedGunnyResponse() != null
                    && issuedGunnyDataResponse.getIssuedGunnyResponse().size() > 0) {

                for (int i = 0; i < issuedGunnyDataResponse.getIssuedGunnyResponse().size(); i++) {
                    setTransactionsData(issuedGunnyDataResponse.getIssuedGunnyResponse().get(i));
                }

            } else if (issuedGunnyDataResponse != null &&
                    issuedGunnyDataResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurement),
                        issuedGunnyDataResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (issuedGunnyDataResponse != null &&
                    issuedGunnyDataResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.PaddyProcurementdetails),
                        issuedGunnyDataResponse.getResponseMessage(), getFragmentManager());
            } else if (issuedGunnyDataResponse != null &&
                    issuedGunnyDataResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
                Utils.callPlayAlert(issuedGunnyDataResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurement),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }


        } catch (Exception e) {
            if (customProgressDialog != null && customProgressDialog.isShowing())
                customProgressDialog.dismiss();
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.PaddyProcurement),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();


        }

        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();

    }

    @Override
    public void PaddyProcurementSubmit(ProcurementSubmitResponse procurementSubmitResponse) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
        try {
            if (procurementSubmitResponse != null && procurementSubmitResponse.getStatusCode() == AppConstants.SUCCESS_CODE
                    && procurementSubmitResponse.getSaveProcurementOutput() != null
                    && procurementSubmitResponse.getSaveProcurementOutput()
                    .getFarmerTransactionID() != null) {

                Fragment fragment = new DashboardFragment();

                PrintAlert(getActivity(), fragment, getFragmentManager(), procurementSubmitResponse,
                        getResources().getString(R.string.PaddyProcurement),
                        procurementSubmitResponse.getResponseMessage() + "\n" + "Transaction ID: " +
                                procurementSubmitResponse.getSaveProcurementOutput().getFarmerTransactionID());

            } else if (procurementSubmitResponse != null &&
                    procurementSubmitResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurement),
                        procurementSubmitResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (procurementSubmitResponse != null &&
                    procurementSubmitResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.PaddyProcurementdetails),
                        procurementSubmitResponse.getResponseMessage(), getFragmentManager());
            } else if (procurementSubmitResponse != null &&
                    procurementSubmitResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(procurementSubmitResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurement),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.PaddyProcurement),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }

    }

    @Override
    public void PaddyProcurementOTP(PaddyProcurementSubmit paddyProcurementSubmit, OTPResponse otpResponse) {

        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
        try {
            if (otpResponse != null && otpResponse.getStatusCode()
                    == AppConstants.SUCCESS_CODE) {
                if (!TextUtils.isEmpty(otpResponse.getOtp())) {
                    showOTPAlert(paddyProcurementSubmit, otpResponse.getOtp());
                } else {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.PaddyProcurement),
                            otpResponse.getResponseMessage(),
                            getString(R.string.otp_is_empty), false);
                }
            } else if (otpResponse != null &&
                    otpResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurement),
                        otpResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (otpResponse != null &&
                    otpResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.PaddyProcurementdetails),
                        otpResponse.getResponseMessage(), getFragmentManager());
            } else if (otpResponse != null &&
                    otpResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(otpResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurement),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.PaddyProcurement),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }

    }

    private void PrintAlert(Activity activity,
                            Fragment fragment,
                            final FragmentManager fragmentManager,
                            ProcurementSubmitResponse procurementSubmitResponse,
                            String title, String alertMsg) {
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
                dialogMessage.setText(alertMsg);


                Button print = dialog.findViewById(R.id.btDialogPrint);
                Button cancel = dialog.findViewById(R.id.btDialogCancel);

                if (!(sharedPreferences.getBoolean(AppConstants.PRINTER_CON_FLAG, false))) {
                    alertMsg = alertMsg.concat("\n").concat(getString(R.string.plz_take_reprint));
                    print.setText(getString(R.string.Ok));
                    cancel.setVisibility(View.GONE);
                }
                dialogMessage.setText(alertMsg);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        AppConstants.FARG_TAG = PaddyProcurementFragment.class.getSimpleName();
                        callFragment(fragment, fragmentManager, AppConstants.FARG_TAG);
                    }
                });

                print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        if (!(sharedPreferences.getBoolean(AppConstants.PRINTER_CON_FLAG, false))) {
                            AppConstants.FARG_TAG = PaddyProcurementFragment.class.getSimpleName();
                            callFragment(fragment, fragmentManager, AppConstants.FARG_TAG);
                            Toast.makeText(activity, getString(R.string.plz_take_reprint), Toast.LENGTH_SHORT).show();
                        } else if (AppConstants.BT_ADDRESS == null || AppConstants.BT_ADDRESS.equals("")) {
                            AppConstants.FARG_TAG = PaddyProcurementFragment.class.getSimpleName();
                            callFragment(fragment, fragmentManager, AppConstants.FARG_TAG);
                            Toast.makeText(activity, getString(R.string.plz_take_reprint), Toast.LENGTH_SHORT).show();
                        } else {
                            callPrintMethod(procurementSubmitResponse);
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

    private void callPrintMethod(ProcurementSubmitResponse procurementSubmitResponse) {

        ProRePrintRequest proRePrintRequest = new ProRePrintRequest();
        proRePrintRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
        proRePrintRequest.setpPCID(String.valueOf(ppcUserDetails.getPPCID()));
        proRePrintRequest.setFarmerTranscationID(procurementSubmitResponse.getSaveProcurementOutput().getFarmerTransactionID().trim());

        if (ConnectionDetector.isConnectedToInternet(getActivity())) {
            customProgressDialog.show();
            rePrintPresenter.GetProTxnData(proRePrintRequest);
        } else {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.PaddyProcurementdetails),
                    getResources().getString(R.string.no_internet),
                    getString(R.string.WARNING), false);
        }
    }

    @Override
    public void GetRePrintTokenData(TokenRePrintResponse tokenRePrintResponse) {

    }

    @Override
    public void GetProcRePrintData(ProcRePrintResponse procRePrintResponse) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
        try {
            if (procRePrintResponse != null && procRePrintResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (procRePrintResponse.getPrintprocurementdata() != null) {

                    if (sharedPreferences.getBoolean(AppConstants.PRINTER_CON_FLAG, false)) {
                        if (AppConstants.BT_ADDRESS == null || AppConstants.BT_ADDRESS.equals("")) {
                            Utils.ShowPrintAlert(getActivity(), getFragmentManager(), customProgressDialog);
                            return;
                        }
                        new printAsyncTask(procRePrintResponse).execute();

                    } else {

                        try {
                            Fragment fragment = new DashboardFragment();
                            AppConstants.FARG_TAG = PaddyProcurementFragment.class.getSimpleName();
                            callFragment(fragment, getFragmentManager(), AppConstants.FARG_TAG);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getActivity(), getResources().getString(R.string.plz_take_reprint), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Utils.customNavigateAlert(getActivity(),
                            getResources().getString(R.string.PaddyProcurement),
                            procRePrintResponse.getResponseMessage()
                                    + "\n"
                                    + getResources().getString(R.string.plz_take_reprint),
                            getFragmentManager());
                }
            } else if (procRePrintResponse != null &&
                    procRePrintResponse.getStatusCode() == AppConstants.FAILURE_CODE) {

                Utils.customNavigateAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurement),
                        procRePrintResponse.getResponseMessage()
                                + "\n"
                                + getResources().getString(R.string.plz_take_reprint),
                        getFragmentManager());

            } else if (procRePrintResponse != null &&
                    procRePrintResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {

                Utils.customNavigateAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurement),
                        procRePrintResponse.getResponseMessage()
                                + "\n"
                                + getResources().getString(R.string.plz_take_reprint),
                        getFragmentManager());

            } else if (procRePrintResponse != null &&
                    procRePrintResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(procRePrintResponse.getResponseMessage(),
                        getActivity(), getFragmentManager(), true);

            } else if (procRePrintResponse != null &&
                    procRePrintResponse.getStatusCode() == AppConstants.SESSION_CODE) {

                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.PaddyProcurementdetails),
                        procRePrintResponse.getResponseMessage(), getFragmentManager());
            } else {
                Utils.customNavigateAlert(getActivity(),
                        getResources().getString(R.string.PaddyProcurement),
                        getResources().getString(R.string.server_not)
                                + "\n"
                                + getResources().getString(R.string.plz_take_reprint),
                        getFragmentManager());
            }
        } catch (Resources.NotFoundException e) {
            Utils.customNavigateAlert(getActivity(),
                    getResources().getString(R.string.PaddyProcurement),
                    getResources().getString(R.string.something)
                            + "\n"
                            + getResources().getString(R.string.plz_take_reprint),
                    getFragmentManager());
            e.printStackTrace();
        }
    }

    @Override
    public void GetTCRePrintData(TCRePrintResponse tcRePrintResponse) {

    }

    @Override
    public void onError(int code, String msg) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();

        if (code == AppConstants.ERROR_CODE) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.PaddyProcurementdetails),
                    msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else if (code == AppConstants.EXCEPTION_CODE) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.PaddyProcurementdetails),
                    getString(R.string.something) +
                            msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.PaddyProcurementdetails),
                    getString(R.string.something),
                    getResources().getString(R.string.ERROR),
                    false);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class printAsyncTask extends AsyncTask<Void, Void, Boolean> {
        ProcRePrintResponse procRePrintResponse;
        Bitmap bmp = null;

        printAsyncTask(ProcRePrintResponse procRePrintResponse) {
            this.procRePrintResponse = procRePrintResponse;
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
            if (customProgressDialog != null && !customProgressDialog.isShowing()) {
                customProgressDialog.show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... args) {
            boolean flag = false;

            try {
                flag = connectBT();
                if (flag) {
                    String ppcTitle = "", farTitle = "";
                    char nLine = '\n';
                    String commonPrintTxt = commonPrint(procRePrintResponse, nLine);

                    ppcTitle = "                       PPC Copy" + nLine + commonPrintTxt;
                    farTitle = "                       Farmer Copy" + nLine + commonPrintTxt;

                    aPrinter.logoPrinting(AppConstants.BT_ADDRESS, bmp);
                    aPrinter.multiLingualPrint(AppConstants.BT_ADDRESS, ppcTitle, 22, Typeface.SANS_SERIF);

                    aPrinter.logoPrinting(AppConstants.BT_ADDRESS, bmp);
                    aPrinter.multiLingualPrint(AppConstants.BT_ADDRESS, farTitle, 22, Typeface.SANS_SERIF);

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

            try {
                aPrinter.closeBT();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!aBoolean) {
                Utils.customPrintFailedAlert(getActivity(),
                        PaddyProcurementFragment.class.getSimpleName(),
                        getFragmentManager(),
                        getString(R.string.PaddyProcurementdetails),
                        getString(R.string.failed_tc_print));
            } else {
                try {
                    Fragment fragment = new DashboardFragment();
                    AppConstants.FARG_TAG = PaddyProcurementFragment.class.getSimpleName();
                    callFragment(fragment, getFragmentManager(), AppConstants.FARG_TAG);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }

    private String commonPrint(ProcRePrintResponse procRePrintResponse, char nLine) {
        return "----------------------" + nLine +
                "(వరి సేకరణ రసీదు): " + nLine +
                "----------------------" + nLine +
                "URN No: " + nLine + procRePrintResponse.getPrintprocurementdata().getURNNumber() + nLine +
                "తేది : " + procRePrintResponse.getPrintprocurementdata().getTransactionDate().substring(0, 10) + nLine +
                "అభ్యర్ధన సంఖ్య : " + nLine + procRePrintResponse.getPrintprocurementdata().getFarmerTransactionID() + nLine +
                "పిపిసిసెంటర్  నంబరు : " + ppcUserDetails.getPPCCode() + nLine +
                "పిపిసిసెంటర్ పేరు : " + ppcUserDetails.getPPCName() + nLine +
                "రైతు వివరాలు : " + nLine +
                "----------------------" + nLine +
                "నమోదు సంఖ్య : " + procRePrintResponse.getPrintprocurementdata().getRegistrationNo() + nLine +
                "రైతు పేరు : " + procRePrintResponse.getPrintprocurementdata().getFarmerName() + nLine +
                "తండ్రి పేరు : " + procRePrintResponse.getPrintprocurementdata().getFarmerFatherName() + nLine +
                "బ్యాంకు  వివరాలు : " + nLine +
                "----------------------" + nLine +
                "ఖాతా నంబరు : " + procRePrintResponse.getPrintprocurementdata().getBankAccountNo() + nLine +
                "బ్యాంక్ పేరు : " + procRePrintResponse.getPrintprocurementdata().getBankName() + nLine +
                "బ్రాంచ్ పేరు : " + procRePrintResponse.getPrintprocurementdata().getBranchName() + nLine +
                "ఐఎఫ్ఎస్సి కోడ్ : " + procRePrintResponse.getPrintprocurementdata().getIFSCCode() + nLine +
                "వరి వివరాలు : " +
                "----------------------" + nLine +
                "వరి రకం : " + procRePrintResponse.getPrintprocurementdata().getPaddyType() + nLine +
                "MSP నగదు : " + procRePrintResponse.getPrintprocurementdata().getMSPAmount() + nLine +
                "కొత్త సంచులు : " + procRePrintResponse.getPrintprocurementdata().getNewBags() + nLine +
                "పాత సంచులు : " + procRePrintResponse.getPrintprocurementdata().getOldBags() + nLine +
                "మొత్తం సంచులు : " + procRePrintResponse.getPrintprocurementdata().getTotalBags() + nLine +
                "పరిమాణం (క్వింటాల్లలో) : " + procRePrintResponse.getPrintprocurementdata().getQuantity() + nLine +
                "----------------------" + nLine +
                "మొత్తం నగదు : " + procRePrintResponse.getPrintprocurementdata().getTotalAmount() + nLine + nLine +
                "పిపిసి సంతకం : " + nLine + nLine +
                "రైతు సంతకం : " + nLine;
    }


    private static void callFragment(Fragment fragment, FragmentManager fragmentManager, String name) {
        try {
            AppConstants.FARG_TAG = name;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.dashboard_container, fragment, AppConstants.FARG_TAG);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTransactionsData(IssuedGunnyResponse issuedGunnyResponse) {
        try {
            if (landDetailsTableRowsArrayList.size() == 0) {
                binding.PPCtoMillerPaadyDetailsTabel.removeAllViews();
            }
            LayoutInflater mInflater = LayoutInflater.from(getActivity());
            final TableRow paramView = (TableRow) mInflater.inflate(R.layout.gunny_bags_row, null);

            CheckBox FRLd_check_Cb = paramView.findViewById(R.id.FRLd_check_Cb);
            TextView orderNumTV = paramView.findViewById(R.id.orderNumTV);
            TextView NewBagsTV = paramView.findViewById(R.id.NewBagsTV);
            TextView OldBagsTV = paramView.findViewById(R.id.OldBagsTV);
            TextView TotBagsTV = paramView.findViewById(R.id.TotBagsTV);

            orderNumTV.setText("" + issuedGunnyResponse.getGunnyOrderID());
            NewBagsTV.setText("" + issuedGunnyResponse.getTokenNewBags());
            OldBagsTV.setText("" + issuedGunnyResponse.getTokenOldBags());
            TotBagsTV.setText("" + issuedGunnyResponse.getTokenTotalBags());

            EditText FilNewBagsET = paramView.findViewById(R.id.FilNewBagsET);
            EditText FilOldBagsET = paramView.findViewById(R.id.FilOldBagsET);
            EditText FilTotBagsET = paramView.findViewById(R.id.FilTotBagsET);

            EditText EmptyNewBagET = paramView.findViewById(R.id.EmptyNewBagET);
            EditText EmpOldBagsET = paramView.findViewById(R.id.EmpOldBagsET);
            EditText EmpTotBagsET = paramView.findViewById(R.id.EmpTotBagsET);

            FilTotBagsET.setEnabled(false);
            EmpTotBagsET.setEnabled(false);

            FRLd_check_Cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (buttonView.isChecked()) {
                            String EmpNewBagsETStr = EmptyNewBagET.getText().toString().trim();
                            String EmpOldBagsETStr = EmpOldBagsET.getText().toString().trim();
                            if (TextUtils.isEmpty(EmpNewBagsETStr)) {
                                EmptyNewBagET.setError("Please enter return new bags");
                                EmptyNewBagET.requestFocus();
                                buttonView.setChecked(false);
                                return;
                            }
                            if (Integer.valueOf(NewBagsTV.getText().toString()) < Integer.valueOf(EmpNewBagsETStr)) {
                                EmptyNewBagET.setError("Return new bags must equal or below new bags");
                                EmptyNewBagET.requestFocus();
                                buttonView.setChecked(false);
                                return;
                            }
                            if (TextUtils.isEmpty(EmpOldBagsETStr)) {
                                EmpOldBagsET.setError("Please enter return old bags");
                                EmpOldBagsET.requestFocus();
                                buttonView.setChecked(false);
                                return;
                            }


                            if (Integer.valueOf(OldBagsTV.getText().toString()) < Integer.valueOf(EmpOldBagsETStr)) {
                                EmpOldBagsET.setError("Return old bags must equal or below old bags");
                                EmpOldBagsET.requestFocus();
                                buttonView.setChecked(false);
                                return;
                            }

                            int FilNewBagsETStr = Integer.valueOf(NewBagsTV.getText().toString()) - Integer.valueOf(EmpNewBagsETStr);
                            int FilOldBagsETStr = Integer.valueOf(OldBagsTV.getText().toString()) - Integer.valueOf(EmpOldBagsETStr);
                            FilNewBagsET.setText("" + FilNewBagsETStr);
                            FilOldBagsET.setText("" + FilOldBagsETStr);
                            Integer FilledTotBags = Integer.valueOf(FilNewBagsETStr) + Integer.valueOf(EmpNewBagsETStr);
                            Integer EmptyTotBags = Integer.valueOf(FilOldBagsETStr) + Integer.valueOf(EmpOldBagsETStr);
                            if (!(FilledTotBags == Integer.parseInt(NewBagsTV.getText().toString()))) {
                                buttonView.setChecked(false);
                                FilTotBagsET.setError("Total new bags must equal with the entered new bags");
                                FilTotBagsET.requestFocus();
                                return;
                            }
                            if (!(EmptyTotBags == Integer.parseInt(OldBagsTV.getText().toString()))) {
                                buttonView.setChecked(false);
                                EmpTotBagsET.setError("Total old bags must equal with the entered old bags");
                                EmpTotBagsET.requestFocus();
                                return;
                            }
                            Integer FinalFilledTotBags = Integer.valueOf(FilNewBagsETStr) + Integer.valueOf(FilOldBagsETStr);
                            Integer FinalEmpTotBags = Integer.valueOf(EmpNewBagsETStr) + Integer.valueOf(EmpOldBagsETStr);
                            FilTotBagsET.setText(String.valueOf(FinalFilledTotBags));
                            EmpTotBagsET.setText(String.valueOf(FinalEmpTotBags));
                            FilNewBagsET.setEnabled(false);
                            FilOldBagsET.setEnabled(false);
                            EmptyNewBagET.setEnabled(false);
                            EmpOldBagsET.setEnabled(false);
                            PaddyOrderResponse paddyOrderResponse = new PaddyOrderResponse();
                            paddyOrderResponse.setGunnyOrderId("" + issuedGunnyResponse.getGunnyOrderID());
                            paddyOrderResponse.setTokenNewBags(issuedGunnyResponse.getTokenNewBags());
                            paddyOrderResponse.setTokenOldBags(issuedGunnyResponse.getTokenOldBags());
                            paddyOrderResponse.setTokenTotalBags(issuedGunnyResponse.getTokenTotalBags());
                            paddyOrderResponse.setProcuredNewBags(Integer.valueOf(FilNewBagsET.getText().toString()));
                            paddyOrderResponse.setProcuredOldBags(Integer.valueOf(FilOldBagsET.getText().toString()));
                            paddyOrderResponse.setProcuredTotalBags(Integer.valueOf(FilTotBagsET.getText().toString()));
                            paddyOrderResponse.setFinalTotalBags(Integer.valueOf(FilTotBagsET.getText().toString()));
                            paddyOrderResponse.setFinalEmptyBags(Integer.valueOf(EmpTotBagsET.getText().toString()));
                            addRowToGunnyOrderTable(paddyOrderResponse);
                        } else {
                            try {
                                int totNew, totOld;
                                if (TextUtils.isEmpty(binding.pproGunnynewbagsEt.getText().toString())) {
                                    totNew = 0;
                                } else {
                                    totNew = Integer.parseInt(binding.pproGunnynewbagsEt.getText().toString());
                                }

                                if (TextUtils.isEmpty(binding.pproGunnyoldbagsEt.getText().toString())) {
                                    totOld = 0;
                                } else {
                                    totOld = Integer.parseInt(binding.pproGunnyoldbagsEt.getText().toString());
                                }

                                if (paddyOrderResponseArrayList.size() > 0) {
                                    String ordID = orderNumTV.getText().toString().trim();
                                    ArrayList<PaddyOrderResponse> tempViewArrayList = new ArrayList<>(paddyOrderResponseArrayList);
                                    for (int x = 0; x < tempViewArrayList.size(); x++) {
                                        if (tempViewArrayList.get(x).getGunnyOrderId().equalsIgnoreCase(ordID)) {
                                            int bags = Integer.valueOf(binding.pproGunnynewbagsEt.getText().toString()) + Integer.valueOf(binding.pproGunnyoldbagsEt.getText().toString());
                                            Double bagsQty = bags * 0.4;
                                            Double CurrentQty = (tempViewArrayList.get(x).getProcuredNewBags() + tempViewArrayList.get(x).getProcuredOldBags()) * 0.4;
                                            binding.pproGunnybagsqtyEt.setText(String.valueOf(bagsQty - CurrentQty));
                                            binding.pproGunnynewbagsEt.setText(String.valueOf(totNew - tempViewArrayList.get(x).getProcuredNewBags()));
                                            binding.pproGunnyoldbagsEt.setText(String.valueOf(totOld - tempViewArrayList.get(x).getProcuredOldBags()));
                                            paddyOrderResponseArrayList.remove(tempViewArrayList.get(x));
                                            break;
                                        }
                                    }
                                    FilTotBagsET.setText("");
                                    EmpTotBagsET.setText("");
                                    FilNewBagsET.setText("");
                                    FilOldBagsET.setText("");
                                    FilNewBagsET.setEnabled(true);
                                    FilOldBagsET.setEnabled(true);
                                    EmptyNewBagET.setEnabled(true);
                                    EmpOldBagsET.setEnabled(true);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            binding.PPCtoMillerPaadyDetailsTabel.addView(paramView);
            landDetailsTableRowsArrayList.add(paramView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRowToGunnyOrderTable(PaddyOrderResponse paddyOrderResponse) {
        try {
            int totNew, totOld;
            if (TextUtils.isEmpty(binding.pproGunnynewbagsEt.getText().toString())) {
                totNew = 0;
            } else {
                totNew = Integer.parseInt(binding.pproGunnynewbagsEt.getText().toString());
            }
            if (TextUtils.isEmpty(binding.pproGunnyoldbagsEt.getText().toString())) {
                totOld = 0;
            } else {
                totOld = Integer.parseInt(binding.pproGunnyoldbagsEt.getText().toString());
            }
            binding.pproGunnynewbagsEt.setText(String.valueOf(totNew + paddyOrderResponse.getProcuredNewBags()));
            binding.pproGunnyoldbagsEt.setText(String.valueOf(totOld + paddyOrderResponse.getProcuredOldBags()));
            int bags = Integer.valueOf(binding.pproGunnynewbagsEt.getText().toString()) + Integer.valueOf(binding.pproGunnyoldbagsEt.getText().toString());
            Double bagsQty = bags * 0.4;
            String bagsQtyStr = String.format("%.3f", bagsQty);

            binding.pproGunnybagsqtyEt.setText(bagsQtyStr);

            paddyOrderResponseArrayList.add(paddyOrderResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setSelectedTokenData(GetTokensResponse getTokensProcurementResponseData) {
        try {
            if (getTokensProcurementResponseData.getGetTokenOutput().get(0).getTokenNo() != null) {
                binding.pproTokenNoTv.setText(getTokensProcurementResponseData.getGetTokenOutput().get(0).getTokenNo().trim());
            }
            if (getTokensProcurementResponseData.getGetTokenOutput().get(0).getRegistrationNo() != null) {
                binding.pproRegIdTv.setText(getTokensProcurementResponseData.getGetTokenOutput().get(0).getRegistrationNo().trim());
            }
            if (getTokensProcurementResponseData.getGetTokenOutput().get(0).getFarmerName() != null) {
                binding.pproFarmerNameTv.setText(getTokensProcurementResponseData.getGetTokenOutput().get(0).getFarmerName().trim());
            }
            if (getTokensProcurementResponseData.getGetTokenOutput().get(0).getRegistrationNo() != null) {
                binding.pproAadhaarNoTv.setText(getTokensProcurementResponseData.getGetTokenOutput().get(0).getRegistrationNo().trim());
            }
            if (getTokensProcurementResponseData.getGetTokenOutput().get(0).getFarmerFatherName() != null) {
                binding.pproFatherNameTv.setText(getTokensProcurementResponseData.getGetTokenOutput().get(0).getFarmerFatherName().trim());
            }
            if (getTokensProcurementResponseData.getGetTokenOutput().get(0).getAge() != null) {
                binding.pproAgeTv.setText("" + getTokensProcurementResponseData.getGetTokenOutput().get(0).getAge());
            }
            if (getTokensProcurementResponseData.getGetTokenOutput().get(0).getGender() != null) {
                binding.pproGenderTv.setText("" + getTokensProcurementResponseData.getGetTokenOutput().get(0).getGender());
                if (getTokensProcurementResponseData.getGetTokenOutput().get(0).getGender() == 1)
                    (binding.pproGenderTv).setText(R.string.Male);
                else
                    (binding.pproGenderTv).setText(R.string.Female);
            }


            if (getTokensProcurementResponseData.getGetTokenOutput().get(0).getMobile() != null) {
                binding.pproMobileTv.setText(getTokensProcurementResponseData.getGetTokenOutput().get(0).getMobile().trim());
            }
            if (getTokensProcurementResponseData.getGetTokenOutput().get(0).getFDistrictID() != null) {
                paddyTestRepository.getDistrictName(this,
                        getTokensProcurementResponseData.getGetTokenOutput().get(0).getFDistrictID().trim());
            }

            if (getTokensProcurementResponseData.getGetTokenOutput().get(0).getFDistrictID() != null
                    && getTokensProcurementResponseData.getGetTokenOutput().get(0).getFMandalID() != null) {
                paddyTestRepository.getMandalName(this,
                        getTokensProcurementResponseData.getGetTokenOutput().get(0).getFDistrictID().trim(),
                        getTokensProcurementResponseData.getGetTokenOutput().get(0).getFMandalID().trim());
            }

            if (getTokensProcurementResponseData.getGetTokenOutput().get(0).getFDistrictID() != null
                    && getTokensProcurementResponseData.getGetTokenOutput().get(0).getFMandalID() != null
                    && getTokensProcurementResponseData.getGetTokenOutput().get(0).getFVillageID() != null) {

                paddyTestRepository.getVillageName(this,
                        getTokensProcurementResponseData.getGetTokenOutput().get(0).getFDistrictID().trim(),
                        getTokensProcurementResponseData.getGetTokenOutput().get(0).getFMandalID().trim(),
                        getTokensProcurementResponseData.getGetTokenOutput().get(0).getFVillageID().trim());
            }
            if (getTokensProcurementResponseData.getGetTokenOutput().get(0).getBankId() != null) {
                bbRepository.getBankName(this,
                        "" + getTokensProcurementResponseData.getGetTokenOutput().get(0).getBankId().toString().trim());
            }
            if (getTokensProcurementResponseData.getGetTokenOutput().get(0).getBankId() != null
                    && getTokensProcurementResponseData.getGetTokenOutput().get(0).getBranchId() != null) {
                bbRepository.getBranchName(this,
                        getTokensProcurementResponseData.getGetTokenOutput().get(0).getBankId().toString().trim(),
                        getTokensProcurementResponseData.getGetTokenOutput().get(0).getBranchId().toString().trim());
            }
            if (getTokensProcurementResponseData.getGetTokenOutput().get(0).getBankAccountNo() != null) {
                binding.pproAccnoTv.setText(getTokensProcurementResponseData.getGetTokenOutput().get(0).getBankAccountNo());
            }
            if (getTokensProcurementResponseData.getGetTokenOutput().get(0).getIFSCCode() != null) {
                binding.pproIFSCcodeTv.setText(getTokensProcurementResponseData.getGetTokenOutput().get(0).getIFSCCode());
            }
            setFaqdata(getTokensProcurementResponseData);
            setGunnyData(getTokensProcurementResponseData);
            setPaddyData(getTokensProcurementResponseData);
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
                    binding.paddyLowerClassGrains7TV.setText(String.valueOf(paddyEntities.get(x).getLowGradeGrain()));
                    binding.MoistureContentMaxTV.setText(String.valueOf(paddyEntities.get(x).getMoisture()));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setViewVISIBLE(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void setViewGONE(View view) {
        view.setVisibility(View.GONE);
    }

    private void setFaqdata(GetTokensResponse getTokenPaddyResponse) {
        try {
            if (getTokenPaddyResponse.getGetTokenOutput().get(0).getPaddyTypeID() != null) {
                getPaddyDetails(getTokenPaddyResponse.getGetTokenOutput().get(0).getPaddyTypeID());
            }
            if (getTokenPaddyResponse.getGetTokenOutput().get(0).getPaddyTypeID() != null) {
                switch (getTokenPaddyResponse.getGetTokenOutput().get(0).getPaddyTypeID()) {
                    case 1:
                        setViewVISIBLE(binding.lowerLL);
                        setViewVISIBLE(binding.pproGardeAValuesHeaderTv);
                        setViewVISIBLE(binding.pproInOrganicMatterGardeALl);
                        setViewVISIBLE(binding.pproOrganicMatterGardeALl);
                        setViewVISIBLE(binding.pproImmatturedGrainsGardeALl);
                        setViewVISIBLE(binding.pproDamegedGrainsGardeALl);
                        setViewVISIBLE(binding.pproLowerClassGrainsGardeALl);
                        setViewVISIBLE(binding.pproMoistureGardeALl);

                        setViewGONE(binding.pproCommonValuesHeaderTv);
                        setViewGONE(binding.pproInOrganicMatterCommonLl);
                        setViewGONE(binding.pproOrganicMatterCommonLl);
                        setViewGONE(binding.pproImmatturedGrainsCommonLl);
                        setViewGONE(binding.pproDamegedGrainsCommonLl);
                        setViewGONE(binding.pproLowerClassGrainsCommonLl);
                        setViewGONE(binding.pproMoistureCommonLl);

                        binding.pproInOrganicMatterGardeAEt.setText("" + getTokenPaddyResponse.getGetTokenOutput().get(0).getWastage());
                        binding.pproOrganicMatterGardeAEt.setText("" + getTokenPaddyResponse.getGetTokenOutput().get(0).getOtherEatingGrains());
                        binding.pproImmatturedGrainsGardeAEt.setText("" + getTokenPaddyResponse.getGetTokenOutput().get(0).getUnmaturedGrains());
                        binding.pproDamegedGrainsGardeAEt.setText("" + getTokenPaddyResponse.getGetTokenOutput().get(0).getDamagedGrains());
                        binding.pproLowerClassGrainsGardeAEt.setText("" + getTokenPaddyResponse.getGetTokenOutput().get(0).getLowGradeGrain());
                        binding.pproMoistureGardeAEt.setText("" + getTokenPaddyResponse.getGetTokenOutput().get(0).getMoisture());
                        break;

                    case 2:
                        setViewGONE(binding.lowerLL);
                        setViewVISIBLE(binding.pproCommonValuesHeaderTv);
                        setViewVISIBLE(binding.pproInOrganicMatterCommonLl);
                        setViewVISIBLE(binding.pproOrganicMatterCommonLl);
                        setViewVISIBLE(binding.pproImmatturedGrainsCommonLl);
                        setViewVISIBLE(binding.pproDamegedGrainsCommonLl);
                        setViewVISIBLE(binding.pproLowerClassGrainsCommonLl);
                        setViewVISIBLE(binding.pproMoistureCommonLl);

                        setViewGONE(binding.pproGardeAValuesHeaderTv);
                        setViewGONE(binding.pproInOrganicMatterGardeALl);
                        setViewGONE(binding.pproOrganicMatterGardeALl);
                        setViewGONE(binding.pproImmatturedGrainsGardeALl);
                        setViewGONE(binding.pproDamegedGrainsGardeALl);
                        setViewGONE(binding.pproLowerClassGrainsGardeALl);
                        setViewGONE(binding.pproMoistureGardeALl);

                        binding.pproInOrganicMatterCommonEt.setText("" + getTokenPaddyResponse.getGetTokenOutput().get(0).getWastage());
                        binding.pproOrganicMatterCommonEt.setText("" + getTokenPaddyResponse.getGetTokenOutput().get(0).getOtherEatingGrains());
                        binding.pproImmatturedGrainsCommonEt.setText("" + getTokenPaddyResponse.getGetTokenOutput().get(0).getUnmaturedGrains());
                        binding.pproDamegedGrainsCommonEt.setText("" + getTokenPaddyResponse.getGetTokenOutput().get(0).getDamagedGrains());
                        binding.pproLowerClassGrainsCommonEt.setText("" + getTokenPaddyResponse.getGetTokenOutput().get(0).getLowGradeGrain());
                        binding.pproMoistureCommonEt.setText("" + getTokenPaddyResponse.getGetTokenOutput().get(0).getMoisture());
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setGunnyData(GetTokensResponse getTokenPaddyResponse) {
        try {
            if (getTokenPaddyResponse.getGetTokenOutput().get(0).getTotalBags() != null) {
                binding.pproTotalBagsGiventoFarmerTv.setText("" + getTokenPaddyResponse.getGetTokenOutput().get(0).getTotalBags());
            }
            if (getTokenPaddyResponse.getGetTokenOutput().get(0).getNewBags() != null) {
                binding.pproTotalNewBagsGivenTv.setText("" + getTokenPaddyResponse.getGetTokenOutput().get(0).getNewBags());
            }

            if (getTokenPaddyResponse.getGetTokenOutput().get(0).getOldBags() != null) {
                binding.pproTotalOldBagsGivenTv.setText("" + getTokenPaddyResponse.getGetTokenOutput().get(0).getOldBags());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPaddyData(GetTokensResponse getTokenPaddyResponse) {
        try {
            if (getTokenPaddyResponse.getGetTokenOutput().get(0).getPaddyType() != null) {
                binding.pproPaddyGradeTv.setText(getTokenPaddyResponse.getGetTokenOutput().get(0).getPaddyType());
            }
            if (getTokenPaddyResponse.getGetTokenOutput().get(0).getPaddyTypeID() != null) {
                double mspAmount = getPaddyMSPAmpount(getTokenPaddyResponse.getGetTokenOutput().get(0).getPaddyTypeID());
                binding.pproMspamountTv.setText(String.valueOf(mspAmount));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double getPaddyMSPAmpount(Integer paddyTypeID) {
        double mspAmount = 0;
        try {
            if (paddyEntities.size() > 0) {
                for (int i = 0; i < paddyEntities.size(); i++) {
                    if (paddyEntities.get(i).getGradeID().equals(paddyTypeID)) {
                        mspAmount = paddyEntities.get(i).getMSPAmount();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mspAmount;
    }

    @Override
    public void getAllPaddyValues(List<PaddyEntity> paddyEntities) {
        try {
            if (paddyEntities.size() > 0) {
                this.paddyEntities = paddyEntities;
                if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                    getGeneratedTokens(-1);
                } else {
                    if (getActivity() != null) {
                        Utils.customAlert(getActivity(),
                                getResources().getString(R.string.PaddyProcurementdetails),
                                getActivity().getResources().getString(R.string.no_internet),
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
                        getResources().getString(R.string.PaddyProcurementdetails),
                        getResources().getString(R.string.no_paddy), true);

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
        binding.pproDistrictTv.setText(distName);
    }

    @Override
    public void getManName(String manName) {
        binding.pproMandalTv.setText(manName);
    }

    @Override
    public void getVilName(String vilName) {
        binding.pproVillageTv.setText(vilName);
    }


    @Override
    public void getAllBranches(List<BranchEntity> branchEntities) {
        try {
            if (branchEntities.size() > 0) {
                paddyTestRepository.getAllPaddyValues(this);
            } else {
                Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                        getResources().getString(R.string.PaddyProcurementdetails),
                        getResources().getString(R.string.no_branches), true);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllDistrictEntities(List<DistrictEntity> districtEntities) {
        try {
            if (districtEntities.size() > 0) {
                dmvRepository.getAllMandals(this);
            } else {
                Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                        getResources().getString(R.string.PaddyProcurementdetails),
                        getResources().getString(R.string.no_districts), true);
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
                        getResources().getString(R.string.PaddyProcurementdetails),
                        getResources().getString(R.string.no_mandals), true);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getAllVillageEntities(List<VillageEntity> villageEntities) {
        try {
            if (villageEntities.size() > 0) {
                bbRepository.getAllBanks(this);
            } else {
                Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                        getResources().getString(R.string.PaddyProcurementdetails),
                        getResources().getString(R.string.no_villages), true);
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
    public void bankCount(int cnt) {

    }

    @Override
    public void branchCount(int cnt) {

    }

    @Override
    public void getBankName(String bankName) {
        binding.pproBankNameTv.setText(bankName);
    }

    @Override
    public void getBranchName(String bankName) {
        binding.pproBrnachNameTv.setText(bankName);
    }

    @Override
    public void socialCount(int cnt) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (customProgressDialog != null) {
                customProgressDialog = null;
            }
            if (paddyProcurementPresenter != null) {
                paddyProcurementPresenter.detachView();
            }

            if (rePrintPresenter != null) {
                rePrintPresenter.detachView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

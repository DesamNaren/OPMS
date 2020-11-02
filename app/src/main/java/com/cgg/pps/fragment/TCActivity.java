package com.cgg.pps.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.analogics.thermalAPI.Bluetooth_Printer_2inch_ThermalAPI;
import com.cgg.pps.R;
import com.cgg.pps.adapter.ROInfoAdapter;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.TruchchitFragmentBinding;
import com.cgg.pps.interfaces.RePrintInterface;
import com.cgg.pps.interfaces.TCInterface;
import com.cgg.pps.interfaces.VehicleInterface;
import com.cgg.pps.model.request.TCRePrintRequest;
import com.cgg.pps.model.request.truckchit.ManualTCRequest;
import com.cgg.pps.model.request.truckchit.MillerMasterRequest;
import com.cgg.pps.model.request.truckchit.OnlineTCRequest;
import com.cgg.pps.model.request.truckchit.TCFinalSubmitRequest;
import com.cgg.pps.model.request.truckchit.TCSubmitRequest;
import com.cgg.pps.model.request.truckchit.TCTransData;
import com.cgg.pps.model.request.truckchit.info.ROInfoRequest;
import com.cgg.pps.model.request.truckchit.transaction.TCTransaction;
import com.cgg.pps.model.request.truckchit.transaction.TransactionRequest;
import com.cgg.pps.model.request.truckchit.vehicledetails.MasterVehicleTypeRequest;
import com.cgg.pps.model.request.truckchit.vehicledetails.VehicleDetailsRequest;
import com.cgg.pps.model.response.reprint.ProcRePrintResponse;
import com.cgg.pps.model.response.reprint.TokenRePrintResponse;
import com.cgg.pps.model.response.reprint.truckchit.TCRePrintResponse;
import com.cgg.pps.model.response.truckchit.info.ROInfoResponse;
import com.cgg.pps.model.response.truckchit.manual.ManualTCResponse;
import com.cgg.pps.model.response.truckchit.mastervehicle.VehicleDetails;
import com.cgg.pps.model.response.truckchit.mastervehicle.VehicleResponse;
import com.cgg.pps.model.response.truckchit.online.OnlineTCResponse;
import com.cgg.pps.model.response.truckchit.ro.MillerData;
import com.cgg.pps.model.response.truckchit.ro.MillerMainResponse;
import com.cgg.pps.model.response.truckchit.submit.TCSubmitResponse;
import com.cgg.pps.model.response.truckchit.transaction.GetTransactionResponse;
import com.cgg.pps.model.response.truckchit.transaction.TransactionsData;
import com.cgg.pps.model.response.truckchit.vehicle_type.TransporterVehicleType;
import com.cgg.pps.model.response.truckchit.vehicle_type.VehicleTypesResponse;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.presenter.RePrintPresenter;
import com.cgg.pps.presenter.TCPresenter;
import com.cgg.pps.room.repository.VehicleRepository;
import com.cgg.pps.util.APrinter;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.ConnectionDetector;
import com.cgg.pps.util.CustomProgressDialog;
import com.cgg.pps.util.Utils;
import com.cgg.pps.view.DashboardActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class TCActivity extends AppCompatActivity implements TCInterface, AdapterView.OnItemSelectedListener
        , View.OnClickListener, VehicleInterface, RePrintInterface {
    private TruchchitFragmentBinding binding;
    private PPCUserDetails ppcUserDetails;
    private TCPresenter tcPresenter;
    private GetTransactionResponse transactionResponseGlobal;
    private ArrayList<TableRow> landDetailsTableRowsArrayList;
    private ArrayList<TCTransaction> pTransactionArrayList;
    private ArrayList<MillerData> millerDataArrayList;
    private MillerMainResponse millerMasterListGlobal;
    private MillerData millerData = null;
    private Double curMilQty = 0.0;
    private static final int REQUEST_CAMERA = 200;
    private String PHOTO_ENCODED_STRING1;
    private VehicleDetails vehicleResponseMain;
    private Bitmap bitmap;

    private boolean manFlag;
    private CustomProgressDialog customProgressDialog;
    private SharedPreferences sharedPreferences;
    private RePrintPresenter rePrintPresenter;
    private APrinter aPrinter;
    private VehicleRepository vehicleRepository;
    private int cnt = -1;
    private Long selTransID;
    private List<TransporterVehicleType> transporterVehicleTypes;
    private static final String IMAGE_DIRECTORY_NAME = "FILEPROVIDER";
    private Uri fileUri; // file url to store image/video
    String FilePath;
    private String timeStamp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.truchchit_fragment);
        try {
            setSupportActionBar(binding.drpToolbar);
            getSupportActionBar().setTitle(getString(R.string.PPCtoMiller));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            int permissionCheck = ContextCompat.checkSelfPermission(
                    this, Manifest.permission.CAMERA);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                customPerAlert();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        tcPresenter = new TCPresenter();
        tcPresenter.attachView(this);
        rePrintPresenter = new RePrintPresenter();
        rePrintPresenter.attachView(this);
        vehicleRepository = new VehicleRepository(TCActivity.this);

        transporterVehicleTypes = new ArrayList<>();
        binding.PPCtoMillerPaadyDetailsTabel.removeAllViews();
        landDetailsTableRowsArrayList = new ArrayList<>();
        pTransactionArrayList = new ArrayList<>();
        millerDataArrayList = new ArrayList<>();

        binding.PPCtoMillerVehicleNoATv.setEnabled(false);
        binding.PPCtoMillerVehicleTypeSp.setEnabled(false);

        binding.PPCtoMillerVehicleNoATv.setOnItemClickListener(
                new AutoCompleteTVListener(this, binding.PPCtoMillerVehicleNoATv));

        binding.PPCtoMillerVehicleNoATv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.PPCtoMillerVehicleTypeSp.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.PPCtoMillerIdSp.setOnItemSelectedListener(this);
        binding.PPCtoMillerTransporterNameSp.setOnItemSelectedListener(this);
        binding.manCheckBtn.setOnClickListener(this);
        binding.manEditBtn.setOnClickListener(this);
        binding.refresh.setOnClickListener(this);
        binding.trnRefresh.setOnClickListener(this);
        binding.btnCapturePicture.setOnClickListener(this);
        binding.PPCtoMillerSubmitBtn.setOnClickListener(this);
        binding.roInfo.setOnClickListener(this);

        getPreferenceData();
        aPrinter = APrinter.getaPrinter();

        customProgressDialog = new CustomProgressDialog(TCActivity.this);

        String transportDate = Utils.getTransportDate();
        binding.PPCtoMillerTransportDateTv.setText(transportDate);


        CallVehicleTypesAPI();

        vehicleRepository.getAllTransports(this);

        if (ConnectionDetector.isConnectedToInternet(TCActivity.this)) {
            getOnlineTCNo();

        } else {
            Utils.customAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.WARNING), false);
        }

        binding.PPCtoMillerVehicleNoATv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.PPCtoMillerVehicleTypeSp.setSelection(0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (sharedPreferences.getBoolean(AppConstants.PRINTER_CON_FLAG, false)) {
            if (AppConstants.BT_ADDRESS == null || AppConstants.BT_ADDRESS.equals("")) {
                Utils.ShowPrintAlert(TCActivity.this, getSupportFragmentManager(), customProgressDialog);
            }
        }

        binding.rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utils.hideKeyboard(view);
                return false;
            }
        });
    }

    private String cacheDate, currentDate;

    public void onResume() {
        super.onResume();
        try {
            boolean isAutomatic = Utils.isTimeAutomatic(this);
            if (!isAutomatic) {
                Utils.customTimeAlert(this,
                        getResources().getString(R.string.app_name),
                        getString(R.string.date_time));
                return;
            }

            currentDate = Utils.getCurrentDate();
            cacheDate = sharedPreferences.getString(AppConstants.CACHE_DATE, "");
            if (cacheDate != null && !TextUtils.isEmpty(cacheDate)) {
                if (!cacheDate.equalsIgnoreCase(currentDate)) {
                    Utils.ShowDeviceSessionAlert(this,
                            getResources().getString(R.string.DRP),
                            getString(R.string.ses_expire_re),
                            getSupportFragmentManager());
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    private boolean connectBT() {
        boolean flag = false;
        try {
            if (!TextUtils.isEmpty(AppConstants.BT_ADDRESS)) {
                flag = aPrinter.openBT(AppConstants.BT_ADDRESS);
            } else {
                Toast.makeText(TCActivity.this, getResources().getString(R.string.print_alert_not), Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    private void getOnlineTCNo() {
        try {
            if (customProgressDialog != null && !customProgressDialog.isShowing())
                customProgressDialog.show();

            OnlineTCRequest onlineTCRequest = new OnlineTCRequest();
            onlineTCRequest.setPpcID(String.valueOf(ppcUserDetails.getPPCID()));
            onlineTCRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
            tcPresenter.GetOnlineTCResponse(onlineTCRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowSubmitTCAlert(TCSubmitRequest tcSubmitRequest, Activity activity) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.tc_submit_confirmation_dialog);
                dialog.setCancelable(false);
                if (!dialog.isShowing())
                    dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                TextView titleTv = dialog.findViewById(R.id.alert_titleTv);
                titleTv.setText(getResources().getString(R.string.tcConfirmation));
                Button yes = dialog.findViewById(R.id.tcProceedBtn);
                Button cancel = dialog.findViewById(R.id.cancelBtn);
                TextView millName = dialog.findViewById(R.id.millName);
                TextView roNum = dialog.findViewById(R.id.roNum);
                TextView Newbags = dialog.findViewById(R.id.Newbags);
                TextView oldBags = dialog.findViewById(R.id.oldBags);
                TextView Totalbags = dialog.findViewById(R.id.Totalbags);
                TextView totalQuantity = dialog.findViewById(R.id.totalQuantity);
                TextView onlineNo = dialog.findViewById(R.id.onlineNo);
                TextView manualNo = dialog.findViewById(R.id.manualNo);
                if (millerData != null) {
                    millName.setText("మిల్లు పేరు" + ": " + millerData.getMillName().trim());
                } else {
                    Log.i("TC", "ShowSubmitTCAlert: " + "Empty miller data");
                }


                roNum.setText("ఆర్ఓ ఐడి" + ": " + binding.PPCROTv.getText().toString().trim());

                if (tcSubmitRequest != null) {
                    Newbags.setText("కొత్త సంచులు" + ": " + tcSubmitRequest.getT_NewBags());
                    oldBags.setText("పాత సంచులు" + ": " + tcSubmitRequest.getT_OldBags());
                    Totalbags.setText("మొత్తం సంచులు" + ": " + tcSubmitRequest.getT_TotalBags());
                    double qtyTC = Double.parseDouble(String.format("%.2f", tcSubmitRequest.getT_TotalQty()));
                    totalQuantity.setText("పరిమాణం (క్వింటాల్లలో)" + ": " + qtyTC);


                    String beforeStr = binding.manualTcNoBfr.getText().toString().trim();
                    String afterStr = binding.manualTcNoAft.getText().toString().trim();
                    String slash = binding.slashTV.getText().toString().trim();
                    String finalStr = beforeStr.concat(slash).concat(afterStr);


                    onlineNo.setText("Truck Chit Number" + ": " + binding.onlineTcNo.getText().toString().trim());
                    manualNo.setText("మాన్యువల్ ట్రక్ షీట్ సంఖ్య" + ": " + finalStr);
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dialog.isShowing())
                                dialog.dismiss();
                            if (ConnectionDetector.isConnectedToInternet(TCActivity.this)) {
                                if (customProgressDialog != null && !customProgressDialog.isShowing())
                                    customProgressDialog.show();

                                TCFinalSubmitRequest tcFinalSubmitRequest = new TCFinalSubmitRequest();
                                tcFinalSubmitRequest.setpPCID(tcSubmitRequest.getPpcid());
                                tcFinalSubmitRequest.setAuthenticationID(tcSubmitRequest.getAuthenticationID());
                                tcFinalSubmitRequest.setTruckchit(binding.onlineTcNo.getText().toString().trim());
                                tcFinalSubmitRequest.setXmlTruckchitData(tcSubmitRequest.getXmlTruckchitData());
                                tcFinalSubmitRequest.setUploadTRImage(tcSubmitRequest.getUploadTRImage());

                                callManualTCAPI(true, tcSubmitRequest);


                            } else {
                                Utils.customAlert(TCActivity.this,
                                        getResources().getString(R.string.PPCtoMiller),
                                        getResources().getString(R.string.no_internet),
                                        getResources().getString(R.string.WARNING), false);
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
                } else {
                    Toast.makeText(activity, getResources().getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.i("TC", "ShowSubmitTCAlert: " + "TC Submit data not found");
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getPreferenceData() {
        try {
            Gson gson = OPMSApplication.get(TCActivity.this).getGson();
            sharedPreferences = OPMSApplication.get(TCActivity.this).getPreferences();
            String string = sharedPreferences.getString(AppConstants.PPC_DATA, "");
            ppcUserDetails = gson.fromJson(string, PPCUserDetails.class);
            if (ppcUserDetails == null) {
                Utils.ShowDeviceSessionAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getString(R.string.ses_expire_re),
                        getSupportFragmentManager());
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callMillerMasterTask() {

        try {
            MillerMasterRequest millerMasterRequest = new MillerMasterRequest();
            millerMasterRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));
            millerMasterRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());

            if (customProgressDialog != null && !customProgressDialog.isShowing())
                customProgressDialog.show();
            tcPresenter.GetMillerMasterResponse(millerMasterRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMillerMasterResponseData(MillerMainResponse millerMasterResponse) {
        try {
            if (customProgressDialog != null && customProgressDialog.isShowing())
                customProgressDialog.dismiss();

            millerMasterListGlobal = millerMasterResponse;
            if (millerMasterResponse != null && millerMasterResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (millerMasterResponse.getMillerData() != null && millerMasterResponse.getMillerData().size() > 0) {
                    ArrayList<String> mills = new ArrayList<>();
                    mills.add(0, "--Select--");
                    for (int i = 0; i < millerMasterResponse.getMillerData().size(); i++) {
                        mills.add(millerMasterResponse.getMillerData().get(i).getMillName() + "~" + millerMasterResponse.getMillerData().get(i).getMillerCode());
                    }
                    if (mills.size() > 0) {
                        Utils.loadSpinnerData(TCActivity.this, mills, binding.PPCtoMillerIdSp);
                    }


                } else {
                    Utils.customAlert(TCActivity.this,
                            getResources().getString(R.string.PPCtoMiller),
                            millerMasterResponse.getResponseMessage(),
                            getResources().getString(R.string.ERROR), false);
                }
            } else if (millerMasterResponse != null &&
                    millerMasterResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        millerMasterResponse.getResponseMessage(),
                        getResources().getString(R.string.ERROR), false);
            } else if (millerMasterResponse != null &&
                    millerMasterResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        millerMasterResponse.getResponseMessage(),
                        getResources().getString(R.string.ERROR), false);
            } else if (millerMasterResponse != null &&
                    millerMasterResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(TCActivity.this, getResources().getString(R.string.PPCtoMiller),
                        millerMasterResponse.getResponseMessage(), getSupportFragmentManager());
            } else if (millerMasterResponse != null &&
                    millerMasterResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(millerMasterResponse.getResponseMessage(), TCActivity.this, getSupportFragmentManager(), true);

            } else {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getResources().getString(R.string.server_not),
                        getResources().getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    getResources().getString(R.string.server_not),
                    getResources().getString(R.string.something), false);
            e.printStackTrace();
        }
    }


    @Override
    public void getOnlineTCResponseData(OnlineTCResponse onlineTCResponse) {
        try {

            binding.refresh.setVisibility(View.VISIBLE);

            if (customProgressDialog != null && customProgressDialog.isShowing())
                customProgressDialog.dismiss();

//            Caused by java.lang.NullPointerException
//            Attempt to invoke virtual method 'java.lang.String java.lang.String.trim()' on a null object reference
//            com.cgg.pps.fragment.TCFragment.getOnlineTCResponseData

            if (onlineTCResponse != null && onlineTCResponse.getStatusCode() == AppConstants.SUCCESS_CODE
                    && onlineTCResponse.getGetOnlineTruckchitNumber() != null) {
                if (onlineTCResponse.getGetOnlineTruckchitNumber().getOnlineTruckChitNumber() != null &&
                        !TextUtils.isEmpty(onlineTCResponse.getGetOnlineTruckchitNumber().getOnlineTruckChitNumber().trim())) {
                    binding.refresh.setVisibility(View.GONE);
                    binding.onlineTcNo.setText(onlineTCResponse.getGetOnlineTruckchitNumber().getOnlineTruckChitNumber().trim());

                    binding.tcLayout.setVisibility(View.VISIBLE);
                    binding.manTcLayout.setVisibility(View.VISIBLE);

                    callMillerMasterTask();
                    callTransactions();

                } else {
                    Utils.customAlert(TCActivity.this,
                            getResources().getString(R.string.PPCtoMiller),
                            onlineTCResponse.getResponseMessage(),
                            getResources().getString(R.string.ERROR), false);
                }
            } else if (onlineTCResponse != null &&
                    onlineTCResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        onlineTCResponse.getResponseMessage(),
                        getResources().getString(R.string.ERROR), false);

            } else if (onlineTCResponse != null &&
                    onlineTCResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        onlineTCResponse.getResponseMessage(),
                        getResources().getString(R.string.ERROR), false);

            } else if (onlineTCResponse != null &&
                    onlineTCResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(TCActivity.this, getResources().getString(R.string.PPCtoMiller),
                        onlineTCResponse.getResponseMessage(), getSupportFragmentManager());
            } else if (onlineTCResponse != null &&
                    onlineTCResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(onlineTCResponse.getResponseMessage(), TCActivity.this, getSupportFragmentManager(), true);

            } else {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getResources().getString(R.string.server_not),
                        getResources().getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    getResources().getString(R.string.something),
                    getResources().getString(R.string.ERROR), false);
            e.printStackTrace();
        }
    }

    @Override
    public void getROInfoResponseData(ROInfoResponse roInfoResponse) {
        try {

            if (customProgressDialog != null && customProgressDialog.isShowing())
                customProgressDialog.dismiss();

            if (roInfoResponse != null && roInfoResponse.getStatusCode() == AppConstants.SUCCESS_CODE
                    && roInfoResponse.getRODetails() != null) {

                showInfoAlert(roInfoResponse);

            } else if (roInfoResponse != null &&
                    roInfoResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        roInfoResponse.getResponseMessage(),
                        getResources().getString(R.string.ERROR), false);

            } else if (roInfoResponse != null &&
                    roInfoResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        roInfoResponse.getResponseMessage(),
                        getResources().getString(R.string.ERROR), false);

            } else if (roInfoResponse != null &&
                    roInfoResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(TCActivity.this, getResources().getString(R.string.PPCtoMiller),
                        roInfoResponse.getResponseMessage(), getSupportFragmentManager());
            } else if (roInfoResponse != null &&
                    roInfoResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(roInfoResponse.getResponseMessage(), TCActivity.this, getSupportFragmentManager(), true);

            } else {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getResources().getString(R.string.server_not),
                        getResources().getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    getResources().getString(R.string.something),
                    getResources().getString(R.string.ERROR), false);
            e.printStackTrace();
        }
    }

    @Override
    public void getManualTCResponseData(ManualTCResponse manualTCResponse, boolean finalSubmitFlag,
                                        TCSubmitRequest tcSubmitRequest) {
        try {
            if (customProgressDialog != null && customProgressDialog.isShowing())
                customProgressDialog.dismiss();

            enableManVerify();

            if (manualTCResponse != null && manualTCResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {

                disableManVerify();

                if (!finalSubmitFlag) {
                    Utils.customAlert(TCActivity.this,
                            getResources().getString(R.string.PPCtoMiller),
                            manualTCResponse.getResponseMessage(),
                            getResources().getString(R.string.SUCCESS), false);
                } else {
                    if (ConnectionDetector.isConnectedToInternet(TCActivity.this)) {
                        if (customProgressDialog != null && !customProgressDialog.isShowing())
                            customProgressDialog.show();

                        TCFinalSubmitRequest tcFinalSubmitRequest = new TCFinalSubmitRequest();
                        tcFinalSubmitRequest.setpPCID(tcSubmitRequest.getPpcid());
                        tcFinalSubmitRequest.setAuthenticationID(tcSubmitRequest.getAuthenticationID());
                        tcFinalSubmitRequest.setTruckchit(binding.onlineTcNo.getText().toString().trim());
                        tcFinalSubmitRequest.setXmlTruckchitData(tcSubmitRequest.getXmlTruckchitData());
                        tcFinalSubmitRequest.setUploadTRImage(tcSubmitRequest.getUploadTRImage());

                        tcPresenter.SubmitCall(tcFinalSubmitRequest);
                    } else {
                        Utils.customAlert(TCActivity.this,
                                getResources().getString(R.string.PPCtoMiller),
                                getResources().getString(R.string.no_internet),
                                getResources().getString(R.string.WARNING), false);
                    }
                }
            } else if (manualTCResponse != null &&
                    manualTCResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        manualTCResponse.getResponseMessage(),
                        getResources().getString(R.string.ERROR), false);
            } else if (manualTCResponse != null &&
                    manualTCResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        manualTCResponse.getResponseMessage(),
                        getResources().getString(R.string.ERROR), false);
            } else if (manualTCResponse != null &&
                    manualTCResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(TCActivity.this, getResources().getString(R.string.PPCtoMiller),
                        manualTCResponse.getResponseMessage(), getSupportFragmentManager());
            } else if (manualTCResponse != null &&
                    manualTCResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(manualTCResponse.getResponseMessage(), TCActivity.this, getSupportFragmentManager(), true);

            } else {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getResources().getString(R.string.server_not),
                        getResources().getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    getResources().getString(R.string.something),
                    getResources().getString(R.string.ERROR), false);
            e.printStackTrace();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void disableManVerify() {
        try {
            binding.manEditBtn.setVisibility(View.VISIBLE);
            binding.manualTcNoBfr.clearFocus();
            binding.manualTcNoBfr.setEnabled(false);
            binding.manualTcNoAft.clearFocus();
            binding.manualTcNoAft.setEnabled(false);
            binding.manCheckBtn.setText(getString(R.string.verified));
            binding.manCheckBtn.setEnabled(false);
            binding.manCheckBtn.setBackgroundColor(getResources().getColor(R.color.dark_gray));
            manFlag = true;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void enableManVerify() {
        try {
            binding.manualTcNoBfr.setEnabled(true);
            binding.manualTcNoAft.setEnabled(true);
            binding.manCheckBtn.setText(getString(R.string.verify));
            binding.manCheckBtn.setEnabled(true);
            binding.manCheckBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            manFlag = false;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getTransactionResponseData(GetTransactionResponse getTransactionResponse) {
        try {

            binding.trnRefresh.setVisibility(View.VISIBLE);

            if (customProgressDialog != null && customProgressDialog.isShowing())
                customProgressDialog.dismiss();

            if (getTransactionResponse != null && getTransactionResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (getTransactionResponse.getTransactionsData() != null && getTransactionResponse.getTransactionsData().size() > 0) {

                    binding.trnRefresh.setVisibility(View.GONE);

                    for (int i = 0; i < getTransactionResponse.getTransactionsData().size(); i++) {
                        if (getTransactionResponse.getTransactionsData().get(i).getTotalQuantity() > 0) {

                            if (getTransactionResponse.getTransactionsData().get(i).getTotalBags() > getTransactionResponse.getTransactionsData().get(i).getTRTotalBags()) {
                                getTransactionResponse.getTransactionsData().get(i).setRemNewBags(getTransactionResponse.getTransactionsData().get(i).getNewBags() - getTransactionResponse.getTransactionsData().get(i).getTRNewBags());
                                getTransactionResponse.getTransactionsData().get(i).setRemOldBags(getTransactionResponse.getTransactionsData().get(i).getOldBags() - getTransactionResponse.getTransactionsData().get(i).getTROldBags());
                                getTransactionResponse.getTransactionsData().get(i).setRemTOtBags(getTransactionResponse.getTransactionsData().get(i).getTotalBags() - getTransactionResponse.getTransactionsData().get(i).getTRTotalBags());
                                getTransactionResponse.getTransactionsData().set(i, getTransactionResponse.getTransactionsData().get(i));
                            }
                        }

                        transactionResponseGlobal = getTransactionResponse;

                        if (getTransactionResponse.getTransactionsData().size() > 0) {
                            setTransactionsData(getTransactionResponse.getTransactionsData().get(i));
                        }

                    }

                } else {
                    Utils.customAlert(TCActivity.this,
                            getResources().getString(R.string.PPCtoMiller),
                            getTransactionResponse.getResponseMessage(),
                            getResources().getString(R.string.ERROR), false);
                }
            } else if (getTransactionResponse != null &&
                    getTransactionResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getTransactionResponse.getResponseMessage(),
                        getResources().getString(R.string.ERROR), false);
            } else if (getTransactionResponse != null &&
                    getTransactionResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getTransactionResponse.getResponseMessage(),
                        getResources().getString(R.string.ERROR), false);
            } else if (getTransactionResponse != null &&
                    getTransactionResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(TCActivity.this, getResources().getString(R.string.PPCtoMiller),
                        getTransactionResponse.getResponseMessage(), getSupportFragmentManager());
            } else if (getTransactionResponse != null &&
                    getTransactionResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(getTransactionResponse.getResponseMessage(), TCActivity.this, getSupportFragmentManager(), true);

            } else {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getResources().getString(R.string.server_not),
                        getResources().getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    getResources().getString(R.string.something),
                    getResources().getString(R.string.ERROR), false);
            e.printStackTrace();
        }
    }

    @Override
    public void getVehicleDataResponse(VehicleResponse vehicleDataResponse, boolean flag) {
        try {
            if (flag) {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
            }

            if (vehicleDataResponse != null && vehicleDataResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (vehicleDataResponse.getVehicleDetails() != null && vehicleDataResponse.getVehicleDetails().size() > 0) {
                    vehicleRepository.insertAllVehicles(this, vehicleDataResponse.getVehicleDetails());
                } else {
                    Utils.customAlert(TCActivity.this,
                            getResources().getString(R.string.PPCtoMiller),
                            vehicleDataResponse.getResponseMessage(),
                            getResources().getString(R.string.ERROR), false);
                }
            } else if (vehicleDataResponse != null &&
                    vehicleDataResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        vehicleDataResponse.getResponseMessage(),
                        getResources().getString(R.string.ERROR), false);

            } else if (vehicleDataResponse != null &&
                    vehicleDataResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        vehicleDataResponse.getResponseMessage(),
                        getResources().getString(R.string.ERROR), false);
            } else if (vehicleDataResponse != null &&
                    vehicleDataResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(vehicleDataResponse.getResponseMessage(), TCActivity.this, getSupportFragmentManager(), true);

            } else if (vehicleDataResponse != null &&
                    vehicleDataResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(TCActivity.this, getResources().getString(R.string.PPCtoMiller),
                        vehicleDataResponse.getResponseMessage(), getSupportFragmentManager());
            } else {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getResources().getString(R.string.server_not),
                        getResources().getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    getResources().getString(R.string.something),
                    getResources().getString(R.string.ERROR), false);
            e.printStackTrace();
        }
    }

    @Override
    public void getMasterVehicleTypeResponse(VehicleTypesResponse vehicleTypesResponse, boolean flag) {
        try {
            if (flag) {
                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();
            }

            if (vehicleTypesResponse != null && vehicleTypesResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (vehicleTypesResponse.getTransporterVehicleType() != null &&
                        vehicleTypesResponse.getTransporterVehicleType().size() > 0) {

                    TransporterVehicleType transporterVehicleType = new TransporterVehicleType();
                    transporterVehicleType.setVehicleTypeID(-1);
                    transporterVehicleType.setVehicleTypeName("--Select--");
                    transporterVehicleTypes.add(0, transporterVehicleType);
                    transporterVehicleTypes.addAll(vehicleTypesResponse.getTransporterVehicleType());


                    ArrayList<String> vehTypes = new ArrayList<>();
                    for (int z = 0; z < transporterVehicleTypes.size(); z++) {
                        vehTypes.add(transporterVehicleTypes.get(z).getVehicleTypeName());
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(TCActivity.this,
                            android.R.layout.simple_spinner_item, vehTypes);

                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.PPCtoMillerVehicleTypeSp.setAdapter(spinnerArrayAdapter);
                    binding.PPCtoMillerVehicleTypeSp.setSelection(0);

//                    vehicleRepository.insertVehicleTypes(this, vehicleTypesResponse.getTransporterVehicleType());
                } else {
                    Utils.customAlert(TCActivity.this,
                            getResources().getString(R.string.PPCtoMiller),
                            vehicleTypesResponse.getResponseMessage(),
                            getResources().getString(R.string.ERROR), false);
                }
            } else if (vehicleTypesResponse != null &&
                    vehicleTypesResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        vehicleTypesResponse.getResponseMessage(),
                        getResources().getString(R.string.ERROR), false);

            } else if (vehicleTypesResponse != null &&
                    vehicleTypesResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        vehicleTypesResponse.getResponseMessage(),
                        getResources().getString(R.string.ERROR), false);
            } else if (vehicleTypesResponse != null &&
                    vehicleTypesResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(vehicleTypesResponse.getResponseMessage(), TCActivity.this, getSupportFragmentManager(), true);

            } else if (vehicleTypesResponse != null &&
                    vehicleTypesResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(TCActivity.this, getResources().getString(R.string.PPCtoMiller),
                        vehicleTypesResponse.getResponseMessage(), getSupportFragmentManager());
            } else {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getResources().getString(R.string.server_not),
                        getResources().getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    getResources().getString(R.string.something),
                    getResources().getString(R.string.ERROR), false);
            e.printStackTrace();
        }
    }

    @Override
    public void getTruckChitSubmitResponse(TCSubmitResponse tcSubmitResponse) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();

        try {
            if (tcSubmitResponse != null && tcSubmitResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
//                Fragment fragment = new DashboardFragment();
                PrintAlert(TCActivity.this, tcSubmitResponse,
                        getResources().getString(R.string.PPCtoMiller),
                        tcSubmitResponse.getResponseMessage());
            } else if (tcSubmitResponse != null &&
                    tcSubmitResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        tcSubmitResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (tcSubmitResponse != null &&
                    tcSubmitResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(TCActivity.this, getResources().getString(R.string.PPCtoMiller),
                        tcSubmitResponse.getResponseMessage(), getSupportFragmentManager());
            } else if (tcSubmitResponse != null &&
                    tcSubmitResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(tcSubmitResponse.getResponseMessage(), TCActivity.this, getSupportFragmentManager(), true);

            } else {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }
    }

    private void showInfoAlert(ROInfoResponse roInfoResponse) {
        try {
            final Dialog dialog = new Dialog(TCActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.ro_info_alert);
                dialog.setCancelable(false);

                TextView dialogTitle = dialog.findViewById(R.id.alert_titleTv);
                dialogTitle.setText(getString(R.string.ro_info));
                TextView millName = dialog.findViewById(R.id.millNameTV);
                TextView millCode = dialog.findViewById(R.id.millCodeTV);
                TextView ppcName = dialog.findViewById(R.id.ppcNameTV);
                TextView ppcCode = dialog.findViewById(R.id.ppcCodeTv);
                TextView roNum = dialog.findViewById(R.id.roNumTV);
                TextView roQty = dialog.findViewById(R.id.alloQtyTV);
                TextView roDate = dialog.findViewById(R.id.roAloDateTv);

                millName.setText(roInfoResponse.getRODetails().getMillName());
                millCode.setText(roInfoResponse.getRODetails().getMillerCode());
                ppcName.setText(roInfoResponse.getRODetails().getPpcName());
                ppcCode.setText(ppcUserDetails.getPPCCode());
                roNum.setText(roInfoResponse.getRODetails().getRoNumber());
                roQty.setText(String.valueOf(roInfoResponse.getRODetails().getAllotedQuantity()));
                roDate.setText(roInfoResponse.getRODetails().getAllotedDate());

                if (roInfoResponse.getDetailedRoInfoOutput() != null &&
                        roInfoResponse.getDetailedRoInfoOutput().size() > 0) {

                    RecyclerView recyclerView = dialog.findViewById(R.id.rvInfo);
                    ROInfoAdapter roInfoAdapter = new ROInfoAdapter(TCActivity.this, roInfoResponse);
                    recyclerView.setLayoutManager(new LinearLayoutManager(TCActivity.this));
                    recyclerView.setAdapter(roInfoAdapter);
                }

                ImageView close = dialog.findViewById(R.id.closeIV);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
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


    private void PrintAlert(Activity activity,
                            TCSubmitResponse tcSubmitResponse,
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
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        callHome();

//                        AppConstants.FARG_TAG = DashboardFragment.class.getSimpleName();
//                        callFragment(fragment, fragmentManager, AppConstants.FARG_TAG);
                    }
                });


                print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        if (!(sharedPreferences.getBoolean(AppConstants.PRINTER_CON_FLAG, false))) {
//                            AppConstants.FARG_TAG = DashboardFragment.class.getSimpleName();
//                            callFragment(fragment, fragmentManager, AppConstants.FARG_TAG);
                            callHome();
                            Toast.makeText(activity, getString(R.string.plz_take_reprint), Toast.LENGTH_SHORT).show();
                        } else if (AppConstants.BT_ADDRESS == null || AppConstants.BT_ADDRESS.equals("")) {
//                            AppConstants.FARG_TAG = DashboardFragment.class.getSimpleName();
//                            callFragment(fragment, fragmentManager, AppConstants.FARG_TAG);
                            callHome();
                            Toast.makeText(activity, getString(R.string.plz_take_reprint), Toast.LENGTH_SHORT).show();
                        } else {
                            callPrintMethod(tcSubmitResponse);
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

    private void callHome(){
        startActivity(new Intent(this, DashboardActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    private void callPrintMethod(TCSubmitResponse tcSubmitResponse) {

        try {
            if (ConnectionDetector.isConnectedToInternet(TCActivity.this)) {
                customProgressDialog.show();
                TCRePrintRequest tcRePrintRequest = new TCRePrintRequest();
                tcRePrintRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                tcRePrintRequest.setOnlineTruckChitNo(tcSubmitResponse.getSaveTruckChitOutput().getTruckSlipNo());
                tcRePrintRequest.setManualTruckChitNo(tcSubmitResponse.getSaveTruckChitOutput().getManualPPCTruckchitID());
                rePrintPresenter.GetTCTxnData(tcRePrintRequest);
            } else {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getResources().getString(R.string.no_internet),
                        getString(R.string.WARNING), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    private static void callFragment(Fragment fragment, FragmentManager fragmentManager, String
            name) {
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


    private void callTransactions() {
        try {
            TransactionRequest transactionRequest = new TransactionRequest();
            transactionRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));
            transactionRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
            transactionRequest.setTokenID("00"); // static
            transactionRequest.setTransactionID("00"); // static
            transactionRequest.setTransactionRowID("00"); // static
            transactionRequest.setTransactionStatus("T"); // static


            if (customProgressDialog != null && !customProgressDialog.isShowing())
                customProgressDialog.show();

            tcPresenter.GetTransactionResponse(transactionRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setTransactionsData(TransactionsData transactionsData) {
        try {
            if (landDetailsTableRowsArrayList.size() == 0) {
                binding.PPCtoMillerPaadyDetailsTabel.removeAllViews();
            }

            LayoutInflater mInflater = LayoutInflater.from(TCActivity.this);
            final TableRow paramView = (TableRow) mInflater.inflate(R.layout.dispatch_paddydetails_tr, null);

            CheckBox PPCtoMiller_paddytype_Cb = paramView.findViewById(R.id.PPCtoMiller_paddytype_Cb);
            TextView PPCtoMiller_token = paramView.findViewById(R.id.PPCtoMiller_token);
            TextView PPCtoMiller_transid = paramView.findViewById(R.id.PPCtoMiller_transid);
            TextView PPCtoMiller_newbagsAvail_tv = paramView.findViewById(R.id.PPCtoMiller_newbagsAvail_tv);
            TextView PPCtoMiller_oldBagsAvail_tv = paramView.findViewById(R.id.PPCtoMiller_oldBagsAvail_tv);
            TextView PPCtoMiller_qtySent_Et = paramView.findViewById(R.id.PPCtoMiller_qtySent_Et);

            EditText PPCtoMiller_newBagsSent_Et = paramView.findViewById(R.id.PPCtoMiller_newBagsSent_Et);
            EditText PPCtoMiller_oldBagsSent_Et = paramView.findViewById(R.id.PPCtoMiller_oldBagsSent_Et);


            PPCtoMiller_paddytype_Cb.setText(transactionsData.getPaddyType());
            PPCtoMiller_token.setText(transactionsData.getTokenNo());
            PPCtoMiller_transid.setText(transactionsData.getFarmerTransactionID());
            PPCtoMiller_newbagsAvail_tv.setText(String.valueOf(transactionsData.getRemNewBags()));
            PPCtoMiller_oldBagsAvail_tv.setText(String.valueOf(transactionsData.getRemOldBags()));


            ((CheckBox) paramView.findViewById(R.id.PPCtoMiller_paddytype_Cb)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (binding.PPCtoMillerIdSp.getSelectedItem() != null &&
                                !binding.PPCtoMillerIdSp.getSelectedItem().equals("--Select--")
                                && millerData != null) {

                            TCTransaction transaction = null;

                            String et_newbags = PPCtoMiller_newBagsSent_Et.getText().toString().trim();
                            String et_oldbags = PPCtoMiller_oldBagsSent_Et.getText().toString().trim();

                            String avilNewBags = PPCtoMiller_newbagsAvail_tv.getText().toString().trim();
                            String avilOldBags = PPCtoMiller_oldBagsAvail_tv.getText().toString().trim();

                            if (TextUtils.isEmpty(et_newbags)) {
                                PPCtoMiller_newBagsSent_Et.setError(getString(R.string.enter_n_bags));
                                PPCtoMiller_newBagsSent_Et.requestFocus();
                                buttonView.setChecked(false);
                                return;
                            }

                            if (Integer.parseInt(avilNewBags) < Integer.parseInt(et_newbags)) {
                                buttonView.setChecked(false);
                                PPCtoMiller_newBagsSent_Et.setError(getString(R.string.enter_valid_n_bags));
                                PPCtoMiller_newBagsSent_Et.requestFocus();
                                return;
                            }


                            if (TextUtils.isEmpty(et_oldbags)) {
                                buttonView.setChecked(false);
                                PPCtoMiller_oldBagsSent_Et.setError(getString(R.string.enter_o_bags));
                                PPCtoMiller_oldBagsSent_Et.requestFocus();
                                return;
                            }


                            if (Integer.parseInt(avilOldBags) < Integer.parseInt(et_oldbags)) {
                                buttonView.setChecked(false);
                                PPCtoMiller_oldBagsSent_Et.setError(getString(R.string.enter_valid_o_bags));
                                PPCtoMiller_oldBagsSent_Et.requestFocus();
                                return;
                            }

                            int totSentBags = Integer.parseInt(et_newbags) + Integer.parseInt(et_oldbags);

                            if (totSentBags == 0) {
                                buttonView.setChecked(false);
                                Toast.makeText(TCActivity.this, getString(R.string.enter_valid_bags), Toast.LENGTH_SHORT).show();
                                PPCtoMiller_qtySent_Et.setText("");
                                PPCtoMiller_qtySent_Et.setVisibility(View.INVISIBLE);
                                return;
                            }

                            if (buttonView.isChecked()) {
                                final String value = Double.toString(totSentBags * 0.4);
                                double intValue = Double.parseDouble(value);
                                intValue = (double) Math.round(intValue * 100);
                                intValue = intValue / 100;


                                PPCtoMiller_qtySent_Et.setVisibility(View.VISIBLE);
                                PPCtoMiller_qtySent_Et.setText(String.valueOf(intValue));

                                if (intValue <= curMilQty) {
                                    transaction = new TCTransaction();
                                    transaction.setTokenNumber(transactionsData.getTokenNo());
                                    transaction.setTokenID(String.valueOf(transactionsData.getTokenID()));
                                    transaction.setTransactionID(transactionsData.getFarmerTransactionID());
                                    transaction.setTransactionRowID(transactionsData.getFarmerTransactionRowID());
                                    transaction.setPaddyType(transactionsData.getPaddyTypeID());
                                    transaction.setNewBags(Integer.valueOf(et_newbags));
                                    transaction.setOldBags(Integer.valueOf(et_oldbags));
                                    transaction.setTotalBags(totSentBags);
                                    transaction.setQuantity(intValue);

                                    transaction.setROID(String.valueOf(millerData.getROID()));
                                    pTransactionArrayList.add(transaction);
                                    curMilQty = curMilQty - transaction.getQuantity();
                                    curMilQty = Double.valueOf(String.format("%.2f", curMilQty));


                                    PPCtoMiller_newBagsSent_Et.setEnabled(false);
                                    PPCtoMiller_oldBagsSent_Et.setEnabled(false);
                                } else {
                                    buttonView.setChecked(false);
                                    PPCtoMiller_newBagsSent_Et.setText("");
                                    PPCtoMiller_oldBagsSent_Et.setText("");
                                    PPCtoMiller_qtySent_Et.setText("");
                                    PPCtoMiller_qtySent_Et.setVisibility(View.INVISIBLE);

                                    PPCtoMiller_newBagsSent_Et.setEnabled(true);
                                    PPCtoMiller_oldBagsSent_Et.setEnabled(true);

                                    Utils.customAlert(TCActivity.this,
                                            getResources().getString(R.string.PPCtoMiller),
                                            getString(R.string.ro_exceed),
                                            getString(R.string.INFORMATION), false);
                                }
                            } else {
                                try {
                                    String trnID = PPCtoMiller_transid.getText().toString().trim();
                                    ArrayList<TCTransaction> tempViewArrayList = new ArrayList<TCTransaction>(pTransactionArrayList);

                                    for (int x = 0; x < tempViewArrayList.size(); x++) {
                                        if (tempViewArrayList.get(x).getTransactionID().equalsIgnoreCase(trnID)) {
                                            curMilQty = curMilQty + tempViewArrayList.get(x).getQuantity();
                                            curMilQty = Double.valueOf(String.format("%.2f", curMilQty));
                                            pTransactionArrayList.remove(tempViewArrayList.get(x));
                                        }
                                    }

                                    buttonView.setChecked(false);
                                    PPCtoMiller_newBagsSent_Et.setText("");
                                    PPCtoMiller_oldBagsSent_Et.setText("");
                                    PPCtoMiller_qtySent_Et.setText("");
                                    PPCtoMiller_qtySent_Et.setVisibility(View.INVISIBLE);

                                    PPCtoMiller_newBagsSent_Et.setEnabled(true);
                                    PPCtoMiller_oldBagsSent_Et.setEnabled(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            buttonView.setChecked(false);
                            PPCtoMiller_newBagsSent_Et.setText("");
                            PPCtoMiller_oldBagsSent_Et.setText("");
                            PPCtoMiller_qtySent_Et.setText("");
                            PPCtoMiller_qtySent_Et.setVisibility(View.INVISIBLE);

                            PPCtoMiller_newBagsSent_Et.setEnabled(true);
                            PPCtoMiller_oldBagsSent_Et.setEnabled(true);

                            Utils.customAlert(TCActivity.this,
                                    getResources().getString(R.string.PPCtoMiller),
                                    getString(R.string.select_mill),
                                    getString(R.string.INFORMATION), false);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.PPCtoMiller_Id_Sp:
                try {

                    curMilQty = 0.0;
                    millerDataArrayList = new ArrayList<>();
                    clearMillForm(binding.millLl);
                    clearTransactionsData();

                    String millNameCode = binding.PPCtoMillerIdSp.getSelectedItem().toString().trim();


                    if (millerMasterListGlobal != null && millerMasterListGlobal.getMillerData().size() > 0 &&
                            !TextUtils.isEmpty(millNameCode) && !millNameCode.equalsIgnoreCase("--Select--")) {
                        String millCode = "";
                        String[] str = millNameCode.split("~");
                        if (str.length > 0) {
                            millCode = str[1];
                        }

                        for (int x = 0; x < millerMasterListGlobal.getMillerData().size(); x++) {

                            if (!TextUtils.isEmpty(millCode) &&
                                    millerMasterListGlobal.getMillerData().get(x)
                                            .getMillerCode().equalsIgnoreCase(millCode)) {
                                millerData = millerMasterListGlobal.getMillerData().get(x);
                                break;
                            }
                        }

                        if (millerData != null) {
                            setSelectedMillData(millerData);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.PPCtoMiller_TransporterName_Sp:
                try {
                    cnt++;
                    if (cnt > 0) {
                        selTransID = null;
                        binding.PPCtoMillerVehicleNoATv.setEnabled(false);
                        binding.PPCtoMillerVehicleTypeSp.setEnabled(false);
                        binding.PPCtoMillerVehicleTypeSp.setSelection(0);
                        binding.PPCtoMillerVehicleNoATv.setText("");

                        String traName = binding.PPCtoMillerTransporterNameSp.getSelectedItem().toString().trim();
                        if (!TextUtils.isEmpty(traName) && !traName.equalsIgnoreCase("--Select--")) {

                            vehicleRepository.getTransporterID(this, traName);
                        } else {
                            Utils.loadAutoCompleteTextData(TCActivity.this,
                                    null,
                                    binding.PPCtoMillerVehicleNoATv);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void getAllVehicles(List<VehicleDetails> vehicleDetails) {
        if (vehicleDetails != null && vehicleDetails.size() > 0) {
            ArrayList<String> vehNums = new ArrayList<>();

            for (VehicleDetails vehicleDetailsStr : vehicleDetails) {
                vehNums.add(vehicleDetailsStr.getVehicalNo());
            }

            if (vehNums.size() > 0) {
                Utils.loadAutoCompleteTextData(TCActivity.this,
                        vehNums,
                        binding.PPCtoMillerVehicleNoATv);

            } else {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getResources().getString(R.string.no_veh_data),
                        getResources().getString(R.string.WARNING), false);
            }

        } else {
            Utils.customAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    getResources().getString(R.string.no_veh_data),
                    getResources().getString(R.string.WARNING), false);
        }
    }

    @Override
    public void getAllTransports(List<String> transports) {
        try {
            if (transports != null && transports.size() > 0) {
                transports.add(0, "--Select--");
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(TCActivity.this, android.R.layout.simple_spinner_item, transports);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.PPCtoMillerTransporterNameSp.setAdapter(spinnerArrayAdapter);
                binding.PPCtoMillerTransporterNameSp.setSelection(0);
            } else {
                customRefreshAlert(getResources().getString(R.string.PPCtoMiller),
                        getResources().getString(R.string.no_tra_data_dow));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getVehiclesTypes(List<TransporterVehicleType> transporterVehicleTypes) {
        try {
            TCActivity.this.transporterVehicleTypes = transporterVehicleTypes;
            if (transporterVehicleTypes != null && transporterVehicleTypes.size() > 0) {
                TransporterVehicleType transporterVehicleType = new TransporterVehicleType();
                transporterVehicleType.setVehicleTypeID(-1);
                transporterVehicleType.setVehicleTypeName("--Select--");
                transporterVehicleTypes.add(0, transporterVehicleType);
                ArrayList<String> vehTypes = new ArrayList<>();
                for (int z = 0; z < transporterVehicleTypes.size(); z++) {
                    vehTypes.add(transporterVehicleType.getVehicleTypeName());
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(TCActivity.this,
                        android.R.layout.simple_spinner_item, vehTypes);

                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.PPCtoMillerTransporterNameSp.setAdapter(spinnerArrayAdapter);
                binding.PPCtoMillerTransporterNameSp.setSelection(0);
            } else {
                CallVehicleTypesAPI();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTransportId(Long trnId) {
        selTransID = trnId;
        if (selTransID != -1) {
            binding.PPCtoMillerVehicleNoATv.setEnabled(true);
            binding.PPCtoMillerVehicleNoATv.setEnabled(true);
            vehicleRepository.getAllVehicles(this, selTransID);

        } else {
            Utils.customAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    getResources().getString(R.string.something),
                    getResources().getString(R.string.WARNING), false);
        }
    }

    @Override
    public void getVehicleDetails(VehicleDetails vehicleDetails) {
        if (vehicleDetails != null) {
            vehicleResponseMain = vehicleDetails;
//            binding.PPCtoMillerVehicleTypeSp.setText(vehicleResponseMain.getVehicleTypeName());

            if (transporterVehicleTypes != null && transporterVehicleTypes.size() > 0 &&
                    !TextUtils.isEmpty(vehicleResponseMain.getVehicleTypeName())) {
                for (int z = 0; z < transporterVehicleTypes.size(); z++) {
                    if (transporterVehicleTypes.get(z).getVehicleTypeName().
                            equalsIgnoreCase(vehicleDetails.getVehicleTypeName())) {
                        binding.PPCtoMillerVehicleTypeSp.setSelection(z);
                        binding.PPCtoMillerVehicleTypeSp.setEnabled(false);
                        break;
                    }
                }
            }
        } else {
            Utils.customAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    getResources().getString(R.string.no_veh_data),
                    getResources().getString(R.string.WARNING), false);
        }
    }

    private void customRefreshAlert(String title, String msg) {
        try {
            final Dialog dialog = new Dialog(TCActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_refresh_alert);
                dialog.setCancelable(false);
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(msg);
                Button refresh = dialog.findViewById(R.id.btDialogRefresh);
                Button cancel = dialog.findViewById(R.id.btDialogCancel);
                refresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        CallVehicleAPI();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CallVehicleAPI() {
        try {
            if (ConnectionDetector.isConnectedToInternet(TCActivity.this)) {
                if (customProgressDialog != null && !customProgressDialog.isShowing())
                    customProgressDialog.show();
                VehicleDetailsRequest vehicleDetailsRequest = new VehicleDetailsRequest();
                vehicleDetailsRequest.setPPCID(ppcUserDetails.getPPCID());
                vehicleDetailsRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                tcPresenter.GetVehicleResponse(vehicleDetailsRequest);
            } else {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getResources().getString(R.string.no_internet),
                        getResources().getString(R.string.WARNING), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void CallVehicleTypesAPI() {
        try {
            if (ConnectionDetector.isConnectedToInternet(TCActivity.this)) {
                if (customProgressDialog != null && !customProgressDialog.isShowing())
                    customProgressDialog.show();
                MasterVehicleTypeRequest vehicleTypeRequest = new MasterVehicleTypeRequest();
                vehicleTypeRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                tcPresenter.GetMasterVehicleTypeResponse(vehicleTypeRequest);
            } else {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getResources().getString(R.string.no_internet),
                        getResources().getString(R.string.WARNING), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getVehCnt(int cnt) {
        if (cnt > 0) {
            vehicleRepository.getAllTransports(this);
        } else {
            Utils.customAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    getResources().getString(R.string.no_tra_data),
                    getResources().getString(R.string.INFORMATION), false);
        }
    }

    @Override
    public void getVehTypeCnt(int cnt) {
        if (cnt > 0) {
//            vehicleRepository.getVehicleMaster(this);
        } else {
            Utils.customAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    getResources().getString(R.string.no_master_veh_type_data),
                    getResources().getString(R.string.INFORMATION), false);
        }
    }

    @Override
    public void getAllMasterTransports(List<VehicleDetails> vehicleDetails) {

    }

    @Override
    public void GetRePrintTokenData(TokenRePrintResponse tokenRePrintResponse) {

    }

    @Override
    public void GetProcRePrintData(ProcRePrintResponse procRePrintResponse) {

    }

    @Override
    public void GetTCRePrintData(TCRePrintResponse tcRePrintResponse) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
        try {
            if (tcRePrintResponse != null && tcRePrintResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (tcRePrintResponse.getGettruck() != null) {
                    if (sharedPreferences.getBoolean(AppConstants.PRINTER_CON_FLAG, false)) {
                        if (AppConstants.BT_ADDRESS == null || AppConstants.BT_ADDRESS.equals("")) {
                            Utils.ShowPrintAlert(TCActivity.this, getSupportFragmentManager(), customProgressDialog);
                            return;
                        }
                        new printAsyncTask(tcRePrintResponse).execute();

                    } else {
                        try {

                            callHome();

//                            Fragment fragment = new DashboardFragment();
//                            AppConstants.FARG_TAG = PaddyProcurementFragment.class.getSimpleName();
//                            callFragment(fragment, getSupportFragmentManager(), AppConstants.FARG_TAG);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(TCActivity.this, getResources().getString(R.string.plz_take_reprint), Toast.LENGTH_LONG).show();
                    }


                } else {
                    Utils.customNavigateAlert(TCActivity.this,
                            getResources().getString(R.string.PPCtoMiller),
                            tcRePrintResponse.getResponseMessage()
                                    + "\n"
                                    + getResources().getString(R.string.plz_take_reprint),
                            getSupportFragmentManager());
                }
            } else if (tcRePrintResponse != null &&
                    tcRePrintResponse.getStatusCode() == AppConstants.FAILURE_CODE) {

                Utils.customNavigateAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        tcRePrintResponse.getResponseMessage()
                                + "\n"
                                + getResources().getString(R.string.plz_take_reprint),
                        getSupportFragmentManager());

            } else if (tcRePrintResponse != null &&
                    tcRePrintResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {

                Utils.customNavigateAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        tcRePrintResponse.getResponseMessage()
                                + "\n"
                                + getResources().getString(R.string.plz_take_reprint),
                        getSupportFragmentManager());

            } else if (tcRePrintResponse != null &&
                    tcRePrintResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(tcRePrintResponse.getResponseMessage(),
                        TCActivity.this, getSupportFragmentManager(), true);

            } else if (tcRePrintResponse != null &&
                    tcRePrintResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(TCActivity.this, getResources().getString(R.string.PPCtoMiller),
                        tcRePrintResponse.getResponseMessage(), getSupportFragmentManager());
            } else {

                Utils.customNavigateAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getResources().getString(R.string.server_not)
                                + "\n"
                                + getResources().getString(R.string.plz_take_reprint),
                        getSupportFragmentManager());
            }
        } catch (Resources.NotFoundException e) {

            Utils.customNavigateAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    getResources().getString(R.string.something)
                            + "\n"
                            + getResources().getString(R.string.plz_take_reprint),
                    getSupportFragmentManager());

            e.printStackTrace();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onError(int code, String msg) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();

        if (code == AppConstants.ERROR_CODE) {
            Utils.customAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else if (code == AppConstants.EXCEPTION_CODE) {
            Utils.customAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    getString(R.string.something) +
                            msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else {
            Utils.customAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    getString(R.string.something),
                    getResources().getString(R.string.ERROR),
                    false);
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class printAsyncTask extends AsyncTask<Void, Void, Boolean> {
        TCRePrintResponse tcRePrintResponse;
        Bitmap bmp = null;
        private boolean gradeAFlag, commonFLag;

        printAsyncTask(TCRePrintResponse tcRePrintResponse) {
            this.tcRePrintResponse = tcRePrintResponse;
            try {
                InputStream is = TCActivity.this.getAssets().open("telanganalogo.bmp");
                bmp = BitmapFactory.decodeStream(is);
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
                    String ppcTitle = "", trnTitle = "", milerTitle;
                    char nLine = '\n';

                    if (tcRePrintResponse.getGettruck().get(0).getGradeApaddydetail() != null) {
                        gradeAFlag = true;
                    }
                    if (tcRePrintResponse.getGettruck().get(0).getCommonpaddydetail() != null) {
                        commonFLag = true;
                    }

                    String commonStr = commonPrint(tcRePrintResponse, nLine, gradeAFlag, commonFLag);

                    ppcTitle = "                       PPC Copy" + nLine + commonStr;
                    trnTitle = "                       Transporter Copy" + nLine + commonStr;
                    milerTitle = "                       Miller Copy" + nLine + commonStr;

                    aPrinter.logoPrinting(AppConstants.BT_ADDRESS, bmp);
                    aPrinter.multiLingualPrint(AppConstants.BT_ADDRESS, ppcTitle, 22, Typeface.SANS_SERIF);

                    aPrinter.logoPrinting(AppConstants.BT_ADDRESS, bmp);
                    aPrinter.multiLingualPrint(AppConstants.BT_ADDRESS, trnTitle, 22, Typeface.SANS_SERIF);

                    aPrinter.logoPrinting(AppConstants.BT_ADDRESS, bmp);
                    aPrinter.multiLingualPrint(AppConstants.BT_ADDRESS, milerTitle, 22, Typeface.SANS_SERIF);

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
                Utils.customPrintFailedAlert(TCActivity.this,
                        TCActivity.class.getSimpleName(),
                        getSupportFragmentManager(),
                        getString(R.string.PPCtoMiller),
                        getString(R.string.failed_tc_print));
            } else {
                try {

                    callHome();

//                    Fragment fragment = new DashboardFragment();
//                    AppConstants.FARG_TAG = TCActivity.class.getSimpleName();
//                    callFragment(fragment, getSupportFragmentManager(), AppConstants.FARG_TAG);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }

    private String commonPrint(TCRePrintResponse tcRePrintResponse, char nLine,
                               boolean gradeAFlag, boolean commonFlag) {

        StringBuilder stringBuilder = new StringBuilder();
        if (gradeAFlag) {
            stringBuilder.append("వరి రకం : " + "Grade-A").append(nLine).append("----------------------")
                    .append(nLine).append("కొత్త సంచులు : ")
                    .append(tcRePrintResponse.getGettruck().get(0).getGradeApaddydetail().getGradeANewbag())
                    .append(nLine).append("పాత సంచులు : ")
                    .append(tcRePrintResponse.getGettruck().get(0).getGradeApaddydetail().getGradeAOldbag())
                    .append(nLine).append("మొత్తం సంచులు : ")
                    .append(tcRePrintResponse.getGettruck().get(0).getGradeApaddydetail().getGradeATotalbag())
                    .append(nLine).append("పరిమాణం (క్వింటాల్లలో) : ")
                    .append(tcRePrintResponse.getGettruck().get(0).getGradeApaddydetail().getGradeAQuantity())
                    .append(nLine).append(nLine);
        }

        if (commonFlag) {
            stringBuilder.append("వరి రకం : " + "Common").append(nLine)
                    .append("----------------------")
                    .append(nLine).append("కొత్త సంచులు : ")
                    .append(tcRePrintResponse.getGettruck().get(0).getCommonpaddydetail().getCommonNewbag())
                    .append(nLine).append("పాత సంచులు : ")
                    .append(tcRePrintResponse.getGettruck().get(0).getCommonpaddydetail().getCommonOldbag())
                    .append(nLine).append("మొత్తం సంచులు : ")
                    .append(tcRePrintResponse.getGettruck().get(0).getCommonpaddydetail().getCommonTotalbag())
                    .append(nLine).append("పరిమాణం (క్వింటాల్లలో) : ")
                    .append(tcRePrintResponse.getGettruck().get(0).getCommonpaddydetail().getCommonQuantity())
                    .append(nLine).append(nLine);
        }

        return "----------------------" + nLine +
                "(ట్రక్ చిట్-పిపిసి రసీదు): " + nLine +
                "----------------------" + nLine +
                "రవాణా తేది : " + tcRePrintResponse.getGettruck().get(0).getTransportDate().substring(0, 10) + nLine +
                "ట్రక్ స్లిప్ సంఖ్య : " + nLine + tcRePrintResponse.getGettruck().get(0).getTruckSlipNo() + nLine +
                "మాన్యువల్ ట్రక్ షీట్  సంఖ్య : " + tcRePrintResponse.getGettruck().get(0).getManualPPCTruckchitID() + nLine +
                "పిపిసి వివరాలు : " + nLine +
                "----------------------" + nLine +
                "పిపిసి పేరు : " + tcRePrintResponse.getGettruck().get(0).getPPCName() + nLine +
                "పిపిసి చిరునామా : " + tcRePrintResponse.getGettruck().get(0).getPPCAddress() + nLine +
                "మిల్లర్ వివరాలు : " + nLine +
                "----------------------" + nLine +
                "మిల్లర్ పేరు : " + tcRePrintResponse.getGettruck().get(0).getMillName() + nLine +
                "మిల్లర్ చిరునామా : " + tcRePrintResponse.getGettruck().get(0).getMillAddress() + nLine +
                "రవాణా వివరాలు : " + nLine +
                "----------------------" + nLine +
                "రవాణాదారుని పేరు : " + tcRePrintResponse.getGettruck().get(0).getTransportName() + nLine +
                "వాహనం రకము : " + tcRePrintResponse.getGettruck().get(0).getVehicalType() + nLine +
                "వాహనం సంఖ్య : " + tcRePrintResponse.getGettruck().get(0).getVehicalNo() + nLine +
                "డ్రైవర్ పేరు : " + tcRePrintResponse.getGettruck().get(0).getDriverName() + nLine +
                "డ్రైవర్ ఫోను నం. : " + tcRePrintResponse.getGettruck().get(0).getDriverMobileNo() + nLine +
                "వరి వివరాలు : " + nLine +
                "----------------------" + nLine +
                stringBuilder + nLine +
                "మొత్తం కొత్త సంచులు : " + tcRePrintResponse.getGettruck().get(0).getTotalNewBags() + nLine +
                "మొత్తం పాత సంచులు : " + tcRePrintResponse.getGettruck().get(0).getTotalOldbags() + nLine +
                "మొత్తం సంచులు : " + tcRePrintResponse.getGettruck().get(0).getTotalBags() + nLine +
                "మొత్తం పరిమాణం (క్వింటాల్లలో) : " + tcRePrintResponse.getGettruck().get(0).getTotalQuantity() + nLine + nLine +
                "పిపిసి సంతకం : " + nLine + nLine +
                "మిల్లర్ సంతకం : " + nLine;
    }


    public class AutoCompleteTVListener implements AdapterView.OnItemClickListener {

        AutoCompleteTextView ac;
        VehicleInterface context;

        AutoCompleteTVListener(VehicleInterface context, AutoCompleteTextView myAc) {
            ac = myAc;
            this.context = context;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            switch (ac.getId()) {
                case R.id.PPCtoMiller_vehicleNo_ATv:
                    vehicleResponseMain = null;
                    binding.PPCtoMillerVehicleTypeSp.setEnabled(true);
                    binding.PPCtoMillerVehicleNoATv.setEnabled(true);
                    try {
                        String vehicleNo = binding.PPCtoMillerVehicleNoATv.getText().toString().trim();
                        if (!TextUtils.isEmpty(vehicleNo) && !vehicleNo.equalsIgnoreCase("--Select--")) {
                            vehicleRepository.getVehicleData(context, selTransID, vehicleNo);
                        } else {
                            binding.PPCtoMillerVehicleTypeSp.setEnabled(true);
                            binding.PPCtoMillerVehicleNoATv.setEnabled(true);
//                            Utils.customAlert(TCActivity.this,
//                                    getResources().getString(R.string.PPCtoMiller),
//                                    getResources().getString(R.string.no_veh_data),
//                                    getResources().getString(R.string.WARNING), false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }

        }

    }

    private void setSelectedMillData(MillerData millerData) {

        try {
            binding.PPCtoMillerOwnerNameTv.setText(millerData.getProprietorName());
            binding.PPCtoMillerOwnerContactNoTv.setText(millerData.getProprietorMobile());
            binding.PPCROTv.setText(millerData.getRoNumber());
            if (millerData.getQuantityAllotedToPPC() != null) {
                curMilQty = millerData.getQuantityAllotedToPPC();
            }
            binding.PPCtoMillerStoarageSpaceAvailTv.setText(String.valueOf(curMilQty));
            binding.PPCtoMillerDistanceFromPPCToMillTv.setText(String.valueOf(millerData.getDistanceFromPPC()));
            binding.PPCtoMillerAddress.setText(millerData.getMillAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearMillForm(ViewGroup group) {
        try {
            for (int i = 0, count = group.getChildCount(); i < count; ++i) {
                View view = group.getChildAt(i);
                if (view instanceof TextView) {
                    int id = view.getId();
                    if (id == R.id.PPCtoMiller_OwnerName_Tv || id == R.id.PPCtoMiller_OwnerContactNo_Tv
                            || id == R.id.PPCRO_Tv || id == R.id.PPCtoMiller_StoarageSpaceAvail_Tv
                            || id == R.id.PPCtoMiller_DistanceFromPPCToMill_Tv || id == R.id.PPCtoMiller_Address)
                        ((TextView) view).setText("");

                }
                if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                    clearMillForm((ViewGroup) view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearTransactionsData() {
        try {
            landDetailsTableRowsArrayList = new ArrayList<>();
            pTransactionArrayList = new ArrayList<>();

            if (transactionResponseGlobal != null) {
                for (int x = 0; x < transactionResponseGlobal.getTransactionsData().size(); x++) {
                    setTransactionsData(transactionResponseGlobal.getTransactionsData().get(x));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.man_edit_btn:
                    enableManEdit();
                    break;
                case R.id.ro_Info:
                    if (validateInfo()) {
                        callTCInfoAPI();
                    }
                    break;
                case R.id.man_check_btn:
                    if (validateManualTC()) {
                        callManualTCAPI(false, null);
                    }
                    break;

                case R.id.refresh:
                    if (ConnectionDetector.isConnectedToInternet(TCActivity.this)) {
                        binding.onlineTcNo.setText("");
                        getOnlineTCNo();
                    } else {
                        Utils.customAlert(TCActivity.this,
                                getResources().getString(R.string.PPCtoMiller),
                                getResources().getString(R.string.no_internet),
                                getResources().getString(R.string.WARNING), false);
                    }
                    break;

                case R.id.trn_refresh:
                    if (ConnectionDetector.isConnectedToInternet(TCActivity.this)) {
                        callTransactions();
                    } else {
                        Utils.customAlert(TCActivity.this,
                                getResources().getString(R.string.PPCtoMiller),
                                getResources().getString(R.string.no_internet),
                                getResources().getString(R.string.WARNING), false);
                    }
                    break;
                case R.id.PPCtoMiller_Submit_Btn:
                    if (!manFlag) {
                        Utils.customAlert(TCActivity.this,
                                getResources().getString(R.string.PPCtoMiller),
                                getString(R.string.plz_ver_man),
                                getString(R.string.INFORMATION), false);
                        return;
                    }


                    TCSubmitRequest tcSubmitRequest = new TCSubmitRequest();
                    if (validateData(tcSubmitRequest)) {
                        if (ppcUserDetails != null) {
                            if (ppcUserDetails.getPPCID() != null) {
                                tcSubmitRequest.setPpcid(ppcUserDetails.getPPCID());
                            } else {
                                Toast.makeText(TCActivity.this, "PPC ID empty", Toast.LENGTH_SHORT).show();
                            }
                            if (ppcUserDetails.getAuthenticationID() != null) {
                                tcSubmitRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                            } else {
                                Toast.makeText(TCActivity.this, "Auth empty", Toast.LENGTH_SHORT).show();
                            }
                            if (binding.onlineTcNo.getText() != null) {
                                tcSubmitRequest.setTruckchit(binding.onlineTcNo.getText().toString().trim());
                            } else {
                                Toast.makeText(TCActivity.this, "Online TC empty", Toast.LENGTH_SHORT).show();
                            }
                            tcSubmitRequest.setSystemIP("");
                            if (ppcUserDetails.getSeasonID() != null) {
                                tcSubmitRequest.setSeasonID(ppcUserDetails.getSeasonID());
                            } else {
                                Toast.makeText(TCActivity.this, "Season ID empty", Toast.LENGTH_SHORT).show();
                            }
                            Long millId = null;
                            if (millerMasterListGlobal != null &&
                                    millerMasterListGlobal.getMillerData() != null &&
                                    millerMasterListGlobal.getMillerData().size() > 0) {

                                String millCode = "";
                                if (binding.PPCtoMillerIdSp.getSelectedItem() != null) {
                                    String millNameCode = binding.PPCtoMillerIdSp.getSelectedItem().toString().trim();
                                    if (millNameCode.contains("~")) {
                                        String[] str = millNameCode.split("~");
                                        if (str.length > 0 && str[1] != null) {
                                            millCode = str[1];
                                            for (int x = 0; x < millerMasterListGlobal.getMillerData().size(); x++) {
                                                if (!TextUtils.isEmpty(millCode) &&
                                                        millerMasterListGlobal.getMillerData().get(x)
                                                                .getMillerCode().equalsIgnoreCase(millCode)) {
                                                    millId = millerMasterListGlobal.getMillerData().get(x).getMillerID();
                                                    break;
                                                }
                                            }
                                            if (millId != null) {
                                                tcSubmitRequest.setMillid(millId);
                                            } else {
                                                Toast.makeText(TCActivity.this, "Empty mill id", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(TCActivity.this, "No Str[1]", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(TCActivity.this, "No negation ", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(TCActivity.this, "Mill is empty", Toast.LENGTH_SHORT).show();
                                }

                            }

                            StringBuilder finalString = tcSubmitData(tcSubmitRequest);
                            if (finalString != null) {
                                tcSubmitRequest.setXmlTruckchitData(finalString.toString());
                                ShowSubmitTCAlert(tcSubmitRequest, TCActivity.this);


                            }
                        } else {
                            Toast.makeText(TCActivity.this, "ppC details null", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case R.id.btn_capture_picture:
                    capturePicture();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callCamPer() {
        try {
            Dexter.withActivity(TCActivity.this)
                    .withPermissions(Manifest.permission.CAMERA)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                capturePicture();
                            } else if (report.isAnyPermissionPermanentlyDenied()) {
                                Utils.openSettings(TCActivity.this);
                                Toast.makeText(TCActivity.this, getString(R.string.all_per_cam), Toast.LENGTH_SHORT).show();

                            } else if (report.getDeniedPermissionResponses().size() > 0) {
                                customPerAlert();
                            }

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void customPerAlert() {
        try {
            final Dialog dialog = new Dialog(TCActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_alert_permission);
                dialog.setCancelable(false);
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(getResources().getString(R.string.app_name));
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(getString(R.string.allow_cam));
                Button yes = dialog.findViewById(R.id.btDialogOk);
                Button no = dialog.findViewById(R.id.btDialogCancel);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(TCActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                CAM_CODE);
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Toast.makeText(TCActivity.this, getString(R.string.cam_den), Toast.LENGTH_SHORT).show();
                    }
                });
                if (!dialog.isShowing())
                    dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enableManEdit() {
        try {
            binding.manualTcNoBfr.setText("");
            binding.manualTcNoAft.setText("");
            binding.manEditBtn.setVisibility(View.GONE);
            binding.manualTcNoBfr.clearFocus();
            binding.manualTcNoBfr.setEnabled(true);
            binding.manualTcNoAft.clearFocus();
            binding.manualTcNoAft.setEnabled(true);
            binding.manCheckBtn.setText(getResources().getString(R.string.verify));
            binding.manCheckBtn.setEnabled(true);
            binding.manCheckBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            manFlag = false;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void callTCInfoAPI() {
        try {

            if (ConnectionDetector.isConnectedToInternet(TCActivity.this)) {

                Long millId = null;
                String roNum = "";
                if (millerMasterListGlobal != null &&
                        millerMasterListGlobal.getMillerData() != null &&
                        millerMasterListGlobal.getMillerData().size() > 0) {
                    String millCode = "";
                    String millNameCode = binding.PPCtoMillerIdSp.getSelectedItem().toString().trim();
                    String[] str = millNameCode.split("~");
                    if (str.length > 0) {
                        millCode = str[1];
                    }
                    for (int x = 0; x < millerMasterListGlobal.getMillerData().size(); x++) {
                        if (!TextUtils.isEmpty(millCode) &&
                                millerMasterListGlobal.getMillerData().get(x)
                                        .getMillerCode().equalsIgnoreCase(millCode)) {
                            millId = millerMasterListGlobal.getMillerData().get(x).getMillerID();
                            roNum = millerMasterListGlobal.getMillerData().get(x).getRoNumber();
                            break;
                        }
                    }
                    if (millId != null && !TextUtils.isEmpty(roNum)) {
                        ROInfoRequest roInfoRequest = new ROInfoRequest();
                        roInfoRequest.setMillerID(String.valueOf(millId));
                        roInfoRequest.setRoNumber(roNum);
                        roInfoRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                        roInfoRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));

                        if (customProgressDialog != null && !customProgressDialog.isShowing())
                            customProgressDialog.show();

                        tcPresenter.GetROInfoResponse(roInfoRequest);

                    } else {
                        Toast.makeText(TCActivity.this, getResources().getString(R.string.something), Toast.LENGTH_SHORT).show();
                    }
                }


            } else {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getResources().getString(R.string.no_internet),
                        getResources().getString(R.string.WARNING), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    private void callManualTCAPI(boolean flag, TCSubmitRequest tcSubmitRequest) {
        try {
            if (ConnectionDetector.isConnectedToInternet(TCActivity.this)) {

                String beforeStr = binding.manualTcNoBfr.getText().toString().trim();
                String afterStr = binding.manualTcNoAft.getText().toString().trim();
                String slash = binding.slashTV.getText().toString().trim();
                String finalStr = beforeStr.concat(slash).concat(afterStr);
                ManualTCRequest manualTCRequest = new ManualTCRequest();
                manualTCRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));
                manualTCRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                manualTCRequest.setManualTruckChit(finalStr);
                if (customProgressDialog != null && !customProgressDialog.isShowing())
                    customProgressDialog.show();

                tcPresenter.GetManualTCResponse(manualTCRequest, flag, tcSubmitRequest);
            } else {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getResources().getString(R.string.no_internet),
                        getResources().getString(R.string.WARNING), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    private boolean validateInfo() {
        String millName = binding.PPCtoMillerIdSp.getSelectedItem().toString();
        if (TextUtils.isEmpty(millName) || millName.equalsIgnoreCase("--Select--")) {
            Utils.customAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    getString(R.string.select_mill),
                    getString(R.string.INFORMATION), false);
            return false;
        }
        return true;
    }


    private boolean validateManualTC() {
        if (TextUtils.isEmpty(binding.manualTcNoBfr.getText().toString().trim())) {
            binding.manualTcNoBfr.setError(getString(R.string.man_req));
            binding.manualTcNoBfr.requestFocus();
            return false;
        }
        if (!(binding.manualTcNoBfr.getText().toString().trim().length() >= 2)) {
            binding.manualTcNoBfr.setError(getString(R.string.man_val));
            binding.manualTcNoBfr.requestFocus();
            return false;
        }
        int manAft = Integer.valueOf(binding.manualTcNoBfr.getText().toString().trim());
        if (manAft == 0) {
            binding.manualTcNoBfr.setError(getString(R.string.man_zero_val));
            binding.manualTcNoBfr.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(binding.manualTcNoAft.getText().toString().trim())) {
            binding.manualTcNoAft.setError(getString(R.string.man_req));
            binding.manualTcNoAft.requestFocus();
            return false;
        }
        if (binding.manualTcNoAft.getText().toString().trim().equalsIgnoreCase("0")) {
            binding.manualTcNoAft.setError(getString(R.string.man_zero_val));
            binding.manualTcNoAft.requestFocus();
            return false;
        }
        return true;
    }

//    public Uri getOutputMediaFileUri(int type) {
//        File imageFile = getOutputMediaFile(type);
//        Uri imageUri = FileProvider.getUriForFile(
//                TCActivity.this,
//                "com.cgg.pps.provider", //(use your app signature + ".provider" )
//                imageFile);
//        return imageUri;
//    }

    //DIRECTORY CREATION CODE
    private File getOutputMediaFile(int type) {

        //File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Android File Upload");

        File mediaStorageDir = new File(Objects.requireNonNull(TCActivity.this).getExternalFilesDir(null) + "/Android/data/"
                + "Files/" + IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TAG", "Oops! Failed create " + "Android File Upload"
                        + " directory");
                return null;
            }
        }

        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date()) + ".jpg";

        File mediaFile;

        if (type == MEDIA_TYPE_IMAGE) {

            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + timeStamp);


        } else {
            return null;
        }

        return mediaFile;
    }


    private void capturePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                onCaptureImageResult(data);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(TCActivity.this,
                        getString(R.string.user_can), Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(TCActivity.this,
                        getString(R.string.failed_cam), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    private void onCaptureImageResult(Intent data) {
        try {
            if (data != null && data.getExtras() != null) {
                Bitmap capturePhotoBitmap = (Bitmap) data.getExtras().get("data");
                if (capturePhotoBitmap != null) {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    capturePhotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                    binding.btnCapturePicture.setImageBitmap(capturePhotoBitmap);
                    Bitmap bitmap = ((BitmapDrawable) binding.btnCapturePicture.getDrawable()).getBitmap();
                    if (bitmap != null)
                        PHOTO_ENCODED_STRING1 = convertBitMap(bitmap);
                    else
                        Toast.makeText(this, "Something went wrong with bitmap", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Something went wrong with photo capture, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong with photo capture, try again", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private String convertBitMap(Bitmap img_bitmap) {
        String encodedImage = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            img_bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encodedImage;
    }

    private boolean validateData(TCSubmitRequest tcSubmitRequest) {
        String millName = null;
        String vehicleNo = null;
        String vehicleType = null;
        String driverName = null;
        String driverMobile = null;
        String driverLicence = null;
        String transporterName = null;
        boolean tFlag = false;

        try {
            if (binding.PPCtoMillerIdSp.getSelectedItem() == null) {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getString(R.string.select_mill),
                        getString(R.string.INFORMATION), false);
                return false;
            }

            millName = binding.PPCtoMillerIdSp.getSelectedItem().toString();
            if (TextUtils.isEmpty(millName) || millName.equalsIgnoreCase("--Select--")) {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getString(R.string.select_mill),
                        getString(R.string.INFORMATION), false);
                return false;
            }

            String transPortDate = Utils.getCurrentDateTime();
            tcSubmitRequest.setTransportDate(transPortDate);

            if (binding.PPCtoMillerTransporterNameSp == null ||
                    binding.PPCtoMillerTransporterNameSp.getSelectedItem() == null) {
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getString(R.string.sel_tra),
                        getString(R.string.INFORMATION), false);
                return false;
            }

            transporterName = binding.PPCtoMillerTransporterNameSp.getSelectedItem().toString().trim();
            if (TextUtils.isEmpty(transporterName) || transporterName.equals("--Select--")) {
                binding.PPCtoMillerTransporterNameSp.requestFocus();
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getString(R.string.sel_tra),
                        getString(R.string.INFORMATION), false);
                return false;
            }

            if (binding.PPCtoMillerVehicleNoATv.getText() != null) {
                vehicleNo = binding.PPCtoMillerVehicleNoATv.getText().toString().trim();
            }
            if (TextUtils.isEmpty(vehicleNo)) {
                binding.PPCtoMillerVehicleNoATv.requestFocus();
                binding.PPCtoMillerVehicleNoATv.setError(getString(R.string.enter_veh_num));
                return false;
            }

            if (vehicleNo.length() < 4) {
                binding.PPCtoMillerVehicleNoATv.requestFocus();
                binding.PPCtoMillerVehicleNoATv.setError(getString(R.string.enter_valid_veh_num));
                return false;
            }

            String strFirst2Char = vehicleNo.substring(0, 2);
            String strLast2Char = vehicleNo.substring(vehicleNo.length() - 2);

            if (!Utils.ValidateVehicleNumberFirst2Digit(strFirst2Char)) {
                binding.PPCtoMillerVehicleNoATv.requestFocus();
                binding.PPCtoMillerVehicleNoATv.setError(getString(R.string.enter_valid_veh_num));
                return false;
            }

            if (!Utils.ValidateVehicleContainDigit(strLast2Char)) {
                binding.PPCtoMillerVehicleNoATv.requestFocus();
                binding.PPCtoMillerVehicleNoATv.setError(getString(R.string.enter_valid_veh_num));
                return false;
            }

            String str4Digit = vehicleNo.substring(vehicleNo.length() - 4);
            if (Utils.ValidateVehicleContainDigit(str4Digit) && str4Digit.equals("0000")) {
                binding.PPCtoMillerVehicleNoATv.requestFocus();
                binding.PPCtoMillerVehicleNoATv.setError(getString(R.string.enter_valid_veh_num));
                return false;
            }

            if (!Utils.ValidateVehicleContainDigit(str4Digit) &&
                    Utils.ValidateVehicleContainDigit(strLast2Char) && strLast2Char.equals("00")) {
                binding.PPCtoMillerVehicleNoATv.requestFocus();
                binding.PPCtoMillerVehicleNoATv.setError(getString(R.string.enter_valid_veh_num));
                return false;
            }
            vehicleType = binding.PPCtoMillerVehicleTypeSp.getSelectedItem().toString().trim();
            if (!TextUtils.isEmpty(vehicleType) && vehicleType.equalsIgnoreCase("--Select--")) {
                binding.PPCtoMillerTransporterNameSp.requestFocus();
                Utils.customAlert(TCActivity.this,
                        getResources().getString(R.string.PPCtoMiller),
                        getString(R.string.sel_veh_type),
                        getString(R.string.INFORMATION), false);
                return false;
            }

            if (vehicleResponseMain != null && vehicleResponseMain.getVehicalNo() != null
                    && vehicleResponseMain.getVehicalNo().trim().equalsIgnoreCase(vehicleNo)) {
                tcSubmitRequest.setVehicleNumber(vehicleResponseMain.getVehicalNo().trim());
                tcSubmitRequest.setVehicleTypeID(vehicleResponseMain.getVehicleID());
                tcSubmitRequest.setTransportID(vehicleResponseMain.getTransporterID());
                tFlag = true;
            }

            if (!tFlag) {
                if (binding.PPCtoMillerVehicleNoATv.getText() != null) {
                    tcSubmitRequest.setVehicleNumber(binding.PPCtoMillerVehicleNoATv.getText().toString());
                } else {
                    Toast.makeText(TCActivity.this, getString(R.string.veh_num_emp), Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (selTransID != -1) {
                    tcSubmitRequest.setTransportID(selTransID);
                } else {
                    Toast.makeText(TCActivity.this, getString(R.string.trs_sel_emp), Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (transporterVehicleTypes != null && transporterVehicleTypes.size() > 0) {
                    for (int z = 0; z < transporterVehicleTypes.size(); z++) {
                        if (transporterVehicleTypes.get(z).getVehicleTypeName().equalsIgnoreCase(vehicleType)) {
                            tcSubmitRequest.setVehicleTypeID(transporterVehicleTypes.get(z).getVehicleTypeID());
                            break;
                        }
                    }
                } else {
                    Toast.makeText(TCActivity.this, getString(R.string.trn_typ_emp), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            driverName = binding.PPCtoMillerDriverNameEt.getText().toString();
            if (TextUtils.isEmpty(driverName)) {
                binding.PPCtoMillerDriverNameEt.requestFocus();
                binding.PPCtoMillerDriverNameEt.setError(getString(R.string.enter_dri_name));
                return false;
            } else if (!driverName.matches("[a-zA-Z. ]*")) {
                binding.PPCtoMillerDriverNameEt.requestFocus();
                binding.PPCtoMillerDriverNameEt.setError(getString(R.string.enter_only_alph));
                return false;
            } else {
                tcSubmitRequest.setDriverName(driverName);
            }


            driverMobile = binding.PPCtoMillerDriverMobileEt.getText().toString();
            if (TextUtils.isEmpty(driverMobile)) {
                binding.PPCtoMillerDriverMobileEt.requestFocus();
                binding.PPCtoMillerDriverMobileEt.setError(getString(R.string.enter_mob_num));

                return false;
            } else if (!driverMobile.matches("^[6-9]{1}[0-9]{9}$")) {
                binding.PPCtoMillerDriverMobileEt.requestFocus();
                binding.PPCtoMillerDriverMobileEt.setError(getString(R.string.enter_valid_mob));
                return false;
            } else {
                tcSubmitRequest.setDriverMobileNumber(driverMobile);
            }

            driverLicence = binding.PPCtoMillerDriverLicenceNoEt.getText().toString();
            if (TextUtils.isEmpty(driverLicence)) {
                binding.PPCtoMillerDriverLicenceNoEt.requestFocus();
                binding.PPCtoMillerDriverLicenceNoEt.setError(getString(R.string.enter_lic_num));
                return false;
            } else {
                tcSubmitRequest.setDriverLicenceNumber(driverLicence);
            }


            if (validateManualTC()) {
                String beforeStr = binding.manualTcNoBfr.getText().toString().trim();
                String afterStr = binding.manualTcNoAft.getText().toString().trim();
                String slash = binding.slashTV.getText().toString().trim();
                String finalStr = beforeStr.concat(slash).concat(afterStr);

                tcSubmitRequest.setManualPPCTruckchitNumber(finalStr);
                tcSubmitRequest.setCreatedDate(transPortDate);
            }

            if (PHOTO_ENCODED_STRING1 == null && TCActivity.this != null) {
                Toast.makeText(TCActivity.this, getString(R.string.plz_take_man_slip), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                tcSubmitRequest.setUploadTRImage(PHOTO_ENCODED_STRING1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<TCTransData> transDataArrayList = new ArrayList<>();
        if (pTransactionArrayList != null && pTransactionArrayList.size() > 0) {
            for (int i = 0; i < pTransactionArrayList.size(); i++) {
                if (pTransactionArrayList.get(i).getQuantity() > 0) {
                    TCTransData tcTransData = new TCTransData();
                    tcTransData.setTokenNumber(pTransactionArrayList.get(i).getTokenNumber());
                    tcTransData.setTokenID(pTransactionArrayList.get(i).getTokenID());
                    tcTransData.setTransactionID(pTransactionArrayList.get(i).getTransactionID());
                    tcTransData.setTransactionRowID(String.valueOf(pTransactionArrayList.get(i).getTransactionRowID()));
                    tcTransData.setPaddyType(pTransactionArrayList.get(i).getPaddyType());
                    tcTransData.setNewBags(pTransactionArrayList.get(i).getNewBags());
                    tcTransData.setOldBags(pTransactionArrayList.get(i).getOldBags());
                    tcTransData.setTotalBags(pTransactionArrayList.get(i).getTotalBags());
                    tcTransData.setQuantity(pTransactionArrayList.get(i).getQuantity());
                    tcTransData.setROID(pTransactionArrayList.get(i).getROID());
                    transDataArrayList.add(tcTransData);
                }
            }

            tcSubmitRequest.setTransDataArrayList(transDataArrayList);
        } else {
            Utils.customAlert(TCActivity.this,
                    getResources().getString(R.string.PPCtoMiller),
                    getString(R.string.plz_chk_trn_rec),
                    getString(R.string.INFORMATION), false);
            return false;
        }
        return true;
    }


    private StringBuilder tcSubmitData(TCSubmitRequest tcSubmitRequest) {

        final StringBuilder xmlDoc = new StringBuilder();

        try {
            long NewBags = 0;
            long OldBags = 0;
            long TotalBags = 0;
            Double TotalQty = 0.0;

            String TransportDate = tcSubmitRequest.getTransportDate();
            String VehicleNumber = tcSubmitRequest.getVehicleNumber();
            String DriverName = tcSubmitRequest.getDriverName();
            String DriverLicenceNumber = tcSubmitRequest.getDriverLicenceNumber();
            String DriverMobileNumber = tcSubmitRequest.getDriverMobileNumber();
            String ManualPPCTruckchitNumber = tcSubmitRequest.getManualPPCTruckchitNumber();
            String CreatedDate = tcSubmitRequest.getCreatedDate();


            if (ppcUserDetails != null
                    && tcSubmitRequest.getTransDataArrayList() != null
                    && tcSubmitRequest.getTransDataArrayList().size() > 0) {

                xmlDoc.append("<TruckchitDetails>");
                xmlDoc.append("<ppc>");
                xmlDoc.append("<ppccode>" + ppcUserDetails.getPPCCode() + "</ppccode>"
                        + "<ppcid>" + ppcUserDetails.getPPCID() + "</ppcid>"
                        + "<millid>" + tcSubmitRequest.getMillid() + "</millid>"
                        + "<SystemIP>" + tcSubmitRequest.getSystemIP() + "</SystemIP>"
                        + "<seasonID>" + tcSubmitRequest.getSeasonID() + "</seasonID>"
                );
                xmlDoc.append("</ppc>");


                xmlDoc.append("<ReleaseOrderTransactionsSET>");

                for (int x = 0; x < tcSubmitRequest.getTransDataArrayList().size(); x++) {
                    TotalQty += tcSubmitRequest.getTransDataArrayList().get(x).getQuantity();
                    NewBags += tcSubmitRequest.getTransDataArrayList().get(x).getNewBags();
                    OldBags += tcSubmitRequest.getTransDataArrayList().get(x).getOldBags();
                    TotalBags += tcSubmitRequest.getTransDataArrayList().get(x).getTotalBags();

                    xmlDoc.append("<Transactions>");
                    xmlDoc.append("<TokenNumber>" + tcSubmitRequest.getTransDataArrayList().get(x).getTokenNumber() + "</TokenNumber>"
                            + "<TokenID>" + tcSubmitRequest.getTransDataArrayList().get(x).getTokenID() + "</TokenID>"
                            + "<TransactionID>" + tcSubmitRequest.getTransDataArrayList().get(x).getTransactionID() + "</TransactionID>"
                            + "<TransactionRowID>" + tcSubmitRequest.getTransDataArrayList().get(x).getTransactionRowID() + "</TransactionRowID>"
                            + "<PaddyType>" + tcSubmitRequest.getTransDataArrayList().get(x).getPaddyType() + "</PaddyType>"
                            + "<NewBags>" + tcSubmitRequest.getTransDataArrayList().get(x).getNewBags() + "</NewBags>"
                            + "<OldBags>" + tcSubmitRequest.getTransDataArrayList().get(x).getOldBags() + "</OldBags>"
                            + "<TotalBags>" + tcSubmitRequest.getTransDataArrayList().get(x).getTotalBags() + "</TotalBags>"
                            + "<Quantity>" + Double.parseDouble(String.format("%.2f", tcSubmitRequest.getTransDataArrayList().get(x).getQuantity())) + "</Quantity>"
                            + "<ROID>" + tcSubmitRequest.getTransDataArrayList().get(x).getROID() + "</ROID>"
                    );
                    xmlDoc.append("</Transactions>");
                }

            } else {
                Toast.makeText(TCActivity.this, "PTransaction empty or ppc details empty", Toast.LENGTH_SHORT).show();
            }

            tcSubmitRequest.setT_NewBags(NewBags);
            tcSubmitRequest.setT_OldBags(OldBags);
            tcSubmitRequest.setT_TotalBags(TotalBags);
            tcSubmitRequest.setT_TotalQty(TotalQty);


            xmlDoc.append("</ReleaseOrderTransactionsSET>");

            xmlDoc.append("<TransportDetails>");
            xmlDoc.append("<TransportID>" + tcSubmitRequest.getTransportID() + "</TransportID>"
                    + "<TransportDate>" + TransportDate + "</TransportDate>"
                    + "<VehicleTypeID>" + tcSubmitRequest.getVehicleTypeID() + "</VehicleTypeID>"
                    + "<VehicleNumber>" + VehicleNumber + "</VehicleNumber>"
                    + "<DriverName>" + DriverName + "</DriverName>"
                    + "<DriverLicenceNumber>" + DriverLicenceNumber + "</DriverLicenceNumber>"
                    + "<DriverMobileNumber>" + DriverMobileNumber + "</DriverMobileNumber>"
                    + "<ManualPPCTruckchitNumber>" + ManualPPCTruckchitNumber + "</ManualPPCTruckchitNumber>"
                    + "<NewBags>" + NewBags + "</NewBags>"
                    + "<OldBags>" + OldBags + "</OldBags>"
                    + "<TotalBags>" + TotalBags + "</TotalBags>"
                    + "<TotalQty>" + TotalQty + "</TotalQty>"
                    + "<CreatedDate>" + CreatedDate + "</CreatedDate>"
            );
            xmlDoc.append("</TransportDetails>");
            xmlDoc.append("</TruckchitDetails>");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return xmlDoc;
    }


//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        try {
//            if (customProgressDialog != null) {
//                customProgressDialog.dismiss();
//                customProgressDialog = null;
//            }
//            if (tcPresenter != null) {
//                tcPresenter.detachView();
//            }
//            if (rePrintPresenter != null) {
//                rePrintPresenter.detachView();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private class CamAsyncTask extends AsyncTask<Void, Void, String> {
        Bitmap bitmap;

        CamAsyncTask(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected String doInBackground(Void... voids) {
            PHOTO_ENCODED_STRING1 = convertBitMap(bitmap);
            return PHOTO_ENCODED_STRING1;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private static final int CAM_CODE = 2000;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAM_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.cam_gra), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getString(R.string.all_per), Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, DashboardActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tc_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {

            startActivity(new Intent(this, DashboardActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        callHome();
    }
}
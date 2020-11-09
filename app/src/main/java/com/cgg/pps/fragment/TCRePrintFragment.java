package com.cgg.pps.fragment;

import android.annotation.SuppressLint;
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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.analogics.thermalAPI.Bluetooth_Printer_2inch_ThermalAPI;
import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.TcReprintFcBinding;
import com.cgg.pps.interfaces.RePrintInterface;
import com.cgg.pps.model.request.reprint.TCRePrintRequest;
import com.cgg.pps.model.response.reprint.ProcRePrintResponse;
import com.cgg.pps.model.response.reprint.TokenRePrintResponse;
import com.cgg.pps.model.response.reprint.truckchit.TCRePrintResponse;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.presenter.RePrintPresenter;
import com.cgg.pps.util.APrinter;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.ConnectionDetector;
import com.cgg.pps.util.CustomProgressDialog;
import com.cgg.pps.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


@SuppressLint("SimpleDateFormat")
public class TCRePrintFragment extends Fragment implements RePrintInterface {

    private PPCUserDetails ppcUserDetails;
    private TcReprintFcBinding binding;
    private RePrintPresenter rePrintPresenter;
    private CustomProgressDialog customProgressDialog;
    private TCRePrintResponse tcRePrintResponseMain;
    private SharedPreferences sharedPreferences;
    private APrinter aPrinter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.tc_reprint_fc, container, false);
        rePrintPresenter = new RePrintPresenter();
        rePrintPresenter.attachView(this);
        customProgressDialog = new CustomProgressDialog(getActivity());
        getPreferenceData();
        aPrinter = APrinter.getaPrinter();

        try {
            binding.tcNumEtDef.setText("TR" + ppcUserDetails.getPPCCode());
        } catch (Exception e) {
            e.printStackTrace();
        }


        binding.btnGetDetails.setOnClickListener(onBtnClick);
        binding.printBtn.setOnClickListener(onBtnClick);

        if (sharedPreferences.getBoolean(AppConstants.PRINTER_CON_FLAG, false)) {
            if (AppConstants.BT_ADDRESS == null || AppConstants.BT_ADDRESS.equals("")) {
                Utils.ShowPrintAlert(getActivity(), getFragmentManager(), customProgressDialog);
            }
        }


        binding.tcManNumEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.scr.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.tcTrnNumEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.scr.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
            if(getActivity()!=null) {
                Gson gson = OPMSApplication.get(getActivity()).getGson();
                sharedPreferences = OPMSApplication.get(getActivity()).getPreferences();
                String string = sharedPreferences.getString(AppConstants.PPC_DATA, "");
                ppcUserDetails = gson.fromJson(string, PPCUserDetails.class);
                if (ppcUserDetails == null) {
                    Utils.ShowDeviceSessionAlert(getActivity(),
                            getResources().getString(R.string.Reprint),
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

    private View.OnClickListener onBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnGetDetails:
                    binding.scr.setVisibility(View.GONE);


                    if (TextUtils.isEmpty(binding.tcManNumEt.getText().toString().trim())) {
                        binding.tcManNumEt.setError(getString(R.string.enter_man_tc_num));
                        binding.tcManNumEt.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(binding.tcTrnNumEt.getText().toString().trim())) {
                        binding.tcTrnNumEt.setError(getString(R.string.enter_online_tc_num));
                        binding.tcTrnNumEt.requestFocus();
                        return;
                    }

                    TCRePrintRequest tcRePrintRequest = new TCRePrintRequest();
                    tcRePrintRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                    tcRePrintRequest.setOnlineTruckChitNo(binding.tcNumEtDef.getText().toString().trim()
                            .concat(binding.tcTrnNumEt.getText().toString().trim()));
                    tcRePrintRequest.setManualTruckChitNo(binding.tcManNumEt.getText().toString().trim());

                    if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                        customProgressDialog.show();
                        rePrintPresenter.GetTCTxnData(tcRePrintRequest);
                    } else {
                        Utils.customAlert(getActivity(),
                                getResources().getString(R.string.Reprint),
                                getResources().getString(R.string.no_internet),
                                getString(R.string.WARNING), false);
                    }
                    break;

                case R.id.print_btn:
                    if (tcRePrintResponseMain != null) {
                        ShowPrintAlert(tcRePrintResponseMain);
                    }
                    break;
            }
        }
    };

    private Dialog dialog;

    private void ShowPrintAlert(TCRePrintResponse tcRePrintResponse) {
        try {
            dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.setContentView(R.layout.tc_re_print_alert);
                dialog.setCancelable(false);
                ImageView iv = dialog.findViewById(R.id.cancel);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                CheckBox ppcCB = dialog.findViewById(R.id.ppc);
                CheckBox trnCB = dialog.findViewById(R.id.trn);
                CheckBox millCB = dialog.findViewById(R.id.mill);


                Button submit = dialog.findViewById(R.id.btDialogYes);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean ppcFlag = false, trnFlag = false, millFlag = false;
                        if (ppcCB.isChecked()) {
                            ppcFlag = true;
                        }
                        if (trnCB.isChecked()) {
                            trnFlag = true;
                        }

                        if (millCB.isChecked()) {
                            millFlag = true;
                        }

                        if (!ppcFlag && !trnFlag && !millFlag) {
                            Toast.makeText(getActivity(), getString(R.string.sel_copy_formats), Toast.LENGTH_SHORT).show();
                        } else {
                            checkPrinterStatus(tcRePrintResponse, ppcFlag, trnFlag, millFlag);
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


    private void checkPrinterStatus(TCRePrintResponse tcRePrintResponse, boolean ppcFlag, boolean trnFlag, boolean millFlag) {

        if (sharedPreferences.getBoolean(AppConstants.PRINTER_CON_FLAG, false)) {
            if (AppConstants.BT_ADDRESS == null || AppConstants.BT_ADDRESS.equals("")) {
                Utils.ShowPrintAlert(getActivity(), getFragmentManager(), customProgressDialog);
                return;
            }
            new printAsyncTask(tcRePrintResponse, ppcFlag, trnFlag, millFlag).execute();

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.print_alert_not), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void GetRePrintTokenData(TokenRePrintResponse tokenRePrintResponse) {

    }

    @Override
    public void GetProcRePrintData(ProcRePrintResponse tcRePrintResponse) {

    }

    @Override
    public void GetTCRePrintData(TCRePrintResponse tcRePrintResponse) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
        try {
            if (tcRePrintResponse != null && tcRePrintResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (tcRePrintResponse.getGettruck() != null && tcRePrintResponse.getGettruck().size() > 0) {
                    setTCData(tcRePrintResponse);
                } else {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.Reprint),
                            tcRePrintResponse.getResponseMessage(),
                            getString(R.string.ERROR), false);
                }
            } else if (tcRePrintResponse != null &&
                    tcRePrintResponse.getStatusCode() == AppConstants.FAILURE_CODE) {

                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.Reprint),
                        tcRePrintResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else if (tcRePrintResponse != null &&
                    tcRePrintResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {

                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.Reprint),
                        tcRePrintResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else if (tcRePrintResponse != null &&
                    tcRePrintResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.Reprint),
                        tcRePrintResponse.getResponseMessage(), getFragmentManager());
            } else if (tcRePrintResponse != null &&
                    tcRePrintResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(tcRePrintResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.Reprint),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {

            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.Reprint),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }
    }

    private void setTCData(TCRePrintResponse tcRePrintResponse) {
        try {
            tcRePrintResponseMain = tcRePrintResponse;
            binding.scr.setVisibility(View.VISIBLE);

            if (tcRePrintResponse.getGettruck().get(0).getTruckSlipNo() != null) {
                binding.txnIDTV.setText(String.valueOf(tcRePrintResponse.getGettruck().get(0).getTruckSlipNo()));
            }

            if (tcRePrintResponse.getGettruck().get(0).getManualPPCTruckchitID() != null) {
                binding.manTxnIDTV.setText(tcRePrintResponse.getGettruck().get(0).getManualPPCTruckchitID());
            }

            if (tcRePrintResponse.getGettruck().get(0).getTransportDate() != null) {
                binding.trnDateTV.setText(tcRePrintResponse.getGettruck().get(0).getTransportDate().trim());
            }

            if (tcRePrintResponse.getGettruck().get(0).getPPCName() != null) {
                binding.ppcNameTV.setText(tcRePrintResponse.getGettruck().get(0).getPPCName().trim());
            }
            if (tcRePrintResponse.getGettruck().get(0).getPPCAddress() != null) {
                binding.ppcAddTV.setText(tcRePrintResponse.getGettruck().get(0).getPPCAddress().trim());
            }

            if (tcRePrintResponse.getGettruck().get(0).getMillName() != null) {
                binding.milllNameTV.setText(tcRePrintResponse.getGettruck().get(0).getMillName().trim());
            }

            if (tcRePrintResponse.getGettruck().get(0).getMillAddress() != null) {
                binding.milAddTV.setText(tcRePrintResponse.getGettruck().get(0).getMillAddress().trim());
            }
            if (tcRePrintResponse.getGettruck().get(0).getTransportName() != null) {
                binding.trnNameTV.setText(tcRePrintResponse.getGettruck().get(0).getTransportName().trim());
            }
            if (tcRePrintResponse.getGettruck().get(0).getVehicalType() != null) {
                binding.vehTypeTV.setText(tcRePrintResponse.getGettruck().get(0).getVehicalType().trim());
            }
            if (tcRePrintResponse.getGettruck().get(0).getVehicalNo() != null) {
                binding.vehNumTV.setText(tcRePrintResponse.getGettruck().get(0).getVehicalNo().trim());
            }
            if (tcRePrintResponse.getGettruck().get(0).getDriverName() != null) {
                binding.driNameTV.setText(tcRePrintResponse.getGettruck().get(0).getDriverName().trim());
            }
            if (tcRePrintResponse.getGettruck().get(0).getDriverMobileNo() != null) {
                binding.driMobTV.setText(tcRePrintResponse.getGettruck().get(0).getDriverMobileNo().trim());
            }


            if (tcRePrintResponse.getGettruck().get(0).getTotalNewBags() != null) {
                binding.totnewBagsTV.setText(String.valueOf(tcRePrintResponse.getGettruck().get(0).getTotalNewBags()));
            }
            if (tcRePrintResponse.getGettruck().get(0).getTotalOldbags() != null) {
                binding.totoldBagsTV.setText(String.valueOf(tcRePrintResponse.getGettruck().get(0).getTotalOldbags()));
            }

            if (tcRePrintResponse.getGettruck().get(0).getTotalBags() != null) {
                binding.totBagsTV.setText(String.valueOf(tcRePrintResponse.getGettruck().get(0).getTotalBags()));
            }
            if (tcRePrintResponse.getGettruck().get(0).getTotalQuantity() != null) {
                binding.totqtyTV.setText(String.valueOf(tcRePrintResponse.getGettruck().get(0).getTotalQuantity()));
            }

            StringBuilder paddyStr = new StringBuilder();
            if (tcRePrintResponse.getGettruck().get(0).getGradeApaddydetail() != null) {
                paddyStr.append("Grade-A");

                binding.graCV.setVisibility(View.VISIBLE);
                binding.graPaddTypeTV.setText("Grade-A");


                if (tcRePrintResponse.getGettruck().get(0).getGradeApaddydetail().getGradeANewbag() != null) {
                    binding.grANewBags.setText(String.valueOf(tcRePrintResponse.getGettruck().get(0).getGradeApaddydetail().getGradeANewbag()));
                }
                if (tcRePrintResponse.getGettruck().get(0).getGradeApaddydetail().getGradeAOldbag() != null) {
                    binding.grAOldBags.setText(String.valueOf(tcRePrintResponse.getGettruck().get(0).getGradeApaddydetail().getGradeAOldbag()));
                }
                if (tcRePrintResponse.getGettruck().get(0).getGradeApaddydetail().getGradeATotalbag() != null) {
                    binding.grATotBags.setText(String.valueOf(tcRePrintResponse.getGettruck().get(0).getGradeApaddydetail().getGradeATotalbag()));
                }
                if (tcRePrintResponse.getGettruck().get(0).getGradeApaddydetail().getGradeAQuantity() != null) {
                    binding.graAQty.setText(String.valueOf(tcRePrintResponse.getGettruck().get(0).getGradeApaddydetail().getGradeAQuantity()));
                }


            }

            if (tcRePrintResponse.getGettruck().get(0).getCommonpaddydetail() != null) {
                paddyStr.append(", Common");
                binding.cmnCV.setVisibility(View.VISIBLE);
                binding.cmnPaddTypeTV.setText("Common");
                if (tcRePrintResponse.getGettruck().get(0).getCommonpaddydetail().getCommonNewbag() != null) {
                    binding.cmnNewBags.setText(String.valueOf(tcRePrintResponse.getGettruck().get(0).getCommonpaddydetail().getCommonNewbag()));
                }
                if (tcRePrintResponse.getGettruck().get(0).getCommonpaddydetail().getCommonOldbag() != null) {
                    binding.cmnOldBags.setText(String.valueOf(tcRePrintResponse.getGettruck().get(0).getCommonpaddydetail().getCommonOldbag()));
                }
                if (tcRePrintResponse.getGettruck().get(0).getCommonpaddydetail().getCommonTotalbag() != null) {
                    binding.cmnTotBags.setText(String.valueOf(tcRePrintResponse.getGettruck().get(0).getCommonpaddydetail().getCommonTotalbag()));
                }
                if (tcRePrintResponse.getGettruck().get(0).getCommonpaddydetail().getCommonQuantity() != null) {
                    binding.cmnAQty.setText(String.valueOf(tcRePrintResponse.getGettruck().get(0).getCommonpaddydetail().getCommonQuantity()));
                }
            }

            if (!TextUtils.isEmpty(paddyStr)) {
                binding.totPaddTypeTV.setText(paddyStr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean connectBT() {
        boolean flag = false;
        try {
            if (!TextUtils.isEmpty(AppConstants.BT_ADDRESS)) {
                flag = aPrinter.openBT(AppConstants.BT_ADDRESS);
            } else {
                Toast.makeText(getActivity(),  getResources().getString(R.string.print_alert_not), Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public void onError(int code, String msg) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();

        if (code == AppConstants.ERROR_CODE) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.Reprint),
                    msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else if (code == AppConstants.EXCEPTION_CODE) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.Reprint),
                    getString(R.string.something) +
                            msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.Reprint),
                    getString(R.string.something),
                    getResources().getString(R.string.ERROR),
                    false);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class printAsyncTask extends AsyncTask<Void, Void, Boolean> {
        TCRePrintResponse tcRePrintResponse;
        Bitmap bmp = null;
        private boolean ppcFlag, trnFlag, millFlag;
        private boolean gradeAFlag, commonFLag;

        printAsyncTask(TCRePrintResponse tcRePrintResponse, boolean ppcFlag, boolean trnFlag, boolean millFlag) {
            this.tcRePrintResponse = tcRePrintResponse;
            this.ppcFlag = ppcFlag;
            this.trnFlag = trnFlag;
            this.millFlag = millFlag;
            try {
                InputStream is = getActivity().getAssets().open("telanganalogo.bmp");
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
            String ppcTitle = "", trnTitle = "", millTitle = "";


            try {
                flag = connectBT();
                if (flag) {
                    char nLine = '\n';

                    if (tcRePrintResponse.getGettruck().get(0).getGradeApaddydetail() != null) {
                        gradeAFlag = true;
                    }
                    if (tcRePrintResponse.getGettruck().get(0).getCommonpaddydetail() != null) {
                        commonFLag = true;
                    }

                    String commonStr = commonPrint(tcRePrintResponse, nLine, gradeAFlag, commonFLag);

                    if (ppcFlag) {
                        ppcTitle = "                       PPC Copy" + nLine + commonStr;
                    }

                    if (trnFlag) {
                        trnTitle = "                       Transporter Copy" + nLine + commonStr;
                    }

                    if (millFlag) {
                        millTitle = "                       Miller Copy" + nLine + commonStr;
                    }

                    if (ppcFlag) {
                        aPrinter.logoPrinting(AppConstants.BT_ADDRESS, bmp);
                        aPrinter.multiLingualPrint(AppConstants.BT_ADDRESS, ppcTitle, 22, Typeface.SANS_SERIF);
                    }

                    if (trnFlag) {
                        aPrinter.logoPrinting(AppConstants.BT_ADDRESS, bmp);
                        aPrinter.multiLingualPrint(AppConstants.BT_ADDRESS, trnTitle, 22, Typeface.SANS_SERIF);
                    }

                    if (millFlag) {
                        aPrinter.logoPrinting(AppConstants.BT_ADDRESS, bmp);
                        aPrinter.multiLingualPrint(AppConstants.BT_ADDRESS, millTitle, 22, Typeface.SANS_SERIF);
                    }

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
                Toast.makeText(getActivity(), "Failed to give truck chit print, Try again", Toast.LENGTH_SHORT).show();
            } else {
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
                binding.tcManNumEt.setText("");
                binding.tcTrnNumEt.setText("");
            }

            try {
                aPrinter.closeBT();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String commonPrint(TCRePrintResponse tcRePrintResponse, char nLine, boolean gradeAFlag, boolean commonFlag) {

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (customProgressDialog != null) {
                customProgressDialog = null;
            }
            if (rePrintPresenter != null) {
                rePrintPresenter.detachView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

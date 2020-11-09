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
import com.cgg.pps.databinding.ProcReprintFcBinding;
import com.cgg.pps.interfaces.RePrintInterface;
import com.cgg.pps.model.request.reprint.ProRePrintRequest;
import com.cgg.pps.model.response.reprint.Printprocurementdata;
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
public class ProcRePrintFragment extends Fragment implements RePrintInterface {

    private PPCUserDetails ppcUserDetails;
    private ProcReprintFcBinding binding;
    private RePrintPresenter rePrintPresenter;
    private CustomProgressDialog customProgressDialog;
    private ProcRePrintResponse procRePrintResponseMain;
    private SharedPreferences sharedPreferences;
    private APrinter aPrinter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.proc_reprint_fc, container, false);
        rePrintPresenter = new RePrintPresenter();
        rePrintPresenter.attachView(this);
        customProgressDialog = new CustomProgressDialog(getActivity());
        getPreferenceData();
        aPrinter = APrinter.getaPrinter();

        binding.btnGetDetails.setOnClickListener(onBtnClick);
        binding.printBtn.setOnClickListener(onBtnClick);

        if (sharedPreferences.getBoolean(AppConstants.PRINTER_CON_FLAG, false)) {
            if (AppConstants.BT_ADDRESS == null || AppConstants.BT_ADDRESS.equals("")) {
                Utils.ShowPrintAlert(getActivity(), getFragmentManager(), customProgressDialog);
            }
        }


        binding.proTrnNumEt.addTextChangedListener(new TextWatcher() {
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

                    if (TextUtils.isEmpty(binding.proTrnNumEt.getText().toString().trim())) {
                        binding.proTrnNumEt.setError(getString(R.string.enter_trn_num));
                        binding.proTrnNumEt.requestFocus();
                        return;
                    }

                    ProRePrintRequest proRePrintRequest = new ProRePrintRequest();
                    proRePrintRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                    proRePrintRequest.setpPCID(String.valueOf(ppcUserDetails.getPPCID()));
                    proRePrintRequest.setFarmerTranscationID(binding.proTrnNumEt.getText().toString().trim());

                    if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                        customProgressDialog.show();
                        rePrintPresenter.GetProTxnData(proRePrintRequest);
                    } else {
                        Utils.customAlert(getActivity(),
                                getResources().getString(R.string.Reprint),
                                getResources().getString(R.string.no_internet),
                                getString(R.string.WARNING), false);
                    }
                    break;

                case R.id.print_btn:
                    if (procRePrintResponseMain != null) {
                        //show dialog

                        ShowPrintAlert(procRePrintResponseMain);

                    }
                    break;
            }
        }
    };

    private Dialog dialog;

    private void ShowPrintAlert(ProcRePrintResponse procRePrintResponse) {
        try {
            dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.setContentView(R.layout.proc_re_print_alert);
                dialog.setCancelable(false);
                ImageView iv = dialog.findViewById(R.id.cancel);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                CheckBox ppcCB = dialog.findViewById(R.id.ppc);
                CheckBox farCB = dialog.findViewById(R.id.far);


                Button submit = dialog.findViewById(R.id.btDialogYes);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean ppcFlag = false, farFlag = false;
                        if (ppcCB.isChecked()) {
                            ppcFlag = true;
                        }
                        if (farCB.isChecked()) {
                            farFlag = true;
                        }

                        if (!ppcFlag && !farFlag) {
                            Toast.makeText(getActivity(), getString(R.string.sel_copy_formats), Toast.LENGTH_SHORT).show();
                        } else {
                            checkPrinterStatus(procRePrintResponse, ppcFlag, farFlag);
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


    private void checkPrinterStatus(ProcRePrintResponse procRePrintResponse, boolean ppcFlag, boolean farFlag) {

        try {
            if (sharedPreferences.getBoolean(AppConstants.PRINTER_CON_FLAG, false)) {
                if (AppConstants.BT_ADDRESS == null || AppConstants.BT_ADDRESS.equals("")) {
                    Utils.ShowPrintAlert(getActivity(), getFragmentManager(), customProgressDialog);
                    return;
                }
                new printAsyncTask(procRePrintResponse, ppcFlag, farFlag).execute();


            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.print_alert_not), Toast.LENGTH_LONG).show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
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
                    procRePrintResponseMain = procRePrintResponse;
                    setPaddyData(procRePrintResponse.getPrintprocurementdata());
                } else {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.Reprint),
                            procRePrintResponse.getResponseMessage(),
                            getString(R.string.ERROR), false);
                }
            } else if (procRePrintResponse != null &&
                    procRePrintResponse.getStatusCode() == AppConstants.FAILURE_CODE) {

                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.Reprint),
                        procRePrintResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else if (procRePrintResponse != null &&
                    procRePrintResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {

                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.Reprint),
                        procRePrintResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else if (procRePrintResponse != null &&
                    procRePrintResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.Reprint),
                        procRePrintResponse.getResponseMessage(), getFragmentManager());
            } else if (procRePrintResponse != null &&
                    procRePrintResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(procRePrintResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

                if (customProgressDialog != null && customProgressDialog.isShowing())
                    customProgressDialog.dismiss();

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

    @Override
    public void GetTCRePrintData(TCRePrintResponse tcRePrintResponse) {

    }

    private void setPaddyData(Printprocurementdata printprocurementdata) {
        try {

            binding.scr.setVisibility(View.VISIBLE);
            if (printprocurementdata.getURNNumber() != null) {
                binding.urnNoTV.setText(String.valueOf(printprocurementdata.getURNNumber()));
            }

            if (printprocurementdata.getFarmerTransactionID() != null) {
                binding.txnIDTV.setText(String.valueOf(printprocurementdata.getFarmerTransactionID()));
            }

            if (printprocurementdata.getTransactionDate() != null) {
                binding.paddyTxndDateTV.setText(String.valueOf(printprocurementdata.getTransactionDate()));
            }

            if (printprocurementdata.getRegistrationNo() != null) {
                binding.regIDTV.setText(String.valueOf(printprocurementdata.getRegistrationNo()));
            }

            if (printprocurementdata.getFarmerName() != null) {
                binding.farNameTv.setText(String.valueOf(printprocurementdata.getFarmerName()));
            }


            if (printprocurementdata.getFarmerFatherName() != null) {
                binding.fatherNameTV.setText(String.valueOf(printprocurementdata.getFarmerFatherName()));
            }

            if (printprocurementdata.getMobile() != null) {
                binding.mobNumTV.setText(String.valueOf(printprocurementdata.getMobile()));
            }

            if (printprocurementdata.getBankAccountNo() != null) {
                binding.bankAccNumTV.setText(String.valueOf(printprocurementdata.getBankAccountNo()));
            }


            if (printprocurementdata.getBankName() != null) {
                binding.bankTV.setText(String.valueOf(printprocurementdata.getBankName()));
            }

            if (printprocurementdata.getBranchName() != null) {
                binding.branchTV.setText(String.valueOf(printprocurementdata.getBranchName()));
            }

            if (printprocurementdata.getIFSCCode() != null) {
                binding.ifscTV.setText(String.valueOf(printprocurementdata.getIFSCCode()));
            }

            if (printprocurementdata.getPaddyType() != null) {
                binding.paddyTypeTV.setText(String.valueOf(printprocurementdata.getPaddyType()));
            }

            if (printprocurementdata.getMSPAmount() != null) {
                binding.mspTV.setText(String.valueOf(printprocurementdata.getMSPAmount()));
            }

            if (printprocurementdata.getNewBags() != null) {
                binding.newBagsTV.setText(String.valueOf(printprocurementdata.getNewBags()));
            }

            if (printprocurementdata.getOldBags() != null) {
                binding.oldBagsTV.setText(String.valueOf(printprocurementdata.getOldBags()));
            }

            if (printprocurementdata.getTotalBags() != null) {
                binding.totBagsTV.setText(String.valueOf(printprocurementdata.getTotalBags()));
            }

            if (printprocurementdata.getQuantity() != null) {
                binding.qtyTV.setText(String.valueOf(printprocurementdata.getQuantity()));
            }
            if (printprocurementdata.getTotalAmount() != null) {
                binding.totAmtTV.setText(String.valueOf(printprocurementdata.getTotalAmount()));
            }


            if (ppcUserDetails != null) {
                binding.ppcCodeTv.setText(String.valueOf(ppcUserDetails.getPPCCode()));
                binding.ppcNameTV.setText(String.valueOf(ppcUserDetails.getPPCName()));
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
        ProcRePrintResponse procRePrintResponse;
        private boolean ppcFlag, farFlag;
        Bitmap bmp = null;

        printAsyncTask(ProcRePrintResponse procRePrintResponse, boolean ppcFlag, boolean farFlag) {
            this.procRePrintResponse = procRePrintResponse;
            this.ppcFlag = ppcFlag;
            this.farFlag = farFlag;
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

            try {
                flag = connectBT();
                if (flag) {
                    String ppcTitle = "", farTitle = "";
                    char nLine = '\n';
                    String commonStr = commonPrint(procRePrintResponse, nLine);

                    if (ppcFlag) {
                        ppcTitle = "                       PPC Copy" + nLine + commonStr;
                    }

                    if (farFlag) {
                        farTitle = "                       Farmer Copy" + nLine + commonStr;
                    }


                    if (ppcFlag) {
                        aPrinter.logoPrinting(AppConstants.BT_ADDRESS, bmp);
                        aPrinter.multiLingualPrint(AppConstants.BT_ADDRESS, ppcTitle, 22, Typeface.SANS_SERIF);
                    }

                    if (farFlag) {
                        aPrinter.logoPrinting(AppConstants.BT_ADDRESS, bmp);
                        aPrinter.multiLingualPrint(AppConstants.BT_ADDRESS, farTitle, 22, Typeface.SANS_SERIF);
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

            if (aBoolean) {
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
                binding.proTrnNumEt.setText("");
            } else {
                Toast.makeText(getActivity(), getString(R.string.fail_paddy_print), Toast.LENGTH_LONG).show();
            }

            try {
                aPrinter.closeBT();
            } catch (IOException e) {
                e.printStackTrace();
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

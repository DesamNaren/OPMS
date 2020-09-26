package com.cgg.pps.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.analogics.thermalAPI.Bluetooth_Printer_2inch_ThermalAPI;
import com.cgg.pps.R;

import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.TokenReprintFcBinding;
import com.cgg.pps.interfaces.RePrintInterface;
import com.cgg.pps.model.request.TokenRePrintRequest;
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
public class TokenRePrintFragment extends Fragment implements RePrintInterface {

    private PPCUserDetails ppcUserDetails;
    private TokenReprintFcBinding binding;
    private RePrintPresenter rePrintPresenter;
    private CustomProgressDialog customProgressDialog;
    private TokenRePrintResponse tokenRePrintResponseMain;
    private SharedPreferences sharedPreferences;
    private APrinter aPrinter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.token_reprint_fc, container, false);
        rePrintPresenter = new RePrintPresenter();
        rePrintPresenter.attachView(this);
        customProgressDialog = new CustomProgressDialog(getActivity());
        getPreferenceData();
        aPrinter = APrinter.getaPrinter();

        try {
            binding.tokenNumEtDef.setText(ppcUserDetails.getPPCCode() + Utils.getCurrentYear() + ppcUserDetails.getSeasonID() + "-");
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

        binding.tokenNumEtDef.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.cv.setVisibility(View.GONE);
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
                    binding.cv.setVisibility(View.GONE);
                    if (TextUtils.isEmpty(binding.tokenNumEt.getText().toString().trim())) {
                        binding.tokenNumEt.setError("Please enter token number");
                        binding.tokenNumEt.requestFocus();
                        return;
                    }

                    TokenRePrintRequest tokenRePrintRequest = new TokenRePrintRequest();
                    tokenRePrintRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                    tokenRePrintRequest.setpPCID(String.valueOf(ppcUserDetails.getPPCID()));
                    tokenRePrintRequest.setTokenNo(binding.tokenNumEtDef.getText().toString().trim()
                            .concat(binding.tokenNumEt.getText().toString().trim()));


                    if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                        customProgressDialog.show();
                        binding.tokenNumEt.clearFocus();
                        rePrintPresenter.GetTokenRePrintData(tokenRePrintRequest);
                    } else {
                        Utils.customAlert(getActivity(),
                                getResources().getString(R.string.Reprint),
                                getResources().getString(R.string.no_internet),
                                getString(R.string.WARNING), false);
                    }
                    break;

                case R.id.print_btn:
                    if (tokenRePrintResponseMain != null) {
                        checkPrinterStatus(tokenRePrintResponseMain);
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.something), Toast.LENGTH_SHORT).show();

                    }
                    break;
            }
        }
    };

    private void checkPrinterStatus(TokenRePrintResponse tokenRePrintResponse) {
        if (sharedPreferences.getBoolean(AppConstants.PRINTER_CON_FLAG, false)){
            if (AppConstants.BT_ADDRESS == null || AppConstants.BT_ADDRESS.equals("")) {
                Utils.ShowPrintAlert(getActivity(), getFragmentManager(), customProgressDialog);
                return;
            }
            new printAsyncTask(tokenRePrintResponse).execute();

        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.print_alert_not), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void GetRePrintTokenData(TokenRePrintResponse tokenRePrintResponse) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
        try {
            if (tokenRePrintResponse != null && tokenRePrintResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (tokenRePrintResponse.getGetTokenOutput() != null && tokenRePrintResponse.getGetTokenOutput().size() > 0) {
                    setSelectedTokenData(tokenRePrintResponse);
                } else {

                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.Reprint),
                            tokenRePrintResponse.getResponseMessage(),
                            getString(R.string.ERROR), false);

                }
            } else if (tokenRePrintResponse != null &&
                    tokenRePrintResponse.getStatusCode() == AppConstants.FAILURE_CODE) {

                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.Reprint),
                        tokenRePrintResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else if (tokenRePrintResponse != null &&
                    tokenRePrintResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {

                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.Reprint),
                        tokenRePrintResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else if (tokenRePrintResponse != null &&
                    tokenRePrintResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.Reprint),
                        tokenRePrintResponse.getResponseMessage(), getFragmentManager());
            } else if (tokenRePrintResponse != null &&
                    tokenRePrintResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(tokenRePrintResponse.getResponseMessage(),
                        getActivity(), getFragmentManager(), true);

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
    public void GetProcRePrintData(ProcRePrintResponse procRePrintResponse) {

    }

    @Override
    public void GetTCRePrintData(TCRePrintResponse tcRePrintResponse) {

    }

    private void setSelectedTokenData(TokenRePrintResponse tokenRePrintResponse) {
        try {
            tokenRePrintResponseMain = tokenRePrintResponse;
            binding.cv.setVisibility(View.VISIBLE);

            if (tokenRePrintResponse.getGetTokenOutput().get(0).getTokenID() != null) {
                binding.tokenIdTV.setText(String.valueOf(tokenRePrintResponse.getGetTokenOutput().get(0).getTokenID()));
            }

            if (tokenRePrintResponse.getGetTokenOutput().get(0).getTokenNo() != null) {
                binding.tokenNumTV.setText(tokenRePrintResponse.getGetTokenOutput().get(0).getTokenNo());
            }

            if (tokenRePrintResponse.getGetTokenOutput().get(0).getFarmerName() != null) {
                binding.framerNameTV.setText(tokenRePrintResponse.getGetTokenOutput().get(0).getFarmerName().trim());
            }

            if (tokenRePrintResponse.getGetTokenOutput().get(0).getMobile() != null) {
                binding.mobNumTV.setText(tokenRePrintResponse.getGetTokenOutput().get(0).getMobile().trim());
            }
            if (tokenRePrintResponse.getGetTokenOutput().get(0).getAppointmentDate() != null) {
                binding.farmerAppDateTv.setText(tokenRePrintResponse.getGetTokenOutput().get(0).getAppointmentDate().trim());
            }

            if (tokenRePrintResponse.getGetTokenOutput().get(0).getCreatedDate() != null) {
                binding.tokenIssuedDateTv.setText(tokenRePrintResponse.getGetTokenOutput().get(0).getCreatedDate().trim());
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
        TokenRePrintResponse tokenRePrintResponse;
        Bitmap bmp = null;

        printAsyncTask(TokenRePrintResponse tokenRePrintResponse) {
            this.tokenRePrintResponse = tokenRePrintResponse;
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
                    aPrinter.logoPrinting(AppConstants.BT_ADDRESS, bmp);
                    char nLine = '\n';
                    String str = "Token Receipt" + nLine +
                            "----------------------" + nLine +
                            "పిపిసి కోడ్: " + ppcUserDetails.getPPCCode() + nLine +
                            "టోకెన్ ఇచ్చిన  తేది : " + tokenRePrintResponse.getGetTokenOutput().get(0).getCreatedDate() + nLine +
                            "రైతు రావాల్సిన తేది: " + tokenRePrintResponse.getGetTokenOutput().get(0).getAppointmentDate() + nLine +
                            "రైతు పేరు: " + tokenRePrintResponse.getGetTokenOutput().get(0).getFarmerName() + nLine +
                            "ఫోన్ నంబరు: " + tokenRePrintResponse.getGetTokenOutput().get(0).getMobile() + nLine +
                            "టోకెన్ నంబరు: " + tokenRePrintResponse.getGetTokenOutput().get(0).getTokenNo() + nLine;

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
            if (aBoolean) {
                binding.tokenNumEt.setText("");
                binding.cv.setVisibility(View.GONE);
            } else {
                Toast.makeText(getActivity(), getString(R.string.failed_token_tc), Toast.LENGTH_SHORT).show();
            }

            try {
                aPrinter.closeBT();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

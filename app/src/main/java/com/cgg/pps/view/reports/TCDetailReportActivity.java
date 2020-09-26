package com.cgg.pps.view.reports;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;

import com.cgg.pps.R;
import com.cgg.pps.adapter.TCDetailReportAdapter;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.ActivityTcWiseDetailReportBinding;
import com.cgg.pps.interfaces.TokenReportInterface;
import com.cgg.pps.model.request.reports.CommonReportRequest;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
import com.cgg.pps.model.response.report.payment.ReportsDataResponce;
import com.cgg.pps.model.response.report.transaction.TransactionReportResponseList;
import com.cgg.pps.model.response.report.truckchit.TCReportResponse;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.presenter.ReportPresenter;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import com.cgg.pps.util.CustomProgressDialog;

public class TCDetailReportActivity extends AppCompatActivity implements TokenReportInterface {
    private TCReportResponse tcReportResponse;
    private TCDetailReportAdapter tcDetailReportAdapter;
    private PPCUserDetails ppcUserDetails;
    private ActivityTcWiseDetailReportBinding binding;
    private CustomProgressDialog customProgressDialog;
    private Context context;
    private ReportPresenter reportPresenter;
    private String cacheDate, currentDate;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tc_wise_detail_report);
        sharedPreferences = OPMSApplication.get(this).getPreferences();
        Toolbar toolbar = findViewById(R.id.toolbar);
        try {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Truck Chit Transaction Report");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            customProgressDialog = new CustomProgressDialog(TCDetailReportActivity.this);

            reportPresenter = new ReportPresenter();
            reportPresenter.attachView(this);

            getPreferenceData();
            getTCCompleteData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    private void getTCCompleteData() {
        try {
            Intent intent = getIntent();
            tcReportResponse = (TCReportResponse) intent.getSerializableExtra("TC_DATA");

            if (tcReportResponse == null) {
                Utils.customAlert(TCDetailReportActivity.this,
                        getResources().getString(R.string.nav_reports),
                        getResources().getString(R.string.something),
                        getString(R.string.ERROR), false);
            } else {
                binCompleteData(tcReportResponse);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void binCompleteData(TCReportResponse tcReportResponse) {
        try {
            binding.trIDTV.setText(tcReportResponse.getTRID());
            binding.trNoTV.setText(tcReportResponse.getTRNo());
            binding.trManTV.setText(tcReportResponse.getTRManualNo());
            binding.vehNoTV.setText(tcReportResponse.getVehicleNo());
            binding.millIDTV.setText(tcReportResponse.getMillID());
            binding.millNameTV.setText(tcReportResponse.getMillName());
            binding.ppcIDTV.setText(tcReportResponse.getPpcID());
            binding.ppcNameTV.setText(tcReportResponse.getPpcName());
            binding.manNameTV.setText(tcReportResponse.getMandalName());
            binding.traNameTV.setText(tcReportResponse.getTransporterName());
            binding.traDateTV.setText(tcReportResponse.getTransportDate());
            binding.ackDateTV.setText(tcReportResponse.getAcknowledgmentDate());
            binding.driNameTV.setText(tcReportResponse.getDriverName());
            binding.driLicNoTV.setText(tcReportResponse.getDriverLicenseNo());

            binding.sentNewTV.setText(tcReportResponse.getBagSentNew());
            binding.sentOldTV.setText(tcReportResponse.getBagSentOld());
            binding.sentTotBagsTV.setText(tcReportResponse.getBagSentTotal());

            binding.sentGradeAQty.setText(tcReportResponse.getQuantitySentGradeA());
            binding.sentComQtyTV.setText(tcReportResponse.getQuantitySentGradeCommon());
            binding.sentTotQtyTV.setText(tcReportResponse.getQuantitySentTotal());

            binding.recNewBagsTV.setText(tcReportResponse.getBagReceivedNew());
            binding.recOldBagsTV.setText(tcReportResponse.getBagReceivedOld());
            binding.recTotBagsTV.setText(tcReportResponse.getBagReceivedTotal());

            binding.recGradeAQtyTV.setText(tcReportResponse.getQuantityReceivedGradeA());
            binding.recComQtyTV.setText(tcReportResponse.getQuantityReceivedGradeCommon());
            binding.recTotQtyTV.setText(tcReportResponse.getQuantityReceivedTotal());

            callTruckChitReportTask(binding.trIDTV.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getPreferenceData() {
        try {
            Gson gson = OPMSApplication.get(TCDetailReportActivity.this).getGson();
            SharedPreferences sharedPreferences = OPMSApplication.get(TCDetailReportActivity.this).getPreferences();
            String string = sharedPreferences.getString(AppConstants.PPC_DATA, "");
            ppcUserDetails = gson.fromJson(string, PPCUserDetails.class);
            if (ppcUserDetails == null) {
                Utils.ShowDeviceSessionAlert(this,
                        getResources().getString(R.string.nav_reports),
                        getString(R.string.ses_expire_re),
                        getSupportFragmentManager());
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void callTruckChitReportTask(String trID) {
        try {
            CommonReportRequest commonReportRequest = new CommonReportRequest();
            commonReportRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
            commonReportRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));
            commonReportRequest.setTokenID("00"); // static
            commonReportRequest.setTokenNo("00"); //static
            commonReportRequest.setRegistrationNo("00"); //static
            commonReportRequest.setTransactionID("00"); //static
            commonReportRequest.setTransactionRowID(trID); //static
            commonReportRequest.setTransactionStatus("00"); //static
            commonReportRequest.setServiceName("GetTruckchitTransactionData"); //static
            if (customProgressDialog != null)
                customProgressDialog.show();
            reportPresenter.GetPaymentStatusReports(commonReportRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getPaymentStatusReportData(ReportsDataResponce paymentReportResponseList) {
        try {
            if (paymentReportResponseList != null && paymentReportResponseList.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (paymentReportResponseList.getGetTruckchitTransactionDataReport() != null && paymentReportResponseList.getGetTruckchitTransactionDataReport().size() > 0) {
                    tcDetailReportAdapter = new TCDetailReportAdapter(TCDetailReportActivity.this, paymentReportResponseList.getGetTruckchitTransactionDataReport());
                    binding.tcTxnRV.setLayoutManager(new GridLayoutManager(TCDetailReportActivity.this, 3));
                    binding.tcTxnRV.setAdapter(tcDetailReportAdapter);
                } else {
                    Utils.customAlert(TCDetailReportActivity.this,
                            getResources().getString(R.string.nav_reports),
                            paymentReportResponseList.getResponseMessage(),
                            getString(R.string.ERROR), false);
                }
            } else if (paymentReportResponseList != null &&
                    paymentReportResponseList.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(TCDetailReportActivity.this,
                        getResources().getString(R.string.nav_reports),
                        paymentReportResponseList.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else if (paymentReportResponseList != null &&
                    paymentReportResponseList.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(TCDetailReportActivity.this,
                        getResources().getString(R.string.nav_reports),
                        paymentReportResponseList.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (paymentReportResponseList != null &&
                    paymentReportResponseList.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(this, getResources().getString(R.string.PPCtoMiller),
                        paymentReportResponseList.getResponseMessage(), getSupportFragmentManager());
            }else if (paymentReportResponseList != null &&
                    paymentReportResponseList.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(paymentReportResponseList.getResponseMessage(),
                        this, getSupportFragmentManager(), true);

            } else {
                Utils.customAlert(TCDetailReportActivity.this,
                        getResources().getString(R.string.nav_reports),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(TCDetailReportActivity.this,
                    getResources().getString(R.string.nav_reports),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();

    }

    @Override
    public void getTokensResponseData(GetTokensResponse getTokensFarmerResponse) {

    }

    @Override
    public void getTransactionsReportData(TransactionReportResponseList transactionReportResponseList) {

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
            Utils.customAlert(this,
                    getResources().getString(R.string.nav_reports),
                    msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else if (code == AppConstants.EXCEPTION_CODE) {
            Utils.customAlert(this,
                    getResources().getString(R.string.nav_reports),
                    getString(R.string.something) +
                            msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else {
            Utils.customAlert(this,
                    getResources().getString(R.string.nav_reports),
                    getString(R.string.something),
                    getResources().getString(R.string.ERROR),
                    false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            boolean isAutomatic = Utils.isTimeAutomatic(this);
            if (!isAutomatic) {
                Utils.customTimeAlert(this,
                        getResources().getString(R.string.nav_reports),
                        getString(R.string.date_time));
                return;
            }

            currentDate = Utils.getCurrentDate();
            cacheDate = sharedPreferences.getString(AppConstants.CACHE_DATE, "");
            if (!TextUtils.isEmpty(cacheDate) && cacheDate != null) {
                if (!cacheDate.equalsIgnoreCase(currentDate)) {
                    Utils.ShowDeviceSessionAlert(this,
                            getResources().getString(R.string.nav_reports),
                            getString(R.string.session_expire),
                            getSupportFragmentManager());
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            cacheDate = currentDate;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(AppConstants.CACHE_DATE, cacheDate);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (customProgressDialog != null) {
                customProgressDialog = null;
            }
            if (reportPresenter != null) {
                reportPresenter.detachView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

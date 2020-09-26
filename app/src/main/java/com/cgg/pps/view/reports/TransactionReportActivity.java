package com.cgg.pps.view.reports;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;

import com.cgg.pps.R;
import com.cgg.pps.adapter.TransactionReportAdapter;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.TransactionDetailReportBinding;
import com.cgg.pps.model.response.report.transaction.TransactionReportResponse;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

public class TransactionReportActivity extends AppCompatActivity {
    private TransactionReportResponse transactionReportResponse;
    private TransactionReportAdapter transactionReportAdapter;
    private TransactionDetailReportBinding binding;
    private PPCUserDetails ppcUserDetails;
    private String cacheDate, currentDate;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.transaction_detail_report);
        sharedPreferences = OPMSApplication.get(this).getPreferences();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Truck Chit Transaction Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getPreferenceData();
        getTCCompleteData();

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    private void getTCCompleteData() {
        try {
            Intent intent = getIntent();
            transactionReportResponse = (TransactionReportResponse) intent.getSerializableExtra("TRANSACTION_DATA");
            if (transactionReportResponse == null) {
                Utils.customAlert(TransactionReportActivity.this,
                        getResources().getString(R.string.nav_reports),
                        getResources().getString(R.string.something),
                        getString(R.string.ERROR), false);
            } else {
                binCompleteData(transactionReportResponse);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void binCompleteData(TransactionReportResponse transactionReportResponse) {
        try {
            binding.tknIDTV.setText(transactionReportResponse.getTokenID());
            binding.tknNoTV.setText(transactionReportResponse.getTokenNo());
            binding.regIDTV.setText(transactionReportResponse.getRegID());
            binding.regNoTV.setText(transactionReportResponse.getRegistrationNo());
            binding.farmerNameTV.setText(transactionReportResponse.getFarmerName());
            binding.fatherNameTV.setText(transactionReportResponse.getFarmerFatherName());
            binding.mobTV.setText(transactionReportResponse.getMobile());
            binding.bankAccNo.setText(transactionReportResponse.getBankAccountNo());
            binding.nameTV.setText(transactionReportResponse.getNameasperBank());
            binding.bankTV.setText(transactionReportResponse.getBankName());
            binding.branchTV.setText(transactionReportResponse.getBranchName());
            binding.ifscTV.setText(transactionReportResponse.getIFSCCode());
            binding.paddyTypeTV.setText(transactionReportResponse.getPaddyType());
            binding.newBagsTV.setText(transactionReportResponse.getNewBags());
            binding.recOldBagsTV.setText(transactionReportResponse.getOldBags());
            binding.recTotBagsTV.setText(transactionReportResponse.getTotalBags());
            binding.trcNewBagsTV.setText(transactionReportResponse.getTRNewBags());
            binding.trcOLdBagsTV.setText(transactionReportResponse.getTROldBags());
            binding.trcTotBagsTV.setText(transactionReportResponse.getTRTotalBags());

            binding.mspAmountTV.setText(transactionReportResponse.getMSPAmount());
            binding.totAmtTV.setText(transactionReportResponse.getTotalAmount());
            binding.trnDateTV.setText(transactionReportResponse.getTransactionDate());
            binding.transactionIDTV.setText(transactionReportResponse.getFarmerTransactionID());
            binding.bankProDateTV.setText(transactionReportResponse.getBankProcessedDate());
            binding.bankRefTV.setText(transactionReportResponse.getBankRefNo());
            binding.statusTV.setText(transactionReportResponse.getStatus());
            binding.totalQtyTV.setText(transactionReportResponse.getTotalQuantity());
            binding.truckchitDateTV.setText(transactionReportResponse.getTruckChitDate());
            binding.onlineTruckChitNumberTV.setText(""+transactionReportResponse.getOnlineTcNo());
            binding.manualTruckchitNumberTV.setText(""+transactionReportResponse.getManTcNo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPreferenceData() {
        try {
            Gson gson = OPMSApplication.get(TransactionReportActivity.this).getGson();
            SharedPreferences sharedPreferences = OPMSApplication.get(TransactionReportActivity.this).getPreferences();
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
}

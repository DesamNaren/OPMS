package com.cgg.pps.view.reports;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.ActivityPaddyPaymentReportBinding;
import com.cgg.pps.model.response.report.payment_report.PaddyPaymentOutput;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.Utils;

public class PaddyPaymentReportActivity extends AppCompatActivity {
    private PaddyPaymentOutput paddyPaymentOutput;
    private ActivityPaddyPaymentReportBinding binding;
    private String cacheDate, currentDate;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_paddy_payment_report);
        sharedPreferences = OPMSApplication.get(this).getPreferences();
        Toolbar toolbar = findViewById(R.id.toolbar);
        try {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Paddy Payment Status Report");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getPaymentData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void getPaymentData() {
        try {
            Intent intent = getIntent();
            paddyPaymentOutput = (PaddyPaymentOutput) intent.getSerializableExtra("PADDY_PAYMENT_DATA");

            if (paddyPaymentOutput == null) {
                Utils.customAlert(PaddyPaymentReportActivity.this,
                        getResources().getString(R.string.nav_reports),
                        getResources().getString(R.string.something),
                        getString(R.string.ERROR), false);
            } else {
                binCompleteData(paddyPaymentOutput);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void binCompleteData(PaddyPaymentOutput paddyPaymentOutput) {
        try {
            binding.regNoTV.setText(paddyPaymentOutput.getRegistrationNo());
            binding.txnIDTV.setText(paddyPaymentOutput.getRegistrationNo1());
            binding.farmerNameTV.setText(paddyPaymentOutput.getFarmerName());

            binding.bankAccNo.setText(paddyPaymentOutput.getBankAccountNo());
            binding.bankTV.setText(paddyPaymentOutput.getBankName());
            binding.branchTV.setText(paddyPaymentOutput.getBranchName());
            binding.nameAs.setText(paddyPaymentOutput.getNameasperBank());

            binding.prcDateTV.setText(paddyPaymentOutput.getTransactionDate());
            binding.tcDateTV.setText(paddyPaymentOutput.getTruckChitDate());
            binding.paymentDateTV.setText(paddyPaymentOutput.getPaymentDate());

            binding.newBagsTV.setText(String.valueOf(paddyPaymentOutput.getNewBags()));
            binding.oldBagsTV.setText(String.valueOf(paddyPaymentOutput.getOldBags()));
            binding.totBagsTV.setText(String.valueOf(paddyPaymentOutput.getTotalBags()));
            binding.graQtyTV.setText(String.valueOf(paddyPaymentOutput.getaQUANTITY()));
            binding.comQtyTV.setText(String.valueOf(paddyPaymentOutput.getcQUANTITY()));
            binding.totQtyTv.setText(String.valueOf(paddyPaymentOutput.getTotalQuantity()));

            binding.totAmtTV.setText(String.valueOf(paddyPaymentOutput.getTotalAmount()));
            binding.curStatusTV.setText(paddyPaymentOutput.getAmountTransferStatus());

        } catch (Exception e) {
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

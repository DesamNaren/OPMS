package com.cgg.pps.view.reports;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.ActivityPaymentWiseDetailReportBinding;
import com.cgg.pps.model.response.report.payment.PaymentReportResponse;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.Utils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

public class PaymentDetailReportActivity extends AppCompatActivity {
    private PaymentReportResponse paymentReportResponse;
    private ActivityPaymentWiseDetailReportBinding binding;
    private String cacheDate, currentDate;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_wise_detail_report);
        sharedPreferences = OPMSApplication.get(this).getPreferences();
        Toolbar toolbar = findViewById(R.id.toolbar);
        try {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Payment Status Report");
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
            paymentReportResponse = (PaymentReportResponse) intent.getSerializableExtra("PAYMENT_DATA");

            if (paymentReportResponse == null) {
                Utils.customAlert(PaymentDetailReportActivity.this,
                        getResources().getString(R.string.nav_reports),
                        getResources().getString(R.string.something),
                        getString(R.string.ERROR), false);
            } else {
                binCompleteData(paymentReportResponse);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void binCompleteData(PaymentReportResponse paymentReportResponse) {
        try {
            binding.regNoTV.setText(paymentReportResponse.getRegistrationNo());
            binding.txnIDTV.setText(paymentReportResponse.getFarmerTransactionID());
            binding.farmerNameTV.setText(paymentReportResponse.getFarmerName());

            binding.bankAccNo.setText(paymentReportResponse.getBankAccountNo());
            binding.bankTV.setText(paymentReportResponse.getBankName());
            binding.branchTV.setText(paymentReportResponse.getBranchName());
            binding.nameAs.setText(paymentReportResponse.getNameasperBank());
            binding.ifscTV.setText(paymentReportResponse.getIFSCCODE());

            binding.newBagsTV.setText(paymentReportResponse.getNewBags());
            binding.oldBagsTV.setText(paymentReportResponse.getOldBags());
            binding.totBagsTV.setText(paymentReportResponse.getTotalBags());
            binding.totQtyTv.setText(paymentReportResponse.getTotalQuantity());
            binding.totAmtTV.setText(paymentReportResponse.getTotalAmount());

            binding.txnDateTV.setText(paymentReportResponse.getTransactionDate());
            binding.curStatusTV.setText(paymentReportResponse.getAmountTransferStatus());
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

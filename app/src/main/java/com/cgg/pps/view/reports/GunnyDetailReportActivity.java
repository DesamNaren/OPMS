package com.cgg.pps.view.reports;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.ActivityGunnyWiseDetailReportBinding;
import com.cgg.pps.model.response.report.gunny.GunnyReportResponse;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.Utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

public class GunnyDetailReportActivity extends AppCompatActivity {
    private ActivityGunnyWiseDetailReportBinding binding;
    private String cacheDate, currentDate;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gunny_wise_detail_report);
        sharedPreferences = OPMSApplication.get(this).getPreferences();
        Toolbar toolbar = findViewById(R.id.toolbar);
        try {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.gunny_report));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getGunnyData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    private void getGunnyData() {
        try {
            Intent intent = getIntent();
            GunnyReportResponse gunnyReportResponse = (GunnyReportResponse) intent.getSerializableExtra("GUNNY_DATA");
            if (gunnyReportResponse == null) {
                Utils.customAlert(GunnyDetailReportActivity.this,
                        getResources().getString(R.string.nav_reports),
                        getResources().getString(R.string.something),
                        getString(R.string.ERROR), false);
            } else {
                binCompleteData(gunnyReportResponse);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void binCompleteData(GunnyReportResponse gunnyReportResponse) {

        try {
            binding.orderIDTV.setText(gunnyReportResponse.getORDERID());
            binding.srcTv.setText(gunnyReportResponse.getSourcetype());
            binding.recFrom.setText(gunnyReportResponse.getReceived());
            binding.manTrcID.setText(gunnyReportResponse.getManualGunniesTruckchitID());

            binding.tknNewBagsTV.setText(gunnyReportResponse.getTokenNewBags());
            binding.tknoldBagsTV.setText(gunnyReportResponse.getTokenOldBags());
            binding.tknTotBagsTV.setText(gunnyReportResponse.getTokenTotalBags());

            binding.ordNewBags.setText(gunnyReportResponse.getOrderNewBags());
            binding.ordOldBagsTV.setText(gunnyReportResponse.getOrderOldBags());
            binding.ordTotBagsTV.setText(gunnyReportResponse.getOrderTotalBags());

            binding.prcNewBagsTV.setText(gunnyReportResponse.getProcuredNewBags());
            binding.prcOldBagsTV.setText(gunnyReportResponse.getProcuredOldBags());
            binding.prcTotBagsTV.setText(gunnyReportResponse.getProcuredTotalBags());

            binding.canNewBagsTV.setText(gunnyReportResponse.getCancelledNewBags());
            binding.canOldBagsTV.setText(gunnyReportResponse.getCancelledOldBags());
            binding.canTotBagsTV.setText(gunnyReportResponse.getCancelledTotalBags());

            binding.avlNewBagsTV.setText(gunnyReportResponse.getAvailableNewBags());
            binding.avlOldBagsTV.setText(gunnyReportResponse.getAvailableOldBags());
            binding.avlTotBagsTV.setText(gunnyReportResponse.getAvailableTotalBags());

            binding.driNameTV.setText(gunnyReportResponse.getDriverName());
            binding.driMobTV.setText(gunnyReportResponse.getDriverMobileNo());
            binding.vehicleTV.setText(gunnyReportResponse.getVehicleNo());
            binding.vehicleTypeTV.setText(gunnyReportResponse.getVehicleType());
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
                            getResources().getString(R.string.nav_reports),getString(R.string.session_expire),
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

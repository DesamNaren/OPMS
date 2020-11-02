package com.cgg.pps.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.widget.Toast;

import com.cgg.pps.BuildConfig;
import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.ActivityDrpBinding;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.Utils;

public class DRPActivity extends AppCompatActivity {

    private ActivityDrpBinding binding;
    private String cacheDate, currentDate;
    private SharedPreferences sharedPreferences;
    private static final int REQUEST_READ_PHONE_STATE = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_drp);
        sharedPreferences = OPMSApplication.get(this).getPreferences();

        try {
            setSupportActionBar(binding.drpToolbar);
            getSupportActionBar().setTitle(getString(R.string.drp));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            binding.drpWV.loadUrl(BuildConfig.SERVER_NEW_URL);
            binding.drpWV.setWebChromeClient(new WebChromeClient());

            int permissionCheck = ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_PHONE_STATE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        REQUEST_READ_PHONE_STATE);
            } else {
                binding.deviceId.setText("Device ID: " + Utils.getDeviceID(this));
                binding.versionId.setText("Version : " + Utils.getVersionName(this));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_PHONE_STATE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                binding.deviceId.setText("Device ID: " + Utils.getDeviceID(this));
                binding.versionId.setText("Version : " + Utils.getVersionName(this));
            } else {
                Toast.makeText(this, getString(R.string.all_per), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onResume() {
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

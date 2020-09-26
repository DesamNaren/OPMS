package com.cgg.pps.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.PermissionActivityBinding;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.Utils;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class PermissionsActivity extends AppCompatActivity {

    PermissionActivityBinding binding;
    private boolean loginFlag;
    private String cacheDate, currentDate;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.permission_activity);


        try {
            sharedPreferences = OPMSApplication.get(this).getPreferences();
            loginFlag = sharedPreferences.getBoolean(AppConstants.LOGIN_FLAG, false);
        } catch (Exception e) {
            loginFlag = false;
            e.printStackTrace();
        }

        binding.accept.setOnClickListener(onBtnClick);
        binding.declined.setOnClickListener(onBtnClick);

    }

    View.OnClickListener onBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.accept:
                    callPermissions();
                    break;
                case R.id.declined:
                    if (loginFlag) {
                        startActivity(new Intent(PermissionsActivity.this, DashboardActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(PermissionsActivity.this, LoginActivity.class));
                        finish();
                    }
                    break;
            }
        }
    };

    void callPermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            if (loginFlag) {
                                startActivity(new Intent(PermissionsActivity.this, DashboardActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(PermissionsActivity.this, LoginActivity.class));
                                finish();
                            }

                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PermissionsActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isAutomatic = Utils.isTimeAutomatic(this);
        if (!isAutomatic) {
            Utils.customTimeAlert(this,
                    getResources().getString(R.string.app_name),
                    "Turn on automatic date/time feature");
            return;
        }

        currentDate = Utils.getCurrentDate();
        cacheDate = sharedPreferences.getString(AppConstants.CACHE_DATE, "");
        if (!TextUtils.isEmpty(cacheDate) && cacheDate != null) {
            if (!cacheDate.equalsIgnoreCase(currentDate)) {
                Utils.ShowSessionAlert(this,
                        getResources().getString(R.string.app_name),
                        "Session expired, Please Re-login",
                        getSupportFragmentManager());
            }
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        cacheDate = currentDate;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstants.CACHE_DATE, cacheDate);
        editor.commit();
    }
}

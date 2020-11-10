package com.cgg.pps.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.cgg.pps.BuildConfig;
import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.ActivityLoginBinding;
import com.cgg.pps.interfaces.DMVDeleteInterface;
import com.cgg.pps.interfaces.LoginInterface;
import com.cgg.pps.model.request.devicemapping.DeviceMappingRequest;
import com.cgg.pps.model.request.truckchit.vehicledetails.VehicleDetailsRequest;
import com.cgg.pps.model.request.validateuser.login.ValidateUserRequest;
import com.cgg.pps.model.response.devicemapping.DeviceMappingResponse;
import com.cgg.pps.model.response.validateuser.login.ValidateUserResponse;
import com.cgg.pps.presenter.LoginPresenter;
import com.cgg.pps.room.repository.DMVRepository;
import com.cgg.pps.room.repository.VehicleRepository;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.ConnectionDetector;
import com.cgg.pps.util.CustomProgressDialog;
import com.cgg.pps.util.Utils;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.gson.Gson;

public class LoginActivity extends LocBaseActivity implements LoginInterface, DMVDeleteInterface {
    private ActivityLoginBinding binding;
    private LoginPresenter loginPresenter;
    private static final int REQUEST_READ_PHONE_STATE = 2000;
    private String deviceID, versionID;
    CustomProgressDialog customProgressDialog;

    private String cacheDate, currentDate;
    private SharedPreferences sharedPreferences;
    private DMVRepository dmvRepository;
    private VehicleRepository vehicleRepository;
    ValidateUserResponse validateUserResponseMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        dmvRepository = new DMVRepository(LoginActivity.this);
        vehicleRepository = new VehicleRepository(LoginActivity.this);

        sharedPreferences = OPMSApplication.get(this).getPreferences();
        binding.loginBtn.setOnClickListener(onBtnClick);
        customProgressDialog = new CustomProgressDialog(this);
        loginPresenter = new LoginPresenter();
        loginPresenter.attachView(this);


        int permissionCheck = ContextCompat.checkSelfPermission(
                LoginActivity.this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_READ_PHONE_STATE);
        } else {
            deviceID = Utils.getDeviceID(LoginActivity.this);
            versionID = Utils.getVersionName(LoginActivity.this);
            binding.deviceId.setText("Device ID: " + deviceID);
            binding.versionId.setText("Version : " + versionID);
        }

        try {
            String env = BuildConfig.SERVER_NEW_URL;
            if (env != null) {
                if (env.contains("devopms")) {
                    binding.envTV.setText("Env: Dev");
                } else if (env.contains("testpps")) {
                    binding.envTV.setText("Env: Test");

                } else if (env.contains("demopps")) {
                    binding.envTV.setText("Env: UAT");

                } else if (env.contains("staging")) {
                    binding.envTV.setText("Env: Stage");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utils.hideKeyboard(view);
                return false;
            }
        });

    }

    View.OnClickListener onBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.loginBtn) {
                if (ConnectionDetector.isConnectedToInternet(LoginActivity.this)) {
                    if (validateData()) {
                        if (mCurrentLocation != null && mCurrentLocation.getLatitude() > 0 && mCurrentLocation.getLongitude() > 0) {

                            ValidateUserRequest validateUserRequest = new ValidateUserRequest();
                            validateUserRequest.setUserID(binding.username.getText().toString().trim());
                            validateUserRequest.setPassword(binding.password.getText().toString().trim());
                            validateUserRequest.setDeviceID(deviceID);
                            validateUserRequest.setVersionID(versionID);
                            validateUserRequest.setLatLong(mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude());
                            if (customProgressDialog != null)
                                customProgressDialog.show();
                            loginPresenter.validateUser(validateUserRequest);
                        } else {
                            ValidateUserRequest validateUserRequest = new ValidateUserRequest();
                            validateUserRequest.setUserID(binding.username.getText().toString().trim());
                            validateUserRequest.setPassword(binding.password.getText().toString().trim());
                            validateUserRequest.setDeviceID(deviceID);
                            validateUserRequest.setVersionID(versionID);
                            validateUserRequest.setLatLong(0 + "," + 0);
                            if (customProgressDialog != null)
                                customProgressDialog.show();
                            loginPresenter.validateUser(validateUserRequest);
//                            Toast.makeText(LoginActivity.this, "Logging in...", Toast.LENGTH_SHORT).show();
//                            Utils.customAlert(LoginActivity.this,
//                                    getResources().getString(R.string.login),
//                                    getResources().getString(R.string.something_loc),
//                                    getString(R.string.WARNING), false);
                        }
                    }
                } else {
                    Utils.customAlert(LoginActivity.this,
                            getResources().getString(R.string.login),
                            getResources().getString(R.string.no_internet),
                            getResources().getString(R.string.WARNING), false);
                }
            }
        }
    };


    @Override
    public void getLoginResponse(ValidateUserResponse validateUserResponse) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();

        if (validateUserResponse != null && validateUserResponse.getStatusCode() != null
                && validateUserResponse.getStatusCode() == AppConstants.SUCCESS_CODE &&
                validateUserResponse.getPPCUserDetails() != null) {
            validateUserResponseMain = validateUserResponse;

            String lastPPCCode = sharedPreferences.getString(AppConstants.LOGGED_PPC_CODE, "");
            if (TextUtils.isEmpty(lastPPCCode)) {
                navDashBoard(validateUserResponse);
            } else if (!lastPPCCode.equalsIgnoreCase(validateUserResponseMain.getPPCUserDetails().getPPCCode())) {
                deleteDMVData();
            } else {
                navDashBoard(validateUserResponse);
            }
        } else if (validateUserResponse != null && validateUserResponse.getStatusCode() != null
                && validateUserResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {

            Utils.customAlert(LoginActivity.this,
                    getResources().getString(R.string.login),
                    validateUserResponse.getResponseMessage(),
                    getString(R.string.ERROR), false);

        } else if (validateUserResponse != null && validateUserResponse.getStatusCode() != null
                && validateUserResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
            Utils.customAlert(LoginActivity.this,
                    getResources().getString(R.string.login),
                    validateUserResponse.getResponseMessage(),
                    getString(R.string.ERROR), false);
        } else if (validateUserResponse != null && validateUserResponse.getStatusCode() != null
                && validateUserResponse.getStatusCode() == AppConstants.SESSION_CODE) {
            Utils.customAlert(LoginActivity.this,
                    getResources().getString(R.string.login),
                    validateUserResponse.getResponseMessage(),
                    getString(R.string.ERROR), false);
        } else if (validateUserResponse != null && validateUserResponse.getStatusCode() != null
                &&
                validateUserResponse.getStatusCode() == AppConstants.VERSION_CODE) {
            Utils.callPlayAlert(validateUserResponse.getResponseMessage(),
                    this, getSupportFragmentManager(), true);
        } else if (validateUserResponse != null && validateUserResponse.getStatusCode() != null
                && validateUserResponse.getStatusCode() == AppConstants.DEVICE_MAPPING_CODE) {

            customDeviceRegisterAlert(LoginActivity.this,
                    getResources().getString(R.string.login),
                    validateUserResponse.getResponseMessage(),
                    getString(R.string.ERROR));

        }  else {
            Utils.customAlert(LoginActivity.this,
                    getResources().getString(R.string.login),
                    getResources().getString(R.string.server_not),
                    getString(R.string.ERROR), false);
        }

    }

    @Override
    public void getDeviceMappingResponse(DeviceMappingResponse deviceMappingResponse) {

        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();

        if (deviceMappingResponse != null && deviceMappingResponse.getStatusCode() != null
                && deviceMappingResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {

            Utils.customAlert(LoginActivity.this,
                    getResources().getString(R.string.login),
                    deviceMappingResponse.getResponseMessage(),
                    getString(R.string.SUCCESS), false);

        } else if (deviceMappingResponse != null && deviceMappingResponse.getStatusCode() != null
                && deviceMappingResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {

            Utils.customAlert(LoginActivity.this,
                    getResources().getString(R.string.login),
                    deviceMappingResponse.getResponseMessage(),
                    getString(R.string.ERROR), false);

        } else if (deviceMappingResponse != null && deviceMappingResponse.getStatusCode() != null
                && deviceMappingResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
            Utils.customAlert(LoginActivity.this,
                    getResources().getString(R.string.login),
                    deviceMappingResponse.getResponseMessage(),
                    getString(R.string.ERROR), false);
        } else if (deviceMappingResponse != null && deviceMappingResponse.getStatusCode() != null
                && deviceMappingResponse.getStatusCode() == AppConstants.SESSION_CODE) {
            Utils.customAlert(LoginActivity.this,
                    getResources().getString(R.string.login),
                    deviceMappingResponse.getResponseMessage(),
                    getString(R.string.ERROR), false);
        } else if (deviceMappingResponse != null && deviceMappingResponse.getStatusCode() != null
                &&
                deviceMappingResponse.getStatusCode() == AppConstants.VERSION_CODE) {
            Utils.callPlayAlert(deviceMappingResponse.getResponseMessage(),
                    this, getSupportFragmentManager(), true);
        } else if (deviceMappingResponse != null && deviceMappingResponse.getStatusCode() != null
                && deviceMappingResponse.getStatusCode() == AppConstants.DEVICE_MAPPING_CODE) {

            Utils.customAlert(LoginActivity.this,
                    getResources().getString(R.string.login),
                    deviceMappingResponse.getResponseMessage(),
                    getString(R.string.ERROR), false);

        }else if (deviceMappingResponse != null && deviceMappingResponse.getStatusCode() != null
                && deviceMappingResponse.getStatusCode() == AppConstants.DEVICE_MAP_SUCCESS) {

            Utils.customAlert(LoginActivity.this,
                    getResources().getString(R.string.login),
                    deviceMappingResponse.getResponseMessage(),
                    getString(R.string.SUCCESS), false);

        } else if (deviceMappingResponse != null && deviceMappingResponse.getStatusCode() != null
                && deviceMappingResponse.getStatusCode() == AppConstants.DEVICE_MAP_EXIST) {

            Utils.customAlert(LoginActivity.this,
                    getResources().getString(R.string.login),
                    deviceMappingResponse.getResponseMessage(),
                    getString(R.string.INFORMATION), false);

        } else {
            Utils.customAlert(LoginActivity.this,
                    getResources().getString(R.string.login),
                    getResources().getString(R.string.server_not),
                    getString(R.string.ERROR), false);
        }
    }

    private void navDashBoard(ValidateUserResponse validateUserResponse) {
        SharedPreferences.Editor editor = OPMSApplication.get(this).getPreferencesEditor();
        Gson gson = OPMSApplication.get(this).getGson();

        editor.putString(AppConstants.PPC_DATA, gson.toJson(validateUserResponseMain.getPPCUserDetails()));
        editor.putString(AppConstants.LOGGED_PPC_CODE, validateUserResponse.getPPCUserDetails().getPPCCode());
        String userPwd = binding.password.getText().toString().trim();
        editor.putString(AppConstants.USER_PWD, userPwd);
        editor.putBoolean(AppConstants.LOGIN_FLAG, true);
        editor.commit();
        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
        finish();
    }

    private void deleteDMVData() {
        vehicleRepository.deleteAllVehicles(this);
    }

    private void CallVehicleAPI(ValidateUserResponse validateUserResponse) {
        if (ConnectionDetector.isConnectedToInternet(LoginActivity.this)) {
            if (customProgressDialog != null)
                customProgressDialog.show();
            VehicleDetailsRequest vehicleDetailsRequest = new VehicleDetailsRequest();
            vehicleDetailsRequest.setPPCID(validateUserResponse.getPPCUserDetails().getPPCID());
            vehicleDetailsRequest.setAuthenticationID(validateUserResponse.getPPCUserDetails().getAuthenticationID());
            // nav dashboard
        } else {
            Utils.customAlert(LoginActivity.this,
                    getResources().getString(R.string.login),
                    getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.WARNING), false);
        }
    }


    private boolean validateData() {
        String UserName = binding.username.getText().toString().toUpperCase();
        String Password = binding.password.getText().toString();
        if (TextUtils.isEmpty(UserName)) {
            binding.username.setError(getString(R.string.plz_ent_user_name));
            binding.username.requestFocus();
            return false;
        }

        if (!(UserName.length() >= 9)) {
            binding.username.setError(getString(R.string.val_user_name));
            binding.username.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(Password)) {
            binding.password.setError(getString(R.string.plz_ent_pwd));
            binding.password.requestFocus();
            return false;
        }
        return true;

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
            Utils.customAlert(LoginActivity.this,
                    getResources().getString(R.string.login),
                    msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else if (code == AppConstants.EXCEPTION_CODE) {
            Utils.customAlert(LoginActivity.this,
                    getResources().getString(R.string.login),
                    getString(R.string.something) +
                            msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else {
            Utils.customAlert(LoginActivity.this,
                    getResources().getString(R.string.login),
                    getString(R.string.something),
                    getResources().getString(R.string.ERROR),
                    false);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
            }
        };

        boolean isAutomatic = Utils.isTimeAutomatic(this);
        if (!isAutomatic) {
            Utils.customTimeAlert(this,
                    getResources().getString(R.string.login),
                    "Turn on automatic date/time feature");
            return;
        }

        currentDate = Utils.getCurrentDate();
        cacheDate = sharedPreferences.getString(AppConstants.CACHE_DATE, "");
        if (!TextUtils.isEmpty(cacheDate)) {
            if (!cacheDate.equalsIgnoreCase(currentDate)) {
                Utils.ShowDeviceSessionAlert(this,
                        getResources().getString(R.string.login),
                        getString(R.string.ses_expire_re),
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

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mGpsSwitchStateReceiver);
    }

    private BroadcastReceiver mGpsSwitchStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                    callPermissions();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                    deviceID = Utils.getDeviceID(this);
                    binding.deviceId.setText("Device ID: " + deviceID);
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (customProgressDialog != null) {
                customProgressDialog = null;
            }
            if (loginPresenter != null) {
                loginPresenter.detachView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataNotAvailable() {

    }

    @Override
    public void distCount(int cnt) {
        if (cnt <= 0) {
            navDashBoard(validateUserResponseMain);
        }
    }

    @Override
    public void vehCount(int cnt) {
        dmvRepository.deleteDMVAsyncTask(this);
    }


    //========== Custom alert for device mapping ================
    private void customDeviceRegisterAlert(Activity activity, String title,
                                           String msg, String type) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                if (type.equalsIgnoreCase(activity.getString(R.string.SUCCESS))) {
                    dialog.setContentView(R.layout.custom_alert_success);
                } else if (type.equalsIgnoreCase(activity.getString(R.string.ERROR))) {
                    dialog.setContentView(R.layout.custom_alert_register);
                } else if (type.equalsIgnoreCase(activity.getString(R.string.WARNING))) {
                    dialog.setContentView(R.layout.custom_alert_warning);
                } else {
                    dialog.setContentView(R.layout.custom_alert_information);
                }
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(activity.getResources().getString(R.string.app_name) + ": " + title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(msg);
                Button yes = dialog.findViewById(R.id.btDialogYes);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        callDeviceMapping();
                    }
                });
                Button no = dialog.findViewById(R.id.btDialogNo);
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if (!dialog.isShowing())
                    dialog.show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void callDeviceMapping() {
        if (ConnectionDetector.isConnectedToInternet(LoginActivity.this)) {
            DeviceMappingRequest deviceMappingRequest = new DeviceMappingRequest();
            deviceMappingRequest.setDeviceId(Utils.getDeviceID(this));
            deviceMappingRequest.setPpcCode(binding.username.getText().toString());
            if (customProgressDialog != null)
                customProgressDialog.show();
            loginPresenter.deviceMapping(deviceMappingRequest);

        } else {
            Utils.customAlert(LoginActivity.this,
                    getResources().getString(R.string.login),
                    getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.WARNING), false);
        }
    }
}

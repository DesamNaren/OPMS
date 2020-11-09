package com.cgg.pps.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cgg.pps.BuildConfig;
import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.fragment.ConfigFragment;
import com.cgg.pps.fragment.DashboardFragment;
import com.cgg.pps.fragment.DeviceMgmtFragment;
import com.cgg.pps.fragment.LogoutFragment;
import com.cgg.pps.fragment.MappedPPCFragment;
import com.cgg.pps.fragment.PaddyProcurementFragment;
import com.cgg.pps.fragment.RePrintFragment;
import com.cgg.pps.view.LoginActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;

public class Utils {

    public static String getDeviceID(Context context) {
        String deviceID = "";
        try {
            deviceID = android.provider.Settings.Secure.getString(
                    context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceID;
    }

    public static void callPlayAlert(String msg, Activity activity, FragmentManager fragmentManager, boolean flag) {
        try {
            if (flag) {
                ShowPlayAlert(activity,
                        activity.getResources().getString(R.string.DataSync),
                        msg, fragmentManager, flag);
            } else {
                Bundle bundle = new Bundle();
                bundle.putBoolean(AppConstants.PLAY_FLAG, flag);
                bundle.putString(AppConstants.PLAY_FLAG_MSG, msg);

                AppConstants.FARG_TAG = LogoutFragment.class.getSimpleName();
                LogoutFragment logoutFragment = new LogoutFragment();
                logoutFragment.setArguments(bundle);
                callFragment(logoutFragment, AppConstants.FARG_TAG, fragmentManager);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void callFragment(Fragment fragment, String name, FragmentManager fragmentManager) {
        try {

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.dashboard_container, fragment, name);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getTransportDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(c.getTime());
    }

    public static String getVersionName(Context context) {
        String version;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "";
            e.printStackTrace();
        }
        return version;
    }

    public static boolean ValidateAadharNumber(String aadharNumber) {
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = false;
        try {
            isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
            if (isValidAadhar) {
                isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValidAadhar;
    }

    public static boolean ValidateVehicleNumber(String vehNum) {
        String pattern = "^[a-zA-z]{2}[0-9]{2}[a-zA-z]{2}[0-9]{4}$";
        Pattern vehPattern = Pattern.compile(pattern);
        boolean isValidVeh = false;
        try {
            isValidVeh = vehPattern.matcher(vehNum).matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValidVeh;
    }

    public static boolean ValidateVehicleNumberFirst2Digit(String vehNum) {
        String pattern = "^[a-zA-z]{2}$";
        Pattern vehPattern = Pattern.compile(pattern);
        boolean isValidVeh = false;
        try {
            isValidVeh = vehPattern.matcher(vehNum).matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValidVeh;
    }

    public static boolean ValidateVehicleContainDigit(String vehNum) {
        String pattern = "\\d+";
        Pattern vehPattern = Pattern.compile(pattern);
        boolean isValidVeh = false;
        try {
            isValidVeh = vehPattern.matcher(vehNum).matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValidVeh;
    }

    public static boolean ValidateVehicleNumber6Digit(String vehNum) {
        String pattern = "^[a-zA-z]{2}[0-9]{4}$";
        Pattern vehPattern = Pattern.compile(pattern);
        boolean isValidVeh = false;
        try {
            isValidVeh = vehPattern.matcher(vehNum).matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValidVeh;
    }

    public static void openSettings(Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
//            activity.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ShowPrintAlert(Activity activity,
                                      FragmentManager fragmentManager,
                                      CustomProgressDialog customProgressDialog) {
        try {
            Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.setContentView(R.layout.bt_connect_alert);
                dialog.setCancelable(false);
                ImageView iv = dialog.findViewById(R.id.cancel);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                Button pairBtn = dialog.findViewById(R.id.btDialogPair);
                Button notNowBtn = dialog.findViewById(R.id.btDialogNo);
                Button ignoreBtn = dialog.findViewById(R.id.btDialogOK);

                CheckBox neverCB = dialog.findViewById(R.id.never);
                neverCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (compoundButton.isChecked()) {
                            ignoreBtn.setVisibility(View.VISIBLE);
                            pairBtn.setVisibility(View.GONE);
                            notNowBtn.setVisibility(View.GONE);
                        } else {
                            ignoreBtn.setVisibility(View.GONE);
                            pairBtn.setVisibility(View.VISIBLE);
                            notNowBtn.setVisibility(View.VISIBLE);
                        }
                    }
                });

                pairBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        if (customProgressDialog != null && customProgressDialog.isShowing())
                            customProgressDialog.dismiss();

                        Fragment fragment = new ConfigFragment();
                        String fragmentTag = ConfigFragment.class.getSimpleName();
                        callFragment(fragment, fragmentManager, fragmentTag);
                    }
                });

                notNowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                    }
                });

                ignoreBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        SharedPreferences.Editor editor = OPMSApplication.get(activity).getPreferencesEditor();
                        editor.putBoolean(AppConstants.PRINTER_CON_FLAG, false);
                        editor.commit();
                        Toast.makeText(activity, activity.getString(R.string.thank_you), Toast.LENGTH_SHORT).show();
                    }
                });

                if (!dialog.isShowing())
                    dialog.show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(View v) {
        try {
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadSpinnerSetSelectedItem(Context context, List<String> list, Spinner spinner, String selection) {
        try {
            if (list.size() > 0) {
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerArrayAdapter);
                spinner.setSelection(spinnerArrayAdapter.getPosition(selection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadAutoCompleteTextData(Context context, List<String> list, AutoCompleteTextView spinner) {
        try {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item, list);
            spinner.setThreshold(1);
            spinner.setAdapter(spinnerArrayAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadAutoCompleteTextDataItem
            (Context context, List<String> list, AutoCompleteTextView spinner, String name) {
        try {
            if (list.size() > 0) {
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item, list);
                spinner.setThreshold(1);
                spinner.setAdapter(spinnerArrayAdapter);
                spinner.setText(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAlertNavigateToMasterDownloads(Activity activity,
                                                          final FragmentManager fragmentManager,
                                                          String title,
                                                          String alertMsg, final boolean goBack) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_alert_information);
                dialog.setCancelable(false);
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(activity.getResources().getString(R.string.app_name) + ": " + title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(alertMsg);
                Button yes = dialog.findViewById(R.id.btDialogYes);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (goBack) {
                            Fragment fragment = new DeviceMgmtFragment();
                            String fragmentTag = DeviceMgmtFragment.class.getSimpleName();
                            callFragment(fragment, fragmentManager, fragmentTag);
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

    public static void showAlertNavigateToFarmer(Activity activity,
                                                 Fragment fragment,
                                                 final FragmentManager fragmentManager,
                                                 String title,
                                                 String alertMsg, final boolean goBack) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_alert_success);
                dialog.setCancelable(false);

                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(activity.getResources().getString(R.string.app_name) + ": " + title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(alertMsg);

                Button yes = dialog.findViewById(R.id.btDialogYes);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (goBack) {
                            AppConstants.FARG_TAG = DashboardFragment.class.getSimpleName();
                            callFragment(fragment, fragmentManager, AppConstants.FARG_TAG);

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

    public static void showAlertNavigateToFarmer1(Activity activity,
                                                  Fragment fragment,
                                                  final FragmentManager fragmentManager,
                                                  String title,
                                                  String alertMsg, final boolean goBack) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
            dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.custom_alert_success);
            dialog.setCancelable(false);

            TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
            dialogTitle.setText(activity.getResources().getString(R.string.app_name) + ": " + title);
            TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
            dialogMessage.setText(alertMsg);

            Button yes = dialog.findViewById(R.id.btDialogYes);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (goBack) {
                        AppConstants.FARG_TAG = fragment.getClass().getSimpleName();
                        callFragment(fragment, fragmentManager, AppConstants.FARG_TAG);
                    }
                }
            });
            if (!dialog.isShowing())
                dialog.show();
        }
    }

    private static void callFragment(Fragment fragment, FragmentManager fragmentManager, String name) {
        AppConstants.FARG_TAG = name;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.dashboard_container, fragment, AppConstants.FARG_TAG);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public static String getCurrentDate() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        return df.format(cal.getTime());
    }

    public static boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return Settings.System.getInt(c.getContentResolver(), Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    public static String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date());
    }

    static String getPdfTime() {
        return new SimpleDateFormat("yyyyMMdd hhmmss", Locale.getDefault()).format(new Date());
    }

    public static String getCurrentYear() {
        return new SimpleDateFormat("yy", Locale.getDefault()).format(new Date());
    }

    public static String getFarmerAppointmentDateToPass(String appDate) {
        String[] strings = appDate.split("-");
        final Calendar c = Calendar.getInstance();
        c.set(Integer.valueOf(strings[0]), Integer.valueOf(strings[1]) - 1, Integer.valueOf(strings[2]));
        Date date = c.getTime();
        return new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(date);
    }

    public static String getFaqRejectedDate(String actDate) {
        String[] strings = actDate.split("-");
        final Calendar c = Calendar.getInstance();
        c.set(Integer.valueOf(strings[0]), Integer.valueOf(strings[1]) - 1, Integer.valueOf(strings[2]));
        Date date = c.getTime();
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date);
    }

    public static String getLocalIpAddress() {
        return null;
    }

    public static void loadSpinnerData(Context context, ArrayList<String> arrayList, Spinner spinner) {
        try {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arrayList);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //========== Custom alert for success,error and warning ================
    public static void customAlert(Activity activity, String title, String msg, String type, boolean flag) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                if (type.equalsIgnoreCase(activity.getString(R.string.SUCCESS))) {
                    dialog.setContentView(R.layout.custom_alert_success);
                } else if (type.equalsIgnoreCase(activity.getString(R.string.ERROR))) {
                    dialog.setContentView(R.layout.custom_alert_error);
                } else if (type.equalsIgnoreCase(activity.getString(R.string.WARNING))) {
                    dialog.setContentView(R.layout.custom_alert_warning);
                } else {
                    dialog.setContentView(R.layout.custom_alert_information);
                }
                dialog.setCancelable(false);
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(activity.getResources().getString(R.string.app_name) + ": " + title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(msg);
                Button yes = dialog.findViewById(R.id.btDialogYes);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (flag)
                            activity.finish();
                    }
                });
                if (!dialog.isShowing())
                    dialog.show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    //========== Custom alert for success,error and warning ================
    public static void customAlert(Context context, String title, String msg, String type, boolean flag) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                if (type.equalsIgnoreCase(context.getString(R.string.SUCCESS))) {
                    dialog.setContentView(R.layout.custom_alert_success);
                } else if (type.equalsIgnoreCase(context.getString(R.string.ERROR))) {
                    dialog.setContentView(R.layout.custom_alert_error);
                } else if (type.equalsIgnoreCase(context.getString(R.string.WARNING))) {
                    dialog.setContentView(R.layout.custom_alert_warning);
                } else {
                    dialog.setContentView(R.layout.custom_alert_information);
                }
                dialog.setCancelable(false);
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(context.getString(R.string.app_name) + ": " + title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(msg);
                Button yes = dialog.findViewById(R.id.btDialogYes);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (flag)
                            ((Activity) context).finish();
                    }
                });
                if (!dialog.isShowing())
                    dialog.show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void customPrintFailedAlert(Activity activity,
                                              String srcFrag,
                                              FragmentManager fragmentManager,
                                              String title,
                                              String msg) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_alert_information);
                dialog.setCancelable(false);
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(activity.getResources().getString(R.string.app_name) + ": " + title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(msg);
                Button yes = dialog.findViewById(R.id.btDialogYes);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        try {
                            Fragment fragment = new DashboardFragment();
                            AppConstants.FARG_TAG = srcFrag;
                            callFragment(fragment, fragmentManager, AppConstants.FARG_TAG);
                        } catch (Exception e) {
                            e.printStackTrace();
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

    public static void customNavigateAlert(Activity activity, String title, String msg, FragmentManager fragmentManager) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_alert_error);
                dialog.setCancelable(false);
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(activity.getResources().getString(R.string.app_name) + ": " + title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(msg);
                Button yes = dialog.findViewById(R.id.btDialogYes);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        try {
                            Fragment fragment = new DashboardFragment();
                            AppConstants.FARG_TAG = PaddyProcurementFragment.class.getSimpleName();
                            callFragment(fragment, fragmentManager, AppConstants.FARG_TAG);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(activity, activity.getResources().getString(R.string.plz_take_reprint), Toast.LENGTH_LONG).show();
                    }
                });
                if (!dialog.isShowing())
                    dialog.show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void customExitAlert(Activity activity, String title, String msg) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_alert_exit);
                dialog.setCancelable(false);
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(activity.getResources().getString(R.string.app_name) + ": " + title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(msg);
                Button exit = dialog.findViewById(R.id.btDialogExit);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        activity.finish();
                        System.exit(0);
                    }
                });

                Button cancel = dialog.findViewById(R.id.btDialogCancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
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

    public static void customTimeAlert(Activity activity, String title, String msg) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_alert_information);
                dialog.setCancelable(false);
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(activity.getResources().getString(R.string.app_name) + ": " + title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(msg);
                Button yes = dialog.findViewById(R.id.btDialogYes);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        activity.startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));
                    }
                });
                if (!dialog.isShowing())
                    dialog.show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void ShowPlayAlert(Activity activity, String title, String msg, FragmentManager fragmentManager, boolean flag) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_alert_information);
                dialog.setCancelable(false);
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(activity.getResources().getString(R.string.app_name) + ": " + title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(msg);
                Button yes = dialog.findViewById(R.id.btDialogYes);
                yes.setText("Update");
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        try {
                            SharedPreferences.Editor editor = OPMSApplication.get(activity).getPreferencesEditor();
                            editor.putString(AppConstants.PPC_DATA, "");
                            editor.putString(AppConstants.USER_PWD, "");
                            editor.putBoolean(AppConstants.LOGIN_FLAG, false);
                            editor.commit();

                            Intent localIntent = new Intent("android.intent.action.VIEW",
                                    Uri.parse("market://details?id=" + activity.getPackageName()));
                            activity.startActivity(localIntent);
                            activity.finish();
                        } catch (Exception e) {
                            Toast.makeText(activity, activity.getString(R.string.google_play), Toast.LENGTH_SHORT).show();
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


    public static void ShowSessionAlert(Activity activity, String title, String msg,
                                        FragmentManager fragmentManager) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_alert_error);
                dialog.setCancelable(false);
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(activity.getResources().getString(R.string.app_name) + ": " + title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(msg);
                Button yes = dialog.findViewById(R.id.btDialogYes);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (dialog.isShowing())
                            dialog.dismiss();

                        Bundle bundle = new Bundle();
                        bundle.putBoolean(AppConstants.PLAY_FLAG, false);
                        bundle.putString(AppConstants.PLAY_FLAG_MSG, msg);

                        AppConstants.FARG_TAG = LogoutFragment.class.getSimpleName();
                        LogoutFragment logoutFragment = new LogoutFragment();
                        logoutFragment.setArguments(bundle);
                        callFragment(logoutFragment, AppConstants.FARG_TAG, fragmentManager);

                    }
                });
                if (!dialog.isShowing())
                    dialog.show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void ShowDeviceSessionAlert(Activity activity, String title, String msg,
                                              FragmentManager fragmentManager) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_alert_error);
                dialog.setCancelable(false);
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(activity.getResources().getString(R.string.app_name) + ": " + title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(msg);
                Button yes = dialog.findViewById(R.id.btDialogYes);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (dialog.isShowing())
                            dialog.dismiss();

                        SharedPreferences.Editor editor = OPMSApplication.get(activity).getPreferencesEditor();
                        editor.putString(AppConstants.PPC_DATA, "");
                        editor.putString(AppConstants.USER_PWD, "");
                        editor.putBoolean(AppConstants.LOGIN_FLAG, false);
                        editor.commit();
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                        activity.finish();
                    }
                });
                if (!dialog.isShowing())
                    dialog.show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void customLogoutAlert(Activity activity, String title, String msg, boolean flag, FragmentManager fragmentManager) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_alert_exit);
                dialog.setCancelable(false);
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(activity.getResources().getString(R.string.app_name) + ": " + title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(msg);
                Button exit = dialog.findViewById(R.id.btDialogExit);
                exit.setText(activity.getString(R.string.yes_logout));
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        if (ConnectionDetector.isConnectedToInternet(activity)) {
                            Utils.callPlayAlert(activity.getString(R.string.log_suc),
                                    activity, fragmentManager, false);
                        } else {
                            Utils.customAlert(activity,
                                    activity.getResources().getString(R.string.app_name),
                                    activity.getResources().getString(R.string.no_internet),
                                    activity.getResources().getString(R.string.WARNING), false);
                        }
                    }
                });

                Button cancel = dialog.findViewById(R.id.btDialogCancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
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


    public static void callRePrintAlert(Activity activity,
                                        String title,
                                        String msg, FragmentManager fragmentManager) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_alert_exit);
                dialog.setCancelable(false);
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(activity.getResources().getString(R.string.app_name) + ": " + title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(msg);
                Button proceed = dialog.findViewById(R.id.btDialogExit);
                proceed.setText(activity.getString(R.string.yes_proceed));
                proceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        try {
                            AppConstants.FARG_TAG = RePrintFragment.class.getSimpleName();
                            Fragment selectedFragment = new RePrintFragment();
                            callFragment(selectedFragment, AppConstants.FARG_TAG, fragmentManager);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                Button cancel = dialog.findViewById(R.id.btDialogCancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
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

    public static void callPPCDetailsAlert(Activity activity,
                                           String title,
                                           String msg, FragmentManager fragmentManager) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_alert_exit);
                dialog.setCancelable(false);
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(activity.getResources().getString(R.string.app_name) + ": " + title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(msg);
                Button proceed = dialog.findViewById(R.id.btDialogExit);
                proceed.setText(activity.getString(R.string.yes_proceed));
                proceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        try {
                            AppConstants.FARG_TAG = MappedPPCFragment.class.getSimpleName();
                            Fragment selectedFragment = new MappedPPCFragment();
                            callFragment(selectedFragment, AppConstants.FARG_TAG, fragmentManager);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                Button cancel = dialog.findViewById(R.id.btDialogCancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
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

    public static void checkBTAddress(Activity _activity,
                                      FragmentManager fragmentManager) {
        new AsyncBTCheckTask(_activity, fragmentManager).execute();
    }

    @SuppressLint("StaticFieldLeak")
    private static class AsyncBTCheckTask extends AsyncTask<Void, Void, Boolean> {
        Activity _activity;
        FragmentManager fragmentManager;

        AsyncBTCheckTask(Activity _activity, FragmentManager fragmentManager) {
            this._activity = _activity;
            this.fragmentManager = fragmentManager;
        }

        protected Boolean doInBackground(Void... x) {
            boolean res;
            File file = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            + File.separator
                            + "BT_ADDRESS");

            if (!file.exists())
                file.mkdirs();

            File f = new File(file + "/BTaddress1.txt");

            if (f.exists()) {
                try {
                    FileInputStream fStream = new FileInputStream(f);
                    DataInputStream in = new DataInputStream(fStream);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String strLine;

                    while ((strLine = br.readLine()) != null) {
                        AppConstants.BT_ADDRESS = strLine;
                    }
                    in.close();
                    APrinter conn = APrinter.getaPrinter();
                    conn.openBT(AppConstants.BT_ADDRESS);
                    res = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    res = false;
                }
            } else {
                res = false;
            }
            return res;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (!result) {
                Utils.ShowPrintAlert(_activity, fragmentManager, null);
            }
        }
    }

    public static boolean saveBTAddress(String btAddress, String btName) {

        boolean flag = false;
        try {

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + File.separator + "BT_ADDRESS");
            if (!file.exists())
                file.mkdir();

            File btFile = new File(file + "/BTaddress1.txt");
            if (!btFile.exists())
                btFile.createNewFile();

            FileOutputStream fOut = new FileOutputStream(btFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(btAddress);
            myOutWriter.close();
            fOut.close();

            AppConstants.BT_ADDRESS = btAddress;
            AppConstants.BT_NAME = btName;
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;

    }

    public static String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    static void customPdfAlert(Activity activity, String title, String msg, File file) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_alert_pdf);
                dialog.setCancelable(false);
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(msg);
                Button view = dialog.findViewById(R.id.btDialogView);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri uri = FileProvider.getUriForFile(
                                activity,
                                BuildConfig.APPLICATION_ID + ".provider",
                                file);


                        intent.setDataAndType(uri, "application/pdf");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        activity.startActivity(intent);
                    }
                });

                Button cancel = dialog.findViewById(R.id.btDialogCancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
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

//    public static class DecimalDigitsInputFilter implements InputFilter {
//
//        Pattern mPattern;
//
//        public DecimalDigitsInputFilter() {
//            mPattern = Pattern.compile("[0-9]*+((\\.[0-9]?)?)|(\\.)?");
//        }
//
//        @Override
//        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//            Matcher matcher = mPattern.matcher(dest);
//            if (!matcher.matches())
//                return "";
//            return null;
//        }
//    }

    public static class DecimalDigitsInputFilter implements InputFilter {
        Pattern pattern;

        public DecimalDigitsInputFilter(int digitsBeforeDecimal, int digitsAfterDecimal) {
            pattern = Pattern.compile("(([1-9]{1}[0-9]{0," + (digitsBeforeDecimal - 1) + "})?||[0]{1})((\\.[0-9]{0," + digitsAfterDecimal + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int sourceStart, int sourceEnd, Spanned destination, int destinationStart, int destinationEnd) {
            // Remove the string out of destination that is to be replaced.
            String newString = destination.toString().substring(0, destinationStart) + destination.toString().substring(destinationEnd, destination.toString().length());

            // Add the new string in.
            newString = newString.substring(0, destinationStart) + source.toString() + newString.substring(destinationStart, newString.length());

            // Now check if the new string is valid.
            Matcher matcher = pattern.matcher(newString);

            if (matcher.matches()) {
                // Returning null indicates that the input is valid.
                return null;
            }

            // Returning the empty string indicates the input is invalid.
            return "";
        }
    }

}



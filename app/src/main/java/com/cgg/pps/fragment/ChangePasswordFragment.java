package com.cgg.pps.fragment;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.ChangePasswordBinding;
import com.cgg.pps.interfaces.ChangePwdInterface;
import com.cgg.pps.model.request.changepwd.ChangePwdRequest;
import com.cgg.pps.model.response.changepwd.ChangePwdResponse;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.presenter.ChangePwdPresenter;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.ConnectionDetector;
import com.cgg.pps.util.CustomProgressDialog;
import com.cgg.pps.util.Utils;
import com.cgg.pps.view.DashboardActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ChangePasswordFragment extends Fragment implements ChangePwdInterface {

    private ChangePasswordBinding binding;
    private String userPWd;
    private SharedPreferences sharedPreferences;
    private PPCUserDetails ppcUserDetails;
    private CustomProgressDialog customProgressDialog;
    private ChangePwdPresenter changePwdPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.change_password, container, false);
        customProgressDialog = new CustomProgressDialog(getActivity());
        changePwdPresenter = new ChangePwdPresenter();
        changePwdPresenter.attachView(this);

        getPreferenceData();

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Fragment fragment = new DashboardFragment();
                    callFragment(fragment, getFragmentManager(), AppConstants.FARG_TAG);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                        if (validatePwdData()) {
                            Utils.hideKeyboard(view);

                            ChangePwdRequest changePwdRequest = new ChangePwdRequest();
                            changePwdRequest.setUserID(ppcUserDetails.getPPCCode().trim());
                            changePwdRequest.setPassword(binding.cnfNewPwd.getText().toString().trim());
                            changePwdRequest.setDeviceID(Utils.getDeviceID(getActivity()));
                            changePwdRequest.setVersionID(Utils.getVersionName(getActivity()));

                            if (customProgressDialog != null && !customProgressDialog.isShowing())
                                customProgressDialog.show();
                            changePwdPresenter.updatePassword(changePwdRequest);

                        }
                    } else {
                        Utils.customAlert(getActivity(),
                                getResources().getString(R.string.change_pwd),
                                getResources().getString(R.string.no_internet),
                                getResources().getString(R.string.WARNING), false);
                    }
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
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

    private static void callFragment(Fragment fragment, FragmentManager fragmentManager, String name) {
        AppConstants.FARG_TAG = name;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.dashboard_container, fragment, AppConstants.FARG_TAG);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }


    private void getPreferenceData() {
        try {
            if(getActivity()!=null) {
                Gson gson = OPMSApplication.get(getActivity()).getGson();
                sharedPreferences = OPMSApplication.get(getActivity()).getPreferences();
                String string = sharedPreferences.getString(AppConstants.PPC_DATA, "");
                ppcUserDetails = gson.fromJson(string, PPCUserDetails.class);
                userPWd = sharedPreferences.getString(AppConstants.USER_PWD, "");
                if (ppcUserDetails == null) {
                    Utils.ShowDeviceSessionAlert(getActivity(),
                            getResources().getString(R.string.change_pwd),
                            getString(R.string.som_re),
                            getFragmentManager());
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean validatePwdData() {
        if (TextUtils.isEmpty(userPWd)) {
            Toast.makeText(getActivity(), getString(R.string.old_pwd_not_found), Toast.LENGTH_SHORT).show();
            return false;
        }

        String oldPwd = binding.oldPwd.getText().toString();
        if (oldPwd.trim().equalsIgnoreCase("")) {
            binding.oldPwd.requestFocus();
            binding.oldPwd.setError(getString(R.string.old_pwd));
            return false;
        }
        if (!binding.oldPwd.getText().toString().equals(userPWd)) {
            binding.oldPwd.requestFocus();
            binding.oldPwd.setError(getString(R.string.enter_old_pwd));
            return false;
        }
        String newPwd = binding.newPwd.getText().toString();
        if (newPwd.trim().equalsIgnoreCase("")) {
            binding.newPwd.requestFocus();
            binding.newPwd.setError(getString(R.string.enter_new));
            return false;
        } else if (newPwd.trim().equalsIgnoreCase(oldPwd)) {
            binding.newPwd.requestFocus();
            binding.newPwd.setError(getString(R.string.diff_pwd));
            return false;
        }
        String reNewPwd = binding.cnfNewPwd.getText().toString().trim();
        if (reNewPwd.equalsIgnoreCase("")) {
            binding.cnfNewPwd.requestFocus();
            binding.cnfNewPwd.setError(getString(R.string.re_new_pwd));
            return false;
        } else if (!reNewPwd.equalsIgnoreCase(newPwd)) {
            binding.cnfNewPwd.requestFocus();
            binding.cnfNewPwd.setError(getString(R.string.pass_not_match));
            return false;
        }
        return true;
    }

    @Override
    public void updatePwdResponse(ChangePwdResponse changePwdResponse) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();


        try {
            if (changePwdResponse != null && changePwdResponse.getStatusCode() != null
                    && changePwdResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(AppConstants.USER_PWD, binding.cnfNewPwd.getText().toString().trim());
                editor.commit();

                Fragment fragment = new DashboardFragment();
                Utils.showAlertNavigateToFarmer1(getActivity(), fragment, getFragmentManager(),
                        getResources().getString(R.string.change_pwd),
                        changePwdResponse.getResponseMessage(), true);


            } else if (changePwdResponse != null && changePwdResponse.getStatusCode() != null
                    && changePwdResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {

                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.change_pwd),
                        changePwdResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else if (changePwdResponse != null && changePwdResponse.getStatusCode() != null
                    && changePwdResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.change_pwd),
                        changePwdResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (changePwdResponse != null && changePwdResponse.getStatusCode() != null &&
                    changePwdResponse.getStatusCode() == AppConstants.VERSION_CODE) {

                Utils.callPlayAlert(changePwdResponse.getResponseMessage(), getActivity(),
                        getFragmentManager(), true);
            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.change_pwd),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        try {
            if (customProgressDialog != null) {
                customProgressDialog = null;
            }
            if (changePwdPresenter != null) {
                changePwdPresenter.detachView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        super.onResume();

        try {
            if (getActivity() != null)
                ((DashboardActivity) getActivity())
                        .setActionBarTitle(getResources().getString(R.string.change_pwd));

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(int code, String msg) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();

        if (code == AppConstants.ERROR_CODE) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.change_pwd),
                    msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else if (code == AppConstants.EXCEPTION_CODE) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.change_pwd),
                    getString(R.string.something) +
                            msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.change_pwd),
                    getString(R.string.something),
                    getResources().getString(R.string.ERROR),
                    false);
        }

    }
}

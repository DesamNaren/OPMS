package com.cgg.pps.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.LogoutFragmentBinding;
import com.cgg.pps.interfaces.LogoutInterface;
import com.cgg.pps.model.request.validateuser.logout.LogoutRequest;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.model.response.validateuser.logout.LogoutResponse;
import com.cgg.pps.presenter.LogoutPresenter;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.CustomProgressDialog;
import com.cgg.pps.util.Utils;
import com.cgg.pps.view.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class LogoutFragment extends Fragment implements LogoutInterface {

    private CustomProgressDialog customProgressDialog;
    private PPCUserDetails ppcUserDetails;
    private boolean playFlag;
    private String playFlagMsg;
    private LogoutPresenter logoutPresenter;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LogoutFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.logout_fragment, container, false);

        getPreferenceData();

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            playFlag = bundle.getBoolean(AppConstants.PLAY_FLAG, false);
            playFlagMsg = bundle.getString(AppConstants.PLAY_FLAG_MSG, "");
        }

        customProgressDialog = new CustomProgressDialog(getActivity());
        logoutPresenter = new LogoutPresenter();
        logoutPresenter.attachView(this);

        if (customProgressDialog != null && !customProgressDialog.isShowing()) {
            customProgressDialog.show();
        }

        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUserID(ppcUserDetails.getPPCCode());
        logoutPresenter.callLogout(logoutRequest);
        return binding.getRoot();
    }

    private void getPreferenceData() {
        try {
            if (getActivity() != null) {
                Gson gson = OPMSApplication.get(getActivity()).getGson();
                SharedPreferences sharedPreferences = OPMSApplication.get(getActivity()).getPreferences();
                String string = sharedPreferences.getString(AppConstants.PPC_DATA, "");
                ppcUserDetails = gson.fromJson(string, PPCUserDetails.class);
                if (ppcUserDetails == null) {

                    Utils.ShowDeviceSessionAlert(getActivity(),
                            getResources().getString(R.string.logout),
                            getString(R.string.ses_expire_re),
                            getFragmentManager());


                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getLogoutResponse(LogoutResponse logoutResponse) {
        try {
            if (customProgressDialog != null && customProgressDialog.isShowing()) {
                customProgressDialog.dismiss();
            }

            SharedPreferences.Editor editor;
            if (getActivity() != null) {
                editor = OPMSApplication.get(getActivity()).getPreferencesEditor();
            } else {
                editor = OPMSApplication.get(mContext).getPreferencesEditor();
            }
            editor.putString(AppConstants.PPC_DATA, "");
            editor.putString(AppConstants.USER_PWD, "");
            editor.putBoolean(AppConstants.LOGIN_FLAG, false);

            editor.commit();

//            if (logoutResponse != null && logoutResponse.getStatusCode() != null
//                    && logoutResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
//
//            } else if (logoutResponse != null && logoutResponse.getStatusCode() != null
//                    && logoutResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
//                Toast.makeText(getActivity(), logoutResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getActivity(), R.string.server_not, Toast.LENGTH_SHORT).show();
//            }

            if (playFlag && getActivity() != null) {
                Intent localIntent = new Intent("android.intent.action.VIEW",
                        Uri.parse("market://details?id=" + getActivity().getPackageName()));
                try {
                    getActivity().startActivity(localIntent);
                    getActivity().finish();
                    System.exit(0);
                } catch (ActivityNotFoundException localActivityNotFoundException) {
                    Toast.makeText(getActivity(), getString(R.string.google_play), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), playFlagMsg, Toast.LENGTH_SHORT).show();
            }

            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();


        }catch (Exception e){
            callLogout();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (customProgressDialog != null) {
                customProgressDialog = null;
            }
            if (logoutPresenter != null) {
                logoutPresenter.detachView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(int code, String msg) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();

        try {
            callLogout();
//            else {
//                if (code == AppConstants.ERROR_CODE) {
//                    Utils.customAlert(getActivity(),
//                            getResources().getString(R.string.logout),
//                            msg,
//                            getResources().getString(R.string.ERROR),
//                            false);
//                } else if (code == AppConstants.EXCEPTION_CODE) {
//                    Utils.customAlert(getActivity(),
//                            getResources().getString(R.string.logout),
//                            getString(R.string.something) +
//                                    msg,
//                            getResources().getString(R.string.ERROR),
//                            false);
//                } else {
//                    Utils.customAlert(getActivity(),
//                            getResources().getString(R.string.logout),
//                            getString(R.string.something),
//                            getResources().getString(R.string.ERROR),
//                            false);
//                }
//            }
        } catch (Exception e) {
            callLogout();
        }


    }

    private void callLogout() {
        SharedPreferences.Editor editor;
        if (getActivity() != null) {
            editor = OPMSApplication.get(getActivity()).getPreferencesEditor();
        } else {
            editor = OPMSApplication.get(mContext).getPreferencesEditor();
        }
        editor.putString(AppConstants.PPC_DATA, "");
        editor.putString(AppConstants.USER_PWD, "");
        editor.putBoolean(AppConstants.LOGIN_FLAG, false);
        editor.commit();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

}

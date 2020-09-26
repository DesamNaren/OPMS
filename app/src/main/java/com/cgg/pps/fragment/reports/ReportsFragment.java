package com.cgg.pps.fragment.reports;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.ReportsFragmentBinding;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.Utils;
import com.cgg.pps.view.DashboardActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.cgg.pps.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ReportsFragment extends Fragment {
    private ReportsFragmentBinding binding;
    private PPCUserDetails ppcUserDetails;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.reports_fragment, container, false);
        getPreferenceData();
        binding.ReportsReportTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Fragment fragment = null;
                    if (position == 0) {
                        fragment = new Fragment();
                    } else if (position == 1) {
                        fragment = new TokenWiseReportFragment();
                    } else if (position == 2) {
                        fragment = new PaymentStatusReportFragment();
                    } else if (position == 3) {
                        fragment = new GunnyReportFragment();
                    } else if (position == 4) {
                        fragment = new TCReportFragment();
                    } else if (position == 5) {
                        fragment = new TransactionReportFragment();
                    } else if (position == 6) {
                        fragment = new MillWiseReportFragment();
                    } else if (position == 7) {
                        fragment = new PaddyPaymentReportFragment();
                    }
                    if (fragment != null && getActivity() != null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.Reports_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commitAllowingStateLoss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return binding.getRoot();
    }


    public void onResume() {
        super.onResume();


        try {
            if (getActivity() != null)
                ((DashboardActivity) getActivity())
                        .setActionBarTitle(getResources().getString(R.string.nav_reports));

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    private void getPreferenceData() {
        try {
            if(getActivity()!=null) {
                Gson gson = OPMSApplication.get(getActivity()).getGson();
                SharedPreferences sharedPreferences = OPMSApplication.get(getActivity()).getPreferences();
                String string = sharedPreferences.getString(AppConstants.PPC_DATA, "");
                ppcUserDetails = gson.fromJson(string, PPCUserDetails.class);
                if (ppcUserDetails == null) {
                    Utils.ShowDeviceSessionAlert(getActivity(),
                            getResources().getString(R.string.nav_reports),
                            getString(R.string.ses_expire_re),
                            getFragmentManager());
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
}

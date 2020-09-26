package com.cgg.pps.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.ReprintFragmentBinding;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


@SuppressLint("SimpleDateFormat")
public class RePrintFragment extends Fragment {

    private PPCUserDetails ppcUserDetails;
    private ReprintFragmentBinding binding;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.reprint_fragment, container, false);
        getPreferenceData();
        dialog = new ProgressDialog(getActivity());


        Utils.checkBTAddress(getActivity(), getFragmentManager());

        binding.reptintRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.reprint_tkn) {
                    try {
                        Fragment fragment = new TokenRePrintFragment();
                        callFragment(fragment, getFragmentManager());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if(checkedId == R.id.reprint_pro) {
                    try {
                        Fragment fragment = new ProcRePrintFragment();
                        callFragment(fragment, getFragmentManager());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if(checkedId == R.id.reprint_tru) {
                    try {
                        Fragment fragment = new TCRePrintFragment();
                        callFragment(fragment, getFragmentManager());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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


    private static void callFragment(Fragment fragment, FragmentManager fragmentManager) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.reprint_container, fragment, "");
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
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
                            getResources().getString(R.string.Reprint),
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
}



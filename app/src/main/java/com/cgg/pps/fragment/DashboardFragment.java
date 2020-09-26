package com.cgg.pps.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cgg.pps.databinding.DashboardFragmentBinding;
import com.cgg.pps.interfaces.DashboardFragmentInterface;
import com.cgg.pps.R;
import com.cgg.pps.util.ConnectionDetector;
import com.cgg.pps.util.Utils;
import com.cgg.pps.view.DRPActivity;
import com.cgg.pps.view.LoginActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class DashboardFragment extends Fragment {
    DashboardFragmentBinding binding;
    private DashboardFragmentInterface dashboardFragmentInterface;
    private static final int REQUEST_READ_PHONE_STATE = 2000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dashboard_fragment, container, false);
        binding.farmerRegID.setOnClickListener(onBtnClick);
        binding.faq.setOnClickListener(onBtnClick);
        binding.gunniestoFormer.setOnClickListener(onBtnClick);
        binding.paddyProcurement.setOnClickListener(onBtnClick);
        binding.reports.setOnClickListener(onBtnClick);
        binding.truckchit.setOnClickListener(onBtnClick);
        binding.drp.setOnClickListener(onBtnClick);
        int permissionCheck = ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_READ_PHONE_STATE);
        } else {
            binding.deviceId.setText("Device ID: " + Utils.getDeviceID(getActivity()));
            binding.versionId.setText("Version : " + Utils.getVersionName(getActivity()));
        }
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DashboardFragmentInterface) {
            dashboardFragmentInterface = (DashboardFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement DashboardFragmentInterface");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_PHONE_STATE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                binding.deviceId.setText("Device ID: " + Utils.getDeviceID(getActivity()));
                binding.versionId.setText("Version : " + Utils.getVersionName(getActivity()));
            } else {
                Toast.makeText(getActivity(), getString(R.string.all_per), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dashboardFragmentInterface = null;
    }

    private View.OnClickListener onBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.farmerRegID:
                    callFragment(getResources().getString(R.string.FarmerRegistration));
                    break;
                case R.id.faq:
                    callFragment(getResources().getString(R.string.FAQ));
                    break;
                case R.id.gunniestoFormer:
                    callFragment(getResources().getString(R.string.GunnysGivenToFarmer));
                    break;
                case R.id.paddyProcurement:
                    callFragment(getResources().getString(R.string.PaddyProcurement));
                    break;
                case R.id.reports:
                    callFragment(getResources().getString(R.string.nav_reports));
                    break;
                case R.id.truckchit:
                    callFragment(getResources().getString(R.string.PPCtoMiller));
                    break;
                case R.id.drp:
                    startActivity(new Intent(getActivity(), DRPActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
            }
        }
    };

    private void callFragment(String frag) {
        if (ConnectionDetector.isConnectedToInternet(getActivity())) {
            dashboardFragmentInterface.CallFragment(frag);
        } else {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.home),
                    getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.WARNING), false);
        }
    }
}

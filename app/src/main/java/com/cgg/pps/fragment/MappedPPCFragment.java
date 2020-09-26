package com.cgg.pps.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cgg.pps.R;
import com.cgg.pps.adapter.MappedVilDetailsAdapter;
import com.cgg.pps.adapter.PPCDetailsAdapter;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.PpcDetailsFragmentBinding;
import com.cgg.pps.interfaces.DMVInterface;
import com.cgg.pps.interfaces.MappedPPCInterface;
import com.cgg.pps.model.request.ppc_details.MappedPPCDetailsRequest;
import com.cgg.pps.model.response.devicemgmt.masterdistrict.DistrictEntity;
import com.cgg.pps.model.response.devicemgmt.mastermandal.MandalEntity;
import com.cgg.pps.model.response.devicemgmt.mastervillage.VillageEntity;
import com.cgg.pps.model.response.ppc_details.MappedPPCDetailsResponse;
import com.cgg.pps.model.response.socialstatus.SocialEntity;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.presenter.MappedPCPresenter;
import com.cgg.pps.room.repository.DMVRepository;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.ConnectionDetector;
import com.cgg.pps.util.CustomProgressDialog;
import com.cgg.pps.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("SimpleDateFormat")
public class MappedPPCFragment extends Fragment implements DMVInterface, AdapterView.OnItemSelectedListener, MappedPPCInterface {

    private PPCUserDetails ppcUserDetails;
    private PpcDetailsFragmentBinding binding;
    private ProgressDialog dialog;
    private DMVRepository dmvRepository;
    private List<DistrictEntity> districtEntities;
    private List<MandalEntity> mandalEntities;
    private List<VillageEntity> villageEntities;
    private List<String> disStringListMain, manStringListMain, vilStringListMain;
    private String f_district = "--Select--";
    private String f_mandal = "--Select--";
    private String f_village = "--Select--";
    private List<String> emptyList;
    private MappedPCPresenter mappedPCPresenter;
    private CustomProgressDialog customProgressDialog;
    private String distID = null, manId = null, villageId = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.ppc_details_fragment, container, false);
        getPreferenceData();
        dialog = new ProgressDialog(getActivity());
        mappedPCPresenter = new MappedPCPresenter();
        mappedPCPresenter.attachView(this);
        customProgressDialog = new CustomProgressDialog(getActivity());

        districtEntities = new ArrayList<>();
        mandalEntities = new ArrayList<>();
        villageEntities = new ArrayList<>();
        disStringListMain = new ArrayList<>();
        manStringListMain = new ArrayList<>();
        vilStringListMain = new ArrayList<>();

        emptyList = new ArrayList<>();
        emptyList.add("--Select--");

        dmvRepository = new DMVRepository(getActivity());
        dmvRepository.getAllDistricts(this);

        binding.spDist.setEnabled(true);
        binding.spMandal.setEnabled(false);
        binding.spVillage.setEnabled(false);

        binding.spDist.setOnItemSelectedListener(this);
        binding.spMandal.setOnItemSelectedListener(this);
        binding.spVillage.setOnItemSelectedListener(this);

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

    @Override
    public void getAllDistrictEntities(List<DistrictEntity> districtEntities) {
        try {
            if (districtEntities.size() > 0) {
                this.districtEntities = districtEntities;
                disStringListMain = new ArrayList<>();
                disStringListMain.add(0, "--Select--");

                for (DistrictEntity districtEntity : districtEntities) {
                    disStringListMain.add(districtEntity.getDistName());
                }
                Utils.loadSpinnerSetSelectedItem(getActivity(), disStringListMain, binding.spDist, f_district);


                if (disStringListMain.size() > 0) {
                    dmvRepository.getAllMandals(this);
                } else {
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                    Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                            getResources().getString(R.string.FarmerRegistration),
                            getString(R.string.no_districts), true);
                }
            } else {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                        getResources().getString(R.string.FarmerRegistration),
                        getString(R.string.no_districts), true);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void getAllMandalEntities(List<MandalEntity> mandalEntities) {
        try {
            if (mandalEntities.size() > 0) {
                this.mandalEntities = mandalEntities;
                manStringListMain = new ArrayList<>();
                manStringListMain.add(0, "--Select--");

                for (MandalEntity mandalEntity : mandalEntities) {
                    manStringListMain.add(mandalEntity.getMandalName());
                }

                Utils.loadSpinnerSetSelectedItem(getActivity(), manStringListMain, binding.spMandal, f_mandal);


                if (manStringListMain.size() > 0) {
                    dmvRepository.getAllVillages(this);
                } else {
                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();
                    Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                            getResources().getString(R.string.FarmerRegistration),
                            getString(R.string.no_mandals), true);
                }
            } else {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                        getResources().getString(R.string.FarmerRegistration),
                        getString(R.string.no_mandals), true);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void getAllVillageEntities(List<VillageEntity> villageEntities) {
        try {
            if (villageEntities.size() > 0) {
                this.villageEntities = villageEntities;
                vilStringListMain = new ArrayList<>();
                vilStringListMain.add(0, "--Select--");

                for (VillageEntity villageEntity : villageEntities) {
                    vilStringListMain.add(villageEntity.getVillageName());
                }

                Utils.loadSpinnerSetSelectedItem(getActivity(), vilStringListMain, binding.spVillage, f_village);
            } else {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                Utils.showAlertNavigateToMasterDownloads(getActivity(), getFragmentManager(),
                        getResources().getString(R.string.FarmerRegistration),
                        getString(R.string.no_villages), true);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllSocialEntities(List<SocialEntity> socialEntities) {

    }

    @Override
    public void onDataNotAvailable() {

    }

    @Override
    public void distCount(int cnt) {

    }

    @Override
    public void mandalCount(int cnt) {

    }

    @Override
    public void villageCount(int cnt) {

    }

    @Override
    public void getAllMandalNamesByDist(List<String> mandalEntities, Spinner spinner, Spinner vspinner, String distName) {

    }

    @Override
    public void getAllLandMandalNamesByDist(List<String> mandalEntities, Spinner spinner, Spinner vspinner, String distName) {

    }

    @Override
    public void getAllVillagesByMan(List<String> villageEntities, Spinner spinner, String distName, String manName) {

    }

    @Override
    public void getAllowDistrictEntities(List<DistrictEntity> districtNames) {

    }

    @Override
    public void getAllowMandalEntities(List<MandalEntity> mandalNames) {

    }

    @Override
    public void getAllowVillageEntities(List<VillageEntity> villageNames) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        try {
            switch (parent.getId()) {
                case R.id.sp_dist:
                    try {
                        binding.vilCV.setVisibility(View.GONE);
                        binding.villageRV.setAdapter(null);

                        String districtName = parent.getSelectedItem().toString().trim();
                        if (!districtName.equalsIgnoreCase("--Select--")) {

                            List<String> mandalsStringList = new ArrayList<>();
                            mandalsStringList.add(0, "--Select--");
                            distID = null;
                            for (DistrictEntity districtEntity : districtEntities) {
                                if (districtEntity.getDistName().equalsIgnoreCase(districtName)) {
                                    distID = districtEntity.getDistId();

                                    for (MandalEntity mandalEntity : mandalEntities) {
                                        if (mandalEntity.getDistrictID().equalsIgnoreCase(distID)) {
                                            mandalsStringList.add(mandalEntity.getMandalName());
                                        }
                                    }
                                }
                            }
                            binding.spMandal.setEnabled(true);
                            Utils.loadSpinnerSetSelectedItem(getActivity(), mandalsStringList, binding.spMandal, f_mandal);
                        } else {
                            distID = null;
                            manId = null;
                            villageId = null;
                            binding.spMandal.setEnabled(false);
                            binding.spVillage.setEnabled(false);
                            Utils.loadSpinnerSetSelectedItem(getActivity(), emptyList, binding.spMandal, f_mandal);
                            Utils.loadSpinnerSetSelectedItem(getActivity(), emptyList, binding.spVillage, f_village);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case R.id.sp_mandal:
                    try {
                        binding.vilCV.setVisibility(View.GONE);
                        binding.villageRV.setAdapter(null);

                        String districtName = binding.spDist.getSelectedItem().toString().trim();
                        String mandalName = parent.getSelectedItem().toString().trim();

                        if (!mandalName.equalsIgnoreCase("--Select--")) {


                            List<String> villagesStringList = new ArrayList<>();
                            villagesStringList.add(0, "--Select--");

                            for (DistrictEntity districtEntity : districtEntities) {
                                if (districtEntity.getDistName().equalsIgnoreCase(districtName)) {
                                    distID = districtEntity.getDistId();
                                }
                            }
                            for (MandalEntity mandalEntity : mandalEntities) {
                                if (mandalEntity.getDistrictID().equalsIgnoreCase(distID) &&
                                        mandalEntity.getMandalName().equalsIgnoreCase(mandalName)) {
                                    manId = mandalEntity.getMandalID();
                                }
                            }

                            if (distID != null && manId != null) {
                                for (VillageEntity villageEntity : villageEntities) {
                                    if (villageEntity.getDistrictID().equalsIgnoreCase(distID)
                                            &&
                                            villageEntity.getMandalID().equalsIgnoreCase(manId)) {
                                        villagesStringList.add(villageEntity.getVillageName());
                                    }

                                }
                            }
                            binding.spVillage.setEnabled(true);
                            Utils.loadSpinnerSetSelectedItem(getActivity(), villagesStringList, binding.spVillage, f_village);
                        } else {
                            manId = null;
                            villageId = null;
                            binding.spVillage.setEnabled(false);
                            Utils.loadSpinnerSetSelectedItem(getActivity(), emptyList, binding.spVillage, f_village);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;


                case R.id.sp_village:

                    try {
                        binding.vilCV.setVisibility(View.GONE);
                        binding.villageRV.setAdapter(null);

                        String districtName = binding.spDist.getSelectedItem().toString().trim();
                        String mandalName = binding.spMandal.getSelectedItem().toString().trim();
                        String villageName = parent.getSelectedItem().toString().trim();

                        if (!villageName.equalsIgnoreCase("--Select--")) {

                            for (DistrictEntity districtEntity : districtEntities) {
                                if (districtEntity.getDistName().equalsIgnoreCase(districtName)) {
                                    distID = districtEntity.getDistId();
                                    break;
                                }
                            }
                            for (MandalEntity mandalEntity : mandalEntities) {
                                if (mandalEntity.getDistrictID().equalsIgnoreCase(distID) &&
                                        mandalEntity.getMandalName().equalsIgnoreCase(mandalName)) {
                                    manId = mandalEntity.getMandalID();
                                    break;
                                }
                            }

                            for (VillageEntity villageEntity : villageEntities) {
                                if (villageEntity.getDistrictID().equalsIgnoreCase(distID) &&
                                        villageEntity.getMandalID().equalsIgnoreCase(manId)
                                        && villageEntity.getVillageName().equalsIgnoreCase(villageName)) {
                                    villageId = villageEntity.getVillageID();
                                    break;
                                }
                            }
                            if (distID != null && manId != null && villageId != null) {

                                if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                                    callPPCDetailsTask();
                                } else {
                                    binding.spVillage.setSelection(0);
                                    Utils.customAlert(getActivity(),
                                            getResources().getString(R.string.vil_details),
                                            getResources().getString(R.string.no_internet),
                                            getResources().getString(R.string.WARNING), false);
                                }
                            }
                        } else {
                            villageId = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callPPCDetailsTask() {
        MappedPPCDetailsRequest mappedPpcDetailsRequest = new MappedPPCDetailsRequest();
        mappedPpcDetailsRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
        mappedPpcDetailsRequest.setDistrictID(distID);
        mappedPpcDetailsRequest.setMandalID(manId);
        mappedPpcDetailsRequest.setVillageID(villageId);
        customProgressDialog.show();
        mappedPCPresenter.GetMappedPPCVillagesData(mappedPpcDetailsRequest);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void getMappedPPCDetailsResponse(MappedPPCDetailsResponse mappedPpcDetailsResponse) {
        try {
            binding.vilCV.setVisibility(View.GONE);
            binding.ppcCV.setVisibility(View.GONE);

            if (customProgressDialog != null && customProgressDialog.isShowing())
                customProgressDialog.dismiss();


            if (mappedPpcDetailsResponse != null && mappedPpcDetailsResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (mappedPpcDetailsResponse.getPpcdetails() != null
                        && mappedPpcDetailsResponse.getPpcdetails().size() > 0) {
                    binding.ppcCV.setVisibility(View.VISIBLE);
                    MappedVilDetailsAdapter mappedVilDetailsAdapter = new MappedVilDetailsAdapter(getActivity(), mappedPpcDetailsResponse.getPpcdetails());
                    binding.ppcRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.ppcRV.setAdapter(mappedVilDetailsAdapter);
                }
                if (mappedPpcDetailsResponse.getMappedVillages() != null
                        && mappedPpcDetailsResponse.getMappedVillages().size() > 0) {
                    binding.vilCV.setVisibility(View.VISIBLE);
                    binding.emptyTV.setVisibility(View.GONE);
                    PPCDetailsAdapter ppcDetailsAdapter = new PPCDetailsAdapter(getActivity(), mappedPpcDetailsResponse.getMappedVillages());
                    binding.villageRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    binding.villageRV.setAdapter(ppcDetailsAdapter);
                } else {
                    binding.emptyTV.setVisibility(View.VISIBLE);
                }
            } else if (mappedPpcDetailsResponse != null &&
                    mappedPpcDetailsResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.nav_reports),
                        mappedPpcDetailsResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else if (mappedPpcDetailsResponse != null &&
                    mappedPpcDetailsResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.nav_reports),
                        mappedPpcDetailsResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (mappedPpcDetailsResponse != null &&
                    mappedPpcDetailsResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(),
                        getResources().getString(R.string.app_name),
                        mappedPpcDetailsResponse.getResponseMessage(),
                        getFragmentManager());
            } else if (mappedPpcDetailsResponse != null &&
                    mappedPpcDetailsResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(mappedPpcDetailsResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.nav_reports),
                        getResources().getString(R.string.server_not),
                        getString(R.string.ERROR), false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.nav_reports),
                    getResources().getString(R.string.something),
                    getString(R.string.ERROR), false);
            e.printStackTrace();
        }

    }

    @Override
    public void onError(int code, String msg) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();

        if (code == AppConstants.ERROR_CODE) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.nav_reports),
                    msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else if (code == AppConstants.EXCEPTION_CODE) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.nav_reports),
                    getString(R.string.something) +
                            msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.nav_reports),
                    getString(R.string.something),
                    getResources().getString(R.string.ERROR),
                    false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (customProgressDialog != null) {
                customProgressDialog.dismiss();
                customProgressDialog = null;
            }
            if (mappedPCPresenter != null) {
                mappedPCPresenter.detachView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



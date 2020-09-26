package com.cgg.pps.fragment.reports;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.cgg.pps.R;
import com.cgg.pps.adapter.MillReportAdapter;
import com.cgg.pps.adapter.TCReportAdapter;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.RoWiseReportsFragmentBinding;
import com.cgg.pps.interfaces.TokenReportInterface;
import com.cgg.pps.model.request.reports.CommonReportRequest;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
import com.cgg.pps.model.response.report.mill.MillReportResponse;
import com.cgg.pps.model.response.report.payment.ReportsDataResponce;
import com.cgg.pps.model.response.report.transaction.TransactionReportResponseList;
import com.cgg.pps.model.response.report.truckchit.TCReportResponse;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.presenter.ReportPresenter;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.ConnectionDetector;
import com.cgg.pps.util.CustomProgressDialog;
import com.cgg.pps.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class TCReportFragment extends Fragment implements TokenReportInterface {
    private PPCUserDetails ppcUserDetails;
    RoWiseReportsFragmentBinding binding;
    private ReportPresenter reportPresenter;
    private CustomProgressDialog customProgressDialog;
    private TCReportAdapter tcReportAdapter;
    private ReportsDataResponce tcReportResponseList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view =  inflater.inflate(R.layout.tc_wise_reports_fragment, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.ro_wise_reports_fragment, container, false);
        reportPresenter = new ReportPresenter();
        reportPresenter.attachView(this);
        customProgressDialog = new CustomProgressDialog(getActivity());
        getPreferenceData();
        if (ConnectionDetector.isConnectedToInternet(getActivity())) {
            callTruckChitReportTask();
        } else {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.nav_reports),
                    getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.WARNING),false);
        }
        binding.inputSearch.setQueryHint(getString(R.string.serarch_by_tc_id));
        search(binding.inputSearch);
        binding.spMill.setVisibility(View.GONE);

        binding.spMill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.inputSearch.setQuery("", false);
                binding.inputSearch.setIconified(true);
                if(!parent.getSelectedItem().equals("--Select--")){
                    List<TCReportResponse> filterList = new ArrayList<>();
                    for(int z=0;z<tcReportResponseList.getGetTruckchitDataReport().size();z++){
                        if(tcReportResponseList.getGetTruckchitDataReport().get(z).getMillName().equalsIgnoreCase(parent.getSelectedItem().toString())){
                            filterList.add(tcReportResponseList.getGetTruckchitDataReport().get(z));
                        }
                    }
                    if(filterList.size()>0) {
                        tcReportAdapter = new TCReportAdapter(getActivity(), filterList);
                        binding.roRV.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                        binding.roRV.setAdapter(tcReportAdapter);
                    }

                }else {
                    tcReportAdapter = new TCReportAdapter(getActivity(), tcReportResponseList.getGetTruckchitDataReport());
                    binding.roRV.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                    binding.roRV.setAdapter(tcReportAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return binding.getRoot();
    }


    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    tcReportAdapter.getFilter().filter(newText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
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



    private void callTruckChitReportTask() {
        try {
            CommonReportRequest commonReportRequest = new CommonReportRequest();
            commonReportRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
            commonReportRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));
            commonReportRequest.setTokenID("00"); // static
            commonReportRequest.setTokenNo("00"); //static
            commonReportRequest.setRegistrationNo("00"); //static
            commonReportRequest.setTransactionID("00"); //static
            commonReportRequest.setTransactionRowID("00"); //static
            commonReportRequest.setTransactionStatus("00"); //static
            commonReportRequest.setServiceName("GetTruckchitData"); //static
            if (customProgressDialog != null)
                customProgressDialog.show();
            reportPresenter.GetPaymentStatusReports(commonReportRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void getPaymentStatusReportData(ReportsDataResponce tcReportResponseList) {
        try {
            TCReportFragment.this.tcReportResponseList=tcReportResponseList;
            if (tcReportResponseList != null && tcReportResponseList.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (tcReportResponseList.getGetTruckchitDataReport() != null && tcReportResponseList.getGetTruckchitDataReport().size() > 0) {
                    tcReportAdapter = new TCReportAdapter(getActivity(), tcReportResponseList.getGetTruckchitDataReport());
                    binding.roRV.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                    binding.roRV.setAdapter(tcReportAdapter);

                    HashMap<String, String> uniqueMills = new HashMap<>();
                    for (TCReportResponse tcReportResponse : tcReportResponseList.getGetTruckchitDataReport()) {
                        uniqueMills.put(tcReportResponse.getMillID(), tcReportResponse.getMillName());
                    }

                    setSpinnerData(uniqueMills);
                } else {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.nav_reports),
                            tcReportResponseList.getResponseMessage(),
                            getString(R.string.ERROR), false);
                }
            } else if (tcReportResponseList != null &&
                    tcReportResponseList.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.nav_reports),
                        tcReportResponseList.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else if (tcReportResponseList != null &&
                    tcReportResponseList.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.nav_reports),
                        tcReportResponseList.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (tcReportResponseList != null &&
                    tcReportResponseList.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(),
                        getResources().getString(R.string.nav_reports),
                        tcReportResponseList.getResponseMessage(),
                        getFragmentManager());
            } else if (tcReportResponseList != null &&
                    tcReportResponseList.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(tcReportResponseList.getResponseMessage(), getActivity(), getFragmentManager(), true);

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
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
    }

    private void setSpinnerData(HashMap<String, String> uniqueMills) {
        List<String> list = new ArrayList<>(uniqueMills.values());
        list.add(0, "--Select--");
        binding.spMill.setVisibility(View.VISIBLE);
        Utils.loadSpinnerSetSelectedItem(getActivity(),
                list,
                binding.spMill, "--Select--");
    }

    @Override
    public void getTokensResponseData(GetTokensResponse getTokensFarmerResponse) {

    }

    @Override
    public void getTransactionsReportData(TransactionReportResponseList transactionReportResponseList) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (customProgressDialog != null) {
                customProgressDialog = null;
            }
            if (reportPresenter != null) {
                reportPresenter.detachView();
            }

        } catch (Exception e) {
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
}

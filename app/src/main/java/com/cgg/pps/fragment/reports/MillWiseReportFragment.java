package com.cgg.pps.fragment.reports;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.cgg.pps.R;
import com.cgg.pps.adapter.MillReportAdapter;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.RoWiseReportsFragmentBinding;
import com.cgg.pps.interfaces.TokenReportInterface;
import com.cgg.pps.model.request.reports.CommonReportRequest;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
import com.cgg.pps.model.response.report.mill.MillReportResponse;
import com.cgg.pps.model.response.report.payment.ReportsDataResponce;
import com.cgg.pps.model.response.report.transaction.TransactionReportResponseList;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.presenter.ReportPresenter;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.ConnectionDetector;
import com.cgg.pps.util.CustomProgressDialog;
import com.cgg.pps.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class MillWiseReportFragment extends Fragment implements TokenReportInterface {

    private PPCUserDetails ppcUserDetails;
    RoWiseReportsFragmentBinding binding;
    private ReportPresenter reportPresenter;
    private CustomProgressDialog customProgressDialog;
    private MillReportAdapter millReportAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.ro_wise_reports_fragment, container, false);
        reportPresenter = new ReportPresenter();
        reportPresenter.attachView(this);
        customProgressDialog = new CustomProgressDialog(getActivity());
        getPreferenceData();
        if (ConnectionDetector.isConnectedToInternet(getActivity())) {
            callROTask();
        } else {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.nav_reports),
                    getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.WARNING), false);
        }
        binding.inputSearch.setQueryHint("Search by Mill ID (or) Mill Code (or) Mill Name");
        search(binding.inputSearch);
        binding.spMill.setVisibility(View.GONE);
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
                    millReportAdapter.getFilter().filter(newText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
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
                            getResources().getString(R.string.nav_reports),
                            getString(R.string.ses_expire_re),
                            getFragmentManager());
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void callROTask() {
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
            commonReportRequest.setServiceName("GetAllotedMillwiseQuantityData"); //static
            if (customProgressDialog != null)
                customProgressDialog.show();
            reportPresenter.GetPaymentStatusReports(commonReportRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getPaymentStatusReportData(ReportsDataResponce millReportResponse) {
        try {
            if (millReportResponse != null && millReportResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (millReportResponse.getGetReportAllocatedMillwiseQuantityData() != null && millReportResponse.getGetReportAllocatedMillwiseQuantityData().size() > 0) {
                    millReportAdapter = new MillReportAdapter(getActivity(), millReportResponse.getGetReportAllocatedMillwiseQuantityData());
                    binding.roRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    binding.roRV.setAdapter(millReportAdapter);
                } else {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.nav_reports),
                            millReportResponse.getResponseMessage(),
                            getString(R.string.ERROR), false);
                }
            } else if (millReportResponse != null &&
                    millReportResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.nav_reports),
                        millReportResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else if (millReportResponse != null &&
                    millReportResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.nav_reports),
                        millReportResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (millReportResponse != null &&
                    millReportResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(),
                        getResources().getString(R.string.nav_reports),
                        "Session expired, Please Re-login",
                        getFragmentManager());
            } else if (millReportResponse != null &&
                    millReportResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(millReportResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

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

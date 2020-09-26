package com.cgg.pps.fragment.reports;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cgg.pps.R;
import com.cgg.pps.adapter.TokenReportAdapter;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.TokenWiseReportsFragmentBinding;
import com.cgg.pps.interfaces.TokenReportInterface;
import com.cgg.pps.model.request.farmer.gettokens.GetTokensRequest;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
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

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

@SuppressLint("SimpleDateFormat")
public class TokenWiseReportFragment extends Fragment implements TokenReportInterface {
    private PPCUserDetails ppcUserDetails;
    private TokenReportAdapter tokenReportAdapter;
    private TokenWiseReportsFragmentBinding binding;
    private ReportPresenter reportPresenter;
    private CustomProgressDialog customProgressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.token_wise_reports_fragment, container, false);
        getPreferenceData();
        reportPresenter = new ReportPresenter();
        reportPresenter.attachView(this);

        customProgressDialog = new CustomProgressDialog(getActivity());
        if (ConnectionDetector.isConnectedToInternet(getActivity())) {
            callTokensTask();
        } else {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.nav_reports),
                    getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.WARNING),false);
        }
        binding.inputSearch.setQueryHint(getString(R.string.enter_tkn));
        search(binding.inputSearch);
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
                    tokenReportAdapter.getFilter().filter(newText);
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
    private void callTokensTask() {
        try {
            GetTokensRequest getTokensFarmerRequest = new GetTokensRequest();
            getTokensFarmerRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
            getTokensFarmerRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));
            if (customProgressDialog != null)
                customProgressDialog.show();
            reportPresenter.GetTokenWiseReports(getTokensFarmerRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTokensResponseData(GetTokensResponse getTokensResponse) {
        try {
            if (getTokensResponse != null && getTokensResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (getTokensResponse.getGetTokenOutput() != null && getTokensResponse.getGetTokenOutput().size() > 0) {
                    tokenReportAdapter = new TokenReportAdapter(getActivity(), getTokensResponse.getGetTokenOutput());
                    binding.tokenRV.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                    binding.tokenRV.setAdapter(tokenReportAdapter);
                } else {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.nav_reports),
                            getTokensResponse.getResponseMessage(),
                            getString(R.string.ERROR), false);
                }
            } else if (getTokensResponse != null &&
                    getTokensResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.nav_reports),
                        getTokensResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else if (getTokensResponse != null &&
                    getTokensResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.nav_reports),
                        getTokensResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (getTokensResponse != null &&
                    getTokensResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(),
                        getResources().getString(R.string.nav_reports),
                        getTokensResponse.getResponseMessage(),
                        getFragmentManager());
            }else if (getTokensResponse != null &&
                    getTokensResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(getTokensResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            }  else {
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
    public void getTransactionsReportData(TransactionReportResponseList transactionReportResponseList) {

    }

    @Override
    public void getPaymentStatusReportData(ReportsDataResponce paymentReportResponseList) {

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

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
import com.cgg.pps.adapter.TransactionReportAdapter;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.TransactionReportFragmentBinding;
import com.cgg.pps.interfaces.TokenReportInterface;
import com.cgg.pps.model.request.reports.TransactionReportRequest;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokensResponse;
import com.cgg.pps.model.response.report.payment.ReportsDataResponce;
import com.cgg.pps.model.response.report.transaction.TransactionReportResponse;
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
import java.util.List;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

@SuppressLint("SimpleDateFormat")
public class TransactionReportFragment extends Fragment implements AdapterView.OnItemSelectedListener,TokenReportInterface {
    private PPCUserDetails ppcUserDetails;
    private TransactionReportAdapter transactionReportAdapter;
    private TransactionReportFragmentBinding binding;
    private ReportPresenter reportPresenter;
    private CustomProgressDialog customProgressDialog;
    private TransactionReportResponseList reportResponseList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.transaction_report_fragment, container, false);
        reportPresenter = new ReportPresenter();
        reportPresenter.attachView(this);
        customProgressDialog = new CustomProgressDialog(getActivity());
        getPreferenceData();
        if (ConnectionDetector.isConnectedToInternet(getActivity())) {
            callTransactionReportTask();
        } else {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.nav_reports),
                    getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.WARNING),false);
        }
        binding.inputSearch.setQueryHint(getString(R.string.search_by_tkn_num));
        search(binding.inputSearch);
        binding.transSpinner.setOnItemSelectedListener(this);
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
                    transactionReportAdapter.getFilter().filter(newText);
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


    private void callTransactionReportTask() {
        try {
            TransactionReportRequest transactionReportRequest = new TransactionReportRequest();
            transactionReportRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
            transactionReportRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));
            transactionReportRequest.setTokenID("00"); // static
            transactionReportRequest.setTransactionID("00"); //static
            transactionReportRequest.setTransactionRowID("00"); //static
            transactionReportRequest.setTransactionStatus("00"); //static
            if (customProgressDialog != null)
                customProgressDialog.show();
            reportPresenter.getTransactionsReports(transactionReportRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        binding.inputSearch.setQuery("", false);
        binding.inputSearch.clearFocus();
        if(reportResponseList!=null &&
                reportResponseList.getGetTranscations()!=null &&
                reportResponseList.getGetTranscations().size()>0){

            List<TransactionReportResponse> tempArrayList = new ArrayList<TransactionReportResponse>();
            switch (pos) {
                case 0:
                    tempArrayList = reportResponseList.getGetTranscations();
                    transactionReportAdapter = new TransactionReportAdapter(getActivity(), reportResponseList.getGetTranscations());
                    binding.transactionRV.setAdapter(transactionReportAdapter);
                    break;
                case 1:
                    for(TransactionReportResponse transactionReportResponse:  reportResponseList.getGetTranscations()){
                        if(transactionReportResponse.getStatusID().toLowerCase().equals("M".toLowerCase())){
                            tempArrayList.add(transactionReportResponse);
                        }
                    }
                    transactionReportAdapter = new TransactionReportAdapter(getActivity(), tempArrayList);
                    binding.transactionRV.setAdapter(transactionReportAdapter);
                    break;
                case 2:
                    for(TransactionReportResponse transactionReportResponse:  reportResponseList.getGetTranscations()){
                        if(transactionReportResponse.getStatusID().toLowerCase().equals("T".toLowerCase())){
                            tempArrayList.add(transactionReportResponse);
                        }
                    }
                    transactionReportAdapter = new TransactionReportAdapter(getActivity(), tempArrayList);
                    binding.transactionRV.setAdapter(transactionReportAdapter);
                    break;
                case 3:
                    for(TransactionReportResponse transactionReportResponse:  reportResponseList.getGetTranscations()){
                        if(transactionReportResponse.getStatusID().toLowerCase().equals("N".toLowerCase())){
                            tempArrayList.add(transactionReportResponse);
                        }
                    }
                    transactionReportAdapter = new TransactionReportAdapter(getActivity(), tempArrayList);
                    binding.transactionRV.setAdapter(transactionReportAdapter);
                    break;
                case 4:
                    for(TransactionReportResponse transactionReportResponse:  reportResponseList.getGetTranscations()){
                        if(transactionReportResponse.getStatusID().toLowerCase().equals("F".toLowerCase())){
                            tempArrayList.add(transactionReportResponse);
                        }
                    }
                    transactionReportAdapter = new TransactionReportAdapter(getActivity(), tempArrayList);
                    binding.transactionRV.setAdapter(transactionReportAdapter);
                    break;
                case 5:
                    for(TransactionReportResponse transactionReportResponse:  reportResponseList.getGetTranscations()){
                        if(transactionReportResponse.getStatusID().toLowerCase().equals("I".toLowerCase())){
                            tempArrayList.add(transactionReportResponse);
                        }
                    }
                    transactionReportAdapter = new TransactionReportAdapter(getActivity(), tempArrayList);
                    binding.transactionRV.setAdapter(transactionReportAdapter);
                    break;
                case 6:
                    for(TransactionReportResponse transactionReportResponse:  reportResponseList.getGetTranscations()){
                        if(transactionReportResponse.getStatusID().toLowerCase().equals("O".toLowerCase())){
                            tempArrayList.add(transactionReportResponse);
                        }
                    }
                    transactionReportAdapter = new TransactionReportAdapter(getActivity(), tempArrayList);
                    binding.transactionRV.setAdapter(transactionReportAdapter);
                    break;
                case 7:
                    for(TransactionReportResponse transactionReportResponse:  reportResponseList.getGetTranscations()){
                        if(transactionReportResponse.getStatusID().toLowerCase().equals("P".toLowerCase())){
                            tempArrayList.add(transactionReportResponse);
                        }
                    }
                    transactionReportAdapter = new TransactionReportAdapter(getActivity(), tempArrayList);
                    binding.transactionRV.setAdapter(transactionReportAdapter);
                    break;
                case 8:
                    for(TransactionReportResponse transactionReportResponse:  reportResponseList.getGetTranscations()){
                        if(transactionReportResponse.getStatusID().toLowerCase().equals("R".toLowerCase())){
                            tempArrayList.add(transactionReportResponse);
                        }
                    }
                    transactionReportAdapter = new TransactionReportAdapter(getActivity(), tempArrayList);
                    binding.transactionRV.setAdapter(transactionReportAdapter);
                    break;
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void getPaymentStatusReportData(ReportsDataResponce paymentReportResponseList) {

    }

    @Override
    public void getTokensResponseData(GetTokensResponse getTokensFarmerResponse) {

    }

    @Override
    public void getTransactionsReportData(TransactionReportResponseList transactionReportResponseList) {
        try {
            if (transactionReportResponseList != null && transactionReportResponseList.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (transactionReportResponseList.getGetTranscations() != null && transactionReportResponseList.getGetTranscations().size() > 0) {
                    reportResponseList = transactionReportResponseList;
                    transactionReportAdapter = new TransactionReportAdapter(getActivity(), transactionReportResponseList.getGetTranscations());
                    binding.transactionRV.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                    binding.transactionRV.setAdapter(transactionReportAdapter);
                } else {
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.nav_reports),
                            transactionReportResponseList.getResponseMessage(),
                            getString(R.string.ERROR), false);
                }
            } else if (transactionReportResponseList != null &&
                    transactionReportResponseList.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.nav_reports),
                        transactionReportResponseList.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else if (transactionReportResponseList != null &&
                    transactionReportResponseList.getStatusCode() == AppConstants.NO_DAT_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.nav_reports),
                        transactionReportResponseList.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (transactionReportResponseList != null &&
                    transactionReportResponseList.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(),
                        getResources().getString(R.string.nav_reports),
                        transactionReportResponseList.getResponseMessage(),
                        getFragmentManager());
            }else if (transactionReportResponseList != null &&
                    transactionReportResponseList.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(transactionReportResponseList.getResponseMessage(), getActivity(), getFragmentManager(), true);

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

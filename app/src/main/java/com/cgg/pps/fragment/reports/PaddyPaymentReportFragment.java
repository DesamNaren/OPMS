package com.cgg.pps.fragment.reports;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cgg.pps.R;
import com.cgg.pps.adapter.PaddyPaymentReportAdapter;
import com.cgg.pps.adapter.PaddyPdfReportAdapter;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.PaddyPaymentReportsFragmentBinding;
import com.cgg.pps.interfaces.PaddyPaymentReportInterface;
import com.cgg.pps.model.request.reports.PaymentInfoRequest;
import com.cgg.pps.model.response.report.payment_report.PaddyPaymentInfoResponse;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.presenter.PaddyPaymentPresenter;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.ConnectionDetector;
import com.cgg.pps.util.CustomProgressDialog;
import com.cgg.pps.util.PdfGenerator;
import com.cgg.pps.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class PaddyPaymentReportFragment extends Fragment implements PaddyPaymentReportInterface {

    private PPCUserDetails ppcUserDetails;
    PaddyPaymentReportsFragmentBinding binding;
    private PaddyPaymentPresenter paddyPaymentPresenter;
    private CustomProgressDialog customProgressDialog;
    private PaddyPaymentReportAdapter paddyPaymentReportAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.paddy_payment_reports_fragment, container, false);
        paddyPaymentPresenter = new PaddyPaymentPresenter();
        paddyPaymentPresenter.attachView(this);
        customProgressDialog = new CustomProgressDialog(getActivity());
        getPreferenceData();

        binding.inputSearch.setQueryHint(getString(R.string.search_by_registration_id));
        binding.fromDateLL.setOnClickListener(onClick);
        binding.toDateLL.setOnClickListener(onClick);
        binding.pdfGenerateBtn.setOnClickListener(onClick);
        search(binding.inputSearch);
        return binding.getRoot();
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fromDateLL:

                    showFromDatePickerDialog();
                    break;
                case R.id.toDateLL:

                    if (!TextUtils.isEmpty(binding.fromDateTV.getText().toString())) {
                        showToDatePickerDialog();
                    } else {
                        Utils.customAlert(getActivity(),
                                getResources().getString(R.string.nav_reports),
                                "Please select from date",
                                getResources().getString(R.string.WARNING),
                                false);
                    }
                    break;
                case R.id.pdfGenerateBtn:

                    if (paddyPaymentReportAdapter != null
                            && paddyPaymentReportAdapter.getItemCount() > 0) {

                        PaddyPdfReportAdapter paddyPdfReportAdapter = new PaddyPdfReportAdapter(getActivity(),
                                paddyPaymentInfoResponseMain.getPaddyPaymentOutput(),
                                binding.fromDateTV.getText().toString()
                                , binding.toDateTV.getText().toString());

//                        PaddyPdfReportAdapterTemp paddyPdfReportAdapter = new PaddyPdfReportAdapterTemp(getActivity(),
//                                paddyPaymentInfoResponseMain.getPaddyPaymentOutput(),
//                                binding.fromDateTV.getText().toString()
//                                , binding.toDateTV.getText().toString());
                        binding.paymentRV1.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.paymentRV1.setAdapter(paddyPdfReportAdapter);
                        PdfGenerator.CallPDFGenerator(binding.paymentRV1, getActivity(),
                                binding.fromDateTV.getText().toString(),
                                binding.toDateTV.getText().toString());
                    }
                    break;
            }
        }
    };

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    if (paddyPaymentReportAdapter != null) {
                        paddyPaymentReportAdapter.getFilter().filter(newText);
                    }
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

    private void callPaymentReportTask() {
        try {
            PaymentInfoRequest paymentInfoRequest = new PaymentInfoRequest();
            paymentInfoRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
            paymentInfoRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));
            paymentInfoRequest.setDistrictID(String.valueOf(ppcUserDetails.getDistrictID()));
            paymentInfoRequest.setFromDate(binding.fromDateTV.getText().toString());
            paymentInfoRequest.setToDate(binding.toDateTV.getText().toString());

            if (customProgressDialog != null)
                customProgressDialog.show();
            paddyPaymentPresenter.getPaddyPaymentReports(paymentInfoRequest);
        } catch (Exception e) {
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
            if (paddyPaymentPresenter != null) {
                paddyPaymentPresenter.detachView();
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

    private PaddyPaymentInfoResponse paddyPaymentInfoResponseMain;

    @Override
    public void getPaddyPaymentReportData(PaddyPaymentInfoResponse paddyPaymentInfoResponse) {

        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
        try {
            if (paddyPaymentInfoResponse != null && paddyPaymentInfoResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (paddyPaymentInfoResponse.getPaddyPaymentOutput() != null
                        && paddyPaymentInfoResponse.getPaddyPaymentOutput().size() > 0) {
                    paddyPaymentInfoResponseMain = paddyPaymentInfoResponse;
                    binding.paymentRV.setVisibility(View.VISIBLE);
                    binding.pdfGenerateBtn.setVisibility(View.VISIBLE);
                    paddyPaymentReportAdapter = new PaddyPaymentReportAdapter(getActivity(),
                            paddyPaymentInfoResponse.getPaddyPaymentOutput());
                    binding.paymentRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    binding.paymentRV.setAdapter(paddyPaymentReportAdapter);
                } else {
                    binding.pdfGenerateBtn.setVisibility(View.GONE);
                    binding.paymentRV.setVisibility(View.GONE);
                    Utils.customAlert(getActivity(),
                            getResources().getString(R.string.nav_reports),
                            paddyPaymentInfoResponse.getResponseMessage(),
                            getString(R.string.ERROR), false);
                }
            } else if (paddyPaymentInfoResponse != null &&
                    paddyPaymentInfoResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                binding.pdfGenerateBtn.setVisibility(View.GONE);
                binding.paymentRV.setVisibility(View.GONE);
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.nav_reports),
                        paddyPaymentInfoResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);

            } else if (paddyPaymentInfoResponse != null &&
                    paddyPaymentInfoResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                binding.pdfGenerateBtn.setVisibility(View.GONE);
                binding.paymentRV.setVisibility(View.GONE);
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.nav_reports),
                        paddyPaymentInfoResponse.getResponseMessage(),
                        getString(R.string.ERROR), false);
            } else if (paddyPaymentInfoResponse != null &&
                    paddyPaymentInfoResponse.getStatusCode() == AppConstants.SESSION_CODE) {

                Utils.ShowSessionAlert(getActivity(),
                        getResources().getString(R.string.nav_reports),
                        "Session expired, Please Re-login",
                        getFragmentManager());
            } else if (paddyPaymentInfoResponse != null &&
                    paddyPaymentInfoResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(paddyPaymentInfoResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

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

    private int year;
    private int month;
    private int day;
    private DatePickerDialog fromDatePicker;
    private DatePickerDialog toDatePicker;

    private void showFromDatePickerDialog() {
        try {
            paddyPaymentReportAdapter = null;
            binding.toDateTV.setText("");
            binding.pdfGenerateBtn.setVisibility(View.GONE);
            binding.paymentRV.setVisibility(View.GONE);
            if (getActivity() != null) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                fromDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        binding.pdfGenerateBtn.setVisibility(View.GONE);
                        binding.paymentRV.setVisibility(View.GONE);
                        binding.fromDateTV.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, year, month, day);
                fromDatePicker.getDatePicker().setMaxDate(c.getTimeInMillis());
                fromDatePicker.show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showToDatePickerDialog() {
        try {
            if (getActivity() != null) {

                paddyPaymentReportAdapter = null;
                binding.toDateTV.setText("");
                binding.pdfGenerateBtn.setVisibility(View.GONE);
                binding.paymentRV.setVisibility(View.GONE);

                final Calendar curCal = Calendar.getInstance();
                final Calendar c = Calendar.getInstance();
                Date curDate = c.getTime();
                year = fromDatePicker.getDatePicker().getYear();
                month = fromDatePicker.getDatePicker().getMonth();
                day = fromDatePicker.getDatePicker().getDayOfMonth();
                c.set(year, month, day);

                toDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        binding.toDateTV.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        if (ConnectionDetector.isConnectedToInternet(getActivity())) {
                            callPaymentReportTask();
                        } else {
                            Utils.customAlert(getActivity(),
                                    getResources().getString(R.string.nav_reports),
                                    getResources().getString(R.string.no_internet),
                                    getResources().getString(R.string.WARNING), false);
                        }
                    }
                }, year, month, day);

                toDatePicker.getDatePicker().setMinDate(c.getTimeInMillis());
                c.add(Calendar.DAY_OF_MONTH, 7);
                Date prefDate = c.getTime();
                long time = 0;

                if (curDate.compareTo(prefDate) > 0) {
                    time = c.getTimeInMillis();
                } else if (curDate.compareTo(prefDate) < 0) {
                    time = curCal.getTimeInMillis();
                } else if (curDate.compareTo(prefDate) == 0) {
                    time = curCal.getTimeInMillis();
                }

                toDatePicker.getDatePicker().setMaxDate(time);
                toDatePicker.show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

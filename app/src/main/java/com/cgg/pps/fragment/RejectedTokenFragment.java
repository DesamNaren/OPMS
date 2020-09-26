package com.cgg.pps.fragment;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.cgg.pps.R;
import com.cgg.pps.adapter.FaqRejectionAdapter;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.RejectedTokenFragmentBinding;
import com.cgg.pps.interfaces.RejectedTokenInterface;
import com.cgg.pps.model.request.rejectedtokenrequest.RejectedTokenRequest;
import com.cgg.pps.model.response.enablefaqresponse.EnableFAQResponse;
import com.cgg.pps.model.response.rejectedtokenresponse.RejectedTokenResponse;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.presenter.RejectedTokenPresenter;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.ConnectionDetector;
import com.cgg.pps.util.CustomProgressDialog;
import com.cgg.pps.util.Utils;
import com.cgg.pps.view.DashboardActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

public class RejectedTokenFragment extends Fragment implements RejectedTokenInterface {
    private RejectedTokenFragmentBinding binding;
    private RejectedTokenPresenter rejectedTokenPresenter;
    private RejectedTokenResponse rejTokenResponse;
    private FaqRejectionAdapter faqRejectionAdapter;
    private PPCUserDetails ppcUserDetails;
    CustomProgressDialog customProgressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.rejected_token_fragment, container, false);
        getPreferenceData();
        rejectedTokenPresenter = new RejectedTokenPresenter();
        rejectedTokenPresenter.attachView(this);
        customProgressDialog = new CustomProgressDialog(getActivity());
        if (ConnectionDetector.isConnectedToInternet(getActivity())) {
            RejectedTokenRequest rejectedTokenRequest = getRejectedTokenRequest();
            if(customProgressDialog != null &&  !customProgressDialog.isShowing())
                customProgressDialog.show();
            rejectedTokenPresenter.GetRejectedTokenData(rejectedTokenRequest);
        } else {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.rejected_faqs),
                    getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.WARNING),false);
        }

        setupSearchView();
        search(binding.inputSearch);

        binding.rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Utils.hideKeyboard(view);
                return false;
            }
        });

        return binding.getRoot();
    }

    public void onResume() {
        super.onResume();

        try {
            if (getActivity() != null)
                ((DashboardActivity) getActivity())
                        .setActionBarTitle(getResources().getString(R.string.rejected_faqs));

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }


    private void setupSearchView() {
        binding.inputSearch.setIconifiedByDefault(false);
        binding.inputSearch.setSubmitButtonEnabled(true);
        binding.inputSearch.setQueryHint("Search by token id or number or registration number");
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
                    faqRejectionAdapter.getFilter().filter(newText);
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
                            getResources().getString(R.string.rejected_faqs),
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

    private RejectedTokenRequest getRejectedTokenRequest() {
        RejectedTokenRequest rejectedTokenRequest = new RejectedTokenRequest();
        rejectedTokenRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
        rejectedTokenRequest.setPPCID(ppcUserDetails.getPPCID());
        return rejectedTokenRequest;
    }

    @Override
    public void GetRejectedTokenData(RejectedTokenResponse rejectedTokenResponse) {
        if(customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
        try {
            rejTokenResponse = rejectedTokenResponse;
            if (rejectedTokenResponse != null && rejectedTokenResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                binding.inputSearch.setVisibility(View.VISIBLE);
                faqRejectionAdapter = new FaqRejectionAdapter(getActivity(),
                        rejectedTokenResponse.getTokenRejectionOutput(),
                        rejectedTokenPresenter, ppcUserDetails,getActivity(),customProgressDialog);
                binding.rejectRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                binding.rejectRV.setAdapter(faqRejectionAdapter);
            } else if (rejectedTokenResponse != null && rejectedTokenResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                binding.inputSearch.setVisibility(View.GONE);
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.rejected_faqs),
                        rejectedTokenResponse.getResponseMessage(),
                        getResources().getString(R.string.ERROR),false);
            }  else if (rejectedTokenResponse != null && rejectedTokenResponse.getStatusCode() == AppConstants.NO_DAT_CODE) {
                binding.inputSearch.setVisibility(View.GONE);
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.rejected_faqs),
                        rejectedTokenResponse.getResponseMessage(),
                        getResources().getString(R.string.ERROR),false);
            } else if (rejectedTokenResponse != null && rejectedTokenResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.Reprint),
                        rejectedTokenResponse.getResponseMessage(), getFragmentManager());
            } else if (rejectedTokenResponse != null &&
                    rejectedTokenResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(rejectedTokenResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);

            }else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.rejected_faqs),
                        getResources().getString(R.string.server_not),
                        getResources().getString(R.string.ERROR),false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.rejected_faqs),
                    getResources().getString(R.string.something),
                    getResources().getString(R.string.ERROR),false);
            e.printStackTrace();
        }
    }

    @Override
    public void GetFAQEnableResponse(EnableFAQResponse enableFAQResponse) {
        if(customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
        try {
            if (enableFAQResponse != null && enableFAQResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                Fragment fragment = new RejectedTokenFragment();
                Utils.showAlertNavigateToFarmer1(getActivity(), fragment, getFragmentManager(),
                        getResources().getString(R.string.rejected_faqs),
                        enableFAQResponse.getResponseMessage(), true);

            } else if (enableFAQResponse != null && enableFAQResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.rejected_faqs),
                        enableFAQResponse.getResponseMessage(),
                        getResources().getString(R.string.ERROR),false);
            } else if (enableFAQResponse != null && enableFAQResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.ShowSessionAlert(getActivity(), getResources().getString(R.string.Reprint),
                        enableFAQResponse.getResponseMessage(), getFragmentManager());
            }else if (enableFAQResponse != null &&
                    enableFAQResponse.getStatusCode() == AppConstants.VERSION_CODE) {
                Utils.callPlayAlert(enableFAQResponse.getResponseMessage(), getActivity(), getFragmentManager(), true);
            } else {
                Utils.customAlert(getActivity(),
                        getResources().getString(R.string.rejected_faqs),
                        getResources().getString(R.string.server_not),
                        getResources().getString(R.string.ERROR),false);
            }
        } catch (Resources.NotFoundException e) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.rejected_faqs),
                    getResources().getString(R.string.something),
                    getResources().getString(R.string.ERROR),false);
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
            if (rejectedTokenPresenter != null) {
                rejectedTokenPresenter.detachView();
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
                    getResources().getString(R.string.rejected_faqs),
                    msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else if (code == AppConstants.EXCEPTION_CODE) {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.rejected_faqs),
                    getString(R.string.something) +
                            msg,
                    getResources().getString(R.string.ERROR),
                    false);
        } else {
            Utils.customAlert(getActivity(),
                    getResources().getString(R.string.rejected_faqs),
                    getString(R.string.something),
                    getResources().getString(R.string.ERROR),
                    false);
        }
    }
}

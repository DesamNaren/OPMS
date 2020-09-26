package com.cgg.pps.view.reports;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.databinding.ActivityTokenWiseDetailReportBinding;
import com.cgg.pps.interfaces.BBInterface;
import com.cgg.pps.model.response.devicemgmt.bankbranch.BranchEntity;
import com.cgg.pps.model.response.devicemgmt.masterbank.BankEntity;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokenOutput;
import com.cgg.pps.room.repository.BBRepository;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.Utils;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class TokenDetailReportActivity extends AppCompatActivity implements BBInterface {
    private GetTokenOutput tokenReportResponse;
    private ActivityTokenWiseDetailReportBinding binding;
    private BBRepository bbRepository;
    private String cacheDate, currentDate;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_token_wise_detail_report);
        sharedPreferences = OPMSApplication.get(this).getPreferences();
        try {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setTitle("Token Wise Report");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            bbRepository = new BBRepository(TokenDetailReportActivity.this);
            getTokenData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void getTokenData() {
        try {
            Intent intent = getIntent();
            tokenReportResponse = (GetTokenOutput) intent.getSerializableExtra("TOKEN_DATA");

            if (tokenReportResponse == null) {
                Utils.customAlert(TokenDetailReportActivity.this,
                        getResources().getString(R.string.nav_reports),
                        getResources().getString(R.string.something),
                        getString(R.string.ERROR), false);
            } else {
                bindTokenData(tokenReportResponse);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void bindTokenData(GetTokenOutput tokenReportResponse) {
        try {
            if (tokenReportResponse.getTokenStatus() == 0 || tokenReportResponse.getTokenStatus() == 1) {
                binding.tokenCV.setVisibility(View.VISIBLE);
            }

            if (tokenReportResponse.getTokenStatus() == 2) {
                binding.tokenCV.setVisibility(View.VISIBLE);
                binding.bankCV.setVisibility(View.VISIBLE);
                binding.paddyCV.setVisibility(View.VISIBLE);
            }

            if (tokenReportResponse.getTokenStatus() == 3) {
                binding.tokenCV.setVisibility(View.VISIBLE);
                binding.bankCV.setVisibility(View.VISIBLE);
                binding.paddyCV.setVisibility(View.VISIBLE);
                binding.faqCV.setVisibility(View.VISIBLE);
            }

            if (tokenReportResponse.getTokenStatus() == 4) {
                binding.tokenCV.setVisibility(View.VISIBLE);
                binding.bankCV.setVisibility(View.VISIBLE);
                binding.paddyCV.setVisibility(View.VISIBLE);
                binding.faqCV.setVisibility(View.VISIBLE);
                binding.gunnyCV.setVisibility(View.VISIBLE);
            }

            if (tokenReportResponse.getTokenStatus() == 5) {
                binding.tokenCV.setVisibility(View.VISIBLE);
                binding.bankCV.setVisibility(View.VISIBLE);
                binding.paddyCV.setVisibility(View.VISIBLE);
                binding.faqCV.setVisibility(View.VISIBLE);
                binding.gunnyCV.setVisibility(View.VISIBLE);
                binding.proCV.setVisibility(View.VISIBLE);
            }

            if (tokenReportResponse.getTokenStatus() == 6) {
                binding.tokenCV.setVisibility(View.VISIBLE);
                binding.bankCV.setVisibility(View.VISIBLE);
                binding.paddyCV.setVisibility(View.VISIBLE);
                binding.faqCV.setVisibility(View.VISIBLE);
                binding.gunnyCV.setVisibility(View.VISIBLE);
                binding.proCV.setVisibility(View.VISIBLE);
                binding.tcCV.setVisibility(View.VISIBLE);
            }

            if (tokenReportResponse.getTokenStatus() == 7) {
                binding.tokenCV.setVisibility(View.VISIBLE);
                binding.bankCV.setVisibility(View.VISIBLE);
                binding.paddyCV.setVisibility(View.VISIBLE);
                binding.faqCV.setVisibility(View.VISIBLE);
                binding.gunnyCV.setVisibility(View.VISIBLE);
                binding.proCV.setVisibility(View.VISIBLE);
                binding.tcCV.setVisibility(View.VISIBLE);
                binding.statusCV.setVisibility(View.VISIBLE);
            }

            binCompleteData(tokenReportResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void binCompleteData(GetTokenOutput tokenReportResponse) {
        try {
            binding.tokenIdTV.setText("" + tokenReportResponse.getTokenID());
            binding.tokenNumTV.setText("" + tokenReportResponse.getTokenNo());
            binding.mobNumTV.setText("" + tokenReportResponse.getMobile());
            binding.farmerNameTV.setText(tokenReportResponse.getFarmerName());
            binding.appDtaeTV.setText(tokenReportResponse.getAppointmentDate());
            binding.transactionNumTV.setText(tokenReportResponse.getFarmerTransactionID());
            bbRepository.getBankName(this,
                    "" + tokenReportResponse.getBankId().toString().trim());

            bbRepository.getBranchName(this,
                    tokenReportResponse.getBankId().toString().trim(),
                    tokenReportResponse.getBranchId().toString().trim());

            binding.ifscTV.setText(tokenReportResponse.getIFSCCode());
            binding.nameAs.setText(tokenReportResponse.getNameasperBank());
            binding.bankAccTV.setText(tokenReportResponse.getBankAccountNo());

            binding.regNumTV.setText(tokenReportResponse.getRegistrationNo());
            binding.paddyTypeTV.setText(tokenReportResponse.getPaddyType());
            binding.appQtyTV.setText(tokenReportResponse.getApproxmateQuantity() + " Qntls");
            binding.culLandTV.setText(tokenReportResponse.getTotalCultivableArea() + " Acres");

            binding.faqTypeTV.setText(tokenReportResponse.getFAQType());
            binding.faqDateTV.setText(tokenReportResponse.getFAQDate());
            binding.wasTV.setText("" + tokenReportResponse.getWastage());
            binding.othTv.setText("" + tokenReportResponse.getOtherEatingGrains());
            binding.unTV.setText("" + tokenReportResponse.getUnmaturedGrains());
            binding.damTV.setText("" + tokenReportResponse.getDamagedGrains());
            binding.lowTV.setText("" + tokenReportResponse.getLowGradeGrain());
            binding.moiTV.setText("" + tokenReportResponse.getMoisture());


            binding.tokNewTV.setText("" + tokenReportResponse.getTokenNewBags());
            binding.tokOldTV.setText("" + tokenReportResponse.getTokenOldBags());
            binding.tokTotTV.setText("" + tokenReportResponse.getTokenTotalBags());


            binding.prcNewTV.setText("" + tokenReportResponse.getProcuredNewBags());
            binding.prcOldTV.setText("" + tokenReportResponse.getProcuredOldBags());
            binding.prcTotTV.setText("" + (tokenReportResponse.getProcuredNewBags() + tokenReportResponse.getProcuredOldBags()));
            binding.prcQtyTV.setText("" + tokenReportResponse.getProcuredQuantity());
            binding.totAmtTV.setText("" + tokenReportResponse.getTotalAmount());


            binding.trcNewTV.setText("" + tokenReportResponse.getTRNewBags());
            binding.trcOldTV.setText("" + tokenReportResponse.getTROldBags());
            binding.trcTotTV.setText("" + tokenReportResponse.getTRTotalBags());

            binding.curStatusTV.setText("" + tokenReportResponse.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getAllBanks(List<BankEntity> bankEntities) {
        try {
            if (bankEntities.size() > 0) {
                bbRepository.getAllBranches(this);
            } else {
                showAlertNavigateToMasterDownloads(TokenDetailReportActivity.this,
                        getResources().getString(R.string.nav_reports),
                        "No banks data found, Please download");

            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllBranches(List<BranchEntity> branchEntities) {
        try {
            if (branchEntities.size() > 0) {

            } else {
                showAlertNavigateToMasterDownloads(TokenDetailReportActivity.this,
                        getResources().getString(R.string.nav_reports),
                        "No branches data found, Please download");
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void showAlertNavigateToMasterDownloads(AppCompatActivity activity, String title, String alertMsg) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_alert_information);
                dialog.setCancelable(false);
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(activity.getResources().getString(R.string.app_name) + ": " + title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(alertMsg);
                Button yes = dialog.findViewById(R.id.btDialogYes);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if (!dialog.isShowing())
                    dialog.show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataNotAvailable() {

    }

    @Override
    public void bankCount(int cnt) {

    }

    @Override
    public void branchCount(int cnt) {

    }

    @Override
    public void getBankName(String bankName) {
        binding.bankTV.setText(bankName);
    }

    @Override
    public void getBranchName(String bankName) {
        binding.branchTV.setText(bankName);
    }

    @Override
    public void socialCount(int cnt) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            boolean isAutomatic = Utils.isTimeAutomatic(this);
            if (!isAutomatic) {
                Utils.customTimeAlert(this,
                        getResources().getString(R.string.nav_reports),
                        getString(R.string.date_time));
                return;
            }

            currentDate = Utils.getCurrentDate();
            cacheDate = sharedPreferences.getString(AppConstants.CACHE_DATE, "");
            if (!TextUtils.isEmpty(cacheDate) && cacheDate != null) {
                if (!cacheDate.equalsIgnoreCase(currentDate)) {
                    Utils.ShowDeviceSessionAlert(this,
                            getResources().getString(R.string.nav_reports),
                            getString(R.string.session_expire),
                            getSupportFragmentManager());
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            cacheDate = currentDate;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(AppConstants.CACHE_DATE, cacheDate);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

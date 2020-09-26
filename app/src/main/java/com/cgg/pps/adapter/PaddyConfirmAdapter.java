package com.cgg.pps.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgg.pps.R;
import com.cgg.pps.model.request.farmer.farmersubmit.FarmerLandXMLData;
import com.cgg.pps.model.request.farmer.farmersubmit.FarmerSubmitRequest;
import com.cgg.pps.model.request.farmer.farmersubmit.FarmerXMLData;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.presenter.FarRegPresenter;
import com.cgg.pps.util.ConnectionDetector;
import com.cgg.pps.util.CustomProgressDialog;
import com.cgg.pps.util.Utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PaddyConfirmAdapter extends RecyclerView.Adapter<PaddyConfirmAdapter.RejectionHolder> {

    private Context context;
    private FarmerSubmitRequest farmerSubmitRequest;
    private Dialog dialog;
    private FarRegPresenter farRegPresenter;
    private PPCUserDetails ppcUserDetails;
    private Activity activity;
    private Double finalProcLand = 0.0, finalProcQty = 0.0;
    private CustomProgressDialog customProgressDialog;

    public PaddyConfirmAdapter(Context context, FarmerSubmitRequest farmerSubmitRequest,
                               Dialog dialog, FarRegPresenter farRegPresenter,
                               PPCUserDetails ppcUserDetails, Activity activity
            , CustomProgressDialog customProgressDialog) {
        this.context = context;
        this.farmerSubmitRequest = farmerSubmitRequest;
        this.dialog = dialog;
        this.activity = activity;
        this.farRegPresenter = farRegPresenter;
        this.ppcUserDetails = ppcUserDetails;
        this.customProgressDialog = customProgressDialog;
    }

    @NonNull
    @Override
    public RejectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.paddy_confirm_row, parent, false);
        return new RejectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RejectionHolder holder, int position) {

        try {
            FarmerLandXMLData farmerLandXMLData = farmerSubmitRequest.getLandXMLDataArrayList().get(position);
            holder.surveyNum.setText(String.valueOf(farmerLandXMLData.getSurveyNo()));

            Integer landAcres = Double.valueOf(farmerLandXMLData.getTotalCultivableArea()).intValue();
            String guntas = "";
            String guntasStr;
            Double guntaFinalVal = 0.0;

            String totLandStr = String.valueOf(farmerLandXMLData.getTotalCultivableArea());

            if (totLandStr.contains(".")) {
                String[] str = totLandStr.split("\\.");
                if (str.length > 0) {
                    guntas = String.valueOf(str[1]);
                    guntasStr = "0.".concat(guntas);
                    guntaFinalVal = Double.valueOf(guntasStr) * 40;
                }
            }
            guntaFinalVal = Double.parseDouble(String.format("%.2f", guntaFinalVal));

            String str = landAcres + " acre(s) " + guntaFinalVal + " guntas";

            finalProcLand += Double.valueOf(farmerLandXMLData.getTotalCultivableArea());

            holder.cultLand.setText(str);

            Double landQty = Double.valueOf(farmerLandXMLData.getTotalCultivableArea()) * Integer.valueOf(farmerLandXMLData.getYieldperAcre());
            landQty = Double.parseDouble(String.format("%.2f", landQty));
            holder.cultQty.setText(String.valueOf(landQty));

            finalProcQty += landQty;

            if (holder.getAdapterPosition() + 1 == farmerSubmitRequest.getLandXMLDataArrayList().size()) {
                holder.total_layout.setVisibility(View.VISIBLE);
                holder.btnLayout.setVisibility(View.VISIBLE);

                finalProcQty = Double.parseDouble(String.format("%.2f", finalProcQty));
                holder.totQty.setText(String.valueOf(finalProcQty));

                Integer finalLandAcres = finalProcLand.intValue();
                String finalGuntas = "";
                String finalGuntasStr;
                Double finalGuntaFinalVal = 0.0;

                String finalTotLandStr = String.valueOf(finalProcLand);

                if (finalTotLandStr.contains(".")) {
                    String[] str1 = finalTotLandStr.split("\\.");
                    if (str1.length > 0) {
                        finalGuntas = String.valueOf(str1[1]);
                        finalGuntasStr = "0.".concat(finalGuntas);
                        finalGuntaFinalVal = Double.valueOf(finalGuntasStr) * 40;
                    }
                }
                finalGuntaFinalVal = Double.parseDouble(String.format("%.2f", finalGuntaFinalVal));

                String str1 = finalLandAcres + " acre(s) " + finalGuntaFinalVal + " guntas";
                holder.totLand.setText(str1);

            }


            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            holder.proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (ConnectionDetector.isConnectedToInternet(context)) {
                            FarmerXMLData farmerXMLData = farmerSubmitRequest.getFarmerXMLData();
                            farmerXMLData.setApproxmateQuantity(String.valueOf(finalProcQty));
                            farmerSubmitRequest.setFarmerXMLData(farmerXMLData);

                            StringBuilder finalString = farmerSubmitReqData(farmerSubmitRequest);
                            if (finalString != null) {
                                farmerSubmitRequest.setpPCID(String.valueOf(ppcUserDetails.getPPCID()));
                                farmerSubmitRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                                farmerSubmitRequest.setXmlFarmerRegistrationData(String.valueOf(finalString));

                                if (customProgressDialog != null && !customProgressDialog.isShowing())
                                    customProgressDialog.show();

                                farRegPresenter.FarmerSubmit(farmerSubmitRequest);
                            }


                        } else {
                            Utils.customAlert(activity,
                                    activity.getResources().getString(R.string.FarmerRegistration),
                                    activity.getResources().getString(R.string.no_internet),
                                    activity.getResources().getString(R.string.WARNING), false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return farmerSubmitRequest.getLandXMLDataArrayList() != null ? farmerSubmitRequest.getLandXMLDataArrayList().size() : 0;
    }

    class RejectionHolder extends RecyclerView.ViewHolder {

        private TextView surveyNum, cultLand, cultQty, totQty, totLand;
        private LinearLayout total_layout, btnLayout;

        private Button cancel, proceed;

        RejectionHolder(@NonNull View itemView) {
            super(itemView);
            surveyNum = itemView.findViewById(R.id.surveyNum);
            cultLand = itemView.findViewById(R.id.cultLand);
            cultQty = itemView.findViewById(R.id.cultQty);
            total_layout = itemView.findViewById(R.id.total_layout);
            totLand = itemView.findViewById(R.id.totLand);
            totQty = itemView.findViewById(R.id.totQty);
            btnLayout = itemView.findViewById(R.id.btnLayout);
            cancel = itemView.findViewById(R.id.cancelBtn);
            proceed = itemView.findViewById(R.id.paddyProceedBtn);
        }
    }

    private StringBuilder farmerSubmitReqData(FarmerSubmitRequest farmerSubmitRequest) {
        final StringBuilder xmlDoc = new StringBuilder();
        try {
            String tokenNum = farmerSubmitRequest.getFarmerXMLData().getTokenNo();
            String tokenID = farmerSubmitRequest.getFarmerXMLData().getTokenID();
            String farmerName = farmerSubmitRequest.getFarmerXMLData().getFarmerName();
            String regNo = farmerSubmitRequest.getFarmerXMLData().getRegistrationNo();
            String regId = farmerSubmitRequest.getFarmerXMLData().getRegistrationID();
            String natureType = "0";
            String gender = farmerSubmitRequest.getFarmerXMLData().getGender();
            String age = farmerSubmitRequest.getFarmerXMLData().getAge();
            String farmerFatherName = farmerSubmitRequest.getFarmerXMLData().getFarmerFatherName();
            String doorNo = farmerSubmitRequest.getFarmerXMLData().getFDoorNo();
            String landmark = farmerSubmitRequest.getFarmerXMLData().getFLandMark();
            String distID = farmerSubmitRequest.getFarmerXMLData().getFDistrictID();
            String manID = farmerSubmitRequest.getFarmerXMLData().getFMandalID();
            String vilID = farmerSubmitRequest.getFarmerXMLData().getFVillageID();
            String pincode = farmerSubmitRequest.getFarmerXMLData().getFPincode();
            String mobile = farmerSubmitRequest.getFarmerXMLData().getFMobile();
            String email = farmerSubmitRequest.getFarmerXMLData().getFEmail();
            String bankID = farmerSubmitRequest.getFarmerXMLData().getBankID();
            String branchId = farmerSubmitRequest.getFarmerXMLData().getBranchID();
            String ifscCode = farmerSubmitRequest.getFarmerXMLData().getIFSCCode();
            String nameAsPerBank = farmerSubmitRequest.getFarmerXMLData().getNameasperBank();
            String bankAccNo = farmerSubmitRequest.getFarmerXMLData().getBankAccountNo();
            String createdDate = farmerSubmitRequest.getFarmerXMLData().getCreatedDate();
            String BankPassBook = farmerSubmitRequest.getFarmerXMLData().getBankPassBook();
            String PattadharPassbook = farmerSubmitRequest.getFarmerXMLData().getPattadharPassbook();
            String VROCertificate = farmerSubmitRequest.getFarmerXMLData().getVROCertificate();
            String paddyType = farmerSubmitRequest.getFarmerXMLData().getApproximatePaddyType();
            String appQty = farmerSubmitRequest.getFarmerXMLData().getApproxmateQuantity();


            xmlDoc.append("<FarmerRegistrationDetails>");
            xmlDoc.append("<ppc>");
            xmlDoc.append("<ppccode>" + ppcUserDetails.getPPCCode() + "</ppccode>"
                    + "<ppcid>" + ppcUserDetails.getPPCID() + "</ppcid>"
                    + "<SystemIP>" + ppcUserDetails.getPPCCode() + "</SystemIP>"
                    + "<seasonID>" + ppcUserDetails.getSeasonID() + "</seasonID>"
            );
            xmlDoc.append("</ppc>");
            xmlDoc.append("<Registration>");
            xmlDoc.append("<TokenNo>" + tokenNum + "</TokenNo>"
                    + "<TokenID>" + tokenID + "</TokenID>"
                    + "<FarmerName>" + farmerName + "</FarmerName>"
                    + "<RegistrationNo>" + regNo + "</RegistrationNo>"
                    + "<RegistrationID>" + regId + "</RegistrationID>"
                    + "<Enjoyer_Nature>" + natureType + "</Enjoyer_Nature>"
                    + "<Gender>" + gender + "</Gender>"
                    + "<Age>" + age + "</Age>"
                    + "<FarmerFatherName>" + farmerFatherName + "</FarmerFatherName>"
                    + "<FDoorNo>" + doorNo + "</FDoorNo>"
                    + "<FLandMark>" + landmark + "</FLandMark>"
                    + "<FDistrictID>" + distID + "</FDistrictID>"
                    + "<FMandalID>" + manID + "</FMandalID>"
                    + "<FVillageID>" + vilID + "</FVillageID>"
                    + "<FPincode>" + pincode + "</FPincode>"
                    + "<FMobile>" + mobile + "</FMobile>"
                    + "<FEmail>" + email + "</FEmail>"
                    + "<BankID>" + bankID + "</BankID>"
                    + "<BranchID>" + branchId + "</BranchID>"
                    + "<IFSCCode>" + ifscCode + "</IFSCCode>"
                    + "<NameasperBank>" + nameAsPerBank + "</NameasperBank>"
                    + "<BankAccountNo>" + bankAccNo + "</BankAccountNo>"
                    + "<CreatedDate>" + createdDate + "</CreatedDate>"
                    + "<BankPassBook>" + BankPassBook + "</BankPassBook>"
                    + "<PattadharPassbook>" + PattadharPassbook + "</PattadharPassbook>"
                    + "<VROCertificate>" + VROCertificate + "</VROCertificate>"
                    + "<ApproximatePaddyType>" + paddyType + "</ApproximatePaddyType>"
                    + "<ApproxmateQuantity>" + appQty + "</ApproxmateQuantity>"
            );
            xmlDoc.append("</Registration>");

            xmlDoc.append("<LandSet>");

            for (int x = 0; x < farmerSubmitRequest.getLandXMLDataArrayList().size(); x++) {
                xmlDoc.append("<FarmerLndDetails>");
                xmlDoc.append("<MainLandRowId>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getMainLandRowId() + "</MainLandRowId>"
                        + "<SubLandRowId>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getSubLandRowId() + "</SubLandRowId>"
                        + "<ProcRowId>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getProcRowId() + "</ProcRowId>"
                        + "<TokenId>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getTokenId() + "</TokenId>"
                        + "<DataSource>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getDataSource() + "</DataSource>"
                        + "<FarmerRegistrationId>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getFarmerRegistrationId() + "</FarmerRegistrationId>"
                        + "<OwnershipTypeId>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getOwnershipTypeId() + "</OwnershipTypeId>"
                        + "<LegalHeirRelationId>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getLegalHeirRelationId() + "</LegalHeirRelationId>"
                        + "<DistrictID>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getDistrictID() + "</DistrictID>"
                        + "<MandalID>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getMandalID() + "</MandalID>"
                        + "<VillageID>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getVillageID() + "</VillageID>"
                        + "<LandTypeId>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getLandTypeId() + "</LandTypeId>"
                        + "<LandOwnerName>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getLandOwnerName() + "</LandOwnerName>"
                        + "<LandOwnerAadhaar>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getLandOwnerAadhaar() + "</LandOwnerAadhaar>"
                        + "<SurveyNo>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getSurveyNo() + "</SurveyNo>"
                        + "<PassBookNo>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getPassBookNo() + "</PassBookNo>"
                        + "<TotalArea>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getTotalArea() + "</TotalArea>"
                        + "<YieldperAcre>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getYieldperAcre() + "</YieldperAcre>"
                        + "<TotalLeaseArea>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getTotalLeaseArea() + "</TotalLeaseArea>"
                        + "<TotalCultivableArea>" + farmerSubmitRequest.getLandXMLDataArrayList().get(x).getTotalCultivableArea() + "</TotalCultivableArea>"
                );

                xmlDoc.append("</FarmerLndDetails>");
            }

            xmlDoc.append("</LandSet>");

            xmlDoc.append("</FarmerRegistrationDetails>");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlDoc;
    }

}

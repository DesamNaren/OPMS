package com.cgg.pps.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cgg.pps.R;
import com.cgg.pps.model.request.enablefaqrequest.EnableFAQRequest;
import com.cgg.pps.model.response.rejectedtokenresponse.TokenRejectionOutput;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.presenter.RejectedTokenPresenter;
import com.cgg.pps.util.ConnectionDetector;
import com.cgg.pps.util.CustomProgressDialog;
import com.cgg.pps.util.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FaqRejectionAdapter extends RecyclerView.Adapter<FaqRejectionAdapter.RejectionHolder> implements Filterable {

    private Context context;
    private Activity activity;
    private List<TokenRejectionOutput> rejectionList;
    private List<TokenRejectionOutput> mFilteredList;
    private RejectedTokenPresenter rejectedTokenPresenter;
    private PPCUserDetails ppcUserDetails;
    private CustomProgressDialog customProgressDialog;

    public FaqRejectionAdapter(Context context, List<TokenRejectionOutput> rejectionList,
                               RejectedTokenPresenter rejectedTokenPresenter,
                               PPCUserDetails ppcUserDetails, Activity activity,CustomProgressDialog customProgressDialog) {
        this.context = context;
        this.rejectionList = rejectionList;
        mFilteredList = rejectionList;
        this.rejectedTokenPresenter = rejectedTokenPresenter;
        this.ppcUserDetails = ppcUserDetails;
        this.activity = activity;
        this.customProgressDialog = customProgressDialog;
    }

    @NonNull
    @Override
        public RejectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.faq_rejectionj_row, parent, false);
        return new RejectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RejectionHolder holder, int position) {
        try {
            TokenRejectionOutput tokenRejectionOutput = mFilteredList.get(position);
            holder.tokenIdTV.setText(String.valueOf(tokenRejectionOutput.getTokenID()));
            holder.tokenNumTV.setText(tokenRejectionOutput.getTokenNo());
            holder.regNumTv.setText(tokenRejectionOutput.getRegistrationNo());
            String paddyTypeStr;
            if (tokenRejectionOutput.getPaddyType().equals(1)) {
                paddyTypeStr = "Grade A";
            } else if (tokenRejectionOutput.getPaddyType().equals(2)) {
                paddyTypeStr = "Common";
            } else {
                paddyTypeStr = String.valueOf(tokenRejectionOutput.getPaddyType());
            }
            holder.paddyTypeTV.setText(paddyTypeStr);

            if(tokenRejectionOutput.getFAQDate()!=null) {
                String[] str = tokenRejectionOutput.getFAQDate().split(" ");
                String faqDate = Utils.getFaqRejectedDate(str[0]);
                holder.rejectedDateTv.setText(faqDate);
            }

            holder.QuantityTV.setText(tokenRejectionOutput.getQuantity() + " qtls");
            holder.rejectedRemarksTv.setText(tokenRejectionOutput.getRemarks());

            holder.approveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(holder.updatedRemarksET.getText().toString().trim())) {
                        holder.updatedRemarksET.setError("Enter remarks");
                    } else {
                        if (ConnectionDetector.isConnectedToInternet(context)) {
                            StringBuilder enableFaqXmlData = constructXMLData(tokenRejectionOutput, holder.updatedRemarksET.getText().toString());

                            EnableFAQRequest enableFAQRequest = new EnableFAQRequest();
                            enableFAQRequest.setAuthenticationID(ppcUserDetails.getAuthenticationID());
                            enableFAQRequest.setPPCID(String.valueOf(ppcUserDetails.getPPCID()));
                            enableFAQRequest.setXmlEnableToFAQ(String.valueOf(enableFaqXmlData));
                            if(customProgressDialog != null &&  !customProgressDialog.isShowing())
                                customProgressDialog.show();
                            rejectedTokenPresenter.GetEnableFaqTokenData(enableFAQRequest);


                        } else {
                            Utils.customAlert(activity,
                                    activity.getResources().getString(R.string.rejected_faqs),
                                    activity.getResources().getString(R.string.no_internet),
                                    activity.getResources().getString(R.string.WARNING),false);
                        }
                    }
                }
            });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private StringBuilder constructXMLData(TokenRejectionOutput tokenRejectionOutput, String remarks) {
        StringBuilder  xmlDoc = new StringBuilder();
        try {
            String curDate = Utils.getCurrentDateTime();

            xmlDoc.append("<FarmerFAQDetails>");
            xmlDoc.append("<ppc>");
            xmlDoc.append("<ppccode>" + ppcUserDetails.getPPCCode() + "</ppccode>"
                    + "<ppcid>" + ppcUserDetails.getPPCID() + "</ppcid>"
                    + "<SystemIP>" + ppcUserDetails.getPPCID() + "</SystemIP>"
                    + "<seasonID>" + ppcUserDetails.getSeasonID() + "</seasonID>"
            );
            xmlDoc.append("</ppc>");

            xmlDoc.append("<Registration>");
            xmlDoc.append("<TokenNo>" + tokenRejectionOutput.getTokenNo() + "</TokenNo>"
                    + "<TokenID>" + tokenRejectionOutput.getTokenID() + "</TokenID>"
                    + "<RegistrationNo>" + tokenRejectionOutput.getRegistrationNo() + "</RegistrationNo>"
                    + "<RegistrationID>" + tokenRejectionOutput.getFarmerRegID() + "</RegistrationID>"
            );
            xmlDoc.append("</Registration>");

            xmlDoc.append("<FAQ>");
            xmlDoc.append("<Remarks>" + remarks + "</Remarks>"
                    + "<FAQEnabledDate>" + curDate + "</FAQEnabledDate>"
                    + "<CreatedDate>" + curDate + "</CreatedDate>"
            );
            xmlDoc.append("</FAQ>");
            xmlDoc.append("</FarmerFAQDetails>");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlDoc;
    }


    @Override
    public int getItemCount() {
        return mFilteredList != null ? mFilteredList.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = null;
                try {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mFilteredList = rejectionList;
                    } else {
                        ArrayList<TokenRejectionOutput> filteredList = new ArrayList<>();
                        for (TokenRejectionOutput tokenRejectionOutput : rejectionList) {
                            if (tokenRejectionOutput.getTokenNo().toLowerCase().contains(charString.toLowerCase())
                                    ||
                                    tokenRejectionOutput.getTokenID().toString().toLowerCase().contains(charString.toLowerCase())
                                    ||
                                    tokenRejectionOutput.getRegistrationNo().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(tokenRejectionOutput);
                            }
                        }
                        mFilteredList = filteredList;
                    }
                    filterResults = new FilterResults();
                    filterResults.values = mFilteredList;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                try {
                    mFilteredList = (List<TokenRejectionOutput>) filterResults.values;
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
    }

    class RejectionHolder extends RecyclerView.ViewHolder {

        private TextView tokenIdTV, tokenNumTV, regNumTv, paddyTypeTV, QuantityTV, rejectedDateTv, rejectedRemarksTv;
        private EditText updatedRemarksET;
        private Button approveBtn;

        RejectionHolder(@NonNull View itemView) {
            super(itemView);
            tokenIdTV = itemView.findViewById(R.id.tokenIdTV);
            tokenNumTV = itemView.findViewById(R.id.tokenNumTV);
            regNumTv = itemView.findViewById(R.id.regNumTv);
            paddyTypeTV = itemView.findViewById(R.id.paddyTypeTV);
            QuantityTV = itemView.findViewById(R.id.QuantityTV);
            rejectedDateTv = itemView.findViewById(R.id.rejectedDateTv);
            rejectedRemarksTv = itemView.findViewById(R.id.rejectedRemarksTv);
            updatedRemarksET = itemView.findViewById(R.id.updatedRemarksET);
            approveBtn = itemView.findViewById(R.id.approveBtn);
        }
    }
}

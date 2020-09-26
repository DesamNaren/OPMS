package com.cgg.pps.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cgg.pps.R;
import com.cgg.pps.model.response.report.payment_report.PaddyPaymentOutput;
import com.cgg.pps.view.reports.PaddyPaymentReportActivity;

import java.util.ArrayList;
import java.util.List;

public class PaddyPaymentReportAdapter extends RecyclerView.Adapter<PaddyPaymentReportAdapter.RejectionHolder> implements Filterable {

    private Context context;
    private List<PaddyPaymentOutput> paddyPaymentOutputs;
    private List<PaddyPaymentOutput> mFilteredList;


    public PaddyPaymentReportAdapter(Context context, List<PaddyPaymentOutput> paddyPaymentOutputs) {
        this.context = context;
        this.paddyPaymentOutputs = paddyPaymentOutputs;
        mFilteredList = paddyPaymentOutputs;
    }

    @NonNull
    @Override
    public RejectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.paddy_payment_report__row, parent, false);
        return new RejectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RejectionHolder holder, int position) {
        try {
            PaddyPaymentOutput paddyPaymentOutput = mFilteredList.get(position);
            holder.slNo.setText(String.valueOf(position+1));
            holder.txnID.setText(paddyPaymentOutput.getRegistrationNo1());
            holder.regID.setText(paddyPaymentOutput.getRegistrationNo());
            holder.farName.setText(paddyPaymentOutput.getFarmerName());
            holder.bankAccNo.setText(paddyPaymentOutput.getBankAccountNo());
            holder.payDate.setText(paddyPaymentOutput.getPaymentDate());
            holder.totAmt.setText(String.valueOf(paddyPaymentOutput.getTotalAmount()));
            holder.payStatus.setText(paddyPaymentOutput.getAmountTransferStatus());

            holder.btnDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, PaddyPaymentReportActivity.class).putExtra("PADDY_PAYMENT_DATA", paddyPaymentOutput));

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                        mFilteredList = paddyPaymentOutputs;
                    } else {
                        ArrayList<PaddyPaymentOutput> filteredList = new ArrayList<>();
                        for (PaddyPaymentOutput paddyPaymentOutput : paddyPaymentOutputs) {
                            if (paddyPaymentOutput.getRegistrationNo1().toLowerCase().contains(charString.toLowerCase())
                                    ||
                                    paddyPaymentOutput.getRegistrationNo().toLowerCase().contains(charString.toLowerCase())
                                    ||
                                    paddyPaymentOutput.getFarmerName().toLowerCase().contains(charString.toLowerCase())
                                    ||
                                    paddyPaymentOutput.getBankAccountNo().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(paddyPaymentOutput);
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
                    mFilteredList = (ArrayList<PaddyPaymentOutput>) filterResults.values;
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    class RejectionHolder extends RecyclerView.ViewHolder {

        private TextView slNo, txnID, regID, farName, bankAccNo, payDate, totAmt, payStatus;
        private Button btnDetails;

        RejectionHolder(@NonNull View itemView) {
            super(itemView);
            slNo = itemView.findViewById(R.id.sNoTV);
            txnID = itemView.findViewById(R.id.txnIDTV);
            regID = itemView.findViewById(R.id.regIDTV);
            farName = itemView.findViewById(R.id.farNameTv);
            bankAccNo = itemView.findViewById(R.id.bankAccNumTV);
            payDate = itemView.findViewById(R.id.paymentDateTV);
            totAmt = itemView.findViewById(R.id.totalAmountTV);
            payStatus = itemView.findViewById(R.id.statusTV);
            btnDetails = itemView.findViewById(R.id.getDetailsBtn);
        }
    }
}

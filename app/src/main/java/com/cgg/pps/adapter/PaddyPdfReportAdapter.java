package com.cgg.pps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cgg.pps.R;
import com.cgg.pps.model.response.report.payment_report.PaddyPaymentOutput;

import java.util.ArrayList;
import java.util.List;

public class PaddyPdfReportAdapter extends RecyclerView.Adapter<PaddyPdfReportAdapter.RejectionHolder> implements Filterable {

    private Context context;
    private List<PaddyPaymentOutput> paddyPaymentOutputs;
    private List<PaddyPaymentOutput> mFilteredList;
    private String fromDate, toDate;


    public PaddyPdfReportAdapter(Context context, List<PaddyPaymentOutput> paddyPaymentOutputs, String fromDate, String toDate) {
        this.context = context;
        this.paddyPaymentOutputs = paddyPaymentOutputs;
        mFilteredList = paddyPaymentOutputs;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @NonNull
    @Override
    public RejectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.paddy_print_row, parent, false);
        return new RejectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RejectionHolder holder, int position) {
        try {
            PaddyPaymentOutput paddyPaymentOutput = mFilteredList.get(position);
            holder.slNo.setText(String.valueOf(position + 1));
            holder.regID.setText(paddyPaymentOutput.getRegistrationNo());


            holder.farName.setText(paddyPaymentOutput.getFarmerName());
            holder.bankAccNo.setText(paddyPaymentOutput.getBankAccountNo());
            holder.nameAs.setText(paddyPaymentOutput.getNameasperBank());
            holder.bankName.setText(paddyPaymentOutput.getBankName());
            holder.branchName.setText(paddyPaymentOutput.getBranchName());


            holder.prcDate.setText(paddyPaymentOutput.getTransactionDate());
            holder.tcDate.setText(paddyPaymentOutput.getTruckChitDate());
            holder.payDate.setText(paddyPaymentOutput.getPaymentDate());

            holder.txnID.setText(paddyPaymentOutput.getRegistrationNo1());

            holder.newBags.setText(String.valueOf(paddyPaymentOutput.getNewBags()));
            holder.oldBags.setText(String.valueOf(paddyPaymentOutput.getOldBags()));
            holder.totBags.setText(String.valueOf(paddyPaymentOutput.getTotalBags()));

            holder.graAQty.setText(String.valueOf(paddyPaymentOutput.getaQUANTITY()));
            holder.comQty.setText(String.valueOf(paddyPaymentOutput.getcQUANTITY()));
            holder.totQty.setText(String.valueOf(paddyPaymentOutput.getTotalQuantity()));

            holder.totAmt.setText(String.valueOf(paddyPaymentOutput.getTotalAmount()));
            holder.payStatus.setText(paddyPaymentOutput.getAmountTransferStatus());


            holder.dateTV.setText("Paddy Payment Details for the period: " + fromDate + " To " + toDate);


            if (position == 0) {
                holder.header_ll.setVisibility(View.VISIBLE);
            } else {
                holder.header_ll.setVisibility(View.GONE);
            }

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

        private TextView slNo, txnID, regID, farName, bankAccNo, nameAs, payDate, totAmt, payStatus;
        private TextView bankName, branchName, prcDate, tcDate, newBags, oldBags, totBags;
        private TextView graAQty, comQty, totQty, dateTV;
        private LinearLayout header_ll, details_ll;

        RejectionHolder(@NonNull View itemView) {
            super(itemView);
            header_ll = itemView.findViewById(R.id.header_layout);
            details_ll = itemView.findViewById(R.id.det_layout);


            slNo = itemView.findViewById(R.id.sNoTV);
            regID = itemView.findViewById(R.id.regNumTV);
            farName = itemView.findViewById(R.id.farNameTv);
            bankAccNo = itemView.findViewById(R.id.bankAccNumTV);
            nameAs = itemView.findViewById(R.id.nameAsTV);
            bankName = itemView.findViewById(R.id.bankNameTV);
            branchName = itemView.findViewById(R.id.branchNameTV);

            prcDate = itemView.findViewById(R.id.prcDateTV);
            tcDate = itemView.findViewById(R.id.tcDateTV);
            payDate = itemView.findViewById(R.id.paymentDateTV);

            txnID = itemView.findViewById(R.id.txnIDTV);


            newBags = itemView.findViewById(R.id.newBagsTV);
            oldBags = itemView.findViewById(R.id.oldBagsTV);
            totBags = itemView.findViewById(R.id.totBagsTV);

            graAQty = itemView.findViewById(R.id.graAQty);
            comQty = itemView.findViewById(R.id.comQtyTV);
            totQty = itemView.findViewById(R.id.totQtyTv);

            totAmt = itemView.findViewById(R.id.totAmtTV);
            payStatus = itemView.findViewById(R.id.statusTV);
            dateTV = itemView.findViewById(R.id.dateTV);

        }
    }
}

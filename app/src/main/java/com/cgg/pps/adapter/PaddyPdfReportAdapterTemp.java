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

public class PaddyPdfReportAdapterTemp extends RecyclerView.Adapter<PaddyPdfReportAdapterTemp.RejectionHolder> implements Filterable {

    private Context context;
    private List<PaddyPaymentOutput> paddyPaymentOutputs;
    private List<PaddyPaymentOutput> mFilteredList;
    private String fromDate, toDate;


    public PaddyPdfReportAdapterTemp(Context context, List<PaddyPaymentOutput> paddyPaymentOutputs, String fromDate, String toDate) {
        this.context = context;
        this.paddyPaymentOutputs = paddyPaymentOutputs;
        mFilteredList = paddyPaymentOutputs;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @NonNull
    @Override
    public RejectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.paddy_print_row_temp, parent, false);
        return new RejectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RejectionHolder holder, int position) {
        try {
            PaddyPaymentOutput paddyPaymentOutput = mFilteredList.get(position);

            holder.dateTV.setText("Paddy Procurement Details for the period: " + fromDate + " To " + toDate);

            holder.slNo.setText(String.valueOf(position + 1));
            holder.basicDet.setText(paddyPaymentOutput.getFarmerName()
                    + "\n" + paddyPaymentOutput.getRegistrationNo());

            holder.bankDet.setText(paddyPaymentOutput.getBankAccountNo()
                    + "\n" + paddyPaymentOutput.getBankName()
                    + "\n" + paddyPaymentOutput.getBranchName());

            holder.proDet.setText("ID: " + paddyPaymentOutput.getRegistrationNo1()
                    + "\n" + "Date: " + paddyPaymentOutput.getTransactionDate()
                    + "\n" + "NB: " + paddyPaymentOutput.getNewBags()
                    + "; OB: " + paddyPaymentOutput.getOldBags()
                    + "; TB: " + paddyPaymentOutput.getTotalBags()
                    + "\n" + "AQ: " + paddyPaymentOutput.getaQUANTITY()
                    + "; CQ: " + paddyPaymentOutput.getcQUANTITY()
                    + "; TQ: " + paddyPaymentOutput.getTotalQuantity()
                    + "\n" + "TC Date: " + paddyPaymentOutput.getTruckChitDate());


            holder.payDet.setText(
                    "Amount: " + paddyPaymentOutput.getTotalAmount()
                            + "\n" + paddyPaymentOutput.getAmountTransferStatus()
                            + "\n" + "Date: " + paddyPaymentOutput.getPaymentDate());


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

        private TextView slNo, basicDet, bankDet, proDet, payDet, dateTV;
        private LinearLayout header_ll, details_ll;

        RejectionHolder(@NonNull View itemView) {
            super(itemView);
            header_ll = itemView.findViewById(R.id.header_layout);
            details_ll = itemView.findViewById(R.id.det_layout);


            dateTV = itemView.findViewById(R.id.dateTV);
            slNo = itemView.findViewById(R.id.sNoTV);
            basicDet = itemView.findViewById(R.id.basicDet);
            bankDet = itemView.findViewById(R.id.bankDet);
            proDet = itemView.findViewById(R.id.prcDet);
            payDet = itemView.findViewById(R.id.payDet);

        }
    }
}

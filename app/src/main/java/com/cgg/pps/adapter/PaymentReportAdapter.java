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

import com.cgg.pps.R;
import com.cgg.pps.model.response.report.payment.PaymentReportResponse;
import com.cgg.pps.view.reports.PaymentDetailReportActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class PaymentReportAdapter extends RecyclerView.Adapter<PaymentReportAdapter.RejectionHolder> implements Filterable {

    private Context context;
    private AppCompatActivity activity;
    private List<PaymentReportResponse> paymentReportResponses;
    private List<PaymentReportResponse> mFilteredList;

    public PaymentReportAdapter(Context context, List<PaymentReportResponse> paymentReportResponses) {
        this.context = context;
        this.activity = (AppCompatActivity) context;
        this.paymentReportResponses = paymentReportResponses;
        mFilteredList = paymentReportResponses;
    }

    @NonNull
    @Override
    public RejectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.payment_report__row, parent, false);
        return new RejectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RejectionHolder holder, int position) {
        try {
            PaymentReportResponse paymentReportResponse = mFilteredList.get(position);
            holder.regID.setText(paymentReportResponse.getRegistrationNo());
            holder.txnID.setText(paymentReportResponse.getFarmerTransactionID());
            holder.fName.setText(paymentReportResponse.getFarmerName());
            holder.status.setText(paymentReportResponse.getAmountTransferStatus());

            holder.getDetailsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, PaymentDetailReportActivity.class).putExtra("PAYMENT_DATA", paymentReportResponse));
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
                        mFilteredList = paymentReportResponses;
                    } else {
                        ArrayList<PaymentReportResponse> filteredList = new ArrayList<>();
                        for (PaymentReportResponse paymentReportResponse : paymentReportResponses) {
                            if (paymentReportResponse.getFarmerName().toLowerCase().contains(charString.toLowerCase())
                                    ||
                                    paymentReportResponse.getFarmerTransactionID().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(paymentReportResponse);
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
                    mFilteredList = (ArrayList<PaymentReportResponse>) filterResults.values;
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    class RejectionHolder extends RecyclerView.ViewHolder {

        private TextView regID, txnID, fName, status;
        private Button getDetailsBtn;

        RejectionHolder(@NonNull View itemView) {
            super(itemView);
            regID = itemView.findViewById(R.id.regIDTV);
            txnID = itemView.findViewById(R.id.txnIDTV);
            fName = itemView.findViewById(R.id.farmerNameTV);
            status = itemView.findViewById(R.id.statusTV);
            getDetailsBtn = itemView.findViewById(R.id.getDetailsBtn);
        }
    }
}

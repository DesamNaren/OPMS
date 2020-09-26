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
import com.cgg.pps.model.response.report.transaction.TransactionReportResponse;
import com.cgg.pps.view.reports.TransactionReportActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionReportAdapter extends RecyclerView.Adapter<TransactionReportAdapter.RejectionHolder> implements Filterable {

    private Context context;
    private AppCompatActivity activity;
    private List<TransactionReportResponse> transactionReportResponses;
    private List<TransactionReportResponse> mFilteredList;

    public TransactionReportAdapter(Context context, List<TransactionReportResponse> transactionReportResponses) {
        this.context = context;
        this.activity = (AppCompatActivity) context;
        this.transactionReportResponses = transactionReportResponses;
        mFilteredList = transactionReportResponses;
    }

    @NonNull
    @Override
    public RejectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_report__row, parent, false);
        return new RejectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RejectionHolder holder, int position) {
        try {
            TransactionReportResponse tcReportResponse = mFilteredList.get(position);
            holder.tknID.setText(tcReportResponse.getTokenID());
            holder.tknNo.setText(tcReportResponse.getTokenNo());
            holder.regID.setText(tcReportResponse.getRegistrationNo());
            holder.farmerName.setText(tcReportResponse.getFarmerName());
            holder.farmerMob.setText(tcReportResponse.getMobile());
            holder.status.setText(tcReportResponse.getStatus());
            holder.transactionIDTV.setText(tcReportResponse.getFarmerTransactionID());
            holder.getDetailsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, TransactionReportActivity.class).putExtra("TRANSACTION_DATA", tcReportResponse));
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
                        mFilteredList = transactionReportResponses;
                    } else {
                        ArrayList<TransactionReportResponse> filteredList = new ArrayList<>();
                        for (TransactionReportResponse transactionReportResponse : transactionReportResponses) {
                            if (transactionReportResponse.getTokenID().toLowerCase().contains(charString.toLowerCase())
                                    ||
                                    transactionReportResponse.getTokenNo().toLowerCase().contains(charString.toLowerCase())
                                    ||
                                    transactionReportResponse.getRegistrationNo().toLowerCase().contains(charString.toLowerCase())
                                    ||
                                    transactionReportResponse.getFarmerName().toLowerCase().contains(charString.toLowerCase())
                                    ||
                                    transactionReportResponse.getMobile().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(transactionReportResponse);
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
                    mFilteredList = (ArrayList<TransactionReportResponse>) filterResults.values;
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    class RejectionHolder extends RecyclerView.ViewHolder {

        private TextView tknID, tknNo, regID, farmerName, farmerMob, status,transactionIDTV;
        private Button getDetailsBtn;

        RejectionHolder(@NonNull View itemView) {
            super(itemView);
            tknID = itemView.findViewById(R.id.tknIDTV);
            tknNo = itemView.findViewById(R.id.tknNoTV);
            regID = itemView.findViewById(R.id.regIDTV);
            farmerName = itemView.findViewById(R.id.farmerNameTV);
            farmerMob = itemView.findViewById(R.id.mobNoTV);
            status = itemView.findViewById(R.id.statusTV);
            getDetailsBtn = itemView.findViewById(R.id.getDetailsBtn);
            transactionIDTV = itemView.findViewById(R.id.transactionIDTV);
        }
    }

    public void updateList(ArrayList<TransactionReportResponse> transactionReportResponses){
        mFilteredList = transactionReportResponses;
        notifyDataSetChanged();
    }
}

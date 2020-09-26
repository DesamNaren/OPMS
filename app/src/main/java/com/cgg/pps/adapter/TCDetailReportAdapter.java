package com.cgg.pps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cgg.pps.R;
import com.cgg.pps.model.response.report.truckchit.TCDetailReportResponse;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class TCDetailReportAdapter extends RecyclerView.Adapter<TCDetailReportAdapter.RejectionHolder> {

    private Context context;
    private AppCompatActivity activity;
    private List<TCDetailReportResponse> tcReportResponses;

    public TCDetailReportAdapter(Context context, List<TCDetailReportResponse> tcReportResponses) {
        this.context = context;
        this.activity = (AppCompatActivity) context;
        this.tcReportResponses = tcReportResponses;
    }

    @NonNull
    @Override
    public RejectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tc_detail_report_row, parent, false);
        return new RejectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RejectionHolder holder, int position) {
        try {
            TCDetailReportResponse tcReportResponse = tcReportResponses.get(position);
            holder.trIDTV.setText(tcReportResponse.getFarmerTransactionID());
            holder.statusTV.setText(tcReportResponse.getStatusDescription());
            holder.sentNew.setText(tcReportResponse.getBagSentNew());
            holder.sentOld.setText(tcReportResponse.getBagSentOld());
            holder.sentTot.setText(tcReportResponse.getBagSentTotal());
            holder.sentGradeA.setText(tcReportResponse.getQuantitySentGradeA());
            holder.sentCommon.setText(tcReportResponse.getQuantitySentGradeCommon());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return tcReportResponses != null ? tcReportResponses.size() : 0;
    }


    class RejectionHolder extends RecyclerView.ViewHolder {

        private TextView trIDTV, statusTV, sentNew, sentOld, sentTot, sentGradeA, sentCommon;

        RejectionHolder(@NonNull View itemView) {
            super(itemView);
            trIDTV = itemView.findViewById(R.id.trIDTV);
            statusTV = itemView.findViewById(R.id.statusTV);
            sentNew = itemView.findViewById(R.id.sentNewBagsTV);
            sentOld = itemView.findViewById(R.id.sentOldBagsTV);
            sentTot = itemView.findViewById(R.id.sentTotBagsTV);
            sentGradeA = itemView.findViewById(R.id.sentGradeAQty);
            sentCommon = itemView.findViewById(R.id.sentComQtyTV);

        }
    }
}

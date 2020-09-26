package com.cgg.pps.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cgg.pps.R;
import com.cgg.pps.model.response.report.truckchit.TCReportResponse;
import com.cgg.pps.view.reports.TCDetailReportActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class TCReportAdapter extends RecyclerView.Adapter<TCReportAdapter.RejectionHolder> implements Filterable {

    private Context context;
    private AppCompatActivity activity;
    private List<TCReportResponse> tcReportResponses;
    private List<TCReportResponse> mFilteredList;

    public TCReportAdapter(Context context, List<TCReportResponse> tcReportResponses) {
        this.context = context;
        this.activity = (AppCompatActivity) context;
        this.tcReportResponses = tcReportResponses;
        mFilteredList = tcReportResponses;
    }

    @NonNull
    @Override
    public RejectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tc_report__row, parent, false);
        return new RejectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RejectionHolder holder, int position) {
        try {
            TCReportResponse tcReportResponse = mFilteredList.get(position);
            holder.trIDTV.setText(tcReportResponse.getTRID());
            holder.trNoTV.setText(tcReportResponse.getTRNo());
            holder.manIDTV.setText(tcReportResponse.getTRManualNo());

            holder.getDetailsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, TCDetailReportActivity.class).putExtra("TC_DATA", tcReportResponse));
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
                        mFilteredList = tcReportResponses;
                    } else {
                        ArrayList<TCReportResponse> filteredList = new ArrayList<>();
                        for (TCReportResponse tcReportResponse : tcReportResponses) {
                            if (tcReportResponse.getTRID().toLowerCase().contains(charString.toLowerCase())
                                    ||
                                    tcReportResponse.getTRNo().toLowerCase().contains(charString.toLowerCase())
                                    ||
                                    tcReportResponse.getTRManualNo().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(tcReportResponse);
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
                    mFilteredList = (ArrayList<TCReportResponse>) filterResults.values;
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    class RejectionHolder extends RecyclerView.ViewHolder {

        private TextView trIDTV, trNoTV, manIDTV;
        private Button getDetailsBtn;

        RejectionHolder(@NonNull View itemView) {
            super(itemView);
            trIDTV = itemView.findViewById(R.id.trIDTV);
            trNoTV = itemView.findViewById(R.id.trNumTV);
            manIDTV = itemView.findViewById(R.id.manIDTV);
            getDetailsBtn = itemView.findViewById(R.id.getDetailsBtn);
        }
    }
}

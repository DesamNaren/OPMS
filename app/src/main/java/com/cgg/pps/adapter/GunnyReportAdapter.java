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
import com.cgg.pps.model.response.report.gunny.GunnyReportResponse;
import com.cgg.pps.view.reports.GunnyDetailReportActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class GunnyReportAdapter extends RecyclerView.Adapter<GunnyReportAdapter.RejectionHolder> implements Filterable {

    private Context context;
    private AppCompatActivity activity;
    private List<GunnyReportResponse> gunnyReportResponses;
    private List<GunnyReportResponse> mFilteredList;

    public GunnyReportAdapter(Context context, List<GunnyReportResponse> gunnyReportResponses) {
        this.context = context;
        this.activity = (AppCompatActivity) context;
        this.gunnyReportResponses = gunnyReportResponses;
        mFilteredList = gunnyReportResponses;
    }

    @NonNull
    @Override
    public RejectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gunny_report__row, parent, false);
        return new RejectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RejectionHolder holder, int position) {
        try {
            GunnyReportResponse gunnyReportResponse = mFilteredList.get(position);
            holder.orderId.setText(gunnyReportResponse.getORDERID());
            holder.srcType.setText(gunnyReportResponse.getSourcetype());
            holder.manIDTV.setText(gunnyReportResponse.getManualGunniesTruckchitID());

            holder.getDetailsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, GunnyDetailReportActivity.class).putExtra("GUNNY_DATA", gunnyReportResponse));
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
                        mFilteredList = gunnyReportResponses;
                    } else {
                        ArrayList<GunnyReportResponse> filteredList = new ArrayList<>();
                        for (GunnyReportResponse gunnyReportResponse : gunnyReportResponses) {
                            if (gunnyReportResponse.getORDERID().toLowerCase().contains(charString.toLowerCase())
                                    ||
                                    gunnyReportResponse.getManualGunniesTruckchitID().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(gunnyReportResponse);
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
                    mFilteredList = (ArrayList<GunnyReportResponse>) filterResults.values;
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    class RejectionHolder extends RecyclerView.ViewHolder {

        private TextView orderId, srcType, manIDTV;
        private Button getDetailsBtn;

        RejectionHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderIDTV);
            srcType = itemView.findViewById(R.id.srcTV);
            manIDTV = itemView.findViewById(R.id.manIDTV);
            getDetailsBtn = itemView.findViewById(R.id.getDetailsBtn);
        }
    }
}

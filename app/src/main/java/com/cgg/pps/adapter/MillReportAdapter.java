package com.cgg.pps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgg.pps.R;
import com.cgg.pps.model.response.report.mill.MillReportResponse;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class
MillReportAdapter extends RecyclerView.Adapter<MillReportAdapter.RejectionHolder> implements Filterable {

    private Context context;
    private AppCompatActivity activity;
    private List<MillReportResponse> millReportResponses;
    private List<MillReportResponse> mFilteredList;


    public MillReportAdapter(Context context, List<MillReportResponse> millReportResponses) {
        this.context = context;
        this.activity = (AppCompatActivity) context;
        this.millReportResponses = millReportResponses;
        mFilteredList = millReportResponses;
    }

    @NonNull
    @Override
    public RejectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mill_report__row, parent, false);
        return new RejectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RejectionHolder holder, int position) {
        try {
            MillReportResponse millReportResponse = mFilteredList.get(position);
            millReportResponse.setStatusValue(false);
            holder.millID.setText(millReportResponse.getMillID());
            holder.millCode.setText(millReportResponse.getMillCode());
            holder.millName.setText(millReportResponse.getMillName());
            holder.distName.setText(millReportResponse.getDistrictName());
            holder.nature.setText(millReportResponse.getNature());
            holder.roNum.setText(millReportResponse.getROCount());
            holder.aloted.setText(millReportResponse.getAllotedQuantity());
            holder.utilized.setText(millReportResponse.getUtilizedQuantity());
            holder.balance.setText(millReportResponse.getBalanceQuantity());

            holder.distNameTVLayout.setVisibility(View.GONE);
            holder.natureTVLayout.setVisibility(View.GONE);
            holder.roNumTVLayout.setVisibility(View.GONE);
            holder.alloQtyTVLayout.setVisibility(View.GONE);
            holder.utiQtyTVLayout.setVisibility(View.GONE);
            holder.getDetailsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!millReportResponse.isStatusValue()) {
                        holder.getDetailsBtn.setText("Hide Details");
                        millReportResponse.setStatusValue(true);
                        holder.distNameTVLayout.setVisibility(View.VISIBLE);
                        holder.natureTVLayout.setVisibility(View.VISIBLE);
                        holder.roNumTVLayout.setVisibility(View.VISIBLE);
                        holder.alloQtyTVLayout.setVisibility(View.VISIBLE);
                        holder.utiQtyTVLayout.setVisibility(View.VISIBLE);
                    }else{
                        holder.getDetailsBtn.setText("Get Details");
                        millReportResponse.setStatusValue(false);
                        holder.distNameTVLayout.setVisibility(View.GONE);
                        holder.natureTVLayout.setVisibility(View.GONE);
                        holder.roNumTVLayout.setVisibility(View.GONE);
                        holder.alloQtyTVLayout.setVisibility(View.GONE);
                        holder.utiQtyTVLayout.setVisibility(View.GONE);
                    }
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
                        mFilteredList = millReportResponses;
                    } else {
                        ArrayList<MillReportResponse> filteredList = new ArrayList<>();
                        for (MillReportResponse millReportResponse : millReportResponses) {
                            if (millReportResponse.getMillID().toLowerCase().contains(charString.toLowerCase())
                                    ||
                                    millReportResponse.getMillCode().toLowerCase().contains(charString.toLowerCase())
                                    ||
                                    millReportResponse.getMillName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(millReportResponse);
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
                    mFilteredList = (ArrayList<MillReportResponse>) filterResults.values;
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    class RejectionHolder extends RecyclerView.ViewHolder {

        private TextView millID, millCode, millName, distName, nature, roNum, aloted, utilized, balance;
        private Button getDetailsBtn;
        private LinearLayout distNameTVLayout,natureTVLayout,roNumTVLayout,alloQtyTVLayout,utiQtyTVLayout;

        RejectionHolder(@NonNull View itemView) {
            super(itemView);
            millID = itemView.findViewById(R.id.millIDTV);
            millCode = itemView.findViewById(R.id.millCodeTV);
            millName = itemView.findViewById(R.id.millNameTV);
            distName = itemView.findViewById(R.id.distNameTV);
            nature = itemView.findViewById(R.id.natureTV);
            roNum = itemView.findViewById(R.id.roNumTV);
            aloted = itemView.findViewById(R.id.alloQtyTV);
            utilized = itemView.findViewById(R.id.utiQtyTV);
            balance = itemView.findViewById(R.id.balQtyTV);
            getDetailsBtn = itemView.findViewById(R.id.getDetailsBtn);
            distNameTVLayout = itemView.findViewById(R.id.distNameTVLayout);
            natureTVLayout = itemView.findViewById(R.id.natureTVLayout);
            roNumTVLayout = itemView.findViewById(R.id.roNumTVLayout);
            alloQtyTVLayout = itemView.findViewById(R.id.alloQtyTVLayout);
            utiQtyTVLayout = itemView.findViewById(R.id.utiQtyTVLayout);
        }
    }
}

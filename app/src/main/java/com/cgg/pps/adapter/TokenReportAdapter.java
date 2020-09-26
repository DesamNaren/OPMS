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
import com.cgg.pps.interfaces.TokenReportInterface;
import com.cgg.pps.model.response.farmer.getfarmertokens.GetTokenOutput;
import com.cgg.pps.view.reports.TokenDetailReportActivity;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TokenReportAdapter extends RecyclerView.Adapter<TokenReportAdapter.RejectionHolder> implements Filterable {

    private Context context;
    private List<GetTokenOutput> tokenReportRespons;
    private List<GetTokenOutput> mFilteredList;
    private TokenReportInterface tokenReportInterface;

    public TokenReportAdapter(Context context, List<GetTokenOutput> tokenReportRespons) {
        this.context = context;
        this.tokenReportRespons = tokenReportRespons;
        mFilteredList = tokenReportRespons;

        try{
            tokenReportInterface = (TokenReportInterface) context;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public RejectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.token_report__row, parent, false);
        return new RejectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RejectionHolder holder, int position) {
        try {
            GetTokenOutput tokenReportResponse = mFilteredList.get(position);
            holder.tokenIdTV.setText(String.valueOf(tokenReportResponse.getTokenID()));
            holder.tokenNumTV.setText(tokenReportResponse.getTokenNo());
            holder.mobileNumTv.setText(tokenReportResponse.getMobile());
            holder.farmerNameTv.setText(tokenReportResponse.getFarmerName());

            holder.getDetailsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, TokenDetailReportActivity.class).putExtra("TOKEN_DATA", tokenReportResponse));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return  mFilteredList == null ? 0 : mFilteredList.size();
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
                        mFilteredList = tokenReportRespons;
                    } else {
                        ArrayList<GetTokenOutput> filteredList = new ArrayList<>();
                        for (GetTokenOutput tokenReportResponse : tokenReportRespons) {
                            if (tokenReportResponse.getTokenNo().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(tokenReportResponse);
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
                    mFilteredList = (ArrayList<GetTokenOutput>) filterResults.values;
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    class RejectionHolder extends RecyclerView.ViewHolder {

        private TextView tokenIdTV, tokenNumTV, mobileNumTv, farmerNameTv;
        private Button getDetailsBtn;

        RejectionHolder(@NonNull View itemView) {
            super(itemView);
            tokenIdTV = itemView.findViewById(R.id.tokenIdTV);
            tokenNumTV = itemView.findViewById(R.id.tokenNumTV);
            mobileNumTv = itemView.findViewById(R.id.mobNumTV);
            farmerNameTv = itemView.findViewById(R.id.framerNameTV);
            getDetailsBtn = itemView.findViewById(R.id.getDetailsBtn);
        }
    }
}

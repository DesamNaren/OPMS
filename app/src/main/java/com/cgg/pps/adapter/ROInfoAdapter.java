package com.cgg.pps.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgg.pps.R;
import com.cgg.pps.model.request.enablefaqrequest.EnableFAQRequest;
import com.cgg.pps.model.response.rejectedtokenresponse.TokenRejectionOutput;
import com.cgg.pps.model.response.truckchit.info.DetailedRoInfoOutput;
import com.cgg.pps.model.response.truckchit.info.RODetails;
import com.cgg.pps.model.response.truckchit.info.ROInfoResponse;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.presenter.RejectedTokenPresenter;
import com.cgg.pps.util.ConnectionDetector;
import com.cgg.pps.util.CustomProgressDialog;
import com.cgg.pps.util.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ROInfoAdapter extends RecyclerView.Adapter<ROInfoAdapter.RejectionHolder> {

    private Context context;
    private ROInfoResponse roInfoResponse;

    public ROInfoAdapter(Context context,
                         ROInfoResponse roInfoResponse) {
        this.context = context;
        this.roInfoResponse = roInfoResponse;
    }

    @NonNull
    @Override
    public RejectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ro_info_row, parent, false);
        return new RejectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RejectionHolder holder, int position) {
        try {
            DetailedRoInfoOutput detailedRoInfoOutput = roInfoResponse.getDetailedRoInfoOutput().get(position);

            holder.slNo.setText(String.valueOf(holder.getAdapterPosition() + 1));
            holder.date.setText(detailedRoInfoOutput.getModifiedDate());

            if(detailedRoInfoOutput.getActivity().contains("Inc")) {
                holder.qty.setCompoundDrawablesWithIntrinsicBounds(R.drawable.upload, 0, 0, 0);
            }
            else if(detailedRoInfoOutput.getActivity().contains("Dec")) {
                holder.qty.setCompoundDrawablesWithIntrinsicBounds(R.drawable.download, 0, 0, 0);
            }

            holder.qty.setText(String.valueOf(detailedRoInfoOutput.getModifiedQuantity()));
//            holder.activity.setText(detailedRoInfoOutput.getActivity());
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return roInfoResponse != null && roInfoResponse.getRODetails() != null
                && roInfoResponse.getDetailedRoInfoOutput() != null ?
                roInfoResponse.getDetailedRoInfoOutput().size() : 0;
    }


    class RejectionHolder extends RecyclerView.ViewHolder {

        private TextView slNo, date, qty, activity;

        RejectionHolder(@NonNull View itemView) {
            super(itemView);
            slNo = itemView.findViewById(R.id.slNo);
            activity = itemView.findViewById(R.id.activity);
            date = itemView.findViewById(R.id.date);
            qty = itemView.findViewById(R.id.qty);
        }
    }
}

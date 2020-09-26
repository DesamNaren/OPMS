package com.cgg.pps.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cgg.pps.R;
import com.cgg.pps.model.response.ppc_details.Ppcdetails;

import java.util.List;

public class MappedVilDetailsAdapter extends RecyclerView.Adapter<MappedVilDetailsAdapter.ItemHolder> {

    private Context context;
    private List<Ppcdetails> ppcdetails;

    public MappedVilDetailsAdapter(Context context, List<Ppcdetails> ppcdetails) {
        this.context = context;
        this.ppcdetails = ppcdetails;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mapped_vill_details_row, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        try {
            Ppcdetails details = ppcdetails.get(position);

            String name = "", mob = "";
            if (!TextUtils.isEmpty(details.getPPCName()))
                holder.name.setText(details.getPPCName());
            if (!TextUtils.isEmpty(details.getPPCCode()))
                holder.code.setText(details.getPPCCode());
            if (!TextUtils.isEmpty(details.getCoordinatorName()))
                name = details.getCoordinatorName();
            if (!TextUtils.isEmpty(details.getCoordinatorMobile()))
                mob = details.getCoordinatorMobile();

            holder.in_name.setText(name+", "+mob);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return ppcdetails != null ? ppcdetails.size() : 0;
    }


    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView name, code, in_name;
        ItemHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ppcNameTV);
            code = itemView.findViewById(R.id.ppcCodeTv);
            in_name = itemView.findViewById(R.id.inchNameTv);
        }
    }
}

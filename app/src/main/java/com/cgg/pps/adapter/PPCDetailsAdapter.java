package com.cgg.pps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cgg.pps.R;
import com.cgg.pps.model.response.ppc_details.MappedVillage;

import java.util.List;

public class PPCDetailsAdapter extends RecyclerView.Adapter<PPCDetailsAdapter.ItemHolder> {

    private Context context;
    private List<MappedVillage> mappedVillages;

    public PPCDetailsAdapter(Context context, List<MappedVillage> mappedVillages) {
        this.context = context;
        this.mappedVillages = mappedVillages;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ppc_details_row, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        try {
            MappedVillage mappedVillage = mappedVillages.get(position);
            holder.village.setText(mappedVillage.getMappedVillage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return mappedVillages != null ? mappedVillages.size() : 0;
    }


    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView village;
        ItemHolder(@NonNull View itemView) {
            super(itemView);
            village = itemView.findViewById(R.id.villageNameTV);
        }
    }
}

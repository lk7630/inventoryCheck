package com.sunrise.inventoryCheck;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

    private List<HashMap<Object, Object>> values;
    ViewHolder holder;
    int position;

    public ViewAdapter(List<HashMap<Object, Object>> values) {
        this.values = values;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        public TextView warehouse;
        public TextView polymer;
        public TextView form;
        public TextView packs;
        public TextView packing;
        public TextView weight;
        public TextView compartment;
        public TextView grade;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView;
            warehouse = (TextView) itemView.findViewById(R.id.warehouseView);
            polymer = (TextView) itemView.findViewById(R.id.polymerView);
            form = (TextView) itemView.findViewById(R.id.formView);
            compartment = (TextView) itemView.findViewById(R.id.compView);
            grade = (TextView) itemView.findViewById(R.id.gradeView);
            packs = (TextView) itemView.findViewById(R.id.packsView);
            packing = (TextView) itemView.findViewById(R.id.packingView);
            weight = (TextView) itemView.findViewById(R.id.weightView);
        }
    }

    @Override
    public ViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder mholder, int mposition) {
        holder = mholder;
        position = mposition;
        String warehouse = getValueFrom("warehouse", position);
        String polymer = getValueFrom("polymer", position);
        String form = getValueFrom("form", position);
        String compartment = getValueFrom("compartment", position);
        String grade = getValueFrom("grade", position);
        String packs = getValueFrom("packs", position);
        String packing = getValueFrom("packing", position);
        String weight = getValueFrom("weight", position);

        holder.warehouse.setText(warehouse);
        holder.polymer.setText(polymer);
        holder.form.setText(form);
        holder.compartment.setText("/" + compartment);
        holder.grade.setText(grade.equals("PARTIAL")?"P":" ");
        holder.packs.setText(packs);
        holder.packing.setText(packing);
        holder.weight.setText(weight + " lbs");
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    private String getValueFrom(String objectKey, int pos) {
        String returnResult = (String) values.get(pos).get(objectKey);
        return returnResult == "null" ? "" : returnResult;
    }

}

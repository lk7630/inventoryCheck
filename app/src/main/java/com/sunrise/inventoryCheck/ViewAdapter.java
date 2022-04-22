package com.sunrise.inventoryCheck;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;
import static android.graphics.Color.rgb;
import static android.graphics.Typeface.BOLD_ITALIC;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

    private List<LotItem> values;
    ViewHolder holder;


    public ViewAdapter(List<LotItem> values) {
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
        View v = layoutInflater.inflate(R.layout.lot_info_row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder mholder, int mposition) {
        holder = mholder;
        String warehouse = values.get(mposition).getWarehouse();
        String polymer = values.get(mposition).getPolymer();
        String form = values.get(mposition).getForm();
        String compartment = values.get(mposition).getCompartment();
        String grade = values.get(mposition).getGrade();
        String packs = String.valueOf(values.get(mposition).getPacks());
        String packing = values.get(mposition).getPacking();
        String weight = String.valueOf(values.get(mposition).getWeight());

        showText(holder.warehouse, warehouse);
        showText(holder.polymer, polymer);
        showText(holder.form, form);
        showText(holder.compartment, compartment);
        showText(holder.grade, grade);
        showText(holder.packs, packs);
        showText(holder.packing, packing);
        showText(holder.weight, weight);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    private void showText(TextView textView, String variable) {
        if (variable == null) {
            textView.setText("");
        } else if (textView.getId() == R.id.polymerView) {
            customizeView(variable, "polymer");
        } else if (textView.getId() == R.id.gradeView) {
            variable = variable.equals("PARTIAL") ? "P" : "";
            holder.grade.setTypeface(holder.grade.getTypeface(), BOLD_ITALIC);
        } else if (textView.getId() == R.id.weightView) {
            variable = variable + " lbs";
        } else if (textView.getId() == R.id.compView) {
            variable = "/" + variable;
        }
        textView.setText(variable);
    }

    private void customizeView(String variable, String textViewName) {
        if (textViewName.equals("polymer")) {
            switch (variable) {
                case "HDPE":
                    holder.polymer.setTextColor(BLUE);
                    break;
                case "LDPE":
                    holder.polymer.setTextColor(rgb(4, 148, 33));
                    break;
                case "PP":
                    holder.polymer.setTextColor(RED);
                    break;
                case "PS":
                    holder.polymer.setTextColor(rgb(255, 165, 0));
                    break;
                default:
                    holder.polymer.setTextColor(BLACK);
                    break;
            }
        }
    }

}

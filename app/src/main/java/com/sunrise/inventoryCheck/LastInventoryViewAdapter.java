package com.sunrise.inventoryCheck;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LastInventoryViewAdapter extends RecyclerView.Adapter<LastInventoryViewAdapter.ViewHolder> {

    private final List<LastInventory> values;
    ViewHolder holder;


    public LastInventoryViewAdapter(List<LastInventory> values) {
        this.values = values;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        public TextView warehouse;
        public TextView inventoryDate;
        public TextView section;
        public TextView packCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView;
            warehouse = (TextView) itemView.findViewById(R.id.warehouseLastInventoryView);
            inventoryDate = (TextView) itemView.findViewById(R.id.dateLastInventoryView);
            section = (TextView) itemView.findViewById(R.id.sectionLastinventoryView);
            packCount = (TextView) itemView.findViewById(R.id.packCountLastInventoryView);
        }
    }

    @Override
    public LastInventoryViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.last_inventory_row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder mholder, int mposition) {
        holder = mholder;
        String warehouse = values.get(mposition).getWareHouse();
        String inventoryDate = values.get(mposition).getInventoryDate().toString();
        String section = values.get(mposition).getSection();
        String packCount = String.valueOf(values.get(mposition).getPackCount());

        holder.warehouse.setText(showText(warehouse));
        holder.inventoryDate.setText(showText(inventoryDate));
        holder.section.setText(showText(section));
        holder.packCount.setText(showText(packCount));
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    private String showText(String text) {
        if (text == null) {
            return "";
        }
        return text;
    }

}

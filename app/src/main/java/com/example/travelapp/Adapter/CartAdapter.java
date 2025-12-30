package com.example.travelapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelapp.Domain.ItemDomain;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ViewholderCartBinding;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<ItemDomain> items;

    public CartAdapter(ArrayList<ItemDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCartBinding binding = ViewholderCartBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemDomain item = items.get(position);

        // Set title
        holder.binding.titleTxt.setText(item.getTitle());

        // Set price per item
        holder.binding.feeEachItem.setText("$" + item.getPrice());

        // Set initial quantity (default 1)
        int quantity = 1;
        holder.binding.numberItemTxt.setText(String.valueOf(quantity));

        // Calculate and set total price
        double totalPrice = item.getPrice() * quantity;
        holder.binding.totalEachItem.setText("$" + totalPrice);

        // Load image if available
        if (item.getPic() != null && !item.getPic().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getPic())
                    .into(holder.binding.pic);
        }

        // Plus button functionality
        holder.binding.plusCartBtn.setOnClickListener(v -> {
            int currentQty = Integer.parseInt(holder.binding.numberItemTxt.getText().toString());
            currentQty++;
            holder.binding.numberItemTxt.setText(String.valueOf(currentQty));

            // Update total price
            double total = item.getPrice() * currentQty;
            holder.binding.totalEachItem.setText("$" + total);
        });

        // Minus button functionality
        holder.binding.minusCartBtn.setOnClickListener(v -> {
            int currentQty = Integer.parseInt(holder.binding.numberItemTxt.getText().toString());
            if (currentQty > 1) {
                currentQty--;
                holder.binding.numberItemTxt.setText(String.valueOf(currentQty));

                // Update total price
                double total = item.getPrice() * currentQty;
                holder.binding.totalEachItem.setText("$" + total);
            }
        });

        // Remove button functionality
        holder.binding.removeBtn.setOnClickListener(v -> {
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, items.size());
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderCartBinding binding;

        public ViewHolder(ViewholderCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
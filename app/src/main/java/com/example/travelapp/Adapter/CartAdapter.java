package com.example.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelapp.Model.CartItem;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ViewholderCartBinding;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private List<CartItem> items;
    private OnItemClickListener listener;

    public CartAdapter(Context context, List<CartItem> items) {
        this.context = context;
        this.items = items;
    }

    public interface OnItemClickListener {
        void onItemRemoved();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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
        CartItem item = items.get(position);

        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.priceTxt.setText("$" + String.format("%.2f", item.getPrice()));
        holder.binding.addressTxt.setText(item.getLocation());

        // Load image with Glide
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.intro_pic)
                    .error(R.drawable.intro_pic)
                    .into(holder.binding.pic);
        }

        // Remove button
        holder.binding.removeBtn.setOnClickListener(v -> {
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, items.size());

            // Notifikasi CartActivity untuk update total
            if (listener != null) {
                listener.onItemRemoved();
            }
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
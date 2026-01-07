package com.example.travelapp.Adapter;

import com.example.travelapp.Activity.DetailActivity;
import android.content.Intent;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelapp.Domain.ItemDomain;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ViewholderTrendingCardBinding;

import java.util.ArrayList;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ItemDomain> items;

    public TrendingAdapter(Context context, ArrayList<ItemDomain> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderTrendingCardBinding binding = ViewholderTrendingCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemDomain item = items.get(position);

        holder.binding.trendingTitle.setText(item.getTitle());
        holder.binding.trendingRating.setText("⭐ " + item.getScore() + " • 2.3K reviews");

        // ✅ Tambahkan di sini
        holder.binding.trendingLocation.setText(item.getAddress());
        holder.binding.trendingPrice.setText("$" + item.getPrice() + " /Person");

        // Load gambar
        if (item.getPic() != null && !item.getPic().isEmpty()) {
            Glide.with(context)
                    .load(item.getPic())
                    .placeholder(R.drawable.intro_pic)
                    .into(holder.binding.trendingImage);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", item);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderTrendingCardBinding binding;

        public ViewHolder(ViewholderTrendingCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
package com.example.travelapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelapp.Activity.DetailActivity;
import com.example.travelapp.Domain.ItemDomain;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ViewholderRecommendedBinding;

import java.util.ArrayList;

public class SeeAllAdapter extends RecyclerView.Adapter<SeeAllAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ItemDomain> items;

    public SeeAllAdapter(Context context, ArrayList<ItemDomain> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderRecommendedBinding binding = ViewholderRecommendedBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemDomain item = items.get(position);

        holder.binding.title.setText(item.getTitle());
        holder.binding.location.setText(item.getAddress());
        holder.binding.rating.setText(String.valueOf(item.getScore()));
        holder.binding.price.setText("$" + item.getPrice() + " /Person");

        // Load gambar dari URL
        Glide.with(context)
                .load(item.getPic()) // Pastikan field `pic` adalah URL string
                .placeholder(R.drawable.placeholder_image) // Opsional
                .into(holder.binding.pic);

        // Klik item → ke DetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", item); // ItemDomain implements Serializable
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderRecommendedBinding binding;

        public ViewHolder(@NonNull ViewholderRecommendedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
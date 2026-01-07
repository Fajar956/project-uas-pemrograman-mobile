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
import com.example.travelapp.databinding.ViewholderPopularBinding;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.Viewholder> {
    private Context context;
    private ArrayList<ItemDomain> items;

    // ✅ Konstruktor diperbaiki: terima Context
    public PopularAdapter(Context context, ArrayList<ItemDomain> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderPopularBinding binding = ViewholderPopularBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        ItemDomain item = items.get(position);

        // ✅ ID SESUAI DENGAN viewholder_popular.xml
        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.addressTxt.setText(item.getAddress());
        holder.binding.priceTxt.setText("$" + item.getPrice());
        holder.binding.scoreTxt.setText(String.valueOf(item.getScore()));

        // Load gambar
        if (item.getPic() != null && !item.getPic().isEmpty()) {
            Glide.with(context)
                    .load(item.getPic())
                    .placeholder(R.drawable.intro_pic)
                    .into(holder.binding.pic);
        }

        // Klik → buka Detail
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

    public static class Viewholder extends RecyclerView.ViewHolder {
        ViewholderPopularBinding binding;

        public Viewholder(ViewholderPopularBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
package com.example.travelapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.travelapp.Domain.SliderItems;
import com.example.travelapp.R;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private ArrayList<SliderItems> sliderItems;
    private ViewPager2 viewPager2;
    private Context context;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Menambahkan item lagi untuk menciptakan efek loop tak terbatas
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };

    public SliderAdapter(ArrayList<SliderItems> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new SliderViewHolder(
                LayoutInflater.from(context).inflate(R.layout.slider_item_container, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        // Pindahkan logika binding ke sini
        holder.setImage(sliderItems.get(position));

        // Jika kita berada di dekat akhir daftar, post runnable untuk menambahkan lebih banyak item
        if (position == sliderItems.size() - 2) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        // Gunakan variabel sliderItems, bukan kelas SliderItems
        return sliderItems.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder {
        // Tambahkan titik koma yang hilang
        private ImageView imageView;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }

        // Pindahkan metode ini ke luar konstruktor
        void setImage(SliderItems item) {
            // Gunakan `item.getUrl()` untuk mendapatkan URL gambar
            Glide.with(context).load(item.getUrl()).into(imageView);
        }
    }
}

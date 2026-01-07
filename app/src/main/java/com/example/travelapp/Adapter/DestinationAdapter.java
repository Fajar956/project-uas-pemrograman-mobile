package com.example.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.Model.Destination;
import com.example.travelapp.R;
import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> {

    private Context context;
    private List<Destination> destinationList;

    public DestinationAdapter(Context context, List<Destination> destinationList) {
        this.context = context;
        this.destinationList = destinationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.viewholder_recommended, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Destination dest = destinationList.get(position);
        holder.title.setText(dest.getTitle());
        holder.location.setText(dest.getLocation());
        holder.rating.setText(String.valueOf(dest.getRating()));
        holder.price.setText("$" + (int) dest.getPrice() + " /Person");
        holder.pic.setImageResource(dest.getImageResId());
    }

    @Override
    public int getItemCount() {
        return destinationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView title, location, rating, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            title = itemView.findViewById(R.id.title);
            location = itemView.findViewById(R.id.location);
            rating = itemView.findViewById(R.id.rating);
            price = itemView.findViewById(R.id.price);
        }
    }
}
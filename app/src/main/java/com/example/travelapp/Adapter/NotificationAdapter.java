package com.example.travelapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.Activity.NotificationActivity;
import com.example.travelapp.R;
import com.example.travelapp.databinding.ViewholderNotificationBinding;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationActivity.NotificationItem> items;

    public NotificationAdapter(List<NotificationActivity.NotificationItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderNotificationBinding binding = ViewholderNotificationBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationActivity.NotificationItem item = items.get(position);

        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.messageTxt.setText(item.getMessage());
        holder.binding.timeTxt.setText(item.getTime());

        if (item.isUnread()) {
            holder.binding.unreadDot.setVisibility(View.VISIBLE);
        } else {
            holder.binding.unreadDot.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderNotificationBinding binding;

        public ViewHolder(ViewholderNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
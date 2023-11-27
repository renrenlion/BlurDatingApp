package com.example.blurdatingapplication.manualmatching;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.blurdatingapplication.R;

import java.util.List;


public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private List<Spot> spots;

    public CardStackAdapter(List<Spot> spots) {
        this.spots = spots;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_spot, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Spot spot = spots.get(position);
        holder.username.setText(spot.getUsername() + " " + spot.getAge());
        holder.location.setText(spot.getLocation());
        Glide.with(holder.image)
                .load(spot.getUrl())
                .into(holder.image);
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), spot.getUsername(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return spots != null ? spots.size() : 0;
    }

    public void setSpots(List<Spot> spots) {
        this.spots = spots;
    }

    public List<Spot> getSpots() {
        return spots;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView location;
        ImageView image;

        ViewHolder(View view) {
            super(view);
            username = view.findViewById(R.id.item_username);
            location = view.findViewById(R.id.item_location);
            image = view.findViewById(R.id.item_image);
        }
    }
}



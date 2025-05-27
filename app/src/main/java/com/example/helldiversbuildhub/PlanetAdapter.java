package com.example.helldiversbuildhub;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlanetAdapter extends RecyclerView.Adapter<PlanetAdapter.PlanetViewHolder> {
    private List<Planet> planetList;

    public PlanetAdapter(List<Planet> planetList) {
        this.planetList = planetList;
    }

    @Override
    public PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.planet_item, parent, false);
        return new PlanetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlanetViewHolder holder, int position) {
        Planet planet = planetList.get(position);
        holder.planetNameTv.setText(planet.getName());
        holder.playersTv.setText(String.valueOf(planet.getPlayers()));
        holder.libRate.setText(String.valueOf(planet.getPercentage()));
    }

    @Override
    public int getItemCount() {
        return planetList.size();
    }

    public static class PlanetViewHolder extends RecyclerView.ViewHolder {
        TextView planetNameTv, playersTv, helmetIcon, factionIcon, percentageTv, libRate, biomeImg;

        public PlanetViewHolder(View itemView) {
            super(itemView);
            planetNameTv = itemView.findViewById(R.id.planetNameTv);
            playersTv = itemView.findViewById(R.id.playersTv);
            libRate = itemView.findViewById(R.id.libRate);
        }
    }
}

package com.example.helldiversbuildhub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class PlanetAdapter extends RecyclerView.Adapter<PlanetAdapter.PlanetViewHolder> {
    private final List<Planet> planetList;

    public PlanetAdapter(List<Planet> planetList) {
        this.planetList = planetList;
    }

    @Override
    public PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.planet_item, parent, false);
        return new PlanetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlanetViewHolder holder, int position) {
        Planet planet = planetList.get(position);

        // Texto
        holder.planetNameTv.setText(planet.getName());
        holder.playersTv.setText(String.valueOf(planet.getPlayers()));
        holder.libRate.setText(planet.getPercentage() + "%");

        // Decide colores e ícono según la facción
        int bgColor, borderColor, iconRes;
        switch (planet.getFaction()) {
            case "Terminids":
                bgColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.terminidos_color_fondo);
                borderColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.terminidos_color_borde);
                iconRes = R.drawable.terminid_icon;
                break;

            case "Super Earth":
                bgColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.iluminados_color_fondo);
                borderColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.iluminados_color_borde);
                iconRes = R.drawable.illuminate_logo;
                break;

            case "Automatons":
                bgColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.bots_color_fondo);
                borderColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.bots_color_borde);
                iconRes = R.drawable.bots_logo;
                break;

            default:
                bgColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.default_fondo);
                borderColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.default_borde);
                iconRes = R.drawable.helldiver_logo;
                break;
        }


        holder.card.setCardBackgroundColor(bgColor);
        holder.card.setStrokeColor(borderColor);
        holder.card.setStrokeWidth(holder.itemView.getResources().getDimensionPixelSize(R.dimen.card_stroke_width));
        holder.factionIcon.setImageResource(iconRes);
    }

    @Override
    public int getItemCount() {
        return planetList.size();
    }

    public static class PlanetViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView card;
        ImageView helmetIcon, factionIcon, biomeImg;
        TextView planetNameTv, playersTv, percentageTv, libRate;

        public PlanetViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.cardview);
            helmetIcon = itemView.findViewById(R.id.helmetIcon);
            factionIcon = itemView.findViewById(R.id.factionIcon);
            biomeImg = itemView.findViewById(R.id.biomeImg);

            planetNameTv = itemView.findViewById(R.id.planetNameTv);
            playersTv = itemView.findViewById(R.id.playersTv);
            percentageTv = itemView.findViewById(R.id.percentageTv);
            libRate = itemView.findViewById(R.id.libRate);
        }
    }
}

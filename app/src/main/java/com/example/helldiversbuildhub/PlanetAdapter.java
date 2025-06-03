// PlanetAdapter.java
package com.example.helldiversbuildhub;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class PlanetAdapter extends RecyclerView.Adapter<PlanetAdapter.PlanetViewHolder> {
    private final List<Planet> planetList;

    public PlanetAdapter(List<Planet> initialList) {
        this.planetList = new ArrayList<>(initialList);
        Collections.sort(planetList, (p1, p2) -> Integer.compare(p2.getPlayers(), p1.getPlayers()));
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
        double porcentaje = planet.getPercentage();
        String formateado = String.format(Locale.getDefault(), "%.3f%%", porcentaje);

        holder.planetNameTv.setText(planet.getName());
        holder.playersTv.setText(String.valueOf(planet.getPlayers()));
        holder.libRate.setText(formateado);

        int progreso = (int) Math.round(porcentaje);
        holder.progressBar.setMax(100);
        holder.progressBar.setProgress(progreso);

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

        String slug = null;
        if (planet.getBiome() != null) {
            slug = planet.getBiome().getSlug();
        }
        int biomeResId;
        if (slug == null) {
            biomeResId = R.drawable.dfltbiome;
        } else {
            switch (slug.toLowerCase(Locale.ROOT)) {
                case "canyon":      biomeResId = R.drawable.canyon;      break;
                case "crimsonmoor": biomeResId = R.drawable.crimsonmoor; break;
                case "desolate":    biomeResId = R.drawable.desolate;    break;
                case "ethereal":    biomeResId = R.drawable.ethereal;    break;
                case "highlands":   biomeResId = R.drawable.highlands;   break;
                case "icemoss":     biomeResId = R.drawable.icemoss;     break;
                case "jungle":      biomeResId = R.drawable.jungle;      break;
                case "mesa":        biomeResId = R.drawable.mesa;        break;
                case "moon":        biomeResId = R.drawable.moon;        break;
                case "morass":      biomeResId = R.drawable.morass;      break;
                case "rainforest":  biomeResId = R.drawable.rainforest;  break;
                case "swamp":       biomeResId = R.drawable.swamp;       break;
                case "toxic":       biomeResId = R.drawable.toxic;       break;
                case "undergrowth": biomeResId = R.drawable.undergrowth; break;
                case "winter":      biomeResId = R.drawable.winter;      break;
                default:            biomeResId = R.drawable.dfltbiome;   break;
            }
        }
        holder.biomeImg.setImageResource(biomeResId);

        holder.card.setCardBackgroundColor(bgColor);
        holder.card.setStrokeColor(borderColor);
        holder.card.setStrokeWidth(
                holder.itemView.getResources().getDimensionPixelSize(R.dimen.card_stroke_width));
        holder.factionIcon.setImageResource(iconRes);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.progressBar.setProgressTintList(ColorStateList.valueOf(borderColor));
            holder.progressBar.setProgressBackgroundTintList(ColorStateList.valueOf(bgColor));
        } else {
            holder.progressBar.getProgressDrawable()
                    .setColorFilter(borderColor, PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public int getItemCount() {
        return planetList.size();
    }


    // Reemplaza la lista interna de planetas y notifica el cambio. Asi el RecyclerView no reinicia la posición de scroll.

    public void updatePlanets(List<Planet> newList) {
        // Ordena antes de copiar
        Collections.sort(newList, (p1, p2) -> Integer.compare(p2.getPlayers(), p1.getPlayers()));

        planetList.clear();
        planetList.addAll(newList);
        notifyDataSetChanged();
    }

    public static class PlanetViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView card;
        ImageView helmetIcon, factionIcon, biomeImg;
        TextView planetNameTv, playersTv, libRate;
        ProgressBar progressBar;

        public PlanetViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.cardview);
            helmetIcon = itemView.findViewById(R.id.helmetIcon);
            factionIcon = itemView.findViewById(R.id.factionIcon);
            biomeImg = itemView.findViewById(R.id.biomeImg);
            progressBar = itemView.findViewById(R.id.progressBar);
            planetNameTv = itemView.findViewById(R.id.planetNameTv);
            playersTv = itemView.findViewById(R.id.playersTv);
            libRate = itemView.findViewById(R.id.libRate);
        }
    }
}

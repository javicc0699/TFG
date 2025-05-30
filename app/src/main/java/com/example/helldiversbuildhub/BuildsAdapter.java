package com.example.helldiversbuildhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BuildsAdapter extends RecyclerView.Adapter<BuildsAdapter.BuildsViewHolder> {

    private final Context context;
    private final List<Build> items;

    private final Map<String, Integer> estratagemasMap;
    private final Map<String, Integer> primariaMap;
    private final Map<String, Integer> secundariaMap;
    private final Map<String, Integer> pasivaArmMap;
    private final Map<String, Integer> potenciadorMap;
    private final Map<String, Integer> faccionMap;

    public BuildsAdapter(Context context, List<Build> items) {
        this.context = context;
        this.items = items;

        estratagemasMap = Util.mapFromArrays(context, R.array.estratagemas_nombre, R.array.estratagemas_iconos);
        primariaMap = Util.mapFromArrays(context, R.array.armas_primarias, R.array.armas_primarias_iconos);
        secundariaMap = Util.mapFromArrays(context, R.array.armas_secundarias, R.array.armas_secundarias_iconos);
        pasivaArmMap = Util.mapFromArrays(context, R.array.pasivas_armadura, R.array.pasivas_armadura_iconos);
        potenciadorMap = Util.mapFromArrays(context, R.array.potenciadores_nombres, R.array.potenciadores_iconos);
        faccionMap = Util.mapFromArrays(context, R.array.facciones_nombres, R.array.facciones_iconos);
    }

    @NonNull
    @Override
    public BuildsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.build_item, parent, false);
        return new BuildsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuildsViewHolder holder, int position) {
        Build build = items.get(position);

        holder.usernameTv.setText(build.username);
        holder.dateTv.setText(build.date);
        holder.likeCountTv.setText(String.valueOf(build.likes));
        holder.dislikeCountTv.setText(String.valueOf(build.dislikes));

        // Facción
        Integer fIcon = faccionMap.get(build.faction);
        if (fIcon != null) holder.factionIv.setImageResource(fIcon);

        // El fragment crasheaba porque detectaba que la lista era null y este fragmento de codigo protege de que crashee.
        List<String> strats = build.stratagems != null
                ? build.stratagems
                : Collections.emptyList();
        // Primero limpia todas las imágenes
        for (ImageView iv : holder.stratIvs) {
            iv.setImageDrawable(null);
        }
        // Luego rellena solo las que existan en la lista
        for (int i = 0; i < strats.size() && i < holder.stratIvs.length; i++) {
            Integer icono = estratagemasMap.get(strats.get(i));
            if (icono != null) {
                holder.stratIvs[i].setImageResource(icono);
            }
        }

        // Armas, pasiva y potenciador. Esto provocaba crasheos y he puesto el getOrDefault que por lo visto funciona.
        holder.primariaIv.setImageResource(primariaMap.getOrDefault(build.primaryWeapon, 0));
        holder.secundariaIv.setImageResource(secundariaMap.getOrDefault(build.secondaryWeapon, 0));
        holder.armorIv.setImageResource(pasivaArmMap.getOrDefault(build.armorPassive, 0));
        holder.potenciadorIv.setImageResource(potenciadorMap.getOrDefault(build.booster, 0));
        holder.factionIv.setImageResource(faccionMap.getOrDefault(build.faction, 0));

        // Listeners de like/dislike que tambien llaman al metodo de la Clase Util para que manejan los votos
        holder.likeBtn.setOnClickListener(v -> {
            holder.likeBtn.setEnabled(false);
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Util.votarBuild(build.id, uid, "like");
        });
        holder.dislikeBtn.setOnClickListener(v -> {
            holder.dislikeBtn.setEnabled(false);
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Util.votarBuild(build.id, uid, "dislike");
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class BuildsViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTv, dateTv, likeCountTv, dislikeCountTv;
        ImageView factionIv, primariaIv, secundariaIv, armorIv, potenciadorIv;
        ImageView[] stratIvs = new ImageView[4];
        ImageButton likeBtn, dislikeBtn;

        public BuildsViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTv = itemView.findViewById(R.id.usernameTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            likeCountTv = itemView.findViewById(R.id.likeCountTv);
            dislikeCountTv = itemView.findViewById(R.id.dislikeCountTv);
            factionIv = itemView.findViewById(R.id.factionIv);

            stratIvs[0] = itemView.findViewById(R.id.strat0Iv);
            stratIvs[1] = itemView.findViewById(R.id.strat1Iv);
            stratIvs[2] = itemView.findViewById(R.id.strat2Iv);
            stratIvs[3] = itemView.findViewById(R.id.strat3Iv);

            primariaIv = itemView.findViewById(R.id.primariaIv);
            secundariaIv = itemView.findViewById(R.id.secundariaIv);
            armorIv = itemView.findViewById(R.id.pasivaIv);
            potenciadorIv = itemView.findViewById(R.id.potenciadorIv);

            likeBtn = itemView.findViewById(R.id.likeBtn);
            dislikeBtn = itemView.findViewById(R.id.dislikeBtn);
        }
    }
}

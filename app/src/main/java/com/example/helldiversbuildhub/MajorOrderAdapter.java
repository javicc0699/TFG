package com.example.helldiversbuildhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.Locale;

public class MajorOrderAdapter extends RecyclerView.Adapter<MajorOrderAdapter.ViewHolder> {

    private Context context;
    private List<MajorOrder> orderList;

    public MajorOrderAdapter(Context context, List<MajorOrder> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public MajorOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.major_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MajorOrderAdapter.ViewHolder holder, int position) {
        MajorOrder order = orderList.get(position);
        Setting setting = order.getSetting();

        // Asignar título, brief y descripción
        holder.titleTv.setText(setting.getOverrideTitle());
        holder.briefTv.setText(setting.getOverrideBrief());
        holder.descriptionTv.setText(setting.getTaskDescription());

        // Construir y mostrar las tareas concatenadas
        StringBuilder sbTasks = new StringBuilder();
        sbTasks.append("Tasks: ");
        List<Task> tasks = setting.getTasks();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            sbTasks.append("type ")
                    .append(task.getType())
                    .append(" → values")
                    .append(task.getValues().toString());
            if (i < tasks.size() - 1) {
                sbTasks.append(";");
            }
        }


        // Mostrar la cantidad de medallas de recompensa
        Reward reward = setting.getReward();
        holder.rewardTv.setText("Medallas: " + reward.getAmount());

        // Calcular progreso en porcentaje y asignarlo
        List<Integer> progressArr = order.getProgress();
        int completed = 0;
        for (Integer progress : progressArr) {
            if (progress != null && progress == 1) {
                completed++;
            }
        }
        int total = progressArr.size();
        int porcen = 0;
        if (total > 0) {
            porcen = (int) Math.round(completed / (float) total * 100f);
        }
        holder.progressTv.setText(String.format(Locale.getDefault(), "Progreso: %d%%", porcen));
        holder.progressBar.setMax(100);
        holder.progressBar.setProgressTintList(
                ContextCompat.getColorStateList(context, R.color.supertierraazul));
        holder.progressBar.setProgress(porcen);

        // Convertir expiresIn (segundos) a “Xd Xh Xm” y mostrarlo
        int totalSeconds = order.getExpiresIn();
        String formattedTime = formatSecondsToDHm(totalSeconds);
        holder.expiresTv.setText("Termina en: " + formattedTime);

        // Colorear el MaterialCardView
        int bgColor = ContextCompat.getColor(context, R.color.default_fondo);
        int borderColor = ContextCompat.getColor(context, R.color.helldivers_yellow);
        holder.card.setCardBackgroundColor(bgColor);
        holder.card.setStrokeColor(borderColor);
        holder.card.setStrokeWidth(
                context.getResources().getDimensionPixelSize(R.dimen.card_stroke_width));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    /**
     * Convierte un valor en segundos a un String con formato Xd Xh Xm, si una unidad es cero, lo salta
     */
    private String formatSecondsToDHm(int totalSeconds) {
        int days = totalSeconds / 86400;
        int remainderAfterDays = totalSeconds % 86400;
        int hours = remainderAfterDays / 3600;
        int remainderAfterHours = remainderAfterDays % 3600;
        int minutes = remainderAfterHours / 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append("d ");
        }
        if (hours > 0) {
            sb.append(hours).append("h ");
        }
        if (minutes > 0) {
            sb.append(minutes).append("m");
        }
        // Si todas las unidades fueron cero se muestra 0m
        if (days == 0 && hours == 0 && minutes == 0) {
            sb.append("0m");
        }
        return sb.toString().trim();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView card;
        TextView titleTv, briefTv, descriptionTv, tasksTv, rewardTv, progressTv, expiresTv;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.cardMajorOrder);
            titleTv = itemView.findViewById(R.id.titleTv);
            briefTv = itemView.findViewById(R.id.briefTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
            rewardTv = itemView.findViewById(R.id.rewardTv);
            progressTv = itemView.findViewById(R.id.progressTv);
            progressBar = itemView.findViewById(R.id.progressBar);
            expiresTv = itemView.findViewById(R.id.expiresTv);
        }
    }
}

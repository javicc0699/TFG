package com.example.helldiversbuildhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class BuildsFragment extends Fragment {

    private RecyclerView buildsRv;
    private TextView buildsVaciasTxt;
    private ProgressBar progresodeCarga;
    private List<Build> buildsList = new ArrayList<>();
    private BuildsAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SwipeRefreshLayout swipe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_builds, container, false);

        buildsRv = view.findViewById(R.id.buildsRv);
        buildsVaciasTxt = view.findViewById(R.id.buildsVaciasTxt);
        progresodeCarga = view.findViewById(R.id.progresodeCarga);
        swipe = view.findViewById(R.id.swipe);

        adapter = new BuildsAdapter(requireContext(), buildsList);
        buildsRv.setLayoutManager(new LinearLayoutManager(requireContext()));
        buildsRv.setAdapter(adapter);
        swipe.setOnRefreshListener(() -> {
            cargarBuilds();
        });

        cargarBuilds();

        return view;
    }

    // Este metodo carga las builds desde Firebase para mostrarlas en el RecyclerView.

    private void cargarBuilds() {

        swipe.setRefreshing(true);
        progresodeCarga.setVisibility(View.VISIBLE);
        buildsVaciasTxt.setVisibility(View.GONE);
        buildsRv.setVisibility(View.GONE);

        db.collection("builds")
                .orderBy("date", Query.Direction.DESCENDING)// Ordenar por fecha, luego lo puedo cambiar por mayor nº de likes cuando funcione.
                .addSnapshotListener((snapshots, error) -> {
                    swipe.setRefreshing(false);
                    progresodeCarga.setVisibility(View.GONE);

                    if (error != null) {
                        buildsVaciasTxt.setText("Error al cargar las builds");
                        buildsVaciasTxt.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (snapshots == null || snapshots.isEmpty()) {
                        // Sin builds
                        buildsList.clear();
                        adapter.notifyDataSetChanged();
                        buildsVaciasTxt.setText("No hay builds todavía");
                        buildsVaciasTxt.setVisibility(View.VISIBLE);
                        return;
                    }

                    // Hay datos: vacía y rellena la lista
                    buildsList.clear();
                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        Build b = doc.toObject(Build.class);
                        if (b != null) buildsList.add(b);
                    }
                    adapter.notifyDataSetChanged();

                    // Mostrar lista o el mensaje
                    if (buildsList.isEmpty()) {
                        buildsVaciasTxt.setText("No hay builds todavía");
                        buildsVaciasTxt.setVisibility(View.VISIBLE);
                    } else {
                        buildsRv.setVisibility(View.VISIBLE);
                    }
                });
    }


}

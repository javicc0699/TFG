package com.example.helldiversbuildhub;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanetsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PlanetAdapter adapter;
    private final int TEMPORIZADOR = 10_000; // 10 segundos
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable actualizador = new Runnable() {
        @Override
        public void run() {
            loadPlanets();
            handler.postDelayed(this, TEMPORIZADOR);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planets, container, false);
        recyclerView = view.findViewById(R.id.planetsRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Se inicializa el adapter con lista vacía mutable
        adapter = new PlanetAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Carga inicial y refresco periódico
        loadPlanets();
        handler.postDelayed(actualizador, TEMPORIZADOR);
        return view;
    }

    private void loadPlanets() {
        ApiService apiService = ApiClient.getApiService();
        apiService.getPlanets().enqueue(new Callback<List<Planet>>() {
            @Override
            public void onResponse(Call<List<Planet>> call, Response<List<Planet>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Planet> planets = response.body();
                    // Se ordena por número de jugadores
                    Collections.sort(planets, (p1, p2) ->
                            Integer.compare(p2.getPlayers(), p1.getPlayers())
                    );

                    // Se actualiza la lista interna del adapter y notificamos
                    adapter.updatePlanets(planets);
                } else {
                    Log.e("PlanetsFragment", "Código error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Planet>> call, Throwable t) {
                Log.e("PlanetsFragment", "Fallo: " + t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(actualizador);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(actualizador);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(actualizador);
    }
}

package com.example.helldiversbuildhub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlanetsFragment extends Fragment {


    private RecyclerView recyclerView;
    private PlanetAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planets, container, false);
        recyclerView = view.findViewById(R.id.planetsRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadPlanets();
        return view;

    }

    private void loadPlanets() {
        ApiService apiService = ApiClient.getApiService();
        apiService.getPlanets().enqueue(new Callback<List<Planet>>() {
            @Override
            public void onResponse(Call<List<Planet>> call, Response<List<Planet>> response) {
                if (response.isSuccessful()) {
                    List<Planet> planets = response.body();
                    Collections.sort(planets, (p1, p2) -> Integer.compare(p2.getPlayers(), p1.getPlayers())); // Esto hace que se ordenen los planetas dependiendo del numero de jugadores.
                    adapter = new PlanetAdapter(planets);
                    recyclerView.setAdapter(adapter);
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
}
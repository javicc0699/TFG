package com.example.helldiversbuildhub;

import static android.app.ProgressDialog.show;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import android.view.Gravity;
import androidx.appcompat.widget.Toolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment que muestra los Major Orders.
 */
public class OrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar loadingPb;
    private TextView emptyTv;
    private MajorOrderAdapter adapter;
    private List<MajorOrder> ordersList = new ArrayList<>();
    private final int TEMPORIZADOR = 30_000;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable actualizador = new Runnable() {
        @Override
        public void run() {
            loadMajorOrders();
            handler.postDelayed(this , TEMPORIZADOR);
        }
    };

    // JSON de ejemplo (empatado a tu muestra). Se usa si la API devuelve vacio o falla.
    private static final String SAMPLE_JSON = "[\n" +
            "  {\n" +
            "    \"id32\": 738112495,\n" +
            "    \"progress\": [1, 1, 1, 0],\n" +
            "    \"expiresIn\": 12069,\n" +
            "    \"setting\": {\n" +
            "      \"type\": 4,\n" +
            "      \"overrideTitle\": \"MAJOR ORDER\",\n" +
            "      \"overrideBrief\": \"The Supercolony has caused sudden outbreaks on multiple planets. They must be contained immediately.\",\n" +
            "      \"taskDescription\": \"Liberate all designated planets.\",\n" +
            "      \"tasks\": [\n" +
            "        {\n" +
            "          \"type\": 11,\n" +
            "          \"values\": [1, 1, 79],\n" +
            "          \"valueTypes\": [3, 11, 12]\n" +
            "        },\n" +
            "        {\n" +
            "          \"type\": 11,\n" +
            "          \"values\": [1, 1, 127],\n" +
            "          \"valueTypes\": [3, 11, 12]\n" +
            "        }\n" +
            "      ],\n" +
            "      \"reward\": {\n" +
            "        \"type\": 1,\n" +
            "        \"id32\": 897894480,\n" +
            "        \"amount\": 40\n" +
            "      },\n" +
            "      \"flags\": 0\n" +
            "    }\n" +
            "  }\n" +
            "]";

    public OrdersFragment() {
        // Constructor vacío obligatorio
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);



        recyclerView = view.findViewById(R.id.recyclerOrders);
        loadingPb = view.findViewById(R.id.loadingPb);
        emptyTv = view.findViewById(R.id.emptyTv);



        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MajorOrderAdapter(getContext(), ordersList);
        recyclerView.setAdapter(adapter);

        // Cargar datos de la API
        loadMajorOrders();

        return view;
    }



    /**
     * Llama al endpoint majorOrders. Si devuelve datos, los muestra.
     * Si la respuesta está vacía o hay error, carga datos de ejemplo para que en la presentacion no salga vacío.
     */
    private void loadMajorOrders() {
        // Mostrar ProgressBar y ocultar RecyclerView / texto vacío
        loadingPb.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyTv.setVisibility(View.GONE);

        ApiService api = ApiClient.getApiService();
        Call<List<MajorOrder>> call = api.getMajorOrders();
        call.enqueue(new Callback<List<MajorOrder>>() {
            @Override
            public void onResponse(@NonNull Call<List<MajorOrder>> call,
                                   @NonNull Response<List<MajorOrder>> response) {
                loadingPb.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    //si la API devolvió datos válidos
                    ordersList.clear();
                    ordersList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    // Si la API devolvió [] o response.body() == null
                    loadSampleOrders();
                    Toast.makeText(getContext(),
                            "No hay Major Orders activas. Mostrando ejemplo.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MajorOrder>> call,
                                  @NonNull Throwable t) {
                loadingPb.setVisibility(View.GONE);
                // Error en la llamada, se carga el ejemplo de orden.
                loadSampleOrders();
                Toast.makeText(getContext(),
                        "Error al obtener las Major Orders. Mostrando ejemplo.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Parsea el JSON de ejemplo y lo muestra en pantalla.
     */
    private void loadSampleOrders() {
        // Ocultar progress y mostrar RecyclerView
        loadingPb.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        emptyTv.setVisibility(View.GONE);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<MajorOrder>>(){}.getType();
        List<MajorOrder> sampleList = gson.fromJson(SAMPLE_JSON, listType);

        // Actualizar adapter
        ordersList.clear();
        ordersList.addAll(sampleList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(actualizador);
    }

    // Reanuda la actualizacion de datos cuando se resuma el fragment.
    @Override
    public void onResume() {
        super.onResume();
        handler.post(actualizador);
    }

    // Parar de actualizar cuando se pause la actividad para no saturar la api.
    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(actualizador);
    }
}

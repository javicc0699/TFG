package com.example.helldiversbuildhub;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        bottomNavigationView = findViewById(R.id.bottom_navigation);

      //  cargarBuildPrueba(db);


        loadFragment(new PlanetsFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();

            if (id == R.id.frag1) {
                fragment = new PlanetsFragment(); // Muestra los planetas
            } else if (id == R.id.frag2) {
                fragment = new BuildsFragment(); // Crear luego
            } else if (id == R.id.frag3) {
                fragment = new UploadFragment(); // Crear luego
            } else if (id == R.id.frag4) {
                fragment = new OrdersFragment(); // Crear luego
            }

            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }

    private void cargarBuildPrueba(FirebaseFirestore db) {

        String buildId = db.collection("builds").document().getId();

        // Crear build
        Build build = new Build(
                buildId,
                "user123",
                "LAS-16 Sickle",
                "P-19 Redeemer",
                "Engineering Kit",
                Arrays.asList("Eagle Airstrike", "Reinforce", "Supply Pack", "Machine Gun"),
                0,
                0,
                "2025-05-27"
        );

        // Guardarla en Firestore
        db.collection("builds")
                .document(buildId)
                .set(build)
                .addOnSuccessListener(aVoid -> Log.d("FIREBASE", "Build subida correctamente"))
                .addOnFailureListener(e -> Log.e("FIREBASE", "Error al subir la build", e));
    }
}

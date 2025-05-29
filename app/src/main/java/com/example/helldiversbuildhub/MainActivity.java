package com.example.helldiversbuildhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializa Firebase
        FirebaseApp.initializeApp(this);
        // Inicializa Auth
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        loadFragment(new PlanetsFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();

            if (id == R.id.frag1) {
                fragment = new PlanetsFragment();
            } else if (id == R.id.frag2) {
                fragment = new BuildsFragment();
            } else if (id == R.id.frag3) {
                fragment = new UploadFragment();
            } else if (id == R.id.frag4) {
                fragment = new OrdersFragment();
            }

            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Comprueba sesión: si no hay usuario, vuelve al login
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.d("MainActivity", "No user logged in, redirecting to LoginActivity");
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }

}

package com.example.helldiversbuildhub;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
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

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
        );


        setContentView(R.layout.activity_main);

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


// Carga la fuente personalizada
        Typeface orbitron = ResourcesCompat.getFont(this, R.font.orbitron_bold);

// Recorre todos los ítems del bottomnavigationview
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            final MenuItem item = bottomNavigationView.getMenu().getItem(i);


            bottomNavigationView.post(() -> {
                View itemView = bottomNavigationView.findViewById(item.getItemId());

                if (itemView instanceof ViewGroup) {
                    setFontToViewGroup((ViewGroup) itemView, orbitron);
                }
            });
        }

    }

    // Este metodo hace que la fuente se aplique tambien en los botones del navigation.
    private void setFontToViewGroup(ViewGroup viewGroup, Typeface font) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTypeface(font);
            } else if (child instanceof ViewGroup) {
                setFontToViewGroup((ViewGroup) child, font);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Comprueba sesión si no hay usuario, vuelve al login
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

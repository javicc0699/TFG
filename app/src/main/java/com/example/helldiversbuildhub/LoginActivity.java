package com.example.helldiversbuildhub;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.*;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        Intent data = result.getData();
                        if (data == null) {
                            Log.w(TAG, "Error al iniciar sesión con Google");
                            return;
                        }
                        handleSignInResult(data);
                    }
            );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        // Configurar Google SignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Botón estándar de Google
        SignInButton btnGoogle = findViewById(R.id.sign_in_button);
        btnGoogle.setSize(SignInButton.SIZE_WIDE);
        btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });

        // Botón de Elegir cuenta
        MaterialButton btnChoose = findViewById(R.id.btnChooseAccount);
        btnChoose.setOnClickListener(v -> {
            Intent chooseIntent = mGoogleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(chooseIntent);
        });
    }

    private void handleSignInResult(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            String idToken = task.getResult(ApiException.class).getIdToken();
            if (idToken != null) {
                AuthCredential cred = GoogleAuthProvider.getCredential(idToken, null);
                mAuth.signInWithCredential(cred)
                        .addOnCompleteListener(this, authTask -> {
                            if (authTask.isSuccessful()) {
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            } else {
                                Log.e(TAG, "Firebase Auth failed", authTask.getException());
                                Toast.makeText(this,
                                        "Error autenticando con Firebase",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Log.e(TAG, "idToken null");
                Toast.makeText(this,
                        "No se recibió token de Google",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (ApiException e) {
            Log.e(TAG, "Google Sign-In error", e);
            Toast.makeText(this,
                    "Error en inicio de sesión de Google",
                    Toast.LENGTH_SHORT).show();
        }
    }
}

package com.example.helldiversbuildhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class UserFragment extends Fragment {

    private ImageView profileImage;
    private TextView profileName;
    private TextView profileEmail;
    private MaterialButton btnLogout;

    public UserFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        profileImage = view.findViewById(R.id.profileImage);
        profileName  = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        btnLogout    = view.findViewById(R.id.btnLogoutProfile);

        // 2) Obtener usuario actual de Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Nombre
            String name = user.getDisplayName();
            profileName.setText(name != null ? name : getString(R.string.loading));

            // Email
            String email = user.getEmail();
            profileEmail.setText(email != null ? email : "");

            // Foto de perfil
            if (user.getPhotoUrl() != null) {
                String photoUrl = user.getPhotoUrl().toString();
                // Carga con Glide
                Glide.with(this)
                        .load(photoUrl)
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_person_circle)
                        .error(R.drawable.ic_person_circle)
                        .into(profileImage);
            } else {
                // Poner icono por defecto
                profileImage.setImageResource(R.drawable.ic_person_circle);
            }
        } else {
            // Si no hay usuario, redirigir a Login
            Toast.makeText(requireContext(),"No hay usuario autenticado", Toast.LENGTH_SHORT).show();
            requireActivity().startActivity(new Intent(getContext(), LoginActivity.class));
            requireActivity().finish();
        }

        //Configurar botón de Logout
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            // Volver la LoginActivity, y se usa FLAG_ACTIVITY_CLEAR_TASK para eliminar el historial de pantallas y que el usuario no
            // pueda volver atras usando el botón de volver atrás del móvil.
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            // mostrar Toast de confirmación
            Toast.makeText(getContext(),
                    "Sesión cerrada", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}

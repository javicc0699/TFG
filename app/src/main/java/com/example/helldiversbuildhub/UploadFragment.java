package com.example.helldiversbuildhub;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UploadFragment extends Fragment {

    private List<Stratagem> stratagemsList;
    private List<String> baseNames;
    private String[] selectedItems = new String[4];
    private Spinner[] spinnersStrats = new Spinner[4];
    private ImageView[] iconosStrats = new ImageView[4];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_upload, container, false);


        spinnersStrats[0] = root.findViewById(R.id.firstStratSpn);
        spinnersStrats[1] = root.findViewById(R.id.secondStratSpn);
        spinnersStrats[2] = root.findViewById(R.id.thirdStratSpn);
        spinnersStrats[3] = root.findViewById(R.id.fourthStratSpn);

        iconosStrats[0] = root.findViewById(R.id.firstStratImg);
        iconosStrats[1] = root.findViewById(R.id.secondStratImg);
        iconosStrats[2] = root.findViewById(R.id.thirdStratImg);
        iconosStrats[3] = root.findViewById(R.id.fourthStratImg);

        String[] nombres = getResources().getStringArray(R.array.estratagemas_nombre);

        // He usado un TypedArray que sirve para cargar un array con Recursos y obtener sus ID.
        TypedArray iconos = getResources().obtainTypedArray(R.array.estratagemas_iconos);

        stratagemsList = new ArrayList<>();
        for (int i = 0; i < nombres.length; i++) {
            int resId = iconos.getResourceId(i, 0);
            stratagemsList.add(new Stratagem(nombres[i], resId));
        }
        iconos.recycle();


        baseNames = Arrays.asList(nombres);

        // Se inicializa los spinner con una copia unica para cada uno de ellos
        for (int i = 0; i < spinnersStrats.length; i++) {
            final int indice = i;
            List<String> copyNames = new ArrayList<>();
            copyNames.add("-- Selecciona --");
            copyNames.addAll(baseNames);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, copyNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnersStrats[indice].setAdapter(adapter);
            spinnersStrats[indice].setSelection(0, false);

            spinnersStrats[indice].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // La posicion 0 es el placeholder
                    if (position == 0) {
                        selectedItems[indice] = null;
                        iconosStrats[indice].setImageDrawable(null);
                    } else {
                        String sel = adapter.getItem(position);
                        selectedItems[indice] = sel;
                        actualizarIcono(indice, sel);
                    }

                    actualizarAdaptadores();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }

        return root;
    }


    private void actualizarAdaptadores() {
        for (int i = 0; i < spinnersStrats.length; i++) {
            Spinner spinner = spinnersStrats[i];
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
            String actual = selectedItems[i];


            List<String> newList = new ArrayList<>();
            newList.add("-- Selecciona --");
            for (String name : baseNames) {
                boolean usado = false;
                for (int j = 0; j < selectedItems.length; j++) {
                    if (j == i) continue;
                    if (name.equals(selectedItems[j])) {
                        usado = true;
                        break;
                    }
                }
                if (!usado) newList.add(name);
            }

            adapter.clear();
            adapter.addAll(newList);
            adapter.notifyDataSetChanged();


            if (actual == null) {
                spinner.setSelection(0, false);
            } else {
                int pos = adapter.getPosition(actual);
                if (pos >= 0) spinner.setSelection(pos, false);
            }
        }
    }

    private void actualizarIcono(int idx, String nombre) {
        for (Stratagem s : stratagemsList) {
            if (s.nombre.equals(nombre)) {
                iconosStrats[idx].setImageResource(s.icono);
                return;
            }
        }
        iconosStrats[idx].setImageDrawable(null);
    }
}
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

import androidx.annotation.DrawableRes;
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
    private Spinner[] spinners = new Spinner[4];
    private ImageView[] images   = new ImageView[4];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_upload, container, false);


        spinners[0] = root.findViewById(R.id.firstStratSpn);
        spinners[1] = root.findViewById(R.id.secondStratSpn);
        spinners[2] = root.findViewById(R.id.thirdStratSpn);
        spinners[3] = root.findViewById(R.id.fourthStratSpn);

        images[0] = root.findViewById(R.id.firstStratImg);
        images[1] = root.findViewById(R.id.secondStratImg);
        images[2] = root.findViewById(R.id.thirdStratImg);
        images[3] = root.findViewById(R.id.fourthStratImg);

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
        for (int i = 0; i < spinners.length; i++) {
            final int idx = i;
            List<String> copyNames = new ArrayList<>();
            copyNames.add("-- Selecciona --");
            copyNames.addAll(baseNames);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, copyNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinners[idx].setAdapter(adapter);
            spinners[idx].setSelection(0, false);

            spinners[idx].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // La posicion 0 es el placeholder
                    if (position == 0) {
                        selectedItems[idx] = null;
                        images[idx].setImageDrawable(null);
                    } else {
                        String sel = adapter.getItem(position);
                        selectedItems[idx] = sel;
                        actualizarIcono(idx, sel);
                    }
                    // Refresh all adapters to remove taken selections
                    actualizarAdaptadores();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }

        return root;
    }


    private void actualizarAdaptadores() {
        for (int i = 0; i < spinners.length; i++) {
            Spinner spinner = spinners[i];
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
            String current = selectedItems[i];


            List<String> newList = new ArrayList<>();
            newList.add("-- Selecciona --");
            for (String name : baseNames) {
                boolean usedElsewhere = false;
                for (int j = 0; j < selectedItems.length; j++) {
                    if (j == i) continue;
                    if (name.equals(selectedItems[j])) {
                        usedElsewhere = true;
                        break;
                    }
                }
                if (!usedElsewhere) newList.add(name);
            }

            adapter.clear();
            adapter.addAll(newList);
            adapter.notifyDataSetChanged();


            if (current == null) {
                spinner.setSelection(0, false);
            } else {
                int pos = adapter.getPosition(current);
                if (pos >= 0) spinner.setSelection(pos, false);
            }
        }
    }

    private void actualizarIcono(int idx, String nombre) {
        for (Stratagem s : stratagemsList) {
            if (s.nombre.equals(nombre)) {
                images[idx].setImageResource(s.icono);
                return;
            }
        }
        images[idx].setImageDrawable(null);
    }
}
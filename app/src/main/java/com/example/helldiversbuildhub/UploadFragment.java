package com.example.helldiversbuildhub;

import android.content.res.TypedArray;
import android.media.Image;
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
    private List<String> nombresBase;
    private String[] itemsSeleccionados = new String[4];
    private Spinner[] spinnersStrats = new Spinner[4];
    private ImageView[] iconosStrats = new ImageView[4];
    private Spinner primaryWpnSpn;
    private ImageView primaryWpnImg;
    private Spinner secondaryWpnSpn;
    private ImageView secondaryWpnImg;
    private Spinner armorPassiveSpn;
    private ImageView armorPassiveImg;
    private Spinner boosterSpn;
    private ImageView boosterImg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_upload, container, false);

        // Referencias a Spinners e ImageViews
        spinnersStrats[0] = root.findViewById(R.id.firstStratSpn);
        spinnersStrats[1] = root.findViewById(R.id.secondStratSpn);
        spinnersStrats[2] = root.findViewById(R.id.thirdStratSpn);
        spinnersStrats[3] = root.findViewById(R.id.fourthStratSpn);

        iconosStrats[0] = root.findViewById(R.id.firstStratImg);
        iconosStrats[1] = root.findViewById(R.id.secondStratImg);
        iconosStrats[2] = root.findViewById(R.id.thirdStratImg);
        iconosStrats[3] = root.findViewById(R.id.fourthStratImg);

        // Carga de datos de estratagemas
        String[] nombres = getResources().getStringArray(R.array.estratagemas_nombre);
        TypedArray iconos = getResources().obtainTypedArray(R.array.estratagemas_iconos);
        stratagemsList = new ArrayList<>();
        for (int i = 0; i < nombres.length; i++) {
            int resId = iconos.getResourceId(i, 0);
            stratagemsList.add(new Stratagem(nombres[i], resId));
        }
        iconos.recycle();
        nombresBase = Arrays.asList(nombres);

        // Configura los spinners e iconos de estratagemas
        configurarSpinnersEstratagemas();

        // Configura los spinners e iconos de armas primarias
        primaryWpnSpn = root.findViewById(R.id.primaryWpnSpn);
        primaryWpnImg = root.findViewById(R.id.primaryWpnImg);
        configurarSpinnersArmasPrimarias(root);

        // Configura los spinners e iconos de armas secundarias
        secondaryWpnImg = root.findViewById(R.id.secondaryWpnImg);
        secondaryWpnSpn = root.findViewById(R.id.secondaryWpnSpn);
        configurarSpinnersArmasSecundarias(root);

        // Configura los spinners de las pasivas de las armaduras;
        armorPassiveImg = root.findViewById(R.id.armorPassiveImg);
        armorPassiveSpn = root.findViewById(R.id.armorPassiveSpn);
        configurarSpinnersPasivas(root);

        // Configura los spinners e iconos de los potenciadores;
        boosterSpn = root.findViewById(R.id.boosterSpn);
        boosterImg = root.findViewById(R.id.boosterImg);
        configurarSpinnersPotenciadores(root);

        return root;
    }

    // Este metodo inicializa los spinners de estratagemas y sus iconos
    private void configurarSpinnersEstratagemas() {
        for (int i = 0; i < spinnersStrats.length; i++) {
            final int indice = i;
            // Lista con placeholder + nombres disponibles
            List<String> listaCopia = new ArrayList<>();
            listaCopia.add("-- Selecciona --");
            listaCopia.addAll(nombresBase);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    listaCopia
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnersStrats[indice].setAdapter(adapter);
            spinnersStrats[indice].setSelection(0, false);

            spinnersStrats[indice].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        itemsSeleccionados[indice] = null;
                        iconosStrats[indice].setImageDrawable(null);
                    } else {
                        String sel = adapter.getItem(position);
                        itemsSeleccionados[indice] = sel;
                        actualizarIcono(indice, sel);
                    }
                    actualizarAdaptadores();
                }
                @Override public void onNothingSelected(AdapterView<?> parent) { }
            });
        }
    }

    // Este metodo actualiza los adaptadores de los spinners si se utiliza una entrada en uno de los spiiners
    // para poder eliminarlo de los otros y que no se repitan
    private void actualizarAdaptadores() {
        for (int i = 0; i < spinnersStrats.length; i++) {
            Spinner spinner = spinnersStrats[i];
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
            String actual = itemsSeleccionados[i];

            List<String> nuevaLista = new ArrayList<>();
            nuevaLista.add("-- Selecciona --");
            for (String nombre : nombresBase) {
                boolean usado = false;
                for (int j = 0; j < itemsSeleccionados.length; j++) {
                    if (j == i) continue;
                    if (nombre.equals(itemsSeleccionados[j])) {
                        usado = true;
                        break;
                    }
                }
                if (!usado) nuevaLista.add(nombre);
            }

            adapter.clear();
            adapter.addAll(nuevaLista);
            adapter.notifyDataSetChanged();

            if (actual == null) {
                spinner.setSelection(0, false);
            } else {
                int pos = adapter.getPosition(actual);
                if (pos >= 0) spinner.setSelection(pos, false);
            }
        }
    }

    private void configurarSpinnersArmasPrimarias(View root) {

        String[] armas = getResources().getStringArray(R.array.armas_primarias);
        TypedArray typedArray = getResources().obtainTypedArray(R.array.armas_primarias_iconos);
        int typedArrayLongitud = typedArray.length();
        final int[] iconRes = new int[typedArrayLongitud];
        for (int i = 0; i < typedArrayLongitud; i++) {
            iconRes[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();


        List<String> lista = new ArrayList<>();
        lista.add("-- Selecciona arma primaria --");
        lista.addAll(Arrays.asList(armas));


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                lista
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        primaryWpnSpn.setAdapter(adapter);
        primaryWpnSpn.setSelection(0, false);


        primaryWpnSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    primaryWpnImg.setImageDrawable(null);
                } else {
                    // Como el placeholder ocupa índice 0, el drawable va en posición-1
                    primaryWpnImg.setImageResource(iconRes[position - 1]);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {
                primaryWpnImg.setImageDrawable(null);
            }
        });
    }

    private void configurarSpinnersArmasSecundarias(View root) {

        String[] armas = getResources().getStringArray(R.array.armas_secundarias);
        TypedArray typedArray = getResources().obtainTypedArray(R.array.armas_secundarias_iconos);
        int typedArrayLongitud = typedArray.length();
        final int[] iconRes = new int[typedArrayLongitud];
        for (int i = 0; i < typedArrayLongitud; i++) {
            iconRes[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();


        List<String> lista = new ArrayList<>();
        lista.add("-- Selecciona arma secundaria --");
        lista.addAll(Arrays.asList(armas));


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secondaryWpnSpn.setAdapter(adapter);
        secondaryWpnSpn.setSelection(0, false);


        secondaryWpnSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    secondaryWpnImg.setImageDrawable(null);
                } else {
                    // Como el placeholder ocupa índice 0, el drawable va en posición-1
                    secondaryWpnImg.setImageResource(iconRes[position - 1]);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {
                secondaryWpnImg.setImageDrawable(null);
            }
        });
    }

    private void configurarSpinnersPasivas(View root){

        String[] pasivas = getResources().getStringArray(R.array.pasivas_armadura);
        TypedArray typedArray = getResources().obtainTypedArray(R.array.pasivas_armadura_iconos);
        int typedArrayLongitud = typedArray.length();
        final int[] iconRes = new int[typedArrayLongitud];
        for (int i = 0; i < typedArrayLongitud; i++) {
            iconRes[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();


        List<String> lista = new ArrayList<>();
        lista.add("-- Selecciona una pasiva --");
        lista.addAll(Arrays.asList(pasivas));


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        armorPassiveSpn.setAdapter(adapter);
        armorPassiveSpn.setSelection(0, false);


        armorPassiveSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    armorPassiveImg.setImageDrawable(null);
                } else {
                    // Como el placeholder ocupa índice 0, el drawable va en posición-1
                    armorPassiveImg.setImageResource(iconRes[position - 1]);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {
                armorPassiveImg.setImageDrawable(null);
            }
        });
    }

    private void configurarSpinnersPotenciadores(View root){

        String[] potenciadores = getResources().getStringArray(R.array.potenciadores_nombres);
        TypedArray typedArray = getResources().obtainTypedArray(R.array.potenciadores_iconos);
        int typedArrayLongitud = typedArray.length();
        final int[] iconRes = new int[typedArrayLongitud];
        for (int i = 0; i < typedArrayLongitud; i++) {
            iconRes[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();


        List<String> lista = new ArrayList<>();
        lista.add("-- Selecciona un potenciador --");
        lista.addAll(Arrays.asList(potenciadores));


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boosterSpn.setAdapter(adapter);
        boosterSpn.setSelection(0, false);


        boosterSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    boosterImg.setImageDrawable(null);
                } else {
                    // Como el placeholder ocupa índice 0, el drawable va en posición-1
                    boosterImg.setImageResource(iconRes[position - 1]);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {
                boosterImg.setImageDrawable(null);
            }
        });
    }

    private void actualizarIcono(int indice, String nombre) {
        for (Stratagem s : stratagemsList) {
            if (s.nombre.equals(nombre)) {
                iconosStrats[indice].setImageResource(s.icono);
                return;
            }
        }
        iconosStrats[indice].setImageDrawable(null);
    }
}

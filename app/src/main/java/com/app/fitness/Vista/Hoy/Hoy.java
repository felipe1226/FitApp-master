package com.app.fitness.Vista.Hoy;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.fitness.Modelo.PlanEntrenamiento.ListaPlanRutinas;
import com.app.fitness.Modelo.PlanEntrenamiento.ListaRutinas;
import com.app.fitness.R;
import com.app.fitness.Modelo.PlanEntrenamiento.ListaEjercicios;

import java.util.ArrayList;

public class Hoy extends Fragment {

    private ArrayList<ListaEjercicios> ejercicios;
    private ArrayList<ListaRutinas> rutinas;
    private ArrayList<ListaPlanRutinas> planRutinas;

    private RecyclerView rvHoy;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_hoy, container, false);

        initView(v);

        generarEjercicios();
        generarRutinas();
        generarPlanesRutinas();

        return v;
    }

    private void initView(View v) {

        rvHoy = v.findViewById(R.id.rvHoy);
    }

    private void generarEjercicios(){

        ejercicios = new ArrayList<>();

        ejercicios.add(new ListaEjercicios(1,
                "Press pecho",
                4,
                10,
                "20 Kg",
                "30 Seg",
                "Agarrar la barra..."));

        ejercicios.add(new ListaEjercicios(2,
                "Press militar",
                4,
                8,
                "15 Kg",
                "45 Seg",
                "Agarrar la barra..."));

        ejercicios.add(new ListaEjercicios(3,
                "Banco plano",
                4,
                10,
                "25 Kg",
                "60 Seg",
                "Agarrar la barra..."));
    }

    private void generarRutinas(){

        rutinas = new ArrayList<>();

        ArrayList<ListaEjercicios> listaEjercicios = new ArrayList<>();

        listaEjercicios.add(ejercicios.get(0));
        listaEjercicios.add(ejercicios.get(1));

        rutinas.add(new ListaRutinas(1,
                "Pecho resistencia",
                "Metabolica",
                2,
                "70 min",
                listaEjercicios));

        listaEjercicios = new ArrayList<>();

        listaEjercicios.add(ejercicios.get(0));
        listaEjercicios.add(ejercicios.get(2));

        rutinas.add(new ListaRutinas(2,
                "Pierna intensivo",
                "Metabolica",
                3,
                "60 min",
                listaEjercicios));

        listaEjercicios = new ArrayList<>();

        listaEjercicios.add(ejercicios.get(1));
        listaEjercicios.add(ejercicios.get(2));

        rutinas.add(new ListaRutinas(3,
                "Espalda bombeo",
                "Metabolica",
                5,
                "50 min",
                listaEjercicios));

        listaEjercicios = new ArrayList<>();

        listaEjercicios.add(ejercicios.get(1));
        listaEjercicios.add(ejercicios.get(2));

        rutinas.add(new ListaRutinas(4,
                "Espalda bombeo",
                "Metabolica",
                30,
                "50 min",
                listaEjercicios));

        listaEjercicios = new ArrayList<>();

        listaEjercicios.add(ejercicios.get(1));
        listaEjercicios.add(ejercicios.get(2));

        rutinas.add(new ListaRutinas(5,
                "Espalda bombeo",
                "Metabolica",
                2,
                "50 min",
                listaEjercicios));
    }

    private void generarPlanesRutinas(){

        planRutinas = new ArrayList<>();

        ArrayList<ListaRutinas> listaRutinas = new ArrayList<>();

        listaRutinas.add(rutinas.get(0));
        listaRutinas.add(rutinas.get(1));
        listaRutinas.add(rutinas.get(2));

        planRutinas.add(new ListaPlanRutinas(1,
                "Reduccion de grasa",
                "Grasa localizada",
                10,
                listaRutinas));


        listaRutinas.clear();

        listaRutinas.add(rutinas.get(0));
        listaRutinas.add(rutinas.get(1));

        planRutinas.add(new ListaPlanRutinas(2,
                "Aumento de masa muscular",
                "Mejora de fuerza",
                8,
                listaRutinas));

        AdapterHoy adapter = new AdapterHoy(getContext(), planRutinas);
        rvHoy.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvHoy.setAdapter(adapter);
    }
}

package com.app.fitness.Vista.Perfil;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.fitness.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Informe extends Fragment {


    public Informe() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_informe_perfil, container, false);
    }

}

package com.jjv.proyectointegradorv1.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jjv.proyectointegradorv1.Adapters.Publicaciones_Adapter;
import com.jjv.proyectointegradorv1.Objects.Publicacion;
import com.jjv.proyectointegradorv1.R;

/**
 * Created by javi0 on 11/01/2017.
 */

public class Buscar_viaje extends Fragment {

    ListView listaPublicaciones;
    Publicacion[]publicaciones;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buscar_viaje, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listaPublicaciones = (ListView) view.findViewById(R.id.lista_publicaciones);

        Publicaciones_Adapter adapter = new Publicaciones_Adapter(getContext(),publicaciones);

        listaPublicaciones.setAdapter(adapter);

    }
}

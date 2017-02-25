package com.jjv.proyectointegradorv1.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jjv.proyectointegradorv1.Adapters.Publicaciones_Adapter;
import com.jjv.proyectointegradorv1.Objects.Publicacion;
import com.jjv.proyectointegradorv1.R;

import java.util.ArrayList;

/**
 * Created by javi0 on 11/01/2017.
 */

public class Mis_viajes extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<Publicacion> publicaciones;
    ListView listaMisViajes;
    Publicaciones_Adapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mis_viajes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Log.i("NOMBRE USR:",user.getDisplayName());

        listaMisViajes = (ListView) view.findViewById(R.id.listMisViajes);
        publicaciones = getPublicacionesUsuario();
        adapter = new Publicaciones_Adapter(view.getContext(),publicaciones);
        listaMisViajes.setAdapter(adapter);
    }

    private ArrayList<Publicacion> getPublicacionesUsuario() {
        ArrayList<Publicacion> publicaciones = new ArrayList<>();
        //TODO: POR DESARROLLAR LA RECUPERACION DE LOS VIAJES DE CADA USUARIO
        return publicaciones;

    }
}

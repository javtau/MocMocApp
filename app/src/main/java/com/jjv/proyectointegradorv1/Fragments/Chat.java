package com.jjv.proyectointegradorv1.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjv.proyectointegradorv1.R;

/**
 * Created by javi0 on 11/01/2017.
 */

public class Chat extends Fragment {

    public static final String TAG = "Chat";
    public static final int SEL_FOTO = 1;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseUser usuarioLogueado;
    private EditText etMensaje;
    private Button btnEnviar;
    private RecyclerView listaMensajes;
    private ImageButton btnImagen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initDatabase();


    }

    private void initViews(View v) {
        etMensaje = (EditText) v.findViewById(R.id.et_msg);
        btnEnviar = (Button) v.findViewById(R.id.btn_enviar);
        listaMensajes = (RecyclerView) v.findViewById(R.id.lista_msgs);
        btnImagen = (ImageButton) v.findViewById(R.id.btn_imagen);

        // asigna un click listener al btnImagen para permitir al usuario seleccionar una imagen
        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(i, SEL_FOTO);
            }
        });

    }

    private void initDatabase() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        usuarioLogueado = FirebaseAuth.getInstance().getCurrentUser();

    }
}

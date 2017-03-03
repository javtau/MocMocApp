package com.jjv.proyectointegradorv1.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjv.proyectointegradorv1.Adapters.ChatAdapter;
import com.jjv.proyectointegradorv1.Objects.MensajeChat;
import com.jjv.proyectointegradorv1.R;

import java.util.ArrayList;

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
    //private ImageButton btnImagen;
    private ChatAdapter chatAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDatabase();
        initViews(view);
    }

    private void initViews(View v) {
        etMensaje = (EditText) v.findViewById(R.id.et_msg);
        btnEnviar = (Button) v.findViewById(R.id.btn_enviar);
        listaMensajes = (RecyclerView) v.findViewById(R.id.lista_msgs);
        //btnImagen = (ImageButton) v.findViewById(R.id.btn_imagen);

        // asigna un click listener al btnImagen para permitir al usuario seleccionar una imagen
        /*btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(i, SEL_FOTO);
            }
        });*/

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MensajeChat msgChat = new MensajeChat(etMensaje.getText().toString(), usuarioLogueado.getDisplayName());
                // push mensaje a la bd
                mRef.push().setValue(msgChat);
                etMensaje.setText("");
                etMensaje.setHint(getString(R.string.chat_hint));

            }
        });

        // TODO: revisar que esto sea correcto, puede ser que sea necesario pasarle un fragment y por tanto habria que inicializar el constructor
        // con uno
    }

    private void initDatabase() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Chat");
        usuarioLogueado = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioLogueado != null){
            mRef.addChildEventListener(new ChildEventListener() {
                ArrayList<MensajeChat> mensajes = new ArrayList<MensajeChat>();
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    // recupera el mensaje y lo añade en la UI
                    MensajeChat msg = dataSnapshot.getValue(MensajeChat.class);
                    mensajes.add(msg);
                    chatAdapter = new ChatAdapter(mensajes);
                    chatAdapter.notifyDataSetChanged();
                    // sin esta linea el layout no muestra nada
                    listaMensajes.setLayoutManager(new LinearLayoutManager(getContext()));
                    listaMensajes.setAdapter(chatAdapter);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    MensajeChat msg = dataSnapshot.getValue(MensajeChat.class);
                    mensajes.add(msg);
                    chatAdapter = new ChatAdapter(mensajes);
                    chatAdapter.notifyDataSetChanged();
                    // sin esta linea el recycler view  no muestra nada
                    listaMensajes.setLayoutManager(new LinearLayoutManager(getContext()));
                    listaMensajes.setAdapter(chatAdapter);

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}

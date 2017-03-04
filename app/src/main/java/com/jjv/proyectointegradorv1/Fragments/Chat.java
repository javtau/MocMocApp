package com.jjv.proyectointegradorv1.Fragments;


import android.app.Dialog;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjv.proyectointegradorv1.Adapters.ChatAdapter;
import com.jjv.proyectointegradorv1.Adapters.Publicaciones_Adapter;
import com.jjv.proyectointegradorv1.Objects.MensajeChat;
import com.jjv.proyectointegradorv1.Objects.Publicacion;
import com.jjv.proyectointegradorv1.R;
import com.jjv.proyectointegradorv1.UI.ConversacionChat;

import java.util.ArrayList;

/**
 * Created by javi0 on 11/01/2017.
 */

public class Chat extends Fragment {
    private static final String KEY_PUB ="KEY_DE_PUBLICACION";
    private FirebaseUser user;
    private ListView listaMisViajes;
    private Publicaciones_Adapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference dbref;
    private ChildEventListener childEvent;
    private Publicacion publicacion;
    private ArrayList<Publicacion>publicacionesbckup;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            Log.i("NOMBRE USR:", user.getDisplayName());
            Log.i("UID USR:", user.getUid());
            dbref = database.getReference("user-trips/" + user.getUid());

            listaMisViajes = (ListView) view.findViewById(R.id.listMisViajes);

            childEvent = new ChildEventListener() {
                ArrayList<Publicacion> publicaciones = new ArrayList<>();

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    publicacion = dataSnapshot.getValue(Publicacion.class);
                    publicaciones.add(publicacion);
                    adapter = new Publicaciones_Adapter(view.getContext(), publicaciones,R.layout.item_sala_viaje);
                    listaMisViajes.setAdapter(adapter);
                    publicacionesbckup = publicaciones;
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    publicacion = dataSnapshot.getValue(Publicacion.class);
                    int pos = getPosition(publicaciones,publicacion);
                    if(pos>-1){
                        publicaciones.remove(pos);
                        publicaciones.add(pos, publicacion);
                        adapter = new Publicaciones_Adapter(view.getContext(), publicaciones,R.layout.item_sala_viaje);
                        listaMisViajes.setAdapter(adapter);
                        publicacionesbckup = publicaciones;
                    }
                    /*publicacion = dataSnapshot.getValue(Publicacion.class);
                    int pos = getPosition(publicaciones, publicacion);
                    if (pos !=-1) {
                        publicaciones.remove(pos);
                        publicaciones.add(pos, publicacion);
                        adapter = new Publicaciones_Adapter(view.getContext(), publicaciones);
                        listaMisViajes.setAdapter(adapter);
                    }*/

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.i("se ejecutaCC","salta onChildRemoved");
                    publicacion = dataSnapshot.getValue(Publicacion.class);
                    int pos = getPosition(publicaciones,publicacion);
                    if(pos>-1){
                        publicaciones.remove(pos);
                        adapter = new Publicaciones_Adapter(view.getContext(), publicaciones,R.layout.item_sala_viaje);
                        listaMisViajes.setAdapter(adapter);
                        publicacionesbckup = publicaciones;

                    }
                    /*publicacion = dataSnapshot.getValue(Publicacion.class);
                    int pos = getPosition(publicaciones, publicacion);
                    if (pos !=-1) {
                        publicaciones.remove(pos);
                        adapter = new Publicaciones_Adapter(view.getContext(), publicaciones);
                        listaMisViajes.setAdapter(adapter);
                    }*/

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            dbref.addChildEventListener(childEvent);
            listaMisViajes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(getContext(), ConversacionChat.class);
                    i.putExtra(KEY_PUB,publicacionesbckup.get(position).getKeyViaje());
                    startActivity(i);
                }
            });


        }
    }

    public int getPosition(ArrayList<Publicacion> array, Publicacion data) {
        int pos = -1;
        boolean esEncontrado = false;
        for (int i = 0; i < array.size() && !esEncontrado; i++) {
            if (array.get(i).getKeyViaje().equals(data.getKeyViaje())) {
                esEncontrado = true;
                pos = i;
            }
        }
        return pos;
    }


}
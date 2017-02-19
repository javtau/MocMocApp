package com.jjv.proyectointegradorv1.Fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjv.proyectointegradorv1.Adapters.Publicaciones_RV_adapter;
import com.jjv.proyectointegradorv1.Objects.Publicacion;
import com.jjv.proyectointegradorv1.R;

import java.util.ArrayList;

/**
 * Created by javi0 on 11/01/2017.
 */

public class Buscar_viaje extends Fragment  {

    private final String TAG = Buscar_viaje.class.getSimpleName();
    //private ListView listaPublicaciones;
    private ArrayList<Publicacion> publicaciones = new ArrayList<>();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private ChildEventListener childEvent;
    private Publicacion publica;
    private Dialog customDialog ;
    private RecyclerView rv;
    private Publicaciones_RV_adapter.OnItemClickListener listenerRv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buscar_viaje, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // listaPublicaciones = (ListView) view.findViewById(R.id.lista_publicaciones);
        //listaPublicaciones.setOnItemClickListener(this);
        // implementacion recycler
        rv= (RecyclerView) view.findViewById(R.id.lista_publicaciones);
        /*
        Si estás seguro que el tamaño del RecyclerView no se cambiará,
        puedes añadirlo lo siguiente para mejorar el desempeño:
         */
        rv.setHasFixedSize(true);
        //para manejar el recycler view como una lista es necesario un manager
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);



        myRef = database.getReference("trip");// hacemos referencia a la rama donde se almacenan todos los viajes

        childEvent = new ChildEventListener() {
            //Publicaciones_Adapter adapt;
            Publicaciones_RV_adapter adapt;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                publica = dataSnapshot.getValue(Publicacion.class);
                publicaciones.add(publica);
                //  adapt=  new Publicaciones_Adapter(getContext(),publicaciones);
                adapt = new Publicaciones_RV_adapter(publicaciones,listenerRv);
                //listaPublicaciones.setAdapter(adapt);
                rv.setAdapter(adapt);
                Log.d(TAG, publica.getOrigen());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                publica = dataSnapshot.getValue(Publicacion.class);
                publicaciones.add(publica);
                //adapt=  new Publicaciones_Adapter(getContext(),publicaciones);
                adapt = new Publicaciones_RV_adapter(publicaciones,listenerRv);
                rv.setAdapter(adapt);
                //listaPublicaciones.setAdapter(adapt);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
               /* publica = dataSnapshot.getValue(Publicacion.class);
                int pos = publicaciones.indexOf(publica);
                //prueba para comprobar
                Log.d(TAG,"borrado: "+pos+"  "+ publica.getDestino()+"  "+publica.getusuario()+"  "+publica.getOrigen()+"  "+publica.getPlazas()+"  "+publica.getPrecio()+"  "+publica.getFecha()+"  "+publica.getHora());

                adapt=  new Publicaciones_Adapter(getContext(),publicaciones);
                listaPublicaciones.setAdapter(adapt);
                */
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.addChildEventListener(childEvent);
        listenerRv=initListener();

        Publicaciones_RV_adapter adapter = new Publicaciones_RV_adapter(publicaciones,listenerRv);
        rv.setAdapter(adapter);
        // Publicaciones_Adapter adapter = new Publicaciones_Adapter(getContext(),publicaciones);
        // listaPublicaciones.setAdapter(adapter);


    }

    private Publicaciones_RV_adapter.OnItemClickListener initListener() {

        Publicaciones_RV_adapter.OnItemClickListener l= new Publicaciones_RV_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(Publicacion item) {
                showDialog(item);
            }


    };
        return l;
    }

    private void showDialog(Publicacion p){

        customDialog =  new ReservarDialog(getContext(), R.style.Theme_Dialog_Translucent, p);
        //deshabilitamos el título por defecto
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        customDialog.setCancelable(false);
        //establecemos el contenido de nuestro dialog
        //LayoutInflater factory = LayoutInflater.from(getContext());
        //View dView = factory.inflate(R.layout.dialog_fragment_reservar, null);
        customDialog.setContentView(R.layout.dialog_fragment_reservar);
        customDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {

                if(i==KeyEvent.KEYCODE_BACK){
                    customDialog.dismiss();
                    return true;

                }
                return false;
            }
        });
        customDialog.show();
    }



}

package com.jjv.proyectointegradorv1.Fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjv.proyectointegradorv1.Adapters.Publicaciones_RV_adapter;
import com.jjv.proyectointegradorv1.Objects.Publicacion;
import com.jjv.proyectointegradorv1.R;
import com.jjv.proyectointegradorv1.UI.ContactoActivity;
import com.jjv.proyectointegradorv1.UI.MainActivity;
import com.jjv.proyectointegradorv1.UI.PerfilActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by javi0 on 11/01/2017.
 */

public class Buscar_viaje extends Fragment {

    private final String TAG = Buscar_viaje.class.getSimpleName();
    //private ListView listaPublicaciones;
    private ArrayList<Publicacion> publicaciones;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ChildEventListener childEvent;
    private FirebaseUser currentUser;
    private Publicacion publica;
    private Dialog customDialog;
    private RecyclerView rv;
    private Publicaciones_RV_adapter.OnItemClickListener listenerRv;
    private Publicaciones_RV_adapter adapt;
    private FloatingActionButton fab;

    // variables para el panel lateral
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buscar_viaje, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // listaPublicaciones = (ListView) view.findViewById(R.id.lista_publicaciones);
        //listaPublicaciones.setOnItemClickListener(this);
        // implementacion recycler
        publicaciones = new ArrayList<>();
        listenerRv = initListener();

        rv = (RecyclerView) view.findViewById(R.id.lista_publicaciones);

        /*
        Si estás seguro que el tamaño del RecyclerView no se cambiará,
        puedes añadirlo lo siguiente para mejorar el desempeño:
         */
        rv.setHasFixedSize(true);
        //para manejar el recycler view como una lista es necesario un manager
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String useruid = currentUser.getUid();
        if (currentUser != null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("trip");// hacemos referencia a la rama donde se almacenan todos los viajes

            childEvent = new ChildEventListener() {
                //Publicaciones_Adapter adapt;

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.i("snap",dataSnapshot.getKey());
                    publica = dataSnapshot.getValue(Publicacion.class);
                    if (publica.getPlazas() > 0 && publica.getIdConductor() != FirebaseAuth.getInstance().getCurrentUser().getUid()) {
                        publicaciones.add(publica);
                        //  adapt=  new Publicaciones_Adapter(getContext(),publicaciones);
                        adapt = new Publicaciones_RV_adapter(publicaciones, listenerRv);
                        //listaPublicaciones.setAdapter(adapt);
                        rv.setAdapter(adapt);
                    }



                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                   /* publica = dataSnapshot.getValue(Publicacion.class);
                    int pos = getPosition(publicaciones, publica);
                    if (pos != -1) {
                        publicaciones.remove(pos);

                        //TODO CARGAR DE NUEVO EL FRAGMENT o no
                        //publica = dataSnapshot.getValue(Publicacion.class);
                        if (publica.getPlazas() > 0 && publica.getIdConductor() != FirebaseAuth.getInstance().getCurrentUser().getUid()) {
                            publicaciones.add(pos, publica);
                            adapt = new Publicaciones_RV_adapter(publicaciones, listenerRv);
                            rv.setAdapter(adapt);
                        }
                    }
                    Log.d(TAG, publica.getOrigen());*/
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    /*publica = dataSnapshot.getValue(Publicacion.class);
                    int pos = getPosition(publicaciones, publica);
                    if (pos != -1) {
                        publicaciones.remove(pos);
                        adapt = new Publicaciones_RV_adapter(publicaciones, listenerRv);
                        rv.setAdapter(adapt);

                    }*/



                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            myRef.addChildEventListener(childEvent);


            adapt = new Publicaciones_RV_adapter(publicaciones, listenerRv);
            rv.setAdapter(adapt);
            // Publicaciones_Adapter adapter = new Publicaciones_Adapter(getContext(),publicaciones);
            // listaPublicaciones.setAdapter(adapter);

        }




        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //lanzar menu de busqueda
            }});

        Animation rotar;
        rotar= AnimationUtils.loadAnimation(getContext(),R.anim.rotar);
        fab.setAnimation(rotar);


        navView = (NavigationView) view.findViewById(R.id.nvViewBusqueda);





        // asigna el listener al navigation view
        // configura acciones segun opcion seleccionada
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navView.setCheckedItem(item.getItemId());
                // acciones segun item seleccionado en el panel lateral
                switch(item.getItemId()){
                    case R.id.drawer_perfil:

                        break;
                    case R.id.drawer_contacto:

                        break;
                    case R.id.drawer_salir:

                        break;

                }

                return false;
            }
        });
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layoutBusqueda);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),
                mDrawerLayout,
                R.string.lat_abierto,
                R.string.lat_cerrado){

            public void OnDrawerClosed(View v){
                super.onDrawerClosed(v);
                customDialog.invalidateOptionsMenu();
            }

            public void OnDrawerOpened(View v){
                super.onDrawerOpened(v);
                customDialog.invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    mDrawerLayout.openDrawer(GravityCompat.START);

            }
        });




    }

    private Publicaciones_RV_adapter.OnItemClickListener initListener() {

        Publicaciones_RV_adapter.OnItemClickListener l = new Publicaciones_RV_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(Publicacion item) {
                showDialog(item);
            }


        };
        return l;
    }

    ////

    ///
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

    private void showDialog(Publicacion p) {

        customDialog = new ReservarDialog(getContext(), R.style.Theme_Dialog_Translucent, p);
        //deshabilitamos el título por defecto
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        customDialog.setCancelable(true);
        //establecemos el contenido de nuestro dialog
        //LayoutInflater factory = LayoutInflater.from(getContext());
        //View dView = factory.inflate(R.layout.dialog_fragment_reservar, null);
        customDialog.setContentView(R.layout.dialog_fragment_reservar);
        customDialog.setCanceledOnTouchOutside(true);

        customDialog.show();
    }


}

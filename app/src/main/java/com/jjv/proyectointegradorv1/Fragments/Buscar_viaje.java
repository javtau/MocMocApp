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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.ValueEventListener;
import com.jjv.proyectointegradorv1.Adapters.Publicaciones_RV_adapter;
import com.jjv.proyectointegradorv1.DB.GestionDB;
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
    private GestionDB gestionDB;
    private EditText etDestinoFiltro,etOrigenFiltro,etPrecioFiltro,etUsuarioFiltro;
    private Button btnFiltrar,btnCancelarFiltro;

    // variables para el panel lateral
    public DrawerLayout mDrawerLayout;
    private NavigationView navView;
    private Publicacion publicacionFiltro;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buscar_viaje, container, false);
    }



    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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

        if (currentUser != null) {
            cargarCardview();
        }
        adapt = new Publicaciones_RV_adapter(publicaciones, listenerRv);
        rv.setAdapter(adapt);


        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        Animation rotar;
        rotar= AnimationUtils.loadAnimation(getContext(),R.anim.rotar);
        fab.setAnimation(rotar);


        navView = (NavigationView) view.findViewById(R.id.nvViewBusqueda);


        // asigna el listener al navigation view
        // configura acciones segun opcion seleccionada
        etDestinoFiltro = (EditText) view.findViewById(R.id.txt_destino_buscar);
        etOrigenFiltro = (EditText) view.findViewById(R.id.txt_origen_buscar);
        etPrecioFiltro = (EditText) view.findViewById(R.id.txt_precioMaximo_buscar);
        etUsuarioFiltro = (EditText) view.findViewById(R.id.txt_conductor_buscar);
        btnFiltrar = (Button) view.findViewById(R.id.btnFiltrar);
        btnCancelarFiltro = (Button) view.findViewById(R.id.btn_cancelar_filtro);
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layoutBusqueda);

        btnCancelarFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarFiltro();
            }
        });

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarFiltro();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
    }
    private void cancelarFiltro(){
        publicacionFiltro = null;
        etDestinoFiltro.setText("");
        etOrigenFiltro.setText("");
        etPrecioFiltro.setText("");
        etUsuarioFiltro.setText("");
        mDrawerLayout.closeDrawers();
        cargarCardview();
    }
    private void buscarFiltro(){
        publicacionFiltro = new Publicacion();
        publicacionFiltro.setDestino(etDestinoFiltro.getText().toString().toLowerCase());
        publicacionFiltro.setOrigen(etOrigenFiltro.getText().toString().toLowerCase());
        publicacionFiltro.setUsuario(etUsuarioFiltro.getText().toString().toLowerCase());

        try{
            if(etPrecioFiltro.getText().toString().equals("")){
                publicacionFiltro.setPrecio("0");
            }else{
                Integer.parseInt(etPrecioFiltro.getText().toString());
                publicacionFiltro.setPrecio(etPrecioFiltro.getText().toString());
            }
            cargarCardview();
            mDrawerLayout.closeDrawers();
        }catch (NumberFormatException e){
            Snackbar.make(getView(), getString(R.string.falloNumero), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void cargarCardview() {
        publicaciones = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("trip");
        ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    publica = appleSnapshot.getValue(Publicacion.class);
                    if (publica.getPlazas() > 0 && !publica.getIdConductor().equals(currentUser.getUid())) {
                        filtrarPublicaciones();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        ref.addListenerForSingleValueEvent(event);
        adapt = new Publicaciones_RV_adapter(publicaciones, listenerRv);
        rv.setAdapter(adapt);
    }

    private void filtrarPublicaciones() {
        if(publicacionFiltro==null){
            publicaciones.add(publica);
            adapt = new Publicaciones_RV_adapter(publicaciones, listenerRv);
            rv.setAdapter(adapt);
        }else{
            if(publicacionFiltro.getPrecio().equals("0")){
                if(publica.getDestino().toLowerCase().contains(publicacionFiltro.getDestino())&&!publicacionFiltro.getDestino().equals("")){
                    agregarPublicacionSetAdapt();
                }else if(publica.getOrigen().toLowerCase().contains(publicacionFiltro.getOrigen())&&!publicacionFiltro.getOrigen().equals("")) {
                    agregarPublicacionSetAdapt();
                }else if(publica.getUsuario().toLowerCase().contains(publicacionFiltro.getUsuario())&&!publicacionFiltro.getUsuario().equals("")) {
                    agregarPublicacionSetAdapt();
                }else if(publicacionFiltro.getDestino().equals("")&&publicacionFiltro.getOrigen().equals("")&&publicacionFiltro.getUsuario().equals("")){
                    agregarPublicacionSetAdapt();
                }
            }else if(Integer.parseInt(publica.getPrecio())<=Integer.parseInt(publicacionFiltro.getPrecio())){
                if(publica.getDestino().toLowerCase().contains(publicacionFiltro.getDestino())&&!publicacionFiltro.getDestino().equals("")){
                    agregarPublicacionSetAdapt();;
                }else if(publica.getOrigen().toLowerCase().contains(publicacionFiltro.getOrigen())&&!publicacionFiltro.getOrigen().equals("")) {
                    agregarPublicacionSetAdapt();
                }else if(publica.getUsuario().toLowerCase().contains(publicacionFiltro.getUsuario())&&!publicacionFiltro.getUsuario().equals("")) {
                    agregarPublicacionSetAdapt();
                }else if(publicacionFiltro.getDestino().equals("")&&publicacionFiltro.getOrigen().equals("")&&publicacionFiltro.getUsuario().equals("")){
                    agregarPublicacionSetAdapt();
                }
            }
        }
    }

    public void agregarPublicacionSetAdapt(){
        publicaciones.add(publica);
        adapt = new Publicaciones_RV_adapter(publicaciones, listenerRv);
        rv.setAdapter(adapt);
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

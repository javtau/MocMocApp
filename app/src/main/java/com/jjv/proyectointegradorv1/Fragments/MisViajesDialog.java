package com.jjv.proyectointegradorv1.Fragments;

/**
 * Created by Victor on 23/02/2017.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjv.proyectointegradorv1.Objects.Publicacion;
import com.jjv.proyectointegradorv1.R;

import java.util.ArrayList;
import java.util.Iterator;

// TODO: CARGAR IMAGEN DE USUARIO --- IMPLEMENTAR BOTON RESERVAR -- IMPLEMENTAR ESTRELLAS VALORACION

public class MisViajesDialog extends Dialog {

    Publicacion pub;
    TextView etConductorNombre, etViajeOrigen, etViajeDestino, etViajePlazas, etViajePrecio;
    TextView etViajeFecha, etViajeHora;
    RatingBar rateConductor;
    String sRateConductor = "";
    Button btnCancelaViaje;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbref;
    Context context;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ChildEventListener childEvent;
    private Publicacion publicacion;
    DataSnapshot userkeyShot;
    private ArrayList<DataSnapshot>publicacionesToUpdate = new ArrayList<>();


    public MisViajesDialog(Context context, int theme, Publicacion pub) {
        super(context);
        this.pub = pub;
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fragment_misviajes);

        etConductorNombre = (TextView) findViewById(R.id.tv_cond_nombre_misviajes);
        etViajeOrigen = (TextView) findViewById(R.id.tv_origen_res_misviajes);
        etViajeDestino = (TextView) findViewById(R.id.tv_destino_res_misviajes);
        etViajePlazas = (TextView) findViewById(R.id.tv_plazas_res_misviajes);
        etViajePrecio = (TextView) findViewById(R.id.tv_precio_res_misviajes);
        etViajeFecha = (TextView) findViewById(R.id.tv_viaje_fecha_res_misviajes);
        etViajeHora = (TextView) findViewById(R.id.tv_viaje_hora_res_misviajes);
        rateConductor = (RatingBar) findViewById(R.id.rate_conductor_misviajes);


        etConductorNombre.setText(pub.getUsuario());
        etViajeOrigen.setText(pub.getOrigen());
        etViajeDestino.setText(pub.getDestino());
        etViajePlazas.setText(String.valueOf(pub.getPlazas()));
        etViajePrecio.setText(pub.getPrecio() + "€");
        etViajeHora.setText(pub.getHora());
        etViajeFecha.setText(pub.getFecha());
        final String idConductor = pub.getIdConductor();
        final String keyViaje = pub.getKeyViaje();
        final int reservas = 1;
        btnCancelaViaje = (Button) findViewById(R.id.btn_cancelar_viaje);
        btnCancelaViaje.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder =
                            new AlertDialog.Builder(context)
                                    .setTitle(R.string.pregunta_cancelar_alert_dialog)
                                    .setMessage(R.string.cancelar_reserva_alert_dialog_msg)
                                    .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            eliminarPublicacion();
                                        }
                                    })
                                    .setNegativeButton(R.string.Cancelar, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                   alertDialogBuilder.show();

                }
            });
        }

    private void eliminarPublicacion() {
        Log.i("llega:","metodo eliminar");
        boolean esPubDeConductor;
        if(pub.getIdConductor().equals(user.getUid())){
            esPubDeConductor=true;
        }else{
            esPubDeConductor=false;
        }
        eliminar(esPubDeConductor);
    }

    private void eliminar(final boolean esPubDeConductor) {
        dbref = database.getReference("user-trips");
        // dbref.orderByChild("keyViaje").equalTo(pub.getKeyViaje()).addValueEventListener(new ValueEventListener() {
        Log.i("pub key eliminar:",pub.getKeyViaje());

        childEvent = new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot userkeyShotRecuperada, String s) {
               /* publicacion = dataSnapshot.getValue(Publicacion.class);
                Log.i("publicacion recuperada:",publicacion.getIdConductor());
                Log.i("usuario:",user.getUid());
                Log.i("publicacion a eliminar:",pub.getIdConductor());
                Log.i("KEY VIAJE:","Key del viaje a eliminar: "+dataSnapshot.getKey());
                Log.i("DSN:::::::",dataSnapshot.getChildrenCount()+"");
                Log.i("keys usuarios::::",dataSnapshot.getKey());
                */
                userkeyShot = userkeyShotRecuperada;
                Log.i("userkeyshot recuperada:",userkeyShot.getKey());
                DatabaseReference childref = dbref.child(userkeyShotRecuperada.getKey());
                Query q = childref.orderByChild("keyViaje").equalTo(pub.getKeyViaje());
                //devolveria los viajes de cada usuario que coincida con la publicacion a eliminar

               q.addChildEventListener(new ChildEventListener() {
                   @Override
                   public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                       Log.i("KEY OCA/OCA:",dataSnapshot.getKey());
                       publicacion = dataSnapshot.getValue(Publicacion.class);
                       Log.i("pub OCA/OCA r. uid:",publicacion.getIdConductor());
                       Log.i("publ OCA/OCA r. key:",publicacion.getKeyViaje());

                       if(esPubDeConductor){
                           //si usuario es el conductor borrara todas
                           dataSnapshot.getRef().removeValue();
                       }else{
                           //si no es el conductor comprobara las publicaciones recibidas
                           //solo borrara la que le pertenece
                           if(userkeyShot.getKey().equals(user.getUid())){
                               Log.i("entra en if userkshot:",userkeyShot.getKey());
                               dataSnapshot.getRef().removeValue();
                           }else{
                               //UPDATE en cada publicacion
                               publicacion = dataSnapshot.getValue(Publicacion.class);
                               publicacion.addPlazas(1);
                               dataSnapshot.getRef().setValue(publicacion);
                           }
                       }

                   }

                   @Override
                   public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
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
        };
        if (esPubDeConductor){
            DatabaseReference ref = database.getReference("Books");
            Query query1 = ref.orderByChild("keyViaje").equalTo(pub.getKeyViaje());
            DatabaseReference ref2 = database.getReference("trip");
            Query query2 = ref2.orderByChild("keyViaje").equalTo(pub.getKeyViaje());
            ValueEventListener event = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            query1.addListenerForSingleValueEvent(event);
            query2.addListenerForSingleValueEvent(event);
        }
        dbref.addChildEventListener(childEvent);

    }
}

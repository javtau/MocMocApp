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
import com.jjv.proyectointegradorv1.Objects.Publicacion;
import com.jjv.proyectointegradorv1.R;

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
                                           //TODO borrar viaje
                                            eliminarPublicacion();

                                        }
                                    })
                                    .setNegativeButton(R.string.Cancelar, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                   alertDialogBuilder.show();


                    /*
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    sRateConductor = String.valueOf(rateConductor.getRating());
                    Toast.makeText(getContext(), sRateConductor, Toast.LENGTH_SHORT).show();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    //Publicacion viaje = new Publicacion(origen,destino,fecha,hora,plazas,precio);
                    String key = mDatabase.child("posts").push().getKey();
                    String userId = user.getUid();
                    pub.setPlazas(pub.getPlazas() - reservas);
                    Map<String, Object> viaje = pub.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/trip/" + keyViaje, viaje);
                    childUpdates.put("/user-trips/" + idConductor + "/" + keyViaje, viaje);
                    pub.setPlazas(reservas);
                    viaje = pub.toMap();
                    childUpdates.put("/Books/" + key, viaje);
                    childUpdates.put("/user-trips/" + userId + "/" + key, viaje);

                    mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dismiss();
                        }
                    });*/
                }
            });
        }

    private void eliminarPublicacion() {
        Log.i("llega:","metodo eliminar");
        dbref = database.getReference("user-trips/"+user.getUid());
        childEvent = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                publicacion = dataSnapshot.getValue(Publicacion.class);
                Log.i("publicacion recuperada:",publicacion.getIdConductor());
                Log.i("usuario:",user.getUid());
                Log.i("publicacion a eliminar:",pub.getIdConductor());

                if(publicacion.getIdConductor().equals(user.getUid())&&user.getUid().equals(pub.getIdConductor())){
                    //si soy el conductor de la publicacion
                    Log.i("KEY VIAJE:","Key del viaje a eliminar: "+dataSnapshot.getKey());
                    database.getReference("user-trips/"+user.getUid()+"/"+dataSnapshot.getKey()).removeValue();

                }else{
                    //TODO
                    //si no soy el conductor de la publicacion
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                publicacion = dataSnapshot.getValue(Publicacion.class);
                Log.i("publicacion recuperada:",publicacion.getIdConductor());
                Log.i("usuario:",user.getUid());
                Log.i("publicacion a eliminar:",pub.getIdConductor());
                if(publicacion.getIdConductor().equals(user.getUid())&&user.getUid().equals(pub.getIdConductor())){
                    //si soy el conductor de la publicacion

                    Log.i("KEY VIAJE:","Key del viaje a eliminar: "+dataSnapshot.getKey());
                    database.getReference("user-trips/"+user.getUid()+"/"+dataSnapshot.getKey()).removeValue();

                }else{
                    //si no soy el conductor de la publicacion
                    //TODO
                }

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

        dbref.addChildEventListener(childEvent);




    }
}

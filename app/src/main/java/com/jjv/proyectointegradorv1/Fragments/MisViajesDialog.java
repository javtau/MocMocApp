package com.jjv.proyectointegradorv1.Fragments;

/**
 * Created by Victor on 23/02/2017.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.jjv.proyectointegradorv1.Utils.GestionDB;
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

    Context context;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    GestionDB gestionDB ;


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
        gestionDB = new GestionDB(database,user,pub);
        }

    private void eliminarPublicacion() {
        if(pub.getIdConductor().equals(user.getUid())){
            //si es conductor debe eliminar la publicacion en trip y en user-trips
            gestionDB.deletePubsEnTrip_UserTrip();

        }else{
            //sino, tiene que eliminar la reserva SOLO del pasajero en user-trip
            //tambien hay que actualizar en user-trip del resto de reservas de esa publicacion
            //añadiendo una plaza libre

            gestionDB.deletePubDeUserEnUserTrip();
            gestionDB.updatePubsEnTrip_UserTrip();
        }

        //gestionDB.eliminar(esPubDeConductor);
        this.dismiss();
    }
}

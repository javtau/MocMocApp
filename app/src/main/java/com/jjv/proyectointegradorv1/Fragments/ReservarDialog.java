package com.jjv.proyectointegradorv1.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjv.proyectointegradorv1.Objects.Publicacion;
import com.jjv.proyectointegradorv1.R;

// TODO: CARGAR IMAGEN DE USUARIO --- IMPLEMENTAR BOTON RESERVAR -- IMPLEMENTAR ESTRELLAS VALORACION

public class ReservarDialog extends Dialog {

    Publicacion pub;
    TextView etConductorNombre, etViajeOrigen, etViajeDestino, etViajePlazas, etViajePrecio;
    TextView etViajeFecha, etViajeHora;
    Button btnReservar;

    public ReservarDialog(Context context, int theme, Publicacion pub) {
        super(context);
        this.pub = pub;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fragment_reservar);

        etConductorNombre = (TextView) findViewById(R.id.tv_cond_nombre);
        etViajeOrigen = (TextView)findViewById(R.id.tv_origen_res);
        etViajeDestino = (TextView)findViewById(R.id.tv_destino_res);
        etViajePlazas = (TextView)findViewById(R.id.tv_plazas_res);
        etViajePrecio = (TextView)findViewById(R.id.tv_precio_res);
        etViajeFecha = (TextView)findViewById(R.id.tv_viaje_fecha_res);
        etViajeHora = (TextView)findViewById(R.id.tv_viaje_hora_res);

        btnReservar = (Button)findViewById(R.id.btn_reservar);
        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click ok", Toast.LENGTH_SHORT).show();
            }
        });

        etConductorNombre.setText(pub.getusuario());
        etViajeOrigen.setText(pub.getOrigen());
        etViajeDestino.setText(pub.getDestino());
        etViajePlazas.setText(String.valueOf(pub.getPlazas()));
        etViajePrecio.setText(pub.getPrecio() + "€");
        etViajeHora.setText(pub.getHora());
        etViajeFecha.setText(pub.getFecha());
    }

}

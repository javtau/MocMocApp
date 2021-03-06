package com.jjv.proyectointegradorv1.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jjv.proyectointegradorv1.DB.GestionDB;
import com.jjv.proyectointegradorv1.Objects.Publicacion;
import com.jjv.proyectointegradorv1.R;
import com.jjv.proyectointegradorv1.UI.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;



public class ReservarDialog extends Dialog {

    Publicacion pub;
    TextView etConductorNombre, etViajeOrigen, etViajeDestino, etViajePlazas, etViajePrecio;
    TextView etViajeFecha, etViajeHora;
    ImageView iv_userimage ;
    static Button btnReservar;
    private DatabaseReference mDatabase;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private GestionDB gestiondb = new GestionDB(FirebaseDatabase.getInstance(),user);

    private static StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://logginpi.appspot.com").child("Userimage");;
    public static final Uri DEFAULTIMAGEURI = Uri.parse("https://firebasestorage.googleapis.com/v0/b/logginpi.appspot.com/o/Userimage%2Fdefault.png?alt=media&token=3791a8b6-c7d0-45fe-b04b-cd0b90ffb6fd");



    public ReservarDialog(){
        super(null, 0);
    }
    public ReservarDialog(Context context, int theme, Publicacion pub) {
        super(context);
        this.pub = pub;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fragment_reservar);
        showUserimage(pub.getIdConductor());
        etConductorNombre = (TextView) findViewById(R.id.tv_cond_nombre);
        etViajeOrigen = (TextView) findViewById(R.id.tv_origen_res);
        etViajeDestino = (TextView) findViewById(R.id.tv_destino_res);
        etViajePlazas = (TextView) findViewById(R.id.tv_plazas_res);
        etViajePrecio = (TextView) findViewById(R.id.tv_precio_res);
        etViajeFecha = (TextView) findViewById(R.id.tv_viaje_fecha_res);
        etViajeHora = (TextView) findViewById(R.id.tv_viaje_hora_res);
        iv_userimage = (ImageView) findViewById(R.id.iv_usuario);

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
        btnReservar = (Button) findViewById(R.id.btn_reservar);
        gestiondb.comprobarReserva(pub,this);
            btnReservar.setText(R.string.reservar_btn_reservar);
            btnReservar.setBackgroundResource(R.color.colorAccent);
            btnReservar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabase = FirebaseDatabase.getInstance().getReference();
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
                    childUpdates.put("/user-trips/" + userId + "/" + key, viaje);

                    mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dismiss();
                        }
                    });
                }
            });
        }

    public static void gestionarBoton(final ReservarDialog a) {
        btnReservar.setText(R.string.yaReservado);
        btnReservar.setBackgroundResource(R.color.colorGris);
        btnReservar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MainActivity.selectPage(2);
                a.cancel();
            }
        });
    }

   public void showUserimage(String uid){
        storageRef.child(uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Picasso.with(getContext()).load(uri).into(iv_userimage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Picasso.with(getContext()).load(DEFAULTIMAGEURI).into(iv_userimage);
            }
        });
    }
}

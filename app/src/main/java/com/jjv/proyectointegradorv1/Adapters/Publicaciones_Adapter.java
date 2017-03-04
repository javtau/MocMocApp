package com.jjv.proyectointegradorv1.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jjv.proyectointegradorv1.Objects.Publicacion;
import com.jjv.proyectointegradorv1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Publicaciones_Adapter extends BaseAdapter {

    Context contexto;
    ArrayList<Publicacion> publicaciones;
    private static StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://logginpi.appspot.com").child("Userimage");;
    public static final Uri DEFAULTIMAGEURI = Uri.parse("https://firebasestorage.googleapis.com/v0/b/logginpi.appspot.com/o/Userimage%2Fdefault.png?alt=media&token=3791a8b6-c7d0-45fe-b04b-cd0b90ffb6fd");


    public Publicaciones_Adapter(Context contexto, ArrayList<Publicacion> publicaciones) {
        this.contexto = contexto;
        this.publicaciones = publicaciones;
    }

    @Override
    public int getCount() {
        return publicaciones.size();
    }

    @Override
    public Object getItem(int i) {
        return publicaciones.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(contexto).inflate(R.layout.item_viaje,null);
            holder = new ViewHolder();
            holder.userImage = (ImageView) convertView.findViewById(R.id.userImage);
            holder.nombreUsuario= (TextView) convertView.findViewById(R.id.txt_userName);
            holder.origenViaje= (TextView) convertView.findViewById(R.id.txt_origen_usr);
            holder.destinoViaje= (TextView) convertView.findViewById(R.id.txt_destino_viaje);
            holder.precioViaje= (TextView) convertView.findViewById(R.id.txt_precio_viaje);
            holder.horaViaje= (TextView) convertView.findViewById(R.id.txt_hora_viaje);
            //holder.plazas= (TextView) convertView.findViewById(R.id.txt_plazas_viaje);
            holder.fechaviaje= (TextView) convertView.findViewById(R.id.txt_fecha_viaje);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nombreUsuario.setText(publicaciones.get(position).getUsuario());
        holder.origenViaje.setText(publicaciones.get(position).getOrigen());
        holder.destinoViaje.setText(publicaciones.get(position).getDestino());
        holder.precioViaje.setText(publicaciones.get(position).getPrecio()+"€");
        holder.horaViaje.setText(publicaciones.get(position).getHora());
        //holder.plazas.setText(publicaciones.get(position).getPlazas()+"");
        holder.fechaviaje.setText(publicaciones.get(position).getFecha());

        storageRef.child(publicaciones.get(position).getIdConductor()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Log.e("caradapter    ", "resultado recivido del estarage " + uri.toString());
                Picasso.with(contexto).load(uri).into(holder.userImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Picasso.with(contexto).load(DEFAULTIMAGEURI).into(holder.userImage);
            }
        });

        return convertView;
    }
    private class ViewHolder{
        ImageView userImage;
        TextView nombreUsuario,origenViaje,destinoViaje,precioViaje,horaViaje,fechaviaje;

    }
}

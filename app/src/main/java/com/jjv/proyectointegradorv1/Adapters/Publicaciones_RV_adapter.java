package com.jjv.proyectointegradorv1.Adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

/**
 * Created by Victor on 18/02/2017.
 */

public class Publicaciones_RV_adapter extends RecyclerView.Adapter<Publicaciones_RV_adapter.PublicacionesViewHolder>{

    private static StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://logginpi.appspot.com").child("Userimage");;
    public static final Uri DEFAULTIMAGEURI = Uri.parse("https://firebasestorage.googleapis.com/v0/b/logginpi.appspot.com/o/Userimage%2Fdefault.png?alt=media&token=3791a8b6-c7d0-45fe-b04b-cd0b90ffb6fd");

    ArrayList<Publicacion> publicaciones;
    OnItemClickListener listener;

    public Publicaciones_RV_adapter(ArrayList<Publicacion> publicaciones, OnItemClickListener listener) {
        this.publicaciones = publicaciones;
        this.listener = listener;
    }

    public void setPublicaciones(ArrayList<Publicacion> publicaciones) {
        this.publicaciones = publicaciones;
    }

    @Override
    public PublicacionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_buscar_viaje,parent,false);
        PublicacionesViewHolder pvh = new PublicacionesViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PublicacionesViewHolder holder, int position) {
        holder.bind(publicaciones.get(position), listener);
        holder.nombreUsuario.setText(publicaciones.get(position).getUsuario());
        holder.origenViaje.setText(publicaciones.get(position).getOrigen());
        holder.destinoViaje.setText(publicaciones.get(position).getDestino());
        //holder.precioViaje.setText(publicaciones.get(position).getPrecio()+"€");
        holder.horaViaje.setText(publicaciones.get(position).getHora());
        holder.fechaViaje.setText(publicaciones.get(position).getFecha());
        holder.plazas.setText(publicaciones.get(position).getPlazas()+"");


        /* TODO : RECUPERAR LAS IMAGENES
        holder.starsImage
        holder.userImage
         */


    }


    @Override
    public int getItemCount() {
        return publicaciones.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PublicacionesViewHolder extends RecyclerView.ViewHolder{
        ImageView userImage,starsImage;
        TextView nombreUsuario,origenViaje,destinoViaje,fechaViaje,horaViaje,plazas;

        public PublicacionesViewHolder(View convertView) {
            super(convertView);

            userImage = (ImageView) convertView.findViewById(R.id.userImage);
            //starsImage = (ImageView) convertView.findViewById(R.id.starsImage);
            nombreUsuario= (TextView) convertView.findViewById(R.id.txt_userName);
            origenViaje= (TextView) convertView.findViewById(R.id.txt_origen_usr);
            destinoViaje= (TextView) convertView.findViewById(R.id.txt_destino_viaje);
            fechaViaje= (TextView) convertView.findViewById(R.id.txt_fecha_viaje);
            horaViaje= (TextView) convertView.findViewById(R.id.txt_hora_viaje);

            plazas= (TextView) convertView.findViewById(R.id.txt_plazas_viaje);
        }
        public void bind(final Publicacion item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }


    }
    public interface OnItemClickListener {
        void onItemClick(Publicacion item);
    }



}

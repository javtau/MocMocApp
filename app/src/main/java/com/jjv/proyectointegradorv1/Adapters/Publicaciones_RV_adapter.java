package com.jjv.proyectointegradorv1.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjv.proyectointegradorv1.Objects.Publicacion;
import com.jjv.proyectointegradorv1.R;

import java.util.ArrayList;

/**
 * Created by Victor on 18/02/2017.
 */

public class Publicaciones_RV_adapter extends RecyclerView.Adapter<Publicaciones_RV_adapter.PublicacionesViewHolder>{


    ArrayList<Publicacion> publicaciones;
    OnItemClickListener listener;

    public Publicaciones_RV_adapter(ArrayList<Publicacion> publicaciones, OnItemClickListener listener) {
        this.publicaciones = publicaciones;
        this.listener = listener;
    }

    @Override
    public PublicacionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_buscar_viaje,parent,false);
        PublicacionesViewHolder pvh = new PublicacionesViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PublicacionesViewHolder holder, int position) {
        holder.bind(publicaciones.get(position), listener);
        holder.nombreUsuario.setText(publicaciones.get(position).getUsuario());
        holder.origenViaje.setText(publicaciones.get(position).getOrigen());
        holder.destinoViaje.setText(publicaciones.get(position).getDestino());
        //holder.precioViaje.setText(publicaciones.get(position).getPrecio()+"€");
        holder.horaViaje.setText(publicaciones.get(position).getHora());
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
        //ImageView userImage,starsImage;
        TextView nombreUsuario,origenViaje,destinoViaje,precioViaje,horaViaje,plazas;

        public PublicacionesViewHolder(View convertView) {
            super(convertView);

            //userImage = (ImageView) convertView.findViewById(R.id.userImage);
            //starsImage = (ImageView) convertView.findViewById(R.id.starsImage);
            nombreUsuario= (TextView) convertView.findViewById(R.id.txt_userName);
            origenViaje= (TextView) convertView.findViewById(R.id.txt_origen_usr);
            destinoViaje= (TextView) convertView.findViewById(R.id.txt_destino_viaje);
            //precioViaje= (TextView) convertView.findViewById(R.id.txt_precio_viaje);
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

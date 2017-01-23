package com.jjv.proyectointegradorv1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjv.proyectointegradorv1.Objects.Publicacion;
import com.jjv.proyectointegradorv1.R;

public class Publicaciones_Adapter extends BaseAdapter {

    Context contexto;
    Publicacion[]publicaciones;


    public Publicaciones_Adapter(Context contexto, Publicacion[] publicaciones) {
        this.contexto = contexto;
        this.publicaciones = publicaciones;
    }

    @Override
    public int getCount() {
        return publicaciones.length;
    }

    @Override
    public Object getItem(int i) {
        return publicaciones[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(contexto).inflate(R.layout.item_buscar_viaje,null);
            holder = new ViewHolder();
            holder.userImage = (ImageView) convertView.findViewById(R.id.userImage);
            holder.starsImage = (ImageView) convertView.findViewById(R.id.starsImage);
            holder.nombreUsuario= (TextView) convertView.findViewById(R.id.txt_userName);
            holder.origenViaje= (TextView) convertView.findViewById(R.id.txt_origen_usr);
            holder.destinoViaje= (TextView) convertView.findViewById(R.id.txt_destino_viaje);
            holder.precioViaje= (TextView) convertView.findViewById(R.id.txt_precio_viaje);
            holder.horaViaje= (TextView) convertView.findViewById(R.id.txt_hora_viaje);
            holder.plazas= (TextView) convertView.findViewById(R.id.txt_plazas_viaje);


            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nombreUsuario.setText(publicaciones[position].getUser());
        holder.origenViaje.setText(publicaciones[position].getOrigen());
        holder.destinoViaje.setText(publicaciones[position].getDestino());
        holder.precioViaje.setText(publicaciones[position].getPrecio());
        holder.horaViaje.setText(publicaciones[position].getHora());
        holder.plazas.setText(publicaciones[position].getPlazas()+"");

        /* TODO : RECUPERAR LAS IMAGENES
        holder.starsImage
        holder.userImage
         */

        return convertView;
    }
    private class ViewHolder{
        ImageView userImage,starsImage;
        TextView nombreUsuario,origenViaje,destinoViaje,precioViaje,horaViaje,plazas;
    }
}

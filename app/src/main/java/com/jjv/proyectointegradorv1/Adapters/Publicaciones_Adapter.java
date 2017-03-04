package com.jjv.proyectointegradorv1.Adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjv.proyectointegradorv1.Objects.Publicacion;
import com.jjv.proyectointegradorv1.R;

import java.util.ArrayList;

public class Publicaciones_Adapter extends BaseAdapter {

    Context contexto;
    ArrayList<Publicacion> publicaciones;
    int layout ;


    public Publicaciones_Adapter(Context contexto, ArrayList<Publicacion> publicaciones,int layout) {
        this.contexto = contexto;
        this.publicaciones = publicaciones;
        this.layout=layout;
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
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(contexto).inflate(layout,null);
            holder = new ViewHolder();
            if(layout==R.layout.item_viaje){
                holder.userImage = (ImageView) convertView.findViewById(R.id.userImage);
                holder.nombreUsuario= (TextView) convertView.findViewById(R.id.txt_userName);
            }
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

        if(layout==R.layout.item_viaje){
           /* TODO : RECUPERAR LAS IMAGENES

        holder.userImage
         */
            holder.nombreUsuario.setText(publicaciones.get(position).getUsuario());
        }

        holder.origenViaje.setText(publicaciones.get(position).getOrigen());
        holder.destinoViaje.setText(publicaciones.get(position).getDestino());
        holder.precioViaje.setText(publicaciones.get(position).getPrecio()+"€");
        holder.horaViaje.setText(publicaciones.get(position).getHora());
        //holder.plazas.setText(publicaciones.get(position).getPlazas()+"");
        holder.fechaviaje.setText(publicaciones.get(position).getFecha());



        return convertView;
    }
    private class ViewHolder{
        ImageView userImage;
        TextView nombreUsuario,origenViaje,destinoViaje,precioViaje,horaViaje,fechaviaje;

    }
}

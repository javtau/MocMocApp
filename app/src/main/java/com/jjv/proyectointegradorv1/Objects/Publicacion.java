package com.jjv.proyectointegradorv1.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.author;

/**
 * Created by javi0 on 17/01/2017.
 */

public class Publicacion implements Parcelable{
    private String usuario;
    private String origen ;
    private String destino;
    private String fecha;
    private String hora ;
    private int plazas ;
    private String precio ;

    public Publicacion() {
    }

    public Publicacion(String usuario,String origen, String destino, String fecha, String hora, int plazas, String precio) {
        this.usuario = usuario;
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.hora = hora;
        this.plazas = plazas;
        this.precio = precio;
    }

    public static final Creator<Publicacion> CREATOR = new Creator<Publicacion>() {
        @Override
        public Publicacion createFromParcel(Parcel in) {
            return new Publicacion(in);
        }

        @Override
        public Publicacion[] newArray(int size) {
            return new Publicacion[size];
        }
    };

    public Map<String, Object> toMap() { //creamos una lista con cada uno de los atibustos del objeto
        HashMap<String, Object> result = new HashMap<>();
        result.put("usuario", usuario);
        result.put("origen", origen);
        result.put("destino", destino);
        result.put("fecha", fecha);
        result.put("hora", hora);
        result.put("plazas", plazas);
        result.put("precio", precio);

        return result;
    }

    public String getusuario() {
        return usuario;
    }

    public void setusuario(String usuario) {
        this.usuario = usuario;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getPlazas() {
        return plazas;
    }

    public void setPlazas(int plazas) {
        this.plazas = plazas;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(usuario);
        dest.writeString(origen);
        dest.writeString(destino);
        dest.writeString(fecha);
        dest.writeString(hora);
        dest.writeInt(plazas);
        dest.writeString(precio);
    }
    private Publicacion(Parcel in){
        usuario = in.readString();
        origen=in.readString();
        destino=in.readString();
        fecha=in.readString();
        hora=in.readString();
        plazas=in.readInt();
        precio=in.readString();


    }
}

package com.jjv.proyectointegradorv1.Objects;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by javi on 19/02/17.
 */

public class MensajeChat {
    private String msgTexto;
    private String msgEmisor;
    private String msgHora;

    public MensajeChat() {}
    public MensajeChat(String msg, String e) {
        this.msgTexto = msg;
        this.msgEmisor = e;
        // consigue la hora actual
        this.msgHora = formatearHora(new Date().getTime());
    }

    public String getMsgTexto() {
        return msgTexto;
    }

    public void setMsgTexto(String msgTexto) {
        this.msgTexto = msgTexto;
    }

    public String getMsgEmisor() {
        return msgEmisor;
    }

    public void setMsgEmisor(String msgEmisor) {
        this.msgEmisor = msgEmisor;
    }

    public String getMsgHora() {
        return msgHora;
    }

    public void setMsgHora(String msgHora) {
        this.msgHora = msgHora;
    }

    public String formatearHora(long hora){
        String horaFormateada;
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(hora);

        horaFormateada = String.format("%02d:%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
        return horaFormateada;
    }
}


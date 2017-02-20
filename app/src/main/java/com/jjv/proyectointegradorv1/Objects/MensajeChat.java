package com.jjv.proyectointegradorv1.Objects;

import java.util.Date;

/**
 * Created by javi on 19/02/17.
 */

public class MensajeChat {
    private String msgTexto;
    private String msgAutor;
    private long msgHora;

    public MensajeChat(){}
    public MensajeChat(String t, String a){
        this.msgTexto = t;
        this.msgAutor = a;

        // consigue la hora actual
        this.msgHora = new Date().getTime();
    }

    public String getMsgTexto() {
        return msgTexto;
    }

    public void setMsgTexto(String msgTexto) {
        this.msgTexto = msgTexto;
    }

    public String getMsgAutor() {
        return msgAutor;
    }

    public void setMsgAutor(String msgAutor) {
        this.msgAutor = msgAutor;
    }

    public long getMsgHora() {
        return msgHora;
    }

    public void setMsgHora(long msgHora) {
        this.msgHora = msgHora;
    }
}

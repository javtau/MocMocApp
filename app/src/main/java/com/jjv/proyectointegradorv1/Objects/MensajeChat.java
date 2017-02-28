package com.jjv.proyectointegradorv1.Objects;

import java.util.Date;

/**
 * Created by javi on 19/02/17.
 */

public class MensajeChat {
    private String msgTexto;
    private String msgEmisor;
    private String msgReceptor;
    private long msgHora;

    public MensajeChat() {
    }

    public MensajeChat(String msg, String e, String r) {
        this.msgTexto = msg;
        this.msgEmisor = e;
        this.msgReceptor = r;

        // consigue la hora actual
        this.msgHora = new Date().getTime();
    }

    public String getMsgTexto() {
        return msgTexto;
    }

    public void setMsgTexto(String msgTexto) {
        this.msgTexto = msgTexto;
    }

    public String getMsgReceptor() {
        return msgReceptor;
    }

    public void setMsgReceptor(String msgReceptor) {
        this.msgReceptor = msgReceptor;
    }

    public String getMsgEmisor() {
        return msgEmisor;
    }

    public void setMsgEmisor(String msgEmisor) {
        this.msgEmisor = msgEmisor;
    }

    public long getMsgHora() {
        return msgHora;
    }

    public void setMsgHora(long msgHora) {
        this.msgHora = msgHora;
    }
}


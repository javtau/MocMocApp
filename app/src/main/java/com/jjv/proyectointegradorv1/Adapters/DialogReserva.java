package com.jjv.proyectointegradorv1.Adapters;


import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jjv.proyectointegradorv1.Objects.Publicacion;
import com.jjv.proyectointegradorv1.R;

/**
 * Created by javi0 on 11/01/2017.
 */

public  class DialogReserva extends DialogFragment {

    Publicacion pub;
    public static String TAG = DialogReserva.class.getSimpleName();

    public static DialogReserva newInstance(Publicacion pub){
        DialogReserva d = new DialogReserva();
        Bundle args = new Bundle();
        args.putParcelable("publicacion", pub);
        d.setArguments(args);
        return d;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Publicacion pub = getArguments().getParcelable("publicacion");
        Log.d(TAG, pub.getDestino());
        return inflater.inflate(R.layout.dialog_fragment_reservar, container, false);
    }
}

package com.jjv.proyectointegradorv1.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jjv.proyectointegradorv1.R;

import java.util.Calendar;

/**
 * Created by javi0 on 16/01/2017.
 */

public class TimepickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // recogemosla hora actual
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // creamos una nueva instancia con la hora actual y la devolvemos

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }



    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView tv1= (TextView) getActivity().findViewById(R.id.txt_hora);
        String hora = hourOfDay>9 ? ""+hourOfDay:"0"+hourOfDay;
        String minutos = minute>9 ? ""+minute:"0"+minute;
        StringBuilder sb = new StringBuilder();
        sb.append(hora);
        sb.append(":");
        sb.append(minutos);
        tv1.setText(sb);
    }



}

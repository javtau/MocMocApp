package com.jjv.proyectointegradorv1.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjv.proyectointegradorv1.Objects.Publicacion;
import com.jjv.proyectointegradorv1.R;
import com.jjv.proyectointegradorv1.UI.MainActivity;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


public class Publicar_viaje extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String[] plazas = {"1", "2", "3", "4"};
    TextView txt_origen, txt_destino, txt_fecha, txt_hora, txt_precio;
    Spinner sp_plazas;

    private DatabaseReference mDatabase;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private FloatingActionButton fab;

    // variables para sonido
    private SoundPool soundPool;
    private int soundID;
    AudioManager audioManager;
    AssetManager assetManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_publicarviaje, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String currentTime = formatearHora(new Date().getTime());
        String currentDate = sdf.format(new Date());

        txt_origen = (TextView) view.findViewById(R.id.txt_origen);
        txt_destino = (TextView) view.findViewById(R.id.txt_distino);
        txt_fecha = (TextView) view.findViewById(R.id.txt_fecha);
        txt_precio = (TextView) view.findViewById(R.id.txt_precio);
        txt_hora = (TextView) view.findViewById(R.id.txt_hora);
        sp_plazas = (Spinner) view.findViewById(R.id.sp_plazas);

        txt_hora.setText(currentTime);
        txt_fecha.setText(currentDate);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, plazas);
        sp_plazas.setAdapter(adapter);

        setFocuListener(txt_origen);
        setFocuListener(txt_destino);
        setFocuListener(txt_precio);

        //Atendemos al evento de que se pulse el campo de la fecha
        /*txt_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos el fragment dialog que contendra el picker de la fecha
                DatepickerFragment newFragment = new DatepickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });*/
        //Dado que el evento onclick no se activa al hacer foco en el campo atendemos al evento on focused
        //para lanzar el picker cuando se haga haga click en el campo por primera vez
        txt_fecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (txt_fecha.isFocused()) {
                    DatepickerFragment newFragment = new DatepickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                    txt_fecha.clearFocus();
                }
            }
        });
        //Atendemos al evento de que se pulse el campo de la hora
        /*txt_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos el fragment dialog que contendra el picker de la hora
                DialogFragment newFragment = new TimepickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });*/
        //Dado que el evento onclick no se activa al hacer foco en el campo atendemos al evento on focused
        //para lanzar el picker cuando se haga haga click en el campo por primera vez
        txt_hora.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (txt_hora.isFocused()) {
                    DialogFragment newFragment = new TimepickerFragment();
                    newFragment.show(getFragmentManager(), "TimePicker");
                    txt_hora.clearFocus();
                }
            }
        });

        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        /** SONIDO **/
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        assetManager = getContext().getAssets();
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // si la version del usuario es lollipop o superior
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build();

            try{
                AssetFileDescriptor descriptor = assetManager.openFd("alert1.mp3");
                soundID = soundPool.load(descriptor, 1);
            }catch(IOException e){
                Log.e("***ASSET ERROR: ", "No se ha recuperado el archivo");
            }
            // en caso contrario
        }else{
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
            try{
                AssetFileDescriptor descriptor = assetManager.openFd("alert1.mp3");
                soundID = soundPool.load(descriptor, 1);
            }catch(IOException e){
                Log.e("***ASSET ERROR: ", "No se ha recuperado el archivo");
            }
        }

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        String origen = txt_origen.getText().toString();
                        String destino = txt_destino.getText().toString();

                        String fecha = txt_fecha.getText().toString();
                        String hora = txt_hora.getText().toString();
                        int plazas =  sp_plazas.getSelectedItemPosition()+1;
                        String precio = txt_precio.getText().toString();
                        if (precio.equals("")){
                            precio="0";
                        }

                        if(origen.equals("")||destino.equals("")){
                            Snackbar.make(getView(), getString(R.string.rellene_los_campos), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                        }else{
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            //Publicacion viaje = new Publicacion(origen,destino,fecha,hora,plazas,precio);
                            String key = mDatabase.child("posts").push().getKey();
                            String userId = user.getUid();
                            Map<String, Object> viaje = new Publicacion(user.getDisplayName(), origen, destino, fecha, hora, plazas, precio,userId,key).toMap();
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/trip/" + key, viaje);
                            childUpdates.put("/user-trips/" + userId + "/" + key, viaje);
                            mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Snackbar.make(getView(), getString(R.string.publicacion_enviada), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    sonar();
                                    txt_origen.setText("");
                                    txt_destino.setText("");
                                    txt_hora.setText(formatearHora(new Date().getTime()));
                                    txt_fecha.setText(sdf.format(new Date()));
                                    txt_precio.setText("");
                                    ((MainActivity) getActivity()).selectPage(2);
                                }
                            });
                        }
                    }
                });
            }
        });

        Animation rotar;
        rotar= AnimationUtils.loadAnimation(getContext(),R.anim.rotar);
        fab.setAnimation(rotar);

    }

    private void setFocuListener(final TextView tv) {
        View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                esconderTecladoDe(getContext(), tv);
            }
        };

        tv.setOnFocusChangeListener(focusListener);
    }

    public static void esconderTecladoDe(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void sonar(){
        soundPool.play(soundID, 1, 1, 0, 0, 1);
    }

    public String formatearHora(long hora){
        String horaFormateada;
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        cal.setTimeInMillis(hora);

        horaFormateada = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        return horaFormateada;
    }

}

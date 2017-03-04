package com.jjv.proyectointegradorv1.UI;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjv.proyectointegradorv1.Adapters.ChatAdapter;
import com.jjv.proyectointegradorv1.Fragments.Chat;
import com.jjv.proyectointegradorv1.Objects.MensajeChat;
import com.jjv.proyectointegradorv1.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ConversacionChat extends AppCompatActivity {

    public static final String TAG = "Chat";

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseUser usuarioLogueado;
    private EditText etMensaje;
    private Button btnEnviar;
    private RecyclerView listaMensajes;
    //private ImageButton btnImagen;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversacion_chat);

        Intent i = getIntent();
        String pubKey = i.getStringExtra(Chat.KEY_PUB);
        initDatabase(pubKey);
        initViews();
    }

    private void initViews() {
        etMensaje = (EditText) findViewById(R.id.et_msg);
        btnEnviar = (Button) findViewById(R.id.btn_enviar);
        listaMensajes = (RecyclerView) findViewById(R.id.lista_msgs);
        //btnImagen = (ImageButton) v.findViewById(R.id.btn_imagen);

        // asigna un click listener al btnImagen para permitir al usuario seleccionar una imagen
        /*btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(i, SEL_FOTO);
            }
        });*/

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String horaFormateada = formatearHora(new Date().getTime());
                MensajeChat msgChat = new MensajeChat(etMensaje.getText().toString(), usuarioLogueado.getDisplayName(), usuarioLogueado.getEmail(), horaFormateada);
                // push mensaje a la bd
                mRef.push().setValue(msgChat);
                etMensaje.setText("");
                etMensaje.setHint(getString(R.string.chat_hint));
                etMensaje.clearFocus();
                esconderTeclado(ConversacionChat.this);

            }
        });
    }

    private void initDatabase(String pubKey) {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Chat/" + pubKey);
        usuarioLogueado = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioLogueado != null){
            mRef.addChildEventListener(new ChildEventListener() {
                ArrayList<MensajeChat> mensajes = new ArrayList<>();
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    // recupera el mensaje y lo añade en la UI
                    MensajeChat msg = dataSnapshot.getValue(MensajeChat.class);
                    mensajes.add(msg);
                    chatAdapter = new ChatAdapter(mensajes);
                    chatAdapter.notifyDataSetChanged();
                    // sin esta linea el layout no muestra nada
                    listaMensajes.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    // para que el recycler no suba al primer elemento
                    listaMensajes.scrollToPosition(mensajes.size()-1);
                    listaMensajes.setAdapter(chatAdapter);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    MensajeChat msg = dataSnapshot.getValue(MensajeChat.class);
                    mensajes.add(msg);
                    chatAdapter = new ChatAdapter(mensajes);
                    chatAdapter.notifyDataSetChanged();
                    // sin esta linea el recycler view  no muestra nada
                    listaMensajes.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    // para que el recycler no suba al primer elemento
                    listaMensajes.scrollToPosition(mensajes.size()-1);
                    listaMensajes.setAdapter(chatAdapter);

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    public static void esconderTeclado(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

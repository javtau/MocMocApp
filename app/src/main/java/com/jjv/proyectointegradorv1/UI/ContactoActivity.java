package com.jjv.proyectointegradorv1.UI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.jjv.proyectointegradorv1.R;

public class ContactoActivity extends AppCompatActivity {


    private EditText etContacto;
    private FloatingActionButton fab;
    private ActionBar actBar;
    String usuarioLogueado, emailLogueado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        actBar = getSupportActionBar();
        actBar.setDisplayHomeAsUpEnabled(true);

        usuarioLogueado = getIntent().getStringExtra(MainActivity.FB_USER);
        emailLogueado = getIntent().getStringExtra(MainActivity.FB_EMAIL);

        etContacto = (EditText) findViewById(R.id.et_contacto);
        etContacto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etContacto.setHint("");
            }
        });
        etContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etContacto, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab_contacto);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etContacto.equals("")){
                    Toast.makeText(ContactoActivity.this, R.string.mensajeParaEnviarnos, Toast.LENGTH_SHORT).show();
                }else{
                    Intent mandarEmail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", "jjvuem@gmail.com", null));
                    // Explicitly only use Gmail to send
                    mandarEmail.putExtra(Intent.EXTRA_SUBJECT, "MSG::" + usuarioLogueado + "::" + emailLogueado);
                    mandarEmail.putExtra(Intent.EXTRA_TEXT, etContacto.getText().toString());
                    startActivity(Intent.createChooser(mandarEmail, "Mandar el mail con:"));
                    Toast.makeText(ContactoActivity.this, R.string.mensajeConfirmacion, Toast.LENGTH_SHORT).show();
                    etContacto.setText("");
                    finish();
                }
            }
        });

        Animation rotar;
        rotar = AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotar);
        fab.setAnimation(rotar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

package com.jjv.proyectointegradorv1.UI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jjv.proyectointegradorv1.R;

public class Loggin extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private static final String ERROR_PWD_INCORRECTA = "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password.";
    private static final String ERROR_MAIL_NO_EXISTENTE = "com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted.";
    private static final String ERROR_USR_INCORRECTO = "com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted.";
    private static final String ERROR_FORMATO = "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.";

    private EditText txt_usuario;
    private EditText txt_pass;
    private Button btn_login;
    private Button btn_register;
    private ImageView ivBocinaCirculo,ivMocs,ivParentesis,ivBocinaCono;

    //variables usadas para el control de usuario
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                // si el usuario esta registrado correctamente finaliza la actividad volviendo a la principal
                if (user != null) {
                    // User is signed in
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    finish();
                }
                //else {
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                //}

            }
        };

        // relacion de los elementos visuales con su implementacion en codigo
        txt_usuario = (EditText) findViewById(R.id.txt_name);
        txt_pass = (EditText) findViewById(R.id.txt_contraseña);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_login);
        ivBocinaCirculo = (ImageView) findViewById(R.id.ivbocina_circulo);
        ivMocs = (ImageView) findViewById(R.id.ivmocs);
        ivParentesis = (ImageView) findViewById(R.id.ivparantesis);
        ivBocinaCono = (ImageView) findViewById(R.id.ivbocina_cono);
        Animation animCirc = AnimationUtils.loadAnimation(this, R.anim.animacion_circ);
        Animation animMocs = AnimationUtils.loadAnimation(this, R.anim.animacion_mocs);
        //ivMocs.startAnimation(animMocs);
        ivBocinaCirculo.startAnimation(animCirc);
        ivMocs.startAnimation(animMocs);

        //ivParentesis.startAnimation(animMocs);
        //ivBocinaCono.startAnimation(animMocs);


    }

    @Override
    public void onBackPressed() {

        finishAffinity();
    }

    // recoge la contraseña y el mail introducido y comprueba si es un usuario registrado en FireBase
    public void ingresar(View v){
        String user,pass,mail;
        // recoge el texto que introduce el usuario
        user = txt_usuario.getText().toString();
        pass = txt_pass.getText().toString();
        if (pass.equals("") || user.equals("")) {
            Toast.makeText(this, getString(R.string.toast_campos_vacios), Toast.LENGTH_SHORT).show();
        } else {
            // metodo de la API de FireBase que hace el login pasandole el mail y la contraseña
            // controla si hay alguna excepcion y lo muestra por pantalla
            mAuth.signInWithEmailAndPassword(user, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                if (task.getException().toString().equals(ERROR_USR_INCORRECTO)){
                                    //Log.d(TAG, "createUserWithEmail:onComplete:" + task.getException());
                                    Toast.makeText(getBaseContext(), R.string.mail_no_existe,
                                            Toast.LENGTH_SHORT).show();
                                }else if (task.getException().toString().equals(ERROR_MAIL_NO_EXISTENTE)){
                                    //Log.d(TAG, "createUserWithEmail:onComplete:" + task.getException());
                                    Toast.makeText(getBaseContext(), R.string.mail_no_existe,
                                            Toast.LENGTH_SHORT).show();
                                } else if (task.getException().toString().equals(ERROR_PWD_INCORRECTA)){
                                    //Log.d(TAG, "createUserWithEmail:onComplete:" + task.getException());
                                    Toast.makeText(getBaseContext(), R.string.auth_incorrect,
                                            Toast.LENGTH_SHORT).show();
                                }else if (task.getException().toString().equals(ERROR_FORMATO)){
                                    //Log.d(TAG, "createUserWithEmail:onComplete:" + task.getException());
                                    Toast.makeText(getBaseContext(), R.string.mail_failed,
                                            Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    //Log.d(TAG, "createUserWithEmail:onComplete:" + task.getException());
                                    Toast.makeText(getBaseContext(), R.string.auth_failed,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }

    }

    public void registrar(View v){
        Intent i = new Intent(this,register.class);
        startActivity(i);

    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}

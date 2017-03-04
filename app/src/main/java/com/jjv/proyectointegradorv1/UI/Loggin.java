package com.jjv.proyectointegradorv1.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jjv.proyectointegradorv1.R;

public class Loggin extends AppCompatActivity {

    private static final String ERROR_PWD_INCORRECTA = "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password.";
    private static final String ERROR_MAIL_NO_EXISTENTE = "com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted.";
    private static final String ERROR_USR_INCORRECTO = "com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted.";
    private static final String ERROR_FORMATO = "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.";

    private EditText txt_usuario;
    private EditText txt_pass;
    private Button btn_login;
    private Button btn_register;
    private ImageView ivBocinaCirculo,ivMocs,ivParentesis,ivBocinaCono;
    private LinearLayout linearLayoutLog;

    //variables usadas para el control de usuario
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Animation animCirc,animMocs;

    private CheckBox checkCredenciales;
    private SharedPreferences.Editor editor;
    private SharedPreferences check;

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

            }
        };

        // relacion de los elementos visuales con su implementacion en codigo
        txt_usuario = (EditText) findViewById(R.id.txt_name);
        txt_pass = (EditText) findViewById(R.id.txt_contraseña);
        btn_login = (Button) findViewById(R.id.btn_login);
        //btn_register = (Button) findViewById(R.id.btn_login);
        ivBocinaCirculo = (ImageView) findViewById(R.id.ivbocina_circulo);
        ivMocs = (ImageView) findViewById(R.id.ivmocs);
        ivParentesis = (ImageView) findViewById(R.id.ivparantesis);
        ivBocinaCono = (ImageView) findViewById(R.id.ivbocina_cono);
        linearLayoutLog = (LinearLayout) findViewById(R.id.linearLayoutLogs);

        checkCredenciales = (CheckBox) findViewById(R.id.chk_credenciales);

        Animation animElemLog = AnimationUtils.loadAnimation(this,R.anim.animacion_elementos_log);
        animCirc = AnimationUtils.loadAnimation(getBaseContext(), R.anim.animacion_circ);
        animMocs = AnimationUtils.loadAnimation(getBaseContext(), R.anim.animacion_mocs);

        linearLayoutLog.startAnimation(animElemLog);
        ivBocinaCirculo.startAnimation(animCirc);
        ivMocs.startAnimation(animMocs);
        ivParentesis.startAnimation(animMocs);
        initAnim();

        //ivParentesis.startAnimation(animMocs);
        //ivBocinaCono.startAnimation(animMocs);

        // comprueba el estado guardado del checkbox
        check = getSharedPreferences("PREF", MODE_PRIVATE);
        checkCredenciales.setChecked(check.getBoolean("PREF", false));
        checkCredenciales.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = check.edit();
                editor.putBoolean("PREF", checkCredenciales.isChecked());
                editor.commit();
            }
        });
        // si el usuario selecciono guardar sus credenciales recuperalas
        if(checkCredenciales.isChecked()){
            SharedPreferences prefs = getBaseContext().getSharedPreferences(getString(R.string.archivo_preferencias_key), MODE_PRIVATE);
            String user = prefs.getString("usuario", "usuario@ejemplo.com");
            String pass = prefs.getString("pwd", "123");
            txt_usuario.setText(user);
            txt_pass.setText(pass);
        }
    }

    private void initAnim() {
        new CountDownTimer(9000, 1000) {
            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                ivBocinaCirculo.startAnimation(animCirc);
                ivMocs.startAnimation(animMocs);
                ivParentesis.startAnimation(animMocs);
                initAnim();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    // recoge la contraseña y el mail introducido y comprueba si es un usuario registrado en FireBase
    public void ingresar(View v){

        String user,pass;

        // guardar credenciales si el checkbox esta seleccionado
        if(checkCredenciales.isChecked()){
            SharedPreferences credenciales = getBaseContext().getSharedPreferences(getString(R.string.archivo_preferencias_key), MODE_PRIVATE);
            editor = credenciales.edit();

            // recoge el texto que introduce el usuario
            user = txt_usuario.getText().toString();
            pass = txt_pass.getText().toString();

            editor.putString("usuario", user);
            editor.putString("pwd", pass);
            editor.commit();
        }

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
                            // controla los posibles errores que pueden salir
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
                                }else{
                                    //Log.d(TAG, "createUserWithEmail:onComplete:" + task.getException());
                                    Toast.makeText(getBaseContext(), R.string.auth_failed,
                                            Toast.LENGTH_SHORT).show();
                                }
                                // si all es correcto inicia MainActivity
                            }else{
                                Intent i = new Intent(Loggin.this, MainActivity.class);
                                startActivity(i);
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

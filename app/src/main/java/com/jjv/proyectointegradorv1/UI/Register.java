package com.jjv.proyectointegradorv1.UI;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.jjv.proyectointegradorv1.R;

public class Register extends AppCompatActivity {
    private static final String ERROR_FORMATO = "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.";
    private static final String ERROR_MAIL_EXISTENTE = "com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.";
    private EditText txt_newuser;
    private EditText txt_newpass;
    private EditText txt_newmail;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Animation animCirc,animMocs;
    private ImageView ivBocinaCirculo,ivMocs,ivParentesis,ivBocinaCono;
    private LinearLayout linearLayoutLog;

    public static final String TAG = MainActivity.class.getSimpleName();

    //private Button btn_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        txt_newuser = (EditText) findViewById(R.id.txt_newuser);
        txt_newpass = (EditText) findViewById(R.id.txt_newPass);
        txt_newmail = (EditText) findViewById(R.id.txt_newmail);
        //btn_register = (Button) findViewById(R.id.btn_login);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(getBaseContext(), getString(R.string.toast_usuario_creado), Toast.LENGTH_LONG).show();

                    finish();
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        };
        ivBocinaCirculo = (ImageView) findViewById(R.id.ivbocina_circulo);
        ivMocs = (ImageView) findViewById(R.id.ivmocs);
        ivParentesis = (ImageView) findViewById(R.id.ivparantesis);
        ivBocinaCono = (ImageView) findViewById(R.id.ivbocina_cono);
        linearLayoutLog = (LinearLayout) findViewById(R.id.linearLayoutLogs);

        Animation animElemLog = AnimationUtils.loadAnimation(this,R.anim.animacion_elementos_log);
        animCirc = AnimationUtils.loadAnimation(getBaseContext(), R.anim.animacion_circ);
        animMocs = AnimationUtils.loadAnimation(getBaseContext(), R.anim.animacion_mocs);

        linearLayoutLog.startAnimation(animElemLog);
        ivBocinaCirculo.startAnimation(animCirc);
        ivMocs.startAnimation(animMocs);
        ivParentesis.startAnimation(animMocs);
        initAnim();


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

    public void registrar(View v) {
        final String username, pass, mail;
        username = txt_newuser.getText().toString();
        pass = txt_newpass.getText().toString();
        mail = txt_newmail.getText().toString();


        if (pass.equals("") || username.equals("") || mail.equals("")) {
            Toast.makeText(this, getString(R.string.toast_campos_vacios), Toast.LENGTH_SHORT).show();
        } else if (pass.length() < 6) {
            Toast.makeText(this, getString(R.string.toast_pwd_incorrecta), Toast.LENGTH_SHORT).show();
        } else {
            // metodo de FireBase que crea un usuario nuevo pasandole el mail y la contraseña como parametros
            // controla las posibles excepciones y muestra el correspondiente menssaje
            mAuth.createUserWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                if (task.getException().toString().equals(ERROR_FORMATO)) {
                                    //Log.d(TAG, "createUserWithEmail:onComplete:" + task.getException());
                                    Toast.makeText(getBaseContext(), R.string.mail_failed,
                                            Toast.LENGTH_SHORT).show();
                                } else if (task.getException().toString().equals(ERROR_MAIL_EXISTENTE)) {
                                    //Log.d(TAG, "createUserWithEmail:onComplete:" + task.getException());
                                    Toast.makeText(getBaseContext(), R.string.mail_exist,
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    //Log.d(TAG, "createUserWithEmail:onComplete:" + task.getException());
                                    Toast.makeText(getBaseContext(), R.string.auth_failed,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                FirebaseUser user = mAuth.getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username).build();
                                user.updateProfile(profileUpdates);
                                FirebaseAuth.getInstance().signOut();
                                mAuth.signInWithEmailAndPassword(mail, pass);
                            }
                        }
                    });
        }

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

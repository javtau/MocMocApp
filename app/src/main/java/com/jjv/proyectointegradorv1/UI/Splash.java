package com.jjv.proyectointegradorv1.UI;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjv.proyectointegradorv1.R;

public class Splash extends AppCompatActivity {
    private ImageView techo, imgv_derecha, imgv_delantera, imgv_trasera, imgv_izquierda;
    private LinearLayout ly_logo;
    private Animation anim_salir;
    private Animation anim_entrarLogo;

    TextView subtitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        techo = (ImageView) findViewById(R.id.imgv_techo);
        imgv_delantera = (ImageView) findViewById(R.id.imgv_detantera);
        imgv_trasera = (ImageView) findViewById(R.id.imgv_trasera);
        imgv_derecha = (ImageView) findViewById(R.id.imgv_derecha);
        imgv_izquierda = (ImageView) findViewById(R.id.imgv_izquierda);
        ly_logo = (LinearLayout) findViewById(R.id.ly_logo);
        ly_logo.setVisibility(View.INVISIBLE);

        subtitulo = (TextView) findViewById(R.id.txt_subtitle);

        Animation animTecho = AnimationUtils.loadAnimation(this, R.anim.move_techo);
        Animation anim_top2 = AnimationUtils.loadAnimation(this, R.anim.top_2);
        Animation anim_bottom2 = AnimationUtils.loadAnimation(this, R.anim.bottom_2);
        Animation anim_right2 = AnimationUtils.loadAnimation(this, R.anim.right_2);
        Animation anim_left2 = AnimationUtils.loadAnimation(this, R.anim.left_2);
        anim_salir = AnimationUtils.loadAnimation(Splash.this, R.anim.salir);
        anim_entrarLogo= AnimationUtils.loadAnimation(Splash.this, R.anim.entrarlogo);

        techo.setAnimation(animTecho);
        imgv_delantera.setAnimation(anim_top2);
        imgv_trasera.setAnimation(anim_bottom2);
        imgv_izquierda.setAnimation(anim_right2);
        imgv_derecha.setAnimation(anim_left2);
        animTecho.start();
        anim_top2.start();
        anim_bottom2.start();
        anim_right2.start();
        anim_left2.start();

        anim_left2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                techo.setImageResource(R.drawable.cochecompleto);
                techo.setAnimation(anim_salir);
                ly_logo.setAnimation(anim_entrarLogo);
                imgv_delantera.setVisibility(View.INVISIBLE);
                imgv_trasera.setVisibility(View.INVISIBLE);
                imgv_izquierda.setVisibility(View.INVISIBLE);
                imgv_derecha.setVisibility(View.INVISIBLE);
                anim_salir.start();
                anim_entrarLogo.start();
                ly_logo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim_salir.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                techo.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        setTypeFace();
        openApp();
    }

    private void setTypeFace() {
        Typeface fuenteSplash = Typeface.createFromAsset(getAssets(), "fonts/Freshman.ttf");
        subtitulo.setTypeface(fuenteSplash);
    }

    private void openApp() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }
}


package com.jjv.proyectointegradorv1.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.jjv.proyectointegradorv1.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView techo, imgv_derecha, imgv_delantera, imgv_trasera, imgv_izquierda;
    private Animation anim_salir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        techo = (ImageView) findViewById(R.id.imgv_techo);
        imgv_delantera = (ImageView) findViewById(R.id.imgv_detantera);
        imgv_trasera = (ImageView) findViewById(R.id.imgv_trasera);
        imgv_derecha = (ImageView) findViewById(R.id.imgv_derecha);
        imgv_izquierda = (ImageView) findViewById(R.id.imgv_izquierda);



        Animation animTecho = AnimationUtils.loadAnimation(this, R.anim.move_techo);
        Animation anim_top2 = AnimationUtils.loadAnimation(this, R.anim.top_2);
        Animation anim_bottom2 = AnimationUtils.loadAnimation(this, R.anim.bottom_2);
        Animation anim_right2 = AnimationUtils.loadAnimation(this, R.anim.right_2);
        Animation anim_left2 = AnimationUtils.loadAnimation(this, R.anim.left_2);
        anim_salir = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.salir);


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

                techo.setAnimation(anim_salir);

                imgv_delantera.setAnimation(anim_salir);
                imgv_trasera.setAnimation(anim_salir);
                imgv_izquierda.setAnimation(anim_salir);
                imgv_derecha.setAnimation(anim_salir);
                anim_salir.start();
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
                imgv_delantera.setVisibility(View.INVISIBLE);
                imgv_trasera.setVisibility(View.INVISIBLE);
                imgv_izquierda.setVisibility(View.INVISIBLE);
                imgv_derecha.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        openApp(true);
    }

    private void openApp(boolean locationPermission) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity
                        .this, Loggin.class);
                startActivity(intent);
                finish();
            }
        }, 6000);
    }


}

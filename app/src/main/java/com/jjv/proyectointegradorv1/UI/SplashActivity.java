package com.jjv.proyectointegradorv1.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.jjv.proyectointegradorv1.R;

public class SplashActivity extends AppCompatActivity {
    private ImageView techo, imgv_luna_derecha, imgv_luna_frontal, imgv_luna_trasera, imgv_luna_izquierda,
            imgv_capo, imgv_maletero, imgv_lateral_derecho, imgv_lateral_izquierdo, imgv_aleta_derecha,
            imgv_aleta_izquierda, imgv_aletaTras_derecha, imgv_aletaTras_izquierda, imgv_retrovisor_derecho,
            imgv_retrovisor_izquierdo, imgv_division_derecha, imgv_division_izquierda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        techo = (ImageView) findViewById(R.id.imgv_techo);
        imgv_luna_frontal = (ImageView) findViewById(R.id.imgv_luna_frontal);
        imgv_luna_trasera = (ImageView) findViewById(R.id.imgv_luna_trasera);
        imgv_luna_derecha = (ImageView) findViewById(R.id.imgv_luna_derecha);
        imgv_luna_izquierda = (ImageView) findViewById(R.id.imgv_luna_izquierda);
        imgv_capo = (ImageView) findViewById(R.id.imgv_capo);
        imgv_maletero = (ImageView) findViewById(R.id.imgv_maletero);
        imgv_lateral_derecho = (ImageView) findViewById(R.id.imgv_lateral_derecho);
        imgv_lateral_izquierdo = (ImageView) findViewById(R.id.imgv_lateral_izquierdo);
        imgv_division_derecha = (ImageView) findViewById(R.id.imgv_division_derecha);
        imgv_division_izquierda = (ImageView) findViewById(R.id.imgv_division_izquierda);

        imgv_aleta_izquierda = (ImageView) findViewById(R.id.imgv_aleta_frontal_izquierda);
        imgv_aleta_derecha = (ImageView) findViewById(R.id.imgv_aleta_frontal_derecho);
        imgv_aletaTras_derecha = (ImageView) findViewById(R.id.imgv_aleta_trasera_derecha);
        imgv_aletaTras_izquierda = (ImageView) findViewById(R.id.imgv_aleta_trasera_izquierda);

        imgv_retrovisor_derecho = (ImageView) findViewById(R.id.imgv_retrovisor_derecho);
        imgv_retrovisor_izquierdo = (ImageView) findViewById(R.id.imgv_retrovisor_izquierdo);


        Animation animTecho = AnimationUtils.loadAnimation(this, R.anim.move_techo);
        Animation anim_top2 = AnimationUtils.loadAnimation(this, R.anim.top_2);
        Animation anim_bottom2 = AnimationUtils.loadAnimation(this, R.anim.bottom_2);
        Animation anim_right2 = AnimationUtils.loadAnimation(this, R.anim.right_2);
        Animation anim_left2 = AnimationUtils.loadAnimation(this, R.anim.left_2);

        Animation anim_top3 = AnimationUtils.loadAnimation(this, R.anim.top_3);
        Animation anim_bottom3 = AnimationUtils.loadAnimation(this, R.anim.bottom_3);
        Animation anim_right3 = AnimationUtils.loadAnimation(this, R.anim.right_3);
        Animation anim_left3 = AnimationUtils.loadAnimation(this, R.anim.left_3);

        Animation anim_right4 = AnimationUtils.loadAnimation(this, R.anim.right_4);
        Animation anim_left4 = AnimationUtils.loadAnimation(this, R.anim.left_4);

        Animation anim_right_top_corner = AnimationUtils.loadAnimation(this, R.anim.right_top_corner_4);
        Animation anim_left_top_corner = AnimationUtils.loadAnimation(this, R.anim.left_top_corner_4);

        Animation anim_right_bottom_corner = AnimationUtils.loadAnimation(this, R.anim.right_bottom_corner_4);
        Animation anim_left_bottom_corner = AnimationUtils.loadAnimation(this, R.anim.left_bottom_corner_4);


        techo.startAnimation(animTecho);
        imgv_luna_frontal.startAnimation(anim_top2);
        imgv_luna_trasera.startAnimation(anim_bottom2);
        imgv_luna_izquierda.startAnimation(anim_right2);
        imgv_luna_derecha.startAnimation(anim_left2);

        imgv_capo.startAnimation(anim_top3);
        imgv_maletero.startAnimation(anim_bottom3);
        imgv_division_izquierda.startAnimation(anim_right3);
        imgv_lateral_izquierdo.startAnimation(anim_right3);
        imgv_division_derecha.startAnimation(anim_left3);
        imgv_lateral_derecho.startAnimation(anim_left3);

        imgv_retrovisor_izquierdo.startAnimation(anim_right4);
        imgv_retrovisor_derecho.startAnimation(anim_left4);

        imgv_aleta_izquierda.startAnimation(anim_right_top_corner);
        imgv_aleta_derecha.startAnimation(anim_left_top_corner);

        imgv_aletaTras_izquierda.startAnimation(anim_right_bottom_corner);
        imgv_aletaTras_derecha.startAnimation(anim_left_bottom_corner);

        openApp(true);
    }

    private void openApp(boolean locationPermission) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity
                        .this, MainActivity.class);
                startActivity(intent);
                imgv_aleta_derecha.destroyDrawingCache();
                finish();
            }
        }, 3000);
    }


}

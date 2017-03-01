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
                finish();
            }
        }, 3000);
    }


}

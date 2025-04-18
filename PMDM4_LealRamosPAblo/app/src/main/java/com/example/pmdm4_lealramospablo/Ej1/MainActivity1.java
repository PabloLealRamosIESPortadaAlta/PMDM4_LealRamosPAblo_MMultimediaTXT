package com.example.pmdm4_lealramospablo.Ej1;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pmdm4_lealramospablo.R;

public class MainActivity1 extends AppCompatActivity {

    ViewFlipper visor;
    MediaPlayer sonido;
    GestureDetector gestos;
    TextView texto;
    Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        Log.d("DEBUG", "onCreate empezado"); // Para ver si entra

        // Enlazar elementos del layout
        boton = findViewById(R.id.botonPaisajes);
        texto = findViewById(R.id.textoAnimado);
        visor = findViewById(R.id.visorImagenes);
        EditText valoracion = findViewById(R.id.valoracionTexto);

        // Cargar sonido al cambiar imagen
        try {
            // sonido= MediaPlayer.create(R.raw.sonido); da error
            sonido = MediaPlayer.create(this, R.raw.desliza);
            if (sonido == null) {
                Log.e("ERROR", "El sonido no se ha cargado. ¿Está el archivo en la carpeta raw?");
            }
        } catch (Exception e) {
            Log.e("ERROR", "Fallo al cargar sonido: " + e.getMessage());
        }

        // Animaciones de entrada/salida en el visor
        visor.setInAnimation(this, android.R.anim.slide_in_left);
        visor.setOutAnimation(this, android.R.anim.slide_out_right);

        // Muy importante: si no desactivamos autoStart, cambia solo
        visor.setAutoStart(false);
        visor.setClickable(true); // Aunque no haga nada, por si acaso

        // Configurar gestos de deslizar
        gestos = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velX, float velY) {
                Log.d("GESTO", "Desliz detectado");


                if (e1.getX() > e2.getX()) {
                    visor.showNext(); // Deslizar a la izquierda
                    Log.d("GESTO", "Mostrando siguiente imagen");
                } else {
                    visor.showPrevious(); // Deslizar a la derecha
                    Log.d("GESTO", "Mostrando imagen anterior");
                }

                try {
                    sonido.start();
                } catch (Exception ex) {
                    Log.e("ERROR", "Error al reproducir sonido: " + ex.getMessage());
                }

                return true;
            }
        });


        visor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("TOQUE", "Toque detectado en el visor");
                return gestos.onTouchEvent(event);
            }
        });


        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BOTON", "Botón pulsado para animar texto");
                animarTextoPaso1();
            }
        });
    }

    // PASO 1 - Animación de mover texto de lado a lado
    private void animarTextoPaso1() {

        Animation mover = new TranslateAnimation(-500, 500, 0, 0);
        mover.setDuration(1500);
        texto.startAnimation(mover);

        Log.d("ANIMACION", "Paso 1: mover el texto de izquierda a derecha");

        mover.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // No necesitamos hacer nada al empezar
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animarTextoPaso2(); // Después del movimiento, vamos al zoom
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    // PASO 2 - Zoom (agrandar el texto)
    private void animarTextoPaso2() {

        Animation zoom = new ScaleAnimation(
                1f, 2f, 1f, 2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        zoom.setDuration(1500);
        texto.startAnimation(zoom);

        Log.d("ANIMACION", "Paso 2: aplicar zoom al texto");

        zoom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                animarTextoPaso3(); // Y luego desaparece
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    // PASO 3 - Desvanecer (fade out)
    private void animarTextoPaso3() {
        // AlphaAnimation(0f, 1f)
        Animation desaparecer = new AlphaAnimation(1f, 0f);
        desaparecer.setDuration(1500);
        texto.startAnimation(desaparecer);

        Log.d("ANIMACION", "Paso 3: desvanecer el texto");
    }
}

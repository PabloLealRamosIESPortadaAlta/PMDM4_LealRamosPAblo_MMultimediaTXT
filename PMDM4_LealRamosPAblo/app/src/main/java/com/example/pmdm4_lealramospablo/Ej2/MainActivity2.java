package com.example.pmdm4_lealramospablo.Ej2;

import android.Manifest;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.pmdm4_lealramospablo.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    ViewFlipper visor;
    ArrayList<String> urlsImagenes = new ArrayList<>();
    int imagenesPendientes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        visor = findViewById(R.id.visor);
        visor.setAutoStart(false);
        visor.setFlipInterval(3000); // cambia cada 3 segundos

// Animación de entrada/salida (no sé si hace algo pero lo dejo)
        Animation fade = new AlphaAnimation(0.0f, 1.0f);
        fade.setDuration(1000);
        visor.setInAnimation(fade);
        visor.setOutAnimation(fade);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);

// Lista de imágenes (algunas fallan a propósito para ver errores)
        urlsImagenes.add("https://dam.org.es/ficheros/spaniel.jpg");
        urlsImagenes.add("https://dam.org.es/ficheros/bordercollie.jpg");
        urlsImagenes.add("https://dam.org.es/ficheros/husky.jpg");
        urlsImagenes.add("http://192.168.2.50/imagen/foto.jpg"); // falla
        urlsImagenes.add("http://192.168.2.50/noexiste.png");     //  falla
        urlsImagenes.add("https://dam.org.es/ficheros/goldenretriever.jpg");

        imagenesPendientes = urlsImagenes.size();


        for ( String url : urlsImagenes) {
            Glide.with(this)
                    .load(url)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                            ImageView imagen = new ImageView(MainActivity2.this);
                            imagen.setImageDrawable(drawable);
                            visor.addView(imagen);
                            Log.d("CARGA", "Imagen cargada: " + url);
                            comprobarFinCarga();
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            Toast.makeText(MainActivity2.this, "Error al cargar: " + url, Toast.LENGTH_SHORT).show();
                            Log.e("ERROR_CARGA", "Fallo al cargar imagen: " + url);
                            guardarError(url, "No se pudo descargar la imagen");
                            comprobarFinCarga();
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            // ni idea de para qué sirve esto, lo dejo vacío
                        }
                    });
        }
    }


    private void comprobarFinCarga() {
        imagenesPendientes--;
        Log.d("CARGA", "Quedan por cargar: " + imagenesPendientes);

        if (imagenesPendientes == 0) {
            Log.d("CARGA", "¡Todas cargadas! Empezamos visor");
            visor.startFlipping();
        }
    }

    // Esta función devuelve el archivo donde guardamos los errores
    private File getCarpetaErrores() {
        File carpeta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "ErroresApp");

        if (!carpeta.exists()) {
            boolean creada = carpeta.mkdirs();
            if (!creada) {
                Log.e("ERROR_ARCHIVO", "No se pudo crear carpeta para errores");
            }
        }

        return new File(carpeta, "errores.txt");
    }

    // Guarda el error en un txt (si funciona)
    private void guardarError(String url, String causa) {
        try {
            File archivo = getCarpetaErrores();

            BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo, true));
            String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            escritor.write(url + " | " + fecha + " | " + causa + "\n");
            escritor.close();

            Log.d("GUARDAR", "Error guardado: " + url);
        } catch (Exception e) {
            Log.e("ERRORES", "No se pudo escribir el error en el archivo", e);
        }
    }
}

package com.example.pmdm4_lealramospablo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button botonEjercicio1 = findViewById(R.id.botonEjercicio1);
        Button botonEjercicio2 = findViewById(R.id.botonEjercicio2);

        botonEjercicio1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.example.pmdm4_lealramospablo.Ej1.MainActivity1.class);
            startActivity(intent);
        });

        botonEjercicio2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.example.pmdm4_lealramospablo.Ej2.MainActivity2.class);
            startActivity(intent);
        });
    }
}

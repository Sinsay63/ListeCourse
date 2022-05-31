package com.example.listecourse;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnProduit;
    private Button btnListe;
    private Button btnRecette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //deleteDatabase("listeCourse.db");

        btnProduit = findViewById(R.id.btnProduit);
        btnListe = findViewById(R.id.btnListe);
        btnRecette = findViewById(R.id.btnRecette);
        btnProduit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProduitIntent = new Intent(MainActivity.this, ProduitActivity.class);
                startActivity(ProduitIntent);
            }
        });

        btnListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent listeIntent = new Intent(MainActivity.this, ListeActivity.class);
                startActivity(listeIntent);
            }
        });

        btnRecette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProduitIntent = new Intent(MainActivity.this, RecetteActivity.class);
                startActivity(ProduitIntent);
            }
        });

    }
}
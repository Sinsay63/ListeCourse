package com.example.listecourse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.example.listecourse.dao.ListeCourse;
import com.example.listecourse.dao.Produit;
import com.example.listecourse.dao.Produit_ListeCourse;
import com.example.listecourse.tools.DataBaseLinker;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TableLayout listeCourse;
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
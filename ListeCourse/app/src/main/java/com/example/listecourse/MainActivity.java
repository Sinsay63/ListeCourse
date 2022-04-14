package com.example.listecourse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.example.listecourse.dao.ListeCourse;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //deleteDatabase("listeCourse.db");

        btnProduit = findViewById(R.id.btnProduit);
        btnListe = findViewById(R.id.btnListe);
        btnProduit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProduitIntent = new Intent(MainActivity.this, ProduitActivity.class);
                //ProduitIntent.putExtra("data", 15);
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

    }


    public ArrayList<com.example.listecourse.dao.Produit> getProduitsByListeId(int idListe){

        ArrayList<Produit_ListeCourse> listeProduitListe = null;
        ArrayList<com.example.listecourse.dao.Produit> listeProduit = new ArrayList<>();

        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produit_ListeCourse, Integer> daoProduit = linker.getDao( Produit_ListeCourse.class );

            QueryBuilder<Produit_ListeCourse, Integer> queryBuilder = daoProduit.queryBuilder();
            queryBuilder.where().eq("idListeCourse",idListe);

            PreparedQuery<Produit_ListeCourse> preparedQuery = queryBuilder.prepare();

            listeProduitListe = (ArrayList<Produit_ListeCourse>) daoProduit.query(preparedQuery);

        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();

        for (Produit_ListeCourse prod : listeProduitListe){
            listeProduit.add(prod.getProduit());
        }

        return listeProduit;
    }

}
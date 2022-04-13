package com.example.listecourse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.example.listecourse.dao.Produit;
import com.example.listecourse.dao.Produit_ListeCourse;
import com.example.listecourse.tools.DataBaseLinker;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TableLayout listeCourse;
    private Button btnAddProduit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deleteDatabase("listeCourse.db");


        getAllProduits();
        btnAddProduit = findViewById(R.id.btnAddProduit) ;
        btnAddProduit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ajouterProduitIntent = new Intent(MainActivity.this, Produits.class);
                startActivity(ajouterProduitIntent);
            }
        });

    }

    public Produit getProduitById(int id){
        Produit prod = null;
        DataBaseLinker linker = new DataBaseLinker(this);

        try {
            Dao<Produit, Integer> daoProduit = linker.getDao( Produit.class );
            prod = daoProduit.queryForId(id);

        } catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
        return prod;
    }

    public ArrayList<Produit> getAllProduits(){
        ArrayList<Produit> listeProduit = null;
        DataBaseLinker linker = new DataBaseLinker(this);

        try {
            Dao<Produit, Integer> daoProduit = linker.getDao( Produit.class );
            listeProduit = (ArrayList<Produit>) daoProduit.queryForAll();

        } catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
        Log.e("testsdt", ""+listeProduit);
        return listeProduit;
    }

    public ArrayList<Produit> getProduitsByListeId(int idListe){

        ArrayList<Produit_ListeCourse> listeProduitListe = null;
        ArrayList<Produit> listeProduit = new ArrayList<>();

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

    //public void createProduit
}
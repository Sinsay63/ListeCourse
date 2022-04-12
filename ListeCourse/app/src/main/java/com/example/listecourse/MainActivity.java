package com.example.listecourse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.example.listecourse.dao.Produit;
import com.example.listecourse.tools.DataBaseLinker;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TableLayout listeCourse;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        deleteDatabase("listeCourse.db");
        btn = findViewById(R.id.btn);
        Produit prod = getProduit(1);
        btn.setText(prod.getLibelle());

        /*ajouterProduit = findViewById(R.id.ajouterProduit) ;
        ajouterProduit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ajouterProduitIntent = new Intent(MainActivity.this, Test.class);
                startActivity(ajouterProduitIntent);
            }
        });
         */
    }

    public Produit getProduit(int id){
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

    public ArrayList<Produit> getAllProduit(){
        ArrayList<Produit> listeProduit = null;
        DataBaseLinker linker = new DataBaseLinker(this);

        try {
            Dao<Produit, Integer> daoProduit = linker.getDao( Produit.class );
            listeProduit = (ArrayList<Produit>) daoProduit.queryForAll();

        } catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
        return listeProduit;
    }


}
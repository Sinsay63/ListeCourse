package com.example.listecourse;

import androidx.appcompat.app.AppCompatActivity;

import android.database.SQLException;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.listecourse.dao.Produit;
import com.example.listecourse.dao.Produit_ListeCourse;
import com.example.listecourse.dao.Produit_Taille;
import com.example.listecourse.dao.Taille;
import com.example.listecourse.tools.DataBaseLinker;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class ProduitActivity extends AppCompatActivity {


    private Button btnAddProduit;
    private TableLayout tableProduit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produits);
        btnAddProduit = findViewById(R.id.btnAddProduit);
        tableProduit = findViewById(R.id.tableProduit);

        displayProduit();
        btnAddProduit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public ArrayList<Produit> getAllProduits(){
        ArrayList<Produit> listeProduit = null;
        DataBaseLinker linker = new DataBaseLinker(this);

        try {
            Dao<Produit, Integer> daoProduit = linker.getDao(Produit.class );
            listeProduit = (ArrayList<Produit>) daoProduit.queryForAll();
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();

        return listeProduit;
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

    public void createProduit(){
        DataBaseLinker linker = new DataBaseLinker(this);

        try {
            Dao<Produit, Integer> daoProduit = linker.getDao(Produit.class );
            String libelle;

            daoProduit.create(new Produit());

        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
    }

    public List<Taille> getTailleProduit(Produit produit){
        DataBaseLinker linker = new DataBaseLinker(this);
        ArrayList<Taille> listeTaille = new ArrayList<>();
        try {
            int idProduit = produit.getIdProduit();
            Dao<Produit_Taille, Integer> daoProduitTaille = linker.getDao(Produit_Taille.class );

            QueryBuilder<Produit_Taille, Integer> queryBuilder = daoProduitTaille.queryBuilder();
            queryBuilder.where().eq("idProduit",idProduit);

            PreparedQuery<Produit_Taille> preparedQuery = queryBuilder.prepare();

            List<Produit_Taille> listeProduitTaille = daoProduitTaille.query(preparedQuery);

            for(Produit_Taille pt : listeProduitTaille){
                listeTaille.add(pt.getTaille());
            }
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();

        return listeTaille;
    }

    public void displayProduit(){
        tableProduit.removeAllViews();
        for(Produit prod : getAllProduits()){

            TableRow.LayoutParams param = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    4f
            );
            TableRow rowProduit = new TableRow(this);

            rowProduit.setLayoutParams(param);

            Spinner tailleSpiner = new Spinner(this);

            TextView textLibelle = new TextView(this);


            textLibelle.setText(prod.getLibelle());

            List<Taille> listeTaille = getTailleProduit(prod);
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,listeTaille);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tailleSpiner.setAdapter(adapter);

            TableRow.LayoutParams paramBtn = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1f
            );


            ImageButton addProduit = new ImageButton(this);
            addProduit.setLayoutParams(paramBtn);
            addProduit.setBackground(null);
            addProduit.setImageResource(R.drawable.add);

            rowProduit.addView(textLibelle);
            rowProduit.addView(tailleSpiner);
            rowProduit.addView(addProduit);

            tableProduit.addView(rowProduit);
        }
    }
}
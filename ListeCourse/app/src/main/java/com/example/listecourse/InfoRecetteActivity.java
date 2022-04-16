package com.example.listecourse;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.listecourse.dao.Recette;
import com.example.listecourse.dao.Produit;
import com.example.listecourse.dao.Produit_Recette;
import com.example.listecourse.tools.DataBaseLinker;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class InfoRecetteActivity extends AppCompatActivity {

    private TableLayout tableInfoRecette;
    private ConstraintLayout page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_recette);
        tableInfoRecette = findViewById(R.id.tableInfoRecette);
        page = findViewById(R.id.page);

        displayProduits();
    }

    public ArrayList<Produit> getProduitsByRecette(Recette recette){

        ArrayList<Produit_Recette> listeProduitRecette = null;
        ArrayList<Produit> listeProduit = new ArrayList<>();

        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produit_Recette, Integer> daoProduitR = linker.getDao( Produit_Recette.class );

            QueryBuilder<Produit_Recette, Integer> queryBuilder = daoProduitR.queryBuilder();
            queryBuilder.where().eq("idRecette",recette.getIdRecette());

            PreparedQuery<Produit_Recette> preparedQuery = queryBuilder.prepare();

            listeProduitRecette = (ArrayList<Produit_Recette>) daoProduitR.query(preparedQuery);

        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
        for (Produit_Recette prod : listeProduitRecette){

            Produit produit = prod.getProduit();

            produit.setQuantite(prod.getQuantite());
            produit.setTaille(prod.getTaille());

            listeProduit.add(produit);
        }

        return listeProduit;
    }

    public Recette getRecette(){
        Recette recette = null;

        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Intent intent = this.getIntent();
            int idListe = intent.getIntExtra("liste",0);
            if(idListe>0) {
                Dao<Recette, Integer> daoRecette = linker.getDao(Recette.class);
                recette = daoRecette.queryForId(idListe);
            }
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
        return recette;
    }

    public void displayProduits(){

        }

    public void deleteProduitFromRecette(Produit produit,Recette recette){
        DataBaseLinker linker = new DataBaseLinker(this);

        try {

            Dao<Produit_Recette, Integer> daoProduitRecette = linker.getDao( Produit_Recette.class );

            DeleteBuilder<Produit_Recette, Integer> deleteBuilder = daoProduitRecette.deleteBuilder();
            deleteBuilder.where().eq("idProduit",produit.getIdProduit()).and()
                    .eq("idRecette",recette.getIdRecette()).and().eq("idTaille",produit.getTaille().getIdTaille());

            PreparedDelete<Produit_Recette> preparedDelete = deleteBuilder.prepare();

            daoProduitRecette.delete(preparedDelete);

        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }


    public Produit_Recette getProduitRecetteByRecetteAndProduit(Recette recette, Produit produit){
        List<Produit_Recette> pl = null;
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produit_Recette, Integer> daoProduitListe = linker.getDao( Produit_Recette.class );

            QueryBuilder<Produit_Recette, Integer> queryBuilder = daoProduitListe.queryBuilder();
            queryBuilder.where().eq("idRecette",recette.getIdRecette()).and()
                    .eq("idProduit",produit.getIdProduit()).and().eq("quantite",produit.getQuantite()).and()
                    .eq("idTaille",produit.getTaille().getIdTaille());

            PreparedQuery<Produit_Recette> preparedQuery = queryBuilder.prepare();

            pl = daoProduitListe.query(preparedQuery);
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
        return pl.get(0);
    }
}

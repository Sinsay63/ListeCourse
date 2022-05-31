package com.example.listecourse;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.listecourse.dao.ListeCourse;
import com.example.listecourse.dao.Produit;
import com.example.listecourse.dao.Produit_ListeCourse;
import com.example.listecourse.tools.DataBaseLinker;
import com.google.android.material.snackbar.Snackbar;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.util.ArrayList;
import java.util.List;

public class InfoListeActivity extends AppCompatActivity {

    private TableLayout tableInfoListe;
    private ConstraintLayout page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_liste);
        tableInfoListe = findViewById(R.id.tableInfoListe);
        page = findViewById(R.id.page);
        displayProduits();
    }

    public ArrayList<Produit> getProduitsByListe(ListeCourse liste){

        ArrayList<Produit_ListeCourse> listeProduitListe = null;
        ArrayList<Produit> listeProduit = new ArrayList<>();

        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produit_ListeCourse, Integer> daoProduitL = linker.getDao( Produit_ListeCourse.class );

            QueryBuilder<Produit_ListeCourse, Integer> queryBuilder = daoProduitL.queryBuilder();
            queryBuilder.where().eq("idListeCourse",liste.getIdListeCourse());

            PreparedQuery<Produit_ListeCourse> preparedQuery = queryBuilder.prepare();

            listeProduitListe = (ArrayList<Produit_ListeCourse>) daoProduitL.query(preparedQuery);

        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
        for (Produit_ListeCourse prod : listeProduitListe){

            Produit produit = prod.getProduit();

            produit.setQuantite(prod.getQuantite());
            produit.setTaille(prod.getTaille());

            listeProduit.add(produit);
        }

        return listeProduit;
    }

    public ListeCourse getListe(){
        ListeCourse liste = null;

        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Intent intent = this.getIntent();
            int idListe = intent.getIntExtra("liste",0);
            if(idListe>0) {
                Dao<ListeCourse, Integer> daoListe = linker.getDao(ListeCourse.class);
                liste = daoListe.queryForId(idListe);
            }
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
        return liste;
    }

    public void displayProduits(){
        tableInfoListe.removeAllViews();
        ListeCourse liste = getListe();
        ArrayList<Produit> listeProduits = getProduitsByListe(liste);
        if(listeProduits.size()>0) {
            for (Produit produit : listeProduits) {
                TableRow.LayoutParams param = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        4f
                );

                TableRow rowProduit = new TableRow(this);
                rowProduit.setLayoutParams(param);

                Produit_ListeCourse pl = getProduitListeByListeAndProduit(liste, produit);


                CheckBox cbCart = new CheckBox(this);
                cbCart.setText(produit.getLibelle() + " - " + produit.getTaille() + " - x" + produit.getQuantite());

                if (pl.isCart()) {
                    cbCart.setChecked(true);
                } else {
                    cbCart.setChecked(false);
                }

                cbCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            Snackbar.make(page, "Le produit " + produit.getLibelle() + " a bien été ajouté au caddie !", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(page, "Le produit " + produit.getLibelle() + " a bien été enlevé du caddie !", Snackbar.LENGTH_LONG).show();
                        }
                        addRemoveCart(produit, liste, b);
                    }
                });


                ImageButton deleteProduit = new ImageButton(this);
                deleteProduit.setBackground(null);
                deleteProduit.setImageResource(R.drawable.delete);

                deleteProduit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteProduitFromListe(produit, liste);
                        tableInfoListe.removeView(rowProduit);
                        Snackbar.make(page, "Le produit " + produit.getLibelle() + " a bien été supprimé de la liste !", Snackbar.LENGTH_LONG).show();
                    }
                });

                rowProduit.addView(cbCart);
                rowProduit.addView(deleteProduit);

                tableInfoListe.addView(rowProduit);
            }
        }
        else{
            TextView textVideProduit = new TextView(this);
            TextView textVideRedirect = new TextView(this);
            TableRow rowProduit = new TableRow(this);
            TableRow rowRedirect = new TableRow(this);

            textVideProduit.setTextSize(20);
            textVideRedirect.setTextSize(20);
            textVideProduit.setText(Html.fromHtml("Aucun produit n'a été ajouté à la liste ! "));
            textVideRedirect.setText(Html.fromHtml("Aller au <u><b>Catalogue</b></u> ! "));

            textVideRedirect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ProduitIntent = new Intent(InfoListeActivity.this, ProduitActivity.class);
                    startActivity(ProduitIntent);
                }
            });



            rowProduit.addView(textVideProduit);
            rowRedirect.addView(textVideRedirect);
            tableInfoListe.addView(rowProduit);
            tableInfoListe.addView(rowRedirect);
        }
    }

    public void deleteProduitFromListe(Produit produit,ListeCourse liste){
        DataBaseLinker linker = new DataBaseLinker(this);

        try {

            Dao<Produit_ListeCourse, Integer> daoProduitListe = linker.getDao( Produit_ListeCourse.class );

            DeleteBuilder<Produit_ListeCourse, Integer> deleteBuilder = daoProduitListe.deleteBuilder();
            deleteBuilder.where().eq("idProduit",produit.getIdProduit()).and()
            .eq("idListeCourse",liste.getIdListeCourse()).and().eq("idTaille",produit.getTaille().getIdTaille());

            PreparedDelete<Produit_ListeCourse> preparedDelete = deleteBuilder.prepare();

            daoProduitListe.delete(preparedDelete);

        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void addRemoveCart(Produit produit, ListeCourse liste, boolean isCart){
        DataBaseLinker linker = new DataBaseLinker(this);

        try {

            Dao<Produit_ListeCourse, Integer> daoProduitListe = linker.getDao( Produit_ListeCourse.class );

            UpdateBuilder<Produit_ListeCourse,Integer> updateBuilder = daoProduitListe.updateBuilder();

            updateBuilder.updateColumnValue("isCart",isCart).where().eq("idProduit",produit.getIdProduit()).and()
            .eq("idListeCourse",liste.getIdListeCourse()).and().eq("idTaille",produit.getTaille().getIdTaille());

            PreparedUpdate preparedUpdate = updateBuilder.prepare();

            daoProduitListe.update(preparedUpdate);
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public Produit_ListeCourse getProduitListeByListeAndProduit(ListeCourse liste, Produit produit){
        List<Produit_ListeCourse> pl = null;
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produit_ListeCourse, Integer> daoProduitListe = linker.getDao( Produit_ListeCourse.class );

            QueryBuilder<Produit_ListeCourse, Integer> queryBuilder = daoProduitListe.queryBuilder();
            queryBuilder.where().eq("idListeCourse",liste.getIdListeCourse()).and()
            .eq("idProduit",produit.getIdProduit()).and().eq("quantite",produit.getQuantite()).and()
            .eq("idTaille",produit.getTaille().getIdTaille());

            PreparedQuery<Produit_ListeCourse> preparedQuery = queryBuilder.prepare();

            pl = daoProduitListe.query(preparedQuery);
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
        return pl.get(0);
    }
}

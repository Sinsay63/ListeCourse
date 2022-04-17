package com.example.listecourse;


import static com.example.listecourse.InfoRecetteActivity.getProduitsByRecette;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.listecourse.dao.ListeCourse;
import com.example.listecourse.dao.Produit;
import com.example.listecourse.dao.Produit_ListeCourse;
import com.example.listecourse.dao.Produit_Recette;
import com.example.listecourse.dao.Recette;
import com.example.listecourse.dao.Taille;
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

public class RecetteActivity extends AppCompatActivity {

    private ConstraintLayout page;
    private TableLayout tableRecette;
    private Button btnCreateRecette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recettes);

        tableRecette = findViewById(R.id.tableRecette);
        btnCreateRecette = findViewById(R.id.btnCreateRecette);
        page = findViewById(R.id.page);
        btnCreateRecette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpRecette();
            }
        });
        displayRecettes();
    }

    public List<ListeCourse> getAllListes(){
        List<ListeCourse> listeListeCourses = null;

        DataBaseLinker linker = new DataBaseLinker(this);
        try {

            Dao<ListeCourse, Integer> daoListe = linker.getDao( ListeCourse.class );
            listeListeCourses  =  daoListe.queryForAll();
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
        return listeListeCourses;
    }

    public ArrayList<Recette> getAllRecettes(){
        DataBaseLinker linker = new DataBaseLinker(this);
        ArrayList<Recette> listeRecettes = null;
        try {
            Dao<Recette, Integer> daoRecette = linker.getDao(Recette.class);
            listeRecettes = (ArrayList<Recette>) daoRecette.queryForAll();
        }
        catch (SQLException | java.sql.SQLException throwables) {
        throwables.printStackTrace();
        }

    return listeRecettes;
    }

    public Recette getRecettebyId(int idRecette){
        DataBaseLinker linker = new DataBaseLinker(this);
        Recette recette = null;
        try {
            Dao<Recette, Integer> daoRecette = linker.getDao(Recette.class);
            recette = daoRecette.queryForId(idRecette);
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();

        return recette;
    }

    public void displayRecettes(){
        tableRecette.removeAllViews();
        ArrayList<Recette> listeRecettes = getAllRecettes();
        if(listeRecettes != null) {
            for (Recette recette : listeRecettes) {

                TableRow.LayoutParams param = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        4f
                );

                TableRow rowRecette = new TableRow(this);
                rowRecette.setLayoutParams(param);

                TextView textLibelle = new TextView(this);
                textLibelle.setText(recette.getLibelle());

                textLibelle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent infoRecette = new Intent(RecetteActivity.this, InfoRecetteActivity.class);
                        infoRecette.putExtra("recette",recette.getIdRecette());
                        startActivity(infoRecette);
                    }
                });

                ImageButton btnDeleteRecette = new ImageButton(this);
                btnDeleteRecette.setBackground(null);
                btnDeleteRecette.setImageResource(R.drawable.delete);

                btnDeleteRecette.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteRecette(recette);
                        tableRecette.removeView(rowRecette);
                    }
                });

                ImageButton btnAddToListe = new ImageButton(this);
                btnAddToListe.setBackground(null);
                btnAddToListe.setImageResource(R.drawable.add);

                btnAddToListe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popUpRecetteToListe(recette);
                    }
                });

                rowRecette.addView(textLibelle);
                rowRecette.addView(btnAddToListe);
                rowRecette.addView(btnDeleteRecette);

                tableRecette.addView(rowRecette);
            }
        }
    }

    public void popUpRecetteToListe(Recette recette){
        AlertDialog.Builder popUpAdd = new AlertDialog.Builder(RecetteActivity.this);

        popUpAdd.setTitle("Ajout de "+recette.getLibelle()+" à une liste");
        popUpAdd.setCancelable(true);

        LinearLayout linearLayout = new LinearLayout(RecetteActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);


        Spinner listeSpinner = new Spinner(this);
        List<ListeCourse> listeListeCourse = getAllListes();
        ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,listeListeCourse);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        listeSpinner.setAdapter(adapter2);

        Button btnAdd = new Button(RecetteActivity.this);
        btnAdd.setText("Ajouter la recette");

        linearLayout.addView(listeSpinner);
        linearLayout.addView(btnAdd);
        popUpAdd.setView(linearLayout);

        final AlertDialog alertDialog = popUpAdd.create();
        alertDialog.show();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListeCourse liste = (ListeCourse) listeSpinner.getSelectedItem();
                addToList(recette,liste);
                alertDialog.cancel();
                Snackbar.make(page, recette.getLibelle()+" a bien été ajouté à "+liste.getLibelle()+ " !", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void popUpRecette(){

        AlertDialog.Builder popUpEdit = new AlertDialog.Builder(RecetteActivity.this);

        popUpEdit.setTitle("Creation d'une recette");
        popUpEdit.setCancelable(true);

        LinearLayout linearLayout = new LinearLayout(RecetteActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);


        EditText editLibelle = new EditText(RecetteActivity.this);
        editLibelle.setHint("Saisir un libelle");

        Button btnCreate = new Button(RecetteActivity.this);
        btnCreate.setText("Créer la recette");

        linearLayout.addView(editLibelle);
        linearLayout.addView(btnCreate);

        popUpEdit.setView(linearLayout);
        final AlertDialog alertDialog = popUpEdit.create();
        alertDialog.show();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editLibelle.getText().toString().equals("")){
                    Recette recette = new Recette(editLibelle.getText().toString());
                    boolean create = createRecette(recette);
                    if(create){
                        alertDialog.cancel();
                        Snackbar.make(page, "La recette "+editLibelle.getText() +" a bien été créée !", Snackbar.LENGTH_LONG).show();
                        displayRecettes();
                    }
                    else{
                        InputMethodManager imm = (InputMethodManager) RecetteActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        Snackbar.make(page, "La recette "+editLibelle.getText() +" existe déjà, veuillez réessayer !", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void addToList(Recette recette, ListeCourse liste){
        ArrayList<Produit> listeProduitsRecette = InfoRecetteActivity.getProduitsByRecette(recette.getIdRecette(),this);

        if(listeProduitsRecette.size()>0){
            for(Produit produit : listeProduitsRecette){
                Produit_ListeCourse pl = checkIfAlreadyInListe(produit,liste);
                if(pl != null){
                    updateProduitListe(produit,liste,pl);
                }
                else{
                    insertProduitListe(produit,liste);
                }
            }
        }
    }

    public Produit_ListeCourse checkIfAlreadyInListe(Produit produit, ListeCourse liste){

        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produit_ListeCourse, Integer> daoProduitListe = linker.getDao( Produit_ListeCourse.class );

            QueryBuilder<Produit_ListeCourse, Integer> queryBuilder = daoProduitListe.queryBuilder();
            queryBuilder.where().eq("idListeCourse",liste.getIdListeCourse()).and()
                    .eq("idProduit",produit.getIdProduit()).and()
                    .eq("idTaille",produit.getTaille().getIdTaille());

            PreparedQuery<Produit_ListeCourse> preparedQuery = queryBuilder.prepare();

            List<Produit_ListeCourse> listeProduitListe= daoProduitListe.query(preparedQuery);

            if(!listeProduitListe.isEmpty()){
                return listeProduitListe.get(0);
            }
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
        return null;
    }

    public boolean createRecette(Recette newRecette){
        DataBaseLinker linker = new DataBaseLinker(this);
        boolean create = true;
        try {

            Dao<Recette, Integer> daoRecette = linker.getDao( Recette.class );

            for(Recette recette : getAllRecettes()){
                if(recette.getLibelle().equals(newRecette.getLibelle())){
                    create = false;
                }
            }
            if(create){
                daoRecette.create(newRecette);
            }
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
        return create;
    }

    public void deleteRecette(Recette recette){

        DataBaseLinker linker = new DataBaseLinker(this);
        try {

            Dao<Recette, Integer> daoRecette = linker.getDao( Recette.class );
            daoRecette.delete(recette);

            Dao<Produit_Recette, Integer> daoProduitR = linker.getDao(Produit_Recette.class);

            DeleteBuilder<Produit_Recette, Integer> deleteBuilder = daoProduitR.deleteBuilder();
            deleteBuilder.where().eq("idRecette",recette.getIdRecette());

            PreparedDelete<Produit_Recette> preparedDelete = deleteBuilder.prepare();

            daoProduitR.delete(preparedDelete);

        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
    }

    public void insertProduitListe(Produit produit, ListeCourse liste){

        DataBaseLinker linker = new DataBaseLinker(this);

        try {
            Dao<Produit_ListeCourse, Integer> daoProduitL = linker.getDao(Produit_ListeCourse.class);
            Produit_ListeCourse pl = new Produit_ListeCourse(produit,liste,produit.getTaille(),produit.getQuantite(),false);

            daoProduitL.create(pl);

        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();

    }

    public void updateProduitListe(Produit produit, ListeCourse liste, Produit_ListeCourse pl){
        DataBaseLinker linker = new DataBaseLinker(this);

        try {
            Dao<Produit_ListeCourse, Integer> daoProduitL = linker.getDao(Produit_ListeCourse.class);
            int quantite = produit.getQuantite() + pl.getQuantite();
            UpdateBuilder<Produit_ListeCourse,Integer> updateBuilder = daoProduitL.updateBuilder();
            updateBuilder.updateColumnValue("quantite",quantite).where().eq("idProduit",produit.getIdProduit())
                    .and().eq("idListeCourse",liste.getIdListeCourse())
                    .and().eq("idTaille",produit.getTaille().getIdTaille());
            PreparedUpdate preparedUpdate = updateBuilder.prepare();

            daoProduitL.update(preparedUpdate);

        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
    }
}

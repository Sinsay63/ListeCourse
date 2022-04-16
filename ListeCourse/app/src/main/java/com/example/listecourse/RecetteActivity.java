package com.example.listecourse;


import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.listecourse.dao.ListeCourse;
import com.example.listecourse.dao.Produit_ListeCourse;
import com.example.listecourse.dao.Produit_Recette;
import com.example.listecourse.dao.Recette;
import com.example.listecourse.tools.DataBaseLinker;
import com.google.android.material.snackbar.Snackbar;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;

import java.util.ArrayList;

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

                ImageButton deleteProduit = new ImageButton(this);
                deleteProduit.setBackground(null);
                deleteProduit.setImageResource(R.drawable.delete);

                deleteProduit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteRecette(recette);
                        tableRecette.removeView(rowRecette);
                    }
                });

                rowRecette.addView(textLibelle);
                rowRecette.addView(deleteProduit);
                tableRecette.addView(rowRecette);
            }
        }
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

}

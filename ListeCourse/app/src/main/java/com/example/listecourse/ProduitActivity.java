package com.example.listecourse;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.database.SQLException;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.listecourse.dao.ListeCourse;
import com.example.listecourse.dao.Produit;
import com.example.listecourse.dao.Produit_ListeCourse;
import com.example.listecourse.dao.Produit_Taille;
import com.example.listecourse.dao.Taille;
import com.example.listecourse.tools.DataBaseLinker;
import com.google.android.material.snackbar.Snackbar;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.util.ArrayList;
import java.util.List;

public class ProduitActivity extends AppCompatActivity {

    private ConstraintLayout page;
    private Button btnAddProduit;
    private TableLayout tableProduit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produits);

        btnAddProduit = findViewById(R.id.btnAddProduit);
        tableProduit = findViewById(R.id.tableProduit);
        page= findViewById(R.id.page);
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

    public List<Taille> getAllTaille(){
        DataBaseLinker linker = new DataBaseLinker(this);
        List<Taille> listeTaille = new ArrayList<>();
        try {

            Dao<Taille, Integer> daoTaille = linker.getDao(Taille.class );
            listeTaille = daoTaille.queryForAll();
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();

        return listeTaille;
    }

    public void deleteProduit(Produit produit){
        DataBaseLinker linker = new DataBaseLinker(this);

        try {

            Dao<Produit, Integer> daoProduit = linker.getDao(Produit.class );
            daoProduit.delete(produit);
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
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

            ImageButton editProduit = new ImageButton(this);
            editProduit.setLayoutParams(paramBtn);
            editProduit.setBackground(null);
            editProduit.setImageResource(R.drawable.edit);

            ImageButton deleteProduit = new ImageButton(this);
            deleteProduit.setLayoutParams(paramBtn);
            deleteProduit.setBackground(null);
            deleteProduit.setImageResource(R.drawable.delete);

            deleteProduit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteProduit(prod);
                    tableProduit.removeView(rowProduit);
                    Snackbar.make(page, "Le produit "+prod.getLibelle()+" a bien été supprimé !", Snackbar.LENGTH_LONG).show();
                }
            });
            editProduit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editProduit(prod);
                }
            });
            addProduit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Taille taille = (Taille) tailleSpiner.getSelectedItem();
                    addProduitList(prod,taille);
                }
            });


            rowProduit.addView(textLibelle);
            rowProduit.addView(tailleSpiner);
            rowProduit.addView(addProduit);
            rowProduit.addView(editProduit);
            rowProduit.addView(deleteProduit);

            tableProduit.addView(rowProduit);
        }
    }

    public void editProduit(Produit prod){
        AlertDialog.Builder popUpEdit = new AlertDialog.Builder(ProduitActivity.this);

        popUpEdit.setTitle("Edition du produit "+prod.getLibelle());
        popUpEdit.setCancelable(true);

        LinearLayout linearLayout = new LinearLayout(ProduitActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);

        TextView textLibelle = new TextView(ProduitActivity.this);
        textLibelle.setText("Libelle: ");
        textLibelle.setGravity(TextView.TEXT_ALIGNMENT_CENTER);

        EditText editLibelle = new EditText(ProduitActivity.this);
        editLibelle.setText(prod.getLibelle());

        Button btnUpdate = new Button(ProduitActivity.this);
        btnUpdate.setText("Mettre à jour");

        Spinner tailleSpiner = new Spinner(this);
        List<Taille> listeTailleProduit = getTailleProduit(prod);
        List<Taille> listeTaille = getAllTaille();

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,listeTaille);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tailleSpiner.setAdapter(adapter);

        linearLayout.addView(textLibelle);
        linearLayout.addView(editLibelle);
        linearLayout.addView(btnUpdate);
        linearLayout.addView(tailleSpiner);

        popUpEdit.setView(linearLayout);
        final AlertDialog alertDialog = popUpEdit.create();
        alertDialog.show();
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

    public void addProduitList(Produit prod,Taille taille){
        AlertDialog.Builder popUpEdit = new AlertDialog.Builder(ProduitActivity.this);

        popUpEdit.setTitle("Ajout de "+prod.getLibelle() + " à une liste.");
        popUpEdit.setCancelable(true);

        LinearLayout linearLayout = new LinearLayout(ProduitActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);

        TextView libelleQuantite = new TextView(ProduitActivity.this);
        libelleQuantite.setText("Saisir une quantité : ");

        EditText textQuantite = new EditText(ProduitActivity.this);
        textQuantite.setInputType(InputType.TYPE_CLASS_NUMBER);

        Spinner listeSpinner = new Spinner(this);
        List<ListeCourse> listeListeCourse = getAllListes();
        ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,listeListeCourse);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        listeSpinner.setAdapter(adapter2);

        Button btnUpdate = new Button(ProduitActivity.this);
        btnUpdate.setText("Ajouter à la liste");

        linearLayout.addView(libelleQuantite);
        linearLayout.addView(textQuantite);
        linearLayout.addView(listeSpinner);
        linearLayout.addView(btnUpdate);

        popUpEdit.setView(linearLayout);
        final AlertDialog alertDialog = popUpEdit.create();
        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantite = Integer.parseInt(textQuantite.getText().toString());
                ListeCourse listeCourse = (ListeCourse) listeSpinner.getSelectedItem();
                addToList(prod,listeCourse,taille,quantite);
                alertDialog.cancel();
            }
        });


    }

    public boolean addToList(Produit produit,ListeCourse listeCourse, Taille taille, int quantite){
        Produit_ListeCourse pl = new Produit_ListeCourse(produit,listeCourse,taille,quantite,false);
        boolean in = checkIfAlreadyInListe(pl);
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produit_ListeCourse, Integer> daoProduitListe = linker.getDao( Produit_ListeCourse.class );
            if(in){
                UpdateBuilder<Produit_ListeCourse,Integer> updateBuilder = daoProduitListe.updateBuilder();
                updateBuilder.updateColumnValue("quantite",quantite).where().eq("idProduit",produit.getIdProduit())
                        .and().eq("idListeCourse",listeCourse.getIdListeCourse())
                        .and().eq("idTaille",taille.getIdTaille());
                PreparedUpdate preparedUpdate = updateBuilder.prepare();

                daoProduitListe.update(preparedUpdate);
                Snackbar.make(page, "Le produit "+produit.getLibelle()+" a été mis à jour dans votre liste "+listeCourse.getLibelle()+" !", Snackbar.LENGTH_LONG).show();

            }
            else{
                daoProduitListe.create(pl);
                Snackbar.make(page, "Le produit "+produit.getLibelle()+" a bien été ajouté à votre liste "+listeCourse.getLibelle()+" !", Snackbar.LENGTH_LONG).show();

            }
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
        return in;
    }

    public boolean checkIfAlreadyInListe(Produit_ListeCourse pl){
        boolean in = false;

        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produit_ListeCourse, Integer> daoProduitListe = linker.getDao( Produit_ListeCourse.class );

            QueryBuilder<Produit_ListeCourse, Integer> queryBuilder = daoProduitListe.queryBuilder();
            queryBuilder.where().eq("idListeCourse",pl.getListeCourse().getIdListeCourse()).and()
            .eq("idProduit",pl.getProduit().getIdProduit()).and()
            .eq("idTaille",pl.getTaille().getIdTaille());

            PreparedQuery<Produit_ListeCourse> preparedQuery = queryBuilder.prepare();

            List<Produit_ListeCourse> listeProduitListe= daoProduitListe.query(preparedQuery);
            if(!listeProduitListe.isEmpty()){
                in=true;
            }
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
        return in;
    }
}
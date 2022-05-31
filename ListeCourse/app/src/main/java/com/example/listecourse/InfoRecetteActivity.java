package com.example.listecourse;

import static com.example.listecourse.ProduitActivity.getAllProduits;
import static com.example.listecourse.ProduitActivity.getTailleProduit;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.listecourse.dao.Recette;
import com.example.listecourse.dao.Produit;
import com.example.listecourse.dao.Produit_Recette;
import com.example.listecourse.dao.Taille;
import com.example.listecourse.tools.DataBaseLinker;
import com.google.android.material.snackbar.Snackbar;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;


import java.util.ArrayList;
import java.util.List;

public class InfoRecetteActivity extends AppCompatActivity {

    private ScrollView scrollProduitsRecette;
    private ScrollView scrollProduits;
    private ConstraintLayout page;
    private int idRecette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_recette);
        scrollProduitsRecette = findViewById(R.id.scrollProduitsRecette);
        scrollProduits = findViewById(R.id.scrollProduits);
        page = findViewById(R.id.page);

        Intent intent = this.getIntent();
        idRecette = intent.getIntExtra("recette",0);
        displayProduitRecette();
        displayProduits();

    }

    public static ArrayList<Produit> getProduitsByRecette(int idRecette, Context context){
        ArrayList<Produit_Recette> listeProduitsRecette = null;
        ArrayList<Produit> listeProduits = new ArrayList<>();

        DataBaseLinker linker = new DataBaseLinker(context);
        try {
            Dao<Produit_Recette, Integer> daoProduitR = linker.getDao( Produit_Recette.class );

            QueryBuilder<Produit_Recette, Integer> queryBuilder = daoProduitR.queryBuilder();
            queryBuilder.where().eq("idRecette",idRecette);

            PreparedQuery<Produit_Recette> preparedQuery = queryBuilder.prepare();

            listeProduitsRecette = (ArrayList<Produit_Recette>) daoProduitR.query(preparedQuery);

        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
        for (Produit_Recette prod : listeProduitsRecette){

            Produit produit = prod.getProduit();

            produit.setQuantite(prod.getQuantite());
            produit.setTaille(prod.getTaille());

            listeProduits.add(produit);
        }

        return listeProduits;
    }

    public Recette getRecetteById(int idRecette){
        Recette recette = null;

        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            if(idRecette>0) {
                Dao<Recette, Integer> daoRecette = linker.getDao(Recette.class);
                recette = daoRecette.queryForId(idRecette);
            }
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
        return recette;
    }


    public void deleteProduitFromRecette(Produit produit){
        DataBaseLinker linker = new DataBaseLinker(this);
        Recette recette = getRecetteById(idRecette);
        try {

            Dao<Produit_Recette, Integer> daoProduitRecette = linker.getDao( Produit_Recette.class );

            DeleteBuilder<Produit_Recette, Integer> deleteBuilder = daoProduitRecette.deleteBuilder();
            deleteBuilder.where().eq("idProduit",produit.getIdProduit()).and()
            .eq("idRecette",recette.getIdRecette());

            PreparedDelete<Produit_Recette> preparedDelete = deleteBuilder.prepare();

            daoProduitRecette.delete(preparedDelete);
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void displayProduits(){
        scrollProduits.removeAllViews();

        LinearLayout linearLayoutScroll = new LinearLayout(this);
        linearLayoutScroll.setOrientation(LinearLayout.VERTICAL);

        ArrayList<Produit> listeProduitsExistants = new ArrayList<>();
        ArrayList<Produit> listeProduits = getAllProduits(this);

        for (Produit produit : listeProduits){
            for(Produit produit1 : getProduitsByRecette(idRecette,this)){
                if(produit1.getLibelle().equals(produit.getLibelle())){
                    listeProduitsExistants.add(produit);
                }
            }
        }
        for(Produit produit : listeProduitsExistants){
            if(listeProduits.contains(produit)){
                listeProduits.remove(produit);
            }
        }

        if(listeProduits.size()>0) {
            for (Produit prod : listeProduits) {
                LinearLayout linearLayoutContenu = new LinearLayout(this);
                Spinner tailleSpiner = new Spinner(this);

                List<Taille> listeTaille = getTailleProduit(prod, this);

                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listeTaille);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tailleSpiner.setAdapter(adapter);

                LinearLayout linearLayoutText = new LinearLayout(this);
                linearLayoutText.setOrientation(LinearLayout.VERTICAL);
                linearLayoutText.setGravity(Gravity.CENTER_VERTICAL);

                RadioButton rb = new RadioButton(this);
                rb.setText(prod.getLibelle());

                EditText textQuantite = new EditText(this);
                textQuantite.setInputType(InputType.TYPE_CLASS_NUMBER);
                textQuantite.setHint("Qte");

                linearLayoutContenu.addView(rb);
                linearLayoutContenu.addView(tailleSpiner);
                linearLayoutContenu.addView(textQuantite);

                rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (!textQuantite.getText().toString().equals("")) {
                            Taille taille = (Taille) tailleSpiner.getSelectedItem();
                            int quantite = Integer.parseInt(textQuantite.getText().toString());

                            prod.setTaille(taille);
                            prod.setQuantite(quantite);
                            addProduitToRecette(prod);

                            linearLayoutScroll.removeView(linearLayoutContenu);
                            Snackbar.make(page, "Le produit " + prod.getLibelle() + " a bien été ajouté à la liste !", Snackbar.LENGTH_LONG).show();

                            displayProduits();
                            displayProduitRecette();
                        }
                        else {
                            rb.setChecked(false);
                            Snackbar.make(page, "Veuillez saisir une quantité ! ", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
                linearLayoutScroll.addView(linearLayoutContenu);
            }
            scrollProduits.addView(linearLayoutScroll);
        }
        else{
            LinearLayout linearLayoutText = new LinearLayout(this);
            linearLayoutText.setOrientation(LinearLayout.VERTICAL);
            linearLayoutText.setGravity(Gravity.CENTER_VERTICAL);

            TextView textProduit = new TextView(this);
            textProduit.setText("Tous les produits disponibles ont été ajoutés à la recette ! ");

            linearLayoutText.addView(textProduit);
            scrollProduits.addView(linearLayoutText);
        }
    }

    public void addProduitToRecette(Produit produit){

        DataBaseLinker linker = new DataBaseLinker(this);
        Recette recette = getRecetteById(idRecette);
        Produit_Recette pr = new Produit_Recette(recette,produit,produit.getTaille(),produit.getQuantite());
        try {
            Dao<Produit_Recette, Integer> daoProduitR = linker.getDao(Produit_Recette.class);
            daoProduitR.create(pr);
        }
            catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void displayProduitRecette() {
        scrollProduitsRecette.removeAllViews();

        LinearLayout linearLayoutScroll = new LinearLayout(this);
        linearLayoutScroll.setOrientation(LinearLayout.VERTICAL);

        ArrayList<Produit> listeProduitsRecette = getProduitsByRecette(idRecette,this);
        if (listeProduitsRecette.size() > 0) {
            for (Produit prod : listeProduitsRecette) {

                LinearLayout linearLayoutContenu = new LinearLayout(this);
                linearLayoutContenu.setGravity(Gravity.CENTER_VERTICAL);

                TextView textProduit = new TextView(this);
                textProduit.setText(prod.getLibelle() + " - " + prod.getTaille() + " - x" + prod.getQuantite());

                ImageButton deleteProduit = new ImageButton(this);
                deleteProduit.setBackground(null);
                deleteProduit.setImageResource(R.drawable.delete);

                deleteProduit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        linearLayoutScroll.removeView(linearLayoutContenu);
                        deleteProduitFromRecette(prod);
                        Snackbar.make(page, "Le produit " + prod.getLibelle() + " a bien été retiré !", Snackbar.LENGTH_LONG).show();
                        displayProduits();
                        displayProduitRecette();
                    }
                });

                linearLayoutContenu.addView(textProduit);
                linearLayoutContenu.addView(deleteProduit);
                linearLayoutScroll.addView(linearLayoutContenu);
            }
            scrollProduitsRecette.addView(linearLayoutScroll);
        }
        else{
            LinearLayout linearLayoutText = new LinearLayout(this);
            linearLayoutText.setOrientation(LinearLayout.VERTICAL);
            linearLayoutText.setGravity(Gravity.CENTER_VERTICAL);

            TextView textProduit = new TextView(this);
            textProduit.setText("Aucun produit n'a été ajouté à la recette ! ");

            linearLayoutText.addView(textProduit);
            scrollProduitsRecette.addView(linearLayoutText);
        }
    }
}

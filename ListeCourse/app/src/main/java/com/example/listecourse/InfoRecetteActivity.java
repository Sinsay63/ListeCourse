package com.example.listecourse;

import static com.example.listecourse.ProduitActivity.getAllProduits;
import static com.example.listecourse.ProduitActivity.getTailleProduit;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_recette);
        scrollProduitsRecette = findViewById(R.id.scrollProduitsRecette);
        scrollProduits = findViewById(R.id.scrollProduits);
        page = findViewById(R.id.page);

        displayProduitRecette();
        displayProduits();

    }

    public ArrayList<Produit> getProduitsByRecette(){
        Recette recette = getRecette();
        ArrayList<Produit_Recette> listeProduitsRecette = null;
        ArrayList<Produit> listeProduits = new ArrayList<>();

        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produit_Recette, Integer> daoProduitR = linker.getDao( Produit_Recette.class );

            QueryBuilder<Produit_Recette, Integer> queryBuilder = daoProduitR.queryBuilder();
            queryBuilder.where().eq("idRecette",recette.getIdRecette());

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

    public Recette getRecette(){
        Recette recette = null;

        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Intent intent = this.getIntent();
            int idListe = intent.getIntExtra("recette",0);
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


    public void deleteProduitFromRecette(Produit produit){
        DataBaseLinker linker = new DataBaseLinker(this);
        Recette recette = getRecette();
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

        ArrayList<Produit> listeProduitRestants = new ArrayList<>();

        for(Produit produit : getAllProduits(this)){
            for(Produit produit2 : getProduitsByRecette()){
                if(!produit.getLibelle().equals(produit2.getLibelle())){
                    listeProduitRestants.add(produit);
                }
            }
        }
        for(Produit prod : getAllProduits(this)){
            LinearLayout linearLayoutContenu = new LinearLayout(this);


            Spinner tailleSpiner = new Spinner(this);

            List<Taille> listeTaille = getTailleProduit(prod,this);

            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,listeTaille);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tailleSpiner.setAdapter(adapter);


            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
            );

            LinearLayout linearLayoutText = new LinearLayout(this);
            linearLayoutText.setOrientation(LinearLayout.VERTICAL);
            linearLayoutText.setGravity(Gravity.CENTER_VERTICAL);

            RadioButton rb = new RadioButton(this);
            rb.setText(prod.getLibelle());

            EditText textQuantite = new EditText(this);
            textQuantite.setInputType(InputType.TYPE_CLASS_NUMBER);
            textQuantite.setHint("Qte ");

            linearLayoutContenu.addView(rb);
            linearLayoutContenu.addView(tailleSpiner);
            linearLayoutContenu.addView(textQuantite);

            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if(!textQuantite.getText().toString().equals("")){
                        Taille taille = (Taille) tailleSpiner.getSelectedItem();
                        int quantite = Integer.parseInt(textQuantite.getText().toString());

                        prod.setTaille(taille);
                        prod.setQuantite(quantite);

                        addProduitToRecette(prod);
                        linearLayoutScroll.removeView(linearLayoutContenu);
                        Snackbar.make(page, "Le produit " + prod.getLibelle() + " a bien été ajouté à la liste !", Snackbar.LENGTH_LONG).show();
                        displayProduitRecette();
                    }
                    else{
                        rb.setChecked(false);
                        Snackbar.make(page, "Veuillez saisir une quantité ! ", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
            linearLayoutScroll.addView(linearLayoutContenu);
        }
        scrollProduits.addView(linearLayoutScroll);
    }

    public void addProduitToRecette(Produit produit){

        DataBaseLinker linker = new DataBaseLinker(this);
        Recette recette = getRecette();
        Produit_Recette pr = new Produit_Recette(recette,produit,produit.getTaille(),produit.getQuantite());
        try {
            Dao<Produit_Recette, Integer> daoProduitR = linker.getDao(Produit_Recette.class);
            daoProduitR.create(pr);
        }
            catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void displayProduitRecette(){
        scrollProduitsRecette.removeAllViews();

        LinearLayout linearLayoutScroll = new LinearLayout(this);
        linearLayoutScroll.setOrientation(LinearLayout.VERTICAL);

        for(Produit prod : getProduitsByRecette()){
            LinearLayout linearLayoutContenu = new LinearLayout(this);

            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
            );

            LinearLayout linearLayoutText = new LinearLayout(this);
            linearLayoutText.setOrientation(LinearLayout.VERTICAL);
            linearLayoutText.setGravity(Gravity.CENTER_VERTICAL);

            TextView textProduit = new TextView(this);
            textProduit.setText(prod.getLibelle()+" - "+prod.getTaille()+" - x"+prod.getQuantite());

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
                }
            });
            linearLayoutContenu.addView(textProduit);
            linearLayoutContenu.addView(deleteProduit);

            linearLayoutScroll.addView(linearLayoutContenu);
        }
        scrollProduitsRecette.addView(linearLayoutScroll);
    }
}

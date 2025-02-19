package com.example.listecourse;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.database.SQLException;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.listecourse.dao.ListeCourse;
import com.example.listecourse.dao.Produit;
import com.example.listecourse.dao.Produit_ListeCourse;
import com.example.listecourse.dao.Produit_Taille;
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


import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class ProduitActivity extends AppCompatActivity {

    private ConstraintLayout page;
    private Button btnAddProduit;
    private ScrollView scrollProduit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produits);

        btnAddProduit = findViewById(R.id.btnAddProduit);
        scrollProduit = findViewById(R.id.scrollProduits);
        page= findViewById(R.id.page);
        displayProduit();

        btnAddProduit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpCreateProduit();
            }
        });
    }

    public static ArrayList<Produit> getAllProduits(Context context){
        ArrayList<Produit> listeProduit = null;
        DataBaseLinker linker = new DataBaseLinker(context);

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

    public Produit createProduit(Produit produit){
        DataBaseLinker linker = new DataBaseLinker(this);

        try {
            Dao<Produit, Integer> daoProduit = linker.getDao(Produit.class );

            daoProduit.create(produit);
            int id = daoProduit.extractId(produit);
            produit.setIdProduit(id);

        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
        return produit;
    }

    public static List<Taille> getTailleProduit(Produit produit,Context context){
        DataBaseLinker linker = new DataBaseLinker(context);
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

            Dao<Produit_ListeCourse, Integer> daoProduitL = linker.getDao(Produit_ListeCourse.class);

            DeleteBuilder<Produit_ListeCourse, Integer> deleteBuilder = daoProduitL.deleteBuilder();
            deleteBuilder.where().eq("idProduit",produit.getIdProduit());

            PreparedDelete<Produit_ListeCourse> preparedDelete = deleteBuilder.prepare();

            daoProduitL.delete(preparedDelete);
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public void displayProduit(){
        scrollProduit.removeAllViews();
        LinearLayout linearLayoutScroll = new LinearLayout(this);
        linearLayoutScroll.setOrientation(LinearLayout.VERTICAL);

        for(Produit prod : getAllProduits(this)){
            LinearLayout linearLayoutContenu = new LinearLayout(this);
            linearLayoutContenu.setGravity(Gravity.CENTER_VERTICAL);

            LinearLayout.LayoutParams paramSpinner = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
            );
            paramSpinner.setMargins(0,5,0,10);

            Spinner tailleSpiner = new Spinner(this);
            tailleSpiner.setLayoutParams(paramSpinner);

            TextView textLibelle = new TextView(this);
            textLibelle.setLayoutParams(paramSpinner);
            textLibelle.setText(prod.getLibelle());
            textLibelle.setTextSize(18);

            List<Taille> listeTaille = getTailleProduit(prod,this);

            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,listeTaille);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tailleSpiner.setAdapter(adapter);

                ImageButton addProduit = new ImageButton(this);
                addProduit.setBackground(null);
                addProduit.setImageResource(R.drawable.add);

                ImageButton editProduit = new ImageButton(this);
                editProduit.setBackground(null);
                editProduit.setImageResource(R.drawable.edit);

                ImageButton deleteProduit = new ImageButton(this);
                deleteProduit.setBackground(null);
                deleteProduit.setImageResource(R.drawable.delete);

                deleteProduit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteProduit(prod);
                        linearLayoutScroll.removeView(linearLayoutContenu);
                        Snackbar.make(page, "Le produit " + prod.getLibelle() + " a bien été supprimé !", Snackbar.LENGTH_LONG).show();
                    }
                });
                editProduit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popUpEditProduit(prod);
                    }
                });
                addProduit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Taille taille = (Taille) tailleSpiner.getSelectedItem();
                        popUpProduitInListe(prod, taille);
                    }
                });

            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
            );
            param2.width=300;
            param2.setMargins(0,0,0,0);

            LinearLayout linearLayoutText = new LinearLayout(this);
            linearLayoutText.setOrientation(LinearLayout.VERTICAL);
            linearLayoutText.setLayoutParams(paramSpinner);

            tailleSpiner.setLayoutParams(param2);

            linearLayoutText.addView(textLibelle);
            linearLayoutText.addView(tailleSpiner);

            linearLayoutContenu.addView(linearLayoutText);
            if(getAllListes().size()>0) {
                linearLayoutContenu.addView(addProduit);
            }
            linearLayoutContenu.addView(editProduit);
            linearLayoutContenu.addView(deleteProduit);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
            );
            param.setMargins(0,10,0,10);
            linearLayoutContenu.setLayoutParams(param);
            linearLayoutScroll.addView(linearLayoutContenu);
        }
        scrollProduit.addView(linearLayoutScroll);
    }

    public void popUpEditProduit(Produit prod){
        AlertDialog.Builder popUpEdit = new AlertDialog.Builder(ProduitActivity.this);
        TextView titrePop = new TextView(this);
        titrePop.setText("Edition du produit "+prod.getLibelle());
        titrePop.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        titrePop.setTextSize(25);
        titrePop.setTypeface(null,Typeface.BOLD);
        titrePop.setPaintFlags(titrePop.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        popUpEdit.setCustomTitle(titrePop);
        popUpEdit.setCancelable(true);

        LinearLayout linearLayout = new LinearLayout(ProduitActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        EditText editLibelle = new EditText(ProduitActivity.this);
        editLibelle.setText(prod.getLibelle());

        TextView textTaille = new TextView(ProduitActivity.this);
        textTaille.setText("Sélectionner une taille minimum :");



        List<Taille> listeTailleProduit = getTailleProduit(prod,this);

        ScrollView scrollTaille = new ScrollView(this);

        ScrollView.LayoutParams param = new ScrollView.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        param.height=350;
        scrollTaille.setLayoutParams(param);
        scrollTaille.setScrollbarFadingEnabled(false);

        Button btnUpdate = new Button(ProduitActivity.this);
        btnUpdate.setText("Mettre à jour");

        LinearLayout linearLayoutScroll = new LinearLayout(this);
        linearLayoutScroll.setOrientation(LinearLayout.VERTICAL);
        ArrayList<CheckBox> listeCheckBox = new ArrayList<>();

        for(Taille taille : getAllTaille()){
            CheckBox cbTaille = new CheckBox(this);
            cbTaille.setText(taille.getLibelle());
            for(Taille tailleProduit : listeTailleProduit){
                if(taille.getLibelle().equals(tailleProduit.getLibelle())){
                    cbTaille.setChecked(true);
                }
            }
            linearLayoutScroll.addView(cbTaille);
            listeCheckBox.add(cbTaille);
        }

        scrollTaille.addView(linearLayoutScroll);
        linearLayout.addView(editLibelle);
        linearLayout.addView(textTaille);
        linearLayout.addView(scrollTaille);
        linearLayout.addView(btnUpdate);

        popUpEdit.setView(linearLayout);
        final AlertDialog alertDialog = popUpEdit.create();
        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nbChecked = 0;
                String libelle = editLibelle.getText().toString();

                boolean existLibelle = checkExistLibelleProduit(libelle);
                if (existLibelle) {
                    InputMethodManager imm = (InputMethodManager) ProduitActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Snackbar.make(page, "Ce nom de produit existe déjà ! ", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    libelle = libelle.substring(0, 1).toUpperCase() + libelle.substring(1).toLowerCase();
                    prod.setLibelle(libelle);

                    for (CheckBox checkBox : listeCheckBox) {
                        if (checkBox.isChecked()) {
                            nbChecked++;
                        }
                    }
                    if (nbChecked > 0) {
                        editProduit(prod, listeCheckBox);
                        displayProduit();
                        alertDialog.cancel();
                    } else {
                        InputMethodManager imm = (InputMethodManager) ProduitActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        Snackbar.make(page, "Aucune taille n'a été sélectionnée ! ", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void popUpCreateProduit(){

        AlertDialog.Builder popUpCreate = new AlertDialog.Builder(ProduitActivity.this);

        TextView titrePop = new TextView(this);
        titrePop.setText("Création d'un produit");
        titrePop.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        titrePop.setTextSize(25);
        titrePop.setTypeface(null,Typeface.BOLD);
        titrePop.setPaintFlags(titrePop.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        popUpCreate.setCustomTitle(titrePop);
        popUpCreate.setCancelable(true);

        LinearLayout linearLayout = new LinearLayout(ProduitActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        EditText editLibelle = new EditText(ProduitActivity.this);
        editLibelle.setHint("Saisir un libelle");

        ScrollView.LayoutParams paramLibelle = new ScrollView.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        paramLibelle.setMargins(0,25,0,15);
        editLibelle.setLayoutParams(paramLibelle);

        TextView textTaille = new TextView(ProduitActivity.this);
        textTaille.setText("Sélectionner une taille minimum :");

        ScrollView scrollTaille = new ScrollView(this);
        LinearLayout linearLayoutScroll = new LinearLayout(this);
        linearLayoutScroll.setOrientation(LinearLayout.VERTICAL);
        scrollTaille.setScrollbarFadingEnabled(false);

        ArrayList<CheckBox> listeCheckBox = new ArrayList<>();

        for(Taille taille : getAllTaille()){
            CheckBox cbTaille = new CheckBox(this);
            cbTaille.setText(taille.getLibelle());
            linearLayoutScroll.addView(cbTaille);
            listeCheckBox.add(cbTaille);
        }

        scrollTaille.addView(linearLayoutScroll);

        ScrollView.LayoutParams param = new ScrollView.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        param.height=333;
        scrollTaille.setLayoutParams(param);
        Button btnCreate = new Button(ProduitActivity.this);
        btnCreate.setText("Créer le produit");

        linearLayout.addView(editLibelle);
        linearLayout.addView(textTaille);
        linearLayout.addView(scrollTaille);
        linearLayout.addView(btnCreate);

        popUpCreate.setView(linearLayout);
        final AlertDialog alertDialog = popUpCreate.create();
        alertDialog.show();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String libelle = editLibelle.getText().toString();
                boolean existLibelle = false;

                //enlève les accents et lowercase
                String newLibelle = Normalizer.normalize(libelle, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();

                //mets la première lettre en maj
                libelle = libelle.substring(0, 1).toUpperCase() + libelle.substring(1).toLowerCase();

                for(Produit existProduit : getAllProduits(ProduitActivity.this)){
                    String oldLibelle = Normalizer.normalize(existProduit.getLibelle(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
                    if(oldLibelle.equals(newLibelle)){
                        existLibelle=true;
                        InputMethodManager imm = (InputMethodManager) ProduitActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        Snackbar.make(page, "Ce nom de produit existe déjà ! ", Snackbar.LENGTH_SHORT).show();
                        break;
                    }
                }

                if(!libelle.equals("") && !existLibelle) {
                    Produit produit = new Produit(libelle);
                    int nbChecked = 0;
                    for (CheckBox checkBox : listeCheckBox) {
                        if (checkBox.isChecked()) {
                            nbChecked++;
                        }
                    }

                    if (nbChecked > 0) {
                        produit = createProduit(produit);
                        createProduitTaille(produit,listeCheckBox);
                        displayProduit();
                        alertDialog.cancel();
                    }
                    else {
                        InputMethodManager imm = (InputMethodManager) ProduitActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        Snackbar.make(page, "Aucune taille n'a été sélectionnée ! ", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
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

    public void popUpProduitInListe(Produit prod,Taille taille){
        AlertDialog.Builder popUpAdd = new AlertDialog.Builder(ProduitActivity.this);

        TextView titrePop = new TextView(this);
        titrePop.setText("Ajout de "+prod.getLibelle() + " à une liste.");
        titrePop.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        titrePop.setTextSize(25);
        titrePop.setTypeface(null,Typeface.BOLD);
        titrePop.setPaintFlags(titrePop.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        popUpAdd.setCustomTitle(titrePop);
        popUpAdd.setCancelable(true);

        LinearLayout linearLayout = new LinearLayout(ProduitActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);


        EditText editQuantite = new EditText(ProduitActivity.this);
        editQuantite.setInputType(InputType.TYPE_CLASS_NUMBER);
        editQuantite.setHint("Saisir une quantité ");

        ScrollView.LayoutParams paramLibelle = new ScrollView.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        paramLibelle.setMargins(0,25,0,15);
        editQuantite.setLayoutParams(paramLibelle);



        TextView textSpinner = new TextView(this);
        textSpinner.setText("Sélectionner une liste de courses :");

        Spinner listeSpinner = new Spinner(this);
        List<ListeCourse> listeListeCourse = getAllListes();
        ArrayAdapter adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,listeListeCourse);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        listeSpinner.setAdapter(adapter2);
        listeSpinner.setPadding(0,10,0,25);

        Button btnUpdate = new Button(ProduitActivity.this);
        btnUpdate.setText("Ajouter à la liste");

        linearLayout.addView(editQuantite);
        linearLayout.addView(textSpinner);
        linearLayout.addView(listeSpinner);
        linearLayout.addView(btnUpdate);

        popUpAdd.setView(linearLayout);
        final AlertDialog alertDialog = popUpAdd.create();
        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantite = Integer.parseInt(editQuantite.getText().toString());
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

    public Taille getTailleByLibelle(String libelle){
        DataBaseLinker linker = new DataBaseLinker(this);
        List<Taille> taille = null;
        try {
            Dao<Taille, Integer> daoTaille = linker.getDao(Taille.class );
            QueryBuilder<Taille,Integer> queryBuilder = daoTaille.queryBuilder();
            queryBuilder.where().eq("libelle",libelle);
            PreparedQuery<Taille> preparedQuery = queryBuilder.prepare();
            taille = daoTaille.query(preparedQuery);

        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
        return taille.get(0);
    }

    public void editProduit(Produit produit,ArrayList<CheckBox> listeCheckbox){
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produit, Integer> daoProduit = linker.getDao( Produit.class );
            daoProduit.update(produit);

            Dao<Produit_Taille, Integer> daoProduitTaille = linker.getDao( Produit_Taille.class );

            DeleteBuilder<Produit_Taille, Integer> deleteBuilder = daoProduitTaille.deleteBuilder();
            deleteBuilder.where().eq("idProduit",produit.getIdProduit());

            PreparedDelete<Produit_Taille> preparedDelete = deleteBuilder.prepare();
            daoProduitTaille.delete(preparedDelete);

            for(CheckBox cb : listeCheckbox){
                Taille taille = getTailleByLibelle(cb.getText().toString());
                if(cb.isChecked()){
                    Produit_Taille pt = new Produit_Taille(produit,taille);
                    daoProduitTaille.create(pt);
                }
            }
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
    }

    public void createProduitTaille(Produit produit, ArrayList<CheckBox> listeCheckbox){
        DataBaseLinker linker = new DataBaseLinker(this);
        try {
            Dao<Produit_Taille, Integer> daoProduitTaille = linker.getDao( Produit_Taille.class );

            for(CheckBox cb : listeCheckbox){
                if(cb.isChecked()){
                    Taille taille = getTailleByLibelle(cb.getText().toString());
                    Produit_Taille pt = new Produit_Taille(produit,taille);
                    daoProduitTaille.create(pt);
                }
            }
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }
        linker.close();
    }

    public boolean checkExistLibelleProduit(String libelle){
        boolean exist = false;
        String newLibelle = Normalizer.normalize(libelle, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();

        for(Produit existProduit : getAllProduits(ProduitActivity.this)){
            String oldLibelle = Normalizer.normalize(existProduit.getLibelle(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
            if(oldLibelle.equals(newLibelle)){
                exist=true;
                break;
            }
        }
        return exist;
    }
}
package com.example.listecourse;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
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

public class ListeActivity extends AppCompatActivity {

    private TableLayout tableListe;
    private Button btnCreateListe;
    private ConstraintLayout page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listes);
        tableListe = findViewById(R.id.tableListe);
        btnCreateListe = findViewById(R.id.btnCreateListe);
        page = findViewById(R.id.page);

        btnCreateListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpListe();
            }
        });
        displayListes();
    }

    public ArrayList<ListeCourse> getAllListes(){
        ArrayList<ListeCourse> listeListeCourses = null;

        DataBaseLinker linker = new DataBaseLinker(this);
        try {

            Dao<ListeCourse, Integer> daoListe = linker.getDao( ListeCourse.class );
            listeListeCourses  = (ArrayList<ListeCourse>) daoListe.queryForAll();
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
        return listeListeCourses;
    }

    public void displayListes(){
        tableListe.removeAllViews();
        ArrayList<ListeCourse> listeCourses = getAllListes();
        if(listeCourses != null) {
            for (ListeCourse liste : listeCourses) {

                TableRow.LayoutParams param = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        4f
                );

                TableRow rowListe = new TableRow(this);
                rowListe.setLayoutParams(param);
                rowListe.setGravity(Gravity.CENTER_VERTICAL);

                TextView textLibelle = new TextView(this);
                textLibelle.setText("• "+liste.getLibelle());
                textLibelle.setTextSize(20);
                textLibelle.setTextColor(Color.parseColor("#000000"));;

                textLibelle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent infoListe = new Intent(ListeActivity.this, InfoListeActivity.class);
                        infoListe.putExtra("liste",liste.getIdListeCourse());

                        startActivity(infoListe);
                    }
                });

                ImageButton deleteProduit = new ImageButton(this);
                deleteProduit.setBackground(null);
                deleteProduit.setImageResource(R.drawable.delete);

                deleteProduit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteListe(liste);
                        tableListe.removeView(rowListe);
                    }
                });

                rowListe.addView(textLibelle);
                rowListe.addView(deleteProduit);
                tableListe.addView(rowListe);
            }
        }
    }
    public void popUpListe(){

        AlertDialog.Builder popUpEdit = new AlertDialog.Builder(ListeActivity.this);

        popUpEdit.setTitle("Creation d'une liste");
        popUpEdit.setCancelable(true);

        LinearLayout linearLayout = new LinearLayout(ListeActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);


        EditText editLibelle = new EditText(ListeActivity.this);
        editLibelle.setHint("Saisir un libelle");

        Button btnCreate = new Button(ListeActivity.this);
        btnCreate.setText("Créer la liste");

        linearLayout.addView(editLibelle);
        linearLayout.addView(btnCreate);

        popUpEdit.setView(linearLayout);
        final AlertDialog alertDialog = popUpEdit.create();
        alertDialog.show();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editLibelle.getText().toString().equals("")){
                    ListeCourse liste = new ListeCourse(editLibelle.getText().toString());
                    boolean create = createListe(liste);
                    if(create){
                        alertDialog.cancel();
                        Snackbar.make(page, "La liste "+editLibelle.getText() +" a bien été créée !", Snackbar.LENGTH_LONG).show();
                        displayListes();
                    }
                    else{
                        InputMethodManager imm = (InputMethodManager) ListeActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        Snackbar.make(page, "La liste "+editLibelle.getText() +" existe déjà, veuillez réessayer !", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public boolean createListe(ListeCourse liste){
        DataBaseLinker linker = new DataBaseLinker(this);
        boolean create = true;
        try {

            Dao<ListeCourse, Integer> daoListe = linker.getDao( ListeCourse.class );

            for(ListeCourse listeCourse : getAllListes()){
                if(listeCourse.getLibelle().equals(liste.getLibelle())){
                    create = false;
                }
            }

            if(create){
                daoListe.create(liste);
            }
        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
        return create;
    }

    public void deleteListe(ListeCourse liste){

        DataBaseLinker linker = new DataBaseLinker(this);
        try {

            Dao<ListeCourse, Integer> daoListe = linker.getDao( ListeCourse.class );
            daoListe.delete(liste);

            Dao<Produit_ListeCourse, Integer> daoProduitL = linker.getDao(Produit_ListeCourse.class);

            DeleteBuilder<Produit_ListeCourse, Integer> deleteBuilder = daoProduitL.deleteBuilder();
            deleteBuilder.where().eq("idListeCourse",liste.getIdListeCourse());

            PreparedDelete<Produit_ListeCourse> preparedDelete = deleteBuilder.prepare();

            daoProduitL.delete(preparedDelete);

        }
        catch (SQLException | java.sql.SQLException throwables) {
            throwables.printStackTrace();
        }

        linker.close();
    }
}

package com.example.listecourse;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.listecourse.dao.ListeCourse;
import com.example.listecourse.dao.Produit;
import com.example.listecourse.dao.Produit_ListeCourse;
import com.example.listecourse.dao.Taille;
import com.example.listecourse.tools.DataBaseLinker;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class ListeActivity extends AppCompatActivity {

    private TableLayout tableListe;
    private Button btnCreateListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listes);
        tableListe = findViewById(R.id.tableListe);
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

                TextView textLibelle = new TextView(this);
                textLibelle.setText(liste.getLibelle());

                textLibelle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent infoListe = new Intent(ListeActivity.this, InfoListeActivity.class);
                        infoListe.putExtra("liste",liste.getIdListeCourse());

                        startActivity(infoListe);
                    }
                });
                rowListe.addView(textLibelle);

                tableListe.addView(rowListe);
            }
        }
    }
}
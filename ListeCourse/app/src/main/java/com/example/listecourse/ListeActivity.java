package com.example.listecourse;

import android.database.SQLException;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

                TextView textLibelle = new TextView(this);
                textLibelle.setText(liste.getLibelle());

                rowListe.addView(textLibelle);

                tableListe.addView(rowListe);
            }
        }
    }
}
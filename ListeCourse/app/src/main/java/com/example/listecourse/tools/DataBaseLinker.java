package com.example.listecourse.tools;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.example.listecourse.dao.ListeCourse;
import com.example.listecourse.dao.Produit;
import com.example.listecourse.dao.Produit_ListeCourse;
import com.example.listecourse.dao.Produit_Recette;
import com.example.listecourse.dao.Produit_Taille;
import com.example.listecourse.dao.Recette;
import com.example.listecourse.dao.Taille;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DataBaseLinker extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "listeCourse.db";
    private static final int DATABASE_VERSION = 1;

    public DataBaseLinker( Context context ) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            TableUtils.createTable( connectionSource, Produit.class );
            TableUtils.createTable( connectionSource, Taille.class );
            TableUtils.createTable( connectionSource, Recette.class );
            TableUtils.createTable( connectionSource, ListeCourse.class );
            TableUtils.createTable( connectionSource, Produit_Taille.class );
            TableUtils.createTable( connectionSource, Produit_Recette.class );
            TableUtils.createTable( connectionSource, Produit_ListeCourse.class );

            Dao<Produit, Integer> daoProduit = this.getDao(Produit.class);
            Dao<ListeCourse, Integer> daoListe = this.getDao(ListeCourse.class);
            Dao<Produit_ListeCourse, Integer> daoProduitListe = this.getDao(Produit_ListeCourse.class);
            Dao<Taille, Integer> daoTaille = this.getDao(Taille.class);
            Dao<Produit_Taille, Integer> daoProduitTaille = this.getDao(Produit_Taille.class);

            ListeCourse liste = new ListeCourse();
            liste.setLibelle("SHEEEEESH");

            Produit chocolat = new Produit();
            chocolat.setLibelle("Chocolat");

            Produit brioche = new Produit();
            brioche.setLibelle("Brioche");

            Taille t33cl = new Taille("33cl");
            Taille t50cl = new Taille("50cl");
            Taille t70cl = new Taille("70cl");
            Taille t50g = new Taille("50g");
            Taille t100g = new Taille("100g");
            Taille t250g = new Taille("250g");
            Taille t500g = new Taille("500g");
            Taille t1kg = new Taille("1kg");

            Produit_Taille pt = new Produit_Taille();
            pt.setProduit(chocolat);
            pt.setTaille(t1kg);

            Produit_Taille pt2 = new Produit_Taille();
            pt2.setProduit(brioche);
            pt2.setTaille(t250g);

            Produit_Taille pt3 = new Produit_Taille();
            pt3.setProduit(brioche);
            pt3.setTaille(t500g);

            Produit_ListeCourse pl1 = new Produit_ListeCourse();
            pl1.setProduit(chocolat);
            pl1.setListeCourse(liste);
            pl1.setQuantite(4);
            pl1.setTaille(t1kg);

            Produit_ListeCourse pl2 = new Produit_ListeCourse();
            pl2.setProduit(brioche);
            pl2.setListeCourse(liste);
            pl2.setQuantite(1);
            pl2.setTaille(t500g);

            daoListe.create(liste);

            daoProduit.create(chocolat);
            daoProduit.create(brioche);

            daoTaille.create(t50g);
            daoTaille.create(t33cl);
            daoTaille.create(t50cl);
            daoTaille.create(t70cl);
            daoTaille.create(t100g);
            daoTaille.create(t250g);
            daoTaille.create(t500g);
            daoTaille.create(t1kg);

            daoProduitTaille.create(pt);
            daoProduitTaille.create(pt2);
            daoProduitTaille.create(pt3);

            daoProduitListe.create(pl1);
            daoProduitListe.create(pl2);


            Log.i( "DATABASE", "onCreate invoked" );
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't create Database", exception );
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i( "DATABASE", "onUpgrade invoked" );
        } catch( Exception exception ) {
            Log.e( "DATABASE", "Can't upgrade Database", exception );
        }
    }

}

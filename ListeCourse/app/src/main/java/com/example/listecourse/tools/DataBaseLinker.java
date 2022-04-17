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
import com.j256.ormlite.stmt.query.In;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.util.prefs.PreferenceChangeEvent;

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
            Dao<Recette, Integer> daoRecette = this.getDao(Recette.class);
            Dao<Produit_Recette, Integer> daoProduitRecette = this.getDao(Produit_Recette.class);

            ListeCourse liste = new ListeCourse("Course de Vendredi");
            Recette crepe = new Recette("Pâte à crêpe");

            Produit oeufs = new Produit("Oeufs");
            Produit farine = new Produit("Farine");
            Produit beurre = new Produit("Beurre");
            Produit lait = new Produit("Lait");
            Produit sucre = new Produit("Sucre");
            Produit cocacola = new Produit("Canette coca cola");

            Taille t33cl = new Taille("33cL");
            Taille t50cl = new Taille("50cL");
            Taille t70cl = new Taille("70cL");
            Taille t1l = new Taille("1cL");
            Taille t50g = new Taille("50g");
            Taille t100g = new Taille("100g");
            Taille t250g = new Taille("250g");
            Taille t500g = new Taille("500g");
            Taille t1kg = new Taille("1kg");
            Taille t2kg = new Taille("2kg");
            Taille t5kg = new Taille("5kg");
            Taille t1pc = new Taille("1pc");
            Taille t5pc = new Taille("5pc");
            Taille t10pc = new Taille("10pc");



            Produit_Taille pt6 = new Produit_Taille();
            pt6.setProduit(farine);
            pt6.setTaille(t1kg);

            Produit_Taille pt7 = new Produit_Taille();
            pt7.setProduit(beurre);
            pt7.setTaille(t100g);

            Produit_Taille pt8 = new Produit_Taille();
            pt8.setProduit(beurre);
            pt8.setTaille(t250g);

            Produit_Taille pt9 = new Produit_Taille();
            pt9.setProduit(sucre);
            pt9.setTaille(t500g);

            Produit_Taille pt10 = new Produit_Taille();
            pt10.setProduit(cocacola);
            pt10.setTaille(t33cl);

            Produit_Taille pt11 = new Produit_Taille();
            pt11.setProduit(cocacola);
            pt11.setTaille(t50cl);


            Produit_Taille pt13 = new Produit_Taille();
            pt13.setProduit(lait);
            pt13.setTaille(t50cl);

            Produit_Taille pt14 = new Produit_Taille();
            pt14.setProduit(lait);
            pt14.setTaille(t1l);

            Produit_Taille pt15 = new Produit_Taille();
            pt15.setProduit(oeufs);
            pt15.setTaille(t10pc);

            Produit_Taille pt16 = new Produit_Taille();
            pt16.setProduit(sucre);
            pt16.setTaille(t250g);

            Produit_ListeCourse pl1 = new Produit_ListeCourse();
            pl1.setListeCourse(liste);
            pl1.setQuantite(2);
            pl1.setTaille(t1kg);

            Produit_ListeCourse pl2 = new Produit_ListeCourse();
            pl2.setProduit(cocacola);
            pl2.setListeCourse(liste);
            pl2.setQuantite(5);
            pl2.setTaille(t50cl);


            Produit_Recette pr = new Produit_Recette();
            pr.setProduit(oeufs);
            pr.setRecette(crepe);
            pr.setQuantite(4);
            pr.setTaille(t1pc);

            Produit_Recette pr2 = new Produit_Recette();
            pr.setProduit(farine);
            pr.setRecette(crepe);
            pr.setQuantite(5);
            pr.setTaille(t50cl);

            Produit_Recette pr3 = new Produit_Recette();
            pr.setProduit(beurre);
            pr.setRecette(crepe);
            pr.setQuantite(1);
            pr.setTaille(t100g);

            Produit_Recette pr4 = new Produit_Recette();
            pr.setProduit(lait);
            pr.setRecette(crepe);
            pr.setQuantite(1);
            pr.setTaille(t50cl);

            Produit_Recette pr5 = new Produit_Recette();
            pr.setProduit(sucre);
            pr.setRecette(crepe);
            pr.setQuantite(1);
            pr.setTaille(t250g);

            daoListe.create(liste);
            daoRecette.create(crepe);

            daoProduit.create(farine);
            daoProduit.create(lait);
            daoProduit.create(sucre);
            daoProduit.create(cocacola);
            daoProduit.create(beurre);
            daoProduit.create(oeufs);

            daoTaille.create(t33cl);
            daoTaille.create(t50cl);
            daoTaille.create(t70cl);
            daoTaille.create(t50g);
            daoTaille.create(t100g);
            daoTaille.create(t250g);
            daoTaille.create(t500g);
            daoTaille.create(t1kg);
            daoTaille.create(t2kg);
            daoTaille.create(t5kg);
            daoTaille.create(t1l);
            daoTaille.create(t1pc);
            daoTaille.create(t5pc);
            daoTaille.create(t10pc);

            daoProduitTaille.create(pt6);
            daoProduitTaille.create(pt7);
            daoProduitTaille.create(pt8);
            daoProduitTaille.create(pt9);
            daoProduitTaille.create(pt10);
            daoProduitTaille.create(pt11);
            daoProduitTaille.create(pt13);
            daoProduitTaille.create(pt14);
            daoProduitTaille.create(pt15);
            daoProduitTaille.create(pt16);

            daoProduitListe.create(pl1);
            daoProduitListe.create(pl2);

            daoProduitRecette.create(pr);
            daoProduitRecette.create(pr2);
            daoProduitRecette.create(pr3);
            daoProduitRecette.create(pr4);
            daoProduitRecette.create(pr5);



            Log.i( "DATABASE", "onCreate invoked" );
        }
        catch( Exception exception ) {
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

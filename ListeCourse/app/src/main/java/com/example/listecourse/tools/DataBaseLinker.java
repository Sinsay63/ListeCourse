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
import com.example.listecourse.dao.Unite;
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
            TableUtils.createTable( connectionSource, Unite.class );
            TableUtils.createTable( connectionSource, Recette.class );
            TableUtils.createTable( connectionSource, ListeCourse.class );
            TableUtils.createTable( connectionSource, Produit_Taille.class );
            TableUtils.createTable( connectionSource, Produit_Recette.class );
            TableUtils.createTable( connectionSource, Produit_ListeCourse.class );

            Dao<Produit, Integer> daoProduit = this.getDao(Produit.class);
            Dao<Unite, Integer> daoUnite = this.getDao(Unite.class);
            Unite kg = new Unite();
            kg.setLibelle("kg");
            daoUnite.create(kg);
            daoProduit.create(new Produit("Chocolat",kg));

            //Dao<Client, Integer> daoClient = this.getDao( Client.class );
            //daoClient.create(client1);

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

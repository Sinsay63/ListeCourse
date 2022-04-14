package com.example.listecourse.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Taille")
public class Taille {

    @DatabaseField(columnName = "idTaille", generatedId = true)
    private int idTaille;

    @DatabaseField(columnName = "libelle")
    private String libelle;

    public Taille(String libelle) {
        this.libelle = libelle;
    }
    public Taille(){

    }

    public int getIdTaille() {
        return idTaille;
    }

    public void setIdTaille(int idTaille) {
        this.idTaille = idTaille;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return this.libelle;
    }
}

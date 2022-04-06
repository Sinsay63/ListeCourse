package com.example.listecourse.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Recette")
public class Recette {

    @DatabaseField( columnName="idRecette", generatedId = true)
    private int idRecette;

    @DatabaseField( columnName="libelle")
    private String libelle;


    public Recette(String libelle) {
        this.libelle = libelle;
    }

    public Recette(){

    }

    public int getIdRecette() {
        return idRecette;
    }

    public void setIdRecette(int idRecette) {
        this.idRecette = idRecette;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}

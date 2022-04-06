package com.example.listecourse.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Unite")
public class Unite {

@DatabaseField(columnName = "libelle", id= true)
    private String libelle;

    public Unite(String libelle) {
        this.libelle = libelle;
    }

    public Unite(){

    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}

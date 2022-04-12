package com.example.listecourse.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Produit")
public class Produit {

    @DatabaseField( columnName="idProduit", generatedId=true)
    private int idProduit;

    @DatabaseField( columnName="libelle")
    private String libelle;


    public Produit(String libelle) {
        this.libelle = libelle;
    }

    public Produit(){

    }

    public int getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

}

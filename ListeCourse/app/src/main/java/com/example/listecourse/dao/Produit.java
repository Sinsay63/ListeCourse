package com.example.listecourse.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Produit")
public class Produit {

    @DatabaseField( columnName="idProduit", generatedId=true)
    private int idProduit;

    @DatabaseField( columnName="libelle")
    private String libelle;

    @DatabaseField (
            columnName = "libelleUnite",
            foreign = true,
            foreignAutoRefresh = true,
            canBeNull = false,
            index = true,
            columnDefinition = "VARCHAR CONSTRAINT FK_Produit_Unite REFERENCES parent(libelle) ON DELETE CASCADE"
    )
    private Unite unite;

    public Produit(String libelle, Unite unite) {
        this.libelle = libelle;
        this.unite = unite;
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

    public Unite getUnite() {
        return unite;
    }

    public void setUnite(Unite unite) {
        this.unite = unite;
    }
}

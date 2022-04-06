package com.example.listecourse.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Produit_ListeCourse")
public class Produit_ListeCourse {

    @DatabaseField (
            foreign = true,
            foreignAutoRefresh = true,
            canBeNull = false,
            index = true,
            columnDefinition = "INTEGER CONSTRAINT FK_Produit_ListeCourse_Produit REFERENCES parent(idProduit) ON DELETE CASCADE"
    )
    private Produit produit;

    @DatabaseField (
            foreign = true,
            foreignAutoRefresh = true,
            canBeNull = false,
            index = true,
            columnDefinition = "INTEGER CONSTRAINT FK_Produit_ListeCourse_ListeCourse REFERENCES parent(idListeCourse) ON DELETE CASCADE"
    )
    private ListeCourse listeCourse;

    @DatabaseField(columnName = "quantite")
    private int quantite;

    public Produit_ListeCourse(Produit produit, ListeCourse listeCourse, int quantite) {
        this.produit = produit;
        this.listeCourse = listeCourse;
        this.quantite = quantite;
    }

    public Produit_ListeCourse(){

    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public ListeCourse getListeCourse() {
        return listeCourse;
    }

    public void setListeCourse(ListeCourse listeCourse) {
        this.listeCourse = listeCourse;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
}

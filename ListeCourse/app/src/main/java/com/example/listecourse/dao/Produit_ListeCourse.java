package com.example.listecourse.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Produit_ListeCourse")
public class Produit_ListeCourse {

    @DatabaseField (
            columnName = "idProduit",
            foreign = true,
            foreignAutoRefresh = true,
            canBeNull = false,
            index = true,
            columnDefinition = "INTEGER CONSTRAINT FK_Produit_ListeCourse_Produit REFERENCES Produit(idProduit) ON DELETE CASCADE"
    )
    private Produit produit;

    @DatabaseField (
            columnName = "idListeCourse",
            foreign = true,
            foreignAutoRefresh = true,
            canBeNull = false,
            index = true,
            columnDefinition = "INTEGER CONSTRAINT FK_Produit_ListeCourse_ListeCourse REFERENCES ListeCourse(idListeCourse) ON DELETE CASCADE"
    )
    private ListeCourse listeCourse;

    @DatabaseField (
            columnName = "idTaille",
            foreign = true,
            foreignAutoRefresh = true,
            canBeNull = true,
            index = true,
            columnDefinition = "INTEGER CONSTRAINT FK_Produit_Recette_Taille REFERENCES Taille(idTaille) ON DELETE CASCADE"
    )
    private Taille taille;

    @DatabaseField(columnName = "quantite")
    private int quantite;

    public Produit_ListeCourse(Produit produit, ListeCourse listeCourse, Taille taille, int quantite) {
        this.produit = produit;
        this.listeCourse = listeCourse;
        this.taille = taille;
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

    public Taille getTaille() {
        return taille;
    }

    public void setTaille(Taille taille) {
        this.taille = taille;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
}

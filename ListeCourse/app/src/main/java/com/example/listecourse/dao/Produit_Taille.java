package com.example.listecourse.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Produit_Taille")
public class Produit_Taille {

    @DatabaseField (
            columnName = "idProduit",
            foreign = true,
            foreignAutoRefresh = true,
            canBeNull = false,
            index = true,
            columnDefinition = "INTEGER CONSTRAINT FK_Produit_Taille_Produit REFERENCES Produit(idProduit) ON DELETE CASCADE"
    )
    private Produit produit;

    @DatabaseField (
            columnName = "idTaille",
            foreign = true,
            foreignAutoRefresh = true,
            canBeNull = false,
            index = true,
            columnDefinition = "INTEGER CONSTRAINT FK_Produit_Taille_Taille REFERENCES Taille(idTaille) ON DELETE CASCADE"
    )
    private Taille taille;

    public Produit_Taille(Produit produit, Taille taille) {
        this.produit = produit;
        this.taille = taille;
    }

    public Produit_Taille(){

    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Taille getTaille() {
        return taille;
    }

    public void setTaille(Taille taille) {
        this.taille = taille;
    }
}

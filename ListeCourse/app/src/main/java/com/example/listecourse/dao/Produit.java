package com.example.listecourse.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

@DatabaseTable(tableName = "Produit")
public class Produit {

    @DatabaseField( columnName="idProduit", generatedId=true)
    private int idProduit;

    @DatabaseField( columnName="libelle")
    private String libelle;

    private Taille taille;

    private ArrayList<Taille> ListeTailleDispo;

    private int quantite;

    private boolean idCart;

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

    public Taille getTaille() {
        return taille;
    }

    public void setTaille(Taille taille) {
        this.taille = taille;
    }

    public ArrayList<Taille> getListeTailleDispo() {
        return ListeTailleDispo;
    }

    public void setListeTailleDispo(ArrayList<Taille> listeTailleDispo) {
        ListeTailleDispo = listeTailleDispo;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public boolean isIdCart() {
        return idCart;
    }

    public void setIdCart(boolean idCart) {
        this.idCart = idCart;
    }
}

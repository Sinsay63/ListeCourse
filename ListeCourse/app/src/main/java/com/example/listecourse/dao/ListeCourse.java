package com.example.listecourse.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ListeCourse")
public class ListeCourse {

    @DatabaseField(columnName = "idListeCourse", generatedId = true)
    private int idListeCourse;

    @DatabaseField(columnName = "libelle")
    private String libelle;

    public ListeCourse(String libelle) {
        this.libelle = libelle;
    }

    public ListeCourse(){

    }

    public int getIdListeCourse() {
        return idListeCourse;
    }

    public void setIdListeCourse(int idListeCourse) {
        this.idListeCourse = idListeCourse;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString(){
        return this.getLibelle();
    }
}

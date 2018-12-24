package com.example.joan.brainallydiary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public class Classe_Perfil_Infant extends AppCompatActivity{
    private String ID, Nom, Cognoms, Genere, Pais;
    private String Data_Neixament;
    private int Codi_Postal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ID = "";
        Nom = "";
        Cognoms = "";
        Genere = "";
        Pais = "";
        Data_Neixament = "";
        Codi_Postal = 0;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getCognoms() {
        return Cognoms;
    }

    public void setCognoms(String cognoms) {
        Cognoms = cognoms;
    }

    public String getGenere() {
        return Genere;
    }

    public void setGenere(String genere) {
        Genere = genere;
    }

    public String getPais() {
        return Pais;
    }

    public void setPais(String pais) {
        Pais = pais;
    }

    public String getData_Neixament() {
        return Data_Neixament;
    }

    public void setData_Neixament(String data_Neixament) {
        Data_Neixament = data_Neixament;
    }

    public int getCodi_Postal() {
        return Codi_Postal;
    }

    public void setCodi_Postal(int codi_Postal) {
        Codi_Postal = codi_Postal;
    }
}

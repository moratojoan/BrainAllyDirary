package com.example.joan.brainallydiary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;


public class Classe_Perfil_Usuari{
    private String Ususari_ID, Email, Contrasenya;
    private String Nom, Cognoms, Genere, Pais;
    private String Data_Neixament;
    private String Codi_Postal;

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Nom = "";
//        Cognoms = "";
//        Genere = "";
//        Pais = "";
//        Data_Neixament = "";
//        Codi_Postal = "";
//
//    }

    public Classe_Perfil_Usuari(String Usuari_ID, String Email, String Contrasenya){
        this.Ususari_ID = Usuari_ID;
        this.Email = Email;
        this.Contrasenya = Contrasenya;
    }

    public String getUsusari_ID() {
        return Ususari_ID;
    }

    public void setUsusari_ID(String ususari_ID) {
        Ususari_ID = ususari_ID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getContrasenya() {
        return Contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        Contrasenya = contrasenya;
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

    public String getCodi_Postal() {
        return Codi_Postal;
    }

    public void setCodi_Postal(String codi_Postal) {
        Codi_Postal = codi_Postal;
    }

}

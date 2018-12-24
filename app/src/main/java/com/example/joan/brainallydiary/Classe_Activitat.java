package com.example.joan.brainallydiary;


import android.support.annotation.NonNull;


public class Classe_Activitat{
    private String NomI, CognomsI, Activitat;
    private String DI, DF, HI, HF;
    private long HI_comparador;
    private int Index_Emocio;
    private String id_Activitat;
    private String id_Infant;
    private String Activitat_o_Emocio;

    public Classe_Activitat(String id_Activitat,String id_Infant,String NomI, String CognomsI, String Activitat, long comparadorI, String DI, String DF, String HI, String HF, int index_Emocio,String Activitat_o_Emocio){
        this.id_Activitat = id_Activitat;
        this.id_Infant = id_Infant;
        this.NomI = NomI;
        this.CognomsI = CognomsI;
        this.Activitat = Activitat;
        //les activitats de moment només poden estar en un sol dia. si es programen per més d'un dia no funcionara bé.
        this.DI = DI;
        this.DF = DF;
        this.HI = HI;
        this.HF = HF;
        this.HI_comparador = comparadorI;
        this.Index_Emocio = index_Emocio;
        this.Activitat_o_Emocio = Activitat_o_Emocio;
    }



    public String getNomI() {
        return NomI;
    }

    public void setNomI(String nomI) {
        NomI = nomI;
    }

    public String getCognomsI() {
        return CognomsI;
    }

    public void setCognomsI(String cognomsI) {
        CognomsI = cognomsI;
    }

    public String getActivitat() {
        return Activitat;
    }

    public void setActivitat(String activitat) {
        Activitat = activitat;
    }

    public String getDI() {
        return DI;
    }

    public void setDI(String DI) {
        this.DI = DI;
    }

    public String getDF() {
        return DF;
    }

    public void setDF(String DF) {
        this.DF = DF;
    }

    public String getHI() {
        return HI;
    }

    public void setHI(String HI) {
        this.HI = HI;
    }

    public String getHF() {
        return HF;
    }

    public void setHF(String HF) {
        this.HF = HF;
    }

    public long getHI_comparador() {
        return HI_comparador;
    }

    public void setIndex_Emocio(int index_Emocio) {
        Index_Emocio = index_Emocio;
    }

    public int getIndex_Emocio() {
        return Index_Emocio;
    }

    public String getId_Activitat() {
        return id_Activitat;
    }

    public String getId_Infant() {
        return id_Infant;
    }

    public String getActivitat_o_Emocio() {
        return Activitat_o_Emocio;
    }
}

package com.example.joan.brainallydiary;

public class Classe_Perfil_Usuari_Trofeus {
    private String Usuari_id;
    private int Pos, Punts;

    public Classe_Perfil_Usuari_Trofeus(String usuari_id, int punts) {
        Usuari_id = usuari_id;
        Punts = punts;
    }

    public void setPos(int pos) {
        Pos = pos;
    }

    public int getPos() {
        return Pos;
    }

    public String getUsuari_id() {
        return Usuari_id;
    }

    public int getPunts() {
        return Punts;
    }
}

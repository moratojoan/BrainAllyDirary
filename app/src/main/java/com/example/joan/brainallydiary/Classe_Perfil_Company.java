package com.example.joan.brainallydiary;

public class Classe_Perfil_Company {
    private String Id_Company, Nom_C, Cognoms_C;
    private String Id_Infant[], Nom_I[], Cgonoms_I[];
    private String Relacio_CI[];

    public Classe_Perfil_Company(String id_Company, String nom_C, String cognoms_C, String[] id_Infant, String[] nom_I, String[] cgonoms_I, String[] relacio_CI) {
        Id_Company = id_Company;
        Nom_C = nom_C;
        Cognoms_C = cognoms_C;
        Id_Infant = id_Infant;
        Nom_I = nom_I;
        Cgonoms_I = cgonoms_I;
        Relacio_CI = relacio_CI;
    }

    public String getId_Company() {
        return Id_Company;
    }
    public void setId_Company(String id_Company) {
        Id_Company = id_Company;
    }

    public String getNom_C() {
        return Nom_C;
    }
    public void setNom_C(String nom_C) {
        Nom_C = nom_C;
    }

    public String getCognoms_C() {
        return Cognoms_C;
    }
    public void setCognoms_C(String cognoms_C) {
        Cognoms_C = cognoms_C;
    }

    public String[] getId_Infant() {
        return Id_Infant;
    }
    public void setId_Infant(String[] id_Infant) {
        Id_Infant = id_Infant;
    }

    public String[] getNom_I() {
        return Nom_I;
    }
    public void setNom_I(String[] nom_I) {
        Nom_I = nom_I;
    }

    public String[] getCgonoms_I() {
        return Cgonoms_I;
    }
    public void setCgonom_I(String[] cgonoms_I) {
        Cgonoms_I = cgonoms_I;
    }

    public String[] getRelacio_CI() {
        return Relacio_CI;
    }
    public void setRelacio_CI(String[] relacio_CI) {
        Relacio_CI = relacio_CI;
    }

    public int getN_Infants(){
        return Id_Infant.length;
    }
}

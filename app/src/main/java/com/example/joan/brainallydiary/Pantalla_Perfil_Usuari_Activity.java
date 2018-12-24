package com.example.joan.brainallydiary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Pantalla_Perfil_Usuari_Activity extends AppCompatActivity {

    private Globals globals;
    //private AdminSQLite BD_conn;
    //private AdminSQLite_S BD_S_conn;
    private SQLiteDatabase BD2, BD2_S;

    private TextView Nom, Cognoms, Data_Neixament, Genere, Pais, Codi_Postal, Usuari, Email, Contrasenya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_perfil_usuari);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        globals = Globals.getInstance(this);
        //BD_conn = globals.getBD_conn();
        //BD_S_conn = globals.getBD_S_conn();
        //BD2 = BD_conn.getWritableDatabase();
        //BD2_S = BD_S_conn.getWritableDatabase();
        BD2 = globals.getBD_w();
        BD2_S = globals.getBD_S_w();


        Nom = findViewById(R.id.txt_pu_nom);
        Cognoms = findViewById(R.id.txt_pu_cognoms);
        Data_Neixament = findViewById(R.id.txt_pu_dataneixament);
        Genere = findViewById(R.id.txt_pu_genere);
        Pais = findViewById(R.id.txt_pu_pais);
        Codi_Postal = findViewById(R.id.txt_pu_codipostal);
        Usuari = findViewById(R.id.txt_pu_usuari);
        Email = findViewById(R.id.txt_pu_email);
        Contrasenya = findViewById(R.id.txt_pu_contrasenya);

        Cursor cursor1 = BD2.rawQuery("SELECT * FROM "+Contract_S.NOM_TAULA_Usuari_Info_Login,null);
        Cursor cursor2 = BD2.rawQuery("SELECT * FROM "+ Contract_S.NOM_TAULA_Usuari_Info_Basica,null);
        if(cursor1.moveToFirst()){
            String Titol_pantalla = "Perfil: "+cursor1.getString(0);
            setTitle(Titol_pantalla);
            Usuari.setText(cursor1.getString(0));
            Email.setText(cursor1.getString(1));
        }
        if(cursor2.moveToFirst()){
            Nom.setText(cursor2.getString(0));
            Cognoms.setText(cursor2.getString(1));
            Data_Neixament.setText(cursor2.getString(2));
            Genere.setText(cursor2.getString(3));
            Pais.setText(cursor2.getString(4));
            Codi_Postal.setText(cursor2.getString(5));
        }

        cursor1.close();
        cursor2.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

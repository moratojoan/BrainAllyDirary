package com.example.joan.brainallydiary;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class Pantalla_Perfils_Infants_Activity extends AppCompatActivity implements View.OnClickListener {

    private Globals globals;
    private AdminSQLite BD_conn;
    private AdminSQLite_S BD_S_conn;
    private SQLiteDatabase BD2, BD2_S;


    private ArrayList<Classe_Perfil_Infant> llistaInfants;
    private RecyclerView recyclerViewInfants;

    private Button btn_NouInfant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_perfils_infants);

        btn_NouInfant = findViewById(R.id.btn_pi_NouInfant);
        btn_NouInfant.setOnClickListener(this);

        globals = Globals.getInstance(this);
        //BD_conn = globals.getBD_conn();
        //BD_S_conn = globals.getBD_S_conn();
        //BD2 = BD_conn.getWritableDatabase();
        //BD2_S = BD_S_conn.getWritableDatabase();
        BD2 = globals.getBD_w();
        BD2_S = globals.getBD_S_w();

        //RECYCLERVIEW
        llistaInfants = new ArrayList<>();
        recyclerViewInfants = findViewById(R.id.RecyclerView_ID);
        recyclerViewInfants.setLayoutManager(new LinearLayoutManager(this));

        omplirllisaInfants();
        AdaptadorInfants adapter = new AdaptadorInfants(llistaInfants);
        recyclerViewInfants.setAdapter(adapter);

        SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
        String usuari_sessio = preferences.getString("Usuari de la Sessió","");
        String usuari_sessio_vector[] = {usuari_sessio};
        Cursor cursor2 = BD2_S.rawQuery("SELECT * FROM "+Contract_S.NOM_TAULA_Infants_Pendents_Compartir+" WHERE "+Contract_S.Camp_Usuari_Receptor_id+" =?",usuari_sessio_vector);
        if(cursor2.moveToFirst()){
            alertdialog_compartir(cursor2);
        }
    }

    private void alertdialog_compartir(final Cursor cursor2) {
        final String Usuari_Receptor_id = cursor2.getString(0);
        String Usuari_principal_id = cursor2.getString(1);
        final String Infant_id = cursor2.getString(2);
        String Nom_I = cursor2.getString(3);
        final String Relacio = cursor2.getString(4);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContentValues values = new ContentValues();
                        values.put(Contract_S.Camp_Usuari_id,Usuari_Receptor_id);
                        values.put(Contract_S.Camp_Infant_ID, Infant_id);
                        values.put(Contract_S.Camp_Relacio, Relacio);

                        BD2.insert(Contract.NOM_TAULA_Relacio_Usuari_Infant,null,values);
                        BD2_S.insert(Contract_S.NOM_TAULA_Relacio_Usuari_Infant,null,values);
                        values.clear();
                        String parametres[]={Usuari_Receptor_id,Infant_id};
                        BD2_S.delete(Contract_S.NOM_TAULA_Infants_Pendents_Compartir,Contract_S.Camp_Usuari_Receptor_id+"=? AND "+Contract_S.Camp_Infant_ID+"=?",parametres);

                        if(cursor2.moveToNext()){
                            alertdialog_compartir(cursor2);
                        }else {
                            SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
                            String usuari_sessio = preferences.getString("Usuari de la Sessió","");
                            globals.ActulitzarBD(getApplicationContext(),usuari_sessio);
                            Intent intent = new Intent(getApplicationContext(), Pantalla_Perfils_Infants_Activity.class);
                            startActivity(intent);
                            cursor2.close();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String parametres[]={Usuari_Receptor_id,Infant_id};
                        BD2_S.delete(Contract_S.NOM_TAULA_Infants_Pendents_Compartir,Contract_S.Camp_Usuari_Receptor_id+"=? AND "+Contract_S.Camp_Infant_ID+"=?",parametres);
                        if(cursor2.moveToNext()){
                            alertdialog_compartir(cursor2);
                        }else {
                            SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
                            String usuari_sessio = preferences.getString("Usuari de la Sessió","");
                            globals.ActulitzarBD(getApplicationContext(),usuari_sessio);
                            Intent intent = new Intent(getApplicationContext(), Pantalla_Perfils_Infants_Activity.class);
                            startActivity(intent);
                            cursor2.close();
                        }
                    }
                })
                .setTitle("L'usuari "+Usuari_principal_id+" vol compartir: "+Nom_I)
                .setMessage("Estàs d'acord?");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_pi_NouInfant:
                Intent intent = new Intent(this, Pantalla_Perfils_Infants_Registrar1.class);
                startActivity(intent);
                break;
        }
    }

    private void omplirllisaInfants() {
        Cursor cursor = BD2.rawQuery("SELECT "+Contract.Camp_Infant_ID+","+Contract.Camp_Nom_I+" FROM "+Contract.NOM_TAULA_Infant_Info_Basica,null);
        if(cursor.moveToFirst()){
            do{
                Classe_Perfil_Infant Infant = new Classe_Perfil_Infant();
                Infant.setID(cursor.getString(0));
                Infant.setNom(cursor.getString(1));
                llistaInfants.add(Infant);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }
}

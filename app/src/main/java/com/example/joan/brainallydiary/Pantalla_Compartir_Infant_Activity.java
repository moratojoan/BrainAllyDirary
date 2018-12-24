package com.example.joan.brainallydiary;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Pantalla_Compartir_Infant_Activity extends AppCompatActivity implements View.OnClickListener {

    private Globals globals;
    private AdminSQLite BD_conn;
    private AdminSQLite_S BD_S_conn;
    private SQLiteDatabase BD2, BD2_S;


    private AutoCompleteTextView Usuari_Email;
    private Spinner spn_relacioCI;
    private Button btn_compartir;

    private String Tipus_Relacio[] = {"Pare","Mare","Altre Familiar","Professor","Cangur"};

    private String usuaris_BD_S[], usuaris_emails_S[];

    private String Infant_ID, Nom_I;
    private int x_existeix;

    private String usuari_sessio;

    private ArrayList<String> llista_Usuaris_Infant, llista_Usuaris_Receptros_Infant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_compartir_infant);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!=null){
            setTitle("Compartir: "+bundle.getString("Nom_I"));
            Infant_ID = bundle.getString("Infant_ID");
            Nom_I = bundle.getString("Nom_I");
        }


        Usuari_Email = findViewById(R.id.txt_ci_Usuari_Email);
        spn_relacioCI = findViewById(R.id.spn_ci_RelacioCI);
        btn_compartir = findViewById(R.id.btn_ci_compartir);
        btn_compartir.setOnClickListener(this);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Tipus_Relacio);
        spn_relacioCI.setAdapter(adapter2);

        globals = Globals.getInstance(this);
        //BD_conn = globals.getBD_conn();
        //BD_S_conn = globals.getBD_S_conn();
        //BD2 = BD_conn.getWritableDatabase();
        //BD2_S = BD_S_conn.getWritableDatabase();
        BD2 = globals.getBD_w();
        BD2_S = globals.getBD_S_w();

        //Llista de suggerencies
        Cursor cursor = BD2_S.rawQuery("SELECT "+Contract_S.Camp_Usuari_id+","+Contract_S.Camp_Email+" FROM "+Contract_S.NOM_TAULA_Usuari_Info_Login,null);
        usuaris_emails_S = new String[cursor.getCount()-1];
        usuaris_BD_S = new String[cursor.getCount()-1];
        int i =0;
        SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
        usuari_sessio = preferences.getString("Usuari de la Sessió","");
        if(cursor.moveToFirst()){
            do{
                if(!cursor.getString(0).equals(usuari_sessio)){
                    usuaris_emails_S[i]=cursor.getString(0)+" : "+cursor.getString(1);
                    usuaris_BD_S[i]=cursor.getString(0);
                    i=i+1;
                }
            }while (cursor.moveToNext());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, usuaris_emails_S);
        Usuari_Email.setAdapter(adapter);

        llista_Usuaris_Infant = new ArrayList<String>();
        llista_Usuaris_Receptros_Infant = new ArrayList<String>();
        String infantID_vector[] = {Infant_ID};
        cursor = BD2_S.rawQuery("SELECT "+Contract_S.Camp_Usuari_id+" FROM "+Contract_S.NOM_TAULA_Relacio_Usuari_Infant+" WHERE "+Contract_S.Camp_Infant_ID+"=?",infantID_vector);
        if(cursor.moveToFirst()){
            do{
                llista_Usuaris_Infant.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }

        cursor = BD2_S.rawQuery("SELECT "+Contract_S.Camp_Usuari_Receptor_id+" FROM "+Contract_S.NOM_TAULA_Infants_Pendents_Compartir+" WHERE "+Contract_S.Camp_Infant_ID+"=?",infantID_vector);
        if(cursor.moveToFirst()){
            do{
                llista_Usuaris_Receptros_Infant.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ci_compartir:
                if(Usuari_Email.getText().toString().equals("")){
                    //No ha introduit res
                    Toast.makeText(this,"Amb qui vols compartir el teu infant?",Toast.LENGTH_SHORT).show();
                }else{
                    int x;
                    x_existeix = -1;
                    boolean Usuari_Existeix = false;
                    for(x=0;x<usuaris_emails_S.length;x++){
                        if(Usuari_Email.getText().toString().equals(usuaris_emails_S[x])){
                            Usuari_Existeix = true;
                            x_existeix=x;
                        }
                    }
                    if (Usuari_Existeix){
                        boolean Infant_Compartit = false;
                        boolean Infant_Receptor = false;
                        for(int i=0;i<llista_Usuaris_Infant.size();i++){
                            if(usuaris_BD_S[x_existeix].equals(llista_Usuaris_Infant.get(i))){
                                Infant_Compartit = true;
                            }
                        }
                        for(int i=0;i<llista_Usuaris_Receptros_Infant.size();i++){
                            if(usuaris_BD_S[x_existeix].equals(llista_Usuaris_Receptros_Infant.get(i))){
                                Infant_Receptor = true;
                            }
                        }

                        if(x_existeix!=-1){
                            if(!Infant_Compartit){
                                if(!Infant_Receptor){
                                    ContentValues values = new ContentValues();
                                    values.put(Contract_S.Camp_Usuari_Receptor_id, usuaris_BD_S[x_existeix]);
                                    values.put(Contract_S.Camp_Usuari_Principal_id,usuari_sessio);
                                    values.put(Contract_S.Camp_Infant_ID, Infant_ID);
                                    values.put(Contract_S.Camp_Nom_I,Nom_I);
                                    values.put(Contract_S.Camp_Relacio, spn_relacioCI.getSelectedItem().toString());

                                    BD2_S.insert(Contract_S.NOM_TAULA_Infants_Pendents_Compartir,null,values);
                                    values.clear();

                                    Intent intent = new Intent(this, Pantalla_Perfils_Infants_Activity.class);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(this,"L'infant ja s'ha compartit amb aquest usuari",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(this,"L'usuari ja és responsable d'aquest Infant",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        Toast.makeText(this,"No existeix aquest Usuari",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}

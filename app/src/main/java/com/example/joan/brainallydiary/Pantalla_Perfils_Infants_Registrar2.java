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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Pantalla_Perfils_Infants_Registrar2 extends AppCompatActivity implements View.OnClickListener {

    private Globals globals;
    private AdminSQLite BD_conn;
    private AdminSQLite_S BD_S_conn;
    private SQLiteDatabase BD2, BD2_S;

    private EditText Trastorn_Principal;
    private Button btn_Guardar;

    private String Infant_ID, Nom_I, Cognoms_I, Data_Neixament, spn_Genere, Pais, Codi_Postal, spn_Relacio_UI;

    private EditText Medicines;
    private Button btn_Afegir;
    private ListView listView_Medicines;
    private ArrayList<String> llista_Medicines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_perfils_infants_registrar2);

        Trastorn_Principal = findViewById(R.id.txt_cpi2_TrastornPrincipal);
        btn_Guardar = findViewById(R.id.btn_cpi2_Guardar);
        Medicines = findViewById(R.id.txt_cpi2_Medicines);
        btn_Afegir = findViewById(R.id.btn_cpi2_Afegir);
        listView_Medicines = findViewById(R.id.listView_cpi2_Medicines);
        llista_Medicines=new ArrayList<String>();

        btn_Afegir.setOnClickListener(this);
        btn_Guardar.setOnClickListener(this);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            Infant_ID = extras.getString(Contract.Camp_Infant_ID);
            Nom_I = extras.getString(Contract.Camp_Nom_I);
            Cognoms_I = extras.getString(Contract.Camp_Cognoms_I);
            Data_Neixament = extras.getString(Contract.Camp_Data_Neixament);
            spn_Genere = extras.getString(Contract.Camp_Pais);
            Pais = extras.getString(Contract.Camp_Codi_Postal);
            Codi_Postal = extras.getString(Contract.Camp_Codi_Postal);
            spn_Relacio_UI = extras.getString(Contract.Camp_Relacio);
        }

        globals = Globals.getInstance(this);
        //BD_conn = globals.getBD_conn();
        //BD_S_conn = globals.getBD_S_conn();
        //BD2 = BD_conn.getWritableDatabase();
        //BD2_S = BD_S_conn.getWritableDatabase();
        BD2 = globals.getBD_w();
        BD2_S = globals.getBD_S_w();
    }

    private void actulitzarListView(ArrayList<String> llista_Medicines) {
        ArrayAdapter<String> adapter_llista = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, llista_Medicines);
        listView_Medicines.setAdapter(adapter_llista);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cpi2_Afegir:
                if(!Medicines.getText().toString().equals("")){
                    llista_Medicines.add(Medicines.getText().toString());
                }
                actulitzarListView(llista_Medicines);
                Medicines.setText("");
                break;
            case R.id.btn_cpi2_Guardar:
                if (!Trastorn_Principal.getText().toString().equals("")){

                    ContentValues dades = new ContentValues();
                    dades.put(Contract.Camp_Infant_ID,Infant_ID);
                    dades.put(Contract.Camp_Nom_I, Nom_I);
                    dades.put(Contract.Camp_Cognoms_I, Cognoms_I);
                    dades.put(Contract.Camp_Data_Neixament, Data_Neixament);
                    dades.put(Contract.Camp_Genere, spn_Genere);
                    dades.put(Contract.Camp_Pais, Pais);
                    dades.put(Contract.Camp_Codi_Postal, Codi_Postal);
                    BD2.insert(Contract.NOM_TAULA_Infant_Info_Basica,null, dades);
                    BD2_S.insert(Contract_S.NOM_TAULA_Infant_Info_Basica,null,dades);
                    dades.clear();


                    ContentValues dades2 = new ContentValues();
                    dades2.put(Contract.Camp_Infant_ID,Infant_ID);
                    dades2.put(Contract.Camp_Relacio, spn_Relacio_UI);
                    BD2.insert(Contract.NOM_TAULA_Relacio_Usuari_Infant,null, dades2);
                    dades2.clear();
                    SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
                    String usuari_sessio = preferences.getString("Usuari de la Sessió","");
                    ContentValues values = new ContentValues();
                    values.put(Contract_S.Camp_Usuari_id, usuari_sessio);
                    values.put(Contract_S.Camp_Infant_ID, Infant_ID);
                    values.put(Contract_S.Camp_Relacio, spn_Relacio_UI);
                    BD2_S.insert(Contract_S.NOM_TAULA_Relacio_Usuari_Infant,null,values);
                    values.clear();


                    BD2.execSQL("INSERT INTO "+Contract.NOM_TAULA_Infant_Info_Trastorn+ " VALUES ('"+Infant_ID+"','"+Trastorn_Principal.getText().toString()+"')");
                    BD2_S.execSQL("INSERT INTO "+Contract_S.NOM_TAULA_Infant_Info_Trastorn+ " VALUES ('"+Infant_ID+"','"+Trastorn_Principal.getText().toString()+"')");


                    ContentValues dades3 = new ContentValues();
                    for(int i=0; i<llista_Medicines.size();i++){
                        dades3.put(Contract.Camp_Infant_ID,Infant_ID);
                        dades3.put(Contract.Camp_Medicina,llista_Medicines.get(i));
                        BD2.insert(Contract.NOM_TAULA_Infant_Medicines,null, dades3);
                        BD2_S.insert(Contract.NOM_TAULA_Infant_Medicines,null, dades3);
                        dades3.clear();
                    }

                    Intent intent = new Intent(this, Pantalla_Perfils_Infants_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else {
                    Toast.makeText(this,"Cal introduir tots els camps",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

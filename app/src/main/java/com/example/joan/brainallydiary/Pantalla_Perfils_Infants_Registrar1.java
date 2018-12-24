package com.example.joan.brainallydiary;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Random;

public class Pantalla_Perfils_Infants_Registrar1 extends AppCompatActivity implements View.OnClickListener {

    private Globals globals;
    private AdminSQLite BD_conn;
    private AdminSQLite_S BD_S_conn;
    private SQLiteDatabase BD2, BD2_S;

    private EditText Nom_I, Cognoms_I, Pais, Codi_Postal;
    private TextView Data_Neixament;
    private Spinner spn_Genere, spn_Relacio_UI;
    private Button btn_Seguent;

    private String Generes[]={"Femení","Masculí","Altre","No vull respondre"};
    private String Tipus_Relacio[] = {"Pare","Mare","Altre Familiar","Professor","Cangur"};

    private String RandomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_perfils_infants_registrar1);

        Nom_I = findViewById(R.id.txt_cpi_Nom);
        Cognoms_I = findViewById(R.id.txt_cpi_Cognoms);
        Data_Neixament = findViewById(R.id.txt_cpi_DataNeixament);
        spn_Genere = findViewById(R.id.spn_cpi_Genere);
        Pais = findViewById(R.id.txt_cpi_Pais);
        Codi_Postal = findViewById(R.id.txt_cpi_CodiPostal);
        spn_Relacio_UI = findViewById(R.id.spn_cpi_RelacioUI);
        btn_Seguent = findViewById(R.id.btn_cpi_Seguent);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Generes);
        spn_Genere.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Tipus_Relacio);
        spn_Relacio_UI.setAdapter(adapter2);

        Data_Neixament.setOnClickListener(this);
        btn_Seguent.setOnClickListener(this);

        globals = Globals.getInstance(this);
        //BD_conn = globals.getBD_conn();
        //BD_S_conn = globals.getBD_S_conn();
        //BD2 = BD_conn.getWritableDatabase();
        //BD2_S = BD_S_conn.getWritableDatabase();
        BD2 = globals.getBD_w();
        BD2_S = globals.getBD_S_w();

        Cursor cursor = BD2.rawQuery("SELECT "+ Contract.Camp_Pais+","+Contract.Camp_Codi_Postal+" FROM "+Contract.NOM_TAULA_Usuari_Info_Basica,null);
        cursor.moveToFirst();
        Pais.setText(cursor.getString(0));
        Codi_Postal.setText(cursor.getString(1));
        cursor.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_cpi_DataNeixament:
                Calendar c = Calendar.getInstance();
                int dia = c.get(Calendar.DAY_OF_MONTH);
                int mes = c.get(Calendar.MONTH);
                int any = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String dia_mes_ano = arreglar_time_date(day)+"/"+(arreglar_time_date(month+1))+"/"+year;
                        Data_Neixament.setText(dia_mes_ano);
                    }
                },any,mes,dia);
                datePickerDialog.show();
                break;
            case R.id.btn_cpi_Seguent:

                if(!Nom_I.getText().toString().isEmpty() && !Cognoms_I.getText().toString().isEmpty()){
                    Cursor cursor = BD2_S.rawQuery("SELECT "+Contract_S.Camp_Infant_ID+" FROM "+Contract_S.NOM_TAULA_Infant_Info_Basica,null);
                    crear_randomID(cursor);
                    cursor.close();

                    Intent intent = new Intent(this, Pantalla_Perfils_Infants_Registrar2.class);
                    intent.putExtra(Contract.Camp_Infant_ID,RandomID);
                    intent.putExtra(Contract.Camp_Nom_I,Nom_I.getText().toString());
                    intent.putExtra(Contract.Camp_Cognoms_I,Cognoms_I.getText().toString());
                    intent.putExtra(Contract.Camp_Data_Neixament,Data_Neixament.getText().toString());
                    intent.putExtra(Contract.Camp_Genere,spn_Genere.getSelectedItem().toString());
                    intent.putExtra(Contract.Camp_Pais,Pais.getText().toString());
                    intent.putExtra(Contract.Camp_Codi_Postal,Codi_Postal.getText().toString());
                    intent.putExtra(Contract.Camp_Relacio,spn_Relacio_UI.getSelectedItem().toString());
                    startActivity(intent);
                }else{
                    Toast.makeText(this,"Introdueix el Nom i els Cognoms",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void crear_randomID(Cursor cursor) {
        Random random = new Random();
        String random2 = new BigInteger(50, random).toString(32);
        int validar =0;
        if (cursor.moveToFirst()){
            cursor.moveToFirst();
            do{
                if (cursor.getString(0).equals(random2)){
                    validar = 1;
                }
            }while (cursor.moveToNext());
            if (validar==0){
                RandomID = random2;
            }else {
                crear_randomID(cursor);
            }
        } else{
            //No s'ha creat cap infant, és el primer, per tant el random és valid
            RandomID = random2;
        }
    }

    private static String arreglar_time_date(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
}

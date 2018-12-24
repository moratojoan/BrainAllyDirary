package com.example.joan.brainallydiary;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.Calendar;

public class Pantalla_Config_Perfil_Usuari_Activity extends AppCompatActivity implements View.OnClickListener {

    private Globals globals;
    private AdminSQLite BD_conn;
    private AdminSQLite_S BD_S_conn;
    private SQLiteDatabase BD2, BD2_S;


    private String usuari_sessio;

    private EditText Nom_U, Cognoms_U, Pais, Codi_Postal;
    private TextView Data_Neixament;
    private Spinner spn_Genere;
    private Button btn_Guardar;
    private String Generes[]={"Femení","Masculí","Altre","No vull respondre"};

    private Boolean controlador_data_seleccionada=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_config_perfil_usuari);

        Nom_U = findViewById(R.id.txt_cp_Nom);
        Cognoms_U = findViewById(R.id.txt_cp_Cognoms);
        Data_Neixament = findViewById(R.id.txt_cp_DataNeixament);
        Pais = findViewById(R.id.txt_cp_Pais);
        Codi_Postal = findViewById(R.id.txt_cp_CodiPostal);
        spn_Genere = findViewById(R.id.spn_cp_Genere);
        btn_Guardar = findViewById(R.id.btn_cp_Guardar);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Generes);
        spn_Genere.setAdapter(adapter);

        Data_Neixament.setOnClickListener(this);
        btn_Guardar.setOnClickListener(this);

        SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
        usuari_sessio = preferences.getString("Usuari de la Sessió","");

        globals = Globals.getInstance(this);
        //BD_conn = globals.getBD_conn();
        //BD_S_conn = globals.getBD_S_conn();
        //BD2 = BD_conn.getWritableDatabase();
        //BD2_S = BD_S_conn.getWritableDatabase();
        BD2 = globals.getBD_w();
        BD2_S = globals.getBD_S_w();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE); //"Dades Sessió" és el nom de l'arxiu de preferencies.
        SharedPreferences.Editor Obj_editor = preferences.edit();
        Obj_editor.putString("Estat Sessió", "Tancada"); //"Estat Sessió" és la referencia del valor que es guarda.
        Obj_editor.apply();
        Intent intent = new Intent(this, Pantalla_Login_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_cp_DataNeixament:
                Calendar c = Calendar.getInstance();
                int dia = c.get(Calendar.DAY_OF_MONTH);
                int mes = c.get(Calendar.MONTH);
                int any = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String dia_mes_ano = arreglar_time_date(day)+"/"+(arreglar_time_date(month+1))+"/"+year;
                        Data_Neixament.setText(dia_mes_ano);
                        controlador_data_seleccionada=true;
                    }
                },any,mes,dia);
                datePickerDialog.show();
                break;
            case R.id.btn_cp_Guardar:

                ContentValues dades = new ContentValues();
                dades.put(Contract_S.Camp_Usuari_id, usuari_sessio);
                dades.put(Contract_S.Camp_Nom_U, Nom_U.getText().toString());
                dades.put(Contract_S.Camp_Cognoms_U, Cognoms_U.getText().toString());
                if(controlador_data_seleccionada){
                    dades.put(Contract_S.Camp_Data_Neixament, Data_Neixament.getText().toString());
                }
                dades.put(Contract_S.Camp_Genere, spn_Genere.getSelectedItem().toString());
                dades.put(Contract_S.Camp_Pais, Pais.getText().toString());
                dades.put(Contract_S.Camp_Codi_Postal, Codi_Postal.getText().toString());

                BD2_S.insert(Contract_S.NOM_TAULA_Usuari_Info_Basica,null, dades);

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    private static String arreglar_time_date(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
}

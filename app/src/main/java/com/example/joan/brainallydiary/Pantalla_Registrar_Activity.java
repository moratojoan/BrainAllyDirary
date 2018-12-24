package com.example.joan.brainallydiary;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pantalla_Registrar_Activity extends AppCompatActivity implements View.OnClickListener {

    private Globals globals;
    private AdminSQLite BD_conn;
    private AdminSQLite_S BD_S_conn;
    private SQLiteDatabase BD2, BD2_S;

    private EditText Usuari, Email, Password, Password2;
    private TextView Error_Usuari, Error_Email, Error_Email2;
    private Button btn_registrar;

//    private int U, E1, E2, P;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_registrar);

        Usuari = findViewById(R.id.txt_reg_usuari);
        Error_Usuari = findViewById(R.id.txt_reg_error_usuari);
        Email = findViewById(R.id.txt_reg_email);
        Error_Email = findViewById(R.id.txt_reg_error_email);
        Error_Email2 = findViewById(R.id.txt_reg_error_email2);
        Password = findViewById(R.id.txt_reg_password);
        Password2 = findViewById(R.id.txt_reg_password2);
        btn_registrar = findViewById(R.id.btn_reg_registrar);
        btn_registrar.setOnClickListener(this);

        globals = Globals.getInstance(this);
        //BD_conn = globals.getBD_conn();
        //BD_S_conn = globals.getBD_S_conn();
        //BD2 = BD_conn.getWritableDatabase();
        //BD2_S = BD_S_conn.getWritableDatabase();
        BD2 = globals.getBD_w();
        BD2_S = globals.getBD_S_w();


        Error_Usuari.setVisibility(View.INVISIBLE);
//        Usuari.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if(!hasFocus) {
//                    int milisegundos = 1500;
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        public void run() {
//                            // acciones que se ejecutan tras los milisegundos
//                            Cursor cursor = BD2_S.rawQuery("SELECT "+Contract_S.Camp_Usuari_id+" FROM "+Contract_S.NOM_TAULA_Usuari_Info_Login,null);
//                            while (cursor.moveToNext()){
//                                //Comparar Usuari amb la BD_S: guardar un valor 0 o 1.
//                                if(Usuari.getText().toString().equals(cursor.getString(0))){
//                                    Error_Usuari.setVisibility(View.VISIBLE);
//                                    Usuari.setTextColor(Color.parseColor("#fb1d1d"));
//                                    break;
//                                }else {
//                                    Error_Usuari.setVisibility(View.INVISIBLE);
//                                    Usuari.setTextColor(Color.parseColor("#000000"));
//                                }
//                            }
//                            cursor.close();
//                        }
//                    }, milisegundos);
//                }
//            }
//        });
        Usuari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Error_Usuari.setVisibility(View.INVISIBLE);
                Usuari.setTextColor(Color.parseColor("#000000"));
                int milisegundos = 1000;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Cursor cursor = BD2_S.rawQuery("SELECT "+Contract_S.Camp_Usuari_id+" FROM "+Contract_S.NOM_TAULA_Usuari_Info_Login,null);
                        while (cursor.moveToNext()){
                            //Comparar Usuari amb la BD_S: guardar un valor 0 o 1.
                            if(Usuari.getText().toString().equals(cursor.getString(0))){
                                // acciones que se ejecutan tras los milisegundos
                                Error_Usuari.setVisibility(View.VISIBLE);
                                Usuari.setTextColor(Color.parseColor("#fb1d1d"));
                                break;
                            }else {
                                Error_Usuari.setVisibility(View.INVISIBLE);
                                Usuari.setTextColor(Color.parseColor("#000000"));
                            }
                        }
                        cursor.close();
                    }
                    }, milisegundos);
            }
        });

        Error_Email.setVisibility(View.INVISIBLE);
        Error_Email2.setVisibility(View.INVISIBLE);
        Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Error_Email.setVisibility(View.INVISIBLE);
                Error_Email2.setVisibility(View.INVISIBLE);
                Email.setTextColor(Color.parseColor("#000000"));
                int milisegundos = 1000;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if(validarEmail(Email.getText().toString())){
                            Cursor cursor = BD2_S.rawQuery("SELECT "+Contract_S.Camp_Email+" FROM "+Contract_S.NOM_TAULA_Usuari_Info_Login,null);
                            while (cursor.moveToNext()){
                                    //Comparar Usuari amb la BD_S: guardar un valor 0 o 1.
                                if(Email.getText().toString().equals(cursor.getString(0))){
                                        // acciones que se ejecutan tras los milisegundos
                                    Error_Email.setVisibility(View.VISIBLE);
                                    Email.setTextColor(Color.parseColor("#fb1d1d"));
                                    break;
                                }else {
                                    Error_Email.setVisibility(View.INVISIBLE);
                                    Email.setTextColor(Color.parseColor("#000000"));
                                }
                            }
                            cursor.close();
                        }else {
                            Error_Email2.setVisibility(View.VISIBLE);
                            Email.setTextColor(Color.parseColor("#fb1d1d"));
                        }
                    }
                    }, milisegundos);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_reg_registrar:
                if(!Password.getText().toString().equals("") && !Password2.getText().toString().equals("") && !Usuari.getText().toString().equals("") && !Email.getText().toString().equals("")){
                    if(!Error_Usuari.isShown()){
                        if(!Error_Email2.isShown()){
                            if(!Error_Email.isShown()){
                                if (Password.getText().toString().equals(Password2.getText().toString())){
                                    ContentValues dades = new ContentValues();
                                    dades.put(Contract_S.Camp_Usuari_id, Usuari.getText().toString());
                                    dades.put(Contract_S.Camp_Email, Email.getText().toString());
                                    dades.put(Contract_S.Camp_Password, Password.getText().toString());
                                    BD2_S.insert(Contract_S.NOM_TAULA_Usuari_Info_Login,null, dades);
                                    dades.clear();

                                    //Crear la taula de Trofeus, tot a 0.
                                    ContentValues values = new ContentValues();
                                    values.put(Contract_S.Camp_Usuari_id,Usuari.getText().toString());
                                    values.put(Contract_S.Camp_Totals,0);
                                    values.put(Contract_S.Camp_Mensuals,0);
                                    values.put(Contract_S.Camp_Setmanals,0);
                                    values.put(Contract_S.Camp_Diaris,0);
                                    values.put(Contract_S.Camp_Ultim_Dia_Entrat,"00-00-0000");
                                    values.put(Contract_S.Camp_Ultim_Dia_20,"00-00-0000");
                                    values.put(Contract_S.Camp_Contador_03,0);
                                    values.put(Contract_S.Camp_lvl_perseverancia,0);
                                    values.put(Contract_S.Camp_Contador_perseverancia,0);
                                    values.put(Contract_S.Camp_RatingBar_perseverancia,0);
                                    values.put(Contract_S.Camp_lvl_registres,0);
                                    values.put(Contract_S.Camp_Contador_registres,0);
                                    values.put(Contract_S.Camp_RatingBar_registres,0);
                                    BD2_S.insert(Contract_S.NOM_TAULA_Trofeus,null,values);
                                    values.clear();

                                    Intent intent = new Intent(this, Pantalla_Login_Activity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(this,"Les contrasenyes no coincideixen",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(this,"L'Email no està disponible",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(this,"L'Email no és vàlid",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this,"L'Usuari no està disponible",Toast.LENGTH_SHORT).show();
                    }

                } else{
                    Toast.makeText(this,"S'han d'omplir tots els camps",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean validarEmail(String email) {
        if(email.equals("")){
            return true;
        }else{
            Pattern pattern = Patterns.EMAIL_ADDRESS;
            return pattern.matcher(email).matches();
        }
    }
}

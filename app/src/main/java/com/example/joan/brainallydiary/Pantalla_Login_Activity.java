package com.example.joan.brainallydiary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Pantalla_Login_Activity extends AppCompatActivity implements View.OnClickListener{

    private Globals globals;
    private AdminSQLite BD_conn;
    private AdminSQLite_S BD_S_conn;
    private SQLiteDatabase BD2, BD2_S;

    private EditText Usuari_Email, Password;
    private Button btn_entrar, btn_registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_login);

        Usuari_Email = findViewById(R.id.txt_login_usuari_email);
        Password = findViewById(R.id.txt_login_password);
        btn_entrar = findViewById(R.id.btn_login_entrar);
        btn_registrar = findViewById(R.id.btn_login_registrar);
        btn_entrar.setOnClickListener(this);
        btn_registrar.setOnClickListener(this);

        Usuari_Email.setText("");
        Password.setText("");

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
        Gestor_Activities.quitApp();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login_entrar:

                String Usuari_Email_contenidor[] = {Usuari_Email.getText().toString()};
                String Password_contenidor = Password.getText().toString();


                if(!Usuari_Email.getText().toString().isEmpty() && !Password.getText().toString().isEmpty()){
                    Cursor info_usuari_registrat = BD2_S.rawQuery("SELECT "+ Contract_S.Camp_Email+","+Contract_S.Camp_Password+" FROM "+Contract_S.NOM_TAULA_Usuari_Info_Login+" WHERE "+Contract_S.Camp_Usuari_id+"=? ",Usuari_Email_contenidor);
                    if (info_usuari_registrat.moveToFirst()){
                        if(info_usuari_registrat.getString(1).equals(Password_contenidor)){
                            Toast.makeText(this,"Usuari i Password correctes",Toast.LENGTH_SHORT).show();

                            SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
                            SharedPreferences.Editor Obj_editor = preferences.edit();
                            Obj_editor.putString("Estat Sessió", "Oberta");
                            Obj_editor.putString("Usuari de la Sessió", Usuari_Email.getText().toString());
                            Obj_editor.putString("Sessió_actulitzada","No");
                            Obj_editor.apply();

                            Intent intent = new Intent(this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else{
                            Toast.makeText(this,"La Contrasenya és incorrecte",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        info_usuari_registrat.close();
                        info_usuari_registrat = BD2_S.rawQuery("SELECT "+ Contract_S.Camp_Usuari_id+","+Contract_S.Camp_Password+" FROM "+Contract_S.NOM_TAULA_Usuari_Info_Login+" WHERE "+Contract_S.Camp_Email+"=? ",Usuari_Email_contenidor);
                        if (info_usuari_registrat.moveToFirst()){
                            if(info_usuari_registrat.getString(1).equals(Password_contenidor)){
                                Toast.makeText(this,"Email i Password correctes",Toast.LENGTH_SHORT).show();

                                SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
                                SharedPreferences.Editor Obj_editor = preferences.edit();
                                Obj_editor.putString("Estat Sessió", "Oberta");
                                Obj_editor.putString("Usuari de la Sessió", info_usuari_registrat.getString(0));
                                Obj_editor.apply();

                                Intent intent = new Intent(this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }else{
                                Toast.makeText(this,"La Contrasenya és incorrecte",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            info_usuari_registrat.close();
                            Toast.makeText(this,"No s'ha trobat l'Usuari/Email introduit",Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(this,"Cal introduir Usuari i Contrasenya",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_login_registrar:
                Intent intent = new Intent(this, Pantalla_Registrar_Activity.class);
                startActivity(intent);
                break;
        }
    }
}

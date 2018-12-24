package com.example.joan.brainallydiary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class Pantalla_Perfils_Companys_Activity extends AppCompatActivity {

    private Globals globals;
    private AdminSQLite BD_conn;
    private AdminSQLite_S BD_S_conn;
    private SQLiteDatabase BD2, BD2_S;


    private ArrayList<Classe_Perfil_Company> llistaCompanys;
    private RecyclerView recyclerViewCompanys;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_perfils_companys);

        globals = Globals.getInstance(this);
        //BD_conn = globals.getBD_conn();
        //BD_S_conn = globals.getBD_S_conn();
        //BD2 = BD_conn.getWritableDatabase();
        //BD2_S = BD_S_conn.getWritableDatabase();
        BD2 = globals.getBD_w();
        BD2_S = globals.getBD_S_w();

        //RECYCLERVIEW
        llistaCompanys = new ArrayList<>();
        recyclerViewCompanys = findViewById(R.id.recyclerView_companys);
        recyclerViewCompanys.setLayoutManager(new LinearLayoutManager(this));
        omplirllisaCompanys();
        AdaptadorCompanys adapter = new AdaptadorCompanys(llistaCompanys);
        recyclerViewCompanys.setAdapter(adapter);
    }

    private void omplirllisaCompanys() {
        Cursor cursor = BD2.rawQuery("SELECT "+Contract.Camp_Company_id+" FROM "+Contract.NOM_TAULA_Relacio_Companys_Infants,null);
        if(cursor.moveToFirst()){
            ArrayList<String> id_companys = new ArrayList<String>();
            do{
                if(id_companys.size()==0){
                    id_companys.add(cursor.getString(0));
                }else{
                    Boolean id_afegit=false;
                    for(int i=0;i<id_companys.size();i++){
                        if (cursor.getString(0).equals(id_companys.get(i))){
                            id_afegit=true;
                        }
                    }
                    if(!id_afegit){
                        id_companys.add(cursor.getString(0));
                    }
                }
            }while (cursor.moveToNext());

            for (int i=0; i<id_companys.size();i++){
                String id_company1[] = {id_companys.get(i)};
                cursor = BD2.rawQuery("SELECT * FROM "+Contract.NOM_TAULA_Relacio_Companys_Infants+" WHERE "+Contract.Camp_Company_id+" =?",id_company1);
                if (cursor.moveToFirst()){
                    String id = cursor.getString(0);
                    String Nom_C = cursor.getString(1);
                    String Cognoms_C = cursor.getString(2);
                    String Id_Infant[] = new String[cursor.getCount()];
                    String Nom_I[] = new String[cursor.getCount()];
                    String Cognoms_I[] = new String[cursor.getCount()];
                    String Relacio_CI[] = new String[cursor.getCount()];
                    int x=0;
                    do{
                        Id_Infant[x] = cursor.getString(3);
                        Nom_I[x] = cursor.getString(4);
                        Cognoms_I[x] = cursor.getString(5);
                        Relacio_CI[x] = cursor.getString(6);
                        x++;
                    }while (cursor.moveToNext());

                    Classe_Perfil_Company Company = new Classe_Perfil_Company(id,Nom_C,Cognoms_C,Id_Infant,Nom_I,Cognoms_I,Relacio_CI);
                    llistaCompanys.add(Company);
                }
            }
        }
        cursor.close();
    }
}

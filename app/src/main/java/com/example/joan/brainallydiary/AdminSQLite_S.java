package com.example.joan.brainallydiary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


//SERVIDOR
public class AdminSQLite_S extends SQLiteOpenHelper {
    public AdminSQLite_S(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Contract_S.TAULA_Usuaris_Info_Login);
        sqLiteDatabase.execSQL(Contract_S.TAULA_Usuaris_Info_Basica);
        sqLiteDatabase.execSQL(Contract_S.TAULA_Relacio_Usuari_Infants);
        sqLiteDatabase.execSQL(Contract_S.TAULA_Infants_Pendents_Compartir);
        sqLiteDatabase.execSQL(Contract_S.TAULA_Infants_Info_Basica);
        sqLiteDatabase.execSQL(Contract_S.TAULA_Infants_Info_Trastorn);
        sqLiteDatabase.execSQL(Contract_S.TAULA_Infants_Medicines);
        sqLiteDatabase.execSQL(Contract_S.TAULA_Activitats_Infants);
        sqLiteDatabase.execSQL(Contract_S.TAULA_Trofeus);
        sqLiteDatabase.execSQL(Contract_S.TAULA_Notificacions_Activitats);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

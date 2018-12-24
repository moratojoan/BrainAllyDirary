package com.example.joan.brainallydiary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


//LOCAL
public class AdminSQLite extends SQLiteOpenHelper {
    public AdminSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Contract.TAULA_Usuaris_Info_Login);
        sqLiteDatabase.execSQL(Contract.TAULA_Usuaris_Info_Basica);
        sqLiteDatabase.execSQL(Contract.TAULA_Relacio_Usuari_Infants);
        sqLiteDatabase.execSQL(Contract.TAULA_Infants_Info_Basica);
        sqLiteDatabase.execSQL(Contract.TAULA_Infants_Info_Trastorn);
        sqLiteDatabase.execSQL(Contract.TAULA_Infants_Medicines);
        sqLiteDatabase.execSQL(Contract.TAULA_Activitats_Infants);
        sqLiteDatabase.execSQL(Contract.TAULA_Relacio_Companys_Infants);
        sqLiteDatabase.execSQL(Contract.TAULA_Trofeus);
        sqLiteDatabase.execSQL(Contract.TAULA_Notificacions_Activitats);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

package com.example.joan.brainallydiary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class Globals {
    private static Globals instance;

    private Globals(){}

    public static synchronized Globals getInstance(Context c){
        if(instance == null){
            instance = new Globals();
            BD_conn = new AdminSQLite(c.getApplicationContext(),"Administrar",null,1);
            BD_S_conn = new AdminSQLite_S(c.getApplicationContext(),"Administrar_SERVIDOR",null,1);
            BD_w = BD_conn.getWritableDatabase();
            BD_S_w = BD_S_conn.getWritableDatabase();
        }
        return instance;
    }

    //BASE DE DADES
    private static AdminSQLite BD_conn;
    private static AdminSQLite_S BD_S_conn;

    private static SQLiteDatabase BD_w;
    private static SQLiteDatabase BD_S_w;

    public AdminSQLite getBD_conn() {
        return BD_conn;
    }
    public AdminSQLite_S getBD_S_conn() {
        return BD_S_conn;
    }

    public SQLiteDatabase getBD_w() {
        return BD_w;
    }
    public SQLiteDatabase getBD_S_w() {
        return BD_S_w;
    }

    public void close() {
        BD_conn.close();
        BD_S_conn.close();
        instance = null;
    }
    //BASE DE DADES//


    //CALENDARI
    private String Data_Seleccionada;
    public void setData_Seleccionada(String data_Seleccionada) {
        this.Data_Seleccionada = data_Seleccionada;
    }
    public String getData_Seleccionada() {
        return this.Data_Seleccionada;
    }

    private String Setmana_Seleccionada_1;
    public void setSetmana_Seleccionada_1(String setmana_Seleccionada_1) {
        Setmana_Seleccionada_1 = setmana_Seleccionada_1;
    }
    public String getSetmana_Seleccionada_1() {
        return Setmana_Seleccionada_1;
    }

    private String Setmana_Seleccionada_2;
    public void setSetmana_Seleccionada_2(String setmana_Seleccionada_2) {
        Setmana_Seleccionada_2 = setmana_Seleccionada_2;
    }
    public String getSetmana_Seleccionada_2() {
        return Setmana_Seleccionada_2;
    }

    private int index_spn_fill_seleccionat=0;
    public void setIndex_spn_fill_seleccionat(int index){
        this.index_spn_fill_seleccionat = index;
    }
    public int getIndex_spn_fill_seleccionat(){
        return this.index_spn_fill_seleccionat;
    }
    //CALENDARI


    //CLASSES USUARI, INFANTS I COMPANYS
    public Classe_Perfil_Usuari Usuari;
    public void setUsuari(Classe_Perfil_Usuari usuari) {
        this.Usuari = usuari;
    }
    public Classe_Perfil_Usuari getUsuari() {
        return this.Usuari;
    }

    private ArrayList<Classe_Perfil_Infant> llistaInfants;
    public void setLlistaInfants(ArrayList<Classe_Perfil_Infant> llistaInfants) {
        this.llistaInfants = llistaInfants;
    }
    public ArrayList<Classe_Perfil_Infant> getLlistaInfants() {
        return llistaInfants;
    }
    //CLASSES USUARI, INFANTS I COMPANYS


    //CLASSES ACTIVITATS
    private ArrayList<Classe_Activitat> llistaActivitats;
    public void setLlistaActivitats(ArrayList<Classe_Activitat> llistaActivitats) {
        this.llistaActivitats = llistaActivitats;
    }
    public ArrayList<Classe_Activitat> getLlistaActivitats() {
        return llistaActivitats;
    }
    //CLASSES ACTIVITATS

    //GESTOR TROFEUS
    public void SumarPuntsEmocions(Context c, String usuari[],String Registre, String Validat){
        Cursor cursor = BD_w.rawQuery("SELECT "+Contract.Camp_Totals+","+Contract.Camp_Mensuals+","+Contract.Camp_Setmanals+","+Contract.Camp_Diaris+","+Contract.Camp_Ultim_Dia_20+","+Contract.Camp_Contador_03+" FROM "+ Contract.NOM_TAULA_Trofeus,null);
        if (cursor.moveToFirst()){
            int totals = cursor.getInt(0);
            int mensuals = cursor.getInt(1);
            int setmanals = cursor.getInt(2);
            int diaris = cursor.getInt(3);

            if(Registre.equals("SI")){
                totals = totals+5;
                mensuals = mensuals+5;
                setmanals = setmanals+5;
                diaris = diaris+5;
            }
            if(Validat.equals("SI")){
                totals = totals+5;
                mensuals = mensuals+5;
                setmanals = setmanals+5;
                diaris = diaris+5;
            }
            if(Registre.equals("SI") && Validat.equals("SI")){
                Toast.makeText(c,"Molt bé!! Has guanyat 10 Punt!!",Toast.LENGTH_SHORT).show();
            }else if(Registre.equals("SI") || Validat.equals("SI")){
                Toast.makeText(c,"Molt bé!! Has guanyat 5 Punt!!",Toast.LENGTH_SHORT).show();
            }
            ContentValues punts_nous = new ContentValues();

            if(diaris>=20){
                //Comprovar si avui ja s'havia arribat
                Calendar calendar = Calendar.getInstance();
                int dia_avui = calendar.get(Calendar.DAY_OF_MONTH);
                int mes_avui = calendar.get(Calendar.MONTH)+1;
                int any_avui = calendar.get(Calendar.YEAR);
                String avui = arreglar_time_date(dia_avui)+"-"+arreglar_time_date(mes_avui)+"-"+String.valueOf(any_avui);
                String ultim_dia_20 = cursor.getString(4);
                if(!avui.equals(ultim_dia_20)){
                    //Avui és el primer cop que s'arriba a 20 punts, sumar 5 punts
                    punts_nous.put(Contract.Camp_Ultim_Dia_20,avui);
                    totals = totals+5;
                    mensuals = mensuals+5;
                    setmanals = setmanals+5;
                    diaris = diaris+5;
                    Toast.makeText(c,"Molt bé!! Has guanyat 5 Punt extres!!",Toast.LENGTH_SHORT).show();


                    //Comprovar si és el 3r dia seguit en arribar-hi
                    String ultim_dia_20_vector[] = ultim_dia_20.split("-");
                    int dia20 = Integer.parseInt(ultim_dia_20_vector[0]);
                    int mes20 = Integer.parseInt(ultim_dia_20_vector[1]);
                    int any20 = Integer.parseInt(ultim_dia_20_vector[2]);
                    calendar.set(any20,mes20-1,dia20);
                    int ultim_dia_mes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    //Sumem 1 dia, per comparar-lo amb avui. Si coincideixen és perquè ahir també es van aconseguir 20 punts.
                    if(dia20<ultim_dia_mes){
                        dia20=dia20+1;
                        ultim_dia_20_vector[0] = arreglar_time_date(dia20);
                    }else {
                        if(mes20<12){
                            mes20=mes20+1;
                            dia20 = 1;
                            ultim_dia_20_vector[0] = arreglar_time_date(dia20);
                            ultim_dia_20_vector[1] = arreglar_time_date(mes20);
                        }else{
                            any20=any20+1;
                            mes20 =1;
                            dia20=1;
                            ultim_dia_20_vector[0] = arreglar_time_date(dia20);
                            ultim_dia_20_vector[1] = arreglar_time_date(mes20);
                            ultim_dia_20_vector[2] = String.valueOf(any20);
                        }
                    }
                    ultim_dia_20=ultim_dia_20_vector[0]+"-"+ultim_dia_20_vector[1]+"-"+ultim_dia_20_vector[2];
                    int contador03 = 1;
                    if(ultim_dia_20.equals(avui)) {
                        //Ahir també es van fer 20 punts. Sumar 1 als contadors.
                        contador03 = cursor.getInt(5);
                        ControlPerseverancia(true,usuari);//És el primer cop al dia que s'arriba a 20 i ahir també s'hi havia arribat.
                        switch (contador03){
                            case 0:
                                contador03++;
                                break;
                            case 1:
                                contador03++;
                                break;
                            case 2:
                                //Avui serà el 3r dia seguit
                                contador03 =0; //Es reinicia
                                totals = totals+5;
                                mensuals = mensuals+5;
                                setmanals = setmanals+5;
                                diaris = diaris+5;
                                break;
                        }
                    }else{
                        ControlPerseverancia(false,usuari);//És el primer cop al dia que s'arriba a 20, però ahir no s'hi havia arribat.
                    }
                    punts_nous.put(Contract.Camp_Contador_03,contador03);
                }
            }

            punts_nous.put(Contract.Camp_Totals, totals);
            punts_nous.put(Contract.Camp_Mensuals, mensuals);
            punts_nous.put(Contract.Camp_Setmanals, setmanals);
            punts_nous.put(Contract.Camp_Diaris, diaris);
            BD_w.update(Contract.NOM_TAULA_Trofeus,punts_nous,null,null);
            BD_S_w.update(Contract_S.NOM_TAULA_Trofeus,punts_nous,Contract_S.Camp_Usuari_id+"=?",usuari);
        }
        cursor.close();
    }

    public void EntrarPrimerCopDia(String usuari[],Context c){
        Calendar calendar = Calendar.getInstance();
        int dia_avui = calendar.get(Calendar.DAY_OF_MONTH);
        int mes_avui = calendar.get(Calendar.MONTH)+1;
        int any_avui = calendar.get(Calendar.YEAR);
        String avui = arreglar_time_date(dia_avui)+"-"+arreglar_time_date(mes_avui)+"-"+String.valueOf(any_avui);
        Cursor cursor = BD_w.rawQuery("SELECT "+Contract.Camp_Totals+","+Contract.Camp_Mensuals+","+Contract.Camp_Setmanals+","+Contract.Camp_Ultim_Dia_Entrat+" FROM "+Contract.NOM_TAULA_Trofeus,null);
        if(cursor.moveToFirst()){
            String ultim_dia_entrat = cursor.getString(3);
            if (!avui.equals(ultim_dia_entrat)){
                //És el primer cop que s'entra a l'app avui:
                Toast.makeText(c,"Molt bé!! Has guanyat 1 Punt!!",Toast.LENGTH_SHORT).show();
                ContentValues dades = new ContentValues();
                dades.put(Contract.Camp_Totals,cursor.getInt(0)+1);
                dades.put(Contract.Camp_Mensuals,cursor.getInt(1)+1);
                dades.put(Contract.Camp_Setmanals,cursor.getInt(2)+1);
                dades.put(Contract.Camp_Diaris,1);
                dades.put(Contract.Camp_Ultim_Dia_Entrat, avui);
                BD_w.update(Contract.NOM_TAULA_Trofeus,dades,null,null);
                BD_S_w.update(Contract_S.NOM_TAULA_Trofeus,dades,Contract_S.Camp_Usuari_id+"=?",usuari);
                // comprovar si ha canviat de mes
                String[] dia_mes_any = ultim_dia_entrat.split("-");
                int dia_ultim = Integer.parseInt(dia_mes_any[0]);
                int mes_ultim = Integer.parseInt(dia_mes_any[1]);
                int any_ultim = Integer.parseInt(dia_mes_any[2]);
                if(any_ultim!=any_avui){
                    //S'ha canviat de d'any i per tant de mes
                    ContentValues punts_nous = new ContentValues();
                    int mensuals = 1;
                    punts_nous.put(Contract.Camp_Mensuals, mensuals);
                    BD_w.update(Contract.NOM_TAULA_Trofeus,punts_nous,null,null);
                    BD_S_w.update(Contract_S.NOM_TAULA_Trofeus,punts_nous,Contract_S.Camp_Usuari_id+"=?",usuari);
                }else{
                    //No ha canviat d'any, pero de mes?
                    if(mes_ultim!=mes_avui){
                        //S'ha canviat de mes
                        ContentValues punts_nous = new ContentValues();
                        int mensuals = 1;
                        punts_nous.put(Contract.Camp_Mensuals, mensuals);
                        BD_w.update(Contract.NOM_TAULA_Trofeus,punts_nous,null,null);
                        BD_S_w.update(Contract_S.NOM_TAULA_Trofeus,punts_nous,Contract_S.Camp_Usuari_id+"=?",usuari);
                    }
                }

                // comprovar si s'ha canviat de setmana
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(any_ultim,mes_ultim-1,dia_ultim);
                int dia_setmana = calendar1.get(Calendar.DAY_OF_WEEK);
                if (dia_setmana==1){
                    dia_setmana=7;
                }else{
                    dia_setmana = dia_setmana-1;
                }

                int ultim_dia_setmana = dia_ultim+(7-dia_setmana);
                int mes_ultim_dia_setmana = mes_ultim;
                int any_ultim_dia_setmana = any_ultim;

                int ultim_dia_mes = calendar1.getActualMaximum(Calendar.DAY_OF_MONTH);
                if(ultim_dia_mes<ultim_dia_setmana){
                    //L'últim dia de la setmana a canviat de mes
                    //També ha canviat d'any?
                    if(mes_ultim>12){
                        //Ha canviat d'any
                        mes_ultim_dia_setmana=1;
                        any_ultim_dia_setmana=any_ultim_dia_setmana+1;
                    }else {
                        //No ha canviat d'any
                        mes_ultim_dia_setmana = mes_ultim_dia_setmana+1;
                    }
                }
                //Avui es més gran que l'ultim dia de la setmana?
                long any_mes_dia_avui = any_avui*10000+mes_avui*100+dia_avui;
                long any_mes_dia_ultim_dia_setmana = any_ultim_dia_setmana*10000+mes_ultim_dia_setmana*100+ultim_dia_setmana;
                if(any_mes_dia_avui>any_mes_dia_ultim_dia_setmana){
                    //S'ha canviat de setmana
                    ContentValues punts_nous = new ContentValues();
                    int setmanals = 1;
                    punts_nous.put(Contract.Camp_Setmanals, setmanals);
                    BD_w.update(Contract.NOM_TAULA_Trofeus,punts_nous,null,null);
                    BD_S_w.update(Contract_S.NOM_TAULA_Trofeus,punts_nous,Contract_S.Camp_Usuari_id+"=?",usuari);
                }
            }
        }
        cursor.close();
    }

    private void ControlPerseverancia(Boolean si_no, String[] usuari){
        //20 punts=4activitats=1emociopunt+2activitats=2emocionspunt
        //Quan es crida aquesta funció es quan s'ha arribat a 20 punts.(true: ahir també, false:ahir no).
        //Per passar de 0-1:(5-1): 2 dies
        //Per passar de 1-2:     : 6 dies
        //Per passar de 2-3:     : 12 dies
        //Per passar de 3-4:     : 20 dies
        //Per passar de 4-5:     : 30 dies

        Cursor cursor = BD_w.rawQuery("SELECT "+Contract.Camp_lvl_perseverancia+","+Contract.Camp_Contador_perseverancia+","+Contract.Camp_RatingBar_perseverancia+" FROM "+Contract.NOM_TAULA_Trofeus,null);
        if(cursor.moveToFirst()){
            int lvl_perseverancia = cursor.getInt(0);
            int contador = cursor.getInt(1);
            int ratingBar = cursor.getInt(2);

            if(si_no){
                contador=contador+1;
                switch (ratingBar){
                    case 0:
                        if(contador>=2){
                            ratingBar=1;
                        }
                    case 1:
                        if(contador>=6){
                            ratingBar=2;
                        }
                        break;
                    case 2:
                        if(contador>=12){
                            ratingBar=3;
                        }
                        break;
                    case 3:
                        if(contador>=20){
                            ratingBar=4;
                        }
                        break;
                    case 4:
                        if(contador>=30){
                            ratingBar=5;
                        }
                        break;
                    case 5:
                        if(contador>=2){
                            ratingBar=1;
                            lvl_perseverancia=lvl_perseverancia+1;
                        }
                        break;
                }
            }else{
                contador=1;
            }
            ContentValues values = new ContentValues();
            values.put(Contract.Camp_lvl_perseverancia, lvl_perseverancia);
            values.put(Contract.Camp_Contador_perseverancia, contador);
            values.put(Contract.Camp_RatingBar_perseverancia, ratingBar);
            BD_w.update(Contract.NOM_TAULA_Trofeus,values,null,null);
            BD_S_w.update(Contract_S.NOM_TAULA_Trofeus,values,Contract_S.Camp_Usuari_id+"=?",usuari);
        }
        cursor.close();
    }

    public void ControlRegistres(String[] usuari){
        //Quan es crida aquesta funció es quan s'ha validat una emocio
        //Per passar de 0-1:(5-1): 8 emocions
        //Per passar de 1-2:     : 24 emocions
        //Per passar de 2-3:     : 48 emocions
        //Per passar de 3-4:     : 80 emocions
        //Per passar de 4-5:     : 120 emocions //4activitats en 30 dies ("el doble d'activitats que l'altre nivell").

        Cursor cursor = BD_w.rawQuery("SELECT "+Contract.Camp_lvl_registres+","+Contract.Camp_Contador_registres+","+Contract.Camp_RatingBar_registres+" FROM "+Contract.NOM_TAULA_Trofeus,null);
        if(cursor.moveToFirst()) {
            int lvl_registre = cursor.getInt(0);
            int contador = cursor.getInt(1);
            int ratingBar = cursor.getInt(2);

            contador = contador + 1;
            switch (ratingBar) {
                case 0:
                    if (contador >= 8) {
                        ratingBar = 1;
                    }
                    break;
                case 1:
                    if (contador >= 24) {
                        ratingBar = 2;
                    }
                    break;
                case 2:
                    if (contador >= 48) {
                        ratingBar = 3;
                    }
                    break;
                case 3:
                    if (contador >= 120) {
                        ratingBar = 4;
                    }
                    break;
                case 4:
                    if (contador >= 500) {
                        ratingBar = 5;
                    }
                    break;
                case 5:
                    if (contador >= 8) {
                        ratingBar = 1;
                        lvl_registre = lvl_registre + 1;
                    }
                    break;
            }
            ContentValues values = new ContentValues();
            values.put(Contract.Camp_lvl_registres, lvl_registre);
            values.put(Contract.Camp_Contador_registres, contador);
            values.put(Contract.Camp_RatingBar_registres, ratingBar);
            BD_w.update(Contract.NOM_TAULA_Trofeus, values, null, null);
            BD_S_w.update(Contract_S.NOM_TAULA_Trofeus, values, Contract_S.Camp_Usuari_id + "=?", usuari);
        }
        cursor.close();

    }
    //GESTOR TROFEUS


    //ELIMINAR NOTIFICACIONS PASSADES
    public void Eliminar_Notificacions_Passades_Infant(String infant_id){
        Calendar calendar = Calendar.getInstance();
        long any = calendar.get(Calendar.YEAR);
        long mes = calendar.get(Calendar.MONTH)+1;
        long dia = calendar.get(Calendar.DAY_OF_MONTH);
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minut = calendar.get(Calendar.MINUTE);
        long Comparador_Ara = minut+hora*100+dia*10000+mes*1000000+any*100000000;
        String[] Infant_Comparador_vector = {infant_id,String.valueOf(Comparador_Ara)};
        BD_w.delete(Contract.NOM_TAULA_Notificacions_Activitats,Contract.Camp_Infant_ID+"=? AND "+Contract.Camp_ComparadorF+" <?",Infant_Comparador_vector);
        BD_S_w.delete(Contract_S.NOM_TAULA_Notificacions_Activitats,Contract_S.Camp_Infant_ID+"=? AND "+Contract_S.Camp_ComparadorF+" <?",Infant_Comparador_vector);
    }
    //ELIMINAR NOTIFICACIONS PASSADES

    private static String arreglar_time_date(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }



    //ACTUALITZAR BASE DE DADES
    public void ActulitzarBD(Context context, String usuari_sessio){
        borrarTaulesLocals(context);
        omplirTaulesLocalsBuides(context, usuari_sessio);
    }

    private void borrarTaulesLocals(Context context) {
        BD_w.delete(Contract.NOM_TAULA_Usuari_Info_Login,null,null);
        BD_w.delete(Contract.NOM_TAULA_Usuari_Info_Basica,null,null);
        BD_w.delete(Contract.NOM_TAULA_Relacio_Usuari_Infant,null,null);
        BD_w.delete(Contract.NOM_TAULA_Infant_Info_Basica,null,null);
        BD_w.delete(Contract.NOM_TAULA_Infant_Info_Trastorn,null,null);
        BD_w.delete(Contract.NOM_TAULA_Infant_Medicines,null,null);
        BD_w.delete(Contract.NOM_TAULA_Activitats_Infants,null,null);
        BD_w.delete(Contract.NOM_TAULA_Relacio_Companys_Infants,null,null);
        BD_w.delete(Contract.NOM_TAULA_Trofeus,null,null);
        BD_w.delete(Contract.NOM_TAULA_Notificacions_Activitats,null,null);

        SharedPreferences preferences = context.getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_editor = preferences.edit();
        Obj_editor.putString("Sessió_actulitzada","Si");
        Obj_editor.apply();
    }

    public void omplirTaulesLocalsBuides(Context context, String usuari_sessio) {
        String usuari_sessio_vector[]={usuari_sessio};

        //Taula Usuari Info Login
        Cursor cursor1 = BD_S_w.rawQuery("SELECT * FROM "+Contract_S.NOM_TAULA_Usuari_Info_Login+" WHERE "+Contract_S.Camp_Usuari_id+" =?",usuari_sessio_vector);
        if (cursor1.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(Contract.Camp_Usuari_id,cursor1.getString(0));
            values.put(Contract.Camp_Email,cursor1.getString(1));
            values.put(Contract.Camp_Password,cursor1.getString(2));

            BD_w.insert(Contract.NOM_TAULA_Usuari_Info_Login,null,values);
            values.clear();
        }

        //Taula Usuari Info Basica
        cursor1 = BD_S_w.rawQuery("SELECT * FROM "+ Contract_S.NOM_TAULA_Usuari_Info_Basica+" WHERE "+Contract_S.Camp_Usuari_id+" =?",usuari_sessio_vector);
        if (cursor1.moveToFirst()){
            cursor1.moveToFirst();
            ContentValues values = new ContentValues();
            values.put(Contract.Camp_Nom_U,cursor1.getString(1));
            values.put(Contract.Camp_Cognoms_U,cursor1.getString(2));
            values.put(Contract.Camp_Data_Neixament,cursor1.getString(3));
            values.put(Contract.Camp_Genere,cursor1.getString(4));
            values.put(Contract.Camp_Pais,cursor1.getString(5));
            values.put(Contract.Camp_Codi_Postal,cursor1.getString(6));

            BD_w.insert(Contract.NOM_TAULA_Usuari_Info_Basica,null,values);
            values.clear();
        }

        //Taula Relació Usuari-Infants
        cursor1 = BD_S_w.rawQuery("SELECT * FROM "+ Contract_S.NOM_TAULA_Relacio_Usuari_Infant+" WHERE "+Contract_S.Camp_Usuari_id+" =?",usuari_sessio_vector);
        if (cursor1.moveToFirst()){
            ContentValues values = new ContentValues();
            do{
                values.put(Contract.Camp_Infant_ID,cursor1.getString(1));
                values.put(Contract.Camp_Relacio,cursor1.getString(2));

                BD_w.insert(Contract.NOM_TAULA_Relacio_Usuari_Infant,null,values);
                values.clear();
            }while (cursor1.moveToNext());
        }

        //Taules Infants Info Basica, Trastorn, Medicines, Activitats
        Cursor cursor2 = BD_w.rawQuery("SELECT "+Contract.Camp_Infant_ID+" FROM "+Contract.NOM_TAULA_Relacio_Usuari_Infant,null);
        if(cursor2.moveToFirst()){
            do{
                String infant_id[]={cursor2.getString(0)};
                cursor1 = BD_S_w.rawQuery("SELECT * FROM "+Contract_S.NOM_TAULA_Infant_Info_Basica+ " WHERE "+ Contract_S.Camp_Infant_ID+ " =?",infant_id);
                if (cursor1.moveToFirst()){
                    ContentValues values = new ContentValues();
                    do{
                        values.put(Contract.Camp_Infant_ID,cursor1.getString(0));
                        values.put(Contract.Camp_Nom_I,cursor1.getString(1));
                        values.put(Contract.Camp_Cognoms_I,cursor1.getString(2));
                        values.put(Contract.Camp_Data_Neixament,cursor1.getString(3));
                        values.put(Contract.Camp_Genere,cursor1.getString(4));
                        values.put(Contract.Camp_Pais,cursor1.getString(5));
                        values.put(Contract.Camp_Codi_Postal,cursor1.getString(6));

                        BD_w.insert(Contract.NOM_TAULA_Infant_Info_Basica,null,values);
                        values.clear();
                    }while (cursor1.moveToNext());
                }
                cursor1 = BD_S_w.rawQuery("SELECT * FROM "+Contract_S.NOM_TAULA_Infant_Info_Trastorn+ " WHERE "+ Contract_S.Camp_Infant_ID+ " =?",infant_id);
                if (cursor1.moveToFirst()){
                    ContentValues values = new ContentValues();
                    do{
                        values.put(Contract.Camp_Infant_ID,cursor1.getString(0));
                        values.put(Contract.Camp_Trastorn_Principal,cursor1.getString(1));

                        BD_w.insert(Contract.NOM_TAULA_Infant_Info_Trastorn,null,values);
                        values.clear();
                    }while (cursor1.moveToNext());
                }
                cursor1 = BD_S_w.rawQuery("SELECT * FROM "+Contract_S.NOM_TAULA_Infant_Medicines+ " WHERE "+ Contract_S.Camp_Infant_ID+ " =?",infant_id);
                if (cursor1.moveToFirst()){
                    ContentValues values = new ContentValues();
                    do{
                        values.put(Contract.Camp_Infant_ID,cursor1.getString(0));
                        values.put(Contract.Camp_Medicina,cursor1.getString(1));

                        BD_w.insert(Contract.NOM_TAULA_Infant_Medicines,null,values);
                        values.clear();
                    }while (cursor1.moveToNext());
                }
                cursor1 = BD_S_w.rawQuery("SELECT * FROM "+Contract_S.NOM_TAULA_Activitats_Infants+ " WHERE "+ Contract_S.Camp_Infant_ID+ " =?",infant_id);
                if (cursor1.moveToFirst()){
                    ContentValues values = new ContentValues();
                    do{
                        values.put(Contract.Camp_Id_Activitat,cursor1.getString(0));
                        values.put(Contract.Camp_Infant_ID,cursor1.getString(1));
                        values.put(Contract.Camp_Activitat,cursor1.getString(2));
                        values.put(Contract.Camp_ComparadorI,cursor1.getLong(3));
                        values.put(Contract.Camp_DI,cursor1.getString(4));
                        values.put(Contract.Camp_DF,cursor1.getString(5));
                        values.put(Contract.Camp_HI,cursor1.getString(6));
                        values.put(Contract.Camp_HF,cursor1.getString(7));
                        values.put(Contract.Camp_Index_Emocio,cursor1.getInt(8));
                        values.put(Contract.Camp_Activitat_o_Emocio,cursor1.getString(9));

                        BD_w.insert(Contract.NOM_TAULA_Activitats_Infants,null,values);
                        values.clear();
                    }while (cursor1.moveToNext());
                }

                //Taula Notificacions Activitats
                cursor1 = BD_S_w.rawQuery("SELECT * FROM "+Contract_S.NOM_TAULA_Notificacions_Activitats+" WHERE "+Contract_S.Camp_Infant_ID+" =?",infant_id);
                if (cursor1.moveToFirst()){
                    ContentValues values = new ContentValues();
                    do{
                        values.put(Contract.Camp_Infant_ID,cursor1.getString(0));
                        values.put(Contract.Camp_id_Notif,cursor1.getInt(1));
                        values.put(Contract.Camp_Titol,cursor1.getString(2));
                        values.put(Contract.Camp_Missatge,cursor1.getString(3));
                        values.put(Contract.Camp_ComparadorF,cursor1.getLong(4));
                        values.put(Contract.Camp_DF,cursor1.getString(5));
                        values.put(Contract.Camp_HF,cursor1.getString(6));

                        BD_w.insert(Contract.NOM_TAULA_Notificacions_Activitats,null,values);
                        values.clear();
                    }while (cursor1.moveToNext());
                    Eliminar_Notificacions_Passades_Infant(cursor2.getString(0));
                }
                startAlarmes(context);
            }while (cursor2.moveToNext());

            //Taula Companys
            //usuari_sessio
            cursor1 = BD_w.rawQuery("SELECT "+Contract.Camp_Infant_ID+" FROM "+Contract.NOM_TAULA_Relacio_Usuari_Infant,null);
            if (cursor1.moveToFirst()){
                do{
                    String infant_id[]={cursor1.getString(0)};
                    cursor2 = BD_S_w.rawQuery("SELECT "+Contract_S.Camp_Usuari_id+","+Contract_S.Camp_Relacio+" FROM "+Contract_S.NOM_TAULA_Relacio_Usuari_Infant+" WHERE "+Contract_S.Camp_Infant_ID+"=?",infant_id);
                    if (cursor2.moveToFirst()){
                        do{
                            if(!cursor2.getString(0).equals(usuari_sessio)){
                                //tinc id_company, id_infant, relacio_CI
                                String id_company = cursor2.getString(0);
                                String id_company_vector[]={id_company};
                                String id_infant = cursor1.getString(0);
                                String id_infant_vector[] = {id_infant};
                                String relacio_CI = cursor2.getString(1);
                                //necessito nom i cognoms de company i de infant
                                String Nom_C = "";
                                String Cognoms_C = "";
                                String Nom_I = "";
                                String Cognoms_I = "";
                                Cursor cursor3 = BD_S_w.rawQuery("SELECT "+Contract_S.Camp_Nom_U+","+Contract_S.Camp_Cognoms_U+" FROM "+Contract_S.NOM_TAULA_Usuari_Info_Basica+" WHERE "+Contract_S.Camp_Usuari_id+" =?",id_company_vector);
                                if(cursor3.moveToFirst()){
                                    Nom_C = cursor3.getString(0);
                                    Cognoms_C = cursor3.getString(1);
                                }
                                cursor3 = BD_w.rawQuery("SELECT "+Contract.Camp_Nom_I+","+Contract.Camp_Cognoms_I+" FROM "+Contract.NOM_TAULA_Infant_Info_Basica+" WHERE "+Contract.Camp_Infant_ID+" =?",id_infant_vector);
                                if(cursor3.moveToFirst()){
                                    Nom_I = cursor3.getString(0);
                                    Cognoms_I = cursor3.getString(1);
                                }
                                cursor3.close();

                                //Insertar dades a la taula  companys
                                ContentValues values = new ContentValues();
                                values.put(Contract.Camp_Company_id,id_company);
                                values.put(Contract.Camp_Nom_C,Nom_C);
                                values.put(Contract.Camp_Cognoms_C,Cognoms_C);
                                values.put(Contract.Camp_Infant_ID,id_infant);
                                values.put(Contract.Camp_Nom_I,Nom_I);
                                values.put(Contract.Camp_Cognoms_I,Cognoms_I);
                                values.put(Contract.Camp_Relacio_CI,relacio_CI);

                                BD_w.insert(Contract.NOM_TAULA_Relacio_Companys_Infants,null,values);
                                values.clear();
                            }
                        }while (cursor2.moveToNext());
                    }
                }while (cursor1.moveToNext());
            }
        }
        //Taula Trofeus
        cursor1 = BD_S_w.rawQuery("SELECT * FROM "+Contract_S.NOM_TAULA_Trofeus+" WHERE "+Contract_S.Camp_Usuari_id+" =?",usuari_sessio_vector);
        if (cursor1.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(Contract.Camp_Totals,cursor1.getInt(1));
            values.put(Contract.Camp_Mensuals,cursor1.getInt(2));
            values.put(Contract.Camp_Setmanals,cursor1.getInt(3));
            values.put(Contract.Camp_Diaris,cursor1.getInt(4));
            values.put(Contract.Camp_Ultim_Dia_Entrat,cursor1.getString(5));
            values.put(Contract.Camp_Ultim_Dia_20,cursor1.getString(6));
            values.put(Contract.Camp_Contador_03,cursor1.getInt(7));
            values.put(Contract.Camp_lvl_perseverancia,cursor1.getInt(8));
            values.put(Contract.Camp_Contador_perseverancia,cursor1.getInt(9));
            values.put(Contract.Camp_RatingBar_perseverancia,cursor1.getInt(10));
            values.put(Contract.Camp_lvl_registres,cursor1.getInt(11));
            values.put(Contract.Camp_Contador_registres,cursor1.getInt(12));
            values.put(Contract.Camp_RatingBar_registres,cursor1.getInt(13));

            BD_w.insert(Contract.NOM_TAULA_Trofeus,null,values);
            values.clear();
        }
        cursor1.close();
        cursor2.close();
    }

    private void startAlarmes(Context context) {
        Cursor cursor = BD_w.rawQuery("SELECT "+Contract.Camp_id_Notif+","+Contract.Camp_Titol+","+Contract.Camp_Missatge+","+Contract.Camp_DF+","+Contract.Camp_HF+" FROM "+Contract.NOM_TAULA_Notificacions_Activitats,null);
        if (cursor.moveToFirst()){
            do{
                int id_noti = cursor.getInt(0);
                String titol = cursor.getString(1);
                String message = cursor.getString(2);
                String DF = cursor.getString(3);
                String HF = cursor.getString(4);

                String dia_mes_anyF[] = DF.split("-");
                String hora_minF[] = HF.split(":");

                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR,Integer.parseInt(dia_mes_anyF[2]));
                c.set(Calendar.MONTH,Integer.parseInt(dia_mes_anyF[1])-1);
                c.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dia_mes_anyF[0]));
                c.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hora_minF[0]));
                c.set(Calendar.MINUTE,Integer.parseInt(hora_minF[1]));
                c.set(Calendar.SECOND,0);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(context, AlertReceiver.class);
                intent.putExtra("Titol",titol);
                intent.putExtra("Missatge",message);
                intent.putExtra("id_notif",id_noti);
                if(titol.equals("Medicina")){
                    intent.putExtra("Medicina","Medicina");
                }
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id_noti, intent ,PendingIntent.FLAG_UPDATE_CURRENT);

                if (c.before(Calendar.getInstance())){
                    c.add(Calendar.DATE, 1);
                }

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }
    //ACTUALITZAR BASE DE DADES

    //Taula previa Trofeus
    public void PrimersTrofeus(Context context){
        String usuari_id[]={"JoanMC","DaniCS","SuperMama_73","Julia1995","MariaMR","Usuari_fictici6","Usuari_fictici7","Usuari_fictici8","Usuari_fictici9","Usuari_fictici10"};
        int totals[]={2000,1000,700,450,200,120,80,60,40,20};
        for(int i=0;i<usuari_id.length;i++){
            ContentValues values = new ContentValues();
            values.put(Contract_S.Camp_Usuari_id,usuari_id[i]);
            BD_S_w.insert(Contract.NOM_TAULA_Usuari_Info_Login,null,values);
            values.put(Contract_S.Camp_Totals,totals[i]);
            values.put(Contract_S.Camp_Mensuals,0);
            values.put(Contract_S.Camp_Setmanals,0);
            values.put(Contract_S.Camp_Diaris,0);
            values.put(Contract_S.Camp_Ultim_Dia_Entrat,"");
            values.put(Contract_S.Camp_Ultim_Dia_20,"");
            values.put(Contract_S.Camp_Contador_03,0);
            values.put(Contract_S.Camp_lvl_perseverancia,0);
            values.put(Contract_S.Camp_Contador_perseverancia,0);
            values.put(Contract_S.Camp_RatingBar_perseverancia,0);
            values.put(Contract_S.Camp_lvl_registres,0);
            values.put(Contract_S.Camp_Contador_registres,0);
            values.put(Contract_S.Camp_RatingBar_registres,0);

            BD_S_w.insert(Contract.NOM_TAULA_Trofeus,null,values);
            values.clear();


        }
        SharedPreferences preferences = context.getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_editor = preferences.edit();
        Obj_editor.putString("trofeus","Si");
        Obj_editor.apply();
    }

}

package com.example.joan.brainallydiary;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;


public class Pantalla_Calendari_Fragment_Setmana extends Fragment {

    private Globals globals;
    private AdminSQLite BD_conn;
    private AdminSQLite_S BD_S_conn;
    private SQLiteDatabase BD2, BD2_S;

    private Spinner spn_infants;
    private Button Nova_Emocio, Nova_Activitat, Nova_Medicina;
    private ImageView btn_dreta, btn_esquerra;

    private ArrayList<String> llistaInfants;

    private TextView Setmana;

    private RecyclerView recyclerView_Setmana;
    private ArrayList<Classe_Activitat> llistaActivitats;

    private String usuari_sessio;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (globals != null){
                String setmana = globals.getSetmana_Seleccionada_1()+"  :  "+globals.getSetmana_Seleccionada_2();
                Setmana.setText(setmana);
                spn_infants.setSelection(globals.getIndex_spn_fill_seleccionat());
                actualitzar_recyclerview();
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pantalla_calendari_setmana, container, false);

        globals = Globals.getInstance(getActivity());
        //BD_conn = globals.getBD_conn();
        //BD_S_conn = globals.getBD_S_conn();
        //BD2 = BD_conn.getWritableDatabase();
        //BD2_S = BD_S_conn.getWritableDatabase();
        BD2 = globals.getBD_w();
        BD2_S = globals.getBD_S_w();

        SharedPreferences preferences = getActivity().getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
        usuari_sessio = preferences.getString("Usuari de la Sessió","");

        Setmana = view.findViewById(R.id.txt_calendari_setmana_data);
        String setmana = globals.getSetmana_Seleccionada_1()+"  :  "+globals.getSetmana_Seleccionada_2();
        Setmana.setText(setmana);

        spn_infants = view.findViewById(R.id.spn_calendari_setmana);
        llistaInfants = new ArrayList<>();
        omplirllisaInfants();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, llistaInfants);
        spn_infants.setAdapter(adapter2);
        spn_infants.setSelection(globals.getIndex_spn_fill_seleccionat());
        spn_infants.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                globals.setIndex_spn_fill_seleccionat(i);
                actualitzar_recyclerview();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_dreta = view.findViewById(R.id.img_calendari_setmana_dreta);
        btn_dreta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Primer dia de la setmana
                String setmana1[] = globals.getSetmana_Seleccionada_2().split("-");
                int dia = Integer.parseInt(setmana1[0]);
                int mes = Integer.parseInt(setmana1[1]);
                int any = Integer.parseInt(setmana1[2]);
                Calendar calendar = Calendar.getInstance();
                calendar.set(any,mes-1,dia);
                int ultim_dia_mes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                if (dia!=ultim_dia_mes){
                    //el primer dia de la següent setmana no canvia de mes
                    dia=dia+1;
                    String nova_setmana1 = arreglar_time_date(dia)+"-"+arreglar_time_date(mes)+"-"+String.valueOf(any);
                    globals.setSetmana_Seleccionada_1(nova_setmana1);
                    //L'últim dia de la setmana ha canviat de mes?
                    dia=dia+6;
                    if(dia<=ultim_dia_mes){
                        //L'últim dia de la setmana no ha canviat de mes
                        String nova_setmana2 = arreglar_time_date(dia)+"-"+arreglar_time_date(mes)+"-"+String.valueOf(any);
                        globals.setSetmana_Seleccionada_2(nova_setmana2);
                    }else{
                        //L'últim dia de la setmana ha canviat de mes
                        dia = dia-ultim_dia_mes;
                        //Ha canviat d'any?
                        if(mes==12){
                            //Ha canviat d'any
                            mes=1;
                            any=any+1;
                            String nova_setmana2 = arreglar_time_date(dia)+"-"+arreglar_time_date(mes)+"-"+String.valueOf(any);
                            globals.setSetmana_Seleccionada_2(nova_setmana2);
                        }else{
                            //No ha canviat d'any
                            mes=mes+1;
                            String nova_setmana2 = arreglar_time_date(dia)+"-"+arreglar_time_date(mes)+"-"+String.valueOf(any);
                            globals.setSetmana_Seleccionada_2(nova_setmana2);
                        }

                    }
                }else{
                    //el primer dia de la següent setmana canvia de mes
                    dia = 1;
                    //Canvia d'any o no?
                    if(mes==12){
                        //el primer dia de la següent setmana canvia d'any
                        mes=1;
                        any = any+1;
                        String nova_setmana1 = arreglar_time_date(dia)+"-"+arreglar_time_date(mes)+"-"+String.valueOf(any);
                        globals.setSetmana_Seleccionada_1(nova_setmana1);
                    }else{
                        //el primer dia de la següent setmana no canvia d'any
                        mes=mes+1;
                        String nova_setmana1 = arreglar_time_date(dia)+"-"+arreglar_time_date(mes)+"-"+String.valueOf(any);
                        globals.setSetmana_Seleccionada_1(nova_setmana1);
                    }
                    //si el primer dia ha canviat de mes, no cal comprovar l'ultim
                    dia = dia+6;
                    String nova_setmana2 = arreglar_time_date(dia)+"-"+arreglar_time_date(mes)+"-"+String.valueOf(any);
                    globals.setSetmana_Seleccionada_2(nova_setmana2);
                }
                String setmana = globals.getSetmana_Seleccionada_1()+"  :  "+globals.getSetmana_Seleccionada_2();
                Setmana.setText(setmana);
                actualitzar_recyclerview();
            }
        });

        btn_esquerra = view.findViewById(R.id.img_calendari_setmana_esquerra);
        btn_esquerra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Últim dia de la setmana
                String setmana1[] = globals.getSetmana_Seleccionada_1().split("-");
                int dia = Integer.parseInt(setmana1[0]);
                int mes = Integer.parseInt(setmana1[1]);
                int any = Integer.parseInt(setmana1[2]);
                if (dia==1){
                    //L'ultim dia de la setmana canvia de mes
                    Calendar calendar = Calendar.getInstance();
                    if(mes==1){
                        //També canvia d'any
                        calendar.set(any-1,11,1);
                        dia = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        mes = 12;
                        any = any-1;
                    }else{
                        //no canvia d'any
                        mes=mes-1;
                        calendar.set(any,mes-1,1);
                        dia = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    }
                    String nova_setmana2 = arreglar_time_date(dia)+"-"+arreglar_time_date(mes)+"-"+String.valueOf(any);
                    globals.setSetmana_Seleccionada_2(nova_setmana2);
                    //No cal comprovar si el primer dia de la setmana canvia de mes
                    dia = dia-6;
                    String nova_setmana1 = arreglar_time_date(dia)+"-"+arreglar_time_date(mes)+"-"+String.valueOf(any);
                    globals.setSetmana_Seleccionada_1(nova_setmana1);
                }else{
                    //L'últim dia de la setmana no canvia de mes
                    dia=dia-1;
                    String nova_setmana2 = arreglar_time_date(dia)+"-"+arreglar_time_date(mes)+"-"+String.valueOf(any);
                    globals.setSetmana_Seleccionada_2(nova_setmana2);
                    //El primer dia canviarà de mes?
                    if (dia<7){
                        //Canviarà de mes
                        //Canviarà d'any?
                        Calendar calendar = Calendar.getInstance();
                        if(mes==1){
                            //Canviarà d'any
                            mes=12;
                            any=any-1;
                            calendar.set(any,mes-1,1);
                            int ultim_dia_mes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                            dia = ultim_dia_mes-(6-dia);
                            String nova_setmana1 = arreglar_time_date(dia)+"-"+arreglar_time_date(mes)+"-"+String.valueOf(any);
                            globals.setSetmana_Seleccionada_1(nova_setmana1);
                        }else{
                            //No canviarà d'any
                            mes=mes-1;
                            calendar.set(any,mes-1,1);
                            int ultim_dia_mes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                            dia = ultim_dia_mes-(6-dia);
                            String nova_setmana1 = arreglar_time_date(dia)+"-"+arreglar_time_date(mes)+"-"+String.valueOf(any);
                            globals.setSetmana_Seleccionada_1(nova_setmana1);
                        }
                    }else{
                        //No canviarà de mes
                        dia=dia-6;
                        String nova_setmana1 = arreglar_time_date(dia)+"-"+arreglar_time_date(mes)+"-"+String.valueOf(any);
                        globals.setSetmana_Seleccionada_1(nova_setmana1);
                    }
                }
                String setmana = globals.getSetmana_Seleccionada_1()+"  :  "+globals.getSetmana_Seleccionada_2();
                Setmana.setText(setmana);
                actualitzar_recyclerview();
            }
        });

        Nova_Activitat = view.findViewById(R.id.btn_calendari_setmana_activitat);
        Nova_Emocio = view.findViewById(R.id.btn_calendari_setmana_emocio);
        Nova_Medicina = view.findViewById(R.id.btn_calendari_setmana_medicina);
        Nova_Activitat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!spn_infants.getSelectedItem().toString().equals("Infants")){
                    Intent intent = new Intent(getActivity(),Pantalla_Nova_Activitat_Activity.class);
                    intent.putExtra("Infant",spn_infants.getSelectedItem().toString());
                    intent.putExtra("Titol_pantalla","Activitat");
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(),"No s'ha introduit cap infant",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Nova_Emocio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!spn_infants.getSelectedItem().toString().equals("Infants")){
                    Calendar calendar = Calendar.getInstance();
                    int any_max = calendar.get(Calendar.YEAR);
                    int mes_max = calendar.get(Calendar.MONTH);
                    int dia_max = calendar.get(Calendar.DAY_OF_MONTH);
                    int data_max = any_max*10000+mes_max*100+dia_max;

                    String data_seleccionada = globals.getData_Seleccionada();
                    String data_seleccionada1[] = data_seleccionada.split("-");
                    int day = Integer.parseInt(data_seleccionada1[0]);
                    int month = Integer.parseInt(data_seleccionada1[1])-1;
                    int year = Integer.parseInt(data_seleccionada1[2]);
                    int data_seleccionada2 = year*10000+month*100+day;
                    if (data_seleccionada2<=data_max){
                        Intent intent = new Intent(getActivity(),Pantalla_Nova_Activitat_Activity.class);
                        intent.putExtra("Infant",spn_infants.getSelectedItem().toString());
                        intent.putExtra("Titol_pantalla","Emoció puntual");
                        startActivity(intent);
                    }else{
                        Toast.makeText(getContext(), "No es pot posar una data futura", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(),"No s'ha introduit cap infant",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Nova_Medicina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!spn_infants.getSelectedItem().toString().equals("Infants")){
                    Intent intent = new Intent(getActivity(),Pantalla_Nova_Medicina_Activity.class);
                    intent.putExtra("Infant",spn_infants.getSelectedItem().toString());
                    intent.putExtra("Titol_pantalla","Medicina");
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(),"No s'ha introduit cap infant",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //RECYCLERVIEW
        recyclerView_Setmana = view.findViewById(R.id.recyclerView_calendari_setmana);
        actualitzar_recyclerview();
        return view;
    }

    private void actualitzar_recyclerview() {
        llistaActivitats = new ArrayList<Classe_Activitat>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_Setmana.setLayoutManager(linearLayoutManager);
        omplirllisaActivitats();
        AdaptadorActivitatsDia adapter = new AdaptadorActivitatsDia(llistaActivitats,BD2,BD2_S,globals,usuari_sessio,getContext());
        recyclerView_Setmana.setAdapter(adapter);
        String data_actual = globals.getData_Seleccionada();
        int pos = 0;
        Boolean posGuardada = false;
        for (int i=0; i<llistaActivitats.size();i++){
            if(llistaActivitats.get(i).getDI().equals(data_actual)){
                if(!posGuardada){
                    pos =i;
                    posGuardada=true;
                }
            }
        }
        linearLayoutManager.scrollToPosition(pos);
    }

    private void omplirllisaActivitats() {
        String NomI, CognomsI;
        //String data_seleccionada[] = {globals.getData_Seleccionada()};
        String setmana_seleccionada1[] = globals.getSetmana_Seleccionada_1().split("-");
        String setmana_seleccionada2[] = globals.getSetmana_Seleccionada_2().split("-");
        String setmana_seleccionada12 = setmana_seleccionada1[2]+setmana_seleccionada1[1]+setmana_seleccionada1[0];
        String setmana_seleccionada22 = setmana_seleccionada2[2]+setmana_seleccionada2[1]+setmana_seleccionada2[0];
        long setmana_seleccionada13 = Long.parseLong(setmana_seleccionada12)*10000;
        long setmana_seleccionada23 = Long.parseLong(setmana_seleccionada22)*10000;
        setmana_seleccionada23 = setmana_seleccionada23 + 9999;
        String data_seleccionada[] = {String.valueOf(setmana_seleccionada13),String.valueOf(setmana_seleccionada23)};


        if (!spn_infants.getSelectedItem().toString().equals("No s'ha introduit cap infant")){
            if (llistaInfants.size()==1){
                Cursor cursor = BD2.rawQuery("SELECT * FROM "+Contract.NOM_TAULA_Activitats_Infants+" WHERE "+Contract.Camp_ComparadorI+" BETWEEN ? AND ?",data_seleccionada);
                if (cursor.moveToFirst()){
                    String infant_id[]= {cursor.getString(1)};
                    Cursor cursor1 = BD2.rawQuery("SELECT "+Contract.Camp_Nom_I+","+Contract.Camp_Cognoms_I+" FROM "+Contract.NOM_TAULA_Infant_Info_Basica+" WHERE "+Contract.Camp_Infant_ID+" =?",infant_id);
                    if(cursor1.moveToFirst()){
                        do{
                            Classe_Activitat Activitat = new Classe_Activitat(cursor.getString(0),cursor.getString(1),cursor1.getString(0),cursor1.getString(1),cursor.getString(2),cursor.getLong(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getInt(8),cursor.getString(9));
                            llistaActivitats.add(Activitat);
                        }while (cursor.moveToNext());
                    }
                    cursor1.close();
                }
                cursor.close();
            }else{
                if (spn_infants.getSelectedItem().toString().equals("Tots els infants")){
                    Cursor cursor = BD2.rawQuery("SELECT * FROM "+Contract.NOM_TAULA_Activitats_Infants+" WHERE "+Contract.Camp_ComparadorI+" BETWEEN ? AND ?",data_seleccionada);
                    if (cursor.moveToFirst()){
                        do{
                            String infant_id[]= {cursor.getString(1)};
                            Cursor cursor1 = BD2.rawQuery("SELECT "+Contract.Camp_Nom_I+","+Contract.Camp_Cognoms_I+" FROM "+Contract.NOM_TAULA_Infant_Info_Basica+" WHERE "+Contract.Camp_Infant_ID+" =?",infant_id);
                            if(cursor1.moveToFirst()){
                                Classe_Activitat Activitat = new Classe_Activitat(cursor.getString(0),cursor.getString(1),cursor1.getString(0),cursor1.getString(1),cursor.getString(2),cursor.getLong(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getInt(8),cursor.getString(9));
                                llistaActivitats.add(Activitat);
                            }
                            cursor1.close();
                        }while (cursor.moveToNext());
                    }
                    cursor.close();
                }else{
                    String nomI_usuariI[] = spn_infants.getSelectedItem().toString().split(" ");
                    NomI = nomI_usuariI[0];
                    CognomsI = nomI_usuariI[1];
                    if (nomI_usuariI.length == 3){
                        CognomsI = CognomsI+" "+nomI_usuariI[2];
                    }
                    String nom_cognoms[]={NomI,CognomsI};
                    Cursor cursor = BD2.rawQuery("SELECT "+Contract.Camp_Infant_ID+" FROM "+Contract.NOM_TAULA_Infant_Info_Basica+" WHERE "+Contract.Camp_Nom_I+ " =? AND "+Contract_S.Camp_Cognoms_I+" =?",nom_cognoms);
                    if (cursor.moveToFirst()){
                        String dades[]={cursor.getString(0),String.valueOf(setmana_seleccionada13),String.valueOf(setmana_seleccionada23)};
                        cursor = BD2.rawQuery("SELECT * FROM "+ Contract.NOM_TAULA_Activitats_Infants+" WHERE "+Contract.Camp_Infant_ID+" =? AND "+Contract.Camp_ComparadorI+" BETWEEN ? AND ?",dades);
                        if(cursor.moveToFirst()){
                            do{
                                Classe_Activitat Activitat = new Classe_Activitat(cursor.getString(0),cursor.getString(1),NomI,CognomsI,cursor.getString(2),cursor.getLong(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getInt(8),cursor.getString(9));
                                llistaActivitats.add(Activitat);
                            }while (cursor.moveToNext());
                        }
                    }
                    cursor.close();
                }
            }
        }
        Collections.sort(llistaActivitats, new Comparator<Classe_Activitat>() {
            @Override
            public int compare(Classe_Activitat p1, Classe_Activitat p2) {
                return Long.compare(p1.getHI_comparador(), p2.getHI_comparador());
            }
        });
    }

    private void omplirllisaInfants() {
        Cursor cursor = BD2.rawQuery("SELECT "+Contract.Camp_Nom_I+","+Contract.Camp_Cognoms_I+" FROM "+Contract.NOM_TAULA_Infant_Info_Basica,null);
        if(cursor.getCount()>1){
            cursor.moveToFirst();
            llistaInfants.add("Tots els infants");
            do{
                llistaInfants.add(cursor.getString(0)+" "+cursor.getString(1));
            }while (cursor.moveToNext());
        } else if(cursor.getCount() == 1){
            cursor.moveToFirst();
            llistaInfants.add(cursor.getString(0)+" "+cursor.getString(1));
        } else{
            llistaInfants.add("Infants");
        }
        cursor.close();
    }

    private static String arreglar_time_date(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
}

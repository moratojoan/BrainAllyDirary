package com.example.joan.brainallydiary;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


public class Pantalla_Calendari_Fragment_Mes extends Fragment{


    private String DataSeleccionada, DiaSetmana1, DiaSetmana2;
    private Globals globals;
    private AdminSQLite BD_conn;
    private AdminSQLite_S BD_S_conn;
    private SQLiteDatabase BD2, BD2_S;

    private CalendarView calendarView;
    private Spinner spn_infants;
    private Button Nova_Emocio, Nova_Activitat, Nova_Medicina;

    private ArrayList<String> llistaInfants;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (globals != null){
                spn_infants.setSelection(globals.getIndex_spn_fill_seleccionat());
            }
        }
    }

    @SuppressLint("CutPasteId")
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pantalla_calendari_mes, container, false);


        globals = Globals.getInstance(getActivity());
        //BD_conn = globals.getBD_conn();
        //BD_S_conn = globals.getBD_S_conn();
        //BD2 = BD_conn.getWritableDatabase();
        //BD2_S = BD_S_conn.getWritableDatabase();
        BD2 = globals.getBD_w();
        BD2_S = globals.getBD_S_w();

        spn_infants = view.findViewById(R.id.spn_calendari_mes);
        llistaInfants = new ArrayList<>();
        omplirllisaInfants();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, llistaInfants);
        spn_infants.setAdapter(adapter2);
        spn_infants.setSelection(globals.getIndex_spn_fill_seleccionat());
        spn_infants.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                globals.setIndex_spn_fill_seleccionat(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Calendar c = Calendar.getInstance();
        int dia= c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH);
        int any = c.get(Calendar.YEAR);
        seleccionarData(dia,mes,any);
        seleccionarSetmana(dia,mes,any);

        calendarView = view.findViewById(R.id.calenarview_calendari_mes);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                int dia= i2;
                int mes = i1;
                int any = i;
                seleccionarData(dia,mes,any);
                seleccionarSetmana(dia,mes,any);
            }
        });

        Nova_Activitat = view.findViewById(R.id.btn_calendari_mes_activitat);
        Nova_Emocio = view.findViewById(R.id.btn_calendari_mes_emocio);
        Nova_Medicina = view.findViewById(R.id.btn_calendari_mes_medicina);
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
        SharedPreferences preferences = getContext().getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
        String usuari_sessio2 = preferences.getString("Usuari de la Sessió","");
        String usuari_sessio_vector[] = {usuari_sessio2};
        Cursor cursor2 = BD2_S.rawQuery("SELECT * FROM "+Contract_S.NOM_TAULA_Infants_Pendents_Compartir+" WHERE "+Contract_S.Camp_Usuari_Receptor_id+" =?",usuari_sessio_vector);
        if(!cursor2.moveToFirst()){
            if(spn_infants.getSelectedItem().toString().equals("Infants")){
                alertdialog();
            }
        }
        cursor2.close();


        return view;
    }


    private void alertdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getContext(), Pantalla_Perfils_Infants_Activity.class);
                            startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setTitle("Crear el perfil d'un infant!")
                .setMessage("El vols crear ara?");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void seleccionarSetmana(int dia, int mes, int any) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(any,mes,dia);
        int dia_setmana = calendar.get(Calendar.DAY_OF_WEEK);
        if (dia_setmana==1){
            dia_setmana=7;
        }else{
            dia_setmana = dia_setmana-1;
        }
        //Primer dia de la setmana:
        switch (dia_setmana){
            case 1:
                DiaSetmana1 = DataSeleccionada;
            default:
                int dia1 = dia - (dia_setmana-1);
                if (dia1>0){
                    DiaSetmana1 = arreglar_time_date(dia1)+"-"+arreglar_time_date(mes+1)+"-"+String.valueOf(any);
                }else{
                    //hem canviat de mes
                    Calendar calendar1 = Calendar.getInstance();
                    if(mes>0){
                        //no hem canviat d'any
                        calendar1.set(any,mes-1,1);
                        int ultim_dia_mes_previ = calendar1.getActualMaximum(Calendar.DAY_OF_MONTH);
                        dia1 = ultim_dia_mes_previ+dia1;
                        DiaSetmana1 = arreglar_time_date(dia1)+"-"+arreglar_time_date((mes+1)-1)+"-"+String.valueOf(any);
                    }else{
                        //hem canviat d'any
                        calendar1.set(any-1,11,1);
                        int ultim_dia_mes_previ = calendar1.getActualMaximum(Calendar.DAY_OF_MONTH);
                        dia1 = ultim_dia_mes_previ+dia1;
                        DiaSetmana1 = arreglar_time_date(dia1)+"-"+arreglar_time_date(12)+"-"+String.valueOf(any-1);
                    }
                }
                break;
        }
        globals.setSetmana_Seleccionada_1(DiaSetmana1);

        //Últim dia de la setmana:
        int ultim_dia_mes_actual = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        switch (dia_setmana){
            case 7:
                DiaSetmana2 = DataSeleccionada;
                break;
            default:
                int dia2 = dia + (7-dia_setmana);
                if(dia2>ultim_dia_mes_actual){
                    //Hem canviat de mes
                    if(mes==11){
                        //hem canviat d'any
                        dia2 = dia2-ultim_dia_mes_actual;
                        DiaSetmana2 = arreglar_time_date(dia2)+"-"+arreglar_time_date(1)+"-"+String.valueOf(any+1);
                    }else{
                        //no hem canviat d'any
                        dia2 = dia2-ultim_dia_mes_actual;
                        DiaSetmana2 = arreglar_time_date(dia2)+"-"+arreglar_time_date((mes+1)+1)+"-"+String.valueOf(any);
                    }
                }else{
                    //No hem canviat de mes
                    DiaSetmana2 = arreglar_time_date(dia2)+"-"+arreglar_time_date(mes+1)+"-"+String.valueOf(any);
                }
                break;
        }
        globals.setSetmana_Seleccionada_2(DiaSetmana2);
    }

    private void seleccionarData(int dia, int mes, int any) {
        DataSeleccionada = arreglar_time_date(dia)+"-"+arreglar_time_date(mes+1)+"-"+String.valueOf(any);
        globals.setData_Seleccionada(DataSeleccionada);
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

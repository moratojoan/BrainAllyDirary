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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


public class Pantalla_Calendari_Fragment_Dia extends Fragment {

    private Globals globals;
    private AdminSQLite BD_conn;
    private AdminSQLite_S BD_S_conn;
    private SQLiteDatabase BD2, BD2_S;



    private Spinner spn_infants;
    private Button Nova_Emocio, Nova_Activitat, Nova_Medicina;
    private ImageView btn_dreta, btn_esquerra;

    private ArrayList<String> llistaInfants;

    private TextView Dia;

    private RecyclerView recyclerView_Dia;
    private ArrayList<Classe_Activitat> llistaActivitats;

    private String usuari_sessio;

    private RadioButton rb_tot, rb_medicines;
    private RadioGroup rg_filtre;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (globals != null){
                Dia.setText(globals.getData_Seleccionada());
                spn_infants.setSelection(globals.getIndex_spn_fill_seleccionat());
                actualitzar_recyclerview();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //FragmentPantallaCalendariDiaBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_pantalla_calendari_dia,container,false);
        //View view = binding.getRoot();
        View view = inflater.inflate(R.layout.fragment_pantalla_calendari_dia, container, false);

        globals = Globals.getInstance(getActivity());
        //BD_conn = globals.getBD_conn();
        //BD_S_conn = globals.getBD_S_conn();
        //BD2 = BD_conn.getWritableDatabase();
        //BD2_S = BD_S_conn.getWritableDatabase();
        BD2 = globals.getBD_w();
        BD2_S = globals.getBD_S_w();


        SharedPreferences preferences = getActivity().getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
        usuari_sessio = preferences.getString("Usuari de la Sessió","");

        Dia = view.findViewById(R.id.txt_calendari_dia_data);
        //binding.setGlobals(globals);
        Dia.setText(globals.getData_Seleccionada());

        spn_infants = view.findViewById(R.id.spn_calendari_dia);
        llistaInfants = new ArrayList<>();
        omplirllistaInfants();
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


        Nova_Activitat = view.findViewById(R.id.btn_calendari_dia_activitat);
        Nova_Emocio = view.findViewById(R.id.btn_calendari_dia_emocio);
        Nova_Medicina = view.findViewById(R.id.btn_calendari_dia_medicina);

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

        btn_dreta = view.findViewById(R.id.img_calendari_dia_dreta);
        btn_esquerra = view.findViewById(R.id.img_calendari_dia_esquerra);
        btn_dreta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                String dataseleccionada = globals.getData_Seleccionada();
                String dia_mes_any[] = dataseleccionada.split("-");
                int dia = Integer.parseInt(dia_mes_any[0]);
                int mes = Integer.parseInt(dia_mes_any[1]);
                int any = Integer.parseInt(dia_mes_any[2]);
                calendar.set(any,mes-1,dia);
                int ultim_dia_mes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                if(dia<ultim_dia_mes){
                    dia=dia+1;
                    dia_mes_any[0] = arreglar_time_date(dia);
                }else {
                    if(mes<12){
                        mes=mes+1;
                        dia = 1;
                        dia_mes_any[0] = arreglar_time_date(dia);
                        dia_mes_any[1] = arreglar_time_date(mes);
                    }else{
                        any=any+1;
                        mes =1;
                        dia=1;
                        dia_mes_any[0] = arreglar_time_date(dia);
                        dia_mes_any[1] = arreglar_time_date(mes);
                        dia_mes_any[2] = String.valueOf(any);
                    }
                }
                globals.setData_Seleccionada(dia_mes_any[0]+"-"+dia_mes_any[1]+"-"+dia_mes_any[2]);
                Dia.setText(globals.getData_Seleccionada());
                actualitzar_recyclerview();
            }
        });
        btn_esquerra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                String dataseleccionada = globals.getData_Seleccionada();
                String dia_mes_any[] = dataseleccionada.split("-");
                int dia = Integer.parseInt(dia_mes_any[0]);
                int mes = Integer.parseInt(dia_mes_any[1]);
                int any = Integer.parseInt(dia_mes_any[2]);
                calendar.set(any,mes-1,dia);
                if(dia>1){
                    dia=dia-1;
                    dia_mes_any[0] = arreglar_time_date(dia);
                }else {
                    if(mes>1){
                        mes=mes-1;
                        calendar.set(any,mes-1,1);
                        dia = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        dia_mes_any[0] = arreglar_time_date(dia);
                        dia_mes_any[1] = arreglar_time_date(mes);
                    }else{
                        any=any-1;
                        mes =12;
                        calendar.set(any,mes-1,1);
                        dia = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        dia_mes_any[0] = arreglar_time_date(dia);
                        dia_mes_any[1] = arreglar_time_date(mes);
                        dia_mes_any[2] = String.valueOf(any);
                    }
                }
                globals.setData_Seleccionada(dia_mes_any[0]+"-"+dia_mes_any[1]+"-"+dia_mes_any[2]);
                Dia.setText(globals.getData_Seleccionada());
                actualitzar_recyclerview();
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
        recyclerView_Dia = view.findViewById(R.id.recyclerView_calendari_dia);
        //actualitzar_recyclerview();

        //RadioButtons
        rb_tot = view.findViewById(R.id.radiobutton_calendari_dia_tot);
        rb_medicines = view.findViewById(R.id.radiobutton_calendari_dia_medicina);
        rb_tot.setChecked(true);
        rg_filtre = view.findViewById(R.id.RadioGroup_Filtre);
        rg_filtre.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                actualitzar_recyclerview();
            }
        });

        return view;
    }

    private void actualitzar_recyclerview() {
        llistaActivitats = new ArrayList<Classe_Activitat>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_Dia.setLayoutManager(linearLayoutManager);
        if(rb_tot.isChecked()){
            omplirllistaActivitats();
        }else{
            omplirllistaActivitatsMedicines();
        }

        AdaptadorActivitatsDia adapter = new AdaptadorActivitatsDia(llistaActivitats,BD2,BD2_S,globals,usuari_sessio,getContext());
        recyclerView_Dia.setAdapter(adapter);

        if(llistaActivitats.size()>0){
            String data_actual = globals.getData_Seleccionada();
            Calendar calendar = Calendar.getInstance();
            int hora = calendar.get(Calendar.HOUR_OF_DAY);
            int minuts = calendar.get(Calendar.MINUTE);
            int hora_actual = hora*100+minuts;
            int pos = 0;
            if(data_actual.equals(llistaActivitats.get(0).getDI())){
                Boolean stop =false;
                for (int i=0; i<llistaActivitats.size();i++){
                    String hora_minut[] = llistaActivitats.get(i).getHI().split(":");
                    int hora2=Integer.parseInt(hora_minut[0]);
                    int minuts2=Integer.parseInt(hora_minut[1]);
                    int hora_minuts2 = hora2*100+minuts2;
                    if(!stop){
                        if(llistaActivitats.get(i).getIndex_Emocio()>0){
                            if(hora_minuts2<hora_actual){
                                pos =i;
                            }
                        }else{
                            stop = true;
                        }
                    }
                }
                linearLayoutManager.scrollToPosition(pos+1);
            }

        }

    }

    private void omplirllistaActivitatsMedicines() {
        String NomI, CognomsI;
        String data_seleccionada_medicines[] = {globals.getData_Seleccionada(),"Medicina"};
        if (!spn_infants.getSelectedItem().toString().equals("No s'ha introduit cap infant")){
            if (llistaInfants.size()==1){
                Cursor cursor = BD2.rawQuery("SELECT * FROM "+Contract.NOM_TAULA_Activitats_Infants+" WHERE "+Contract.Camp_DI+" =? AND "+Contract.Camp_Activitat_o_Emocio+" =?",data_seleccionada_medicines);
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
                    Cursor cursor = BD2.rawQuery("SELECT * FROM "+Contract.NOM_TAULA_Activitats_Infants+" WHERE "+Contract.Camp_DI+" =? AND "+Contract.Camp_Activitat_o_Emocio+" =?",data_seleccionada_medicines);
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
                        String dades[]={cursor.getString(0),globals.getData_Seleccionada(),"Medicina"};
                        cursor = BD2.rawQuery("SELECT * FROM "+ Contract.NOM_TAULA_Activitats_Infants+" WHERE "+Contract.Camp_Infant_ID+" =? AND "+Contract.Camp_DI+" =? AND "+Contract.Camp_Activitat_o_Emocio+" =?",dades);
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

    private void omplirllistaActivitats() {
        String NomI, CognomsI;
        String data_seleccionada[] = {globals.getData_Seleccionada()};
        if (!spn_infants.getSelectedItem().toString().equals("No s'ha introduit cap infant")){
            if (llistaInfants.size()==1){
                Cursor cursor = BD2.rawQuery("SELECT * FROM "+Contract.NOM_TAULA_Activitats_Infants+" WHERE "+Contract.Camp_DI+" =?",data_seleccionada);
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
                    Cursor cursor = BD2.rawQuery("SELECT * FROM "+Contract.NOM_TAULA_Activitats_Infants+" WHERE "+Contract.Camp_DI+" =?",data_seleccionada);
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
                        String dades[]={cursor.getString(0),globals.getData_Seleccionada()};
                        cursor = BD2.rawQuery("SELECT * FROM "+ Contract.NOM_TAULA_Activitats_Infants+" WHERE "+Contract.Camp_Infant_ID+" =? AND "+Contract.Camp_DI+" =?",dades);
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

    private void omplirllistaInfants() {
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

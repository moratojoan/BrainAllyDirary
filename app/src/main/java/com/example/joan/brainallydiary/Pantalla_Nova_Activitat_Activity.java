package com.example.joan.brainallydiary;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class Pantalla_Nova_Activitat_Activity extends AppCompatActivity implements View.OnClickListener {

    private Globals globals;
    private AdminSQLite BD_conn;
    private AdminSQLite_S BD_S_conn;
    private SQLiteDatabase BD2, BD2_S;

    private Spinner spn_infants, spn_activitats;
    private ArrayList<String> llistaInfants, llistaActivitats;

    private TextView DI, DF, HI, HF;

    private String Infant;
    private int index_infant;

    private Button btn_Guardar;

    private String RandomID;
    private int id_noti;

    private int dia_Seleccionat,mes_Seleccionat,any_Seleccionat;
    private int dia_max_emocio, mes_max_emocio, any_max_emocio, data_max_emocio;
    private int horaI, minutosI, horaF, minutosF;
    private Boolean horaCorrecte = true;

    private TextView txt_emocio, txt_informacio;
    private int Index_Emocio=-1;

    private String Titol_pantalla;

    private CardView c0,c1,c2,c3,c4;
    private ImageView img_plorant, img_trist, img_neutre, img_content, img_super;

    //Variables per EDITAR:
    private String Activitat_id_bundle, Activitat_bundle, DI_bundle, DF_bundle, HI_bundle, HF_bundle;
    private int Index_Emocio_bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_nova_activitat);

        //Globals
        globals = Globals.getInstance(this);
        //BD_conn = globals.getBD_conn();
        //BD_S_conn = globals.getBD_S_conn();
        //BD2 = BD_conn.getWritableDatabase();
        //BD2_S = BD_S_conn.getWritableDatabase();
        BD2 = globals.getBD_w();
        BD2_S = globals.getBD_S_w();

        //Connectar xml i java
        spn_infants = findViewById(R.id.spn_activitat_infants);
        spn_activitats = findViewById(R.id.spn_activitat_activitats);
        txt_emocio = findViewById(R.id.txt_emocio);
        c0 = findViewById(R.id.card_activitat_0);
        c1 = findViewById(R.id.card_activitat_1);
        c2 = findViewById(R.id.card_activitat_2);
        c3 = findViewById(R.id.card_activitat_3);
        c4 = findViewById(R.id.card_activitat_4);
        txt_informacio= findViewById(R.id.txt_activitat_informació);
        DI = findViewById(R.id.txt_activitat_DI);
        DF = findViewById(R.id.txt_activitat_DF);
        HI = findViewById(R.id.txt_activitat_HI);
        HF = findViewById(R.id.txt_activitat_HF);
        btn_Guardar = findViewById(R.id.btn_activitat_Guardar);

        img_plorant = findViewById(R.id.img_plorant);
        img_trist = findViewById(R.id.img_trist);
        img_neutre = findViewById(R.id.img_neutre);
        img_content = findViewById(R.id.img_content);
        img_super = findViewById(R.id.img_super);

//        img_plorant.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.a0plorant, 50, 50));
//        img_trist.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.a1trist, 50, 50));
//        img_neutre.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.a2neutre, 50, 50));
//        img_content.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.a3content, 50, 50));
//        img_super.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.a4genial, 50, 50));
        Glide.with(this).load(R.drawable.a0plorant).into(img_plorant);
        Glide.with(this).load(R.drawable.a1trist).into(img_trist);
        Glide.with(this).load(R.drawable.a2neutre).into(img_neutre);
        Glide.with(this).load(R.drawable.a3content).into(img_content);
        Glide.with(this).load(R.drawable.a4genial).into(img_super);

        //Bundle
        Titol_pantalla = "";
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            Titol_pantalla = extras.getString("Titol_pantalla");
            Infant = extras.getString("Infant");
            if(Titol_pantalla.equals("Editar: Activitat") || Titol_pantalla.equals("Editar: Emoció puntual")){
                Activitat_id_bundle = extras.getString("Activitat_id");
                Activitat_bundle = extras.getString("Activitat");
                Index_Emocio_bundle = extras.getInt("Index_Emocio");
                DI_bundle = extras.getString("DI");
                DF_bundle = extras.getString("DF");
                HI_bundle = extras.getString("HI");
                HF_bundle = extras.getString("HF");
                btn_Guardar.setText("Guardar  canvis");
            }
        }

        //titol pantalla
        setTitle(Titol_pantalla);

        //Infant seleccionat
        llistaInfants = new ArrayList<>();
        omplirllisaInfants();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, llistaInfants);
        spn_infants.setAdapter(adapter1);
        for(int i=0 ; i<llistaInfants.size() ; i++)
        {
            if (Infant.equals(llistaInfants.get(i))){
                index_infant = i;
            }
        }
        spn_infants.setSelection(index_infant);

        //Activitats
        llistaActivitats = new ArrayList<>();
        if(Titol_pantalla.equals("Emoció puntual") || Titol_pantalla.equals("Editar: Emoció puntual")){
            llistaActivitats.add("Res particular");
            llistaActivitats.add("Mirar TV");
            llistaActivitats.add("Jugar");
            llistaActivitats.add("Llegir");
            llistaActivitats.add("Deures");
            llistaActivitats.add("Estudiar");
        }else{
            llistaActivitats.add("Escola");
            llistaActivitats.add("Repàs");
            llistaActivitats.add("Anglès");
            llistaActivitats.add("Esport");
            llistaActivitats.add("Música");
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, llistaActivitats);
        spn_activitats.setAdapter(adapter2);
        if(Titol_pantalla.equals("Editar: Activitat") || Titol_pantalla.equals("Editar: Emoció puntual")){
            int index_emocio=0;
            for(int i=0 ; i<llistaActivitats.size() ; i++)
            {
                if (Activitat_bundle.equals(llistaActivitats.get(i))){
                    index_emocio = i;
                }
            }
            spn_activitats.setSelection(index_emocio);
        }

        //Hores Inici i Final
        if(Titol_pantalla.equals("Activitat") || Titol_pantalla.equals("Emoció puntual")){
            String data_seleccionada = globals.getData_Seleccionada();
            String data_seleccionada1[] = data_seleccionada.split("-");
            dia_Seleccionat = Integer.parseInt(data_seleccionada1[0]);
            mes_Seleccionat = Integer.parseInt(data_seleccionada1[1])-1;
            any_Seleccionat = Integer.parseInt(data_seleccionada1[2]);
        }else{
            String data_seleccionada1[] = DI_bundle.split("-");
            dia_Seleccionat = Integer.parseInt(data_seleccionada1[0]);
            mes_Seleccionat = Integer.parseInt(data_seleccionada1[1])-1;
            any_Seleccionat = Integer.parseInt(data_seleccionada1[2]);
        }

        Calendar c_avui = Calendar.getInstance();
        dia_max_emocio = c_avui.get(Calendar.DAY_OF_MONTH);
        mes_max_emocio = c_avui.get(Calendar.MONTH)*100;
        any_max_emocio = c_avui.get(Calendar.YEAR)*10000;
        data_max_emocio = dia_max_emocio+mes_max_emocio+any_max_emocio;

        if(Titol_pantalla.equals("Activitat") || Titol_pantalla.equals("Emoció puntual")){
            DI.setText(globals.getData_Seleccionada());
            DF.setText(globals.getData_Seleccionada());
            horaI = c_avui.get(Calendar.HOUR_OF_DAY);
            if(Titol_pantalla.equals("Activitat")){
                horaF = horaI+1;
            }else{
                horaF = horaI;
            }
            minutosI = c_avui.get(Calendar.MINUTE);
            minutosF = minutosI;
        }else{
            DI.setText(DI_bundle);
            DF.setText(DF_bundle);
            String HI_seleccionada[] = HI_bundle.split(":");
            horaI = Integer.parseInt(HI_seleccionada[0]);
            minutosI = Integer.parseInt(HI_seleccionada[1]);
            String HF_seleccionada[] = HF_bundle.split(":");
            horaF = Integer.parseInt(HF_seleccionada[0]);
            minutosF = Integer.parseInt(HF_seleccionada[1]);
        }
        String hora_minutoI = arreglar_time_date(horaI)+":"+arreglar_time_date(minutosI);
        HI.setText(hora_minutoI);
        String hora_minutoF = arreglar_time_date(horaF)+":"+arreglar_time_date(minutosF);
        HF.setText(hora_minutoF);

        HF.setTextColor(Color.parseColor("#000000"));
        DF.setTextColor(Color.parseColor("#000000"));
        HI.setTextColor(Color.parseColor("#000000"));
        DI.setTextColor(Color.parseColor("#000000"));


        //Visible vs Invisible
        if(Titol_pantalla.equals("Activitat")){
            int data_seleccionada = any_Seleccionat*10000+mes_Seleccionat*100+dia_Seleccionat;
            if(data_seleccionada<data_max_emocio){
                txt_emocio.setVisibility(View.VISIBLE);
                txt_informacio.setVisibility(View.VISIBLE);
                c0.setVisibility(View.VISIBLE);
                c1.setVisibility(View.VISIBLE);
                c2.setVisibility(View.VISIBLE);
                c3.setVisibility(View.VISIBLE);
                c4.setVisibility(View.VISIBLE);
            }else{
                txt_emocio.setVisibility(View.INVISIBLE);
                txt_informacio.setVisibility(View.INVISIBLE);
                c0.setVisibility(View.INVISIBLE);
                c1.setVisibility(View.INVISIBLE);
                c2.setVisibility(View.INVISIBLE);
                c3.setVisibility(View.INVISIBLE);
                c4.setVisibility(View.INVISIBLE);
            }
        }else if(Titol_pantalla.equals("Emoció puntual")){
            txt_informacio.setVisibility(View.INVISIBLE);
        }else if(Titol_pantalla.equals("Editar: Emoció puntual")){
            txt_informacio.setVisibility(View.INVISIBLE);
        }else{
            Calendar calendar = Calendar.getInstance();
            long any = calendar.get(Calendar.YEAR);
            long mes = calendar.get(Calendar.MONTH)+1;
            long dia = calendar.get(Calendar.DAY_OF_MONTH);
            int hora = calendar.get(Calendar.HOUR_OF_DAY);
            int minut = calendar.get(Calendar.MINUTE);
            long Comparador_Ara = minut+hora*100+dia*10000+mes*1000000+any*100000000;
            String dia2[] = DF_bundle.split("-");
            long any_activitat = Integer.parseInt(dia2[2]);
            long mes_activitat = Integer.parseInt(dia2[1]);
            long dia_activitat = Integer.parseInt(dia2[0]);
            String HF2[]=HF_bundle.split(":");
            int hora_activitat = Integer.parseInt(HF2[0]);
            int minuts_activitat = Integer.parseInt(HF2[1]);
            long Comparador_activitat = minuts_activitat+hora_activitat*100+dia_activitat*10000+mes_activitat*1000000+any_activitat*100000000;
            if(Comparador_activitat>Comparador_Ara){
                c0.setVisibility(View.INVISIBLE);
                c1.setVisibility(View.INVISIBLE);
                c2.setVisibility(View.INVISIBLE);
                c3.setVisibility(View.INVISIBLE);
                c4.setVisibility(View.INVISIBLE);
                txt_informacio.setVisibility(View.INVISIBLE);
            }
        }

        //Emoció seleccionada si és EDITAR: Si index_emocio_bundle==0, no s'ha seleccionat cap emoció.
        if(Titol_pantalla.equals("Editar: Activitat") || Titol_pantalla.equals("Editar: Emoció puntual")){
            if(Index_Emocio_bundle!=0){
                Index_Emocio = Index_Emocio_bundle-1;
            }
            switch (Index_Emocio){
                case 0:
                    c0.setBackgroundColor(Color.parseColor("#99ccff"));
                    c1.setBackgroundColor(Color.parseColor("#ffffff"));
                    c2.setBackgroundColor(Color.parseColor("#ffffff"));
                    c3.setBackgroundColor(Color.parseColor("#ffffff"));
                    c4.setBackgroundColor(Color.parseColor("#ffffff"));
                    break;
                case 1:
                    c1.setBackgroundColor(Color.parseColor("#99ccff"));
                    c0.setBackgroundColor(Color.parseColor("#ffffff"));
                    c2.setBackgroundColor(Color.parseColor("#ffffff"));
                    c3.setBackgroundColor(Color.parseColor("#ffffff"));
                    c4.setBackgroundColor(Color.parseColor("#ffffff"));
                    break;
                case 2:
                    c2.setBackgroundColor(Color.parseColor("#99ccff"));
                    c1.setBackgroundColor(Color.parseColor("#ffffff"));
                    c0.setBackgroundColor(Color.parseColor("#ffffff"));
                    c3.setBackgroundColor(Color.parseColor("#ffffff"));
                    c4.setBackgroundColor(Color.parseColor("#ffffff"));
                    break;
                case 3:
                    c3.setBackgroundColor(Color.parseColor("#99ccff"));
                    c1.setBackgroundColor(Color.parseColor("#ffffff"));
                    c2.setBackgroundColor(Color.parseColor("#ffffff"));
                    c0.setBackgroundColor(Color.parseColor("#ffffff"));
                    c4.setBackgroundColor(Color.parseColor("#ffffff"));
                    break;
                case 4:
                    c4.setBackgroundColor(Color.parseColor("#99ccff"));
                    c1.setBackgroundColor(Color.parseColor("#ffffff"));
                    c2.setBackgroundColor(Color.parseColor("#ffffff"));
                    c3.setBackgroundColor(Color.parseColor("#ffffff"));
                    c0.setBackgroundColor(Color.parseColor("#ffffff"));
                    break;
            }
        }

        //setOnClickListener
        c0.setOnClickListener(this);
        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);
        DI.setOnClickListener(this);
        DF.setOnClickListener(this);
        HI.setOnClickListener(this);
        HF.setOnClickListener(this);
        btn_Guardar.setOnClickListener(this);
    }


    private void omplirllisaInfants() {
        Cursor cursor = BD2.rawQuery("SELECT "+Contract.Camp_Nom_I+","+Contract.Camp_Cognoms_I+" FROM "+Contract.NOM_TAULA_Infant_Info_Basica,null);
        if(cursor.getCount()>1){
            cursor.moveToFirst();
            llistaInfants.add("Selecciona un infant...");
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


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_activitat_DI:
                DatePickerDialog datePickerDialogI = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String dia_mes_ano = arreglar_time_date(day)+"-"+(arreglar_time_date(month+1))+"-"+String.valueOf(year);
                        if(Titol_pantalla.equals("Activitat") || Titol_pantalla.equals("Editar: Activitat")){
                            DI.setText(dia_mes_ano);
                            DF.setText(dia_mes_ano);
                            any_Seleccionat=year;
                            mes_Seleccionat=month;
                            dia_Seleccionat=day;

                            //Comprovar si l'hora final de l'activitat és inferior a l'hora programada, per poder introduir l'emoció directament
                            if(horaCorrecte){
                                Calendar calendar = Calendar.getInstance();
                                int hora_actual=calendar.get(Calendar.HOUR_OF_DAY);
                                int minutos_actual=calendar.get(Calendar.MINUTE);
                                int hora_min_actual = hora_actual*100+minutos_actual;
                                int horaF_minF=horaF*100+minutosF;
                                int data_seleccionada = any_Seleccionat*10000+mes_Seleccionat*100+dia_Seleccionat;
                                if(data_seleccionada<data_max_emocio){
                                    txt_emocio.setVisibility(View.VISIBLE);
                                    txt_informacio.setVisibility(View.VISIBLE);
                                    c0.setVisibility(View.VISIBLE);
                                    c1.setVisibility(View.VISIBLE);
                                    c2.setVisibility(View.VISIBLE);
                                    c3.setVisibility(View.VISIBLE);
                                    c4.setVisibility(View.VISIBLE);
                                }else if(data_seleccionada==data_max_emocio){
                                    if(horaF_minF<=hora_min_actual){
                                        txt_emocio.setVisibility(View.VISIBLE);
                                        txt_informacio.setVisibility(View.VISIBLE);
                                        c0.setVisibility(View.VISIBLE);
                                        c1.setVisibility(View.VISIBLE);
                                        c2.setVisibility(View.VISIBLE);
                                        c3.setVisibility(View.VISIBLE);
                                        c4.setVisibility(View.VISIBLE);
                                    }else{
                                        txt_emocio.setVisibility(View.INVISIBLE);
                                        txt_informacio.setVisibility(View.INVISIBLE);
                                        c0.setVisibility(View.INVISIBLE);
                                        c1.setVisibility(View.INVISIBLE);
                                        c2.setVisibility(View.INVISIBLE);
                                        c3.setVisibility(View.INVISIBLE);
                                        c4.setVisibility(View.INVISIBLE);
                                        Index_Emocio = -1;
                                    }
                                }else{
                                    txt_emocio.setVisibility(View.INVISIBLE);
                                    txt_informacio.setVisibility(View.INVISIBLE);
                                    c0.setVisibility(View.INVISIBLE);
                                    c1.setVisibility(View.INVISIBLE);
                                    c2.setVisibility(View.INVISIBLE);
                                    c3.setVisibility(View.INVISIBLE);
                                    c4.setVisibility(View.INVISIBLE);
                                    Index_Emocio = -1;
                                }
                            }
                        }else{
                            int data_seleccionada = year*10000+month*100+day;
                            if (data_seleccionada<=data_max_emocio){
                                DI.setText(dia_mes_ano);
                                DF.setText(dia_mes_ano);
                                any_Seleccionat=year;
                                mes_Seleccionat=month;
                                dia_Seleccionat=day;
                            }else{
                                Toast.makeText(getApplicationContext(), "No es pot posar una data futura", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },any_Seleccionat,mes_Seleccionat,dia_Seleccionat);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    datePickerDialogI.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
                }
                datePickerDialogI.show();
                break;
            case R.id.txt_activitat_DF:
                Toast.makeText(this,"De moment el dia inicial i final han de ser el mateix",Toast.LENGTH_LONG).show();
                break;
            case R.id.txt_activitat_HI:
                TimePickerDialog timePickerDialogHI = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        if(Titol_pantalla.equals("Activitat")|| Titol_pantalla.equals("Editar: Activitat")){
                            String hora_minuto = arreglar_time_date(hour)+":"+arreglar_time_date(minute);
                            HI.setText(hora_minuto);
                            if(horaCorrecte){
                                int diferenciaH = horaF - horaI;
                                int diferenciaM = minutosF - minutosI;
                                int horaF_prov = hour + diferenciaH;
                                int minutosF_prov = minute + diferenciaM;
                                if (minutosF_prov>=60){
                                    minutosF_prov = minutosF_prov-60;
                                    horaF_prov = horaF_prov+1;
                                }else if(minutosF_prov<0){
                                    minutosF_prov = 60+minutosF_prov;
                                    horaF_prov = horaF_prov-1;
                                }
                                //Comprovar si HF ha pasat de les 23:59. Si és així, la hora es canvia, com a molt el dia pot ser fins les 23:59.
                                if(horaF_prov>=24){
                                    horaF_prov=23;
                                    minutosF_prov=59;
                                }else if(horaF_prov==23 && minutosF_prov==59){
                                    horaF_prov=23;
                                    minutosF_prov=59;
                                }
                                horaF = horaF_prov;
                                minutosF = minutosF_prov;
                                String hora_minutoF = arreglar_time_date(horaF)+":"+arreglar_time_date(minutosF);
                                HF.setText(hora_minutoF);
                            }else{
                                //Comprovar si ara ja es correcte per canviar de color el text
                                if (hour<horaF){
                                    horaCorrecte = true;
                                    HI.setTextColor(Color.parseColor("#000000"));
                                }else if(hour==horaF && minute<=minutosF){
                                    horaCorrecte = true;
                                    HI.setTextColor(Color.parseColor("#000000"));
                                }
                            }
                            horaI = hour;
                            minutosI = minute;

                            //Comprovar si l'hora final de l'activitat és inferior a l'hora programada, per poder introduir l'emoció directament
                            if(horaCorrecte){
                                Calendar calendar = Calendar.getInstance();
                                int hora_actual=calendar.get(Calendar.HOUR_OF_DAY);
                                int minutos_actual=calendar.get(Calendar.MINUTE);
                                int hora_min_actual = hora_actual*100+minutos_actual;
                                int horaF_minF=horaF*100+minutosF;
                                int data_seleccionada = any_Seleccionat*10000+mes_Seleccionat*100+dia_Seleccionat;
                                if(data_seleccionada<data_max_emocio){
                                    txt_emocio.setVisibility(View.VISIBLE);
                                    txt_informacio.setVisibility(View.VISIBLE);
                                    c0.setVisibility(View.VISIBLE);
                                    c1.setVisibility(View.VISIBLE);
                                    c2.setVisibility(View.VISIBLE);
                                    c3.setVisibility(View.VISIBLE);
                                    c4.setVisibility(View.VISIBLE);
                                }else if(data_seleccionada==data_max_emocio){
                                    if(horaF_minF<=hora_min_actual){
                                        txt_emocio.setVisibility(View.VISIBLE);
                                        txt_informacio.setVisibility(View.VISIBLE);
                                        c0.setVisibility(View.VISIBLE);
                                        c1.setVisibility(View.VISIBLE);
                                        c2.setVisibility(View.VISIBLE);
                                        c3.setVisibility(View.VISIBLE);
                                        c4.setVisibility(View.VISIBLE);
                                    }else{
                                        txt_emocio.setVisibility(View.INVISIBLE);
                                        txt_informacio.setVisibility(View.INVISIBLE);
                                        c0.setVisibility(View.INVISIBLE);
                                        c1.setVisibility(View.INVISIBLE);
                                        c2.setVisibility(View.INVISIBLE);
                                        c3.setVisibility(View.INVISIBLE);
                                        c4.setVisibility(View.INVISIBLE);
                                        Index_Emocio = -1;
                                    }
                                }else{
                                    txt_emocio.setVisibility(View.INVISIBLE);
                                    txt_informacio.setVisibility(View.INVISIBLE);
                                    c0.setVisibility(View.INVISIBLE);
                                    c1.setVisibility(View.INVISIBLE);
                                    c2.setVisibility(View.INVISIBLE);
                                    c3.setVisibility(View.INVISIBLE);
                                    c4.setVisibility(View.INVISIBLE);
                                    Index_Emocio = -1;
                                }
                            }
                        }else{
                            Calendar calendar = Calendar.getInstance();
                            int hora_max=calendar.get(Calendar.HOUR_OF_DAY);
                            int minutos_max=calendar.get(Calendar.MINUTE);
                            int hora_min_max= hora_max*100+minutos_max;
                            int hora_min_seleccionat=hour*100+minute;
                            if(hora_min_seleccionat<=hora_min_max){
                                String hora_minuto = arreglar_time_date(hour)+":"+arreglar_time_date(minute);
                                HI.setText(hora_minuto);
                                if(horaCorrecte){
                                    int diferenciaH = horaF - horaI;
                                    int diferenciaM = minutosF - minutosI;
                                    int horaF_prov = hour + diferenciaH;
                                    int minutosF_prov = minute + diferenciaM;
                                    if (minutosF_prov>=60){
                                        minutosF_prov = minutosF_prov-60;
                                        horaF_prov = horaF_prov+1;
                                    }else if(minutosF_prov<0){
                                        minutosF_prov = 60+minutosF_prov;
                                        horaF_prov = horaF_prov-1;
                                    }
                                    //Comprovar si HF ha passat de les 23:59. Si és així, la hora es canvia, com a molt el dia pot ser fins les 23:59.
                                    //No cal comprovar-ho! Només cal comprovar que la hora final no sigui més gran que la hora actual.
//                                    if(horaF_prov>=24){
//                                        horaF_prov=23;
//                                        minutosF_prov=59;
//                                    }else if(horaF_prov==23 && minutosF_prov==59){
//                                        horaF_prov=23;
//                                        minutosF_prov=59;
//                                    }

                                    //Comprovar si la HF ha passat de la hora actual
                                    int horaF_minF_prov = horaF_prov*100+minutosF_prov;
                                    if(horaF_minF_prov>hora_min_max){
                                        //Si no entra és perque la hora i minuts provisionals estan bé i són els definitius
                                        horaF_prov=hora_max;
                                        minutosF_prov=minutos_max;
                                    }

                                    horaF = horaF_prov;
                                    minutosF = minutosF_prov;
                                    String hora_minutoF = arreglar_time_date(horaF)+":"+arreglar_time_date(minutosF);
                                    HF.setText(hora_minutoF);
                                }else{
                                    //Comprovar si ara ja es correcte per canviar de color el text
                                    if (hour<horaF){
                                        horaCorrecte = true;
                                        HI.setTextColor(Color.parseColor("#000000"));
                                    }else if(hour==horaF && minute<=minutosF){
                                        horaCorrecte = true;
                                        HI.setTextColor(Color.parseColor("#000000"));
                                    }
                                }
                                horaI = hour;
                                minutosI = minute;
                            }else{
                                Toast.makeText(getApplicationContext(), "No es pot posar una hora futura", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                },horaI,minutosI,true);
                timePickerDialogHI.show();
                break;
            case R.id.txt_activitat_HF:
                TimePickerDialog timePickerDialogHF = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        if(Titol_pantalla.equals("Activitat")|| Titol_pantalla.equals("Editar: Activitat")){
                            String hora_minuto = arreglar_time_date(hour)+":"+arreglar_time_date(minute);
                            HF.setText(hora_minuto);
                            horaF = hour;
                            minutosF = minute;
                            if(horaF<horaI){
                                HI.setTextColor(Color.parseColor("#fb1d1d"));
                                horaCorrecte = false;
                            }else if(horaF == horaI){
                                if(minutosF<minutosI){
                                    HI.setTextColor(Color.parseColor("#fb1d1d"));
                                    horaCorrecte = false;
                                }
                            }else{
                                HI.setTextColor(Color.parseColor("#000000"));
                            }

                            //Comprovar si l'hora final de l'activitat és inferior a l'hora programada, per poder introduir l'emoció directament
                            if(horaCorrecte){
                                Calendar calendar = Calendar.getInstance();
                                int hora_actual=calendar.get(Calendar.HOUR_OF_DAY);
                                int minutos_actual=calendar.get(Calendar.MINUTE);
                                int hora_min_actual = hora_actual*100+minutos_actual;
                                int horaF_minF=horaF*100+minutosF;
                                int data_seleccionada = any_Seleccionat*10000+mes_Seleccionat*100+dia_Seleccionat;
                                if(data_seleccionada<=data_max_emocio){
                                    if(horaF_minF<=hora_min_actual){
                                        txt_emocio.setVisibility(View.VISIBLE);
                                        txt_informacio.setVisibility(View.VISIBLE);
                                        c0.setVisibility(View.VISIBLE);
                                        c1.setVisibility(View.VISIBLE);
                                        c2.setVisibility(View.VISIBLE);
                                        c3.setVisibility(View.VISIBLE);
                                        c4.setVisibility(View.VISIBLE);
                                    }else{
                                        txt_emocio.setVisibility(View.INVISIBLE);
                                        txt_informacio.setVisibility(View.INVISIBLE);
                                        c0.setVisibility(View.INVISIBLE);
                                        c1.setVisibility(View.INVISIBLE);
                                        c2.setVisibility(View.INVISIBLE);
                                        c3.setVisibility(View.INVISIBLE);
                                        c4.setVisibility(View.INVISIBLE);
                                        Index_Emocio = -1;
                                    }
                                }
                            }
                        }else{
                            Calendar calendar = Calendar.getInstance();
                            int hora_max=calendar.get(Calendar.HOUR_OF_DAY);
                            int minutos_max=calendar.get(Calendar.MINUTE);
                            int hora_min_max= hora_max*100+minutos_max;
                            int hora_min_seleccionat=hour*100+minute;
                            if(hora_min_seleccionat<=hora_min_max){
                                String hora_minuto = arreglar_time_date(hour)+":"+arreglar_time_date(minute);
                                HF.setText(hora_minuto);
                                horaF = hour;
                                minutosF = minute;
                                if(horaF<horaI){
                                    HI.setTextColor(Color.parseColor("#fb1d1d"));
                                    horaCorrecte = false;
                                }else if(horaF == horaI){
                                    if(minutosF<minutosI){
                                        HI.setTextColor(Color.parseColor("#fb1d1d"));
                                        horaCorrecte = false;
                                    }
                                }else{
                                    HI.setTextColor(Color.parseColor("#000000"));
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "No es pot posar una hora futura", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },horaF,minutosF,true);
                timePickerDialogHF.show();
                break;
            case R.id.card_activitat_0:
                c0.setBackgroundColor(Color.parseColor("#99ccff"));
                c1.setBackgroundColor(Color.parseColor("#ffffff"));
                c2.setBackgroundColor(Color.parseColor("#ffffff"));
                c3.setBackgroundColor(Color.parseColor("#ffffff"));
                c4.setBackgroundColor(Color.parseColor("#ffffff"));
                Index_Emocio = 0;
                break;
            case R.id.card_activitat_1:
                c1.setBackgroundColor(Color.parseColor("#99ccff"));
                c0.setBackgroundColor(Color.parseColor("#ffffff"));
                c2.setBackgroundColor(Color.parseColor("#ffffff"));
                c3.setBackgroundColor(Color.parseColor("#ffffff"));
                c4.setBackgroundColor(Color.parseColor("#ffffff"));
                Index_Emocio = 1;
                break;
            case R.id.card_activitat_2:
                c2.setBackgroundColor(Color.parseColor("#99ccff"));
                c1.setBackgroundColor(Color.parseColor("#ffffff"));
                c0.setBackgroundColor(Color.parseColor("#ffffff"));
                c3.setBackgroundColor(Color.parseColor("#ffffff"));
                c4.setBackgroundColor(Color.parseColor("#ffffff"));
                Index_Emocio = 2;
                break;
            case R.id.card_activitat_3:
                c3.setBackgroundColor(Color.parseColor("#99ccff"));
                c1.setBackgroundColor(Color.parseColor("#ffffff"));
                c2.setBackgroundColor(Color.parseColor("#ffffff"));
                c0.setBackgroundColor(Color.parseColor("#ffffff"));
                c4.setBackgroundColor(Color.parseColor("#ffffff"));
                Index_Emocio = 3;
                break;
            case R.id.card_activitat_4:
                c4.setBackgroundColor(Color.parseColor("#99ccff"));
                c1.setBackgroundColor(Color.parseColor("#ffffff"));
                c2.setBackgroundColor(Color.parseColor("#ffffff"));
                c3.setBackgroundColor(Color.parseColor("#ffffff"));
                c0.setBackgroundColor(Color.parseColor("#ffffff"));
                Index_Emocio = 4;
                break;
            case R.id.btn_activitat_Guardar:
                if(horaCorrecte){
                    if(Titol_pantalla.equals("Activitat") || Titol_pantalla.equals("Editar: Activitat")){
                        guardar();
                    }else{
                        if(Index_Emocio!=-1){
                            guardar();
                        }else {
                            Toast.makeText(this,"Selecciona una emoció!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(this,"Introdueix les hores correctament",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private void guardar() {
        String nomI_usuariI[] = spn_infants.getSelectedItem().toString().split(" ");
        String nomI = nomI_usuariI[0];
        String cognomsI = nomI_usuariI[1];
        if (nomI_usuariI.length == 3){
            cognomsI = cognomsI+" "+nomI_usuariI[2];
        }

        String nom_cognoms[]={nomI,cognomsI};
        Cursor cursor = BD2.rawQuery("SELECT "+Contract.Camp_Infant_ID+" FROM "+Contract.NOM_TAULA_Infant_Info_Basica+" WHERE "+Contract.Camp_Nom_I+ " =? AND "+Contract_S.Camp_Cognoms_I+" =?",nom_cognoms);

        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            ContentValues dades = new ContentValues();

            String id_infant[] = {cursor.getString(0)};

            if(Titol_pantalla.equals("Activitat") || Titol_pantalla.equals("Emoció puntual")){
                Cursor cursorRandom = BD2_S.rawQuery("SELECT "+Contract_S.Camp_Id_Activitat+" FROM "+Contract_S.NOM_TAULA_Activitats_Infants+" WHERE "+Contract_S.Camp_Infant_ID+"=?",id_infant);
                crear_randomID(cursorRandom);
                cursorRandom.close();
            }else{
                RandomID = Activitat_id_bundle;
            }

            String dia_mes_any[] = DI.getText().toString().split("-");
            String dia_mes_any2 = dia_mes_any[2]+dia_mes_any[1]+dia_mes_any[0];
            long dia_mes_any3 = Long.parseLong(dia_mes_any2)*10000;
            String hora_min[] = HI.getText().toString().split(":");
            String hora_min2 = hora_min[0]+hora_min[1];
            int hora_min3 = Integer.parseInt(hora_min2);
            long comparador = dia_mes_any3+hora_min3;

            dades.put(Contract.Camp_Id_Activitat,RandomID);
            dades.put(Contract.Camp_Infant_ID, cursor.getString(0));
            dades.put(Contract.Camp_Activitat, spn_activitats.getSelectedItem().toString());
            dades.put(Contract.Camp_ComparadorI,comparador);
            dades.put(Contract.Camp_DI, DI.getText().toString());
            dades.put(Contract.Camp_DF, DF.getText().toString());
            dades.put(Contract.Camp_HI, HI.getText().toString());
            dades.put(Contract.Camp_HF, HF.getText().toString());

            SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
            String usuari_sessio = preferences.getString("Usuari de la Sessió","");
            String usuari_sessio_vector[] = {usuari_sessio};
            if(txt_emocio.isShown()){
                if(Index_Emocio != -1){
                    dades.put(Contract.Camp_Index_Emocio,Index_Emocio+1);
                    globals.SumarPuntsEmocions(this,usuari_sessio_vector,"SI","SI");
                    globals.ControlRegistres(usuari_sessio_vector);
                }else{
                    dades.put(Contract.Camp_Index_Emocio,0);
                    globals.SumarPuntsEmocions(this,usuari_sessio_vector,"SI","NO");
                }
            }else {
                dades.put(Contract.Camp_Index_Emocio,0);
                globals.SumarPuntsEmocions(this,usuari_sessio_vector,"SI","NO");

                if(Titol_pantalla.equals("Activitat") || Titol_pantalla.equals("Editar: Activitat")){

                    if(Titol_pantalla.equals("Editar: Activitat")){
                        //Eliminar notificació i desactivar l'alarma si es una activitat futura:
                        String id_Activitat[]={RandomID};
                        Cursor cursor2 = BD2.rawQuery("SELECT "+Contract.Camp_id_Notif+","+Contract.Camp_Titol+","+Contract.Camp_Missatge+" FROM "+Contract.NOM_TAULA_Notificacions_Activitats+" WHERE "+Contract.Camp_Id_Activitat+"=?",id_Activitat);
                        if(cursor2.moveToFirst()){
                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            Intent intent2 = new Intent(this, AlertReceiver.class);
                            intent2.putExtra("Titol",cursor2.getString(1));
                            intent2.putExtra("Missatge",cursor2.getString(2));
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, cursor2.getInt(0), intent2, PendingIntent.FLAG_UPDATE_CURRENT);
                            if (alarmManager != null) {
                                alarmManager.cancel(pendingIntent);
                            }
                            //Borrar de les bases de dades:
                            String id[] = {String.valueOf(cursor2.getInt(0))};
                            BD2.delete(Contract.NOM_TAULA_Notificacions_Activitats,Contract.Camp_id_Notif+"=?",id);
                            BD2_S.delete(Contract_S.NOM_TAULA_Notificacions_Activitats,Contract_S.Camp_id_Notif+"=?",id);
                        }
                        cursor2.close();
                    }

                    //Guardar Notificació: És una Activitat que l'hora final encara no ha finalitzat.
                    ContentValues info_activitat = new ContentValues();
                    info_activitat.put(Contract.Camp_Infant_ID,cursor.getString(0));
                    info_activitat.put(Contract.Camp_Id_Activitat,RandomID);

                    //ID Notificació
                    Cursor cursorRandom2 = BD2_S.rawQuery("SELECT "+Contract_S.Camp_id_Notif+" FROM "+Contract_S.NOM_TAULA_Notificacions_Activitats+" WHERE "+Contract_S.Camp_Infant_ID+"=?",id_infant);
                    crear_randomID_notif(cursorRandom2);
                    cursorRandom2.close();
                    info_activitat.put(Contract.Camp_id_Notif,id_noti);

                    String titol = "Introdueix l'emoció!!";
                    String message = "Ha finalitzat l'activitat: "+spn_activitats.getSelectedItem().toString()+" de "+nomI;
                    info_activitat.put(Contract.Camp_Titol,titol);
                    info_activitat.put(Contract.Camp_Missatge,message);

                    //Comparador final
                    String dia_mes_anyF[] = DF.getText().toString().split("-");
                    String dia_mes_anyF2 = dia_mes_anyF[2]+dia_mes_anyF[1]+dia_mes_anyF[0];
                    long dia_mes_anyF3 = Long.parseLong(dia_mes_anyF2)*10000;
                    String hora_minF[] = HF.getText().toString().split(":");
                    String hora_minF2 = hora_minF[0]+hora_minF[1];
                    int hora_minF3 = Integer.parseInt(hora_minF2);
                    long comparadorF = dia_mes_anyF3+hora_minF3;

                    info_activitat.put(Contract.Camp_ComparadorF,comparadorF);
                    info_activitat.put(Contract.Camp_DF,DF.getText().toString());
                    info_activitat.put(Contract.Camp_HF,HF.getText().toString());

                    BD2.insert(Contract.NOM_TAULA_Notificacions_Activitats,null,info_activitat);
                    BD2_S.insert(Contract.NOM_TAULA_Notificacions_Activitats,null,info_activitat);

                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR,Integer.parseInt(dia_mes_anyF[2]));
                    c.set(Calendar.MONTH,Integer.parseInt(dia_mes_anyF[1])-1);
                    c.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dia_mes_anyF[0]));
                    c.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hora_minF[0]));
                    c.set(Calendar.MINUTE,Integer.parseInt(hora_minF[1]));
                    c.set(Calendar.SECOND,0);

                    startAlarm(c,titol,message);
                }
            }

            if(Titol_pantalla.equals("Activitat") || Titol_pantalla.equals("Editar: Activitat")){
                dades.put(Contract.Camp_Activitat_o_Emocio,"Activitat");
            }else{
                dades.put(Contract.Camp_Activitat_o_Emocio,"Emoció");
            }

            if(Titol_pantalla.equals("Activitat") || Titol_pantalla.equals("Emoció puntual")){
                BD2.insert(Contract.NOM_TAULA_Activitats_Infants,null, dades);
                BD2_S.insert(Contract_S.NOM_TAULA_Activitats_Infants,null,dades);
            }else{
                String parametres[]={RandomID};
                BD2.update(Contract.NOM_TAULA_Activitats_Infants,dades,Contract.Camp_Id_Activitat+"=?",parametres);
                String parametres2[]={RandomID, cursor.getString(0)};
                BD2_S.update(Contract_S.NOM_TAULA_Activitats_Infants,dades,Contract_S.Camp_Id_Activitat+"=? AND "+Contract_S.Camp_Infant_ID+"=?",parametres2);
            }

            cursor.close();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else{
            Toast.makeText(this,"Selecciona un infant",Toast.LENGTH_SHORT).show();
        }
    }

    private void startAlarm(Calendar c,String titol, String message){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("Titol",titol);
        intent.putExtra("Missatge",message);
        intent.putExtra("id_notif",id_noti);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id_noti, intent ,PendingIntent.FLAG_UPDATE_CURRENT);

        if (c.before(Calendar.getInstance())){
            c.add(Calendar.DATE, 1);
        }

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
    }

    private static String arreglar_time_date(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void crear_randomID(Cursor cursor) {
        Random random = new Random();
        String random2 = new BigInteger(50, random).toString(32);
        int validar =0;
        if (cursor.moveToFirst()){
            cursor.moveToFirst();
            do{
                if (cursor.getString(0).equals(random2)){
                    validar = 1;
                }
            }while (cursor.moveToNext());
            if (validar==0){
                RandomID = random2;
            }else {
                crear_randomID(cursor);
            }
        } else{
            //No s'ha creat cap infant, és el primer, per tant el random és valid
            RandomID = random2;
        }
    }

    private void crear_randomID_notif(Cursor cursor) {
        Random rand = new Random();
        int random2 = rand.nextInt(1000);
        int validar =0;
        if (cursor.moveToFirst()){
            cursor.moveToFirst();
            do{
                if (cursor.getInt(0)==random2){
                    validar = 1;
                }
            }while (cursor.moveToNext());
            if (validar==0){
                id_noti = random2;
            }else {
                crear_randomID(cursor);
            }
        } else{
            //No s'ha creat cap infant, és el primer, per tant el random és valid
            id_noti = random2;
        }
    }
}

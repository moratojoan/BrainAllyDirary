package com.example.joan.brainallydiary;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class Pantalla_Nova_Medicina_Activity extends AppCompatActivity implements View.OnClickListener {

    private Globals globals;
    private AdminSQLite BD_conn;
    private AdminSQLite_S BD_S_conn;
    private SQLiteDatabase BD2, BD2_S;

    private Spinner spn_infants, spn_medicines;
    private TextView Data, Hora;
    private Button btn_Guardar;

    private String Titol_pantalla, Infant, Infant_Id, nomI;

    private ArrayList<String> llistaInfants, llistaMedicines;
    private int index_infant;

    private int hora, minuts;

    private int dia_Seleccionat,mes_Seleccionat,any_Seleccionat;

    private String RandomID;
    private int id_noti;

    private String Activitat_id_bundle, Activitat_bundle, Data_bundle, Hora_bundle;
    private int Index_Emocio_bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_nova_medicina);

        //Globals
        globals = Globals.getInstance(this);
        //BD_conn = globals.getBD_conn();
        //BD_S_conn = globals.getBD_S_conn();
        //BD2 = BD_conn.getWritableDatabase();
        //BD2_S = BD_S_conn.getWritableDatabase();
        BD2 = globals.getBD_w();
        BD2_S = globals.getBD_S_w();

        //Connectar xml i java
        spn_infants = findViewById(R.id.spn_medicina_infants);
        spn_medicines = findViewById(R.id.spn_medicina_medicines);
        Data = findViewById(R.id.txt_medicina_data);
        Hora = findViewById(R.id.txt_medicina_hora);
        btn_Guardar = findViewById(R.id.btn_medicina_Guardar);

        //Bundle
        Titol_pantalla = "";
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            Titol_pantalla = extras.getString("Titol_pantalla");
            Infant = extras.getString("Infant");
            if(Titol_pantalla.equals("Editar: Medicina")){
                Activitat_id_bundle = extras.getString("Activitat_id");
                Activitat_bundle = extras.getString("Activitat");
                Index_Emocio_bundle = extras.getInt("Index_Emocio");
                Data_bundle = extras.getString("DI");
                //DF_bundle = extras.getString("DF");
                Hora_bundle = extras.getString("HI");
                //HF_bundle = extras.getString("HF");
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

        //Medicines de l'infant
        actualitzarSpnMedicines(Infant);

        spn_infants.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Infant = spn_infants.getSelectedItem().toString();
                actualitzarSpnMedicines(Infant);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(Titol_pantalla.equals("Medicina")){
            Calendar c_avui = Calendar.getInstance();
            String data_seleccionada = globals.getData_Seleccionada();
            String data_seleccionada1[] = data_seleccionada.split("-");
            dia_Seleccionat = Integer.parseInt(data_seleccionada1[0]);
            mes_Seleccionat = Integer.parseInt(data_seleccionada1[1])-1;
            any_Seleccionat = Integer.parseInt(data_seleccionada1[2]);
            Data.setText(data_seleccionada);
            hora = c_avui.get(Calendar.HOUR_OF_DAY);
            minuts = c_avui.get(Calendar.MINUTE);
            String hora_minutoI = arreglar_time_date(hora)+":"+arreglar_time_date(minuts);
            Hora.setText(hora_minutoI);
            Data.setTextColor(Color.parseColor("#000000"));
            Hora.setTextColor(Color.parseColor("#000000"));
        }else{
            String data_seleccionada = Data_bundle;
            String data_seleccionada1[] = data_seleccionada.split("-");
            dia_Seleccionat = Integer.parseInt(data_seleccionada1[0]);
            mes_Seleccionat = Integer.parseInt(data_seleccionada1[1])-1;
            any_Seleccionat = Integer.parseInt(data_seleccionada1[2]);
            Data.setText(data_seleccionada);
            String Hora_Min[] = Hora_bundle.split(":");
            hora = Integer.parseInt(Hora_Min[0]);
            minuts = Integer.parseInt(Hora_Min[1]);
            String hora_minutoI = arreglar_time_date(hora)+":"+arreglar_time_date(minuts);
            Hora.setText(hora_minutoI);
            Data.setTextColor(Color.parseColor("#000000"));
            Hora.setTextColor(Color.parseColor("#000000"));
        }

        Data.setOnClickListener(this);
        Hora.setOnClickListener(this);
        btn_Guardar.setOnClickListener(this);

    }

    private void actualitzarSpnMedicines(String NomI_CognomsI) {
        llistaMedicines = new ArrayList<>();

        String nomI_usuariI[] = NomI_CognomsI.split(" ");
        nomI = nomI_usuariI[0];
        String cognomsI = nomI_usuariI[1];
        if (nomI_usuariI.length == 3){
            cognomsI = cognomsI+" "+nomI_usuariI[2];
        }
        String nom_cognoms[]={nomI,cognomsI};
        Cursor cursor = BD2.rawQuery("SELECT "+Contract.Camp_Infant_ID+" FROM "+Contract.NOM_TAULA_Infant_Info_Basica+" WHERE "+Contract.Camp_Nom_I+ " =? AND "+Contract_S.Camp_Cognoms_I+" =?",nom_cognoms);
        if (cursor.moveToFirst()){
            Infant_Id = cursor.getString(0);
            omplirllisaMedicines(Infant_Id);
        }
        cursor.close();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, llistaMedicines);
        spn_medicines.setAdapter(adapter2);
        if(Titol_pantalla.equals("Editar: Medicina")){
            int index=0;
            for(int i=0 ; i<llistaMedicines.size() ; i++)
            {
                if (Activitat_bundle.equals(llistaMedicines.get(i))){
                    index = i;
                }
            }
            spn_medicines.setSelection(index);
        }
    }

    private void omplirllisaMedicines(String Infant_Id) {
        String parametres[] = {Infant_Id};
        Cursor cursor = BD2.rawQuery("SELECT "+Contract.Camp_Medicina+" FROM "+Contract.NOM_TAULA_Infant_Medicines+" WHERE "+Contract.Camp_Infant_ID+"=?",parametres);
        if(cursor.moveToFirst()){
            do{
                llistaMedicines.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        cursor.close();
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

    private static String arreglar_time_date(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_medicina_data:
                DatePickerDialog datePickerDialogI = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String dia_mes_ano = arreglar_time_date(day)+"-"+(arreglar_time_date(month+1))+"-"+String.valueOf(year);
                        Data.setText(dia_mes_ano);
                        any_Seleccionat=year;
                        mes_Seleccionat=month;
                        dia_Seleccionat=day;
                    }
                },any_Seleccionat,mes_Seleccionat,dia_Seleccionat);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    datePickerDialogI.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
                }
                datePickerDialogI.show();
                break;
            case R.id.txt_medicina_hora:
                TimePickerDialog timePickerDialogHI = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String hora_minuto = arreglar_time_date(hour)+":"+arreglar_time_date(minute);
                        Hora.setText(hora_minuto);
                        hora = hour;
                        minuts = minute;
                    }
                },hora,minuts,true);
                timePickerDialogHI.show();
                break;
            case R.id.btn_medicina_Guardar:
                if(!spn_infants.getSelectedItem().toString().equals("Tots els infants")){
                    if(spn_medicines.getSelectedItem()!=null){
                        guardar();
                    }else{
                        Toast.makeText(this,"L'infant no té medicines registrades",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this,"Selecciona un infant",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void guardar() {
        ContentValues dades = new ContentValues();
        String id_infant[] = {Infant_Id};

        if(Titol_pantalla.equals("Medicina")){
            Cursor cursorRandom = BD2_S.rawQuery("SELECT "+Contract_S.Camp_Id_Activitat+" FROM "+Contract_S.NOM_TAULA_Activitats_Infants+" WHERE "+Contract_S.Camp_Infant_ID+"=?",id_infant);
            crear_randomID(cursorRandom);
            cursorRandom.close();
        }else{
            RandomID = Activitat_id_bundle;
        }

        String dia_mes_any[] = Data.getText().toString().split("-");
        String dia_mes_any2 = dia_mes_any[2]+dia_mes_any[1]+dia_mes_any[0];
        long dia_mes_any3 = Long.parseLong(dia_mes_any2)*10000;
        String hora_min[] = Hora.getText().toString().split(":");
        String hora_min2 = hora_min[0]+hora_min[1];
        int hora_min3 = Integer.parseInt(hora_min2);
        long comparador = dia_mes_any3+hora_min3;

        dades.put(Contract.Camp_Id_Activitat,RandomID);
        dades.put(Contract.Camp_Infant_ID, Infant_Id);
        dades.put(Contract.Camp_Activitat, spn_medicines.getSelectedItem().toString());
        dades.put(Contract.Camp_ComparadorI,comparador);
        dades.put(Contract.Camp_DI, Data.getText().toString());
        dades.put(Contract.Camp_DF, Data.getText().toString());
        dades.put(Contract.Camp_HI, Hora.getText().toString());
        dades.put(Contract.Camp_HF, Hora.getText().toString());
        if(Titol_pantalla.equals("Medicina")){
            dades.put(Contract.Camp_Index_Emocio,0);
        }else {
            dades.put(Contract.Camp_Index_Emocio,Index_Emocio_bundle);
        }

        dades.put(Contract.Camp_Activitat_o_Emocio,"Medicina");
        if(Titol_pantalla.equals("Medicina")){
            BD2.insert(Contract.NOM_TAULA_Activitats_Infants,null, dades);
            BD2_S.insert(Contract_S.NOM_TAULA_Activitats_Infants,null,dades);
        }else{
            String parametres[]={RandomID};
            BD2.update(Contract.NOM_TAULA_Activitats_Infants,dades,Contract.Camp_Id_Activitat+"=?",parametres);
            String parametres2[]={RandomID, Infant_Id};
            BD2_S.update(Contract_S.NOM_TAULA_Activitats_Infants,dades,Contract_S.Camp_Id_Activitat+"=? AND "+Contract_S.Camp_Infant_ID+"=?",parametres2);
        }


        SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
        String usuari_sessio = preferences.getString("Usuari de la Sessió","");
        String usuari_sessio_vector[] = {usuari_sessio};
        globals.SumarPuntsEmocions(this,usuari_sessio_vector,"SI","NO");

        //Guardar Notificació: És una Activitat que l'hora final encara no ha finalitzat.
        if(Titol_pantalla.equals("Editar: Medicina")){
            //Eliminar notificació i desactivar l'alarma si es una activitat futura:
            String id_Activitat[]={RandomID};
            Cursor cursor2 = BD2.rawQuery("SELECT "+Contract.Camp_id_Notif+","+Contract.Camp_Titol+","+Contract.Camp_Missatge+" FROM "+Contract.NOM_TAULA_Notificacions_Activitats+" WHERE "+Contract.Camp_Id_Activitat+"=?",id_Activitat);
            if(cursor2.moveToFirst()){
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent2 = new Intent(this, AlertReceiver.class);
                intent2.putExtra("Titol",cursor2.getString(1));
                intent2.putExtra("Missatge",cursor2.getString(2));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, cursor2.getInt(0), intent2, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                //Borrar de les bases de dades:
                String id[] = {String.valueOf(cursor2.getInt(0))};
                BD2.delete(Contract.NOM_TAULA_Notificacions_Activitats,Contract.Camp_id_Notif+"=?",id);
                BD2_S.delete(Contract_S.NOM_TAULA_Notificacions_Activitats,Contract_S.Camp_id_Notif+"=?",id);
            }
            cursor2.close();
        }
        ContentValues info_activitat = new ContentValues();
        info_activitat.put(Contract.Camp_Infant_ID,Infant_Id);
        info_activitat.put(Contract.Camp_Id_Activitat,RandomID);
        //ID Notificació
        Cursor cursorRandom2 = BD2_S.rawQuery("SELECT "+Contract_S.Camp_id_Notif+" FROM "+Contract_S.NOM_TAULA_Notificacions_Activitats+" WHERE "+Contract_S.Camp_Infant_ID+"=?",id_infant);
        crear_randomID_notif(cursorRandom2);
        cursorRandom2.close();
        info_activitat.put(Contract.Camp_id_Notif,id_noti);
        String titol = "Medicina";
        String message = "Hora de la medicina: "+spn_medicines.getSelectedItem().toString()+" de " + nomI;
        info_activitat.put(Contract.Camp_Titol,titol);
        info_activitat.put(Contract.Camp_Missatge,message);
        //Comparador final
        String dia_mes_anyF[] = Data.getText().toString().split("-");
        String dia_mes_anyF2 = dia_mes_anyF[2]+dia_mes_anyF[1]+dia_mes_anyF[0];
        long dia_mes_anyF3 = Long.parseLong(dia_mes_anyF2)*10000;
        String hora_minF[] = Hora.getText().toString().split(":");
        String hora_minF2 = hora_minF[0]+hora_minF[1];
        int hora_minF3 = Integer.parseInt(hora_minF2);
        long comparadorF = dia_mes_anyF3+hora_minF3;
        info_activitat.put(Contract.Camp_ComparadorF,comparadorF);
        info_activitat.put(Contract.Camp_DF,Data.getText().toString());
        info_activitat.put(Contract.Camp_HF,Hora.getText().toString());
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


        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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

    private void startAlarm(Calendar c,String titol, String message){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("Titol",titol);
        intent.putExtra("Missatge",message);
        intent.putExtra("id_notif",id_noti);
        intent.putExtra("Medicina","Medicina");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id_noti, intent ,PendingIntent.FLAG_UPDATE_CURRENT);

        if (c.before(Calendar.getInstance())){
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }
}

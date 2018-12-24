package com.example.joan.brainallydiary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Calendar;
import java.util.zip.InflaterInputStream;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Globals globals;
    private AdminSQLite BD_conn;
    private AdminSQLite_S BD_S_conn;
    private SQLiteDatabase BD2, BD2_S;

    private String estat_sessio, usuari_sessio, sessio_actulitzada;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //icono en el action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_logo_simple3);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);

        Gestor_Activities.setActivity(this);

        globals = Globals.getInstance(this);
        //BD_conn = globals.getBD_conn();
        //BD_S_conn = globals.getBD_S_conn();
        //BD2 = BD_conn.getWritableDatabase();
        //BD2_S = BD_S_conn.getWritableDatabase();
        BD2 = globals.getBD_w();
        BD2_S = globals.getBD_S_w();


        SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
        estat_sessio = preferences.getString("Estat Sessió", "");
        usuari_sessio = preferences.getString("Usuari de la Sessió","");
        sessio_actulitzada = preferences.getString("Sessió_actulitzada","");
        String usuari_sessio_vector[] = {usuari_sessio};

        switch (estat_sessio) {
            case "Oberta":
                Cursor cursor = BD2_S.rawQuery("SELECT * FROM "+Contract_S.NOM_TAULA_Usuari_Info_Basica+" WHERE "+Contract_S.Camp_Usuari_id+" =?",usuari_sessio_vector);
                if(cursor.moveToFirst()){
                    cursor.close();
                    if(sessio_actulitzada.equals("No")){
                        actualitzar_BD_Local();
                    }
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Pantalla_Calendari_Fragment pantalla_calendari_fragment =  new Pantalla_Calendari_Fragment();

                    //Comprovar si es ve d'una notificacio
                    Intent intent2 = getIntent();
                    Bundle extras = intent2.getExtras();
                    String notificacio = "";
                    String notificacio2 ="";
                    if(extras != null){
                        notificacio = extras.getString("Notificacio","");
                        notificacio2 = extras.getString("Notificacio2","");
                    }
                    if(notificacio.equals("Notificacio")){
                        Bundle bundle = new Bundle();
                        bundle.putString("Notificacio", "Notificacio");
                        pantalla_calendari_fragment.setArguments(bundle);
                    }

                    fragmentManager.beginTransaction().replace(R.id.contenedor, pantalla_calendari_fragment).commit();
                    getSupportActionBar().setTitle(R.string.pantalla_calendari);

                    //Trofeus: Comprovar si es el primer cop que s'obre l'app al dia
                    globals.EntrarPrimerCopDia(usuari_sessio_vector,this);
                    Cursor cursor2 = BD2_S.rawQuery("SELECT * FROM "+Contract_S.NOM_TAULA_Infants_Pendents_Compartir+" WHERE "+Contract_S.Camp_Usuari_Receptor_id+" =?",usuari_sessio_vector);
                    if(cursor2.moveToFirst()){
                        alertdialog_compartir(cursor2);

                        String Usuari_principal_id = cursor2.getString(1);
                        String Nom_I = cursor2.getString(3);
                        NotificationHelper notificationHelper = new NotificationHelper(this);
                        String titol = "L'usuari "+Usuari_principal_id+" vol compartir l'infant "+Nom_I+":";
                        String message = "Vols acceptar l'infant?";

                        Intent intent3 = new Intent(this,MainActivity.class);
                        //intent3.putExtra("Notificacio2","Notificacio2");
                        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent3,PendingIntent.FLAG_UPDATE_CURRENT);

                        NotificationCompat.Builder nb = notificationHelper.getChannel1Notification(titol, message, pendingIntent);
                        notificationHelper.getManager().notify(1,nb.build());
                    }
                }else{
                    cursor.close();
                    Intent intent = new Intent(this, Pantalla_Config_Perfil_Usuari_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;
            case "Tancada":
                Intent intent = new Intent(this, Pantalla_Login_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            default:
                Intent intent2 = new Intent(this, Pantalla_Login_Activity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                break;
        }
    }

    private void alertdialog_compartir(final Cursor cursor2) {
        final String Usuari_Receptor_id = cursor2.getString(0);
        String Usuari_principal_id = cursor2.getString(1);
        final String Infant_id = cursor2.getString(2);
        String Nom_I = cursor2.getString(3);
        final String Relacio = cursor2.getString(4);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContentValues values = new ContentValues();
                        values.put(Contract_S.Camp_Usuari_id,Usuari_Receptor_id);
                        values.put(Contract_S.Camp_Infant_ID, Infant_id);
                        values.put(Contract_S.Camp_Relacio, Relacio);

                        BD2.insert(Contract.NOM_TAULA_Relacio_Usuari_Infant,null,values);
                        BD2_S.insert(Contract_S.NOM_TAULA_Relacio_Usuari_Infant,null,values);
                        values.clear();
                        String parametres[]={Usuari_Receptor_id,Infant_id};
                        BD2_S.delete(Contract_S.NOM_TAULA_Infants_Pendents_Compartir,Contract_S.Camp_Usuari_Receptor_id+"=? AND "+Contract_S.Camp_Infant_ID+"=?",parametres);

                        if(cursor2.moveToNext()){
                            alertdialog_compartir(cursor2);
                        }else {
                            actualitzar_BD_Local();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            Pantalla_Calendari_Fragment pantalla_calendari_fragment =  new Pantalla_Calendari_Fragment();
                            fragmentManager.beginTransaction().replace(R.id.contenedor, pantalla_calendari_fragment).commit();
                            getSupportActionBar().setTitle(R.string.pantalla_calendari);
                            cursor2.close();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String parametres[]={Usuari_Receptor_id,Infant_id};
                        BD2_S.delete(Contract_S.NOM_TAULA_Infants_Pendents_Compartir,Contract_S.Camp_Usuari_Receptor_id+"=? AND "+Contract_S.Camp_Infant_ID+"=?",parametres);
                        if(cursor2.moveToNext()){
                            alertdialog_compartir(cursor2);
                        }else {
                            actualitzar_BD_Local();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            Pantalla_Calendari_Fragment pantalla_calendari_fragment =  new Pantalla_Calendari_Fragment();
                            fragmentManager.beginTransaction().replace(R.id.contenedor, pantalla_calendari_fragment).commit();
                            getSupportActionBar().setTitle(R.string.pantalla_calendari);
                            cursor2.close();
                        }
                    }
                })
                .setTitle("L'usuari "+Usuari_principal_id+" vol compartir: "+Nom_I)
                .setMessage("Estàs d'acord?");
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_actualitzar) {
            actualitzar_BD_Local();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Gestor_Activities.quitApp();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.pantalla_perfil_usuari) {
            Intent intent = new Intent(this, Pantalla_Perfil_Usuari_Activity.class);
            startActivity(intent);
        } else if (id == R.id.pantalla_calendari) {
            //fragmentManager.beginTransaction().replace(R.id.contenedor,new Pantalla_Calendari_Fragment()).commit();
            //getSupportActionBar().setTitle(R.string.pantalla_calendari);
        } else if (id == R.id.pantalla_estadistiques) {
            Toast toast = Toast.makeText(this,"La pantalla ESTADÍSTIQUES \n està en construcció!!",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,50);
            toast.show();
            navigationView.getMenu().getItem(1).setChecked(true);
//            Intent intent = new Intent(this, Pantalla_Estadistiques_Activity.class);
//            startActivity(intent);
        } else if (id == R.id.pantalla_trofeus) {
            Intent intent = new Intent(this, Pantalla_Trofeus_Activity.class);
            startActivity(intent);
        } else if (id == R.id.pantalla_perfils_infants) {
            Intent intent = new Intent(this, Pantalla_Perfils_Infants_Activity.class);
            startActivity(intent);
        } else if (id == R.id.pantalla_perfils_companys) {
            Intent intent = new Intent(this, Pantalla_Perfils_Companys_Activity.class);
            startActivity(intent);
        }else if (id == R.id.pantalla_configuració) {
            Toast toast = Toast.makeText(this,"La pantalla CONFIGURACIÓ \n està en construcció!!",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,50);
            toast.show();
            navigationView.getMenu().getItem(1).setChecked(true);
//            Intent intent = new Intent(this, Pantalla_Configuracio_Activity.class);
//            startActivity(intent);
        } else if (id == R.id.tancar_sessio) {
            cancelAlarm();
            SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE); //"Dades Sessió" és el nom de l'arxiu de preferencies.
            SharedPreferences.Editor Obj_editor = preferences.edit();
            Obj_editor.putString("Estat Sessió", "Tancada"); //"Estat Sessió" és la referencia del valor que es guarda.
            Obj_editor.apply();
            Intent intent = new Intent(this, Pantalla_Login_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void cancelAlarm(){
        Cursor cursor = BD2.rawQuery("SELECT "+Contract.Camp_id_Notif+","+Contract.Camp_Titol+","+Contract.Camp_Missatge+" FROM "+Contract.NOM_TAULA_Notificacions_Activitats,null);

        if(cursor.moveToFirst()){
            do{
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, AlertReceiver.class);
                intent.putExtra("Titol",cursor.getString(1));
                intent.putExtra("Missatge",cursor.getString(2));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, cursor.getInt(0), intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.cancel(pendingIntent);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void actualitzar_BD_Local() {
        Cursor cursor = BD2.rawQuery("SELECT "+Contract.Camp_Usuari_id+" FROM "+Contract.NOM_TAULA_Usuari_Info_Login,null);
        if (cursor.moveToFirst()){
            //Hi ha hagut alguna sessio abans que aquesta
            if (!usuari_sessio.equals(cursor.getString(0))){
                //És una sessió diferent a la guardada en local i cal borrar totes les taules i introduir amb la nova informació
                //Borrar dades taules Locals
                //borrarTaulesLocals();

                //Omplir les Taules Locals amb les dades de les Taules del Servidor
                //omplirTaulesLocalsBuides();

                globals.ActulitzarBD(this,usuari_sessio);

            }else{
                //És la mateixa sessió a la guardada en local i només cal actualitzar els camps.
                //(Caldrà quan l'adult tingui algun company que pugui introduir dades en els seus infans).
                //De moment fem com abans, borrar-ho tot i tornar-ho a omplir
                //borrarTaulesLocals();
                //omplirTaulesLocalsBuides();
                globals.ActulitzarBD(this,usuari_sessio);
            }
            cursor.close();
        }else {
            //No hi ha hagut cap sessió abnas que aquesta. He d'omplir les dades en taules buides d'onat una usuari.
            //omplirTaulesLocalsBuides();
            globals.omplirTaulesLocalsBuides(this,usuari_sessio);
        }

    }


//    private void borrarTaulesLocals() {
//        BD2.delete(Contract.NOM_TAULA_Usuari_Info_Login,null,null);
//        BD2.delete(Contract.NOM_TAULA_Usuari_Info_Basica,null,null);
//        BD2.delete(Contract.NOM_TAULA_Relacio_Usuari_Infant,null,null);
//        BD2.delete(Contract.NOM_TAULA_Infant_Info_Basica,null,null);
//        BD2.delete(Contract.NOM_TAULA_Infant_Info_Trastorn,null,null);
//        BD2.delete(Contract.NOM_TAULA_Infant_Medicines,null,null);
//        BD2.delete(Contract.NOM_TAULA_Activitats_Infants,null,null);
//        BD2.delete(Contract.NOM_TAULA_Relacio_Companys_Infants,null,null);
//        BD2.delete(Contract.NOM_TAULA_Trofeus,null,null);
//        BD2.delete(Contract.NOM_TAULA_Notificacions_Activitats,null,null);
//        //Toast.makeText(this,"BD Local actulitzada",Toast.LENGTH_SHORT).show();
//
//
//        SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
//        SharedPreferences.Editor Obj_editor = preferences.edit();
//        Obj_editor.putString("Sessió_actulitzada","Si");
//        Obj_editor.apply();
//    }

//    private void omplirTaulesLocalsBuides() {
////        String usuari_sessio_vector[]={usuari_sessio};
////
////        //Taula Usuari Info Login
////        Cursor cursor1 = BD2_S.rawQuery("SELECT * FROM "+Contract_S.NOM_TAULA_Usuari_Info_Login+" WHERE "+Contract_S.Camp_Usuari_id+" =?",usuari_sessio_vector);
////        if (cursor1.moveToFirst()){
////            ContentValues values = new ContentValues();
////            values.put(Contract.Camp_Usuari_id,cursor1.getString(0));
////            values.put(Contract.Camp_Email,cursor1.getString(1));
////            values.put(Contract.Camp_Password,cursor1.getString(2));
////
////            BD2.insert(Contract.NOM_TAULA_Usuari_Info_Login,null,values);
////            values.clear();
////        }
////
////        //Taula Usuari Info Basica
////        cursor1 = BD2_S.rawQuery("SELECT * FROM "+ Contract_S.NOM_TAULA_Usuari_Info_Basica+" WHERE "+Contract_S.Camp_Usuari_id+" =?",usuari_sessio_vector);
////        if (cursor1.moveToFirst()){
////            cursor1.moveToFirst();
////            ContentValues values = new ContentValues();
////            values.put(Contract.Camp_Nom_U,cursor1.getString(1));
////            values.put(Contract.Camp_Cognoms_U,cursor1.getString(2));
////            values.put(Contract.Camp_Data_Neixament,cursor1.getString(3));
////            values.put(Contract.Camp_Genere,cursor1.getString(4));
////            values.put(Contract.Camp_Pais,cursor1.getString(5));
////            values.put(Contract.Camp_Codi_Postal,cursor1.getString(6));
////
////            BD2.insert(Contract.NOM_TAULA_Usuari_Info_Basica,null,values);
////            values.clear();
////        }
////
////        //Taula Relació Usuari-Infants
////        cursor1 = BD2_S.rawQuery("SELECT * FROM "+ Contract_S.NOM_TAULA_Relacio_Usuari_Infant+" WHERE "+Contract_S.Camp_Usuari_id+" =?",usuari_sessio_vector);
////        if (cursor1.moveToFirst()){
////            ContentValues values = new ContentValues();
////            do{
////                values.put(Contract.Camp_Infant_ID,cursor1.getString(1));
////                values.put(Contract.Camp_Relacio,cursor1.getString(2));
////
////                BD2.insert(Contract.NOM_TAULA_Relacio_Usuari_Infant,null,values);
////                values.clear();
////            }while (cursor1.moveToNext());
////        }
////
////        //Taules Infants Info Basica, Trastorn, Medicines i Activitats
////        Cursor cursor2 = BD2.rawQuery("SELECT "+Contract.Camp_Infant_ID+" FROM "+Contract.NOM_TAULA_Relacio_Usuari_Infant,null);
////        if(cursor2.moveToFirst()){
////            do{
////                String infant_id[]={cursor2.getString(0)};
////                cursor1 = BD2_S.rawQuery("SELECT * FROM "+Contract_S.NOM_TAULA_Infant_Info_Basica+ " WHERE "+ Contract_S.Camp_Infant_ID+ " =?",infant_id);
////                if (cursor1.moveToFirst()){
////                    ContentValues values = new ContentValues();
////                    do{
////                        values.put(Contract.Camp_Infant_ID,cursor1.getString(0));
////                        values.put(Contract.Camp_Nom_I,cursor1.getString(1));
////                        values.put(Contract.Camp_Cognoms_I,cursor1.getString(2));
////                        values.put(Contract.Camp_Data_Neixament,cursor1.getString(3));
////                        values.put(Contract.Camp_Genere,cursor1.getString(4));
////                        values.put(Contract.Camp_Pais,cursor1.getString(5));
////                        values.put(Contract.Camp_Codi_Postal,cursor1.getString(6));
////
////                        BD2.insert(Contract.NOM_TAULA_Infant_Info_Basica,null,values);
////                        values.clear();
////                    }while (cursor1.moveToNext());
////                }
////                cursor1 = BD2_S.rawQuery("SELECT * FROM "+Contract_S.NOM_TAULA_Infant_Info_Trastorn+ " WHERE "+ Contract_S.Camp_Infant_ID+ " =?",infant_id);
////                if (cursor1.moveToFirst()){
////                    ContentValues values = new ContentValues();
////                    do{
////                        values.put(Contract.Camp_Infant_ID,cursor1.getString(0));
////                        values.put(Contract.Camp_Trastorn_Principal,cursor1.getString(1));
////
////                        BD2.insert(Contract.NOM_TAULA_Infant_Info_Trastorn,null,values);
////                        values.clear();
////                    }while (cursor1.moveToNext());
////                }
//    cursor1 = BD2_S.rawQuery("SELECT * FROM "+Contract_S.NOM_TAULA_Infant_Medicines+ " WHERE "+ Contract_S.Camp_Infant_ID+ " =?",infant_id);
//                if (cursor1.moveToFirst()){
//        ContentValues values = new ContentValues();
//        do{
//            values.put(Contract.Camp_Infant_ID,cursor1.getString(0));
//            values.put(Contract.Camp_Medicina,cursor1.getString(1));
//
//            BD2.insert(Contract.NOM_TAULA_Infant_Medicines,null,values);
//            values.clear();
//        }while (cursor1.moveToNext());
//    }
////                cursor1 = BD2_S.rawQuery("SELECT * FROM "+Contract_S.NOM_TAULA_Activitats_Infants+ " WHERE "+ Contract_S.Camp_Infant_ID+ " =?",infant_id);
////                if (cursor1.moveToFirst()){
////                    ContentValues values = new ContentValues();
////                    do{
////                        values.put(Contract.Camp_Id_Activitat,cursor1.getString(0));
////                        values.put(Contract.Camp_Infant_ID,cursor1.getString(1));
////                        values.put(Contract.Camp_Activitat,cursor1.getString(2));
////                        values.put(Contract.Camp_ComparadorI,cursor1.getLong(3));
////                        values.put(Contract.Camp_DI,cursor1.getString(4));
////                        values.put(Contract.Camp_DF,cursor1.getString(5));
////                        values.put(Contract.Camp_HI,cursor1.getString(6));
////                        values.put(Contract.Camp_HF,cursor1.getString(7));
////                        values.put(Contract.Camp_Index_Emocio,cursor1.getInt(8));
////                        values.put(Contract.Camp_Activitat_o_Emocio,cursor1.getString(9));
////
////                        BD2.insert(Contract.NOM_TAULA_Activitats_Infants,null,values);
////                        values.clear();
////                    }while (cursor1.moveToNext());
////                }
////
////                //Taula Notificacions Activitats
////                cursor1 = BD2_S.rawQuery("SELECT * FROM "+Contract_S.NOM_TAULA_Notificacions_Activitats+" WHERE "+Contract_S.Camp_Infant_ID+" =?",infant_id);
////                if (cursor1.moveToFirst()){
////                    ContentValues values = new ContentValues();
////                    do{
////                        values.put(Contract.Camp_Infant_ID,cursor1.getString(0));
////                        values.put(Contract.Camp_id_Notif,cursor1.getInt(1));
////                        values.put(Contract.Camp_Titol,cursor1.getString(2));
////                        values.put(Contract.Camp_Missatge,cursor1.getString(3));
////                        values.put(Contract.Camp_ComparadorF,cursor1.getLong(4));
////                        values.put(Contract.Camp_DF,cursor1.getString(5));
////                        values.put(Contract.Camp_HF,cursor1.getString(6));
////
////                        BD2.insert(Contract.NOM_TAULA_Notificacions_Activitats,null,values);
////                        values.clear();
////                    }while (cursor1.moveToNext());
////                    globals.Eliminar_Notificacions_Passades_Infant(cursor2.getString(0));
////                }
////                startAlarmes();
////            }while (cursor2.moveToNext());
////
////            //Taula Companys
////            //usuari_sessio
////            cursor1 = BD2.rawQuery("SELECT "+Contract.Camp_Infant_ID+" FROM "+Contract.NOM_TAULA_Relacio_Usuari_Infant,null);
////            if (cursor1.moveToFirst()){
////                do{
////                    String infant_id[]={cursor1.getString(0)};
////                    cursor2 = BD2_S.rawQuery("SELECT "+Contract_S.Camp_Usuari_id+","+Contract_S.Camp_Relacio+" FROM "+Contract_S.NOM_TAULA_Relacio_Usuari_Infant+" WHERE "+Contract_S.Camp_Infant_ID+"=?",infant_id);
////                    if (cursor2.moveToFirst()){
////                        do{
////                            if(!cursor2.getString(0).equals(usuari_sessio)){
////                                //tinc id_company, id_infant, relacio_CI
////                                String id_company = cursor2.getString(0);
////                                String id_company_vector[]={id_company};
////                                String id_infant = cursor1.getString(0);
////                                String id_infant_vector[] = {id_infant};
////                                String relacio_CI = cursor2.getString(1);
////                                //necessito nom i cognoms de company i de infant
////                                String Nom_C = "";
////                                String Cognoms_C = "";
////                                String Nom_I = "";
////                                String Cognoms_I = "";
////                                Cursor cursor3 = BD2_S.rawQuery("SELECT "+Contract_S.Camp_Nom_U+","+Contract_S.Camp_Cognoms_U+" FROM "+Contract_S.NOM_TAULA_Usuari_Info_Basica+" WHERE "+Contract_S.Camp_Usuari_id+" =?",id_company_vector);
////                                if(cursor3.moveToFirst()){
////                                    Nom_C = cursor3.getString(0);
////                                    Cognoms_C = cursor3.getString(1);
////                                }
////                                cursor3 = BD2.rawQuery("SELECT "+Contract.Camp_Nom_I+","+Contract.Camp_Cognoms_I+" FROM "+Contract.NOM_TAULA_Infant_Info_Basica+" WHERE "+Contract.Camp_Infant_ID+" =?",id_infant_vector);
////                                if(cursor3.moveToFirst()){
////                                    Nom_I = cursor3.getString(0);
////                                    Cognoms_I = cursor3.getString(1);
////                                }
////                                cursor3.close();
////
////                                //Insertar dades a la taula  companys
////                                ContentValues values = new ContentValues();
////                                values.put(Contract.Camp_Company_id,id_company);
////                                values.put(Contract.Camp_Nom_C,Nom_C);
////                                values.put(Contract.Camp_Cognoms_C,Cognoms_C);
////                                values.put(Contract.Camp_Infant_ID,id_infant);
////                                values.put(Contract.Camp_Nom_I,Nom_I);
////                                values.put(Contract.Camp_Cognoms_I,Cognoms_I);
////                                values.put(Contract.Camp_Relacio_CI,relacio_CI);
////
////                                BD2.insert(Contract.NOM_TAULA_Relacio_Companys_Infants,null,values);
////                                values.clear();
////                            }
////                        }while (cursor2.moveToNext());
////                    }
////                }while (cursor1.moveToNext());
////            }
////        }
////        //Taula Trofeus
////        cursor1 = BD2_S.rawQuery("SELECT * FROM "+Contract_S.NOM_TAULA_Trofeus+" WHERE "+Contract_S.Camp_Usuari_id+" =?",usuari_sessio_vector);
////        if (cursor1.moveToFirst()){
////            ContentValues values = new ContentValues();
////            values.put(Contract.Camp_Totals,cursor1.getInt(1));
////            values.put(Contract.Camp_Mensuals,cursor1.getInt(2));
////            values.put(Contract.Camp_Setmanals,cursor1.getInt(3));
////            values.put(Contract.Camp_Diaris,cursor1.getInt(4));
////            values.put(Contract.Camp_Ultim_Dia_Entrat,cursor1.getString(5));
////            values.put(Contract.Camp_Ultim_Dia_20,cursor1.getString(6));
////            values.put(Contract.Camp_Contador_03,cursor1.getInt(7));
////            values.put(Contract.Camp_lvl_perseverancia,cursor1.getInt(8));
////            values.put(Contract.Camp_Contador_perseverancia,cursor1.getInt(9));
////            values.put(Contract.Camp_RatingBar_perseverancia,cursor1.getInt(10));
////            values.put(Contract.Camp_lvl_registres,cursor1.getInt(11));
////            values.put(Contract.Camp_Contador_registres,cursor1.getInt(12));
////            values.put(Contract.Camp_RatingBar_registres,cursor1.getInt(13));
////
////            BD2.insert(Contract.NOM_TAULA_Trofeus,null,values);
////            values.clear();
////        }
////        cursor1.close();
////        cursor2.close();
////    }

//    private void startAlarmes() {
//        Cursor cursor = BD2.rawQuery("SELECT "+Contract.Camp_id_Notif+","+Contract.Camp_Titol+","+Contract.Camp_Missatge+","+Contract.Camp_DF+","+Contract.Camp_HF+" FROM "+Contract.NOM_TAULA_Notificacions_Activitats,null);
//        if (cursor.moveToFirst()){
//            do{
//                int id_noti = cursor.getInt(0);
//                String titol = cursor.getString(1);
//                String message = cursor.getString(2);
//                String DF = cursor.getString(3);
//                String HF = cursor.getString(4);
//
//                String dia_mes_anyF[] = DF.split("-");
//                String hora_minF[] = HF.split(":");
//
//                Calendar c = Calendar.getInstance();
//                c.set(Calendar.YEAR,Integer.parseInt(dia_mes_anyF[2]));
//                c.set(Calendar.MONTH,Integer.parseInt(dia_mes_anyF[1])-1);
//                c.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dia_mes_anyF[0]));
//                c.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hora_minF[0]));
//                c.set(Calendar.MINUTE,Integer.parseInt(hora_minF[1]));
//                c.set(Calendar.SECOND,0);
//
//                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                Intent intent = new Intent(this, AlertReceiver.class);
//                intent.putExtra("Titol",titol);
//                intent.putExtra("Missatge",message);
//                intent.putExtra("id_notif",id_noti);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id_noti, intent ,PendingIntent.FLAG_UPDATE_CURRENT);
//
//                if (c.before(Calendar.getInstance())){
//                    c.add(Calendar.DATE, 1);
//                }
//
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//            }while (cursor.moveToNext());
//        }
//        cursor.close();
//    }

}

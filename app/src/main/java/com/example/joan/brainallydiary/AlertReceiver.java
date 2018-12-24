package com.example.joan.brainallydiary;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;


public class AlertReceiver extends BroadcastReceiver {

    private Globals globals;
    private AdminSQLite BD_conn;
    private AdminSQLite_S BD_S_conn;
    private SQLiteDatabase BD2, BD2_S;

    @Override
    public void onReceive(Context context, Intent intent) {

        globals = Globals.getInstance(context);
        //BD_conn = globals.getBD_conn();
        //BD_S_conn = globals.getBD_S_conn();
        //BD2 = BD_conn.getWritableDatabase();
        //BD2_S = BD_S_conn.getWritableDatabase();
        BD2 = globals.getBD_w();
        BD2_S = globals.getBD_S_w();


        NotificationHelper notificationHelper = new NotificationHelper(context);
        String titol = "Titol";
        String message = "Missatge";
        int id_notif = -1;
        String medicina ="";
        Bundle extras = intent.getExtras();
        if(extras != null){
            titol = extras.getString("Titol");
            message = extras.getString("Missatge");
            id_notif = extras.getInt("id_notif");
            medicina = extras.getString("Medicina","");
        }

        Intent intent2 = new Intent(context,SplashScreen.class);
        intent2.putExtra("Notificacio","Notificacio");

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent2,PendingIntent.FLAG_UPDATE_CURRENT);

        if(medicina.equals("Medicina")){
            NotificationCompat.Builder nb = notificationHelper.getChannel2Notification(titol,message,pendingIntent);
            notificationHelper.getManager().notify(2,nb.build());
        }else{
            NotificationCompat.Builder nb = notificationHelper.getChannel0Notifiaction(titol, message,pendingIntent);
            notificationHelper.getManager().notify(0,nb.build());
        }


        //Despres de fer la notificació, s'elimina de la basde de dades:
        if(id_notif != -1){
            String id[] = {String.valueOf(id_notif)};
            BD2.delete(Contract.NOM_TAULA_Notificacions_Activitats,Contract.Camp_id_Notif+"=?",id);
            BD2_S.delete(Contract_S.NOM_TAULA_Notificacions_Activitats,Contract_S.Camp_id_Notif+"=?",id);
        }

    }
}

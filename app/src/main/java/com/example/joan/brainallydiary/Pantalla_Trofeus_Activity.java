package com.example.joan.brainallydiary;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Pantalla_Trofeus_Activity extends AppCompatActivity {

    private Globals globals;
    private AdminSQLite BD_conn;
    private AdminSQLite_S BD_S_conn;
    private SQLiteDatabase BD2, BD2_S;

    private TextView Totals, Mensuals, Setmanals, Diaris, lvl_perseverancia, lvl_regular;
    private RatingBar ratingBar_perseverancia, ratingBar_regular;

    private ArrayList<Classe_Perfil_Usuari_Trofeus> llistaUsuariTrofeus;
    private RecyclerView recyclerViewUsuariTrofeus;
    private int punts_totals;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_trofeus);

        globals = Globals.getInstance(this);
        //BD_conn = globals.getBD_conn();
        //BD_S_conn = globals.getBD_S_conn();
        //BD2 = BD_conn.getWritableDatabase();
        //BD2_S = BD_S_conn.getWritableDatabase();
        BD2 = globals.getBD_w();
        BD2_S = globals.getBD_S_w();

        Totals = findViewById(R.id.txt_trofeus_punts_totals);
        Mensuals = findViewById(R.id.txt_trofeus_punts_mensuals);
        Setmanals = findViewById(R.id.txt_trofeus_punts_setmanals);
        Diaris = findViewById(R.id.txt_trofeus_punts_diaris);
        lvl_perseverancia = findViewById(R.id.txt_trofeus_lvl_perseverancia);
        lvl_regular = findViewById(R.id.txt_trofeus_lvl_registres);
        ratingBar_perseverancia = findViewById(R.id.ratingBar_trofeus_perseverancia);
        ratingBar_regular = findViewById(R.id.ratingBar_trofeus_regular);


        Cursor cursor = BD2.rawQuery("SELECT "+Contract.Camp_Totals+","+Contract.Camp_Mensuals+"," +
                ""+Contract.Camp_Setmanals+","+Contract.Camp_Diaris+","+Contract.Camp_lvl_perseverancia+","+Contract.Camp_RatingBar_perseverancia+"," +
                ""+Contract.Camp_lvl_registres+","+Contract.Camp_RatingBar_registres+" FROM "+Contract.NOM_TAULA_Trofeus,null);
        if (cursor.moveToFirst()){
            punts_totals = cursor.getInt(0);
            Totals.setText(String.valueOf(punts_totals));
            Mensuals.setText(String.valueOf(cursor.getInt(1)));
            Setmanals.setText(String.valueOf(cursor.getInt(2)));
            Diaris.setText(String.valueOf(cursor.getInt(3)));
            lvl_perseverancia.setText(String.valueOf(cursor.getInt(4)));
            Float rating_persistencia = (float) cursor.getInt(5);
            ratingBar_perseverancia.setRating(rating_persistencia);
            lvl_regular.setText(String.valueOf(cursor.getInt(6)));
            Float rating_regular = (float) cursor.getInt(7);
            ratingBar_regular.setRating(rating_regular);

            //Colores ratingBar
            String[] colors1 ={"#00FFFF","#00FF40","#FFFF00","#FF8000","#FE642E","#FF0000","#FF0080","#FF00FF","#7401DF","#0000FF"};
            String[] colors2 ={"#01FFFF","#00FF41","#FFFF01","#FF8001","#FE642F","#FF0001","#FF0081","#FF01FF","#7402DF","#0001FF"};

            int n_color_perseverancia = cursor.getInt(4)%10;
            Drawable drawable1 = ratingBar_perseverancia.getProgressDrawable();
            drawable1.setTint(Color.parseColor(colors1[n_color_perseverancia]));

            int n_color_registres = cursor.getInt(6)%10;
            Drawable drawable2 = ratingBar_regular.getProgressDrawable();
            drawable2.setTint(Color.parseColor(colors2[n_color_registres]));
        }
        cursor.close();

        //RECYCLERVIEW
        SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
        String usuari_sessio = preferences.getString("Usuari de la Sessió","");
        llistaUsuariTrofeus = new ArrayList<Classe_Perfil_Usuari_Trofeus>();
        recyclerViewUsuariTrofeus = findViewById(R.id.recyclerView_Trofeus);
        recyclerViewUsuariTrofeus.setLayoutManager(new LinearLayoutManager(this));
        omplirllisaUsuariTrofeus(usuari_sessio);
        AdaptadorTrofeusPosicions adapter = new AdaptadorTrofeusPosicions(llistaUsuariTrofeus,usuari_sessio);
        recyclerViewUsuariTrofeus.setAdapter(adapter);
    }

    private void omplirllisaUsuariTrofeus(String usuari_sessio) {
        Cursor cursor = BD2_S.rawQuery("SELECT "+Contract_S.Camp_Usuari_id+","+Contract_S.Camp_Totals+" FROM "+Contract_S.NOM_TAULA_Trofeus,null);
        ArrayList<Classe_Perfil_Usuari_Trofeus> llistaUsuariTrofeus2 = new ArrayList<Classe_Perfil_Usuari_Trofeus>();
        if(cursor.moveToFirst()){
            do{
                Classe_Perfil_Usuari_Trofeus usuari_trofeus = new Classe_Perfil_Usuari_Trofeus(cursor.getString(0),cursor.getInt(1));
                llistaUsuariTrofeus2.add(usuari_trofeus);
            }while (cursor.moveToNext());
        }
        cursor.close();
        Collections.sort(llistaUsuariTrofeus2, new Comparator<Classe_Perfil_Usuari_Trofeus>() {
            @Override
            public int compare(Classe_Perfil_Usuari_Trofeus p1, Classe_Perfil_Usuari_Trofeus p2) {
                return Integer.compare(p1.getPunts(), p2.getPunts())*(-1);
            }
        });

        Boolean usuariTop = false;
        int i=0;
        if(llistaUsuariTrofeus2.size()>5){
            for(i=0; i<5; i++){
                Classe_Perfil_Usuari_Trofeus usuari_trofeus=llistaUsuariTrofeus2.get(i);
                usuari_trofeus.setPos(i+1);
                llistaUsuariTrofeus.add(usuari_trofeus);
                if(usuari_trofeus.getUsuari_id().equals(usuari_sessio)){
                    usuariTop = true;
                }
            }
        }else{
            for(i=0; i<llistaUsuariTrofeus2.size(); i++){
                Classe_Perfil_Usuari_Trofeus usuari_trofeus=llistaUsuariTrofeus2.get(i);
                usuari_trofeus.setPos(i+1);
                llistaUsuariTrofeus.add(usuari_trofeus);
                if(usuari_trofeus.getUsuari_id().equals(usuari_sessio)){
                    usuariTop = true;
                }
            }
        }
        if (!usuariTop){
            Classe_Perfil_Usuari_Trofeus usuari_trofeus = new Classe_Perfil_Usuari_Trofeus(usuari_sessio,punts_totals);
            while(!llistaUsuariTrofeus2.get(i).getUsuari_id().equals(usuari_sessio)){
                i++;
            }
            usuari_trofeus.setPos(i+1);
            llistaUsuariTrofeus.add(usuari_trofeus);
        }
    }
}

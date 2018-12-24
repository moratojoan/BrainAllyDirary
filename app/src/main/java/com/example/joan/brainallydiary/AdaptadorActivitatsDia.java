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
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AdaptadorActivitatsDia extends RecyclerView.Adapter<AdaptadorActivitatsDia.ViewHolderActivitats>{

    private ArrayList<Classe_Activitat> llistaActivitats;
    private SQLiteDatabase BD2, BD2_S;
    private Globals globals;
    private String usuari;
    private Context context;

    public AdaptadorActivitatsDia(ArrayList<Classe_Activitat> llistaActivitats, SQLiteDatabase BD, SQLiteDatabase BD_S,Globals globals, String usuari,Context context){
        this.llistaActivitats = llistaActivitats;
        this.BD2 = BD;
        this.BD2_S = BD_S;
        this.globals = globals;
        this.usuari = usuari;
        this.context = context;
    }


    @Override
    public ViewHolderActivitats onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activitat,null,false);
        return new AdaptadorActivitatsDia.ViewHolderActivitats(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderActivitats holder, final int position) {
        holder.Nom_Infant.setText(llistaActivitats.get(position).getNomI());
        holder.Activitat.setText(llistaActivitats.get(position).getActivitat());
        if(llistaActivitats.get(position).getActivitat_o_Emocio().equals("Medicina")){
            String HI_HF = llistaActivitats.get(position).getHI();
            holder.HI_HF.setText(HI_HF);
        }else{
            String HI_HF = llistaActivitats.get(position).getHI() + " - " + llistaActivitats.get(position).getHF();
            holder.HI_HF.setText(HI_HF);
        }
        String dia2[] = llistaActivitats.get(position).getDI().split("-");
        String dia3 = dia2[0]+"/"+dia2[1];
        holder.Dia.setText(dia3);
        if(llistaActivitats.get(position).getActivitat_o_Emocio().equals("Medicina")){
            String [] emocions={"EMOCIÓ","Tick","Creu"};
            int[] img_emocions={R.drawable.interrogant,R.drawable.tick,R.drawable.creu};
            //ArrayAdapter<String> adapter_llista = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, emocions);
            AdaptadorSpnEmocions adapter_llista = new AdaptadorSpnEmocions(context,emocions,img_emocions);
            holder.spn_emocions.setAdapter(adapter_llista);
        }else{
            String [] emocions={"EMOCIÓ","Plorant","Trist","Neutre","Content","Genial"};
            int[] img_emocions={R.drawable.interrogant,R.drawable.a0plorant,R.drawable.a1trist,R.drawable.a2neutre,R.drawable.a3content,R.drawable.a4genial};
            //ArrayAdapter<String> adapter_llista = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, emocions);
            AdaptadorSpnEmocions adapter_llista = new AdaptadorSpnEmocions(context,emocions,img_emocions);
            holder.spn_emocions.setAdapter(adapter_llista);
        }

        holder.spn_emocions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int check = 0;
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                llistaActivitats.get(position).setIndex_Emocio(i);
                ContentValues registro = new ContentValues();
                registro.put(Contract.Camp_Index_Emocio, i);
                String parametres[]={String.valueOf(llistaActivitats.get(position).getId_Activitat())};
                BD2.update(Contract.NOM_TAULA_Activitats_Infants,registro,Contract.Camp_Id_Activitat+"=?" ,parametres);
                String parametres2[]={String.valueOf(llistaActivitats.get(position).getId_Activitat()),llistaActivitats.get(position).getId_Infant()};
                BD2_S.update(Contract_S.NOM_TAULA_Activitats_Infants,registro,Contract_S.Camp_Id_Activitat+"=? AND "+Contract_S.Camp_Infant_ID+"=?" ,parametres2);

                String usuari_sessio_vector[] = {usuari};
                if(check>0){
                    globals.SumarPuntsEmocions(context,usuari_sessio_vector,"NO","SI");
                    globals.ControlRegistres(usuari_sessio_vector);
                }
                check++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        holder.spn_emocions.setSelection(llistaActivitats.get(position).getIndex_Emocio());

        if(llistaActivitats.get(position).getActivitat_o_Emocio().equals("Activitat")){
            holder.cardView.setBackgroundColor(Color.parseColor("#99ccff"));
            //Spinner emocions visible o no visible:
            Calendar calendar = Calendar.getInstance();
            long any = calendar.get(Calendar.YEAR);
            long mes = calendar.get(Calendar.MONTH)+1;
            long dia = calendar.get(Calendar.DAY_OF_MONTH);
            int hora = calendar.get(Calendar.HOUR_OF_DAY);
            int minut = calendar.get(Calendar.MINUTE);
            long Comparador_Ara = minut+hora*100+dia*10000+mes*1000000+any*100000000;
            long any_activitat = Integer.parseInt(dia2[2]);
            long mes_activitat = Integer.parseInt(dia2[1]);
            long dia_activitat = Integer.parseInt(dia2[0]);
            String HF2[]=llistaActivitats.get(position).getHF().split(":");
            int hora_activitat = Integer.parseInt(HF2[0]);
            int minuts_activitat = Integer.parseInt(HF2[1]);
            long Comparador_activitat = minuts_activitat+hora_activitat*100+dia_activitat*10000+mes_activitat*1000000+any_activitat*100000000;
            if(Comparador_activitat>Comparador_Ara){
                //holder.spn_emocions.setVisibility(View.INVISIBLE);
                String [] emocions={"EMOCIÓ"};
                int[] img_emocions={R.drawable.rellotge};
                //ArrayAdapter<String> adapter_llista = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, emocions);
                AdaptadorSpnEmocions adapter_llista = new AdaptadorSpnEmocions(context,emocions,img_emocions);
                holder.spn_emocions.setAdapter(adapter_llista);
            }
        }else if (llistaActivitats.get(position).getActivitat_o_Emocio().equals("Emoció")){
            holder.cardView.setBackgroundColor(Color.parseColor("#99ff99"));
        }else{
            holder.cardView.setBackgroundColor(Color.parseColor("#F781F3"));
            //Spinner emocions visible o no visible:
            Calendar calendar = Calendar.getInstance();
            long any = calendar.get(Calendar.YEAR);
            long mes = calendar.get(Calendar.MONTH)+1;
            long dia = calendar.get(Calendar.DAY_OF_MONTH);
            int hora = calendar.get(Calendar.HOUR_OF_DAY);
            int minut = calendar.get(Calendar.MINUTE);
            long Comparador_Ara = minut+hora*100+dia*10000+mes*1000000+any*100000000;
            long any_activitat = Integer.parseInt(dia2[2]);
            long mes_activitat = Integer.parseInt(dia2[1]);
            long dia_activitat = Integer.parseInt(dia2[0]);
            String HF2[]=llistaActivitats.get(position).getHF().split(":");
            int hora_activitat = Integer.parseInt(HF2[0]);
            int minuts_activitat = Integer.parseInt(HF2[1]);
            long Comparador_activitat = minuts_activitat+hora_activitat*100+dia_activitat*10000+mes_activitat*1000000+any_activitat*100000000;
            if(Comparador_activitat>Comparador_Ara){
                //holder.spn_emocions.setVisibility(View.INVISIBLE);
                String [] emocions={"EMOCIÓ"};
                int[] img_emocions={R.drawable.rellotge};
                //ArrayAdapter<String> adapter_llista = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, emocions);
                AdaptadorSpnEmocions adapter_llista = new AdaptadorSpnEmocions(context,emocions,img_emocions);
                holder.spn_emocions.setAdapter(adapter_llista);
            }
        }

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Eliminar activitat
                                String parametres[]={String.valueOf(llistaActivitats.get(position).getId_Activitat())};
                                BD2.delete(Contract.NOM_TAULA_Activitats_Infants,Contract.Camp_Id_Activitat+"=?",parametres);
                                String parametres2[]={String.valueOf(llistaActivitats.get(position).getId_Activitat()),llistaActivitats.get(position).getId_Infant()};
                                BD2_S.delete(Contract_S.NOM_TAULA_Activitats_Infants,Contract.Camp_Id_Activitat+"=? AND "+Contract_S.Camp_Infant_ID+"=?",parametres2);
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);

                                //Eliminar notificació i d'esactivar l'alarma si es una activitat futura:
                                //Desactivar Alarma
                                String id_Activitat[]={llistaActivitats.get(position).getId_Activitat()};
                                Cursor cursor = BD2.rawQuery("SELECT "+Contract.Camp_id_Notif+","+Contract.Camp_Titol+","+Contract.Camp_Missatge+" FROM "+Contract.NOM_TAULA_Notificacions_Activitats+" WHERE "+Contract.Camp_Id_Activitat+"=?",id_Activitat);

                                if(cursor.moveToFirst()){
                                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                    Intent intent2 = new Intent(context, AlertReceiver.class);
                                    intent2.putExtra("Titol",cursor.getString(1));
                                    intent2.putExtra("Missatge",cursor.getString(2));
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, cursor.getInt(0), intent2, PendingIntent.FLAG_UPDATE_CURRENT);
                                    alarmManager.cancel(pendingIntent);
                                    //Borrar de les bases de dades:
                                    String id[] = {String.valueOf(cursor.getInt(0))};
                                    BD2.delete(Contract.NOM_TAULA_Notificacions_Activitats,Contract.Camp_id_Notif+"=?",id);
                                    BD2_S.delete(Contract_S.NOM_TAULA_Notificacions_Activitats,Contract_S.Camp_id_Notif+"=?",id);
                                }
                                cursor.close();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setTitle("Esborrar:")
                        .setMessage("Estàs segur?");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if(llistaActivitats.get(position).getActivitat_o_Emocio().equals("Medicina")){
                    intent = new Intent(context, Pantalla_Nova_Medicina_Activity.class);
                }else{
                    intent = new Intent(context, Pantalla_Nova_Activitat_Activity.class);
                }

                if(llistaActivitats.get(position).getActivitat_o_Emocio().equals("Activitat")){
                    intent.putExtra("Titol_pantalla","Editar: Activitat");
                }else if(llistaActivitats.get(position).getActivitat_o_Emocio().equals("Emoció")){
                    intent.putExtra("Titol_pantalla","Editar: Emoció puntual");
                }else{
                    intent.putExtra("Titol_pantalla","Editar: Medicina");
                }
                String Nom_Cognoms = llistaActivitats.get(position).getNomI() + " " + llistaActivitats.get(position).getCognomsI();
                intent.putExtra("Infant",Nom_Cognoms);
                intent.putExtra("Activitat_id",llistaActivitats.get(position).getId_Activitat());
                intent.putExtra("Activitat",llistaActivitats.get(position).getActivitat());
                intent.putExtra("Index_Emocio",llistaActivitats.get(position).getIndex_Emocio());
                intent.putExtra("DI",llistaActivitats.get(position).getDI());
                intent.putExtra("DF",llistaActivitats.get(position).getDF());
                intent.putExtra("HI",llistaActivitats.get(position).getHI());
                intent.putExtra("HF",llistaActivitats.get(position).getHF());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return llistaActivitats.size();
    }

    public class ViewHolderActivitats extends RecyclerView.ViewHolder{

        private TextView Nom_Infant, Activitat, HI_HF, Dia;
        private ImageView img_edit, img_delete;
        private Spinner spn_emocions;
        private CardView cardView;

        private Context context;

        public ViewHolderActivitats(View Itemview) {
            super(Itemview);

            context = itemView.getContext();

            Nom_Infant = itemView.findViewById(R.id.txt_item_act_infant);
            Activitat = itemView.findViewById(R.id.txt_item_act_activitat);
            HI_HF = itemView.findViewById(R.id.txt_item_act_hores);
            Dia = itemView.findViewById(R.id.txt_item_act_dia);
            img_edit = itemView.findViewById(R.id.img_item_act_edit);
            img_delete = itemView.findViewById(R.id.img_item_act_delete);
            spn_emocions = itemView.findViewById(R.id.spn_item_act_emocions);
            cardView = itemView.findViewById(R.id.cardview_activitat);

        }
    }
}

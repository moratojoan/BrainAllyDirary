package com.example.joan.brainallydiary;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorTrofeusPosicions extends RecyclerView.Adapter<AdaptadorTrofeusPosicions.ViewHolderTrofeusPosicions> {

    private ArrayList<Classe_Perfil_Usuari_Trofeus> llistaUsuariTrofeus;
    private String usuari_sessio;

    public AdaptadorTrofeusPosicions(ArrayList<Classe_Perfil_Usuari_Trofeus> llistaUsuariTrofeus, String usuari_sessio){
        this.llistaUsuariTrofeus = llistaUsuariTrofeus;
        this.usuari_sessio = usuari_sessio;
    }

    @Override
    public ViewHolderTrofeusPosicions onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_perfil_usuaris_trofeu,null,false);
        return new AdaptadorTrofeusPosicions.ViewHolderTrofeusPosicions(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderTrofeusPosicions holder, int position) {
        String pos = String.valueOf(llistaUsuariTrofeus.get(position).getPos())+".";
        holder.Posicio.setText(pos);
        holder.Usuari_id.setText(llistaUsuariTrofeus.get(position).getUsuari_id());
        String punts = String.valueOf(llistaUsuariTrofeus.get(position).getPunts())+" punts";
        holder.Punts.setText(punts);
        switch (position){
            case 0:
                holder.cardView_pos.setBackgroundColor(Color.parseColor("#FFFF00"));
                break;
            case 1:
                holder.cardView_pos.setBackgroundColor(Color.parseColor("#BDBDBD"));
                break;
            case 2:
                holder.cardView_pos.setBackgroundColor(Color.parseColor("#FE9A2E"));
                break;
        }
        if(llistaUsuariTrofeus.get(position).getUsuari_id().equals(usuari_sessio)){
            holder.cardView.setBackgroundColor(Color.parseColor("#5ca3e9"));
        }
    }

    @Override
    public int getItemCount() {
        return llistaUsuariTrofeus.size();
    }

    public class ViewHolderTrofeusPosicions extends RecyclerView.ViewHolder {
        private TextView Posicio, Usuari_id, Punts;
        private CardView cardView_pos, cardView;

        public ViewHolderTrofeusPosicions(View itemView) {
            super(itemView);

            Posicio = itemView.findViewById(R.id.txt_item_trofeu_pos);
            Usuari_id = itemView.findViewById(R.id.txt_item_trofeu_usuari);
            Punts = itemView.findViewById(R.id.txt_item_trofeu_punts);
            cardView_pos = itemView.findViewById(R.id.cardView_item_trofeus_pos);
            cardView = itemView.findViewById(R.id.cardView_item_trofeus);
        }
    }
}

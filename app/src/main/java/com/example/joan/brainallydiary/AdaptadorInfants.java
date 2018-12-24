package com.example.joan.brainallydiary;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdaptadorInfants extends RecyclerView.Adapter<AdaptadorInfants.ViewHolderInfants>{

    private ArrayList<Classe_Perfil_Infant> llistaInfants;

    public AdaptadorInfants(ArrayList<Classe_Perfil_Infant> llistaInfants){
        this.llistaInfants = llistaInfants;
    }

    @Override
    public ViewHolderInfants onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_perfil_infant,null,false);
        return new ViewHolderInfants(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderInfants holder, final int position) {
        holder.Nom_Infant.setText(llistaInfants.get(position).getNom());
        holder.setOnClickListeners();
        holder.img_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Pantalla_Compartir_Infant_Activity.class);
                intent.putExtra("Infant_ID",llistaInfants.get(position).getID());
                intent.putExtra("Nom_I",llistaInfants.get(position).getNom());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return llistaInfants.size();
    }

    public class ViewHolderInfants extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView Nom_Infant;
        private ImageView img_edit, img_compartir, img_delete;

        public ViewHolderInfants(View itemView) {
            super(itemView);

            Nom_Infant = itemView.findViewById(R.id.txt_cardview_nom);
            img_edit = itemView.findViewById(R.id.img_cardview_edit);
            img_compartir = itemView.findViewById(R.id.img_cardview_compartir);
            img_delete = itemView.findViewById(R.id.img_cardview_delete);
        }

        public void setOnClickListeners(){
            img_edit.setOnClickListener(this);
            img_delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.img_cardview_edit:
                    Toast.makeText(view.getContext(),"Encara no es pot Editar: "+Nom_Infant.getText().toString(),Toast.LENGTH_SHORT).show();
                    break;
                case R.id.img_cardview_delete:
                    Toast.makeText(view.getContext(),"Encara no es pot Esborrar: "+Nom_Infant.getText().toString(),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}

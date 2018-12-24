package com.example.joan.brainallydiary;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdaptadorCompanys extends RecyclerView.Adapter<AdaptadorCompanys.ViewHolderCompanys>{

    private ArrayList<Classe_Perfil_Company> llistaCompanys;
    private View.OnClickListener listener;

    public AdaptadorCompanys(ArrayList<Classe_Perfil_Company> llistaCompanys){
        this.llistaCompanys = llistaCompanys;
    }

    @Override
    public AdaptadorCompanys.ViewHolderCompanys onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_perfil_company,null,false);
        return new AdaptadorCompanys.ViewHolderCompanys(view);
    }

    @Override
    public void onBindViewHolder(AdaptadorCompanys.ViewHolderCompanys holder, int position) {
        if(!llistaCompanys.get(position).getNom_C().equals("")){
            holder.Nom_C.setText(llistaCompanys.get(position).getNom_C());
        }else{
            holder.Nom_C.setText(llistaCompanys.get(position).getId_Company());
        }
        String RelacioCI[] = llistaCompanys.get(position).getRelacio_CI();
        String Infants[] = llistaCompanys.get(position).getNom_I();
        StringBuilder RelacioCI_Infants= new StringBuilder();
        RelacioCI_Infants.append("");
        for (int i=0; i<llistaCompanys.get(position).getN_Infants();i++){
            if(RelacioCI_Infants.toString().equals("")){
                RelacioCI_Infants.append(RelacioCI[i]).append(" - ").append(Infants[i]);
            }else{
                RelacioCI_Infants.append("\n").append(RelacioCI[i]).append(" - ").append(Infants[i]);
            }
        }
        holder.RelacioCI_Infant.setText(RelacioCI_Infants.toString());
    }

    @Override
    public int getItemCount() {
        return llistaCompanys.size();
    }

    public class ViewHolderCompanys extends RecyclerView.ViewHolder{
        private TextView Nom_C;
        private TextView RelacioCI_Infant;

        public ViewHolderCompanys(View itemView) {
            super(itemView);

            Nom_C = itemView.findViewById(R.id.txt_item_company_nom);
            RelacioCI_Infant = itemView.findViewById(R.id.txt_item_company_RI);
        }
    }
}

package com.example.joan.brainallydiary;


import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;


public class Pantalla_Calendari_Fragment extends Fragment {

    private Globals globals;
    private AdminSQLite BD_conn;
    private AdminSQLite_S BD_S_conn;
    private SQLiteDatabase BD2, BD2_S;

    private AppBarLayout appBar; //On s'inclouran les pestanyes
    private TabLayout tabs;
    private ViewPager viewPager; //Per mostrar una vista de cada framgent.

    private String DataSeleccionada;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pantalla_calendari, container, false);

        globals = Globals.getInstance(getActivity());
        //BD_conn = globals.getBD_conn();
        //BD_S_conn = globals.getBD_S_conn();
        //BD2 = BD_conn.getWritableDatabase();
        //BD2_S = BD_S_conn.getWritableDatabase();
        BD2 = globals.getBD_w();
        BD2_S = globals.getBD_S_w();

        View contenedor = (View) container.getParent();
        appBar = contenedor.findViewById(R.id.appbar);
        tabs = new TabLayout(getActivity());
        tabs.setTabTextColors(Color.parseColor("#FFFFFF"),Color.parseColor("#33FF57"));
        tabs.setSelectedTabIndicatorColor(Color.parseColor("#33FF57"));
        appBar.addView(tabs);


        viewPager = view.findViewById(R.id.pager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        //Comprovar si es ve duna notificacio
        Bundle bundle = getArguments();
        String notificacio = "";
        if (bundle != null) {
            notificacio = bundle.getString("Notificacio");
        }
        if(notificacio.equals("Notificacio")){
            Calendar c = Calendar.getInstance();
            int dia= c.get(Calendar.DAY_OF_MONTH);
            int mes = c.get(Calendar.MONTH);
            int any = c.get(Calendar.YEAR);
            seleccionarData(dia,mes,any);
            viewPager.setCurrentItem(1);
        }
        tabs.setupWithViewPager(viewPager);

        return view;
    }

    private void seleccionarData(int dia, int mes, int any) {
        DataSeleccionada = arreglar_time_date(dia)+"-"+arreglar_time_date(mes+1)+"-"+String.valueOf(any);
        globals.setData_Seleccionada(DataSeleccionada);
    }

    private static String arreglar_time_date(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    // Ens permet eliminar les pestanyes insertades en el fragment,
    // sinó les pestanyes s'anirien introduint a sota cada vegada que s'entres en aquest fragemnt.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appBar.removeView(tabs);
    }

    //Gestionar les vistes i els tituls de les pestanyes que es mostraran a través del fragment.
    public class ViewPagerAdapter extends FragmentStatePagerAdapter{
        public ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        String titulos_tabs[]={"Mes","Dia","Setmana"};

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new Pantalla_Calendari_Fragment_Mes();
                case 1:
                    return new Pantalla_Calendari_Fragment_Dia();
                case 2:
                    return new Pantalla_Calendari_Fragment_Setmana();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titulos_tabs[position];
        }
    }
}

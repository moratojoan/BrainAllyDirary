package com.example.joan.brainallydiary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 750; //0,75 segons
    private Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Gestor_Activities.setActivity(this);
        globals = Globals.getInstance(this);

        SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
        String trofues=preferences.getString("trofeus","No");
        if(trofues.equals("No")){
            globals.PrimersTrofeus(this);
        }

        SharedPreferences.Editor Obj_editor = preferences.edit();
        Obj_editor.putString("Sessió_actulitzada","No");
        Obj_editor.apply();



        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);

                Intent intent2 = getIntent();
                Bundle extras = intent2.getExtras();
                if(extras != null){
                    String notificacio = extras.getString("Notificacio","");
                    if(notificacio.equals("Notificacio")){
                        i.putExtra("Notificacio","Notificacio");
                    }
                }
                startActivity(i);

                // close this activity
                //finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences preferences = getSharedPreferences("Dades Sessió", Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_editor = preferences.edit();
        Obj_editor.putString("Sessió_actulitzada","No");
        Obj_editor.apply();
        globals.close();
    }
}
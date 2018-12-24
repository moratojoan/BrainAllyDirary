package com.example.joan.brainallydiary;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class Gestor_Activities {
    private static List<Activity> activities = new ArrayList<Activity>();

    public static void setActivity(Activity act){
        activities.add(act);
    }

    public static void quitApp(){
        for(Activity a : activities){
            a.finish();
        }
    }
}

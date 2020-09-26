package com.cgg.pps.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.cgg.pps.network.OPMSService;
import com.google.gson.Gson;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class OPMSApplication extends MultiDexApplication {

    private OPMSService dbService;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);
    }

    public static OPMSApplication get(Context context) {
        return (OPMSApplication) context.getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


    public OPMSService getDBService() {
        dbService = OPMSService.Factory.create();
        return dbService;
    }


    public SharedPreferences getPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences("OPMS_PREFERENCES", MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public SharedPreferences.Editor getPreferencesEditor() {
        if (editor == null) {
            editor = getPreferences().edit();
        }
        return editor;
    }

    public Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public Context getContext() {
        if (context == null) {
            context = getApplicationContext();
        }
        return context;
    }
}

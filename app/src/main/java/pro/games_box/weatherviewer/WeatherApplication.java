package pro.games_box.weatherviewer;

import com.facebook.stetho.Stetho;

import android.app.Application;
import android.content.Context;

/**
 * Created by TESLA on 13.04.2017.
 */

public class WeatherApplication extends Application {
    private static WeatherApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (BuildConfig.DEBUG) {
            stethoInit();
        }
    }

    public static WeatherApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return getInstance().getApplicationContext();
    }

    private void stethoInit() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

    }
}

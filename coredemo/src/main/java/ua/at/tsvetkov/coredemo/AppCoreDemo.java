package ua.at.tsvetkov.coredemo;

import android.app.Application;
import android.util.Log;

import ua.at.tsvetkov.application.AppConfig;
import ua.at.tsvetkov.application.Apps;
import ua.at.tsvetkov.ui.Converter;

/**
 * Created by lordtao on 30.10.2017.
 */
public class AppCoreDemo extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppConfig.init(this);
        AppConfig.printInfo(this);
        Apps.printInstalledAppsPackageAndClass(this);

    }

}

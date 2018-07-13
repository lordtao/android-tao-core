package ua.at.tsvetkov.coredemo;

import android.app.Application;

import ua.at.tsvetkov.application.AppConfig;
import ua.at.tsvetkov.application.Apps;

/**
 * Created by lordtao on 30.10.2017.
 */
public class AppCoreDemo extends Application {

   @Override
   public void onCreate() {
      super.onCreate();

      AppConfig.init(this);
      AppConfig.printInfo();
      Apps.printInstalledAppsPackageAndClass(this);

   }

}

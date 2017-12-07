package ua.at.tsvetkov.taocoredemo;

import android.app.Application;

import ua.at.tsvetkov.application.AppConfig;
import ua.at.tsvetkov.util.Log;

/**
 * Created by lordtao on 06.12.2017.
 */

public class AppTaoCoreDemo extends Application {

   @Override
   public void onCreate() {
      super.onCreate();

      AppConfig.init(this);
      AppConfig.printInfo(this);

      Log.setStamp(BuildConfig.GIT_SHA);
      Log.enableActivityLifecycleAutoLogger(this);

   }

}

package ua.at.tsvetkov.sample;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import ua.at.tsvetkov.application.AppConfig;
import ua.at.tsvetkov.util.Log;

/**
 * Created by max on 30.01.16.
 */
public class App extends Application {

    public void onCreate() {
        super.onCreate();
        AppConfig.init(this);
        AppConfig.printInfo(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.i(activity, "onActivityCreated");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                //Log.i("onActivityStarted" + activity.getLocalClassName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                //Log.i("onActivityResumed" + activity.getLocalClassName());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                //Log.i("onActivityPaused" + activity.getLocalClassName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                //Log.i("onActivityStopped" + activity.getLocalClassName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                //Log.i("onActivitySaveInstanceState" + activity.getLocalClassName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                //Log.i("onActivityDestroyed" + activity.getLocalClassName());
            }
        });
    }

}

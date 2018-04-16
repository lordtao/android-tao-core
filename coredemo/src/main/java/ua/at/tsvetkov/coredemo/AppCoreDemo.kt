package ua.at.tsvetkov.coredemo

import android.app.Application
import ua.at.tsvetkov.application.AppConfig

/**
 * Created by lordtao on 30.10.2017.
 */
class AppCoreDemo : Application() {

    override fun onCreate() {
        super.onCreate()

        AppConfig.init(this)
        AppConfig.printInfo(this)

    }

}

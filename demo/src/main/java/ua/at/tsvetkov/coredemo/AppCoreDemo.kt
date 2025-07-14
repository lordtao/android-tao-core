package ua.at.tsvetkov.coredemo

import android.app.Application
import ua.at.tsvetkov.application.AppConfig.init
import ua.at.tsvetkov.application.AppConfig.printInfo
import ua.at.tsvetkov.application.Apps.Companion.printInstalledAppsPackageAndClass

/**
 * Created by lordtao on 30.10.2017.
 */
class AppCoreDemo : Application() {
    override fun onCreate() {
        super.onCreate()
        init(this)
        printInfo()
        printInstalledAppsPackageAndClass(this)
    }
}

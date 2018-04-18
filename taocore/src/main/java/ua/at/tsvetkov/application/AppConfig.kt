/**
 * ****************************************************************************
 * Copyright (c) 2010 Alexandr Tsvetkov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 *
 * Contributors:
 * Alexandr Tsvetkov - initial API and implementation
 *
 *
 * Project:
 * TAO Core
 *
 *
 * License agreement:
 *
 *
 * 1. This code is published AS IS. Author is not responsible for any damage that can be
 * caused by any application that uses this code.
 * 2. Author does not give a garantee, that this code is error free.
 * 3. This code can be used in NON-COMMERCIAL applications AS IS without any special
 * permission from author.
 * 4. This code can be modified without any special permission from author IF AND ONLY IF
 * this license agreement will remain unchanged.
 * ****************************************************************************
 */
package ua.at.tsvetkov.application

import android.annotation.TargetApi
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Build
import android.os.StrictMode
import android.preference.PreferenceManager
import android.provider.Settings
import android.widget.Toast
import ua.at.tsvetkov.ui.Screen
import ua.at.tsvetkov.util.Log
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

/**
 * Return the basic parameters of the application. Initialize and restoration of essential parameters for the app. Data saves and loads in
 * to the Shared Preferences with name equals the app packages name. Automatically create the working directory for the application in the
 * standard place. Checks is the app is new or is new version and whether it was previously installed.
 *
 * @author A.Tsvetkov 2010 http://tsvetkov.at.ua mailto:al@ukr.net
 */
object AppConfig {

    const val APP_VERSION_NAME = "APP_VERSION_NAME"
    const val APP_VERSION_CODE = "APP_VERSION_CODE"
    const val SAVE = true
    const val NOT_SAVE = false

    //    private val PREFIX = "| "
    private const val LINE_DOUBLE = "=============================================================================================="
    private const val DEFAULT_SETTINGS_STRING = "|                             Default Shared Preferences Data"
    private const val TRIAL_IS_EXPIRED = "Sorry, the trial version has expired "
    private const val MAX_LENGTH = 93

    private var mPreferences: SharedPreferences? = null
    private lateinit var mEditor: Editor
    private var isInitialized = false


    /**
     * Return the app name without spaces.
     *
     * @return the app name without spaces.
     */
    var appName: String? = null
        private set
    /**
     * Return device Diagonal enum constant
     *
     * @return Diagonal
     */
    var deviceDiagonal: Diagonal? = null
        private set
    /**
     * Return device diagonal in inches
     *
     * @return inches
     */
    var diagonalInInch: Float = 0f
        private set
    /**
     * Whether the app is debuggable or not
     *
     * @return is debuggable
     */
    var isDebuggable = true
        private set
    /**
     * Whether the app is currently being debugged (e.g. over ADB)
     *
     * @return is being debugged
     */
    var isBeingDebugged = true
        private set
    /**
     * Return true if the app is absolutely new
     *
     * @return is the app is absolutely new
     */
    var isFreshInstallation = false
        private set
    /**
     * Return true if new version of the app is started
     *
     * @return is new version of app is started
     */
    var isNewVersion = false
        private set
    /**
     * Return is StrictMode is enabled.
     *
     * @return Return is StrictMode is enabled.
     */
    var isStrictModeEnabled = false
        private set
    /**
     * Return the app signature KeyHash for this application
     *
     * @return the app signature KeyHash
     */
    var applicationSignatureKeyHash: String? = null
        private set
    /**
     * Return the app signature fingerprint for this application
     *
     * @return the app signature fingerprint
     */
    var signatureFingerprint: String? = null
        private set
    /**
     * Name of the app package.
     *
     * @return the package name
     */
    var packageName: String? = null
        private set
    /**
     * The version name of this package, as specified by the <manifest> tag's versionName attribute.
     *
     * @return the app version name
    </manifest> */
    var appVersionName: String? = null
        private set
    /**
     * The version number of this package, as specified by the <manifest> tag's versionCode attribute.
     *
     * @return the app version code
    </manifest> */
    var appVersionCode = 0
        private set
    /**
     * A 64-bit number (as a hex string) that is randomly generated when the user first sets up the device
     * and should remain constant for the lifetime of the user's device.
     * The value may change if a factory reset is performed on the device.
     *
     * @return the android id
     */
    var androidId: String? = null
        private set
    /**
     * Checks whether the device is a telephone
     *
     * @return is the device is a telephone
     */
    var isPhone: Boolean = false
        private set
    /**
     * Checks whether the device is a tablet
     *
     * @return is the device is a tablet
     */
    var isTablet: Boolean = false
        private set
      /**
     * Checking the initializing of this class and print error stack trace otherwise.
     *
     * @return true if initiated
     */
    val isInitializing: Boolean
        get() = if (isInitialized) {
            true
        } else {
            throw IllegalArgumentException("AppConfig is not initiated. Call ua.at.tsvetkov.application.AppConfig.init(application) in your Application code.")
        }

    /**
     * Init configuration.
     *
     * @param application the Application
     * @throws NumberFormatException
     */
    fun init(application: Application) {
        packageName = application.applicationInfo.packageName
        var appData: PackageInfo? = null
        try {
            appData = application.packageManager.getPackageInfo(application.packageName, PackageManager.GET_SIGNATURES)
        } catch (e: NameNotFoundException) {
            Log.e("Package not found", e)
        }

        mPreferences = PreferenceManager.getDefaultSharedPreferences(application)
        mEditor = mPreferences!!.edit()
        appName = application.getString(appData!!.applicationInfo.labelRes)
        isFreshInstallation = mPreferences!!.getInt(APP_VERSION_CODE, -1) < 0
        if (appData.versionCode > mPreferences!!.getInt(APP_VERSION_CODE, 0)) {
            isNewVersion = true
        }

        deviceDiagonal = Screen.getDiagonal(application)
        diagonalInInch = Screen.getDiagonalInInch(application).toFloat()
        isPhone = Screen.isPhone(application)
        isTablet = Screen.isTablet(application)

        applicationSignatureKeyHash = Apps.getApplicationSignatureKeyHash(application, packageName!!)
        signatureFingerprint = Apps.getSignatureFingerprint(application, packageName!!)

        appVersionName = appData.versionName
        appVersionCode = appData.versionCode
        androidId = Settings.Secure.getString(application.contentResolver, Settings.Secure.ANDROID_ID)

        mEditor!!.putString(APP_VERSION_NAME, appVersionName)
        mEditor!!.putInt(APP_VERSION_CODE, appVersionCode)

        save()

        isDebuggable = 0 != application.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        isBeingDebugged = android.os.Debug.isDebuggerConnected()
        if (!isDebuggable) {
            android.util.Log.w("▪ $appName ▪", "➧ Log is prohibited because debug mode is disabled.")
            Log.setDisabled(true)
        }

        isInitialized = true
    }

    /**
     * Print the app data and shared mPreferences in to the LogCat
     *
     * @param context the app context
     */
    fun printInfo(context: Context) {
        if (!isDebuggable) {
            return
        }

        var max = mPreferences!!.all.keys.maxBy { it.length }!!.length
        max = if (max < 31) 31 else max
        val formatPref = "| %-${max}s%s"


        val df = DecimalFormat("##.##")
        df.roundingMode = RoundingMode.DOWN
        val realDiagonal = df.format(diagonalInInch)

        val sb = StringBuilder(" \n$LINE_DOUBLE\n"
                + "| Application name               $appName".padEnd(MAX_LENGTH, ' ').plus("|\n")
                + "| Android  device ID             $androidId".padEnd(MAX_LENGTH, ' ').plus("|\n")
                + "| Application package            $packageName".padEnd(MAX_LENGTH, ' ').plus("|\n")
                + "| Signature Fingerprint SHA-1    $signatureFingerprint".padEnd(MAX_LENGTH, ' ').plus("|\n")
                + "| Signature Key Hash             $applicationSignatureKeyHash".dropLast(1).padEnd(MAX_LENGTH, ' ').plus("|\n")
                + "| Diagonal                       $deviceDiagonal - $realDiagonal\"".padEnd(MAX_LENGTH, ' ').plus("|\n")
                + "| New app version                $isNewVersion".padEnd(MAX_LENGTH, ' ').plus("|\n")
                + "| Fresh installation             $isFreshInstallation".padEnd(MAX_LENGTH, ' ').plus("|\n")
                + "| Strict mode                    $isStrictModeEnabled".padEnd(MAX_LENGTH, ' ').plus("|\n")
                + "$LINE_DOUBLE\n"
                + "$DEFAULT_SETTINGS_STRING".padEnd(MAX_LENGTH, ' ').plus("|\n")
                + "$LINE_DOUBLE\n")


        val prefs = mPreferences!!.all.entries.sortedBy { it.key }
        for ((key, value) in prefs) {
            sb.append(String.format(formatPref, key, value).padEnd(MAX_LENGTH, ' ').plus("|\n"))
        }
        sb.append("$LINE_DOUBLE\n ")
        android.util.Log.i("▪ $appName ▪", sb.toString())
    }

    /**
     * Retrieve a boolean value from the mPreferences.
     *
     * @param key      The name of the preference to modify.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a boolean.
     */
    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return mPreferences!!.getBoolean(key, defValue)
    }

    /**
     * Retrieve a float value from the mPreferences.
     *
     * @param key      The name of the preference to modify.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a float.
     */
    fun getFloat(key: String, defValue: Float): Float {
        return mPreferences!!.getFloat(key, defValue)
    }

    /**
     * Retrieve a float value from the mPreferences, 0 if not found
     *
     * @param key The name of the preference to modify.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a float.
     */
    fun getFloat(key: String): Float {
        return mPreferences!!.getFloat(key, 0f)
    }

    /**
     * Return int value
     *
     * @param key      The name of the preference to modify.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a int.
     */
    fun getInt(key: String, defValue: Int): Int {
        return mPreferences!!.getInt(key, defValue)
    }

    /**
     * Return int value, 0 if not found
     *
     * @param key The name of the preference to modify.
     * @return value
     */
    fun getInt(key: String): Int {
        return mPreferences!!.getInt(key, 0)
    }

    /**
     * Return long value
     *
     * @param key      The name of the preference to modify.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a long.
     */
    fun getLong(key: String, defValue: Long): Long {
        return mPreferences!!.getLong(key, defValue)
    }

    /**
     * Return long value, 0 if not found
     *
     * @param key The name of the preference to modify.
     * @return value
     */
    fun getLong(key: String): Long {
        return mPreferences!!.getLong(key, 0)
    }

    /**
     * Return value or "" if not found
     *
     * @param key The name of the preference to modify.
     * @return value
     */
    fun getString(key: String): String {
        return mPreferences!!.getString(key, "")
    }

    /**
     * Return value
     *
     * @param key      The name of the preference to modify.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws ClassCastException if there is a preference with this name that is not a String.
     */
    fun getString(key: String, defValue: String): String? {
        return mPreferences!!.getString(key, defValue)
    }

    /**
     * Retrieve a set of String values from the mPreferences.
     *
     *
     *
     * Note that you *must not* modify the set instance returned
     * by this call.  The consistency of the stored data is not guaranteed
     * if you do, nor is your ability to modify the instance at all.
     *
     * @param key       The name of the preference to retrieve.
     * @param defValues Values to return if this preference does not exist.
     * @return Return Returns the preference values if they exist, or defValues.
     * Throws ClassCastException if there is a preference with this name
     * that is not a Set.
     * @throws Exception Not supported before version API 11 (HONEYCOMB)
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Throws(Exception::class)
    fun getStringSet(key: String, defValues: Set<String>): Set<String>? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mPreferences!!.getStringSet(key, defValues)
        } else {
            throw Exception("Not supported before version API 11 (HONEYCOMB)")
        }
    }

    /**
     * Set a new for this key
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    fun putBoolean(key: String, value: Boolean) {
        mEditor!!.putBoolean(key, value)
    }

    /**
     * Set a new for this key
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    fun putFloat(key: String, value: Float) {
        mEditor!!.putFloat(key, value)
    }

    /**
     * Set a new for this key
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    fun putInt(key: String, value: Int) {
        mEditor!!.putInt(key, value)
    }

    /**
     * Set a new for this key
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    fun putLong(key: String, value: Long) {
        mEditor!!.putLong(key, value)
    }

    /**
     * Set a new for this key
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    fun putString(key: String, value: String) {
        mEditor!!.putString(key, value)
    }

    /**
     * Set a new for this key
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun putStringSet(key: String, value: Set<String>) {
        mEditor!!.putStringSet(key, value)
    }

    /**
     * Set a new for this key
     *
     * @param key          The name of the preference to modify.
     * @param value        The new value for the preference.
     * @param isNeedToSave Go write the SharedPreferences or postpone.
     */
    fun putBoolean(key: String, value: Boolean, isNeedToSave: Boolean) {
        mEditor!!.putBoolean(key, value)
        if (isNeedToSave) {
            save()
        }
    }

    /**
     * Set a new for this key
     *
     * @param key          The name of the preference to modify.
     * @param value        The new value for the preference.
     * @param isNeedToSave Go write the SharedPreferences or postpone.
     */
    fun putFloat(key: String, value: Float, isNeedToSave: Boolean) {
        mEditor!!.putFloat(key, value)
        if (isNeedToSave) {
            save()
        }
    }

    /**
     * Set a new for this key
     *
     * @param key          The name of the preference to modify.
     * @param value        The new value for the preference.
     * @param isNeedToSave Go write the SharedPreferences or postpone.
     */
    fun putInt(key: String, value: Int, isNeedToSave: Boolean) {
        mEditor!!.putInt(key, value)
        if (isNeedToSave) {
            save()
        }
    }

    /**
     * Set a new for this key
     *
     * @param key          The name of the preference to modify.
     * @param value        The new value for the preference.
     * @param isNeedToSave Go write the SharedPreferences or postpone.
     */
    fun putLong(key: String, value: Long, isNeedToSave: Boolean) {
        mEditor!!.putLong(key, value)
        if (isNeedToSave) {
            save()
        }
    }

    /**
     * Set a new for this key
     *
     * @param key          The name of the preference to modify.
     * @param value        The new value for the preference.
     * @param isNeedToSave Go write the SharedPreferences or postpone.
     */
    fun putString(key: String, value: String, isNeedToSave: Boolean) {
        mEditor!!.putString(key, value)
        if (isNeedToSave) {
            save()
        }
    }

    /**
     * Set a new for this key
     *
     * @param key          The name of the preference to modify.
     * @param value        The new value for the preference.
     * @param isNeedToSave Go write the SharedPreferences or postpone.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun putStringSet(key: String, value: Set<String>, isNeedToSave: Boolean) {
        mEditor!!.putStringSet(key, value)
        if (isNeedToSave) {
            save()
        }
    }

    /**
     * Go write the SharedPreferences.
     */
    fun save() {
        mEditor!!.commit()
    }

    /**
     * Clear the SharedPreferences
     */
    fun clear() {
        mEditor!!.clear()
        mEditor!!.commit()
        Log.i(">>> Shared mPreferences was CLEARED! <<<")
    }

    /**
     * Enable StrictMode in the app. Put call in the first called onCreate() method in Application or Activity.
     *
     * @param aContext the app Context
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun enableStrictMode(aContext: Context) {
        try {
            val appFlags = aContext.applicationInfo.flags
            if (appFlags and ApplicationInfo.FLAG_DEBUGGABLE != 0) {
                StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build())
                StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().detectActivityLeaks().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build())
            }
            isStrictModeEnabled = true
            Log.i("StrictMode is enabled.")
        } catch (throwable: Throwable) {
            isStrictModeEnabled = false
            Log.i("StrictMode is not supported. Skipping...")
        }

    }

    /**
     * Return true if application run in debug mode
     *
     * @return Return true if application run in debug mode
     */
    fun isDebugMode(): Boolean {
        return isDebuggable
    }

    /**
     * Check evaluate date and finish activity if expired.
     *
     * @param activity current activity
     * @param year     evaluate year
     * @param month    evaluate month
     * @param day      evaluate day
     */
    fun finishIfTrialExpired(activity: Activity, year: Int, month: Int, day: Int) {
        val date = Date()
        val today = Date()
        date.year = year - 1900
        date.month = month - 1
        date.date = day
        if (date.before(today)) {
            Toast.makeText(activity, TRIAL_IS_EXPIRED + date.toGMTString(), Toast.LENGTH_LONG).show()
            Log.w(TRIAL_IS_EXPIRED + date.toGMTString())
            activity.finish()
        }
    }

}
